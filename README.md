## What's this?
    This is a cli command parser util, which is able to parse command arguments into pojo object  
    immediately. For instance, Suppose you want to process a command like:   
    "magic --defense --word Expecto Patronus --t Lord Voldemort --be-nervoue"
    With this util, you can forget the tedious if/else then getOptionValues() steps, and get a valued
    bean object immediately.
## Quick start
### Step 0: Add maven dependency 
```
    <dependency>
      <groupId>org.yyuze</groupId>
      <artifactId>parser</artifactId>
      <version>1.0</version>
    </dependency>
```
### Step 1: Define an action handler
Use annotation ***@Action*** to define the action('defense' in this example) handler class  
This handler class should be the subclass of ***BaseHandler*** :
```
@Action(name = "act-defense", abbreviation = "d", full = "defense", description = "Cast a defense mana")
public class DefenseHandler extends BaseHandler {

    @Override
    public <T extends BaseArgumentsBean> ExecuteResult execute(T argsBean) {
        /**
         * put your process code here
         */
         DefenseArgumentsBean bean = (DefenseArgumentsBean)argsBean;
         System.out.println("Cast " + bean.getWord() + " on " + bean.getTarget());
         retuen new ExecuteResult();
    }
}
```
### Step 2: Define the corresponding bean class
Use annotation ***@Arguments*** to define bean that contains optional arguments  
Note: value of **action** field should be identical with the value of **name** field in ***@Action*** annotation which is tagged on corresponding handler class  
This class should be the subclass of ***BaseArgumentsBean*** :
```
@Arguments(action = "act-defense")
public class DefenseArgumentsBean extends BaseArgumentsBean {

}
```
### Step 3: Define the arguments properties
Use annotation ***@Argument*** to define the argument details: 
```
@Arguments(action = "act-defense")
public class DefenseArgumentsBean extends BaseArgumentsBean {

    @Argument(abbreviation = "w", full = "word", hasArgument = true, required = true, description = "Words with magic power")
    private String word;
    
    @Argument(abbreviation = "t", full = "target", hasArgument = true, argumentName = "target", description = "Mana target")
    private String target;

    @Argument(full = "be-nervous", description = "Are you feeling good?")
    private String nervous;
    
    public String getWord() {
        return this.word;
    }

    public String getTarget() {
        return this.target;
    }

    public String getNervous() {
        return this.nervous;
    }
}
```
### Step 4: Define Main class
Use **CommandDispacher.dispatch(String[] args)** to execute
```
public class Magic {

    public static void main(String[] args) {
        String[] arguments = {"--defense", "--word", "Expecto Patronus", "--t", "Lord Voldemort", "--be-nervoue"};
        try {
            new CommandDispacher(Magic.class).dispatch(arguments);
            /*
            You also can print help list with the method printHelpList(String commandName)
             */
            new CommandDispacher(Magic.class).printHelpList("magic");
        } catch (ActionInputException e) {
            e.printStackTrace();
        }
    }
}
```
Note that in reality cli service, you may enter command like "magic --defense --word Expecto Patronus --t Lord Voldemort --be-nervoue", just pass parameter 'args' instand of 'arguments' to dispatch() method.   
You can define as many actions as you want to process following the previous 4 steps.
