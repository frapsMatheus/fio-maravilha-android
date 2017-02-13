package br.com.fiomaravilhabarbearia.fio_maravilha.Options;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fraps on 13/02/17.
 */

public class Options extends Fragment {

    private Unbinder _unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        _unbinder = ButterKnife.bind(this, view);
        return view;
    }
}
