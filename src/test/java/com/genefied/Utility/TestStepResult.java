package com.genefied.Utility;

public class TestStepResult {

    private String stepName;
    private String status;

    public TestStepResult(String stepName, String status) {
        this.stepName = stepName;
        this.status = status;
    }

    public String getStepName() {
        return stepName;
    }

    public String getStatus() {
        return status;
    }
}
