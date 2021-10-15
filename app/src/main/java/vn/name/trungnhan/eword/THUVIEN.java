package vn.name.trungnhan.eword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import static vn.name.trungnhan.eword.MainActivity.mp;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */

public class THUVIEN extends AppCompatActivity {
    List<TV_ADD> list = new ArrayList<TV_ADD>();
    String tvtv = "";
     ListView listView;
     private LinearLayout linearLayout3;
     private TextView txt_wait_ban;
     String nhoten_file = "";
  //   private Button bt_wait;
  String UTF8 = "utf8";
    int BUFFER_SIZE = 8192;
    Integer ghinho_dack = 0;
    String luudata = "";
    boolean taixong = false;
    Integer ghinho_dw = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuvien);
        listView = findViewById(R.id.lst_item_TV);
        Toast.makeText(THUVIEN.this, getString(R.string.tv_wait_for), Toast.LENGTH_LONG).show();
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout3.setVisibility(View.INVISIBLE);
        txt_wait_ban = findViewById(R.id.txt_tlwait);
      //  bt_wait = findViewById(R.id.bt_return);


        GETTVV();
        Hide_lib_tv();
//        List<TV_ADD> image_details = getListData();

        // Khi người dùng click vào các ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                TV_ADD Mota = (TV_ADD) o;
                nhoten_file = Mota.toString();
              //  Toast.makeText(THUVIEN.this, "Selected :" + " " + Mota, Toast.LENGTH_LONG).show();
                TaiveTV(Mota.toString());

            }
        });
    }

    public  List<TV_ADD> getListData() {
        String[] get_lib = tvtv.split("\\|");
        if (get_lib.length == 0) {
            txt_wait_ban.setText(getString(R.string.baotri_tv));
        } else {
        for (String name_lb : get_lib) {
            String[] mota_lb = name_lb.split("\\~");
            list.add(new TV_ADD(mota_lb[0],"tuvung",mota_lb[1]));
          //  Toast.makeText(THUVIEN.this, name_lb, Toast.LENGTH_SHORT).show();

        }
            linearLayout3.setVisibility(View.VISIBLE);
            txt_wait_ban.setVisibility(View.INVISIBLE);
           // progressBar.setVisibility(View.INVISIBLE);
        }
      //  Toast.makeText(THUVIEN.this, "Đã nạp thư viện", Toast.LENGTH_LONG).show();

                            //
        return list;
    }
                    public void GETTVV() {
                        String url = "https://<server>/eword/get_lib.php" ;
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
                        THUVIEN.this.runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    if (myRespose.trim().length() > 0) {
                        tvtv = myRespose.trim();
                        listView.setAdapter(new CustomListAdapter(THUVIEN.this, getListData()));

                    } else {
                        txt_wait_ban.setText(getString(R.string.baotri_tv));
                       // progressBar.setVisibility(View.INVISIBLE);
                      //  bt_wait.setVisibility(View.VISIBLE);
                    }
                }
                        });
                        } else {
                         //   txt_wait_ban.setText("Máy chủ đang bảo trì hoặc đang gặp sự cố!\nKhông thể tải thư viện lúc này!\nNhấn BACK trên điện thoại để quay lại!");
                         //   Toast.makeText(THUVIEN.this, "Máy chủ đang bảo trì hoặc đang gặp sự cố!\nKhông thể tải thư viện lúc này!\nNhấn BACK trên điện thoại để quay lại!", Toast.LENGTH_LONG).show();
                            onBackPressed();

                        }
                }
                        });
                    }

    @Override
    public void onBackPressed() {
       // if (mp != null) {
       //     mp.stop();
       //     mp.release();
       //     mp = null;
       // }
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
        THUVIEN.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
        private void TaiveTV(String string){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.tv_on));
            builder.setMessage(getString(R.string.dw_1) + string + getString(R.string.dw_2));
            builder.setPositiveButton(getString(R.string.dw), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        taive_ne();
                }
            });
            builder.show();
        }


    //het enco

    //CK_ dong bo ve
    public  void taive_ne() {
        Toast.makeText(THUVIEN.this, getString(R.string.dangtai), Toast.LENGTH_LONG).show();
        CK_taixong();
        String url = "https://<server>/eword/get_db_tv.php?name=" + nhoten_file.trim();
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
                    THUVIEN.this.runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            //    input_tv.setText(myRespose.trim());
                            luudata = myRespose.trim();
                            //
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
                            //Tao thu muc
                            try {
                                File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                                        "/Folder_Word/" + nhoten_file.trim() + ".ewd");
                                file_fd.createNewFile();
                                //write the bytes in file
                                if (file_fd.exists()) {
                                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_fd), UTF8), BUFFER_SIZE);
                                        XULI_DATA xuli_data  = new XULI_DATA(luudata);
                                        bw.write(xuli_data.AddAnti());
                                        bw.close();
                                    ThongBaoXONG(getString(R.string.datai)+nhoten_file+"'!");
                                  //  Toast.makeText(THUVIEN.this, "Đã tải xuống '"+nhoten_file+"'!", Toast.LENGTH_LONG).show();
                                taixong = true;
                                }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                             //   Toast.makeText(THUVIEN.this, "Có lỗi khi tải xuống!", Toast.LENGTH_LONG).show();
                                ThongBaoXONG(getString(R.string.err_dw));

                            }
                            //Tao xong


    }

    //het dong bo
    private void Hide_lib_tv(){

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (txt_wait_ban.getVisibility() == View.VISIBLE) {
                    if (ghinho_dack == 0) {
                        ghinho_dack =1;
                        list.clear();
                       new ToastTB(THUVIEN.this, getString(R.string.re_dw));
                        GETTVV();

                        Hide_lib_tv();
                    }  else {
                       new ToastTB(THUVIEN.this, getString(R.string.wait_not_res));
                        onBackPressed();
                    }
                }

            }
        }, 20000);
    }
    //Ck tai xong
    private void CK_taixong(){

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (taixong == false) {
                    if ( ghinho_dw == 0) {
                        ghinho_dw =1 ;
                        new ToastTB(THUVIEN.this, getString(R.string.re_dw2));
                        taive_ne();
                        CK_taixong();
                    } else {
                        new ToastTB(THUVIEN.this, getString(R.string.fail_dw));
                    }
                }
            }
        }, 6000);
    }
    private void ThongBaoXONG(String string) {
        THONGBAOGAME thongbaogame = new THONGBAOGAME(this, getString(R.string.trinhtai), string);
        thongbaogame.getTBS();
    }
    //
}

