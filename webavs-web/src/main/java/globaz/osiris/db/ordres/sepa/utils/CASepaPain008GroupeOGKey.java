package globaz.osiris.db.ordres.sepa.utils;

import globaz.osiris.api.APIOperationOrdreRecouvrement;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentInstructionInformation4;

/**
 * wrapper of Blevel identify by 4field info/type to identify group to aggregate
 * 
 * @author cel
 * 
 */
public class CASepaPain008GroupeOGKey {

    // key info
    private String monnaieISO;
    private String typeOrdre;
    private String paysDest;
    private String typeTransfert;

    // B-level info
    private PaymentInstructionInformation4 bLevel;

    public CASepaPain008GroupeOGKey(APIOperationOrdreRecouvrement or, CAAdressePaiementFormatter adpf,
            String typeTransfert) throws Exception {

        this(or.getCodeISOMonnaieBonification(), CASepaORConverterUtils.getTypeVersement(), CASepaOVConverterUtils
                .getPaysDestination(adpf), typeTransfert);
    }

    public CASepaPain008GroupeOGKey(String monnaieISO, String typeOrdre, String paysDest, String typeTransfert) {
        super();
        this.monnaieISO = monnaieISO;
        this.typeOrdre = typeOrdre;
        this.paysDest = paysDest;
        this.typeTransfert = typeTransfert;
    }

    /**
     * wrapper de bLevel header avec la clé de regroupement des Clevel
     * 
     * @param bLevel
     */
    public void setbLevel(PaymentInstructionInformation4 bLevel) {
        this.bLevel = bLevel;
    }

    @Override
    public int hashCode() {
        return getKeyString().hashCode();
    }

    public String getKeyString() {
        return monnaieISO + typeOrdre + paysDest + typeTransfert;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CASepaPain008GroupeOGKey) {
            return getKeyString().equals(((CASepaPain008GroupeOGKey) obj).getKeyString());
        }
        return false;

    }

    public PaymentInstructionInformation4 getbLevel() {
        return bLevel;
    }

    /**
     * 
     * @param CASepaOVConverterUtils.PAYS_DESTINATION_SUISSE<br>
     *            CASepaOVConverterUtils.PAYS_DESTINATION_INTERNATIONNAL
     * @return true/false
     */

    public boolean isPaysDestination(String expectedPaysDestination) {
        return paysDest.equals(expectedPaysDestination);
    }

}
