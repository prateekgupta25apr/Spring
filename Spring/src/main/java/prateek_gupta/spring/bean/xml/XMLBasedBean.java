package prateek_gupta.spring.bean.xml;

import lombok.Getter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Getter

public class XMLBasedBean {
    String beanName;

    XMLBasedSubBean xmlBasedSubBean;

    // For Getter Setter Based
    public XMLBasedBean() {
        System.out.println("Default Constructor called");
    }

    public void setBeanName(String beanName) {
        System.out.println("Setter for beanName called");
        this.beanName = beanName;
    }

    public void setXmlBasedSubBean(XMLBasedSubBean xmlBasedSubBean) {
        System.out.println("Setter for xmlBasedSubBean called");
        this.xmlBasedSubBean = xmlBasedSubBean;
    }

    // For Constructor Based
    public XMLBasedBean(String beanName,
                        XMLBasedSubBean xmlBasedSubBean) {
        System.out.println("Parameterised Constructor called");
        this.beanName = beanName;
        this.xmlBasedSubBean = xmlBasedSubBean;
    }

    public static void main(String[] args) {
        System.out.println("#####Getter Setter Based(With Default Constructor)#####");
        XMLBasedBean getterSetterBasedXMLBasedBean =
                (XMLBasedBean) new ClassPathXmlApplicationContext(
                        "GetterSetter.xml")
                .getBean("getterSetterBasedXMLBasedBean");
        System.out.println("Value for beanName : "+getterSetterBasedXMLBasedBean.beanName);
        System.out.println("Value for subBeanName : "+
                getterSetterBasedXMLBasedBean.xmlBasedSubBean.subBeanName);
        System.out.println("##########");

        System.out.println();

        System.out.println("#####Constructor Based#####");
        XMLBasedBean constructorBasedXMLBasedBean =
                (XMLBasedBean) new ClassPathXmlApplicationContext(
                "Constructor.xml")
                .getBean("constructorBasedXMLBasedBean");
        System.out.println("Value for beanName : "+constructorBasedXMLBasedBean.beanName);
        System.out.println("Value for subBeanName : "+
                constructorBasedXMLBasedBean.xmlBasedSubBean.subBeanName);
        System.out.println("##########");

        System.out.println();

        System.out.println("#####Constructor Autowired#####");
        XMLBasedBean constructorAutowiredXMLBasedBean =
                (XMLBasedBean) new ClassPathXmlApplicationContext(
                "ConstructorAutowired.xml")
                .getBean("constructorAutowiredBasedBean");
        System.out.println("Value for beanName : "+constructorAutowiredXMLBasedBean.beanName);
        System.out.println("Value for subBeanName : "+
                constructorAutowiredXMLBasedBean.xmlBasedSubBean.subBeanName);
        System.out.println("##########");

        System.out.println();

        System.out.println("#####By Name Autowired#####");
        XMLBasedBean byNameAutowiredBasedXMLBasedBean =
                (XMLBasedBean) new ClassPathXmlApplicationContext(
                "ByNameAutowired.xml")
                .getBean("byNameAutowiredBasedXMLBasedBean");
        System.out.println("Value for beanName : "+
                byNameAutowiredBasedXMLBasedBean.beanName);
        System.out.println("Value for subBeanName : "+
                byNameAutowiredBasedXMLBasedBean.xmlBasedSubBean.subBeanName);
    }
}