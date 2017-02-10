package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Schedule;

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

    public void downloadSchedules() {
        ParseQuery query = new ParseQuery("Schedules");
//        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                downloaded = true;
                if (e == null) {
                    _proximos = new ArrayList<>();
                    _history = new ArrayList<>();
                    for (ParseObject object : objects) {
                        Schedule newSchedule = new Schedule(object);
                        if (isHistory(newSchedule.date)) {
                            _history.add(newSchedule);
                        } else {
                            _proximos.add(newSchedule);
                        }
                    }
                    setChanged();
                    notifyObservers();
                }
            }
        });
    }

    private boolean isHistory(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar chosenCal = Calendar.getInstance();
        chosenCal.setTime(date);
        return chosenCal.getTimeInMillis() < (c.getTimeInMillis() - 3600 * 1000);
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
