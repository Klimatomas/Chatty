package klima.tomas.chatandlocation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginScreenActivity extends AppCompatActivity {
	@BindView(R.id.email) EditText email;
	@BindView(R.id.password) EditText password;

	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Timber.plant();
		setContentView(R.layout.activity_login_screen);
		ButterKnife.bind(this);
		Toast.makeText(this, "zacinam", Toast.LENGTH_SHORT).show();

		mAuth = FirebaseAuth.getInstance();

		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					// User is signed in
					Timber.d("onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					// User is signed out
					Timber.d("onAuthStateChanged:signed_out");
				}
				// ...
			}
		};
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
		Timber.d("fgh");

		mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Timber.d("createUserWithEmail:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Toast.makeText(LoginScreenActivity.this, R.string.authfailed, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LoginScreenActivity.this, "registration successful!", Toast.LENGTH_SHORT).show();
						}
						// ...
					}
				});	}

	@OnClick(R.id.loginbutton)
	void login() {
		Timber.d("asd");
		mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Timber.w("signInWithEmail:failed", task.getException());
							Toast.makeText(LoginScreenActivity.this, R.string.authfailed, Toast.LENGTH_SHORT).show();
						}

						// ... 
					}
				});
	}
}
