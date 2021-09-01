package globaz.ij.acor2020.mapper;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityUtils;
import globaz.globall.db.BSession;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.prestation.acor.PRACORConst;

public class IJIJIndemniteJournaliereMapper {

    public static void baseCalculEtIjMapToIndemniteJournaliere(FCalcul.Cycle.BasesCalcul basesCalcul, IJIJCalculee ijijCalculee, BSession session){

        // On contrôle qu'au moins une indemnité journalière existe
        if(!basesCalcul.getIj().isEmpty()) {
            for (FCalcul.Cycle.BasesCalcul.Ij ij :
                    basesCalcul.getIj()) {
                IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();
                if (PRACORConst.CA_TYPE_MESURE_INTERNE.equals(ij.getCategorie())) {
                    indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
                } else {
                    indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
                }
                indemniteJournaliere.setMontantSupplementaireReadaptation(String.valueOf(ij.getDeduction()));
                indemniteJournaliere.setMontantGarantiAANonReduit(String.valueOf(basesCalcul.getGarantieAANonReduite()));
                indemniteJournaliere.setIndemniteAvantReduction(String.valueOf(basesCalcul.getMontantBase()));
                indemniteJournaliere.setDeductionRenteAI(String.valueOf(basesCalcul.getReductionAI()));
                indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(String.valueOf(basesCalcul.getReductionRevenu()));
                indemniteJournaliere.setMontantJournalierIndemnite(String.valueOf(ij.getMontantIndemnite()));
                indemniteJournaliere.setMontantGarantiAAReduit(String.valueOf(basesCalcul.getMontantGarantiAA()));
                indemniteJournaliere.setIdIJCalculee(ijijCalculee.getIdIJCalculee());
                EntityUtils.saveEntity(indemniteJournaliere, session);
            }
        // Sinon on créé des indemnités interne et externe à 0.-
        } else {
            creerIJIndemniteJournaliere(ijijCalculee.getIdIJCalculee(), String.valueOf(basesCalcul.getReductionAI()), session);
        }
    }

    private static void creerIJIndemniteJournaliere(String idIJCalculee, String reductionAi, BSession session) {
        try {
            // 1ère IJ de type interne
            IJIndemniteJournaliere indemniteJournaliere = creerIndemniteJournaliereSansType();
            indemniteJournaliere.setSession(session);
            indemniteJournaliere.setIdIJCalculee(idIJCalculee);
            indemniteJournaliere.setDeductionRenteAI(reductionAi);
            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
            indemniteJournaliere.add();

            // 2ème IJ de type externe
            indemniteJournaliere = creerIndemniteJournaliereSansType();
            indemniteJournaliere.setSession(session);
            indemniteJournaliere.setIdIJCalculee(idIJCalculee);
            indemniteJournaliere.setDeductionRenteAI(reductionAi);
            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
            indemniteJournaliere.add();
        }catch(Exception e){
            throw new CommonTechnicalException(e);
        }
    }

    /**
     * Création d'indemnité journalière sans type.
     */
    private static IJIndemniteJournaliere creerIndemniteJournaliereSansType() {
        IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();
        indemniteJournaliere.setDeductionRenteAI("0");
        indemniteJournaliere.setFractionReductionSiRevenuAvantReadaptation("0");
        indemniteJournaliere.setIndemniteAvantReduction("0");
        indemniteJournaliere.setMontantComplet("0");
        indemniteJournaliere.setMontantGarantiAANonReduit("0");
        indemniteJournaliere.setMontantGarantiAAReduit("0");
        indemniteJournaliere.setMontantJournalierIndemnite("0");
        indemniteJournaliere.setMontantPlafonne("0");
        indemniteJournaliere.setMontantPlafonneMinimum("0");
        indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation("0");
        indemniteJournaliere.setMontantSupplementaireReadaptation("0");
        return indemniteJournaliere;
    }
}
