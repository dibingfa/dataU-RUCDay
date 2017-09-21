package com.flash.dataU.rucday.dao.util;

import java.io.Serializable;

/**
 * 分页工具类.
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年07月25日 14时17分
 */
public class PageInfo implements Serializable{

    private static final long serialVersionUID = -1584284815901481363L;
    private static final int DEFAULT_FIRST_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 30;
    private Integer page;
    private Integer pageSize;
    private int begin;
    private int expectMinSize;

    public PageInfo() {
        this(DEFAULT_FIRST_PAGE, DEFAULT_PAGE_SIZE);
    }

    public PageInfo(Integer page, Integer pageSize) {
        if (null != page && page > 0) {
            this.page = page;
        }
        else {
            this.page = 1;
        }

        if (null != pageSize && pageSize > 0) {
            this.pageSize = pageSize;
        }
        else {
            this.pageSize = 30;
        }

        this.recalculateBegin();
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
        this.recalculateBegin();
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        this.recalculateBegin();
    }

    public int getBegin() {
        return this.begin;
    }

    public int getExpectMinSize() {
        return this.expectMinSize;
    }

    private void recalculateBegin() {
        this.begin = (this.page - 1) * this.pageSize;
        this.expectMinSize = this.begin + 1;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (o != null && this.getClass() == o.getClass()) {
            PageInfo info = (PageInfo)o;
            if (this.page != null) {
                if (this.page.equals(info.page)) {
                    return this.pageSize != null ? this.pageSize.equals(info.pageSize) : info.pageSize == null;
                }
            }
            else if (info.page == null) {
                return this.pageSize != null ? this.pageSize.equals(info.pageSize) : info.pageSize == null;
            }

            return false;
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.page != null ? this.page.hashCode() : 0;
        result = 31 * result + (this.pageSize != null ? this.pageSize.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "PageInfo{page=" + this.page + ", pageSize=" + this.pageSize + ", begin=" + this.begin + ", expectMinSize=" + this.expectMinSize + '}';
    }
}

