package globaz.apg.business.service;

import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.pojo.APBreakRulesFromView;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.apg.vb.droits.APDroitPanSituationViewBean;
import globaz.apg.vb.droits.APDroitPanViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.db.demandes.PRDemande;
import java.util.List;

public interface APEntityService extends JadeApplicationService {

    /**
     * Créer l'intégralité du graphe d'objet lié au droit. En cas d'exception la transaction n'est pas rollbackée !!!
     */
    APDroitAPG creerDroitAPGComplet(BSession session, BTransaction transaction, APDroitAPGPViewBean viewBean)
            throws IllegalArgumentException;

    APDroitPandemie creerDroitPanComplet(BSession session, BTransaction transaction, APDroitPanViewBean viewBean)
            throws IllegalArgumentException;

    APDroitPanSituation creerDroitPanSituation(BSession session, BTransaction transaction, APDroitPanSituationViewBean viewBean);

    /** Recherche si le tiers possède des droits pandémie datant d'avant la 2ème vague (<17.09.2020) et si c'est le cas, la
     * méthode va récupérer la ou les situations profesionnelles de cet ancien droit et les créer pour le nouveau droit (en paramètre).
     * Si des situations professionnelles sont créées pour le nouveau droit, alors on retourne TRUE, sinon FALSE
     * @param transaction
     * @param droit
     * @param idTiers
     * @return TRUE si des situations professionnelles ont été créées pour le droit passé en paramètre
     */
    boolean creerSituationProfPanSelonDroitPrecedent(BSession session, final BTransaction transaction, final APDroitLAPG droit, final String idTiers) throws Exception;

    /**
     * Retourne la demande lié au droit LAPG
     * 
     * @param session
     *            La session à utiliser
     * @param transaction
     *            La transaction à utiliser
     * @param idDroit
     *            L'id du droit lié à la deamnde à récupérer
     * @return La demande liée au droit
     * @throws Exception
     */
    PRDemande getDemandeDuDroit(BSession session, BTransaction transaction, String idDroit) throws Exception;

    /**
     * Retourne dans tous les cas le droit APG demandé, sinon une exception sera lancée
     */
    APDroitAPG getDroitAPG(BSession session, BTransaction transaction, String idDroit) throws Exception,
            APEntityNotFoundException;

    /**
     * Retourne le droit demande dans le bon type, APDroitAPG ou APDroitMaternite sinon une exception sera lancée
     * 
     * @param session
     * @param idDroit
     * @return
     * @throws Exception
     * @throws APEntityNotFoundException
     */
    APDroitLAPG getDroitLAPG(BSession session, BTransaction transaction, String idDroit) throws Exception,
            APEntityNotFoundException;

    /**
     * Retourne dans tous les cas le droit maternité demandé, sinon une exception sera lancée
     */
    APDroitMaternite getDroitMaternite(BSession session, BTransaction transaction, String idDroit)
            throws Exception, APEntityNotFoundException;

    /**
     * Retourne le droit demande dans le bon type, APDroitPandemie  sinon une exception sera lancée
     *
     * @param session
     * @param idDroit
     * @return
     * @throws Exception
     * @throws APEntityNotFoundException
     */
    APDroitPandemie getDroitPandemie(BSession session, BTransaction transaction, String idDroit) throws Exception,
            APEntityNotFoundException;

    /**
     * Retourne les enfants du droit APG
     */
    List<APEnfantAPG> getEnfantsAPGDuDroitAPG(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les périodes du droit APG</br> Si le droit n'existe pas ou si le droit est de type maternité, une
     * exception sera lancé
     * 
     * @param session
     * @param transaction
     * @param idDroit
     * @return
     * @throws Exception
     *             Si le droit n'existe pas ou si le droit est de type maternité, une exception sera lancé
     */
    List<APPeriodeAPG> getPeriodesDuDroitAPG(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les prestations du droit APG ou Maternité
     * 
     * @return
     */
    List<APPrestation> getPrestationDuDroit(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les prestation jointe aux répartition du droit APG ou Maternité La répartition joint à la prestation est
     * bien la répartition (par opposition aux ventilation)
     * 
     * @return
     */
    List<APRepartitionJointPrestation> getRepartitionJointPrestationDuDroit(BSession session,
            BTransaction transaction, String idDroit) throws Exception;

    List<APRepartitionPaiements> getRepartitionsPaiementsDuDroit(final BSession session,
            final BTransaction transaction, final String idDroit) throws Exception;

    /**
     * Retourne les situations profesionnelles du droit
     * 
     * @param session
     * @param transaction
     * @param idDroit
     * @return
     * @throws Exception
     */
    List<APSitProJointEmployeur> getSituationProfJointEmployeur(BSession session, BTransaction transaction,
            String idDroit) throws Exception;

    APDroitAPG miseAjourDroit(BSession session, BTransaction transaction, APDroitAPGPViewBean viewBean)
            throws Exception;

    APDroitPandemie miseAjourDroitPan(BSession session, BTransaction transaction,
                                 APDroitPanViewBean viewBean) throws Exception;

    /**
     * @param session
     * @param idDroit
     * @throws Exception
     * @throws APEntityNotFoundException
     */
    void remplacerBreakRulesDuDroit(BSession session, BTransaction transaction, String idDroit,
            List<APBreakRulesFromView> nouvellesBreakRules) throws Exception;

    /**
     * Supprime les périodes existantes du droit APG et recréer les nouvelles périodes
     */
    void remplacerPeriodesDroitAPG(BSession session, BTransaction transaction, String idDroit,
            List<PRPeriode> nouvellesPeriodes) throws Exception, APEntityNotFoundException;

    /**
     * Supprime l'intégralité du graphe d'objet lié au droit
     */
    void supprimerDroitCompletAPG(BSession session, BTransaction transaction, String idDroit) throws Exception;
    /**
     * Supprime l'intégralité du graphe d'objet lié au droit
     */
    void supprimerDroitCompletPan(BSession session, BTransaction transaction, String idDroit) throws Exception;

    void supprimerLesBreakRulesDuDroit(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Supprime l'intégralité du graphe d'objet lié aux prestations d'un droit APG
     */
    void supprimerLesPrestationsDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception;

    /**
     * Supprime l'intégralité du graphe d'objet lié aux prestations d'un droit APG par genre
     */
    void supprimerLesPrestationsDuDroitParGenre(final BSession session, final BTransaction transaction,
                                        final String idDroit, String genre) throws Exception;

    APDroitPanSituation getDroitPanSituation(BSession session, BTransaction transaction, String idDroitSituation) throws Exception;

    APDroitPanSituation miseAjourDroitPanSituation(BSession session, BTransaction transaction, APDroitPanSituationViewBean viewBean) throws Exception;

    int getTotalJourAutreDroit(BSession session, String idDroit) throws Exception;

    List<String> getAutresDroits(BSession session, String idDroit) throws Exception;
}
