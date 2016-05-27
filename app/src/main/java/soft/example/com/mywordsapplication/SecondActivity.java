package soft.example.com.mywordsapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 21.02.2016.
 */
public class SecondActivity extends Activity {

    SharedPreferences sp;
    TextView tvInfo;


    protected void onResume() {
        Boolean notif = sp.getBoolean("notif", false);
        String address = sp.getString("address", "");
        String text = "Notifications are "
                + ((notif) ? "enabled" : "disabled");
        tvInfo.setText(text);
        super.onResume();
    }
/*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, "Preferences");
        mi.setIntent(new Intent(this, PrefActivity.class));
        return super.onCreateOptionsMenu(menu);
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_activity);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        Button btnGo = (Button)findViewById(R.id.buttonGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), PrefActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                v.getContext().startActivity(myIntent);
                //buttonGotoClick(v);

            }
        });

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        // sleep

        /*
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
         */


    }

    public void stopApp(View v){
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        /*
        Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_HOME);
startActivity(intent);
         */

    }

    public void buttonGotoClick(View v){


        Intent myIntent = new Intent(this, MainActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        this.startActivityForResult(myIntent, 1);
        //Intent i = new Intent(this, SecondActivity.class);
        //startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                //display in short period of time
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout_root));

                //ImageView image = (ImageView) layout.findViewById(R.id.image);
                //image.setImageResource(R.drawable.android);
                TextView text = (TextView) layout.findViewById(R.id.text);


                Toast toast = new Toast(getApplicationContext());
                //toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                //toast.setText("пришли из main по cancel");
                //toast.setDuration(Toast.LENGTH_SHORT);
                //8toast.setView(layout);
                //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Log.i("ring", " notif:" + sp.getBoolean("notif", false));
                //if(sp.getBoolean("notif", false)) {
                //   Uri notification = Uri.parse("android.resource://soft.example.com.mywordsapplication/" + R.raw.mysound);
                //  Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                //  r.play();

                // }
            //  toast.show();



            }
        }
    }//onActivityResult
}
