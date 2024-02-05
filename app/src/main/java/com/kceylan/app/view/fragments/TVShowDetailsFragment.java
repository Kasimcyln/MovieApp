package com.kceylan.app.view.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kceylan.app.R;
import com.kceylan.app.databinding.FragmentTVShowDetailsBinding;
import com.kceylan.app.databinding.LayoutEpisodesBottomSheetBinding;
import com.kceylan.app.models.TVShow;
import com.kceylan.app.view.adapters.EpisodesAdapter;
import com.kceylan.app.view.adapters.ImageSliderAdapter;
import com.kceylan.app.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowDetailsFragment extends Fragment {
    private NavController controller;
    private FragmentTVShowDetailsBinding binding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_t_v_show_details, container, false);
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
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

        assert getArguments() != null;
        tvShow = (TVShow) getArguments().getSerializable("tvShow");
        checkTVShowWatchlist();
        getTVShowDetails();

        binding.imageBack.setOnClickListener(view1 -> controller.navigateUp());
    }

    private void checkTVShowWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailableInWatchlist = true;
                    binding.ivFavoriteList.setImageResource(R.drawable.ic_star_selected);
                    compositeDisposable.dispose();
                }));
    }

    private void getTVShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId);
        tvShowDetailsViewModel.tvShowDetailsLiveData.observe(getViewLifecycleOwner(), tvShowDetailsResponse -> {
            binding.setIsLoading(false);
            if (tvShowDetailsResponse != null && tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                binding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                binding.imageTVShow.setVisibility(View.VISIBLE);
                binding.setDescription(HtmlCompat.fromHtml(
                        tvShowDetailsResponse.getTvShowDetails().getDescription(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                binding.textDescription.setVisibility(View.VISIBLE);
                binding.textReadMore.setVisibility(View.VISIBLE);
                binding.textReadMore.setOnClickListener(view -> {
                    if (binding.textReadMore.getText().toString().equals("Read More")) {
                        binding.textDescription.setMaxLines(Integer.MAX_VALUE);
                        binding.textReadMore.setEllipsize(null);
                        binding.textReadMore.setText(R.string.read_less);
                    } else {
                        binding.textDescription.setMaxLines(4);
                        binding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                        binding.textReadMore.setText(R.string.read_more);
                    }
                });
                binding.setRating(String.format(
                        Locale.getDefault(),
                        "%.2f",
                        Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                ));
                String genre = tvShowDetailsResponse.getTvShowDetails().getGenres() != null
                        ? tvShowDetailsResponse.getTvShowDetails().getGenres()[0]
                        : "N/A";
                binding.setGenre(genre);
                binding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                binding.viewDivider1.setVisibility(View.VISIBLE);
                binding.layoutMisc.setVisibility(View.VISIBLE);
                binding.viewDivider2.setVisibility(View.VISIBLE);

                binding.buttonWebsite.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });
                binding.buttonWebsite.setVisibility(View.VISIBLE);
                binding.buttonEpisodes.setVisibility(View.VISIBLE);

                binding.buttonEpisodes.setOnClickListener(view -> {
                    if (episodesBottomSheetDialog == null) {
                        episodesBottomSheetDialog = new BottomSheetDialog(requireContext());
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(requireContext()),
                                R.layout.layout_episodes_bottom_sheet,
                                requireView().findViewById(R.id.episodesContainer),
                                false
                        );
                        episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                        layoutEpisodesBottomSheetBinding.episodeRecyclerView.setAdapter(
                                new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                        );
                        layoutEpisodesBottomSheetBinding.textTitle.setText(
                                String.format("Episodes | %s", tvShow.getName())
                        );
                        layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());
                    }
                    FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (frameLayout != null) {
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    episodesBottomSheetDialog.show();
                });

                binding.ivFavoriteList.setOnClickListener(view -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTVShowAvailableInWatchlist) {
                        compositeDisposable.add(
                                tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTVShowAvailableInWatchlist = false;
                                            binding.ivFavoriteList.setImageResource(R.drawable.ic_star_unselected);
                                            Toast.makeText(requireContext(), "Removed from favorite list", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                    } else {
                        compositeDisposable.add(
                                tvShowDetailsViewModel.addToWatchList(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTVShowAvailableInWatchlist = true;
                                            binding.ivFavoriteList.setImageResource(R.drawable.ic_star_selected);
                                            Toast.makeText(requireContext(), "Added to favorite list", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                        );
                    }
                });
                binding.ivFavoriteList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {
        binding.sliderViewPager.setOffscreenPageLimit(1);
        binding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.sliderViewPager.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        binding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });

    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(requireContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            binding.layoutSliderIndicators.addView(indicators[i]);
        }
        binding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = binding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTVShowDetails() {
        binding.setTvShowName(tvShow.getName());
        binding.setNetworkCountry(
                tvShow.getNetwork() + " (" +
                        tvShow.getCountry() + ")"
        );
        binding.setStatus(tvShow.getStatus());
        binding.setStartedDate(tvShow.getStartDate());
        binding.textName.setVisibility(View.VISIBLE);
        binding.textNetworkCountry.setVisibility(View.VISIBLE);
        binding.textStatus.setVisibility(View.VISIBLE);
        binding.textStarted.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}