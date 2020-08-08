package org.yyuze.exception;

/**
 * Author: yyuze
 * Time: 6/26/2020
 */
public class ArgumentsDefinationException extends Exception{

    /**
     * action name
     */
    private final String actName;

    /**
     * the number of arguments defination class
     * （ought to be one）
     */
    private final Integer type;

    public static final Integer MULTIPLE_DEF = 0;

    public static final Integer NOT_DEF = 1;

    public ArgumentsDefinationException(String actName, int type){
        super();
        this.actName = actName;
        this.type = type;
    }

    @Override
    public String getMessage() {
        if(type.equals(NOT_DEF)){
            return "Defination class of "+ this.actName + "'s arguments is not found";
        }
        else{
            return "Defination class of "+ this.actName + "'s arguments are multiple";
        }

    }
}
