package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by fraps on 07/02/17.
 */

public class BarberAdapter extends RecyclerView.Adapter<BarberCell> {

    private ArrayList<Barber> _barbers;
    private final SelectBarber _fragment;

    private int currentSelected = -1;


    BarberAdapter(SelectBarber fragment, ArrayList<Barber> barbers) {
        _barbers = barbers;
        _fragment = fragment;
    }

    @Override
    public BarberCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_barber, parent, false);
        return new BarberCell(view);
    }

    @Override
    public void onBindViewHolder(BarberCell holder, int position) {
        holder.onBind = true;
        if (currentSelected == position) {
            holder.selectBarber();
        } else {
            holder.deselectBarber();
        }
        holder.setBarber(_barbers.get(position), v -> {
            _fragment.showBarberInfo(_barbers.get(position));
        }, (buttonView, isChecked) -> {
            if (currentSelected != -1 && currentSelected != position && !holder.onBind) {
                notifyItemChanged(currentSelected);
            } else if (currentSelected == position) {
                currentSelected = -1;
            }
            currentSelected = position;
        });
        holder.onBind = false;
    }

    public Barber getSelectedBarber() throws Exception {
        if (currentSelected == -1) {
            throw new Exception();
        }
        return _barbers.get(currentSelected);
    }

    @Override
    public int getItemCount() {
        return _barbers.size();
    }
}
