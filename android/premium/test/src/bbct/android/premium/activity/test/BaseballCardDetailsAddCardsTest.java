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
package bbct.android.premium.activity.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import bbct.android.common.BbctPictureHelper;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.premium.activity.BaseballCardDetails;
import bbct.android.premium.utils.BBCTTestUtil;
import bbct.android.premium.utils.BaseballCardCsvFileReader;
import bbct.android.premium.utils.DatabaseUtil;
import com.robotium.solo.Solo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardDetails}.
 */
public class BaseballCardDetailsAddCardsTest extends
        ActivityInstrumentationTestCase2<BaseballCardDetails> {

    /**
     * Create instrumented test cases for {@link BaseballCardDetails}.
     */
    public BaseballCardDetailsAddCardsTest() {
        super(BaseballCardDetails.class);
    }
    
    /**
     * Checks if the given file name is present in the asset folder
     * 
     * @param assetList
     *          list of strings containing all the files present in asset folder
     *          
     * @param fileName
     *          name of the file to be checked in the asset folder
     * 
     */
    private boolean isFilePresentInAsset (String[] assetList, String fileName) {
        int length = assetList.length;
        int index=0;
        for (index=0; index<length; index++) {
            if(assetList[index].equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and all of its {@link EditText} and
     * {@link Button} views and a list of {@link BaseballCard} data. It stores the 
     * images from asset into the path specified in the corresponding card to be used
     * while adding the card.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();
        AssetManager assetMngr = this.inst.getContext().getAssets();
        InputStream in = assetMngr.open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        this.allCards = cardInput.getAllBaseballCards();
        this.card = this.allCards.get(3); // Ken Griffey Jr.
        cardInput.close();
        
        int BUFFER_LEN = 1024;
        String[] picList = assetMngr.list("");
        
        //Store the pictures from the asset to sd card
        for (int index=0; index<allCards.size(); index++) {
            BaseballCard cardTemp = allCards.get(index);
            String picName = cardTemp.getPlayerName() + " Front.jpg";
            if (isFilePresentInAsset(picList, picName)) {
                InputStream inPicFront = assetMngr.open(picName);
                File outFront = new File(directoryName,picName);
                byte[] bufferFront = new byte[BUFFER_LEN ];
                FileOutputStream fosFront = new FileOutputStream(outFront);
                int read = 0;

                 while ((read = inPicFront.read(bufferFront, 0, BUFFER_LEN)) >= 0) {
                        fosFront.write(bufferFront, 0, read);
                  }

                fosFront.flush();
                fosFront.close();
                inPicFront.close();
            }
            
            picName = cardTemp.getPlayerName() + " Back.jpg";
            if (isFilePresentInAsset(picList, picName)) {
                InputStream inPicBack = assetMngr.open(picName);

                File outBack = new File(directoryName,picName);
                byte[] bufferBack = new byte[BUFFER_LEN];
                FileOutputStream fosBack = new FileOutputStream(outBack);
                int read = 0;

                while ((read = inPicBack.read(bufferBack, 0, BUFFER_LEN)) >= 0) {
                    fosBack.write(bufferBack, 0, read);
                }

                fosBack.flush();
                fosBack.close();
                inPicBack.close();
            }
        }

        this.activity = this.getActivity();

        this.solo = new Solo(this.inst, this.activity);
        frontImage = (ImageView) solo.getView(R.id.image_card_details_front);
        backImage = (ImageView) solo.getView(R.id.image_card_details_back);
    }
    
    private void setImageInImageView(BaseballCard card) {
        frontImage = (ImageView) solo.getView(R.id.image_card_details_front);
        Activity activity = solo.getCurrentActivity();
        BbctPictureHelper picHelper = new BbctPictureHelper();
        String picturePath = directoryName + this.card.getPlayerName() + " Front.jpg";
        if (false == card.getPathToPictureFront().equals("")) {
            frontPic = picHelper.GetScaledImageFromPath(picturePath, 120, 120);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    frontImage.setImageBitmap(frontPic);
                }
            });
        }
        
        backImage = (ImageView) solo.getView(R.id.image_card_details_back);
        picturePath = directoryName + this.card.getPlayerName() + " Back.jpg";
        if (false == card.getPathToPictureBack().equals("")) {
            backPic = picHelper.GetScaledImageFromPath(picturePath, 120, 120);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    backImage.setImageBitmap(backPic);
                }
            });
        }
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()} and
     * deleting the app's database.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        DatabaseUtil dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        dbUtil.clearDatabase();

        super.tearDown();
    }

    /**
     * Test that baseball card data is correctly added to the database when it
     * is entered into the {@link BaseballCardDetails} activity.
     *
     * @throws Throwable
     *             If an error occurs while the portion of the test on the UI
     *             thread runs.
     */
    
    public void testAddCard() throws Throwable {
        setImageInImageView(this.card);
        BBCTTestUtil.addCard(this.solo, this.card);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        DatabaseUtil dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        Assert.assertTrue("Missing card: " + this.card,
                dbUtil.containsBaseballCard(this.card));
    }

    /**
     * Test that baseball card data for multiple cards is correctly added to the
     * database when it is entered into the {@link BaseballCardDetails}
     * activity. This test enters all data using a single invocation of the
     * {@link BaseballCardDetails} activity.
     *
     * @throws Throwable
     *             If an error occurs while the portion of the test on the UI
     *             thread runs.
     */
    public void testAddMultipleCards() throws Throwable {
        for (BaseballCard nextCard : this.allCards) {
            setImageInImageView(this.card);
            BBCTTestUtil.addCard(this.solo, nextCard);
            BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        }

        DatabaseUtil dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        for (BaseballCard nextCard : this.allCards) {
            Assert.assertTrue("Missing card: " + nextCard,
                    dbUtil.containsBaseballCard(nextCard));
        }
    }

    public void testBrandAutoComplete() throws Throwable {
        AutoCompleteTextView brandText = (AutoCompleteTextView) this.activity
                .findViewById(R.id.brand_text);
        this.testAutoComplete(brandText, this.card.getBrand());
    }

    public void testPlayerNameAutoComplete() throws Throwable {
        AutoCompleteTextView playerNameText = (AutoCompleteTextView) this.activity
                .findViewById(R.id.player_name_text);
        this.testAutoComplete(playerNameText, this.card.getPlayerName());
    }

    public void testTeamAutoComplete() throws Throwable {
        AutoCompleteTextView teamText = (AutoCompleteTextView) this.activity
                .findViewById(R.id.team_text);
        this.testAutoComplete(teamText, this.card.getTeam());
    }

    private void testAutoComplete(AutoCompleteTextView textView, String text)
            throws Throwable {
        setImageInImageView(this.card);
        BBCTTestUtil.addCard(this.solo, this.card);
        this.solo.typeText(textView, text.substring(0, 2));
        this.solo.waitForText(text);
        Assert.assertTrue(textView.isPopupShowing());
    }

    private Solo solo = null;
    private Activity activity = null;
    private Instrumentation inst = null;
    private List<BaseballCard> allCards = null;
    private BaseballCard card = null;
    private static Bitmap frontPic = null;
    private static Bitmap backPic = null;
    private static String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
    private static ImageView frontImage = null;
    private static ImageView backImage = null;
}
