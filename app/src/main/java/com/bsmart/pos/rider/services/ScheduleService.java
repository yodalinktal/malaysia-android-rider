package com.bsmart.pos.rider.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScheduleService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("Yoda", "定时Job 执行.");
        Intent i = new Intent(this, CheckCaseService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(i);
        } else {
            this.startService(i);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
