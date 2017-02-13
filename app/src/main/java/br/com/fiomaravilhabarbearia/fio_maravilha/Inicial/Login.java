package br.com.fiomaravilhabarbearia.fio_maravilha.Inicial;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.ErrorManager;
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
                } else {
                    showErrorDialog(ErrorManager.getErrorMessage(e));
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
                            showErrorDialog(ErrorManager.getErrorMessage(e));
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
