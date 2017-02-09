package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class CalendarioFragment extends Fragment {

    private Unbinder _unbinder;

    @BindView(R.id.tx_calendar)
    CalendarView _calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);
        _unbinder = ButterKnife.bind(this, view);
        ((MainActivity)getActivity()).setCurrentFragmentAgendamento(this,3);
        _calendar.setEventHandler(date -> {
            _calendar.selectDate(date);
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    @OnClick(R.id.tx_proximo)
    public void proximo() {
        AgendamentoInstance.getInstace()._chosendDate = _calendar._selectedDate;
        Horarios.getInstace().getHorarios(AgendamentoInstance.getInstace()._chosenBarber,
                AgendamentoInstance.getInstace()._chosendDate, msg -> {
                    ((MainActivity)getActivity()).changeFragment(new HorariosFragment());
                    return true;
                });
    }
}
