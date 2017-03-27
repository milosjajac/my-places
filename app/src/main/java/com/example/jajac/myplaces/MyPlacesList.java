package com.example.jajac.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyPlacesList extends AppCompatActivity {

    static final int NEW_PLACE = 1;

    static final int EDIT_PLACE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView myPlacesList = (ListView)findViewById(R.id.my_places_list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
        myPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle positionBundle = new Bundle();
                positionBundle.putInt("position", position);
                Intent i = new Intent(MyPlacesList.this, ViewMyPlaceActivity.class);
                i.putExtras(positionBundle);
                startActivity(i);
            }
        });

        registerForContextMenu(myPlacesList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MyPlace place = MyPlacesData.getInstance().getPlace(info.position);
        menu.setHeaderTitle(place.getName());
        getMenuInflater().inflate(R.menu.menu_places_list_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Bundle positionBundle = new Bundle();
        positionBundle.putInt("position", info.position);
        Intent i;
        switch (item.getItemId()) {
            case R.id.places_list_context_view_item:
                i = new Intent(this, ViewMyPlaceActivity.class);
                i.putExtras(positionBundle);
                startActivity(i);
                break;
            case R.id.places_list_context_edit_item:
                i = new Intent(this, EditMyPlaceActivity.class);
                i.putExtras(positionBundle);
                startActivityForResult(i, EDIT_PLACE_REQUEST);
                break;
            case R.id.places_list_context_delete_item:
                MyPlacesData.getInstance().deletePlace(info.position);
                setList();
                break;
            case R.id.places_list_context_map_item:
                i = new Intent(this, MyPlacesMapActivity.class);
                i.putExtra("state", MyPlacesMapActivity.CENTER_PLACE_ON_MAP);
                MyPlace place = MyPlacesData.getInstance().getPlace(info.position);
                i.putExtra("lat", place.getLatitude());
                i.putExtra("lon", place.getLongitude());
                startActivity(i);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void setList() {
        ListView myPlacesList = (ListView)findViewById(R.id.my_places_list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_places_list, menu);
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
            case R.id.new_place_item:
                i = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(i, NEW_PLACE);
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
        switch (requestCode) {
            case EDIT_PLACE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    ListView myPlacesList = (ListView) findViewById(R.id.my_places_list);
                    myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
                }
                break;
        }
    }
}
