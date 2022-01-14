package com.websarva.wings.android.alarm_menu;

import static android.os.Build.TIME;

import static java.lang.String.format;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.util.Calendar;
import java.util.Locale;

public class AlarmRegisterActivity extends AppCompatActivity {


    TextView txt = null;
    private MyOpenHelper helper;
    String kbn = "";
    String toastMessage = "登録しました。戻るを押してください。";
    String toastMessage2 = "登録するものがありません";
    String toastMessage3 = "更新しました。戻るを押してください。";
    String toastMessage4 = "更新するものがありません";


    //時間になったら起動するパラメータ
    PendingIntent pendingIntent;
    AlarmManager manager;

    //設定時刻表示用
    String data;
    //初期値用 x:時　y:分
    int x=0;
    int y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_register);
        helper = new MyOpenHelper(getApplicationContext());
        txt = (TextView) findViewById(R.id.textView8);
        Intent intent = getIntent();
        String KBN  = intent.getStringExtra("KBN");
        Button button = findViewById(R.id.button8);
        View view = findViewById(R.id.Layout);
        Button button1 = findViewById(R.id.button4);





        if (KBN.length() != 0) {
            kbn = KBN;
            button.setText("保存");
            view.setBackgroundColor(Color.rgb(240, 230, 140));
            readData(KBN);
        } else {
            kbn = "登録";
            button.setText("登録");
            view.setBackgroundColor(Color.rgb(240, 230, 140));
        }
    }

    //データを参照する
    private void readData(String read) {
        System.out.println("AlarmRegisterActivity::readDate() read="+read);

        SQLiteDatabase db = helper.getReadableDatabase();
        EditText ed1 = findViewById(R.id.editText1);
        TextView txt = findViewById(R.id.textView8);
        Switch switch1 = findViewById(R.id.switch1);
        EditText ed2 = findViewById(R.id.editText2);
        Spinner spinner = findViewById(R.id.sp_teacher);
/*
        Cursor cursor = db.query(
                "t_alarm",
                new String[]{"a_no","a_time","a_snooze","a_name","a_voice" },
                Arrays.toString(new String[]{read}),
                null,null,null,null,null
        );
*/
        Cursor cursor = db.query(
                "t_alarm",
                new String[]{"a_no","a_time","a_snooze","a_name","a_voice" },
                "_id=?",
                new String[]{read},null,null,null,null
        );
        cursor.moveToFirst();

        System.out.println("AlarmRegisterActivity::readDate() cursor.getCount()="+cursor.getCount());

        for(int i=0;i<cursor.getCount();i++){
            ed1.setText(cursor.getString(0));
            txt.setText(cursor.getString(1));
            switch1.setText(cursor.getString(2));
            ed2.setText(cursor.getString(3));
            spinner.setTextAlignment(cursor.getInt(4));
        }
        cursor.close();

    }

    public void saveData(View view) {

        aet(view);

        System.out.println("AlarmRegisterActivity:saveData");
        System.out.println("kbn="+kbn);

        SQLiteDatabase db = helper.getWritableDatabase();

        EditText editText = findViewById(R.id.editText1);
        TextView text = findViewById(R.id.textView8);
        Switch switch1 = findViewById(R.id.switch1);
        EditText editText2 = findViewById(R.id.editText2);
        Spinner spinner = findViewById(R.id.sp_teacher);

        String a_no = editText.getText().toString();
        String a_time = text.getText().toString();
        String a_snooze = switch1.getText().toString();
        String a_name = editText2.getText().toString();
        String a_voice = spinner.getClass().toString();

        ContentValues values = new ContentValues();
        values.put("a_no", a_no);
        values.put("a_time", a_time);
        values.put("a_snooze", a_snooze);
        values.put("a_name", a_name);
        values.put("a_voice", a_voice);

        try {
            if (kbn.equals("登録")) {
                if (TIME != 0) {
                    //新規登録
                    db.insert("t_alarm", null, values);
                    //トースト表示
                    toastMake(toastMessage, 0, 350);
                } else {
                    //トースト表示
                    toastMake(toastMessage2, 0, 350);
                }

                //ボタンが更新の場合
            } else {
                if (TIME != 0) {
                    //更新
                    UPData(kbn);
                    //トースト表示
                    toastMake(toastMessage3, 0, 350);
                } else {
                    //トースト表示
                    toastMake(toastMessage4, 0, 350);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private void UPData(String read) {
        System.out.println("AlarmRegisterActivity:UPData");
        SQLiteDatabase db = helper.getReadableDatabase();

        EditText editText = findViewById(R.id.editText1);
        TextView text = findViewById(R.id.textView8);
        Switch switch1 = findViewById(R.id.switch1);
        EditText editText2 = findViewById(R.id.editText2);
        Spinner spinner = findViewById(R.id.sp_teacher);

        String a_no = editText.getText().toString();
        String a_time = text.getText().toString();
        String a_snooze = switch1.getText().toString();
        String a_name = editText2.getText().toString();
        String a_voice = spinner.getClass().toString();

        ContentValues value = new ContentValues();
        value.put("a_no",a_no);
        value.put("a_time",a_time);
        value.put("a_snooze",a_snooze);
        value.put("a_name",a_name);
        value.put("a_voice",a_voice);

        db.update("t_alarm",value,"_id=?",new String[]{read});
    }

    private void toastMake(String message, int x ,int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER,x,y);
        toast.show();
    }
    public void onClose( View v){
        finish();       // 画面を閉じる（アクティビティの終了）
    }













    public void nya(View v){

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        System.out.println("AlarmRegister::nya() hour="+hour+"minute="+minute);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    Toast.makeText(AlarmRegisterActivity.this, hourOfDay+"時"+ minute1 +"分", Toast.LENGTH_LONG).show();
                    // 画面上のテキストビューに設定時刻を表示する
                    txt.setText(hourOfDay+":"+ minute1 );

                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void aet(View view) {
        System.out.println("AlarmRegisterActivity::aet()");
        write();

        //開始ボタンを押したら画面を隠す
        //finish();

        //タイマー起動
        //ベースコンテキストを取得
        Context context = getBaseContext();

        //アラームマネージャーの作成と設定
        manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //インテントの作成   指定時刻にSubActivityを起動
        Intent intent = new Intent(context, SubAlarmActivity.class);

        //ペンディングインテントの作成
        pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        //カレンダーの作成
        Calendar calendar = Calendar.getInstance();     //現在時間が取得される

        //指定時間をセット
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, x);
        calendar.set(Calendar.MINUTE, y);
        calendar.set(Calendar.SECOND, 0);

        System.out.println("AlarmRegisterActivity::aet() x="+x+" y="+y);
        System.out.println("AlarmRegisterActivity::aet() calendar.getTimeInMillis()="+calendar.getTimeInMillis());

// 1/14 12:11 スヌーズを繰り返すので、いったんコメント
/*
        //指定間隔を分単位でする場合
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 1, pendingIntent);
        System.out.println("開始時------------------>" + pendingIntent);
*/

        //指定間隔を予め用意されている間隔でする場合
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //        AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        //1 分後にデバイスのスリープを解除し、アラームを 1 回だけ（反復なし）トリガーします。
//        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);


    }

    private void write(){
        System.out.println("AlarmRegisterActivity::write()");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //プレーンテキストから値取得
        TextView TIME=findViewById(R.id.textView8);
        data=TIME.getText().toString();
        String ttt=(data.substring(0,2));
        String mmm=(data.substring(3));

        System.out.println("AlarmRegisterActivity::write() ttt="+ttt);
        System.out.println("AlarmRegisterActivity::write() mmm="+mmm);

        //TextView pct = findViewById(R.id.percent);
        //String text = pct.getText().toString();

        //数値に変換
        int tt = Integer.parseInt(ttt);
        int mm = Integer.parseInt(mmm);
        //int ps = Integer.parseInt(text);

        //SharedPreferencesに保存
        editor.putInt("SET_TIME", tt);
        editor.putInt("SET_Minutes", mm);
        //editor.putInt("percent", ps);

        //実際の保存
        editor.apply();

        read();

    }

    private void read() {
        System.out.println("AlarmRegisterActivity::read()");

        //値を読み出す
        SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(this);

        //SharedPreferencesからの呼び出し時刻
        int time = pref.getInt("SET_TIME", 0);
        int Minutes = pref.getInt("SET_Minutes", 0);

        //バッテリーボーダーラインの呼び出し
        //int percent = pref.getInt("percent", 40);


        x=time;
        y=Minutes;

        //テキストにセット
        String sTIME = format(Locale.JAPANESE, "%02d:%02d", x, y);
        TextView textView = findViewById(R.id.textView8);
        textView.setText(sTIME);


    }


}
