package com.anlcik.pocketmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class gelirActivity extends AppCompatActivity {

    SQLiteDatabase database;

    ListView listViewGelir;

    Button btnBakiyeTab, btnGiderTab, btnGelirEkle;
    Spinner spinnerGelir;
    EditText editTextGelir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelir);

        btnBakiyeTab = findViewById(R.id.btnBakiyeTab);
        btnGiderTab = findViewById(R.id.btnGiderTab);
        btnGelirEkle = findViewById(R.id.btnGelirEkle);

        spinnerGelir = findViewById(R.id.spinnerGelir);

        editTextGelir = findViewById(R.id.editTextGelir);

        listViewGelir = findViewById(R.id.listViewGelir);


        //VERİTABANI OLUŞTURMA / AÇMA KISMI
        try {
            database = this.openOrCreateDatabase("harcamalar",MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS tblGelirler(gelirID INTEGER PRIMARY KEY AUTOINCREMENT,kategori NVARCHAR, miktar INT)");

        }catch (Exception e){
            e.printStackTrace();
        }

        //VERİLERİ GETİRME METODUNUN ÇAĞRILDIĞI KISIM
        verileriGetir();

        // BAKİYE SAYFASI BUTONUNA BASILDIĞINDA OLACAKLAR
        btnBakiyeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(gelirActivity.this, bakiyeActivity.class);
                startActivity(intent);
            }
        });

        // GİDER SAYFASI BUTONUNA BASILDIĞINDA OLACAKLAR
        btnGiderTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gelirActivity.this,giderActivity.class);
                startActivity(intent);
            }
        });

        //GELİR EKLE BUTONUNA BASILDIĞINA OLACAKLAR
        btnGelirEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (!TextUtils.isEmpty(editTextGelir.getText())){

                        String kategori = spinnerGelir.getSelectedItem().toString();
                        Integer miktar = Integer.parseInt(editTextGelir.getText().toString());

                        database.execSQL("INSERT INTO tblGelirler (kategori, miktar) VALUES ('"+kategori+"',"+miktar+")");

                        verileriGetir();

                        editTextGelir.setText("");
                    }
                    else{
                        Toast.makeText(gelirActivity.this, "Lütfen miktar giriniz.", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    //VERİLERİ VERİTABANINDAN ALIP LISTVİEW'E EKLEYEN KISIM
    private void verileriGetir(){
        Cursor cursor = database.rawQuery("SELECT * FROM tblGelirler",null);

        int kategoriIndex = cursor.getColumnIndex("kategori");
        int miktarIndex = cursor.getColumnIndex("miktar");
        int idIndex = cursor.getColumnIndex("gelirID");

        ArrayList<String> gelirler =new ArrayList<String>();
        ArrayAdapter adapter;

        adapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1,gelirler);
        listViewGelir.setAdapter(adapter);

        while (cursor.moveToNext()){

            gelirler.add( cursor.getString(kategoriIndex) + "           " + cursor.getInt(miktarIndex) + "TL");
            listViewGelir.setAdapter(adapter);

            //VERİLERİN KONTROLÜ
            //System.out.println( cursor.getInt(idIndex)+ " " + cursor.getString(kategoriIndex) + "           " + cursor.getInt(miktarIndex) + "TL");
        }
        cursor.close();
    }
}