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
     * Cr�er l'int�gralit� du graphe d'objet li� au droit. En cas d'exception la transaction n'est pas rollback�e !!!
     */
    public APDroitAPG creerDroitAPGComplet(BSession session, BTransaction transaction, APDroitAPGPViewBean viewBean)
            throws IllegalArgumentException;

    /**
     * Retourne la demande li� au droit LAPG
     * 
     * @param session
     *            La session � utiliser
     * @param transaction
     *            La transaction � utiliser
     * @param idDroit
     *            L'id du droit li� � la deamnde � r�cup�rer
     * @return La demande li�e au droit
     * @throws Exception
     */
    public PRDemande getDemandeDuDroit(BSession session, BTransaction transaction, String idDroit) throws Exception;

    /**
     * Retourne dans tous les cas le droit APG demand�, sinon une exception sera lanc�e
     */
    public APDroitAPG getDroitAPG(BSession session, BTransaction transaction, String idDroit) throws Exception,
            APEntityNotFoundException;

    /**
     * Retourne le droit demande dans le bon type, APDroitAPG ou APDroitMaternite sinon une exception sera lanc�e
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
     * Retourne dans tous les cas le droit maternit� demand�, sinon une exception sera lanc�e
     */
    public APDroitMaternite getDroitMaternite(BSession session, BTransaction transaction, String idDroit)
            throws Exception, APEntityNotFoundException;

    /**
     * Retourne les enfants du droit APG
     */
    public List<APEnfantAPG> getEnfantsAPGDuDroitAPG(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les p�riodes du droit APG</br> Si le droit n'existe pas ou si le droit est de type maternit�, une
     * exception sera lanc�
     * 
     * @param session
     * @param transaction
     * @param idDroit
     * @return
     * @throws Exception
     *             Si le droit n'existe pas ou si le droit est de type maternit�, une exception sera lanc�
     */
    public List<APPeriodeAPG> getPeriodesDuDroitAPG(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les prestations du droit APG ou Maternit�
     * 
     * @return
     */
    public List<APPrestation> getPrestationDuDroit(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Retourne les prestation jointe aux r�partition du droit APG ou Maternit� La r�partition joint � la prestation est
     * bien la r�partition (par opposition aux ventilation)
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
     * Supprime les p�riodes existantes du droit APG et recr�er les nouvelles p�riodes
     */
    public void remplacerPeriodesDroitAPG(BSession session, BTransaction transaction, String idDroit,
            List<PRPeriode> nouvellesPeriodes) throws Exception, APEntityNotFoundException;

    /**
     * Supprime l'int�gralit� du graphe d'objet li� au droit
     */
    public void supprimerDroitCompletAPG(BSession session, BTransaction transaction, String idDroit) throws Exception;

    public void supprimerLesBreakRulesDuDroit(BSession session, BTransaction transaction, String idDroit)
            throws Exception;

    /**
     * Supprime l'int�gralit� du graphe d'objet li� aux prestations d'un droit APG
     */
    public void supprimerLesPrestationsDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception;
}