package globaz.osiris.db.ordres.sepa;

import java.util.Arrays;
import java.util.List;

public enum CACamt054DefinitionType {
    CAMT054_BVR(CaCamtDefinitionType.DOMAINE_PAIEMENT, CaCamtDefinitionType.FAMILY_CREDIT, Arrays.asList(
            CaCamtDefinitionType.SUBFAMILY_VCOM, CaCamtDefinitionType.SUBFAMILY_CAJT)),
    CAMT054_DD(CaCamtDefinitionType.DOMAINE_EMPTY, CaCamtDefinitionType.FAMILY_EMPTY, Arrays
            .asList(CaCamtDefinitionType.SUBFAMILY_EMPTY)),
    UNKNOWN(CaCamtDefinitionType.DOMAINE_EMPTY, CaCamtDefinitionType.FAMILY_EMPTY, Arrays
            .asList(CaCamtDefinitionType.SUBFAMILY_EMPTY));

    final CaCamtDefinitionType domainCode;
    final CaCamtDefinitionType familyCode;
    final List<CaCamtDefinitionType> listSubFamilyCode;

    private CACamt054DefinitionType(final CaCamtDefinitionType domainCode, final CaCamtDefinitionType familyCode,
            final List<CaCamtDefinitionType> listSubFamilyCode) {
        this.domainCode = domainCode;
        this.familyCode = familyCode;
        this.listSubFamilyCode = listSubFamilyCode;
    }

    public String getDomainCode() {
        return domainCode.getCode();
    }

    public String getFamilyCode() {
        return familyCode.getCode();
    }

    public boolean isDefinitionMatching(final String domainCode, final String familyCode, final String subFamilyCode) {

        if (domainCode == null || familyCode == null || subFamilyCode == null) {
            return false;
        }

        boolean isSameValues = getDomainCode().equals(domainCode);
        isSameValues &= getFamilyCode().equals(familyCode);

        boolean hasSubFamily = false;
        for (CaCamtDefinitionType subFamily : listSubFamilyCode) {
            if (subFamily.getCode().equals(subFamilyCode)) {
                hasSubFamily = true;
            }
        }
        isSameValues &= hasSubFamily;
        return isSameValues;
    }
}
