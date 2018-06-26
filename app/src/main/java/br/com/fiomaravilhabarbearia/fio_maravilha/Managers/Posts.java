package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Post;

/**
 * Created by fraps on 08/02/17.
 */

public class Posts extends Observable {

    public ArrayList<Post> _posts = new ArrayList<>();

    private static Posts _shared;
    boolean downloaded = false;

    public static Posts getInstace() {
        if (_shared == null) {
            _shared = new Posts();
            _shared.downloadPosts();
        }
        return _shared;
    }

    public void downloadPosts() {
        ParseQuery query = new ParseQuery("Posts");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                downloaded = true;
                if (e == null) {
                    _posts.clear();
                    for (ParseObject object : objects) {
                        _posts.add(new Post(object));
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
