package prateek_gupta.SampleProject.base;

public class Context {

    /**
     * Field to store the Tenant details in a Thread safe way
     */
    private static final ThreadLocal<Context> currentContext =
            new InheritableThreadLocal<>();

    public Integer userId;

    public Context(Integer userId) {
        this.userId = userId;
    }

    public static void setCurrentContext(Integer userId){
        currentContext.set(new Context(userId));
    }

    public static Context getCurrentContext(){
        return currentContext.get();
    }
}
