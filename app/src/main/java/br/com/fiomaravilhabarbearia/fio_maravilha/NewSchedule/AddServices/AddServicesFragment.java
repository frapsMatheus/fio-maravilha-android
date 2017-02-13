package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.AgendamentoInstance;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Barbers;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Services;
import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber.SelectBarber;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Fraps on 15/12/2016.
 */

public class AddServicesFragment extends Fragment implements Observer {

    private Unbinder _unbinder;

    @BindView(R.id.services_recyclerView)
    RecyclerView _recyclerView;
    private ServicesAdapter _adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_services, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new ServicesAdapter(Services.getInstace()._services);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        Services.getInstace().addObserver(this);
        ((MainActivity)getActivity()).setCurrentFragmentAgendamento(this,0);
        return view;
    }

    @OnClick(R.id.tx_proximo)
    public void proximo() {
        if (_adapter._selectedServices.isEmpty()) {
            ((BaseActivity)getActivity()).showErrorDialog("Escolha ao menos um serviço para continuar");
            return;
        }
        ((BaseActivity)getActivity()).showLoadingDialog();
        Barbers.getInstace().downloadBarbers(_adapter._selectedServices, msg -> {
            ((BaseActivity)getActivity()).dismissLoadingDialog();
            AgendamentoInstance.getInstace()._chosenServices =
                    new ArrayList<>(_adapter._selectedServices);
            ((MainActivity)getActivity()).changeFragment(new SelectBarber());
            return true;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    @Override
    public void update(Observable o, Object arg) {
        _adapter.setSevices(Services.getInstace()._services);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
