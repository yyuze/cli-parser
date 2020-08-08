package yyuze.bean.args;

import org.yyuze.anno.Argument;
import org.yyuze.anno.Arguments;
import org.yyuze.bean.BaseArgumentsBean;

/**
 * @Author: yanghanbo
 * @Date: 6/24/2020 12:19 PM
 */

@Arguments(action = "demo_action_name")
public class DemoArgumentsBean extends BaseArgumentsBean {

    @Argument(abbreviation = "a", full = "test_a", hasArgument = true, argumentName = "argument_name", description = "this is a demo arg")
    private String test;

    @Argument(abbreviation = "b", hasArgument = true, required = true, numberOfArgs = 3)
    private String test2;

    @Argument(full = "c")
    private String test3;

    public String getTest() {
        return this.test;
    }

    public String getTest2() {
        return test2;
    }

    public String getTest3() {
        return test3;
    }

}
