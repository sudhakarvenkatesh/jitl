package com.sos.jitl.notification.plugins.notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.spooler.Job;
import sos.spooler.Spooler;
import sos.spooler.Task;
import sos.spooler.Variable_set;

import com.sos.jitl.notification.db.DBItemSchedulerMonChecks;
import com.sos.jitl.notification.db.DBItemSchedulerMonNotifications;
import com.sos.jitl.notification.db.DBItemSchedulerMonSystemNotifications;
import com.sos.jitl.notification.db.DBLayerSchedulerMon;
import com.sos.jitl.notification.helper.EServiceMessagePrefix;
import com.sos.jitl.notification.helper.EServiceStatus;
import com.sos.jitl.notification.helper.ElementNotificationMonitor;
import com.sos.jitl.notification.helper.ElementNotificationMonitorCommand;
import com.sos.jitl.notification.jobs.notifier.SystemNotifierJobOptions;

public class SystemNotifierJobPlugin extends SystemNotifierPlugin {
	final Logger logger = LoggerFactory.getLogger(SystemNotifierJobPlugin.class);
		
	@Override
	public void init(ElementNotificationMonitor monitor) throws Exception{
		super.init(monitor);
		
		ElementNotificationMonitorCommand configuredCommand = getNotificationMonitor().getMonitorCommand();
		if(configuredCommand == null){
			throw new Exception(String.format("%s: Command element is missing (not configured)"
					,getClass().getSimpleName()));
	
		}
		setCommand(configuredCommand.getCommand());
	}
	
	@Override
	public int notifySystem(Spooler spooler, SystemNotifierJobOptions options,
			DBLayerSchedulerMon dbLayer,
			DBItemSchedulerMonNotifications notification,
			DBItemSchedulerMonSystemNotifications systemNotification,
			DBItemSchedulerMonChecks check,
			EServiceStatus status,
			EServiceMessagePrefix prefix)
			throws Exception {

		String serviceStatus = getServiceStatusValue(status);
		String servicePrefix = getServiceMessagePrefixValue(prefix);
				
		resolveCommandAllTableFieldVars(dbLayer, notification,systemNotification,check);
		resolveCommandServiceNameVar(systemNotification.getServiceName());
		resolveCommandServiceStatusVar(serviceStatus);
		resolveCommandServiceMessagePrefixVar(servicePrefix);
		resolveCommandAllEnvVars();
		
		Variable_set parameters = spooler.create_variable_set();
		parameters.set_var("command", getCommand());

		logger.info(String.format("call job = %s with command = %s", 
				options.plugin_job_name.getValue(),
				this.getCommand()));
		
		Job j = spooler.job(options.plugin_job_name.getValue());
		if (j == null) {
			throw new Exception(String.format("job not found : %s",
					options.plugin_job_name.getValue()));
		}
		Task t = j.start(parameters);
		
		//@TODO is not set on this place 
		return t.exit_code();
	}

	
}