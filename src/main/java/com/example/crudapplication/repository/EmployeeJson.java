package com.example.crudapplication.repository;

public class EmployeeJson {
    private Long id;
    private String name;
    private Integer age;
    private String cellPhone;

    public EmployeeJson() {
    }

    public EmployeeJson(Long id, String name, Integer age, String cellPhone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.cellPhone = cellPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    @Override
    public String toString() {
        return "EmployeeJson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", cellPhone='" + cellPhone + '\'' +
                '}';
    }
}
