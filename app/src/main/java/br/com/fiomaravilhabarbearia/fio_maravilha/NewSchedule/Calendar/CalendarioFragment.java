package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.AgendamentoInstance;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Horarios;
import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Horarios.HorariosFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fraps on 08/02/17.
 */

public class CalendarioFragment extends BaseFragment {

    private Unbinder _unbinder;

    @BindView(R.id.tx_calendar)
    CalendarView _calendar;

    @BindView(R.id.tx_pick_date_label)
    TextView _dateLabel;

    @BindView(R.id.tx_passo_label)
    TextView _passoLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);
        _unbinder = ButterKnife.bind(this, view);
        ((MainActivity)getActivity()).setCurrentFragmentAgendamento(this,3);
        _calendar.setEventHandler(date -> {
            _calendar.selectDate(date);
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        if (height < 900) {
            _dateLabel.setVisibility(View.GONE);
            _passoLabel.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    @OnClick(R.id.tx_proximo)
    public void proximo() {
        if (_calendar.dateWasSelected == false) {
            ((BaseActivity)getActivity()).showErrorDialog("Escolha um dia para continuar o agendamento");
            return;
        }
        AgendamentoInstance.getInstace()._chosendDate = _calendar._selectedDate;
        try {
            JSONObject props = new JSONObject();
            props.put("Dia", AgendamentoInstance.getInstace()._chosendDate);
            FioUtils.getMixpanel(getActivity())
                    .track("Escolheu Dia", props);
        } catch (JSONException e) {
        }
        ((BaseActivity)getActivity()).showLoadingDialog();
        Horarios.getInstace().getHorarios(AgendamentoInstance.getInstace()._chosenBarber,
                AgendamentoInstance.getInstace()._chosendDate, msg -> {
                    ((BaseActivity)getActivity()).dismissLoadingDialog();
                    ((MainActivity)getActivity()).changeFragment(new HorariosFragment());
                    return true;
                });
    }
}
