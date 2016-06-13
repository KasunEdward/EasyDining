package com.example.newlogin.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends ActionBarActivity implements LocationListener {
    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Button btnLogout;
    private Session session;
    Marker marker;
    HashMap newMap=new HashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        session = new Session(MapsActivity.this);
       /* ActionBar actionBar=getActionBar();
        actionBar.setTitle("Easy Dining");
        actionBar.setIcon(R.drawable.app);*/
        if(!session.getLoggedIn()){
            logoutUser();
        }
        if(session.getLoggedIn()) {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();

            setUpMap();
        }
    }
    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #} once when {@link #googleMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */


    @Override
    public void onLocationChanged(Location location) {
        if(marker!=null){
            marker.remove();
        }
        //googleMap.clear();

           /* MarkerOptions mp = new MarkerOptions();

            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

            mp.title("my position");
            //setUpMap();
            googleMap.addMarker(mp);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(,
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));*/
        marker=googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
        }
        else if(id==R.id.action_about){
            View customToastView=getLayoutInflater().inflate(R.layout.custom_toast,null);
            Toast toast=new Toast(getApplicationContext());
            toast.setView(customToastView);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProviderDisabled(String s) {

    }
    private void setUpMap(){
        getJSON("http://www.slcabs.com/easydining/restaurants.php");
    }
    private void getJSON(String url){
        class GetJSON extends AsyncTask<String,Void ,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this, "Please Wait...",null,true,true);
            }
            @Override
            protected String doInBackground(String...params) {
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //con.setRequestProperty("Content-Type", "application/json");
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    Log.w("android", sb.toString());
                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // List<List<Object>> listOfList=new ArrayList<List<Object>>();

                // Log.w("android",s);
                try {
                    JSONObject jsonResponse = new JSONObject(s);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("restaurants");
                    for(int i=0;i<jsonMainNode.length();i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        List<Object> list=new ArrayList();
                        int id=jsonChildNode.optInt("id");
                        String name = jsonChildNode.optString("name");
                        String address=jsonChildNode.optString("address");
                        Double latitude = jsonChildNode.optDouble("latitude");
                        Double longitude = jsonChildNode.optDouble("longitude");
                        String telno = jsonChildNode.optString("telno");
                        String email=jsonChildNode.optString("email");
                        String web=jsonChildNode.optString("web");
                        String facebook=jsonChildNode.optString("facebook");
                        String twitter=jsonChildNode.optString("twitter");
                        String features=jsonChildNode.optString("features");
                        Double rating=jsonChildNode.optDouble("rating");
                        int total_votes=jsonChildNode.optInt("total_votes");
                        String str=String.valueOf(id);
                        list.add(str);
                        list.add(name);
                        list.add(address);
                        list.add(longitude);
                        list.add(latitude);
                        list.add(telno);
                        list.add(email);
                        list.add(web);
                        list.add(facebook);
                        list.add(twitter);
                        list.add(features);
                        list.add(rating);
                        list.add(total_votes);
                        newMap.put(str,list);
                     //   listOfList.add(list);
                       // String fb = jsonChildNode.optString("fbLink");
                        //String web = jsonChildNode.optString("webLink");
                        //String newStr="Rating:"+ String.valueOf(rating)+"/nTotal Votes:"+String.valueOf(total_votes);
                        MarkerOptions marker=new MarkerOptions().position(new LatLng(latitude, longitude)).title(name).snippet(str);
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                       googleMap.addMarker(marker);

                        //Log.w("android",name);
                    }

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent=new Intent(MapsActivity.this,DisplayActivity.class);
                           List<Object>list= (List<Object>) newMap.get(marker.getSnippet());

                           Log.w("android",list.get(2).toString());
                            intent.putExtra("id",(String)list.get(0));
                            intent.putExtra("name",(String)list.get(1));
                            intent.putExtra("address",(String)list.get(2));
                           // intent.putExtra("latitude",(String)list.get(3));
                           // intent.putExtra("longitude",(String)list.get(4));
                            intent.putExtra("telno",(String)list.get(5));
                            intent.putExtra("email",(String)list.get(6));
                            intent.putExtra("web",(String)list.get(7));
                            intent.putExtra("facebook",(String)list.get(8));
                            intent.putExtra("twitter",(String)list.get(9));
                            intent.putExtra("features",(String)list.get(10));
                            intent.putExtra("rating",(Double)list.get(11));
                            intent.putExtra("total_votes",(Integer)list.get(12));
                            startActivity(intent);


                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
























            }

        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
