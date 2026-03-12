package com.genefied.nonSAAS.Vega;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.genefied.Utility.RetryAnalyzer;
import com.genefied.Utility.TestStepResult;

@Listeners(com.genefied.Utility.MyListener.class)
public class test {
	
	public static List<TestStepResult> stepResults = new ArrayList<>();
	
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
	
	public void testautomation() {
		int a=1;
		int b=2;
		System.out.println("sum: "+(a+b));
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
		try {
            warrantyForm();
            stepResults.add(new TestStepResult("Warranty Form", "PASS"));
        } catch (Exception e) {
            stepResults.add(new TestStepResult("Warranty Form", "FAIL"));
        }
		try {
            warrantyClaim();
            stepResults.add(new TestStepResult("Warranty Claim", "PASS"));
        } catch (Exception e) {
            stepResults.add(new TestStepResult("Warranty Claim", "FAIL"));
        }
	}

}
