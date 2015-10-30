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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.data.BaseballCard;
import android.view.View;
import bbct.android.common.R;
import bbct.android.common.BbctPictureHelper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;

import android.os.Bundle;

public class PremiumCardDetails
        extends bbct.android.common.activity.BaseballCardDetails {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public static PremiumCardDetails getInstance(long id, BaseballCard card) {
        Bundle args = new Bundle();
        args.putLong(ID, id);
        args.putSerializable(CARD, card);

        PremiumCardDetails details = new PremiumCardDetails();
        details.setArguments(args);

        return details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.imageCardDetailsFront = (ImageView) view.findViewById(R.id.image_card_details_front);
        this.imageCardDetailsBack = (ImageView) view.findViewById(R.id.image_card_details_back);
        this.imageCardDetailsFront.setOnClickListener(this.onImageCardDetailsFrontClick);
        this.imageCardDetailsBack.setOnClickListener(this.onImageCardDetailsBackClick);

        Bundle args = this.getArguments();
        if (args != null) {
            long id = args.getLong(ID);
            BaseballCard card = (BaseballCard) args.getSerializable(CARD);
            //this.setCard(id, card);
            this.mCurrentFrontPhotoPath = card.getPathToPictureFront();
            this.mCurrentBackPhotoPath = card.getPathToPictureBack();
            listenerFront = new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    int width = 120;//imageCardDetailsFront.getWidth();
                    int height = 120;//imageCardDetailsFront.getHeight();
                    if (mCurrentFrontPhotoPath != "") {
                        Bitmap frontImage = BbctPictureHelper.GetScaledImageFromPath(mCurrentFrontPhotoPath, width, height);
                        imageCardDetailsFront.setImageBitmap(frontImage);
                    }
                    removeGlobalLayoutListener(imageCardDetailsFront.getViewTreeObserver(), this);
                }
            };
            this.imageCardDetailsFront.getViewTreeObserver().addOnGlobalLayoutListener(listenerFront);

            listenerBack = new ViewTreeObserver.OnGlobalLayoutListener(){

                @Override
                public void onGlobalLayout() {
                    int width = 120;//imageCardDetailsBack.getWidth();
                    int height = 120;//imageCardDetailsBack.getHeight();
                    if (mCurrentBackPhotoPath != "") {
                        Bitmap backImage = BbctPictureHelper.GetScaledImageFromPath(mCurrentBackPhotoPath, width, height);
                        imageCardDetailsBack.setImageBitmap(backImage);
                    }
                    removeGlobalLayoutListener(imageCardDetailsBack.getViewTreeObserver(), listenerBack);
                }
            };
            this.imageCardDetailsBack.getViewTreeObserver().addOnGlobalLayoutListener(listenerBack);
        }

        return view;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.save_menu) {
            this.onSave();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
    * Initiates the ChangeCardImage activity and sets the
    * path of the image.
    *
    * @param frontPicture
    *            true if the picture to be changed is the front picture,
    *            false if it is back picture.
    */
    private void ChangePicture(boolean frontPicture) {
        isFrontPicture = frontPicture;
        BaseballCard card = getBaseballCard();
        ((PremiumActivity)this.getActivity()).onChangeCardSelected(frontPicture, card);

    }

     /**
     * Initiates the camera activity and sets the
     * path of the image.
     *
     * @param frontPicture
     *            true if the picture to be changed is the front picture,
     *            false if it is back picture.
     */
    private void TakePicture(boolean frontPicture) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
         // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = BbctPictureHelper.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "Take Picture IO Exception: " + ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                if( frontPicture == true ) {
                    mCurrentFrontPhotoPath = photoFile.getAbsolutePath();
                    isFrontPicture = true;
                }
                else {
                    mCurrentBackPhotoPath = photoFile.getAbsolutePath();
                    isFrontPicture = false;
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,
                        REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    /**
     * Respond to the result of a child activity by setting the
     * path to the image and the imageView.
     *
     * @param requestCode
     *            The integer request code originally supplied to
     *            startActivityForResult(), allowing you to identify
     *            who this result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its setResult().
     * @param data
     *            An Intent with the data returned by the child activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE
                && resultCode == Activity.RESULT_OK) {
            ImageView imageCardDetails = null;
            String photoPath = "";
            if( isFrontPicture == true ) {
                imageCardDetails = imageCardDetailsFront;
                photoPath = mCurrentFrontPhotoPath;
            }
            else {
                imageCardDetails = imageCardDetailsBack;
                photoPath = mCurrentBackPhotoPath;
            }

            if (photoPath.equals("")) {
                imageCardDetails.setImageResource(R.drawable.no_card_image);
            } else {
                Bitmap scaledBitmap = BbctPictureHelper.GetScaledImageFromPath( photoPath, imageCardDetails.getWidth(), imageCardDetails.getHeight());
                imageCardDetails.setImageBitmap(scaledBitmap);
            }
        }
    }

    /**
     * Default listener to handle front image click event
     */
    private View.OnClickListener onImageCardDetailsFrontClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mCurrentFrontPhotoPath.equals("")) {
                TakePicture(true);
            }
            else {
                ChangePicture(true);
            }
        }
    };

    /**
     * Default listener to handle back image click event
     */
    private View.OnClickListener onImageCardDetailsBackClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mCurrentBackPhotoPath.equals("")) {
                TakePicture(false);
            }
            else {
                ChangePicture(false);
            }

        }
    };

    @Override
    protected void onHome() {
        Fragment list = PremiumCardList.getInstance(null);
        this.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, list, FragmentTags.CARD_LIST)
                .addToBackStack(FragmentTags.CARD_LIST)
                .commit();
    }

    /**
    *
    * Returns the current {@link BaseballCard} object.
    *
    * @return {@link BaseballCard} object
    */
    @Override
    public BaseballCard getBaseballCard() {
        BaseballCard card = super.getBaseballCard();
        if (card!=null) {
            card.setPathToPictureFront(mCurrentFrontPhotoPath);
            card.setPathToPictureBack(mCurrentBackPhotoPath);
        }
        return card;
    }

    /**
    *
    * Resets the input to default values.
    */
    private void resetInput() {
        mCurrentBackPhotoPath = "";
        mCurrentFrontPhotoPath = "";
        imageCardDetailsFront.setImageResource(R.drawable.no_card_image);
        imageCardDetailsBack.setImageResource(R.drawable.no_card_image);
    }

    /**
    *
    * Handles the save menu click event.
    *
    */
    @Override
    protected void onSave() {
            BaseballCard card = getBaseballCard();
            saveCard(card);
            resetInput();
    }

    private ImageView imageCardDetailsFront = null;
    private ImageView imageCardDetailsBack = null;
    private String mCurrentFrontPhotoPath = "";
    private String mCurrentBackPhotoPath = "";
    private boolean isFrontPicture = true;
    private ViewTreeObserver.OnGlobalLayoutListener listenerFront = null;
    private ViewTreeObserver.OnGlobalLayoutListener listenerBack = null;
    private static final String TAG = PremiumCardDetails.class.getName();
}
