package com.tomatoo.MenuActivities.Communications;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Models.HowToUseModel;
import com.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HowToUseActivity extends AppCompatActivity {

    @BindView(R.id.howToUse_txtV_id)
    TextView desc_txtV;

    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable())
            getUsingData();
        else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getUsingData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<HowToUseModel> call = serviceInterface.getHowToUse();
        call.enqueue(new Callback<HowToUseModel>() {
            @Override
            public void onResponse(Call<HowToUseModel> call, Response<HowToUseModel> response) {
                if (response.body().getStatus()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        desc_txtV.setText(Html.fromHtml(response.body().getHow_to_use().getDesc_en(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        desc_txtV.setText(response.body().getHow_to_use().getDesc_ar());
                    }
                }
            }

            @Override
            public void onFailure(Call<HowToUseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.back_img)
    void goBack() {
        finish();
    }
}
