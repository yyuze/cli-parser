package org.yyuze.handler.enable;

import org.yyuze.bean.BaseArgumentsBean;
import org.yyuze.bean.ExecuteResult;

/**
 * @Author: yanghanbo
 * @Date: 6/20/2020 6:38 PM
 */

/**
 * 执行命令行的使能
 */
public interface CommandHandleable {

    <T extends BaseArgumentsBean> ExecuteResult execute(T argsBean);

}
