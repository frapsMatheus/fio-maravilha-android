package br.com.fiomaravilhabarbearia.fio_maravilha.Feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Observable;
import java.util.Observer;

import br.com.fiomaravilhabarbearia.fio_maravilha.BaseFragment;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.Managers.Posts;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fraps on 08/02/17.
 */

public class FeedFragment extends BaseFragment implements Observer {

    @BindView(R.id.feed_recycler)
    RecyclerView _recycler;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout _refresh;

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
        _refresh.setProgressBackgroundColorSchemeColor(FioUtils.getColor(getActivity(),R.color.colorBlack));
        _refresh.setColorSchemeColors(FioUtils.getColor(getActivity(),R.color.colorLightOrange));
        _refresh.setOnRefreshListener(() -> {
            Posts.getInstace().downloadPosts();
        });
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
        if (_refresh != null) {
            _refresh.setRefreshing(false);
            _adapter.setPosts(Posts.getInstace()._posts);
        }
    }

}
