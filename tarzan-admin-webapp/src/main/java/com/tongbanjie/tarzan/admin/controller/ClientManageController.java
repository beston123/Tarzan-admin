package com.tongbanjie.tarzan.admin.controller;

import com.tongbanjie.tarzan.admin.common.Controllers;
import com.tongbanjie.tarzan.admin.common.Response;
import com.tongbanjie.tarzan.admin.service.ClientManageService;
import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.common.util.ResultValidate;
import com.tongbanjie.tarzan.registry.ClientAddress;
import com.tongbanjie.tarzan.registry.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 〈客户端管理Controller〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 16/11/17
 */

@RequestMapping(Controllers.CLIENT)
@Controller
public class ClientManageController extends BaseController{

    @Autowired
    private ClientManageService clientManageService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        Response response = Response.newInstance();
        try {
            Result<List<ClientAddress>> result = clientManageService.getAllClients();
            ResultValidate.isTrue(result);
            response.putSuccess("成功", result.getData());
        } catch (Exception e) {
            LOGGER.error("",e);
            response.putFail("查询失败.", e);
        }
        model.addAllAttributes(response.toJSON());
        return "client/clientList";
    }

}
