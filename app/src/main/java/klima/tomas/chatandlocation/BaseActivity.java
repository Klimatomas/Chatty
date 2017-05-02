package klima.tomas.chatandlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import klima.tomas.chatandlocation.Services.LocationService;


public abstract class BaseActivity extends AppCompatActivity {


//	@Nullable @BindView(R.id.toolbar) Toolbar toolbar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		Intent mServiceIntent = new Intent(this, LocationService.class);
		startService(mServiceIntent);

		super.onCreate(savedInstanceState);
		int activityLayout = getActivityLayout();
		if (activityLayout != 0) {
			setContentView(activityLayout);
			ButterKnife.bind(this);

		}

	}


	public abstract @LayoutRes int getActivityLayout();

}
