package prateek_gupta.sample_project.ioc_containers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubBean {
    String param;

    // For Getter Setter Based
    public SubBean() {
    }

    // For Constructor Based
    public SubBean(String param) {
        this.param = param;
    }
}
