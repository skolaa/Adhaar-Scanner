package dns.adhaarscanner.Screen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import dns.adhaarscanner.R;

/**
 * Created by nayan on 12/31/16.
 */

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    Button scanButton;

    private static final String CAMERA_PERMISSION = "android.permission.CAMERA";
    private static final String FLASH_PERMISSION = "android.permission.FLASHLIGHT";

    private static final int PERMISSION_REQ_CODE = 11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        scanButton = (Button) findViewById(R.id.scan_btn);
        scanButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int permission1 = checkCallingOrSelfPermission(CAMERA_PERMISSION);
        List<String> permissions = new ArrayList<>();

        if (permission1 == PackageManager.PERMISSION_DENIED) {
            permissions.add(CAMERA_PERMISSION);
        }

        if (permissions.isEmpty()) {
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
        } else {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 11);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int res : grantResults) {
            if (res == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }

        onClick(scanButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String contents = data.getStringExtra("SCAN_RESULT");
            Intent intent = new Intent(this, ResultScreen.class);
            intent.putExtra("data", contents);
            startActivity(intent);
        }
    }
}
