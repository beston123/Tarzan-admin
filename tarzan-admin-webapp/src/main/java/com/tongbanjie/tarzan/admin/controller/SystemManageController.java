package com.tongbanjie.tarzan.admin.controller;

import com.tongbanjie.tarzan.admin.common.AdminUtils;
import com.tongbanjie.tarzan.admin.common.BasePage;
import com.tongbanjie.tarzan.admin.common.Controllers;
import com.tongbanjie.tarzan.admin.common.Response;
import com.tongbanjie.tarzan.admin.service.SystemManageService;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.store.model.MessageAggregatePlan;
import com.tongbanjie.tarzan.store.query.MessageAggregatePlanQuery;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 〈系统管理Controller〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/4
 */
@RequestMapping(Controllers.SYSTEM)
@Controller
public class SystemManageController extends BaseController{

    @Autowired
    private SystemManageService systemManageService;

    @RequestMapping(value = "aggPlan")
    public String aggPlan(Model model){
        MessageAggregatePlanQuery aggregatePlanQuery = new MessageAggregatePlanQuery();
        //查询今天的数据
        Date todayFrom = AdminUtils.setDate000000(new Date());
        Date todayTo = AdminUtils.setDate235959(new Date());
        aggregatePlanQuery.setTimeStartFrom(todayFrom);
        aggregatePlanQuery.setTimeStartTo(todayTo);
        return queryAggPlan(model, aggregatePlanQuery, new BasePage());
    }

    /**
     * 待确认事务 查询
     * @param model
     * @param aggregatePlanQuery
     * @param basePage
     * @return
     */
    @RequestMapping(value = "queryAggPlan")
    public String queryAggPlan(Model model, MessageAggregatePlanQuery aggregatePlanQuery, BasePage basePage){
        Response response = Response.newInstance();
        try {
            response.put("query", aggregatePlanQuery);
            response.put("basePage", basePage);

            Result<Integer> countRet = systemManageService.countAggregatePlan(aggregatePlanQuery);
            Validate.isTrue(countRet.isSuccess(), countRet.getErrorDetail());
            aggregatePlanQuery.setPagingParam(basePage.getPagingParam(countRet.getData()));

            if(basePage.getTotalCount() > 0){
                Result<List<MessageAggregatePlan>> ret = systemManageService.listAggregatePlan(aggregatePlanQuery);
                Validate.isTrue(ret.isSuccess(), ret.getErrorDetail());
                response.put("list", ret.getData());
            }
        } catch (Exception e) {
            LOGGER.error("查询失败，参数："+aggregatePlanQuery, e);
            response.putFail("查询失败.", e);
        }
        model.addAllAttributes(response.toJSON());
        return Controllers.SYSTEM+"/aggregatePlanList";
    }

    /**
     * 清理汇集计划
     * 谨慎操作
     * @param aggregatePlanQuery
     * @return
     */
    @RequestMapping(value = "/cleanAggPlan", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteConsume(MessageAggregatePlanQuery aggregatePlanQuery){
        Response response = Response.newInstance();
        try {
            Result<Integer> result = systemManageService.cleanAggregatePlan(aggregatePlanQuery);
            if(result.isSuccess()){
                response.putSuccess("成功删除"+result.getData()+"条");
            }else{
                response.putFail("删除失败，" + result.getErrorDetail());
            }
        } catch (Exception e) {
            LOGGER.error("删除失败",e);
            response.putFail("删除失败，"+e.getMessage());
        }
        return response.toJSON();
    }
}
