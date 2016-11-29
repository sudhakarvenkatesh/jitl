package com.sos.jitl.reporting.db.filter;

import java.util.ArrayList;

import com.sos.jitl.reporting.db.DBItemReportTrigger;

public class ReportTriggerFilter extends ReportHistoryFilter {

    protected String jobchain = null;
    protected String orderid = null;
    protected Long reportTriggerId = null;
    protected ArrayList<DBItemReportTrigger> listOfReportItems;
    private Boolean failed;
    private Boolean success;

    public ArrayList<DBItemReportTrigger> getListOfReportItems() {
        return listOfReportItems;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getJobchain() {
        if (jobchain != null && jobchain.startsWith("/")) {
            return jobchain.substring(1);
        } else {
            return jobchain;
        }
    }

    public ReportTriggerFilter() {
        super();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setJobchain(String jobchain) {
        this.jobchain = jobchain;
    }

    public Long getReportTriggerId() {
        return reportTriggerId;
    }

    public void setReportTriggerId(Long reportTriggerId) {
        this.reportTriggerId = reportTriggerId;
    }

    public void addOrderPath(String jobChain, String orderId) {
        if (listOfReportItems == null) {
            listOfReportItems = new ArrayList<DBItemReportTrigger>();
        }
        DBItemReportTrigger d = new DBItemReportTrigger();
        d.setParentName(jobChain);
        d.setName(orderId);
        listOfReportItems.add(d);
    }

    public Boolean getFailed() {
        return  failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
        this.success = null;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
        this.failed = null;
    }

}