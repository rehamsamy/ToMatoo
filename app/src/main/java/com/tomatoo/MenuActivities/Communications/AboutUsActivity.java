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
import com.tomatoo.Models.AboutResponseModel;
import com.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.about_txtV_id)
    TextView about_txtV;

    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable())
            getAboutData();
        else
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
    }

    private void getAboutData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AboutResponseModel> call = serviceInterface.getAbout();
        call.enqueue(new Callback<AboutResponseModel>() {
            @Override
            public void onResponse(Call<AboutResponseModel> call, Response<AboutResponseModel> response) {
                if (response.body().getStatus()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        about_txtV.setText(Html.fromHtml(response.body().getPage(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        about_txtV.setText(response.body().getPage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AboutResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.aboutUs_back_txtV_id)
    void goBack() {
        finish();
    }
}
