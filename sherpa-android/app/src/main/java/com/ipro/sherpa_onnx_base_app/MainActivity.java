package com.ipro.sherpa_onnx_base_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ipro.sherpa_onnx_base_app.databinding.ActivityMainBinding;
import com.k2fsa.sherpa.onnx.SherpaOnnxTTSPlugin;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        SherpaOnnxTTSPlugin plugin = new SherpaOnnxTTSPlugin();
        Context context = getApplicationContext();
        plugin.setActivity((Activity)context);
        plugin.initialize();

        plugin.generateAndPlay(0, 1.0f, context.getResources().getString(R.string.first_fragment_content));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
