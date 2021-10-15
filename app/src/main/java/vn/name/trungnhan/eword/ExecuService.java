package vn.name.trungnhan.eword;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class ExecuService extends BroadcastReceiver {
    public static String fd_name = "folder_tam";
    private SharedPreferences SP_fd;
    String get_txt = "";
    String nghia1 = "";
    String nghia2 = "";
    @Override
    public void onReceive(Context mcontext, Intent intent2) {
        SP_fd = mcontext.getSharedPreferences(fd_name, 0);
        String thongbao_share = SP_fd.getString("nd_tb", null);
        String bat_nhacnho = SP_fd.getString("st_nc", null);
        if (bat_nhacnho.contains("true")) {
            SharedPreferences.Editor editit = SP_fd.edit();

            String name_fd_tm = "";
            String getname = "";
            File folder = new File(mcontext.getExternalFilesDir(null).getAbsolutePath() +
                    "/Folder_Word");
            for (File f : folder.listFiles()) {
                if (f.isFile())
                    name_fd_tm = name_fd_tm + f.getName() + "|";
            }
            String[] arrOfStr = name_fd_tm.split("\\|");
            if (arrOfStr.length > 0) {
                String randomStr = arrOfStr[new Random().nextInt(arrOfStr.length)];
                getname = randomStr;
                File file_tv = new File(mcontext.getExternalFilesDir(null).getAbsolutePath() +
                        "/Folder_Word/" + getname);
                //Mo file TV bat ki va lay tu vung
                try {
                    FILE_XULY file_xuly = new FILE_XULY(file_tv);
                    String[] arr_tv = file_xuly.catchuoi();
                    String randomStr_tv = arr_tv[new Random().nextInt(arr_tv.length)];
                    String[] hienthi_tv = randomStr_tv.split("~");
                    if (arr_tv.length > 1) {
                        get_txt = hienthi_tv[0] + " : " + hienthi_tv[1];
                        nghia1 = hienthi_tv[0];
                        nghia2 = hienthi_tv[1];
                        editit.putString("nd_tb", get_txt);
                        editit.commit();
                    } else {
                        get_txt = mcontext.getString(R.string.remembr_you);
                        editit.putString("nd_tb", "new");
                        editit.commit();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //  Log.i("nayne","da thong bao");
            // Toast.makeText(context,"Hello!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mcontext, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(NOTIFICATION_SERVICE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification

                //  NotificationChannel notificationChannel = new NotificationChannel(mcontext.getString(R.string.default_notification_channel_id), "Nhắc nhở từ vựng", NotificationManager.IMPORTANCE_HIGH);
                NotificationChannel channel = new NotificationChannel("1", mcontext.getString(R.string.thongbao_nhacnho), importance);

                channel.setDescription("Kenh 1");
                channel.setShowBadge(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
            }
            if (nghia1.trim().length() > 0) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mcontext, "1")
                    .setContentTitle(nghia1)
                    .setSmallIcon(R.mipmap.logo_nof)
                    .setContentText(nghia2)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_MAX)
                 //   .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);

           // Random random = new Random();
          //  int randomNumber = random.nextInt(2000);
            //  notificationManager.notify(1122 /* ID of notification */, notificationBuilder.build());

                Notification buildNotification = notificationBuilder.build();
                NotificationManager mNotifyMgr = (NotificationManager) mcontext.getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(1122, buildNotification);
            } else {
                Log.i(TAG, "Notifications are disabled. Because it tu vung");
            }
        } else {
            Log.i(TAG, "Notifications are disabled");
        }

    }



}
