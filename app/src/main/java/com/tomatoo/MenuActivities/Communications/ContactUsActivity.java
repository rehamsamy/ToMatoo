package com.tomatoo.MenuActivities.Communications;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Models.SocialMediaModel;
import com.tomatoo.R;
import com.tomatoo.utils.DialogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.phone_value_txtV)
    TextView phone_value_txtV;
    @BindView(R.id.address_value_txtV)
    TextView address_value_txtV;
    @BindView(R.id.website_value_txtV)
    TextView website_value_txtV;
    @BindView(R.id.email_value_txtV)
    TextView email_value_txtV;
    @BindView(R.id.send_suggestion_btn)
    MaterialButton sendSuggestion;
    @BindView(R.id.report_problem_btn)
    MaterialButton reportProblem;

    private DialogUtil dialogUtil;
    private NetworkAvailable networkAvailable;
    private List<SocialMediaModel.MediaData> mediaData;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        dialogUtil = new DialogUtil();
        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable()) {
            getMediaContacts();
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getMediaContacts() {
        final ProgressDialog dialog = dialogUtil.showProgressDialog(ContactUsActivity.this, getString(R.string.loading), false);

        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<SocialMediaModel> call = serviceInterface.socialContacts();
        call.enqueue(new Callback<SocialMediaModel>() {
            @Override
            public void onResponse(Call<SocialMediaModel> call, Response<SocialMediaModel> response) {
                Log.i(TAG, "onResponse " + response.body().getStatus());
                if (response.body().getStatus()) {
                    mediaData = response.body().getSocialmedia();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<SocialMediaModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.back_img)
    void goBack() {
        finish();
    }

    @OnClick(R.id.share_img)
    void shareApp() {
        int applicationNameId = this.getApplicationInfo().labelRes;
        final String appPackageName = this.getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
        String text = "Install this cool application: ";
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
        startActivity(Intent.createChooser(i, "Share link:"));
    }

    @OnClick(R.id.send_suggestion_btn)
    void setSendSuggestion() {
        startActivity(new Intent(getApplicationContext(), SendSuggestionActivity.class));
    }

    @OnClick(R.id.report_problem_btn)
    void setReportProblem() {
        startActivity(new Intent(getApplicationContext(), ReportProblemActivity.class));
    }

    @OnClick(R.id.facebook_icon)
    void faceIconClicked() {
        if (mediaData.get(0).getName().equals("facebookpage")) {
            openWebPage(this, mediaData.get(0).getLink());
        }
    }

    @OnClick(R.id.twitter_icon)
    void twitterIconClicked() {
        if (mediaData.get(4).getName().equals("twitterpage")) {
            openWebPage(this, mediaData.get(4).getLink());
        }
    }

    @OnClick(R.id.instagram_icon)
    void instagramIconClicked() {
        if (mediaData.get(2).getName().equals("instagrampage")) {
            openWebPage(this, mediaData.get(2).getLink());
        }
    }

    public static void openWebPage(Context context, String url) {
        try {
            if (!URLUtil.isValidUrl(url)) {
                Toast.makeText(context, R.string.not_valid_link, Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.not_browser_toOpen_page, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
