package vn.name.trungnhan.eword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
/**
 * @desc Ứng dụng học từ vựng tiếng Anh EWORD
 * @author Nguyễn Trung Nhẫn trungnhan0911@yandex.com
 * @coppyright 2021 - MIT Lisence
 */
public class THONGBAOGAME {
    private String name_tb;
    private String nd_tb;
    private LayoutInflater layoutInflater;
    public THONGBAOGAME(Context aContext, String name_tb, String nd_tb) {
        this.name_tb = name_tb;
        this.nd_tb = nd_tb;
        layoutInflater = LayoutInflater.from(aContext);
    }
    public void getTBS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(layoutInflater.getContext());
        builder.setTitle(name_tb);
        builder.setMessage(nd_tb);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
