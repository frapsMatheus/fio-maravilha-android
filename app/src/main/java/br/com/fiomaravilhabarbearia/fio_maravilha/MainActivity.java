package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.roughike.bottombar.BottomBar;

import br.com.fiomaravilhabarbearia.fio_maravilha.Feed.FeedFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices.AddServicesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomBar)
    BottomBar _bottomBar;

    FeedFragment _feedsFragment;

    Fragment _agendamentoFragment;
    int _currentStateAgendamento = 0;

    @BindView(R.id.activity_background)
    ImageView _backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _feedsFragment = new FeedFragment();
        _agendamentoFragment = new AddServicesFragment();
        _bottomBar.setOnTabSelectListener(tabId -> {
            Fragment fragment;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            switch (tabId) {
                case R.id.tab_news:
                    fragment = _feedsFragment; // f1_container is your FrameLayout container
                    break;
                case R.id.tab_schedule:
                    getFragmentManager().popBackStack(_agendamentoFragment.getClass().getName(),0);
                    fragment = _agendamentoFragment;
                    if (_currentStateAgendamento == 0) {
                        ft.addToBackStack(null);
                    }
                    break;
                default:
                    fragment = _agendamentoFragment;
            }
            ft.replace(R.id.main_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void changeFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left,
                R.animator.enter_from_left,R.animator.exit_to_right);
        ft.replace(R.id.main_container, fragment, backStateName);
        ft.addToBackStack(backStateName);
        ft.commit();
    }

    public void setCurrentFragmentAgendamento(Fragment fragment, int tag) {
        _agendamentoFragment = fragment;
        _currentStateAgendamento = tag;
    }

    @Override
    public void onBackPressed() {
        switch (_bottomBar.getCurrentTabId()) {
            case R.id.tab_schedule:
                if (_currentStateAgendamento > 0) {
                    super.onBackPressed();
                }
                break;
            default:
                break;
        }
    }
}
