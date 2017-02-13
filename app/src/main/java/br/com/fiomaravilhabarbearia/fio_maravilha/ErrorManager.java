package br.com.fiomaravilhabarbearia.fio_maravilha;


import com.crashlytics.android.Crashlytics;
import com.parse.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fraps on 10/5/16.
 */
public class ErrorManager {


    public static String getErrorMessage(ParseException exception) {
        Crashlytics.logException(exception);
        try {
            JSONObject jsonObject = new JSONObject(exception.getLocalizedMessage());
            switch (jsonObject.getInt("code")) {
                case 666:
                case 1001:
                case 1002:
                case 1003:
                case 1004:
                    return jsonObject.getString("message");
                default:
                    return "Falha na conexão. Por favor tente novamente";
            }
        } catch (JSONException e) {
            switch (exception.getCode()) {
                case ParseException.EMAIL_NOT_FOUND:
                    return "Essa conta ainda não foi cadastrada";
                case ParseException.OBJECT_NOT_FOUND:
                    return "Combinação email e senha incorreto";
                case ParseException.EMAIL_TAKEN:
                    return "O email dessa conta já foi cadastrado";
                case ParseException.USERNAME_TAKEN:
                    return "Essa conta já foi cadastrada";
                default:
                    return "Falha na conexão. Por favor tente novamente";
            }
        }
    }

    public static String getErrorMessage(Exception exception) {
        return exception.getLocalizedMessage();
    }

}
