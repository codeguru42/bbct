/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.premium.activity;

import android.support.v7.app.ActionBar;

import android.support.v7.app.ActionBarActivity;
import bbct.android.common.BbctPictureHelper;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.IOException;

import android.view.MenuItem;
import android.view.Menu;
import android.annotation.SuppressLint;
import android.os.Build;

import android.graphics.BitmapFactory;

import android.graphics.Bitmap;
import android.view.ViewTreeObserver;

import android.widget.ImageView;

import bbct.android.premium.R;

import android.os.Bundle;

import android.app.Activity;

public class ChangeCardImage extends ActionBarActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_picture_image);
        image_path = this.getIntent().getStringExtra(this.getString(R.string.image_path_extra));

        card_Image = (ImageView)findViewById(R.id.card_image_view);
                
        listenerImage = new ViewTreeObserver.OnGlobalLayoutListener(){
            
            @Override
            public void onGlobalLayout() {
                if (image_path != "") {
                    Bitmap cardImageBmp = BitmapFactory.decodeFile(image_path);
                    card_Image.setImageBitmap(cardImageBmp);
                }
                removeGlobalLayoutListener(card_Image.getViewTreeObserver(), listenerImage);
            }
        };
        this.card_Image.getViewTreeObserver().addOnGlobalLayoutListener(listenerImage);
        resultIntent = new Intent();
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @SuppressLint("NewApi")
    public void removeGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeGlobalOnLayoutListener(listener);
        }
        else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }
    
    /**
     * Create the options menu. This is simply inflated from the
     * {@code change_picture_option.xml} resource file.
     *
     * @param The
     *            The options menu in which new menu items are placed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.change_picture_option, menu);

        return super.onCreateOptionsMenu(menu);
    }
    
    /*
     * A new file is created for the new picture and the camera intent
     * is initiated.
     */
    private void ChangePicture() {
        BbctPictureHelper picHelper = new BbctPictureHelper();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
         // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = picHelper.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "Take Picture IO Exception: " + ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                image_path = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 
                        REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    
    /**
     * Respond to the user selecting a menu item.
     * If change picture is selected, a new camera intent is
     * initiated
     * If remove picture is selected, the image is reset to null
     * and the activity is closed.
     * If home button is selected, the activity is closed. 
     * @param item
     *            The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.change_picture_menu) {
            ChangePicture();
            resultIntent.putExtra(getString(R.string.image_path_extra), image_path);
            setResult(Activity.RESULT_OK, resultIntent);
            return true;
        } else if (itemId == R.id.remove_picture_menu) {
            image_path = "";
            resultIntent.putExtra(getString(R.string.image_path_extra), image_path);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Respond to the result of a child activity by setting the 
     * path to the image and the imageView.
     *
     * @param requestCode
     *            The integer request code originally supplied to
     *            {@link #startActivityForResult()}, allowing you to identify
     *            who this result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its {@link #setResult()}.
     * @param data
     *            An Intent with the data returned by the child activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BbctPictureHelper pictureHelper = new BbctPictureHelper();
            Bitmap cardImageBmp = pictureHelper.GetImageFromPath(image_path);
            card_Image.setImageBitmap(cardImageBmp);
        }
    }

    private Intent resultIntent = null;
    private String image_path = "";
    private ImageView card_Image = null;
    private ViewTreeObserver.OnGlobalLayoutListener listenerImage = null;
    private static final String TAG = ChangeCardImage.class.getName();
}
