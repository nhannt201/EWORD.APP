package vn.name.trungnhan.eword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
public class game2 extends AppCompatActivity {
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;
    private Button da1;
    private Button da2;
    private Button da3;
    private Button da4;
    private TextView txt1;
    private ProgressBar tientrinhagme;
    private ImageView soundam;
    private TextView txt_tv;
    String getname;
    String de_save = "";
    String dapan;
    String coamthanh = "0" ;
    Integer toida_game = 0;
    Integer da_en = 0 ;
    private static MediaPlayer mn = null;
    Integer DIEM = 0;
    TextToSpeech t1;
    String tranhtrunglap = "" ;
    private TextView ck_da_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ck_da_view = findViewById(R.id.txt_ck_da);
        da1 = findViewById(R.id.bt_gm2_da1);
        da2 = findViewById(R.id.bt_gm2_da2);
        da3 = findViewById(R.id.bt_gm2_da3);
        da4 = findViewById(R.id.bt_gm2_da4);
        soundam = findViewById(R.id.sound_sg2);
        SP_fd = getSharedPreferences(fd_name, 0);
        coamthanh = SP_fd.getString("main_sound",null);

        //  final MediaPlayer mpp = MediaPlayer.create(this, R.raw.background); //mp3 file in res/raw folder
      //  mpp.start();
        if (coamthanh.equals("0")) {
            play_eff(R.raw.nengame);
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
                if (coamthanh.equals("0")) {
                     play_eff(R.raw.nengame);
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
        tientrinhagme = findViewById(R.id.tientrinhagme_2);
        txt1 = findViewById(R.id.txt_title_game);
        if (getname.length() > 10) {
            txt1.setText(txt1.getText().toString() + " - " + (getname.substring(0, 10))+"...");
        } else {
            txt1.setText(txt1.getText().toString() + " - " + getname);
        }
        //
        //Log file game
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

            tranhtrunglap = tranhtrunglap + randomStr + "|";
            dapan = xulidapan[1];
            txt_tv = findViewById(R.id.txt_TAGAME_2);
            txt_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (da_en == 1) {
                        t1.speak(dapan, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
            txt_tv.setText(xulidapan[0]);
            //xulira4 dap an
            Random ran_bt = new Random();
            int ran_btg = ran_bt.nextInt(4) ;
            if (ran_btg == 0) {
                da1.setText(dapan);
                random_dapan(da2);
                random_dapan(da3);
                random_dapan(da4);
            } else  if (ran_btg == 1) {
                random_dapan(da1);
                da2.setText(dapan);
                random_dapan(da3);
                random_dapan(da4);
            } else  if (ran_btg == 2) {
                random_dapan(da1);
                random_dapan(da2);
                da3.setText(dapan);
                random_dapan(da4);
            } else {
                random_dapan(da1);
                random_dapan(da2);
                random_dapan(da3);
                da4.setText(dapan);
            }

            //het xu li
            t1.speak(xulidapan[0], TextToSpeech.QUEUE_FLUSH, null);

            //  Toast.makeText(getApplicationContext(), randomStr, Toast.LENGTH_LONG).show();

            //   txt_tv.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //end log

        da1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ck_game(da1);
            }
        });

        da2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck_game(da2);
            }
        });

        da3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck_game(da3);
            }
        });

        da4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck_game(da4);
            }
        });

    }
    Integer ran_doms = 0;
    private void random_dapan(Button button) {
        String[] arrOfStr = de_save.split("\\|");
        for (String dapan_cho : arrOfStr) {
            String randomStr2 = arrOfStr[new Random().nextInt(arrOfStr.length)];
            String sdfdsfdsf = randomStr2 + "|";
            if ((tranhtrunglap.indexOf(sdfdsfdsf) > 0)) {

            } else {
                tranhtrunglap =tranhtrunglap + randomStr2 + "|" ;
                String[] xulidapan = randomStr2.split("~");
                if (ran_doms == 0) {
                    button.setText(xulidapan[1]);
                } else  {
                    button.setText(xulidapan[0]);
                }

                break;
            }
        }
    }
    private void next_game() {
        tranhtrunglap = "";
        //xulira4 dap an
        Random random_ssss = new Random();
        int randomNumberssss = random_ssss.nextInt(2) ;
        if (randomNumberssss == 0) {
            ran_doms = 0;
            da_en = 0;
        } else  {
            ran_doms = 1;
            da_en = 1;
        }
        String[] arrOfStr = de_save.split("\\|");
        Random ran_bt = new Random();
        String randomStr2 = arrOfStr[new Random().nextInt(arrOfStr.length)];
        tranhtrunglap =tranhtrunglap + randomStr2 + "|" ;
        String[] xulidapan = randomStr2.split("~");

        if (ran_doms == 0) {
            dapan = xulidapan[1];
            txt_tv.setText(xulidapan[0]);

            t1.speak(xulidapan[0], TextToSpeech.QUEUE_FLUSH, null);
        } else  {
            dapan = xulidapan[0];
            txt_tv.setText(xulidapan[1]);

        }


        int ran_btg = ran_bt.nextInt(4) ;
        if (ran_btg == 0) {
            da1.setText(dapan);
            random_dapan(da2);
            random_dapan(da3);
            random_dapan(da4);
        } else  if (ran_btg == 1) {
            random_dapan(da1);
            da2.setText(dapan);
            random_dapan(da3);
            random_dapan(da4);
        } else  if (ran_btg == 2) {
            random_dapan(da1);
            random_dapan(da2);
            da3.setText(dapan);
            random_dapan(da4);
        } else {
            random_dapan(da1);
            random_dapan(da2);
            random_dapan(da3);
            da4.setText(dapan);
        }

        //het xu li
    }
    private void ck_game(Button button) {
        if (button.getText().toString().trim().toLowerCase().contains(dapan.toLowerCase().trim())) {

            if (DIEM > toida_game) {
                ms_thongbao(true);
                play_eff(R.raw.thang);

                  Toast.makeText(getApplicationContext(), getString(R.string.youwin), Toast.LENGTH_LONG).show();
                  tientrinhagme.setProgress(0);
                if (mn != null) {
                    mn.stop();
                    mn.release();
                    mn = null;
                }
            } else {
                play_eff(R.raw.dung);
                if (da_en == 1) {
                    t1.speak(button.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
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
                tientrinhagme.startAnimation(AnimationUtils.loadAnimation(game2.this, R.layout.eff_move_bt));
                next_game();
            }
        } else {

            if (DIEM <= 0) {
                DIEM =0;
                ms_thongbao(false);
                play_eff(R.raw.thua);
                if (mn != null) {
                    mn.stop();
                    mn.release();
                    mn = null;
                }
              //  ck_da.setText("KẾT THÚC");
            }else {
                DIEM -=1;
            }
            tientrinhagme.setProgress(DIEM);
            play_eff(R.raw.sai);
            tientrinhagme.startAnimation(AnimationUtils.loadAnimation(game2.this, R.layout.shake_3));
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
    public void onBackPressed() {
        if (mn != null) {
            mn.stop();
            mn.release();
            mn = null;
        }
        Intent intent= new Intent(getApplicationContext(), Game.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
        game2.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finish();
    }
    private void ms_thongbao(boolean debik) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.thongbao));

        if (debik == true) {
            builder.setMessage(getString(R.string.youwin));
            dichvugame dichvugame = new dichvugame(game2.this, 1);
            dichvugame.sendgame();
        } else {
            builder.setMessage(getString(R.string.youfail));
            dichvugame dichvugame = new dichvugame(game2.this, 0);
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
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                        onBackPressed();
                    }
                }
        );
    }
    private void play_eff(int as) {
       // MediaPlayer mp_wf = new MediaPlayer();
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

}
