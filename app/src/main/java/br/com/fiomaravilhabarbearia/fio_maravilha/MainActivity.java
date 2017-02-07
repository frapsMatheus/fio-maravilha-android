package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;

import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices.AddServicesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomBar)
    BottomBar _bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _bottomBar.setOnTabSelectListener(tabId -> {
            AddServicesFragment f1 = new AddServicesFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_container, f1); // f1_container is your FrameLayout container
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        });
    }

    public void changeFragment(Fragment fragment, String fragmentTag) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left,
                R.animator.enter_from_left,R.animator.exit_to_right);
        ft.replace(R.id.main_container, fragment, fragmentTag);
        ft.addToBackStack(fragmentTag);
        ft.commit();
    }
}
