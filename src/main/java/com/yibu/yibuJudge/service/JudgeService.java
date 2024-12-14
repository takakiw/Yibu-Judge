package com.yibu.yibuJudge.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.yibu.yibuJudge.actuator.Compiler;
import com.yibu.yibuJudge.actuator.CompilerFactory;
import com.yibu.yibuJudge.constant.JudgeConstants;
import com.yibu.yibuJudge.enumerate.Error;
import com.yibu.yibuJudge.enumerate.Result;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.exceptions.CompilerException;
import com.yibu.yibuJudge.model.cmd.CommandArgs;
import com.yibu.yibuJudge.model.cmd.CommandResult;

import com.yibu.yibuJudge.model.entity.Problem;
import com.yibu.yibuJudge.model.entity.Testcase;
import com.yibu.yibuJudge.model.mq.SubmitMQType;
import com.yibu.yibuJudge.model.response.JudgeResult;
import com.yibu.yibuJudge.properties.FileProperties;
import com.yibu.yibuJudge.properties.SandBoxProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class JudgeService {

    private final CompilerFactory compilerFactory;

    private final FileProperties fileProperties;

    private final SandBoxProperties sandBoxProperties;


    public JudgeService(CompilerFactory compilerFactory,
                        FileProperties fileProperties,
                        SandBoxProperties sandBoxProperties) {
        this.compilerFactory = compilerFactory;
        this.fileProperties = fileProperties;
        this.sandBoxProperties = sandBoxProperties;
    }


    public JudgeResult debug(String language, String code, String inputData) {
        String tempInputPath = null;
        String realOutputPath = null;
        String realErrorPath = null;
        try {
            // 保存输入数据
            tempInputPath = fileProperties.getTempCasePath() + "/" + UUID.randomUUID().toString().replace("-", "") + ".txt";
            try (FileOutputStream outputStream = new FileOutputStream(tempInputPath)) {
                IoUtil.writeUtf8(outputStream, true, inputData);
            } catch (Exception e) {
                throw new BaseException(JudgeConstants.SAVE_INPUT_ERROR);
            }
            JudgeResult judgeResult = new JudgeResult();
            // 获取编译器
            Compiler compiler = compilerFactory.getCompiler(language);
            // 保存代码
            String codeName = compiler.saveCode(code);
            // 编译代码
            String buildName;
            try {
                buildName = compiler.build(codeName, true);
            } catch (CompilerException e) { // 编译错误
                judgeResult.setStatus(Result.BUILD_ERROR.getCode());
                judgeResult.setMessage(e.getMessage());
                return judgeResult;
            }

            String uuid = UUID.randomUUID().toString().replace("-", "");
            realOutputPath = fileProperties.getTempOutPath() + "/" + uuid;
            realErrorPath = fileProperties.getTempErrPath() + "/" + uuid;
            CommandArgs commandArgs = new CommandArgs(
                    sandBoxProperties.getExePath(),
                    sandBoxProperties.getLogPath(),
                    buildName,
                    List.of(tempInputPath),
                    realOutputPath,
                    realErrorPath,
                    sandBoxProperties.getDefaultMaxCpuTime(),
                    sandBoxProperties.getDefaultMaxRealTime(),
                    sandBoxProperties.getDefaultMaxMemory(),
                    sandBoxProperties.getDefaultMaxOutputSize());
            // 运行代码
            List<CommandResult> results = compiler.run(commandArgs);
            CommandResult commandResult = results.get(0);
            // 删除临时用例和编译结果
            FileUtil.del(tempInputPath);
            compiler.deStory(buildName);
            // 保存输出数据
            Error error = Error.fromCode(commandResult.getError());
            if (error != Error.SUCCESS) {
                judgeResult.setStatus(error.getCode());
                judgeResult.setMessage(error.getErrorMessage());
                return judgeResult;
            }
            Result result = Result.fromCode(commandResult.getResult());
            if (result != Result.SUCCESS) {
                judgeResult.setStatus(result.getCode());
                judgeResult.setMessage(result.getMessage());
                try {
                    if (result != Result.RUNTIME_ERROR) judgeResult.setOutput(IoUtil.readUtf8(new FileInputStream(commandArgs.getRealOutputPath(0))));
                    else judgeResult.setOutput(IoUtil.readUtf8(new FileInputStream(commandArgs.getRealErrorPath(0))));
                } catch (Exception e) {
                    judgeResult.setOutput("");
                }
                return judgeResult;
            }
            judgeResult.setStatus(Result.SUCCESS.getCode());
            judgeResult.setMessage(Result.SUCCESS.getMessage());
            try {
                judgeResult.setOutput(IoUtil.readUtf8(new FileInputStream(commandArgs.getRealOutputPath(0))));
            } catch (FileNotFoundException e) {
                throw new BaseException(Result.SYSTEM_ERROR.getMessage());
            }
            return judgeResult;
        }finally {
            FileUtil.del(tempInputPath);
            FileUtil.del(realOutputPath);
            FileUtil.del(realErrorPath);
        }
    }

    public JudgeResult judge(SubmitMQType submit) {
        // 获取 submit 的相关信息
        String language = submit.getLanguage();
        String code = submit.getCode();
        Problem problem = submit.getProblem();
        List<Testcase> testcases = submit.getTestcases();

        // 初始化 JudgeResult
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setCaseCount((long) testcases.size()); // 总用例数
        judgeResult.setAcCaseCount(0L);
        judgeResult.setCpuTime(0L);
        judgeResult.setMemory(0L);

        // 获取编译器
        Compiler compiler = compilerFactory.getCompiler(language);
        // 保存代码
        String codeName = compiler.saveCode(code);
        judgeResult.setCodePath(fileProperties.getCodePath() + "/" + codeName);
        log.info("code saved to " + judgeResult.getCodePath());
        // 编译代码
        String buildName;
        try {
            buildName = compiler.build(codeName, false); // 编译不删除源文件
        } catch (CompilerException e) { // 编译错误
            judgeResult.setStatus(Result.BUILD_ERROR.getCode());
            judgeResult.setMessage(e.getMessage());
            return judgeResult;
        }
        // 运行代码
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String realOutputPath = fileProperties.getTempOutPath() + "/" + uuid;
        String realErrorPath = fileProperties.getTempErrPath() + "/" + uuid;
        List<String> inputPaths = testcases.stream().map(Testcase::getInputPath).toList();

        CommandArgs commandArgs = new CommandArgs(
                sandBoxProperties.getExePath(),
                sandBoxProperties.getLogPath(),
                buildName,
                inputPaths,
                realOutputPath,
                realErrorPath,
                problem.getTimeLimit().toString(),
                problem.getTimeLimit() * 2 + "",
                problem.getMemoryLimit() + "",
                sandBoxProperties.getDefaultMaxOutputSize()
        );

        try {
            List<CommandResult> commandResultList = compiler.run(commandArgs);
            for (int i = 0; i < commandResultList.size(); i++) {
                CommandResult commandResult = commandResultList.get(i);
                Testcase testcase = testcases.get(i);
                // 累积 CPU 时间和内存
                judgeResult.setCpuTime(judgeResult.getCpuTime() + commandResult.getCpuTime());
                judgeResult.setMemory(Math.max(judgeResult.getMemory(), commandResult.getMemory())); // 最大内存
                // 判断运行结果
                Error error = Error.fromCode(commandResult.getError());
                if (error != Error.SUCCESS) {
                    judgeResult.setStatus(error.getCode());
                    judgeResult.setMessage(error.getErrorMessage());
                    judgeResult.setFirstErrorCaseInput(IoUtil.readUtf8(new FileInputStream(testcase.getInputPath()))); // 记录第一个错误用例的输入数据
                    return judgeResult;
                }

                // 读取程序输出并对比结果
                Result result = Result.fromCode(commandResult.getResult());
                if (result != Result.SUCCESS) {
                    // 错误用例信息
                    judgeResult.setStatus(result.getCode());
                    judgeResult.setMessage(result.getMessage());
                    judgeResult.setFirstErrorCaseInput(IoUtil.readUtf8(new FileInputStream(testcase.getInputPath())));
                    if (result != Result.RUNTIME_ERROR) judgeResult.setFirstErrorYueOutput(IoUtil.readUtf8(new FileInputStream(commandArgs.getRealOutputPath(0))));
                    else judgeResult.setFirstErrorYueOutput(IoUtil.readUtf8(new FileInputStream(commandArgs.getRealErrorPath(0))));
                    return judgeResult;
                }
                // 读取程序输出
                String output = IoUtil.readUtf8(new FileInputStream(commandArgs.getRealOutputPath(i)));
                // 读取正确输出
                String correctOutput = IoUtil.readUtf8(new FileInputStream(testcase.getOutputPath()));
                // 对比结果
                if (!output.equals(correctOutput)) {
                    judgeResult.setStatus(Result.WRONG_ANSWER.getCode());
                    judgeResult.setMessage(Result.WRONG_ANSWER.getMessage());
                    judgeResult.setFirstErrorCaseInput(IoUtil.readUtf8(new FileInputStream(testcase.getInputPath())));
                    judgeResult.setFirstErrorYueOutput(output);
                    return judgeResult;
                }
                // 如果通过，累积通过数
                judgeResult.setAcCaseCount(judgeResult.getAcCaseCount() + 1);
            }

            // 所有测试用例通过
            judgeResult.setStatus(Result.SUCCESS.getCode());
            judgeResult.setMessage(Result.SUCCESS.getMessage());
            return judgeResult;

        } catch (Exception e) {
            judgeResult.setStatus(Result.SYSTEM_ERROR.getCode());
            judgeResult.setMessage(e.getMessage());
            return judgeResult;

        } finally {
            compiler.deStory(buildName);
            FileUtil.del(realOutputPath);
            FileUtil.del(realErrorPath);
        }
    }
}
