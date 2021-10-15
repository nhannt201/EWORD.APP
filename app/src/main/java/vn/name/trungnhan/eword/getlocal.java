package vn.name.trungnhan.eword;

import java.util.Locale;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class getlocal {
    public  getlocal(){
    }
    public String get(){
        return   Locale.getDefault().getLanguage();
    }
}
