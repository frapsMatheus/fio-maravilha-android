package br.com.fiomaravilhabarbearia.fio_maravilha.Feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Posts;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fraps on 08/02/17.
 */

public class FeedFragment extends Fragment implements Observer {

    @BindView(R.id.feed_recycler)
    RecyclerView _recycler;

    private Unbinder _unbinder;
    private PostAdapter _adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        _unbinder = ButterKnife.bind(this, view);
        _recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        _adapter = new PostAdapter(getActivity(),Posts.getInstace()._posts);
        _recycler.setAdapter(_adapter);
        _recycler.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        Posts.getInstace().addObserver(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    @Override
    public void update(Observable o, Object arg) {
        _adapter.setPosts(Posts.getInstace()._posts);
    }

}
