package com.example.khaled_sa2r.tomatoo.MenuActivities.Communications;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.send_suggestion_btn) MaterialButton sendSuggestion;
    @BindView(R.id.report_problem_btn) MaterialButton reportProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.send_suggestion_btn)
    void setSendSuggestion(){
        startActivity(new Intent(getApplicationContext(),SendSuggestionActivity.class));
    }

    @OnClick(R.id.report_problem_btn)
    void setReportProblem(){
        startActivity(new Intent(getApplicationContext(),ReportProblemActivity.class));
    }
}
