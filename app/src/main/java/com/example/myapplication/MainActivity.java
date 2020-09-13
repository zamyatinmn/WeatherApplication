package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    EditText writeCity;
    public static boolean humVisCB = true;
    public static boolean windVisCB = true;
    public static boolean darkThemeCB = false;
    public static int cityPos = 0;
    public static ArrayList<String> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (darkThemeCB){
            setTheme(R.style.DarkTheme);
        } else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        writeCity = findViewById(R.id.editText_write_city);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onOptionsItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        TextView city = findViewById(R.id.city);
        switch (id) {
            case R.id.menu_add: {
                if (writeCity.getVisibility() == View.VISIBLE) {
                    if (writeCity.length() > 1){
                        String upperString = writeCity.getText().toString().substring(0, 1)
                                .toUpperCase() + writeCity.getText().toString().substring(1).toLowerCase();
                        city.setText(upperString);
                        writeCity.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        item.setIcon(android.R.drawable.ic_menu_add);
                    }
                    writeCity.setVisibility(View.GONE);
                    Objects.requireNonNull(getSupportActionBar()).setTitle(getTitle());
                } else {
                    Objects.requireNonNull(getSupportActionBar()).setTitle("");
                    writeCity.setVisibility(View.VISIBLE);
                    writeCity.requestFocus();
                    item.setIcon(android.R.drawable.ic_menu_send);
                }
                break;
            }
            case R.id.menu_location: {
                break;
            }
            default: {

            }
        }
    }
}