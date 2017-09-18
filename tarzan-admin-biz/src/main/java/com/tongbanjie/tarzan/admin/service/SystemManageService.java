package com.tongbanjie.tarzan.admin.service;

import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.store.model.MessageAggregatePlan;
import com.tongbanjie.tarzan.store.query.MessageAggregatePlanQuery;

import java.util.List;

/**
 * 〈系统管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/4
 */
public interface SystemManageService {

    Result<Integer> countAggregatePlan(MessageAggregatePlanQuery aggregatePlanQuery);

    Result<List<MessageAggregatePlan>> listAggregatePlan(MessageAggregatePlanQuery aggregatePlanQuery);

    Result<Integer> cleanAggregatePlan(MessageAggregatePlanQuery aggregatePlanQuery);
}
