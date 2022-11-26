package com.example.crudapplication.ui.result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.crudapplication.R;
import com.example.crudapplication.db.entity.Employee;
import com.example.crudapplication.repository.EmployeeRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ResultViewModel extends ViewModel {
    private final EmployeeRepository employeeRepository;
    private List<EmployeeUiState> employeeUiStates;
    private final Set<Long> deleteData;
    private MutableLiveData<Boolean> deleteBtnVisible;
    private final BiConsumer<Long, Boolean> deleteChoiceAction;
    private final Consumer<EmployeeUiState> updateAction;
    private final MutableLiveData<EmployeeUiState> updateData;
    private final MutableLiveData<Integer> scrollTo;
    private Long clickId;
    private final MutableLiveData<Integer> toastId;
    @Inject
    public ResultViewModel(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        deleteData = new HashSet<>();
        updateData=new MutableLiveData<>();
        scrollTo=new MutableLiveData<>();
        toastId=new MutableLiveData<>();
        deleteChoiceAction = (id, clicked) -> {
            if (clicked) {
                deleteData.add(id);
            } else {
                deleteData.remove(id);
            }
            System.out.println("deleteBtnVisible="+(!deleteData.isEmpty()));
            deleteBtnVisible.setValue(!deleteData.isEmpty());
        };
        updateAction= updateData::setValue;
    }

    public LiveData<List<EmployeeUiState>> getAllEmployee() {
        return Transformations.map(employeeRepository.getAll(), input -> {
            System.out.println("收到DB資料");
            System.out.println(input);
                    employeeUiStates = input.stream()
                            .map(e ->{
                                return new EmployeeUiState(
                                        e.id,
                                        e.name,
                                        e.age,
                                        e.phone,
                                        deleteData.contains(e.id),
                                        Objects.equals(e.id, clickId),
                                        deleteChoiceAction, updateAction);
                            } )
                            .collect(Collectors.toList());
                    return employeeUiStates;
                }
        );
    }

    public LiveData<Boolean> getDeleteBtnVisible() {
        if (deleteBtnVisible == null) {
            deleteBtnVisible = new MutableLiveData<>(!deleteData.isEmpty());
        }
        return deleteBtnVisible;
    }

    public void deleteUserSelectData() {
        employeeRepository.delete(deleteData)
                .whenComplete((success, throwable) -> {
                    if (throwable == null) {
                        deleteData.clear();
                        deleteBtnVisible.postValue(false);
                    }
                });
    }

    public LiveData<EmployeeUiState> getUpdateData() {
        return updateData;
    }

    public void updateOrCreateEmployee(String name, String age, String phone) {
        //不可以為空的
        if(inputCheckError(name,age,phone)){
            return;
        }


        EmployeeUiState employeeUiState=updateData.getValue();
        if(employeeUiState==null){
            employeeRepository.createEmployee(new Employee(name,Integer.valueOf(age),phone))
                    .whenComplete((aLong, throwable) -> {
                        clickId=aLong;
                        scrollTo.postValue(0);
                    });
        }else{
            clickId=employeeUiState.getId();
            employeeRepository.updateEmployee(new Employee(employeeUiState.getId(),name,Integer.valueOf(age),phone))
                    .whenComplete((aBoolean, throwable) ->{
                        int index = employeeUiStates.indexOf(employeeUiState);
                        scrollTo.postValue(index);
                        updateData.postValue(null);
                    } );
        }
    }

    private boolean inputCheckError(String name, String age, String phone) {
        if(name.isEmpty() || age.isEmpty() || phone.isEmpty()){
            toastId.setValue(R.string.empty_reeor);
            return true;
        }
        return false;
    }

    public void createEmployee() {
        updateData.setValue(null);
    }

    public LiveData<Integer> getScrollTo() {
        return scrollTo;
    }

    public LiveData<Integer> getToastId(){
        return toastId;
    }

    public void msgHasShow() {
        toastId.setValue(null);
    }

    public List<EmployeeUiState> getAllEmployeeStates() {
        return employeeUiStates;
    }
}