package tests;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.eBay.base.BaseClass;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class eBayTestCase extends BaseClass{

	@Test(priority = 1)
	public void appLaunchValidation() throws Exception
	{
		//App Launch Message
		System.out.println("App Launched");
		
		//Wait for the eBay Logo to load 
		WebElement eBayLogo = driver.findElement(By.id("com.ebay.mobile:id/logo"));
		WebDriverWait wait = new WebDriverWait(driver,8);
		wait.until(ExpectedConditions.visibilityOf(eBayLogo));

	}

	@Test(priority = 2)
	public void search() throws InterruptedException
	{
		Thread.sleep(3000);
		driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Search keyword Search for anything\"]")).click();

		//Search for 65-Inch Tv 
		//Passing the data from external data source (Excel) by invoking getcelldata method from base class
		
		WebElement SearchBox = driver.findElement(By.id("com.ebay.mobile:id/search_src_text"));
		SearchBox.sendKeys(getCellData(1,0, "CogniData"));
		((AndroidDriver<AndroidElement>)driver).pressKey(new KeyEvent(AndroidKey.ENTER));

		Thread.sleep(3000);
	}

	@Test(priority = 3)
	public void selectRandomOption() throws InterruptedException
	{

		driver.findElement(By.id("com.ebay.mobile:id/text_slot_1")).click();

		//Swipe and Select Random Product
		WebElement Product1 = driver.findElement(By.xpath("//android.widget.RelativeLayout[@index='2']"));
		int a=Product1.getLocation().getX();
		int b=Product1.getLocation().getY();
		
		
		WebElement Product2 = driver.findElement(By.xpath("//android.widget.RelativeLayout[@index='3']"));
		int x=Product2.getLocation().getX();
		int y=Product2.getLocation().getY();
		
		
		//Swipe from one product to another Product
		TouchAction ta = new TouchAction(driver);
		ta.press(PointOption.point(a, b)).waitAction(new WaitOptions().withDuration(Duration.ofMillis(600))).moveTo(PointOption.point(x, y)).release().perform();
			
		driver.findElement(By.id("com.ebay.mobile:id/text_slot_1")).click();
		Thread.sleep(4000);
		
		//getting list of all the products for the mentioned X path
		List<AndroidElement> products = driver.findElements(By.xpath("//android.widget.RelativeLayout"));

		//Choosing a random product within the list of products
		System.out.println(products.size());  
		Random r = new Random(); 
		int randvalue =r.nextInt(products.size());
		products.get(randvalue).click();
		System.out.println(randvalue);
		

	}


	@Test(priority = 4, dependsOnMethods = "selectRandomOption")
	public void validateProductDetails() throws InterruptedException
	{

		
		//Image Validation for the randomly selected product 
		if(driver.findElement(By.xpath("//android.widget.ImageView[@resource-id='com.ebay.mobile:id/imageview_image']")).isDisplayed())
		{
			System.out.println("Image is displayed for the product");
		}
		else
		{
			Assert.fail("Image is not displayed for the product");
		}

		//Validating Image in different view	
		driver.findElement(By.xpath("//android.widget.ImageView[@resource-id='com.ebay.mobile:id/imageview_image']")).click();
		driver.rotate(ScreenOrientation.LANDSCAPE);
		System.out.println("The app is running in :" +driver.getOrientation()+ " Orientation");
		Thread.sleep(2000);
		driver.rotate(ScreenOrientation.PORTRAIT);
		System.out.println("The app is running in :" +driver.getOrientation()+ " Orientation");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Close']")).click();
		
		//Validating the dimension of the mobile screen
		Dimension dimensions = driver.manage().window().getSize();
		System.out.println("The scripts are running in the mobile screen with the dimensions :" +dimensions.getWidth()+ ": Width ," +dimensions.getHeight()+":Height");
		
	}

	@Test(priority = 5)
	public void Checkout() throws InterruptedException
	{
		//Validate Buying option
		WebElement BuyOption = driver.findElement(By.xpath("//android.widget.Button[@resource-id='com.ebay.mobile:id/cta_button']"));
		
		if(BuyOption.isEnabled())
		{
			System.out.println("product is in stock and available");
			BuyOption.click();
			Thread.sleep(4000);
		}
		else
		{
			Assert.fail("Product is not available in store");
		}

		//Validate if product is deliverable to the address specified
		//eBay is not delivering to specified location of the user for this script
		if(driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.ebay.mobile:id/alertTitle']")).isDisplayed())
		{
			driver.findElement(By.xpath("//android.widget.Button[@text='OK']")).click();
			Assert.fail("Seller cannot deliver the product to primary address");
		}
		else
		{
			System.out.println("Delivery is available to primary address");
		}
	}


	
	
}
