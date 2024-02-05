package com.kceylan.app.view.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kceylan.app.R;
import com.kceylan.app.databinding.ItemContainerTvShowBinding;
import com.kceylan.app.listeners.TVShowListener;
import com.kceylan.app.models.TVShow;

import java.util.ArrayList;
import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {

    private List<TVShow> tvShows = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private final TVShowListener tvShowListener;

    public TvShowsAdapter(TVShowListener tvShowListener) {
        this.tvShowListener = tvShowListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<TVShow> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show,parent,false
        );
        return new TvShowsViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowsViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TvShowsViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TvShowsViewHolder (ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TVShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(view -> tvShowListener.onTvShowClicked(tvShow));
        }
    }
}
