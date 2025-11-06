package com.github.kosmateus.shinden.http.request;

import com.github.kosmateus.shinden.request.Sort.Order;

public interface SortParam<T extends Enum<T> & SortParam<T>> {

    String getSortValue();

    String getSortParameter();

    Order<T> desc();

    Order<T> asc();

}
