package prateek_gupta.sample_project.ioc_containers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
        Resource resource=new ClassPathResource("beans.xml");
        BeanFactory factory=new XmlBeanFactory(resource);
        Bean getterSetterBasedBean= (Bean) factory.getBean("getterSetterBasedBean");
        System.out.println("Getter Setter Based");
        System.out.println(getterSetterBasedBean.param1);
        System.out.println(getterSetterBasedBean.subBean.param);

        Bean constructorBasedBean= (Bean) factory.getBean("constructorBasedBean");
        System.out.println("Constructor Based");
        System.out.println(constructorBasedBean.param1);
        System.out.println(constructorBasedBean.subBean.param);
    }
}
