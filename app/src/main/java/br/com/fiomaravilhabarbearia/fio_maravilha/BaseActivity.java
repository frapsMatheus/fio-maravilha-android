package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by fraps on 9/7/16.
 */
public class BaseActivity extends AppCompatActivity {

    private MaterialDialog _dialog;
    private MaterialDialog _sucessDialog;
    private MaterialDialog _errorDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showLoadingDialog() {
        runOnUiThread(() -> _dialog = new MaterialDialog.Builder(this)
                .title("Carregando")
                .content("Aguarde")
                .backgroundColor(FioUtils.getColor(this, R.color.colorBlack))
                .titleColor(FioUtils.getColor(this, R.color.colorOrange))
                .contentColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .widgetColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .progress(true, 0)
                .cancelable(false)
                .show());
    }

    public interface SuccessHandler {
        void finish();
    }

    public void showSuccessDialog(String text, SuccessHandler successHandler) {
        runOnUiThread(() -> {
            _sucessDialog = new MaterialDialog.Builder(this)
                    .backgroundColor(FioUtils.getColor(this, R.color.colorBlack))
                    .titleColor(FioUtils.getColor(this, R.color.colorOrange))
                    .contentColor(FioUtils.getColor(this, R.color.colorLightOrange))
                    .widgetColor(FioUtils.getColor(this, R.color.colorLightOrange)).title("Sucesso")
                .content(text).show();
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                _sucessDialog.dismiss();
                successHandler.finish();
            }, 750);
        });
    }

    public void showSuccessDialog(String text, boolean dismiss, SuccessHandler successHandler) {
        runOnUiThread(() -> {
            _sucessDialog = new MaterialDialog.Builder(this)
                    .backgroundColor(FioUtils.getColor(this, R.color.colorBlack))
                    .titleColor(FioUtils.getColor(this, R.color.colorOrange))
                    .contentColor(FioUtils.getColor(this, R.color.colorLightOrange))
                    .widgetColor(FioUtils.getColor(this, R.color.colorLightOrange)).title("Sucesso").neutralText("OK")
                    .onNeutral((dialog, which) -> {
                        _sucessDialog.dismiss();
                        successHandler.finish();
            }).content(text).show();
        });
    }

    public void dismissLoadingDialog() {
        _dialog.dismiss();
    }

    public void showErrorDialog(String error) {
        runOnUiThread(() -> _errorDialog = new MaterialDialog.Builder(this)
                .backgroundColor(FioUtils.getColor(this, R.color.colorBlack))
                .titleColor(FioUtils.getColor(this, R.color.colorOrange))
                .contentColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .widgetColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .title("Erro")
                .content(error + ". Se o problema persistir, entrar em contato através do telefone: (61) 3202 - 5006")
                .cancelable(false)
                .positiveText("OK")
                .show());
    }

}
