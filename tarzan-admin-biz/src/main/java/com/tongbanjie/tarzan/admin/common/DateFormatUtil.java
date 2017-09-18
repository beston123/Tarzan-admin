package com.tongbanjie.tarzan.admin.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈时间格式化工具〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 16/12/14
 */
public class DateFormatUtil {

    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 显示日期
     * @param date
     * @return
     */
    public static String showDate(Date date){
        if(null==date){
            return "";
        }
        return sdf1.format(date);
    }

    /**
     * 显示日期时间
     * @param date
     * @return
     */
    public static String showDateTime(Date date){
        if(null==date){
            return "";
        }
        return sdf2.format(date);
    }

}
