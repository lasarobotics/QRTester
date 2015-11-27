package com.lasarobotics.qrtester;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;

import java.util.zip.Checksum;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    QRDetector qrd = new QRDetector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void detect(View v) {
        //Detects using ZXing library
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(imageBitmap);
            iv.setMinimumHeight(600);
            try {
                Result r = qrd.detectFromBitmap(imageBitmap);
                writeToTextField("Found!: " + r.getText());
                writeToTextField2("Points: " + java.util.Arrays.toString(r.getResultPoints()) + "\n"
                        + "Timestamp: " + r.getTimestamp() + "\n"
                        + "Orientation (Google): " + r.getResultMetadata().get(ResultMetadataType.ORIENTATION) + "\n"
                        + "Error correction level: " + r.getResultMetadata().get(ResultMetadataType.ERROR_CORRECTION_LEVEL) + "\n"
                        + "Orientation: " + qrd.getOrientationFromResult(r));
                qrd.reset();
            } catch(NotFoundException nfe) {
                writeToTextField("Not found exception: " + nfe.getMessage());
                writeToTextField2("No metadata");
            } catch(FormatException fe) {
                writeToTextField("Format exception: " + fe.getMessage());
                writeToTextField2("No metadata");
            } catch(ChecksumException ce) {
                writeToTextField("Checksum exception: " + ce.getMessage());
                writeToTextField2("No metadata");
            }
        }
    }

    private void writeToTextField(String text) {
        TextView tv = (TextView) findViewById(R.id.textview);
        tv.setText(text);
    }

    private void writeToTextField2(String text) {
        TextView tv = (TextView) findViewById(R.id.textview2);
        tv.setText(text);
    }
}
