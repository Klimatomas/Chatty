package klima.tomas.chatandlocation;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		ButterKnife.bind(this);

		mAuth = FirebaseAuth.getInstance();

		mAuthListener = firebaseAuth -> {
			FirebaseUser user = firebaseAuth.getCurrentUser();
			if (user != null) {
				// User is signed in
				Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
			} else {
				// User is signed out
				Timber.d("onAuthStateChanged:signed_out");
			}
			// ...
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

					// If sign in fails, display a message to the user. If sign in succeeds
					// the auth state listener will be notified and logic to handle the
					// signed in user can be handled in the listener.
					if (!task.isSuccessful()) {
						Toast.makeText(LoginScreenActivity.this, R.string.authfailed, Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(LoginScreenActivity.this, R.string.registrationsucces, Toast.LENGTH_SHORT).show();
					}
					// ...
				});
	}

	@OnClick(R.id.loginbutton)
	void login() {
		if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
			Toast.makeText(this, "Invalid name or password", Toast.LENGTH_SHORT).show();
			return;
		}
		progressbar.setVisibility(View.VISIBLE);

		mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
				.addOnCompleteListener(this, task -> {
					Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());

					// If sign in fails, display a message to the user. If sign in succeeds
					// the auth state listener will be notified and logic to handle the
					// signed in user can be handled in the listener.
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
