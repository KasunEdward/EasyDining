package com.example.newlogin.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DisplayActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {



    ListView lview3;
    ListViewCustomAdapter adapter;
    private ArrayList<Object> itemList;
    private ItemBean bean;
    Bundle b;

    String url="http://www.slcabs.com/android_login_api/update_rate.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent displayIntent=getIntent();
         b=displayIntent.getExtras();
        prepareArrayLits();
        lview3 = (ListView) findViewById(R.id.listView);
        adapter = new ListViewCustomAdapter(this, itemList);
        lview3.setAdapter(adapter);

        lview3.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        // TODO Auto-generated method stub
        if(position==2){
          //  Intent in=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + (String) b.get("telno")));
            try{
               showDialog();
            }
            catch(Exception e){
                e.printStackTrace();

            }
        }
        else if(position==3){
            Intent in=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + (String) b.get("telno")));
            try{
                startActivity(in);
            }
            catch(Exception e){
                e.printStackTrace();

            }
        }
        else if(position==4){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+(String)b.get("web")));
            try {
                startActivity(browserIntent);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(position==5){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+(String)b.get("facebook")));
            try {
                startActivity(browserIntent);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(position==6){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+(String)b.get("twitter")));
            try {
                startActivity(browserIntent);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

      //  ItemBean bean = (ItemBean) adapter.getItem(position);
       // Toast.makeText(this, "Title => " + bean.getTitle() + " n Description => " + bean.getDescription(), Toast.LENGTH_SHORT).show();
    }

    /* Method used to prepare the ArrayList,
     * Same way, you can also do looping and adding object into the ArrayList.
     */
    public void prepareArrayLits()
    {
        itemList = new ArrayList<Object>();

        AddObjectToList(R.drawable.app, (String)b.get("name"),"");
        AddObjectToList(R.mipmap.address,"Address" , (String)b.get("address"));
        AddObjectToList(R.mipmap.rate,"Rate" ,"Click to Rate");
        AddObjectToList(R.mipmap.phone, (String)b.get("telno"), "Click To Call");
        AddObjectToList(R.mipmap.web, "Web Site", "Click Here");
        AddObjectToList(R.mipmap.facebook, "Faceboook Page", "Click Here");
        AddObjectToList(R.mipmap.twitter, "Twitter Page", "Click Here");
        AddObjectToList(R.mipmap.gmail, "Email", "Click Here");
        AddObjectToList(R.mipmap.features, "Features",(String)b.get("features"));
       // AddObjectToList(R.drawable.app, "Settings", "Settings desc");
    }

    // Add one item into the Array List
    public void AddObjectToList(int image, String title, String desc)
    {
        bean = new ItemBean();
        bean.setDescription(desc);
        bean.setImage(image);
        bean.setTitle(title);
        itemList.add(bean);
    }

    public void showDialog(){
        final AlertDialog.Builder popDialog= new AlertDialog.Builder(this);
        final RatingBar rating=new RatingBar(this);
        rating.setMax(5);

        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Rate!! ");
        popDialog.setView(rating);
        // Button OK

        popDialog.setPositiveButton(android.R.string.ok,

        new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                //String val=String.valueOf(rating.getRating());
               // Toast.makeText(getApplicationContext(),val,Toast.LENGTH_LONG).show();
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                //PD.dismiss();
                                //item_et.setText("");
                                Toast.makeText(getApplicationContext(),
                                        "Data Inserted Successfully",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //PD.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "failed to insert", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    Double val;
                    Double prev_val;
                    int tot;
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        tot=b.getInt("total_votes");
                        prev_val=b.getDouble("rating")*tot;
                        prev_val+=rating.getRating();

                        tot+=1;
                        val=prev_val/tot;
                       // Toast.makeText(getApplicationContext(),String.valueOf(val),Toast.LENGTH_LONG).show();
                        params.put("id",b.getString("id"));
                        params.put("new_rate",String.valueOf(val));
                        params.put("total_votes",String.valueOf(tot));
                        return params;
                    }
                   // Toast.makeText(getApplicationContext(),String.valueOf(val),Toast.LENGTH_LONG).show();
                };

                AppController.getInstance().addToRequestQueue(postRequest);
                dialog.dismiss();

            }
        })
// Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();
    }

 public void updateRate(float rate){

 }
}
