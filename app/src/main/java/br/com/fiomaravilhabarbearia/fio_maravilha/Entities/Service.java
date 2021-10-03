package br.com.fiomaravilhabarbearia.fio_maravilha.Entities;

import com.parse.ParseObject;

/**
 * Created by Fraps on 15/12/2016.
 */

public class Service extends Object {

    public String id;
    public String name;
    public String type;
    public double price;
    public Integer duration;


    public Service(ParseObject object) {
        this.id = object.getObjectId();
        this.name = object.getString("name");
        this.price = object.getDouble("price");
        this.type = object.getString("type");
        if (this.type == null) {
            this.type = "COMBOS";
        }
        this.duration = object.getInt("duration");
    }

    public Service(String id, String name, double price, Integer duration) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

}
