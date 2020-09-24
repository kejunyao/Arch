package com.kejunyao.arch.permission;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 权限请求Activity
 *
 * @author kejunyao
 * @since 2020年09月09日
 */
public class PermissionRequestActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 6324290;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionManager.getInstance().doPermissionsRequest(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
