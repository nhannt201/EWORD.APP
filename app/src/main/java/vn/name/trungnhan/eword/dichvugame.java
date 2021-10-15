package vn.name.trungnhan.eword;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

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
public class dichvugame {
    private Activity activity;
    private Context context;
    Integer integer;
    SharedPreferences lgrg;
    public static String log_reg = "logreg";
    String mail_tg = "";
    String colog = "";
    public  dichvugame(Activity activity, Integer integer) {
        this.activity = activity;
        this.context = activity;
        this.integer = integer;

    }

    //CK_ send score
    public  void sendgame() {
        if (checkInternetConnection()) {
            lgrg = context.getSharedPreferences(log_reg, 0);
            mail_tg = lgrg.getString("name_mail", null);
            colog = lgrg.getString("log", null);
            if (colog.contains("true")) {
            XULI_DATA xuli = new XULI_DATA(mail_tg);
            if (xuli.isAnti()) {
                String[] xuli_mail = xuli.catchuoi_string();

                String url = "https://<server>/eword/send_diem.php?email=" + xuli_mail[0] + "&name=" + xuli_mail[1] + "&rank=" + integer;
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
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //    input_tv.setText(myRespose.trim());
                                    // if (myRespose.trim().contains("yes")) {
                                    Log.i("ServiceGame", "dagui");

                                }
                            });
                        }
                    }
                });
            }
        }
        }
    }

    private boolean checkInternetConnection() {

        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


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
}
