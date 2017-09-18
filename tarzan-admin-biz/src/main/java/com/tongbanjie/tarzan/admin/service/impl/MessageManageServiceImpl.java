package com.tongbanjie.tarzan.admin.service.impl;

import com.tongbanjie.tarzan.admin.common.AdminUtils;
import com.tongbanjie.tarzan.admin.component.AdminServerComponent;
import com.tongbanjie.tarzan.admin.service.MessageManageService;
import com.tongbanjie.tarzan.common.FailResult;
import com.tongbanjie.tarzan.common.OrderBy;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.common.exception.RpcException;
import com.tongbanjie.tarzan.common.message.MQType;
import com.tongbanjie.tarzan.rpc.exception.RpcTimeoutException;
import com.tongbanjie.tarzan.rpc.protocol.RequestCode;
import com.tongbanjie.tarzan.rpc.protocol.ResponseCode;
import com.tongbanjie.tarzan.rpc.protocol.RpcCommand;
import com.tongbanjie.tarzan.rpc.protocol.RpcCommandBuilder;
import com.tongbanjie.tarzan.rpc.protocol.header.AdminRequestHeader;
import com.tongbanjie.tarzan.rpc.protocol.header.CustomHeader;
import com.tongbanjie.tarzan.store.model.MessageConsume;
import com.tongbanjie.tarzan.store.model.ToCheckMessage;
import com.tongbanjie.tarzan.store.model.ToSendMessage;
import com.tongbanjie.tarzan.store.query.MessageConsumeQuery;
import com.tongbanjie.tarzan.store.query.ToCheckMessageQuery;
import com.tongbanjie.tarzan.store.query.ToSendMessageQuery;
import com.tongbanjie.tarzan.store.service.MessageConsumeService;
import com.tongbanjie.tarzan.store.service.ToCheckMessageService;
import com.tongbanjie.tarzan.store.service.ToSendMessageService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈消息管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/2
 */
@Service
public class MessageManageServiceImpl implements MessageManageService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(MessageManageServiceImpl.class);

    @Autowired
    private ToCheckMessageService toCheckMessageService;

    @Autowired
    private ToSendMessageService toSendMessageService;

    @Autowired
    private MessageConsumeService messageConsumeService;

    @Autowired
    private AdminServerComponent adminServerComponent;

    @Override
    public Result<List<ToCheckMessage>> listToCheck(ToCheckMessageQuery toCheckMessageQuery) {
        AdminUtils.setDate235959(toCheckMessageQuery.getSourceTimeTo());
        toCheckMessageQuery.setOrderByClause(OrderBy.ORDER_BY_ID_DESC);
        return toCheckMessageService.query(toCheckMessageQuery, toCheckMessageQuery.getPagingParam());
    }

    @Override
    public Result<Integer> countToCheck(ToCheckMessageQuery toCheckMessageQuery) {
        AdminUtils.setDate235959(toCheckMessageQuery.getSourceTimeTo());
        return toCheckMessageService.count(toCheckMessageQuery);
    }

    @Override
    public Result<List<ToSendMessage>> listToSend(ToSendMessageQuery toSendMessageQuery) {
        AdminUtils.setDate235959(toSendMessageQuery.getSourceTimeTo());
        toSendMessageQuery.setOrderByClause(OrderBy.ORDER_BY_ID_DESC);
        return toSendMessageService.query(toSendMessageQuery, toSendMessageQuery.getPagingParam());
    }

    @Override
    public Result<Integer> countToSend(ToSendMessageQuery toSendMessageQuery) {
        AdminUtils.setDate235959(toSendMessageQuery.getSourceTimeTo());
        return toSendMessageService.count(toSendMessageQuery);
    }

    @Override
    public Result<List<MessageConsume>> listMessageConsume(MessageConsumeQuery messageConsumeQuery) {
        messageConsumeQuery.setOrderByClause(OrderBy.ORDER_BY_ID_DESC);
        return messageConsumeService.query(messageConsumeQuery);
    }

    @Override
    public Result<Integer> countMessageConsume(MessageConsumeQuery messageConsumeQuery) {
        return messageConsumeService.count(messageConsumeQuery);
    }

    @Override
    public Result<Integer> deleteConsume(Long id) {
        return messageConsumeService.delete(id);
    }

    @Override
    public Result<Void> checkTransaction(Long tid, MQType mqType) {
        Result<Void> result;
        try {
            Validate.notNull(tid, "tid 不能为空");
            Validate.notNull(mqType, "mqType 不能为空");
            RpcCommand request = buildRequest(RequestCode.ADMIN_CHECK_TRANSACTION_ONCE, buildRequestHeader(tid, mqType));
            RpcCommand response = invokeSync(request);
            if (response.getCmdCode() == ResponseCode.SUCCESS) {
                result = Result.buildSucc(null);
            } else {
                result = Result.buildFail(FailResult.SYSTEM, response.getRemark());
            }
        } catch (RpcTimeoutException e) {
            result = Result.buildFail(FailResult.TIMEOUT, e.getMessage());
        } catch (RpcException e) {
            result = Result.buildFail(FailResult.RPC, e.getMessage());
        } catch (IllegalArgumentException e){
            result = Result.buildFail(FailResult.PARAMETER, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> sendMessage(Long tid, MQType mqType) {
        Result<String> result;
        try {
            Validate.notNull(tid, "tid 不能为空");
            Validate.notNull(mqType, "mqType 不能为空");
            RpcCommand request = buildRequest(RequestCode.ADMIN_SEND_MESSAGE_ONCE, buildRequestHeader(tid, mqType));
            RpcCommand response = invokeSync(request);
            if (response.getCmdCode() == ResponseCode.SUCCESS) {
                result = Result.buildSucc(response.getBody(String.class));
            } else {
                result = Result.buildFail(FailResult.SYSTEM, response.getRemark());
            }
        } catch (RpcTimeoutException e) {
            result = Result.buildFail(FailResult.TIMEOUT, e.getMessage());
        } catch (RpcException e) {
            result = Result.buildFail(FailResult.RPC, e.getMessage());
        } catch (IllegalArgumentException e){
            result = Result.buildFail(FailResult.PARAMETER, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<Void> sendMessageBatch(String[] idArray) {
        Result<Void> result;
        try {
            for(String id : idArray){
                MessageConsume messageConsume = getById(Long.parseLong(id));
                if(messageConsume != null && messageConsume.getTid() != null){
                    Result<String> sendRet = sendMessage(messageConsume.getTid(), MQType.valueOf(messageConsume.getMqType()));
                    if(!sendRet.isSuccess()){
                        LOGGER.warn("消息发送失败，id="+id +", 原因："+sendRet.getErrorDetail());
                    }
                }else if(messageConsume == null){
                    LOGGER.warn("消息消费结果不存在，id="+id);
                }else if(messageConsume.getTid() == null){
                    LOGGER.warn("该消息消费结果没有存储消息内容，id="+id);
                }
            }
            result = Result.buildSucc(null);
        } catch (RpcTimeoutException e) {
            result = Result.buildFail(FailResult.TIMEOUT, e.getMessage());
        } catch (RpcException e) {
            result = Result.buildFail(FailResult.RPC, e.getMessage());
        } catch (IllegalArgumentException e){
            result = Result.buildFail(FailResult.PARAMETER, e.getMessage());
        }
        return result;
    }

    private MessageConsume getById(Long id){
        Result<MessageConsume> dataRet = messageConsumeService.get(id);
        Validate.isTrue(dataRet.isSuccess(), dataRet.getErrorMsg());
        return dataRet.getData();
    }

    private AdminRequestHeader buildRequestHeader(Long tid, MQType mqType){
        AdminRequestHeader requestHeader = new AdminRequestHeader();
        requestHeader.setTransactionId(tid);
        requestHeader.setMqType(mqType);
        return requestHeader;
    }

    private RpcCommand buildRequest(int requestCode, CustomHeader customHeader){
        return RpcCommandBuilder.buildRequest(requestCode, customHeader);
    }

    private RpcCommand invokeSync(RpcCommand request) throws RpcException{
        return adminServerComponent.invokeSync(request, 3000);
    }

}
