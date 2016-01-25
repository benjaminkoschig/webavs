package globaz.apg.business.service;

import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.pojo.APBreakRulesFromView;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
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
    public APDroitAPG creerDroitAPGComplet(BSession session, BTransaction transaction, APDroitAPGPViewBean viewBean)
            throws IllegalArgumentException;

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
    public PRDemande getDemandeDuDroit(BSession session, BTransaction transaction, String idDroit) throws Exception;

    /**
     * Retourne dans tous les cas le droit APG demandé, sinon une exception sera lancée
     */
    public APDroitAPG getDroitAPG(BSession session, BTransaction transaction, String idDroit) throws Exception,
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
    public APDroitLAPG getDroitLAPG(BSession session, BTransaction transaction, String idDroit) throws Exception,
            APEntityNotFoundException;

    /**
     * Retourne dans tous les cas le droit maternité demandé, sinon une exception sera lancée
     */
    public APDroitMaternite getDroitMaternite(BSession session, BTransaction transaction, String idDroit)
            throws Exception, APEntityNotFoundException;

    /**
     * Retourne les enfants du droit APG
     */
    public List<APEnfantAPG> getEnfantsAPGDuDroitAPG(BSession session, BTransaction transaction, String idDroit)
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
    public List<APPeriodeAPG> getPeriodesDuDroitAPG(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les prestations du droit APG ou Maternité
     * 
     * @return
     */
    public List<APPrestation> getPrestationDuDroit(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les prestation jointe aux répartition du droit APG ou Maternité La répartition joint à la prestation est
     * bien la répartition (par opposition aux ventilation)
     * 
     * @return
     */
    public List<APRepartitionJointPrestation> getRepartitionJointPrestationDuDroit(BSession session,
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
    public List<APSitProJointEmployeur> getSituationProfJointEmployeur(BSession session, BTransaction transaction,
            String idDroit) throws Exception;

    public APDroitAPG miseAjourDroit(BSession session, BTransaction transaction, APDroitAPGPViewBean viewBean)
            throws Exception;

    /**
     * @param session
     * @param idDroit
     * @throws Exception
     * @throws APEntityNotFoundException
     */
    public void remplacerBreakRulesDuDroit(BSession session, BTransaction transaction, String idDroit,
            List<APBreakRulesFromView> nouvellesBreakRules) throws Exception;

    /**
     * Supprime les périodes existantes du droit APG et recréer les nouvelles périodes
     */
    public void remplacerPeriodesDroitAPG(BSession session, BTransaction transaction, String idDroit,
            List<PRPeriode> nouvellesPeriodes) throws Exception, APEntityNotFoundException;

    /**
     * Supprime l'intégralité du graphe d'objet lié au droit
     */
    public void supprimerDroitCompletAPG(BSession session, BTransaction transaction, String idDroit) throws Exception;

    public void supprimerLesBreakRulesDuDroit(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Supprime l'intégralité du graphe d'objet lié aux prestations d'un droit APG
     */
    public void supprimerLesPrestationsDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception;
}