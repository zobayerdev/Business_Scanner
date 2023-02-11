package com.trodev.businessscanner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.businessscanner.R;

public class StudentActivity extends AppCompatActivity {

    private Button generate, download;
    private ImageView imageView;
    private EditText nameET,facebook, linkedin, instagram,twitter, github,university ;

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // Title generator
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student QR Generator");

        nameET = findViewById(R.id.nameET);
        facebook = findViewById(R.id.facebookET);
        linkedin = findViewById(R.id.linkedinET);
        instagram = findViewById(R.id.instagramET);
        twitter = findViewById(R.id.twitterET);
        github = findViewById(R.id.githubET);
        university = findViewById(R.id.universityET);

        generate = findViewById(R.id.Generate);
        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameET.getText().toString().trim().length()
                        + facebook.getText().toString().trim().length()
                        + linkedin.getText().toString().length()
                        + instagram.getText().toString().length()
                        + twitter.getText().toString().length()
                        + github.getText().toString().length()
                        + university.getText().toString().length() == 0)
                {
                    Toast.makeText(StudentActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("Name: "+nameET.getText().toString()
                                + "\nFacebook: " + facebook.getText().toString()
                                + "\nLinkedin: " + linkedin.getText().toString().trim()
                                + "\nInstagram: " + instagram.getText().toString().trim()
                                + "\nTwitter: " + twitter.getText().toString().trim()
                                + "\nGitHub: " + github.getText().toString().trim()
                                + "\nUniversity: " + university.getText().toString().trim()
                                + "\n\nMake by Business Scanner");
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        // Download System Working Here....
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Student_Identity"
                                        , null);
                                Toast.makeText(StudentActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}