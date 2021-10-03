package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Schedule;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioAnalytics;

/**
 * Created by fraps on 10/02/17.
 */

public class Schedules extends Observable {

    public ArrayList<Schedule> _proximos = new ArrayList<>();
    public ArrayList<Schedule> _history = new ArrayList<>();

    private static Schedules _shared;
    boolean downloaded = false;

    public static Schedules getInstace() {
        if (_shared == null) {
            _shared = new Schedules();
            _shared.downloadSchedules();
        }
        return _shared;
    }

    public void kill() {
        _shared = null;
    }

    public void downloadSchedules() {
        ParseQuery query = new ParseQuery("Schedules");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("barber");
        query.whereContainedIn("state", Arrays.asList("Criado","Finalizado", "Fidelizi"));
        query.orderByAscending("date");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                downloaded = true;
                if (e == null) {
                    _proximos = new ArrayList<>();
                    _history = new ArrayList<>();
                    for (ParseObject object : objects) {
                        if (object.getParseObject("barber") != null) {
                            Schedule newSchedule = new Schedule(object);
                            if (newSchedule.state.equals("Criado")) {
                                _proximos.add(newSchedule);
                            } else {
                                _history.add(newSchedule);
                            }
                        }
                    }
                    setChanged();
                    notifyObservers();
                    FioAnalytics.logSimpleEvent("Baixou agendamentos");
                } else {
                    FioAnalytics.logError("Agendamentos", e.getLocalizedMessage(), e);
                }
            }
        });
    }

    public void removeSchedule(Schedule schedule) {
        _proximos.remove(schedule);
        setChanged();
        notifyObservers();
    }

    private boolean isHistory(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar chosenCal = Calendar.getInstance();
        chosenCal.setTime(date);
        return chosenCal.getTimeInMillis() < (c.getTimeInMillis() - 3600 * 1000);
    }

    public void clear() {
        _proximos.clear();
        _history.clear();
        downloaded = false;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        if (downloaded) {
            setChanged();
            notifyObservers(o);
        }
    }
}
