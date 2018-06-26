package br.com.fiomaravilhabarbearia.fio_maravilha.Phone;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fraps on 02/08/17.
 */

public class PhoneInputFragment extends Fragment {

    @BindView(R.id.phone_input)
    EditText _input;

    private Unbinder _unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_phone_input, container, false);
        _unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.phone_send)
    public void sendPhone() {
        String phone = "+55" + _input.getText().toString().replace("(","").replace(")","")
                .replace("-","").replace(" ","");
        if (phone.length() < 13) {
            ((PhoneInputActivity)getActivity()).showErrorDialog("O número de telefone digitado não é válido");
            return;
        }
        ((PhoneInputActivity)getActivity()).showLoadingDialog();
        FirebaseAuth.getInstance().signOut();
        ((PhoneInputActivity)getActivity()).mPhone = phone;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                // OnVerificationStateChangedCallbacks
                ((PhoneInputActivity)getActivity()).mCallbacks);
    }

    @Override
    public void onDestroy() {
        _unbinder.unbind();
        super.onDestroy();
    }

}
