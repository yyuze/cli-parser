package yyuze.handler;

import org.yyuze.anno.Action;
import org.yyuze.bean.BaseArgumentsBean;
import org.yyuze.bean.ExecuteResult;
import org.yyuze.handler.BaseHandler;

/**
 * @Author: yanghanbo
 * @Date: 7/6/2020 11:08 AM
 */
@Action(name="demo_action_name_2", abbreviation = "d2", full = "demo2")
public class DemoHandler2 extends BaseHandler {

    public <T extends BaseArgumentsBean> ExecuteResult execute(T argsBean) {
        return null;
    }
}
