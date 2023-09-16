package app.navigational.RoutingReportSystem.Utilities;

public enum VerifiedType {
    VERIFIED(true), NOT_VERIFIED(false);

    private Boolean code;

    private VerifiedType(Boolean code) {
        this.code = code;
    }

    public Boolean getCode() {
        return code;
    }
}
