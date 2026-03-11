package com.genefied.Utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
//import org.openqa.selenium.devtools.v129.network.Network;
//import org.openqa.selenium.devtools.v129.network.model.RequestId;
//import org.openqa.selenium.devtools.v129.network.model.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class allUtility {
	private ChromeDriver wdriver;
	private Properties pro;
	String filepath = "C:\\Users\\ganes\\eclipse-workspace\\Genefied\\src\\test\\java\\com\\genefied\\Utility\\TestProperty.properties";

	public void webclosure() {
		wdriver.quit();
	}

	public ChromeDriver initializeWebDriver() throws MalformedURLException, InterruptedException {
//		WebDriverManager.chromedriver().setup();
//		wdriver = new ChromeDriver();
		ChromeOptions options = new ChromeOptions();
        wdriver = new ChromeDriver(options);
		wdriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30)); 
		Thread.sleep(300);
		return wdriver;
	}
	

//	public void response() {
//	    DevTools devTools = wdriver.getDevTools();
//	    devTools.createSession();
//	    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
//
//	    devTools.addListener(Network.responseReceived(), response -> {
//	        String responseUrl = response.getResponse().getUrl();
//	        RequestId requestId = response.getRequestId();
//	        int statusCode = response.getResponse().getStatus();
//	        
//	        
//	        String requestMethod = "Unknown";
//	        if (response.getResponse().getRequestHeaders().isPresent()) {
//	            Map<String, Object> requestHeaders = response.getResponse().getRequestHeaders().get();
//	            requestMethod = requestHeaders.containsKey("method") ? (String) requestHeaders.get("method") : "Unknown";
//	        }
//
//	       
//	        String contentLength = (String) response.getResponse().getHeaders().getOrDefault("Content-Length", "Unknown");
//
//	        if (statusCode < 200 || statusCode >= 400) {
//	        	Reporter.log("---------Network Response------------",true);
//	        	Reporter.log("Request Method: " + requestMethod,true);
//	        	Reporter.log("Response URL: " + responseUrl,true);
//	        	Reporter.log("Response Status: " + statusCode,true);
//	        	Reporter.log("Content-Length: " + contentLength,true);
//	            String responseBody = getResponseBody(devTools, requestId);
//	            Reporter.log("Response Body: " + responseBody,true);
//	            Reporter.log("---------Network Response------------",true);
//	            Reporter.log("",true);
//	        }
//	    });
//	}
//
//	private String getResponseBody(DevTools devTools, RequestId requestId) {
//	    try {
//	        return devTools.send(Network.getResponseBody(requestId)).getBody();
//	    } catch (Exception e) {
//	    	Reporter.log("---------Network Response------------",true);
//	    	Reporter.log("❌ Failed to get response body: " + e.getMessage(),true);
//	    	Reporter.log("---------Network Response------------",true);
//	        return "Error retrieving body";
//	    }
//	}
//	
//	public AtomicReference<String> paginationresponse(String url) throws InterruptedException {
//		DevTools devTools = wdriver.getDevTools();
//		devTools.createSession();
//		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
//
//		AtomicReference<String> responseBody = new AtomicReference<>();
//		devTools.addListener(Network.responseReceived(), response -> {
//			RequestId requestId = response.getRequestId();
//			Response res = response.getResponse();
//			if (res.getUrl().contains(url)) {
////               System.out.println("Response URL: " + res.getUrl());
//				String body = devTools.send(Network.getResponseBody(requestId)).getBody();
////                System.out.println("Response Body: " + body);
//				responseBody.set(body);
//			}
//		});
//		
//		return responseBody;
//
//	}

	public void checkPagination(int totalrow, int rowposition, AtomicReference<String> responseBody ,String bodytype) throws InterruptedException {

		// xpath should be of unique code in every column and it should be repeat every
		// time at same position
		// totalrows is Number of rows in the page
		// rowposition is position of that unique code in one column , mark 1 row 1
		// column as 0 and then count
	
		WebElement noOfRows = wdriver.findElement(By.xpath("//select[@aria-label='Rows per page:']"));
		Select select = new Select(noOfRows);
		select.selectByValue("30");
		Thread.sleep(5000);
		String xpath="//div[@role='cell']";
		String responseBodys = responseBody.get();
		int totalProducts = 0;
		if (responseBodys != null) {
			JSONObject jsonResponse = new JSONObject(responseBodys);
			JSONObject bodyObject = jsonResponse.getJSONObject("body");
	            String totalProductsString = bodyObject.getString(bodytype);
//			String totalProductsString = bodyObject.getString("total");
			totalProducts = Integer.parseInt(totalProductsString);
			System.out.println("Total Products: " + totalProducts);
			
			int producton1page=totalProducts-30;
			int producton2page=producton1page-30;
			
			List<WebElement> cell = wdriver.findElements(By.xpath(xpath));
			List<String> listOfProductcode = new ArrayList<>();

			int row = cell.size() / totalrow;

			for (int i = 0; i < row; i++) {
				int elementposition = i * totalrow + rowposition;
				String productCode = cell.get(elementposition).getText();
				listOfProductcode.add(productCode);
			}
			

			List<WebElement> columncount = wdriver.findElements(By.xpath(xpath));
			int intcolumncount = columncount.size() / totalrow;
			
			if(producton1page<0) {
				Reporter.log("Item is Less than 30 No need To test pagination", true);
			}
			
			if (intcolumncount == 30 & producton1page>0 ) {
				wdriver.findElement(By.id("pagination-next-page")).click();

				Thread.sleep(2000);

				List<WebElement> columncountnextpage = wdriver.findElements(By.xpath(xpath));
				int intcolumncountnextpage = columncountnextpage.size() / totalrow;
				
				if(producton2page<0) {
				
				if (intcolumncountnextpage == producton1page ) {
					List<WebElement> cellnext = wdriver.findElements(By.xpath(xpath));
					int rownext = cellnext.size() / totalrow;
					int mismatchCount = 0;

					for (int i = 0; i < rownext; i++) {

						int elementposition = i * totalrow + rowposition;
						String productCodenext = cellnext.get(elementposition).getText();
						boolean foundMatch = false;

						for (int k = 0; k < listOfProductcode.size(); k++) {
							String matching = listOfProductcode.get(k);
							if (matching.equals(productCodenext)) {
								foundMatch = true;
								break;
							}
						}

						if (!foundMatch) {
							mismatchCount++;
						}
					}

					if (mismatchCount > 0) {
						Reporter.log("Pagination Is Working", true);
					} else {
						Reporter.log("❌ Pagination Is Not Working", true);
					}
				} else {
					Reporter.log("❌ Next Page Button is not working", true);
				}
				
				}
				else if(producton2page>=0) {
					
					if (intcolumncountnextpage == 30 ) {
						List<WebElement> cellnext = wdriver.findElements(By.xpath(xpath));
						int rownext = cellnext.size() / totalrow;
						int mismatchCount = 0;

						for (int i = 0; i < rownext; i++) {

							int elementposition = i * totalrow + rowposition;
							String productCodenext = cellnext.get(elementposition).getText();
							boolean foundMatch = false;

							for (int k = 0; k < listOfProductcode.size(); k++) {
								String matching = listOfProductcode.get(k);
								if (matching.equals(productCodenext)) {
									foundMatch = true;
									break;
								}
							}

							if (!foundMatch) {
								mismatchCount++;
							}
						}

						if (mismatchCount > 0) {
							Reporter.log("Pagination Is Working", true);
						} else {
							Reporter.log("❌ Pagination Is Not Working", true);
						}
					} else {
						Reporter.log("❌ Next Page Button is not working", true);
					}
					
					}
				
			} else {
				Reporter.log("❌ 'Numbers of Column on Page' Dropdown is not working", true);
			}
			
			
			
			
		} else {
			System.out.println("For Pagination No response body captured.");
		}
		
		
	}
	
//	public void checkPagination (String xpath,int totalrow,int rowposition) throws InterruptedException {
//		
//		//xpath should be of unique code in every column and it should be repeat every time at same position
//		//totalrows is Number of rows in the page 
//		//rowposition is position of that unique code in one column , mark 1 row 1 column as 0 and then count
//		WebElement noOfRows=wdriver.findElement(By.xpath("//select[@aria-label='Rows per page:']"));
//		Select select=new Select(noOfRows);
//		select.selectByValue("30");
//		Thread.sleep(2000);
//		List<WebElement> cell = wdriver.findElements(By.xpath(xpath));
//		List<String> listOfProductcode = new ArrayList<>();
//
//		int row = cell.size() / totalrow;
//		
//		for (int i = 0; i < row; i++) {
//		    int elementposition = i * totalrow + rowposition; 
//		    String productCode = cell.get(elementposition).getText();
//		    listOfProductcode.add(productCode);
//		}
////
////		String lastnumber = wdriver.findElement(By.xpath("//div[contains(text(),'30')]")).getText();
//		List<WebElement> columncount = wdriver.findElements(By.xpath(xpath));
//		int intcolumncount = columncount.size() / totalrow;
//		if (intcolumncount==30) {
//		    clickWeb_ElementById("pagination-next-page");
//		    
//		    Thread.sleep(2000);
//
////		    String nextnumber = wdriver.findElement(By.xpath("//div[contains(text(),'60')]")).getText();
//		    List<WebElement> columncountnextpage = wdriver.findElements(By.xpath(xpath));
//			int intcolumncountnextpage = columncountnextpage.size() / totalrow;
//		    if (intcolumncountnextpage==30) {
//		        List<WebElement> cellnext = wdriver.findElements(By.xpath(xpath));
//		        int rownext = cellnext.size() / totalrow;
//		        int mismatchCount = 0; 
//		        
//		        for (int i = 0; i < rownext; i++) {
//		        	
//		            int elementposition = i * totalrow + rowposition;
//		            String productCodenext = cellnext.get(elementposition).getText();
//		            boolean foundMatch = false;
//		            
//		            for (int k = 0; k < listOfProductcode.size(); k++) {
//		                String matching = listOfProductcode.get(k);
//		                if (matching.equals(productCodenext)) {
//		                    foundMatch = true;
//		                    break; 
//		                }
//		            }
//
//		            if (!foundMatch) {
//		                mismatchCount++; 
//		            }
//		        }
//
//		        if (mismatchCount > 0) {
//		            Reporter.log("Pagination Is Working", true);
//		        } else {
//		            Reporter.log("❌ Pagination Is Not Working", true);
//		        }
//		    } else {
//		        Reporter.log("❌ Next Page Button is not working", true);
//		    }
//		} else {
//		    Reporter.log("❌ 'Numbers of Column on Page' Dropdown is not working", true);
//		}
//	}


	public Properties loadPropertiesFile() throws Exception {
		try (FileInputStream file = new FileInputStream(filepath)) {
			pro = new Properties();
			pro.load(file);
			return pro;
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage());
			throw new Exception(e);
		}
	}

	public void updateProperty(String key, String value) throws IOException {
		pro.setProperty(key, value);
		try (FileOutputStream fileOutput = new FileOutputStream(filepath)) {
			pro.store(fileOutput, null);
		}
		System.out.println("Property '" + key + "' updated to: " + value);
	}

	ArrayList<String> mobiles = getRandomMobileNumber(2);

	private ArrayList<String> getRandomMobileNumber(int n) {
		ArrayList<String> arr = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			String a = randomNumber(1, 6, 9);
			String b = randomNumber(9, 0, 9);
			arr.add(a + b);
		}
		return arr;
	}

	private String randomNumber(int length, int start, int end) {
		Random ran = new Random();
		StringBuilder number = new StringBuilder();
		int[] a = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		ArrayList<Integer> array = new ArrayList<>();
		for (int i = start; i < end; i++) {
			array.add(a[i]);
		}
		for (int i = 0; i < length; i++) {
			int arr = ran.nextInt(array.size());
			int nextNumber = array.get(arr);
			number.append(nextNumber);
		}
		return number.toString();
	}

	public ArrayList<String> getRandomNumber(int usertype, String usertypeName) throws IOException {
		ArrayList<String> mobileNumbers = new ArrayList<>();
		String mobileNumber;

		if (usertype == 1) {
			mobileNumber = mobiles.get(0);
			mobileNumbers.add(mobileNumber);
			updateProperty(usertypeName, mobileNumber);
		} else if (usertype == 2) {
			mobileNumber = mobiles.get(1);
			mobileNumbers.add(mobileNumber);
			updateProperty(usertypeName, mobileNumber);
		}
//        else {
//            String influencerNo = mobiles.get(0);
//            mobileNumbers.add(influencerNo);
//            String dealerNo = mobiles.get(1);
//            mobileNumbers.add(dealerNo);
//            updateProperty("influencer", influencerNo);
//            updateProperty("dealer", dealerNo);
//        }

		return mobileNumbers;
	}

	public void navigateToUrl(String url) {
		try {
			wdriver.get(url);
			Thread.sleep(300);
			System.out.println("➡️ Navigated to URL: " + url);
		} catch (Exception e) {
			System.err.println("❌ Failed to navigate to URL: " + url);
			e.printStackTrace();
			webclosure();
		}
	}

	public void sendTextByWeb_Xpath(String locator, String text) {
		try {
			String buttonname=wdriver.findElement(By.xpath(locator)).getText();
			WebDriverWait wait = new WebDriverWait(wdriver, Duration.ofSeconds(20));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
			element.sendKeys(text);
			if (buttonname != null && !buttonname.isEmpty()) {
				Reporter.log("🔤 In "+buttonname + " Input text field Value '"+text+"' is passed then " ,true);
			} else {
				Reporter.log("🔤 In Input Text Field '"+ text +"' data is Passed then",true);
			}
			Thread.sleep(300);
		} catch (Exception e) {
			System.err.println("❌ Error while sending text to the web element with xpath: " + locator);
			System.err.println("❌ Exception message: " + e.getMessage());
			e.printStackTrace();
			webclosure();
		}
	}

	public void sendTextByWeb_Id(String locator, String text) {
		try {
			String buttonname=wdriver.findElement(By.id(locator)).getText();
			WebDriverWait wait = new WebDriverWait(wdriver, Duration.ofSeconds(20));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
			element.sendKeys(text);
			if (buttonname != null && !buttonname.isEmpty()) {
				Reporter.log("🔤 In "+buttonname + " Input text field Value '"+text+"' is passed then" ,true);
			} else {
				Reporter.log("🔤 In Input Text Field '"+ text +"' data is Passed then",true);
			}
			Thread.sleep(300);
		} catch (Exception e) {
			System.err.println("❌ Error while sending text to the web element with ID: " + locator);
			System.err.println("❌ Exception message: " + e.getMessage());
			e.printStackTrace();
			webclosure();
		}
	}

	public void clickWeb_ElementByXpath(String locator) {
		try {
			String buttonname=wdriver.findElement(By.xpath(locator)).getText();
			WebDriverWait wait = new WebDriverWait(wdriver, Duration.ofSeconds(20));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
			element.click();
			if (buttonname != null && !buttonname.isEmpty()) {
				Reporter.log("✅ '"+buttonname + "'---->Button Get clicked then",true);
			} else {
				Reporter.log("✅ Button name is not present or is empty but it get clicked then",true);
			}
			Thread.sleep(500);
		} catch (Exception e) {
			System.err.println("❌ Error while clicking the web element with xpath: " + locator);
			System.err.println("❌ Exception message: " + e.getMessage());
			e.printStackTrace();

			try {
				String buttonname=wdriver.findElement(By.xpath(locator)).getText();
				WebElement element = wdriver.findElement(By.xpath(locator));
				JavascriptExecutor js = (JavascriptExecutor) wdriver;
				js.executeScript("arguments[0].click();", element);
				if (buttonname != null && !buttonname.isEmpty()) {
					Reporter.log("✅ '"+buttonname + "'---->Button Get clicked then",true);
					System.out.println("Element clicked using JavaScript: " + locator);
				} else {
					Reporter.log("✅ Button name is not present or is empty but it get clicked then",true);
				}
				
			} catch (Exception jsException) {
				System.err.println("❌ Error while clicking the web element with JavaScript: " + locator);
				System.err.println("❌ Exception message: " + jsException.getMessage());
				jsException.printStackTrace();
				webclosure();
			}
		}
	}

	public void clickWeb_ElementById(String locator) {
		try {
			String buttonname=wdriver.findElement(By.id(locator)).getText();
			WebDriverWait wait = new WebDriverWait(wdriver, Duration.ofSeconds(20));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
			element.click();
			if (buttonname != null && !buttonname.isEmpty()) {
				Reporter.log("✅ '"+buttonname + "'---->Button Get clicked then",true);
			} else {
				Reporter.log("✅ Button name is not present or is empty but it get clicked then",true);
			}
			Thread.sleep(500);
		} catch (Exception e) {
			System.err.println("❌ Error while clicking the web element with ID: " + locator);
			System.err.println("❌ Exception message: " + e.getMessage());
			e.printStackTrace();

			try {
				String buttonname=wdriver.findElement(By.id(locator)).getText();
				WebElement element = wdriver.findElement(By.id(locator));
				JavascriptExecutor js = (JavascriptExecutor) wdriver;
				js.executeScript("arguments[0].click();", element);
				
				if (buttonname != null && !buttonname.isEmpty()) {
					Reporter.log("✅ '"+buttonname + "'---->Button Get clicked then",true);
					System.out.println("Element clicked using JavaScript: " + locator);
				} else {
					Reporter.log("✅ Button name is not present or is empty but it get clicked then",true);
				}
			} catch (Exception jsException) {
				System.err.println("❌ Error while clicking the web element with JavaScript: " + locator);
				System.err.println("❌ Exception message: " + jsException.getMessage());
				jsException.printStackTrace();
				webclosure();
			}
		}
	}

	public String get_web_Text(By locator) {
		try {
			return wdriver.findElement(locator).getText();
		} catch (Exception e) {
			System.err.println("❌ Error getting text from element, retrying: " + e.getMessage());
			try {
				Thread.sleep(1000);
				return wdriver.findElement(locator).getText();
			} catch (Exception retryException) {
				System.err.println("❌ Retry failed: " + retryException.getMessage());
				return "";
			}
		}
	}
}
