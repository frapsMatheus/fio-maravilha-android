package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by fraps on 9/7/16.
 */
public class BaseActivity extends AppCompatActivity {

    private MaterialDialog _dialog;
    private MaterialDialog _sucessDialog;
    private MaterialDialog _errorDialog;
    private MaterialDialog _newDialog;

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

    public void showDialog(String title, String texto) {
        runOnUiThread(() -> _newDialog = new MaterialDialog.Builder(this)
                .backgroundColor(FioUtils.getColor(this, R.color.colorBlack))
                .titleColor(FioUtils.getColor(this, R.color.colorOrange))
                .contentColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .widgetColor(FioUtils.getColor(this, R.color.colorLightOrange))
                .title(title)
                .content(texto)
                .cancelable(false)
                .negativeText("Fechar")
                .positiveText("(61) 3202 - 5006")
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:6132025006"));
                    startActivity(intent);
                })
                .show());
    }

}
