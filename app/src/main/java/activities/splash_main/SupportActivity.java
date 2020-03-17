package activities.splash_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_support_activity);
        Toolbar tb = findViewById(R.id.supportTB);
        setSupportActionBar(tb);
        setTitle(R.string.supportLbl);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToMainActivity());
    }

    public void backToMainActivity() {
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
        finish();
    }

    public void displayProjReflection(View view) {
        Intent toReflection = new Intent(this, ReflectionActivity.class);
        startActivity(toReflection);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }
}
