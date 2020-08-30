package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;


/**
 * Created by Fraps on 15/12/2016.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeCell> {

    private List<String> _types;
    public String selectedType;
    BaseFragment _fragment;

    TypeAdapter(List<String> types, BaseFragment fragment) {
        _types = types;
        _fragment = fragment;
    }

    public void setTypes(List<String> types) {
        _types = types;
        selectedType = _types.get(0);
        notifyDataSetChanged();
    }

    @Override
    public TypeCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_types, parent, false);
        return new TypeCell(view);
    }

    @Override
    public void onBindViewHolder(TypeCell holder, int position) {
        String type = _types.get(position);
        boolean isCurrentType = type.equals(selectedType);
        holder.setType(type, isCurrentType, (click) -> {
            selectedType = type;
            ((AddServicesFragment) _fragment).changeType(type);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return _types.size();
    }
}
