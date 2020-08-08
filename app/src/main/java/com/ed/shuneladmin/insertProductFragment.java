package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yalantis.ucrop.UCrop;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class insertProductFragment extends Fragment {
    private final static String TAG = "insertProductFragment";
    Activity activity;
    EditText nameOfProduct,colorOfProduct,priceOfProduct,detailOfProduct,categoryOfProduct,statusOfProduct;
    Product product;
    Button btaddproduct;
    Common common;
    CommonTask insertProduct;
    ImageView ivinsertbuttom,ivshowpicture;
    private byte[] image;
    private Uri contentUri;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private AlertDialog dialog;



    public insertProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameOfProduct = view.findViewById(R.id.nameOfProduct);
        colorOfProduct = view.findViewById(R.id.colorOfProduct);
        priceOfProduct = view.findViewById(R.id.priceOfProduct);
        detailOfProduct = view.findViewById(R.id.detailOfProduct);
        categoryOfProduct = view.findViewById(R.id.categoryOfProduct);
        statusOfProduct = view.findViewById(R.id.statusOfProduct);
        btaddproduct = view.findViewById(R.id.btaddproduct);
        ivinsertbuttom = view.findViewById(R.id.ivinsertbuttom);
        ivshowpicture = view.findViewById(R.id.ivshowpicture);





        ivinsertbuttom.setOnClickListener(new View.OnClickListener() {  //按下拍照的圖示後會跳出dialog
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                v = LayoutInflater.from(activity).inflate(R.layout.dialog_insertpicture,null);
                alertDialog.setView(v);

                dialog = alertDialog.create();
                dialog.show();


                TextView ivtakepicture,tvalbum;
                ivtakepicture = v.findViewById(R.id.ivtakepicture);
                tvalbum = v.findViewById(R.id.tvalbum);
                ivtakepicture.setOnClickListener(new View.OnClickListener() { //拍照
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定存檔路徑
                        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        file = new File(file, "picture.jpg");
                        contentUri = FileProvider.getUriForFile(
                                activity, activity.getPackageName() + ".provider", file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                        if (intent.resolveActivity(activity.getPackageManager()) != null) {
                            startActivityForResult(intent, REQ_TAKE_PICTURE);
                            dialog.dismiss();
                        } else {
                            Common.showToast(activity, R.string.textNoCameraApp);
                        }
                    }
                });

                tvalbum.setOnClickListener(new View.OnClickListener() { //從相簿取照片
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQ_PICK_IMAGE);
                        dialog.dismiss();
                    }
                });


            }
        });





        btaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.networkConnected(activity)){
                    product = new Product();
                    product.setProduct_Name(nameOfProduct.getText().toString());
                    product.setProduct_Color(colorOfProduct.getText().toString());
                    product.setProduct_Price(Integer.parseInt(priceOfProduct.getText().toString()));
                    product.setProduct_Ditail(detailOfProduct.getText().toString());
                    product.setProduct_Category_ID(Integer.parseInt(categoryOfProduct.getText().toString()));
                    product.setProduct_Status(Integer.parseInt(statusOfProduct.getText().toString()));




                    String url = Common.URL_SERVER + "Prouct_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "insertProduct");
                    jsonObject.addProperty("product",new Gson().toJson(product));
                    if (image != null) {
                        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
                    }

                    insertProduct = new CommonTask(url, jsonObject.toString());

                    try {
                        String rp =  insertProduct.execute().get();
                        int count = Integer.parseInt(rp);

                        if (count == 1) {

                            Toast.makeText(activity, R.string.insertsuccess, Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(activity, R.string.insertfail, Toast.LENGTH_SHORT).show();
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }






            }
        });





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_IMAGE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    handleCropResult(intent);
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .start(activity, this, REQ_CROP_PICTURE);
    }

    private void handleCropResult(Intent intent) {
        Uri resultUri = UCrop.getOutput(intent);
        if (resultUri == null) {
            return;
        }
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                bitmap = BitmapFactory.decodeStream(
                        activity.getContentResolver().openInputStream(resultUri));
            } else {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivshowpicture.setImageBitmap(bitmap);
        } else {
            ivshowpicture.setImageResource(R.drawable.no_image);
        }
    }






}
