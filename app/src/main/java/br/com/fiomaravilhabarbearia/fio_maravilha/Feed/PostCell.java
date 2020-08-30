package br.com.fiomaravilhabarbearia.fio_maravilha.Feed;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Post;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 08/02/17.
 */

public class PostCell extends RecyclerView.ViewHolder {

    @BindView(R.id.feed_image)
    ImageView _image;

    @BindView(R.id.feed_text)
    TextView _text;

    @BindView(R.id.feed_title)
    TextView _title;

    @BindView(R.id.feed_date)
    TextView _date;

    boolean imageLoaded = false;

    public PostCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setPost(Activity context, Post post) {
        _text.setText(post.text);
        _title.setText(post.title);
        SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
        _date.setText(spf.format(post.date));
        if (post.picture != null) {
            String url = post.picture.getUrl();
            Picasso.with(context).load(url).into(_image);
        }
    }

}
