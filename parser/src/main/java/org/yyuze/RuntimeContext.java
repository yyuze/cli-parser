package org.yyuze;

import org.yyuze.anno.Argument;
import org.yyuze.anno.proc.DefinationProcessor;
import org.yyuze.bean.BaseArgumentsBean;
import org.yyuze.exception.ActionInputException;
import org.yyuze.handler.enable.CommandHandleable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * @Author: yanghanbo
 * @Date: 6/28/2020 3:53 PM
 */
class RuntimeContext {

    private DefinationProcessor definationProcessor;

    RuntimeContext(Class<?> runtimeClass) {
        this.definationProcessor = new DefinationProcessor(runtimeClass);
    }

    /**
     * 解析输入动作参数对应的Option对象
     */
    private Option getActionArgumentOption(String[] args) throws ActionInputException {
        String actArg = this.getActionArgument(args);
        return this.definationProcessor.getActionOption(actArg);
    }

    /**
     * 解析输入动作参数对应的变量Options对象
     */
    private Options getActionArgumentOptions(String[] args) throws ActionInputException {
        String actArg = this.getActionArgument(args);
        return this.definationProcessor.getActionOptions(actArg);
    }

    /**
     * 解析输入的命令行参数得到对应的动作handler实例
     */
    CommandHandleable getHandler(String[] args) throws ActionInputException {
        String actArg = this.getActionArgument(args);
        return this.definationProcessor.getHandler(actArg);
    }

    /**
     * 解析输入的命令行参数获取对应的bean实例
     */
    <T extends BaseArgumentsBean> T getBean(String[] args)
        throws ActionInputException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        String actArg = this.getActionArgument(args);
        Options opts = new Options();
        opts.addOption(this.getActionArgumentOption(args));
        this.getActionArgumentOptions(args).getOptions().forEach(opts::addOption);
        CommandLine cmd = new DefaultParser().parse(opts, args);
        Class<?> beanClass = this.definationProcessor.getActionArgumentsClass(actArg);
        Object bean = beanClass.getConstructor().newInstance();
        Arrays.stream(beanClass.getDeclaredFields())
            .filter(field -> field.getAnnotation(Argument.class) != null)
            .forEach(field -> {
                field.setAccessible(true);
                Argument anno = field.getAnnotation(Argument.class);
                if (anno.hasArgument()) {
                    String[] values = !anno.full().equals("") ? cmd.getOptionValues(anno.full())
                        : cmd.getOptionValues(anno.abbreviation());
                    try {
                        if (values == null) {
                            field.set(bean, null);
                        }
                        else {
                            field.set(bean, String.join(" ", values));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        if (cmd.hasOption(anno.full()) || cmd.hasOption(anno.abbreviation())) {
                            field.set(bean, "true");
                        }
                        else {
                            field.set(bean, "false");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            });
        return (T) bean;
    }

    /**
     * 从命令行传入参数中解析动作参数
     */
    private String getActionArgument(String[] args) throws ActionInputException {
        List<String> actArg = Arrays.stream(args)
            .filter(arg -> {
                boolean flag = arg.startsWith("--") || arg.startsWith("-");
                if (arg.startsWith("--")) {
                    flag &= this.definationProcessor.avaliableActionArgs().contains(arg.substring(2));
                }
                else if (arg.startsWith("-")) {
                    flag &= this.definationProcessor.avaliableActionArgs().contains(arg.substring(1));
                }
                else {
                    flag = false;
                }
                return flag;
            })
            .map(arg -> arg.startsWith("--") ? arg.substring(2) : arg.substring(1))
            .collect(Collectors.toList());
        if (actArg.size() != 1) {
            throw new ActionInputException();
        }
        return actArg.get(0);
    }

    Map<String, Options> getActionArg2OptionsMap() {
        Map<String, Options> map = new HashMap<>();
        Set<Options> alreadyIn = new HashSet<>();
        this.definationProcessor.avaliableActionArgs().forEach(actArg -> {
            Options opts = this.definationProcessor.getActionOptions(actArg);
            if (!alreadyIn.contains(opts)) {
                map.put(actArg, opts);
                alreadyIn.add(opts);
            }
        });
        return map;
    }

}
