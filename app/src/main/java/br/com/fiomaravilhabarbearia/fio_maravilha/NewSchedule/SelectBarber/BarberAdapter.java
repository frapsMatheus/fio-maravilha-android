package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by fraps on 07/02/17.
 */

public class BarberAdapter extends RecyclerView.Adapter<BarberCell> {

    private ArrayList<Barber> _barbers;
    private final SelectBarber _fragment;


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
        holder.setBarber(_barbers.get(position), v -> {
            _fragment.showBarberInfo(_barbers.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return _barbers.size();
    }
}
