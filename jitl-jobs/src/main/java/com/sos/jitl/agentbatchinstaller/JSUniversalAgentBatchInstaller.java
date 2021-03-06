package com.sos.jitl.agentbatchinstaller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.JSHelper.Basics.IJSCommands;
import com.sos.JSHelper.Basics.JSJobUtilities;
import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.jitl.agentbatchinstaller.model.JSUniversalAgentBatchInstallerExecuter;
import com.sos.localization.Messages;

@I18NResourceBundle(baseName = "com.sos.scheduler.messages", defaultLocale = "en")
public class JSUniversalAgentBatchInstaller extends JSToolBox implements JSJobUtilities, IJSCommands {

    protected JSUniversalAgentBatchInstallerOptions objOptions = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(JSUniversalAgentBatchInstaller.class);
    private JSJobUtilities objJSJobUtilities = this;
    private IJSCommands objJSCommands = this;

    public JSUniversalAgentBatchInstaller() {
        super();
        Messages = new Messages("com_sos_scheduler_messages", Locale.getDefault());
    }

    public IJSCommands getJSCommands() {
        return objJSCommands;
    }

    public JSUniversalAgentBatchInstallerOptions options() {
        if (objOptions == null) {
            objOptions = new JSUniversalAgentBatchInstallerOptions();
        }
        return objOptions;
    }

    public JSUniversalAgentBatchInstallerOptions options(final JSUniversalAgentBatchInstallerOptions pobjOptions) {
        objOptions = pobjOptions;
        return objOptions;
    }

    public JSUniversalAgentBatchInstaller execute() throws Exception {
        final String methodName = "JSUniversalAgentBatchInstaller::Execute";
        LOGGER.debug(String.format(Messages.getMsg("JSJ-I-110"), methodName));
        try {
            options().checkMandatory();
            LOGGER.debug(options().toString());
            JSUniversalAgentBatchInstallerExecuter jsUniversalAgentBatchInstaller = new JSUniversalAgentBatchInstallerExecuter();
            jsUniversalAgentBatchInstaller.performInstallation(this);
        } catch (Exception e) {
            LOGGER.error(String.format(Messages.getMsg("JSJ-I-107"), methodName) + " " + e, e);
            throw e;      
        }
        return this;
    }

    @Override
    public String replaceSchedulerVars(final String pstrString2Modify) {
        LOGGER.debug("replaceSchedulerVars as Dummy-call executed. No Instance of JobUtilites specified.");
        return pstrString2Modify;
    }

    @Override
    public void setJSParam(final String pstrKey, final String pstrValue) {
    }

    @Override
    public void setJSParam(final String pstrKey, final StringBuffer pstrValue) {
    }

    @Override
    public void setJSJobUtilites(final JSJobUtilities pobjJSJobUtilities) {
        if (pobjJSJobUtilities == null) {
            objJSJobUtilities = this;
        } else {
            objJSJobUtilities = pobjJSJobUtilities;
        }
        LOGGER.debug("objJSJobUtilities = " + objJSJobUtilities.getClass().getName());
    }

    @Override
    public String getCurrentNodeName() {
        return null;
    }

    public void setJSCommands(final IJSCommands pobjJSCommands) {
        if (pobjJSCommands == null) {
            objJSCommands = this;
        } else {
            objJSCommands = pobjJSCommands;
        }
        LOGGER.debug("pobjJSCommands = " + pobjJSCommands.getClass().getName());
    }

    @Override
    public Object getSpoolerObject() {
        return null;
    }

    @Override
    public String executeXML(final String pstrJSXmlCommand) {
        return "";
    }

    @Override
    public void setStateText(final String pstrStateText) {
    }

    @Override
    public void setCC(final int pintCC) {
    }

    @Override
    public void setNextNodeState(final String pstrNodeName) {
    }

}