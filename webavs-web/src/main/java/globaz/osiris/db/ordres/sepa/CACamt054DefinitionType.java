package globaz.osiris.db.ordres.sepa;

public enum CACamt054DefinitionType {

    CAMT054_BVR("PMNT", "RCDT", "VCOM"),
    CAMT054_DD("", "", ""),
    UNKNOWN("", "", "");

    final String domainCode;
    final String familyCode;
    final String subFamilyCode;

    private CACamt054DefinitionType(final String domainCode, final String familyCode, final String subFamilyCode) {
        this.domainCode = domainCode;
        this.familyCode = familyCode;
        this.subFamilyCode = subFamilyCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public String getSubFamilyCode() {
        return subFamilyCode;
    }

    public boolean isGoodType(final String domainCode, final String familyCode, final String subFamilyCode) {

        if (domainCode == null || familyCode == null || subFamilyCode == null) {
            return false;
        }

        boolean isSameValues = getDomainCode().equals(domainCode);
        isSameValues &= getFamilyCode().equals(familyCode);
        isSameValues &= getSubFamilyCode().equals(subFamilyCode);

        return isSameValues;
    }
}
