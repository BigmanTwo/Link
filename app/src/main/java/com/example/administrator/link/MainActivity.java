package com.example.administrator.link;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.link.views.CityPicker;

public class MainActivity extends AppCompatActivity {
    CityPicker cityPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityPicker= (CityPicker) findViewById(R.id.citypicker);
    }
}
