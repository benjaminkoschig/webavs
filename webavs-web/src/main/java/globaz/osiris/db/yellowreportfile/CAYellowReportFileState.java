package globaz.osiris.db.yellowreportfile;

public enum CAYellowReportFileState {
    IDENTIFIED("YELLOWREPORTFILE_STATE_IDENTIFIED", "blank2.png"),
    IN_TREATMENT("YELLOWREPORTFILE_STATE_IN_TREATMENT", "avertissement.gif"),
    PARTIAL("YELLOWREPORTFILE_STATE_PARTIAL", "avertissement2.gif"),
    EXECUTED("YELLOWREPORTFILE_STATE_EXECUTED", "ok.gif"),
    FAILED("YELLOWREPORTFILE_STATE_FAILED", "erreur2.gif");

    final String label;
    final String imagePlace;

    private CAYellowReportFileState(final String label, final String imagePlace) {
        this.label = label;
        this.imagePlace = imagePlace;
    }

    public static CAYellowReportFileState getEnumFromName(final String name) {
        if (name != null) {
            for (CAYellowReportFileState type : CAYellowReportFileState.values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    public String getImagePlace() {
        return imagePlace;
    }
}
