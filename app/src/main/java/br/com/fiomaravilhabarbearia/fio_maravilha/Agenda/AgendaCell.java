package br.com.fiomaravilhabarbearia.fio_maravilha.Agenda;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 10/02/17.
 */

public class AgendaCell extends RecyclerView.ViewHolder  {

    @BindView(R.id.agenda_hora)
    TextView _agendaHora;

    @BindView(R.id.agenda_data)
    TextView _agendaData;

    private final SimpleDateFormat spf = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
    private final SimpleDateFormat spHour = new SimpleDateFormat("HH':'mm");

    public AgendaCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setAgenda(Date date) {
        _agendaData.setText(FioUtils.capitalize(spf.format(date).replace("-feira","")).replace("De","de"));
        _agendaHora.setText(spHour.format(date));
    }
}
