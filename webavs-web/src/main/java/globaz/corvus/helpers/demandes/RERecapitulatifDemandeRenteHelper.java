/*
 * Créé le 12 juil. 07
 */

package globaz.corvus.helpers.demandes;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.dao.REInfoCompta;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.vb.demandes.RERecapitulatifDemandeRenteViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRAssert;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.Iterator;

/**
 * @author HPE
 * 
 */

public class RERecapitulatifDemandeRenteHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RERecapitulatifDemandeRenteViewBean vb = (RERecapitulatifDemandeRenteViewBean) viewBean;

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession((BSession) session);
            demandeRente.setIdDemandeRente(vb.getIdDemandeRente());
            demandeRente.retrieve(transaction);

            REDeleteCascadeDemandeAPrestationsDues.supprimerDemandeRenteCascade_noCommit((BSession) session,
                    transaction, demandeRente, IREValidationLevel.VALIDATION_LEVEL_NONE);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
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

    public FWViewBeanInterface validerHistorique(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        BITransaction transaction = null;
        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            RERecapitulatifDemandeRenteViewBean vb = (RERecapitulatifDemandeRenteViewBean) viewBean;
            REDemandeRente dem = new REDemandeRente();
            dem.setIdDemandeRente(vb.getIdDemandeRente());
            dem.retrieve(transaction);
            PRAssert.notIsNew(dem, null);
            // MAJ de la demande dans l'état VALIDE
            dem.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
            dem.update(transaction);

            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(dem.getIdRenteCalculee());
            rc.retrieve(transaction);
            PRAssert.notIsNew(rc, null);

            // MAJ des rentes accordées dans l'état VALIDE
            RERenteAccJoinTblTiersJoinDemRenteManager mgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
            mgr.setSession(session);
            mgr.setForNoDemandeRente(dem.getIdDemandeRente());
            mgr.find(transaction);

            boolean allRenteAccordeeClosed = true;
            JADate dateFin = null;

            for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) iterator.next();

                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(session);
                ra.setIdPrestationAccordee(elm.getIdPrestationAccordee());
                ra.retrieve(transaction);
                PRAssert.notIsNew(ra, null);

                // la rente accordee est en cours, on controle l'adresse de
                // paiement
                if (JadeStringUtil.isEmpty(ra.getDateFinDroit())) {

                    REInformationsComptabilite infoCompt = ra.loadInformationsComptabilite();

                    if (infoCompt == null || infoCompt.isNew()) {
                        throw new Exception(session.getLabel("ERREUR_ADRESSE_PAIEMENT_RA"));
                    }

                    // Si le domaine rente n'existe pas, on récupère le domaine
                    // standard.
                    TIAdressePaiementData adr = PRTiersHelper.getAdressePaiementData(session,
                            (BTransaction) transaction, infoCompt.getIdTiersAdressePmt(),
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

                    if (adr == null || adr.isNew()) {
                        throw new Exception(session.getLabel("ERREUR_ADRESSE_PAIEMENT_RA"));
                    }
                }

                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                ra.update(transaction);

                if (JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
                    allRenteAccordeeClosed = false;
                } else {
                    // pour la date de fin, il faut prendre le dernier jour du
                    // mois et non pas le premier
                    // comme le fait les JADate
                    JACalendar cal = new JACalendarGregorian();
                    JADate dfCourant = cal.addMonths(new JADate(ra.getDateFinDroit()), 1);
                    dfCourant = cal.addDays(dfCourant, -1);

                    if (dateFin == null || cal.compare(dateFin, dfCourant) == JACalendar.COMPARE_SECONDUPPER) {
                        dateFin = dfCourant;
                    }
                }

                // Création du compta annexe si non existant, et maj des données
                // dans INFOCOMPTA!!!
                REInformationsComptabilite ic = new REInformationsComptabilite();
                ic.setSession(session);
                ic.setIdInfoCompta(ra.getIdInfoCompta());
                ic.retrieve(transaction);
                PRAssert.notIsNew(ic, null);

                if (JadeStringUtil.isBlankOrZero(ic.getIdCompteAnnexe())) {
                    REInfoCompta.initCompteAnnexe_noCommit(session, transaction, ra.getIdTiersBeneficiaire(), ic,
                            IREValidationLevel.VALIDATION_LEVEL_ALL);
                }

                // Récupération des prestations dues de type $p...
                REPrestationsDuesManager mgr2 = new REPrestationsDuesManager();
                mgr2.setSession(session);
                mgr2.setForIdRenteAccordes(ra.getIdPrestationAccordee());
                mgr2.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                mgr2.find(transaction, BManager.SIZE_NOLIMIT);
                for (Iterator iterator2 = mgr2.iterator(); iterator2.hasNext();) {
                    REPrestationDue pd = (REPrestationDue) iterator2.next();
                    // Si la prestation due à une date de fin, la mettre dans
                    // l'état TRAITE.
                    // Si la prestation due n'a pas de date de fin, la mettre
                    // dans l'état ACTIF.

                    if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                        pd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                    } else {
                        pd.setCsEtat(IREPrestationDue.CS_ETAT_TRAITE);
                    }
                    pd.update(transaction);
                }
            }

            // MAJ de la date de fin de la demande
            if (allRenteAccordeeClosed && dateFin != null) {
                dem.setDateFin(dateFin.toStr("."));
                dem.update(transaction);
            }

            return viewBean;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

}
