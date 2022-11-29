package com.example.crudapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.crudapplication.ui.read.EmployeeUiState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    /*
    *第一個需求測試
    * 打開App看到所有後台員工資料在一個recycle view 裡面
    * 在Espresso裡要找到list adapter的view要用onData()來找
    * */
    @Test
    public void appOpenSeeTheAllEmployeesTest() {
        //測試打開畫面是否有出現EmployeeUiState,預計至少會有一個欄位是字串,所以就檢查有沒有任何字串出現
        onData(allOf(is(instanceOf(EmployeeUiState.class)))).check(matches(withText(any(String.class))));
    }
}
