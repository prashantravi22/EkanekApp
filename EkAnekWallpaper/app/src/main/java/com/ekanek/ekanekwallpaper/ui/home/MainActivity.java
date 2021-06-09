package com.ekanek.ekanekwallpaper.ui.home;

import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ekanek.ekanekwallpaper.EkAnekApplication;
import com.ekanek.ekanekwallpaper.R;
import com.ekanek.ekanekwallpaper.data.model.Photo;
import com.ekanek.ekanekwallpaper.data.model.Resource;
import com.ekanek.ekanekwallpaper.data.remote.PhotosResponse;
import com.ekanek.ekanekwallpaper.util.ViewModelFactory;

import java.util.List;

import static com.ekanek.ekanekwallpaper.data.model.Resource.Status.ERROR;
import static com.ekanek.ekanekwallpaper.data.model.Resource.Status.LOADING;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mMainViewModel;
    private RecyclerView mPicsRecyclerView;
    private TextView mTitleView;
    private EditText mSearchView;
    private View mStatusLayout;
    private ProgressBar mProgressBar;
    private ProgressBar mLoadMoreProgressBar;
    private TextView mMessageView;
    private ImageView mErrorImageView;
    private EachRowPhotosAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        ViewModelFactory modelFactory = ((EkAnekApplication)getApplication()).getViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, modelFactory).get(MainViewModel.class);
        getViews();
        setupSearchView();
        setupTitleView();
        setupPicsRecyclerView(2);
        subscribeToViewModel();
        if (savedInstanceState == null) {
            mMainViewModel.fetchPhotos("");
        }
    }

    private void getViews() {
        mPicsRecyclerView = findViewById(R.id.recycler_view_pics);
        mSearchView = findViewById(R.id.et_search);
        mTitleView = findViewById(R.id.tv_title);
        mStatusLayout = findViewById(R.id.layout_status);
        mProgressBar = findViewById(R.id.progressbar);
        mLoadMoreProgressBar = findViewById(R.id.progressbar_loadmore);
        mMessageView = findViewById(R.id.tv_message);
        mErrorImageView = findViewById(R.id.image_error);
    }

    private void setupSearchView() {
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mSearchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                dismissKeyboard(v.getWindowToken());
                mMainViewModel.fetchPhotos(mSearchView.getText().toString());
                return true;
            }
            return false;
        });

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mMainViewModel.searchTextChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupTitleView() {
        mTitleView.setText(R.string.app_name);
    }

    private void dismissKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    private void setupPicsRecyclerView(int items) {
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), items);
        mPicsRecyclerView.setLayoutManager(manager);
        mPicsRecyclerView.addItemDecoration(new SpacesItemDecoration(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources().getDisplayMetrics())));
        mAdapter = new EachRowPhotosAdapter();

        mPicsRecyclerView.setAdapter(mAdapter);
        mPicsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if (lastPosition == mAdapter.getItemCount() - 1) {
                    mMainViewModel.loadNextPage();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.grid_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.two:
                setupPicsRecyclerView(2);
                subscribeToViewModel();
                break;
            case R.id.three:
                setupPicsRecyclerView(3);
                subscribeToViewModel();
                break;
            case R.id.four:
                setupPicsRecyclerView(4);
                subscribeToViewModel();
                break;
        }
        return true;
    }

    private Spannable getStyledText(String finalString, String query) {
        Spannable spannable = new SpannableString(finalString);

        int color = getResources().getColor(android.R.color.holo_blue_dark);

        spannable.setSpan(new ForegroundColorSpan(color),
                finalString.indexOf(query),
                finalString.indexOf(query) + query.length() ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1.2f),
                finalString.indexOf(query),
                finalString.indexOf(query) + query.length() ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void subscribeToViewModel() {
        mMainViewModel.getResultsState().observe(this, (val) -> {
            Resource<PhotosResponse> result = val.first;
            if (result.status == ERROR) {
                showError(result.message);
            }
            if (result.status == LOADING) {
                showLoading(val.second);
            }
        });

        mMainViewModel.getAllResults().observe(this, val -> {
            if (val != null) {
                showResults(val.first, val.second);
            }
        });

        mMainViewModel.getLoadMoreState().observe(this, val -> {
            if (val == null) {
                return;
            }
            if (val.first.status == LOADING) {
                mLoadMoreProgressBar.setVisibility(View.VISIBLE);
            } else {
                mLoadMoreProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showLoading(String query) {
        mStatusLayout.setVisibility(View.VISIBLE);
        if (query.trim().equals("")) {
            mMessageView.setText("Loading pictures");
        } else {
            mMessageView.setText("Loading pictures for '" + query+"'");
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorImageView.setVisibility(View.GONE);

        mAdapter.setPhotoList(null);
        mAdapter.notifyDataSetChanged();
    }

    private void showError(String message){
        mStatusLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mMessageView.setText(message + "\n Tap here to retry");
        mStatusLayout.setOnClickListener((view) -> {
            mMainViewModel.refresh();
            mStatusLayout.setOnClickListener(null);
        });
        mErrorImageView.setVisibility(View.VISIBLE);
    }

    private void showResults(List<Photo> photoList, String query) {
        if (photoList.size() == 0) {
            mStatusLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mMessageView.setVisibility(View.VISIBLE);
            String finalString = "No results found for " + query;
            mMessageView.setText(getStyledText(finalString, query));
            mErrorImageView.setVisibility(View.GONE);
            mTitleView.setText(R.string.app_name);
            return;
        }

        if (query.equals("")) {
            mTitleView.setText(R.string.app_name);
        } else {
            String finalString = "Showing results for " + query;
            mTitleView.setText(getStyledText(finalString, query));
        }
        mStatusLayout.setVisibility(View.GONE);
        mAdapter.setPhotoList(photoList);
        mAdapter.notifyDataSetChanged();
    }
}
