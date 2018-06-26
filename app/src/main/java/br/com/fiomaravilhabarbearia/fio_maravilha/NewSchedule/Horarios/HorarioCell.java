package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Horarios;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
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

    public boolean onBind = false;

    public HorarioCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setCell(Context context, Horario horario) {
        String splitHorario = horario.horario.split("/")[1];
        _horario.setText(splitHorario);
        _horario.setTextColor(FioUtils.getColor(context,R.color.colorLightOrange));
        _innerLayout.setBackgroundResource(R.drawable.horario_background);
        _outerLayout.setBackgroundResource(R.drawable.horario_background_no_outline);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        if (height < 800) {
            _horario.setTextSize(12);
            _horario.setPadding(20,4,20,4);
        } else if (height < 900) {
            _horario.setTextSize(16);
            _horario.setPadding(20,4,20,4);
        }
    }

    public void setChosenHorario(Context context) {
        _horario.setTextColor(FioUtils.getColor(context,R.color.colorBlack));
        _innerLayout.setBackgroundResource(R.drawable.horario_selected_background);
        _outerLayout.setBackgroundResource(R.drawable.horario_selected_background_no_outline);
    }
}
