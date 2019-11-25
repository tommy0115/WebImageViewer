package com.ktm.homp.webimageviewerforjava.option;

import com.ktm.homp.webimageviewerforjava.option.type.LicenseType;
import com.ktm.homp.webimageviewerforjava.option.type.SortBy;

public class FilterOption {

    SortBy sortBy;
    LicenseType licenseType;

    private static FilterOption filterOption;

    public static FilterOption newInstance(SortBy sortBy, LicenseType licenseType){
        if(filterOption == null){
            filterOption = new FilterOption();
        }

        filterOption.setSortBy(sortBy);
        filterOption.setLicenseType(licenseType);

        return filterOption;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }
}
