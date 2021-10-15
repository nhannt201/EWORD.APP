package vn.name.trungnhan.eword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


//import static vn.name.trungnhan.eword.MainActivity.mp;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class Game extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    //   int clickCounter=0;

    public static String fd_name = "folder_tam";
    public static MediaPlayer mn = null;
    SharedPreferences SP_fd;
    private Button bt_g1;
    private Button bt_g1_1;
    private Button bt_g1_2;
    private Button bt_g2;
    private Button bt_g2_1;
    private Button bt_g2_2;
    private Button bt_gm3;
  //  Integer total_r;
    String UTF8 = "utf8";
    int BUFFER_SIZE = 8192;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (mn != null) {
            mn.stop();
            mn.release();
            mn = null;
        }
        bt_gm3 = findViewById(R.id.bt_gm3);
        bt_gm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonTM(3);
            }
        });
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
bt_g1_1 = findViewById(R.id.bt_gm1_1);
bt_g1_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ChonTM(11);
    }
});
        bt_g1_2 = findViewById(R.id.bt_gm1_2);
        bt_g1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonTM(12);
            }
        });
        bt_g2_1 = findViewById(R.id.bt_gm2_1);
        bt_g2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonTM(21);
            }
        });
        bt_g2_2 = findViewById(R.id.bt_gm2_2);
        bt_g2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonTM(22);
            }
        });
        bt_g1 = findViewById(R.id.bt_gm1);
        bt_g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonTM(0);
                //
            }
        });
        bt_g2 = findViewById(R.id.bt_gm2);
        bt_g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChonTM(1);
            }
        });
    }



    //start
    @Override
    public void onBackPressed() {
       // if (mp != null) {
      //      mp.stop();
       //     mp.release();
       //     mp = null;
       // }
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
     //   Toast.makeText(Game.this, "Đang về trang chủ"  , Toast.LENGTH_SHORT).show();
        startActivity(intent);
        Game.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    //end

    public void ChonTM(final Integer thutugame) {


            LayoutInflater factory = LayoutInflater.from(Game.this);
            final View textEntryView = factory.inflate(R.layout.dialog_chon_fd, null);
        final ListView mListView = textEntryView.findViewById(R.id.lst_chontm);
        final TextView txt_chose = textEntryView.findViewById(R.id.thumucdangchon);

        adapter=new ArrayAdapter<String>(Game.this, android.R.layout.simple_list_item_1,listItems);
        mListView.setAdapter(adapter);
        //
        listItems.clear();
        Integer dem_1 = 0;
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() +
                "/Folder_Word");        for (File f : folder.listFiles()) {
            if (f.isFile())
                //  name = f.getName();
                listItems.add(f.getName().replace(".ewd",""));
            dem_1 = dem_1 +1;
            // Do your stuff
        }
        adapter.notifyDataSetChanged();
        //
        if (dem_1 == 0 ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.thongbao));
            builder.setMessage(getString(R.string.cont_ms2));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

        // Populate ListView with items from ArrayAdapter
        mListView.setAdapter(adapter);
        // Set an item click listener for ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
              //  num_id =  position;
                txt_chose.setText(selectedItem);
                SP_fd = getSharedPreferences(fd_name, 0 );
                SharedPreferences.Editor editit = SP_fd.edit();
                editit.putString("fd_name", selectedItem);
                editit.commit();

                //Check vo game luon
                //
                File file_tv = new File(getExternalFilesDir(null).getAbsolutePath() +
                        "/Folder_Word/" + txt_chose.getText().toString().trim() + ".ewd");
                //     FileInputStream fin = null;
                int ch;
                StringBuffer sb = new StringBuffer();

                try {
                    // create FileInputStream object
                    BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file_tv), UTF8),BUFFER_SIZE);
                    while((ch = fin.read()) != -1) {
                        sb.append((char)ch);
                    }

                    String[] arrOfStr;
                    if (isAnti(sb.toString()) == true) {
                        byte[] data = Base64.decode(load_anti(sb.toString()), Base64.DEFAULT);
                        String text_sb = new String(data, StandardCharsets.UTF_8);
                        arrOfStr = text_sb.split("\\|");

                    } else {
                        arrOfStr = sb.toString().split("\\|");
                    }
                    // for (String a : arrOfStr) {
                    //    total_r = total_r +1;
                    // }
                    if (arrOfStr.length < 5) {
                        Toast.makeText(Game.this, getString(R.string.it_tv), Toast.LENGTH_SHORT).show();
                    } else {
                        //GO game
                        Intent intent;
                        if (thutugame == 0) {
                             intent = new Intent(getApplicationContext(),Game1.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else if (thutugame == 11) {
                            intent = new Intent(getApplicationContext(),Game1_1.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         //   Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }  else if (thutugame == 12) {
                            intent = new Intent(getApplicationContext(),Game1_2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         //   Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else if (thutugame == 21) {
                            intent = new Intent(getApplicationContext(),game2_1.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //    Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }  else if (thutugame == 22) {
                            intent = new Intent(getApplicationContext(),game2_2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else if(thutugame == 1) {
                             intent = new Intent(getApplicationContext(),game2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          //  Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else if(thutugame == 3) {
                            intent = new Intent(getApplicationContext(),game3.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //  Toast.makeText(Game.this, "Đang mở game...", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }

                        Game.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        finish();
                        //end go
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //

                //end check

            }
        });
           // final ListView input_ta = (EditText) textEntryView.findViewById(R.id.input_ta);
            final AlertDialog.Builder alert = new AlertDialog.Builder(Game.this);
            alert.setIcon(R.mipmap.tuvung).setTitle(getString(R.string.chose_fd)).setView(textEntryView);
            alert.show();
            //Het Edit TV
        }

        //Ck encode
    private String load_anti(String rass) {
        rass = rass.replace("SHjhjYUkmlrdrJBlllkSPGCH","").replace("jjkkhFIUDMKSDJFLNVVKDFNF","")
                .replace("MUSADNfdfLkjljNKNgFKJNFTU","").replace("lLJDNMMVCDXJDSFuihilIVXLN","");
        return  rass;
    }

    private boolean isAnti(String string) {
        if (string.contains("SHjhjYUkmlrdrJBlllkSPGCH") == true) {
            return true;
        } else if (string.contains("jjkkhFIUDMKSDJFLNVVKDFNF") == true) {
            return true;
        } else if (string.contains("MUSADNfdfLkjljNKNgFKJNFTU") == true) {
            return true;
        } else return string.contains("lLJDNMMVCDXJDSFuihilIVXLN") == true;
    }
    //end ck
    }
    //Het edit

