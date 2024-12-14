package com.yibu.yibuJudge.actuator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.yibu.yibuJudge.constant.JudgeConstants;
import com.yibu.yibuJudge.enumerate.Result;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.exceptions.CompilerException;
import com.yibu.yibuJudge.model.cmd.CommandArgs;
import com.yibu.yibuJudge.model.cmd.CommandResult;
import com.yibu.yibuJudge.properties.ExecProperties;
import com.yibu.yibuJudge.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

@Slf4j
@Component("Java")
public class JavaCompiler implements Compiler {

    private final ExecProperties execProperties;

    private final FileProperties fileProperties;

    private final ThreadPoolTaskExecutor judgeExecutor;


    public JavaCompiler(ExecProperties execProperties,
                        FileProperties fileProperties,
                        ThreadPoolTaskExecutor judgeExecutor) {
        this.execProperties = execProperties;
        this.fileProperties = fileProperties;
        this.judgeExecutor = judgeExecutor;
    }

    @Override
    public String build(String codeSource, boolean rmSource) throws CompilerException{
        String buildDir = UUID.randomUUID().toString().replace("-", "");
        String realCodePath = fileProperties.getCodePath() + "/" + codeSource;
        FileUtil.mkParentDirs(fileProperties.getBuildPath() + "/" + buildDir);
        String[] command = {execProperties.getJavac(), "-encoding", "UTF-8", "-d", fileProperties.getBuildPath() + "/" + buildDir, realCodePath};
        try{
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode!= 0) {
                String error = IoUtil.readUtf8(process.getErrorStream());
                error = error.substring(error.indexOf("\n") + 1);
                FileUtil.del(fileProperties.getBuildPath() + "/" + buildDir);
                throw new CompilerException(error);
            }
        } catch (IOException | InterruptedException e) {
            FileUtil.del(fileProperties.getBuildPath() + "/" + buildDir);
            throw new BaseException(Result.SYSTEM_ERROR.getMessage());
        }finally {
            if (rmSource) {
                FileUtil.del(realCodePath.substring(0, realCodePath.lastIndexOf("/")));
            }
        }
        return buildDir;
    }

    @Override
    public List<CommandResult> run(CommandArgs args) {
        args.init();
        List<String> inputPaths = args.getInputPaths();
        List<CommandResult> results = new ArrayList<>();
        List<Future<CommandResult>> futures = new ArrayList<>();
        // 提交所有任务
        try {
            for (int i = 0; i < inputPaths.size(); i++) {
                int finalI = i;
                Future<CommandResult> future = judgeExecutor.submit(() -> {
                    String inputPath = inputPaths.get(finalI);
                    String[] commandStr = args.getCommandStr(
                            execProperties.getJava(),
                            "-cp " + fileProperties.getBuildPath() + "/" + args.getBuildName(),
                            inputPath,
                            finalI
                    );
                    log.info("[exec] {}", String.join(" ", commandStr));
                    Process exec = Runtime.getRuntime().exec(commandStr);
                    int exitCode = exec.waitFor();
                    if (exitCode != 0) {
                        throw new BaseException(Result.SYSTEM_ERROR.getMessage());
                    }
                    String data = IoUtil.readUtf8(exec.getInputStream());
                    CommandResult commandResult = JSONObject.parseObject(data, CommandResult.class);
                    commandResult.setOutputPath(args.getRealOutputPath(finalI));
                    commandResult.setErrorPath(args.getRealErrorPath(finalI));
                    return commandResult;
                });
                futures.add(future);
            }
            for (Future<CommandResult> future : futures) {
                try {
                    results.add(future.get()); // 阻塞直到任务完成
                } catch (Exception e) {
                    throw new BaseException(Result.SYSTEM_ERROR.getMessage());
                }
            }
            return results;
        }finally {
            deStory(args.getBuildName());
        }
    }


    @Override
    public void deStory(String buildDir) {
        String buildPath = fileProperties.getBuildPath() + "/" + buildDir;
        boolean del = FileUtil.del(buildPath);
        if (!del){
            log.error("delete build file error {}", buildPath);
        }
    }

    @Override
    public String saveCode(String code) {
        String codeSource = UUID.randomUUID().toString().replace("-", "") + "/Main.java";
        String reaSaveCodePath = fileProperties.getCodePath() + "/" + codeSource;
        FileUtil.mkParentDirs(reaSaveCodePath);
        try(FileOutputStream fileOutputStream = new FileOutputStream(reaSaveCodePath)){
            IoUtil.writeUtf8(fileOutputStream, false, code);
        }catch (Exception e){
            throw new BaseException(JudgeConstants.SYSTEM_ERROR);
        }
        return codeSource;
    }
}
