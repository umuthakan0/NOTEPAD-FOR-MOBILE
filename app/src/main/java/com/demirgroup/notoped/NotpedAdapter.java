package com.demirgroup.notoped;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demirgroup.notoped.databinding.RecyclerviewBinding;

import java.util.ArrayList;
import java.util.HashSet;

public class NotpedAdapter extends RecyclerView.Adapter<NotpedAdapter.notpedHolder> {
    ArrayList<Notped> notpedArrayList;
    SQLiteDatabase database;
    public NotpedAdapter(ArrayList<Notped> notpedArrayList) {
        this.notpedArrayList = notpedArrayList;
    }

    @NonNull
    @Override
    public notpedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewBinding recyclerviewBinding=RecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new notpedHolder(recyclerviewBinding);
    }
    Intent intent;
    NotpedAdapter notpedAdapter;
    @Override
    public void onBindViewHolder(@NonNull notpedHolder holder, int position) {
        holder.binding.txtnotes.setText(notpedArrayList.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(holder.itemView.getContext(), AddNote.class);
                intent.putExtra("info","old");
                intent.putExtra("noteId",notpedArrayList.get(position).id);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position=holder.getLayoutPosition();
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to the this item?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deletedId=notpedArrayList.get(position).id;
                        try {
                            database=holder.itemView.getContext().openOrCreateDatabase("Notepad",MODE_PRIVATE,null);
                            database.execSQL("DELETE FROM notepad WHERE id=?",new String[]{String.valueOf(deletedId)});
                            notifyItemRemoved(position);
                            notpedArrayList.remove(position);
                            Intent delintent=new Intent(holder.itemView.getContext(),MainActivity.class);
                            delintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            holder.itemView.getContext().startActivity(delintent);
                            Toast.makeText(holder.itemView.getContext(),"Deleted Succesfully Note.",Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notpedArrayList.size();
    }

    class notpedHolder extends RecyclerView.ViewHolder{

        private RecyclerviewBinding binding;

        public notpedHolder(RecyclerviewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
