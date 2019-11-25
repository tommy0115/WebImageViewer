package com.ktm.homp.webimageviewerforjava.model.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import com.ktm.homp.webimageviewerforjava.model.Repository;
import com.ktm.homp.webimageviewerforjava.option.FilterOption;
import com.ktm.homp.webimageviewerforjava.option.type.LicenseType;
import com.ktm.homp.webimageviewerforjava.option.type.SortBy;
import com.ktm.homp.webimageviewerforjava.option.type.ViewSortType;

import java.util.ArrayList;
import java.util.List;

public class MainRepositoriesPresentationModel implements Parcelable {

    private FilterOption filterOption;
    private int viewSortType;
    private int page = 1;
    private List<Repository> repositories;

    private MainRepositoriesPresentationModel(Parcel in) {
        readFromParcel(in);
    }
    public MainRepositoriesPresentationModel(FilterOption filterOption, @ViewSortType int viewSortType, int page){
        this.filterOption = filterOption;
        this.viewSortType = viewSortType;
        this.page = page;
        this.repositories = new ArrayList<>();
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public boolean shouldFetchRepositories() {
        return repositories == null || repositories.isEmpty();
    }

    public SortBy getSortBy() {
        return filterOption.getSortBy();
    }

    public void setSortBy(SortBy sortBy) {
        filterOption.setSortBy(sortBy);
    }

    public LicenseType getLicenseType() {
        return filterOption.getLicenseType();
    }

    public void setLicenseType(LicenseType licenseType) {
        filterOption.setLicenseType(licenseType);
    }

    public FilterOption getFilterOption() {
        return filterOption;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public @ViewSortType int getViewSortType() {
        return viewSortType;
    }

    public void setViewSortType(@ViewSortType int viewSortType) {
        this.viewSortType = viewSortType;
    }

    public static final Parcelable.Creator<MainRepositoriesPresentationModel> CREATOR
            = new Parcelable.Creator<MainRepositoriesPresentationModel>() {
        public MainRepositoriesPresentationModel createFromParcel(Parcel in) {
            return new MainRepositoriesPresentationModel(in);
        }

        public MainRepositoriesPresentationModel[] newArray(int size) {
            return new MainRepositoriesPresentationModel[size];
        }
    };

    private void readFromParcel(Parcel in){
        int sortByCode = in.readInt();
        int licenseTypeCode = in.readInt();

        filterOption = FilterOption.newInstance(
                SortBy.getMatchingSortByCode(sortByCode),
                LicenseType.getMatchingLicenseTypeCode(licenseTypeCode));

        viewSortType = in.readInt();
        page = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(filterOption.getSortBy().getCode());
        dest.writeInt(filterOption.getLicenseType().getCode());
        dest.writeInt(viewSortType);
        dest.writeInt(page);
    }
}
