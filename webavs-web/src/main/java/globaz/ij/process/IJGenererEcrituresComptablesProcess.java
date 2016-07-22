/*
 * Créé le 29 août 05
 */
package globaz.ij.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJFactureACompenser;
import globaz.ij.db.lots.IJFactureACompenserManager;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJCotisation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartJointCotJointPrestJointEmployeur;
import globaz.ij.db.prestations.IJRepartJointCotJointPrestJointEmployeurManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Process effectuant les écritures comptable. Il procède par étapes successives :
 * 
 * <dl>
 * <dt>Etape 1</dt>
 * <dd>instanciation du processus de compta</dd>
 * 
 * <dt>Etape 2</dt>
 * <dd>Initialisation des rubriques une fois pour tout le process. On a les numéros de rubriques mais on a besoin de
 * leur id pour générer les écritures</dd>
 * 
 * <dt>Etape 3</dt>
 * <dd>Regroupement des répartitions (méthode {@link #getMapRepartitions() getMapRepartitions()}). On obtient une map
 * contenant en clef une instance de {@link globaz.ij.process.Key Key} et contenant des listes de répartitions.
 * Concrètement, les répartitions de paiement du lot que l'on est en train de traiter sont regroupées par idTiers(du
 * bénéficiaire), idAffilie(du bénéficiaire), idTiersEmployeur non affilié. Dans le cas d'un bénéficiaire de type
 * employeur non affilié, l'id tiers de la clé est remplacé par l'id tiers de l'assuré de base; car c'est sur le compte
 * annexe de l'assuré que doivent s'effectuer les écritures comptables, avec un ordre de versement sur l'adresse de
 * l'employeur non affilié.
 * 
 * Si un employeur non affilié à des dettes à compenser, elles ne seront pas effectuées car l'idTiers de cet employeur
 * est remplacé par celui de l'assuré. Cela dit, un non affilié ne devrait jamais avoir de dettes à compenser.</dd>
 * 
 * <dt>Etape 4</dt>
 * <dd>Cette map de listes de répartitions est ensuite parcourue pour ajouter a chaque répartition la liste de
 * ventilations lui correspondant {@link #createVentilations(Map) createVentilations(Map)}</dd>
 * 
 * <dt>Etape 5</dt>
 * <dd>Création d'une autre map ayant les mêmes clefs que la map des répartitions et en valeur des listes de
 * compensations ( {@link #createCompensations(Map) createCompensations(Map)}). On a donc à ce moment une map contenant
 * des listes de répartitions avec leurs ventilations regroupées par idTiers, idAffilie, etc. et une map contenant les
 * compensations correspondantes.</dd>
 * 
 * <dt>Etape 6</dt>
 * <dd>génération des écritures pour chaque regroupement de répartitions. cette étape est elle même composée de
 * plusieurs étapes
 * 
 * <ul>
 * <li>Regroupement pour chaque groupement de répartition des cotisations par année de cotisation, et des montants
 * bruts. ces regroupements sont faits en différenciant les prestations normales des prestations de restitutions qui
 * doivent être écrits différemment en compta</li>
 * <li>Versement des ventilations de la répartition qu'on est en train de traiter</li>
 * <li>Ecriture des montants bruts et des restitutions sur les rubriques concernées</li>
 * <li>Ecriture des cotisations pour la répartition en cours</li>
 * <li>Ecriture des compensations pour la répartition en cours</li>
 * <li>Versement effectif de ce qui reste a verser après cotisations, ventilations, compensations</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @author dvh
 */
public class IJGenererEcrituresComptablesProcess extends BProcess {

    /**
     * Une classe représentant une compensation
     */
    private class Compensation {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        public String idFacture;
        public boolean isCompensationPourRoleAffiliePersonnel = false;
        public String montant;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public Compensation(String montant, String idFacture, boolean isCompensationPourRoleAffiliePersonnel) {
            this.montant = montant;
            this.idFacture = idFacture;
            this.isCompensationPourRoleAffiliePersonnel = isCompensationPourRoleAffiliePersonnel;
        }
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Inner class
    class CompensationInterne {
        public String idCompteAnnexe = null;
        public String idSectionNormale = null;
        public String idSectionRestitution = null;
        public FWCurrency montantTotPrestations = null;
        public FWCurrency montantTotRestitutions = null;
    }

    /**
     * classe représentant une répartition de paiement.
     */
    private class Repartition {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        public String anneeCotisation = "";
        public String cotisationAC = "";

        public String cotisationAVS = "";
        public String cotisationLFA = "";
        public String fraisAdministration = "";
        public String genrePrestation = "";
        public String idAdressePaiement = "";
        public String idCompensation = "";
        public String idDepartement = "";
        public String idRepartitionPaiement = "";
        public String impotSource = "";
        public boolean isEmployeur = false;
        public boolean isIndependant = false;
        public boolean isRestitution = false;

        public String montant = "";

        public APIRubrique rubriqueConcernee = null;
        public String section = "";
        public List ventilations = new ArrayList();
    }

    /**
     * classe représentant une ventilation
     */
    private class Ventilation {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        public String idAdressePaiement = "";
        public String montant = "";
        public String referenceInterne = "";

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        public Ventilation(String montant, String idAdressePaiement, String referenceInterne) {
            this.montant = montant;
            this.idAdressePaiement = idAdressePaiement;
            this.referenceInterne = referenceInterne;
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 6328283098307831355L;
    private APIRubrique ASSURE_OU_INDEPENDANT = null;
    private APIRubrique COMPENSATION = null;
    private APIRubrique COT_AC = null;
    private APIRubrique COT_AVS = null;
    private APIRubrique COT_LFA = null;
    private String dateComptable = "";

    private String dateSurDocument = "";
    private APIRubrique EMPLOYEUR = null;
    private APIRubrique FONDS_DE_COMPENSATION = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private APIRubrique FRAIS_ADMINISTRATION = null;

    private String idLot = "";

    private APIRubrique IMPOT_SOURCE = null;

    private APIRubrique PRESTATIONS_A_RESTITUER = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererEcrituresComptablesProcess.
     */
    public IJGenererEcrituresComptablesProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererEcrituresComptablesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJGenererEcrituresComptablesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJGenererEcrituresComptablesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererEcrituresComptablesProcess(BSession session) {
        super(session);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {

        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        boolean noErrorBeforeClose = false;

        try {

            IJLot lot = new IJLot();
            lot.setSession(getSession());
            lot.setIdLot(idLot);
            lot.retrieve(getTransaction());

            /*
             * D0113
             * En 1er lieu on contrôle qu'il y ait un montant (positif ou négatif) avant de lancer le process de compta
             * Ceci dans le cas ou le lot ne contiendrais que des prestations à 0.-
             */
            IJRepartJointCotJointPrestJointEmployeurManager manager = createManagerRechercheRepartition();
            manager.find(BManager.SIZE_NOLIMIT);

            for (Object object : manager.getContainer()) {
                IJRepartJointCotJointPrestJointEmployeur repartition = (IJRepartJointCotJointPrestJointEmployeur) object;
                if (!new FWCurrency(repartition.getMontantBrut()).isZero()) {
                    break;
                }
                return true;
            }

            // instanciation du processus de compta
            BISession sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");
            APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) sessionOsiris
                    .getAPIFor(APIGestionComptabiliteExterne.class);
            compta.setDateValeur(dateComptable);
            compta.setEMailAddress(getEMailAddress());

            comptaMemoryLog.setSession((BSession) sessionOsiris);
            compta.setMessageLog(comptaMemoryLog);

            compta.setSendCompletionMail(false);
            compta.setTransaction(getTransaction());
            compta.setProcess(this);

            if (lot != null) {
                compta.setLibelle(lot.getDescription());
            } else {
                compta.setLibelle("IJ");
            }

            APIJournal journal = compta.createJournal();

            // initialisation des ids des rubrique une fois pour tout le
            // process.
            initIdsRubriques(sessionOsiris);

            // Récupération du regroupement des répartitions de paiement
            Map repartitionsClassees = getMapRepartitions();

            // Mise ajout des ventilation pour ces répartitions de paiement
            createVentilations(repartitionsClassees);

            // Creation d'une map de listes de compensations dont les clefs sont
            // les mêmes que celles des répartition.
            // On aura ainsi pour chaque regroupement de repartitions les
            // compensations correspondantes.
            Map compensations = createCompensations(repartitionsClassees);

            // pour chaque element de la map repartitionsclassee, on va faire
            // les ecriture comptables
            Set keysRepartitionsClassees = repartitionsClassees.keySet();
            Iterator repartitionsClassesIterator = keysRepartitionsClassees.iterator();

            Map mapCompensationsInterne = new HashMap();

            while (repartitionsClassesIterator.hasNext()) {
                Key key = (Key) repartitionsClassesIterator.next();
                List repartitions = (List) repartitionsClassees.get(key);
                List compensationsPourCetId = (List) compensations.get(key);

                FWCurrency montantBrutTotal = new FWCurrency(0);
                FWCurrency ventilationTotale = new FWCurrency(0);
                FWCurrency compensationTotale = new FWCurrency(0);

                Iterator repartitionsIterator = repartitions.iterator();

                // recherche du montant total des ventilations et du montant
                // brut total
                while (repartitionsIterator.hasNext()) {
                    Repartition repartition = (Repartition) repartitionsIterator.next();
                    montantBrutTotal.add(new FWCurrency(repartition.montant).toString());
                    Iterator ventilationIterator = repartition.ventilations.iterator();

                    while (ventilationIterator.hasNext()) {
                        Ventilation ventilation = (Ventilation) ventilationIterator.next();
                        ventilationTotale.add(new FWCurrency(ventilation.montant.toString()));
                    }

                }

                Iterator compensationsPourCetIdIterator = compensationsPourCetId.iterator();

                // recherche du montant total des compensations
                while (compensationsPourCetIdIterator.hasNext()) {
                    Compensation compensation = (Compensation) compensationsPourCetIdIterator.next();
                    compensationTotale.add(new FWCurrency(compensation.montant));
                }

                CompensationInterne ci = faisEcritures(repartitions, compensationsPourCetId, montantBrutTotal,
                        ventilationTotale, compensationTotale, key, compta, sessionOsiris);

                if (mapCompensationsInterne.containsKey(key)) {
                    CompensationInterne totCI = (CompensationInterne) mapCompensationsInterne.get(key);
                    totCI.montantTotPrestations.add(ci.montantTotPrestations);
                    totCI.montantTotRestitutions.add(ci.montantTotRestitutions);

                    // Si idExtra1 ou 2 n'est pas vide, cela signifie qu'il
                    // s'agit d'un employeur non affilié.
                    // Dans ce cas, il ne faut pas prendre la section du non
                    // affilié pour pour compenser la restitution,
                    // mais celle de l'assuré / affilié.
                    if (JadeStringUtil.isBlankOrZero(totCI.idSectionNormale)
                            && JadeStringUtil.isBlankOrZero(key.idExtra1) && JadeStringUtil.isBlankOrZero(key.idExtra2)) {
                        totCI.idSectionNormale = ci.idSectionNormale;
                    }

                    if (JadeStringUtil.isBlankOrZero(totCI.idSectionRestitution)
                            && JadeStringUtil.isBlankOrZero(key.idExtra1) && JadeStringUtil.isBlankOrZero(key.idExtra2)) {
                        totCI.idSectionRestitution = ci.idSectionRestitution;
                    }

                    mapCompensationsInterne.put(key, totCI);
                } else {
                    mapCompensationsInterne.put(key, ci);
                }
            }

            doCompensationsInterne(compta, mapCompensationsInterne);

            FWMemoryLog beforeCloseComptaMemoryLog = new FWMemoryLog();
            // si pas d'erreurs avant le close, on sauvegarde les messages du
            // comptaMemoryLog
            // pour les restaurer si une erreure survient durant le close
            if (!comptaMemoryLog.hasErrors()) {
                noErrorBeforeClose = true;
                beforeCloseComptaMemoryLog.logMessage(comptaMemoryLog);

                // on memorise l'id du journal en CA pour pouvoir rediriger sur
                // celui-ci depuis les IJ
                lot.setIdJournalCA(journal.getIdJournal());
                lot.update(getTransaction());
            }

            compta.comptabiliser();

            // si pas d'erreurs avant le close et en erreur après le close, on
            // restaure l'ancien
            // memory log pour masquer l'exception.
            // Elle sera directement traitee dans la compta.
            if (noErrorBeforeClose && comptaMemoryLog.hasErrors()) {
                comptaMemoryLog = beforeCloseComptaMemoryLog;
            }

        } catch (Exception e) {
            // si l'exception survient durant le close -> noErrorBeforeClose ==
            // true, l'exception n'est pas remontee
            // Elle sera directement traitee dans la compta.
            if (!noErrorBeforeClose) {

                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
                return false;
            }
        } finally {
            if (comptaMemoryLog != null && comptaMemoryLog.size() > 0) {

                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: START LOG OSIRIS :::::::::::::::::::::::::::::");

                for (int i = 0; i < comptaMemoryLog.size(); i++) {

                    getMemoryLog()
                            .logMessage(null, comptaMemoryLog.getMessage(i).getComplement(),
                                    comptaMemoryLog.getMessage(i).getTypeMessage(),
                                    comptaMemoryLog.getMessage(i).getIdSource());

                }
                getMemoryLog().logMessage("", FWMessage.INFORMATION,
                        ":::::::::::: END LOG OSIRIS :::::::::::::::::::::::::::::");
            }
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idLot)) {
            _addError(getSession().getLabel("NO_LOT_REQUIS"));
        }

        super._validate();
    }

    private Map createCompensations(Map repartitionsClassees) throws Exception {
        Map compensations = new HashMap();
        Set keysRepartitionClassees = repartitionsClassees.keySet();
        Iterator repartitionsClassesIterator = keysRepartitionClassees.iterator();

        while (repartitionsClassesIterator.hasNext()) {
            List compensationsPourCetteRepartition = new ArrayList();

            Key key = (Key) repartitionsClassesIterator.next();

            List listDesCompensationsTraitees = new ArrayList();

            boolean isCompensationPourRoleAffiliePersonnel = false;
            // On parcours la liste de toute les répartitions pour contrôler si
            // au moins une de ces répartition
            // est versée à l'indépendant.
            // Si c'est le cas, toutes les écritures comptable devront se faire
            // sur le role : AFFILIE PERSONNEL

            Iterator iterRepartition = ((List) repartitionsClassees.get(key)).iterator();
            while (iterRepartition.hasNext()) {
                Repartition element = (Repartition) iterRepartition.next();

                if (element.isIndependant && element.isEmployeur) {
                    isCompensationPourRoleAffiliePersonnel = true;
                    break;
                }

                String idCompensation = element.idCompensation;

                // Les compensations sont regroupées et cumulées par
                // idTiers/idAffilie/idParticularité
                // Les écritures sont regroupées par idTiers/idAffilie
                // Pour une clé donnée (idTiers/idAffilié), il est donc possible
                // de d'obtenir
                // plusieurs Repartition, ayant le même idCompensation.
                // Il ne faut donc pas traiter plusieurs fois cette même
                // compensation !!!.
                if (listDesCompensationsTraitees.contains(idCompensation)) {
                    // Passe à l'itération suivante
                    continue;
                } else {
                    listDesCompensationsTraitees.add(idCompensation);
                }

                // l'idCompensation peut être vide s'il s'agit d'une répartition
                // de paiement de frais de garde. Dans ce
                // cas, on ne fait rien
                if (!JadeStringUtil.isIntegerEmpty(idCompensation)) {
                    IJFactureACompenserManager factureACompenserManager = new IJFactureACompenserManager();
                    factureACompenserManager.setSession(getSession());
                    factureACompenserManager.setForIdCompensation(idCompensation);

                    factureACompenserManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < factureACompenserManager.size(); i++) {
                        IJFactureACompenser factureACompenser = (IJFactureACompenser) factureACompenserManager
                                .getEntity(i);
                        if (factureACompenser.getIsCompense().booleanValue()) {

                            compensationsPourCetteRepartition.add(new Compensation(factureACompenser.getMontant(),
                                    factureACompenser.getIdFactureCompta(), isCompensationPourRoleAffiliePersonnel));
                        }
                    }
                }
            }

            if (compensations.containsKey(key) && !compensationsPourCetteRepartition.isEmpty()) {
                List listFactureACompenser = (List) compensations.get(key);
                listFactureACompenser.addAll(compensationsPourCetteRepartition);
                compensations.put(key, listFactureACompenser);
            } else {
                compensations.put(key, compensationsPourCetteRepartition);
            }

        }

        return compensations;
    }

    /**
     * Ajoute les ventilations pour chaque repartition de paiement de chaque liste de la map donnée en parametre
     * 
     * @param repartitionsClassees
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void createVentilations(Map repartitionsClassees) throws Exception {
        Set repartitionsClassesKeys = repartitionsClassees.keySet();
        Iterator repartitionsClasseesIterator = repartitionsClassesKeys.iterator();

        while (repartitionsClasseesIterator.hasNext()) {
            List repartitions = (List) repartitionsClassees.get(repartitionsClasseesIterator.next());
            Iterator repartitionsIterator = repartitions.iterator();

            while (repartitionsIterator.hasNext()) {
                Repartition repartition = (Repartition) repartitionsIterator.next();
                String idRepartition = repartition.idRepartitionPaiement;
                IJRepartitionPaiementsManager repartitionPaiementsManager = new IJRepartitionPaiementsManager();
                repartitionPaiementsManager.setSession(getSession());
                repartitionPaiementsManager.setForIdParent(idRepartition);
                repartitionPaiementsManager.find(getTransaction());

                for (int i = 0; i < repartitionPaiementsManager.size(); i++) {
                    IJRepartitionPaiements repartitionPaiementFille = (IJRepartitionPaiements) repartitionPaiementsManager
                            .getEntity(i);

                    if (repartitionPaiementFille.loadAdressePaiement(getDateComptable()) == null) {
                        throw new Exception(getSession().getLabel("ADR_PMT_REAPRTITION_ERR") + " idRepap/idPrest : "
                                + repartitionPaiementFille.getIdRepartitionPaiement() + "/"
                                + repartitionPaiementFille.getIdPrestation());
                    }

                    repartition.ventilations
                            .add(new Ventilation(repartitionPaiementFille.getMontantVentile(), repartitionPaiementFille
                                    .loadAdressePaiement(getDateComptable()).getIdAvoirPaiementUnique(),
                                    repartitionPaiementFille.getReferenceInterne()));
                }
            }
        }
    }

    //
    // Compensation Interne dans CA
    // //////////////////////////////////////////////////////////////////////////////////////////
    private void doCompensationsInterne(APIGestionComptabiliteExterne compta, Map map) throws Exception {

        Set keys = map.keySet();
        for (Iterator iter = keys.iterator(); iter.hasNext();) {
            Key key = (Key) iter.next();

            CompensationInterne ci = (CompensationInterne) map.get(key);

            // S'il y a des restitutions ainsi que des prestations standard, on
            // va compenser en interne
            // ces écritures. Cela évite de faire le lettrage à la main.

            if (!ci.montantTotPrestations.isZero() && !ci.montantTotRestitutions.isZero()) {

                ci.montantTotRestitutions.abs();
                ci.montantTotPrestations.abs();

                FWCurrency plusPetitDesMontantAbs = null;
                // prendre le plus petit des montant en val. abs
                if (ci.montantTotPrestations.compareTo(ci.montantTotRestitutions) < 0) {
                    plusPetitDesMontantAbs = ci.montantTotPrestations;
                } else {
                    plusPetitDesMontantAbs = ci.montantTotRestitutions;
                }

                FWCurrency montantInverse = new FWCurrency(plusPetitDesMontantAbs.toString());
                montantInverse.negate();

                getMemoryLog().logMessage("Compensations internes à la CA >>>>>>>>", FWMessage.INFORMATION,
                        getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

                doEcriture(compta, plusPetitDesMontantAbs.toString(), COMPENSATION, ci.idCompteAnnexe,
                        ci.idSectionRestitution, null);

                doEcriture(compta, montantInverse.toString(), COMPENSATION, ci.idCompteAnnexe, ci.idSectionNormale,
                        null);
            }

            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        }
    }

    /**
     * écrit une écriture en compta. Ne fait rien si le montant est nul.
     * 
     * @param compta
     *            une instance de APIProcessComptabilisation
     * @param montantSigne
     *            Le montant a écrire, signé
     * @param rubrique
     *            l'id de la rubrique
     * @param idCompteAnnexe
     *            l'id du compta annexe
     * @param idSection
     *            l'id de la section
     * @param anneeCotisation
     *            l'année de la cotisation, null s'il ne s'agit pas d'une cotisation
     */
    private void doEcriture(APIGestionComptabiliteExterne compta, String montantSigne, APIRubrique rubrique,
            String idCompteAnnexe, String idSection, String anneeCotisation) {
        if (!JadeStringUtil.isDecimalEmpty(montantSigne)) {
            FWCurrency montant = new FWCurrency(montantSigne);
            boolean positif = true;

            if (montant.isNegative()) {
                montant.negate();
                positif = false;
            }

            APIEcriture ecriture = compta.createEcriture();
            ecriture.setIdCompteAnnexe(idCompteAnnexe);
            ecriture.setIdSection(idSection);
            ecriture.setDate(dateComptable);
            ecriture.setIdCompte(rubrique.getIdRubrique());
            ecriture.setMontant(montant.toString());

            getMemoryLog().logMessage(
                    java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ECRITURE"), new Object[] {
                            montantSigne.toString(), rubrique.getIdExterne() }), FWMessage.INFORMATION,
                    getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

            if (positif) {
                ecriture.setCodeDebitCredit(APIEcriture.CREDIT);
            } else {
                ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
            }

            if (anneeCotisation != null) {
                ecriture.setAnneeCotisation(anneeCotisation);
            }

            compta.addOperation(ecriture);
        }
    }

    /**
     * Effectue un ordre de versement, lance une Exception si le montant est négatif
     * 
     * @param compta
     *            DOCUMENT ME!
     * @param idCompteAnnexe
     *            DOCUMENT ME!
     * @param idSection
     *            DOCUMENT ME!
     * @param montant
     *            DOCUMENT ME!
     * @param idAdressePaiement
     *            DOCUMENT ME!
     * @param nssRequerant
     *            String, NSS du requérant principal. Cette valeur peut être null.
     * 
     * @throws Exception
     *             Si le montant est négatif
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    private void doOrdreVersement(APIGestionComptabiliteExterne compta, String idCompteAnnexe, String idSection,
            String montant, String idAdressePaiement, String nomPrenomRequerant, String referenceInterne)
            throws Exception {
        if (new FWCurrency(montant).isNegative()) {
            throw new IllegalArgumentException(getSession().getLabel("MONTANT_NEG_VERSEMENT_ERR"));
        }

        APIOperationOrdreVersement ordreVersement = compta.createOperationOrdreVersement();
        ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDate(dateComptable);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        ordreVersement.setMontant(montant);
        ordreVersement.setCodeISOMonnaieBonification(getSession().getCode(
                IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setCodeISOMonnaieDepot(getSession()
                .getCode(IPRConstantesExternes.OSIRIS_CS_CODE_ISO_MONNAIE_CHF));
        ordreVersement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

        getMemoryLog().logMessage(
                java.text.MessageFormat.format(getSession().getLabel("ECR_COM_ORDRE_VERSEMENT"), new Object[] {
                        montant, idAdressePaiement }), FWMessage.INFORMATION,
                getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

        ordreVersement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_IJAI);

        String motif = FWMessageFormat.format(getSession().getLabel("MOTIF_VERSEMENT_IJAI"), getDateSurDocument());

        if (nomPrenomRequerant != null) {
            motif += " (" + nomPrenomRequerant + ")";
        }

        if (referenceInterne != null && !JadeStringUtil.isEmpty(referenceInterne)) {
            motif += " " + getSession().getLabel("REFERENCE_INTERNE_VENTILLATION") + " : " + referenceInterne;
        }

        ordreVersement.setMotif(motif);
        compta.addOperation(ordreVersement);
    }

    /**
     * Ecris en compta les cotisations d'un certain type (AVS, AC, etc.)
     * 
     * @param compta
     *            une instance de APIProcessComptabilisation
     * @param rubrique
     *            l'id de la rubrique sur laquelle écrire
     * @param idCompteAnnexe
     *            l'id du compte annexe
     * @param idSection
     *            l'id de la section
     * @param cotisations
     *            une map contenant les montants des cotisations regroupés par année de cotisation
     * @return le montant total des écritures passées.
     */
    private FWCurrency ecrisCotisations(APIGestionComptabiliteExterne compta, APIRubrique rubrique,
            String idCompteAnnexe, String idSection, Map cotisations) {

        FWCurrency result = new FWCurrency(0);

        Set keys = cotisations.keySet();
        Iterator iterator = keys.iterator();

        while (iterator.hasNext()) {
            String annee = (String) iterator.next();
            FWCurrency montant = (FWCurrency) cotisations.get(annee);
            result.add(new FWCurrency(montant.toString()));
            doEcriture(compta, montant.toString(), rubrique, idCompteAnnexe, idSection, annee);
        }

        return result;
    }

    /**
     * Cette méthode va parcourir les répartitions pour regrouper les montants à inscrire par rubrique, en faisant la
     * différence entre des répartitions normales et des répartitions de restitution.
     * 
     * <p>
     * Elle fait ensuite les écritures dans la compta
     * </p>
     * 
     * @param repartitions
     *            Une List de répartitions concernant le même idTiers, idAffilie et idAdressePaiement
     * @param compensationsPourCetId
     *            Les compensations ratachées aux répartitions
     * @param montantBrutTotal
     *            Le montant brut total des répartitions
     * @param ventilationTotale
     *            Le montant total des ventilations pour les répartitions
     * @param compensationTotale
     *            le montant total des compensations pour les répartitions
     * @param key
     *            une clef contenant les infos sur le regroupement des répartitions
     * @param compta
     *            une instance de APIProcessComptabilisation (OSIRIS)
     * @param sessionOsiris
     *            Une session d'OSIRIS
     * 
     * @throws Exception
     */
    private CompensationInterne faisEcritures(List repartitions, List compensationsPourCetId,
            FWCurrency montantBrutTotal, FWCurrency ventilationTotale, FWCurrency compensationTotale, Key key,
            APIGestionComptabiliteExterne compta, BISession sessionOsiris) throws Exception {
        // classe interne à la méthode, elle ne sert qu'ici, pour regrouper les
        // montants par rubrique et section (apg
        // ou affiliation);
        class KeyRegroupementRubriqueConcernee {
            APIRubrique rubrique;
            String section;

            KeyRegroupementRubriqueConcernee(APIRubrique rubrique, String section) {
                this.rubrique = rubrique;
                this.section = section;
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Object#equals(java.lang.Object)
             */
            @Override
            public boolean equals(Object obj) {
                KeyRegroupementRubriqueConcernee key = (KeyRegroupementRubriqueConcernee) obj;

                return (rubrique == key.rubrique) && section.equals(key.section);
            }

            @Override
            public int hashCode() {
                return rubrique.getIdRubrique().hashCode() + section.hashCode();
            }
        }

        // creation de l'idExterneRole (qui est tout simplement le numéro
        // d'affilié s'il s'agit d'un affilié, le numéro
        // AVS sinon)
        String idExterneRole = null;

        if (JadeStringUtil.isIntegerEmpty(key.idAffilie)) {
            idExterneRole = PRTiersHelper.getTiersParId(getSession(), key.idTiers).getProperty(
                    PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            idExterneRole = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getTransaction(), key.idAffilie,
                    key.idTiers).getNumAffilie();
        }

        getMemoryLog().logMessage("------------------------", FWMessage.INFORMATION,
                getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));
        getMemoryLog().logMessage(getSession().getLabel("ECR_COM_TRAITEMENT_DE") + " " + idExterneRole,
                FWMessage.INFORMATION, getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS"));

        // récupération du compte annexe IJAI
        APICompteAnnexe compteAnnexeIJ = compta.getCompteAnnexeByRole(key.idTiers, IntRole.ROLE_IJAI, idExterneRole);

        if (compteAnnexeIJ == null) {
            throw new Exception("Impossible de créer le compte annexe, contrôlez les logs pour plus de détails.");
        }

        // on créé un numero de facture unique qui servira a creer la section
        String noFactureNormale = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(), IntRole.ROLE_IJAI,
                idExterneRole, APISection.ID_TYPE_SECTION_IJAI, String.valueOf(new JADate(dateComptable).getYear()),
                APISection.ID_CATEGORIE_SECTION_IJAI);

        // création de la section "normale" elle servira pour toutes les
        // écritures hormis les restitutions et les
        // compensations sur facture future
        APISection sectionNormale = compta.getSectionByIdExterne(compteAnnexeIJ.getIdCompteAnnexe(),
                APISection.ID_TYPE_SECTION_IJAI, noFactureNormale);

        // Plein plein plein de variables pour garder ce qu'on a besoin
        // les cotisations sont sommées dans une map avec comme clef l'année de
        // cotisation (nécessaire pour les
        // écritures en compta)
        Map totalAVS = new HashMap();
        Map totalAC = new HashMap();
        Map totalLFA = new HashMap();
        FWCurrency totalFA = new FWCurrency(0);
        FWCurrency totalIS = new FWCurrency(0);
        FWCurrency totalFondDeCompensation = new FWCurrency(0);
        Map totalAVSRestitution = new HashMap();
        Map totalACRestitution = new HashMap();
        Map totalLFARestitution = new HashMap();
        FWCurrency totalFARestitution = new FWCurrency(0);
        FWCurrency totalISRestitution = new FWCurrency(0);
        FWCurrency totalFondDeCompensationRestitution = new FWCurrency(0);
        Map ecrituresRubriquesConcernees = new HashMap();
        Map ecrituresRubriquesConcerneesRestitution = new HashMap();
        FWCurrency totalCotisations = new FWCurrency(0);
        String idAdressePaiementBeneficiaireDeBase = "";

        Iterator repartitionsIterator = repartitions.iterator();

        Repartition repartition = null;

        // pour savoir s'il sera nécessaire de créer la section de restitution
        boolean hasRestitution = false;

        // parcours de toutes les répartitions
        while (repartitionsIterator.hasNext()) {

            repartition = (Repartition) repartitionsIterator.next();

            // recup de l'idAdressePaiement
            idAdressePaiementBeneficiaireDeBase = repartition.idAdressePaiement;

            boolean isRestitution = repartition.isRestitution;

            // regroupement des montants par rubrique, en diférenciant les
            // restitutions des normales
            KeyRegroupementRubriqueConcernee keyRegroupementRubriqueConcernee = new KeyRegroupementRubriqueConcernee(
                    repartition.rubriqueConcernee, repartition.section);

            if (!isRestitution) {
                if (ecrituresRubriquesConcernees.containsKey(keyRegroupementRubriqueConcernee)) {
                    ((FWCurrency) ecrituresRubriquesConcernees.get(keyRegroupementRubriqueConcernee))
                            .add(new FWCurrency(repartition.montant));
                } else {
                    ecrituresRubriquesConcernees.put(keyRegroupementRubriqueConcernee, new FWCurrency(
                            repartition.montant));
                }
            } else {
                hasRestitution = true;

                if (ecrituresRubriquesConcerneesRestitution.containsKey(keyRegroupementRubriqueConcernee)) {
                    ((FWCurrency) ecrituresRubriquesConcerneesRestitution.get(keyRegroupementRubriqueConcernee))
                            .add(new FWCurrency(repartition.montant));
                } else {
                    ecrituresRubriquesConcerneesRestitution.put(keyRegroupementRubriqueConcernee, new FWCurrency(
                            repartition.montant));
                }
            }

            keyRegroupementRubriqueConcernee = null;

            // les cotisations AVS et AC ne doivent pas être écrites si c'est un
            // employeur non indépendant, on n'écrit
            // que dans
            // le fond de compensation dans ce cas

            // regroupement des cotisations pour les faire en une seule écriture
            // (normales, et restitutions, regroupées
            // suivant l'annee de cotisation)

            // AC
            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationAC)) {
                totalCotisations.add(new FWCurrency(repartition.cotisationAC));

                if (!isRestitution) {
                    if (!(repartition.isEmployeur && !repartition.isIndependant)) {
                        if (totalAC.containsKey(repartition.anneeCotisation)) {
                            ((FWCurrency) totalAC.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAC));
                            ((FWCurrency) totalAC.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAC));
                        } else {
                            totalAC.put(repartition.anneeCotisation, new FWCurrency(repartition.cotisationAC));
                            ((FWCurrency) totalAC.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAC));
                        }
                    }

                    if (repartition.isIndependant) {
                        totalFondDeCompensation.sub(new FWCurrency(repartition.cotisationAC));
                    } else if (repartition.isEmployeur) {
                        totalFondDeCompensation.add(new FWCurrency(repartition.cotisationAC));
                    } else {
                        totalFondDeCompensation.sub(new FWCurrency(repartition.cotisationAC));
                    }

                } else {
                    if (!(repartition.isEmployeur && !repartition.isIndependant)) {
                        if (totalACRestitution.containsKey(repartition.anneeCotisation)) {
                            ((FWCurrency) totalACRestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAC));
                            ((FWCurrency) totalACRestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAC));
                        } else {
                            totalACRestitution.put(repartition.anneeCotisation,
                                    new FWCurrency(repartition.cotisationAC));
                            ((FWCurrency) totalACRestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAC));
                        }
                    }

                    if (repartition.isIndependant) {
                        totalFondDeCompensationRestitution.sub(new FWCurrency(repartition.cotisationAC));
                    } else if (repartition.isEmployeur) {
                        totalFondDeCompensationRestitution.add(new FWCurrency(repartition.cotisationAC));
                    } else {
                        totalFondDeCompensationRestitution.sub(new FWCurrency(repartition.cotisationAC));
                    }

                }
            }

            // AVS
            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationAVS)) {
                totalCotisations.add(new FWCurrency(repartition.cotisationAVS));

                if (!isRestitution) {
                    if (!(repartition.isEmployeur && !repartition.isIndependant)) {
                        if (totalAVS.containsKey(repartition.anneeCotisation)) {
                            ((FWCurrency) totalAVS.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAVS));
                            ((FWCurrency) totalAVS.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAVS));
                        } else {
                            totalAVS.put(repartition.anneeCotisation, new FWCurrency(repartition.cotisationAVS));
                            ((FWCurrency) totalAVS.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAVS));
                        }
                    }

                    if (repartition.isIndependant) {
                        totalFondDeCompensation.sub(new FWCurrency(repartition.cotisationAVS));
                    } else if (repartition.isEmployeur) {
                        totalFondDeCompensation.add(new FWCurrency(repartition.cotisationAVS));
                    } else {
                        totalFondDeCompensation.sub(new FWCurrency(repartition.cotisationAVS));
                    }

                } else {
                    if (!(repartition.isEmployeur && !repartition.isIndependant)) {
                        if (totalAVSRestitution.containsKey(repartition.anneeCotisation)) {
                            ((FWCurrency) totalAVSRestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAVS));
                            ((FWCurrency) totalAVSRestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAVS));
                        } else {
                            totalAVSRestitution.put(repartition.anneeCotisation, new FWCurrency(
                                    repartition.cotisationAVS));
                            ((FWCurrency) totalAVSRestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                    repartition.cotisationAVS));
                        }
                    }

                    if (repartition.isIndependant) {
                        totalFondDeCompensationRestitution.sub(new FWCurrency(repartition.cotisationAVS));
                    } else if (repartition.isEmployeur) {
                        totalFondDeCompensationRestitution.add(new FWCurrency(repartition.cotisationAVS));
                    } else {
                        totalFondDeCompensationRestitution.sub(new FWCurrency(repartition.cotisationAVS));
                    }

                }
            }

            // LFA
            if (!JadeStringUtil.isDecimalEmpty(repartition.cotisationLFA)) {
                totalCotisations.add(new FWCurrency(repartition.cotisationLFA));

                if (!isRestitution) {
                    if (totalLFA.containsKey(repartition.anneeCotisation)) {
                        ((FWCurrency) totalLFA.get(repartition.anneeCotisation)).add(new FWCurrency(
                                repartition.cotisationLFA));
                    } else {
                        totalLFA.put(repartition.anneeCotisation, new FWCurrency(repartition.cotisationLFA));
                    }
                } else {
                    if (totalLFARestitution.containsKey(repartition.anneeCotisation)) {
                        ((FWCurrency) totalLFARestitution.get(repartition.anneeCotisation)).add(new FWCurrency(
                                repartition.cotisationLFA));
                    } else {
                        totalLFARestitution.put(repartition.anneeCotisation, new FWCurrency(repartition.cotisationLFA));
                    }
                }
            }

            // FA
            if (!JadeStringUtil.isDecimalEmpty(repartition.fraisAdministration)) {
                totalCotisations.add(new FWCurrency(repartition.fraisAdministration));

                if (!isRestitution) {
                    totalFA.add(new FWCurrency(repartition.fraisAdministration));
                } else {
                    totalFARestitution.add(new FWCurrency(repartition.fraisAdministration));
                }
            }

            // IS
            if (!JadeStringUtil.isDecimalEmpty(repartition.impotSource)) {
                totalCotisations.add(new FWCurrency(repartition.impotSource));

                if (!isRestitution) {
                    totalIS.add(new FWCurrency(repartition.impotSource));
                } else {
                    totalISRestitution.add(new FWCurrency(repartition.impotSource));
                }
            }

            // Versement des ventilations de cette répartition
            Iterator ventilationsIterator = repartition.ventilations.iterator();

            String nomPrenom = null;
            if (ventilationsIterator.hasNext()) {

                // Pour chaque ventilation, il faut ajouter dans le motif de
                // l'ordre de versement
                // le nss du requérant principal.
                nomPrenom = getNomPrenomRequerantPrincipal(repartition.idRepartitionPaiement);
            }

            while (ventilationsIterator.hasNext()) {
                Ventilation ventilation = (Ventilation) ventilationsIterator.next();

                if (ventilation.montant != null) {
                    FWCurrency mnt = new FWCurrency(ventilation.montant);
                    // Les ventilations négatives ne sont pas versées.
                    if (mnt.isPositive()) {
                        doOrdreVersement(compta, compteAnnexeIJ.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                                ventilation.montant, ventilation.idAdressePaiement, nomPrenom,
                                ventilation.referenceInterne);
                    }
                }

            }
        }

        // Ecritures sur les rubriques concernées (pour normal et restitution)
        Set keys = ecrituresRubriquesConcernees.keySet();
        Iterator ecrituresRubriquesConcerneesIterator = keys.iterator();

        FWCurrency montantTotalStandard = new FWCurrency(0);
        FWCurrency montantTotalRestitution = new FWCurrency(0);

        while (ecrituresRubriquesConcerneesIterator.hasNext()) {
            KeyRegroupementRubriqueConcernee keyRegroupementRubriqueConcernee = (KeyRegroupementRubriqueConcernee) ecrituresRubriquesConcerneesIterator
                    .next();
            FWCurrency montant = (FWCurrency) (ecrituresRubriquesConcernees.get(keyRegroupementRubriqueConcernee));

            if (montant != null) {
                montantTotalStandard.add(new FWCurrency(montant.toString()));
            }

            doEcriture(compta, montant.toString(), keyRegroupementRubriqueConcernee.rubrique,
                    compteAnnexeIJ.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

        }

        // Restitutions
        // creation de la section de restitution seulement si cela est
        // nécessaire
        APISection sectionRestitution = null;

        if (hasRestitution) {
            String noFactureRestitution = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(),
                    IntRole.ROLE_IJAI, idExterneRole, APISection.ID_TYPE_SECTION_RESTITUTION,
                    String.valueOf(new JADate(dateComptable).getYear()), APISection.ID_CATEGORIE_SECTION_RESTITUTIONS);
            sectionRestitution = compta.getSectionByIdExterne(compteAnnexeIJ.getIdCompteAnnexe(),
                    APISection.ID_TYPE_SECTION_RESTITUTION, noFactureRestitution);
        }

        // Ecriture des restitutions
        Set keysRestitution = ecrituresRubriquesConcerneesRestitution.keySet();
        Iterator ecrituresRubriquesConcerneesRestitutionIterator = keysRestitution.iterator();

        while (ecrituresRubriquesConcerneesRestitutionIterator.hasNext()) {
            KeyRegroupementRubriqueConcernee keyRegroupementRubriqueConcernee = (KeyRegroupementRubriqueConcernee) ecrituresRubriquesConcerneesRestitutionIterator
                    .next();
            FWCurrency montant = (FWCurrency) (ecrituresRubriquesConcerneesRestitution
                    .get(keyRegroupementRubriqueConcernee));
            if (montant != null) {
                montantTotalRestitution.add(new FWCurrency(montant.toString()));
            }

            doEcriture(compta, montant.toString(), keyRegroupementRubriqueConcernee.rubrique,
                    compteAnnexeIJ.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
        }

        FWCurrency montantTemp = new FWCurrency(0);

        // ecriture des cotisations (normales et restitutions)
        montantTemp = ecrisCotisations(compta, COT_AVS, compteAnnexeIJ.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), totalAVS);
        montantTotalStandard.add(new FWCurrency(montantTemp.toString()));

        montantTemp = ecrisCotisations(compta, COT_AC, compteAnnexeIJ.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), totalAC);
        montantTotalStandard.add(new FWCurrency(montantTemp.toString()));

        montantTotalStandard.add(new FWCurrency(totalFA.toString()));
        doEcriture(compta, totalFA.toString(), FRAIS_ADMINISTRATION, compteAnnexeIJ.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);

        montantTemp = ecrisCotisations(compta, COT_LFA, compteAnnexeIJ.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), totalLFA);
        montantTotalStandard.add(new FWCurrency(montantTemp.toString()));

        montantTotalStandard.add(new FWCurrency(totalIS.toString()));
        doEcriture(compta, totalIS.toString(), IMPOT_SOURCE, compteAnnexeIJ.getIdCompteAnnexe(),
                sectionNormale.getIdSection(), null);

        montantTotalStandard.add(new FWCurrency(totalFondDeCompensation.toString()));
        doEcriture(compta, totalFondDeCompensation.toString(), FONDS_DE_COMPENSATION,
                compteAnnexeIJ.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

        if (hasRestitution) {
            montantTemp = ecrisCotisations(compta, COT_AVS, compteAnnexeIJ.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), totalAVSRestitution);
            montantTotalRestitution.add(new FWCurrency(montantTemp.toString()));

            montantTemp = ecrisCotisations(compta, COT_AC, compteAnnexeIJ.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), totalACRestitution);
            montantTotalRestitution.add(new FWCurrency(montantTemp.toString()));

            montantTotalRestitution.add(new FWCurrency(totalFARestitution.toString()));
            doEcriture(compta, totalFARestitution.toString(), FRAIS_ADMINISTRATION, compteAnnexeIJ.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), null);

            montantTemp = ecrisCotisations(compta, COT_LFA, compteAnnexeIJ.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), totalLFARestitution);
            montantTotalRestitution.add(new FWCurrency(montantTemp.toString()));

            montantTotalRestitution.add(new FWCurrency(totalISRestitution.toString()));
            doEcriture(compta, totalISRestitution.toString(), IMPOT_SOURCE, compteAnnexeIJ.getIdCompteAnnexe(),
                    sectionRestitution.getIdSection(), null);

            montantTotalRestitution.add(new FWCurrency(totalFondDeCompensationRestitution.toString()));
            doEcriture(compta, totalFondDeCompensationRestitution.toString(), FONDS_DE_COMPENSATION,
                    compteAnnexeIJ.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);

        }

        // compensations
        Iterator compensationsPourCetIdIterator = compensationsPourCetId.iterator();

        // creation de la section pour les compensations seulement si nécessaire
        APICompteAnnexe compteAnnexeAffilie = null;
        APISection sectionCompensationFutures = null;

        while (compensationsPourCetIdIterator.hasNext()) {
            Compensation compensation = (Compensation) compensationsPourCetIdIterator.next();

            BigDecimal montantACompenserInverse = new BigDecimal(compensation.montant);
            montantACompenserInverse = montantACompenserInverse.negate();

            montantTotalStandard.add(new FWCurrency(montantACompenserInverse.toString()));

            if (montantACompenserInverse.signum() == 1 && sectionRestitution != null) {
                doEcriture(compta, montantACompenserInverse.toString(), COMPENSATION,
                        compteAnnexeIJ.getIdCompteAnnexe(), sectionRestitution.getIdSection(), null);
            } else {
                doEcriture(compta, montantACompenserInverse.toString(), COMPENSATION,
                        compteAnnexeIJ.getIdCompteAnnexe(), sectionNormale.getIdSection(), null);

            }

            if (!JadeStringUtil.isIntegerEmpty(compensation.idFacture)) {
                // c'est une facture existante
                APISection section = new CASection();
                section.setISession(getSession());
                section.setIdSection(compensation.idFacture);
                section.retrieve(getTransaction());

                montantTotalStandard.add(new FWCurrency(compensation.montant));
                doEcriture(compta, compensation.montant, COMPENSATION, section.getIdCompteAnnexe(),
                        section.getIdSection(), null);
            } else {
                // facture future
                if (compteAnnexeAffilie == null) {
                    // Uniquement pour les affilié. Si compensation manuelle sur
                    // facture future pour un assuré
                    // non répertorié dans la liste des situations prof. avec no
                    // affilié, généré message d'erreur.
                    if (JadeStringUtil.isIntegerEmpty(key.idAffilie)) {
                        getMemoryLog().logMessage(getSession().getLabel("COMPENS_NON_AFF_ERR"), FWMessage.ERREUR,
                                this.getClass().getName());
                        getMemoryLog().logMessage("idExterne = " + idExterneRole, FWMessage.ERREUR,
                                this.getClass().getName());
                        throw new Exception(getSession().getLabel("COMPENS_NON_AFF_ERR"));
                    }

                    String idRole = null;
                    if (compensation.isCompensationPourRoleAffiliePersonnel) {
                        idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                                getSession().getApplication());
                    } else {
                        idRole = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                                getSession().getApplication());
                    }

                    compteAnnexeAffilie = compta.getCompteAnnexeByRole(key.idTiers, idRole, idExterneRole);
                    // bz-4200
                    // sectionCompensationFutures =
                    // compta.getSectionByIdExterne(compteAnnexeAffilie.getIdCompteAnnexe(),
                    // APISection.ID_TYPE_SECTION_IJAI,
                    // String.valueOf(new JADate(dateComptable)
                    // .getYear()) +
                    // APISection.ID_TYPE_SECTION_IJAI +
                    // "000");
                    sectionCompensationFutures = compta.getSectionByIdExterne(compteAnnexeAffilie.getIdCompteAnnexe(),
                            APISection.ID_TYPE_SECTION_IJAI, noFactureNormale);

                }

                montantTotalStandard.add(new FWCurrency(compensation.montant));
                doEcriture(compta, compensation.montant, COMPENSATION, compteAnnexeAffilie.getIdCompteAnnexe(),
                        sectionCompensationFutures.getIdSection(), null);
            }
        }

        // versement effectif
        FWCurrency versement = new FWCurrency(montantBrutTotal.toString());
        versement.add(new FWCurrency(totalCotisations.toString()));
        versement.sub(new FWCurrency(compensationTotale.toString()));
        versement.sub(new FWCurrency(ventilationTotale.toString()));

        if (versement.isPositive()) {
            doOrdreVersement(compta, compteAnnexeIJ.getIdCompteAnnexe(), sectionNormale.getIdSection(),
                    versement.toString(), idAdressePaiementBeneficiaireDeBase, null, null);
        }

        if (getTransaction().hasErrors()) {
            throw new Exception(getTransaction().getErrors().toString());
        }

        // Retourne les éventuelles compensations interne pour ce
        // benéficiaire...
        CompensationInterne ci = new CompensationInterne();
        ci.montantTotPrestations = new FWCurrency(montantTotalStandard.toString());
        ci.montantTotRestitutions = new FWCurrency(montantTotalRestitution.toString());
        ci.idCompteAnnexe = compteAnnexeIJ.getIdCompteAnnexe();

        if (sectionNormale != null) {
            ci.idSectionNormale = sectionNormale.getIdSection();
        }

        if (sectionRestitution != null) {
            ci.idSectionRestitution = sectionRestitution.getIdSection();
        }
        return ci;
    }

    /**
     * getter pour l'attribut date comptable
     * 
     * @return la valeur courante de l'attribut date comptable
     */
    public String getDateComptable() {
        return dateComptable;
    }

    /**
     * @return
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("ECR_COM_GENERER_ECRITURES_COMPTABLES_PROCESS");
    }

    /**
     * getter pour l'attribut no lot
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * renvoie une map contenant des listes de IJGenererEcrituresComptablesProcess.Repartition regroupées par idTiers,
     * idAffilie et idAdressePaiement
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     *             <p>
     *             ATTENTION : Les répartitions doivent être lcassées
     *             </p>
     */
    private Map getMapRepartitions() throws Exception {
        IJRepartJointCotJointPrestJointEmployeurManager repartJointCotJointPrestManager = createManagerRechercheRepartition();

        BStatement statement = repartJointCotJointPrestManager.cursorOpen(getTransaction());

        IJRepartJointCotJointPrestJointEmployeur repartJointCotJointPrest = (IJRepartJointCotJointPrestJointEmployeur) repartJointCotJointPrestManager
                .cursorReadNext(statement);

        Map repartitions = new HashMap();

        while (repartJointCotJointPrest != null) {
            // On a à ce moment la premiere ligne d'une série de lignes
            // correspondant à la même répartition de
            // paiement (la repartition est la même, la différence entre les
            // lignes est la cotisation

            String idRepartitionPaiement = repartJointCotJointPrest.getIdRepartitionPaiement();
            boolean isEmployeur = repartJointCotJointPrest.getTypePaiement().equals(
                    IIJRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR);
            boolean isIndependant = repartJointCotJointPrest.getIsIndependant().booleanValue();
            boolean hasAC = false;
            boolean hasCotisation = false;
            String idTiers = repartJointCotJointPrest.getIdTiers();
            String idAffilie = repartJointCotJointPrest.getIdAffilie();
            boolean isRestitution = repartJointCotJointPrest.getContenuAnnonce().equals(IIJPrestation.CS_RESTITUTION);

            Repartition repartition = new Repartition();
            repartition.montant = repartJointCotJointPrest.getMontantBrut();

            if (JadeStringUtil.isBlankOrZero(repartJointCotJointPrest.getIdTiersAdressePaiement())) {
                // Si exception, c'est qu'il n'y a pas d'adresse de paiement...
                IJRepartitionPaiementsManager rep = new IJRepartitionPaiementsManager();
                rep.setForIdParent(repartJointCotJointPrest.getIdRepartitionPaiement());
                rep.setNotForIdRepartitionPaiement(repartJointCotJointPrest.getIdRepartitionPaiement());
                rep.setSession(getSession());

                // on check l'adresse de paiement, seulement si le montant net -
                // les ventilations donnent un reste
                FWCurrency totalVentilations = new FWCurrency(rep.getSum("XRMMVE", statement.getTransaction())
                        .toString());
                FWCurrency montantCtrl = new FWCurrency(repartJointCotJointPrest.getMontantNet());
                montantCtrl.sub(totalVentilations);

                if (montantCtrl.isPositive()) {
                    throw new Exception(getSession().getLabel("ADR_PMT_ERREUR_TIERS") + " "
                            + repartJointCotJointPrest.getIdTiers() + " idAff : "
                            + repartJointCotJointPrest.getIdAffilie() + " (" + getDateComptable() + ")");
                }
            } else {
                if (!JadeStringUtil.isIntegerEmpty(repartJointCotJointPrest.getMontantBrut())) {
                    try {
                        repartition.idAdressePaiement = repartJointCotJointPrest
                                .loadAdressePaiement(getDateComptable()).getIdAvoirPaiementUnique();
                    } catch (NullPointerException e) {
                        throw new Exception(getSession().getLabel("ADR_PMT_ERREUR_TIERS") + " "
                                + repartJointCotJointPrest.getIdTiers() + " idAff : "
                                + repartJointCotJointPrest.getIdAffilie() + " (" + getDateComptable() + ")");
                    }
                }
            }

            repartition.idRepartitionPaiement = repartJointCotJointPrest.getIdRepartitionPaiement();
            repartition.isRestitution = isRestitution;
            repartition.idCompensation = repartJointCotJointPrest.getIdCompensation();
            repartition.isEmployeur = isEmployeur;
            repartition.isIndependant = repartJointCotJointPrest.getIsIndependant().booleanValue();
            repartition.anneeCotisation = String.valueOf(new JADate(repartJointCotJointPrest.getDateDebutPrestation())
                    .getYear());
            repartition.idDepartement = repartJointCotJointPrest.getIdDepartement();

            String idPrestation = repartJointCotJointPrest.getIdPrestation();

            // On regarde ce qu'on a a regarder pour chaque cotisation de cette
            // repartition de paiement
            do {

                // //Si des répartitions on un montant égal à zéro, on ne les
                // prends pas en compte.
                // //Ce cas est possible si on verse la totalité de la
                // prestations à l'employeur.
                // //L'assuré n'est pas supprimé des répartitions, mais à un
                // montant égal à zéro
                // if
                // (!JadeStringUtil.isIntegerEmpty(repartJointCotJointPrest.getMontantBrut()))
                // {

                String genreCotisation = repartJointCotJointPrest.getGenreCotisation();

                // On regarde si il y a un genre de cotisation ET si l'idExterne
                // (id de l'assurance) n'est pas vide
                // (cela peut arriver si l'id n'est pas mis dans IJ.properties)
                // ou si c'est un impot a la soirce, il
                // est possible que l'on ait pas d'id externe
                if (genreCotisation.equals(IJRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE)
                        || (!JadeStringUtil.isEmpty(genreCotisation) && !JadeStringUtil
                                .isIntegerEmpty(repartJointCotJointPrest.getIdExterne()))) {
                    String montantCotisation = repartJointCotJointPrest.getMontantCotisation();
                    hasCotisation = true;

                    if (genreCotisation.equals(IJRepartJointCotJointPrestJointEmployeur.AC)) {
                        hasAC = true;
                        repartition.cotisationAC = montantCotisation;
                    } else if (genreCotisation.equals(IJRepartJointCotJointPrestJointEmployeur.AVS)) {
                        repartition.cotisationAVS = montantCotisation;
                    } else if (genreCotisation.equals(IJRepartJointCotJointPrestJointEmployeur.FRAIS_ADMINISTRATION)) {
                        repartition.fraisAdministration = montantCotisation;
                    } else if (genreCotisation.equals(IJRepartJointCotJointPrestJointEmployeur.IMPOT_SOURCE)) {
                        repartition.impotSource = montantCotisation;
                    } else if (genreCotisation.equals(IJRepartJointCotJointPrestJointEmployeur.LFA)) {
                        repartition.cotisationLFA = montantCotisation;
                    } else {
                        // ben là on est pas dans la mouise...
                        throw new Exception("Impossible de trouver le genre de la cotisation");
                    }
                }
                // }
            } while (((repartJointCotJointPrest = (IJRepartJointCotJointPrestJointEmployeur) repartJointCotJointPrestManager
                    .cursorReadNext(statement)) != null)
                    && idRepartitionPaiement.equals(repartJointCotJointPrest.getIdRepartitionPaiement()));

            // choix de la rubrique
            repartition.rubriqueConcernee = getRubriqueConcernee(isEmployeur, isIndependant, hasAC, hasCotisation,
                    isRestitution);

            Key key = null;

            // On récupère l'id de l'assuré de base
            IJPrestation prestation = new IJPrestation();
            prestation.setSession(getSession());
            prestation.setIdPrestation(idPrestation);
            prestation.retrieve(getTransaction());

            IJPrononce prononce = new IJPrononce();
            prononce.setSession(getSession());
            prononce.setIdPrononce(prestation.loadBaseIndemnisation(getTransaction()).getIdPrononce());
            prononce.retrieve(getTransaction());
            String idAssureDeBase = prononce.loadDemande(getTransaction()).getIdTiers();

            // Cas ou le bénéficiaire est l'assuré de base
            if (idAssureDeBase.equals(idTiers)) {
                // choix de la section
                repartition.section = getSection(idTiers, idAffilie, isRestitution);

                // key = new Key(idTiers, idAffilie,
                // repartition.idAdressePaiement, repartition.idDepartement);
                key = new Key(idTiers, idAffilie, "0", "0");

            }
            // Cas ou le bénéficiaire est un affilié
            else if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
                // choix de la section
                repartition.section = getSection(idTiers, idAffilie, isRestitution);

                // key = new Key(idTiers, idAffilie,
                // repartition.idAdressePaiement, repartition.idDepartement);
                key = new Key(idTiers, idAffilie, "0", "0");

            }
            // Cas ou le bénéficiaire est un employeur non affilié,
            // la clé est composé de l'idtiers du l'assuré principal.
            // Ceci implique que : les écritures seront effectuées sur le CA de
            // l'assuré.
            // On garde cependant l'adresse de paiement du non affilié pour le
            // versement.
            else {
                // choix de la section
                repartition.section = getSection(idAssureDeBase, idAffilie, isRestitution);

                // key = new Key(idAssureDeBase, idAffilie,
                // repartition.idAdressePaiement, repartition.idDepartement);
                key = new Key(idAssureDeBase, "0", idTiers, "0");
            }

            if (repartitions.containsKey(key)) {
                ((List) repartitions.get(key)).add(repartition);
            } else {
                List l = new ArrayList();
                l.add(repartition);
                repartitions.put(key, l);
            }
        }

        return repartitions;
    }

    /**
     * @return
     */
    private IJRepartJointCotJointPrestJointEmployeurManager createManagerRechercheRepartition() {
        IJRepartJointCotJointPrestJointEmployeurManager repartJointCotJointPrestManager = new IJRepartJointCotJointPrestJointEmployeurManager();
        repartJointCotJointPrestManager.setSession(getSession());
        repartJointCotJointPrestManager.setForIdLot(idLot);
        repartJointCotJointPrestManager.setForParentOnly(true);
        repartJointCotJointPrestManager.setOrderBy(IJCotisation.FIELDNAME_IDREPARTITIONPAIEMENTS);
        return repartJointCotJointPrestManager;
    }

    /**
     * @param idRepartitionPmt
     * @return NSS du requérant principal, à partir de l'id de la répartition des paiements. null si non trouvé.
     */
    private String getNomPrenomRequerantPrincipal(String idRepartitionPmt) {

        String nomPrenom = null;

        try {
            IJRepartitionPaiements rep = new IJRepartitionPaiements();
            rep.setSession(getSession());
            rep.setIdRepartitionPaiement(idRepartitionPmt);
            rep.retrieve(getTransaction());

            if (rep != null && !rep.isNew()) {
                String idPrestation = rep.getIdPrestation();
                IJPrestation prestation = new IJPrestation();
                prestation.setSession(getSession());
                prestation.setIdPrestation(idPrestation);
                prestation.retrieve(getTransaction());
                if (prestation != null && !prestation.isNew()) {

                    IJBaseIndemnisation bi = new IJBaseIndemnisation();
                    bi.setSession(getSession());
                    bi.setIdBaseIndemisation(prestation.getIdBaseIndemnisation());
                    bi.retrieve(getTransaction());
                    if (bi != null && !bi.isNew()) {
                        IJPrononce prononce = new IJPrononce();
                        prononce.setSession(getSession());
                        prononce.setIdPrononce(bi.getIdPrononce());
                        prononce.retrieve(getTransaction());

                        if (prononce != null && !prononce.isNew()) {
                            PRTiersWrapper tiersWP = prononce.loadDemande(getTransaction()).loadTiers();
                            nomPrenom = tiersWP.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tiersWP.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return nomPrenom;
    }

    /**
     * @param isEmployeur
     *            si c'est un employeur
     * @param isIndependant
     *            Si c'est un indépendant
     * @param hasAC
     *            si il a des cotisations AC
     * @param hasCotisation
     *            si il a des cotisations
     * @param isRestitution
     *            s'il s'agit d'une restitution
     * 
     * @return la rubrique concernée
     */
    private APIRubrique getRubriqueConcernee(boolean isEmployeur, boolean isIndependant, boolean hasAC,
            boolean hasCotisation, boolean isRestitution) {
        APIRubrique rubrique = null;

        if (isRestitution) {
            rubrique = PRESTATIONS_A_RESTITUER;
        } else {
            if (isEmployeur) {
                if (isIndependant) {
                    rubrique = ASSURE_OU_INDEPENDANT;
                } else {
                    rubrique = EMPLOYEUR;
                }
            } else {
                rubrique = ASSURE_OU_INDEPENDANT;
            }
        }

        return rubrique;
    }

    private String getSection(String idTiers, String idAffilie, boolean isRestitution) throws Exception {
        String idExterne = null;

        if (!JadeStringUtil.isIntegerEmpty(idAffilie)) {
            idExterne = PRAffiliationHelper
                    .getEmployeurParIdAffilie(getSession(), getTransaction(), idAffilie, idTiers).getNumAffilie();
        } else {
            idExterne = PRTiersHelper.getTiersParId(getSession(), idTiers).getProperty(
                    PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        }

        String section = null;

        if (isRestitution) {
            section = "restitution " + idExterne;
        } else {
            section = "IJAI " + idExterne;
        }

        return section;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * Initialise les Id des rubriques
     * 
     * @param sessionOsiris
     *            une instance de APIProcessComptabilisation
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void initIdsRubriques(BISession sessionOsiris) throws Exception {
        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);
        EMPLOYEUR = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_EMPLOYEUR);
        ASSURE_OU_INDEPENDANT = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_ASSURE_OU_INDEPENDANT);
        PRESTATIONS_A_RESTITUER = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_PRESTATIONS_A_RESTITUER);
        COT_AVS = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_COTISATIONS_AVS);
        COT_AC = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_COTISATIONS_AC);
        FONDS_DE_COMPENSATION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_FONDS_DE_COMPENSATION);
        COT_LFA = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_COTISATIONS_LFA);
        FRAIS_ADMINISTRATION = referenceRubrique
                .getRubriqueByCodeReference(APIReferenceRubrique.IJAI_FRAIS_ADMINISTRATION);
        IMPOT_SOURCE = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.IJAI_IMPOT_A_LA_SOURCE);
        COMPENSATION = referenceRubrique.getRubriqueByCodeReference(APIReferenceRubrique.COMPENSATION_IJAI);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * setter pour l'attribut date comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateComptable(String string) {
        dateComptable = string;
    }

    /**
     * @param string
     */
    public void setDateSurDocument(String string) {
        dateSurDocument = string;
    }

    /**
     * setter pour l'attribut no lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

}
