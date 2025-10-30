package dev.manal.shorty.dto;

public class TopReferrer {
    public String referrer;
    public long count;

    public TopReferrer() {}

    public TopReferrer(String referrer, long count) {
        this.referrer = referrer;
        this.count = count;
    }
}
