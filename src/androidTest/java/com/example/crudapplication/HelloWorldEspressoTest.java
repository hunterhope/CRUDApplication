package com.example.crudapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HelloWorldEspressoTest {
    private IdlingResource mIdlingResource;


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void registerIdlingResource() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                mIdlingResource = activity.getIdlingResource();
                // To prove that the test fails, omit this call:
                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void basic_espresso_usage_test() {
        //測試啟動後,畫面有下面字串顯示
        onView(withText("This is home fragment")).check(matches(isDisplayed()));

        //測試 下方導航按鈕dashboard按下,會出現 This is dashboard fragment
        onView(withId(R.id.navigation_c)).perform(click());
        onView(withText("This is dashboard fragment")).check(matches(isDisplayed()));
        //測試 下方導航按鈕Notifications按下,會出現 This is notifications fragment
//        onView(withId(R.id.navigation_notifications)).perform(click());
//        onView(withText("This is notifications fragment")).check(matches(isDisplayed()));
        //測試 下方導航按鈕home按下,會出現 This is home fragment
        onView(withId(R.id.navigation_r)).perform(click());
        onView(withText("This is home fragment")).check(matches(isDisplayed()));

    }
}
