package vn.name.trungnhan.eword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class NotificationActivity extends AppCompatActivity {
    public static String fd_name = "folder_tam";
    private SharedPreferences SP_fd;
    private TextView txt1;
    private TextView txt2;
    private Button bt1;
    private Button bt2;
    String thongbao_share;
    String[] cattua;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_v_m_n);
        txt1 = findViewById(R.id.txt_show_tv1);
        txt2 = findViewById(R.id.txt_show_tv2);
        SP_fd = getSharedPreferences(fd_name, 0);
        thongbao_share = SP_fd.getString("nd_tb", null);
        if (thongbao_share.contains("new")) {
            txt1.setText(getString(R.string.chuc1));
            txt2.setText(getString(R.string.chuc2));
        } else {
            cattua = thongbao_share.split(" : ");
            txt1.setText(cattua[0]);
            txt2.setText(cattua[1]);
        }
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(cattua[0], TextToSpeech.QUEUE_FLUSH, null);

            }
        });


        bt1 = findViewById(R.id.bt_sh1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                NotificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        bt2 = findViewById(R.id.bt_sh2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                NotificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
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

}
