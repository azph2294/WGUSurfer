package activities.splash_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dev.alvatech.c196_wgusurfer_ericbsmith.R;

public class ReflectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reflection_layout);
        Toolbar tb = findViewById(R.id.reflectTB);
        setSupportActionBar(tb);
        setTitle(R.string.reflect_lbl);
        tb.setNavigationIcon(R.drawable.back_arrow_tb);
        tb.setNavigationOnClickListener(v -> backToSupportActivity());

        TextView link = findViewById(R.id.sources_txt);
        link.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void backToSupportActivity() {
        Intent toSupport = new Intent(this, SupportActivity.class);
        startActivity(toSupport);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.home_tb) {
            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backToSupportActivity();
    }
}
