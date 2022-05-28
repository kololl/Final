package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.FoodAdapter.FoodAddapter;
import com.example.afinal.Model.Category;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private TextView name, price;
    private RecyclerView recyclerView;
    private FoodAddapter mfFoodAddapter;
    private List<Category> mlistCate;
    //Them
    private EditText nameAdd, priceAdd;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//Load list
        name= findViewById(R.id.txtname);
        price= findViewById(R.id.txtprice);
        recyclerView= findViewById(R.id.rycyler);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration= new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mlistCate= new ArrayList<>();
        mfFoodAddapter= new FoodAddapter(mlistCate, new FoodAddapter.Iclicklistener() {
            @Override
            public void onclickUpdate(Category category) {
                openDialogUpdateItem(category);
            }

            @Override
            public void onclickDelete(Category category) {
                onClickDelete(category);

            }
        });

        recyclerView.setAdapter(mfFoodAddapter);

       getListCatefromFireBase();

       //Add
        nameAdd= findViewById(R.id.txtNameAdd);
        priceAdd= findViewById(R.id.txtPriceAdd);
        btnAdd= findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= nameAdd.getText().toString().trim();
                String price= priceAdd.getText().toString().trim();
                Category category=new Category(name,price);
                onClickAdd(category);

            }
        });

    }

    private void getListCatefromFireBase(){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Category");


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category= snapshot.getValue(Category.class);
                if (category!=null){
                    mlistCate.add(category);
                    mfFoodAddapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category= snapshot.getValue(Category.class);
                if (mlistCate==null|| mlistCate.isEmpty()){
                    return;
                }
                for (int i=0; i<mlistCate.size(); i++){
                    if (category.getName()==mlistCate.get(i).getName() && category.getPrice()==mlistCate.get(i).getPrice()){
                        mlistCate.set(i, category);
                        break;
                    }
                }
mfFoodAddapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Category category= snapshot.getValue(Category.class);
                if (mlistCate==null|| mlistCate.isEmpty()){
                    return;
                }
                for (int i=0; i<mlistCate.size(); i++){
                    if (category.getName()==mlistCate.get(i).getName() && category.getPrice()==mlistCate.get(i).getPrice()){
                        mlistCate.remove(mlistCate.get(i));
                        break;
                    }
                }
                mfFoodAddapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void onClickAdd(Category category){
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Category");

        String pathObject=String.valueOf(category.getName());
        reference.child(pathObject).setValue(category);

    }
    private void openDialogUpdateItem(Category category){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update);
        Window window=dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText txtName2=dialog.findViewById(R.id.txtNameUpdate);
        EditText txtPrice2=dialog.findViewById(R.id.txtPriceUpdate);
        Button btnUpdate1= dialog.findViewById(R.id.btnUpdate2);
        Button btnCanel= dialog.findViewById(R.id.btnCancel);

        txtName2.setText(category.getName());
        txtPrice2.setText(category.getPrice());
        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference reference= database.getReference("Category");

                String newName= txtName2.getText().toString().trim();
                String newPrice= txtPrice2.getText().toString().trim();

                category.setName(newName);
                category.setPrice(newPrice);
                reference.child(String.valueOf(category.getName())).updateChildren(category.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Home.this, "Update suscess", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();

    }
    private void onClickDelete(Category category){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("ban co chac chan xoa dong nay khong?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database= FirebaseDatabase.getInstance();
                        DatabaseReference reference= database.getReference("Category");

                        reference.child(String.valueOf(category.getName())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(Home.this, "Delete suscess", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).setNegativeButton("Cancel", null).show();


    }
}