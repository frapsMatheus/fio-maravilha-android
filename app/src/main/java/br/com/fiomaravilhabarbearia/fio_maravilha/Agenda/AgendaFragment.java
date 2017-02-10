package br.com.fiomaravilhabarbearia.fio_maravilha.Agenda;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Schedules;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fraps on 10/02/17.
 */

public class AgendaFragment extends Fragment implements Observer {

    private Unbinder _unbinder;

    @BindView(R.id.tx_recyclerView)
    RecyclerView _recyclerView;
    private AgendaAdapter _adapter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout _refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new AgendaAdapter(new ArrayList<>(), new ArrayList<>());
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        Schedules.getInstace().addObserver(this);
        _refresh.setProgressBackgroundColorSchemeColor(FioUtils.getColor(getActivity(),R.color.colorBlack));
        _refresh.setColorSchemeColors(FioUtils.getColor(getActivity(),R.color.colorLightOrange));
        _refresh.setOnRefreshListener(() -> {
            Schedules.getInstace().downloadSchedules();
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    @Override
    public void update(Observable o, Object arg) {
        _refresh.setRefreshing(false);
        _adapter.setData(Schedules.getInstace()._proximos,Schedules.getInstace()._history);
    }
}
