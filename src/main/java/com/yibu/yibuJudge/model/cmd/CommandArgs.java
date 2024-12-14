package com.yibu.yibuJudge.model.cmd;

import cn.hutool.core.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


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


    public String[] getCommandStr(String exePath, String args, String inputPath, int i){

        return new String[]{
                "sudo",
                executablePath,
                "--max_cpu_time=" + maxCpuTime,
                "--max_real_time=" + maxRealTime,
                "--exe_path=" + exePath,
                "--args=" + args,
                "--input_path=" + inputPath,
                "--output_path=" + outputPath + "/" + i + ".txt",
                "--error_path=" + errorPath + "/" + i + ".txt",
                "--log_path=" + logPath
        };
    }

    public String getRealOutputPath(int i){
        return outputPath + "/" + i + ".txt";
    }

    public String getRealErrorPath(int i){
        return errorPath + "/" + i + ".txt";
    }
}