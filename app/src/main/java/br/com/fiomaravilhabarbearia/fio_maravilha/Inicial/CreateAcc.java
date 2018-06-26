package br.com.fiomaravilhabarbearia.fio_maravilha.Inicial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

public class CreateAcc extends BaseActivity {

    @BindView(R.id.input_email)
    EditText _email;

    @BindView(R.id.input_nome)
    EditText _name;

    @BindView(R.id.input_password)
    EditText _password;

    private int PHONE_AUTH_CODE = 500;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_AUTH_CODE) {
            if(resultCode == Activity.RESULT_OK){
                ParseUser newUser = new ParseUser();
                showLoadingDialog();
                if (_password.getText().toString().length() > 2) {
                    newUser.setPassword(_password.getText().toString());
                }
                newUser.setUsername(_email.getText().toString());
                newUser.setEmail(_email.getText().toString());
                newUser.put("name", _name.getText().toString());
                createUser(data,newUser);
            }
        }
    }//onActivityResult


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

        FioUtils.getMixpanel(CreateAcc.this)
                .track("Finalizar cadastro");
        MixpanelAPI.People people = FioUtils.getMixpanel(getApplicationContext()).getPeople();
        people.identify(newUser.getObjectId());
        people.set("$email",_email.getText().toString());
        people.set("$name",_name.getText().toString());
        people.set("$phone",phone);
        people.set("Created", DateFormat.getDateTimeInstance().format(new Date()));
        people.initPushHandling("845359638452");
        Intent newIntent = new Intent(CreateAcc.this, MainActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
    }

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
            Intent i = new Intent(this, PhoneInputActivity.class);
            startActivityForResult(i, PHONE_AUTH_CODE);
        }
    }
}
