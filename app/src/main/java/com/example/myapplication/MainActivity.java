package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.myapplication.ui.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

        private AppBarConfiguration mAppBarConfiguration;
        private View headerView;
        private TextView UserName;
        private TextView UserEmail;
        private FirebaseUser user;
        private NavController navController;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            user = FirebaseAuth.getInstance().getCurrentUser();

            Toolbar toolbar = findViewById(R.id.toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            setSupportActionBar(toolbar);

            headerView = navigationView.getHeaderView(0);
            UserName = headerView.findViewById(R.id.UserName);
            UserEmail = headerView.findViewById(R.id.UserEmail);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_tools, R.id.nav_signin, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                    if (destination.getLabel().toString().equals("登出")) {
                        FirebaseAuth.getInstance().signOut();
                        controller.navigate(R.id.nav_home);
                        UserName.setText("userName");
                        UserEmail.setText("XXX@gmail.com");
                        Toast.makeText(MainActivity.this,"登出成功",Toast.LENGTH_SHORT).show();
                    }

                    String title;
                    title = "記帳吧!月光族";
                    ActionBar a = getSupportActionBar();
                    a.setTitle(title);
                }
            });

            FloatingActionButton fab = findViewById(R.id.fab);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(MainActivity.this,AddActivity.class),1);
                    }
                });
            }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }


        @Override
        public boolean onSupportNavigateUp() {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }

        public void setNavigate(String name, String email) {
            Log.e("getDisplayName", name+"!");
            Log.e("getEmail", email+"!");

            UserName.setText(name);
            UserEmail.setText(email);

            navController.navigate(R.id.nav_home);
        }
    }
