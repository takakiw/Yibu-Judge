package com.yibu.yibujudge.actuator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.yibu.yibujudge.constant.JudgeConstants;
import com.yibu.yibujudge.enumerate.Result;
import com.yibu.yibujudge.exceptions.BaseException;
import com.yibu.yibujudge.exceptions.CompilerException;
import com.yibu.yibujudge.model.cmd.CommandArgs;
import com.yibu.yibujudge.model.cmd.CommandResult;
import com.yibu.yibujudge.properties.ExecProperties;
import com.yibu.yibujudge.properties.FileProperties;
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
@Component("C")
public class CCompiler implements Compiler {

    private final ExecProperties execProperties;
    private final FileProperties fileProperties;
    private final ThreadPoolTaskExecutor judgeExecutor;

    public CCompiler(ExecProperties execProperties,
                     FileProperties fileProperties,
                     ThreadPoolTaskExecutor judgeExecutor) {
        this.execProperties = execProperties;
        this.fileProperties = fileProperties;
        this.judgeExecutor = judgeExecutor;
    }

    @Override
    public String build(String codeSource, boolean rmSource) throws CompilerException {
        String buildDir = UUID.randomUUID().toString().replace("-", "");
        String realCodePath = fileProperties.getCodePath() + "/" + codeSource;
        FileUtil.mkParentDirs(fileProperties.getBuildPath() + "/" + buildDir);
        String[] command = {execProperties.getGcc(), realCodePath, "-std=c11", "-o", fileProperties.getBuildPath() + "/" + buildDir};
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String error = IoUtil.readUtf8(process.getErrorStream());
                FileUtil.del(fileProperties.getBuildPath() + "/" + buildDir);
                throw new CompilerException(error);
            }
        } catch (IOException | InterruptedException e) {
            FileUtil.del(fileProperties.getBuildPath() + "/" + buildDir);
            throw new BaseException(Result.SYSTEM_ERROR.getMessage());
        } finally {
            if (rmSource) {
                FileUtil.del(realCodePath);
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

        try {
            for (int i = 0; i < inputPaths.size(); i++) {
                int finalI = i;
                Future<CommandResult> future = judgeExecutor.submit(() -> {
                    String inputPath = inputPaths.get(finalI);
                    String[] commandStr = args.getCommandStr(
                            fileProperties.getBuildPath() + "/" + args.getBuildName(),
                            null,
                            inputPath,
                            finalI,
                            "c_cpp",
                            null
                    );
                    log.info("[exec] {}", String.join(" ", commandStr));
                    Process exec = Runtime.getRuntime().exec(commandStr);
                    int exitCode = exec.waitFor();
                    if (exitCode != 0) {
                        throw new BaseException(Result.SYSTEM_ERROR.getMessage());
                    }
                    String data = IoUtil.readUtf8(exec.getInputStream());
                    log.info("[result] {}", data);
                    CommandResult commandResult = JSONObject.parseObject(data, CommandResult.class);
                    commandResult.setOutputPath(args.getRealOutputPath(finalI));
                    commandResult.setErrorPath(args.getRealErrorPath(finalI));
                    return commandResult;
                });
                futures.add(future);
            }

            for (Future<CommandResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    throw new BaseException(Result.SYSTEM_ERROR.getMessage());
                }
            }
            return results;
        } finally {
            deStory(args.getBuildName());
        }
    }

    @Override
    public void deStory(String buildDir) {
        String buildPath = fileProperties.getBuildPath() + "/" + buildDir;
        boolean del = FileUtil.del(buildPath);
        if (!del) {
            log.error("delete build file error {}", buildPath);
        }
    }

    @Override
    public String saveCode(String code) {
        String codeSource = UUID.randomUUID().toString().replace("-", "") + ".c";
        String reaSaveCodePath = fileProperties.getCodePath() + "/" + codeSource;
        FileUtil.mkParentDirs(reaSaveCodePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(reaSaveCodePath)) {
            IoUtil.writeUtf8(fileOutputStream, false, code);
        } catch (Exception e) {
            throw new BaseException(JudgeConstants.SYSTEM_ERROR);
        }
        return codeSource;
    }
}
