package br.com.fiomaravilhabarbearia.fio_maravilha.Entities;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fraps on 08/02/17.
 */

public class Schedule extends Object {

    public String id;
    public ParseUser user;
    public Date date;
    public String state;
    public ArrayList<String> services;
    public Barber barber;
    public ArrayList<Horario> horarios;

    public Schedule(ParseObject object) {
        this.id = object.getObjectId();
        user = object.getParseUser("user");
        date = object.getDate("date");
        state = object.getString("state");
        JSONArray array = object.getJSONArray("services");
        services = new ArrayList<>();
        for(int i = 0, count = array.length(); i< count; i++) {
            try {
                services.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray arrayHorarios = object.getJSONArray("hours");
        horarios = new ArrayList<>();
        for (int j = 0, count = arrayHorarios.length(); j< count; j++) {
            try {
                horarios.add(new Horario(arrayHorarios.getString(j)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        barber = new Barber(object.getParseObject("barber"));
    }

}
