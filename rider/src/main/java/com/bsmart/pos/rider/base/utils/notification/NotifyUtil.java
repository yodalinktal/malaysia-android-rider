package com.bsmart.pos.rider.base.utils.notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.services.ScheduleService;
import com.bsmart.pos.rider.views.LoginActivity;


public class NotifyUtil {

    public static void sendNotify(Context context, String msg, boolean isStick, PendingIntent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(context, msg, isStick, intent);
        if (isStick) {
            nm.notify(1, notification);
        } else {
            nm.notify(2, notification);
        }
    }

    public static Notification getNotification(Context context, String msg, boolean isStick, PendingIntent intent) {
        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("RRU", "RRU", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 注册通道，注册后除非卸载再安装否则不改变
            nm.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent);
        builder.setChannelId("POSCustomer");
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        int flags = (Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_ONLY_ALERT_ONCE);
        if (isStick) {
            flags |= (Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR);
        } else {
            flags |= (Notification.FLAG_AUTO_CANCEL);
        }
        notification.flags |= flags;
        return notification;
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static void setAlarm(Context context) {
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(
                "com.yoda.cn.ticker"), 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(mPendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 5 * 60000, mPendingIntent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Activity activity) {
        String packageName = activity.getPackageName();
        PowerManager pm = (PowerManager) activity
                .getSystemService(Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivityForResult(intent, 100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setJobSchedule(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1,
                    new ComponentName(context.getPackageName(), ScheduleService.class.getName()));
            builder.setPeriodic(30 * 60000);
            scheduler.cancelAll();
            scheduler.schedule(builder.build());
        }
    }
}
