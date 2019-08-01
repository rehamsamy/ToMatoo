package com.example.khaled_sa2r.tomatoo.MyBase;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

public class MyBaseFragment extends Fragment{
    protected MyBaseActivity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(MyBaseActivity) context;

    }
    public MaterialDialog ShowMessage(String title, String content){
        return activity.ShowMessage(title,content);
    }

    public MaterialDialog ShowProgressBar(){
        return activity.ShowProgressBar();
    }

    public void HideProgressBar(){
        activity.HideProgressBar();
    }

    public MaterialDialog ShowConfirmationDialog(String title, String content, String pos, String neg, MaterialDialog.SingleButtonCallback posCallback, MaterialDialog.SingleButtonCallback negCallback){

        return activity.ShowConfirmationDialog(title,content,pos,neg,posCallback,negCallback);
    }
}
