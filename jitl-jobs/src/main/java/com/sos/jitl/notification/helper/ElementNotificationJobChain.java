package com.sos.jitl.notification.helper;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sos.jitl.notification.db.DBLayerSchedulerMon;

import sos.util.SOSString;

public class ElementNotificationJobChain {

    private Node xml;
    private ElementNotificationMonitor monitor;
    private String schedulerId;
    private String name;
    private Long notifications;
    private String stepFrom;
    private String stepTo;
    private String returnCodeFrom;
    private String returnCodeTo;
    private ArrayList<String> excludedSteps;
    private String excludedStepsAsString;

    public ElementNotificationJobChain(ElementNotificationMonitor monitor, Node jobChain) {
        this.monitor = monitor;
        this.xml = jobChain;
        Element el = (Element) this.xml;
        this.schedulerId = this.getValue(NotificationXmlHelper.getSchedulerId(el));
        this.name = this.getValue(NotificationXmlHelper.getJobChainName(el));
        this.notifications = this.getLongValue(NotificationXmlHelper.getNotifications(el));
        this.stepFrom = this.getValue(NotificationXmlHelper.getStepFrom(el));
        this.stepTo = this.getValue(NotificationXmlHelper.getStepTo(el));
        this.returnCodeFrom = this.getValue(NotificationXmlHelper.getReturnCodeFrom(el));
        this.returnCodeTo = this.getValue(NotificationXmlHelper.getReturnCodeTo(el));
        this.setExcludedSteps(el);
    }

    private String getValue(String val) {
        return SOSString.isEmpty(val) ? DBLayerSchedulerMon.DEFAULT_EMPTY_NAME : val;
    }

    private Long getLongValue(String val) {
        return SOSString.isEmpty(val) ? new Long(1) : new Long(val);
    }

    public ElementNotificationMonitor getMonitor() {
        return this.monitor;
    }

    public Node getXml() {
        return this.xml;
    }

    public String getSchedulerId() {
        return this.schedulerId;
    }

    public String getName() {
        return this.name;
    }

    public Long getNotifications() {
        return this.notifications;
    }

    public String getStepFrom() {
        return this.stepFrom;
    }

    public String getStepTo() {
        return this.stepTo;
    }

    public String getReturnCodeFrom() {
        return this.returnCodeFrom;
    }

    public String getReturnCodeTo() {
        return this.returnCodeTo;
    }
    
    private void setExcludedSteps(Element jobChain) {
        this.excludedSteps = new ArrayList<String>();
        this.excludedStepsAsString = "";
        String es = NotificationXmlHelper.getExcludedSteps(jobChain);
        if (!SOSString.isEmpty(es)) {
            this.excludedStepsAsString = es;
            String[] arr = es.trim().split(";");
            for (int i = 0; i < arr.length; i++) {
                if (!arr[i].trim().isEmpty()) {
                    this.excludedSteps.add(arr[i].trim());
                }
            }
        }
    }

    public ArrayList<String> getExcludedSteps() {
        return this.excludedSteps;
    }

    public String getExcludedStepsAsString() {
        return this.excludedStepsAsString;
    }

}