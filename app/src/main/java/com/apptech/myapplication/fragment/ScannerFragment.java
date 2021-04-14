package com.apptech.myapplication.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.apptech.myapplication.databinding.FragmentScannerBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class ScannerFragment extends Fragment {


    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    Vibrator vibrator;
    FragmentScannerBinding binding;
    BackPress backPress;

    public ScannerFragment(BackPress backPress) {
        this.backPress = backPress;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentScannerBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    private void initialiseDetectorsAndSources() {

//        Toast.makeText(requireContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(requireContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(requireContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        binding.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(binding.surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(requireContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    binding.txtBarcodeValue.post(() -> {
                        if (barcodes.valueAt(0).email != null) {
                            binding.txtBarcodeValue.removeCallbacks(null);
                            intentData = barcodes.valueAt(0).email.address;
                            binding.txtBarcodeValue.setText(intentData);
//                                backPress.Onbackpress(intentData);
                        } else {
                            intentData = barcodes.valueAt(0).displayValue;
                            binding.txtBarcodeValue.setText(intentData);
                            backPress.Onbackpress(intentData);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();

    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }


    public interface BackPress {
        void Onbackpress(String imei);
    }

}





























