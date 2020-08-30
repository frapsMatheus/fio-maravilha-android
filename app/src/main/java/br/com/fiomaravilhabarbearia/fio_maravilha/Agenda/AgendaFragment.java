package br.com.fiomaravilhabarbearia.fio_maravilha.Agenda;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Schedule;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.ErrorManager;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Schedules;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fraps on 10/02/17.
 */

public class AgendaFragment extends BaseFragment implements Observer {

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
        _adapter = new AgendaAdapter(this,new ArrayList<>(), new ArrayList<>());
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
        if (_refresh != null) {
            _refresh.setRefreshing(false);
            _adapter.setData(Schedules.getInstace()._proximos, Schedules.getInstace()._history);
        }
    }

    public void showDetailsDialog(Schedule schedule, boolean isHistory) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(R.layout.agendamento_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView _dialogDate = (TextView)dialog.findViewById(R.id.dialog_date);
        final SimpleDateFormat spDate = new SimpleDateFormat("dd/MM/yy");
        _dialogDate.setText(spDate.format(schedule.date));
        TextView _dialogHour = (TextView)dialog.findViewById(R.id.dialog_hour);
        _dialogHour.setText(schedule.horarios.get(0).horario.split("/")[1]);
        TextView _dialogServices = (TextView)dialog.findViewById(R.id.dialog_servicos);
        String services = "";
        for (String service : schedule.services) {
            services += service +", ";
        }
        _dialogServices.setText(services.substring(0, services.length()-2));
        TextView _dialogBarber = (TextView)dialog.findViewById(R.id.dialog_barbeiro);
        _dialogBarber.setText(schedule.barber.name);
        Button _dialogCancel = (Button)dialog.findViewById(R.id.dialog_cancel);
        if (schedule.state.equals("Finalizado")) {
            _dialogCancel.setVisibility(View.GONE);
        } else {
            _dialogCancel.setVisibility(View.VISIBLE);
        }
        _dialogCancel.setOnClickListener(v -> {
            HashMap<String, String> params = new HashMap();
            params.put("scheduleId", schedule.id);
            ParseCloud.callFunctionInBackground("Cancelamento", params, new FunctionCallback<String>() {
                @Override
                public void done(String response, ParseException e) {
                    if (e == null) {
                        Schedules.getInstace().downloadSchedules();
                        dialog.dismiss();
                        ((BaseActivity)getActivity()).showDialog("Cancelamento realizado", "O cancelamento foi realizado com sucesso");
                    } else {
                        ((BaseActivity)getActivity()).showDialog("Cancelamento", e.getMessage() + " Caso queira cancelar ou trocar o seu horário, por favor, ligue no 3202-5006. Obrigado!");
                    }
                }
            });
        });
        Button _dialogClose = (Button)dialog.findViewById(R.id.toolbar_cancel);
        _dialogClose.setOnClickListener(v -> dialog.dismiss());
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        if (height < 800) {
            _dialogBarber.setTextSize(16);
            _dialogDate.setTextSize(16);
            _dialogServices.setTextSize(16);
            _dialogHour.setTextSize(16);
        } else if (height < 900) {
            _dialogBarber.setTextSize(18);
            _dialogDate.setTextSize(18);
            _dialogServices.setTextSize(18);
            _dialogHour.setTextSize(18);
        }
        dialog.show();
    }

}
