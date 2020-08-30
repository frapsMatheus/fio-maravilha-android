package br.com.fiomaravilhabarbearia.fio_maravilha.Agenda;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 10/02/17.
 */

public class AgendaHeader extends RecyclerView.ViewHolder {

    @BindView(R.id.header_agenda_title)
    TextView _title;

    public AgendaHeader(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setTitle(String title) {
        _title.setText(title);
    }
}
