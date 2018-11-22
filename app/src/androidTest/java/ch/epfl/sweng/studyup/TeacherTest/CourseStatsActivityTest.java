package ch.epfl.sweng.studyup.TeacherTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.CourseStatsActivity;
import ch.epfl.sweng.studyup.teacher.DisplayCourseStatsActivity;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class CourseStatsActivityTest {

    @Rule
    public final ActivityTestRule<CourseStatsActivity> mActivityRule =
            new ActivityTestRule<>(CourseStatsActivity.class);

    @BeforeClass
    public static void init() {
        Intents.init();
    }

    @AfterClass
    public static void clean() {
        Intents.release();
    }

    @Test
    public void singleCourseTest(){
        cleanAndAdd();
        onView(withId(R.id.listViewCourses)).check(matches(Matchers.withListSize(1)));
    }

    @Test
    public void onClickTest(){
        cleanAndAdd();
        onData(anything()).inAdapterView(withId(R.id.listViewCourses)).atPosition(0).perform(click());
        Intents.intended(hasComponent(DisplayCourseStatsActivity.class.getName()));
    }

    private void cleanAndAdd(){
        List<Constants.Course> courseList = new ArrayList<>();
        courseList.add(Constants.Course.Blacksmithing);
        Player.get().setCourses(courseList);
        //reload activity
        mActivityRule.launchActivity(new Intent());
        Utils.waitAndTag(500, "waiting for the display to be drawn");
    }

    static class Matchers {
        public static Matcher<View> withListSize (final int size) {
            return new TypeSafeMatcher<View>() {
                @Override public boolean matchesSafely (final View view) {
                    return ((ListView) view).getCount () == size;
                }

                @Override public void describeTo (final Description description) {
                    description.appendText ("ListView should have " + size + " items");
                }
            };
        }
    }

}
