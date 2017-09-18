package com.tongbanjie.tarzan.admin.common;

import com.alibaba.fastjson.JSON;
import com.tongbanjie.tarzan.common.TarzanVersion;

/**
 * 〈显示工具〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/9
 */
public class ViewUtils {

    /**
     * 获取版本号
     * @param value
     * @return
     */
    public static String getVersionName(Integer value){
        if(value == null){
            return null;
        }
        return TarzanVersion.getVersionName(value);
    }

    /**
     * 转JSON对象
     * @param object
     * @return
     */
    public static Object toJSON(Object object){
        return JSON.toJSON(object);
    }

}
