package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
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

public class AddServicesFragment extends BaseFragment implements Observer {

    private Unbinder _unbinder;

    @BindView(R.id.services_recyclerView)
    RecyclerView _recyclerView;

    @BindView(R.id.type_recyclerView)
    RecyclerView _typesRecyclerView;

    private ServicesAdapter _adapter;
    private TypeAdapter _typesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_services, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new ServicesAdapter(Services.getInstace()._services);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);

        _typesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        _typesAdapter = new TypeAdapter(Services.getInstace()._types, this);
        _typesRecyclerView.setAdapter(_typesAdapter);

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
        Barbers.getInstace().downloadBarbers(_adapter._selectedServices.values(), msg -> {
            ((BaseActivity)getActivity()).dismissLoadingDialog();
            AgendamentoInstance.getInstace()._chosenServices =
                    new ArrayList<>(_adapter._selectedServices.values());
            try {
                JSONObject props = new JSONObject();
                props.put("Servicos", AgendamentoInstance.getInstace()._chosenServices);
            } catch (JSONException e) {
            }
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
        _typesAdapter.setTypes(Services.getInstace()._types);
    }

    public void changeType(String type) {
        _adapter.filterServices(type);
        _recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
