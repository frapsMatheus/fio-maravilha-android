package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 07/02/17.
 */

public class BarberCell extends RecyclerView.ViewHolder {

    @BindView(R.id.tx_barber)
    TextView _barberName;

    @BindView(R.id.tx_barber_profile)
    TextView _barberButton;

    @BindView(R.id.profile_image)
    ImageView _barberImage;

    @BindView(R.id.tx_checkbox)
    CheckBox _checkbox;

    public boolean onBind = false;

    BarberCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setBarber(Barber barber, View.OnClickListener click, CheckBox.OnCheckedChangeListener checked) {
        _barberName.setText(barber.name);
        String udata="Ver Perfil";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        _barberButton.setText(content);
        _barberButton.setOnClickListener(click);
        if (barber.picture != null) {
            barber.picture.getDataInBackground((data, e) -> {
                if (e == null) {
                    if (barber.picture != null) {
                        Bitmap bmp = BitmapFactory
                                .decodeByteArray(
                                        data, 0,
                                        data.length);
                        int imageWidth = bmp.getWidth();
                        int imageHeight = bmp.getHeight();
                        int width;
                        int height;
                        if (imageWidth> imageHeight) {
                            height = _barberImage.getMeasuredHeight();
                            width = (height * imageWidth)/imageHeight;
                        } else {
                            width = _barberImage.getMeasuredWidth();
                            height = (width * imageHeight)/imageWidth;
                        }
                        _barberImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, width, height, false));
                    }
                }
            });
        }
        _checkbox.setOnCheckedChangeListener(checked);
    }

    public void selectBarber() {
        _checkbox.setChecked(true);
    }

    public void deselectBarber() {
        _checkbox.setChecked(false);
    }
}
