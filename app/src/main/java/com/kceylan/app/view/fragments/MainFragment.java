/*
package com.kceylan.app.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kceylan.app.R;
import com.kceylan.app.databinding.FragmentMainBinding;
import com.kceylan.app.listeners.TVShowListener;
import com.kceylan.app.models.TVShow;
import com.kceylan.app.view.adapters.TvShowsAdapter;
import com.kceylan.app.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements TVShowListener {
    private FragmentMainBinding binding;
    private MostPopularTVShowsViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    NavController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
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

        initialize();
        viewModel.data.observe(getViewLifecycleOwner(), mostPopularTVShowsResponse -> {
            binding.setIsLoading(false);
            if (mostPopularTVShowsResponse != null) {
                totalAvailablePages = mostPopularTVShowsResponse.getTotalPages();
                if (mostPopularTVShowsResponse.getTvShows() != null) {
                    tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                    tvShowsAdapter.setData(tvShows);
                }
            }
        });
    }

    private void initialize() {
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(this);
        binding.tvShowsRecyclerView.setHasFixedSize(true);
        binding.tvShowsRecyclerView.setAdapter(tvShowsAdapter);

        binding.imageSearch.setOnClickListener(view -> controller.navigate(R.id.action_mainFragment_to_searchFragment));
        binding.imageWatchList.setOnClickListener(view -> controller.navigate(R.id.action_mainFragment_to_favoriteFragment));

        binding.tvShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        binding.setIsLoading(true);
        viewModel.getMostPopularTVShows(currentPage);
    }

    @Override
    public void onTvShowClicked(TVShow tvShow) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tvShow", tvShow);
        controller.navigate(R.id.action_mainFragment_to_TVShowDetailsFragment, bundle);
    }

    @Override
    public void onPause() {
        viewModel.data.removeObservers(this);
        super.onPause();
    }
}

*/

package com.kceylan.app.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kceylan.app.R;
import com.kceylan.app.databinding.FragmentMainBinding;
import com.kceylan.app.listeners.TVShowListener;
import com.kceylan.app.models.TVShow;
import com.kceylan.app.view.adapters.TvShowsAdapter;
import com.kceylan.app.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements TVShowListener {

    // Declare instance variables
    // Bu satır, örnek değişkenlerin deklare edildiği kısımdır.
    private FragmentMainBinding binding;
    private MostPopularTVShowsViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    NavController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using DataBinding
        // Bu satır, veri bağlama kullanarak bu fragment'in düzenini şişirir.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the fragment as the binding's lifecycle owner
        // Bu satır, fragment'i bağlamanın yaşam döngüsü sahibi olarak ayarlar.
        binding.setFragment(this);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Get the NavController from the NavHostFragment
        // Bu satır, NavController'ı NavHostFragment'tan alır.
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            controller = navHostFragment.getNavController();
        }

        // Initialize the fragment's UI components and ViewModel
        // Bu satır, fragment'in UI bileşenlerini ve ViewModel'ini başlatır.
        initialize();

        // Observe changes to the ViewModel's data
        // Bu satır, ViewModel'in verilerindeki değişiklikleri izler.
        viewModel.data.observe(getViewLifecycleOwner(), mostPopularTVShowsResponse -> {
            binding.setIsLoading(false);

            if (mostPopularTVShowsResponse != null) {
                totalAvailablePages = mostPopularTVShowsResponse.getTotalPages();
                if (mostPopularTVShowsResponse.getTvShows() != null) {
                    tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                    tvShowsAdapter.setData(tvShows);
                }
            }
        });
    }

    private void initialize() {
        // Create a new ViewModel instance for this fragment
        // Bu satır, bu fragment için yeni bir ViewModel örneği oluşturur.
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);

        // Create a new adapter for the RecyclerView
        // Bu satır, RecyclerView için yeni bir adaptör oluşturur.
        tvShowsAdapter = new TvShowsAdapter(this);

        // Set the fixed size for the RecyclerView
        // Bu satır, RecyclerView'nin sabit bir boyuta sahip olmasını sağlar.
        binding.tvShowsRecyclerView.setHasFixedSize(true);

        // Set the adapter for the RecyclerView
        // Bu satır, RecyclerView için oluşturulan adaptörü ayarlar.
        binding.tvShowsRecyclerView.setAdapter(tvShowsAdapter);

        // Set click listeners for the search and watchlist buttons
        // Bu satırlar, arama ve izleme listesi düğmeleri için tıklama dinleyicilerini ayarlar.
        binding.imageSearch.setOnClickListener(view -> controller.navigate(R.id.action_mainFragment_to_searchFragment));
        binding.imageWatchList.setOnClickListener(view -> controller.navigate(R.id.action_mainFragment_to_favoriteFragment));

        // Add a scroll listener to the RecyclerView
        // Bu satır, RecyclerView'ye bir kaydırma dinleyicisi ekler.
        binding.tvShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Check if the RecyclerView has reached the bottom
                // Bu satır, RecyclerView'nin altına ulaşıp ulaşmadığını kontrol eder.
                if (!binding.tvShowsRecyclerView.canScrollVertically(1)) {
                    // If not all pages have been loaded, increment the current page and load more data
                    // Bu satır, tüm sayfalar yüklenmediyse, mevcut sayfayı artırır ve daha fazla veri yükler.
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        // Load the first page of data
        // Bu satır, ilk sayfayı yükler.
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        // Show the loading indicator
        // Bu satır, yükleme göstergesini gösterir.
        binding.setIsLoading(true);
        // Load the most popular TV shows from the ViewModel
        // Bu satır, ViewModel'den en popüler TV şovlarını yükler.
        viewModel.getMostPopularTVShows(currentPage);
    }

    @Override
    public void onTvShowClicked(TVShow tvShow) {
        // Navigate to the TVShowDetailsFragment when a TV show is clicked
        // Bu satır, bir TV şovuna tıklandığında TVShowDetailsFragment'e geçer.
        Bundle bundle = new Bundle();
        bundle.putSerializable("tvShow", tvShow);
        controller.navigate(R.id.action_mainFragment_to_TVShowDetailsFragment, bundle);
    }

    @Override
    public void onPause() {
        // Remove the observer for the ViewModel's data when the fragment is paused
        // Bu satır, fragment duraklatıldığında ViewModel'in verileri için gözlemleyiciyi kaldırır.
        viewModel.data.removeObservers(this);
        super.onPause();
    }
}