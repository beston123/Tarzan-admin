package com.tongbanjie.tarzan.admin.service;

import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.registry.ClientAddress;

import java.util.List;

/**
 * 〈客户端管理服务〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/9
 */
public interface ClientManageService {

    /**
     * 查询所有Client列表
     * @return
     */
    Result<List<ClientAddress>> getAllClients();
}
