package com.demirgroup.notoped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.demirgroup.notoped.databinding.ActivityAddNoteBinding;

public class AddNote extends AppCompatActivity {
    SQLiteDatabase database;
    private ActivityAddNoteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddNoteBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        Intent intent=getIntent();
        String info=intent.getStringExtra("info");
        if (info.equals("new")){
            //new notes
            binding.button.setVisibility(View.VISIBLE);
        }else {
            //old notes
            binding.button.setVisibility(View.INVISIBLE);
            int noteId=intent.getIntExtra("noteId",0);
            try {
                database=this.openOrCreateDatabase("Notepad",MODE_PRIVATE,null);
                Cursor cursor=database.rawQuery("SELECT * FROM notepad WHERE id=?",new String[]{String.valueOf(noteId)});
                while (cursor.moveToNext()){
                    binding.txttitle.setText(cursor.getString(1));
                    binding.txtnote.setText(cursor.getString(2));
                }
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void save(View view){
        try {
            database=this.openOrCreateDatabase("Notepad",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS notepad (id INTEGER PRIMARY KEY,title VARCHAR,note VARCHAR)");
            SQLiteStatement sqLiteStatement=database.compileStatement("INSERT INTO notepad (title,note) VALUES(?,?)");
            sqLiteStatement.bindString(1,binding.txttitle.getText().toString());
            sqLiteStatement.bindString(2,binding.txtnote.getText().toString());
            sqLiteStatement.execute();
            database.close();
            Toast.makeText(AddNote.this,"Added Succesfully Note.",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(AddNote.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}