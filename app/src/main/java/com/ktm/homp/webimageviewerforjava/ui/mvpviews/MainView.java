package com.ktm.homp.webimageviewerforjava.ui.mvpviews;

import com.ktm.homp.webimageviewerforjava.model.Repository;
import com.ktm.homp.webimageviewerforjava.option.FilterOption;
import com.ktm.homp.webimageviewerforjava.option.type.SortBy;
import com.ktm.homp.webimageviewerforjava.option.type.ViewSortType;

import java.util.List;

import io.appflate.droidmvp.DroidMVPView;

public interface MainView extends DroidMVPView {
    void showLoadingProgress();
    void hideProgress();
    void showRepositoriesList(List<Repository> repositories, @ViewSortType int layoutSortType);
    void showError(int statusCode, String statusMsg);
    void updateViewSort(int sortType);
    void updatePageNumber(int n);
    void updateFilterOption(FilterOption filterOption);
}
