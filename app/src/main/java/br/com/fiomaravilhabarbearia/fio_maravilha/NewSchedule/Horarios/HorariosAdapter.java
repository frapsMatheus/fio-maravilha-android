package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Horarios;

import android.content.Context;
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
    private int chosenHorario = -1;
    private final Context _context;

    public HorariosAdapter(Context context, ArrayList<Horario> horarios) {
        _horarios = horarios;
        _context = context;
    }

    @Override
    public HorarioCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_horario, parent, false);
        return new HorarioCell(view);
    }

    @Override
    public void onBindViewHolder(HorarioCell holder, int position) {
        holder.onBind = true;
        holder.setCell(_context,_horarios.get(position));
        if (position == chosenHorario) {
            holder.setChosenHorario(_context);
        }
        holder.itemView.setOnClickListener(v -> {
            holder.setChosenHorario(_context);
            if (chosenHorario != -1 && chosenHorario != position && !holder.onBind) {
                notifyItemChanged(chosenHorario);
            } else if (chosenHorario == position) {
                chosenHorario = -1;
            }
            chosenHorario = position;
        });
        holder.onBind = false;
    }

    @Override
    public int getItemCount() {
        return _horarios.size();
    }

    public Horario getChosenHorario() throws Exception{
        if (chosenHorario == -1) {
            throw new Exception();
        }
        return _horarios.get(chosenHorario);
    }
}
