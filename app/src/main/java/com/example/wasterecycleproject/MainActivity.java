package com.example.wasterecycleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private RecycleFragment recycleFragment;
    private CommunityFragment communityFragment;
    private MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBar();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_recycle:
                        setFrag(1);
                        break;
                    case R.id.action_cart:
                        setFrag(2);
                        break;
                    case R.id.action_myPage:
                        setFrag(3);
                        break;

                }
                return true;
            }
        });


        homeFragment = new HomeFragment();
        recycleFragment = new RecycleFragment();
        communityFragment = new CommunityFragment();
        myPageFragment = new MyPageFragment();
        setFrag(0); // 첫 프래그먼트 화면 지정

    }
    private void setActionBar() {
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.Main_Frame,homeFragment);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.Main_Frame,recycleFragment);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.Main_Frame,communityFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.Main_Frame,myPageFragment);
                ft.commit();
                break;


        }
    }
}
