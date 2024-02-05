package com.kceylan.app.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kceylan.app.R;
import com.kceylan.app.databinding.FragmentFavoriteBinding;
import com.kceylan.app.listeners.FavoriteListener;
import com.kceylan.app.models.TVShow;
import com.kceylan.app.view.adapters.FavoriteAdapter;
import com.kceylan.app.viewmodels.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteFragment extends Fragment implements FavoriteListener {

    private FragmentFavoriteBinding binding;
    private WatchListViewModel viewModel;
    private FavoriteAdapter favoriteAdapter;
    private final List<TVShow> favoriteList = new ArrayList<>();
    NavController controller;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);

        if (navHostFragment != null) {
            controller = navHostFragment.getNavController();
        }
        doInitialization();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadWatchlist();
    }
    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        binding.imageBack.setOnClickListener(view -> controller.navigateUp());
        setupRecyclerview();
    }

    private void loadWatchlist() {
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                viewModel.loadWatchList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShows -> {
                            binding.setIsLoading(false);
                            if (favoriteList.size() > 0) {
                                favoriteList.clear();
                            }
                            favoriteList.addAll(tvShows);
                            favoriteAdapter.setData(favoriteList);
                            compositeDisposable.dispose();

                        }));
    }

    private void setupRecyclerview() {
        favoriteAdapter = new FavoriteAdapter(this);
        binding.watchlistRecyclerView.setAdapter(favoriteAdapter);
        binding.watchlistRecyclerView.setVisibility(View.VISIBLE);

    }
    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tvShow", tvShow);
        controller.navigate(R.id.action_favoriteFragment_to_TVShowDetailsFragment2, bundle);
    }

    @Override
    public void removeTVShowFromWatchList(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(
                viewModel.removeTVShowFromWatchlist(tvShow)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            if (position >= 0 && position < favoriteList.size()) {
                                favoriteList.remove(position);
                                favoriteAdapter.notifyItemRemoved(position);
                            }
                            compositeDisposableForDelete.dispose();
                        }));
    }
}