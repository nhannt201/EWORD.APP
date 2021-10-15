package vn.name.trungnhan.eword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class Tuvung extends AppCompatActivity {

   // private String m_Text = "";

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
 //   int clickCounter=0;
    private ListView mListView;
    public static String fd_name = "folder_tam";
    SharedPreferences SP_fd;

   // String nameoffd;
    private Button bt_xoa;
    private Button bt_edit;
    private TextView txt_sts;
    private Button bt_add;
    private ImageView back;
    Integer num_id;
    Integer db_click = 0;
    String txt_nhotam;
    private TextView lb_fd;
    Integer so_tm = 0;
    String tenthumyuc = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuvung);
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txt_sts = findViewById(R.id.txt_chose_fd);
        lb_fd = findViewById(R.id.txt_label_fd);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //add demo
        if (mListView == null) {
            mListView = findViewById(R.id.lst_fd);
        }

        adapter=new ArrayAdapter<String>(Tuvung.this, android.R.layout.simple_list_item_1,listItems);
        setListAdapter(adapter);
        //add lisst
     //   listItems.add("DEMO");
     //   listItems.add("DEMO2");
      //  adapter.notifyDataSetChanged();

        File folder = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word");        for (File f : folder.listFiles()) {
            if (f.isFile())
                //  name = f.getName();
                listItems.add(f.getName().replace(".ewd", ""));
                // Do your stuff
                so_tm += 1;
        }
        if (so_tm == 0) {
            THONGBAOGAME thongbaogame = new THONGBAOGAME(this,getString(R.string.thongbao),getString(R.string.cont_ms2));
       thongbaogame.getTBS();
        }
        lb_fd.setText(getString(R.string.tmtv)+" (" + so_tm + "):");
        adapter.notifyDataSetChanged();
        // Populate ListView with items from ArrayAdapter
        mListView.setAdapter(adapter);

        // Set an item click listener for ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                num_id =  position;

                // Display the selected item text on TextView

                if (selectedItem.length() > 14) {
                    txt_sts.setText(  selectedItem.substring(0,14) + "..");
                } else {
                    txt_sts.setText(  selectedItem);
                }
                tenthumyuc = selectedItem;
                SP_fd = getSharedPreferences(fd_name, 0 );
             //   nameoffd = SP_fd.getString()
                SharedPreferences.Editor editit = SP_fd.edit();
                editit.putString("fd_name", selectedItem);
                editit.commit();
                if (db_click.equals(0)) {
                    txt_nhotam = selectedItem;
                    db_click = 1;
                    new ToastTB(Tuvung.this,getString(R.string.lanua) + tenthumyuc);
                } else if (db_click.equals(1)) {
                    if (txt_nhotam.equals(selectedItem)) {
                        onItemDoubleClick();
                        db_click = 0;
                    } else {
                        db_click = 0;
                    }
                }
            }
            public void onItemDoubleClick() {
                OpenTV();
            }
        });
        //bt open
     //   bt_open = findViewById(R.id.bt_open_fd);
      //  bt_open.setOnClickListener(new View.OnClickListener() {
       //     @Override
      //      public void onClick(View v) {
        //        if(listItems.contains(txt_sts.getText().toString().trim())) {
         //          OpenTV();
          //      } else {
          //          Toast.makeText(Tuvung.this, "Chưa chọn thư mục!", Toast.LENGTH_SHORT).show();
           //     }
        //    }
      //  });
        //
        //txt_st_fd

        bt_edit = findViewById(R.id.bt_edit_fd);
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit list
                if(listItems.contains(tenthumyuc.trim())){
                   // String pos_edit = (Long) txt_sts.getText().toString();
                 //   adapter.insert("Tao lao", num_id);
                    //mo hop thoai de edit
                    AlertDialog.Builder builder = new AlertDialog.Builder(Tuvung.this);
                    builder.setTitle(getString(R.string.name_fd));

// Set up the input
                    final EditText input = new EditText(Tuvung.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                   // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_CLASS_NUMBER);
                    input.setText(tenthumyuc);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    input.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(50) {
                                public CharSequence filter(CharSequence src, int start,
                                                           int end, Spanned dst, int dstart, int dend) {
                                    if (src.equals("")) {
                                        return src;
                                    }
                                    if (src.toString().matches("[a-zA-Z 0-9 _-]+")) {
                                        return src;
                                    }
                                    return "";
                                }
                            }
                    });
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton(getString(R.string.doiten), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // m_Text = input.getText().toString();
                            //Bat dau doi ten thu muc
                            if(listItems.contains(tenthumyuc)){
                                //Kiem tra lan 2 neu ten thu muc thay doi giong nhau
                                if (tenthumyuc.trim().equals(input.getText().toString().trim())) {
                                 //   new ToastTB(Tuvung.this,"Tên thư mục không thay đổi!");
                                } else {
                                    listItems.set(num_id, input.getText().toString().trim());
                                    adapter.notifyDataSetChanged();
                                    //doi ten file
                                    File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                                            "/Folder_Word/" + tenthumyuc.trim() + ".ewd");
                                    File to        = new File(getExternalFilesDir(null).getAbsolutePath() +
                                            "/Folder_Word/" , input.getText().toString().trim() + ".ewd");
                                    file_fd.renameTo(to);
                                    //doi ten
//                                    new ToastTB(Tuvung.this, "Đã đổi tên '" + txt_sts.getText().toString().trim() + "' sang '" + input.getText().toString() + "'");
                                    THONGBAOGAME thongbaogame = new THONGBAOGAME(Tuvung.this,getString(R.string.thongbao),getString(R.string.dadoiten) + " '" + tenthumyuc.trim() + "' " +getString(R.string.sang)+" '" + input.getText().toString() + "'");
                                    thongbaogame.getTBS();
                                    txt_sts.setText("");
                                    tenthumyuc = "";
                                }

                            } else {
                                new ToastTB(Tuvung.this, getString(R.string.no_fd_Ed));
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

                } else {
                  // new ToastTB(Tuvung.this,"Không có thư mục được chọn để Sửa!!");
                   THONGBAOGAME thongbaogame = new THONGBAOGAME(Tuvung.this, getString(R.string.thongbao),getString(R.string.no_chsoe_fd));
                   thongbaogame.getTBS();
                }
            }
        });
        bt_xoa = findViewById(R.id.bt_del_fd);
        bt_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listItems.contains(tenthumyuc.trim())){
                    listItems.remove(tenthumyuc.trim());
                    adapter.notifyDataSetChanged();

                    File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                            "/Folder_Word/" + tenthumyuc.trim() + ".ewd");
                    file_fd.delete();
                    THONGBAOGAME thongbaogame = new THONGBAOGAME(Tuvung.this,getString(R.string.thongbao), getString(R.string.daxoatm) + " '" + tenthumyuc + "'.");
                //  new ToastTB(Tuvung.this,"Đã xoá thư mục được chọn!!");
                    thongbaogame.getTBS();
                    txt_sts.setText("");
                    tenthumyuc = "";
                    so_tm -=1;
                    set_num_tm();
                } else {
                    new ToastTB(Tuvung.this,getString(R.string.no_chsoe_fd));

                }
            }
        });
        //
        bt_add = findViewById(R.id.bt_add);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click ne
                AlertDialog.Builder builder = new AlertDialog.Builder(Tuvung.this);
                builder.setTitle(getString(R.string.name_fd));

// Set up the input
                final EditText input = new EditText(Tuvung.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            //    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
             //   input.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                input.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(50) {
                            public CharSequence filter(CharSequence src, int start,
                                                       int end, Spanned dst, int dstart, int dend) {
                                if (src.equals("")) {
                                    return src;
                                }
                                if (src.toString().matches("[a-zA-Z 0-9 _-]+")) {
                                    return src;
                                }
                                return "";
                            }
                        }
                });
//                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });

                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton(getString(R.string.bt_add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // m_Text = input.getText().toString();
                            //

                        //check item tontai
                        if(!input.getText().toString().trim().isEmpty()) {
                            if(listItems.contains(input.getText().toString().trim())) {
                              //  new ToastTB(Tuvung.this,"Thư mục này đã tồn tại!");
                                THONGBAOGAME thongbaogame = new THONGBAOGAME(Tuvung.this,getString(R.string.thongbao), getString(R.string.fd_exits));
                                //  new ToastTB(Tuvung.this,"Đã xoá thư mục được chọn!!");
                                thongbaogame.getTBS();
                            } else {
                                //add lisst
                                listItems.add(input.getText().toString().trim());
                                adapter.notifyDataSetChanged();
                                //Tao file thu muc
                                try {
                                File file_fd = new File(getExternalFilesDir(null).getAbsolutePath() +
                                        "/Folder_Word/" + input.getText().toString().trim() + ".ewd");
                                    file_fd.createNewFile();
                                byte[] data1={};
                                //write the bytes in file
                                if(file_fd.exists())
                                {
                                    OutputStream fo = new FileOutputStream(file_fd);
                                    fo.write(data1);
                                    fo.close();
                                   // new ToastTB(Tuvung.this,"Đã thêm thư mục '"+input.getText().toString().trim()+"'!");
                                    THONGBAOGAME thongbaogame = new THONGBAOGAME(Tuvung.this,getString(R.string.thongbao), getString(R.string.dathem_tm) + " '"+input.getText().toString().trim()+"'!");
                                    thongbaogame.getTBS();
                                    so_tm +=1;
                                    set_num_tm();
                                }
                                } catch (IOException ioe)
                                {
                                    ioe.printStackTrace();
                                  //  new ToastTB(Tuvung.this,"Không thể tạo thư mục '"+input.getText().toString().trim()+"'!");
                                    THONGBAOGAME thongbaogame = new THONGBAOGAME(Tuvung.this,getString(R.string.thongbao), getString(R.string.kothetm) + " '"+input.getText().toString().trim()+"'!");
                                    thongbaogame.getTBS();
                                }
                                //het tao

                                //
                            }
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
                //het click
            }
        });
    }
    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    //dong nay cua listbox
  //  public void addItems(View v) {
   //     listItems.add("Clicked : "+clickCounter++);
    //    adapter.notifyDataSetChanged();
  //  }

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
    //het cua listbox
    //Open sang view tv
    public void OpenTV() {
        Intent intent = new Intent(getApplicationContext(), View_tuvung.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //new ToastTB(Tuvung.this, "Đang mở '" + tenthumyuc + "'\nBấm 'Back' trên điện thoại để quay lại");
        startActivity(intent);
        Tuvung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
    //het open
    //start
    @Override
    public void onBackPressed() {
     //   if (mp != null) {
     //       mp.stop();
      //      mp.release();
       //     mp = null;
     //   }
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
       // new ToastTB(Tuvung.this, "Đang về trang chủ"  );
        startActivity(intent);
        Tuvung.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    //end
    private void set_num_tm() {
        lb_fd.setText(getString(R.string.tmtv) + " (" + so_tm + "):");
    }
    //

}
