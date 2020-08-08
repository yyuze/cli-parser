package org.yyuze.exception;

/**
 * @Author: yanghanbo
 * @Date: 6/28/2020 10:42 AM
 */
public class ActionMutipleDefinedException extends Exception{

    private final String className;

    private final String actionName;

    public ActionMutipleDefinedException(String className, String actionName){
        super();
        this.className = className;
        this.actionName = actionName;
    }

    @Override
    public String getMessage() {
        return "Action argument " + this.actionName + " is multiple defined in " + this.className;
    }
}
