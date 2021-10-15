package vn.name.trungnhan.eword;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class BXH extends AppCompatActivity {
    List<TV_ADD> list = new ArrayList<TV_ADD>();
    String tvtv = "";
     ListView listView;
     private LinearLayout linearLayout3;
     private TextView txt_wait_ban;
     String nhoten_file = "";
    Integer ghinho_dack = 0;
    private TextView txt_rank;
    String login_name = "";
    SharedPreferences lgrg;
    String m_mail;
    String m_name;
    Integer ok_r = 0 ;
    String colog= "";
    public static String log_reg = "logreg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bxh);


        txt_rank = findViewById(R.id.txt_yourank);
        listView = findViewById(R.id.lst_item_TV);
        Toast.makeText(BXH.this, getString(R.string.wait_bxh), Toast.LENGTH_LONG).show();
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout3.setVisibility(View.INVISIBLE);
        txt_wait_ban = findViewById(R.id.txt_tlwait);
      //  bt_wait = findViewById(R.id.bt_return);
        lgrg = getSharedPreferences(log_reg, 0);
       // login_has =  lgrg.getString("log",null);
        login_name =  lgrg.getString("name_mail",null);
        colog =  lgrg.getString("log",null);
        if (login_name == null) { } else if (colog.contains("true")){
            XULI_DATA xuli = new XULI_DATA(login_name);
            if (xuli.isAnti()) {
                String[] xuli_name =  xuli.catchuoi_string();
                m_mail = xuli_name[0];
                m_name = xuli_name[1];
            //    txt_rank.setText(xuli_name[1]);

            }
        }
       GETTVV();
        Hide_lib_tv();
//        List<TV_ADD> image_details = getListData();

        if (colog.contains("true")) {
            THONGBAOGAME thongbao = new THONGBAOGAME(this, getString(R.string.thongbao), getString(R.string.cach_co_rank));
            thongbao.getTBS();
        }
        // Khi người dùng click vào các ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                TV_ADD Mota = (TV_ADD) o;
                nhoten_file = Mota.toString();
              //  Toast.makeText(THUVIEN.this, "Selected :" + " " + Mota, Toast.LENGTH_LONG).show();
                //TaiveTV(Mota.toString());

            }
        });
    }

    public  List<TV_ADD> getListData() {
        String[] get_lib = tvtv.split("\\|");
        if (tvtv.contains("no")) {
            txt_wait_ban.setText(getString(R.string.new_week));
            ok_r = 1;
        } else {
        if (get_lib.length == 0) {
            txt_wait_ban.setText(getString(R.string.baotri_tv));
        } else {
            int num_rank = 1;
        for (String name_lb : get_lib) {
            String[] mota_lb = name_lb.split("\\~");
            if (name_lb.contains(get_lib[0])) {
                list.add(new TV_ADD(mota_lb[0] , "rank1", mota_lb[1] + " " + getString(R.string.diem)));
            } else if  (name_lb.contains(get_lib[1])){
                list.add(new TV_ADD(mota_lb[0] , "rank2" , mota_lb[1]+ " " + getString(R.string.diem)));
            } else if  (name_lb.contains(get_lib[2])){
                list.add(new TV_ADD(mota_lb[0], "rank3", mota_lb[1]+ " " + getString(R.string.diem)));
            } else {
                list.add(new TV_ADD(mota_lb[0] , "norank", mota_lb[1]+ " " + getString(R.string.diem) + " - " + getString(R.string.hang) + " " + num_rank ));
            }
        /**    if (mota_lb[2].contains(m_mail)) {
                txt_rank.setText(m_name + " - " + mota_lb[1] + " " + getString(R.string.diem) + " - " + getString(R.string.hang) + " " + num_rank );
            } else {
                get_my_rank();
            }*/
          //  Toast.makeText(THUVIEN.this, name_lb, Toast.LENGTH_SHORT).show();
            num_rank +=1;
        }
            if (colog.contains("true")) {
                get_my_rank();
            } else {
                txt_rank.setText(getString(R.string.bxh_can));
            }
            linearLayout3.setVisibility(View.VISIBLE);
            txt_wait_ban.setVisibility(View.INVISIBLE);
           // progressBar.setVisibility(View.INVISIBLE);
        }
      //  Toast.makeText(THUVIEN.this, "Đã nạp thư viện", Toast.LENGTH_LONG).show();
        }
                            //
        return list;
    }
                    public void GETTVV() {
                        String url = "https://<server>/eword/bxh.php" ;
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
                        BXH.this.runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    if (myRespose.trim().length() > 0) {
                        tvtv = myRespose.trim();
                        listView.setAdapter(new CustomListAdapter(BXH.this, getListData()));

                    } else {
                        txt_wait_ban.setText(getString(R.string.baotri_tv));
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
        BXH.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



    //het enco


    //
    //het lay

    private void Hide_lib_tv(){

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (txt_wait_ban.getVisibility() == View.VISIBLE) {
                    if (ghinho_dack == 0) {
                        ghinho_dack =1;
                        list.clear();
                       new ToastTB(BXH.this, getString(R.string.wait_bxh));
                        GETTVV();

                        Hide_lib_tv();
                    }  else {
                        if ( ok_r == 1) { } else {
                            new ToastTB(BXH.this, getString(R.string.wait_not_res));
                            onBackPressed();
                        }
                    }
                }

            }
        }, 15000);
    }

    //CK_ email ton tai
    public  void get_my_rank() {
        String url = "https://<server>/eword/get_bxh.php?email=" + m_mail ;
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
                    BXH.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if  (myRespose.trim().contains("no")) {
                                txt_rank.setText(m_name + " - 0" + " " + getString(R.string.diem));// + " - "  + getString(R.string.hang) + " " + getString(R.string.hang_koco));
                            } else {
                                txt_rank.setText(m_name + " - " + myRespose.trim() + " " + getString(R.string.diem) );
                            }


                        }
                    });
                }
            }
        });
    }

    //
}

