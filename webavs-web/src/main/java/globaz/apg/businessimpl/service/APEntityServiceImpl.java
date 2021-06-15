package globaz.apg.businessimpl.service;

import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.common.util.Dates;
import ch.globaz.queryexec.bridge.jade.SCM;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.business.service.APEntityService;
import globaz.apg.db.annonces.APBreakRule;
import globaz.apg.db.annonces.APBreakRuleManager;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.*;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.enums.APValidationDroitError;
import globaz.apg.exceptions.APBusinessException;
import globaz.apg.exceptions.APEntityNotFoundException;
import globaz.apg.pojo.APBreakRulesFromView;
import globaz.apg.properties.APParameter;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.*;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.patterns.JadeAbstractService;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.phenix.api.ICPDecision;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
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
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour la manipulation des entitées APG/Mat en base de données
 *
 * @author lga
 */

/**
 * @author lga
 */
public class APEntityServiceImpl extends JadeAbstractService implements APEntityService {

    @Override
    public APDroitPandemie creerDroitPanComplet(final BSession session, final BTransaction transaction,
                                                final APDroitPanViewBean viewBean) throws IllegalArgumentException {
        try {
            validateSessionAndTransactionNotNull(session, transaction);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Unable to create the new APDroitAPG for reason : " + e.toString());
        }
        // On va d'abord s'assurer que le droit n'existe pas
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new IllegalArgumentException("Unable to create a new APDroitAPG because it is already exist with id ["
                    + viewBean.getDroit().getIdDroit() + "]");
        }

        // Validation des infos du viewBean
        validationPandemieViewBean(session, transaction, viewBean);

        PRDemande demande = null;
        APDroitPandemie droit = null;
        APSituationFamilialeAPG situationFamiliale = null;

        try {
            // Recherche du tiers en fonction du NSS
            final String nss = viewBean.getNss();
            final PRTiersWrapper tierWrapper = PRTiersHelper.getTiers(session, nss);

            if ((tierWrapper == null) || (tierWrapper.getIdTiers() == null)) {
                throw new Exception("Unable to find the idTiers for the NSS [" + nss + "]");
            }

            demande = getOrCreateDemandeDuTiersPandemie(session, transaction, tierWrapper.getIdTiers());
            droit = creationDuDroitPandemie(viewBean, session, transaction, demande);
            creerSituationProfPanSiIndependant(session, transaction, droit, tierWrapper.getIdTiers());
            situationFamiliale = creerSituationFamiliale(session, transaction);

            // Mise à jour de certaines entitées
            droit.setIdSituationFam(situationFamiliale.getIdSitFamAPG());
            droit.update(transaction);

        } catch (final Exception exception) {
            transaction.setRollbackOnly();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(exception.toString());
        }
        return droit;
    }

    /**
     * La PRDemande est gérée de la façon suivante :</br>
     * - Une seule PRDemande existe pour un tiers dans un domaine
     * particulier(rente, apg, ij, etc)</br>
     * - Si une demande existe dans le domain des APG pour l'idTiers fournis en
     * paramètre, elle sera retournée</br>
     * - Si aucune demande existe dans le domain des APG pour l'idTiers, elle sera
     * créée et retournée</br>
     * <strong>INFO : L'état de la demande (ouverte, clôturée) n'est pas pris en compte. Toute
     * les demande sont en état 'ouverte'</strong>
     *
     * @param session     LA session courante
     * @param transaction La transaction courante
     * @param idTiers     L'idTiers du tiers en question
     * @return Une PRDemande liée au tiers dans le domain des APG
     * @throws Exception Si l'idTiers est null ou vide ou en cas d'erreur de persistance
     */
    public PRDemande getOrCreateDemandeDuTiersPandemie(final BSession session, final BTransaction transaction,
                                                       final String idTiers) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Unable to retrieve the PRDemande with an empty idTiers [" + idTiers + "]");
        }
        PRDemande demande = new PRDemande();
        PRDemandeManager mgr = new PRDemandeManager();
        mgr.setSession(session);
        mgr.setForIdTiers(idTiers);
        mgr.setForTypeDemande(IPRDemande.CS_TYPE_PANDEMIE);
        mgr.find(BManager.SIZE_USEDEFAULT);
        if (!mgr.isEmpty()) {
            demande = (PRDemande) mgr.get(0);
        } else {
            demande.setTypeDemande(IPRDemande.CS_TYPE_PANDEMIE);
            demande.setIdTiers(idTiers);
            demande.add(transaction);
        }
        return demande;
    }

    @Override
    public APDroitPanSituation creerDroitPanSituation(BSession session, BTransaction transaction, APDroitPanSituationViewBean viewBean) {
        try {
            validateSessionAndTransactionNotNull(session, transaction);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Unable to create the new APDroitPanSituation for reason : " + e.toString());
        }
        // On va d'abord s'assurer que le droit n'existe pas
        if (!JadeStringUtil.isBlankOrZero(viewBean.getIdDroitPanSituation())) {
            throw new IllegalArgumentException("Unable to create a new APDroitPanSituation because it is already exist with id ["
                    + viewBean.getIdDroit() + "]");
        }

        APDroitPanSituation droit = null;

        try {
            droit = creationDuDroitPandemieSituation(viewBean, session, transaction);

            // Mise à jour de certaines entitées
            droit.update(transaction);

        } catch (final Exception exception) {
            transaction.setRollbackOnly();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(exception.toString());
        }
        return droit;
    }

    private APDroitPanSituation creationDuDroitPandemieSituation(APDroitPanSituationViewBean viewBean, BSession session, BTransaction transaction) throws Exception {
        return editionDroitPandemieSituation(viewBean, session, transaction, APModeEditionDroit.CREATION);
    }

    private APDroitPanSituation editionDroitPandemieSituation(APDroitPanSituationViewBean viewBean, BSession session, BTransaction transaction, APModeEditionDroit modeEdition) throws Exception {
        APDroitPanSituation droitPandemieSituation = new APDroitPanSituation();
        droitPandemieSituation.setSession(session);

        if (modeEdition.equals(APModeEditionDroit.EDITION)) {
            droitPandemieSituation.setIdApgPandemie(viewBean.getIdDroitPanSituation());
            droitPandemieSituation.retrieve();
        }

        // MAJ des champs du droit
        droitPandemieSituation.setIdDroit(viewBean.getIdDroit());
        droitPandemieSituation.setActiviteSalarie(viewBean.isActiviteSalarie());
        droitPandemieSituation.setCopieDecompteEmployeur(viewBean.isCopieDecompteEmployeur());
        droitPandemieSituation.setPaiementEmployeur(viewBean.isPaiementEmployeur());
        droitPandemieSituation.setCategorieEntreprise(viewBean.getCategorieEntreprise());
        droitPandemieSituation.setCategorieEntrepriseLibelle(viewBean.getCategorieEntrepriseLibelle());
        droitPandemieSituation.setMotifGarde(viewBean.getMotifGarde());
        droitPandemieSituation.setMotifGardeHandicap(viewBean.getMotifGardeHandicap());
        droitPandemieSituation.setQuarantaineOrdonnee(viewBean.isQuarantaineOrdonnee());
        droitPandemieSituation.setQuarantaineOrdonneePar(viewBean.getQuarantaineOrdonneePar());
        droitPandemieSituation.setDateFermetureEtablissementDebut(viewBean.getDateDebutFermeture());
        droitPandemieSituation.setDateFermetureEtablissementFin(viewBean.getDateFinFermeture());
        droitPandemieSituation.setDateDebutManifestationAnnulee(viewBean.getDateDebutManifAnnulee());
        droitPandemieSituation.setDateFinManifestationAnnulee(viewBean.getDateFinManifAnnulee());
        droitPandemieSituation.setDateDebutPerteGains(viewBean.getDateDebutPerteGains());
        droitPandemieSituation.setDateFinPerteGains(viewBean.getDateFinPerteGains());
        droitPandemieSituation.setRemarque(viewBean.getRemarqueRefus());
        droitPandemieSituation.setDateDebutActiviteLimitee(viewBean.getDateDebutActiviteLimitee());
        droitPandemieSituation.setDateFinActiviteLimitee(viewBean.getDateFinActiviteLimitee());


        if (modeEdition.equals(APModeEditionDroit.CREATION)) {
            // On vérifie s'il existe déjà un droit pandémie situation pour ce droit : problème lorsque les utilisateur utilisent le back du navigateur.
            APDroitPanSituation oldPanSituation = getDroitPanSituation(session, transaction, viewBean.getIdDroit());
            if (StringUtils.isEmpty(oldPanSituation.getIdApgPandemie())) {
                droitPandemieSituation.add(transaction);
            } else {
                throw new APBusinessException("Impossible de créer la situation pandémie du droit car celle-ci existe déjà.");
            }
        } else {
            droitPandemieSituation.update(transaction);
        }

        return droitPandemieSituation;

    }

    private APDroitPandemie editionDroitPan(APDroitPanViewBean viewBean, BSession session, BTransaction transaction, PRDemande demande, final APModeEditionDroit modeEdition) throws Exception {
        if (modeEdition == null) {
            throw new Exception("Any edition mode was specified for the APDroitAPG edition");
        }

        final List<PRPeriode> periodes = viewBean.getPeriodes();

        List<String> datesDeDebut = periodes.stream().map(PRPeriode::getDateDeDebut).collect(Collectors.toList());
        List<String> datesDeFin = periodes.stream().map(PRPeriode::getDateDeFin).collect(Collectors.toList());

        datesDeDebut = PRDateUtils.sortDate(datesDeDebut, DateOrder.NEWER_TO_OLDER);
        final String dateDeDebutDroit = datesDeDebut.get(0);

        String dateDeFinDroit;
        if (datesDeFin.contains("")) {
            dateDeFinDroit = "";
        } else {
            datesDeFin = PRDateUtils.sortDate(datesDeFin, DateOrder.OLDER_TO_NEWER);
            dateDeFinDroit = datesDeFin.get(0);
        }

        // la date de début du droit doit être ultérieure au 01.07.1999
        if (new JACalendarGregorian().compare("01.07.1999", dateDeDebutDroit) == JACalendar.COMPARE_FIRSTUPPER) {
            throw new Exception("Unable to create the APDroitAPG : " + session.getLabel("DROIT_TROP_ANCIEN"));
        }

        final APDroitPandemie droitPandemie = new APDroitPandemie();
        droitPandemie.setSession(session);

        if (modeEdition.equals(APModeEditionDroit.EDITION)) {
            // On va d'abord s'assurer qu'on a bien un id droit
            if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
                throw new Exception(
                        "Unable to update the APDroitAPG with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
            }
            // Ensuite on s'assure que le droit existe
            droitPandemie.setIdDroit(viewBean.getIdDroit());
            droitPandemie.retrieve(transaction);
            if (droitPandemie.isNew()) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it doesn't already exist");
            }
            if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(viewBean.getEtat())) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it is not 'En attente'");
            }
        }

        // Création de l'idCaisse
        String idCaisse = null;
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            idCaisse = noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            throw new Exception("A fatal exception was thrown when accessing to the CommonProperties", exception);
        }

        // MAJ des champs du droit
        droitPandemie.setIdDemande(demande.getIdDemande());
        droitPandemie.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droitPandemie.setDateDebutDroit(dateDeDebutDroit);
        droitPandemie.setDateFinDroit(dateDeFinDroit);
        droitPandemie.setDateReception(viewBean.getDateReception());
        droitPandemie.setDateDepot(viewBean.getDateDepot());
        droitPandemie.setDroitAcquis(viewBean.getDroitAcquis());
        droitPandemie.setGenreService(viewBean.getGenreService());
        droitPandemie.setIdGestionnaire(viewBean.getIdGestionnaire());
        droitPandemie.setTauxImpotSource(viewBean.getTauxImpotSource());
        droitPandemie.setIsSoumisImpotSource(viewBean.getIsSoumisCotisation());
        droitPandemie.setNpa(viewBean.getNpa());
        droitPandemie.setPays(viewBean.getPays());
        droitPandemie.setRemarque(viewBean.getRemarque());
        droitPandemie.setNoRevision(viewBean.getNoRevision());
        droitPandemie.setNoControlePers(viewBean.getNoControlePers());
        droitPandemie.setNoCompte(viewBean.getNoCompte());
        droitPandemie.setDuplicata(viewBean.getDuplicata());
        droitPandemie.setCsProvenanceDroitAcquis(viewBean.getCsProvenanceDroitAcquis());
        droitPandemie.setIdCaisse(idCaisse);
        droitPandemie.setReference(viewBean.getReference());
        droitPandemie.setCsCantonDomicile(viewBean.getCsCantonDomicile());

        if (modeEdition.equals(APModeEditionDroit.CREATION)) {
            droitPandemie.add(transaction);
        } else {
            droitPandemie.update(transaction);
        }

        remplacerPeriodesDroitPan(session, transaction, droitPandemie.getIdDroit(), viewBean.getPeriodes());
        return droitPandemie;
    }

    private void remplacerPeriodesDroitPan(BSession session, BTransaction transaction, String idDroit, List<PRPeriode> nouvellesPeriodes) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitPandemie droit = getDroitPandemie(session, transaction, idDroit);

        // On supprime les périodes existantes
        supprimerLesPeriodesDuDroit(session, transaction, droit.getIdDroit());

        // On ordonne les nouvelles périodes
        Collections.sort(nouvellesPeriodes);

        // Et on les recréer
        for (final PRPeriode periode : nouvellesPeriodes) {
            final APPeriodeAPG p = new APPeriodeAPG();
            p.setSession(session);
            p.setIdDroit(idDroit);
            p.setDateDebutPeriode(periode.getDateDeDebut());
            p.setDateFinPeriode(periode.getDateDeFin());
            p.setNbrJours(periode.getNbJour());
            p.wantCallValidate(false);
            p.add(transaction);
        }
    }

    private void validationPandemieViewBean(final BSession session, final BTransaction transaction,
                                            final APDroitPanViewBean viewBean) throws IllegalArgumentException {
        final List<APValidationDroitError> errors = new ArrayList<APValidationDroitError>();
        // NSS
        if (!PRNSSUtil.isValidNSS(viewBean.getNss(), FormatNSS.COMPLET_FORMATE)) {
            errors.add(APValidationDroitError.NSS_INVALID);
        }
        // Date de dépôt
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateDepot())) {
            errors.add(APValidationDroitError.DATE_DEPOT_VIDE);
        }
        // Date de réception
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateReception())) {
            errors.add(APValidationDroitError.DATE_RECEPTION_VIDE);
        }

        final boolean isDuplicata = Boolean.TRUE.equals(viewBean.getDuplicata());

        // Genre service
        final String genreDeService = viewBean.getCuGenreService();
        boolean isServiceTypeValid = true;
        if ((genreDeService == null) || !APGenreServiceAPG.isValidGenreServicePandemie(genreDeService)) {
            errors.add(APValidationDroitError.GENRE_SERVICE_INVALID);
            isServiceTypeValid = false;
        }
        // Périodes
        if ((viewBean.getPeriodes() == null) || (viewBean.getPeriodes().size() == 0)) {
            errors.add(APValidationDroitError.AUCUNE_PERIODE_DEFINIE);
        } else {
            final List<PRPeriode> periodes = viewBean.getPeriodes();
            int nombrePeriodes = 0;
            boolean periodeErrorsfounded = false;
            for (final PRPeriode periode : periodes) {
                if (StringUtils.isNotEmpty(periode.getDateDeFin())) {
                    if (!JadeDateUtil.isGlobazDate(periode.getDateDeDebut())
                            || !JadeDateUtil.isGlobazDate(periode.getDateDeFin())) {
                        periodeErrorsfounded = true;
                        errors.add(APValidationDroitError.FORMAT_DATE_INVALID);
                    } else if ((PRDateUtils.compare(periode.getDateDeDebut(),
                            periode.getDateDeFin()) != PRDateEquality.AFTER)
                            && (PRDateUtils.compare(periode.getDateDeDebut(),
                            periode.getDateDeFin()) != PRDateEquality.EQUALS)) {
                        errors.add(APValidationDroitError.PERIODE_INCOHERENTE);
                        periodeErrorsfounded = true;
                    }
                } else {
                    if (!JadeDateUtil.isGlobazDate(periode.getDateDeDebut())) {
                        periodeErrorsfounded = true;
                        errors.add(APValidationDroitError.FORMAT_DATE_INVALID);
                    }
                }
                nombrePeriodes++;
            }
            // On test le chevauchement uniquement s'il n'y a pas d'erreurs lors des contrôles précédents des périodes
            // Ceci dans le but d'éviter les exceptions si un format de date est incorrect
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
            // // Numéro de contrôle ou controlNumber
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

        // NPA : si le pays est Suisse, le NPA doit être renseigné
        String npa = null;
        if (IConstantes.ID_PAYS_SUISSE.equals(viewBean.getPays())) {
            npa = viewBean.getNpa();
            if ((npa == null) || (npa.length() < 4)) {
                errors.add(APValidationDroitError.NPA_VIDE);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(npa)) {
            // le pays doit être la suisse
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
            throw new IllegalArgumentException("Unable to create a new APDroitAPG because it is already exist with id ["
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

            // Mise à jour de certaines entitées
            droit.setIdSituationFam(situationFamiliale.getIdSitFamAPG());
            droit.update(transaction);

        } catch (final Exception exception) {
            transaction.setRollbackOnly();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(exception.toString());
        }
        return droit;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.utils.APEntityService#creerDroitComplet(globaz.globall.db.BSession,
     * globaz.globall.db.BTransaction, globaz.apg.vb.droits.APDroitAPGPViewBean)
     */
    @Override
    public APDroitPaternite creerDroitPatComplet(final BSession session, final BTransaction transaction,
                                                 final APDroitPatPViewBean viewBean) throws IllegalArgumentException {
        try {
            validateSessionAndTransactionNotNull(session, transaction);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Unable to create the new APDroitPaternite for reason : " + e.toString());
        }
        // On va d'abord s'assurer que le droit n'existe pas
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new IllegalArgumentException("Unable to create a new APDroitPaternite because it is already exist with id ["
                    + viewBean.getDroit().getIdDroit() + "]");
        }

        // Validation des infos du viewBean
        validationPatViewBean(session, transaction, viewBean);

        PRDemande demande = null;
        APDroitPaternite droit = null;
        APSituationFamilialeAPG situationFamiliale = null;

        try {
            // Recherche du tiers en fonction du NSS
            final String nss = viewBean.getNss();
            final PRTiersWrapper tierWrapper = PRTiersHelper.getTiers(session, nss);

            if ((tierWrapper == null) || (tierWrapper.getIdTiers() == null)) {
                throw new Exception("Unable to find the idTiers for the NSS [" + nss + "]");
            }

            demande = getOrCreateDemandeDuTiersPat(session, transaction, tierWrapper.getIdTiers());
            droit = creationDuDroitPaternite(viewBean, session, transaction, demande);
            creerSituationProfSiIndependant(session, transaction, droit, tierWrapper.getIdTiers());
            situationFamiliale = creerSituationFamiliale(session, transaction);

            // Mise à jour de certaines entitées
            droit.setIdSituationFam(situationFamiliale.getIdSitFamAPG());
            droit.setDateDebutDroit(viewBean.getDateDebutDroit());
            droit.update(transaction);

        } catch (final Exception exception) {
            transaction.setRollbackOnly();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(exception.toString());
        }
        return droit;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.utils.APEntityService#creerDroitComplet(globaz.globall.db.BSession,
     * globaz.globall.db.BTransaction, globaz.apg.vb.droits.APDroitAPGPViewBean)
     */
    @Override
    public APDroitProcheAidant creerDroitPaiComplet(final BSession session, final BTransaction transaction,
                                                 final APDroitPaiPViewBean viewBean) throws IllegalArgumentException {
        try {
            validateSessionAndTransactionNotNull(session, transaction);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Unable to create the new APDroitPaternite for reason : " + e.toString());
        }
        // On va d'abord s'assurer que le droit n'existe pas
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new IllegalArgumentException("Unable to create a new APDroitPaternite because it is already exist with id ["
                    + viewBean.getDroit().getIdDroit() + "]");
        }

        // Validation des infos du viewBean
        validationPaiViewBean(session, transaction, viewBean);

        PRDemande demande = null;
        APDroitProcheAidant droit = null;
        APSituationFamilialeAPG situationFamiliale = null;

        try {
            // Recherche du tiers en fonction du NSS
            final String nss = viewBean.getNss();
            final PRTiersWrapper tierWrapper = PRTiersHelper.getTiers(session, nss);

            if ((tierWrapper == null) || (tierWrapper.getIdTiers() == null)) {
                throw new Exception("Unable to find the idTiers for the NSS [" + nss + "]");
            }

            demande = getOrCreateDemandeDuTiersPai(session, transaction, tierWrapper.getIdTiers());
            droit = creationDuDroitProcheAidant(viewBean, session, transaction, demande);
            creerSituationProfSiIndependant(session, transaction, droit, tierWrapper.getIdTiers());
            situationFamiliale = creerSituationFamiliale(session, transaction);

            // Mise à jour de certaines entitées
            droit.setIdSituationFam(situationFamiliale.getIdSitFamAPG());
            droit.setDateDebutDroit(viewBean.getDateDebutDroit());
            droit.update(transaction);

        } catch (final Exception exception) {
            transaction.setRollbackOnly();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(exception.toString());
        }
        return droit;
    }


    private APDroitPandemie creationDuDroitPandemie(final APDroitPanViewBean viewBean, final BSession session,
                                                    final BTransaction transaction, final PRDemande demande) throws Exception {
        return editionDroitPan(viewBean, session, transaction, demande, APModeEditionDroit.CREATION);
    }

    private APDroitAPG creationDuDroitAPG(final APDroitAPGPViewBean viewBean, final BSession session,
                                          final BTransaction transaction, final PRDemande demande) throws Exception {
        return editionDroit(session, transaction, viewBean, demande, APModeEditionDroit.CREATION);
    }

    private APDroitPaternite creationDuDroitPaternite(final APDroitPatPViewBean viewBean, final BSession session,
                                                      final BTransaction transaction, final PRDemande demande) throws Exception {
        return editionDroitPat(session, transaction, viewBean, demande, APModeEditionDroit.CREATION);
    }

    private APDroitProcheAidant creationDuDroitProcheAidant(final APDroitPaiPViewBean viewBean, final BSession session,
                                                      final BTransaction transaction, final PRDemande demande) throws Exception {
        return editionDroitPai(session, transaction, viewBean, demande, APModeEditionDroit.CREATION);
    }

    private APSituationFamilialeAPG creerSituationFamiliale(final BSession session, final BTransaction transaction)
            throws Exception {
        final APSituationFamilialeAPG situationFamilialeAPG = new APSituationFamilialeAPG();
        situationFamilialeAPG.setSession(session);
        situationFamilialeAPG.add(transaction);
        return situationFamilialeAPG;
    }

    /**
     * Si le tiers possède une affiliation personnelle en cours durant la période du droit, on creer une situation prof.
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

                // InfoRom531 : On ne reprend que les indépendants et indépendant + employeur.
                if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                        || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                    final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                            .compareDateFirstGreaterOrEqual(session, droit.getDateDebutDroit(), aff.getDateDebut());
                    final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                            .compareDateFirstLowerOrEqual(session, droit.getDateDebutDroit(), aff.getDateFin());
                    // si l'affiliation est en cours
                    if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                            || globaz.jade.client.util.JadeStringUtil.isEmpty(aff.getDateFin()))) {

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
                        if (APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
                            // Récupération des décisons de l'année précédente PANDEMIE
                            params.put(ICPDecision.FIND_FOR_ANNEE_DECISION,
                                    Integer.toString(new JADate(droit.getDateDebutDroit()).getYear() - 1));
                        } else {
                            params.put(ICPDecision.FIND_FOR_ANNEE_DECISION,
                                    Integer.toString(new JADate(droit.getDateDebutDroit()).getYear()));

                        }
                        params.put(ICPDecision.FIND_FOR_ID_AFFILIATION, aff.getAffiliationId());
                        params.put(ICPDecision.FIND_FOR_IS_ACTIVE, Boolean.TRUE);

                        final ICPDecision[] decisions = decision.findDecisions(params);

                        // on cherche les données calculées en fonction de la
                        // decision
                        if ((decisions != null) && (decisions.length > 0)) {

                            final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) session
                                    .getAPIFor(ICPDonneesCalcul.class);
                            decision.setISession(PRSession.connectSession(session, "PHENIX"));

                            final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
                            parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, decisions[0].getIdDecision());
                            if (APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);
                            } else {
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_CI);
                            }

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

                            // pas d'allocations d'exploitation si pour un droit maternité
                            if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())
                                    && !APGUtils.isTypeAllocationPandemie(droit.getGenreService())
                                    && !APGUtils.isTypeAllocationJourIsole(droit.getGenreService())) {
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


    @Override
    public boolean creerSituationProfPanSelonDroitPrecedent(BSession session, BTransaction transaction, APDroitLAPG droit, String idTiers) throws Exception {
        // Récupération du dernier droit
        String idLastDroit = findLastIDDroitPandemie(session, transaction, idTiers);
        // Récupération de(s) dernière(s) situation(s) prof
        List<APSituationProfessionnelle> listLastSituationPro = findSituationProfessionnelleByIDDroit(session, transaction, idLastDroit);
        // Création des situations professionnelles
        List<APSituationProfessionnelle> listLastSituationProCrees = creerCopieSituationProf(session, transaction, listLastSituationPro, droit);
        // Permet de savoir si au moins une situation professionnelle a été créée
        return listLastSituationProCrees.size() > 0;
    }

    private String findLastIDDroitPandemie(BSession session, final BTransaction transaction, final String idTiers) throws Exception {
        List<String> etat = new ArrayList<>();
        etat.add(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
        APDroitPanJointTiersManager manager = new APDroitPanJointTiersManager();
        manager.setSession(session);
        manager.setForIdTiers(idTiers);
        manager.setToDateDebutDroit("17.09.2020");
        manager.setToDateFinDroit("17.09.2020");
        manager.setForEtatDroitIn(etat);
        manager.setOrderByDroitDesc(true);
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        List<APDroitPanJointTiers> droitsPandemie = manager.getContainer();
        if (droitsPandemie.size() > 0) {
            return droitsPandemie.get(0).getIdDroit();
        }
        return null;
    }

    private List<APSituationProfessionnelle> creerCopieSituationProf(BSession session, BTransaction transaction, List<APSituationProfessionnelle> listLastSituationPro, APDroitLAPG droit) throws Exception {
        List<APSituationProfessionnelle> situProCrees = new ArrayList<>();
        for (APSituationProfessionnelle sitPro : listLastSituationPro) {
            // creation de la situation prof.
            APSituationProfessionnelle newSituationProfessionnelle = new APSituationProfessionnelle();
            newSituationProfessionnelle.setSession(session);
            newSituationProfessionnelle.setIdDroit(droit.getIdDroit());
            newSituationProfessionnelle.setIdEmployeur(sitPro.getIdEmployeur());
            newSituationProfessionnelle.setIsIndependant(sitPro.getIsIndependant());
            newSituationProfessionnelle.setIsVersementEmployeur(sitPro.getIsVersementEmployeur());
            // on set la masse annuelle
            newSituationProfessionnelle.setRevenuIndependant(sitPro.getRevenuIndependant());
            newSituationProfessionnelle.wantCallValidate(false);
            newSituationProfessionnelle.add(transaction);
            situProCrees.add(newSituationProfessionnelle);
        }
        return situProCrees;
    }

    private List<APSituationProfessionnelle> findSituationProfessionnelleByIDDroit(BSession session, final BTransaction transaction, String idDroit) throws Exception {
        List<APSituationProfessionnelle> listSituationPro = new ArrayList<>();
        if (!JadeStringUtil.isBlankOrZero(idDroit)) {
            APSituationProfessionnelleManager managerSitu = new APSituationProfessionnelleManager();
            managerSitu.setSession(session);
            managerSitu.setForIdDroit(idDroit);
            managerSitu.find(transaction, BManager.SIZE_NOLIMIT);

            for (int idSitPro = 0; idSitPro < managerSitu.size(); ++idSitPro) {
                APSituationProfessionnelle sitPro = (APSituationProfessionnelle) managerSitu.get(idSitPro);
                listSituationPro.add(sitPro);
            }
        }
        return listSituationPro;
    }

    /**
     * Si le tiers possède une affiliation personnelle en cours durant la période du droit, on creer une situation prof.
     * avec cette affiliation
     */
    private APSituationProfessionnelle creerSituationProfPanSiIndependant(final BSession session,
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

                // InfoRom531 : On ne reprend que les indépendants et indépendant + employeur.
                if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                        || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                    if (!creerSituationProfPanSelonDroitPrecedent(session, transaction, droit, idTiers)) {

                        final boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                                .compareDateFirstGreaterOrEqual(session, droit.getDateDebutDroit(), aff.getDateDebut());
                        final boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                                .compareDateFirstLowerOrEqual(session, droit.getDateDebutDroit(), aff.getDateFin());
                        // si l'affiliation est en cours
                        if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                                || globaz.jade.client.util.JadeStringUtil.isEmpty(aff.getDateFin()))) {

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
                            if (APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
                                // Récupération des décisons de l'année précédente PANDEMIE
                                params.put(ICPDecision.FIND_FOR_ANNEE_DECISION,
                                        Integer.toString(new JADate(droit.getDateDebutDroit()).getYear() - 1));
                            } else {
                                params.put(ICPDecision.FIND_FOR_ANNEE_DECISION,
                                        Integer.toString(new JADate(droit.getDateDebutDroit()).getYear()));

                            }
                            params.put(ICPDecision.FIND_FOR_ID_AFFILIATION, aff.getAffiliationId());
                            params.put(ICPDecision.FIND_FOR_IS_ACTIVE, Boolean.TRUE);

                            final ICPDecision[] decisions = decision.findDecisions(params);

                            // on cherche les données calculées en fonction de la
                            // decision
                            if ((decisions != null) && (decisions.length > 0)) {

                                final ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) session
                                        .getAPIFor(ICPDonneesCalcul.class);
                                decision.setISession(PRSession.connectSession(session, "PHENIX"));

                                final Hashtable<Object, Object> parms = new Hashtable<Object, Object>();
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, decisions[0].getIdDecision());
                                if (APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
                                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_NET);
                                } else {
                                    parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_CI);
                                }

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

                                // pas d'allocations d'exploitation si pour un droit maternité
                                if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())
                                        && !APGUtils.isTypeAllocationPandemie(droit.getGenreService())
                                        && !APGUtils.isTypeAllocationJourIsole(droit.getGenreService())) {
                                    situationProfessionnelle.setIsAllocationExploitation(Boolean.TRUE);
                                }
                            }

                            // on set la masse annuelle
                            situationProfessionnelle.setRevenuIndependant(masseAnnuel);
                            situationProfessionnelle.wantCallValidate(false);
                            situationProfessionnelle.add(transaction);
                        }
                    } else {
                        // Si des situations professionnelles sont trouvées dans le dernier droit pandémie et ont été créées
                        // dans le nouveau droit, alors pas besoin d'itérer sur les affiliations, tout a été créés
                        return situationProfessionnelle;
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

        final List<PRPeriode> periodes = validerNombreJoursSoldes(viewBean);

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

        // la date de début du droit doit être ultérieure au 01.07.1999
        if (new JACalendarGregorian().compare("01.07.1999", dateDeDebutDroit) == JACalendar.COMPARE_FIRSTUPPER) {
            throw new Exception("Unable to create the APDroitAPG : " + session.getLabel("DROIT_TROP_ANCIEN"));
        }

        final APDroitAPG droitAPG = new APDroitAPG();
        droitAPG.setSession(session);

        if (modeEdition.equals(APModeEditionDroit.EDITION)) {
            // On va d'abord s'assurer qu'on a bien un id droit
            if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
                throw new Exception(
                        "Unable to update the APDroitAPG with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
            }
            // Ensuite on s'assure que le droit existe
            droitAPG.setIdDroit(viewBean.getIdDroit());
            droitAPG.retrieve(transaction);
            if (droitAPG.isNew()) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it doesn't already exist");
            }
            if (!(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(droitAPG.getEtat()) || IAPDroitLAPG.CS_ETAT_DROIT_ERREUR.equals(droitAPG.getEtat()) || IAPDroitLAPG.CS_ETAT_DROIT_VALIDE.equals(droitAPG.getEtat()))) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it is not 'En attente/En erreur'");
            }
        }

        // Création de l'idCaisse
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
        droitAPG.setCsCantonDomicile(viewBean.getCsCantonDomicile());

        if (modeEdition.equals(APModeEditionDroit.CREATION)) {
            droitAPG.add(transaction);
        } else {
            droitAPG.update(transaction);
        }

        remplacerPeriodesDroitAPG(session, transaction, droitAPG.getIdDroit(), viewBean.getPeriodes());
        return droitAPG;
    }

    private APDroitPaternite editionDroitPat(final BSession session, final BTransaction transaction,
                                             final APDroitPatPViewBean viewBean, final PRDemande demande, final APModeEditionDroit modeEdition)
            throws Exception {

        if (modeEdition == null) {
            throw new Exception("Any edition mode was specified for the APDroitAPG edition");
        }

        final List<PRPeriode> periodes = validerNombreJoursSoldesPat(viewBean);
        int joursSoldes = 0;
        List<String> datesDeDebut = new ArrayList<String>();
        List<String> datesDeFin = new ArrayList<String>();
        for (int ctr = 0; ctr < periodes.size(); ctr++) {
            datesDeDebut.add(periodes.get(ctr).getDateDeDebut());
            datesDeFin.add(periodes.get(ctr).getDateDeFin());
            joursSoldes += Integer.parseInt(periodes.get(ctr).getNbJour());
        }
        datesDeDebut = PRDateUtils.sortDate(datesDeDebut, DateOrder.NEWER_TO_OLDER);
        datesDeFin = PRDateUtils.sortDate(datesDeFin, DateOrder.OLDER_TO_NEWER);
        final String dateDeDebutDroit = datesDeDebut.get(0);
        final String dateDeFinDroit = datesDeFin.get(0);

        // la date de début du droit doit être ultérieure au 01.07.1999
        if (new JACalendarGregorian().compare("01.07.1999", dateDeDebutDroit) == JACalendar.COMPARE_FIRSTUPPER) {
            throw new Exception("Unable to create the APDroitAPG : " + session.getLabel("DROIT_TROP_ANCIEN"));
        }

        /**
         * CONTROLE SUR LE JOURS MAX ET DATE MAX
         */
        final String dateNaissance = viewBean.getDateDebutDroit();

        if (JadeStringUtil.isBlankOrZero(dateNaissance)) {
            throw new Exception(session.getLabel("DATE_DE_NAISSANCE_VIDE"));
        }
        String parameterName = null;
        parameterName = APParameter.PATERNITE.getParameterName();
        FWFindParameterManager manager = new FWFindParameterManager();
        manager.setSession(session);
        manager.setIdCleDiffere(parameterName);
        manager.setIdCodeSysteme("1");
        manager.find(BManager.SIZE_NOLIMIT);
        String dateMin = "01.01.2021";
        if (manager.size() > 0) {
            FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
            Date d = new SimpleDateFormat("yyyyMMdd").parse(param.getDateDebutValidite());
            dateMin = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH).format(d);
        }else{
            manager.setIdCodeSysteme("1");
            manager.find(BManager.SIZE_NOLIMIT);
            if (manager.size() > 0) {
                FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
                Date d = new SimpleDateFormat("yyyyMMdd").parse(param.getDateDebutValidite());
                dateMin = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH).format(d);
            }
        }
        if (JadeDateUtil.isDateBefore(dateNaissance, dateMin)) {
            String msgError = session.getLabel("ERREUR_MIN_DATE_NAI");
            msgError = PRStringUtils.replaceString(msgError, "{0}", dateMin);
            throw new Exception(msgError);
        }

        BigDecimal joursMax = new BigDecimal(14);

        parameterName = APParameter.PATERNITE_JOUR_MAX.getParameterName();
        manager.setIdCleDiffere(parameterName);
        manager.setIdCodeSysteme("1");
        manager.find(BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
            joursMax = new BigDecimal(param.getValeurNumParametre());
        }else{
            manager.setIdCodeSysteme("0");
            manager.find(BManager.SIZE_NOLIMIT);
            if (manager.size() > 0) {
                FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
                joursMax = new BigDecimal(param.getValeurNumParametre());
            }
        }
        if (joursSoldes > joursMax.intValue()) {
            throw new Exception(session.getLabel("ERREUR_MAX_JOURS"));
        }

        BigDecimal nombreMoisMaxApres =  new BigDecimal(6);

        parameterName = APParameter.PATERNITE_MOIS_MAX.getParameterName();
        manager.setIdCleDiffere(parameterName);
        manager.setIdCodeSysteme("1");
        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.size() > 0) {
            FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
            nombreMoisMaxApres = new BigDecimal(param.getValeurNumParametre());
        }else{
            manager.setIdCodeSysteme("0");
            manager.find(BManager.SIZE_NOLIMIT);
            if (manager.size() > 0) {
                FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
                nombreMoisMaxApres = new BigDecimal(param.getValeurNumParametre());
            }
        }


        String dateFinMax = JadeDateUtil.addMonths(dateDeDebutDroit, nombreMoisMaxApres.intValue());
        if (JadeDateUtil.isDateAfter(dateDeFinDroit, dateFinMax)) {
            String msgError = session.getLabel("ERREUR_MAX_DATE");
            msgError = PRStringUtils.replaceString(msgError, "{0}", dateDeFinDroit);
            msgError = PRStringUtils.replaceString(msgError, "{1}", dateFinMax);
            throw new Exception(msgError);
        }


        if (viewBean.getCsSexe().equals(ALCSTiers.SEXE_FEMME)) {
            throw new Exception(session.getLabel("ERROR_PATERNITE_S"));
        }

        //FIN DE CONTROLE

        final APDroitPaternite droitPat = new APDroitPaternite();
        droitPat.setSession(session);

        if (modeEdition.equals(APModeEditionDroit.EDITION)) {
            // On va d'abord s'assurer qu'on a bien un id droit
            if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
                throw new Exception(
                        "Unable to update the APDroitAPG with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
            }
            // Ensuite on s'assure que le droit existe
            droitPat.setIdDroit(viewBean.getIdDroit());
            droitPat.retrieve(transaction);
            if (droitPat.isNew()) {
                throw new Exception("Unable to update the APDroitPat with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it doesn't already exist");
            }
            if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(viewBean.getEtat())) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it is not 'En attente/En erreur'");
            }
        }

        // Création de l'idCaisse
        String idCaisse = null;
        try {
            final String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
            final String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
            idCaisse = noCaisse + noAgence;
        } catch (final PropertiesException exception) {
            throw new Exception("A fatal exception was thrown when accessing to the CommonProperties", exception);
        }

        // MAJ des champs du droit
        droitPat.setIdDemande(demande.getIdDemande());
        droitPat.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droitPat.setDateDebutDroit(dateDeDebutDroit);
        droitPat.setDateFinDroit(dateDeFinDroit);
        droitPat.setDateReception(viewBean.getDateReception());
        droitPat.setDateDepot(viewBean.getDateDepot());
        droitPat.setDroitAcquis(viewBean.getDroitAcquis());
        droitPat.setGenreService(viewBean.getGenreService());
        droitPat.setIdGestionnaire(viewBean.getIdGestionnaire());
        droitPat.setTauxImpotSource(viewBean.getTauxImpotSource());
        droitPat.setIsSoumisImpotSource(viewBean.getIsSoumisCotisation());
        droitPat.setNpa(viewBean.getNpa());
        droitPat.setPays(viewBean.getPays());
        droitPat.setRemarque(viewBean.getRemarque());
        droitPat.setNoRevision(viewBean.getNoRevision());
        droitPat.setNoControlePers(viewBean.getNoControlePers());
        droitPat.setNoCompte(viewBean.getNoCompte());
        droitPat.setNbrJourSoldes(viewBean.getNbrJourSoldes());
        droitPat.setDuplicata(viewBean.getDuplicata());
        droitPat.setCsProvenanceDroitAcquis(viewBean.getCsProvenanceDroitAcquis());
        droitPat.setIdCaisse(idCaisse);
        droitPat.setReference(viewBean.getReference());
        droitPat.setCsCantonDomicile(viewBean.getCsCantonDomicile());

        if (modeEdition.equals(APModeEditionDroit.CREATION)) {
            droitPat.add(transaction);
        } else {
            droitPat.update(transaction);
        }

        remplacerPeriodesDroitPat(session, transaction, droitPat.getIdDroit(), viewBean.getPeriodes());
        return droitPat;
    }

    private APDroitProcheAidant editionDroitPai(final BSession session, final BTransaction transaction,
                                    final APDroitPaiPViewBean viewBean, final PRDemande demande, final APModeEditionDroit modeEdition)
            throws Exception {

        if (modeEdition == null) {
            throw new Exception("Any edition mode was specified for the APDroitAPG edition");
        }

        if(JadeStringUtil.isBlankOrZero(viewBean.getDateDebutDroit())){
            String dateDebutDroit = viewBean.getPeriodes().stream()
                                            .map(PRPeriode::getDateDeDebut).map(Dates::toDate)
                                            .findFirst().map(Dates::formatSwiss).orElse("");
            viewBean.setDateDebutDroit(dateDebutDroit);
        }

        final List<PRPeriode> periodes = validerNombreJoursSoldesPai(viewBean);

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

        // la date de début du droit doit être ultérieure au 01.07.1999
        if (new JACalendarGregorian().compare("01.07.1999", dateDeDebutDroit) == JACalendar.COMPARE_FIRSTUPPER) {
            throw new Exception("Unable to create the APDroitAPG : " + session.getLabel("DROIT_TROP_ANCIEN"));
        }

        final APDroitProcheAidant droitAPG = new APDroitProcheAidant();
        droitAPG.setSession(session);

        if (modeEdition.equals(APModeEditionDroit.EDITION)) {
            // On va d'abord s'assurer qu'on a bien un id droit
            if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
                throw new Exception(
                        "Unable to update the APDroitAPG with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
            }
            // Ensuite on s'assure que le droit existe
            droitAPG.setIdDroit(viewBean.getIdDroit());
            droitAPG.retrieve(transaction);
            if (droitAPG.isNew()) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it doesn't already exist");
            }
            if (!(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(droitAPG.getEtat()) || IAPDroitLAPG.CS_ETAT_DROIT_ERREUR.equals(droitAPG.getEtat()) || IAPDroitLAPG.CS_ETAT_DROIT_VALIDE.equals(droitAPG.getEtat()))) {
                throw new Exception("Unable to update the APDroitAPG with id [" + viewBean.getDroit().getIdDroit()
                        + "] because it is not 'En attente/En erreur'");
            }
        }

        // Création de l'idCaisse
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
        droitAPG.setDateFinDroit(viewBean.getDateFinDroit());
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
        droitAPG.setCsCantonDomicile(viewBean.getCsCantonDomicile());

        if (modeEdition.equals(APModeEditionDroit.CREATION)) {
            droitAPG.add(transaction);
        } else {
            droitAPG.update(transaction);
        }

        remplacerPeriodesDroitPai(session, transaction, droitAPG.getIdDroit(), viewBean.getPeriodes());
        return droitAPG;
    }

    private List<PRPeriode> validerNombreJoursSoldes(final APDroitAPGPViewBean viewBean) throws Exception {
        final List<PRPeriode> periodes = viewBean.getPeriodes();

        if (periodes == null || periodes.isEmpty()) {
            throw new Exception("Aucne périodes définie pour le droit. Au moins une période doit être définie.");
        }

        int nombreDeJoursPeriodes = 0;
        for (PRPeriode periode : periodes) {
            nombreDeJoursPeriodes += (PRDateUtils.getNbDayBetween(periode.getDateDeDebut(), periode.getDateDeFin())
                    + 1);
        }
        if (Integer.valueOf(viewBean.getNbrJourSoldes()) > nombreDeJoursPeriodes) {
            throw new Exception("Le nombre de jours soldés dépasse le nombre de jours des périodes saisies");
        }
        return periodes;
    }


    private List<PRPeriode> validerNombreJoursSoldesPat(final APDroitPatPViewBean viewBean) throws Exception {
        final List<PRPeriode> periodes = viewBean.getPeriodes();

        if (periodes == null || periodes.isEmpty()) {
            throw new Exception("Aucne périodes définie pour le droit. Au moins une période doit être définie.");
        }

        int nombreDeJoursPeriodes = 0;
        int nombreDeJoursSaisies = 0;
        for (PRPeriode periode : periodes) {
            nombreDeJoursPeriodes += (PRDateUtils.getNbDayBetween(periode.getDateDeDebut(), periode.getDateDeFin())
                    + 1);
            if (!JadeStringUtil.isBlankOrZero(periode.getNbJour())) {
                nombreDeJoursSaisies += Integer.valueOf(periode.getNbJour());
            } else {
                nombreDeJoursSaisies = 0;
            }

        }
        if (nombreDeJoursSaisies > nombreDeJoursPeriodes) {
            throw new Exception("Le nombre de jours soldés dépasse le nombre de jours des périodes saisies");
        }
        return periodes;
    }

    private List<PRPeriode> validerNombreJoursSoldesPai(final APDroitPaiPViewBean viewBean) throws Exception {
        final List<PRPeriode> periodes = viewBean.getPeriodes();

        if (periodes == null || periodes.isEmpty()) {
            throw new Exception("Aucne périodes définie pour le droit. Au moins une période doit être définie.");
        }

        int nombreDeJoursPeriodes = 0;
        int nombreDeJoursSaisies = 0;
        for (PRPeriode periode : periodes) {
            nombreDeJoursPeriodes += (PRDateUtils.getNbDayBetween(periode.getDateDeDebut(), periode.getDateDeFin())
                    + 1);
            if (!JadeStringUtil.isBlankOrZero(periode.getNbJour())) {
                nombreDeJoursSaisies += Integer.valueOf(periode.getNbJour());
                if (!JadeStringUtil.isBlankOrZero(periode.getNbJoursupplementaire())) {
                    nombreDeJoursPeriodes += Integer.parseInt(periode.getNbJoursupplementaire());
                }
            } else {
                nombreDeJoursSaisies = 0;
            }

        }
        if (nombreDeJoursSaisies > nombreDeJoursPeriodes) {
            throw new Exception("Le nombre de jours soldés dépasse le nombre de jours des périodes saisies");
        }
        return periodes;
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
        if (APGUtils.isTypeAllocationPandemie(droit.getGenreService())) {
            final APDroitPandemie droitPandemie = new APDroitPandemie();
            droitPandemie.setSession(session);
            droitPandemie.setIdDroit(idDroit);
            droitPandemie.retrieve(transaction);
            if (droitPandemie.isNew()) {
                throw new APEntityNotFoundException(APDroitPandemie.class, idDroit);
            }
            return droitPandemie;
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            final APDroitMaternite droitMaternite = new APDroitMaternite();
            droitMaternite.setSession(session);
            droitMaternite.setIdDroit(idDroit);
            droitMaternite.retrieve(transaction);
            if (droitMaternite.isNew()) {
                throw new APEntityNotFoundException(APDroitMaternite.class, idDroit);
            }
            return droitMaternite;
        } else if (IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE.equals(droit.getGenreService())) {
            final APDroitPaternite droitPaternite = new APDroitPaternite();
            droitPaternite.setSession(session);
            droitPaternite.setIdDroit(idDroit);
            droitPaternite.retrieve(transaction);
            if (droitPaternite.isNew()) {
                throw new APEntityNotFoundException(APDroitPaternite.class, idDroit);
            }
            return droitPaternite;
        } else if (IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT.equals(droit.getGenreService())) {
            final APDroitProcheAidant droitProcheAidant = new APDroitProcheAidant();
            droitProcheAidant.setSession(session);
            droitProcheAidant.setIdDroit(idDroit);
            droitProcheAidant.retrieve(transaction);
            if (droitProcheAidant.isNew()) {
                throw new APEntityNotFoundException(APDroitPaternite.class, idDroit);
            }
            return droitProcheAidant;
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

    @Override
    public APDroitPaternite getDroitPaternite(final BSession session, final BTransaction transaction,
                                              final String idDroit) throws Exception, APEntityNotFoundException {

        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitPaternite)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitPaternite");
        }
        return (APDroitPaternite) droit;
    }

    @Override
    public APDroitProcheAidant getDroitProcheAidant(final BSession session, final BTransaction transaction,
                                              final String idDroit) throws Exception, APEntityNotFoundException {

        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitProcheAidant)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitProcheAidant");
        }
        return (APDroitProcheAidant) droit;
    }

    @Override
    public APDroitPandemie getDroitPandemie(final BSession session, final BTransaction transaction,
                                            final String idDroit) throws Exception, APEntityNotFoundException {

        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitPandemie)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitPandemie");
        }
        return (APDroitPandemie) droit;
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
        // On récupère tous les enfants de la sit. familiale
        final APEnfantAPGManager managerEnfants = new APEnfantAPGManager();
        managerEnfants.setSession(session);
        managerEnfants.setForIdSituationFamiliale(idSituationFamiliale);
        managerEnfants.find(transaction, BManager.SIZE_NOLIMIT);
        return managerEnfants.getContainer();
    }

    /**
     * La PRDemande est gérée de la façon suivante :</br>
     * - Une seule PRDemande existe pour un tiers dans un domaine
     * particulier(rente, apg, ij, etc)</br>
     * - Si une demande existe dans le domain des APG pour l'idTiers fournis en
     * paramètre, elle sera retournée</br>
     * - Si aucune demande existe dans le domain des APG pour l'idTiers, elle sera
     * créée et retournée</br>
     * <strong>INFO : L'état de la demande (ouverte, clôturée) n'est pas pris en compte. Toute
     * les demande sont en état 'ouverte'</strong>
     *
     * @param session     LA session courante
     * @param transaction La transaction courante
     * @param idTiers     L'idTiers du tiers en question
     * @return Une PRDemande liée au tiers dans le domain des APG
     * @throws Exception Si l'idTiers est null ou vide ou en cas d'erreur de persistance
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

    /**
     * La PRDemande est gérée de la façon suivante :</br>
     * - Une seule PRDemande existe pour un tiers dans un domaine
     * particulier(rente, apg, ij, etc)</br>
     * - Si une demande existe dans le domain des APG pour l'idTiers fournis en
     * paramètre, elle sera retournée</br>
     * - Si aucune demande existe dans le domain des APG pour l'idTiers, elle sera
     * créée et retournée</br>
     * <strong>INFO : L'état de la demande (ouverte, clôturée) n'est pas pris en compte. Toute
     * les demande sont en état 'ouverte'</strong>
     *
     * @param session     LA session courante
     * @param transaction La transaction courante
     * @param idTiers     L'idTiers du tiers en question
     * @return Une PRDemande liée au tiers dans le domain des APG
     * @throws Exception Si l'idTiers est null ou vide ou en cas d'erreur de persistance
     */
    public PRDemande getOrCreateDemandeDuTiersPat(final BSession session, final BTransaction transaction,
                                                  final String idTiers) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Unable to retrieve the PRDemande with an empty idTiers [" + idTiers + "]");
        }
        final PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setTypeDemande(IPRDemande.CS_TYPE_PATERNITE);
        demande.setIdTiers(idTiers);
        demande.retrieve(transaction);
        if (demande.isNew()) {
            demande.add(transaction);
        }
        return demande;
    }

    /**
     * La PRDemande est gérée de la façon suivante :</br>
     * - Une seule PRDemande existe pour un tiers dans un domaine
     * particulier(rente, apg, ij, etc)</br>
     * - Si une demande existe dans le domain des APG pour l'idTiers fournis en
     * paramètre, elle sera retournée</br>
     * - Si aucune demande existe dans le domain des APG pour l'idTiers, elle sera
     * créée et retournée</br>
     * <strong>INFO : L'état de la demande (ouverte, clôturée) n'est pas pris en compte. Toute
     * les demande sont en état 'ouverte'</strong>
     *
     * @param session     LA session courante
     * @param transaction La transaction courante
     * @param idTiers     L'idTiers du tiers en question
     * @return Une PRDemande liée au tiers dans le domain des APG
     * @throws Exception Si l'idTiers est null ou vide ou en cas d'erreur de persistance
     */
    public PRDemande getOrCreateDemandeDuTiersPai(final BSession session, final BTransaction transaction,
                                                  final String idTiers) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Unable to retrieve the PRDemande with an empty idTiers [" + idTiers + "]");
        }
        final PRDemande demande = new PRDemande();
        demande.setSession(session);
        demande.setTypeDemande(IPRDemande.CS_TYPE_PROCHE_AIDANT);
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
        // Le droit est chargé pour s'assurer que l'idDroit est valid et que le droit existe
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);
        if (!(droit instanceof APDroitAPG || droit instanceof APDroitPandemie || droit instanceof APDroitPaternite
                || droit instanceof APDroitProcheAidant)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitAPG");
        }
        // if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
        // throw new Exception("The provided id [" + idDroit +
        // "] does not match an APDroitAPG, it's an APDroitMaternite");
        // }

        // On récupère toutes les périodes du droit
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
     * Retourne les répartitions liées à chaque prestation standard du droit
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
        // TODO clauses à compléter
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

        @SuppressWarnings("unchecked") final Iterator<APSitProJointEmployeur> iter = apSitProJoiEmpMgr.iterator();

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
            throw new Exception(
                    "Unable to update the APDroitAPG with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
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
            throw new Exception(
                    "Unable to edit the APDroitAPG with id [" + droit.getIdDroit() + "] becuse it is not editable");
        }

        // Suppression des périodes déjà existante pour ce droit
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

    /**
     * (non-Javadoc)
     */
    @Override
    public APDroitPaternite miseAjourDroitPat(final BSession session, final BTransaction transaction,
                                              final APDroitPatPViewBean viewBean) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        // On va d'abord s'assurer qu'on a bien un id droit
        if (viewBean.getDroit() == null) {
            throw new Exception(
                    "The viewBean from type APDroitPatPViewBean has a null APDroitLAPG. Can not found the idDroit ");
        }
        if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new Exception(
                    "Unable to update the APDroitPaternite with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        // Validation des infos du viewBean
        validationPatViewBean(session, transaction, viewBean);

        APDroitPaternite droit = getDroitPaternite(session, transaction, viewBean.getDroit().getIdDroit());

        droit.setSession(session);
        droit.retrieve(transaction);
        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitAPG with id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        if (!droit.isModifiable()) {
            throw new Exception(
                    "Unable to edit the APDroitAPG with id [" + droit.getIdDroit() + "] becuse it is not editable");
        }

        // Suppression des périodes déjà existante pour ce droit
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

        droit = editionDroitPat(session, transaction, viewBean, demande, APModeEditionDroit.EDITION);
        /**
         * Prends le jours de la date de naissance
         */
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutDroit())) {
            droit.setDateDebutDroit(viewBean.getDateDebutDroit());
            droit.update();
        }
        return droit;
    }

    /**
     * (non-Javadoc)
     */
    @Override
    public APDroitProcheAidant miseAjourDroitPai(final BSession session, final BTransaction transaction,
                                              final APDroitPaiPViewBean viewBean) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        // On va d'abord s'assurer qu'on a bien un id droit
        if (viewBean.getDroit() == null) {
            throw new Exception(
                    "The viewBean from type APDroitPatPViewBean has a null APDroitLAPG. Can not found the idDroit ");
        }
        if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new Exception(
                    "Unable to update the APDroitPaternite with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        // Validation des infos du viewBean
        validationPaiViewBean(session, transaction, viewBean);

        APDroitProcheAidant droit = getDroitProcheAidant(session, transaction, viewBean.getDroit().getIdDroit());

        droit.setSession(session);
        droit.retrieve(transaction);
        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitAPG with id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        if (!droit.isModifiable()) {
            throw new Exception(
                    "Unable to edit the APDroitAPG with id [" + droit.getIdDroit() + "] becuse it is not editable");
        }

        // Suppression des périodes déjà existante pour ce droit
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

        droit = editionDroitPai(session, transaction, viewBean, demande, APModeEditionDroit.EDITION);

        droit.setDateFinDroit(viewBean.getDateFinDroit());
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutDroit())) {
            droit.setDateDebutDroit(viewBean.getDateDebutDroit());
            droit.update();
        }
        return droit;
    }


    /**
     * (non-Javadoc)
     */
    @Override
    public APDroitPandemie miseAjourDroitPan(final BSession session, final BTransaction transaction,
                                             final APDroitPanViewBean viewBean) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        // On va d'abord s'assurer qu'on a bien un id droit
        if (viewBean.getDroit() == null) {
            throw new Exception(
                    "The viewBean from type APDroitAPGPViewBean has a null APDroitLAPG. Can not found the idDroit ");
        }
        if (JadeStringUtil.isBlankOrZero(viewBean.getDroit().getIdDroit())) {
            throw new Exception(
                    "Unable to update the APDroitAPG with the empty id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        // Validation des infos du viewBean
        validationPandemieViewBean(session, transaction, viewBean);

        final APDroitPandemie droit = getDroitPandemie(session, transaction, viewBean.getDroit().getIdDroit());

        droit.setSession(session);
        droit.retrieve(transaction);
        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitAPG with id [" + viewBean.getDroit().getIdDroit() + "]");
        }
        if (!droit.isModifiable()) {
            throw new Exception(
                    "Unable to edit the APDroitAPG with id [" + droit.getIdDroit() + "] becuse it is not editable");
        }

        // Suppression des périodes déjà existante pour ce droit
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

        return editionDroitPan(viewBean, session, transaction, demande, APModeEditionDroit.EDITION);
    }


    private void reactiverDroitParent(final BSession session, final BTransaction transaction,
                                      final String idDroitParent) throws Exception {
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

        // Dans ce cas, le droit parent doit être remis dans l'état 'PARTIEL'.
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
    public void remplacerBreakRulesDuDroit(final BSession session, final BTransaction transaction, final String idDroit,
                                           final List<APBreakRulesFromView> nouvellesBreakRules) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        supprimerLesBreakRulesDuDroit(session, transaction, idDroit);

        // Pour chaque breakRule reçu depuis l'écrans de validation des prestation,
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

        // Pour chacune de ces breakRule, on va créer une entité correspondante en DB
        APBreakRule breakRuleEntity = null;
        for (final APBreakRulesFromView breakRule : nouvellesBreakRules) {
            breakRuleEntity = new APBreakRule();
            breakRuleEntity.setSession(session);
            breakRuleEntity.setIdDroit(idDroit);
            breakRuleEntity.setIdPrestation(breakRule.getIdPrestation());
            breakRuleEntity.setBreakRuleCode(breakRule.getCodeAsString());
            breakRuleEntity.setDateQuittance(JadeDateUtil.getGlobazFormattedDate(new Date()));
            breakRuleEntity.setGestionnaire(session.getUserName());
            breakRuleEntity.setLibelleBreakCode(breakRule.getLibelleBreakRule());
            breakRuleEntity.add(transaction);
        }
    }

    @Override
    public void remplacerPeriodesDroitAPG(final BSession session, final BTransaction transaction, final String idDroit,
                                          final List<PRPeriode> nouvellesPeriodes) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);

        if (!(droit instanceof APDroitAPG) && !(droit instanceof APDroitPandemie)) {
            throw new Exception("The provided id [" + idDroit + "] does not match an APDroitAPG or an APDroitPandemie");
        }

        // On supprime les périodes existantes
        supprimerLesPeriodesDuDroit(session, transaction, droit.getIdDroit());

        // On ordonne les nouvelles périodes
        Collections.sort(nouvellesPeriodes);

        // Et on les recréer
        for (final PRPeriode periode : nouvellesPeriodes) {
            final APPeriodeAPG p = new APPeriodeAPG();
            p.setSession(session);
            p.setIdDroit(idDroit);
            p.setDateDebutPeriode(periode.getDateDeDebut());
            p.setDateFinPeriode(periode.getDateDeFin());
            p.setNbrJours(periode.getNbJour());
            p.add(transaction);
        }
    }

    private void remplacerPeriodesDroitPat(BSession session, BTransaction transaction, String idDroit, List<PRPeriode> nouvellesPeriodes) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitPaternite droit = getDroitPaternite(session, transaction, idDroit);

        // On supprime les périodes existantes
        supprimerLesPeriodesDuDroit(session, transaction, droit.getIdDroit());

        // On ordonne les nouvelles périodes
        Collections.sort(nouvellesPeriodes);

        // Et on les recréer
        for (final PRPeriode periode : nouvellesPeriodes) {
            final APPeriodeAPG p = new APPeriodeAPG();
            p.setSession(session);
            p.setIdDroit(idDroit);
            p.setDateDebutPeriode(periode.getDateDeDebut());
            p.setDateFinPeriode(periode.getDateDeFin());
            p.setNbrJours(periode.getNbJour());
            p.setTauxImposition(periode.getTauxImposition());
            p.setCantonImposition(periode.getCantonImposition());
            p.wantCallValidate(false);
            p.add(transaction);
        }
    }

    private void remplacerPeriodesDroitPai(BSession session, BTransaction transaction, String idDroit, List<PRPeriode> nouvellesPeriodes) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitLAPG droit = getDroitLAPG(session, transaction, idDroit);

        // On supprime les périodes existantes
        supprimerLesPeriodesDuDroit(session, transaction, droit.getIdDroit());

        // On ordonne les nouvelles périodes
        Collections.sort(nouvellesPeriodes);

        // Et on les recréer
        for (final PRPeriode periode : nouvellesPeriodes) {
            final APPeriodeAPG p = new APPeriodeAPG();
            p.setSession(session);
            p.setIdDroit(idDroit);
            p.setDateDebutPeriode(periode.getDateDeDebut());
            p.setDateFinPeriode(periode.getDateDeFin());
            p.setNbrJours(periode.getNbJour());
            p.setTauxImposition(periode.getTauxImposition());
            p.setCantonImposition(periode.getCantonImposition());
            p.setNbJourSupplementaire(periode.getNbJoursupplementaire());
            p.wantCallValidate(false);
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
            throw new Exception(
                    "Unable to delete the APDroitAPG with id [" + idDroit + "] because it is nott editable");
        }

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            throw new Exception(
                    "The provided id [" + idDroit + "] does not match an APDroitAPG, it's an APDroitMaternite");
        }

        supprimerSituationFamilialle(session, transaction, droit.getIdSituationFam());
        supprimerLesPeriodesDuDroit(session, transaction, idDroit);
        supprimerLesSituationProfessionnelle(session, transaction, idDroit);
        supprimerLesPrestationsDuDroit(session, transaction, idDroit);
        supprimerLesBreakRulesDuDroit(session, transaction, idDroit);

        // les prestations annulées du droit parent doivent être misent dans l'etat validé
        if (!JadeStringUtil.isBlankOrZero(droit.getIdDroitParent())) {
            reactiverDroitParent(session, transaction, droit.getIdDroitParent());
        }

        droit.delete(transaction);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.utils.APEntityService#supprimerDroitComplet(globaz.globall.db.BSession,
     * globaz.globall.db.BTransaction, java.lang.String)
     */
    @Override
    public void supprimerDroitCompletPan(final BSession session, final BTransaction transaction, final String idDroit)
            throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception("Unable to delete the APDroitAPG with an empty id [" + idDroit + "]");
        }
        validateSessionAndTransactionNotNull(session, transaction);
        final APDroitPandemie droit = new APDroitPandemie();
        droit.setSession(session);
        droit.setIdDroit(idDroit);
        droit.retrieve(transaction);

        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitAPG with id [" + idDroit + "] to delete it");
        }

        if (!droit.isModifiable()) {
            throw new Exception(
                    "Unable to delete the APDroitAPG with id [" + idDroit + "] because it is nott editable");
        }

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())) {
            throw new Exception(
                    "The provided id [" + idDroit + "] does not match an APDroitAPG, it's an APDroitMaternite");
        }

        supprimerSituationFamiliallePan(session, transaction, idDroit);
        supprimerLesPeriodesDuDroit(session, transaction, idDroit);
        supprimerLesSituationProfessionnelle(session, transaction, idDroit);
        supprimerLesPrestationsDuDroit(session, transaction, idDroit);
        supprimerLesBreakRulesDuDroit(session, transaction, idDroit);
        supprimerLesDonnesPanDuDroit(session, transaction, idDroit);

        // les prestations annulées du droit parent doivent être misent dans l'etat validé
        if (!JadeStringUtil.isBlankOrZero(droit.getIdDroitParent())) {
            reactiverDroitParent(session, transaction, droit.getIdDroitParent());
        }

        droit.delete(transaction);
    }

    private void supprimerSituationFamiliallePan(BSession session, BTransaction transaction, String idDroit) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception(
                    "Unable to delete the situationFamilialePan for an APDroitPandemie with an empty id [" + idDroit + "]");
        }
        APSituationFamilialePanManager manager = new APSituationFamilialePanManager();
        manager.setSession(session);
        manager.setForIdDroit(idDroit);
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            final APSituationFamilialePan situationFamilialePan = (APSituationFamilialePan) manager.getEntity(i);
            situationFamilialePan.setSession(session);
            situationFamilialePan.delete(transaction);
        }
    }

    private void supprimerLesDonnesPanDuDroit(BSession session, BTransaction transaction, String idDroit) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception(
                    "Unable to delete the APPrestation for an APDroitAPG with an empty id [" + idDroit + "]");
        }
        APDroitPanSituationManager manager = new APDroitPanSituationManager();
        manager.setSession(session);
        manager.setForIdDroit(idDroit);
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            final APDroitPanSituation droitPanSituation = (APDroitPanSituation) manager.getEntity(i);
            droitPanSituation.setSession(session);
            droitPanSituation.delete(transaction);
        }
    }

    private void supprimerEmployeur(final BSession session, final BTransaction transaction, final String idEmployeur)
            throws Exception {
        if (JadeStringUtil.isBlankOrZero(idEmployeur)) {
            throw new Exception(
                    "Unable to retrieve the APEmployeur with an empty id [" + idEmployeur + "] to delete it");
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
            throw new Exception(
                    "Unable to delete the APPeriodeAPG for an APDroitAPG with an empty id [" + idDroit + "]");
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
     * Supprime l'intégralité du graphe d'objet lié aux prestations d'un droit APG
     */
    @Override
    public void supprimerLesPrestationsDuDroit(final BSession session, final BTransaction transaction,
                                               final String idDroit) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception(
                    "Unable to delete the APPrestation for an APDroitAPG with an empty id [" + idDroit + "]");
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
     * Supprime l'intégralité du graphe d'objet lié aux prestations d'un droit APG par genre
     */
    @Override
    public void supprimerLesPrestationsDuDroitParGenre(final BSession session, final BTransaction transaction,
                                                       final String idDroit, String genre) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new Exception(
                    "Unable to delete the APPrestation for an APDroitAPG with an empty id [" + idDroit + "]");
        }
        final APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(session);
        prestationManager.setForIdDroit(idDroit);
        prestationManager.setForGenre(genre);
        prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < prestationManager.size(); i++) {
            final APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
            if (!APTypeDePrestation.MATCIAB2.isCodeSystemEqual(genre) || JadeNumericUtil.isEmptyOrZero(prestation.getMontantBrut()) || JadeNumericUtil.isNumericPositif(prestation.getMontantBrut())) {
                prestation.setSession(session);
                prestation.delete(transaction);
            }
        }
    }

    @Override
    public APDroitPanSituation getDroitPanSituation(BSession session, BTransaction transaction, String idDroit) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        if (JadeStringUtil.isBlankOrZero(idDroit)) {
            throw new IllegalArgumentException("Unable to read the APDroitPanSituation with the empty idDroit [" + idDroit + "]");
        }

        APDroitPanSituationManager manager = new APDroitPanSituationManager();
        manager.setSession(session);
        manager.setForIdDroit(idDroit);
        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.size() > 1) {
            throw new APBusinessException("Le droit " + idDroit + " possède plusieurs situations.");
        } else if (manager.size() == 1) {
            return (APDroitPanSituation) manager.getFirstEntity();
        } else {
            return new APDroitPanSituation();
        }
    }

    @Override
    public APDroitPanSituation miseAjourDroitPanSituation(BSession session, BTransaction transaction, APDroitPanSituationViewBean viewBean) throws Exception {
        validateSessionAndTransactionNotNull(session, transaction);
        // On va d'abord s'assurer qu'on a bien un id droit
        if (viewBean.getDroitPanSituation() == null) {
            throw new Exception(
                    "The viewBean from type APDroitPanSituationViewBean has a null APDroitPanSituation. Can not found the idDroit ");
        }
        if (JadeStringUtil.isBlankOrZero(viewBean.getIdDroit())) {
            throw new Exception(
                    "Unable to update the APDroitPanSituation with the empty idDroit [" + viewBean.getIdDroit() + "]");
        }

        final APDroitPanSituation droit = getDroitPanSituation(session, transaction, viewBean.getIdDroit());

        if (droit.isNew()) {
            throw new Exception("Unable to retrieve the APDroitPanSituation with idDroit [" + viewBean.getIdDroit() + "]");
        }

        return editionDroitPandemieSituation(viewBean, session, transaction, APModeEditionDroit.EDITION);
    }

    /**
     * Supprime toutes les situations professionnelles liées au droit ainsi que les employeurs associés à chaque sit
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
            // Supression de 'employeur associé à cette sit prof
            supprimerEmployeur(session, transaction, sitPro.getIdEmployeur());
            sitPro.delete(transaction);
        }
    }

    /**
     * Supprime la situation familiale ainsi que tous les enfants associés
     *
     * @param session
     * @param transaction
     * @param idSituationFamiliale
     * @throws Exception
     */
    private void supprimerSituationFamilialle(final BSession session, final BTransaction transaction,
                                              final String idSituationFamiliale) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idSituationFamiliale)) {
            throw new Exception(
                    "Unable to delete the APSituationFamilialeAPG with an empty id [" + idSituationFamiliale + "]");
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
        // Date de dépôt
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateDepot())) {
            errors.add(APValidationDroitError.DATE_DEPOT_VIDE);
        }
        // Date de réception
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
        // Jours soldés
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
        // Périodes
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
                } else if ((PRDateUtils.compare(periode.getDateDeDebut(),
                        periode.getDateDeFin()) != PRDateEquality.AFTER)
                        && (PRDateUtils.compare(periode.getDateDeDebut(),
                        periode.getDateDeFin()) != PRDateEquality.EQUALS)) {
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
            // On test le chevauchement uniquement s'il n'y a pas d'erreurs lors des contrôles précédents des périodes
            // Ceci dans le but d'éviter les exceptions si un format de date est incorrect
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
            // // Numéro de contrôle ou controlNumber
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

        // NPA : si le pays est Suisse, le NPA doit être renseigné
        String npa = null;
        if (IConstantes.ID_PAYS_SUISSE.equals(viewBean.getPays())) {
            npa = viewBean.getNpa();
            if ((npa == null) || (npa.length() < 4)) {
                errors.add(APValidationDroitError.NPA_VIDE);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(npa)) {
            // le pays doit être la suisse
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

    private void validationPatViewBean(final BSession session, final BTransaction transaction,
                                       final APDroitPatPViewBean viewBean) throws IllegalArgumentException {
        final List<APValidationDroitError> errors = new ArrayList<APValidationDroitError>();
        // NSS
        if (!PRNSSUtil.isValidNSS(viewBean.getNss(), FormatNSS.COMPLET_FORMATE)) {
            errors.add(APValidationDroitError.NSS_INVALID);
        }
        // Date de dépôt
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateDepot())) {
            errors.add(APValidationDroitError.DATE_DEPOT_VIDE);
        }
        // Date de réception
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
        // Jours soldés
//        int nbrJourSoldes = 0;
//        if ((viewBean.getNbrJourSoldes() == null) || !JadeNumericUtil.isIntegerPositif(viewBean.getNbrJourSoldes())) {
//            errors.add(APValidationDroitError.JOURS_SOLDE_INVALID);
//        } else {
//            try {
//                nbrJourSoldes = Integer.valueOf(viewBean.getNbrJourSoldes());
//            } catch (final NumberFormatException e) {
//                // Nothing to do with e. We only say value is invalid
//                errors.add(APValidationDroitError.JOURS_SOLDE_INVALID);
//            }
//        }
        // Périodes
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
                } else if ((PRDateUtils.compare(periode.getDateDeDebut(),
                        periode.getDateDeFin()) != PRDateEquality.AFTER)
                        && (PRDateUtils.compare(periode.getDateDeDebut(),
                        periode.getDateDeFin()) != PRDateEquality.EQUALS)) {
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
//            if (nbrJourSoldes < nombrePeriodes) {
//                errors.add(APValidationDroitError.JOURS_SOLDE_INSUFFISANT);
//            }
            // On test le chevauchement uniquement s'il n'y a pas d'erreurs lors des contrôles précédents des périodes
            // Ceci dans le but d'éviter les exceptions si un format de date est incorrect
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
            // // Numéro de contrôle ou controlNumber
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

        // NPA : si le pays est Suisse, le NPA doit être renseigné
        String npa = null;
        if (IConstantes.ID_PAYS_SUISSE.equals(viewBean.getPays())) {
            npa = viewBean.getNpa();
            if ((npa == null) || (npa.length() < 4)) {
                errors.add(APValidationDroitError.NPA_VIDE);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(npa)) {
            // le pays doit être la suisse
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

    private void validationPaiViewBean(final BSession session, final BTransaction transaction,
                                       final APDroitPaiPViewBean viewBean) throws IllegalArgumentException {
        final List<APValidationDroitError> errors = new ArrayList<>();
        // NSS
        if (!PRNSSUtil.isValidNSS(viewBean.getNss(), FormatNSS.COMPLET_FORMATE)) {
            errors.add(APValidationDroitError.NSS_INVALID);
        }
        // Date de dépôt
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateDepot())) {
            errors.add(APValidationDroitError.DATE_DEPOT_VIDE);
        }
        // Date de réception
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
        // Jours soldés

        // Périodes
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
                } else if ((PRDateUtils.compare(periode.getDateDeDebut(),
                        periode.getDateDeFin()) != PRDateEquality.AFTER)
                        && (PRDateUtils.compare(periode.getDateDeDebut(),
                        periode.getDateDeFin()) != PRDateEquality.EQUALS)) {
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
            // // Numéro de contrôle ou controlNumber
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

        // NPA : si le pays est Suisse, le NPA doit être renseigné
        String npa = null;
        if (IConstantes.ID_PAYS_SUISSE.equals(viewBean.getPays())) {
            npa = viewBean.getNpa();
            if ((npa == null) || (npa.length() < 4)) {
                errors.add(APValidationDroitError.NPA_VIDE);
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(npa)) {
            // le pays doit être la suisse
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

        String messageError = checkPeriodesChevauchantes(viewBean, errors);

        errors.addAll(validerNoCompte(session, genreDeService, viewBean.getNoCompte()));
        if (errors.size() > 0 || !messageError.isEmpty()) {
            final StringBuilder message = new StringBuilder();
            for (final APValidationDroitError error : errors) {
                message.append(session.getLabel(error.getErrorLabel()));
                message.append("</br>");
            }
            message.append(messageError);
            throw new IllegalArgumentException(message.toString());
        }
    }

    private String checkPeriodesChevauchantes(final APDroitPaiPViewBean viewBean, final List<APValidationDroitError> errors) {
        String idTiers = Exceptions.checkedToUnChecked(() -> viewBean.loadDemande().getIdTiers(), "Impossible to load the demand");

        String date = viewBean.getPeriodes().stream()
                              .findFirst()
                              .map(PRPeriode::getDateDeDebut)
                              .map(Dates::toDbDate)
                              .orElse("0");
        //language=sql
        SQLWriter sqlWriter = SQLWriter.writeWithSchema()
                                       .append("" +
                                                       "select schema.APDROIP.VAIDRO as id_droit," +
                                                       "       schema.APPERIP.VCDDEB," +
                                                       "       schema.APPERIP.VCDFIN" +
                                                       "  from schema.PRDEMAP" +
                                                       " inner join schema.APDROIP on schema.APDROIP.VAIDEM = schema.PRDEMAP.WAIDEM" +
                                                       " inner join schema.APDROITPROCHEAIDANT " +
                                                       "    on schema.APDROITPROCHEAIDANT.ID_DROIT = schema.APDROIP.VAIDRO" +
                                                       " inner join schema.APPERIP ON schema.APPERIP.VCIDRO = schema.APDROIP.VAIDRO" +
                                                       " where WAITIE = ? " +
                                                       "   and ? between VCDDEB and VCDFIN" +
                                                       "   and 0 = (select count(*) from schema.APDROIP as droitEnfant " +
                                                       "                           where droitEnfant.VAIPAR = schema.APDROIP.VAIDRO)"+
                                                       "   and 0 = (select count(*) from schema.APDROIP as droitCorrige "+
                                                       "                           where droitCorrige.VAIPAR = (select droitEncours.VAIPAR from schema.APDROIP as droitEncours where droitEncours.VAIDRO = ?) "+
                                                       "   and droitCorrige.VAIDRO = schema.APDROIP.VAIDRO) "
                                               ,idTiers, date, viewBean.getIdDroit())
                                       .append("and schema.APDROIP.VAIDRO != ? ",viewBean.getIdDroit());



        List<SamePeriode> samePeriodes = SCM.newInstance(SamePeriode.class)
                                            .session(viewBean.getSession())
                                            .query(sqlWriter.toSql()).execute();
        if (!samePeriodes.isEmpty()) {
            return MessageFormat.format(viewBean.getSession().getLabel(APValidationDroitError.PERIODE_CHEVAUCHANTE_AVEC_AUTRE_DROIT.getErrorLabel())
                    ,samePeriodes.get(0).getIdDroit());
        }
        return "";
    }

    /**
     * Validation du numero de compte en fonction du genre de service
     *
     * @param
     */
    private List<APValidationDroitError> validerNoCompte(final BSession session, final String genreServiceCS,
                                                         final String numeroCompte) {
        final List<APValidationDroitError> errors = new ArrayList<APValidationDroitError>();

        final String genreService = session.getCode(genreServiceCS);

        // Selon bulletin 162
        // Genre de service = 20 ou 21
        // Si le numero de compte est < 100000
        // le format est CCIIF avec
        // CC = cantons = 1 à 26 et 50
        // II = centre d'instruction = 11 ou 99. Sont autorises en plus pour
        // les cantons suivants:
        // ZH 01 : 12, 13
        // BE 02 : 12, 13, 14, 15, 16
        // GE 25 : 12
        // CCS 26 : 00 remplace le 11
        // F = filiale = 1 ou 2
        // Si le numero de compte est > 100000
        // le format est CCNNNNF avec
        // CC = cantons = 1 à 25 et 50
        // NNNN = NPA = (pas de controle sur la validite du NPA)
        // F = filiale = 1 ou 2
        if ((APGenreServiceAPG.ProtectionCivileServiceNormale.getCodePourAnnonce().equals(genreService)
                || APGenreServiceAPG.ProtectionCivileCadreSpecialiste.getCodePourAnnonce().equals(genreService)
                || APGenreServiceAPG.ProtectionCivileCommandant.getCodePourAnnonce().equals(genreService)
                || APGenreServiceAPG.ProtectionCivileFormationDeBase.getCodePourAnnonce().equals(genreService))
                && !JadeStringUtil.isIntegerEmpty(numeroCompte)) {

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

    /**
     * Récupère le nombre de jours des prestations des autres droits pour le même genre de service
     *
     * @param session
     * @param idDroit
     * @return
     * @throws Exception
     */
    @Override
    public int getTotalJourAutreDroit(BSession session, String idDroit) throws Exception {
        String sql = "SELECT COALESCE(SUM("
                + "case when pr.VHTTYP = '" + IAPPrestation.CS_TYPE_ANNULATION + "' then -pr.VHNNJS " // si annulation retirer le nombre de jour
                + "else pr.VHNNJS end),0) "
                + "FROM :schema.APDROIP dr1 "
                + "INNER JOIN :schema.PRDEMAP dm1 ON dr1.VAIDEM=dm1.WAIDEM "
                + "INNER JOIN :schema.TITIERP ON dm1.WAITIE=:schema.TITIERP.HTITIE "
                + "INNER JOIN :schema.PRDEMAP dm2 ON dm2.WAITIE=:schema.TITIERP.HTITIE "
                + "INNER JOIN :schema.APDROIP dr2 ON dr2.VAIDEM=dm2.WAIDEM "
                + "and dr2.VAIDRO <> :idDroit and dr2.VAIDRO <> dr1.VAIPAR " // différent du droit en court et du droit parent
                + "INNER JOIN :schema.APPRESP pr ON pr.VHIDRO=dr2.VAIDRO "
                + "WHERE dr1.VAIDRO=:idDroit and dr2.VATGSE = dr1.VATGSE ";

        sql = sql.replace(":schema", Jade.getInstance().getDefaultJdbcSchema());
        sql = sql.replace(":idDroit", idDroit);
        BStatement statement = new BStatement(session.getCurrentThreadTransaction());
        statement.createStatement();
        ResultSet res = statement.executeQuery(sql);
        res.next();
        return JadeStringUtil.toInt(res.getObject(1).toString());
    }

    /**
     * Récupère les autres droits pour le même nss et le même genre de service
     *
     * @param session
     * @param idDroit
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getAutresDroits(BSession session, String idDroit) throws Exception {
        List<String> listDroit = new ArrayList<>();
        String sql = "SELECT dr2.VAIDRO as idDroit "
                + "FROM :schema.APDROIP dr1 "
                + "INNER JOIN :schema.PRDEMAP dm1 ON dr1.VAIDEM=dm1.WAIDEM "
                + "INNER JOIN :schema.TITIERP ON dm1.WAITIE=:schema.TITIERP.HTITIE "
                + "INNER JOIN :schema.PRDEMAP dm2 ON dm2.WAITIE=:schema.TITIERP.HTITIE "
                + "INNER JOIN :schema.APDROIP dr2 ON dr2.VAIDEM=dm2.WAIDEM "
                + "and dr2.VAIDRO <> :idDroit and dr2.VAIDRO <> dr1.VAIPAR " // différent du droit en court et du droit parent
                + "WHERE dr1.VAIDRO=:idDroit and dr2.VATGSE = dr1.VATGSE "
                + "AND dr2.VADDDR < dr1.VADDDR";

        sql = sql.replace(":schema", Jade.getInstance().getDefaultJdbcSchema());
        sql = sql.replace(":idDroit", idDroit);
        BStatement statement = new BStatement(session.getCurrentThreadTransaction());
        statement.createStatement();
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {
            listDroit.add(res.getString("idDroit"));
        }
        return listDroit;
    }

    @Data
    public static class SamePeriode {
        private String dateDebut;
        private String dateFin;
        private String idDroit;
    }
}
