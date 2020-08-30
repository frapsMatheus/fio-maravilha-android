package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.AgendamentoInstance;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Barbers;
import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Calendar.CalendarioFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fraps on 07/02/17.
 */

public class SelectBarber extends BaseFragment {

    private Unbinder _unbinder;

    @BindView(R.id.services_recyclerView)
    RecyclerView _recyclerView;
    private BarberAdapter _adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_barber, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new BarberAdapter(getActivity(), this, Barbers.getInstace()._barbers);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        ((MainActivity)getActivity()).setCurrentFragmentAgendamento(this,1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    public void showBarberInfo(Barber barber) {
        AgendamentoInstance.getInstace()._chosenBarber = barber;
        ((MainActivity)getActivity()).changeFragment(new BarberInfo());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick(R.id.tx_proximo)
    public void proximo() {
        try {
            AgendamentoInstance.getInstace()._chosenBarber = _adapter.getSelectedBarber();
            try {
                JSONObject props = new JSONObject();
                props.put("Barbeiro", AgendamentoInstance.getInstace()._chosenBarber.name);
            } catch (JSONException e) {
            }
            ((MainActivity)getActivity()).changeFragment(new CalendarioFragment());
        } catch (Exception e) {
            ((BaseActivity)getActivity()).showErrorDialog("Escolha um barbeiro para continuar o agendamento");
            e.printStackTrace();
        }
    }

}
