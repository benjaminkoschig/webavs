/**
 *
 */
package globaz.apg.business.service;

import java.util.List;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.exceptions.APPlausibilitesException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.pojo.APErreurValidationPeriode;
import globaz.apg.pojo.APValidationPrestationAPGContainer;
import globaz.apg.pojo.ViolatedRule;
import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author dde
 */
public interface APPlausibilitesApgService extends JadeApplicationService {

    // /**
    // * Contrôle des toutes les plausibilités (après modification manuelle)
    // *
    // * @param champsAnnonce
    // * @return Liste des régles violées
    // */
    // public List<ViolatedRule> checkAll(APChampsAnnonce annonce) throws APPlausibilitesException;

    /**
     * Contrôle des plausibilités APG de l'étape 1
     *
     * @param champsAnnonce
     * @return Liste des régles violées
     */
    public List<ViolatedRule> checkAnnonce(BSession session, APValidationPrestationAPGContainer container,
            PRTiersWrapper tiers) throws APPlausibilitesException;

    /**
     * Validation des plausibilités contrôlées dans le XSD
     *
     * @param champsAnnonce
     * @return Liste de messages d'erreur
     */
    public List<String> checkPlausisXSD(APChampsAnnonce champsAnnonce, BSession session)
            throws APPlausibilitesException;

    // /**
    // * Contrôle des plausibilités APG de l'étape 2
    // *
    // * @param champsAnnonce
    // * @return Liste des régles violées
    // */
    // public List<ViolatedRule> checkStep2(IAPChampsAnnonce champsAnnonce) throws PlausibilitesException;
    //
    // /**
    // * Contrôle des plausibilités APG de l'étape 3
    // *
    // * @param champsAnnonce
    // * @return Liste des régles violées
    // */
    // public List<ViolatedRule> checkStep3(IAPChampsAnnonce champsAnnonce) throws PlausibilitesException;

    /**
     * Contrôle des plausibilités APG juste avant d'envoyer l'annonce à la centrale
     *
     * @param champsAnnonce
     * @return Liste des régles violées
     */
    public List<ViolatedRule> checkStepSendAnnonce(BSession session, APChampsAnnonce champsAnnonce)
            throws APPlausibilitesException;

    List<APErreurValidationPeriode> controllerPrestationEnFonctionPeriodes(BSession session, APDroitLAPG droit,
            List<APPeriodeAPG> periodesAPG, List<APPrestation> prestations);

    public List<String> controllerPrestationsJoursIsoles(BSession session, List<APPrestation> prestations,
            APDroitLAPG droit);
}
