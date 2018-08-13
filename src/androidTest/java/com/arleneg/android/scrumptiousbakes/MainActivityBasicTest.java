package com.arleneg.android.scrumptiousbakes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.arleneg.android.scrumptiousbakes.ui.MainActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickItem() {
        onView(ViewMatchers.withId(R.id.main_activity_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.ingredients_tv))
                .check(matches(isDisplayed()))
                .check(matches(withText("2.0 CUP Graham Cracker crumbs\n" +
                        "6.0 TBLSP unsalted butter, melted\n" +
                        "250.0 G granulated sugar\n" +
                        "1.0 TSP salt\n" +
                        "4.0 TSP vanilla,divided\n" +
                        "680.0 G cream cheese, softened\n" +
                        "3.0 UNIT large whole eggs\n" +
                        "2.0 UNIT large egg yolks\n" +
                        "250.0 G heavy cream\n")));

    }

}
