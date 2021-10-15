package vn.name.trungnhan.eword;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class ToastTB {
    public ToastTB(Context context, String string) {
        Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

}
