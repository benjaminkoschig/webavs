/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul.rev2005;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.services.APRechercherAssuranceFromDroitCotisationService;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.api.IAFAssurance;

/**
 * @author scr
 *
 *         Calcul du service normal
 */
public class APModuleCalculAllocJourIsole extends AAPModuleCalculSalaireJournalier {

    private static final BigDecimal DIVISION_CALCUL_JOUR_ISOLE = new BigDecimal("21.75");

    /**
     *
     */
    public APModuleCalculAllocJourIsole() {
    }

    public APResultatCalcul calculerMontantAllocation(APBaseCalcul baseCalcul, IAPReferenceDataPrestation refData,
            BSession session) throws Exception {
        APResultatCalcul result = super.calculerMontantAllocation(baseCalcul, refData);
        result.setCsGenrePrestion(APTypeDePrestation.JOUR_ISOLE.getCodesystemString());

        // Récupération du canton venant de la première situation professionnelle
        ECanton enumCant = getCantonFromSituationProf(baseCalcul, session);
        BigDecimal salaireMax = retrieveSalaireMaxFromCanton(enumCant, baseCalcul, session);

        FWCurrency revenuDeterminantMoyen = result.getRevenuDeterminantMoyen();
        BigDecimal salaireMensuel = new BigDecimal(revenuDeterminantMoyen.toString()).multiply(new BigDecimal(30));
        BigDecimal salaireJournalier = arrondir(
                salaireMensuel.divide(DIVISION_CALCUL_JOUR_ISOLE, 2, RoundingMode.HALF_UP));

        if (salaireJournalier.compareTo(salaireMax) >= 1) {
            salaireJournalier = salaireMax;
        }

        // Diviser le montant journalier par deux car c'est un demi jour et non un jour complet
        if (IAPDroitLAPG.CS_DECES_DEMI_JOUR_CIAB.equals(baseCalcul.getTypeAllocation())) {
            salaireJournalier = salaireJournalier.divide(new BigDecimal(2));
        }

        result.setMontantJournalier(new FWCurrency(salaireJournalier.toString()));
        return result;
    }

    private BigDecimal retrieveSalaireMaxFromCanton(ECanton enumCant, APBaseCalcul baseCalcul, BSession session)
            throws Exception {
        BigDecimal salaireMax = BigDecimal.ZERO;
        if (enumCant != null) {
            if (ECanton.JU.name().equals(enumCant.name())) {
                salaireMax = new BigDecimal(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1",
                        EMontantsMax.COMCIABJUA.getValue(), baseCalcul.getDateDebut().toString(), "", 0));
            } else if (ECanton.BE.name().equals(enumCant.name())) {
                salaireMax = new BigDecimal(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1",
                        EMontantsMax.COMCIABBEA.getValue(), baseCalcul.getDateDebut().toString(), "", 0));
            }
        }
        return salaireMax;
    }

    private ECanton getCantonFromSituationProf(APBaseCalcul baseCalcul, BSession session) throws Exception {
        // Récupération des deux propriétés
        String idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        String idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);

        String idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        String idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

        // Parcours des IDs des situations professionnelles de la base de calcul
        for (Iterator iter = baseCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {

            APBaseCalculSituationProfessionnel bcSitProf = (APBaseCalculSituationProfessionnel) iter.next();

            // on recherche les situation professionnelle de ce droit
            APSituationProfessionnelleManager sitProManager = new APSituationProfessionnelleManager();
            sitProManager.setSession(session);
            sitProManager.setForIdSituationProfessionnelle(bcSitProf.getIdSituationProfessionnelle());
            sitProManager.find(BManager.SIZE_NOLIMIT);

            Iterator iterSitPro = sitProManager.iterator();

            while (iterSitPro.hasNext()) {
                APSituationProfessionnelle sitPro = (APSituationProfessionnelle) iterSitPro.next();

                List<IAFAssurance> listAssurance = APRechercherAssuranceFromDroitCotisationService
                        .rechercher(sitPro.getIdDroit(), bcSitProf.getIdAffilie(), session);

                for (IAFAssurance assurance : listAssurance) {
                    if ((assurance.getAssuranceId().equals(idAssuranceParitaireBE) && !sitPro.getIsIndependant())
                            || (assurance.getAssuranceId().equals(idAssurancePersonnelBE) && sitPro.getIsIndependant())) {
                        return ECanton.BE;
                    } else if ((assurance.getAssuranceId().equals(idAssuranceParitaireJU) && !sitPro.getIsIndependant())
                            || (assurance.getAssuranceId().equals(idAssurancePersonnelJU) && sitPro.getIsIndependant())) {
                        return ECanton.JU;
                    }
                }
            }
        }
        return null;
    }

    /**
     * arrondi à 2 chiffres après la virgule, à 5cts près.
     *
     * @param montant
     * @return
     */
    public BigDecimal arrondir(BigDecimal montant) {
        // arrondi à 2 chiffres après la virgule, à 5cts près.
        return new BigDecimal(JANumberFormatter
                .deQuote(JANumberFormatter.format(montant.toString(), 0.05, 2, JANumberFormatter.NEAR)));
    }

}
