package com.maoding.utils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/11
 * 类名: com.maoding.utils.CommandUtils
 * 作者: 张成亮
 * 描述: 命令行管理器
 **/
public class ShellUtils {
    /**
     * 描述       执行命令行
     * 日期       2018/9/11
     * @author   张成亮
     * @param cmd 命令行字符串
     *            以";"分隔运行程序和参数
     *            使用绝对路径
     * @param title 运行窗口标题
     **/
    public void execute(@NotNull String cmd, String title){
        if (StringUtils.isNotEmpty(title)){
            TraceUtils.info("启动" + title + ":" + TraceUtils.getCaller());
        }

        String[] cmdArray = cmd.split(";");
        //如果是bat文件，使用cmd执行
        if ((cmdArray.length > 0) && (StringUtils.isSame(StringUtils.getFileExt(cmdArray[0]),".bat"))){
            List<String> cmdList = new ArrayList<>();
            cmdList.add("cmd");
            cmdList.add("/c");
            cmdList.add("start");
            if (StringUtils.isNotEmpty(title)) {
                cmdList.add("\"" + title + "\"");
            } else {
                cmdList.add("\"" + cmdArray[0] + "\"");
            }
            Collections.addAll(cmdList, cmdArray);
            cmdArray = cmdList.toArray(new String[cmdList.size()]);
        }

        if (cmdArray.length > 0) {
            try {
                Runtime.getRuntime().exec(cmdArray);
            } catch (IOException e) {
                TraceUtils.error("执行命令时出现错误",e);
            }
        }
    }

    public void execute(@NotNull String cmd){
        execute(cmd,null);
    }
}
