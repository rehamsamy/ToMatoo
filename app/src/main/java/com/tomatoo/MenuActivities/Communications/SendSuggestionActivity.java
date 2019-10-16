package com.tomatoo.MenuActivities.Communications;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.SendSuggestionModel;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendSuggestionActivity extends AppCompatActivity {

    @BindView(R.id.send_suggestion_btn_id)
    Button send_suggestion_btn;
    @BindView(R.id.suggestion_ed_id)
    EditText suggestion_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_suggestion);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.send_suggestion_back_img)
    void goBack() {
        finish();
    }

    @OnClick(R.id.send_suggestion_btn_id)
    void setSend_suggestion_btn() {

        if (TextUtils.isEmpty(suggestion_ed.getText().toString())) {
            suggestion_ed.setError(getString(R.string.required));
            suggestion_ed.requestFocus();
            return;
        }

        if (networkAvailable.isNetworkAvailable()) {
            final ProgressDialog dialog = dialogUtil.showProgressDialog(SendSuggestionActivity.this, getString(R.string.sending), false);
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<SendSuggestionModel> call = serviceInterface.sendSuggestion(MainPageActivity.user_Id, MainPageActivity.userData.getToken(), suggestion_ed.getText().toString());
            call.enqueue(new Callback<SendSuggestionModel>() {
                @Override
                public void onResponse(Call<SendSuggestionModel> call, Response<SendSuggestionModel> response) {
                    dialog.dismiss();
                    if (response.body().getStatus()) {
                        Toast.makeText(SendSuggestionActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SendSuggestionActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
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
