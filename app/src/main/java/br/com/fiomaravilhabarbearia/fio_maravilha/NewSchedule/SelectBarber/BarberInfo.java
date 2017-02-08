package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.SelectBarber;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.MainActivity;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.AgendamentoInstance;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fraps on 07/02/17.
 */

public class BarberInfo extends Fragment {

    private Unbinder _unbinder;

    @BindView(R.id.tx_barber_name)
    TextView _name;

    @BindView(R.id.tx_barber_image)
    ImageView _image;

    @BindView(R.id.tx_idade)
    TextView _idade;

    @BindView(R.id.tx_bio)
    TextView _bio;

    @BindView(R.id.tx_servicos)
    TextView _servicos;

    Barber _barber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barber_info, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _barber = AgendamentoInstance.getInstace()._chosenBarber;
        _name.setText(_barber.name);
        _bio.setText(_barber.bio);
        setServicos(_barber);
        setAge(_barber);
        setImage(_barber);
        ((MainActivity)getActivity()).setCurrentFragmentAgendamento(this,2);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    private void setImage(Barber barber) {
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
                            height = _image.getMeasuredHeight();
                            width = (height * imageWidth)/imageHeight;
                        } else {
                            width = _image.getMeasuredWidth();
                            height = (width * imageHeight)/imageWidth;
                        }
                        _image.setImageBitmap(Bitmap.createScaledBitmap(bmp, width, height, false));
                    }
                }
            });
        }
    }

    private void setAge(Barber barber) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(barber.bornDate);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        _idade.setText(ageS + " Anos");
    }

    private void setServicos(Barber barber) {
        String services = "";
        for (int i = 0; i < barber.services.size() ; i++) {
            services += barber.services.get(i);
            if (i < barber.services.size() - 1) {
                services += ", ";
            }
        }
        _servicos.setText(services);
    }
}
