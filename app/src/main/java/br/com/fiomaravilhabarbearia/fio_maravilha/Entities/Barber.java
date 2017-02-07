package br.com.fiomaravilhabarbearia.fio_maravilha.Entities;

import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Fraps on 15/12/2016.
 */


public class Barber extends Object {

    public String id;
    public String name;
    public String bio;
    public ParseFile picture;
    public Date bornDate;
    public ArrayList<String> services;

    public Barber(ParseObject object) {
        this.id = object.getObjectId();
        this.name = object.getString("name");
        this.bio = object.getString("bio");
        JSONArray array = object.getJSONArray("services");
        services = new ArrayList<>();
        for(int i = 0, count = array.length(); i< count; i++)
        {
            try {
                String jsonObject = array.getString(i);
                services.add(jsonObject.toString());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.picture = object.getParseFile("picture");
        this.bornDate = object.getDate("born_date");
    }
}
