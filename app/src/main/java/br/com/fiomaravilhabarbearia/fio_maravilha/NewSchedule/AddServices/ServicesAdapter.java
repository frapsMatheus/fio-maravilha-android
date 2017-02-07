package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;


/**
 * Created by Fraps on 15/12/2016.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServiceCell> {

    private List<Service> _services;
    public ArrayList<Service> _selectedServices = new ArrayList<>();

    ServicesAdapter(List<Service> services) {
        _services = services;
    }


    public void setSevices(List<Service> services) {
        _services = services;
        _selectedServices.clear();
        notifyDataSetChanged();
    }

    @Override
    public ServiceCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_service, parent, false);
        return new ServiceCell(view);
    }

    @Override
    public void onBindViewHolder(ServiceCell holder, int position) {
        Service service = _services.get(position);
        holder.setService(service);
        holder.setCheckBox((buttonView, isChecked) -> {
            if (isChecked) {
                _selectedServices.add(service);
            } else {
                _selectedServices.remove(service);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _services.size();
    }
}
