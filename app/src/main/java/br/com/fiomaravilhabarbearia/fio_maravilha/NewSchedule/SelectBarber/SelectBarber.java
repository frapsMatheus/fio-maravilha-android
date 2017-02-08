package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
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

public class SelectBarber extends Fragment {

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
        _adapter = new BarberAdapter(this, Barbers.getInstace()._barbers);
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
            ((MainActivity)getActivity()).changeFragment(new CalendarioFragment());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
