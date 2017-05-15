package klima.tomas.chatandlocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginScreenActivity extends BaseActivity {
	@BindView(R.id.email) EditText email;
	@BindView(R.id.password) EditText password;
	@BindView(R.id.progressbar) FrameLayout progressbar;

	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;

	SharedPreferences sharedPreferences;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String Email = "emailKey";
	public static final String Password = "passwordKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		ButterKnife.bind(this);
		sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		email.setText(sharedPreferences.getString(Email, Email));
		password.setText(sharedPreferences.getString(Password, Password));



		mAuth = FirebaseAuth.getInstance();

		mAuthListener = firebaseAuth -> {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null) {
				Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
			} else {
				Timber.d("onAuthStateChanged:signed_out");
			}
		};
	}

	@Override public int getActivityLayout() {
		return R.layout.activity_login_screen;
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}


	@OnClick(R.id.registerbutton)
	void register() {
		mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
				.addOnCompleteListener(this, task -> {
					Timber.d("createUserWithEmail:onComplete:" + task.isSuccessful());
					if (!task.isSuccessful()) {
						Toast.makeText(LoginScreenActivity.this, R.string.authfailed, Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(LoginScreenActivity.this, R.string.registrationsucces, Toast.LENGTH_SHORT).show();
					}
				});
	}

	@OnClick(R.id.loginbutton)
	void login() {
		if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
			Toast.makeText(this, "Invalid name or password", Toast.LENGTH_SHORT).show();
			return;
		}
		progressbar.setVisibility(View.VISIBLE);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Email, email.getText().toString()).apply();
		editor.putString(Password, password.getText().toString()).apply();

		mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
				.addOnCompleteListener(this, task -> {
					Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());

					if (!task.isSuccessful()) {
						progressbar.setVisibility(View.GONE);

						Timber.d(task.getException(), "signInWithEmail:failed");
						Toast.makeText(LoginScreenActivity.this, R.string.loginfailed, Toast.LENGTH_SHORT).show();

					}
					else {
						startActivity(MapsActivity.getStartIntent(this));
						finish();
					}
				});
	}



}
