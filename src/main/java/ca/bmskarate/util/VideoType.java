package ca.bmskarate.util;

public enum VideoType {
    T("Training"), S("Student");

    String desc;
    VideoType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
