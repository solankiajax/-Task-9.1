package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfound.data.DatabaseHelper;
import com.example.lostandfound.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {
    ListView itemsListView ;

    ArrayList<String> itemArrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all);

        itemsListView = findViewById(R.id.itemsListView);
        itemArrayList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);

        List<Item> itemList = db.fetchAllItems();
        for (Item item :itemList)
        {
                itemArrayList.add(item.getDescription());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemArrayList);
        itemsListView.setAdapter(adapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    //Toast.makeText(ShowAllActivity.this,Integer.toString(position),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShowAllActivity.this,RemoveItemActivity.class);
                    for (Item item :itemList)
                    {
                        if(item.getDescription().equals((String) itemsListView.getItemAtPosition(position))){
                            int ITEM_ID = item.getItem_id();
                            intent.putExtra("position",ITEM_ID);
                        }
                    }
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.d("REACHED",e.getMessage());
                }

           }
        });

    }
}