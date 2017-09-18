package com.tongbanjie.tarzan.admin.service.impl;

import com.tongbanjie.tarzan.admin.common.AdminUtils;
import com.tongbanjie.tarzan.admin.service.SystemManageService;
import com.tongbanjie.tarzan.common.OrderBy;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.store.model.MessageAggregatePlan;
import com.tongbanjie.tarzan.store.query.MessageAggregatePlanQuery;
import com.tongbanjie.tarzan.store.service.MessageAggregatePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈系统管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/4
 */
@Service
public class SystemManageServiceImpl implements SystemManageService {

    @Autowired
    private MessageAggregatePlanService messageAggregatePlanService;

    @Override
    public Result<Integer> countAggregatePlan(MessageAggregatePlanQuery aggregatePlanQuery) {
        AdminUtils.setDate235959(aggregatePlanQuery.getTimeStartTo());
        return messageAggregatePlanService.countAggregatePlan(aggregatePlanQuery);
    }

    @Override
    public Result<List<MessageAggregatePlan>> listAggregatePlan(MessageAggregatePlanQuery aggregatePlanQuery) {
        AdminUtils.setDate235959(aggregatePlanQuery.getTimeStartTo());
        aggregatePlanQuery.setOrderByClause(OrderBy.ORDER_BY_ID_DESC);
        return messageAggregatePlanService.listAggregatePlan(aggregatePlanQuery);
    }

    @Override
    public Result<Integer> cleanAggregatePlan(MessageAggregatePlanQuery aggregatePlanQuery) {
        AdminUtils.setDate235959(aggregatePlanQuery.getTimeStartTo());
        return messageAggregatePlanService.cleanAggregatePlan(aggregatePlanQuery);
    }

}
