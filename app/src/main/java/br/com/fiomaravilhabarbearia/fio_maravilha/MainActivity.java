package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageView;

import com.roughike.bottombar.BottomBar;

import br.com.fiomaravilhabarbearia.fio_maravilha.Agenda.AgendaFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Feed.FeedFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices.AddServicesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomBar)
    BottomBar _bottomBar;

    FeedFragment _feedsFragment;

    Fragment _agendamentoFragment;

    Fragment _agendaFragment;

    Fragment _agendamentoRootFragment;

    int _currentStateAgendamento = 0;

    @BindView(R.id.activity_background)
    ImageView _backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _bottomBar.setOnTabSelectListener(tabId -> {
            Fragment fragment;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            switch (tabId) {
                case R.id.tab_news:
                    if (_feedsFragment == null) {
                        _feedsFragment = new FeedFragment();
                    }
                    fragment = _feedsFragment;
                    // f1_container is your FrameLayout container
                    break;
                case R.id.tab_schedule:
                    if (_agendamentoFragment == null) {
                        _agendamentoRootFragment = new AddServicesFragment();
                        _agendamentoFragment = _agendamentoRootFragment;
                    }
                    getFragmentManager().popBackStack(_agendamentoFragment.getClass().getName(),0);
                    fragment = _agendamentoFragment;
                    if (_currentStateAgendamento == 0) {
                        ft.addToBackStack(_agendamentoRootFragment.getClass().getName());
                    }
                    break;
                case R.id.tab_my_schedules:
                    if (_agendaFragment == null) {
                        _agendaFragment = new AgendaFragment();
                    }
                    fragment = _agendaFragment;
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

    public void goBackToAddServices() {
        String className = _agendamentoRootFragment.getClass().getName();
        getFragmentManager().popBackStackImmediate(className,0);
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
