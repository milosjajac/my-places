package com.example.jajac.myplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            this.position = positionBundle.getInt("position");
        }
        catch (Exception e) {
            Log.e("ViewMyPlaceActivity", e.getMessage());
            finish();
        }

        if (this.position >= 0) {
            MyPlace place = MyPlacesData.getInstance().getPlace(this.position);
            TextView twName = (TextView)findViewById(R.id.viewmyplace_name_text);
            twName.setText(place.getName());
            TextView twDesc = (TextView)findViewById(R.id.viewmyplace_desc_text);
            twDesc.setText(place.getDesc());
            TextView twLat = (TextView)findViewById(R.id.viewmyplace_lat_text);
            twLat.setText(place.getLatitude());
            TextView twLon = (TextView)findViewById(R.id.viewmyplace_long_text);
            twLon.setText(place.getLongitude());
        }
        final Button finishedButton = (Button)findViewById(R.id.viewmyplace_finished_button);
        finishedButton.setOnClickListener(this);
        final Button showOnMapButton = (Button)findViewById(R.id.viewmyplace_show_on_map_button);
        showOnMapButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_my_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.show_map_item:
                i = new Intent(this, MyPlacesMapActivity.class);
                i.putExtra("state", MyPlacesMapActivity.SHOW_MAP);
                startActivity(i);
                break;
            case R.id.my_places_item:
                i = new Intent(this, MyPlacesList.class);
                startActivity(i);
                break;
            case R.id.about_item:
                i = new Intent(this, About.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewmyplace_finished_button:
                finish();
                break;
            case R.id.viewmyplace_show_on_map_button:
                Intent i = new Intent(this, MyPlacesMapActivity.class);
                i.putExtra("state", MyPlacesMapActivity.CENTER_PLACE_ON_MAP);
                MyPlace place = MyPlacesData.getInstance().getPlace(this.position);
                i.putExtra("lat", place.getLatitude());
                i.putExtra("lon", place.getLongitude());
                startActivity(i);
                break;
        }
    }
}
