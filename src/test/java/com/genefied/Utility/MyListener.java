package com.genefied.Utility;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class MyListener implements ITestListener {

    public ChromeDriver wdriver;

    @Override
    public void onTestStart(ITestResult result) {
        Reporter.log("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Reporter.log("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.log("Test failed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Reporter.log("Test skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        Reporter.log("Test suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {

        Reporter.log("Test suite finished: " + context.getName());

        try {

            EmailSender sender = new EmailSender();
            sender.sendReport();

            Reporter.log("Email sent successfully");

        } catch (Exception e) {

            Reporter.log("Email sending failed");
            e.printStackTrace();
        }
    }
}