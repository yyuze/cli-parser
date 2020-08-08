package yyuze;

import org.yyuze.CommandDispacher;

import org.yyuze.exception.ActionInputException;

/**
 * @Author: yanghanbo
 * @Date: 6/24/2020 2:15 PM
 */
public class DemoMain {

    public static void main(String[] args) {
        String[] arguments = {"-d", "-a", "world", "-b", "hello", "my", "world", "--c"};
        try {
            /*
            该方法可打印命令行帮助列表
             */
            new CommandDispacher(DemoMain.class).printHelpList();
            new CommandDispacher(DemoMain.class).dispatch(arguments);
        } catch (ActionInputException e) {
            e.printStackTrace();
        }
    }
}
