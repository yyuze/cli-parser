package yyuze.handler;

import org.yyuze.anno.Action;
import org.yyuze.bean.BaseArgumentsBean;
import org.yyuze.bean.ExecuteResult;
import org.yyuze.handler.BaseHandler;
import yyuze.bean.args.DemoArgumentsBean;

/**
 * @Author: yanghanbo
 * @Date: 6/24/2020 12:20 PM
 */
@Action(name = "demo_action_name", abbreviation = "d", full = "demo", description = "this is a demo action")
public class DemoHandler extends BaseHandler {


    public <T extends BaseArgumentsBean> ExecuteResult execute(T argsBean) {
        /**
         * put your process code here
         */
        System.out.println(((DemoArgumentsBean) argsBean).getTest());
        System.out.println(((DemoArgumentsBean) argsBean).getTest2());
        System.out.println(((DemoArgumentsBean)argsBean).getTest3());
        return this.res;
    }
}
