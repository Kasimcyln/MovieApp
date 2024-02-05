package com.kceylan.app.view.fragments;

import android.annotation.SuppressLint;
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

import com.jakewharton.rxbinding4.widget.RxTextView;
import com.kceylan.app.R;
import com.kceylan.app.databinding.FragmentSearchBinding;
import com.kceylan.app.listeners.TVShowListener;
import com.kceylan.app.models.TVShow;
import com.kceylan.app.view.adapters.TvShowsAdapter;
import com.kceylan.app.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchFragment extends Fragment implements TVShowListener {
    private FragmentSearchBinding binding;

    private SearchViewModel viewModel;
    private final List<TVShow> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    NavController controller;
    private boolean isSearchCompleted = true;
    private Disposable disposable;

    @Override
    //onCreateView metodu, fragment oluşturulduğunda ve kullanıcı arayüzü görüntülenmeden önce çağrılır.
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Bu satır, fragment_search.xml dosyasındaki düzeni şişirir.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        // Initialize the ViewModel
        // Bu satır, SearchViewModel sınıfından bir ViewModel örneği oluşturur.
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Observe the LiveData for TV shows
        // Bu satır, tvShowsLiveData adlı LiveData'yı gözlemleyerek arama sonuçlarını RecyclerView'a yükler.
        viewModel.tvShowsLiveData.removeObservers(getViewLifecycleOwner());
        viewModel.tvShowsLiveData.observe(getViewLifecycleOwner(), tvShowsResponse -> {
            if (tvShowsResponse != null) {
                // Get the total number of available pages
                // Bu satır, toplam mevcut sayfaların sayısını alır.
                totalAvailablePages = tvShowsResponse.getTotalPages();

                if (tvShowsResponse.getTvShows() != null) {
                    // Add the TV shows to the list and set the adapter data
                    // Bu satır, arama sonuçlarını tvShows listesine ekler ve RecyclerView adapter'ını günceller.
                    tvShows.addAll(tvShowsResponse.getTvShows());
                    tvShowsAdapter.setData(tvShowsResponse.getTvShows());
                }
            }

            // Set the search as completed and hide the loading indicator
            // Bu satır, aramanın tamamlandığını ve yükleme göstergesinin gizlendiğini belirtir.
            isSearchCompleted = true;
            binding.setIsLoading(false);
        });

        // Return the root view
        // Bu satır, fragment'in kök görünümünü döndürür.
        return binding.getRoot();
    }

    @Override
    //onViewCreated metodu, fragment'in kullanıcı arayüzü görüntülendikten hemen sonra çağrılan bir metodudur.
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the fragment as the binding variable
        // Bu satır, fragment'i bağlama değişkeni olarak ayarlar.
        binding.setFragment(this);

        // Set the lifecycle owner for the binding
        // Bu satır, veri bağlama işleminin yaşam döngüsü sahibini ayarlar.
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Perform initialization tasks
        // Bu satır, fragment'in inşası tamamlandıktan sonra başlatma işlemlerini gerçekleştirir.
        doInitialization();

        // Get the NavController for the NavHostFragment
        // Bu satır, NavHostFragment'in NavController örneğini alır.
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);

        // Set the NavController for the fragment
        // Bu satır, NavController örneğini fragment'e atar.
        if (navHostFragment != null) {
            controller = navHostFragment.getNavController();
        }
    }

    @SuppressLint("CheckResult")
    private void doInitialization() {
        // Set a click listener for the back button to navigate up
        // Bu satır, geri tuşuna tıklanıldığında NavController aracılığıyla üst düzeye yönlendirmek üzere bir tıklama dinleyicisi ekler.
        binding.ivBack.setOnClickListener(view -> controller.navigateUp());

        // Set up the RecyclerView
        // Bu satır, RecyclerView'ı hazırlar ve görünümün diğer öğeleriyle etkileşime girer.
        setupRecyclerView();

        // Set up a listener for text changes in the search EditText using RxJava
        // Bu satır, RxJava kullanarak arama EditText'indeki metin değişikliklerini dinler ve arama sonuçlarını günceller.
        disposable = RxTextView.textChanges(binding.etSearch)
                .debounce(800, TimeUnit.MILLISECONDS) // öğelerin yayınlanmasını belirli bir süre geciktirerek arka arkaya gelen hızlı öğeleri filtrelemeye yarar
                .map(CharSequence::toString) //Bu satır, akıştaki her öğeyi String türüne dönüştürüyor.
                .distinctUntilChanged() // Ardışık yinelenen sorguları filtreler
                .filter(query -> !query.trim().isEmpty()) // Boş sorguları filtreler
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> searchTVShow(query.trim()));

        // Set focus on the search EditText
        // Bu satır, arama EditText'ine odağı ayarlar.
        binding.etSearch.requestFocus();
    }

    private void setupRecyclerView() {
        // Create a new adapter for the RecyclerView
        // Bu satır, RecyclerView için yeni bir adaptör oluşturur.
        tvShowsAdapter = new TvShowsAdapter(this);

        // Set the adapter for the RecyclerView
        // Bu satır, RecyclerView için oluşturulan adaptörü ayarlar.
        binding.tvShowsRecyclerView.setAdapter(tvShowsAdapter);

        // Set the fixed size for the RecyclerView
        // Bu satır, RecyclerView'nin sabit bir boyuta sahip olmasını sağlar.
        binding.tvShowsRecyclerView.setHasFixedSize(true);

        // Add a scroll listener to the RecyclerView
        // Bu satır, RecyclerView'ye bir kaydırma dinleyicisi ekler.
        binding.tvShowsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the RecyclerView can be scrolled vertically
                // Bu satır, RecyclerView'nin dikey olarak kaydırılıp kaydırılamayacağını kontrol eder.
                if(!binding.tvShowsRecyclerView.canScrollVertically(1)) {

                    // Check if there are more pages to load
                    // Bu satır, yüklenmesi gereken daha fazla sayfa olup olmadığını kontrol eder.
                    if(currentPage < totalAvailablePages) {

                        // Set the search flag to false
                        // Bu satır, arama işleminin tamamlandığını belirtmek için arama bayrağını false olarak ayarlar.
                        isSearchCompleted = false;

                        // Increase the current page counter
                        // Bu satır, mevcut sayfa sayacını artırır.
                        currentPage += 1;

                        // Search for TV shows with the current search query and page number
                        // Bu satır, mevcut arama sorgusu ve sayfa numarası ile TV şovlarını arar.
                        searchTVShow(binding.etSearch.getText().toString().trim());
                    }
                }
            }
        });
    }

    private void searchTVShow(String query) {
        // Check if a search is already in progress
        // Bu satır, zaten bir arama işlemi yapılıp yapılmadığını kontrol eder.
        if (!isSearchCompleted) {
            return; // Return if a search is already in progress
        }

        // Call the searchTVShow() method of the ViewModel with the current search query and page number
        // Bu satır, ViewModel'in searchTVShow() yöntemini mevcut arama sorgusu ve sayfa numarası ile çağırır.
        viewModel.searchTVShow(query, currentPage);
    }

    @Override
    public void onTvShowClicked(TVShow tvShow) {
        // Create a new Bundle to hold the selected TV show object
        // Bu satır, seçilen TV şovu nesnesini tutmak için yeni bir Bundle oluşturur.
        Bundle bundle = new Bundle();

        // Put the selected TV show object into the Bundle with a key of "tvShow"
        // Bu satır, seçilen TV şovu nesnesini "tvShow" anahtarlığı ile Bundle'a ekler.
        bundle.putSerializable("tvShow", tvShow);

        // Navigate to the TVShowDetailsFragment using the NavController and pass in the Bundle
        // Bu satır, NavController aracılığıyla TVShowDetailsFragment'e geçer ve Bundle'ı geçer.
        controller.navigate(R.id.action_searchFragment_to_TVShowDetailsFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}