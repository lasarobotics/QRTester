package com.lasarobotics.qrtester;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Uses Google's ZXing library to detect QR codes
 */
public class QRDetector {
    QRCodeReader qrc;

    private enum Orientation {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public QRDetector() {
        qrc = new QRCodeReader();
    }

    public Result detectFromBitmap(Bitmap bMap) throws FormatException, ChecksumException, NotFoundException {
        //Convert Bitmap into BinaryBitmap
        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //Copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(),intArray);

        //Send BinaryBitmap to other method
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return detectFromBinaryBitmap(bitmap);
    }

    public Result detectFromBinaryBitmap(BinaryBitmap map) throws NotFoundException, ChecksumException, FormatException {
        //Read QR data from BinaryBitmap
        return qrc.decode(map);
    }

    /* Test data:
     * Up:    (77.5, 98.0)  (77.5, 33.5)  (143.5, 34.5)
     * Down:  (143.0, 32.0) (144.0, 94.0) (82.5, 94.5)
     * Left:  (142.5, 99.0) (75.5, 98.5)  (76.5, 31.5)
     * Right: (85.0, 32.0)  (148.0, 33.0) (146.5, 95.0)
     */
    public static Orientation getOrientation(ResultPoint[] points) {
        if(points.length != 3) {
            throw new RuntimeException("Wrong number of points");
        }

        //Determine which two points are closest
        float xDifferences[] = new float[2];
    }

    public void reset() {
        qrc.reset();
    }
}
