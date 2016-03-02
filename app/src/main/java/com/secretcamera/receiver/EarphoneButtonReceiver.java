package com.secretcamera.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import com.secretcamera.service.CameraService;

/**
 * Created by Khatri on 2/3/2016.
 */
public class EarphoneButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            return;
        }

        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }

        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            // Start service to capture image
            Intent cameraService = new Intent(context.getApplicationContext(), CameraService.class);
            context.startService(cameraService);
        }

        abortBroadcast();
    }
}
