package com.ktm.homp.webimageviewerforjava.presenters;

import android.util.Log;

import com.ktm.homp.webimageviewerforjava.model.Repository;
import com.ktm.homp.webimageviewerforjava.model.presentation.MainRepositoriesPresentationModel;
import com.ktm.homp.webimageviewerforjava.option.FilterOption;
import com.ktm.homp.webimageviewerforjava.ui.mvpviews.MainView;
import com.ktm.homp.webimageviewerforjava.utils.HtmlParsingCallback;
import com.ktm.homp.webimageviewerforjava.utils.HtmlParsingRequester;
import com.ktm.homp.webimageviewerforjava.option.type.LicenseType;
import com.ktm.homp.webimageviewerforjava.option.type.SortBy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.appflate.droidmvp.SimpleDroidMVPPresenter;

public class MainPresenter extends SimpleDroidMVPPresenter<MainView, MainRepositoriesPresentationModel> {

    private HtmlParsingRequester htmlParsingRequester;

    public MainPresenter() {
        super();
        htmlParsingRequester = new HtmlParsingRequester();
    }

    @Override
    public void attachView(MainView mvpView, MainRepositoriesPresentationModel presentationModel) {
        super.attachView(mvpView, presentationModel);
        updateControlUIs();
        if (presentationModel.shouldFetchRepositories()) {
            onParsingImage();
        }
//        else{
//            onRepositoriesFetched();
//        }
    }

    private void updateControlUIs(){
        if(getMvpView() != null){
            getMvpView().hideProgress();
            getMvpView().updateViewSort(getPresentationModel().getViewSortType());
            getMvpView().updatePageNumber(getPresentationModel().getPage());
            getMvpView().updateFilterOption(getPresentationModel().getFilterOption());
        }
    }

    public void onViewSortBtnClicked(int sortType) {
        getPresentationModel().setViewSortType(sortType);
        if (getMvpView() != null) {
            getMvpView().updateViewSort(sortType);
        }
    }

    public void onFilterApply(SortBy sortBy, LicenseType licenseType) {
        getPresentationModel().setSortBy(sortBy);
        getPresentationModel().setLicenseType(licenseType);
        getPresentationModel().setPage(1);

        onParsingImage();

        if(getMvpView() != null){
            getMvpView().updatePageNumber(1);
        }

    }

    public void onPageMove(int n) {
        getPresentationModel().setPage(n);

        onParsingImage();

        if(getMvpView() != null){
            getMvpView().updatePageNumber(getPresentationModel().getPage());
        }
    }

    public void onRepositoriesFetched() {
        if (getMvpView() != null) {
            getMvpView().showRepositoriesList(getPresentationModel().getRepositories(), getPresentationModel().getViewSortType());
        }
    }

    private void onShowProgressBar(){
        if (getMvpView() != null) {
            getMvpView().showLoadingProgress();
        }
    }

    private void onHideProgressBar(){
        if (getMvpView() != null) {
            getMvpView().hideProgress();
        }
    }

    private void onParsingImage() {
        onShowProgressBar();

        FilterOption filterOption = getPresentationModel().getFilterOption();
        int page = getPresentationModel().getPage();

        htmlParsingRequester.request(HtmlParsingRequester.BuildURL(filterOption, page), new HtmlParsingCallback() {
            @Override
            public void onResult(Document result) {
                getPresentationModel().setRepositories(null);

                Elements imageSection = result.getElementsByClass("search-content__gallery-assets");
                List<Repository> repositories = new ArrayList<>();

                for (Element root : imageSection){
                    Elements elements = root.getElementsByClass("gallery-mosaic-asset__container");
                    for (Element mosaic : elements) {
                        Repository repository = new Repository();

                        Elements data = mosaic.select(".gallery-asset-schema meta");
                        Log.d("data", data.html());

                        repository.setName(data.get(0).attr("content"));
                        repository.setDescription(data.get(1).attr("content"));
                        repository.setThumbnailUrl(data.get(2).attr("content"));
                        repository.setLinkImageUrl(data.get(3).attr("content"));
                        repositories.add(repository);
                    }
                }

                getPresentationModel().setRepositories(repositories);
                onRepositoriesFetched();
                onHideProgressBar();
            }

            @Override
            public void onError(int statusCode, String statusMsg) {
                getPresentationModel().setRepositories(null);

                onRepositoriesFetched();

                onHideProgressBar();

                if (getMvpView() != null) {
                    getMvpView().showError(statusCode, statusMsg);
                }
            }
        });
    }


}
