package globaz.osiris.db.ordres.sepa.utils;

import globaz.osiris.api.ordre.APICommonOdreVersement;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentInstructionInformation3CH;

/**
 * wrapper of Blevel identify by 4field info/type to identify group to aggregate
 * 
 * @author cel
 * 
 */
public class CASepaGroupeOGKey {

    // key info
    private String monnaieISO;
    private String typeOrdre;
    private String typeVirement;
    private String paysDest;

    // B-level info
    private com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentInstructionInformation3CH bLevel;

    public CASepaGroupeOGKey(APICommonOdreVersement ov) throws Exception {
        this(ov.getCodeISOMonnaieBonification(), CASepaOVConverterUtils.getTypeVersement(ov), CASepaOVConverterUtils
                .getTypeVirement(ov), CASepaOVConverterUtils.getPaysDestination(ov));
    }

    public CASepaGroupeOGKey(String monnaieISO, String typeOrdre, String typeVirement, String paysDest) {
        super();
        this.monnaieISO = monnaieISO;
        this.typeOrdre = typeOrdre;
        this.typeVirement = typeVirement;
        this.paysDest = paysDest;
    }

    /**
     * wrapper de bLevel header avec la clé de regroupement des Clevel
     * 
     * @param bLevel
     */
    public void setbLevel(com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentInstructionInformation3CH bLevel) {
        this.bLevel = bLevel;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return monnaieISO + typeOrdre + typeVirement + paysDest;
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    public PaymentInstructionInformation3CH getbLevel() {
        return bLevel;
    }

}
