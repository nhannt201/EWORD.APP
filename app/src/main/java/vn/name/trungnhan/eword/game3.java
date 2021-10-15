package vn.name.trungnhan.eword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class game3 extends AppCompatActivity {
     int DIEM = 0 ;
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;
    private Button da1;
    private Button da2;
    private TextView txt_da;
    String getname;
    String coamthanh = "0" ;
    private static MediaPlayer mn = null;
    private ImageView img_tu;
    String name_fd_tm = "";
    Integer toida_game = 0;
 //   String[] luu_cache ;
    String luu_cache = "";
    private ProgressBar tientrinh;
    Integer num_cache;
    String dapan = "";
    String kq_ch = "";
    TextToSpeech t1;
    String nghiavn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game3);
        img_tu = findViewById(R.id.img_gm3);
        tientrinh = findViewById(R.id.pr_gm3);
        txt_da = findViewById(R.id.txt_gm3);
        da1 = findViewById(R.id.bt_gm3_1);
        da1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kq_ch = da1.getText().toString();
                ck_game();
            }
        });
        da2 = findViewById(R.id.bt_gm3_2);
        da2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kq_ch = da2.getText().toString();
                ck_game();
            }
        });
        SP_fd = getSharedPreferences(fd_name, 0 );
        coamthanh = SP_fd.getString("main_sound",null);
        getname = SP_fd.getString("fd_name","");
        new ToastTB(this, getString(R.string.freeimg));
        File file_tv = new File(this.getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word/" + getname + ".ewd");
        //Mo file TV bat ki va lay tu vung
        try {
            FILE_XULY file_xuly = new FILE_XULY(file_tv);
            String[] arr_tv = file_xuly.catchuoi();
            for (String a : arr_tv) {
                luu_cache = luu_cache + a.trim() + "|";
            }
            num_cache = arr_tv.length;
          //  luu_cache = arr_tv;
            //Xay dung game
            if (num_cache<= 10) {
                toida_game = 15;
            } else if ((num_cache > 10) && (num_cache <= 30) ) {
                toida_game = 25;
            } else if ((num_cache > 30) && (num_cache <= 60) ) {
                toida_game = 40;
            } else if ((num_cache > 60) && (num_cache <= 100) ) {
                toida_game = 60;
            } else if ((num_cache > 100) && (num_cache <= 150) ) {
                toida_game = 80;
            } else {
                toida_game = 100;
            }
            tientrinh.setMax(toida_game);
            lay_tv();
        } catch (IOException e) {
            e.printStackTrace();
        }

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }
    public void onBackPressed() {
        if (mn != null) {
            mn.stop();
            mn.release();
            mn = null;
        }
        Intent intent= new Intent(getApplicationContext(), Game.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
        game3.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finish();
    }
    public void int_tv(Integer integer, String string) {
        String[] luutam = luu_cache.split("\\|");
        String randomStr_tv = luutam[new Random().nextInt(luutam.length)];
        String[] hienthi_tv = randomStr_tv.split("~");
        if (hienthi_tv[0].equals(string)) {
            int_tv(integer,string);
        } else {
            if (integer == 0) {
                da2.setText(hienthi_tv[0]);
            } else {
                da1.setText(hienthi_tv[0]);
            }
            da1.setEnabled(true);
            da2.setEnabled(true);
        }
    }
    public void lay_tv(){
        img_tu.setImageResource(R.mipmap.wait);
        txt_da.setText("");
                //het xu ly
        da1.setEnabled(false);
        da2.setEnabled(false);
        String[] arrOfStr = luu_cache.split("\\|");
        String randomStr = arrOfStr[new Random().nextInt(arrOfStr.length)];
            //    String randomStr_tv = luutam[new Random().nextInt(luutam.length)];
                String[] hienthi_tv = randomStr.split("~");
                if (arrOfStr.length > 1) {
                    Wait_img(hienthi_tv[0]);
                 dapan = (hienthi_tv[0]);
                 nghiavn = hienthi_tv[1];
                    Random random = new Random();
                    int rd = random.nextInt(2) ;
                    if (rd == 0) {
                        da1.setText(hienthi_tv[0]);
                        int_tv(0,hienthi_tv[0]);
                    } else {
                        da2.setText(hienthi_tv[0]);
                        int_tv(1,hienthi_tv[0]);
                    }
                 //   get_txt = hienthi_tv[0] + " : " + hienthi_tv[1];
                 //   nghia1 = hienthi_tv[0];
                //    nghia2 = hienthi_tv[1];
                } else {
                 //   get_txt = mcontext.getString(R.string.remembr_you);
                }



    }
    private void ms_thongbao(boolean debik) {
        //  THONGBAOGAME thongbaogame = new THONGBAOGAME(Game1.this,"THÔNG BÁO","CHÚC MỪNG BẠN ĐÃ CHIẾN THẮNG!");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.thongbao));
        if (debik == true) {
            builder.setMessage(getString(R.string.youwin));
            dichvugame dichvugame = new dichvugame(game3.this, 1);
            dichvugame.sendgame();
        } else {
            builder.setMessage(getString(R.string.youfail));
            dichvugame dichvugame = new dichvugame(game3.this, 0);
            dichvugame.sendgame();
        }
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mn != null) {
                    mn.stop();
                    mn.release();
                    mn = null;
                }
                onBackPressed();
            }
        });
        builder.show();
        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mn != null) {
                            mn.stop();
                            mn.release();
                            mn = null;
                        }
                        onBackPressed();
                    }
                }
        );
    }
    private void ck_game() {
        //  hideKeyboard(Game1.this); //won't work
        if (kq_ch.trim().toLowerCase().contains(dapan.toLowerCase().trim())) {

            if (DIEM > toida_game) {
                ms_thongbao(true);
             //   play_eff(R.raw.thang);
              //  txt_kq_input.setEnabled(false);
             //   hideKeyboard(Game1.this); //won't work
              //  ck_da.setText(getString(R.string.kethuc));
                //  Toast.makeText(getApplicationContext(), "Bạn đã thắng rồi nha!!", Toast.LENGTH_LONG).show();
                //  tientrinhagme.setProgress(0);
            } else {
                //DUoi 50 diem
              //  play_eff(R.raw.dung);
                DIEM +=1;
                t1.speak(dapan, TextToSpeech.QUEUE_FLUSH, null);
                tientrinh.setProgress(DIEM);
                tientrinh.startAnimation(AnimationUtils.loadAnimation(game3.this, R.layout.eff_move_bt));
             //   txt_kq_input.setText("");
                lay_tv();
            }
        } else {

            if (DIEM <= 0) {
                DIEM =0;
             //   txt_kq_input.setEnabled(false);
               // hideKeyboard(Game1.this); //won't work
                ms_thongbao(false);
             //   play_eff(R.raw.thua);
             //   ck_da.setText(getString(R.string.kethuc));
            }else {
                DIEM -=1;
            }
         //   txt_kq_input.setText("");
            tientrinh.setProgress(DIEM);
            tientrinh.startAnimation(AnimationUtils.loadAnimation(game3.this, R.layout.shake_3));
            THONGBAOGAME showsai = new THONGBAOGAME(this, getString(R.string.gm_wr), getString(R.string.dala) + " " + dapan + "\n" + getString(R.string.nghiala)  + " " +nghiavn);
            showsai.getTBS();
            // Toast.makeText(getApplicationContext(), "TIẾC QUÁ SAI RỒI NÈ!\n" + txt_tv.getText().toString() + " : " +dapan + "\nMỚI ĐÚNG NHA!", Toast.LENGTH_LONG).show();

            lay_tv();
        }
    }
    public void Lay_img(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall( request ).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myRespose = response .body().string();
                    game3 .this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           String setString = myRespose.trim();
                           if (setString.equals("https://i.imgur.com/B1Q9h26.png")) {
                               txt_da.setText(nghiavn);
                               img_tu.setImageResource(R.mipmap.noimg);
                           } else {
                               txt_da.setText(getString(R.string.anh_conghia));
                               Picasso.get().load(myRespose.trim()).into(img_tu);
                           }
                        //    Toast.makeText(Find_one.this, "Nhận thông tin sản phẩm..."  , Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
    private void Wait_img(final String string) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Lay_img("https://<server>/eword/api_img.php?key=" + string);
            }
        }, 1);
    }
}
