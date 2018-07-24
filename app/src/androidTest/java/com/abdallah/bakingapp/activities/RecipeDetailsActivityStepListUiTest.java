package com.abdallah.bakingapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.models.recipe.Ingredient;
import com.abdallah.bakingapp.models.recipe.Recipe;
import com.abdallah.bakingapp.models.recipe.Step;
import com.google.android.exoplayer2.ui.PlayerView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityStepListUiTest {

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule
            = new ActivityTestRule<>(RecipeDetailsActivity.class, true, false);

    @Test
    public void recipeStepListUiTest() {

        Intent intent = new Intent();
        ArrayList<Step> stepList = new ArrayList<>();
        stepList.add(new Step(0, "Recipe Introduction", "Recipe Introduction"
                , "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4"
                , ""));
        Recipe recipe = new Recipe(1, "Nutella Pie", 8, ""
                , new ArrayList<Ingredient>(), stepList);
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, Parcels.wrap(recipe));
        mActivityTestRule.launchActivity(intent);

        ViewInteraction stepsRecyclerView = onView(
                allOf(withId(R.id.rv_steps),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1)));
        stepsRecyclerView.perform(actionOnItemAtPosition(0, click()));

        int orientation =  mActivityTestRule.getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewInteraction titleTextView = onView(
                    allOf(
                            isAssignableFrom(TextView.class),
                            withParent(isAssignableFrom(Toolbar.class))));
            titleTextView.check(matches(withText("Recipe Introduction")));
        }
        else {
            onView(allOf(isAssignableFrom(PlayerView.class))).check(matches(isDisplayed()));
        }

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}


