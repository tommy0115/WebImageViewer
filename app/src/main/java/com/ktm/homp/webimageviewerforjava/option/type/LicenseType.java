package com.ktm.homp.webimageviewerforjava.option.type;

public enum LicenseType {

    RF_AND_RM("none", 0), RF("rf", 1), RM("rm", 2);

    final private String name;
    final private int code;

    public String getName() {
        if(name.equals("none")){
            return "";
        }
        return name;
    }

    public int getCode() {
        return code;
    }

    private LicenseType(String name, int code){
        this.name = name;
        this.code = code;
    }

    public static LicenseType getItem(int n){
        if(LicenseType.values().length < n ){
            return  null;
        }
        return LicenseType.values()[n];
    }

    public static LicenseType getMatchingLicenseTypeCode(int code){
        for(LicenseType licenseType : values()){
            if(licenseType.getCode() == code){
                return licenseType;
            }
        }
        return null;
    }

    public static int getIndex(LicenseType licenseType) {
        for (int n = 0; n < LicenseType.values().length; n++) {
            if (LicenseType.values()[n] == licenseType) {
                return n;
            }
        }
        return -1;
    }

}
