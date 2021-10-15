package vn.name.trungnhan.eword;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.ProgressBar;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class DownloadFile {
    private Context mContext;
   // private String url;
    private ProgressBar progressBar;
    public void DownloadFile(Context context, String url, String namefile, ProgressBar progressBar){
        mContext = context;
        //this.url = url;
        this.progressBar = progressBar;
        String serviceString = Context.DOWNLOAD_SERVICE;
        DownloadManager downloadManager;
        downloadManager = (DownloadManager)mContext.getSystemService(serviceString);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalFilesDir(mContext,  "/Media", namefile);
        long reference = downloadManager.enqueue(request);
//        RegisterDownloadManagerReciever(mContext);

    }

    public void RegisterDownloadManagerReciever(Context context) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

              Integer integer =    progressBar.getProgress();
                    integer = integer +1;
                    //new ToastTB(mContext, integer);
                    progressBar.setProgress(integer);

                }
            }
        };
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
