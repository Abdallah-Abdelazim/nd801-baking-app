package com.abdallah.bakingapp.activities;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.models.recipe.Ingredient;
import com.abdallah.bakingapp.models.recipe.Recipe;
import com.abdallah.bakingapp.models.recipe.Step;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule
            = new ActivityTestRule<>(RecipeDetailsActivity.class, true, false);

    @Test
    public void recipeDetailsActivityTest() {

        Intent intent = new Intent();
        Recipe recipe = new Recipe(1, "Nutella Pie", 8, ""
                , new ArrayList<Ingredient>(), new ArrayList<Step>());
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, Parcels.wrap(recipe));
        mActivityTestRule.launchActivity(intent);


        ViewInteraction textView = onView(
                allOf(withText("Nutella Pie")));
        textView.check(matches(withText("Nutella Pie")));

    }

}
