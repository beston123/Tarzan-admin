package com.tongbanjie.tarzan.admin.service.impl;

import com.tongbanjie.tarzan.admin.component.AdminDiscovery;
import com.tongbanjie.tarzan.admin.component.AdminServerComponent;
import com.tongbanjie.tarzan.admin.service.ServerManageService;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.common.FailResult;
import com.tongbanjie.tarzan.common.exception.RpcException;
import com.tongbanjie.tarzan.common.util.DistributedIdGenerator;
import com.tongbanjie.tarzan.common.util.Timeout;
import com.tongbanjie.tarzan.registry.Address;
import com.tongbanjie.tarzan.registry.ServerAddress;
import com.tongbanjie.tarzan.registry.zookeeper.ZkConstants;
import com.tongbanjie.tarzan.rpc.exception.RpcTimeoutException;
import com.tongbanjie.tarzan.rpc.protocol.RequestCode;
import com.tongbanjie.tarzan.rpc.protocol.ResponseCode;
import com.tongbanjie.tarzan.rpc.protocol.RpcCommand;
import com.tongbanjie.tarzan.rpc.protocol.RpcCommandBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈Server管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 16/11/17
 */
@Service
public class ServerManageServiceImpl implements ServerManageService{

    /**
     * 健康检查 超时时间
     */
    private static final long HEALTH_CHECK_TIMEOUT = 6000L;

    @Autowired
    private AdminDiscovery adminDiscovery;

    @Autowired
    private AdminServerComponent adminServerComponent;

    @Override
    public Result<List<ServerAddress>> getAllServers() {
        List<Address> list = adminDiscovery.getDiscovered(ZkConstants.SERVERS_ROOT);
        return Result.buildSucc(castToServerAddress(list));
    }

    @Override
    public Result<List<ServerAddress>> getServerIds() {
        List<Address> occupiedList = adminDiscovery.discover(ZkConstants.SERVER_IDS_ROOT);
        List<ServerAddress> list = mergeServerIdList(castToServerAddress(occupiedList));
        return Result.buildSucc(list);
    }

    private List<ServerAddress> mergeServerIdList(List<ServerAddress> occupiedList){
        List<ServerAddress> all = new ArrayList<ServerAddress>();
        for(int i=0; i<=DistributedIdGenerator.getMaxWorkId(); i++){
            boolean isOccupied = false;
            for(ServerAddress occupied : occupiedList){
                if(i == occupied.getServerId()){
                    isOccupied = true;
                    all.add(occupied);
                }
            }
            if(!isOccupied){
                ServerAddress address = new ServerAddress(null);
                address.setServerId(i);
                all.add(address);
            }
        }
        return all;
    }

    private List<ServerAddress> castToServerAddress(List<Address> list){
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<ServerAddress>(0);
        }
        List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>(list.size());
        for(Address address : list){
            if(address instanceof ServerAddress){
                serverAddressList.add((ServerAddress)address);
            }
        }
        return serverAddressList;
    }

    @Override
    public Result<Void> deleteServerId(int serverId){
        Validate.notNull(serverId, "server Id is null");
        Result<Void> canBeDelete = isDeletable(serverId);
        if(!canBeDelete.isSuccess()){
            return canBeDelete;
        }
        if(adminDiscovery.deleteServerId(serverId)){
            return Result.buildSucc(null);
        }else{
            return Result.buildFail(FailResult.SYSTEM);
        }
    }

    @Override
    public Result<String> healthCheck(String address) {
        Result<String> result = null;
        try {
            Timeout timeout = new Timeout(HEALTH_CHECK_TIMEOUT);
            RpcCommand request = RpcCommandBuilder.buildRequest(RequestCode.HEALTH_CHECK, null);
            RpcCommand response = adminServerComponent.invokeSync(address, request, HEALTH_CHECK_TIMEOUT);
            if(response.getCmdCode() == ResponseCode.SUCCESS){
                result = Result.buildSucc("运行正常,响应时间:"+timeout.cost()+"ms");
            }else{
                result = Result.buildSucc("运行异常:"+response.getRemark());
            }
        } catch (RpcTimeoutException e) {
            result = Result.buildFail(FailResult.TIMEOUT, "连接超时");
        } catch (RpcException e){
            result = Result.buildFail(FailResult.RPC, "连接失败");
        }
        return result;
    }

    private Result<Void> isDeletable(int serverId){
        List<ServerAddress> serverList = this.getAllServers().getData();
        if(serverList == null){
            return Result.buildFail(FailResult.SYSTEM);
        }
        for(ServerAddress address : serverList){
            if(serverId == address.getServerId()){
                return Result.buildFail(FailResult.BUSINESS, "服务端Id使用中");
            }
        }
        return Result.buildSucc(null);
    }



}
