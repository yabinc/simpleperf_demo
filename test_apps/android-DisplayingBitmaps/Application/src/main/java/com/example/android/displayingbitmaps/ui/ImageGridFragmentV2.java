package com.example.android.displayingbitmaps.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.displayingbitmaps.BuildConfig;
import com.example.android.displayingbitmaps.R;
import com.example.android.displayingbitmaps.provider.Images;
import com.example.android.displayingbitmaps.util.ImageCache;
import com.example.android.displayingbitmaps.util.ImageFetcher;

public class ImageGridFragmentV2 extends Fragment {
    private static final String TAG = "ImageGridFragmentV2";
    private static final String IMAGE_CACHE_DIR = "thumbs";


    private MyAdapter mAdapter;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageFetcher mImageFetcher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MyAdapter();
        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_grid_fragmentv2, container, false);

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        //mGridView.setOnItemClickListener(this);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(
                                recyclerView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                        Log.e(TAG, "onGlobalLayoutListener, recyclerView.getWidth() = " + recyclerView.getWidth() + ", mImageThumbSize = " + mImageThumbSize + ", mImageThumbSpacing = " + mImageThumbSpacing + ", numColumns = " + numColumns);
                        if (numColumns > 0) {
                            final int columnWidth =
                                    (recyclerView.getWidth() / numColumns) - mImageThumbSpacing;
                            mAdapter.setItemHeight(columnWidth);
                            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                            if (gridLayoutManager.getSpanCount() != numColumns) {
                                gridLayoutManager.setSpanCount(numColumns);
                            }
                            recyclerView.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        }
                    }
                });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new ImageGridFragmentV2.MyMenuProvider(), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    private class MyMenuProvider implements MenuProvider {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.clear_cache) {
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            return true;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Recycling2ImageView recyclingImageView;
        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            Log.e(TAG, "Create MyViewHolder");
            recyclingImageView = (Recycling2ImageView) itemView.findViewById(R.id.thumbImageView);
            recyclingImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            recyclingImageView.setOnClickListener(this);
        }

        public void setLayout(ViewGroup.LayoutParams layoutParams) {
            recyclingImageView.setLayoutParams(layoutParams);
        }

        public void setImagePosition(int position) {
            Log.e(TAG, "setImagePosition: " + position);
            this.position = position;
            //textView.setText(Images.imageThumbUrls[position]);
            recyclingImageView.setContentDescription(Images.imageThumbUrls[position]);
            mImageFetcher.loadImage(Images.imageThumbUrls[position], recyclingImageView);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(), "You clicked " + textView.getText(),
            //        Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onItemClick: " + position);
            Log.e(TAG, "skip Creating ImageDetailActivity.class");
            final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
            i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) position);
            //  makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
            // show plus the thumbnail image in GridView is cropped. so using
            // makeScaleUpAnimation() instead.
            ActivityOptions options =
                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            getActivity().startActivity(i, options.toBundle());
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private int mItemHeight = 0;
        private ViewGroup.LayoutParams mImageViewLayoutParams;

        public MyAdapter() {
            super();
            mImageViewLayoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_grid_fragmentv2_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.setLayout(mImageViewLayoutParams);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.setLayout(mImageViewLayoutParams);
            holder.setImagePosition(position);
        }

        @Override
        public int getItemCount() {
            return Images.imageThumbUrls.length;
        }

        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams.width = height;
            mImageViewLayoutParams.height = height;
            notifyDataSetChanged();
        }
    }
}
