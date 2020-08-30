package br.com.fiomaravilhabarbearia.fio_maravilha.Inicial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.parse.ParseUser;

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

public class CreateAcc extends BaseActivity {

    @BindView(R.id.input_email)
    EditText _email;

    @BindView(R.id.input_nome)
    EditText _name;

    @BindView(R.id.input_password)
    EditText _password;

    @BindView(R.id.phone_input)
    EditText _input;

    private int PHONE_AUTH_CODE = 500;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_AUTH_CODE) {
            if(resultCode == Activity.RESULT_OK){

                showLoadingDialog();

            }
        }
    }//onActivityResult


    private void createUser() {
        ParseUser newUser = new ParseUser();
        if (_password.getText().toString().length() > 2) {
            newUser.setPassword(_password.getText().toString());
        }
        newUser.setUsername(_email.getText().toString());
        newUser.setEmail(_email.getText().toString());
        newUser.put("name", _name.getText().toString());
        String phone = "+55" + _input.getText().toString().replace("(","").replace(")","")
                .replace("-","").replace(" ","");
        if (phone.length() < 13) {
            this.showErrorDialog("O número de telefone digitado não é válido");
            return;
        }
        newUser.put("phone", phone);
        this.showLoadingDialog();
        newUser.signUpInBackground( e -> {
            dismissLoadingDialog();
            if (e == null) {
                FioAnalytics.logSimpleEvent("Primeira etapa cadastro");
                finishAccCreation();
            } else {
                FioAnalytics.logError("Create user", ErrorManager.getErrorMessage(e), e);
                if (e.getCode() == 201) {
                    showErrorDialog("Uma conta para esse email já existe. Utilize a recuperação de senha para acessar a conta.");
                    onBackPressed();
                } else {
                    showErrorDialog(ErrorManager.getErrorMessage(e));
                }
            }
        });
    }

    private void finishAccCreation() {
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
            createUser();
        }
    }
}
