package br.com.fiomaravilhabarbearia.fio_maravilha.Inicial;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.ParseUser;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.ErrorManager;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import br.com.fiomaravilhabarbearia.fio_maravilha.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fraps on 13/02/17.
 */

public class CreateAcc extends BaseActivity {

    @BindView(R.id.input_email)
    EditText _email;

    @BindView(R.id.input_nome)
    EditText _name;

    @BindView(R.id.input_password)
    EditText _password;

    AuthCallback callback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
            showLoadingDialog();
            ParseUser newUser = new ParseUser();
            newUser.setEmail(_email.getText().toString());
            newUser.setPassword(_password.getText().toString());
            newUser.setUsername(_email.getText().toString());
            newUser.put("telefone", phoneNumber);
            newUser.put("nome", _name.getText().toString());
            newUser.put("digits_id", String.valueOf(session.getId()));
            newUser.signUpInBackground(e -> {
                dismissLoadingDialog();
                if (e == null) {
                    Intent newIntent = new Intent(CreateAcc.this, MainActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                } else {
                    showErrorDialog(ErrorManager.getErrorMessage(e));
                }
            });
        }

        @Override
        public void failure(DigitsException error) {
            showErrorDialog(ErrorManager.getErrorMessage(error));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_create);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.create_next)
    public void criarConta() {
        if (!StringUtils.isNameValid(_name.getText().toString())) {
            _name.setError("Escreva um nome válido");
        } else if (!StringUtils.isEmailValid(_email.getText().toString())) {
            _email.setError("Digite um email válido");
        } else if (!StringUtils.isPasswordValid(_password.getText().toString())) {
            _password.setError("A senha deve conter ao menos 6 caracteres");
        } else {
            AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                    .withAuthCallBack(callback)
                    .withPhoneNumber("+55");
            Digits.logout();
            Digits.authenticate(authConfigBuilder.build());
        }
    }
}
