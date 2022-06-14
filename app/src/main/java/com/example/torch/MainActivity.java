package com.example.torch;

import static com.example.torch.R.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    SwitchCompat switchCompat ;
    AppCompatButton lightButton;
    AppCompatImageView lightVisible;
    Boolean switchState = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                runFlashLight();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void runFlashLight() {
        lightButton = (AppCompatButton) findViewById(id.light_button);
        lightVisible = (AppCompatImageView) findViewById(id.light);
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!switchState){
                    try{
                        String cameraID = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraID,true);
                        switchState = true;
                        lightButton.setBackground(getDrawable(drawable.rounded_button));
                        lightVisible.setVisibility(View.VISIBLE);
                    }catch (CameraAccessException e)
                    {}

                }else{
                    try{
                        String cameraID = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraID,false);
                        switchState = false;
                        lightButton.setBackground(getDrawable(drawable.rounded_button_off));
                        lightVisible.setVisibility(View.INVISIBLE);

                    }catch (CameraAccessException e){}
                }

            }

        });

    }
}