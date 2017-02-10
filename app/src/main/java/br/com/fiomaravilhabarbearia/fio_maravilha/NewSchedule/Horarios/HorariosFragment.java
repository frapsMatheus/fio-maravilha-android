package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Horarios;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.AgendamentoInstance;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Horarios;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fraps on 08/02/17.
 */

public class HorariosFragment extends Fragment {

    @BindView(R.id.horarios_recyclerView)
    RecyclerView _recyclerView;
    private HorariosAdapter _adapter;

    @BindView(R.id.tx_passo_label)
    TextView _passoLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_horario, container, false);
        ((MainActivity)getActivity()).setCurrentFragmentAgendamento(this,4);
        ButterKnife.bind(this,view);
        _recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4,
                LinearLayoutManager.HORIZONTAL,false));
        _adapter = new HorariosAdapter(getActivity().getApplicationContext(),
                Horarios.getInstace()._horarios);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        if (height < 800) {
            _passoLabel.setVisibility(View.GONE);
        }
        return view;
    }

    @OnClick(R.id.tx_proximo)
    public void finalizarAgendamento() {
        try {
            ((BaseActivity)getActivity()).showLoadingDialog();
            Horario horario = _adapter.getChosenHorario();
            if (AgendamentoInstance.getInstace().isHorarioValid(horario)) {
                AgendamentoInstance.getInstace()._chosenHorario = horario;
                AgendamentoInstance.getInstace().createAgendamento(ParseUser.getCurrentUser(), e -> {
                    ((BaseActivity)getActivity()).dismissLoadingDialog();
                    if (e == null) {
                        AgendamentoInstance.getInstace().clean();
                        ((MainActivity)getActivity()).goBackToAddServices();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
