package br.com.fiomaravilhabarbearia.fio_maravilha.Options;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Inicial.Login;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Schedules;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fraps on 13/02/17.
 */

public class Options extends BaseFragment {

    private Unbinder _unbinder;

    @BindView(R.id.phone_button)
    Button _phoneButton;

    @BindView(R.id.dia_do_noivo)
    Button _diaDoNoivo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _phoneButton.setPaintFlags(_phoneButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        _diaDoNoivo.setPaintFlags(_phoneButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _unbinder.unbind();
    }

    @OnClick(R.id.phone_button)
    public void dialNumber() {
        String phone = "+556132025006";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @OnClick(R.id.options_fb_button)
    public void fbLink() {
        String url = "https://www.facebook.com/fiomaravilhabarbearia/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.options_insta_button)
    public void instaLink() {
        String url = "https://www.instagram.com/fiomaravilhabarbearia/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.options_email_button)
    public void emailLink() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

        getActivity().startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
    }

    @OnClick(R.id.dia_do_noivo)
    public void diaDoNoivo() {
        String phone = "+556132025006";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @OnClick(R.id.options_logout)
    public void logout() {
        ParseUser.logOutInBackground();
        Schedules.getInstace().clear();
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
