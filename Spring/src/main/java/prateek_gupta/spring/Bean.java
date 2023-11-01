package prateek_gupta.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Getter
@Setter
public class Bean {
    String param1;

    SubBean subBean;

    // For Getter Setter Based
    public Bean() {
    }

    // For Constructor Based
    public Bean(String param1, SubBean subBean) {
        this.param1 = param1;
        this.subBean = subBean;
    }

    public static void main(String[] args) {
        Bean getterSetterBasedBean= (Bean) new ClassPathXmlApplicationContext(
                "beans.xml")
                .getBean("getterSetterBasedBean");
        System.out.println("Getter Setter Based");
        System.out.println(getterSetterBasedBean.param1);
        System.out.println(getterSetterBasedBean.subBean.param);

        Bean constructorBasedBean= (Bean) new ClassPathXmlApplicationContext(
                "beans.xml")
                .getBean("constructorBasedBean");
        System.out.println("Constructor Based");
        System.out.println(constructorBasedBean.param1);
        System.out.println(constructorBasedBean.subBean.param);

        Bean constructorAutowiredBean= (Bean) new ClassPathXmlApplicationContext(
                "beans.xml")
                .getBean("constructorAutowiredBasedBean");
        System.out.println("Constructor Autowired");
        System.out.println(constructorAutowiredBean.param1);
        System.out.println(constructorAutowiredBean.subBean.param);

        Bean byNameAutowiredBasedBean= (Bean) new ClassPathXmlApplicationContext(
                "beans.xml")
                .getBean("byNameAutowiredBasedBean");
        System.out.println("By Name Autowired");
        System.out.println(byNameAutowiredBasedBean.param1);
        System.out.println(byNameAutowiredBasedBean.subBean.param);
    }
}