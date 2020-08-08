package yyuze.bean.args;

import org.yyuze.anno.Argument;
import org.yyuze.anno.Arguments;
import org.yyuze.bean.BaseArgumentsBean;

/**
 * @Author: yanghanbo
 * @Date: 7/6/2020 11:10 AM
 */
@Arguments(action = "demo_action_name_2")
public class DemoArgumentsBean2 extends BaseArgumentsBean {

    @Argument(abbreviation = "a_2", full = "test_a_2", hasArgument = true, argumentName = "argument_name", description = "this is a demo arg")
    private String test;

    @Argument(abbreviation = "b_2", hasArgument = true, required = true, numberOfArgs = 3)
    private String test2;

    @Argument(full = "c_2")
    private String test3;

}
