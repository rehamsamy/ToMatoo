package com.tomatoo.MenuActivities.Communications;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.SendSuggestionModel;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportProblemActivity extends AppCompatActivity {

    @BindView(R.id.reportProblem_ed_id)
    EditText reportProblem_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.reportProblem_back_img)
    void goBack() {
        finish();
    }

    @OnClick(R.id.report_problem_btn)
    void sendReport() {
        if (TextUtils.isEmpty(reportProblem_ed.getText().toString())) {
            reportProblem_ed.setError(getString(R.string.required));
            reportProblem_ed.requestFocus();
            return;
        }

        if (networkAvailable.isNetworkAvailable()) {
            final ProgressDialog dialog = dialogUtil.showProgressDialog(ReportProblemActivity.this, getString(R.string.sending), false);
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<SendSuggestionModel> call = serviceInterface.reportAProblem(MainPageActivity.user_Id, MainPageActivity.userData.getToken(), reportProblem_ed.getText().toString());
            call.enqueue(new Callback<SendSuggestionModel>() {
                @Override
                public void onResponse(Call<SendSuggestionModel> call, Response<SendSuggestionModel> response) {
                    dialog.dismiss();
                    if (response.body().getStatus()) {
                        Toast.makeText(ReportProblemActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReportProblemActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SendSuggestionModel> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                }
            });
        }
    }
}
