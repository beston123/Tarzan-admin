package com.tongbanjie.tarzan.admin.service;

import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.common.message.MQType;
import com.tongbanjie.tarzan.store.model.MessageConsume;
import com.tongbanjie.tarzan.store.model.ToCheckMessage;
import com.tongbanjie.tarzan.store.model.ToSendMessage;
import com.tongbanjie.tarzan.store.query.MessageConsumeQuery;
import com.tongbanjie.tarzan.store.query.ToCheckMessageQuery;
import com.tongbanjie.tarzan.store.query.ToSendMessageQuery;

import java.util.List;

/**
 * 〈消息管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/2
 */
public interface MessageManageService {

    Result<List<ToCheckMessage>> listToCheck(ToCheckMessageQuery toCheckMessageQuery);

    Result<Integer> countToCheck(ToCheckMessageQuery toCheckMessageQuery);

    Result<List<ToSendMessage>> listToSend(ToSendMessageQuery toSendMessageQuery);

    Result<Integer> countToSend(ToSendMessageQuery toSendMessageQuery);

    Result<List<MessageConsume>> listMessageConsume(MessageConsumeQuery messageConsumeQuery);

    Result<Integer> countMessageConsume(MessageConsumeQuery messageConsumeQuery);

    Result<Integer> deleteConsume(Long id);

    Result<Void> checkTransaction(Long tid, MQType mqType);

    Result<String> sendMessage(Long tid, MQType mqType);

    Result<Void> sendMessageBatch(String[] idArray);
}
