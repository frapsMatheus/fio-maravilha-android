package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Horarios;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by fraps on 08/02/17.
 */

public class HorariosAdapter extends RecyclerView.Adapter<HorarioCell> {


    private ArrayList<Horario> _horarios;

    public HorariosAdapter(ArrayList<Horario> horarios) {
        _horarios = horarios;
    }

    @Override
    public HorarioCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_horario, parent, false);
        return new HorarioCell(view);
    }

    @Override
    public void onBindViewHolder(HorarioCell holder, int position) {
//        holder.setCell(_horarios.get(position));
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
