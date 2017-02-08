package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Horarios;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 08/02/17.
 */

public class HorarioCell extends RecyclerView.ViewHolder {

    @BindView(R.id.horario_text)
    TextView _horario;

    @BindView(R.id.horario_inner_background)
    FrameLayout _innerLayout;

    @BindView(R.id.horario_outer_background)
    FrameLayout _outerLayout;

    public HorarioCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setCell(Horario horario) {
        _horario.setText(horario.horario);
    }
}
