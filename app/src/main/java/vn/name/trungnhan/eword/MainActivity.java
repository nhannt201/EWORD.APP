package vn.name.trungnhan.eword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Random;

/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class MainActivity extends AppCompatActivity {
    private Button thuvien_click;
    private Button tuvung;
    private Button trochoi;
    private Button caidat;
    private Button tacgia;
    private Button rank;
    private ImageView logo_main;
    String coamthanh = null ;
    public static String fd_name = "folder_tam";
   // public static String log_reg = "logreg";
  //  String login_has = "";
  //  SharedPreferences lgrg;
    SharedPreferences SP_fd;
    String hieuung = "true";
    String thongbao_share = "";
    String bat_nhacnho = "";
  public static MediaPlayer mp = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ghi data nho tam
        SP_fd = getSharedPreferences(fd_name, 0);
        coamthanh = SP_fd.getString("main_sound",null);
        hieuung = SP_fd.getString("st_eff",null);
        thongbao_share = SP_fd.getString("nd_tb", null);
        bat_nhacnho = SP_fd.getString("st_nc", null);
        if (bat_nhacnho == null) {
            SharedPreferences.Editor editit = SP_fd.edit();
            editit.putString("st_nc", "false");
            editit.apply();
        }
        if (thongbao_share == null) {
            SharedPreferences.Editor editit = SP_fd.edit();
            editit.putString("nd_tb", "new");
            editit.apply();
        }
        if (hieuung == null) {
            SharedPreferences.Editor editit = SP_fd.edit();
            editit.putString("st_eff", "true");
            editit.apply();
            hieuung = "false";
        } else if (hieuung.contains("true")) {
            hieuung = "true";
        } else {
            hieuung = "false";
        }
        //logreg
        /**    lgrg = getSharedPreferences(log_reg, 0);
     login_has =  lgrg.getString("log",null);
        if (login_has == null) {
            SharedPreferences.Editor editit = lgrg.edit();
            editit.putString("log", "false");
            editit.commit();
        } */

        if (coamthanh == (null)) {
            coamthanh = "0";
            SharedPreferences.Editor editit = SP_fd.edit();
            editit.putString("main_sound", "0");
            editit.commit();
            play_music();
            Toast.makeText(MainActivity.this, "Welcome!"  , Toast.LENGTH_SHORT).show();

        } else {
            if (coamthanh.equals("0")) {
                play_music();
            }
        }
        //Phan button control...

                rank = findViewById(R.id.bt_bxh);
        if (hieuung.contains("true")) {
            rank.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake_3));
        }
                rank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getApplicationContext(), BXH.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        // Toast.makeText(MainActivity.this, "Đang mở 'TRÒ CHƠI'"  , Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        /** Fading Transition Effect */
                        MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
            thuvien_click = findViewById(R.id.bt_lib);
        if (hieuung.contains("true")) {
            thuvien_click.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake_3));
        }
            thuvien_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(getApplicationContext(), THUVIEN.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                   // Toast.makeText(MainActivity.this, "Đang mở 'TRÒ CHƠI'"  , Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    /** Fading Transition Effect */
                    MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });



        //Lay danh sach thu muc TV
        /**
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word");        for (File f : folder.listFiles()) {
            if (f.isFile())
              name_fd_tm =   name_fd_tm + f.getName() + "|";
        }
        String[] arrOfStr = name_fd_tm.split("\\|");
                if ( arrOfStr.length > 0 ) {
                    String randomStr = arrOfStr[new Random().nextInt(arrOfStr.length)];
                    getname = randomStr;
                    File file_tv = new File(getExternalFilesDir(null).getAbsolutePath() +
                            "/Folder_Word/" + getname );
                    //Mo file TV bat ki va lay tu vung
                    try {
                        FILE_XULY file_xuly = new FILE_XULY(file_tv);
                        String[] arr_tv = file_xuly.catchuoi();
                        String randomStr_tv = arr_tv[new Random().nextInt(arr_tv.length)];
                        String[] hienthi_tv = randomStr_tv.split("~");
                        if (arr_tv.length > 1) {
                            Toast.makeText(getApplicationContext(), hienthi_tv[0] + " : " + hienthi_tv[1], Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
        //Het doc
        //



        //hetmusic
        logo_main = findViewById(R.id.img_main_logo);
        if (hieuung.contains("true")) {
            logo_main.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake));
        }
    //    img_ef = findViewById(R.id.img_ef1);
    //    img_ef.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.ef_q));


        tuvung = findViewById(R.id.bt_tv);
        if (hieuung.contains("true")) {
            tuvung.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake_3));
        }
        tuvung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_music();

                Intent intent= new Intent(getApplicationContext(), Tuvung.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
              //  Toast.makeText(MainActivity.this, "Đang mở 'TỪ VỪNG'"  , Toast.LENGTH_SHORT).show();
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        trochoi = findViewById(R.id.bt_gm);
        if (hieuung.contains("true")) {
            trochoi.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake_3));
        }
        trochoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_music();

                Intent intent= new Intent(getApplicationContext(), Game.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                //Toast.makeText(MainActivity.this, "Đang mở 'TRÒ CHƠI'"  , Toast.LENGTH_SHORT).show();
                startActivity(intent);
                /** Fading Transition Effect */
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        caidat = findViewById(R.id.bt_st);
        if (hieuung.contains("true")) {
            caidat.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake_3));
        }
        caidat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(), Caidat.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
              //  Toast.makeText(MainActivity.this, "Đang mở 'CÀI ĐẶT'"  , Toast.LENGTH_SHORT).show();
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        tacgia = findViewById(R.id.bt_tg);
        if (hieuung.contains("true")) {
            tacgia.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.layout.shake_3));
        }
        tacgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplicationContext(), tacgia.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                //Toast.makeText(MainActivity.this, "Đang mở 'TÁC GIẢ'"  , Toast.LENGTH_SHORT).show();
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

    }


private void play_music() {
 //   mp.setLooping(true);
    // Play sound using  resource reference.
    if (mp != null) {
        mp.stop();
    }

    Random random = new Random();
    int randomNumber = random.nextInt(2) ;
    if (randomNumber == 0) {
        mp = MediaPlayer.create(this, R.raw.background);
  } else {
        mp = MediaPlayer.create(this, R.raw.backround2);
    }
    mp.start();
}



private void stop_music() {
    SP_fd = getSharedPreferences(fd_name, 0);
    coamthanh = SP_fd.getString("main_sound",null);
    if (coamthanh .equals("0")) {
        if (mp != null) {
           mp.stop();
          mp.release();
          mp = null;
       }
    }

}
    //start
    @Override
    public void onBackPressed() {
     //   mp.stop();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    //end

    private String load_anti(String rass) {
        rass = rass.replace("SHjhjYUkmlrdrJBlllkSPGCH","").replace("jjkkhFIUDMKSDJFLNVVKDFNF","")
                .replace("MUSADNfdfLkjljNKNgFKJNFTU","").replace("lLJDNMMVCDXJDSFuihilIVXLN","");
        return  rass;
    }

    private boolean isAnti(String string) {
        if (string.contains("SHjhjYUkmlrdrJBlllkSPGCH") == true) {
            return true;
        } else if (string.contains("jjkkhFIUDMKSDJFLNVVKDFNF") == true) {
            return true;
        } else if (string.contains("MUSADNfdfLkjljNKNgFKJNFTU") == true) {
            return true;
        } else return string.contains("lLJDNMMVCDXJDSFuihilIVXLN") == true;
    }
 /**
private void Hide_lib_tv(){

    new Handler().postDelayed(new Runnable(){
        @Override
        public void run() {
            hide_thuvien_moi.setVisibility(View.INVISIBLE);
        }
    }, 5000);
}*/



}
