package soft.example.com.mywordsapplication;

/**
 * Created by admin on 10.04.2016.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
