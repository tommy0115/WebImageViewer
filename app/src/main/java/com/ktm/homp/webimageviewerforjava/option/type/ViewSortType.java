package com.ktm.homp.webimageviewerforjava.option.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ktm.homp.webimageviewerforjava.option.type.ViewSortType.VIEW_SORT_COLUMN;
import static com.ktm.homp.webimageviewerforjava.option.type.ViewSortType.VIEW_SORT_LIST;

@Retention(RetentionPolicy.SOURCE)
@IntDef({VIEW_SORT_COLUMN, VIEW_SORT_LIST})
public @interface ViewSortType {
    int VIEW_SORT_COLUMN = 0;
    int VIEW_SORT_LIST = 1;
}
