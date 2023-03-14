package com.gogitek.toeictest.config.pagination;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PaginationPage<E> {
    private long totalRecords;
    private long offset;
    private long limit;
    private Collection<E> records;

    public PaginationPage(long totalRecords, long offset, long limit, List<E> records) {
        this.totalRecords = totalRecords;
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public PaginationPage(long totalRecords, long offset, long limit, Set<E> records) {
        this.totalRecords = totalRecords;
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public PaginationPage(){

    }
    public long getTotalRecords() {
        return this.totalRecords == 0L ? (long)this.records.size() : this.totalRecords;
    }

    public PaginationPage<E> setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public PaginationPage<E> setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public long getLimit() {
        return limit;
    }

    public PaginationPage<E> setLimit(long limit) {
        this.limit = limit;
        return this;
    }

    public Collection<E> getRecords() {
        return records;
    }

    public PaginationPage<E> setRecords(Collection<E> records) {
        this.records = (Collection)(records == null ? Collections.emptyList() : records);
        return this;
    }
}

