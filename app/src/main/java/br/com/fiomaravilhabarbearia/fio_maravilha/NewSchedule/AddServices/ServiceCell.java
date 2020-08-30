package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

    void  setCheckBox(boolean isChecked, CheckBox.OnClickListener check) {
        _checkbox.setChecked(isChecked);
        _checkbox.setOnClickListener(check);
    }

}
