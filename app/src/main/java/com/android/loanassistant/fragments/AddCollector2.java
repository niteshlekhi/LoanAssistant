package com.android.loanassistant.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.loanassistant.R;
import com.android.loanassistant.helper.Constants;
import com.android.loanassistant.helper.CustomProgressDialog;
import com.android.loanassistant.interfaces.CallBackInterface;
import com.android.loanassistant.model.Collector;
import com.android.loanassistant.model.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class AddCollector2 extends Fragment {

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @BindView(R.id.ctrAddress)
    EditText txtAddress;

    @BindView(R.id.ctrDp)
    ImageView imgDp;

    @BindView(R.id.ctrDpPreview)
    ImageView imgDpPreview;

    @BindView(R.id.btnPrevPage)
    Button btnPrevPage;

    @BindView(R.id.btnAddCollector)
    Button btnAdd;

    private CallBackInterface callBackInterface;

    private CustomProgressDialog dialog;
    private Bitmap bitmap;

    public AddCollector2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_collector2, container, false);
        getActivity().setTitle(Constants.ADD_COLLECTOR);
        dialog = new CustomProgressDialog(getActivity());
        ButterKnife.bind(this, view);

        if ((savedInstanceState != null) && (savedInstanceState.getString("keyAddress") != null)) {
            String address = savedInstanceState
                    .getString("keyAddress");
            txtAddress.setText(address);
        }

        imgDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        btnPrevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callBackInterface != null)
                    callBackInterface.onPreviousFragment(txtAddress.getText().toString());
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
//                    dialog.showDialog(Constants.ADDING);

                    String dpUrl = uploadImage();

                    Bundle bundle = new Bundle();
                    if (getArguments() != null)
                        bundle = getArguments();

                    final Collector collector = (Collector) bundle.getSerializable("objSave");
                    collector.setAddress(txtAddress.getText().toString());
                    collector.setDpUrl(dpUrl);
                    CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
                    final CollectionReference ref1 = FirebaseFirestore.getInstance().collection("login");
                    final String email = collector.getEmail();

                    ref.document(email).set(collector).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ref1.document(email).set(new Login(email, Constants.default_pwd, Constants.type_c));
                            dialog.stopDialog();
                            Toast.makeText(getActivity(), "Collector Added!", Toast.LENGTH_SHORT).show();
//                            clearData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.stopDialog();
                            Toast.makeText(getActivity(), "Collector NOT Added!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(getActivity(), "Collector Added!", Toast.LENGTH_SHORT).show();

                    /*final Collector collector = new Collector(name, email, phone, aadhar);
                    CollectionReference ref = FirebaseFirestore.getInstance().collection("collector");
                    final CollectionReference ref1 = FirebaseFirestore.getInstance().collection("login");

                    ref.document(email).set(collector).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ref1.document(email).set(new Login(email, Constants.default_pwd, Constants.type_c));
                            dialog.stopDialog();
                            Toast.makeText(getActivity(), "Collector Added!", Toast.LENGTH_SHORT).show();
                            clearData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.stopDialog();
                            Toast.makeText(getActivity(), "Collector NOT Added!", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });

        return view;
    }

    private String uploadImage() {
//        dialog.showDialog("Uploading Data...");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://loanassistant-e7d68.appspot.com/");
        final StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                dialog.showDialog("Uploaded " + (int) progress + "%");
            }
        });

        /*ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.stopDialog();
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("demo", "onSuccess: uri= "+ uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.stopDialog();
                        Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        dialog.showDialog("Uploaded "+(int)progress+"%");
                    }
                });*/

        return String.valueOf(ref.getDownloadUrl());
    }

    private void captureImage() {

        if (checkPermission()) {
            Intent pictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
            );
            if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }

        } else {
            requestPermission();
        }

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String address = txtAddress.getText().toString();
        outState.putString("keyAddress", address);
    }

    public void setCallBack(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }

    private boolean validate() {
        boolean valid = true;
        String address = txtAddress.getText().toString();

        if (address.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields ", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imgDpPreview.setImageBitmap(bitmap);
            }
        }
    }

    /* private void clearData() {
        txtAddress.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAadhar.setText("");
    }*/

}