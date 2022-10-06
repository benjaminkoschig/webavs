package globaz.osiris.process.ebill;

import globaz.aquila.print.CO00CSommationPaiement;
import globaz.aquila.print.CO04ReceptionPaiement;
import globaz.aquila.print.CODecision;
import globaz.musca.itext.FAImpressionFacture_BVR_Doc;
import globaz.musca.process.FAImpressionFactureEBillProcess;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvBVR4;

public enum EBillTypeDocument {
    RECLAMATION_FRAIS_ET_INTERET(CO04ReceptionPaiement.NUMERO_REFERENCE_INFOROM, CO04ReceptionPaiement.class.getSimpleName()),
    SOMMATION(CO00CSommationPaiement.NUMERO_REFERENCE_INFOROM, CO00CSommationPaiement.class.getSimpleName()),
    DECISION(CODecision.NUMERO_REFERENCE_INFOROM, CODecision.class.getSimpleName()),
    SURSIS(CAILettrePlanRecouvBVR4.NUMERO_REFERENCE_INFOROM, CAILettrePlanRecouvBVR4.class.getSimpleName()),
    BULLETIN_DE_SOLDES(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE, CAImpressionBulletinsSoldes_Doc.class.getSimpleName()),
    FACTURE(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE, FAImpressionFactureEBillProcess.class.getSimpleName());

    private String classeImplementation;
    private String numeroInforom;

    EBillTypeDocument(String classeImplementation, String numeroInforom) {
        this.classeImplementation = classeImplementation;
        this.numeroInforom = numeroInforom;
    }

    public String getClasseImplementation() {
        return classeImplementation;
    }

    public String getNumeroInforom() {
        return numeroInforom;
    }
}