package com.tongbanjie.tarzan.admin.common;

import com.tongbanjie.tarzan.common.PagingParam;

import java.io.Serializable;

/**
 * 〈前台分页组件〉<p>
 * 〈功能详细描述〉
 *
 * @author zixiao
 * @date 17/5/3
 */
public class BasePage implements Serializable {

    private static final long     serialVersionUID = -1682356143695291160L;

    /**
     * 默认分页记录显示数
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    private int pageSize    = DEFAULT_PAGE_SIZE; // 每页记录数. webPage15,数据型应用10~25
    private int totalCount  = -1;                // 总记录数
    private int pageNo      = 1;                 // 当前页码
    private int pageCount   = 1;                 // 总页数
    private int startRow    = 0;
    private int endRow      = 20;

    /**
     * 设置分页各参数
     *
     * @param count
     */
    public void doPage(int count) {

        this.totalCount = count;
        if (pageSize <= 0) this.pageSize = DEFAULT_PAGE_SIZE;
        int mod = -1;
        if (totalCount != -1) {
            if (totalCount > pageSize) {
                mod = totalCount % pageSize;
                if (mod != 0) {
                    pageCount = (totalCount / pageSize) + 1;
                } else {
                    pageCount = (totalCount / pageSize);
                }
            } else {
                pageCount = 1;
            }
        }
        if (pageNo <= 0) this.pageNo = 1;
        if (pageNo > pageCount) this.pageNo = pageCount;
        this.startRow = (this.pageNo - 1) * this.pageSize; // mysql: 0; Oracle: 1
        this.endRow = this.pageNo * this.pageSize;
    }

    public void doPage(int count, int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.doPage(count);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int iPageNo) {
        this.pageNo = iPageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * 客户端程序设置每页条数
     *
     * @param iPageSize
     */
    public void setPageSize(int iPageSize) {
        if (iPageSize >= 1) {
            this.pageSize = iPageSize;
        }
    }

    public int getTotalCount() {
        return this.totalCount == -1 ? 0 : totalCount;
    }

    public void setTotalCount(int iTotalCount) {
        if (iTotalCount > 0) {
            this.totalCount = iTotalCount;
        }
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int iPageCount) {
        this.pageCount = iPageCount;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        if(startRow<0){
            startRow=0;
        }
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    /**
     * 获取调用后端接口的查询参数。
     * @param count 总记录数
     * @return
     */
    public PagingParam getPagingParam(int count){
        this.doPage(count);
        PagingParam pagingParam = new PagingParam(pageSize, totalCount);
        pagingParam.setPageNo(pageNo);
        return pagingParam;
    }

}