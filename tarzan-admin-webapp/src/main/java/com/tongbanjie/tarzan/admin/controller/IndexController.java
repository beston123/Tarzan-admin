package com.tongbanjie.tarzan.admin.controller;

import com.tongbanjie.tarzan.admin.common.AdminUtils;
import com.tongbanjie.tarzan.admin.component.AdminServerComponent;
import com.tongbanjie.tarzan.common.TarzanVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private AdminServerComponent adminServerComponent;

    @RequestMapping("/")
    public String index(Model model) {
        return toIndex(model);
    }

    @RequestMapping("/index")
    public String toIndex(Model model) {
        model.addAttribute("version", TarzanVersion.CURRENT.getName());
        model.addAttribute("registryType", "ZooKeeper");
        model.addAttribute("registryAddress", adminServerComponent.getZkAddress());
        model.addAttribute("registryAddressIp", AdminUtils.getIpByDomain(adminServerComponent.getZkAddress()));
        return "index";
    }

    @RequestMapping(value = "/user/logout")
    public String logOut() {
        return "index";
    }

}
