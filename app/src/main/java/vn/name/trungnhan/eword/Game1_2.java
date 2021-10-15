package vn.name.trungnhan.eword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class Game1_2 extends AppCompatActivity {
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;
   private TextView txt1;
   private TextView txt_tv;
    private TextView txt_kq_input;
   private Button ck_da;
   private ProgressBar tientrinhagme;
   private ImageView soundam;
    String getname;
    String de_save = "";
    String dapan;
    String coamthanh = "0" ;
    Integer toida_game = 0;
    public static MediaPlayer mn = null;
    private TextView ck_da_view;
    //   String TV;
 //   String TA;
    Integer DIEM = 0;
 //   Integer total_arr = 0;
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1_2);
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ck_da_view = findViewById(R.id.txt_ck_da);
        soundam = findViewById(R.id.sound_sg1);
        SP_fd = getSharedPreferences(fd_name, 0);
        coamthanh = SP_fd.getString("main_sound",null);
        if (coamthanh.equals("0")) {
            mn = MediaPlayer.create(this, R.raw.nengame);
            mn.setVolume(40,40);
            mn.start();
            coamthanh = "1";
        } else {
            coamthanh = "0";
            soundam.setImageResource(R.mipmap.mute_sound);
        }
        getname = SP_fd.getString("fd_name","");

        //   soundam.startAnimation(AnimationUtils.loadAnimation(Game1.this, R.layout.eff_move));
        soundam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coamthanh == "0") {
                    mn = MediaPlayer.create(Game1_2.this, R.raw.nengame);
                    mn.setVolume(40,40);
                    mn.start();
                    coamthanh = "1";
                    soundam.setImageResource(R.mipmap.has_sound);
                } else {
                    coamthanh = "0";
                    soundam.setImageResource(R.mipmap.mute_sound);
                    if (mn != null) {
                        mn.stop();
                        mn.release();
                        mn = null;
                    }
                }
            }
        });
        //
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        //
       //
        tientrinhagme = findViewById(R.id.tientrinhagme_1);
     //   tientrinhagme.setMax(50);

        //
       txt1 = findViewById(R.id.txt_title_gm1_1_1);
        if (getname.length() > 6) {
            txt1.setText(txt1.getText().toString() + " - " + (getname.substring(0, 6))+"...");
        } else {
            txt1.setText(txt1.getText().toString() + " - " + getname);
        }
        File file_tv = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word/" + getname + ".ewd");
        FILE_XULY file_xuly = new FILE_XULY(file_tv);
        try {
            String[] arrOfStr = file_xuly.catchuoi();
            for (String a : arrOfStr) {
                de_save = de_save + a.trim() + "|";
            }
            //Xay dung game
            if (arrOfStr.length <= 10) {
                toida_game = 15;
            } else if ((arrOfStr.length > 10) && (arrOfStr.length <= 30) ) {
                toida_game = 25;
            } else if ((arrOfStr.length > 30) && (arrOfStr.length <= 60) ) {
                toida_game = 40;
            } else if ((arrOfStr.length > 60) && (arrOfStr.length <= 100) ) {
                toida_game = 60;
            } else if ((arrOfStr.length > 100) && (arrOfStr.length <= 150) ) {
                toida_game = 80;
            } else {
                toida_game = 100;
            }
            tientrinhagme.setMax(toida_game);
            String randomStr = arrOfStr[new Random().nextInt(arrOfStr.length)];
            String[] xulidapan = randomStr.split("~");
            dapan = xulidapan[0];
            txt_tv = findViewById(R.id.txt_TAGAME_1_1);
            txt_tv.setText(xulidapan[1]);
            //t1.speak(xulidapan[0], TextToSpeech.QUEUE_FLUSH, null);
            txt_kq_input = findViewById(R.id.txt_gm1_kq_input);
            txt_kq_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        /* Write your logic here that will be executed when user taps next button */
                    ck_game();

                        handled = true;
                    }
                    return handled;
                }

            });
            ck_da = findViewById(R.id.bt_guiDA);
            ck_da.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((DIEM <0) || (DIEM > (toida_game))) {
                        onBackPressed();
                    } else {
                        ck_game();
                    }
                }
            });
          //  Toast.makeText(getApplicationContext(), randomStr, Toast.LENGTH_LONG).show();

         //   txt_tv.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ck_game() {
      //  hideKeyboard(Game1.this); //won't work
        if (txt_kq_input.getText().toString().trim().toLowerCase().contains(dapan.toLowerCase().trim())) {
            t1.speak(dapan, TextToSpeech.QUEUE_FLUSH, null);

            if (DIEM > toida_game) {
                ms_thongbao(true);
                if (mn != null) {
                    mn.stop();
                    mn.release();
                    mn = null;
                }
                play_eff(R.raw.thang);
                txt_kq_input.setEnabled(false);
                hideKeyboard(Game1_2.this); //won't work
                ck_da.setText(getString(R.string.kethuc));
              //  Toast.makeText(getApplicationContext(), "Bạn đã thắng rồi nha!!", Toast.LENGTH_LONG).show();
              //  tientrinhagme.setProgress(0);
            } else {
                //DUoi 50 diem
                play_eff(R.raw.dung);
                Random random = new Random();
                int randomNumber = random.nextInt(3) ;
                if (randomNumber == 0) {
                    // Toast.makeText(getApplicationContext(), "ĐÚNG RỒI!", Toast.LENGTH_LONG).show();
                    ck_da_view.setText(getString(R.string.dungr));
                }else if (randomNumber == 1) {
                    //  Toast.makeText(getApplicationContext(), "BẠN GIỎI QUÁ!", Toast.LENGTH_LONG).show();
                    ck_da_view.setText(getString(R.string.gioiqua));
                }  else {
                    //   Toast.makeText(getApplicationContext(), "XUẤT SẮC!", Toast.LENGTH_LONG).show();
                    ck_da_view.setText(getString(R.string.xuatsac));
                }
                DIEM +=1;
                tientrinhagme.setProgress(DIEM);
                  tientrinhagme.startAnimation(AnimationUtils.loadAnimation(Game1_2.this, R.layout.eff_move_bt));
                txt_kq_input.setText("");
                next_game();
            }
        } else {

            if (DIEM <= 0) {
                DIEM =0;
                txt_kq_input.setEnabled(false);
                hideKeyboard(Game1_2.this); //won't work
                ms_thongbao(false);
                play_eff(R.raw.thua);
                ck_da.setText(getString(R.string.kethuc));
            }else {
                DIEM -=1;
            }
            txt_kq_input.setText("");
            tientrinhagme.setProgress(DIEM);
            play_eff(R.raw.sai);
            tientrinhagme.startAnimation(AnimationUtils.loadAnimation(Game1_2.this, R.layout.shake_3));
            THONGBAOGAME showsai = new THONGBAOGAME(this, getString(R.string.gm_wr),txt_tv.getText().toString() + " " +  getString(R.string.iss)  + " " +dapan);
            showsai.getTBS();
            //  Toast.makeText(getApplicationContext(), "TIẾC QUÁ SAI RỒI NÈ!\n" + txt_tv.getText().toString() + " : " +dapan + "\nMỚI ĐÚNG NHA!", Toast.LENGTH_LONG).show();
            Random random = new Random();
            int randomNumber = random.nextInt(3) ;
            if (randomNumber == 0) {
                ck_da_view.setText(getString(R.string.colen));
            }else if (randomNumber == 1) {
                ck_da_view.setText(getString(R.string.thatbaila));
            }  else {
                ck_da_view.setText(getString(R.string.cuocsongma));
            }
            next_game();
        }
    }
    private void next_game(){
     //   Random random = new Random();
      //  int randomNumber = random.nextInt(2) ;
        String[] arrOfStr = de_save.split("\\|");
        String randomStr = arrOfStr[new Random().nextInt(arrOfStr.length)];
        String[] xulidapan = randomStr.split("~");
     //   if (randomNumber == 0) {
    //        dapan = xulidapan[1];
     //       txt_tv.setText(xulidapan[0]);
      //      t1.speak(xulidapan[0], TextToSpeech.QUEUE_FLUSH, null);
      //  } else {
            dapan = xulidapan[0];
            txt_tv.setText(xulidapan[1]);
      //  }

    }
private void ms_thongbao(boolean debik) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(getString(R.string.thongbao));

    if (debik == true) {
        builder.setMessage(getString(R.string.youwin));
        dichvugame dichvugame = new dichvugame(Game1_2.this, 1);
        dichvugame.sendgame();
    } else {
        builder.setMessage(getString(R.string.youfail));
        dichvugame dichvugame = new dichvugame(Game1_2.this, 0);
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
                    //When you touch outside of dialog bounds,
                    //the dialog gets canceled and this method executes.
                    onBackPressed();
                }
            }
    );
}
    private void play_eff(int as) {
       mn = MediaPlayer.create(this, as);
       mn.start();
    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //start
    @Override
    public void onBackPressed() {
        if (mn != null) {
            mn.stop();
            mn.release();
            mn = null;
        }
        Intent intent= new Intent(getApplicationContext(), Game.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
      //  Toast.makeText(Game1.this, "Đang về trang chủ"  , Toast.LENGTH_SHORT).show();
        startActivity(intent);
        Game1_2.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finish();
    }
    //end

}
