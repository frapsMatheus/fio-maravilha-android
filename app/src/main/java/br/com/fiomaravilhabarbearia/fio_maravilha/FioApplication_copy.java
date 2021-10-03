package br.com.fiomaravilhabarbearia.fio_maravilha;

import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.parse.Parse;
import com.parse.ParseInstallation;

import io.fabric.sdk.android.Fabric;

public class FioApplication extends MultiDexApplication {


    private static final String ParseServer = "https://parseapi.back4app.com/";

//    Release
    private static final String ParseIDRelease = "ParseIDRelease";
    private static final String ParseKeyRelease = "ParseKeyRelease";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(ParseIDRelease)
                .server(ParseServer)
                .clientKey(ParseKeyRelease)
                .build()
        );

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "FLURRY_ID");

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "GCM_ID");
        installation.saveInBackground();
    }

}
