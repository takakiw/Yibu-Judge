package com.yibu.yibujudge.actuator;

import com.yibu.yibujudge.exceptions.CompilerException;
import com.yibu.yibujudge.model.cmd.CommandArgs;
import com.yibu.yibujudge.model.cmd.CommandResult;

import java.util.List;

public interface Compiler {
    String build(String codePath, boolean rmSource) throws CompilerException;
    List<CommandResult> run(CommandArgs args);
    void deStory(String codePath);
    String saveCode(String code);
}
