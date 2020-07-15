package me.yanghao.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author YangHao
 * @ClassName: ThrowableUtil
 * @Description: TODO
 * @date 2020/7/15 16:54
 * @Version V1.0
 */
public class ThrowableUtil {

    /**
     * @Description 读取堆栈信息
     * @Date 2020/7/15 16:57
     * @param throwable
     * @return java.lang.String
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace();
            return sw.toString();
        }
    }
}
