package Laptop;

public class OS {
    String brand;

    public OS(String brand) {
        System.out.println("OS constructor called");
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "OS{" +
                "brand='" + brand + '\'' +
                '}';
    }
}
