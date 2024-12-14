package com.yibu.yibuJudge.actuator;

import com.yibu.yibuJudge.exceptions.CompilerException;
import com.yibu.yibuJudge.model.cmd.CommandArgs;
import com.yibu.yibuJudge.model.cmd.CommandResult;

import java.util.List;

public interface Compiler {
    String build(String codePath, boolean rmSource) throws CompilerException;
    List<CommandResult> run(CommandArgs args);
    void deStory(String codePath);
    String saveCode(String code);
}
