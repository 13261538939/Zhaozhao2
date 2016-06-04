package com.szl.zhaozhao2.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.zxing.camera.CameraManager;
import com.szl.zhaozhao2.zxing.decoding.CaptureActivityHandler;
import com.szl.zhaozhao2.zxing.decoding.InactivityTimer;
import com.szl.zhaozhao2.zxing.view.ViewfinderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

public class MipCaptureActivity extends BaseFragmentActivity implements SurfaceHolder.Callback {

    private static final String TAG = "MipCaptureActivity";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mip_capture);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

//        Button mButtonBack = (Button) findViewById(R.id.button_back);
//        mButtonBack.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                MipCaptureActivity.this.finish();
//
//            }
//        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * ����ɨ����
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(MipCaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        }else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("result", resultString);
//            bundle.putParcelable("bitmap", barcode);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
            Toast.makeText(MipCaptureActivity.this, "成功"+resultString, Toast.LENGTH_SHORT).show();
            getUserInfo(resultString);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


 public void getUserInfo(String scanResutl){
     JSONObject parameters = new JSONObject();

     try {
         parameters.put("qrCode",scanResutl);
         parameters.put("imsi", DApplication.imei);
     } catch (JSONException e) {
         e.printStackTrace();
     }
     RequestManager.getInstance().getData(Request.Method.POST, parameters, new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject jsonObject) {
             JSONObject meta = jsonObject.optJSONObject("meta");
             String result = meta.optString("s");
             String message = meta.optString("m");
             if(result.equals("1")){

                 JSONObject user = jsonObject.optJSONObject("user");
                 showScanSuccessDialog(user);
             }
             CommonUtil.showToast(MipCaptureActivity.this,
                     message);
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError volleyError) {
             CommonUtil.showToast(MipCaptureActivity.this,
                     getString(R.string.no_connect));
         }
     }, Contants.QRCodeScan,TAG);
 }

    public void showScanSuccessDialog(final JSONObject user){
            dialog = new Dialog(this, R.style.CustomDialog);
            dialog.setContentView(R.layout.dialog_scan);
            dialog.setCanceledOnTouchOutside(false);
            Button cancel_btn = (Button) dialog.findViewById(R.id.btn_cancel);
             TextView name_tv = (TextView) dialog.findViewById(R.id.tv_name);
             TextView sex_tv = (TextView) dialog.findViewById(R.id.tv_sex);
            TextView phone_tv = (TextView) dialog.findViewById(R.id.tv_phone);
            if(user != null){
                name_tv.setText("姓名 ："+user.optString("name"));
                sex_tv.setText("性别 ："+user.optString("sex"));
                phone_tv.setText("手机号 ："+user.optString("phone"));
            }
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            Button ok_btn = (Button) dialog.findViewById(R.id.btn_ok);
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    traineeConfirmInfo(user.optString("aaa"));
                }
            });
            dialog.show();
    }

    public void traineeConfirmInfo(final String aaa){
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("aaa",aaa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestManager.getInstance().getData(Request.Method.POST, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject meta = jsonObject.optJSONObject("meta");
                String result = meta.optString("s");
                String message = meta.optString("m");
                if(result.equals("1")){
                    Intent intent = new Intent(MipCaptureActivity.this,RegisterForTraineeActivity.class);
                    intent.putExtra("ckid",jsonObject.optString("ckId"));
                    intent.putExtra("aaa",aaa);
                    startActivity(intent);
                    finish();
                }
                CommonUtil.showToast(MipCaptureActivity.this,
                        message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(MipCaptureActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.Affirm,TAG);
    }

    }
