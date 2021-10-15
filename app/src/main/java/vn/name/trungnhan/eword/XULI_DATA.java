package vn.name.trungnhan.eword;
import android.util.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class XULI_DATA {
    String noidung;


    public XULI_DATA(String noidung) {
        this.noidung = noidung;
    }

    public String AddAnti( ) throws UnsupportedEncodingException {
        byte[] cov_base64  = noidung.getBytes(StandardCharsets.UTF_8);
        String mahoa =  Base64.encodeToString(cov_base64, Base64.DEFAULT);
        String rans = noidung;
        Random random = new Random();
        int randomNumber = random.nextInt(4) ;
        if (randomNumber == 0) {
            rans = "SHjhjYUkmlrdrJBlllkSPGCH";
        } else  if (randomNumber == 1) {
            rans = "jjkkhFIUDMKSDJFLNVVKDFNF";
        } else  if (randomNumber == 2) {
            rans = "MUSADNfdfLkjljNKNgFKJNFTU";
        } else  if (randomNumber == 3) {
            rans = "lLJDNMMVCDXJDSFuihilIVXLN";
        }
        return rans + mahoa;
    }
    //
    public String AddAntiN( ) {
        byte[] cov_base64  = noidung.getBytes(StandardCharsets.UTF_8);
        String mahoa =  Base64.encodeToString(cov_base64, Base64.DEFAULT);
        String rans = noidung;
        Random random = new Random();
        int randomNumber = random.nextInt(4) ;
        if (randomNumber == 0) {
            rans = "SHjhjYUkmlrdrJBlllkSPGCH";
        } else  if (randomNumber == 1) {
            rans = "jjkkhFIUDMKSDJFLNVVKDFNF";
        } else  if (randomNumber == 2) {
            rans = "MUSADNfdfLkjljNKNgFKJNFTU";
        } else  if (randomNumber == 3) {
            rans = "lLJDNMMVCDXJDSFuihilIVXLN";
        }
        return rans + mahoa;
    }

    public byte[] DelAnti() {
        String rass = noidung;
        rass = rass.replace("SHjhjYUkmlrdrJBlllkSPGCH","").replace("jjkkhFIUDMKSDJFLNVVKDFNF","")
                .replace("MUSADNfdfLkjljNKNgFKJNFTU","").replace("lLJDNMMVCDXJDSFuihilIVXLN","");
        byte[] data = Base64.decode(rass, Base64.DEFAULT);

        return  data;
    }
    public String[] catchuoi_string() {
        XULI_DATA xuli = new XULI_DATA(noidung);
        String text_sb = new String(xuli.DelAnti(), StandardCharsets.UTF_8);
        return text_sb.split("\\|");
    }
    public boolean isAnti() {
        String string = noidung;
        if (string.contains("SHjhjYUkmlrdrJBlllkSPGCH") == true) {
            return true;
        } else if (string.contains("jjkkhFIUDMKSDJFLNVVKDFNF") == true) {
            return true;
        } else if (string.contains("MUSADNfdfLkjljNKNgFKJNFTU") == true) {
            return true;
        } else return string.contains("lLJDNMMVCDXJDSFuihilIVXLN") == true;
    }

}
