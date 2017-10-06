package com.popland.pop.sqliteopenhelperdemo;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_Data extends AppCompatActivity {
StackView sv;
    String id, keyword;
    byte[] pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__data);
        sv = (StackView)findViewById(R.id.stackView);
        final ArrayList<TeIm> arrData = new ArrayList<>();
        
        // 1. tranfer data from SQLite to ArrayList
        Cursor cursor = MainActivity.dataBase.getData("SELECT * FROM NuIm");
        while(cursor.moveToNext()){
            id = cursor.getString(1);
            keyword = cursor.getString(2);
            pic = cursor.getBlob(3);
            arrData.add(new TeIm(id, keyword, pic));
        }

        //2.  AdapterView receive data from Arraylist
        final StackViewAdapter adapter = new StackViewAdapter(arrData);
        sv.setAdapter(adapter);

        // Delete data from SQlite, ArrayList, AdapterView
        sv.setOnItemClickListener(new AdapterView.OnItemClickListener() {//LongClick sucks
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//position from 0
              //  Toast.makeText(Activity_Data.this,"position: "+position,Toast.LENGTH_LONG).show();
                MainActivity.dataBase.queryData("DELETE FROM NuIm WHERE id ="+arrData.get(position).id);
                arrData.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(Activity_Data.this,"deleted "+(position+1),Toast.LENGTH_LONG).show();
            }
        });
    }

     class StackViewAdapter extends BaseAdapter {
        ArrayList<TeIm> arr;

        StackViewAdapter(ArrayList<TeIm> arr){
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.stackview_custom,parent,false);
            TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
            ImageView ivPicture = (ImageView)convertView.findViewById(R.id.ivPicture);
            tvText.setText(arr.get(position).id+". "+arr.get(position).text);
            Bitmap bm = BitmapFactory.decodeByteArray(arr.get(position).hinh,0,arr.get(position).hinh.length);
            ivPicture.setImageBitmap(bm);
            return convertView;
        }
    }
}
//C:\Users\hai\AppData\Local\Android\sdk1\platform-tools