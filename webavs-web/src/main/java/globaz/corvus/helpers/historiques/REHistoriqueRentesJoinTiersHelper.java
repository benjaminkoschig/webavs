/*
 * Créé le 29 juin 07
 */

package globaz.corvus.helpers.historiques;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.historiques.REHistoriqueRentes;
import globaz.corvus.db.historiques.REHistoriqueRentesJoinTiersManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersListViewBean;
import globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRAssert;

/**
 * 
 * @author SCR
 * 
 */

public class REHistoriqueRentesJoinTiersHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public static String doReloadHistorique(BSession session, BTransaction transaction,
            REHistoriqueRentesJoinTiersViewBean vb) throws Exception {

        String listIdTiers = "";

        String idTiersRequerant = vb.getIdTiersRequerant();
        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);
        ISFMembreFamilleRequerant[] membresFamille = null;
        try {
            membresFamille = sf.getMembresFamille(idTiersRequerant);
        }
        // Peut arriver lorsque l'on change de bénéficiaire, et que ce dernier n'existe pas dans les familles.
        catch (Exception e) {
            ;
        }
        listIdTiers = idTiersRequerant;

        if (membresFamille != null) {
            for (int i = 0; i < membresFamille.length; i++) {
                ISFMembreFamilleRequerant mbr = membresFamille[i];
                if (!JadeStringUtil.isBlankOrZero(mbr.getIdTiers())) {
                    if (!idTiersRequerant.equals(mbr.getIdTiers())) {
                        listIdTiers += ", " + mbr.getIdTiers();
                    }
                }
            }
        }
        if (!vb.isReloadHistorique()) {
            vb.setIdTiersIn(listIdTiers);
            return listIdTiers;
        }

        REHistoriqueRentesJoinTiersManager mgr = new REHistoriqueRentesJoinTiersManager();
        mgr.setSession(session);
        mgr.setForIdTiersIn(listIdTiers);
        mgr.setForIsRefSurRenteAccordeeRenseignee(Boolean.TRUE);
        mgr.setForIsModifie(Boolean.FALSE);
        mgr.setForIsEnvoyerAcor(Boolean.TRUE);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.size(); i++) {
            REHistoriqueRentes hist = (REHistoriqueRentes) mgr.get(i);
            hist.delete(transaction);
        }

        RERenteAccordeeManager mgr2 = new RERenteAccordeeManager();
        mgr2.setSession(session);
        mgr2.setForIdTiersBeneficiaireIn(listIdTiers);
        mgr2.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                + IREPrestationAccordee.CS_ETAT_DIMINUE);
        mgr2.setForDateDebutAvantDateFin(Boolean.TRUE);
        mgr2.find(transaction, BManager.SIZE_NOLIMIT);

        for (int j = 0; j < mgr2.size(); j++) {
            RERenteAccordee ra = (RERenteAccordee) mgr2.get(j);

            // S'il s'agit d'une RA de type API, on ne la prend pas en compte
            if (ra.isRAAPI().booleanValue()) {
                continue;
            }

            // On contrôle si cette RA est déjà référencée dans l'historique...
            REHistoriqueRentesJoinTiersManager mgr3 = new REHistoriqueRentesJoinTiersManager();
            mgr3.setSession(session);
            mgr3.setForIdRA(ra.getIdPrestationAccordee());
            mgr3.find(transaction);
            if (mgr3.isEmpty()) {

                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(session);
                bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                bc.retrieve(transaction);
                PRAssert.notIsNew(bc, null);

                if ("9".equals(bc.getDroitApplique()) || "09".equals(bc.getDroitApplique())) {
                    bc = new REBasesCalculNeuviemeRevision();
                    bc.setSession(session);
                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc.retrieve(transaction);
                    PRAssert.notIsNew(bc, null);
                } else {
                    bc = new REBasesCalculDixiemeRevision();
                    bc.setSession(session);
                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc.retrieve(transaction);
                    PRAssert.notIsNew(bc, null);
                }

                REHistoriqueRentes hr = new REHistoriqueRentes();
                hr.setSession(session);

                // bz-5092
                if (!JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())
                        && !JadeStringUtil.isBlankOrZero(bc.getAnneeTraitement())) {
                    hr.setAnneeMontantRAM(bc.getAnneeTraitement());
                } else {
                    hr.setAnneeMontantRAM(ra.getAnneeMontantRAM());
                }

                hr.setAnneeNiveau(bc.getAnneeDeNiveau());
                hr.setCleInfirmiteAtteinteFct(bc.getCleInfirmiteAyantDroit());
                hr.setCodePrestation(ra.getCodePrestation());

                hr.setCodeMutation(ra.getCodeMutation());

                hr.setCs1(ra.getCodeCasSpeciaux1());
                hr.setCs2(ra.getCodeCasSpeciaux2());
                hr.setCs3(ra.getCodeCasSpeciaux3());
                hr.setCs4(ra.getCodeCasSpeciaux4());
                hr.setCs5(ra.getCodeCasSpeciaux5());
                hr.setDateDebutAnticipation(ra.getDateDebutAnticipation());
                hr.setDateDebutDroit(ra.getDateDebutDroit());
                hr.setDateFinDroit(ra.getDateFinDroit());

                String dateRevocationAjournement = ra.getDateRevocationAjournement();
                hr.setDateRevocationAjournement(dateRevocationAjournement);
                hr.setDegreInvalidite(bc.getDegreInvalidite());
                hr.setDroitApplique(bc.getDroitApplique());
                hr.setDureeAjournement(ra.getDureeAjournement());
                hr.setDureeCotAp73(bc.getDureeCotiDes73());
                hr.setDureeCotAv73(bc.getDureeCotiAvant73());
                hr.setDureeCotiClasseAge(bc.getAnneeCotiClasseAge());
                hr.setDureeCotRam(bc.getDureeRevenuAnnuelMoyen());
                hr.setEchelle(bc.getEchelleRente());
                hr.setFractionRente(ra.getFractionRente());
                hr.setIdRenteAccordee(ra.getIdPrestationAccordee());
                hr.setIdTiers(ra.getIdTiersBeneficiaire());
                hr.setIsInvaliditePrecoce(bc.isInvaliditePrecoce());
                hr.setIsModifie(Boolean.FALSE);
                hr.setIsPrendreEnCompteCalculAcor(Boolean.TRUE);
                hr.setIsRevenuSplitte(bc.isRevenuSplitte());
                hr.setIsSurvivantInvalid(new Boolean(ra.getCodeSurvivantInvalide()));
                hr.setIsRenteAjournee(IREPrestationAccordee.CS_ETAT_AJOURNE.equals(ra.getCsEtat())
                        || ra.contientCodeCasSpecial("08"));

                // On charge la demande de rente !!
                RERenteCalculee rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(bc.getIdRenteCalculee());
                rc.retrieve(transaction);
                PRAssert.notIsNew(rc, null);

                REDemandeRente dem = new REDemandeRente();
                dem.setSession(session);
                dem.setIdRenteCalculee(rc.getIdRenteCalculee());
                dem.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                dem.retrieve(transaction);
                PRAssert.notIsNew(dem, null);

                if (!JadeStringUtil.isBlankOrZero(dem.getIdInfoComplementaire())) {
                    PRInfoCompl infoCompl = new PRInfoCompl();
                    infoCompl.setSession(session);
                    infoCompl.setIdInfoCompl(dem.getIdInfoComplementaire());
                    infoCompl.retrieve(transaction);
                    hr.setIsTransfere(infoCompl.getIsTransfere());
                } else {
                    hr.setIsTransfere(Boolean.FALSE);
                }

                hr.setMoisAppointAp73(bc.getMoisAppointsDes73());
                hr.setMoisAppointAv73(bc.getMoisAppointsAvant73());

                hr.setMontantPrestation(ra.getMontantPrestation());
                hr.setMontantReducAnticipation(ra.getMontantReducationAnticipation());
                hr.setNbrAnneeAnticipation(ra.getAnneeAnticipation());
                hr.setNbrAnneeBTA(bc.getAnneeBonifTacheAssistance());
                hr.setNbrAnneeBTE(bc.getAnneeBonifTacheEduc());
                hr.setNbrAnneeBTR(bc.getAnneeBonifTransitoire());
                hr.setOfficeAI(bc.getCodeOfficeAi());
                hr.setRam(bc.getRevenuAnnuelMoyen());
                hr.setSupplementAjournement(ra.getSupplementAjournement());
                hr.setSupplementCarriere(bc.getSupplementCarriere());
                hr.setSurvenanceEvenementAssure(bc.getSurvenanceEvtAssAyantDroit());

                if (bc instanceof REBasesCalculNeuviemeRevision) {
                    hr.setCodeRevenu(((REBasesCalculNeuviemeRevision) bc).getRevenuPrisEnCompte());
                    hr.setMontantBTE(((REBasesCalculNeuviemeRevision) bc).getBonificationTacheEducative());
                }

                hr.add(transaction);

                if (transaction.hasErrors()) {
                    throw new Exception(transaction.toString());
                }
            }
        }
        return listIdTiers;
    }

    public static void doReloadHistorique(BSession session, BTransaction transaction, String idTiers) throws Exception {
        REHistoriqueRentesJoinTiersViewBean vb = new REHistoriqueRentesJoinTiersViewBean();
        vb.setIdTiersRequerant(idTiers);
        vb.reloadHistorique(true);
        REHistoriqueRentesJoinTiersHelper.doReloadHistorique(session, transaction, vb);
    }

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        // Doit être casté en type BEntity pour que la classe parente soit
        // également supprimée. L'autres solution serait d'utiliser un container PRabstractProxiViewBean.
        if (viewBean instanceof REHistoriqueRentes) {
            REHistoriqueRentes elm = new REHistoriqueRentes();
            elm.copyDataFromEntity((BEntity) viewBean);
            elm.setSession((BSession) session);
            elm.add();
        } else {
            super._add(viewBean, action, session);
        }

    }

    /**
     * 
     * Traitement :
     * 
     * Pour tous les membres de la famille : - 1) parcours des historiques - si idRA <> 0 ET flag isModifier = false ET
     * isPrisEnCompteCalculAcor = true. --> Suppression de l'historique
     * 
     * - 2) parcours de toutes les BC/RA pour tous les membres de la famille. Si cette RA n'est pas référencée dans
     * l'historique, la créé. Renseigné le lien idRA dans la table d'historique
     * 
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (!(viewBean instanceof REHistoriqueRentesJoinTiersViewBean)) {
            throw new Exception("Wrong viewBean type class. Should be REHistoriqueRentesJoinTiersViewBean; is : "
                    + viewBean.getClass().getName());
        }

        BITransaction transaction = null;
        String listIdTiers = "";
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();
            listIdTiers = REHistoriqueRentesJoinTiersHelper.doReloadHistorique((BSession) session,
                    (BTransaction) transaction, (REHistoriqueRentesJoinTiersViewBean) viewBean);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiersIn(listIdTiers);
            if ((transaction != null) && transaction.isOpened()) {
                transaction.closeTransaction();
            }
        }
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        // Doit être casté en type BEntity pour que la classe parente soit
        // également supprimée. L'autres solution serait d'utiliser un container PRabstractProxiViewBean.
        if (viewBean instanceof REHistoriqueRentes) {
            REHistoriqueRentes elm = new REHistoriqueRentes();
            elm.setSession((BSession) session);
            elm.setIdHistorique(((REHistoriqueRentes) viewBean).getIdHistorique());
            elm.retrieve();
            elm.delete();
        } else {
            super._delete(viewBean, action, session);
        }
    }

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        REHistoriqueRentesJoinTiersListViewBean vb = (REHistoriqueRentesJoinTiersListViewBean) persistentList;

        // vb.setIsList(true);
        StringBuilder orderByBuilder = new StringBuilder();

        orderByBuilder.append(REHistoriqueRentes.CONSTANTE_ORDER_BY_DATE).append(" DESC").append(",");
        orderByBuilder.append(REHistoriqueRentes.FIELDNAME_DATE_DEB_DROIT).append(" DESC");

        vb.setOrderBy(orderByBuilder.toString());

        super._find(persistentList, action, session);
    }

    public FWViewBeanInterface actionActiverEnvoiAcor(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        ((REHistoriqueRentesJoinTiersViewBean) viewBean).retrieve();
        ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIsPrendreEnCompteCalculAcor(Boolean.TRUE);
        ((REHistoriqueRentesJoinTiersViewBean) viewBean).update();
        return viewBean;
    }

    public FWViewBeanInterface actionDesactiverEnvoiAcor(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        ((REHistoriqueRentesJoinTiersViewBean) viewBean).retrieve();
        ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIsPrendreEnCompteCalculAcor(Boolean.FALSE);
        ((REHistoriqueRentesJoinTiersViewBean) viewBean).update();

        return viewBean;
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
