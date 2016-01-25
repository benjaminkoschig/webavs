package globaz.apg.helpers.prestation;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.prestation.APRepartitionPaiementsViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APRepartitionPaiementsHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redefini pour renseigner les champs du viewbean qui sera affiche dans l'ecran rc.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        actionPreparerChercher(viewBean, action, (BSession) session);
    }

    /**
     * redefini pour charger l'adresse de paiement.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        // charger l'adresse de paiement
        rechargerAdressePaiement((BSession) session, (APRepartitionPaiementsViewBean) viewBean);

        // charge l'etat de la prestation courante
        APRepartitionPaiementsViewBean rpViewBean = (APRepartitionPaiementsViewBean) viewBean;

        if (!(rpViewBean.getIdPrestationApg() == "") && !(rpViewBean.getIdPrestationApg() == null)) {
            rechargerEtatPrestation((BSession) session, rpViewBean);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        APRepartitionPaiementsViewBean repPaiements = (APRepartitionPaiementsViewBean) viewBean;
        super._update(repPaiements, action, session);

        // si adresse de paiement modifiee, on effectue la mise a jours des
        // adresses de paiement
        // vide du beneficiaire dans les prestations du meme droit
        if ("true".equalsIgnoreCase(repPaiements.isAdresseModifiee())
                && !JadeStringUtil.isEmpty(repPaiements.getIdDomaineAdressePaiement())) {

            APPrestationManager prestManager = new APPrestationManager();
            prestManager.setForIdDroit(repPaiements.getIdDroit());
            prestManager.setSession((BSession) session);
            BTransaction transaction = (BTransaction) ((BSession) session).newTransaction();
            BStatement statement = null;
            BStatement statInt = null;
            APPrestation prestation = null;

            try {
                transaction.openTransaction();
                statement = prestManager.cursorOpen(transaction);

                // pour toutes les prestation du droit
                while ((prestation = (APPrestation) prestManager.cursorReadNext(statement)) != null) {

                    APRepartitionJointPrestationManager repPrestManager = new APRepartitionJointPrestationManager();
                    repPrestManager.setSession((BSession) session);
                    repPrestManager.setForIdPrestation(prestation.getIdPrestationApg());
                    repPrestManager.setForIdAffilie(repPaiements.getIdAffilie());
                    repPrestManager.setForIdTiers(repPaiements.getIdTiers());

                    APRepartitionJointPrestation repartPrest = null;
                    try {
                        statInt = repPrestManager.cursorOpen(transaction);

                        while ((repartPrest = (APRepartitionJointPrestation) repPrestManager.cursorReadNext(statInt)) != null) {

                            // si l'adresse est vide (0) on maj avec la nouvelle
                            // adresse
                            if ("0".equals(repartPrest.getIdTiersAdressePaiement())) {

                                APRepartitionPaiements repPay = new APRepartitionPaiements();

                                repPay.setSession((BSession) session);
                                repPay.setIdRepartitionBeneficiairePaiement(repartPrest
                                        .getIdRepartitionBeneficiairePaiement());
                                repPay.retrieve(transaction);

                                repPay.setIdDomaineAdressePaiement(repPaiements.getIdDomaineAdressePaiement());
                                repPay.setIdTiersAdressePaiement(repPaiements.getIdTiersAdressePaiement());
                                repPay.update(transaction);
                            }
                        }
                    } finally {
                        if (statInt != null) {
                            try {
                                repPrestManager.cursorClose(statement);
                            } finally {
                                statInt.closeStatement();
                            }
                        }
                    }
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            } finally {
                try {
                    if (statement != null) {
                        try {
                            prestManager.cursorClose(statement);
                        } finally {
                            statement.closeStatement();
                        }
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * prepare un viewBean pour l'affichage d'informations dans la page rc de la ca page.
     * <p>
     * Charge le droit et les prestations du droit. Positionne les champs du list viewBean en fonction, obtient une
     * adresse de paiement valide.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionPreparerChercher(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        APRepartitionPaiementsViewBean rpViewBean = (APRepartitionPaiementsViewBean) viewBean;

        chargerInfosDroit(session, rpViewBean);
        chargerInfosPrestations(session, rpViewBean);

        // recharger l'adresse de paiement pour les cas ou l'adresse est
        // invalide
        rechargerAdressePaiement(session, rpViewBean);

        return viewBean;
    }

    /**
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionRepartirLesMontants(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        chargerInfosDroit(session, (APRepartitionPaiementsViewBean) viewBean);
        chargerInfosPrestations(session, (APRepartitionPaiementsViewBean) viewBean);

        return viewBean;
    }

    /**
     * charge le droit et renseigne le viewBean avec certaines valeurs choisies.
     * 
     * @param session
     * @param rpViewBean
     * @throws Exception
     */
    private void chargerInfosDroit(BSession session, APRepartitionPaiementsViewBean rpViewBean) throws Exception {
        APDroitLAPG droit = APGUtils.loadDroit(session, rpViewBean.getIdDroit(), rpViewBean.getGenreService());
        PRTiersWrapper tiers = droit.loadDemande().loadTiers();

        rpViewBean.setDateDebutDroit(droit.getDateDebutDroit());
        rpViewBean.setNoAVSAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        rpViewBean.setNomPrenomAssure(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
    }

    /**
     * charge toutes les prestations pour ce droit, enumere les id de ces prestations pour permettre la navigation entre
     * celle-ci dans l'ecran et charge la prestation courante (par defaut la premiere).
     * 
     * @param session
     * @param rpViewBean
     * @throws Exception
     */
    private void chargerInfosPrestations(BSession session, APRepartitionPaiementsViewBean rpViewBean) throws Exception {
        APPrestationManager prestations = new APPrestationManager();

        prestations.setForIdDroit(rpViewBean.getIdDroit());
        prestations.setOrderBy(APPrestation.FIELDNAME_IDPRESTATIONAPG);
        prestations.setSession(session);
        prestations.find();

        // retrouver les ids des prestations du droit
        LinkedList idsPrestations = new LinkedList();

        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            idsPrestations.add(((APPrestation) prestations.get(idPrestation)).getIdPrestationApg());
        }

        rpViewBean.setIdsPrestations(idsPrestations);

        // recupere l'id de la prestation dont on veut forcer l'affichage (on
        // vient de l'ecran des prestations)
        if (!JadeStringUtil.isIntegerEmpty(rpViewBean.getIdPrestationApg())) {
            rpViewBean.setIdOfIdPrestationCourante(idsPrestations.indexOf(rpViewBean.getIdPrestationApg()));
        }

        // plausi
        if (rpViewBean.getIdOfIdPrestationCourante() < 0) {
            rpViewBean.setIdOfIdPrestationCourante(0);
        }

        // charger la prestation courante
        APPrestation prestation = new APPrestation();

        prestation.setIdPrestationApg(rpViewBean.getIdPrestationCourante());
        prestation.setSession(session);
        prestation.retrieve();

        rpViewBean.setTauxPrestation(prestation.getMontantJournalier());
        rpViewBean.setMontantBrutPrestation(prestation.getMontantBrut());
        rpViewBean.setNbJoursPrestation(prestation.getNombreJoursSoldes());
        rpViewBean.setDateDebutPrestation(prestation.getDateDebut());
        rpViewBean.setDateFinPrestation(prestation.getDateFin());
        rpViewBean.setEtatPrestation(prestation.getEtat());
        rpViewBean.setGenrePrestation(prestation.getGenre());
    }

    /**
     * recherche par introspection la methode a appeller pour executer l'action courante.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * charge une adresse de paiement valide.
     * <p>
     * si les id adresse de paiment et domaine d'adresses sont renseignes, charge et formatte l'adresse correspondante,
     * sinon recherche, charge et formatte une adresse pour le tiers courant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param rpViewBean
     * @throws Exception
     */
    private void rechargerAdressePaiement(BSession session, APRepartitionPaiementsViewBean rpViewBean) throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse
        // paiement
        if (rpViewBean.isTiersBeneficiaireChange()) {
            rpViewBean.setIdTiersAdressePaiement(rpViewBean.getIdTiers());

            // si le tiers beneficiaire a change, on set le domaine a Maternite
            // ou APG en fonction du genre de service
            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(rpViewBean.getSession());
            droit.setIdDroit(rpViewBean.getIdDroit());
            droit.retrieve();

            if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
                rpViewBean.setIdDomaineAdressePaiement(IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE);
            } else {
                rpViewBean.setIdDomaineAdressePaiement(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG);
            }
        }

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise
        // dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(rpViewBean.getIdTiersAdressePaiement())) {
            return;
        }

        TIAdressePaiementData adresse = null;

        adresse = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(),
                rpViewBean.getIdTiersAdressePaiement(), rpViewBean.getIdDomaineAdressePaiement(),
                rpViewBean.getIdAffilieAdrPmt(), JACalendar.todayJJsMMsAAAA());

        if (adresse != null) {
            // il existe une adresse de paiement
        } else {

            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(rpViewBean.getSession());
            droit.setIdDroit(rpViewBean.getIdDroit());
            droit.retrieve();

            // pour les droits maternite, apres le domaine Mat, on cherche dans
            // APG, puis le domaine standard
            if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {

                adresse = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(),
                        rpViewBean.getIdTiersAdressePaiement(), IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE,
                        rpViewBean.getIdAffilieAdrPmt(), JACalendar.todayJJsMMsAAAA());

            } else {

                adresse = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(),
                        rpViewBean.getIdTiersAdressePaiement(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG,
                        rpViewBean.getIdAffilieAdrPmt(), JACalendar.todayJJsMMsAAAA());

            }
        }

        rpViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                rpViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                rpViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            rpViewBean.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            rpViewBean.setCcpOuBanqueFormatte("");
            rpViewBean.setAdresseFormattee("");

            // si le tiers beneficiaire a change et que l'on a pas trouve
            // d'adresse
            // on enleve l'idTiersAdresseDePaiement
            if (rpViewBean.isTiersBeneficiaireChange()) {
                rpViewBean.setIdTiersAdressePaiement("0");
            }
        }
    }

    /**
     * Charge l'etat de la prestation courante
     * 
     * @param rpViewBean
     * @param session
     * @return rpViewBean
     * @throws Exception
     */
    public APRepartitionPaiementsViewBean rechargerEtatPrestation(BSession session,
            APRepartitionPaiementsViewBean rpViewBean) throws Exception {

        APPrestation prestation = new APPrestation();

        prestation.setIdPrestationApg(rpViewBean.getIdPrestationApg());
        prestation.setSession(session);
        prestation.retrieve();

        rpViewBean.setEtatPrestation(prestation.getEtat());

        return rpViewBean;
    }
}
