package com.fantafeat.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Model.PromoteChanelItem;
import com.fantafeat.R;

import java.util.ArrayList;

public class PromoteFieldAdapter extends RecyclerView.Adapter<PromoteFieldAdapter.FieldHolder>  {

    private Context context;
    private ArrayList<PromoteChanelItem> list;
    private onRemove listner;
    private String[] typeArr = {"Select Chanel Type","Facebook","Youtube","Twitter","Instagram","Telegram","Other"};

    public PromoteFieldAdapter(Context context, ArrayList<PromoteChanelItem> list, onRemove listner) {
        this.context = context;
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public FieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FieldHolder(LayoutInflater.from(context).inflate(R.layout.promote_chanel_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FieldHolder holder, int position) {
        PromoteChanelItem item=list.get(position);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(context,R.layout.spinner_text,typeArr);
        holder.spinChannelType.setAdapter(adapter);

        if (position==0){
            holder.imgClose.setVisibility(View.GONE);
        }else {
            holder.imgClose.setVisibility(View.VISIBLE);
        }

        for (int i=0;i<typeArr.length;i++){
            if (!TextUtils.isEmpty(item.getCh_type()) && typeArr[i].equals(item.getCh_type())){
                holder.spinChannelType.setSelection(i);
            }
        }

        holder.spinChannelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                item.setCh_type(holder.spinChannelType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.edtChName.setText(item.getCh_name());
        holder.edtChUrl.setText(item.getCh_url());

        holder.edtChName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                item.setCh_name(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.edtChUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                item.setCh_url(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.imgClose.setOnClickListener(view->{
            listner.onClose(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class FieldHolder extends RecyclerView.ViewHolder{

        public ImageView imgClose;
        public Spinner spinChannelType;
        public EditText edtChName,edtChUrl;

        public FieldHolder(@NonNull View itemView) {
            super(itemView);
            imgClose=itemView.findViewById(R.id.imgClose);
             spinChannelType=itemView.findViewById(R.id.spinChannelType);
             edtChName=itemView.findViewById(R.id.edtChName);
            edtChUrl=itemView.findViewById(R.id.edtChUrl);

        }
    }

    public interface onRemove{
        public void onClose(int position);
    }

}
