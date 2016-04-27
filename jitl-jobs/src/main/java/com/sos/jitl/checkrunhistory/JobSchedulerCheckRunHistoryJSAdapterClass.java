package com.sos.jitl.checkrunhistory;

import sos.scheduler.job.JobSchedulerJobAdapter;

public class JobSchedulerCheckRunHistoryJSAdapterClass extends JobSchedulerJobAdapter {

    @Override
    public boolean spooler_process() throws Exception {
        try {
            super.spooler_process();
            doProcessing();
        } catch (Exception e) {
            return false;
        }
        return (spooler_task.job().order_queue() != null);
    }

    protected void doProcessing() throws Exception {
        JobSchedulerCheckRunHistory jobSchedulerCheckRunHistory = new JobSchedulerCheckRunHistory();
        JobSchedulerCheckRunHistoryOptions jobSchedulerCheckRunHistoryOptions = jobSchedulerCheckRunHistory.options();
        jobSchedulerCheckRunHistoryOptions.CurrentNodeName(getCurrentNodeName());
        jobSchedulerCheckRunHistoryOptions.setAllOptions(getSchedulerParameterAsProperties(getParameters()));
        jobSchedulerCheckRunHistory.setJSJobUtilites(this);
        jobSchedulerCheckRunHistory.setJSCommands(this);
        jobSchedulerCheckRunHistory.setPathOfJob(spooler_job.folder_path());
        jobSchedulerCheckRunHistory.Execute();
        if (this.isOrderJob()) {
            spooler_task.order().params().set_var("check_run_history_result", jobSchedulerCheckRunHistoryOptions.result.Value());
            spooler_task.order().params().set_var("check_run_history_number_of_starts", jobSchedulerCheckRunHistoryOptions.numberOfStarts.Value());
            spooler_task.order().params().set_var("check_run_history_number_of_completed",
                    jobSchedulerCheckRunHistoryOptions.numberOfCompleted.Value());
            spooler_task.order().params().set_var("check_run_history_number_of_completed_with_error",
                    jobSchedulerCheckRunHistoryOptions.numberOfCompletedWithError.Value());
            spooler_task.order().params().set_var("check_run_history_number_of_completed_successful",
                    jobSchedulerCheckRunHistoryOptions.numberOfCompletedSuccessful.Value());
        } else {
            spooler_task.params().set_var("check_run_history_result", jobSchedulerCheckRunHistoryOptions.result.Value());
            spooler_task.params().set_var("check_run_history_number_of_starts", jobSchedulerCheckRunHistoryOptions.numberOfStarts.Value());
            spooler_task.params().set_var("check_run_history_number_of_completed", jobSchedulerCheckRunHistoryOptions.numberOfCompleted.Value());
            spooler_task.params().set_var("check_run_history_number_of_completed_with_error",
                    jobSchedulerCheckRunHistoryOptions.numberOfCompletedWithError.Value());
            spooler_task.params().set_var("check_run_history_number_of_completed_successful",
                    jobSchedulerCheckRunHistoryOptions.numberOfCompletedSuccessful.Value());
        }
    }

}