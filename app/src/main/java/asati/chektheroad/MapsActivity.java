package asati.chektheroad;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnLocationChangedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);
        setUpMapIfNeeded();
        if (savedInstanceState == null) {
           getSupportFragmentManager()
                   .beginTransaction()
                   .add(new LocationFragment(), "location")
                   .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mOverridenLocation = latLng;
                }
            });
            // Check if we were successful in obtaining the map.
        }
    }

    private LatLng mMyCurrentLocation;
    private LatLng mOverridenLocation;

    @Override
    public void onLocationChanged(Location location) {
        mMyCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (mOverridenLocation == null) {
            mOverridenLocation = mMyCurrentLocation;
        }
        mMap.clear();
        Set<Map.Entry<LatLng, Database.Type>> entries = Database.listEverything();
        for (Map.Entry<LatLng, Database.Type> entry : entries) {
            mMap.addMarker(new MarkerOptions().position(entry.getKey()).icon(BitmapDescriptorFactory.fromResource(entry.getValue().resId)).title("Title"));
        }
        mMap.addMarker(new MarkerOptions().position(mMyCurrentLocation).title("My location"));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mMyCurrentLocation)
                .zoom(17f)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @OnClick(R.id.icon_road)
    public void onDamagedRoadIconClicked(){
        startReportActivity(ReportProblemActviity.PROBLEM_TYPE_DAMAGED_ROAD);
    }

    @OnClick(R.id.icon_water)
    public void onDamagedPipelineIconClicked(){
        startReportActivity(ReportProblemActviity.PROBLEM_TYPE_PIPELINE);
    }

    @OnClick(R.id.icon_polution)
    public void onDamagedGarbageIconClicked(){
        startReportActivity(ReportProblemActviity.PROBLEM_TYPE_GARBAGE);
    }

    @OnClick(R.id.icon_more)
    public void onDamagedOtherIconClicked(){
        startReportActivity(ReportProblemActviity.PROBLEM_TYPE_OTHER);
    }

    private void startReportActivity(int problemType) {
        if (mOverridenLocation == null) {
            Toast.makeText(this, "Current location is not known yet", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ReportProblemActviity.class);
            intent.putExtra(ReportProblemActviity.EXTRA_PROBLEM_TYPE, problemType);
            intent.putExtra(ReportProblemActviity.EXTRA_LOCATION, mOverridenLocation);
            startActivity(intent);
        }
    }


}
