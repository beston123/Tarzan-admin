package com.tongbanjie.tarzan.admin.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 〈工具类〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/8
 */
public class AdminUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminUtils.class);

    /**
     * 设置时间到当天 00:00:00
     * @param date
     */
    @SuppressWarnings("deprecation")
    public static Date setDate000000(Date date){
        if(date != null){
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
        }
        return date;
    }

    /**
     * 设置时间到当天 23:59:59
     * @param date
     */
    @SuppressWarnings("deprecation")
    public static Date setDate235959(Date date){
        if(date != null){
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
        }
        return date;
    }

    /**
     *
     * @param registryAddress
     * @return
     */
    public static String getIpByDomain(String registryAddress) {
        String[] addressArray = registryAddress.split(",");
        StringBuffer ipBuffer = new StringBuffer();
        for(String address : addressArray){
            String[] strings = address.split(":");
            try {
                InetAddress ipAddress = InetAddress.getByName(strings[0]);
                ipBuffer.append(",").append(ipAddress.getHostAddress());
            } catch (UnknownHostException e) {
                LOGGER.error("Host解析错误，"+address, e);
            }
        }
        if(ipBuffer.length() > 0){
            return ipBuffer.toString().substring(1);
        }else{
            return null;
        }
    }
}
