package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

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
    private List<Service> _filteredServices = new ArrayList<>();
    public HashMap<String, Service> _selectedServices = new HashMap<>();

    ServicesAdapter(List<Service> services) {
        _services = services;
    }


    public void setSevices(List<Service> services) {
        _services = services;
        _selectedServices.clear();
        filterServices(services.get(0).type);

    }

    public void filterServices(String type) {
        _filteredServices = new ArrayList<>();
        for(Service service : _services) {
            if (service.type != null && service.type.equals(type)) {
                _filteredServices.add(service);
            }
        }
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
        Service service = _filteredServices.get(position);
        holder.setService(service);
        boolean isChecked = _selectedServices.containsKey(service.id);
        Log.d(service.name, String.valueOf(isChecked));
        holder.setCheckBox(isChecked, (click) -> {
            if (isChecked) {
                _selectedServices.remove(service.id);
            } else {
                _selectedServices.put(service.id, service);
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
        return _filteredServices.size();
    }
}
