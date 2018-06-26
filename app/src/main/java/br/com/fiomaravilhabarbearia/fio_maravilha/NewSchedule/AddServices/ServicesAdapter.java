package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;


/**
 * Created by Fraps on 15/12/2016.
 */

public class ServicesAdapter extends RecyclerView.Adapter<ServiceCell> {

    private List<Service> _services;
    public HashMap<String, Service> _selectedServices = new HashMap<>();

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
                _selectedServices.put(service.id, service);
            } else {
                _selectedServices.remove(service.id);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            holder._checkbox.setChecked(!holder._checkbox.isChecked());
            if (holder._checkbox.isChecked()) {
                _selectedServices.put(service.id, service);
            } else {
                _selectedServices.remove(service.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _services.size();
    }
}
