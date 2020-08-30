package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.app.Fragment;

import java.lang.reflect.Field;

/**
 * Created by fraps on 21/03/17.
 */

public class BaseFragment extends Fragment {

    private static final Field sChildFragmentManagerField;

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {

        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {

            }
        }
    }
}
