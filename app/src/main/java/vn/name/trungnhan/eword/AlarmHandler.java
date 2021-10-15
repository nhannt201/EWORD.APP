package vn.name.trungnhan.eword;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class AlarmHandler {
    private static long MILLISECS_PER_MIN = 60000L;
    private static long delay = MILLISECS_PER_MIN * 1;   // 2 min
    private Context context;
    public AlarmHandler(Context context){
        this.context = context;
    }
    public void setAlarmManager(){
        Intent intent = new Intent(context, ExecuService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,9812,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null){
            long triggerAfter = 15 * 60 * 1000;
            long triggerEvery = 15 * 60 * 1000;
            am.setRepeating(AlarmManager.RTC_WAKEUP,triggerAfter, triggerEvery, sender);
        }
        Log.i("nayne","bat dau");
    }

    public void cancelAlarmManager(){
        Intent intent = new Intent(context, ExecuService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,9812,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null){
            am.cancel(sender);
        Log.i("nayne","lap lai");
        }

    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //calendar.add(Calendar.HOUR, NOTIFICATIONS_INTERVAL_IN_HOURS);
        return calendar.getTimeInMillis();
    }
}
