package br.com.fiomaravilhabarbearia.fio_maravilha.Inicial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.ErrorManager;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Phone.PhoneInputActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import br.com.fiomaravilhabarbearia.fio_maravilha.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fraps on 13/02/17.
 */

public class Login extends BaseActivity {

    @BindView(R.id.logo_view)
    ImageView _logoView;

    @BindView(R.id.input_email)
    EditText _email;

    @BindView(R.id.input_password)
    EditText _password;

    private int PHONE_AUTH_CODE_FB = 501;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        ViewGroup.LayoutParams params = _logoView.getLayoutParams();
        final float scale = getResources().getDisplayMetrics().density;
        if (height < 500) {
            int pixels = (int) (140 * scale + 0.5f);
            params.height = pixels;
        } else if (height < 840) {
            int pixels = (int) (200 * scale + 0.5f);
            params.height = pixels;
        } else {
            int pixels = (int) (240 * scale + 0.5f);
            params.height = pixels;
        }
        _logoView.setLayoutParams(params);
        String[] permissionsList = {"email","public_profile"};
        _permissions = Arrays.asList(permissionsList);
        _callbackManager = CallbackManager.Factory.create();
    }

    @OnClick(R.id.login_entrar)
    public void entrar() {
        if (!StringUtils.isEmailValid(_email.getText().toString())) {
            _email.setError("Digite um email válido");
        } else if (!StringUtils.isPasswordValid(_password.getText().toString())) {
            _password.setError("A senha deve conter ao menos 6 caracteres");
        } else {
            showLoadingDialog();
            ParseUser.logInInBackground(_email.getText().toString(), _password.getText().toString(), (user, e) -> {
                dismissLoadingDialog();
                if (user != null) {
                    FioUtils.getMixpanel(Login.this)
                            .track("Login");
                    MixpanelAPI.People people = FioUtils.getMixpanel(this).getPeople();
                    people.identify(user.getObjectId());
                    people.initPushHandling("845359638452");
                    Intent newIntent = new Intent(this, MainActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                } else {
                    showErrorDialog(ErrorManager.getErrorMessage(Login.this, e));
                }
            });

        }
    }

    @OnClick(R.id.login_forgot_pass)
    public void forgot() {
        new MaterialDialog.Builder(this)
                .title("Recupere a sua senha")
                .backgroundColor(FioUtils.getColor(this, R.color.colorBlack))
                .titleColor(FioUtils.getColor(this, R.color.colorOrange))
                .contentColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .widgetColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .content("Digite o email da sua conta")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input("Email", _email.getText().toString(), (dialog, input) -> {
                    showLoadingDialog();
                    ParseUser.requestPasswordResetInBackground(input.toString(), e -> {
                        dismissLoadingDialog();
                        if (e == null) {
                            showSuccessDialog("Foi enviado um email para a sua conta com instruções para gerar uma nova senha",true,
                                    () -> {

                                    });
                        } else {
                            showErrorDialog(ErrorManager.getErrorMessage(Login.this, e));
                        }
                    });
                }).show();
    }

    @OnClick(R.id.fb_login)
    public void fbLogin() {
        LoginManager manager = LoginManager.getInstance();
        manager.logOut();
        manager.registerCallback(_callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                checkUserExists();
            }

            @Override
            public void onCancel() {
                showErrorDialog("Erro ao logar com Facebook. Tente novamente");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG);
//                fbLogin();
            }
        });
        manager.logInWithReadPermissions(this, _permissions);
    }

    private void checkUserExists() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            ParseQuery query = new ParseQuery("_User");
            query.whereEqualTo("username",token.getUserId());
            showLoadingDialog();
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (objects.isEmpty()) {
                            callGraph();
                        } else {
                            ParseFacebookUtils.logInInBackground(token, (user, e1) -> {
                                dismissLoadingDialog();
                                if (e1 == null) {
                                    FioUtils.getMixpanel(Login.this)
                                            .track("Login");
                                    MixpanelAPI mixpanelAPI = FioUtils.getMixpanel(Login.this);
                                    mixpanelAPI.getPeople().identify(user.getObjectId());
                                    mixpanelAPI.getPeople().initPushHandling("845359638452");
                                    Intent intent = new Intent(Login.this,
                                            MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showErrorDialog(ErrorManager.getErrorMessage(Login.this, e1));
                                }
                            });
                        }
                    } else {
                        showErrorDialog(ErrorManager.getErrorMessage(Login.this, e));
                    }
                }
            });
        } else {
            showErrorDialog("Erro ao logar com Facebook. Tente novamente");
        }
    }

    private void callGraph() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
                if (response.getError() != null) {
                    fbLogin();
                } else {
                    try {
                        _emailText = response.getJSONObject().getString("email");
                        _name = response.getJSONObject().getString("name");
                        callDigits();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog("Erro ao logar com facebook. Tente novamente");
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,email");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            showErrorDialog("Erro ao logar com facebook. Tente novamente");
        }
    }

    private CallbackManager _callbackManager;
    private List<String> _permissions;
    private String _name;
    private String _emailText;

    public JSONObject getAuthData(AccessToken accessToken) throws JSONException {
        JSONObject result = new JSONObject();
        JSONObject object = new JSONObject();
        object.put("id", accessToken.getUserId());
        object.put("access_token", accessToken.getToken());
        object.put("expiration_date", fbFormatDate(accessToken.getExpires()));
        result.put("facebook",object);
        return result;
    }

    private String fbFormatDate(Date date) {
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        spf.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return spf.format(date);
    }

    private void callDigits() {
        Intent i = new Intent(this, PhoneInputActivity.class);
        startActivityForResult(i, PHONE_AUTH_CODE_FB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_AUTH_CODE_FB) {
            if(resultCode == Activity.RESULT_OK){
                showLoadingDialog();
                createFBUser(data);
            }
        }
    }

    private void createFBUser(Intent data) {
        ParseUser newUser = new ParseUser();
        try {
            AccessToken token = AccessToken.getCurrentAccessToken();
            JSONObject fbObject = getAuthData(token);
            String android_id = Settings.Secure.getString(Login.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            newUser.setPassword(android_id);
            newUser.setUsername(token.getUserId());
            newUser.put("fbAuthData",fbObject);
            newUser.put("name", _name);
            newUser.setEmail(_emailText);
            createUser(data, newUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createUser(Intent data, ParseUser newUser) {
        String phoneId = data.getStringExtra("phone_id");
        String phone = data.getStringExtra("phone");
        newUser.put("phone", phone);
        newUser.put("digits_id", String.valueOf(phoneId));
        newUser.signUpInBackground( e -> {
            dismissLoadingDialog();
            if (e == null) {
                finishAccCreation(newUser, phone);
            } else {
                showErrorDialog(ErrorManager.getErrorMessage(e));
            }
        });
    }

    private void finishAccCreation(ParseUser newUser, String phone) {
        FioUtils.getMixpanel(Login.this)
                .track("Finalizar cadastro");
        MixpanelAPI mixpanelAPI = FioUtils.getMixpanel(Login.this);
        mixpanelAPI.getPeople().identify(newUser.getObjectId());
        mixpanelAPI.getPeople().initPushHandling("845359638452");
        mixpanelAPI.getPeople().set("$email",_emailText);
        mixpanelAPI.getPeople().set("$name",_name);
        mixpanelAPI.getPeople().set("$phone",phone);
        mixpanelAPI.getPeople().set("Created", DateFormat.getDateTimeInstance().format(new Date()));
        Intent agendamentoActivity = new Intent(Login.this,MainActivity.class);
        agendamentoActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(agendamentoActivity);
    }

    @OnClick(R.id.login_new_acc)
    public void criarConta() {
        Intent newIntent = new Intent(this, CreateAcc.class);
        startActivity(newIntent);
    }
}
