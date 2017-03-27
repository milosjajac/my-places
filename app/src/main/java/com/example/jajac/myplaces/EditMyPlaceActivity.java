package com.example.jajac.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    static final int GET_LOCATION_REQUEST = 1;

    boolean editMode = true;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            if (positionBundle != null) {
                position = positionBundle.getInt("position");
            } else {
                editMode = false;
            }
        } catch (Exception e) {
            editMode = false;
        }

        final Button finishedButton = (Button)findViewById(R.id.editmyplace_finished_button);
        Button cancelButton = (Button)findViewById(R.id.editmyplace_cancel_button);

        EditText nameEt = (EditText)findViewById(R.id.editmyplace_name_edit);
        if (!editMode) {
            finishedButton.setEnabled(false);
            finishedButton.setText("Add");
        } else if (position >= 0) {
            finishedButton.setText("Save");
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            nameEt.setText(place.getName());
            EditText descEt = (EditText)findViewById(R.id.editmyplace_desc_edit);
            descEt.setText(place.getDesc());
            EditText lonEt = (EditText)findViewById(R.id.editmyplace_long_edit);
            lonEt.setText(place.getLongitude());
            EditText latEt = (EditText)findViewById(R.id.editmyplace_lat_edit);
            latEt.setText(place.getLatitude());
        }

        finishedButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length() > 0);
            }
        });

        Button locationButton = (Button)findViewById(R.id.editmyplace_location_button);
        locationButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_my_place, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check the request code to know which activity ended
        // it's the same request code that was sent when calling startActivityForResult
        switch (requestCode) {
            case GET_LOCATION_REQUEST:
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        String lon = data.getExtras().getString("lon");
                        EditText lonEt = (EditText)findViewById(R.id.editmyplace_long_edit);
                        lonEt.setText(lon);
                        String lat = data.getExtras().getString("lat");
                        EditText latEt = (EditText)findViewById(R.id.editmyplace_lat_edit);
                        latEt.setText(lat);
                    }
                } catch (Exception e) {
                    Log.e("EditMyPlaceActivity", "Failed fetching location.");
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editmyplace_finished_button:
                EditText etName = (EditText)findViewById(R.id.editmyplace_name_edit);
                String name = etName.getText().toString();
                EditText etDesc = (EditText)findViewById(R.id.editmyplace_desc_edit);
                String desc = etDesc.getText().toString();
                EditText etLon = (EditText)findViewById(R.id.editmyplace_long_edit);
                String lon = etLon.getText().toString();
                EditText etLat = (EditText)findViewById(R.id.editmyplace_lat_edit);
                String lat = etLat.getText().toString();
                if (!editMode) {
                    MyPlace place = new MyPlace(name, desc, lat, lon);
                    MyPlacesData.getInstance().addNewPlace(place);
                } else {
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    place.setName(name);
                    place.setDescription(desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().updatePlace(place);
                }
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.editmyplace_cancel_button:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.editmyplace_location_button:
                Intent i = new Intent(this, MyPlacesMapActivity.class);
                i.putExtra("state", MyPlacesMapActivity.SELECT_COORDINATES);
                startActivityForResult(i, GET_LOCATION_REQUEST);
                break;
        }
    }
}
