package com.example.nela.earthquakealert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class About extends AppCompatActivity {

    LinearLayout versionInformation, contactInformation, supportInformation;
    // Button btn_versionInfo, btn_contactInfo, btn_supportInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        versionInformation = findViewById(R.id.version_information_content);
        contactInformation = findViewById(R.id.contact_us_content);
        supportInformation = findViewById(R.id.support_us_content);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showAboutContent(View view) {
        contactInformation.setVisibility(View.VISIBLE);
        if (view.getId() == R.id.btn_contact) {
            contactInformation.setVisibility(View.VISIBLE);
            supportInformation.setVisibility(View.GONE);
            versionInformation.setVisibility(View.GONE);
        } else if (view.getId() == R.id.btn_support) {
            versionInformation.setVisibility(View.GONE);
            supportInformation.setVisibility(View.VISIBLE);
            contactInformation.setVisibility(View.GONE);
        } else if (view.getId() == R.id.btn_version) {
            versionInformation.setVisibility(View.VISIBLE);
            supportInformation.setVisibility(View.GONE);
            contactInformation.setVisibility(View.GONE);
        }
    }
}
