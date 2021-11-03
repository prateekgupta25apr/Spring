package Laptop;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Laptop {
    String brandName;
    int version;
    OS osName;


    public static void main(String[] args) {
        System.out.println(new
                ClassPathXmlApplicationContext("laptop.xml")
                .getBean("laptop"));
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "brand='" + brandName + '\'' +
                ", version=" + version +
                ", osName=" + osName +
                '}';
    }

    public void setBrandName(String brandName) {
        System.out.println("called");
        this.brandName = brandName;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setOsName(OS os) {
        this.osName = os;
    }
}
