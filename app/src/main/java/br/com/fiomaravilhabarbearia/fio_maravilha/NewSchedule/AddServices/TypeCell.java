package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices;

import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fraps on 15/12/2016.
 */

class TypeCell extends RecyclerView.ViewHolder {

    @BindView(R.id.type_text)
    TextView _typeName;

    @BindView(R.id.type_inner_background)
    FrameLayout _frame;

    TypeCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    void setType(String type, boolean selected, View.OnClickListener click) {
        _typeName.setText(type);
        if (selected) {
            _frame.setBackgroundResource(R.drawable.type_selected);
        } else {
            _frame.setBackgroundResource(R.drawable.horario_background);
        }
        _frame.setOnClickListener(click);
    }
}
