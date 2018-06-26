package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 26/03/17.
 */

public class DiaDoNoivo extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.toolbar_title)
    TextView _titleToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_do_noivo);
        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _titleToolbar.setText("Dia do Noivo");
        _toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
