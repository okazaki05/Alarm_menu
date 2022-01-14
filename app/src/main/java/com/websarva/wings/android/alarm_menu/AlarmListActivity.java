package com.websarva.wings.android.alarm_menu;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;




public class AlarmListActivity extends AppCompatActivity {
    ListView myListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("AlarmListActivity::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        Button button = findViewById(R.id.button6);
        button.setOnClickListener(v -> finish());



        myListView = findViewById(R.id.listView);
        MyOpenHelper myOpenHelper = new MyOpenHelper(this);


        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from t_alarm", null);
        boolean flg=false;
        if(c != null) {
            System.out.println("AlarmListActivity::onCreate() cursor" + c.getCount());
            // 検索結果が0件でなければ(!=)、リスト配列に内容をセットする
            if (c.getCount() != 0) {
                // 検索結果の先頭にカーソルを移動する
                flg = c.moveToFirst();
            }
        }


        String[] from = {"_id", "a_time"};

        //バインドするViewリソース
        int[] to = {android.R.id.text1, android.R.id.text2};

        //adapterの生成
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to, 0);

        //バインドして表示
        myListView.setAdapter(adapter);


        //リストビューをタップした時の各行のデータを取得
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {




                //各要素を取得
                //_id
                String s1 = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();


                //参照・更新へ
                Intent intent = new Intent(getApplication(),AlarmRegisterActivity.class);




                //モード指定　_idを渡す
                intent.putExtra("KBN", s1);

                //行く
                startActivity(intent);
            }
        });
    }





    //リターン時
    @Override
    protected void onRestart(){
        super.onRestart();
        reload();
    }


    //新規ボタンを押したときは新規登録へ画面を遷移
    public void Register(View view) {
        Intent intent = new Intent(getApplication(), AlarmRegisterActivity.class);

        // モード指定　空は新規
        intent.putExtra("KBN", "");

        //行く
        startActivity(intent);
    }


    public void reload(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

}