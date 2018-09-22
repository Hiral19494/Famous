package ca.android.famous.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.android.famous.R;
import ca.android.famous.map.DirectionsJSONParser;
import ca.android.famous.map.GPSService;
import ca.android.famous.model.Product;

public class MapFragment extends Fragment {
    private static final String PRODUCT = "product";
    Product product;
    @BindView(R.id.txv_latitude)
    TextView txvLatitude;
    @BindView(R.id.txv_longitude)
    TextView txvLongitude;
    @BindView(R.id.map_view)
    MapView mapView;
    public GoogleMap mGoogleMap;
    Double latitude, longitude;

    public static MapFragment newInstance(Product product) {
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(PRODUCT, product);
        mapFragment.setArguments(args);
        return mapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_map, parent, false);
        ButterKnife.bind(this, view);
        product = (Product) getArguments().get(PRODUCT);
        txvLatitude.setText(String.valueOf((product.getWarehouseLocation().getLatitude())));
        txvLongitude.setText(String.valueOf(product.getWarehouseLocation().getLongitude()));

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);
                MapsInitializer.initialize(getActivity());
                getMyLOcation();
                String url = getDirectionsUrl();
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);


            }
        });

        return view;
    }

    private void getMyLOcation() {
        GPSService mGPSService = new GPSService(getActivity());
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getActivity(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            latitude = mGPSService.getLatitude();
            longitude = mGPSService.getLongitude();
        }


        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
    }

    private String getDirectionsUrl() {
        // Origin of route
        MarkerOptions optionOrigin = new MarkerOptions();
        String str_origin = "origin=" + latitude + "," + longitude;

        LatLng latLngOrigin = new LatLng(latitude,longitude);
        optionOrigin.position(latLngOrigin);
        if(str_origin!= null) {
            optionOrigin.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mGoogleMap.addMarker(optionOrigin);

        }
        String str_dest = "destination=" + product.getWarehouseLocation().getLatitude() + "," + product.getWarehouseLocation().getLongitude();
        MarkerOptions optionDest= new MarkerOptions();
        LatLng latLngDest = new LatLng(product.getWarehouseLocation().getLatitude(),product.getWarehouseLocation().getLongitude());
        optionDest.position(latLngDest);
        if(str_dest!= null) {
            optionDest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mGoogleMap.addMarker(optionDest);

        }
        // Destination of route


        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";
        Log.d("latt", String.valueOf(latitude
    +":"+longitude+":"+product.getWarehouseLocation().getLatitude()+":"+product.getWarehouseLocation().getLongitude()));


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("url", url);

        return url;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            //      Log.d("data", data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            //        Log.d("result", result);
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();
            LatLng position = null;
            lineOptions.width(12);
            lineOptions.color(Color.RED);
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    position = new LatLng(lat, lng);

                    points.add(position);
                }
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                lineOptions.addAll(points);

                //  lineOptions.geodesic(true);

            }
            if (points.size() != 0) mGoogleMap.addPolyline(lineOptions);
// Drawing polyline in the Google Map for the i-th route
            //mGoogleMap.addPolyline(lineOptions);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
