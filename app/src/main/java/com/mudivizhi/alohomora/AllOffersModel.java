package com.mudivizhi.alohomora;

public class AllOffersModel {

    private String url,offerPercent,desc;

    public AllOffersModel(){}

    public AllOffersModel(String url, String offerPercent, String desc) {
        this.url = url;
        this.offerPercent = offerPercent;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(String offerPercent) {
        this.offerPercent = offerPercent;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
