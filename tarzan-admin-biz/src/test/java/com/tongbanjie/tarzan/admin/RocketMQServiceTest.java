package com.tongbanjie.tarzan.admin;

import com.tongbanjie.tarzan.common.Result;
import com.tongbanjie.tarzan.common.message.MQType;
import com.tongbanjie.tarzan.store.service.RocketMQStoreService;
import org.junit.Assert;
import org.junit.Test;


import javax.annotation.Resource;

/**
 * 〈RocketMQService〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/1/18
 */
public class RocketMQServiceTest extends BaseTestCase{

    @Resource
    private RocketMQStoreService rocketMQStoreService;

    @Test
    public void countTest(){
        Result<Integer> result =rocketMQStoreService.countToCheck(MQType.ROCKET_MQ);
        Assert.assertEquals(true, result.isSuccess());
        System.out.println("Count:"+result.getData());
    }


}
