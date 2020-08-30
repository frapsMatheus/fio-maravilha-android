package br.com.fiomaravilhabarbearia.fio_maravilha;

import com.flurry.android.FlurryAgent;

public class FioAnalytics {

    public static void setUserData(String userID) {
        FlurryAgent.setUserId(userID);
    }

    public static void logSimpleEvent(String event) {
        FlurryAgent.logEvent(event);
    }

    public static void logError(String errorID, String message, Throwable exception) {
        FlurryAgent.onError(errorID, message, exception);
    }

}
