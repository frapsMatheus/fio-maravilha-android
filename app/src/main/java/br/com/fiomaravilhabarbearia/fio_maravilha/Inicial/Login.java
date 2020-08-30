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
import com.parse.FindCallback;
import com.parse.ParseException;
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
import br.com.fiomaravilhabarbearia.fio_maravilha.FioAnalytics;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
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
                    Intent newIntent = new Intent(this, MainActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                    FioAnalytics.logSimpleEvent("Login realizado");
                } else {
                    FioAnalytics.logError("Login", ErrorManager.getErrorMessage(Login.this, e), e);
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
                            FioAnalytics.logSimpleEvent("Forgot");
                            showSuccessDialog("Foi enviado um email por no-reply@back4app.com para a sua conta com instruções para gerar uma nova senha",true,
                                    () -> {

                                    });
                        } else {
                            FioAnalytics.logError("Forgot", ErrorManager.getErrorMessage(Login.this, e), e);
                            showErrorDialog(ErrorManager.getErrorMessage(Login.this, e));
                        }
                    });
                }).show();
    }

    @OnClick(R.id.login_new_acc)
    public void criarConta() {
        Intent newIntent = new Intent(this, CreateAcc.class);
        startActivity(newIntent);
    }
}
