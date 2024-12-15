package com.yibu.yibuJudge.model.cmd;

import cn.hutool.core.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
public class CommandArgs {
    private String executablePath; // 沙箱路径
    private String logPath; // 日志路径
    private String buildName; // 编译名
    private List<String> inputPaths; // 输入路径
    private String outputPath;  // 输出路径
    private String errorPath; // 错误路径
    private String maxCpuTime; // 最大cpu时间
    private String maxRealTime; // 最大运行时间
    private String maxMemory; // 最大内存
    private String maxOutputSize; // 最大输出大小

    public void init() {
        FileUtil.mkdir(outputPath);
        FileUtil.mkdir(errorPath);
    }


    public String[] getCommandStr(String exePath, List<String> args, String inputPath, int i, String seccompRuleName, Map<String, String> kwargs) {
        // 创建动态参数列表
        List<String> command = new ArrayList<>();

        // 固定的基础参数
        command.add(executablePath);
        command.add("--max_cpu_time=" + maxCpuTime);
        command.add("--max_real_time=" + maxRealTime);
        command.add("--exe_path=" + exePath);
        command.add("--max_output_size=" + maxOutputSize);


        if (args != null && !args.isEmpty()) {
            for (String arg : args) {
                command.add("--arg=" + arg);
            }
        }

        if (inputPath != null && !inputPath.trim().isEmpty()) {
            command.add("--input_path=" + inputPath);
        }


        command.add("--output_path=" + outputPath + "/" + i + ".txt");
        command.add("--error_path=" + errorPath + "/" + i + ".txt");
        command.add("--log_path=" + logPath);


        if (seccompRuleName != null && !seccompRuleName.trim().isEmpty()) {
            command.add("--seccomp_rule_name=" + seccompRuleName);
        }

        if (kwargs != null && !kwargs.isEmpty()) {
            for (String key : kwargs.keySet()) {
                command.add("--" + key + "=" + kwargs.get(key));
            }
        }else {
            command.add("--max_memory=" + maxMemory);
        }


        return command.toArray(new String[0]);
    }

    public String getRealOutputPath(int i){
        return outputPath + "/" + i + ".txt";
    }

    public String getRealErrorPath(int i){
        return errorPath + "/" + i + ".txt";
    }
}