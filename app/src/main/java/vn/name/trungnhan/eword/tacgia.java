package vn.name.trungnhan.eword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static vn.name.trungnhan.eword.MainActivity.mp;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class tacgia extends AppCompatActivity {
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;
    private TextView TG_ver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tacgia);
        TG_ver = findViewById(R.id.tg_ver);
        String versionName = BuildConfig.VERSION_NAME;
        TG_ver.setText("EWORD v" +versionName+ "\n" + getString(R.string.learn_easy));
    }
    //start
    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
      //  Toast.makeText(tacgia.this, "Đang về trang chủ"  , Toast.LENGTH_SHORT).show();
        startActivity(intent);
        tacgia.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    //end
}
