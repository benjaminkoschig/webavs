package globaz.osiris.helpers.retours;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.osiris.db.retours.CALignesRetours;
import globaz.osiris.db.retours.CARetoursViewBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author BSC
 * 
 */

public class CARetoursHelper extends FWHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final Class[] PARAMS = new Class[] { FWViewBeanInterface.class, FWAction.class, BSession.class };

    /**
     */
    public static final String SET_SESSION = "setSession";

    // ~ Fields
    // -------------------------------------------------------------------------------------

    protected boolean hasBeanInsertedIntoTiers = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public FWViewBeanInterface actionAjouterLignesRetoursSurAdressePaiement(FWViewBeanInterface viewBean,
            FWAction action, BSession session) throws Exception {

        CARetoursViewBean retour = (CARetoursViewBean) viewBean;

        CALignesRetours ligneRetour = new CALignesRetours();
        ligneRetour.setSession(session);
        ligneRetour.setIdTiers(retour.getIdTiersLigneRetourSurAdressePaiement());
        ligneRetour.setIdExterne(retour.getNumAffilieLigneRetourSurAdressePaiement());
        ligneRetour.setIdDomaine(retour.getIdDomaineLigneRetourSurAdressePaiement());
        ligneRetour.setMontant(retour.getMontantLigneRetourSurAdressePaiement());
        ligneRetour.setIdRetour(retour.getIdRetour());
        ligneRetour.setCsType(CALignesRetours.CS_ETAT_LIGNE_REPAIEMENT);

        ligneRetour.add();

        // si il y a une erreure, on garde les ids de l'adresse de paiement
        if (!ligneRetour.isOnError()) {
            retour.setIdTiersLigneRetourSurAdressePaiement("");
            retour.setIdDomaineLigneRetourSurAdressePaiement("");
        }

        return retour;
    }

    public FWViewBeanInterface actionAjouterLignesRetoursSurSection(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        CARetoursViewBean retour = (CARetoursViewBean) viewBean;

        CALignesRetours ligneRetour = new CALignesRetours();
        ligneRetour.setSession(session);
        ligneRetour.setIdSection(retour.getIdSectionLigneRetourSurSection());
        ligneRetour.setMontant(retour.getMontantLigneRetourSurSection());
        ligneRetour.setIdRetour(retour.getIdRetour());
        ligneRetour.setCsType(CALignesRetours.CS_ETAT_LIGNE_COMPENSATION);

        ligneRetour.add();

        // si il y a une erreure, on garde l'id de la section
        if (!ligneRetour.isOnError()) {
            retour.setIdSectionLigneRetourSurSection("");
        }

        return retour;
    }

    public FWViewBeanInterface actionCreerRetourSplit(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        CARetoursViewBean retourOriginal = (CARetoursViewBean) viewBean;
        retourOriginal.setModeCreerRetourSplit(true);

        try {
            retourOriginal.update();
        } finally {
            retourOriginal.setModeCreerRetourSplit(false);
        }

        return retourOriginal;

    }

    // Bug 6227 - La saisie du retour, comptabilisation du retour et de l'OV ne prennent pas la même date
    public FWViewBeanInterface actionListerLignesRetoursSurAdressePaiement(FWViewBeanInterface viewBean,
            FWAction action, BSession session) throws Exception {
        return viewBean;
    }

    // public FWViewBeanInterface actionAfficherPeriodeINV(FWViewBeanInterface
    // viewBean,
    // FWAction action,
    // BSession session) throws Exception {
    //
    // RESaisieDemandeRenteViewBean vb = (RESaisieDemandeRenteViewBean)viewBean;
    //
    // TreeSet listePeriodeINV = vb.getPeriodesInvalidite();
    //
    // REPeriodeInvaliditeViewBean periodeInvDelete = new
    // REPeriodeInvaliditeViewBean();
    //
    // // Reprendre la bonne entité de la liste et la setter dans le viewBean
    // for (Iterator iter = listePeriodeINV.iterator(); iter.hasNext();) {
    // REPeriodeInvaliditeViewBean periodeINVVB = (REPeriodeInvaliditeViewBean)
    // iter.next();
    //
    // if
    // (periodeINVVB.getIdProvisoire()==vb.getIdProvisoirePeriodeINV().intValue()){
    //
    // vb.setDateDebutInvalidite(periodeINVVB.getDateDebutInvalidite());
    // vb.setDateFinInvalidite(periodeINVVB.getDateFinInvalidite());
    // vb.setDegreInvalidite(periodeINVVB.getDegreInvalidite());
    // vb.setIdPeriodeInvalidite(periodeINVVB.getIdPeriodeInvalidite());
    //
    // periodeInvDelete = periodeINVVB;
    //
    // }
    //
    // }
    //
    // listePeriodeINV.remove(periodeInvDelete);
    //
    // vb.setModifie(true);
    //
    // return vb;
    // }

    // Bug 6227 - La saisie du retour, comptabilisation du retour et de l'OV ne prennent pas la même date
    public FWViewBeanInterface actionListerLignesRetoursSurSection(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {
        return viewBean;
    }

    public FWViewBeanInterface actionSupprimerLignesRetoursSurAdressePaiement(FWViewBeanInterface viewBean,
            FWAction action, BSession session) throws Exception {

        CARetoursViewBean vb = (CARetoursViewBean) viewBean;

        CALignesRetours lr = new CALignesRetours();
        lr.setSession(session);
        lr.setIdLigneRetour(vb.getSelectedId());
        lr.retrieve();

        lr.delete();

        return viewBean;
    }

    public FWViewBeanInterface actionSupprimerLignesRetoursSurSection(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        CARetoursViewBean vb = (CARetoursViewBean) viewBean;

        CALignesRetours lr = new CALignesRetours();
        lr.setSession(session);
        lr.setIdLigneRetour(vb.getSelectedId());
        lr.retrieve();

        lr.delete();

        return viewBean;
    }

    /**
     * recherche une méthode PUBLIQUE de cette classe qui porte le nom de la partie action de l'instance 'action' et
     * prend les mêmes paramètres que cette méthode sauf pour BSession a la place de BISession et l'invoke.
     * 
     * <p>
     * le tout est fait dans un bloc try...catch, il est donc possible de lancer une exception dans ces méthodes. Cette
     * méthode retourne ensuite le viewBean retourné par ladite méthode ou celui transis en argument si la méthode n'a
     * pas de return value.
     * </p>
     * 
     * <p>
     * si la partie action contient la chaine SET_ACTION, la session est simplement settee dans le viewBean et celui-ci
     * est retourné.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ClassCastException
     *             si session n'est pas une instance de BSession
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    protected FWViewBeanInterface deleguerExecute(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws ClassCastException {
        if (CARetoursHelper.SET_SESSION.equals(action.getActionPart())) {
            viewBean.setISession(session);

            return viewBean;
        } else {
            try {
                Method method = this.getClass().getMethod(action.getActionPart(), CARetoursHelper.PARAMS);
                Object retValue = method.invoke(this, new Object[] { viewBean, action, (BSession) session });

                if ((retValue != null) && (retValue instanceof FWViewBeanInterface)) {
                    return (FWViewBeanInterface) retValue;
                }
            } catch (InvocationTargetException ie) {
                if (ie.getTargetException() != null) {
                    ((BSession) session).addError(ie.getTargetException().getMessage());
                } else {
                    ((BSession) session).addError(ie.getMessage());
                }
            } catch (Exception e) {
                ((BSession) session).addError(e.getMessage());
            }

            return viewBean;
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
