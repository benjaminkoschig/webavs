package globaz.osiris.db.yellowreportfile;

import globaz.osiris.db.ordres.sepa.CACamt054DefinitionType;

public enum CAYellowReportFileType {
    CAMT054_BVR("YELLOWREPORTFILE_TYPE_CAMT054_BVR", "bvr", CACamt054DefinitionType.BVR),
    CAMT054_LSV("YELLOWREPORTFILE_TYPE_CAMT054_LSV", "lsv", CACamt054DefinitionType.LSV),
    ISO20022("YELLOWREPORTFILE_TYPE_ISO20022", null, CACamt054DefinitionType.UNKNOWN);

    private String label;
    /**
     * Pour le passage du type au niveau écran ou autre
     */
    private String typeFile;
    private CACamt054DefinitionType linkedType;

    private CAYellowReportFileType(final String label, final String typeFile, final CACamt054DefinitionType linkedType) {
        this.label = label;
        this.typeFile = typeFile;
        this.linkedType = linkedType;
    }

    public String getLabel() {
        return label;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public CACamt054DefinitionType getLinkedType() {
        return linkedType;
    }

    public static CAYellowReportFileType getYellowReportTypeFromLinkedType(final CACamt054DefinitionType linkedType) {
        if (linkedType != null) {
            for (CAYellowReportFileType type : CAYellowReportFileType.values()) {
                if (type.getLinkedType().equals(linkedType)) {
                    return type;
                }
            }
        }

        return CAYellowReportFileType.ISO20022;
    }

    public static CAYellowReportFileType getEnumFromName(final String name) {
        if (name != null) {
            for (CAYellowReportFileType type : CAYellowReportFileType.values()) {
                if (type.name().equals(name)) {
                    return type;
                }
            }
        }
        return null;
    }
}
