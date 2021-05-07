package com.apptech.myapplication.ui.qr;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.myapplication.MobileNavigationDirections;
import com.apptech.myapplication.databinding.QrFragmentBinding;
import com.apptech.myapplication.fragment.ScannerFragment;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QrFragment extends Fragment {

    private QrViewModel mViewModel;
    QrFragmentBinding binding;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    Vibrator vibrator;
    ScannerFragment.BackPress backPress;
    String LastFragment = "";
    NavController navController;

    public static QrFragment newInstance() {
        return new QrFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = QrFragmentBinding.inflate( inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QrViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
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
                            switch (LastFragment){
                                case "REPORT_SELL_OUT_ENTRIES":
                                    MobileNavigationDirections.ActionGlobalReportSellOutEntriesFragment action = QrFragmentDirections.actionGlobalReportSellOutEntriesFragment();
                                    action.setIMEI(intentData);
                                    navController.navigate(action);
                                    cameraSource.release();
                                    break;
                            }
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
        LastFragment = QrFragmentArgs.fromBundle(getArguments()).getTAG();
        initialiseDetectorsAndSources();
    }


}