package com.tongbanjie.tarzan.admin.service.impl;

import com.tongbanjie.tarzan.admin.component.AdminDiscovery;
import com.tongbanjie.tarzan.admin.service.ClientManageService;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.registry.Address;
import com.tongbanjie.tarzan.registry.ClientAddress;
import com.tongbanjie.tarzan.registry.zookeeper.ZkConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 〈客户端管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/9
 */
@Service
public class ClientManageServiceImpl implements ClientManageService {

    @Autowired
    private AdminDiscovery adminDiscovery;

    @Override
    public Result<List<ClientAddress>> getAllClients() {
        List<Address> list = adminDiscovery.getDiscovered(ZkConstants.CLIENTS_ROOT);
        List<ClientAddress> clientAddressList = castToClientAddress(list);
        orderByAppName(clientAddressList);
        return Result.buildSucc(clientAddressList);
    }

    private List<ClientAddress> castToClientAddress(List<Address> list){
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<ClientAddress>(0);
        }
        List<ClientAddress> clientAddressList = new ArrayList<ClientAddress>(list.size());
        for(Address address : list){
            if(address instanceof ClientAddress){
                clientAddressList.add((ClientAddress) address);
            }
        }
        return clientAddressList;
    }

    /**
     * 按AppName排序
     * @param clientAddressList
     */
    private void orderByAppName(List<ClientAddress> clientAddressList){
        Collections.sort(clientAddressList, new Comparator<ClientAddress>() {
            @Override
            public int compare(ClientAddress t1, ClientAddress t2) {
                return t1.getAppName().compareTo(t2.getAppName());
            }
        });
    }

}
