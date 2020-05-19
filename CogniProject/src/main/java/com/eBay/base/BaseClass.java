package com.eBay.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class BaseClass {


	public AndroidDriver<AndroidElement> driver;

	
		@BeforeTest
		public void setup() throws MalformedURLException
		{
			//Setting Up Environment for Appium Tests
			System.out.println("Environment Setup Initiated");

			try {
				//Setting desired capabilities for the test cases
				//Using real device
				DesiredCapabilities caps= new DesiredCapabilities();

				//Device Related Capabilities
				caps.setCapability(MobileCapabilityType.DEVICE_NAME, "SamsungM31");
				caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
				caps.setCapability(MobileCapabilityType.UDID,"RZ8N306D8TM");
				caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10");
				
				
				//eBay App related Capabilities
				caps.setCapability("appPackage", "com.ebay.mobile");
				caps.setCapability("appActivity", "com.ebay.mobile.activities.MainActivity");
				//Resetting the app data after the execution of scripts
				caps.setCapability("noReset", true);



				//Appium Server 
				URL url = new URL("http://127.0.0.1:4723/wd/hub");
				driver = new AndroidDriver<AndroidElement>(url, caps);
				driver.resetApp();
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				Thread.sleep(5000);


			} catch (Exception ex) {
				System.out.println(ex.getCause());
				System.out.println(ex.getMessage());
			}

		}

		public static String getCellData(int rownum, int colnum, String fileName)
		{
			try {
				FileInputStream file = new FileInputStream("C:\\Users\\Akshay\\eclipse-workspace\\CogniProject\\src\\test\\resources\\"+fileName+".xlsx");
				
				XSSFWorkbook wb = new XSSFWorkbook(file);
				XSSFSheet sheet = wb.getSheet("ProductSearch");
				XSSFRow row= sheet.getRow(rownum);
				XSSFCell cell = row.getCell(colnum);
				System.out.println("the test data, passed in the script is : "+cell.getStringCellValue());

				return cell.getStringCellValue();
			}
			catch (Exception e)
		     {
		        System.out.println(e);
		         return "Exception Occured";
		    }
		}

		
		@AfterClass
		public void teardown() throws IOException
		{
			System.out.println("Automation Script Ended");
			driver.closeApp();
		}



}
