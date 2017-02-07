package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fraps on 15/12/2016.
 */

class ServiceCell extends RecyclerView.ViewHolder {

    @BindView(R.id.tx_service)
    TextView _serviceName;

    @BindView(R.id.tx_service_price)
    TextView _servicePrice;

    @BindView(R.id.tx_checkbox)
    CheckBox _checkbox;

    ServiceCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    void setService(Service service) {
        _serviceName.setText(service.name);
        _servicePrice.setText("R$" + String.format("%.2f",service.price));
    }

    void  setCheckBox(CheckBox.OnCheckedChangeListener check) {
        _checkbox.setOnCheckedChangeListener(check);
    }

}
