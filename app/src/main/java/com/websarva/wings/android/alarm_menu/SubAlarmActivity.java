package com.websarva.wings.android.alarm_menu;

import static android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
import static android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED;
import static android.media.AudioAttributes.USAGE_ALARM;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubAlarmActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer; //メディアプレイヤー
    Uri uri; //音声ファイルのURI
    int snz_cnt = 0; // スヌーズの回数
    long startMillis =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("SubAlarmActivity::onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_alarm);

        TextView textView = findViewById(R.id.textView);
        textView.setText("停止ボタンを押してアラームを止めてください");
    }
    @Override
    protected void onResume() {
        System.out.println("SubAlarmActivity::onResume()");
        super.onResume();
        //アラーム起動
        Alarm();
    }

    //アラームを鳴らす
    private void Alarm() {
        System.out.println("SubAlarmActivity::Alarm()");

        //uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);        //着信音
        //uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);    //通知音
        //uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);             //アラーム音

        if( snz_cnt == 0 ) {
            //mediaPlayer = new MediaPlayer();
            mediaPlayer = MediaPlayer.create(this, R.raw.morning);
            //ミュートでなければこれはいらない　つまりミュート対策-------
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(USAGE_ALARM)                //アラーム音
                            //.setUsage(USAGE_ASSISTANCE_SONIFICATION)            //着信音と通知音
                            .setContentType(CONTENT_TYPE_MUSIC)   //アラーム・メディアの音量
                            .setFlags(FLAG_AUDIBILITY_ENFORCED)   //ミュートでも鳴らす
                            .build());
            //mediaPlayer.setDataSource(this, uri);
            //mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    //停止ボタンが押された時
    public void end(View view) {
        System.out.println("SubAlarmActivity::end()");

        //ベースコンテキストを取得
        Context context = getBaseContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SubAlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);


        //音の停止
        mediaPlayer.stop();

        finish();
    }

    //スヌーズボタンが押された時
    public void snooze(View view) {

        //再びアラームを鳴らす時間を設定
        long repeatPeriod = 60*1000; //1分
        startMillis = System.currentTimeMillis() + repeatPeriod;

        // 初回
        if(snz_cnt == 0){

        // 2回目以降
        }else{

        }

        //音の停止
        mediaPlayer.stop();

        //カウントアップ
        snz_cnt = snz_cnt + 1;
        //onResume
        onResume();
    }
}
