package com.example.parkpal1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import java.util.List;
import java.util.Objects;

import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;

import com.google.gson.JsonObject;
public class SimpleMapActivity extends AppCompatActivity {

    private String userRole;
    private MapView mapView;
    private FloatingActionButton floatingActionButton;
    private boolean consumeClickEvent = false;
    private static final String POINT_1_ID = "point_1";
    private static final String POINT_2_ID = "point_2";
    private static final String POINT_3_ID = "point_3";
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(SimpleMapActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(16.5).bearing(v).build());
        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(16.5).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        }
    };
    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            floatingActionButton.show();
        }
        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        mapView = findViewById(R.id.mapView);
        floatingActionButton = findViewById(R.id.focusLocation);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", "Driver"); // Default is "Driver"

        if (ActivityCompat.checkSelfPermission(SimpleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        floatingActionButton.hide();

        mapView.getMapboxMap().loadStyleUri("mapbox://styles/zeynepdk/clue3tlec00d101r21og5g9ek", new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                Drawable originalDrawable = AppCompatResources.getDrawable(SimpleMapActivity.this, R.drawable.red_loc);

                if (originalDrawable != null) {
                    int newWidth = originalDrawable.getIntrinsicWidth() / 6;
                    int newHeight = originalDrawable.getIntrinsicHeight() / 6;
                    Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) originalDrawable).getBitmap(), newWidth, newHeight, true);
                    Drawable resizedDrawable = new BitmapDrawable(getResources(), bitmap);

                    LocationPuck2D locationPuck2D = new LocationPuck2D();
                    locationPuck2D.setBearingImage(resizedDrawable);
                    locationComponentPlugin.setLocationPuck(locationPuck2D);
                }

                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        floatingActionButton.hide();
                    }
                });

                AnnotationPlugin annotationPlugin = mapView.getPlugin(Plugin.MAPBOX_ANNOTATION_PLUGIN_ID);
                PointAnnotationManager pointAnnotationManager = (PointAnnotationManager) annotationPlugin.createAnnotationManager(AnnotationType.PointAnnotation, null);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loc_icon);
                style.addImage("loc_icon.png", bitmap);

                PointAnnotationOptions firstAnnotationOptions = new PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(29.079911, 40.956000))
                        .withIconImage("loc_icon.png")
                        .withIconSize(0.1f)
                        .withData(createJsonData(POINT_1_ID)); // Set unique identifier for first point
                pointAnnotationManager.create(firstAnnotationOptions);

                PointAnnotationOptions secondPointOptions = new PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(29.080463, 40.956739))
                        .withIconImage("loc_icon.png")
                        .withIconSize(0.1f)
                        .withData(createJsonData(POINT_2_ID)); // Set unique identifier for second point
                pointAnnotationManager.create(secondPointOptions);

                PointAnnotationOptions thirdPointOptions = new PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(29.079050, 40.956987))
                        .withIconImage("loc_icon.png")
                        .withIconSize(0.1f)
                        .withData(createJsonData(POINT_3_ID)); // Set unique identifier for third point
                pointAnnotationManager.create(thirdPointOptions);

                pointAnnotationManager.addClickListener(new OnPointAnnotationClickListener() {
                    @Override
                    public boolean onAnnotationClick(@NonNull PointAnnotation pointAnnotation) {
                        String annotationId = pointAnnotation.getData().getAsJsonObject().get("id").getAsString();
                        Log.d("SimpleMapActivity", "Annotation ID: " + annotationId);

                        switch (annotationId) {
                            case POINT_1_ID:
                                Log.d("SimpleMapActivity", "Clicked on Point 1");
                                if ("Driver".equals(userRole)) {
                                    Intent driverIntent = new Intent(SimpleMapActivity.this, Street3Activity_Driver.class);
                                    startActivity(driverIntent);
                                } else if ("Attendant".equals(userRole)) {
                                    Intent attendantIntent = new Intent(SimpleMapActivity.this, Street3Activity_Attendant.class);
                                    startActivity(attendantIntent);
                                }
                                break;
                            case POINT_2_ID:
                                Log.d("SimpleMapActivity", "Clicked on Point 2");
                                Intent intent2 = new Intent(SimpleMapActivity.this, Street2Activity.class);
                                startActivity(intent2);
                                break;
                            case POINT_3_ID:
                                Log.d("SimpleMapActivity", "Clicked on Point 3");
                                Intent intent3 = new Intent(SimpleMapActivity.this, Street1Activity.class);
                                startActivity(intent3);
                                break;
                        }
                        return consumeClickEvent;
                    }
                });
            }
        });
    }
    private JsonObject createJsonData(String pointId) {
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("id", pointId);
        return jsonData;
    }
}