package org.yyuze;

import org.yyuze.bean.ExecuteResult;
import org.yyuze.ret.ExecuteResultEnum;
import org.yyuze.exception.ActionInputException;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

/**
 * @Author: yanghanbo
 * @Date: 6/23/2020 2:35 PM
 */
public class CommandDispacher {

    private RuntimeContext context;

    public CommandDispacher(Class<?> invocationClass) {
        this.context = new RuntimeContext(invocationClass);
    }

    public void printHelpList() {
        HelpFormatter helpFormatter = new HelpFormatter();
        this.context.getActionArg2OptionsMap().forEach(helpFormatter::printHelp);
    }

    public ExecuteResult dispatch(String[] args) throws ActionInputException {
        try {
            return this.context.getHandler(args).execute(this.context.getBean(args));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ParseException e) {
            e.printStackTrace();
            return new ExecuteResult(ExecuteResultEnum.buildCmdFail(String.join(" ", args)));
        }
    }

}
