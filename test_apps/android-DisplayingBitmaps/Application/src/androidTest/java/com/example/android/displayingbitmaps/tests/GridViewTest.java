package com.example.android.displayingbitmaps.tests;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.android.displayingbitmaps.provider.Images;
import com.example.android.displayingbitmaps.ui.ImageGridActivity;
import com.example.android.displayingbitmaps.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GridViewTest {
    @Rule
    public ActivityTestRule<ImageGridActivity> activityRule = new ActivityTestRule<>(ImageGridActivity.class);

    @Test
    public void scrollTopToDown() {
        onView(withId(R.id.gridView)).check(matches(isDisplayed()));
        //clearCache();
        for (int i = 0; i < Images.imageThumbUrls.length; i++) {
            scrollToUrl(Images.imageThumbUrls[i]).perform(click());
            pressBack();
        }
    }

    public void clearCache() {
        // This is flaky
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText("Clear Caches")).perform(click());
    }

    ViewInteraction scrollToUrl(String url) {
        return onData(is(url)).check(matches(isDisplayed()));
    }

}