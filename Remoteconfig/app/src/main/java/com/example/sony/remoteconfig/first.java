package com.example.sony.remoteconfig;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

public class first extends AppCompatActivity {

    // Firebase instance variables
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RelativeLayout container;
    private static final String TAG = "first"/*first.class.getSimpleName()*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Log.d(TAG, "Layout visible");

        container = (RelativeLayout) findViewById(R.id.activity_first);

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

// Define default config values. Defaults are used when fetched config values are not
// available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("color_code", "#ff0000");

        Log.d(TAG, "Firebase Init and defaults");

// Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

// Fetch remote config.
        fetchConfig();
    }

    private void fetchConfig() {
        Log.d(TAG, "fetchConfig called");
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that
        // each fetch goes to the server. This should not be used in release
        // builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings()
                .isDeveloperModeEnabled()) {
            cacheExpiration = 0;
            Log.d(TAG, "In Debug mode condition check");
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "fetch Success");
                mFirebaseRemoteConfig.activateFetched();
                Log.d(TAG, "fetch activated");
                String colorCode = mFirebaseRemoteConfig.getString("color_code");
                String tv = mFirebaseRemoteConfig.getString("text");
                Log.d(TAG, "Color code from Firebase: " + colorCode);

                container.setBackgroundColor(Color.parseColor(colorCode));
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "fetch Failed: " + e.getMessage());
            }
        });
    }
}
