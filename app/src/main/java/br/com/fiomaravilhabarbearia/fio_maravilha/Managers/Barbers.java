package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import android.os.Handler;
import android.os.Message;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;

/**
 * Created by fraps on 07/02/17.
 */

public class Barbers extends Observable {

    public ArrayList<Barber> _barbers = new ArrayList<>();

    private static Barbers _barbersShared;
    boolean downloaded = false;

    public static Barbers getInstace() {
        if (_barbersShared == null) {
            _barbersShared = new Barbers();
        }
        return _barbersShared;
    }

    public void downloadBarbers(Collection<Service> services, Handler.Callback callback) {
        ParseQuery query = new ParseQuery("Barbers");
        ArrayList<String> stringServices = new ArrayList<>();
        for(Service service : services){
            stringServices.add(service.name);
        }
        query.whereContainsAll("services",stringServices);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                downloaded = true;
                if (e == null) {
                    _barbers.clear();
                    for (ParseObject object : objects) {
                        _barbers.add(new Barber(object));
                    }
                    Collections.shuffle(_barbers);
                    setChanged();
                    notifyObservers();
                    callback.handleMessage(new Message());
                }
            }
        });
    }
}