package globaz.osiris.db.ordres.sepa;

import java.util.Arrays;
import java.util.List;

public enum CACamt054DefinitionType {
    BVR(CACamt054BaliseType.DOM_PMNT, Arrays.asList(CACamt054BaliseType.FA_RCDT, CACamt054BaliseType.FA_CNTR), Arrays.asList(
            CACamt054BaliseType.SUBFA_VCOM, CACamt054BaliseType.SUBFA_CAJT, CACamt054BaliseType.SUBFA_CDPT)),
    LSV(CACamt054BaliseType.DOM_PMNT, Arrays.asList(CACamt054BaliseType.FA_IDDT, CACamt054BaliseType.FA_RDDT), null),
    UNKNOWN(CACamt054BaliseType.DOM_EMPTY, Arrays.asList(CACamt054BaliseType.FA_EMPTY), Arrays
            .asList(CACamt054BaliseType.SUBFA_EMPTY));

    final CACamt054BaliseType domainCode;
    final List<CACamt054BaliseType> listFamilyCode;
    final List<CACamt054BaliseType> listSubFamilyCode;

    private CACamt054DefinitionType(final CACamt054BaliseType domainCode, final List<CACamt054BaliseType> familyCode,
            final List<CACamt054BaliseType> listSubFamilyCode) {
        this.domainCode = domainCode;
        listFamilyCode = familyCode;
        this.listSubFamilyCode = listSubFamilyCode;
    }

    public String getDomainCode() {
        return domainCode.getCode();
    }

    public boolean isDefinitionMatching(final String domainCode, final String familyCode, final String subFamilyCode) {

        if (domainCode == null || familyCode == null || subFamilyCode == null) {
            return false;
        }

        boolean isSameValues = getDomainCode().equals(domainCode);

        boolean hasFamily = false;
        for (CACamt054BaliseType family : listFamilyCode) {
            if (family.getCode().equals(familyCode)) {
                hasFamily = true;
            }
        }
        isSameValues &= hasFamily;

        boolean hasSubFamily = false;
        if (listSubFamilyCode == null) {
            hasSubFamily = true;
        } else {
            for (CACamt054BaliseType subFamily : listSubFamilyCode) {
                if (subFamily.getCode().equals(subFamilyCode)) {
                    hasSubFamily = true;
                }
            }
        }
        isSameValues &= hasSubFamily;
        return isSameValues;
    }
}
