package com.example.crudapplication.ui.result;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EmployeeUiState {
    private final Long id;
    private final String name;
    private final Integer age;
    private final String phone;
    private boolean selected;
    private final boolean click;
    private final BiConsumer<Long,Boolean> deleteChockBoxAction;
    private final Consumer<EmployeeUiState> updateAction;

    public EmployeeUiState(Long id, String name, Integer age, String phone,boolean selected, boolean click, BiConsumer<Long, Boolean> deleteChockBox, Consumer<EmployeeUiState> updateAction) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.selected = selected;
        this.click = click;
        this.deleteChockBoxAction =deleteChockBox;
        this.updateAction = updateAction;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeUiState that = (EmployeeUiState) o;
        return selected == that.selected && click == that.click && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(age, that.age) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, phone, selected, click);
    }

    public void idClick(boolean click){
        this.selected=click;
        deleteChockBoxAction.accept(id,click);
    }

    public void update() {
        updateAction.accept(this);
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isClick() {
        return click;
    }

    @Override
    public String toString() {
        return "EmployeeUiState{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", selected=" + selected +
                ", click=" + click +
                '}';
    }
}
