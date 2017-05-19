package com.mapzen.android.sdk.sample;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Basic SDK demo, tracks user's current location on map.
 */
public class BasicMapzenActivity extends BaseDemoActivity
    implements AdapterView.OnItemSelectedListener {

  private static final String KEY_LOCATION_ENABLED = "enabled";
  private static final String TAG = BasicMapzenActivity.class.getSimpleName();

  private MapboxMap map;
  private MapView mapView;

  /**
   * To conserve resources, {@link MapzenMap#setMyLocationEnabled} is set to false when
   * the activity is paused and re-enabled when the activity resumes.
   */
  private boolean enableLocationOnResume = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Mapbox.getInstance(this, "MAPBOX_API_KEY_HERE");

    setContentView(R.layout.activity_spinner);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    Spinner spinner = (Spinner) findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter =
        ArrayAdapter.createFromResource(this, R.array.enabled_disabled_array,
            R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);

    final boolean enabled = (savedInstanceState == null ||
        savedInstanceState.getBoolean(KEY_LOCATION_ENABLED));

    final long millis = System.currentTimeMillis();

    mapView = (MapView) findViewById(R.id.map_view);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override public void onMapReady(MapboxMap mapboxMap) {
        BasicMapzenActivity.this.map = mapboxMap;
        configureMap(enabled);
      }
    });
  }

  private void configureMap(boolean enabled) {
    if (enabled) {
      setMyLocationEnabled(true);
    }
  }

  @Override protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
    mapView.onPause();
    if (map != null && map.isMyLocationEnabled()) {
      map.setMyLocationEnabled(false);
    }
  }

  @Override protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    mapView.onSaveInstanceState(outState);
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
    setMyLocationEnabled(false);
  }

  @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    if (map == null) {
      return;
    }

    switch (position) {
      case 0:
        setMyLocationEnabled(true);
        break;
      case 1:
        setMyLocationEnabled(false);
        break;
      default:
        setMyLocationEnabled(true);
        break;
    }
  }

  @Override public void onNothingSelected(AdapterView<?> parent) {

  }

  private void setMyLocationEnabled(boolean enabled) {
    if (map == null) {
      return;
    }
    map.getMyLocationViewSettings().setEnabled(enabled);
    map.setMyLocationEnabled(enabled);
    enableLocationOnResume = enabled;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(KEY_LOCATION_ENABLED, enableLocationOnResume);
  }
}
