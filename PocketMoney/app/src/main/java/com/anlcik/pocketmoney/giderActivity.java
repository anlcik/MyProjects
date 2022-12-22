package com.anlcik.pocketmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class giderActivity extends AppCompatActivity {

    SQLiteDatabase database;

    ListView listViewGider;

    Button btnGelirTab, btnBakiyeTab, btnGiderEkle;
    Spinner spinnerGider;
    EditText editTextGider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gider);

        btnGelirTab = findViewById(R.id.btnGelirTab);
        btnBakiyeTab = findViewById(R.id.btnBakiyeTab);
        btnGiderEkle = findViewById(R.id.btnGiderEkle);

        spinnerGider = findViewById(R.id.spinnerGider);

        editTextGider = findViewById(R.id.editTextGider);

        listViewGider = findViewById(R.id.listViewGider);

        //VERİTABANI OLUŞTURMA / AÇMA KISMI
        try {
            database = this.openOrCreateDatabase("harcamalar",MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS tblGiderler(gelirID INTEGER PRIMARY KEY AUTOINCREMENT,kategori NVARCHAR, miktar INT)");

        }catch (Exception e){
            e.printStackTrace();
        }

        verileriGetir();

        // GELİR SAYFASI BUTONUNA BASILDIĞINDA OLACAKLAR
        btnGelirTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(giderActivity.this, gelirActivity.class);
                startActivity(intent);
            }
        });

        // BAKİYE SAYFASI BUTONUNA BASILDIĞINDA OLACAKLAR
        btnBakiyeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(giderActivity.this, bakiyeActivity.class);
                startActivity(intent);
            }
        });

        //GİDER EKLE BUTONUNA BASILDIĞINDA OLACAKLAR
        btnGiderEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (!TextUtils.isEmpty(editTextGider.getText())){

                        String kategori = spinnerGider.getSelectedItem().toString();
                        Integer miktar = Integer.parseInt(editTextGider.getText().toString());

                        database.execSQL("INSERT INTO tblGiderler (kategori, miktar) VALUES ('"+kategori+"',"+miktar+")");

                        verileriGetir();

                        editTextGider.setText("");
                    }
                    else{
                        Toast.makeText(giderActivity.this, "Lütfen miktar giriniz.", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //VERİLERİ VERİTABANINDAN ALIP LISTVİEW'E EKLEYEN KISIM
    private void verileriGetir(){
        Cursor cursor = database.rawQuery("SELECT * FROM tblGiderler",null);

        int kategoriIndex = cursor.getColumnIndex("kategori");
        int miktarIndex = cursor.getColumnIndex("miktar");
        int idIndex = cursor.getColumnIndex("gelirID");

        ArrayList<String> giderler =new ArrayList<String>();
        ArrayAdapter adapter;

        adapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1,giderler);
        listViewGider.setAdapter(adapter);

        while (cursor.moveToNext()){

            giderler.add( cursor.getString(kategoriIndex) + "           " + cursor.getInt(miktarIndex) + "TL");
            listViewGider.setAdapter(adapter);

            //VERİLERİN KONTROLÜ
            //System.out.println( cursor.getInt(idIndex)+ " " + cursor.getString(kategoriIndex) + "           " + cursor.getInt(miktarIndex) + "TL");
        }
        cursor.close();
    }
}