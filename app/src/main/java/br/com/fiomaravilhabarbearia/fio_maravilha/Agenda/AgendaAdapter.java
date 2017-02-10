package br.com.fiomaravilhabarbearia.fio_maravilha.Agenda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Post;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Schedule;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by fraps on 10/02/17.
 */

public class AgendaAdapter extends RecyclerView.Adapter {

    private static int HEADER = 011;
    private static int CELL = 012;
    private ArrayList<Schedule> _proximos;
    private ArrayList<Schedule> _history;

    AgendaAdapter(ArrayList<Schedule> proximos, ArrayList<Schedule> history) {
        _proximos = proximos;
        _history = history;
    }

    public void setData(ArrayList<Schedule> proximos, ArrayList<Schedule> history) {
        _proximos = proximos;
        _history = history;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_agenda, parent, false);
            return new AgendaHeader(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_agenda, parent, false);
            return new AgendaCell(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == _proximos.size() + 1) {
            return HEADER;
        } else {
            return CELL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == HEADER) {
            if (position == 0) {
                ((AgendaHeader)holder).setTitle("PRÓXIMOS HORÁRIOS");
            } else  {
                ((AgendaHeader)holder).setTitle("HISTÓRICO");
            }
        } else {
            if (position <= _proximos.size()) {
                ((AgendaCell)holder).setAgenda(_proximos.get(position-1).date);
            } else  {
                ((AgendaCell)holder).setAgenda(_history.get(position-2-_proximos.size()).date);
            }
        }
    }

    @Override
    public int getItemCount() {
        return _proximos.size() + _history.size() + 2;
    }
}
