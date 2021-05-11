/*
 * Créé le 27 juin 06
 */
package globaz.apg.impl.process;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.api.process.IAPGenererCompensationProcess;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.lots.APCompensation;
import globaz.apg.db.lots.APCompensationManager;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsJointEmployeur;
import globaz.apg.db.prestation.APRepartitionPaiementsJointEmployeurManager;
import globaz.apg.process.Key;
import globaz.apg.process.MontantsPorterEnCompte;
import globaz.apg.properties.APProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <H1>Process de la génération des compensations</H1>
 * 
 * Une copie générale du module APGenererCompensationsProcess003.
 * Traitement spécial :
 * 
 * Si paiement à l'employeur et que la case à cocher porter en compte est actif,
 * alors on compense sur les factures futures.
 * 
 * @author dcl
 */
public class APGenererCompensationsProcess006 extends BProcess implements IAPGenererCompensationProcess {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(APGenererCompensationsProcess006.class.getName());

    private String forIdLot = "";
    private String moisPeriodeFacturation = "";

    public APGenererCompensationsProcess006() {
        super();
    }

    public APGenererCompensationsProcess006(BProcess parent) {
        super(parent);
    }

    public APGenererCompensationsProcess006(BSession session) {
        super(session);
    }

    @Override
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    @Override
    public void setMoisPeriodeFacturation(String string) {
        moisPeriodeFacturation = string;
    }

    public String getMoisPeriodeFacturation() {
        return moisPeriodeFacturation;
    }

    @Override
    protected boolean _executeProcess() {
        final BSession session = getSession();
        final BTransaction transaction = getTransaction();

        try {
            /**
             * Vérification des règles métiers
             */
            if (!checkBusinessRules()) {
                return true;
            }

            /**
             * Suppression de toutes les compensations existantes du lot courant
             */
            removeExistingCompensations(session, transaction);

            /**
             * Création des clés de regroupement
             */
            final SortedMap<Key, MontantsPorterEnCompte> keys = creationOfKeys(session, transaction);

            /**
             * Création des nouvelles compensations et des nouvelles factures à compenser par clé de regroupement
             */
            manageCompensation(session, transaction, keys);

            /**
             * Mise à jour du lot
             */
            updateLot(session, transaction);
        } catch (Exception e) {
            log(Level.INFO, e, e.getMessage());

            try {
                transaction.rollback();
            } catch (Exception e1) {
                log(Level.INFO, e1, e1.getMessage());
            }

            memoryLog(e.getMessage(), "");
        }

        return true;
    }

    private void updateLot(final BSession session, final BTransaction transaction) throws Exception {

        final APLot lot = new APLot();
        lot.setSession(session);
        lot.setIdLot(forIdLot);
        lot.retrieve(transaction);

        lot.setEtat(IAPLot.CS_COMPENSE);
        lot.update(transaction);
    }

    private void manageCompensation(final BSession session, final BTransaction transaction,
            final SortedMap<Key, MontantsPorterEnCompte> keys) throws Exception {

        for (Entry<Key, MontantsPorterEnCompte> entrySet : keys.entrySet()) {
            final Key key = entrySet.getKey();
            final MontantsPorterEnCompte value = entrySet.getValue();

            /**
             * Création d'une nouvelle compensation par clé de regroupement
             */
            final APCompensation compensation = createCompensation(session, transaction, key, value);

            /**
             * Création d'une factures futures à compenser par clé de regroupement
             */
            createFactureFutureACompenser(session, key, compensation, value);

            /**
             * Mise à jour des répartitions
             */
            updateRepartitions(session, transaction, key, compensation);
        }
    }

    private void memoryLog(final String message, final String level) {
        memoryLog(message, level, new Object[] {});
    }

    private void memoryLog(final String message, final String level, Object... objects) {
        String messageToPrint = message;
        if (message == null) {
            messageToPrint = "";
        }
        if (objects == null || objects.length == 0) {
            getMemoryLog().logMessage(FWMessageFormat.prepareQuotes(messageToPrint, false), level, labelProcess());
        } else {
            getMemoryLog().logMessage(
                    MessageFormat.format(FWMessageFormat.prepareQuotes(messageToPrint, false), objects), level,
                    labelProcess());
        }

        logger.log(Level.INFO, messageToPrint + " - " + labelProcess());
    }

    private String labelProcess() {
        return getSession().getLabel(PROCESS_GENERER_COMPENSATIONS);
    }

    private void updateRepartitions(final BSession session, final BTransaction transaction, final Key key,
            final APCompensation compensation) throws Exception {

        /**
         * mise à jour des repartitions pour lier la nouvelle compensation)
         */
        final APRepartitionPaiementsJointEmployeurManager repartitionPaiementsJointEmployeurManager = new APRepartitionPaiementsJointEmployeurManager();
        repartitionPaiementsJointEmployeurManager.setSession(session);
        repartitionPaiementsJointEmployeurManager.setForIdLot(forIdLot);
        repartitionPaiementsJointEmployeurManager.setForIdTiers(key.idTiers);
        repartitionPaiementsJointEmployeurManager.setForIdAffilie(key.idAffilie);
        repartitionPaiementsJointEmployeurManager.setForIdParticularite(key.idExtra2);
        repartitionPaiementsJointEmployeurManager.setForInGenrePrestation(Arrays.asList(key.genrePrestation));

        final BStatement statement = repartitionPaiementsJointEmployeurManager.cursorOpen(transaction);

        APRepartitionPaiementsJointEmployeur repartitionPaiements;
        while ((repartitionPaiements = (APRepartitionPaiementsJointEmployeur) repartitionPaiementsJointEmployeurManager
                .cursorReadNext(statement)) != null) {
            if (compensation.getIsPorteEnCompte()) {
                if (isSituationProfPorteEnCompte(repartitionPaiements.getIdSituationProfessionnelle())) {
                    /**
                     * Paternité : On vérie si le même droit pour prendre le bon ID de compensation.
                     */
                    if(repartitionPaiements.getGenreService().equals(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE)
                        || repartitionPaiements.getGenreService().equals(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT)){
                        if(repartitionPaiements.getIdDroit().equals(compensation.getIdDroit())){
                            repartitionPaiements.setIdCompensation(compensation.getIdCompensation());
                            repartitionPaiements.wantMiseAJourLot(false);
                            repartitionPaiements.update(transaction);
                            memoryLog(getSession().getLabel("REPARTITION_MISE_A_JOUR"), "",
                                    repartitionPaiements.getIdRepartitionBeneficiairePaiement());
                        }
                    }else{
                        repartitionPaiements.setIdCompensation(compensation.getIdCompensation());
                        repartitionPaiements.wantMiseAJourLot(false);
                        repartitionPaiements.update(transaction);
                        memoryLog(getSession().getLabel("REPARTITION_MISE_A_JOUR"), "",
                                repartitionPaiements.getIdRepartitionBeneficiairePaiement());
                    }
                }
            } else {
                if(repartitionPaiements.getGenreService().equals(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE)
                        || repartitionPaiements.getGenreService().equals(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT)){
                    if(repartitionPaiements.getIdDroit().equals(compensation.getIdDroit())){
                        repartitionPaiements.setIdCompensation(compensation.getIdCompensation());
                        repartitionPaiements.wantMiseAJourLot(false);
                        repartitionPaiements.update(transaction);
                        memoryLog(getSession().getLabel("REPARTITION_MISE_A_JOUR"), "",
                                repartitionPaiements.getIdRepartitionBeneficiairePaiement());
                    }
                }else{
                    repartitionPaiements.setIdCompensation(compensation.getIdCompensation());
                    repartitionPaiements.wantMiseAJourLot(false);
                    repartitionPaiements.update(transaction);
                    memoryLog(getSession().getLabel("REPARTITION_MISE_A_JOUR"), "",
                            repartitionPaiements.getIdRepartitionBeneficiairePaiement());
                }
            }
        }
        repartitionPaiementsJointEmployeurManager.cursorClose(statement);
        repartitionPaiementsJointEmployeurManager.clear();
    }

    private void createFactureFutureACompenser(final BSession session, final Key key,
            final APCompensation compensation, MontantsPorterEnCompte value) throws Exception {

        /**
         * Création d'une facture à compenser uniquement pour les affiliés et que le montant porter en compte contient
         * quelque chose
         */
        if (!JadeStringUtil.isIntegerEmpty(compensation.getIdAffilie()) && !value.getMontantPorterEnCompte().isZero()) {

            final IAFAffiliation employeur = getAffiliation(session, key);
            createFactureACompenser(session, compensation, value, employeur);

            /**
             * Un employeur qui est en cours de radiation ou est radié, nous mettons une avertissement en indiquant que
             * l'on à mis la facture à compenser non compensable automatiquement
             */
            if (isRadie(employeur)) {
                memoryLog(getSession().getLabel("FACTURE_A_COMPENSER_DESACTIVER_RADIE"), FWMessage.AVERTISSEMENT,
                        employeur.getAffilieNumero());
            }
        }
    }

    private void createFactureACompenser(final BSession session, final APCompensation compensation,
            MontantsPorterEnCompte value, final IAFAffiliation employeur) throws Exception {

        final APFactureACompenser factureACompenser = new APFactureACompenser();

        factureACompenser.setSession(session);
        factureACompenser.setIdTiers(compensation.getIdTiers());
        factureACompenser.setIdAffilie(compensation.getIdAffilie());
        factureACompenser.setIdCompensationParente(compensation.getIdCompensation());

        String montantAReporter = value.getMontantPorterEnCompte().toString();
        boolean mustBeCompenser = true;
        if (value.getMontantTotal().doubleValue() < value.getMontantPorterEnCompte().doubleValue()) {
            montantAReporter = value.getMontantTotal().toString();

            if (value.getMontantTotal().isZero() || value.getMontantTotal().isNegative()) {
                mustBeCompenser = false;
            }
        }

        factureACompenser.setIsCompenser(isRadie(employeur) || !mustBeCompenser ? Boolean.FALSE : Boolean.TRUE);
        factureACompenser.setMontant(montantAReporter);

        factureACompenser.add();

        getMemoryLog().logMessage(getSession().getLabel("FACTURE_AJOUTEE"), "",
                factureACompenser.getIdFactureACompenser());
    }

    private APCompensation createCompensation(final BSession session, final BTransaction transaction, final Key key,
            final MontantsPorterEnCompte value) throws Exception {

        final APCompensation compensation = new APCompensation();
        compensation.setSession(session);
        compensation.setIdLot(forIdLot);
        compensation.setIdTiers(key.idTiers);
        compensation.setIdAffilie(key.idAffilie);
        compensation.setMontantTotal(value.getMontantTotal().toString());
        compensation.setGenrePrestation(key.genrePrestation);
        compensation.setIsIndependant(key.isIndependant);
        compensation.setIsEmployeur(key.isEmployeur);
        compensation.setIsPorteEnCompte(key.isPorteEnCompte);
        compensation.setIdDroit(key.idExtra1);

        compensation.add(transaction);

        memoryLog(getSession().getLabel("COMPENSATION_AJOUTEE"), FWMessage.INFORMATION,
                compensation.getIdCompensation());

        return compensation;
    }

    private IAFAffiliation getAffiliation(final BSession session, final Key key) throws Exception {
        final IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
        employeur.setAffiliationId(key.idAffilie);
        employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
        employeur.retrieve(getTransaction());

        return employeur;
    }

    /**
     * Nous considérons l'affilié comme radié, même si la date de fin est dans le futur. Si isRadie = true, pas de
     * compensation sur facture future.
     * 
     * @param employeur L'employeur.
     * @return Vrai si il est en phase de radiation ou déjà radié.
     */
    private boolean isRadie(IAFAffiliation employeur) {
        boolean isRadie = false;

        if (employeur != null && !employeur.isNew() && !JadeStringUtil.isBlankOrZero(employeur.getDateFin())) {
            isRadie = true;
        }

        return isRadie;
    }

    private SortedMap<Key, MontantsPorterEnCompte> creationOfKeys(final BSession session, final BTransaction transaction)
            throws Exception {

        /**
         * Map contenant la clé de regroupement avec une valeur contenant deux montants ; une pour le montant totale qui
         * va être toujours additionner par le montant de la répartition et un montantPorterEncompte qui va être sommer
         * uniquement par les répartitions qui ont une situation professionnelle avec la case "porté en compte" actif
         */
        final SortedMap<Key, MontantsPorterEnCompte> mapToReturn = new TreeMap<Key, MontantsPorterEnCompte>();

        final APRepartitionPaiementsJointEmployeurManager manager = new APRepartitionPaiementsJointEmployeurManager();
        manager.setSession(session);
        manager.setForIdLot(forIdLot);
        manager.setParentOnly(true);
        manager.setForTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
        manager.setOrderBy(APRepartitionPaiements.FIELDNAME_IDTIERS);

        final BStatement statement = manager.cursorOpen(transaction);
        APRepartitionPaiementsJointEmployeur repartition;
        while ((repartition = (APRepartitionPaiementsJointEmployeur) manager.cursorReadNext(statement)) != null) {

            final APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(getSession());
            droit.setIdDroit(repartition.getIdDroit());
            droit.retrieve();

            final String idTiersAssure = droit.loadDemande().loadTiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            /**
             * Si des répartitions porte sur un employeur non affilié, on ne le prends pas en compte
             */
            if (JadeStringUtil.isIntegerEmpty(repartition.getIdAffilie())
                    && !idTiersAssure.equals(repartition.getIdTiers())) {
                continue;
            }

            final boolean isEmployeur = repartition.getTypePaiement().equals(
                    IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);
            final boolean isIndependant = repartition.getIsIndependant().booleanValue();
            final String idAssureDeBase = droit.loadDemande().getIdTiers();

            Boolean isPorteEnCompte = isSituationProfPorteEnCompte(repartition.getIdSituationProfessionnelle());

            final Key key;
            // Cas ou le bénéficiaire est l'assuré de base
            if(repartition.getGenreService().equals(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE)
                || repartition.getGenreService().equals(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT)){
                if (idAssureDeBase.equals(repartition.getIdTiers())) {
                    key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(),  repartition.getIdDroit(),
                            repartition.getIdParticularite(), repartition.getGenrePrestationPrestation(), false, false, "",
                            false);
                }
                // Cas ou le bénéficiaire est un affilié
                else if (!JadeStringUtil.isIntegerEmpty(repartition.getIdAffilie())) {
                    key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(), repartition.getIdDroit(),
                            repartition.getIdParticularite(), repartition.getGenrePrestationPrestation(), false, false, "",
                            isPorteEnCompte);
                } else {
                    key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(), repartition.getIdDroit(),
                            repartition.getIdParticularite(), repartition.getGenrePrestationPrestation(), false, false, "",
                            false);
                }
            }else{
                if (idAssureDeBase.equals(repartition.getIdTiers())) {
                    key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(), "0",
                            repartition.getIdParticularite(), repartition.getGenrePrestationPrestation(), false, false, "",
                            false);
                }
                // Cas ou le bénéficiaire est un affilié
                else if (!JadeStringUtil.isIntegerEmpty(repartition.getIdAffilie())) {
                    key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(), "0",
                            repartition.getIdParticularite(), repartition.getGenrePrestationPrestation(), false, false, "",
                            isPorteEnCompte);
                } else {
                    key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(), "0",
                            repartition.getIdParticularite(), repartition.getGenrePrestationPrestation(), false, false, "",
                            false);
                }

            }



            // key = new Key(repartition.getIdTiers(), repartition.getIdAffilie(), "0",
            // repartition.getIdParticularite(),
            // repartition.getGenrePrestationPrestation(), isEmployeur, isIndependant, "", false);
            key.idDomaineAdressePaiement = repartition.getIdDomaineAdressePaiement();
            key.idTiersAdressePaiement = repartition.getIdTiersAdressePaiement();

            final FWCurrency montantRepartition = new FWCurrency(repartition.getMontantNet());
            montantRepartition.sub(getMontantVentile(session, repartition.getIdRepartitionBeneficiairePaiement()));

            MontantsPorterEnCompte montants = new MontantsPorterEnCompte();
            if (mapToReturn.containsKey(key)) {
                montants = mapToReturn.get(key);
            }

            final FWCurrency total = new FWCurrency(montants.getMontantTotal().toString());
            total.add(new FWCurrency(montantRepartition.toString()));

            final FWCurrency porterEnCompte = new FWCurrency(montants.getMontantPorterEnCompte().toString());
            if (repartition.loadSituationProfessionnelle() != null
                    && repartition.loadSituationProfessionnelle().getIsPorteEnCompte()) {
                porterEnCompte.add(new FWCurrency(montantRepartition.toString()));
            }

            montants.setMontantTotal(total);
            montants.setMontantPorterEnCompte(porterEnCompte);

            mapToReturn.put(key, montants);
        }

        manager.cursorClose(statement);
        manager.clear();

        return mapToReturn;
    }

    private void removeExistingCompensations(final BSession session, final BTransaction transaction) throws Exception {

        final APCompensationManager compensationManager = new APCompensationManager();
        compensationManager.setSession(session);
        compensationManager.setForIdLot(forIdLot);
        compensationManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < compensationManager.size(); i++) {
            final APCompensation compensation = (APCompensation) compensationManager.getEntity(i);
            compensation.delete(transaction);
        }
    }

    private boolean checkBusinessRules() throws Exception {
        boolean isOk = true;

        final APRepartitionJointPrestationManager repartitionJointPrestationManager = new APRepartitionJointPrestationManager();
        repartitionJointPrestationManager.setSession(getSession());
        repartitionJointPrestationManager.setForIdLot(getForIdLot());
        repartitionJointPrestationManager.find(BManager.SIZE_NOLIMIT);

        APRepartitionJointPrestation repartitionJointPrestation;
        for (int i = 0; i < repartitionJointPrestationManager.size(); i++) {
            repartitionJointPrestation = (APRepartitionJointPrestation) repartitionJointPrestationManager.getEntity(i);

            final APDroitLAPG droitLAPG = new APDroitLAPG();
            droitLAPG.setSession(getSession());
            droitLAPG.setIdDroit(repartitionJointPrestation.getIdDroit());
            droitLAPG.retrieve();

            final PRTiersWrapper tw = droitLAPG.loadDemande().loadTiers();

            /**
             * Vérification des règles métiers
             */
            isOk &= checkTiersActif(repartitionJointPrestation, tw);
            isOk &= checkAdressePaiementSiVentilation(repartitionJointPrestation, tw);
            isOk &= checkAdresseCourrier(repartitionJointPrestation,droitLAPG, tw);
            isOk &= checkEtatCivil(repartitionJointPrestation, droitLAPG);
            isOk &= checkMontants(repartitionJointPrestation, droitLAPG);
            isOk &= checkRepartition(repartitionJointPrestation, droitLAPG);
        }

        return isOk;
    }

    private boolean checkRepartition(APRepartitionJointPrestation repartition, APDroitLAPG droitLAPG) throws Exception {
        boolean isOk = true;

        APSituationProfessionnelle situationProf = repartition.loadSituationProfessionnelle();

        if (situationProf != null && !situationProf.isNew() && situationProf.getIsPorteEnCompte()
                && !JadeStringUtil.isBlankOrZero(repartition.getIdAffilie())) {
            /**
             * Nous recherchons les liens d'affiliation de type personnel déclarer par avec une date de validité encore
             * active, pour les situations professionnelles avec la case porté en compte
             */
            final AFLienAffiliationManager manager = new AFLienAffiliationManager();
            manager.setSession(getSession());
            manager.setForTypeLien(CodeSystem.TYPE_LIEN_PERSONNEL_DECLARE);
            manager.setForAffiliationId(repartition.getIdAffilie());
            manager.setForDate(droitLAPG.getDateDebutDroit());
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.getSize() > 0) {
                isOk = false;
                final String noAffilie = ((AFLienAffiliation) manager.get(0)).getAffiliation().getAffilieNumero();
                final String messagePorteEnCompte = getSession().getLabel(
                        "PORTER_EN_COMPTE_PERSONNEL_DECLARE_PAR_ERREUR");
                memoryLog(messagePorteEnCompte, FWMessage.ERREUR, noAffilie, repartition.getId(),
                        droitLAPG.getNoDroit());
            }
        }
        return isOk;
    }

    private boolean checkMontants(APRepartitionJointPrestation repartitionJointPrestation, APDroitLAPG droitLAPG)
            throws Exception {
        boolean isOk = true;

        /**
         * Repris cette condition, même si les == ne sont pas top pour les float...
         */
        if (Float.parseFloat(repartitionJointPrestation.getMontantBrut()) == 0
                && Float.parseFloat(repartitionJointPrestation.getMontantVentile()) == 0) {

            final String noAVS = droitLAPG.loadDemande().loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            final String idPrestationAPG = repartitionJointPrestation.getIdPrestationApg();
            final String messageMontant = getSession().getLabel("MONTANT_BRUT_VAUT_ZERO");

            memoryLog(messageMontant, FWMessage.ERREUR, idPrestationAPG, noAVS);

            isOk = false;
        }

        return isOk;
    }

    private boolean checkEtatCivil(APRepartitionJointPrestation repartitionJointPrestation, APDroitLAPG droitLAPG)
            throws Exception {
        boolean isOk = true;

        final PRTiersWrapper personneAVS = PRTiersHelper.getPersonneAVS(getSession(), droitLAPG.loadDemande()
                .getIdTiers());

        if (personneAVS != null) {
            if (JadeStringUtil.isIntegerEmpty(personneAVS.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL))) {

                final String noAVS = droitLAPG.loadDemande().loadTiers()
                        .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                final String nom = repartitionJointPrestation.getNom();
                final String idPrestationAPG = repartitionJointPrestation.getIdPrestationApg();
                final String messageEtatCivil = getSession().getLabel("ETAT_CIVIL_ABSENT");

                memoryLog(messageEtatCivil, FWMessage.ERREUR, nom, noAVS, idPrestationAPG);

                isOk = false;
            }
        } else {
            final String message = "Tiers non trouvé pour idTiers n° " + droitLAPG.loadDemande().getIdTiers();
            memoryLog(message, FWMessage.ERREUR);
            isOk = false;
        }

        return isOk;
    }

    private boolean checkAdresseCourrier(APRepartitionJointPrestation repartitionJointPrestation, APDroitLAPG droitLAPG, PRTiersWrapper tw)
            throws Exception {
        boolean isOk = true;

        // adresse de courrier absente

        boolean isProcheAidant = false;
        // adresse de courrier absente
        switch(droitLAPG.getGenreService()){
            case IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT:
                isProcheAidant = true;
            case IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE :
                String domaine = APProperties.DOMAINE_ADRESSE_APG_PATERNITE.getValue();
                if(isProcheAidant){
                    domaine = APProperties.DOMAINE_ADRESSE_APG_PROCHE_AIDANT.getValue();
                }
                if (JadeStringUtil.isEmpty(PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                        repartitionJointPrestation.getIdTiers(), repartitionJointPrestation.getIdAffilie(),
                        domaine))) {

                    String nss = "";
                    if (tw != null) {
                        nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    }
                    final String nom = repartitionJointPrestation.getNom();
                    final String idPrestationAPG = repartitionJointPrestation.getIdPrestationApg();
                    final String messageAdresseAbsente = getSession().getLabel("ADRESSE_COURRIER_ABSENTE");

                    memoryLog(messageAdresseAbsente, FWMessage.ERREUR, nom, nss, idPrestationAPG);
                    isOk = false;
                }
                break;
            default:
                if (JadeStringUtil.isEmpty(PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                        repartitionJointPrestation.getIdTiers(), repartitionJointPrestation.getIdAffilie(),
                        APApplication.CS_DOMAINE_ADRESSE_APG))) {

                    String nss = "";
                    if (tw != null) {
                        nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    }
                    final String nom = repartitionJointPrestation.getNom();
                    final String idPrestationAPG = repartitionJointPrestation.getIdPrestationApg();
                    final String messageAdresseAbsente = getSession().getLabel("ADRESSE_COURRIER_ABSENTE");

                    memoryLog(messageAdresseAbsente, FWMessage.ERREUR, nom, nss, idPrestationAPG);
                    isOk = false;
                }
        }




        return isOk;
    }

    private boolean checkAdressePaiementSiVentilation(APRepartitionJointPrestation repartitionJointPrestation,
            PRTiersWrapper tw) throws Exception {
        boolean isOk = true;

        final FWCurrency montantTotalVentilations = giveMontantVentilationRepartitionParent(repartitionJointPrestation);

        if (repartitionJointPrestation.loadAdressePaiement(null) == null
                && !montantTotalVentilations.equals(new FWCurrency(repartitionJointPrestation.getMontantNet()))) {

            String nss = "";
            if (tw != null) {
                nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            }
            final String nom = repartitionJointPrestation.getNom();
            final String idPrestationApg = repartitionJointPrestation.getIdPrestationApg();
            final String msgAdressePaiementAbsente = getSession().getLabel("ADRESSE_PAIEMENT_ABSENTE");

            memoryLog(msgAdressePaiementAbsente, FWMessage.ERREUR, nom, nss, idPrestationApg);
            isOk = false;
        }

        return isOk;
    }

    private boolean checkTiersActif(APRepartitionJointPrestation repartitionJointPrestation, PRTiersWrapper tw) {
        boolean isOk = true;

        if (!isTiersRequerantActif(tw)) {
            final String msgTiersInactif = getSession().getLabel("TIERS_INACTIF");

            String nss = "";
            if (tw != null) {
                nss = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            }

            final String nom = repartitionJointPrestation.getNom();
            final String idPrestationApg = repartitionJointPrestation.getIdPrestationApg();
            final String messageInfo = msgTiersInactif + " " + nss + " " + nom;

            memoryLog(messageInfo, FWMessage.ERREUR, nom, nss, idPrestationApg);
            isOk = false;
        }

        return isOk;
    }

    private FWCurrency giveMontantVentilationRepartitionParent(APRepartitionJointPrestation repartitionJointPrestation)
            throws Exception {
        final FWCurrency montantTotalVentilations = new FWCurrency();

        final APRepartitionJointPrestationManager mgr = new APRepartitionJointPrestationManager();
        mgr.setSession(getSession());
        mgr.setForIdParent(repartitionJointPrestation.getIdRepartitionBeneficiairePaiement());
        mgr.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.getSize(); i++) {
            final APRepartitionJointPrestation entity = (APRepartitionJointPrestation) mgr.get(i);

            montantTotalVentilations.add(entity.getMontantVentile());
        }

        return montantTotalVentilations;
    }

    @Override
    protected void _validate() throws Exception {
        final APLot lot = new APLot();
        lot.setSession(getSession());
        lot.setIdLot(forIdLot);
        lot.retrieve();

        if (JadeStringUtil.isIntegerEmpty(lot.getIdLot())) {
            _addError(getTransaction(), getSession().getLabel("NO_LOT_REQUIS"));
        }

        if (lot.getEtat().equals(IAPLot.CS_VALIDE)) {
            _addError(getTransaction(), getSession().getLabel("LOT_DEJA_VALIDE"));
        }
    }

    @Override
    protected void _executeCleanUp() {
        // Nothing to do
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    @Override
    protected String getEMailObject() {
        final APLot lot = new APLot();
        lot.setSession(getSession());
        lot.setIdLot(forIdLot);

        try {
            lot.retrieve();
        } catch (Exception e) {
            log(Level.INFO, e, e.getMessage());
        }

        return labelProcess() + " du lot: " + lot.getDescription() + ", status: "
                + (getMemoryLog().hasErrors() ? "ECHEC" : "SUCCES");
    }

    @Override
    public boolean isModulePorterEnCompte() {
        /**
         * Besoin spécifique de la FER-CIAM pour le porté en compte sur la situation professionnelle du droit APG
         * S161012_001
         */
        return true;
    }

    /**
     * 
     * Retourne la somme des montants ventilés d'une répartition à partir du parent.
     * 
     * @param session
     * @param idParent
     * @return
     * @throws Exception
     */
    private FWCurrency getMontantVentile(BSession session, String idParent) throws Exception {

        final FWCurrency result = new FWCurrency();

        final APRepartitionPaiementsJointEmployeurManager mgr = new APRepartitionPaiementsJointEmployeurManager();
        mgr.setForIdLot(forIdLot);
        mgr.setForIdParent(idParent);
        mgr.setParentOnly(false);
        mgr.setSession(session);
        mgr.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.size(); i++) {
            APRepartitionPaiementsJointEmployeur entity = (APRepartitionPaiementsJointEmployeur) mgr.getEntity(i);
            result.add(new FWCurrency(entity.getMontantVentile()));
        }

        return result;
    }

    protected boolean isTiersRequerantActif(PRTiersWrapper tiers) {
        if (tiers == null) {
            return false;
        }
        return !"true".equalsIgnoreCase(tiers.getProperty(PRTiersWrapper.PROPERTY_INACTIF));
    }

    private void log(final Level level, final Throwable throwable, final String message) {
        if (Level.INFO.equals(level)) {
            JadeLogger.info(throwable, message);
        } else if (Level.WARNING.equals(level)) {
            JadeLogger.warn(throwable, message);
        } else if (Level.SEVERE.equals(level)) {
            JadeLogger.error(throwable, message);
        }

        logger.log(level, message, throwable);
    }

    /**
     * Recherche la situation professionnelle avec son ID et récupère son champ isPorteEnCompte
     * 
     * @param idSituationProfessionnelle
     *            Retourne true si la situation professionnelle passée en paramètre est portée en compte
     * @throws Exception
     */
    private boolean isSituationProfPorteEnCompte(String idSituationProfessionnelle) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(idSituationProfessionnelle)) {
            APSituationProfessionnelle situationPro = new APSituationProfessionnelle();
            situationPro.setId(idSituationProfessionnelle);
            situationPro.setSession(getSession());
            situationPro.retrieve(getTransaction());
            if (!situationPro.isNew()) {
                return situationPro.getIsPorteEnCompte();
            }
        }
        return false;
    }
}
