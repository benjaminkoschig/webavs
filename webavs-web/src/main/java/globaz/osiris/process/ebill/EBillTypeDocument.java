package globaz.osiris.process.ebill;

import globaz.aquila.print.CO00CSommationPaiement;
import globaz.musca.itext.FAImpressionFacture_BVR_Doc;
import globaz.musca.process.FAImpressionFactureEBillProcess;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvBVR4;

public enum EBillTypeDocument {
    SOMMATION(CO00CSommationPaiement.NUMERO_REFERENCE_INFOROM, CO00CSommationPaiement.class.getSimpleName()),
    SURSIS(CAILettrePlanRecouvBVR4.NUMERO_REFERENCE_INFOROM, CAILettrePlanRecouvBVR4.class.getSimpleName()),
    BULLETIN_DE_SOLDES(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE, CAImpressionBulletinsSoldes_Doc.class.getSimpleName()),
    FACTURE(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE, FAImpressionFactureEBillProcess.class.getSimpleName());

    private String classeImnplementation;
    private String numeroInforom;

    EBillTypeDocument(String classeImnplementation, String numeroInforom) {
        this.classeImnplementation = classeImnplementation;
        this.numeroInforom = numeroInforom;
    }

    public String getClasseImnplementation() {
        return classeImnplementation;
    }

    public String getNumeroInforom() {
        return numeroInforom;
    }
}