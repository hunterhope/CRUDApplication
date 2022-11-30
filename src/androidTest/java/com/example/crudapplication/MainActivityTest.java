package com.example.crudapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
    public void appOpenSeeTheAllEmployeesTest() throws InterruptedException {
        //測試打開畫面是否有出現EmployeeUiState,預計至少會有一個欄位是字串,所以就檢查有沒有任何字串出現
        onView(withId(R.id.recyclerView))
                .perform(
                        RecyclerViewActions.scrollToLastPosition()
                ).check(matches(isDisplayed()));
        Thread.sleep(1000);
        onView(withId(R.id.recyclerView))
                .perform(
                        RecyclerViewActions.scrollToPosition(0)
                ).check(matches(isDisplayed()));
    }
}
