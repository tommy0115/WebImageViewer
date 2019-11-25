package com.ktm.homp.webimageviewerforjava.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ktm.homp.webimageviewerforjava.R;
import com.ktm.homp.webimageviewerforjava.model.Repository;
import com.ktm.homp.webimageviewerforjava.model.presentation.MainRepositoriesPresentationModel;
import com.ktm.homp.webimageviewerforjava.option.type.ViewSortType;
import com.ktm.homp.webimageviewerforjava.presenters.MainPresenter;
import com.ktm.homp.webimageviewerforjava.ui.adapter.RepositoryRecyclerAdapter;
import com.ktm.homp.webimageviewerforjava.ui.base.BaseActivity;
import com.ktm.homp.webimageviewerforjava.ui.event.OnRepositoryItemClickListener;
import com.ktm.homp.webimageviewerforjava.ui.mvpviews.MainView;
import com.ktm.homp.webimageviewerforjava.option.FilterOption;
import com.ktm.homp.webimageviewerforjava.utils.HtmlParsingRequester;
import com.ktm.homp.webimageviewerforjava.utils.InputFilterMinMax;
import com.ktm.homp.webimageviewerforjava.utils.KeyboardUtils;
import com.ktm.homp.webimageviewerforjava.option.type.LicenseType;
import com.ktm.homp.webimageviewerforjava.option.type.SortBy;

import java.util.List;

public class MainActivity extends BaseActivity<MainRepositoriesPresentationModel, MainView, MainPresenter> implements MainView{

    private DrawerLayout drawerLayout;
    private View drawerView;
    private ImageButton buttonOpenDrawer;

    private FrameLayout progressLayout;

    private ImageButton viewSortColumnButton, viewSortListButton;

    private RecyclerView recyclerView;

    private EditText pageEditText;

    private RadioGroup licenseTypeRadioGroup;
    private RadioGroup sortByRadioGroup;

    @NonNull
    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @NonNull
    @Override
    protected MainRepositoriesPresentationModel createPresentationModel() {
        return new MainRepositoriesPresentationModel(FilterOption.newInstance(SortBy.MOST_POPULAR, LicenseType.RF), ViewSortType.VIEW_SORT_COLUMN,1);
    }

    public void initFindViewById(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);
        buttonOpenDrawer = (ImageButton) findViewById(R.id.open_filter_drawer);

        progressLayout = findViewById(R.id.progress_layout);

        viewSortColumnButton = findViewById(R.id.column_sort);
        viewSortListButton = findViewById(R.id.list_sort);

        recyclerView = findViewById(R.id.recyclerView);

        licenseTypeRadioGroup = findViewById(R.id.licenseTypeRadioGroup);
        sortByRadioGroup = findViewById(R.id.sortByRadioGroup);

        pageEditText = findViewById(R.id.edit_page);
    }

    public void initListener(){

        buttonOpenDrawer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if(drawerLayout.isDrawerOpen(drawerView)){
                    drawerLayout.closeDrawer(drawerView);
                }else{
                    drawerLayout.openDrawer(drawerView);
                }
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.close_drawer);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                drawerLayout.closeDrawers();
            }
        });

        drawerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // Page Enter Event
        pageEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
        pageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        getPresenter().onPageMove(Integer.valueOf(pageEditText.getText().toString()));
                        KeyboardUtils.hideKeyboard(MainActivity.this);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        // Prevent background click by FrameLayout
        progressLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    // process activity life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFindViewById();
        initListener();

        // Restrictions EditText
        pageEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 100)});
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }

    // left, right button page move event
    public void onClickPageMoveButton(View view){

        KeyboardUtils.hideKeyboard(MainActivity.this);

        int page = Integer.valueOf(pageEditText.getText().toString());

        boolean checkUpdate = true;

        if(view.getId() == R.id.left_move_btn){
            if(page > 1){
                page -= 1;
            }else{
                checkUpdate = false;
                Toast.makeText(this, "첫번째 페이지 입니다", Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == R.id.right_move_btn){
            if(page < 100){
                page += 1;
            }else{
                checkUpdate = false;
                Toast.makeText(this, "마지막 페이지 입니다", Toast.LENGTH_SHORT).show();
            }
        }

        if(checkUpdate){
            getPresenter().onPageMove(page);
        }
    }

    // column, list button click event
    public void onClickViewSortButton(View view) {
        getPresenter().onViewSortBtnClicked(view.getId() == R.id.column_sort ? ViewSortType.VIEW_SORT_COLUMN : ViewSortType.VIEW_SORT_LIST);
    }

    public void onClickFilterOptionRadioGroup(View view){
        executeFilterOption();
    }

    private void executeFilterOption(){
        RadioButton radioSortByButton = findViewById(sortByRadioGroup.getCheckedRadioButtonId());
        RadioButton radioLicenseTypeButton = findViewById(licenseTypeRadioGroup.getCheckedRadioButtonId());

        int sortIndex = sortByRadioGroup.indexOfChild(radioSortByButton);
        int licenseTypeIndex = licenseTypeRadioGroup.indexOfChild(radioLicenseTypeButton);

        getPresenter().onFilterApply(
                SortBy.getItem(sortIndex),
                LicenseType.getItem(licenseTypeIndex));
    }

    @Override
    public void updatePageNumber(int n) {
        pageEditText.setText(String.valueOf(n));
    }

    @Override
    public void showLoadingProgress() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRepositoriesList(List<Repository> repositories, int layoutListSortType) {
        RepositoryRecyclerAdapter repositoryRecyclerAdapter = new RepositoryRecyclerAdapter(repositories, Glide.with(this), layoutListSortType);
        repositoryRecyclerAdapter.setOnRepositoryItemClickListener(new OnRepositoryItemClickListener() {
            @Override
            public void onItemClick(Repository repository) {
                String url = /*HtmlParsingRequester.BASE_URL + */repository.getLinkImageUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        recyclerView.setAdapter(repositoryRecyclerAdapter);
    }

    @Override
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void showError(int statusCode, String statusMsg) {
        progressLayout.setVisibility(View.GONE);
        Toast.makeText(this, statusMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateViewSort(@ViewSortType  int sortType) {
        if(recyclerView.getAdapter() != null) {
            ((RepositoryRecyclerAdapter) recyclerView.getAdapter()).setViewSortType(sortType);
        }

        if(sortType == ViewSortType.VIEW_SORT_COLUMN){
            if(!viewSortColumnButton.isSelected()){
                viewSortColumnButton.setSelected(true);
                viewSortListButton.setSelected(false);

                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
        }else{
            if(!viewSortListButton.isSelected()){
                viewSortColumnButton.setSelected(false);
                viewSortListButton.setSelected(true);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }

    @Override
    public void updateFilterOption(FilterOption filterOption) {
        int sortByIndex = SortBy.getIndex(filterOption.getSortBy());
        int licenseTypeIndex = LicenseType.getIndex(filterOption.getLicenseType());

        RadioButton sortByRadioGroupChildAt = (RadioButton)(sortByRadioGroup.getChildAt(sortByIndex));
        RadioButton licenseTypeRadioGroupChildAt = (RadioButton)(licenseTypeRadioGroup.getChildAt(licenseTypeIndex));

        sortByRadioGroupChildAt.setChecked(true);
        licenseTypeRadioGroupChildAt.setChecked(true);
    }

}
