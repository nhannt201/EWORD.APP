package vn.name.trungnhan.eword;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class FILE_XULY {
    String UTF8 = "utf8";
    int BUFFER_SIZE = 8192;
    File file_read;
  //  String nhofiletam = "";
    public  FILE_XULY(File namefile) {
        this.file_read = namefile;
    }
    public String readfile() {
        int ch;
        StringBuffer sb = new StringBuffer();
            try {
                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file_read), UTF8),BUFFER_SIZE);
                while((ch = fin.read()) != -1) {
                    sb.append((char)ch);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return sb.toString();
    }

    public void writefile(String de_save, Context context) {
        try {
        if (file_read.exists()) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_read), UTF8), BUFFER_SIZE);
            XULI_DATA xuly = new XULI_DATA(de_save);
            bw.write(xuly.AddAnti());
            bw.close();
        }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Toast.makeText(context.getApplicationContext(),"Can't save data",Toast.LENGTH_LONG).show();
        }
    }

    public String[] catchuoi() throws UnsupportedEncodingException {
        FILE_XULY file_xuly = new FILE_XULY(file_read);
        XULI_DATA xuly = new XULI_DATA(file_xuly.readfile());
        if (xuly.isAnti() == true) {
            String text_sb = new String(xuly.DelAnti(), StandardCharsets.UTF_8);
           return text_sb.split("\\|");
        } else {
            return file_xuly.readfile().split("\\|");
        }
    }

}
