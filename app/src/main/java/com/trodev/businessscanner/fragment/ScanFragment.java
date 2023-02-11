package com.trodev.businessscanner.fragment;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import com.trodev.businessscanner.R;
import com.trodev.businessscanner.activity.ImageActivity;

import java.io.IOException;


public class ScanFragment extends Fragment {
    public static final int CAMERA_PERMISSION_CODE = 100;


    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private MaterialCardView copy, share;
    private MaterialButton storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        // all widget working here....
        surfaceView = view.findViewById(R.id.camera);
        textView = view.findViewById(R.id.TV);
        copy = view.findViewById(R.id.copyBtn);
        copy.setVisibility(View.INVISIBLE);
        share = view.findViewById(R.id.share);
        share.setVisibility(View.INVISIBLE);
        storage = view.findViewById(R.id.storage);


        // barcode & camera working on background....
        barcodeDetector = new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector).setRequestedPreviewSize(640, 480).build();

        // Copy Text in Scanning QR.
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyService();
            }
        });

        //Share Copy Text in Scanning QR.
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareService();
            }
        });

        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ImageActivity.class));
            }
        });

        // scanning area working process....
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });


        // barcode detector working process....
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if (qrcode.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(qrcode.valueAt(0).displayValue);
                            copy.setVisibility(View.VISIBLE);
                            share.setVisibility(View.VISIBLE);

                        }
                    });
                }
            }
        });

        return view;

    }


    // copy system working here....
    private void CopyService() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("EditText", textView.getText().toString());
        clipboard.setPrimaryClip(clipData);

        Toast.makeText(getActivity(), "Text Copied Successfully", Toast.LENGTH_SHORT).show();
    }

    // share system working here....
    private void ShareService() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plane");
        share.putExtra(Intent.EXTRA_TEXT, textView.getText());
        startActivity(share);

        Toast.makeText(getActivity(), "Share Your Text Any-Where", Toast.LENGTH_SHORT).show();
    }

}