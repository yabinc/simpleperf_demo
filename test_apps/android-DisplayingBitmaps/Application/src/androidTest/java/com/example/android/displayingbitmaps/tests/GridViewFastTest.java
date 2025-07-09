package com.example.android.displayingbitmaps.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.android.displayingbitmaps.R;
import com.example.android.displayingbitmaps.provider.Images;
import com.example.android.displayingbitmaps.ui.ImageGridActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GridViewFastTest {
    @Test
    public void scrollTopToDown() {
        ActivityScenario<ImageGridActivity> scenario = ActivityScenario.launch(ImageGridActivity.class);
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
        for (int position = 0; position < 15; position++) {
            scrollToUrlAndClick(position);
            onView(allOf(withId(R.id.imageView), withContentDescription(Images.imageUrls[position]))).check(matches(isDisplayed()));
            pressBack();
        }
    }

    void scrollToUrlAndClick(int position) {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

}
