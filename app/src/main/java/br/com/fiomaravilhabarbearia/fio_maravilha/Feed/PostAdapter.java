package br.com.fiomaravilhabarbearia.fio_maravilha.Feed;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Post;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by fraps on 08/02/17.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Post> _posts;
    private Activity _context;

    PostAdapter(Activity context, ArrayList<Post> posts){
        _posts = posts;
        _context = context;
    }

    public void setPosts(ArrayList<Post> posts) {
        _posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_feed_header, parent, false);
            return new FeedHeaderCell(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_feed, parent, false);
            return new PostCell(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0) {
            ((PostCell)holder).setPost(_context,_posts.get(position-1));
        }
    }

    @Override
    public int getItemCount() {
        return _posts.size() + 1;
    }
}
