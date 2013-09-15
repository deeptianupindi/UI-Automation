package com.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


public class GrouponMainPage {
	public static String URL = "http://www.groupon.com";
	public static String EMAIL = "abc@xyz.com";
	public static String ZIP = "12345";
	public static int BATCH_PRINT_QTY = 100;
	
	private WebDriver driver;	
	
	private int totalDealCount;
	private File file;
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	
	private int printerCount;
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public static void main(String[] args) {	
		System.out.println("All deals are being printed for " + ZIP);
		GrouponMainPage pageParser = new GrouponMainPage();
		pageParser.printerCount = 1;
		
		File dir = new File("C:\\GrouponDeals_DAnupindi");
		dir.mkdir();
		pageParser.file = new File("C:\\GrouponDeals_DAnupindi\\GrouponDeals_DAnupindi.txt");
		try {
			pageParser.fw = new FileWriter(pageParser.file,false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pageParser.bw = new BufferedWriter(pageParser.fw);
				
		pageParser.accessWebElements();

		pageParser.driver.quit();
		System.out.println("Please find the results printed at this location: " + pageParser.file);
		try {
			pageParser.bw.flush();
			pageParser.bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private void accessWebElements() {
		
		initializeDriver(URL);
		
		navigateToAllDeals(EMAIL, ZIP);
		
		captureFeaturedDeal();
				
		//Parse the deal count - number of deals available for this search
		String dealCount = driver.findElement(By.xpath(".//*[@id='content']/div[2]/div[1]/div/div/span/span/strong")).getText();
			
		totalDealCount = Integer.parseInt(dealCount);
		System.out.println("Total number deals found: " + totalDealCount);
		
		captureAllDeals();
	}

	private void captureFeaturedDeal() {
		//Print the title, url, merchant, location and price of the featured deal in "Browse-Featured" section
		String featuredTitle = driver.findElement(By.xpath(".//*[@id='hero-tile']/div/hgroup/h1/a")).getText();	
		String featuredUrl = driver.findElement(By.xpath(".//*[@id='hero-tile']/div/hgroup/h1/a")).getAttribute("href");
		String featuredMerchant = driver.findElement(By.xpath(".//*[@id='hero-tile']/div/hgroup/h2")).getText();
		String featuredLocation = driver.findElement(By.xpath(".//*[@id='hero-tile']/div/p")).getText();
		String featuredPrice = driver.findElement(By.xpath(".//*[@id='hero-tile']/div/div[2]/div[2]/span[2]")).getText();
		
		printAllDeals(featuredTitle, featuredUrl, featuredMerchant, featuredLocation, featuredPrice);
	}

	public String navigateToAllDeals(String email, String zip) {
		//Find the email text box element and enter an email address	
		driver.findElement(By.xpath(".//*[@id='email']")).sendKeys(email);

		//Click on the 'Continue' button
		driver.findElement(By.xpath(".//*[@id='js-email-step']/div[2]/div/button")).click();
	
		//Find the postal code text box and enter a zipcode	
		driver.findElement(By.xpath(".//*[@id='postal_code']")).sendKeys(zip);

		//Click on the 'Continue' button
		driver.findElement(By.xpath(".//*[@id='js-zip-step']/div[2]/button")).click();	
		
		String getPageTitle = driver.getTitle();	
		System.out.println("after navigating to all deals, the page title is:" + getPageTitle);
		return getPageTitle; 
	}

	public String initializeDriver(String url) {
		//Create a new object of Webdriver for Firefox browser
		
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.get(url);
		driver.manage().window().maximize();
		String getTitle = driver.getTitle();		
		return getTitle;
	}

	private void printAllDeals(String title, String url, String merchant,String location, String price) {		
		try 
		{			
			// If file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			bw.append(printerCount + ". ");
			bw.append("Title: "+ title);
			bw.newLine();
			bw.append("Link: "+ url);
			bw.newLine();
			bw.append("Merchant: "+ merchant);
			bw.newLine();
			bw.append("Location: "+ location);
			bw.newLine();
			bw.append("Price: "+ price);
			bw.newLine();
			bw.newLine();
			bw.flush();
			printerCount++;
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}	
		}

	private void captureAllDeals() {
		Boolean shouldContinue = true;
		
		WebElement dealButton;

		while ( shouldContinue )
		{			
			try {
				dealButton = driver.findElement(By.xpath(".//*[@id='js-deals-container']/div[2]/span/span"));
				dealButton.click();
			} catch ( Exception e ) {
				shouldContinue = false;
			}
		}

		List<WebElement> allDeals = driver.findElements(By.className("deal-list-tile"));		
		allDeals.remove(0);

		String title, url, merchant, location, price;
		WebElement allDealTitle, allDealUrl, allDealsMerchant, allDealsPrice, allDealsLocation;
		for(int j = 0; j < allDeals.size(); j++) {
			if ( j % BATCH_PRINT_QTY == 0 )
			{
				System.out.println("Processing " + j + " of " + totalDealCount);
			}
			
			try {
				allDealTitle = allDeals.get(j).findElement(By.xpath(".//a/div/div[1]"));
				allDealUrl = allDeals.get(j).findElement(By.xpath(".//a"));
				allDealsMerchant = allDeals.get(j).findElement(By.xpath(".//a/div/div[2]/p[2]"));
				allDealsPrice = allDeals.get(j).findElement(By.xpath(".//a/div/div[2]/div/span[2]"));
				allDealsLocation = allDeals.get(j).findElement(By.xpath(".//a/div/div[2]/p[3]"));
				
				title = allDealTitle.getText();
				url = allDealUrl.getAttribute("href");
				merchant = allDealsMerchant.getText();
				location = allDealsLocation.getText();
				price = allDealsPrice.getText();		
				
				printAllDeals(title, url, merchant, location, price);
			} catch (Exception e) {
				System.out.println("lost connection to driver");
				e.printStackTrace();
			}
		}
		
		System.out.println("Finished processing all deals");
		}
	}
