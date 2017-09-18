package com.tongbanjie.tarzan.admin.common;

/**
 * 〈Controller Request Mapping〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 16/11/22
 */
public abstract class Controllers {

    /**
     * 服务端管理
     */
    public static final String SERVER = "/server";

    /**
     * 客户端管理
     */
    public static final String CLIENT = "/client";

    /**
     * 消息管理
     */
    public static final String MESSAGE = "/message";

    /**
     * RocketMQ消息
     */
    public static final String MESSAGE_ROCKETMQ = MESSAGE + "/rocketmq";

    /**
     * 系统管理
     */
    public static final String SYSTEM = "/system";


}
