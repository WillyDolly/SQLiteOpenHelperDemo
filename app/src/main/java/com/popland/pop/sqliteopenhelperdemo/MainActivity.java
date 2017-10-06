package com.popland.pop.sqliteopenhelperdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
EditText edtId, edtKeyWord;
ImageView ivPic;
Button btnInsert, btnUpdate, btnSelect;
int REQUEST_CODE = 999;
public static   MySQlite dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();

        dataBase = new MySQlite(this,"MajorSystem.db",null,1);// SQLiteBrowser can find .db
        //SQLite only host Blob (Blob = byte[])
        dataBase.queryData("CREATE TABLE IF NOT EXISTS NuIm(stt INTEGER PRIMARY KEY AUTOINCREMENT, id VARCHAR, keyword VARCHAR, pic BLOB)");
        //AUTOINCREMENT start from 1
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String keyword = edtKeyWord.getText().toString();
               // dataBase.queryData("INSERT INTO NuIm VALUES(null,"+id+","+keyword+","+ImageView_To_ByteArray(ivPic)+")"); // Blob need SQLiteStatement
                dataBase.Insert_Data(id,keyword,ImageView_To_ByteArray(ivPic));
                edtId.setText("");
                edtKeyWord.setText("");
                ivPic.setImageResource(android.R.drawable.ic_input_add);
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Activity_Data.class);
                startActivity(i);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String newKeyWord = edtKeyWord.getText().toString();
                //never update image in queryData, must use Statement
                dataBase.queryData("UPDATE NuIm SET keyword= '"+newKeyWord+"' WHERE id ="+id);// note ' ' for TEXT/Varchar
            }
        });
    }

    void Anhxa(){
        edtId = (EditText)findViewById(R.id.edtId);
        edtKeyWord = (EditText)findViewById(R.id.edtKeywor);
        ivPic = (ImageView)findViewById(R.id.ivPic);
        btnInsert = (Button)findViewById(R.id.btnInsert);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnSelect = (Button)findViewById(R.id.btnSelect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            Bitmap bm = (Bitmap)data.getExtras().get("data");
            ivPic.setImageBitmap(bm);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    byte[] ImageView_To_ByteArray(ImageView iv){// Picasso does magic with ImageView(pic frame)
        BitmapDrawable bd = (BitmapDrawable) iv.getDrawable();// white paper
        Bitmap bm = bd.getBitmap();// image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// bag
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);// filter
        byte[] pixel = baos.toByteArray();// cai mang
        return pixel;
    }
}
