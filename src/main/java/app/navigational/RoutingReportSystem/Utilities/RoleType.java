package app.navigational.RoutingReportSystem.Utilities;

public enum RoleType {
    USER("USER"), OPERATOR("OPERATOR"), ADMIN("ADMIN");

    private String code;

    private RoleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
