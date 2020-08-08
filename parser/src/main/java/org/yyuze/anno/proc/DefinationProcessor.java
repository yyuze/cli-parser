package org.yyuze.anno.proc;

import org.yyuze.anno.Action;
import org.yyuze.anno.Argument;
import org.yyuze.anno.Arguments;
import org.yyuze.bean.BaseArgumentsBean;
import org.yyuze.exception.ActionMutipleDefinedException;
import org.yyuze.exception.ArgumentsDefinationException;
import org.yyuze.handler.enable.CommandHandleable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.Options;

/**
 * @Author: yanghanbo
 * @Date: 6/20/2020 4:47 PM
 */

/**
 * 预定义注解的处理器
 */
public class DefinationProcessor {

    private Class<?> runtimeClass;

    private List<Class<?>> allClasses;

    /**
     * argument name (包括缩写和全写)与 action name 的map
     */
    private Map<String, String> actionArgumentName2ActionName;

    /**
     * action name 与 动作参数的Option对象 的map
     */
    private Map<String, Option> actionName2Option;

    /**
     * action name 与 Options 的map
     */
    private Map<String, Options> actionName2Options;

    /**
     * action name 与 Handler 的 map
     */
    private Map<String, CommandHandleable> actionName2Handler;

    /**
     * action name 与定义了 arguments 参数的类的 map
     */
    private Map<String, Class<? extends BaseArgumentsBean>> actionName2ArgumentsClass;

    public Class<? extends BaseArgumentsBean> getActionArgumentsClass(String actArg){
        String actName = this.actionArgumentName2ActionName.get(actArg);
        return this.actionName2ArgumentsClass.get(actName);
    }

    public Option getActionOption(String actArg){
        String actName = this.actionArgumentName2ActionName.get(actArg);
        return this.actionName2Option.get(actName);
    }

    public Options getActionOptions(String actArg){
        String actName = this.actionArgumentName2ActionName.get(actArg);
        return this.actionName2Options.get(actName);
    }

    public CommandHandleable getHandler(String actArg){
        String actName = this.actionArgumentName2ActionName.get(actArg);
        return this.actionName2Handler.get(actName);
    }

    public Set<String> avaliableActionArgs(){
        return this.actionArgumentName2ActionName.keySet();
    }

    public DefinationProcessor(Class<?> runtimeClass) {
        this.runtimeClass = runtimeClass;
        try {
            this.allClasses = (List<Class<?>>) this.loadClasses();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
        this.initMaps();
    }

    /**
     * 为增强结构性将每个映射的初始化逻辑分开
     */
    private void initMaps(){
        this.initActionArgumentName2ActionName();
        this.initActionName2Option();
        this.initActionName2Options();
        this.initActionName2Handler();
        this.initActionName2ArgumentsClass();
    }

    /**
     * 找出有 @Action 注解的类，将命令行参数名(abbr & full) 与 动作名(name) 作映射
     */
    private void initActionArgumentName2ActionName() {
        this.actionArgumentName2ActionName = new HashMap<>();
        this.allClasses.stream()
            .filter(clz -> clz.getAnnotation(Action.class) != null)
            .forEach(clz -> {
                Action anno = clz.getAnnotation(Action.class);
                String abbrArgName = anno.abbreviation();
                String fullArgName = anno.full();
                String actionName = anno.name();
                if (!abbrArgName.equals("")) {
                    this.actionArgumentName2ActionName.put(abbrArgName, actionName);
                }
                if (!fullArgName.equals("")) {
                    this.actionArgumentName2ActionName.put(fullArgName, actionName);
                }
            });
    }

    /**
     * 找出有 @Action 注解的类，将 动作名(name) 与 对应的动作Option实例 作映射
     */
    private void initActionName2Option() {
        this.actionName2Options = new HashMap<>();
        this.actionName2Option = new HashMap<>();
        this.allClasses.stream()
            .filter(clz -> clz.getAnnotation(Action.class) != null)
            .forEach(clz -> {
                Action anno = clz.getAnnotation(Action.class);
                String actionName = anno.name();
                Option opt = buildOptionFromAnno(anno);
                this.actionName2Option.put(actionName, opt);
            });

    }

    /**
     * 找出有 @Arguments 注解的类，将域中的 @Argument 注解实例化为Option对象并放入Options对象中 将 动作名(action) 与 Options 对象作映射
     */
    private void initActionName2Options() {
        this.actionName2Handler = new HashMap<>();
        this.allClasses.stream()
            .filter(clz -> clz.getAnnotation(Arguments.class) != null)
            .forEach(clz -> {
                String actionName = clz.getAnnotation(Arguments.class).action();
                Options opts = new Options();
                Arrays.stream(clz.getDeclaredFields())
                    .filter(field -> field.getAnnotation(Argument.class) != null)
                    .forEach(field -> {
                        Argument anno = field.getAnnotation(Argument.class);
                        Option opt = buildOptionFromAnno(anno);
                        opts.addOption(opt);
                    });
                this.actionName2Options.put(actionName, opts);
            });
    }

    /**
     * 找出有 @Action 注解的类将其实例化，并且与 动作名(action) 作映射
     */
    private void initActionName2Handler() {
        this.actionName2ArgumentsClass = new HashMap<>();
        this.allClasses.stream()
            .filter(clz -> clz.getAnnotation(Action.class) != null)
            .forEach(clz -> {
                try {
                    String actionName = clz.getAnnotation(Action.class).name();
                    if (this.actionName2Handler.containsKey(actionName)) {
                        throw new ActionMutipleDefinedException(clz.getName(), actionName);
                    }
                    CommandHandleable handler = (CommandHandleable) clz.getConstructor().newInstance();
                    this.actionName2Handler.put(actionName, handler);
                } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException
                    | ActionMutipleDefinedException e) {
                    e.printStackTrace();
                }
            });
    }

    /**
     * 找出有 @Arguments 注解的类，将其与 动作名(action) 作映射
     */
    private void initActionName2ArgumentsClass() {
        this.allClasses.stream()
            .filter(clz -> clz.getAnnotation(Arguments.class) != null)
            .forEach(clz -> {
                String actionName = clz.getAnnotation(Arguments.class).action();
                if (this.actionName2ArgumentsClass.containsKey(actionName)) {
                    try {
                        throw new ArgumentsDefinationException(actionName, ArgumentsDefinationException.MULTIPLE_DEF);
                    } catch (ArgumentsDefinationException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    if (clz.getSuperclass().equals(BaseArgumentsBean.class)) {
                        this.actionName2ArgumentsClass.put(actionName, (Class<? extends BaseArgumentsBean>) clz);
                    }
                }
            });
    }

    /**
     * 加载rootPath下全部.class文件的全路径
     */
    private static final String JAVA_CLASS_FILE_POSTFIX = ".class";

    private void collectClassFile(Path rootPath, Collection<Path> collection) {
        String[] content = rootPath.toFile().list();
        if (content == null || content.length == 0) {
            return;
        }
        Arrays.stream(content).forEach(fileName -> {
            Path abPath = Paths.get(Paths.get(rootPath.toUri()).toString(), fileName);
            if (abPath.toString().endsWith(JAVA_CLASS_FILE_POSTFIX)) {
                collection.add(abPath);
            }
            if (abPath.toFile().isDirectory()) {
                collectClassFile(abPath, collection);
            }
        });
    }

    /**
     * 加载classpath下的全部类
     *
     * @return 包含了全部Class对象的集合
     */
    private Collection<Class<?>> loadClasses()
        throws URISyntaxException, MalformedURLException {
        List<Class<?>> clzes = new ArrayList<>();
        /*
        获取当前运行路径
         */
        String filePath = this.runtimeClass.getProtectionDomain().getCodeSource().getLocation().getPath();
        /*
        如果当前路径为jar包，则从jar包中获取.class
         */
        if (filePath.endsWith(".jar")) {
            try (JarFile jar = new JarFile(filePath)) {
                Enumeration<JarEntry> enums = jar.entries();
                while (enums.hasMoreElements()) {
                    JarEntry entry = enums.nextElement();
                    if (entry.getName().endsWith(JAVA_CLASS_FILE_POSTFIX)) {
                        String className = entry.getName()
                            .replace("/", ".")
                            .replace("\\", ".")
                            .replace(JAVA_CLASS_FILE_POSTFIX, "");
                        clzes.add(Class.forName(className));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        /*
        如果当前路径为文件系统目录，则从文件系统中获取.class
         */
        else {
            Path rootPath = Paths.get(new URL("file:" + filePath).toURI());
            List<Path> clzPaths = new ArrayList<>();
            collectClassFile(rootPath, clzPaths);
            clzPaths.forEach(clzPath -> {
                String clzName = clzPath.toString()
                    .replace(rootPath.toString(), "")
                    .replace("/", ".")
                    .replace("\\", ".")
                    .replace(JAVA_CLASS_FILE_POSTFIX, "")
                    .substring(1);
                try {
                    clzes.add(Class.forName(clzName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        return clzes;
    }

    /**
     * 获取注释中的属性，构造 Option 实例
     *
     * @return Argument 实例
     */
    private Option buildOptionFromAnno(Argument anno) {
        String abbr = anno.abbreviation();
        String full = anno.full();
        boolean hasArg = anno.hasArgument();
        String argName = anno.argumentName();
        String desc = anno.description();
        boolean required = anno.required();
        int numberOfArgs = anno.numberOfArgs();

        Builder builder = abbr.equals("") ? Option.builder() : Option.builder(abbr);
        builder.required(required);
        if (!full.equals("")) {
            builder.longOpt(full);
        }
        if (hasArg) {
            builder.hasArg(true);
            builder.numberOfArgs(numberOfArgs);
            if (!argName.equals("")) {
                builder.argName(argName);
            }
        }
        builder.desc(desc);
        return builder.build();
    }

    /**
     * 获取注释中的属性，构造 Option 实例
     *
     * @return Argument 实例
     */
    private Option buildOptionFromAnno(Action anno) {
        String abbr = anno.abbreviation();
        String full = anno.full();
        String desc = anno.description();
        Builder builder = abbr.equals("") ? Option.builder() : Option.builder(abbr);
        if (!full.equals("")) {
            builder.longOpt(full);
        }
        builder.desc(desc);
        return builder.build();
    }
}
