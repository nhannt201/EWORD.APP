package vn.name.trungnhan.eword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;

import vn.name.trungnhan.eword.login.Login;

//import androidx.annotation.RequiresApi;


public class ScreenIntro extends AppCompatActivity {
   // private TextView textView;
   // Integer nhoo = 0 ;
   // private ProgressBar progressBar;
  //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   String login_has = "";
    SharedPreferences lgrg;
    public static String log_reg = "logreg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_intro);
      //  progressBar = findViewById(R.id.progressBar_intro);
    //    progressBar.setVisibility(View.INVISIBLE);
     //   progressBar.setVisibility(View.VISIBLE);
     //   textView = findViewById(R.id.txt_khoitao);
     //   textView.setVisibility(View.INVISIBLE);
       // Object downloadManager = getSystemService(Context.DOWNLOAD_SERVICE);
        //Theo doi cua GG Analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ID");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "NAME");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Open_App");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        File folder = new File(getExternalFilesDir(null).getAbsolutePath() +
                 "/Folder_Word");
        File folder_backup = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Bachup");
        File folder_data = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media");
     //   boolean success = true;
        if (!folder.exists()) {
             folder.mkdirs();
        //     progressBar.setProgress(2);

          //  SharedPreferences.Editor editit = SP_fd.edit();
         //   editit.putString("main_sound", "1");
         //   editit.commit();
         //   textView.setText("Khởi tạo lần đầu, vui lòng đợi...");
         //   getlocal getlocal = new getlocal();
          //  if ( getlocal.get() == "en") {
                Toast.makeText(ScreenIntro.this, getString(R.string.khoitao_dau)  , Toast.LENGTH_SHORT).show();
        }
        // else {
        //    textView.setText("Đang nạp dữ liệu...");
          //  progressBar.setProgress(7);
        //}
     //   if (!folder_data.exists()) {
     //       folder_data.mkdirs();
      //  }
        if (!folder_backup.exists()) {
            folder_backup.mkdirs();
        //    textView.setText("Đang tải dữ liệu âm thanh...");
         //   taifilelandau();
      //  } else {
      //     textView.setText("Đang nạp dữ liệu...");
       //     taifilelandau();
        }
        lgrg = getSharedPreferences(log_reg, 0);
        login_has =  lgrg.getString("log",null);


        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                   sleep(500);

                 //   if (conga.contains("coroi")) {
                //        Intent intent = new Intent(getApplicationContext(), Home.class);
                //        startActivity(intent);
                //    } else {
                    if (login_has == null) {
                        SharedPreferences.Editor editit = lgrg.edit();
                        editit.putString("log", "false");
                        editit.commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } /**else if (login_has.contains("true")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }*/ else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                  //  }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
    private String getDeviceResolution()
    {
        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density)
        {
            case DisplayMetrics.DENSITY_MEDIUM:
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }
    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
  /**  private void taifilelandau(){
      //  check();
        String url_dv = "https://apps.4it.top/eword/sound/";
        final File file1 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/background.mp3");
        final File file2 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/background_2.mp3");
        final File file3 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/dung.wav");
        final File file4 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/game_start.mp3");
        final File file5 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/nen_game.mp3");
        final File file6 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/sai.wav");
        final File file7 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/thang.mp3");
        final File file8 = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Media/thua.wav");
        if (!file1.exists()) {
            DownloadFile d1 = new DownloadFile();
            d1.DownloadFile(ScreenIntro.this, url_dv+ "background.mp3", "background.mp3", progressBar);
            d1.RegisterDownloadManagerReciever(this);

        } else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file2.exists()) {
            DownloadFile d2 = new DownloadFile();
            d2.DownloadFile(ScreenIntro.this, url_dv+ "backround_2.mp3", "background_2.mp3", progressBar);
            d2.RegisterDownloadManagerReciever(this);

        } else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file3.exists()) {
            DownloadFile d3 = new DownloadFile();
            d3.DownloadFile(ScreenIntro.this, url_dv + "dung.wav", "dung.wav", progressBar);
            d3.RegisterDownloadManagerReciever(this);

        }else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file4.exists()) {
            DownloadFile d4 = new DownloadFile();
            d4.DownloadFile(ScreenIntro.this, url_dv + "game_start.mp3", "game_start.mp3", progressBar);
            d4.RegisterDownloadManagerReciever(this);

        }else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file5.exists()) {
            DownloadFile d5 = new DownloadFile();
            d5.DownloadFile(ScreenIntro.this, url_dv + "nen_game.mp3", "nen_game.mp3", progressBar);
            d5.RegisterDownloadManagerReciever(this);

        }else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file6.exists()) {
            DownloadFile d6 = new DownloadFile();
            d6.DownloadFile(ScreenIntro.this, url_dv + "sai.wav", "sai.wav", progressBar);
            d6.RegisterDownloadManagerReciever(this);

        }else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file7.exists()) {
            DownloadFile d7 = new DownloadFile();
            d7.DownloadFile(ScreenIntro.this, url_dv + "thang.mp3", "thang.mp3", progressBar);
            d7.RegisterDownloadManagerReciever(this);

        }else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
        if (!file8.exists()) {
            DownloadFile d8 = new DownloadFile();
            d8.DownloadFile(ScreenIntro.this, url_dv + "thua.wav", "thua.wav", progressBar);
            d8.RegisterDownloadManagerReciever(this);

        }else {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }

    }
    //END FORM */
  /**  private void check(){
        new Handler().postDelayed(new Runnable(){
        //    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
              if (progressBar.getProgress() >= 8) {
                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(intent);
                       textView.setText("Khởi tạo hoàn tất!");
                  Toast.makeText(ScreenIntro.this, "Khởi tạo hoàn tất!"  , Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.INVISIBLE);
                       finish();
              } else {
                  check();
              }

            }
        }, 500);
    } */

}