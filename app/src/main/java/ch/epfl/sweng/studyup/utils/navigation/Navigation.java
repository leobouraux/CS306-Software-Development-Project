package ch.epfl.sweng.studyup.utils.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.settings.SettingsActivity;
import ch.epfl.sweng.studyup.utils.RefreshContext;


/**
 * Navigation class that serve as basic interface for navigation between activities
 *
 */
public abstract class Navigation extends RefreshContext implements ActivityCompat
        .OnRequestPermissionsResultCallback {
    protected ArrayList<Integer> buttonIds = null;
    protected ArrayList<Class> activities = null;
    protected ArrayList<Integer> activitiesId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupNavigation();
    }

    public void navigationSwitcher(final Context cn, final Class<?> activity, final int current_index) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem = menu.getItem(current_index);

        menuItem.setChecked(false); // Give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (int i = 0; i < buttonIds.size(); i++) {   //all three lists should have the same size
                    if (item.getItemId() == buttonIds.get(i) && !activity.equals(activities.get(i))) {
                        Intent intent = new Intent(cn, activities.get(i));
                        cn.startActivity(intent);
                        transitionForNavigation(current_index, activitiesId.get(i));
                    }
                }
                return false;
            }
        });
    }

    public void transitionForNavigation(int current_index, int destination_index) {
        if (destination_index > current_index) {
            overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
        } else if (destination_index < current_index) {
            overridePendingTransition(R.anim.go_left_in, R.anim.go_left_out);
        }
    }

    public void navigationTopToolbar(MenuItem item) {
        if (item.getItemId() == R.id.top_navigation_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        navigationTopToolbar(item);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that set the correct data to buttonIds, activities and activitiesIds
     */
    protected abstract void setupNavigation();
}
