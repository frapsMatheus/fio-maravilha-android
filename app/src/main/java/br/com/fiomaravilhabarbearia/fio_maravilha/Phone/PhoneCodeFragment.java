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

public class PhoneCodeFragment extends Fragment {

    @BindView(R.id.code_input)
    EditText _input;

    private Unbinder _unbinder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_phone_code, container, false);
        _unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.code_send)
    public void sendPhone() {
        String codigo = _input.getText().toString();
        if (codigo.length() < 6) {
            ((PhoneInputActivity)getActivity()).showErrorDialog("O código digitado não é válido");
            return;
        }
        ((PhoneInputActivity)getActivity()).showLoadingDialog();
        ((PhoneInputActivity)getActivity()).setCodigo(codigo);
    }

    @OnClick(R.id.code_resend)
    public void resendPhone() {
        FirebaseAuth.getInstance().signOut();
        ((PhoneInputActivity)getActivity()).showLoadingDialog();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ((PhoneInputActivity)getActivity()).mPhone,        // Phone number to verify
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
