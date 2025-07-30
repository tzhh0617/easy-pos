package com.freemud.app.easypos.common.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;


import org.simple.eventbus.EventBus;

/**
 * Created by shuyuanbo on 2022/3/11.
 * Description:
 */
public class TimeSyncSchedule extends JobService {

    private JobScheduler jobScheduler;
    private int JOBID = 100;
    private JobInfo jobInfo;
    private int period = 60000;     //周期60s

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //

        startSchedule(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public void startSchedule(Context context) {
        if (jobScheduler == null) {
            jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        cancelSchedule();
        if (jobInfo == null) {
            jobInfo = new JobInfo.Builder(JOBID, new ComponentName(context, TimeSyncSchedule.class))
                    .setMinimumLatency(period).build();
        }
        jobScheduler.schedule(jobInfo);
    }

    private void cancelSchedule() {
        if (jobScheduler != null)
            jobScheduler.cancel(JOBID);
    }
}
