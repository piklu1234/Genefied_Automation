package com.genefied.nonSAAS.Vega;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.genefied.Utility.*;

@Listeners(com.genefied.Utility.MyListener.class)
public class VegaTesting {
	public WebDriver wdriver;
	public FileInputStream file;
	public Properties pro;
	public static List<TestStepResult> stepResults = new ArrayList<>();

	String img = System.getProperty("user.dir") + "\\Image\\Vega.jpg";

	@AfterMethod
	public void closure() {

		if (wdriver != null) {
			wdriver.quit();
		}
	}

	public void login() {
		System.out.println("Login step executed");
	}

	public void dashboard() {
		System.out.println("Dashboard step executed");
	}

	public void warrantyForm() {
		System.out.println("Warranty form step executed");
	}

	public void warrantyClaim() {
		System.out.println("Warranty claim step executed");
	}

	@Test(priority = 1, enabled = true, retryAnalyzer = RetryAnalyzer.class)

	public void User_Registration() throws Exception {

		allUtility util = new allUtility();
		pro = util.loadPropertiesFile();
		wdriver = util.initializeWebDriver();
		WebDriverWait wait = new WebDriverWait(wdriver, Duration.ofSeconds(2000));
		util.navigateToUrl(pro.getProperty("vegaQRcodeurl"));
		WebElement tab_length = wdriver.findElement(By.xpath("//select[@name='mainTable_length']"));
		Select tablength = new Select(tab_length);
		tablength.selectByValue("100");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='mainTable_processing']")));
		// tr[@role="row"]/td[contains(.,'Sca')]

		boolean QrStatus = true;
		String QrURL = null;
		String batch = null;
		while (QrStatus) {
			boolean QRfound = false;
			String ScannedStatus = null;
			String pagecount = wdriver.findElement(By.className("dataTables_info")).getText();
			int currentCount = 0;
			int totalCount = 0;
//			System.out.println(pagecount);
			List<WebElement> rowdata = wdriver.findElements(By.xpath("//tr[@role=\"row\"]/td"));
			int totalrow = rowdata.size() / 5;
			int scannedstatus = 0;
			for (int i = 0; i < totalrow; i++) {
				wdriver.findElements(By.xpath("//tr[@role=\"row\"]/td"));
				int qrurl = i * 5;
				scannedstatus = i * 5 + 3;
				int batchcode = i * 5 + 2;
				ScannedStatus = rowdata.get(scannedstatus).getText();

				if (rowdata.get(scannedstatus).getText().equals("Not Scanned")) {
					QrURL = rowdata.get(qrurl).getText();
					batch = rowdata.get(batchcode).getText();

//					System.out.println("QR URL:" + rowdata.get(qrurl).getText());
//					System.out.println("Scanned Status:" + rowdata.get(scannedstatus).getText());
					QRfound = true;
					QrStatus = false;
					break;
				}

			}
			
			if(QRfound == true) {
				break;	
			}
			Pattern pattern = Pattern.compile("to\\s+(\\d+)\\s+of\\s+([\\d,]+)");
			Matcher matcher = pattern.matcher(pagecount);

			if (matcher.find()) {
				currentCount = Integer.parseInt(matcher.group(1));
				totalCount = Integer.parseInt(matcher.group(2).replace(",", ""));
			}

			if (currentCount == totalCount & ScannedStatus.equals("Scanned")) {
				Reporter.log("All QR Get Exhausted there is no Scannable QR Left kindly create new QR code", true);
			}

			if (QRfound == false) {
				util.clickWeb_ElementByXpath("//a[normalize-space()=\"Next\"]");
				wait.until(
						ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='mainTable_processing']")));
			}
		}
		((JavascriptExecutor) wdriver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(wdriver.getWindowHandles());
		wdriver.switchTo().window(tabs.get(1));
		wdriver.get(QrURL);
		Thread.sleep(1000);
//		driver.switchTo().window(tabs.get(0));
		util.sendTextByWeb_Xpath("//input[@placeholder='10 digit mobile no']", "1234567899");
		Thread.sleep(2000);
		util.clickWeb_ElementByXpath("//button[normalize-space()='Get OTP']");
		Thread.sleep(1000);
		List<WebElement> otp = wdriver.findElements(By.xpath("//input[contains(@name,'otpdigit')]"));

		for (int k = 0; k < otp.size(); k++) {
			Thread.sleep(100);
			try {

				otp.get(k).sendKeys(String.valueOf(k + 1));
			} catch (StaleElementReferenceException e) {
				otp = wdriver.findElements(By.xpath("//input[@type='tel']"));
				otp.get(k).sendKeys(String.valueOf(k + 1));
			}
		}
		util.clickWeb_ElementById("verifyno");
		try {
			login();
			stepResults.add(new TestStepResult("Login", "PASS"));
		} catch (Exception e) {
			stepResults.add(new TestStepResult("Login", "FAIL"));
		}
		try {
			dashboard();
			stepResults.add(new TestStepResult("Dashboard", "PASS"));
		} catch (Exception e) {
			stepResults.add(new TestStepResult("Dashboard", "FAIL"));
		}
		Thread.sleep(1000);
		util.clickWeb_ElementByXpath("//img[@alt='Activate Warranty']");
		String productcode = util.get_web_Text(By.xpath("//div[@class='diseble_frm nonebr']/h6"));
		System.out.println("productcode:" + productcode);
		System.out.println("batchcode:" + batch);
		Assert.assertEquals(productcode, batch);
		util.sendTextByWeb_Xpath("//input[@placeholder='Pincode *']", "110054");
		wdriver.findElement(By.xpath("//input[@placeholder='Invoice']")).sendKeys(img);
		WebElement day = wdriver.findElement(By.xpath("(//select[@class='inputbox inht'])[1]"));
		Select days = new Select(day);
		days.selectByValue("23");
		WebElement month = wdriver.findElement(By.xpath("(//select[@class='inputbox inht'])[2]"));
		Select months = new Select(month);
		months.selectByValue("9");
		WebElement year = wdriver.findElement(By.xpath("(//select[@class='inputbox inht'])[3]"));
		Select years = new Select(year);
		years.selectByValue("2000");
		util.clickWeb_ElementById("popshow");

		util.clickWeb_ElementByXpath("//span[@class='close_btn']");
		try {
			warrantyForm();
			stepResults.add(new TestStepResult("Warranty Form", "PASS"));
		} catch (Exception e) {
			stepResults.add(new TestStepResult("Warranty Form", "FAIL"));
		}

		String activated_warrenty = util.get_web_Text(By.xpath("//a[normalize-space()='warranty activated']"));
		Assert.assertEquals(activated_warrenty, "WARRANTY ACTIVATED");
		util.clickWeb_ElementById("nav-icon3");
		util.clickWeb_ElementByXpath("//div[@class='menu_txt']");
		List<WebElement> warrentydata = wdriver.findElements(By.xpath("//div[contains(@class,'cntpwt')]/span"));
		int warrenty_details = warrentydata.size() / 4;
		for (int x = 0; x < warrenty_details; x++) {
			int wdetails = x * 4;
			String productwarrenty = warrentydata.get(wdetails).getText();
			String batchCode = productwarrenty.substring(productwarrenty.indexOf(":") + 1).trim();
			System.out.println(batchCode);

			if (batchCode.equals(productcode)) {
				List<WebElement> clame = wdriver.findElements(By.xpath("//a[contains(text(),'Claim')]"));
				clame.get(x).click();
				String clameproductcode = util.get_web_Text(By.xpath("(//h6)[2]"));
				Assert.assertEquals(clameproductcode, productcode);
				util.sendTextByWeb_Xpath("//textarea[@placeholder='Enter Description']",
						"Test Description Created During Automation");
				util.sendTextByWeb_Xpath("//input[@type='file']", img);
				util.clickWeb_ElementById("popshow");
				util.clickWeb_ElementByXpath("//span[@class='close_btn']");

				try {
					warrantyClaim();
					stepResults.add(new TestStepResult("Warranty Claim", "PASS"));
				} catch (Exception e) {
					stepResults.add(new TestStepResult("Warranty Claim", "FAIL"));
				}

				break;
			}
		}
//				wdriver.switchTo().window(tabs.get(0));
//				wdriver.get("https://genuinemark.org/partner_cpanel/index.php");
//				util.sendTextByWeb_Id("email", "info@vegaauto.com");
//				util.sendTextByWeb_Id("password", "vega@123@");
//				util.clickWeb_ElementById("submit_login");
//				util.clickWeb_ElementByXpath("//span[contains(.,'GenuineMark')]");
//				util.clickWeb_ElementByXpath("//span[contains(.,'Customer Warranty Claims')]");
//				List<WebElement> warrentypage = wdriver.findElements(By.xpath("//tr[@role=\"row\"]/td"));
//				int warrentyrow = warrentypage.size() / 16;
//				for (int t = 0; t < warrentyrow; t++) {
//					int tab = t * 16 + 6;
//
//					String mobile = warrentypage.get(tab).getText();
//					System.out.println(mobile);
//					if (mobile.equals("1234567899")) {
//						List<WebElement> chat = wdriver.findElements(By.xpath("//a[@title='Approve']"));
//						chat.get(t).click();
//						util.clickWeb_ElementByXpath("//button[@class='swal2-confirm swal2-styled']");
//						Thread.sleep(2000);
//						util.clickWeb_ElementByXpath("//button[@class='swal2-confirm swal2-styled']");
//						wdriver.findElement(By.linkText("Process")).click();
//						util.clickWeb_ElementByXpath("(//a[@class='btn-approve btn btn-success btn-sm'])[1]");
//						util.sendTextByWeb_Xpath("//textarea[@name='remarks']",
//								"This Is Test Remark Created During Automation By Genefied");
//						util.clickWeb_ElementByXpath("//button[normalize-space()='Submit']");
//						util.clickWeb_ElementByXpath("//button[@class='swal2-confirm swal2-styled']");
//						break;
//					}
//				}

	}
}
