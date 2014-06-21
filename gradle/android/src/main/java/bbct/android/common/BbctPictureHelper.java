package bbct.android.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BbctPictureHelper {

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        Environment.getExternalStorageState();
        File storageDir = Environment.getExternalStorageDirectory();
        File picFile = new File(storageDir.getAbsolutePath() + "/DCIM");
        File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            picFile      /* directory */
        );
        return image;
     }
    
    public Bitmap GetScaledImageFromPath(String imagePath, int widthToScale, int heightToScale) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        
        ExifInterface exif = null;
        int orientation = 0;
        try {
            exif = new ExifInterface(imagePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        // Determine how much to scale down the image
        if (widthToScale > 0 && heightToScale > 0) {
            int scaleFactor = Math.max(photoW/widthToScale, photoH/heightToScale);
            if (scaleFactor > 0) {
                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap scaledBitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                if(orientation == 6) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    return rotatedBitmap;
                }
                return scaledBitmap;
            }
        }
        return null;
    }
    
    public Bitmap GetImageFromPath(String imagePath) {
        ExifInterface exif = null;
        int orientation = 0;
        try {
            exif = new ExifInterface(imagePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        // Determine how much to scale down the image
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if(orientation == 6) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return rotatedBitmap;
        }
        return bitmap;
    }
}
