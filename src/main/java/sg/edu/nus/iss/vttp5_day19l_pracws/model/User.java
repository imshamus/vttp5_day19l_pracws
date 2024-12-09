package sg.edu.nus.iss.vttp5_day19l_pracws.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class User {

    @NotEmpty(message = "Full name must not be empty.")
    private String fullName;

    @NotNull(message = "Age must not be null.")
    @Min(value = 1, message = "Age must be greater than 0.")
    private Integer age;

    public User() {
    }

    public User(String fullName, Integer age) {
        this.fullName = fullName;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return fullName + "," + age;
    }
}
