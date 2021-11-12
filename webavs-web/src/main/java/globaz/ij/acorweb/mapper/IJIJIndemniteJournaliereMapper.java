package globaz.ij.acorweb.mapper;

import acor.ij.xsd.ij.out.FCalcul;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.common.util.Strings;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.prestation.acor.PRACORConst;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IJIJIndemniteJournaliereMapper {

    private final IJIJCalculee ijijCalculee;
    private final EntityService entityService;

    public void map(FCalcul.Cycle.BasesCalcul basesCalcul){
        // On contrôle qu'au moins une indemnité journalière existe
        if(!basesCalcul.getIj().isEmpty()) {
            for (FCalcul.Cycle.BasesCalcul.Ij ij :
                    basesCalcul.getIj()) {
                IJIndemniteJournaliere indemniteJournaliere = new IJIndemniteJournaliere();
                if (PRACORConst.CA_TYPE_MESURE_INTERNE.equals(Strings.toStringOrNull(ij.getCategorie()))) {
                    indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
                } else {
                    indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
                }
                indemniteJournaliere.setMontantSupplementaireReadaptation(Strings.toStringOrNullDoubleFormat(ij.getDeduction()));
                indemniteJournaliere.setMontantGarantiAANonReduit(Strings.toStringOrNullDoubleFormat(basesCalcul.getGarantieAANonReduite()));
                indemniteJournaliere.setIndemniteAvantReduction(Strings.toStringOrNullDoubleFormat(basesCalcul.getMontantBase()));
                indemniteJournaliere.setDeductionRenteAI(Strings.toStringOrNullDoubleFormat(basesCalcul.getReductionAI()));

                indemniteJournaliere.setMontantReductionSiRevenuAvantReadaptation(Strings.toStringOrNullDoubleFormat(basesCalcul.getReductionRevenu()));
                indemniteJournaliere.setMontantJournalierIndemnite(Strings.toStringOrNullDoubleFormat(ij.getMontantIndemnite()));
                indemniteJournaliere.setMontantGarantiAAReduit(Strings.toStringOrNullDoubleFormat(basesCalcul.getMontantGarantiAA()));
                indemniteJournaliere.setIdIJCalculee(ijijCalculee.getIdIJCalculee());
                entityService.add(indemniteJournaliere);
            }
        // Sinon on créé des indemnités interne et externe à 0.-
        } else {
            creerIJIndemniteJournaliere(ijijCalculee.getIdIJCalculee(), Strings.toStringOrNullDoubleFormat(basesCalcul.getReductionAI()), entityService);
        }
    }

    private void creerIJIndemniteJournaliere(String idIJCalculee, String reductionAi, EntityService entityService) {
        try {
            // 1ère IJ de type interne
            IJIndemniteJournaliere indemniteJournaliere = creerIndemniteJournaliereSansType();
            indemniteJournaliere.setIdIJCalculee(idIJCalculee);
            indemniteJournaliere.setDeductionRenteAI(reductionAi);
            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_INTERNE);
            entityService.add(indemniteJournaliere);

            // 2ème IJ de type externe
            indemniteJournaliere = creerIndemniteJournaliereSansType();
            indemniteJournaliere.setIdIJCalculee(idIJCalculee);
            indemniteJournaliere.setDeductionRenteAI(reductionAi);
            indemniteJournaliere.setCsTypeIndemnisation(IIJMesure.CS_EXTERNE);
            entityService.add(indemniteJournaliere);
        }catch(Exception e){
            throw new CommonTechnicalException(e);
        }
    }

    /**
     * Création d'indemnité journalière sans type.
     */
    private IJIndemniteJournaliere creerIndemniteJournaliereSansType() {
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
