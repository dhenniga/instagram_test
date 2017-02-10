package com.tag.instagramtest;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.tag.instagramtest.InstagramApp.OAuthAuthenticationListener;


public class LoginActivity extends Activity implements OnClickListener {

	private InstagramApp mApp;
	private Button btnConnect;

	private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == InstagramApp.WHAT_FINALIZE) {
				userInfoHashmap = mApp.getUserInfo();
			} else if (msg.what == InstagramApp.WHAT_FINALIZE) {
				Toast.makeText(LoginActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(this);

		mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

		mApp.setListener(new OAuthAuthenticationListener() {

			@Override
			public void onSuccess() {

				btnConnect.setText("Disconnect");
				Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
				mApp.fetchUserName(handler);
				Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
				startActivity(intent);

			}

			@Override
			public void onFail(String error) {

				Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();

			}
		});



		if (mApp.hasAccessToken()) {
			btnConnect.setText("LOGOUT");
			mApp.fetchUserName(handler);
			Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
			startActivity(intent);

		}

	}



	/**
	 *
	 * @param v
     */
	@Override
	public void onClick(View v) {

		if (v == btnConnect) {

			connectOrDisconnectUser();

		}
	}



	/**
	 *   Check the connection status and react accordingly
	 */
	private void connectOrDisconnectUser() {

		if (mApp.hasAccessToken()) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
			builder.setMessage("Disconnect from Instagram?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int id) {
									mApp.resetAccessToken();
									btnConnect.setText("LOGIN");
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			final AlertDialog alert = builder.create();
			alert.show();

		} else {

			mApp.authorize();

		}
	}
}