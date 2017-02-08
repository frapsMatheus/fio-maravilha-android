package br.com.fiomaravilhabarbearia.fio_maravilha.Feed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public PostCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setPost(Post post) {
        _text.setText(post.text);
        _title.setText(post.title);
        SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
        _date.setText(spf.format(post.date));
        if (post.picture != null) {
            post.picture.getDataInBackground((data, e) -> {
                if (e == null) {
                    if (post.picture != null) {
                        Bitmap bmp = BitmapFactory
                                .decodeByteArray(
                                        data, 0,
                                        data.length);
                        int imageWidth = bmp.getWidth();
                        int imageHeight = bmp.getHeight();
                        int width;
                        int height;
                        width = _image.getMeasuredWidth();
                        height = (width * imageHeight)/imageWidth;
                        _image.setImageBitmap(Bitmap.createScaledBitmap(bmp, width, height, false));
                    }
                }
            });
        }
    }

}
