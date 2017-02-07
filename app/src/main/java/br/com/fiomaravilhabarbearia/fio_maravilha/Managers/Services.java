package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;

/**
 * Created by fraps on 07/02/17.
 */

public class Services extends Observable {

    public ArrayList<Service> _services = new ArrayList<>();

    private static Services _servicesShared = null;

    boolean downloaded = false;

    public static Services getInstace() {
        if (_servicesShared == null) {
            _servicesShared = new Services();
            _servicesShared.downloadServices();
        }
        return _servicesShared;
    }

    public void clean() {
        _services.clear();
    }

    public void downloadServices() {
        ParseQuery query = new ParseQuery("Services");
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                downloaded = true;
                if (e == null) {
                    _services.clear();
                    for (ParseObject object : objects) {
                        _services.add(new Service(object));
                    }
                    setChanged();
                    notifyObservers();
                }
            }
        });
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
