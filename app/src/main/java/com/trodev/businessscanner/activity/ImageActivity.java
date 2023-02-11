package com.trodev.businessscanner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.zxing.Reader;
import com.trodev.businessscanner.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {

    private MaterialButton storage;
    private TextView TV;
    private MaterialCardView copy, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        // Title generator
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Scan QR");


        storage = findViewById(R.id.storage);
        TV = findViewById(R.id.TV);
        copy = findViewById(R.id.copyBtn);
        copy.setVisibility(View.INVISIBLE);
        share = findViewById(R.id.share);
        share.setVisibility(View.INVISIBLE);

        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phototogallery = new Intent(Intent.ACTION_PICK);
                phototogallery.setType("image/*");
                startActivityForResult(phototogallery, 1000);
            }
        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyService();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareService();
            }
        });
    }


    @Override

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            try {

                final Uri imageUri = data.getData();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                try {

                    Bitmap bMap = selectedImage;

                    String contents = null;


                    int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];

                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());


                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);

                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));


                    Reader reader = new MultiFormatReader();

                    Result result = reader.decode(bitmap);

                    contents = result.getText();

                    TV.setText(contents);
                    copy.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    // Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_LONG).show();

                } catch (Exception e) {

                    e.printStackTrace();

                }

                //  image_view.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {

                e.printStackTrace();

                Toast.makeText(ImageActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }


        } else {

            Toast.makeText(ImageActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();

        }

    }

    // copy system working here....
    private void CopyService() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("EditText", TV.getText().toString());
        clipboard.setPrimaryClip(clipData);

        Toast.makeText(ImageActivity.this, "Text Copied Successfully", Toast.LENGTH_SHORT).show();
    }

    // share system working here....
    private void ShareService() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plane");
        share.putExtra(Intent.EXTRA_TEXT, TV.getText());
        startActivity(share);

        Toast.makeText(ImageActivity.this, "Share Your Text Any-Where", Toast.LENGTH_SHORT).show();
    }
}