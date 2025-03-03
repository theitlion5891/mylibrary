package com.fantafeat.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fantafeat.Adapter.OffefAdapter;
import com.fantafeat.Model.NewOfferModal;
import com.fantafeat.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class OfferListSheet extends BottomSheetDialogFragment {

    Context mContext;
    private ArrayList<NewOfferModal> offerList;
    BottomSheetDialog bottomSheet;

    public OfferListSheet(Context mContext, ArrayList<NewOfferModal> offerTree) {
        this.mContext = mContext;
        this.offerList = offerTree;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.offer_sheet_dialog, null);
        bottomSheet.setContentView(view);
      //  ((View) view.getParent()).setBackgroundResource(android.R.color.transparent);

        setViewCreated(view);
        return bottomSheet;

    }

    private void setViewCreated(View view) {
        RecyclerView recyclerOffer=view.findViewById(R.id.recyclerOffer);
        ImageView imgClose=view.findViewById(R.id.imgClose);

        OffefAdapter adapter=new OffefAdapter(mContext,offerList);
        recyclerOffer.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        recyclerOffer.setAdapter(adapter);

        imgClose.setOnClickListener(v->{
            if (bottomSheet!=null && bottomSheet.isShowing()){
                bottomSheet.dismiss();
            }
        });

    }

}
