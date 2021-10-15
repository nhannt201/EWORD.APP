package vn.name.trungnhan.eword.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.name.trungnhan.eword.MainActivity;
import vn.name.trungnhan.eword.R;
import vn.name.trungnhan.eword.THONGBAOGAME;
import vn.name.trungnhan.eword.ToastTB;
import vn.name.trungnhan.eword.XULI_DATA;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class Reg extends AppCompatActivity {
    String login_has = "";
    String login_name = "";
    SharedPreferences lgrg;
    public static String log_reg = "logreg";
    private Button reg_bt;
    private EditText mail;
    private EditText name;
    private EditText pass;
    private TextView redict;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        //chuyenhuong reg
        redict = findViewById(R.id.txt_lg_2);
        redict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        //xulylogin
        mail = findViewById(R.id.username);
        name = findViewById(R.id.name_user);
        pass = findViewById(R.id.password);
        reg_bt = findViewById(R.id.reg);
        reg_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  ((mail.getText().toString().length() > 5) && (name.getText().toString().length() > 2) && (pass.getText().toString().length() > 0)) {
                    isMAIL(mail,name,pass);
                } else {
                    THONGBAOGAME thongbao = new THONGBAOGAME(Reg.this,getString(R.string.thongbao),getString(R.string.kohople));
                    thongbao.getTBS();
                }
            }
        });
    }

    public static String convertByteToHex1(byte[] data) {
        BigInteger number = new BigInteger(1, data);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return convertByteToHex1(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    //CK_ email ton tai
    public  void isMAIL(EditText editText,EditText name, EditText pass) {
        String url = "https://<server>/eword/reg.php?email=" + editText.getText().toString().trim()+ "&name=" + name.getText().toString().trim() + "&pass=" + getMD5(pass.getText().toString());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myRespose = response.body().string();
                    Reg.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if  (myRespose.trim().contains("yes")) {
                                THONGBAOGAME thongbao = new THONGBAOGAME(Reg.this,getString(R.string.thongbao),getString(R.string.datontai));
                                thongbao.getTBS();
                            } else if (myRespose.trim().contains("no")){
                                new ToastTB(Reg.this,getString(R.string.reg_ok));
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                THONGBAOGAME thongbao = new THONGBAOGAME(Reg.this,getString(R.string.thongbao),getString(R.string.loi_reg));
                                thongbao.getTBS();
                            }


                        }
                    });
                }


            }
        });
    }
}
