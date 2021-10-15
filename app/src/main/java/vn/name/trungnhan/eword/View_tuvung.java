package vn.name.trungnhan.eword;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
public class View_tuvung extends AppCompatActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    private ListView mListView;
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;
    private TextView   view_demo;
    private Button bt_add_tv;
    private Button bt_del_tv;
    private TextView lb_fd;
    String nghiatv_tam;
    String de_save ="";
    String getname;
    Integer num_id;
    String selectedItem;
    Integer db_click = 0;
    String txt_nhotam;
    Integer sotv = 0;
    String nhotvtam = "";
    private ImageView back;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tv);
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lb_fd = findViewById(R.id.txt_label_fd);
        //lay tu bo nho tam
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SP_fd = getSharedPreferences(fd_name, 0);
         getname = SP_fd.getString("fd_name","");
        view_demo = findViewById(R.id.txt_chose_word);
      //  view_demo.setText(getname);
        //het
        if (mListView == null) {
            mListView = findViewById(R.id.lst_tv);
        }

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedItem = (String) parent.getItemAtPosition(position);
            num_id = position;
                                                 String[] arrOfStr = selectedItem.split(" : ", 2);
                                                 nghiatv_tam = arrOfStr[1];
                                               //  view_demo.setText(arrOfStr[0]);
                                                 if (arrOfStr[0].length() > 20) {
                                                     view_demo.setText(  arrOfStr[0].substring(0,20) + "...");
                                                 } else {
                                                     view_demo.setText(  arrOfStr[0]);
                                                 }
                                                 nhotvtam =  arrOfStr[0];
                                                 //check_double click
                                                 if (db_click.equals(0)) {
                                                     txt_nhotam = selectedItem;
                                                     db_click = 1;
                                                     new ToastTB(View_tuvung.this,getString(R.string.motv));
                                                 } else if (db_click.equals(1)) {
                                                     if (txt_nhotam.equals(selectedItem)) {
                                                         onItemDoubleClick();
                                                         db_click = 0;
                                                     } else {
                                                         db_click = 0;
                                                     }
                                                 }
                                                 //end check
            }
            public void onItemDoubleClick() {
                Edit_TV();
            }
        });

        //het lisst
    //Doc tu vung

       try {
           File file_tv = new File(getExternalFilesDir(null).getAbsolutePath() +
                   "/Folder_Word/" + getname + ".ewd");
           FILE_XULY file_xuly = new FILE_XULY(file_tv);
           String[] arrOfStr = file_xuly.catchuoi();
           //Xu li tu vung
           for (String a : arrOfStr) {
               if (a.trim().length() < 1) {
                   lb_fd.setText(getString(R.string.yourword));
                   sotv = 0;
               } else {
                   listItems.add(a.replace("~", " : ").trim());
                   de_save = de_save + a.trim() + "|";
               }
           }
           //het x li tu vung
           //Xu li bao nhieu tung vung
           if (arrOfStr.length  == 1) {
               if (arrOfStr[0].trim().length() > 0) {
                   lb_fd.setText(getString(R.string.yourword2) + " (" + arrOfStr.length + "): ");
                   sotv = arrOfStr.length;
               } else {
                   lb_fd.setText(getString(R.string.yourword));
                   sotv = 0;
               }
           } else {
               lb_fd.setText(getString(R.string.yourword2) + " (" + + arrOfStr.length + "): ");
               sotv = arrOfStr.length;
           }
           //het xu ly
           adapter.notifyDataSetChanged();
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
        //Het doc va xu li tu vung

        bt_add_tv = findViewById(R.id.bt_add_tv);
        bt_add_tv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //Them TV
                LayoutInflater factory = LayoutInflater.from(View_tuvung.this);
                final View textEntryView = factory.inflate(R.layout.custom_edit_add_tv, null);
                final EditText input_tv = textEntryView.findViewById(R.id.input_tv);
                final EditText input_ta = textEntryView.findViewById(R.id.input_ta);
                input_tv.setText("", TextView.BufferType.EDITABLE);
                input_tv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input_tv.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(300) {
                            public CharSequence filter(CharSequence src, int start,
                                                       int end, Spanned dst, int dstart, int dend) {
                                if (src.equals("")) {
                                    return src;
                                }
                                if (src.toString().matches("[a-zA-Z 0-9()àáảãạăằắẳẵặâầấẩẫậÀÁẢÃẠĂẰẮẲẴẶÂẦẤẨẪẬÒÒÓỎÕỌÔỒỐỔỖỘỜỚỞỠỢÈÉẺẼẸÊỀẾỂỄỆÙÚỦŨỤƯỪỨỬỮỰÌÍỈĨỊYỲÝỶỸỴĐđòóỏõọôồốổỗộơờớởỡợèéẻẽẹêềếểễệùúủũụưừứửữựìíỉĩịyỳýỷỹỵ ,]+")) {
                                    return src;
                                }
                                return "";
                            }
                        }
                });
                input_ta.setText("", TextView.BufferType.EDITABLE);
                input_ta.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input_ta.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });
                final AlertDialog.Builder alert = new AlertDialog.Builder(View_tuvung.this);
                alert.setIcon(R.mipmap.tuvung).setTitle(getString(R.string.new_word)).setView(textEntryView).setPositiveButton(getString(R.string.bt_add),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                //
                                //    listItems.add(input_ta.getText().toString());
                                //     adapter.notifyDataSetChanged();

                                if (((input_ta.getText().toString().trim().length() > 0)) && ((input_tv.getText().toString().trim().length() > 0))) {
                               if (listItems.contains(InChuCaiDau(input_ta) + " : " + InChuCaiDau(input_tv))) {
                                 //  new ToastTB(View_tuvung.this,"Từ này hình như đã có!" );
                                   THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao), getString(R.string.dacotu));
                                   thongbaogame.getTBS();
                                } else {
                                    listItems.add(InChuCaiDau(input_ta) + " : " + InChuCaiDau(input_tv));
                                    adapter.notifyDataSetChanged();
                                    ghi_file(getname,input_ta,input_tv);
                                   sotv +=1;
                                   set_num_tv();
                                //    new ToastTB(View_tuvung.this, "Đã thêm từ mới!");
                                   THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this,  getString(R.string.thongbao), getString(R.string.dathemtv));
                                   thongbaogame.getTBS();
                                 }
                                } else {
                                    //Xu li auto dich
                                    if (((input_ta.getText().toString().trim().length() > 0)) && (input_tv.getText().toString().trim().length() < 1)) {
                                     //   dialog.dismiss();
                                        if (checkInternetConnection() == true) {
                                            new ToastTB(View_tuvung.this,getString(R.string.dichtudong));
                                            DichTV(input_ta, input_tv);
                                        } else {
                                         //   new ToastTB(View_tuvung.this,"Dịch tự đông cần có kết nối Internet!");
                                            THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao),getString(R.string.canin_dich));
                                            thongbaogame.getTBS();
                                        }
                                    } else {
                                      //  new ToastTB(View_tuvung.this,"Không được bỏ trống từ Tiếng Anh!");
                                        THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao),getString(R.string.no_empt_en));
                                        thongbaogame.getTBS();

                                    }
                                    }

                                /* User clicked OK so do some stuff */
                            }
                        }).setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                               dialog.cancel();
                            }
                        });
                alert.show();
                //Het Them TV
            }
        });
    //    bt_edit_tv = findViewById(R.id.bt_edit_tv);
      //  bt_edit_tv.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {
         //       Edit_TV();
         //   }
      //  });
        bt_del_tv = findViewById(R.id.bt_del_tv);
        bt_del_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nhotvtam.trim().equals("")) {
                  //  new ToastTB(View_tuvung.this, "Chưa chọn từ để xoá!");
                    THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao), getString(R.string.cct));
                thongbaogame.getTBS();
                } else {
                    listItems.remove(nhotvtam.trim() + " : " + nghiatv_tam.trim());
                    de_save = de_save.replace(nhotvtam.trim() + "~" + nghiatv_tam.trim() + "|", "");
                    remove_line_file(getname);
                    adapter.notifyDataSetChanged();
                   // new ToastTB(View_tuvung.this,"Đã xoá từ '" + nhotvtam + "'" );
                    THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao),  getString(R.string.daxoatu)+" '" + nhotvtam + "'");
                    thongbaogame.getTBS();
                    sotv -=1;
                    set_num_tv();
                    view_demo.setText("");
                    nhotvtam = "";
                }
            }
        });
    }

    protected ListView getListView() {
        if (mListView == null) {
            mListView = findViewById(R.id.lst_fd);
        }
        return mListView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListAdapter getListAdapter() {
        ListAdapter adapter = getListView().getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        } else {
            return adapter;
        }
    }
    //start
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(getApplicationContext(), Tuvung.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
      //  Toast.makeText(View_tuvung.this, "Đang về 'THƯ MỤC TỪ VỰNG'"  , Toast.LENGTH_SHORT).show();
        startActivity(intent);
        View_tuvung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    //end
    //private boolean isEmpty(EditText etText) {
  //      return etText.getText().toString().trim().length() <= 0;
   // }
    //bien tap file


    public void ghi_file(String name_fd, EditText ip1, EditText ip2) {
        //Tao file thu muc
        File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word/" + name_fd + ".ewd");
        de_save = de_save + ip1.getText().toString() + "~" + ip2.getText().toString() + "|";
        FILE_XULY file_xuly = new FILE_XULY(file_fd);
        file_xuly.writefile(de_save,this);
    }
    public void up_edit_file(String name_fd, EditText ip1, EditText ip2) {
        File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word/" + name_fd + ".ewd");
        FILE_XULY file_xuly = new FILE_XULY(file_fd);
        file_xuly.writefile(de_save,this);
    }
    public void remove_line_file(String name_fd) {
        //Tao file thu muc
        File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word/" + name_fd + ".ewd");
            FILE_XULY file_xuly = new FILE_XULY(file_fd);
            file_xuly.writefile(de_save,this);
        //het tao
    }
        //het bien tap file
    //Edit TV
    public void Edit_TV() {

        //Edit TV
        if (nhotvtam.trim().equals("")) {
       //     new ToastTB(View_tuvung.this, "Chưa chọn từ để sửa!");
            THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this,getString(R.string.thongbao), getString(R.string.ccxoa));
        thongbaogame.getTBS();
        } else {
            LayoutInflater factory = LayoutInflater.from(View_tuvung.this);
            final View textEntryView = factory.inflate(R.layout.custom_edit_add_tv, null);
            final EditText input_tv = textEntryView.findViewById(R.id.input_tv);
            final EditText input_ta = textEntryView.findViewById(R.id.input_ta);
            input_tv.setText(nghiatv_tam, TextView.BufferType.EDITABLE);
            input_tv.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            input_tv.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(300) {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {
                            if (src.equals("")) {
                                return src;
                            }
                            if (src.toString().matches("[a-zA-Z 0-9()àáảãạăằắẳẵặâầấẩẫậÀÁẢÃẠĂẰẮẲẴẶÂẦẤẨẪẬÒÒÓỎÕỌÔỒỐỔỖỘỜỚỞỠỢÈÉẺẼẸÊỀẾỂỄỆÙÚỦŨỤƯỪỨỬỮỰÌÍỈĨỊYỲÝỶỸỴĐđòóỏõọôồốổỗộơờớởỡợèéẻẽẹêềếểễệùúủũụưừứửữựìíỉĩịyỳýỷỹỵ ,]+")) {
                                return src;
                            }
                            return "";
                        }
                    }
            });
            input_ta.setText(nhotvtam, TextView.BufferType.EDITABLE);
            input_ta.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            input_ta.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });

            final AlertDialog.Builder alert = new AlertDialog.Builder(View_tuvung.this);
            alert.setIcon(R.mipmap.tuvung).setTitle(getString(R.string.edit_tv)).setView(textEntryView).setPositiveButton(getString(R.string.update),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            if (((input_ta.getText().toString().trim().length() > 0)) && ((input_tv.getText().toString().trim().length() > 0))) {
                                if (listItems.contains(InChuCaiDau(input_ta) + " : " + InChuCaiDau(input_tv))) {
                                    THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this,getString(R.string.thongbao), getString(R.string.tugiong));
                                    thongbaogame.getTBS();
                                //    new ToastTB(View_tuvung.this, "Từ này hình như giống từ nào đó!");
                                } else {
                                    listItems.set(num_id, InChuCaiDau(input_ta) + " : " + InChuCaiDau(input_tv));
                                    de_save = de_save.replace(InChuCaiDauTV(nhotvtam) + "~" + nghiatv_tam.trim(), InChuCaiDau(input_ta) + "~" + InChuCaiDau(input_tv));
                                    adapter.notifyDataSetChanged();
                                    up_edit_file(getname, (input_ta), input_tv);
                                    view_demo.setText("");
                                    nhotvtam = "";
                                   // new ToastTB(View_tuvung.this, "Đã chỉnh sửa!");
                                    THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this,getString(R.string.thongbao), getString(R.string.dasuatv));
                                    thongbaogame.getTBS();
                                }
                            } else {
                              //  new ToastTB(View_tuvung.this, "Không được bỏ trống hoặc quá ngắn!");
                                THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this,getString(R.string.thongbao), getString(R.string.kobotrong));
                                thongbaogame.getTBS();
                            }

                        }
                    }).setNegativeButton(getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.cancel();
                        }
                    });
            alert.show();
            //Het Edit TV
        }
    }
    //Het edit
    //
    private String InChuCaiDau(EditText txt) {
        txt.setText( txt.getText().toString().trim().substring(0, 1).toUpperCase() + txt.getText().toString().trim().substring(1));
        return txt.getText().toString();
    }
    private String InChuCaiDauTV(String txt) {
        txt = ( txt.substring(0, 1).toUpperCase() + txt.trim().substring(1));
        return txt;
    }
    //
    public static boolean exists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //
    public void DichTV(final EditText input_ta, final EditText input_tv) {
        String url = "https://<server>/api_eword.php?text="+input_ta.getText().toString().trim();
     //   if (exists(url) == true) {
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
                    View_tuvung    .this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String deckne = myRespose.trim();
                          //  if (deckne.length() < 2) {
                           //     input_tv.setText("Trống");
                           // } else  {
                                input_tv.setText(deckne);
                           // }
                            if (listItems.contains(InChuCaiDau(input_ta) + " : " + InChuCaiDau(input_tv))) {
                                new ToastTB(View_tuvung.this, getString(R.string.tu) + " '" +input_ta.getText().toString()+"' " + getString(R.string.hinhnhudaco));
                            } else if (deckne.length() > 0) {
                              if (InChuCaiDau(input_ta).contains(InChuCaiDau(input_tv))) {
                             //   new ToastTB(View_tuvung.this, "Dịch tự động thất bại!\nKhông tìm thấy nghĩa cho: '" + input_ta.getText().toString().trim() + "'");
                                THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao),getString(R.string.fail_dich)+ " '" + input_ta.getText().toString().trim() + "'");
                            thongbaogame.getTBS();
                              } else {
                                  listItems.add(InChuCaiDau(input_ta) + " : " + InChuCaiDau(input_tv));
                                  adapter.notifyDataSetChanged();
                                  ghi_file(getname,input_ta,input_tv);
                                  new ToastTB(View_tuvung.this,getString(R.string.won_dih) + "\n" + input_ta.getText().toString() + " : " + input_tv.getText().toString());
                                sotv +=1;
                                set_num_tv();
                              }
                            } else {
                              //  new ToastTB(View_tuvung.this, "Dịch tự động thất bại!\nKhông tìm thấy nghĩa cho: '" + input_ta.getText().toString().trim() + "'");
                                THONGBAOGAME thongbaogame = new THONGBAOGAME(View_tuvung.this, getString(R.string.thongbao),getString(R.string.fail_dich) + " '" + input_ta.getText().toString().trim() + "'");
                                thongbaogame.getTBS();
                            }

                        }
                    });
                }
            }
        });
   // } else {
         //   Toast.makeText(View_tuvung.this, "Dịch thất bại, máy chủ gặp sự cố!", Toast.LENGTH_SHORT).show();
     //   }
    }
    //

//check IE
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
    //end ck
    private void  set_num_tv() {
        lb_fd.setText(getString(R.string.yourword2) + " (" + sotv + "): ");
    }
    //
}
