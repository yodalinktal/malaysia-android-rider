package com.bsmart.pos.rider.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.utils.notification.NotifyUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CheckCaseService extends Service {
    private final ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        Notification n = NotifyUtil.getNotification(this, "POSCustomer is running", true, NotifyUtil.getPendingIntent(this));
        this.startForeground(1, n);
        thread.scheduleAtFixedRate(netWorker, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Runnable netWorker = new Runnable() {
        private List<Integer> caseBeans = null;

        @Override
        public void run() {
            Map<String, String> requestData = App.getMetaRequestData();
            //API
        }
    };
}
