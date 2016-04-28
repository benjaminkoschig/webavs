package globaz.apg.businessimpl.service;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.business.service.APEntityService;
import globaz.apg.db.annonces.APBreakRule;
import globaz.apg.db.annonces.APBreakRuleManager;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APEnfantAPGManager;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APPeriodeAPGManager;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.droits.APSitProJointEmployeurManager;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.enums.APValidationDroitError;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.pojo.APBreakRulesFromView;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.patterns.JadeAbstractService;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.phenix.api.ICPDecision;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.prestation.tools.nnss.PRNSSUtil.FormatNSS;
import globaz.prestation.utils.PRDateUtils;
import globaz.prestation.utils.PRDateUtils.DateOrder;
import globaz.prestation.utils.PRDateUtils.PRDateEquality;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * Service pour la manipulation des entit�es APG/Mat en base de donn�es
 * 
 * @author lga
 */
/**
 * @author lga
 */
public class APEntityServiceImpl extends JadeAbstractService implements APEntityService {

    private APDroitAPG creationDuDroitAPG(final APDroitAPGPViewBean viewBean, final BSession session,
            final BTransaction transaction, final PRDemande demande) throws Exception {
        return editionDroit(session, transaction, viewBean, demande, APModeEditionDroit.CREATION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.utils.APEntityService#creerDroitComplet(globaz.globall.db.BSession,
     * globaz.globall.db.BTransaction, globaz.apg.vb.droits.APDroitAPGPViewBean)
     */
    @Override
    public APDroitAPG creerDroitAPGComplet(final BSession session, final BTransaction transaction,
            final APDroitAPGPViewBean viewBean) throws IllegalArgumentException {
        try {
            validateSessionAndTransactionNotNull(session, transaction);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Unable to create the new APDroitAPG for reason : " + e.toString());
        }
        // On va d'abord s'assurer que le droit n'existe pas
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new IllegalArgumentException(
                    "Unable to create a new APDroitAPG because it is already exist with id ["
                            + viewBean.getDroit().getIdDroit() + "]");
        }

        // Validation des infos du viewBean
        validationViewBean(session, transaction, viewBean);

        PRDemande demande = null;
        APDroitAPG droit = null;
        APSituationFamilialeAPG situationFamiliale = null;

        try {
            // Recherche du tiers en fonction du NSS
            final String nss = viewBean.getNss();
            final PRTiersWrapper tierWrapper = PRTiersHelper.getTiers(session, nss);

            if ((tierWrapper == null) || (tierWrapper.getIdTiers() == null)) {
                throw new Exception("Unable to find the idTiers for the NSS [" + nss + "]");
            }

            demande = getOrCreateDemandeDuTiers(session, transaction, tierWrapper.getIdTiers());
            droit = creationDuDroitAPG(viewBean, session, transaction, demande);
            creerSituationProfSiIndependant(session, transaction, droit, tierWrapper.getIdTiers());
            situationFamiliale = creerSituationFamiliale(session, transaction);

            // Mise � jour de certaines entit�es
            droit.setIdSituationFam(situationFamiliale.getIdSitFamAPG());
            droit.update(transaction);

        } catch (final Exception exception) {
            transaction.setRollbackOnly();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(exception.toString());
        }
        return droit;
    }

    private APSituationFamilialeAPG creerSituationFamiliale(final BSession session, final BTransaction transaction)
            throws Exception {
        final APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
        situationFamilialeAPG.setSession(session);
        situationFamilialeAPG.add(transaction);
        return situationFamilialeAPG;
    }

    /**
     * Si le tiers poss�de une affiliation personnelle en cours durant la p�riode du droit, on creer une situation prof.
     * avec cette affiliation
     */
    private APSituationProfessionnelle creerSituationProfSiIndependant(final BSession session,
            final BTransaction transaction, final APDroitLAPG droit, final String idTiers) throws Exception {

        APSituationProfessionnelle situationProfessionnelle = null;
        String masseAnnuel = "0";

        // on cherche les affiliations pour ce tiers

        final IAFAffiliation affiliation = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        affiliation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
        final Hashtable<Object, Object> param = new Hashtable<Object, Object>();
        param.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
        final IAFAffiliation[] affiliations = affiliation.findAffiliation(param);

        if (affiliations.length > 0) {
            for (int i = 0; i < affiliations.length; i++) {
                final IAFAffiliation aff = affiliations[i];

                // InfoRom531 : On ne reprend que les ind�pendants et ind�pendant + employeur.
                if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                        || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                    final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                            .compareDateFirstGreaterOrEqual(session, droit.getDateDebutDroit(), aff.getDateDebut());
                    final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil.compareDateFirstLowerOrEqual(
                            session, droit.getDateDebutDroit(), aff.getDateFin());
                    // si l'affiliation est en cours
                    if (dateDebutDroitGreaterOrEqualDateDebutApg
                            && (dateDebutDroitLowerOrEqualDateFinApg || globaz.jade.client.util.JadeStringUtil
                                    .isEmpty(aff.getDateFin()))) {

                        // creation de l'employeur
                        final APEmployeur emp = new APEmployeur();
                        emp.setSession(session);
                        emp.setIdTiers(aff.getIdTiers());
                        emp.setIdAffilie(aff.getAffiliationId());
                        emp.add(transaction);

                        // retrouver la masse annuelle dans les cotisations
                        // pers.

                        // on cherche la decision
                        final ICPDecision decision = (ICPDecision) session.getAPIFor(ICPDecision.class);
                        decision.setISession(PRSession.connectSession(session, "PHENIX"));

                        final Hashtable<Object, Object> params = new Hashtable<Object, Object>();
                        params.put(ICPDecision.FIND_FOR_ANNEE_DECISION,
                                Integer.toString(new JADate(droit.getDateDebutDroit()).getYear()));
                        params.put(ICPDecision.FIND_FOR_ID_AFFILIATION, aff.getAffiliationId());
                        params.put(ICPDecision.FIND_FOR_IS_ACTIVE, Boolean.TRUE);

                        final ICPDecision[] decisions = decision.findDecisions(params);

                        // on cherche les donn�es calcul�es en fonction de la
                        // decision
                        if ((decisions != null) && (decisions.length > 0)) {

                            final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) session
                                    .getAPIFor(ICPDonneesCalcul.class);
                            decision.setISession(PRSession.connectSession(session, "PHENIX"));

                            final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
                            parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, decisions[0].getIdDecision());
                            parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_CI);

                            final ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                            if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                                masseAnnuel = donneesCalculs[0].getMontant();
                            }
                        }

                        // creation de la situation prof.
                        situationProfessionnelle = new APSituationProfessionnelle();
                        situationProfessionnelle.setSession(session);
                        situationProfessionnelle.setIdDroit(droit.getIdDroit());
                        situationProfessionnelle.setIdEmployeur(emp.getIdEmployeur());
                        situationProfessionnelle.setIsIndependant(Boolean.TRUE);
                        situationProfessionnelle.setIsVersementEmployeur(Boolean.TRUE);
                        // si pas "non-actif" on donne les allocations
                        // d'exploitation
                        if (!IAFAffiliation.TYPE_AFFILI_NON_ACTIF.equals(aff.getTypeAffiliation())) {

                            // pas d'allocations d'exploitation si pour un droit maternit�
                            if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
                                situationProfessionnelle.setIsAllocationExploitation(Boolean.TRUE);
                            }
                        }

                        // on set la masse annuelle
                        situationProfessionnelle.setRevenuIndependant(masseAnnuel);
                        situationProfessionnelle.wantCallValidate(false);
                        situationProfessionnelle.add(transaction);
                    }
                }
            }
        }
        return situationProfessionnelle;
    }

    private boolean doMoreTestOnControlNumber(final String genreService, final boolean isDuplicata) {
        final int intValue = Integer.valueOf(genreService);
        final List<Integer> services = new ArrayList<Integer>();
        services.add(40);
        services.add(41);
        return !isDuplicata && services.contains(intValue);
    }

    private boolean doMoreTestOnReferenceNumber(final String genreService, final boolean isDuplicata) {
        final int intValue = Integer.valueOf(genreService);
        final List<Integer> services = new ArrayList<Integer>();
        services.add(20);
        services.add(21);
        services.add(22);
        services.add(23);
        return !isDuplicata && services.contains(intValue);
    }

    private APDroitAPG editionDroit(final BSession session, final BTransaction transaction,
            final APDroitAPGPViewBean viewBean, final PRDemande demande, final APModeEditionDroit modeEdition)
            throws Exception {

        if (modeEdition == null) {
            throw new Exception("Any edition mode was specified for the APDroitAPG edition");
        }

        final List<PRPeriode> periodes = viewBean.getPeriodes();
        List<String> datesDeDebut = new ArrayList<String>();
        List<String> datesDeFin = new ArrayList<String>();
        for (int ctr = 0; ctr < periodes.size(); ctr++) {
            datesDeDebut.add(periodes.get(ctr).getDateDeDebut());
            datesDeFin.add(periodes.get(ctr).getDateDeFin());
        }
        datesDeDebut = PRDateUtils.sortDate(datesDeDebut, DateOrder.NEWER_TO_OLDER);
        datesDeFin = PRDateUtils.sortDate(datesDeFin, DateOrder.OLDER_TO_NEWER);
        final String dateDeDebutDroit = datesDeDebut.get(0);
        final String dateDeFinDroit = datesDeFin.get(0);

        // la date de d�but du droit doit �tre ult�rieure au 01.07.1999
        if (new JACalendarGregorian().compare("01.07.1999", dateDeDebutDroit) == JACalendar.COMPARE_FIRSTUPPER) {
            throw new Exception("Unable to create the APDroitAPG : " + session.getLabel("DROIT_TROP_ANCIEN"));
        }

        final APDroitAPG droitAPG = new APDroitAPG();
        droitAPG.setSession(session);

        if (modeEdition.equals(APModeEditionDroit.EDITION)) {
            // On va d'abord s'assurer qu'on a bien un id droit
            if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
                throw new Exception("Unable to update the APDroitAPG with the empty id ["
                        + viewBean.getDroit().getIdDroit() + "]");
            }
            // Ensuite on s'assure que le droit existe
            droitAPG.setIdDroit(viewBean.getIdDroit());
            droitAPG.retrieve(transaction);
            if (droitAPG.isNew()) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it doesn't already exist");
            }
            if (!IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(droitAPG.getEtat())) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it is not 'En attente'");
            }
        }

        // Cr�ation de l'idCaisse
        String idCaisse = null;
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            idCaisse = noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            throw new Exception("A fatal exception was thrown when accessing to the CommonProperties", exception);
        }

        // MAJ des champs du droit
        droitAPG.setIdDemande(demande.getIdDemande());
        droitAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droitAPG.setDateDebutDroit(dateDeDebutDroit);
        droitAPG.setDateFinDroit(dateDeFinDroit);
        droitAPG.setDateReception(viewBean.getDateReception());
        droitAPG.setDateDepot(viewBean.getDateDepot());
        droitAPG.setDroitAcquis(viewBean.getDroitAcquis());
        droitAPG.setGenreService(viewBean.getGenreService());
        droitAPG.setIdGestionnaire(viewBean.getIdGestionnaire());
        droitAPG.setTauxImpotSource(viewBean.getTauxImpotSource());
        droitAPG.setIsSoumisImpotSource(viewBean.getIsSoumisCotisation());
        droitAPG.setNpa(viewBean.getNpa());
        droitAPG.setPays(viewBean.getPays());
        droitAPG.setRemarque(viewBean.getRemarque());
        droitAPG.setNoRevision(viewBean.getNoRevision());
        droitAPG.setNoControlePers(viewBean.getNoControlePers());
        droitAPG.setNoCompte(viewBean.getNoCompte());
        droitAPG.setNbrJourSoldes(viewBean.getNbrJourSoldes());
        droitAPG.setDuplicata(viewBean.getDuplicata());
        droitAPG.setCsProvenanceDroitAcquis(viewBean.getCsProvenanceDroitAcquis());
        droitAPG.setIdCaisse(idCaisse);
        droitAPG.setReference(viewBean.getReference());

        if (modeEdition.equals(APModeEditionDroit.CREATION)) {
            droitAPG.add(transaction);
        } else {
            droitAPG.update(transaction);
        }

        remplacerPeriodesDroitAPG(session, transaction, droitAPG.getIdDroit(), viewBean.getPeriodes());
        return droitAPG;
    }

    @Override
    public PRDemande getDemandeDuDroit(final BSession session, final BTransaction transaction, final String idDroit)
            throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        final PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setIdDemande(droit.getIdDemande());
        demande.retrieve(transaction);
        if (demande.isNew()) {
            throw new APEntityNotFoundException(PRDemande.class, idDroit);
        }
        return demande;
    }

    @Override
    public APDroitAPG getDroitAPG(final BSession session, final BTransaction transaction, final String idDroit)
            throws Exception, APEntityNotFoundException {
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitAPG)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitAPG");
        }
        return (APDroitAPG) droit;
    }

    @Override
    public APDroitLAPG getDroitLAPG(final BSession session, final BTransaction transaction, final String idDroit)
            throws Exception, APEntityNotFoundException {
        validateSessionAndTransactionNotNull(session, transaction);
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException("Unable to read the APDroitAPG with the empty id [" + idDroit + "]");
        }

        final APDroitLAPG droit = new APDroitLAPG();
        droit.setSession(session);
        droit.setIdDroit(idDroit);
        droit.retrieve(transaction);
        if (droit.isNew()) {
            throw new APEntityNotFoundException(APDroitLAPG.class, idDroit);
        }

        if (JadeStringUtil.isBlankOrZero(droit.getGenreService())) {
            throw new IllegalArgumentException("GenreService is empty for APDroitAPG with id [" + idDroit + "]");
        }

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            final APDroitMaternite droitMaternite = new APDroitMaternite();
            droitMaternite.setSession(session);
            droitMaternite.setIdDroit(idDroit);
            droitMaternite.retrieve(transaction);
            if (droitMaternite.isNew()) {
                throw new APEntityNotFoundException(APDroitMaternite.class, idDroit);
            }
            return droitMaternite;
        } else {
            final APDroitAPG droitAPG = new APDroitAPG();
            droitAPG.setSession(session);
            droitAPG.setIdDroit(idDroit);
            droitAPG.retrieve(transaction);
            if (droitAPG.isNew()) {
                throw new APEntityNotFoundException(APDroitAPG.class, idDroit);
            }
            return droitAPG;
        }
    }

    @Override
    public APDroitMaternite getDroitMaternite(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception, APEntityNotFoundException {

        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitMaternite)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitMaternite");
        }
        return (APDroitMaternite) droit;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<APEnfantAPG> getEnfantsAPGDuDroitAPG(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);

        if (!(droit instanceof APDroitAPG)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitAPG");
        }
        final String idSituationFamiliale = ((APDroitAPG) droit).getIdSituationFam();
        if (JadeStringUtil.isBlankOrZero(idSituationFamiliale)) {
            throw new Exception("The APDroitAPG with the id [" + idDroit + "] has empty APSituationFamilialeAPG id");
        }
        // On r�cup�re tous les enfants de la sit. familiale
        final APEnfantAPGManager managerEnfants = new APEnfantAPGManager();
        managerEnfants.setSession(session);
        managerEnfants.setForIdSituationFamiliale(idSituationFamiliale);
        managerEnfants.find(transaction, BManager.SIZE_NOLIMIT);
        return managerEnfants.getContainer();
    }

    /**
     * La PRDemande est g�r�e de la fa�on suivante :</br> - Une seule PRDemande existe pour un tiers dans un domaine
     * particulier(rente, apg, ij, etc)</br> - Si une demande existe dans le domain des APG pour l'idTiers fournis en
     * param�tre, elle sera retourn�e</br> - Si aucune demande existe dans le domain des APG pour l'idTiers, elle sera
     * cr��e et retourn�e</br> <strong>INFO : L'�tat de la demande (ouverte, cl�tur�e) n'est pas pris en compte. Toute
     * les demande sont en �tat 'ouverte'</strong>
     * 
     * @param session
     *            LA session courante
     * @param transaction
     *            La transaction courante
     * @param idTiers
     *            L'idTiers du tiers en question
     * @return Une PRDemande li�e au tiers dans le domain des APG
     * @throws Exception
     *             Si l'idTiers est null ou vide ou en cas d'erreur de persistance
     */
    public PRDemande getOrCreateDemandeDuTiers(final BSession session, final BTransaction transaction,
            final String idTiers) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Unable to retrieve the PRDemande with an empty idTiers [" + idTiers + "]");
        }
        final PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setTypeDemande(IPRDemande.CS_TYPE_APG);
        demande.setIdTiers(idTiers);
        demande.retrieve(transaction);
        if (demande.isNew()) {
            demande.add(transaction);
        }
        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.utils.APEntityService#controllerEtDecouperPeriodeAPGSiEnfantNeEnCoursDePeriode(globaz.globall.db
     * .BSession, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<APPeriodeAPG> getPeriodesDuDroitAPG(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        // Le droit est charg� pour s'assurer que l'idDroit est valid et que le droit existe
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitAPG)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitAPG");
        }
        // if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
        // throw new Exception("The provided id [" + idDroit +
        // "] does not match an APDroitAPG, it's an APDroitMaternite");
        // }

        // On r�cup�re toutes les p�riodes du droit
        final APPeriodeAPGManager managerPeriodes = new APPeriodeAPGManager();
        managerPeriodes.setSession(session);
        managerPeriodes.setForIdDroit(droit.getIdDroit());
        managerPeriodes.find(transaction, BManager.SIZE_NOLIMIT);
        return managerPeriodes.getContainer();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<APPrestation> getPrestationDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception {
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        final APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(session);
        prestationManager.setForIdDroit(droit.getIdDroit());
        prestationManager.find(transaction, BManager.SIZE_NOLIMIT);
        return prestationManager.getContainer();
    }

    @Override
    public List<APRepartitionJointPrestation> getRepartitionJointPrestationDuDroit(final BSession session,
            final BTransaction transaction, final String idDroit) throws Exception {

        // Seulement pour validation : idDroit, droit existe, ...
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);

        final APRepartitionJointPrestationManager repartitionsMgr = new APRepartitionJointPrestationManager();
        repartitionsMgr.setSession(session);
        repartitionsMgr.setForIdDroit(idDroit);
        repartitionsMgr.setParentOnly(true);
        repartitionsMgr.find(BManager.SIZE_NOLIMIT);
        return repartitionsMgr.getContainer();
    }

    /**
     * Retourne les r�partitions li�es � chaque prestation standard du droit
     * 
     * @param session
     * @param transaction
     * @param idDroit
     * @return
     * @throws Exception
     */
    @Override
    public List<APRepartitionPaiements> getRepartitionsPaiementsDuDroit(final BSession session,
            final BTransaction transaction, final String idDroit) throws Exception {
        final List<APRepartitionPaiements> repartitions = new ArrayList<APRepartitionPaiements>();
        final List<APPrestation> prestations = getPrestationDuDroit(session, transaction, idDroit);
        final APRepartitionPaiementsManager manager = new APRepartitionPaiementsManager();
        manager.setSession(session);
        // TODO clauses � compl�ter
        // manager.setForTypePaiement(IAPRepartitionPaiements.);
        for (final APPrestation prestation : prestations) {
            manager.setForIdPrestation(prestation.getIdPrestation());
            manager.find(transaction, BManager.SIZE_NOLIMIT);
            repartitions.addAll(manager.getContainer());
        }
        return repartitions;
    }

    @Override
    public List<APSitProJointEmployeur> getSituationProfJointEmployeur(final BSession session,
            final BTransaction transaction, final String idDroit) throws Exception {

        final List<APSitProJointEmployeur> apSitProJoiEmpList = new ArrayList<APSitProJointEmployeur>();

        // Recherche de la situation professionnelle
        final APSitProJointEmployeurManager apSitProJoiEmpMgr = new APSitProJointEmployeurManager();
        apSitProJoiEmpMgr.setSession(session);
        apSitProJoiEmpMgr.setForIdDroit(idDroit);

        apSitProJoiEmpMgr.find(BManager.SIZE_NOLIMIT);

        @SuppressWarnings("unchecked")
        final Iterator<APSitProJointEmployeur> iter = apSitProJoiEmpMgr.iterator();

        while (iter.hasNext()) {
            final APSitProJointEmployeur apSitProJoiEmp = iter.next();
            if (apSitProJoiEmp != null) {
                apSitProJoiEmpList.add(apSitProJoiEmp);
            }
        }

        return apSitProJoiEmpList;

    }

    private boolean isRemarqueTropLongue(final String remarque) {
        final String[] tab = PRStringUtils.split(remarque, '\n');
        if (tab.length > 3) {
            return true;
        }
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.apg.utils.APEntityService#miseAjourDroit(globaz.globall.db.BSession, globaz.globall.db.BTransaction,
     *      globaz.apg.vb.droits.APDroitAPGPViewBean)
     */
    @Override
    public APDroitAPG miseAjourDroit(final BSession session, final BTransaction transaction,
            final APDroitAPGPViewBean viewBean) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        // On va d'abord s'assurer qu'on a bien un id droit
        if (viewBean.getDroit() == null) {
            throw new Exception(
                    "The viewBean from type APDroitAPGPViewBean has a null APDroitLAPG. Can not found the idDroit ");
        }
        if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new Exception("Unable to update the APDroitAPG with the empty id ["
                    + viewBean.getDroit().getIdDroit() + "]");
        }
        // Validation des infos du viewBean
        validationViewBean(session, transaction, viewBean);

        final APDroitLAPG droit = getDroitLAPG(session, transaction, viewBean.getDroit().getIdDroit());

        droit.setSession(session);
        droit.retrieve(transaction);
        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitAPG with id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        if (!droit.isModifiable()) {
            throw new Exception("Unable to edit the APDroitAPG with id [" + droit.getIdDroit()
                    + "] becuse it is not editable");
        }

        // Suppression des p�riodes d�j� existante pour ce droit
        supprimerLesPeriodesDuDroit(session, transaction, droit.getIdDroit());
        // Suppression des prestations
        supprimerLesPrestationsDuDroit(session, transaction, droit.getIdDroit());

        final PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setIdDemande(droit.getIdDemande());
        demande.retrieve(transaction);
        if (demande.isNew()) {
            throw new Exception("Unable to retrieve the PRDemande with id [" + droit.getIdDemande() + "]");
        }

        return editionDroit(session, transaction, viewBean, demande, APModeEditionDroit.EDITION);
    }

    private void reactiverDroitParent(final BSession session, final BTransaction transaction, final String idDroitParent)
            throws Exception {
        final APPrestationManager pMgr = new APPrestationManager();

        pMgr.setSession(session);
        pMgr.setForIdDroit(idDroitParent);
        pMgr.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_ANNULE);
        pMgr.find(transaction);

        final boolean foundPrestation = pMgr.getContainer().size() > 0;
        for (int idPrestation = 0; idPrestation < pMgr.size(); ++idPrestation) {
            final APPrestation prestation = (APPrestation) pMgr.get(idPrestation);
            prestation.setSession(session);
            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestation.update(transaction);
        }

        // Dans ce cas, le droit parent doit �tre remis dans l'�tat 'PARTIEL'.
        if (foundPrestation) {
            final APDroitLAPG parent = new APDroitLAPG();
            parent.setSession(session);
            parent.setIdDroit(idDroitParent);
            parent.retrieve(transaction);
            if (parent.isNew()) {
                throw new Exception();
            }
            parent.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL);
            parent.update(transaction);
        }
    }

    @Override
    public void remplacerBreakRulesDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit, final List<APBreakRulesFromView> nouvellesBreakRules) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        supprimerLesBreakRulesDuDroit(session, transaction, idDroit);

        // Pour chaque breakRule re�u depuis l'�crans de validation des prestation,
        // on recherche si la prestation existe bien
        APPrestation prestation = null;
        for (final APBreakRulesFromView breakRule : nouvellesBreakRules) {
            prestation = new APPrestation();
            prestation.setSession(session);
            prestation.setIdPrestationApg(breakRule.getIdPrestation());
            prestation.retrieve(transaction);
            if (prestation.isNew()) {
                throw new APEntityNotFoundException(APPrestation.class, idDroit);
            }
        }

        // Pour chacune de ces breakRule, on va cr�er une entit� correspondante en DB
        APBreakRule breakRuleEntity = null;
        for (final APBreakRulesFromView breakRule : nouvellesBreakRules) {
            breakRuleEntity = new APBreakRule();
            breakRuleEntity.setSession(session);
            breakRuleEntity.setIdDroit(idDroit);
            breakRuleEntity.setIdPrestation(breakRule.getIdPrestation());
            breakRuleEntity.setBreakRuleCode(breakRule.getCodeAsString());
            breakRuleEntity.setDateQuittance(JadeDateUtil.getGlobazFormattedDate(new Date()));
            breakRuleEntity.setGestionnaire(session.getUserName());
            breakRuleEntity.add(transaction);
        }
    }

    @Override
    public void remplacerPeriodesDroitAPG(final BSession session, final BTransaction transaction, final String idDroit,
            final List<PRPeriode> nouvellesPeriodes) throws Exception, APEntityNotFoundException {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);

        if (!(droit instanceof APDroitAPG)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitAPG");
        }

        // On supprime les p�riodes existantes
        supprimerLesPeriodesDuDroit(session, transaction, droit.getIdDroit());

        // On ordonne les nouvelles p�riodes
        Collections.sort(nouvellesPeriodes);

        // Et on les recr�er
        for (final PRPeriode periode : nouvellesPeriodes) {
            final APPeriodeAPG p = new APPeriodeAPG();
            p.setSession(session);
            p.setIdDroit(idDroit);
            p.setDateDebutPeriode(periode.getDateDeDebut());
            p.setDateFinPeriode(periode.getDateDeFin());
            p.add(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.utils.APEntityService#supprimerDroitComplet(globaz.globall.db.BSession,
     * globaz.globall.db.BTransaction, java.lang.String)
     */
    @Override
    public void supprimerDroitCompletAPG(final BSession session, final BTransaction transaction, final String idDroit)
            throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception("Unable to delete the APDroitAPG with an empty id [" + idDroit + "]");
        }
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitAPG droit = new APDroitAPG();
        droit.setSession(session);
        droit.setIdDroit(idDroit);
        droit.retrieve(transaction);

        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitAPG with id [" + idDroit + "] to delete it");
        }

        if (!droit.isModifiable()) {
            throw new Exception("Unable to delete the APDroitAPG with id [" + idDroit + "] because it is nott editable");
        }

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            throw new Exception("The provided id [" + idDroit
                    + "] does not match an APDroitAPG, it's an APDroitMaternite");
        }

        supprimerSituationFamilialle(session, transaction, droit.getIdSituationFam());
        supprimerLesPeriodesDuDroit(session, transaction, idDroit);
        supprimerLesSituationProfessionnelle(session, transaction, idDroit);
        supprimerLesPrestationsDuDroit(session, transaction, idDroit);
        supprimerLesBreakRulesDuDroit(session, transaction, idDroit);

        // les prestations annul�es du droit parent doivent �tre misent dans l'etat valid�
        if (!JadeStringUtil.isBlankOrZero(droit.getIdDroitParent())) {
            reactiverDroitParent(session, transaction, droit.getIdDroitParent());
        }

        droit.delete(transaction);
    }

    private void supprimerEmployeur(final BSession session, final BTransaction transaction, final String idEmployeur)
            throws Exception {
        if (JadeStringUtil.isBlankOrZero(idEmployeur)) {
            throw new Exception("Unable to retrieve the APEmployeur with an empty id [" + idEmployeur
                    + "] to delete it");
        }
        final APEmployeur employeur = new APEmployeur();
        employeur.setSession(session);
        employeur.setIdEmployeur(idEmployeur);
        employeur.retrieve(transaction);
        if (employeur.isNew()) {
            throw new Exception("Unable to retrieve the APEmployeur with id [" + idEmployeur + "] to delete it");
        }
        employeur.delete(transaction);
    }

    private void supprimerEnfantsDeLaSituationFamilialleDuDroit(final BSession session, final BTransaction transaction,
            final String idSituationFamiliale) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idSituationFamiliale)) {
            throw new Exception("Unable to delete the APEnfantAPG from the APSituationFamilialeAPG with an empty id ["
                    + idSituationFamiliale + "]");
        }
        final APEnfantAPGManager manager = new APEnfantAPGManager();
        manager.setSession(session);
        manager.setForIdSituationFamiliale(idSituationFamiliale);
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        for (final Object o : manager.getContainer()) {
            final APEnfantAPG enfant = (APEnfantAPG) o;
            enfant.setSession(session);
            enfant.delete(transaction);
        }
    }

    @Override
    public void supprimerLesBreakRulesDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception, APEntityNotFoundException {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitLAPG droitLAPG = getDroitLAPG(session, transaction, idDroit);

        // On supprime tous les breakRules
        final APBreakRuleManager brManager = new APBreakRuleManager();
        brManager.setSession(session);
        brManager.setForIdDroit(droitLAPG.getIdDroit());
        brManager.delete(transaction);
    }

    private void supprimerLesPeriodesDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception("Unable to delete the APPeriodeAPG for an APDroitAPG with an empty id [" + idDroit
                    + "]");
        }
        final APPeriodeAPGManager mgr = new APPeriodeAPGManager();
        mgr.setSession(session);
        mgr.setForIdDroit(idDroit);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int ctr = 0; ctr < mgr.size(); ++ctr) {
            final APPeriodeAPG periode = (APPeriodeAPG) mgr.get(ctr);
            periode.setSession(session);
            periode.wantCallMethodAfter(false); // on ne veut pas mettre a jour les dates du droit
            periode.delete(transaction);
        }
    }

    /**
     * Supprime l'int�gralit� du graphe d'objet li� aux prestations d'un droit APG
     */
    @Override
    public void supprimerLesPrestationsDuDroit(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception("Unable to delete the APPrestation for an APDroitAPG with an empty id [" + idDroit
                    + "]");
        }
        final APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(session);
        prestationManager.setForIdDroit(idDroit);
        prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < prestationManager.size(); i++) {
            final APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
            prestation.setSession(session);
            prestation.delete(transaction);
        }
    }

    /**
     * Supprime toutes les situations professionnelles li�es au droit ainsi que les employeurs associ�s � chaque sit
     * prof
     * 
     * @param session
     * @param transaction
     * @param idDroit
     * @throws Exception
     */
    private void supprimerLesSituationProfessionnelle(final BSession session, final BTransaction transaction,
            final String idDroit) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception("Unable to search the APSituationProfessionnelle for an APDroitAPG with an empty id ["
                    + idDroit + "]");
        }
        final APSituationProfessionnelleManager manager = new APSituationProfessionnelleManager();
        manager.setSession(session);
        manager.setForIdDroit(idDroit);
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int idSitPro = 0; idSitPro < manager.size(); ++idSitPro) {
            final APSituationProfessionnelle sitPro = (APSituationProfessionnelle) manager.get(idSitPro);
            sitPro.setSession(session);
            // Supression de 'employeur associ� � cette sit prof
            supprimerEmployeur(session, transaction, sitPro.getIdEmployeur());
            sitPro.delete(transaction);
        }
    }

    /**
     * Supprime la situation familiale ainsi que tous les enfants associ�s
     * 
     * @param session
     * @param transaction
     * @param idSituationFamiliale
     * @throws Exception
     */
    private void supprimerSituationFamilialle(final BSession session, final BTransaction transaction,
            final String idSituationFamiliale) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idSituationFamiliale)) {
            throw new Exception("Unable to delete the APSituationFamilialeAPG with an empty id ["
                    + idSituationFamiliale + "]");
        }

        final APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
        situationFamilialeAPG.setSession(session);
        situationFamilialeAPG.setIdSitFamAPG(idSituationFamiliale);
        situationFamilialeAPG.retrieve(transaction);
        if (situationFamilialeAPG.isNew()) {
            throw new Exception("Unable to retrieve the APSituationFamilialeAPG with id [" + idSituationFamiliale
                    + "] to delete it");
        }

        supprimerEnfantsDeLaSituationFamilialleDuDroit(session, transaction, idSituationFamiliale);
        situationFamilialeAPG.delete(transaction);
    }

    private void validateSessionAndTransactionNotNull(final BSession session, final BTransaction transaction)
            throws Exception {
        if (session == null) {
            throw new IllegalArgumentException("The provided BSession is null");
        }
        if (transaction == null) {
            throw new IllegalArgumentException("The provided BTransaction is null");
        }
    }

    private void validationViewBean(final BSession session, final BTransaction transaction,
            final APDroitAPGPViewBean viewBean) throws IllegalArgumentException {
        final List<APValidationDroitError> errors = new ArrayList<APValidationDroitError>();
        // NSS
        if (!PRNSSUtil.isValidNSS(viewBean.getNss(), FormatNSS.COMPLET_FORMATE)) {
            errors.add(APValidationDroitError.NSS_INVALID);
        }
        // Date de d�p�t
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateDepot())) {
            errors.add(APValidationDroitError.DATE_DEPOT_VIDE);
        }
        // Date de r�ception
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateReception())) {
            errors.add(APValidationDroitError.DATE_RECEPTION_VIDE);
        }

        final boolean isDuplicata = Boolean.TRUE.equals(viewBean.getDuplicata());

        // Genre service
        final String genreDeService = viewBean.getCuGenreService();
        boolean isServiceTypeValid = true;
        if ((genreDeService == null) || !APGenreServiceAPG.isValidGenreService(genreDeService)) {
            errors.add(APValidationDroitError.GENRE_SERVICE_INVALID);
            isServiceTypeValid = false;
        }
        // Jours sold�s
        int nbrJourSoldes = 0;
        if ((viewBean.getNbrJourSoldes() == null) || !JadeNumericUtil.isIntegerPositif(viewBean.getNbrJourSoldes())) {
            errors.add(APValidationDroitError.JOURS_SOLDE_INVALID);
        } else {
            try {
                nbrJourSoldes = Integer.valueOf(viewBean.getNbrJourSoldes());
            } catch (final NumberFormatException e) {
                // Nothing to do with e. We only say value is invalid
                errors.add(APValidationDroitError.JOURS_SOLDE_INVALID);
            }
        }
        // P�riodes
        if ((viewBean.getPeriodes() == null) || (viewBean.getPeriodes().size() == 0)) {
            errors.add(APValidationDroitError.AUCUNE_PERIODE_DEFINIE);
        } else {
            final List<PRPeriode> periodes = viewBean.getPeriodes();
            int nombrePeriodes = 0;
            boolean periodeErrorsfounded = false;
            for (final PRPeriode periode : periodes) {
                if (!JadeDateUtil.isGlobazDate(periode.getDateDeDebut())
                        || !JadeDateUtil.isGlobazDate(periode.getDateDeFin())) {
                    periodeErrorsfounded = true;
                    errors.add(APValidationDroitError.FORMAT_DATE_INVALID);
                } else if ((PRDateUtils.compare(periode.getDateDeDebut(), periode.getDateDeFin()) != PRDateEquality.AFTER)
                        && (PRDateUtils.compare(periode.getDateDeDebut(), periode.getDateDeFin()) != PRDateEquality.EQUALS)) {
                    errors.add(APValidationDroitError.PERIODE_INCOHERENTE);
                    periodeErrorsfounded = true;
                } else if ((PRDateUtils.compare(PRDateUtils.convertCalendarToGlobazDate(Calendar.getInstance()),
                        periode.getDateDeFin()) == PRDateEquality.AFTER)
                        || (PRDateUtils.compare(PRDateUtils.convertCalendarToGlobazDate(Calendar.getInstance()),
                                periode.getDateDeFin()) == PRDateEquality.AFTER)) {
                    errors.add(APValidationDroitError.PERIODE_DANS_FUTUR);
                    periodeErrorsfounded = true;
                }
                nombrePeriodes++;
            }
            if (nbrJourSoldes < nombrePeriodes) {
                errors.add(APValidationDroitError.JOURS_SOLDE_INSUFFISANT);
            }
            // On test le chevauchement uniquement s'il n'y a pas d'erreurs lors des contr�les pr�c�dents des p�riodes
            // Ceci dans le but d'�viter les exceptions si un format de date est incorrect
            if (!periodeErrorsfounded) {
                if (PRDateUtils.hasChevauchementDePeriodes(periodes)) {
                    errors.add(APValidationDroitError.CHEVAUCHEMENT_PERIODES_DETECTE);
                }
            }
        }

        if (isServiceTypeValid && doMoreTestOnReferenceNumber(genreDeService, isDuplicata)) {
            // Compte ou referenceNumber
            final String numeroDeCompte = viewBean.getNoCompte();
            if (JadeStringUtil.isEmpty(numeroDeCompte)) {
                errors.add(APValidationDroitError.NUMERO_REFERENCE_NON_RENSEIGNE);
            } else if ((numeroDeCompte == null) || (numeroDeCompte.length() < 4)) {
                errors.add(APValidationDroitError.NUMERO_REFERENCE_TROP_COURT);
            }

        }

        if (isServiceTypeValid && doMoreTestOnControlNumber(genreDeService, isDuplicata)) {
            // // Num�ro de contr�le ou controlNumber
            final String numeroDeControle = viewBean.getNoControlePers();
            // La longueur min du controlNumber est 2 position
            if ((numeroDeControle == null) || (numeroDeControle.length() < 2)) {
                errors.add(APValidationDroitError.NUMERO_CONTROLE_INVALID);
            }
        }
        // Test de la longueur de la remarque
        if (isRemarqueTropLongue(viewBean.getRemarque())) {
            errors.add(APValidationDroitError.REMARQUE_TROP_LONGUE);
        }

        // Pays
        final String pays = viewBean.getPays();
        if (JadeStringUtil.isBlankOrZero(pays)) {
            errors.add(APValidationDroitError.PAYS_INVALID);
        }

        // NPA : si le pays est Suisse, le NPA doit �tre renseign�
        String npa = null;
        if (IConstantes.ID_PAYS_SUISSE.equals(viewBean.getPays())) {
            npa = viewBean.getNpa();
            if ((npa == null) || (npa.length() < 4)) {
                errors.add(APValidationDroitError.NPA_VIDE);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(npa)) {
            // le pays doit �tre la suisse
            if (!IConstantes.ID_PAYS_SUISSE.equals(pays)) {
                errors.add(APValidationDroitError.PAYS_DOIT_ETRE_SUISSE);
            } else {
                // recherche du canton
                try {
                    final String canton = PRTiersHelper.getCanton(session, npa);
                    if (canton == null) {
                        errors.add(APValidationDroitError.CANTON_INTROUVABLE);
                    }
                } catch (final Exception e) {
                    errors.add(APValidationDroitError.CANTON_INTROUVABLE);
                }
            }
        }

        // Droit acquis
        if (!JadeStringUtil.isEmpty(viewBean.getDroitAcquis())) {
            if (!JadeStringUtil.endsWith(viewBean.getDroitAcquis(), "5")) {
                if (!JadeStringUtil.endsWith(viewBean.getDroitAcquis(), "0")) {
                    errors.add(APValidationDroitError.DROITS_ACQUIS_PAS_ARRONDI);
                }
            }
        }
        errors.addAll(validerNoCompte(session, genreDeService, viewBean.getNoCompte()));
        if (errors.size() > 0) {
            final StringBuilder message = new StringBuilder();
            for (final APValidationDroitError error : errors) {
                message.append(session.getLabel(error.getErrorLabel()));
                message.append("</br>");
            }
            throw new IllegalArgumentException(message.toString());
        }
    }

    /**
     * Validation du numero de compte en fonction du genre de service
     * 
     * @param statement
     */
    private List<APValidationDroitError> validerNoCompte(final BSession session, final String genreServiceCS,
            final String numeroCompte) {
        final List<APValidationDroitError> errors = new ArrayList<APValidationDroitError>();

        final String genreService = session.getCode(genreServiceCS);

        // Selon bulletin 162
        // Genre de service = 20 ou 21
        // Si le numero de compte est < 100000
        // le format est CCIIF avec
        // CC = cantons = 1 � 26 et 50
        // II = centre d'instruction = 11 ou 99. Sont autorises en plus pour
        // les cantons suivants:
        // ZH 01 : 12, 13
        // BE 02 : 12, 13, 14, 15, 16
        // GE 25 : 12
        // CCS 26 : 00 remplace le 11
        // F = filiale = 1 ou 2
        // Si le numero de compte est > 100000
        // le format est CCNNNNF avec
        // CC = cantons = 1 � 25 et 50
        // NNNN = NPA = (pas de controle sur la validite du NPA)
        // F = filiale = 1 ou 2
        if ((APGenreServiceAPG.ProtectionCivileServiceNormale.getCodePourAnnonce().equals(genreService)
                || APGenreServiceAPG.ProtectionCivileCadreSpecialiste.getCodePourAnnonce().equals(genreService)
                || APGenreServiceAPG.ProtectionCivileCommandant.getCodePourAnnonce().equals(genreService) || APGenreServiceAPG.ProtectionCivileFormationDeBase
                .getCodePourAnnonce().equals(genreService)) && !JadeStringUtil.isIntegerEmpty(numeroCompte)) {

            // format=CCIIF
            if (Integer.parseInt(numeroCompte) < 100000) {

                // check du nb de positions
                if (numeroCompte.length() < 4) {
                    errors.add(APValidationDroitError.NO_COMPTE_CCIIF_NB_POS);
                    // this._addError(statement.getTransaction(), session.getLabel("NO_COMPTE_CCIIF_NB_POS"));
                } else {

                    // recuperation des champs
                    final String f = numeroCompte.substring(numeroCompte.length() - 1, numeroCompte.length());
                    final String ii = numeroCompte.substring(numeroCompte.length() - 3, numeroCompte.length() - 1);
                    final String cc = numeroCompte.substring(0, numeroCompte.length() - 3);

                    // check de F
                    if (!f.equals("1") && !f.equals("2")) {
                        errors.add(APValidationDroitError.NO_COMPTE_DOIT_FINIR_PAR_1_2_POUR_GENRE_20_21);
                        // this._addError(statement.getTransaction(),
                        // session.getLabel("NO_COMPTE_DOIT_FINIR_PAR_1_2_POUR_GENRE_20_21"));
                    }

                    // check de CC
                    final int ccInt = Integer.parseInt(cc);
                    if (!(((ccInt >= 1) && (ccInt <= 26)) || (ccInt == 50))) {
                        errors.add(APValidationDroitError.NO_COMPTE_CCIIF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21);
                        // this._addError(statement.getTransaction(),
                        // session.getLabel("NO_COMPTE_CCIIF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21"));
                    }

                    // check de II
                    final int iiInt = Integer.parseInt(ii);
                    if (ccInt == 1) {
                        // ZH: 11, 99, 12, 13
                        if (!((iiInt == 11) || (iiInt == 99) || (iiInt == 12) || (iiInt == 13))) {
                            errors.add(APValidationDroitError.NO_COMPTE_CCIIF_II_INCORECTE_A);
                            // this._addError(statement.getTransaction(),
                            // MessageFormat.format(session.getLabel("NO_COMPTE_CCIIF_II_INCORECTE"), new Object[] { cc,
                            // "11, 12, 13 et 99" }));
                        }
                    } else if (ccInt == 2) {
                        // BE: 11, 99, 12, 13, 14, 15, 16
                        if (!((iiInt == 11) || (iiInt == 99) || (iiInt == 12) || (iiInt == 13) || (iiInt == 14)
                                || (iiInt == 15) || (iiInt == 16))) {
                            errors.add(APValidationDroitError.NO_COMPTE_CCIIF_II_INCORECTE_B);
                            // this._addError(
                            // statement.getTransaction(),
                            // MessageFormat.format(session.getLabel("NO_COMPTE_CCIIF_II_INCORECTE"), new Object[] { cc,
                            // "11, 12, 13, 14, 15, 16 et 99" }));
                        }
                    } else if (ccInt == 25) {
                        // GE: 11, 99, 12
                        if (!((iiInt == 11) || (iiInt == 99) || (iiInt == 12))) {
                            errors.add(APValidationDroitError.NO_COMPTE_CCIIF_II_INCORECTE_C);
                            // this._addError(statement.getTransaction(),
                            // MessageFormat.format(session.getLabel("NO_COMPTE_CCIIF_II_INCORECTE"), new Object[] { cc,
                            // "11, 12 et 99" }));
                        }
                    } else if (ccInt == 26) {
                        // CCS: 00, 99
                        if (!((iiInt == 0) || (iiInt == 99))) {
                            errors.add(APValidationDroitError.NO_COMPTE_CCIIF_II_INCORECTE_D);
                            // this._addError(statement.getTransaction(),
                            // MessageFormat.format(session.getLabel("NO_COMPTE_CCIIF_II_INCORECTE"), new Object[] { cc,
                            // "00 et 99" }));
                        }
                    } else {
                        // autres: 11, 99
                        if (!((iiInt == 11) || (iiInt == 99) || (iiInt == 12) || (iiInt == 13))) {
                            errors.add(APValidationDroitError.NO_COMPTE_CCIIF_II_INCORECTE_E);
                            // this._addError(statement.getTransaction(),
                            // MessageFormat.format(session.getLabel("NO_COMPTE_CCIIF_II_INCORECTE"), new Object[] { cc,
                            // "11 et 99" }));
                        }
                    }
                }

            } else {
                // format=CCNNNNF

                // check du nb de positions
                if (numeroCompte.length() < 6) {
                    errors.add(APValidationDroitError.NO_COMPTE_CCNNNNF_NB_POS);
                    // this._addError(statement.getTransaction(), session.getLabel("NO_COMPTE_CCNNNNF_NB_POS"));
                } else {

                    // recuperation des champs
                    final String f = numeroCompte.substring(numeroCompte.length() - 1, numeroCompte.length());
                    // String nnnn =
                    // getNoCompte().substring(getNoCompte().length()-5,
                    // getNoCompte().length()-1);
                    final String cc = numeroCompte.substring(0, numeroCompte.length() - 5);

                    // check de F
                    if (!f.equals("1") && !f.equals("2")) {
                        errors.add(APValidationDroitError.NO_COMPTE_DOIT_FINIR_PAR_1_2_POUR_GENRE_20_21);
                        // this._addError(statement.getTransaction(),
                        // session.getLabel("NO_COMPTE_DOIT_FINIR_PAR_1_2_POUR_GENRE_20_21"));
                    }

                    // check de NNNN
                    // pas de check sur NNNN

                    // check de CC
                    final int ccInt = Integer.parseInt(cc);
                    if (!(((ccInt >= 1) && (ccInt <= 25)) || (ccInt == 50))) {
                        errors.add(APValidationDroitError.NO_COMPTE_CCNNNNF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21);
                        // this._addError(statement.getTransaction(),
                        // session.getLabel("NO_COMPTE_CCNNNNF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21"));
                    }
                }
            }
        }
        return errors;
    }

}
