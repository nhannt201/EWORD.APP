package vn.name.trungnhan.eword.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.name.trungnhan.eword.FILE_XULY;
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
public class Login extends AppCompatActivity {
    String login_has = "";
    String login_name = "";
    SharedPreferences lgrg;
    public static String log_reg = "logreg";
    private Button login_bt;
    private EditText mail;
    private EditText pass;
    private TextView redict;
    String UTF8 = "utf8";
    int BUFFER_SIZE = 8192;
    String dongbove = "";
    String myname = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //chuyenhuong reg
        redict = findViewById(R.id.txt_lg_2);
        redict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Reg.class);
                startActivity(intent);
                finish();
            }
        });
        //xulylogin
        mail = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login_bt = findViewById(R.id.login);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mail.getText().toString().trim().length() > 5) && (pass.getText().toString().length() > 0)) {
                    isMAIL(mail, pass);

                } else {
                    THONGBAOGAME thongbao = new THONGBAOGAME(Login.this,getString(R.string.thongbao),getString(R.string.kohople));
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
    public  void isMAIL(EditText editText, EditText pass) {
        String url = "https://<server>/eword/login.php?email=" + editText.getText().toString().trim() + "&pass=" + getMD5(pass.getText().toString());
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
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                         if  (myRespose.trim().contains("no")) {
                            THONGBAOGAME thongbao = new THONGBAOGAME(Login.this,getString(R.string.thongbao),getString(R.string.kotontai));
                            thongbao.getTBS();
                         } else if  (myRespose.trim().contains("pass_wr")) {
                             THONGBAOGAME thongbao = new THONGBAOGAME(Login.this,getString(R.string.thongbao),getString(R.string.log_fail));
                             thongbao.getTBS();
                         } else if (myRespose.trim().contains("yes")){
                             get_name(mail);
                             new ToastTB(Login.this,getString(R.string.log_ok));
                             //logreg

                         } else {
                             THONGBAOGAME thongbao = new THONGBAOGAME(Login.this,getString(R.string.thongbao),getString(R.string.loi_log));
                             thongbao.getTBS();
                         }


                        }
                    });
                }
            }
        });
    }

    //CK_ dong bo ve
    public  void dongbo_ne(EditText editText) {
        String url = "https://<server>/eword/get_db.php?email=" + editText.getText().toString().trim();
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
                    Login.this.runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            //    input_tv.setText(myRespose.trim());
                            dongbove = myRespose.trim();
                            File fileOrDirectory = new File(getExternalFilesDir(null).getAbsolutePath() +
                                    "/Folder_Word");
                         //   deleteFile(fileOrDirectory);
                            //   Toast.makeText(Caidat.this, "Sắp xong...", Toast.LENGTH_LONG).show();
                         //   trangthai_dongbo.setText(getString(R.string.sapxog));
                            xuli_db();
                            //

                        }
                    });
                }
            }
        });
    }
    //
    //Get name
    //CK_ dong bo ve
    public  void get_name(EditText editText) {
        String url = "https://<server>/eword/get_name.php?email=" + editText.getText().toString().trim();
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
                    Login.this.runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            //    input_tv.setText(myRespose.trim());
                        //    dongbove = myRespose.trim();
                          myname = myRespose.trim();
                            //
                            lgrg = getSharedPreferences(log_reg, 0);
                            login_has =  lgrg.getString("log",null);
                            login_name =  lgrg.getString("name_mail",null);
                            XULI_DATA xuli = new XULI_DATA(mail.getText().toString().trim()+"|" + myname);
                            SharedPreferences.Editor editit = lgrg.edit();
                            editit.putString("name_mail", xuli.AddAntiN());
                            editit.apply();
                            SharedPreferences.Editor editit_log = lgrg.edit();
                            editit_log.putString("log", "true");
                            editit_log.apply();
                            kiemco_dongbochua(mail);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    //het name
    //het lay
    //Bat dau xu li dong bo
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void xuli_db() {
        XULI_DATA xuli_data = new XULI_DATA(dongbove);
        if (xuli_data.isAnti() == true) {
            byte[] data = xuli_data.DelAnti();
            String text_sb = new String(data, StandardCharsets.UTF_8);
            String[] tach_name_file = text_sb.split("\\[");
            if (tach_name_file.length > 0) {
                String[] name_file = tach_name_file[0].split("\\=");
                String[] content_file = tach_name_file[1].split("\\#");

                Integer num_md = 0;
                if (name_file.length > 0) {
                    for (String make_file : name_file) {
                        if (make_file.trim().length() > 2) {

                            //Tao thu muc
                            try {
                                File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                                        "/Folder_Word/" + make_file.trim() + ".ewd");
                                file_fd.createNewFile();

                                //write the bytes in file
                                //   if (file_fd.exists()) {
                                //      if(k!=null && k.length>0){
//                                    Toast.makeText(Caidat.this, content_file[num_md], Toast.LENGTH_LONG).show();
                                //vi co thu muc trong dan den viec ko co nd nen phai lam cach nay
                                if (num_md <  content_file.length) {
                                    FILE_XULY file_xuly = new FILE_XULY(file_fd);
                                    file_xuly.writefile(content_file[num_md],this);
                                } else {
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_fd), UTF8), BUFFER_SIZE);
                                    bw.write("");
                                    bw.close();
                                }
                                // }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                                 new ToastTB(Login.this, getString(R.string.err_db));
                       //         trangthai_dongbo.setText(getString(R.string.err_db));
                            }
                            //Tao xong
                            num_md = num_md + 1;
                        }

                    }
                }


            }
            //   Toast.makeText(Caidat.this, "Đã đồng bộ xong!", Toast.LENGTH_LONG).show();
     //       trangthai_dongbo.setText(getString(R.string.ok_db));
       //     bt_saoluu.setEnabled(true);
        }


    }

    //CK_ dong bo ve
    public  void kiemco_dongbochua(final EditText editText) {
        String url = "https://<server>/eword/ck_bk.php?email=" + editText.getText().toString().trim();
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
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            File fileOrDirectory = new File(getExternalFilesDir(null).getAbsolutePath() +
                                    "/Folder_Word");
                            //    input_tv.setText(myRespose.trim());
                            if (myRespose.trim().contains("yes")) {
                             //   deleteFile(fileOrDirectory);
                                //  Toast.makeText(Caidat.this, "Bước cuối cùng...", Toast.LENGTH_LONG).show();
                              //  trangthai_dongbo.setText(getString(R.string.final_bc));
                               dongbo_ne(editText);
                            } else {
                                //    Toast.makeText(Caidat.this, "Bạn chưa có bản sao lưu nào để đồng bộ!", Toast.LENGTH_SHORT).show();
                             //   trangthai_dongbo.setText(getString(R.string.ck_sl));
                            }


                        }
                    });
                }
            }
        });
    }
}
