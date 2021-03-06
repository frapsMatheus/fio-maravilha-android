package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;

import java.util.List;

import br.com.fiomaravilhabarbearia.fio_maravilha.Agenda.AgendaFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Post;
import br.com.fiomaravilhabarbearia.fio_maravilha.Feed.FeedFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Inicial.Login;
import br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.AddServices.AddServicesFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.Options.Options;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottomBar)
    BottomBar _bottomBar;

    FeedFragment _feedsFragment;

    Fragment _agendamentoFragment;

    Fragment _agendaFragment;

    Fragment _agendamentoRootFragment;

    Fragment _optionsFragment;

    int _currentStateAgendamento = 0;

    @BindView(R.id.activity_background)
    ImageView _backgroundImage;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.toolbar_title)
    TextView _titleToolbar;

    @BindView(R.id.toolbar_description)
    TextView _descriptionToolbar;

    @BindView(R.id.toolbar_ajuda)
    Button _ajudaToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        _ajudaToolbar.setOnClickListener(v -> {
            switch (_bottomBar.getCurrentTabId()) {
                case R.id.tab_schedule:
                    showInfoDialog(R.layout.dialog_agendamento_info);
                    break;
                case R.id.tab_my_schedules:
                    showInfoDialog(R.layout.dialog_agenda_info);
                    break;
            }
        });
        _bottomBar.setOnTabSelectListener(tabId -> {
            Fragment fragment;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            _ajudaToolbar.setVisibility(View.GONE);
            switch (tabId) {
                case R.id.tab_news:
                    if (_feedsFragment == null) {
                        _feedsFragment = new FeedFragment();
                    }
                    fragment = _feedsFragment;
                    _titleToolbar.setText("Home");
                    _descriptionToolbar.setText("Feed de Not??cias");
                    // f1_container is your FrameLayout container
                    break;
                case R.id.tab_schedule:
                    _ajudaToolbar.setVisibility(View.VISIBLE);
                    if (_agendamentoFragment == null) {
                        _agendamentoRootFragment = new AddServicesFragment();
                        _agendamentoFragment = _agendamentoRootFragment;
                    }
                    getFragmentManager().popBackStack(_agendamentoFragment.getClass().getName(),0);
                    fragment = _agendamentoFragment;
                    if (_currentStateAgendamento == 0 && _agendamentoRootFragment != null) {
                        ft.addToBackStack(_agendamentoRootFragment.getClass().getName());
                    } else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                    }
                    _titleToolbar.setText("Agendar");
                    _descriptionToolbar.setText("Agende Seu Hor??rio");
                    break;
                case R.id.tab_my_schedules:
                    _ajudaToolbar.setVisibility(View.VISIBLE);
                    if (_agendaFragment == null) {
                        _agendaFragment = new AgendaFragment();
                    }
                    fragment = _agendaFragment;
                    _titleToolbar.setText("Minha Agenda");
                    _descriptionToolbar.setText("Acompanhe Seus Hor??rios");
                    break;
                default:
                    if (_optionsFragment == null) {
                        _optionsFragment = new Options();
                    }
                    fragment = _optionsFragment;
                    _titleToolbar.setText("Op????es");
                    _descriptionToolbar.setText("Configura????es e Contato");
            }
            ft.replace(R.id.main_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });
        checkToken();
    }
    public void changeFragment(Fragment fragment) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String backStateName = fragment.getClass().getName();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left,
                R.animator.enter_from_left,R.animator.exit_to_right);
        ft.replace(R.id.main_container, fragment, backStateName);
        ft.addToBackStack(backStateName);
        ft.commitAllowingStateLoss();
    }

    public void setCurrentFragmentAgendamento(Fragment fragment, int tag) {
        _agendamentoFragment = fragment;
        _currentStateAgendamento = tag;
    }

    public void goBackToAddServices() {
        if (_agendamentoRootFragment != null) {
            String className = _agendamentoRootFragment.getClass().getName();
            getFragmentManager().popBackStackImmediate(className, 0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    private void checkToken() {
        ParseQuery query = new ParseQuery("Posts");
        query.orderByDescending("createdAt");
        Activity activity = this;
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e != null && e.getCode() == 209) {
                    FioUtils.logout(activity);
                }
                Log.d("Teste", "Teste");
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (_bottomBar.getCurrentTabId()) {
            case R.id.tab_schedule:
                if (_currentStateAgendamento > 0) {
                    if (_currentStateAgendamento == 1) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setDisplayShowHomeEnabled(false);
                    }
                    super.onBackPressed();
                }
                break;
            default:
                break;
        }
    }

    public void showInfoDialog(int layoutId) {
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_frame);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        FrameLayout container = (FrameLayout) dialog.findViewById(R.id.dialog_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        View inflatedLayout= inflater.inflate(layoutId, null);
        container.addView(inflatedLayout);
        ImageButton _cancel = (ImageButton)dialog.findViewById(R.id.dialog_cancel);
        _cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
