package com.sos.jitl.notification.jobs.notifier;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.jitl.notification.helper.NotificationMail;

import sos.scheduler.job.JobSchedulerJobAdapter;
import sos.util.SOSString;

public class SystemNotifierJobJSAdapterClass extends JobSchedulerJobAdapter {

    private SystemNotifierJob job;

    @Override
    public boolean spooler_init() {
        try {
            job = new SystemNotifierJob();
            SystemNotifierJobOptions options = job.getOptions();
            options.setCurrentNodeName(this.getCurrentNodeName());
            options.setAllOptions(getSchedulerParameterAsProperties(getParameters()));
            job.setJSJobUtilites(this);
            job.setJSCommands(this);

            if (SOSString.isEmpty(options.hibernate_configuration_file_reporting.getValue())) {
                options.hibernate_configuration_file_reporting.setValue(getHibernateConfigurationReporting().toString());
            }
            options.scheduler_mail_settings.setValue(NotificationMail.getSchedulerMailOptions(spooler, spooler_log));
            job.init(spooler);
        } catch (Exception e) {
            throw new JobSchedulerException("Fatal Error:" + e.getMessage(), e);
        }
        return super.spooler_init();
    }

    @Override
    public boolean spooler_process() throws Exception {

        try {
            super.spooler_process();

            SystemNotifierJobOptions options = job.getOptions();
            options.setCurrentNodeName(this.getCurrentNodeName());
            options.setAllOptions(getSchedulerParameterAsProperties(getParameters()));

            job.openSession();
            job.execute();
        } catch (Exception e) {
            throw new JobSchedulerException("Fatal Error:" + e.getMessage(), e);
        } finally {
            job.closeSession();
        }
        return signalSuccess();

    }

    @Override
    public void spooler_close() throws Exception {
        if (job != null) {
            job.exit();
        }
        super.spooler_close();
    }
}
