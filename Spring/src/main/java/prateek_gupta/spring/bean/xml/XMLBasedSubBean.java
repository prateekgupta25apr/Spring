    package prateek_gupta.spring.bean.xml;

    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class XMLBasedSubBean {
        String subBeanName;

        // For Getter Setter Based
        public XMLBasedSubBean() {
        }

        // For Constructor Based
        public XMLBasedSubBean(String subBeanName) {
            this.subBeanName = subBeanName;
        }
    }

