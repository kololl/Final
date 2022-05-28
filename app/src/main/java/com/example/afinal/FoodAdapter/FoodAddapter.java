package com.example.afinal.FoodAdapter;

import android.nfc.cardemulation.CardEmulation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.Model.Category;
import com.example.afinal.R;

import java.util.List;

public class FoodAddapter extends RecyclerView.Adapter<FoodAddapter.FoodViewHoder>{
    private List<Category> mlistCate;
    //Update
    private Iclicklistener mIclicklistener;


    public interface Iclicklistener{
        void onclickUpdate(Category category);
        void onclickDelete(Category category);
    }

    public FoodAddapter(List<Category> mlistCate, Iclicklistener listener) {
        this.mlistCate = mlistCate;
        this.mIclicklistener = listener;
    }

    @NonNull
    @Override
    public FoodViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new FoodAddapter.FoodViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHoder holder, int position) {
        Category category=mlistCate.get(position);
        if (category==null){
            return;
        }
        holder.name.setText("name:"+ category.getName());
        holder.price.setText("price:"+ category.getPrice());

        //Update
        holder.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIclicklistener.onclickUpdate(category);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIclicklistener.onclickDelete(category);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mlistCate!=null){
            return mlistCate.size();
        }
        return 0;
    }


    public class FoodViewHoder extends RecyclerView.ViewHolder{
        private TextView name, price;
        //update
        private Button btnupdate;
        //delete
        private  Button btnDelete;


        public FoodViewHoder(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.txtname);
            price=(TextView) itemView.findViewById(R.id.txtprice);
////update
            btnupdate= itemView.findViewById(R.id.btnUpdate);
///Delete
            btnDelete= itemView.findViewById(R.id.btnDelete);
        }
    }
}
