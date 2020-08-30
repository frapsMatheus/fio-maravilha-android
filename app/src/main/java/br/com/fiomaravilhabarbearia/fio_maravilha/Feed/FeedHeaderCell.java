package br.com.fiomaravilhabarbearia.fio_maravilha.Feed;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fraps on 08/02/17.
 */

public class FeedHeaderCell extends RecyclerView.ViewHolder {

    @BindView(R.id.feed_header_date)
    TextView _currentDate;

    public FeedHeaderCell(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat spf = new SimpleDateFormat("EEEE,\ndd 'de' MMMM");
        _currentDate.setText(spf.format(currentDate).toUpperCase());
    }
}
