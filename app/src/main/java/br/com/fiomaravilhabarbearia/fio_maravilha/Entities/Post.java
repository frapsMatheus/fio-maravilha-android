package br.com.fiomaravilhabarbearia.fio_maravilha.Entities;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by fraps on 08/02/17.
 */

public class Post extends Object {

    public String id;
    public String title;
    public String text;
    public ParseFile picture;
    public Date date;

    public Post(ParseObject object) {
        this.id = object.getObjectId();
        this.title = object.getString("title");
        this.text = object.getString("text");
        this.picture = object.getParseFile("picture");
        this.date = object.getCreatedAt();
    }

}
