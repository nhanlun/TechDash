package com.example.techdash.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.techdash.R;
import com.example.techdash.fragments.CustomFragmentNavigator;
import com.example.techdash.models.Contest;
import com.example.techdash.repositories.ContestRepository;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 123;
    private static final String TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private BottomAppBar bottomAppBar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(myToolBar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        navController = Navigation.findNavController(this, R.id.navHostFragment);

        fab = findViewById(R.id.runButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.runFragment);
            }
        });

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(myToolBar, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id = destination.getId();
                // TODO: remove login fragment in this condition
                if (id == R.id.homeFragment || id == R.id.friendFragment || id == R.id.displayMapFragment ||
                        id == R.id.runFragment || id == R.id.contestFragment || id == R.id.historyFragment) {
                    if (myToolBar.getVisibility() != View.VISIBLE) {
                        myToolBar.setVisibility(View.VISIBLE);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        bottomAppBar.setVisibility(View.VISIBLE);
                        fab.show();
                    }
                } else {
                    Log.d("gone","gone");
                    if (myToolBar.getVisibility() != View.GONE) {
                        myToolBar.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.GONE);
                        bottomAppBar.setVisibility(View.GONE);
                        fab.hide();
                    }
                }
            }
        });
    }
}