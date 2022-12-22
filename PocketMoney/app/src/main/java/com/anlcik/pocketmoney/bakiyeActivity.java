package com.anlcik.pocketmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;

public class bakiyeActivity extends AppCompatActivity {

    SQLiteDatabase database;

    Button btnGelirTab, btnGiderTab;
    TextView textViewGelirler, textViewGiderler, textViewBakiye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakiye);

        textViewGelirler = findViewById(R.id.textViewGelirler);
        textViewGiderler = findViewById(R.id.textViewGiderler);
        textViewBakiye = findViewById(R.id.textViewBakiye);

        btnGelirTab = findViewById(R.id.btnGelirTab);
        btnGiderTab = findViewById(R.id.btnGiderTab);


        //VERİTABANI OLUŞTURMA / AÇMA KISMI
        try {
            database = this.openOrCreateDatabase("harcamalar",MODE_PRIVATE, null);

        }catch (Exception e){
            e.printStackTrace();
        }

        //TOPLAM GELİR MİKTARINI GETİREN FONKSİYONU ÇAĞIRDIK VE DEĞERİ TEXTVIEW'E EKLEDİK
        int topGelir = toplamGelir();
        textViewGelirler.setText(String.valueOf(topGelir+"TL"));
        //TOPLAM GİDER MİKTARINI GETİREN FONKSİYONU ÇAĞIRDIK VE DEĞERİ TEXTVIEW'E EKLEDİK
        int topGider = toplamGider();
        textViewGiderler.setText(String.valueOf(topGider+"TL"));
        //GELİR'DEN GİDER'İ ÇIKARTTIK VE BAKİYE'Yİ BULDUK, ARDINDAN TEXTVIEW'E EKLEDİK
        textViewBakiye.setText(String.valueOf(topGelir-topGider+"TL"));


        // GELİR SAYFASI BUTONUNA BASILDIĞINDA OLACAKLAR
        btnGelirTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bakiyeActivity.this, gelirActivity.class);
                startActivity(intent);
            }
        });

        // GİDER SAYFASI BUTONUNA BASILDIĞINDA OLACAKLAR
        btnGiderTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bakiyeActivity.this, giderActivity.class);
                startActivity(intent);
            }
        });
    }

    // TBLGELİRLER TABLOSUNDAKİ MİKTARLARI TOPLAR
    private int toplamGelir(){
        Cursor cursor = database.rawQuery("SELECT miktar FROM tblGelirler",null);

        int miktarIndex = cursor.getColumnIndex("miktar");
        int toplam = 0;

        while (cursor.moveToNext()){

            toplam = toplam + cursor.getInt(miktarIndex);

        }

        cursor.close();
        return toplam;
    }

    private int toplamGider(){
        Cursor cursor = database.rawQuery("SELECT miktar FROM tblGiderler",null);

        int miktarIndex = cursor.getColumnIndex("miktar");
        int toplam = 0;

        while(cursor.moveToNext()){

            toplam = toplam + cursor.getInt(miktarIndex);

        }

        cursor.close();
        return toplam;
    }
}