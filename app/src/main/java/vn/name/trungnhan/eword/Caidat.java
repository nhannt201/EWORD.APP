package vn.name.trungnhan.eword;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.name.trungnhan.eword.login.Login;

import static vn.name.trungnhan.eword.MainActivity.mp;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class Caidat extends AppCompatActivity {
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;
    SharedPreferences lgrg;
    public static String log_reg = "logreg";
    String mail_tg = "";
    private ImageView sound_st;
    private Button saoluu_bt;
    private TextView txt_mail;
    String coamthanh = null;
    String mailof = null;
    public static final String UPLOAD_URL = "https://<server>/eword/backup.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Button bt_saoluu;
    String namefile = "";
    String data_file = "";
    String UTF8 = "utf8";
    int BUFFER_SIZE = 8192;
    String dongbove = "";
    private TextView trangthai_dongbo;
    private CheckBox ck_st1;
    private CheckBox ck_st2;
    Integer nhodasl = 0;
    private Button dangxuat;
    String colog= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caidat);
        dangxuat = findViewById(R.id.bt_dangxuat);
        txt_mail = findViewById(R.id.txt_email);
        saoluu_bt = findViewById(R.id.bt_saoluu);
        trangthai_dongbo = findViewById(R.id.txt_trangthai);
        bt_saoluu = findViewById(R.id.bt_dongbo);
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ck_st1 = findViewById(R.id.ck_st1);
        ck_st2 = findViewById(R.id.ck_st2);
        lgrg = getSharedPreferences(log_reg, 0);
         colog =  lgrg.getString("log",null);
        if (colog == null) { } else if (colog.contains("true")) {
//            bt_saoluu.setEnabled(true);
   //         saoluu_bt.setEnabled(true);
    //        dangxuat.setEnabled(true);
        } else {
            txt_mail.setText(getString(R.string.canlog));
            bt_saoluu.setEnabled(false);
            saoluu_bt.setEnabled(false);
          //  dangxuat.setEnabled(false);
            dangxuat.setText(getString(R.string.dangnhap));
        }

        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (colog.contains("true")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Caidat.this);
                builder.setTitle(getString(R.string.thongbao));
                builder.setMessage(getString(R.string.chacchan_out));

// Set up the buttons
                builder.setPositiveButton(getString(R.string.bt_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dongbo

                        lgrg = getSharedPreferences(log_reg, 0);
                        SP_fd = getSharedPreferences(fd_name, 0);
                        SharedPreferences.Editor editit_log = lgrg.edit();
                        editit_log.putString("log", "false");
                        editit_log.apply();
                        SharedPreferences.Editor editit = lgrg.edit();
                        editit.putString("name_mail", null);
                        editit.apply();
                        SharedPreferences.Editor editit_fd = SP_fd.edit();
                        editit_fd.putString("st_nc", "false");
                        editit_fd.apply();
                        File fileOrDirectory = new File(getExternalFilesDir(null).getAbsolutePath() +
                                "/Folder_Word");
                        deleteFile(fileOrDirectory);
                        new ToastTB(Caidat.this,getString(R.string.out_thanhcong));
                        Intent intent = new Intent(getApplicationContext(), ScreenIntro.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
                builder.setOnCancelListener(
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //When you touch outside of dialog bounds,
                                //the dialog gets canceled and this method executes.
                                dialog.cancel();
                            }
                        }
                );

            }  else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //het kt
        SP_fd = getSharedPreferences(fd_name, 0);
        String bat_nhacnho = SP_fd.getString("st_nc", null);
        if (bat_nhacnho.contains("true")) {
            ck_st2.setChecked(true);
        } else {
            ck_st2.setChecked(false);
        }
        ck_st2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP_fd = getSharedPreferences(fd_name, 0);
                String trangthai = SP_fd.getString("st_nc", null);
                if (ck_st2.isChecked()) {
                    SharedPreferences.Editor editit = SP_fd.edit();
                    editit.putString("st_nc", "true");
                    editit.commit();
                    ck_st2.setChecked(true);
                //    NotificationEventReceiver.setupAlarm(getApplicationContext());
                    AlarmHandler alarmHandler = new AlarmHandler(Caidat.this);
                    //Xoa bao thuc trc do
                    alarmHandler.cancelAlarmManager();
                    //Dat lai bao thuc
                    alarmHandler.setAlarmManager();
                    new ToastTB(Caidat.this,getString(R.string.will_nhac));
                } else {
                    SharedPreferences.Editor editit = SP_fd.edit();
                    editit.putString("st_nc", "false");
                    editit.commit();
                    ck_st2.setChecked(false);
                }
            }
        });
        String trangthai = SP_fd.getString("st_eff", null);
         if (trangthai.contains("true")) {
            ck_st1.setChecked(true);
        } else {
            ck_st1.setChecked(false);
        }
        ck_st1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP_fd = getSharedPreferences(fd_name, 0);
                String trangthai = SP_fd.getString("st_eff", null);
                if (ck_st1.isChecked()) {
                    SharedPreferences.Editor editit = SP_fd.edit();
                    editit.putString("st_eff", "true");
                    editit.commit();
                    ck_st1.setChecked(true);
                } else {
                    SharedPreferences.Editor editit = SP_fd.edit();
                    editit.putString("st_eff", "false");
                    editit.commit();
                    ck_st1.setChecked(false);
                }
            }
        });
        bt_saoluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Caidat.this);
                    builder.setTitle(getString(R.string.thongbao));
                    builder.setMessage(getString(R.string.content_thongbao1));

// Set up the buttons
                builder.setPositiveButton(getString(R.string.bt_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dongbo

                        if (checkInternetConnection() == true) {
                         //   Toast.makeText(Caidat.this, "Bắt đầu đồng bộ!", Toast.LENGTH_LONG).show();
                            trangthai_dongbo.setText(getString(R.string.tt_db));
                            bt_saoluu.setEnabled(false);

                            isMAIL();
                        } else {
                          //  Toast.makeText(Caidat.this, "Cần Internet để đồng bộ!", Toast.LENGTH_LONG).show();
                            trangthai_dongbo.setText(getString(R.string.tt_db2));

                        }
                    }
                });
                builder.show();
                builder.setOnCancelListener(
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                //When you touch outside of dialog bounds,
                                //the dialog gets canceled and this method executes.
                               dialog.cancel();
                            }
                        }
                );
            }
        });

        saoluu_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nhodasl == 0) {
                    nhodasl = 1;
                    //   Toast.makeText(Caidat.this, "Đang tiến hành sao lưu!", Toast.LENGTH_LONG).show();
                    trangthai_dongbo.setText(getString(R.string.tt_db3));
                    //Check bao nhieu fd
                    Integer demsothumuc = 0;
                    File folder = new File(getExternalFilesDir(null).getAbsolutePath() +
                            "/Folder_Word");
                    for (File f : folder.listFiles()) {
                        if (f.isFile())
                            namefile = namefile + f.getName().replace(".ewd", "") + "=";
                        demsothumuc = demsothumuc + 1;
                        //   listItems.add(f.getName().replace(".ewd",""));
                        // Do your stuff
                        //read file luon
                        File file_tv = new File(getExternalFilesDir(null).getAbsolutePath() +
                                "/Folder_Word/" + f.getName());
                        //     FileInputStream fin = null;
                        int ch;
                        StringBuffer sb = new StringBuffer();

                        try {
                            // create FileInputStream object
                            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file_tv), UTF8), BUFFER_SIZE);
                            while ((ch = fin.read()) != -1) {
                                sb.append((char) ch);
                            }
                            String sb_de;
                            XULI_DATA xuli_data = new XULI_DATA(sb.toString());
                            if (xuli_data.isAnti() == true) {
                                byte[] data = xuli_data.DelAnti();
                                String text_sb = new String(data, StandardCharsets.UTF_8);
                                sb_de = text_sb;

                            } else {
                                sb_de = sb.toString();
                            }
                            //String[] arrOfStr = sb.toString().split("\\|");
                            // for (String a : arrOfStr) {
                            //    total_r = total_r +1;
                            // }
                            data_file = data_file + sb_de + "#";

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //
                        //end ck
                        //het read file
                    }

                    if (checkInternetConnection()) {
                        ghi_file_bk();
                    } else {
                      //  Toast.makeText(Caidat.this, "Không thể sao lưu, sao lưu cần kết nối Internet!", Toast.LENGTH_LONG).show();
                        THONGBAOGAME thongbaogame = new THONGBAOGAME(Caidat.this,getString(R.string.thongbao), getString(R.string.no_inter_sl));
                        thongbaogame.getTBS();
                    }

                } else {
                    THONGBAOGAME thongbaogame = new THONGBAOGAME(Caidat.this,getString(R.string.thongbao), getString(R.string.saoluuroi));
                    thongbaogame.getTBS();
                    //Toast.makeText(Caidat.this, "Bạn vừa sao lưu rồi, cần thực hiện thay đổi để sao lưu lần nữa!", Toast.LENGTH_LONG).show();
                }
            }
        });

        sound_st = findViewById(R.id.sound_st);
        SP_fd = getSharedPreferences(fd_name, 0);
        coamthanh = SP_fd.getString("main_sound", null);
        if (coamthanh.equals("0")) {
            coamthanh = "1";
            //   sound_st.setImageResource(R.mipmap.has_sound);
        } else {
            sound_st.setImageResource(R.mipmap.mute_sound);
            coamthanh = "0";
        }

        //kiem tra lay email
   //     mailof = SP_fd.getString("st_mail", null);

        mail_tg =  lgrg.getString("name_mail",null);
        if (mail_tg == null) {

        } else if (mail_tg.length() > 1) {

            XULI_DATA xuli = new XULI_DATA(mail_tg);
            if (xuli.isAnti()) {
                String[] xuli_mail =  xuli.catchuoi_string();
                txt_mail.setText(getString(R.string.mail_sl) + " " + xuli_mail[0]);
                mailof = xuli_mail[0];
                isSV();

            }
        }  else {
            bt_saoluu.setVisibility(View.INVISIBLE);
            saoluu_bt.setVisibility(View.INVISIBLE);
        }

      /**  txt_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Caidat.this);
                builder.setTitle(getString(R.string.mail_sl));
                final EditText input = new EditText(Caidat.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // m_Text = input.getText().toString();
                        //Bat dau doi ten thu muc
                        if (input.getText().toString().trim().length() < 7) {
                         //   Toast.makeText(Caidat.this, "E-mail của bạn không hợp lệ!", Toast.LENGTH_LONG).show();
                            THONGBAOGAME thongbaogame = new THONGBAOGAME(Caidat.this,getString(R.string.thongbao), getString(R.string.wr_email));
                            thongbaogame.getTBS();
                        } else {
                            SP_fd = getSharedPreferences(fd_name, 0);
                            //   nameoffd = SP_fd.getString()
                            SharedPreferences.Editor editit = SP_fd.edit();
                            editit.putString("st_mail", input.getText().toString().trim());
                            editit.commit();
                            isSV();
                            txt_mail.setText(getString(R.string.mail_sl) + input.getText().toString().trim());
                            mailof = input.getText().toString().trim();
                            //  saoluu_bt.setEnabled(true);
                         //   Toast.makeText(Caidat.this, "Bạn cần khởi động lại để có thể sao lưu!", Toast.LENGTH_LONG).show();
                            THONGBAOGAME thongbaogame = new THONGBAOGAME(Caidat.this,getString(R.string.thongbao), getString(R.string.re_op_st));
                            thongbaogame.getTBS();
                        }

                        //
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                //het mo hop thoai

            }
        }); */
        sound_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coamthanh.equals("0")) {
                    sound_st.setImageResource(R.mipmap.has_sound);
                    coamthanh = "1";
                    SharedPreferences.Editor editit = SP_fd.edit();
                    editit.putString("main_sound", "0");
                    editit.commit();
                } else {
                    sound_st.setImageResource(R.mipmap.mute_sound);
                    coamthanh = "0";
                    //  try {
                    //   mp.stop();
                    if (mp != null) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                    //   } finally  {

                    //  }
                    SharedPreferences.Editor editit = SP_fd.edit();
                    editit.putString("main_sound", "1");
                    editit.commit();
                }
            }
        });
    }

    public void ghi_file_bk() {
        //Tao file trc
        try {
            File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                    "/Bachup/storage.ewd");
            file_fd.createNewFile();
            byte[] data1 = {};
            //write the bytes in file
            if (file_fd.exists()) {
                OutputStream fo = new FileOutputStream(file_fd);
                fo.write(data1);
                fo.close();
                //  Toast.makeText(Caidat.this,"Đã thêm thư mục '"+input.getText().toString().trim()+"'!",Toast.LENGTH_LONG).show();
                // url = upload.upload(file);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
          //  Toast.makeText(Caidat.this, "Không thể sao lưu" , Toast.LENGTH_LONG).show();
            trangthai_dongbo.setText(getString(R.string.fail_sl));

        }
        //Tao file thu muc
        File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Bachup/storage.ewd");
     //   String UTF8 = "utf8";
     //   int BUFFER_SIZE = 8192;

        if (file_fd.exists()) {
            String honhop = namefile + "[" + data_file;
            FILE_XULY file_xuly = new FILE_XULY(file_fd);
            file_xuly.writefile(honhop,this);
           /** byte[] data = honhop.getBytes(StandardCharsets.UTF_8);
            String base64 = anti_trans(0) + Base64.encodeToString(data, Base64.DEFAULT);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_fd), UTF8), BUFFER_SIZE);
            bw.write(base64);
            bw.close();*/
        }
        uploadMultipart();


    }

    //start
    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // Toast.makeText(Caidat.this, "Đang về trang chủ", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        Caidat.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    //end
    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //getting name for the image
        String name = mailof.trim();

        //getting the actual path of the file
        File folder_backup = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Bachup/storage.ewd");

        // String path = getPath(folder_backup);

        //Uploading code
        try {
            UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            UploadNotificationConfig config = new UploadNotificationConfig();
            config.getCompleted().autoClear = true;
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload((folder_backup.getPath()), "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    //  .addParameter("content", name)
                    .setNotificationConfig(config)
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {  //Add these lines to get upload status
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            // your code here
                        //    Toast.makeText(context, "Đang tải lên máy chủ", Toast.LENGTH_SHORT).show();
                            trangthai_dongbo.setText(getString(R.string.up_bk));
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                         //   Toast.makeText(context, "Sao lưu thất bại! ErrCode: " + serverResponse, Toast.LENGTH_SHORT).show();
                            trangthai_dongbo.setText(getString(R.string.fail_sl) + " ErrCode:" + serverResponse);
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            //Toast.makeText(Caidat.this, "Sao lưu hoàn tất!", Toast.LENGTH_LONG).show();
                            trangthai_dongbo.setText(getString(R.string.ok_sl));
                            //    Toast.makeText(Caidat.this, "Sao lưu hoàn tất!\nLần đăng nhập sau chỉ cần nhập e-mail và nhấn 'Đồng bộ'",Toast.LENGTH_LONG).show();
                            THONGBAOGAME thongbaogame = new THONGBAOGAME(Caidat.this, getString(R.string.thongbao), getString(R.string.ok_sl2));
                            thongbaogame.getTBS();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            // your code here
                        //    Toast.makeText(context, "Đã huỷ sao lưu!", Toast.LENGTH_SHORT).show();
                            trangthai_dongbo.setText(getString(R.string.cancel_sl));

                        }
                        //    Toast.makeText(Caidat.this, "Sao lưu hoàn tất!\nLần đăng nhập sau chỉ cần nhập e-mail và nhấn 'Đồng bộ'",Toast.LENGTH_LONG).show();
                    })
                    .startUpload(); //Starting the upload
         //   trangthai_dongbo.setText("Sao lưu hoàn tất!");

//https://github.com/gotev/android-upload-service/wiki/Monitoring-upload-status


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(this, getString(R.string.quyensl), Toast.LENGTH_LONG).show();

        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, getString(R.string.quyen1), Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, getString(R.string.quyen2), Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean checkInternetConnection() {

        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            //   Toast.makeText(this, "Không kết nối mạng một vài chức năng của App sẽ không hoạt động ổn định!" , Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            // Toast.makeText(this, "Mạng không được kết nối", Toast.LENGTH_LONG).show();
            return false;
        }

        // Toast.makeText(this, "Không có mạng!", Toast.LENGTH_LONG).show();
        return networkInfo.isAvailable();
        // Toast.makeText(this, "Thiết lập kết nối máy chủ!", Toast.LENGTH_LONG).show();
        // button1.setEnabled(true);
    }
    //    if (v == buttonUpload) {
    //      uploadMultipart();
    //  }



    //het enco
    public  void isSV() {
        String url = "https://<server>/eword/allow_sl.txt";
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
                    Caidat.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //    input_tv.setText(myRespose.trim());
                            if (myRespose.trim().contains("yes")) {
                                saoluu_bt.setEnabled(true);
                            } else {
                            //    Toast.makeText(Caidat.this, "Máy chủ tạm bảo trì, chức năng sao lưu bị khoá tạm thời!", Toast.LENGTH_SHORT).show();
                                trangthai_dongbo.setText(getString(R.string.baotri));
                                saoluu_bt.setEnabled(false);
                                bt_saoluu.setEnabled(false);
                            }


                        }
                    });
                }
            }
        });
    }
//
    //CK_ dong bo ve
public  void isMAIL() {
    String url = "https://<server>/eword/ck_bk.php?email=" + mailof;
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
                Caidat.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        File fileOrDirectory = new File(getExternalFilesDir(null).getAbsolutePath() +
                                "/Folder_Word");
                        //    input_tv.setText(myRespose.trim());
                        if (myRespose.trim().contains("yes")) {
                            deleteFile(fileOrDirectory);
                          //  Toast.makeText(Caidat.this, "Bước cuối cùng...", Toast.LENGTH_LONG).show();
                            trangthai_dongbo.setText(getString(R.string.final_bc));
                            dongbo_ne();
                        } else {
                        //    Toast.makeText(Caidat.this, "Bạn chưa có bản sao lưu nào để đồng bộ!", Toast.LENGTH_SHORT).show();
                            trangthai_dongbo.setText(getString(R.string.ck_sl));
                        }


                    }
                });
            }
        }
    });
}
    //
    //Lay dong bo
    //CK_ dong bo ve
    public  void dongbo_ne() {
        String url = "https://<server>/eword/get_db.php?email=" + mailof;
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
                    Caidat.this.runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            //    input_tv.setText(myRespose.trim());
                           dongbove = myRespose.trim();
                            //
                         //   Toast.makeText(Caidat.this, "Sắp xong...", Toast.LENGTH_LONG).show();
                            trangthai_dongbo.setText(getString(R.string.sapxog));
                            xuli_db();
                            //

                        }
                    });
                }
            }
        });
    }
    //
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
                            //    Toast.makeText(Caidat.this, "Có lỗi khi đồng bộ!", Toast.LENGTH_LONG).show();
                                trangthai_dongbo.setText(getString(R.string.err_db));
                            }
                            //Tao xong
                            num_md = num_md + 1;
                        }

                    }
                }


            }
         //   Toast.makeText(Caidat.this, "Đã đồng bộ xong!", Toast.LENGTH_LONG).show();
            trangthai_dongbo.setText(getString(R.string.ok_db));
            bt_saoluu.setEnabled(true);
        }
    }

    //het dong bo
    public void deleteFile(File f) {
        String[] flist = f.list();
        for (int i = 0; i < flist.length; i++) {
            System.out.println(" " + f.getAbsolutePath());
            File temp = new File(f.getAbsolutePath() + "/" + flist[i]);
            if (temp.isDirectory()) {
                deleteFile(temp);
                temp.delete();
            } else {
                temp.delete();
            }
        }
        //Toast.makeText(Caidat.this, "Đã loại bỏ tập tin cũ!", Toast.LENGTH_LONG).show();
        trangthai_dongbo.setText(getString(R.string.del_file));

    }
    //end
}
