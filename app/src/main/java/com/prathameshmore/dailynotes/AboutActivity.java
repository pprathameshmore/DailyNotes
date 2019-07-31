package com.prathameshmore.dailynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private TextView tvVersion;
    private Button btnGitHub, btnJetPack, btnToasty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setTitle("About");

        btnGitHub = findViewById(R.id.btn_github);
        btnJetPack = findViewById(R.id.btn_jetpack);
        btnToasty = findViewById(R.id.btn_toasty);

        btnGitHub.setOnClickListener(this);
        btnJetPack.setOnClickListener(this);
        btnToasty.setOnClickListener(this);


        tvVersion = findViewById(R.id.textViewVersion);
        tvVersion.setText("v" + BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_github:
                linkIntent("https://github.com/pprathameshmore/DailyNotes");
                break;

            case R.id.btn_jetpack:
                linkIntent("https://developer.android.com/jetpack");
                break;

            case R.id.btn_toasty:
                linkIntent("https://android-arsenal.com/details/1/7781");
                break;

        }
    }

    private void linkIntent(String link) {
        Uri uri = Uri.parse(link);
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(linkIntent);
    }
}
