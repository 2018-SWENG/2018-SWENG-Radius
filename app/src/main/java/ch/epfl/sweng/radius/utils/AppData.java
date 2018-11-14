package ch.epfl.sweng.radius.utils;

public enum AppData {

    INSTANCE;

    private boolean isTestModeOn;

    AppData() {
        isTestModeOn = false;
    }

    public boolean isTestModeOn() {
        return isTestModeOn;
    }

    public void setTestMode(boolean isTestModeOn) {
        this.isTestModeOn = isTestModeOn;
    }

}
