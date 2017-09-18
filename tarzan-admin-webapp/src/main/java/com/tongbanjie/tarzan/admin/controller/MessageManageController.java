package com.tongbanjie.tarzan.admin.controller;

import com.tongbanjie.tarzan.admin.common.BasePage;
import com.tongbanjie.tarzan.admin.common.Controllers;
import com.tongbanjie.tarzan.admin.common.Response;
import com.tongbanjie.tarzan.admin.service.MessageManageService;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.common.message.MQType;
import com.tongbanjie.tarzan.store.model.MessageConsume;
import com.tongbanjie.tarzan.store.model.ToCheckMessage;
import com.tongbanjie.tarzan.store.model.ToSendMessage;
import com.tongbanjie.tarzan.store.query.MessageConsumeQuery;
import com.tongbanjie.tarzan.store.query.ToCheckMessageQuery;
import com.tongbanjie.tarzan.store.query.ToSendMessageQuery;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈消息管理Controller〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/2
 */
@RequestMapping(Controllers.MESSAGE)
@Controller
public class MessageManageController extends BaseController {

    @Resource
    private MessageManageService messageManageService;

    /**
     * 待确认事务 查询
     * @param model
     * @param toCheckMessageQuery
     * @param basePage
     * @return
     */
    @RequestMapping(value = "queryToCheck")
    public String queryToCheck(Model model, ToCheckMessageQuery toCheckMessageQuery, BasePage basePage){
        Response response = Response.newInstance();
        try {
            response.put("query", toCheckMessageQuery);
            response.put("basePage", basePage);

            Result<Integer> countRet = messageManageService.countToCheck(toCheckMessageQuery);
            Validate.isTrue(countRet.isSuccess(), countRet.getErrorDetail());
            toCheckMessageQuery.setPagingParam(basePage.getPagingParam(countRet.getData()));

            if(basePage.getTotalCount() > 0){
                Result<List<ToCheckMessage>> ret = messageManageService.listToCheck(toCheckMessageQuery);
                Validate.isTrue(ret.isSuccess(), ret.getErrorDetail());
                response.put("list", ret.getData());
            }
        } catch (Exception e) {
            LOGGER.error("查询失败，参数："+toCheckMessageQuery, e);
            response.putFail("查询失败.", e);
        }
        model.addAllAttributes(response.toJSON());
        return Controllers.MESSAGE+"/toCheckList";
    }

    /**
     * 检查事务状态
     * @param tid
     * @param mqType
     * @return
     */
    @RequestMapping(value = "/check/{tid}/{mqType}", method = RequestMethod.POST)
    public @ResponseBody
    Object checkTransaction(@PathVariable Long tid, @PathVariable Byte mqType){
        Response response = Response.newInstance();
        try {
            MQType type = MQType.valueOf(mqType);
            Result<Void> ret = messageManageService.checkTransaction(tid, type);
            if(ret.isSuccess()){
                response.putSuccess("检查事务成功");
            }else{
                response.putFail("检查事务失败，" + ret.getErrorDetail());
            }
        } catch (Exception e) {
            LOGGER.error("检查事务失败，tid="+tid,e);
            response.putFail("检查事务失败，"+e.getMessage());
        }
        return response.toJSON();
    }

    /**
     * 待发送消息 查询
     * @param model
     * @param toSendMessageQuery
     * @param basePage
     * @return
     */
    @RequestMapping(value = "queryToSend")
    public String queryToSend(Model model, ToSendMessageQuery toSendMessageQuery, BasePage basePage){
        Response response = Response.newInstance();
        try {
            response.put("query", toSendMessageQuery);
            response.put("basePage", basePage);

            Result<Integer> countRet = messageManageService.countToSend(toSendMessageQuery);
            Validate.isTrue(countRet.isSuccess(), countRet.getErrorDetail());
            toSendMessageQuery.setPagingParam(basePage.getPagingParam(countRet.getData()));

            if(basePage.getTotalCount() > 0){
                Result<List<ToSendMessage>> ret = messageManageService.listToSend(toSendMessageQuery);
                Validate.isTrue(ret.isSuccess(), ret.getErrorDetail());
                response.put("list", ret.getData());
            }
        } catch (Exception e) {
            LOGGER.error("查询失败，参数："+toSendMessageQuery, e);
            response.putFail("查询失败.", e);
        }
        model.addAllAttributes(response.toJSON());
        return Controllers.MESSAGE+"/toSendList";
    }

    /**
     * 发送消息
     * @param tid
     * @param mqType
     * @return
     */
    @RequestMapping(value = "/send/{tid}/{mqType}", method = RequestMethod.POST)
    public @ResponseBody
    Object sendMessage(@PathVariable Long tid, @PathVariable Byte mqType){
        Response response = Response.newInstance();
        try {
            MQType type = MQType.valueOf(mqType);
            Result<String> ret = messageManageService.sendMessage(tid, type);
            if(ret.isSuccess()){
                response.putSuccess("发送消息成功, 消息Id: "+ret.getData());
            }else{
                response.putFail("发送消息失败，" + ret.getErrorDetail());
            }
        } catch (Exception e) {
            LOGGER.error("发送消息失败，tid="+tid,e);
            response.putFail("发送消息失败，"+e.getMessage());
        }
        return response.toJSON();
    }

    /**
     * 批量发送消息成功
     * @return
     */
    @RequestMapping(value = "/sendBatch", method = RequestMethod.POST)
    public @ResponseBody
    Object sendMessageBatch(String ids){
        Response response = Response.newInstance();
        try {
            String[] idArray = ids.split(",");
            Result<Void> ret = messageManageService.sendMessageBatch(idArray);
            if(ret.isSuccess()){
                response.putSuccess("批量发送消息成功");
            }else{
                response.putFail("批量发送消息失败，" + ret.getErrorDetail());
            }
        } catch (Exception e) {
            LOGGER.error("批量发送消息失败，ids="+ids,e);
            response.putFail("批量发送消息失败，"+e.getMessage());
        }
        return response.toJSON();
    }

    /**
     * 消息消费结果 查询
     * @param model
     * @param messageConsumeQuery
     * @param basePage
     * @return
     */
    @RequestMapping(value = "queryConsume")
    public String queryConsume(Model model, MessageConsumeQuery messageConsumeQuery, BasePage basePage){
        Response response = Response.newInstance();
        try {
            response.put("query", messageConsumeQuery);
            response.put("basePage", basePage);

            Result<Integer> countRet = messageManageService.countMessageConsume(messageConsumeQuery);
            Validate.isTrue(countRet.isSuccess(), countRet.getErrorDetail());
            messageConsumeQuery.setPagingParam(basePage.getPagingParam(countRet.getData()));

            if(basePage.getTotalCount() > 0){
                Result<List<MessageConsume>> ret = messageManageService.listMessageConsume(messageConsumeQuery);
                Validate.isTrue(ret.isSuccess(), ret.getErrorDetail());
                response.put("list", ret.getData());
            }
        } catch (Exception e) {
            LOGGER.error("查询失败，参数："+messageConsumeQuery, e);
            response.putFail("查询失败.", e);
        }
        model.addAllAttributes(response.toJSON());
        return Controllers.MESSAGE+"/consumeList";
    }

    /**
     * 删除消费结果
     * 谨慎操作
     * @param id
     * @return
     */
    @RequestMapping(value = "/consume/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteConsume(@PathVariable Long id){
        Response response = Response.newInstance();
        try {
            Result<Integer> result = messageManageService.deleteConsume(id);
            if(result.isSuccess()){
                response.putSuccess("删除成功");
            }else{
                response.putFail("删除失败，" + result.getErrorDetail());
            }
        } catch (Exception e) {
            LOGGER.error("删除失败",e);
            response.putFail("删除失败，"+e.getMessage());
        }
        return response.toJSON();
    }

    /**
     * 批量删除消费结果成功
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public @ResponseBody
    Object deleteBatch(String ids){
        Response response = Response.newInstance();
        try {
            String[] idArray = ids.split(",");
            for(String id : idArray){
                Result<Integer> ret = messageManageService.deleteConsume(Long.parseLong(id));
                if(!ret.isSuccess()){
                    LOGGER.warn("删除消费结果失败，id="+id);
                }
            }
            response.putSuccess("删除消费结果成功");
        } catch (Exception e) {
            LOGGER.error("批量删除消费结果失败，ids="+ids,e);
            response.putFail("批量删除消费结果失败，"+e.getMessage());
        }
        return response.toJSON();
    }
}
