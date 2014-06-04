/*
 * UIAutomator example taken from http://developer.android.com/tools/testing/testing_ui.html.
 */

package bbct.common.uitest;

import android.view.KeyEvent;
import android.widget.TextView;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchBBCT extends UiAutomatorTestCase {

	public void testDemo() throws UiObjectNotFoundException {

	    //UiDevice device = getUiDevice();
		// Simulate a short press on the HOME button.
	    getUiDevice().pressHome();

		// We’re now in the home screen. Next, we want to simulate
		// a user bringing up the All Apps screen.
		// If you use the uiautomatorviewer tool to capture a snapshot
		// of the Home screen, notice that the All Apps button’s
		// content-description property has the value “Apps�?. We can
		// use this property to create a UiSelector to find the button.
		UiObject allAppsButton = new UiObject(
				new UiSelector().description("Apps"));

		// Simulate a click to bring up the All Apps screen.
		allAppsButton.clickAndWaitForNewWindow();

		// In the All Apps screen, the Settings app is located in
		// the Apps tab. To simulate the user bringing up the Apps tab,
		// we create a UiSelector to find a tab with the text
		// label “Apps�?.
		UiObject appsTab = new UiObject(new UiSelector().text("Apps"));

		// Simulate a click to enter the Apps tab.
		appsTab.click();

		// Next, in the apps tabs, we can simulate a user swiping until
		// they come to the Settings app icon. Since the container view
		// is scrollable, we can use a UiScrollable object.
		UiScrollable appViews = new UiScrollable(
				new UiSelector().scrollable(true));

		// Set the swiping mode to horizontal (the default is vertical)
		appViews.setAsHorizontalList();

		// Create a UiSelector to find the Settings app and simulate
		// a user click to launch the app.
		UiObject BBCTApp = appViews.getChildByText(
				new UiSelector().className(TextView.class.getName()),
				"BBCT Premium");
		BBCTApp.clickAndWaitForNewWindow();

		// Validate that the package name is the expected one
		UiObject BBCTValidation = new UiObject(
				new UiSelector().packageName("bbct.android.premium"));
		assertTrue("Unable to detect BBCT", BBCTValidation.exists());
		
		UiObject addCard = new UiObject(new UiSelector()
		.description("Add Cards"));
		assertTrue("Unable to detect Add Cards button", addCard.exists());
		addCard.clickAndWaitForNewWindow();
		
		UiObject takePicture = new UiObject(new UiSelector()
        .description("Front Image"));
        assertTrue("Unable to detect Take Picture Image", takePicture.exists());
        takePicture.clickAndWaitForNewWindow();
        
        getUiDevice().pressKeyCode(KeyEvent.KEYCODE_CAMERA);
        
        UiObject savePic = new UiObject(new UiSelector()
        .className("android.widget.ImageView").index(2));
        assertTrue("Unable to detect save Picture Image", savePic.exists());
        savePic.click();
        
        UiObject cardDetailsText = new UiObject(new UiSelector()
        .className("android.widget.TextView").text("BBCT Premium - Card Details"));
        assertTrue("Unable to detect card details Image after saving picture", 
                cardDetailsText.exists());		
	}
}
