package globaz.apg.impl.process;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.api.process.IAPGenererCompensationProcess;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.lots.APCompensation;
import globaz.apg.db.lots.APCompensationManager;
import globaz.apg.db.lots.APFactureACompenser;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.db.prestation.APRepartitionPaiementsJointEmployeur;
import globaz.apg.db.prestation.APRepartitionPaiementsJointEmployeurManager;
import globaz.apg.exceptions.APTechnicalException;
import globaz.apg.process.Key;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;

/**
 * <p>
 * Process de base permettant de générer une compensation dans les APG.
 * </p>
 * <p>
 * L'abstraction est là car {@link APGenererCompensationsProcess004} (nouvelle implementation, découlant du mandat
 * Inforom 463) est une copie de {@link APGenererCompensationsProcess002} à cela près que 002 ne prend pas en compte la
 * compensation automatique de section. Cela évite donc de tout récrire (002 retournant <code>false</code> dans tous les
 * cas) grâce aux méthodes {@link #isSectionACompenserAutomatiquement(APISection)} et
 * {@link #isSectionAVerifier(APISection)}.
 * </p>
 *
 * @author PBA
 */
public abstract class APGenererCompensationsProcessAvecSectionCompensable extends BProcess implements
        IAPGenererCompensationProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected class SectionEtCompteAnnexe {
        private APICompteAnnexe compteAnnexe;
        private APISection section;

        public SectionEtCompteAnnexe(APISection section, APICompteAnnexe compteAnnexe) {
            this.section = section;
            this.compteAnnexe = compteAnnexe;
        }

        public APICompteAnnexe getCompteAnnexe() {
            return compteAnnexe;
        }

        public String getIdExterne() {
            if (section == null) {
                return null;
            }
            return section.getIdExterne();
        }

        public String getIdExterneRole() {
            if (compteAnnexe == null) {
                return null;
            }
            return compteAnnexe.getIdExterneRole();
        }

        public String getIdSection() {
            if (section == null) {
                return null;
            }
            return section.getIdSection();
        }

        public String getIdTiers() {
            if (compteAnnexe == null) {
                return null;
            }
            return compteAnnexe.getIdTiers();
        }

        public APISection getSection() {
            return section;
        }

        public String getSolde() {
            if (section == null) {
                return null;
            }
            return section.getSolde();
        }
    };

    private String forIdLot = "";
    private String moisPeriodeFacturation = "";

    public APGenererCompensationsProcessAvecSectionCompensable() {
        super();
    }

    public APGenererCompensationsProcessAvecSectionCompensable(BProcess parent) {
        super(parent);
    }

    public APGenererCompensationsProcessAvecSectionCompensable(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        try {
            // Il faut que toutes les répartitions de paiement aient une adresse de paiement.
            // cette validation a été mise ici pour qu'elle apparaisse dans le mail
            APRepartitionJointPrestationManager repartitionJointPrestationManager = new APRepartitionJointPrestationManager();
            repartitionJointPrestationManager.setSession(getSession());
            repartitionJointPrestationManager.setForIdLot(forIdLot);

            APRepartitionJointPrestation repartitionJointPrestation = null;
            repartitionJointPrestationManager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < repartitionJointPrestationManager.size(); i++) {
                repartitionJointPrestation = (APRepartitionJointPrestation) repartitionJointPrestationManager
                        .getEntity(i);

                /*
                 * Test pour les adresses de paiement absente
                 * 
                 * On génère une erreur uniquement dans le cas où la personne reçoit quelque chose !
                 * 
                 * 2) Si total des ventilations = montant net 3) Si adresse de paiement = null
                 * 
                 * --> > Si 3 && !2 => KO > Sinon, OK
                 */

                APDroitLAPG droitLAPG = new APDroitLAPG();
                droitLAPG.setSession(getSession());
                droitLAPG.setIdDroit(repartitionJointPrestation.getIdDroit());
                droitLAPG.retrieve();

                PRTiersWrapper tw = droitLAPG.loadDemande().loadTiers();
                if (!isTiersRequerantActif(tw)) {

                    getMemoryLog().logMessage(
                            java.text.MessageFormat.format(
                                    getSession().getLabel("TIERS_INACTIF") + " "
                                            + tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                    new Object[] { repartitionJointPrestation.getNom(),
                                            tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                            repartitionJointPrestation.getIdPrestationApg() }), FWMessage.ERREUR,
                            getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }

                // unique test
                FWCurrency montantTotalVentilations = new FWCurrency();

                APRepartitionJointPrestationManager mgr = new APRepartitionJointPrestationManager();
                mgr.setSession(getSession());
                mgr.setForIdParent(repartitionJointPrestation.getIdRepartitionBeneficiairePaiement());
                mgr.find();

                for (int j = 0; j < mgr.size(); j++) {
                    APRepartitionJointPrestation entity = (APRepartitionJointPrestation) mgr.get(j);

                    montantTotalVentilations.add(entity.getMontantVentile());

                }

                if ((repartitionJointPrestation.loadAdressePaiement(null) == null)
                        && !montantTotalVentilations.equals(new FWCurrency(repartitionJointPrestation.getMontantNet()))) {

                    String noAVS = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    getMemoryLog().logMessage(
                            java.text.MessageFormat.format(getSession().getLabel("ADRESSE_PAIEMENT_ABSENTE"),
                                    new Object[] { repartitionJointPrestation.getNom(), noAVS,
                                            repartitionJointPrestation.getIdPrestationApg() }), FWMessage.ERREUR,
                            getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }

                // adresse de courrier absente
                if (JadeStringUtil.isEmpty(PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                        repartitionJointPrestation.getIdTiers(), repartitionJointPrestation.getIdAffilie(),
                        APApplication.CS_DOMAINE_ADRESSE_APG))) {

                    String noAVS = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    getMemoryLog().logMessage(
                            java.text.MessageFormat.format(getSession().getLabel("ADRESSE_COURRIER_ABSENTE"),
                                    new Object[] { repartitionJointPrestation.getNom(), noAVS,
                                            repartitionJointPrestation.getIdPrestationApg() }), FWMessage.ERREUR,
                            getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }

                // Etat civil absent

                if (PRTiersHelper.getPersonneAVS(getSession(), droitLAPG.loadDemande().getIdTiers()) != null) {
                    if (JadeStringUtil.isIntegerEmpty(PRTiersHelper.getPersonneAVS(getSession(),
                            droitLAPG.loadDemande().getIdTiers()).getProperty(
                            PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL))) {

                        String noAVS = droitLAPG.loadDemande().loadTiers()
                                .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                        getMemoryLog().logMessage(
                                java.text.MessageFormat.format(getSession().getLabel("ETAT_CIVIL_ABSENT"),
                                        new Object[] { repartitionJointPrestation.getNom(), noAVS,
                                                repartitionJointPrestation.getIdPrestationApg() }), FWMessage.ERREUR,
                                getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                    }
                } else {
                    getMemoryLog().logMessage(
                            "Tiers non trouvé pour idTiers n° " + droitLAPG.loadDemande().getIdTiers(),
                            FWMessage.ERREUR, getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }

                // Montant brut vaut zero
                if ((Float.parseFloat(repartitionJointPrestation.getMontantBrut()) == 0)
                        && (Float.parseFloat(repartitionJointPrestation.getMontantVentile()) == 0)) {

                    String noAVS = droitLAPG.loadDemande().loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    getMemoryLog().logMessage(
                            java.text.MessageFormat.format(getSession().getLabel("MONTANT_BRUT_VAUT_ZERO"),
                                    new Object[] { repartitionJointPrestation.getIdPrestationApg(), noAVS }),
                            FWMessage.ERREUR, getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }

            }

            if (!getMemoryLog().isOnErrorLevel() && !getMemoryLog().isOnFatalLevel()) {
                // Suppression des compensations ratachées à ce lot
                APCompensationManager compensationManager = new APCompensationManager();
                compensationManager.setForIdLot(forIdLot);
                compensationManager.setSession(session);
                compensationManager.find(transaction, BManager.SIZE_NOLIMIT);

                for (int i = 0; i < compensationManager.size(); i++) {
                    APCompensation compensation = (APCompensation) compensationManager.getEntity(i);
                    compensation.delete(transaction);
                }

                compensationManager = null;

                // Parcours des répartitions parentes de ce lot pour calculer la somme des montants regroupés par tiers
                // affilié adresse de paiement et département.
                // Création dans le même temps d'une map contenant en clef un idTiers et en valeur un montant, pour
                // avoir la somme pour un idTiers

                APRepartitionPaiementsJointEmployeurManager repartitionPaiementsJointEmployeurManager = new APRepartitionPaiementsJointEmployeurManager();
                repartitionPaiementsJointEmployeurManager.setForIdLot(forIdLot);
                repartitionPaiementsJointEmployeurManager.setForTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
                repartitionPaiementsJointEmployeurManager.setParentOnly(true);
                repartitionPaiementsJointEmployeurManager.setSession(session);
                repartitionPaiementsJointEmployeurManager.setOrderBy(APRepartitionPaiements.FIELDNAME_IDTIERS);

                BStatement statement = repartitionPaiementsJointEmployeurManager.cursorOpen(transaction);
                APRepartitionPaiementsJointEmployeur repartitionPaiementsJointEmployeur = null;

                SortedMap<Key, FWCurrency> sommes = new TreeMap<Key, FWCurrency>();
                Map<String, FWCurrency> sommePourUnTiers = new HashMap<String, FWCurrency>();

                while ((repartitionPaiementsJointEmployeur = (APRepartitionPaiementsJointEmployeur) repartitionPaiementsJointEmployeurManager
                        .cursorReadNext(statement)) != null) {
                    APDroitLAPG droit = new APDroitLAPG();

                    droit.setSession(getSession());
                    droit.setIdDroit(repartitionPaiementsJointEmployeur.getIdDroit());
                    droit.retrieve();

                    String idTiersAssure = droit.loadDemande().loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

                    // Si des répartitions porte sur un employeur non affilié, on ne le prends pas en compte
                    if (JadeStringUtil.isIntegerEmpty(repartitionPaiementsJointEmployeur.getIdAffilie())
                            && !idTiersAssure.equals(repartitionPaiementsJointEmployeur.getIdTiers())) {

                        continue;
                    }

                    Key key = new Key(repartitionPaiementsJointEmployeur.getIdTiers(),
                            repartitionPaiementsJointEmployeur.getIdAffilie(), "0",
                            repartitionPaiementsJointEmployeur.getIdParticularite(),
                            repartitionPaiementsJointEmployeur.getGenrePrestationPrestation());

                    key.idDomaineAdressePaiement = repartitionPaiementsJointEmployeur.getIdDomaineAdressePaiement();
                    key.idTiersAdressePaiement = repartitionPaiementsJointEmployeur.getIdTiersAdressePaiement();

                    // On ne prend que les répartition parente. Les répartitions enfants (montant ventilé) correspondent
                    // à une adresse de paiement au niveau comptable. Le bénéficiaire en compta est celui de la
                    // répartition parent, seul l'adresse de paiement est celle de la répartition ventilée.
                    // Il n'est donc pas possible de compensé sur une 'adresse de paiement'.
                    // Le montant total à répartir est donc égal au montant net du parent moins la sommes des montants
                    // ventilés.

                    FWCurrency montantRepartition = new FWCurrency(repartitionPaiementsJointEmployeur.getMontantNet());
                    montantRepartition.sub(getMontantVentile(session,
                            repartitionPaiementsJointEmployeur.getIdRepartitionBeneficiairePaiement(), forIdLot));

                    if (sommes.containsKey(key)) {
                        FWCurrency somme = sommes.get(key);
                        somme.add(new FWCurrency(montantRepartition.toString()));
                    } else {
                        sommes.put(key, new FWCurrency(montantRepartition.toString()));
                    }

                    if (sommePourUnTiers.containsKey(repartitionPaiementsJointEmployeur.getIdTiers())) {
                        FWCurrency sommePourCeTiers = sommePourUnTiers.get(repartitionPaiementsJointEmployeur
                                .getIdTiers());
                        sommePourCeTiers.add(new FWCurrency(montantRepartition.toString()));
                    } else {
                        sommePourUnTiers.put(repartitionPaiementsJointEmployeur.getIdTiers(), new FWCurrency(
                                montantRepartition.toString()));
                    }
                }

                repartitionPaiementsJointEmployeurManager.cursorClose(statement);
                repartitionPaiementsJointEmployeurManager = null;

                SectionEtCompteAnnexe sectionEnCours = null;
                Collection<SectionEtCompteAnnexe> propositionsCompensationsPourTiersEnCours = null;
                String idTiersEnCours = null;
                Iterator<SectionEtCompteAnnexe> propositionsCompensationsIterator = null;
                FWCurrency montantDejaCompenseSurSectionEnCours = new FWCurrency(0);

                for (Key key : sommes.keySet()) {

                    // creation de la compensation
                    APCompensation compensation = new APCompensation();
                    compensation.setSession(session);
                    compensation.setIdLot(forIdLot);
                    compensation.setIdTiers(key.idTiers);
                    compensation.setIdAffilie(key.idAffilie);
                    compensation.setMontantTotal(sommes.get(key).toString());
                    compensation.setDettes(getDettes(key.idTiers));
                    compensation.setGenrePrestation(key.genrePrestation);
                    compensation.add(transaction);
                    getMemoryLog().logMessage(
                            MessageFormat.format(getSession().getLabel("COMPENSATION_AJOUTEE"),
                                    new Object[] { compensation.getIdCompensation() }), FWMessage.INFORMATION,
                            getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));

                    IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
                    employeur.setAffiliationId(key.idAffilie);
                    employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                    employeur.retrieve(getTransaction());

                    // Si on n'a pas de section en cours, ou que on est sur un
                    // autre idTiers que l'itération
                    // précédente, on
                    // va créer tout ça
                    if ((sectionEnCours == null) || !key.idTiers.equals(idTiersEnCours)) {
                        idTiersEnCours = key.idTiers;
                        montantDejaCompenseSurSectionEnCours = new FWCurrency(0);
                        propositionsCompensationsPourTiersEnCours = getCollectionSectionsACompenser(key.idTiers,
                                sommes.get(key));
                        propositionsCompensationsIterator = propositionsCompensationsPourTiersEnCours.iterator();

                        if (propositionsCompensationsIterator.hasNext()) {
                            sectionEnCours = propositionsCompensationsIterator.next();
                        } else {
                            sectionEnCours = null;
                        }
                    }

                    // compensation de la section en cours
                    FWCurrency montantTotalCompense = new FWCurrency(0);
                    new FWCurrency(0);

                    if (sectionEnCours != null) {
                        // si on n'a pas encore tout compensé sur la section en
                        // cours
                        if (Math.abs(montantDejaCompenseSurSectionEnCours.doubleValue()) != Math.abs(Double
                                .parseDouble(sectionEnCours.getSolde()))) {
                            APFactureACompenser factureACompenser = new APFactureACompenser();
                            factureACompenser.setSession(session);
                            factureACompenser.setIdAffilie(compensation.getIdAffilie());

                            // on va compenser ce qui reste a compenser sur
                            // cette section si on peu
                            if (Math.abs(Double.parseDouble(compensation.getMontantTotal())) > ((Math.abs(Double
                                    .parseDouble(sectionEnCours.getSolde()))) - Math
                                    .abs(montantDejaCompenseSurSectionEnCours.doubleValue()))) {
                                // on compense la totalité de ce qui reste de la
                                // section
                                FWCurrency montantCompense = new FWCurrency(sectionEnCours.getSolde());
                                montantCompense.sub(new FWCurrency(montantDejaCompenseSurSectionEnCours.toString()));
                                factureACompenser.setMontant(montantCompense.toString());
                                montantTotalCompense.add(new FWCurrency(montantCompense.toString()));
                                montantDejaCompenseSurSectionEnCours.add(new FWCurrency(montantCompense.toString()));
                            } else {
                                // on compense ce que l'on peut
                                factureACompenser.setMontant(compensation.getMontantTotal());
                                montantTotalCompense.add(new FWCurrency(compensation.getMontantTotal()));
                                montantDejaCompenseSurSectionEnCours
                                        .add(new FWCurrency(compensation.getMontantTotal()));
                            }

                            factureACompenser.setIdCompensationParente(compensation.getIdCompensation());
                            factureACompenser.setIdTiers(sectionEnCours.getIdTiers());
                            factureACompenser.setIdFacture(sectionEnCours.getIdSection());
                            factureACompenser.setNoFacture(sectionEnCours.getIdExterne());

                            // Inforom 463 et BZ 8605
                            if (isAffilieEnFaillite(sectionEnCours, getAnneePeriodeFacturation())
                                    || isSectionACompenserAutomatiquement(sectionEnCours)) {
                                factureACompenser.setIsCompenser(Boolean.TRUE);

                                getMemoryLog().logMessage(
                                        MessageFormat.format(
                                                getSession()
                                                        .getLabel("PROCESS_GENERER_COMPENSATIONS_SECTION_POURSUITE"),
                                                sectionEnCours.getIdExterneRole(), sectionEnCours.getIdExterne()),
                                        FWMessage.AVERTISSEMENT, "");
                            } else {
                                factureACompenser.setIsCompenser(Boolean.FALSE);

                                if (isSectionAVerifier(sectionEnCours)) {
                                    getMemoryLog()
                                            .logMessage(
                                                    MessageFormat.format(
                                                            getSession()
                                                                    .getLabel(
                                                                            "PROCESS_GENERER_COMPENSATIONS_SECTION_CONTENTIEUX_OU_RADIEE"),
                                                            sectionEnCours.getIdExterneRole(), sectionEnCours
                                                                    .getIdExterne()), FWMessage.AVERTISSEMENT, "");
                                }
                            }

                            factureACompenser.add(transaction);
                        }

                        // compensation des suivantes, tant qu'on en a a
                        // compenser et qu'on peut encore compenser
                        while (propositionsCompensationsIterator.hasNext()
                                && (Math.abs(montantTotalCompense.doubleValue()) < Math.abs(Double
                                        .parseDouble(compensation.getMontantTotal())))) {
                            sectionEnCours = propositionsCompensationsIterator.next();
                            montantDejaCompenseSurSectionEnCours = new FWCurrency(0);

                            APFactureACompenser factureACompenser = new APFactureACompenser();
                            factureACompenser.setSession(session);
                            factureACompenser.setIdAffilie(compensation.getIdAffilie());

                            // On regarde si le solde de la section est
                            // supérieur au montant total moins ce qu'on a déjà
                            // compensé. Si oui, on compense ce qu'on peut, si
                            // non, on compense tout.
                            if (Math.abs(Double.parseDouble(compensation.getMontantTotal())) > (Math
                                    .abs(montantTotalCompense.doubleValue()) + Math.abs(Double
                                    .parseDouble(sectionEnCours.getSolde())))) {
                                factureACompenser.setMontant(sectionEnCours.getSolde());
                                montantTotalCompense.add(new FWCurrency(sectionEnCours.getSolde()));
                                montantDejaCompenseSurSectionEnCours.add(new FWCurrency(sectionEnCours.getSolde()));
                            } else {
                                FWCurrency montantQuonPeutCompenser = new FWCurrency(compensation.getMontantTotal());
                                montantQuonPeutCompenser.sub(new FWCurrency(montantTotalCompense.toString()));
                                factureACompenser.setMontant(montantQuonPeutCompenser.toString());
                                montantTotalCompense.add(new FWCurrency(montantQuonPeutCompenser.toString()));
                                montantDejaCompenseSurSectionEnCours.add(new FWCurrency(montantQuonPeutCompenser
                                        .toString()));
                            }

                            factureACompenser.setIdCompensationParente(compensation.getIdCompensation());

                            factureACompenser.setIdTiers(sectionEnCours.getIdTiers());
                            factureACompenser.setIdFacture(sectionEnCours.getIdSection());
                            factureACompenser.setNoFacture(sectionEnCours.getIdExterne());

                            // Inforom 463
                            if (isSectionACompenserAutomatiquement(sectionEnCours)) {
                                factureACompenser.setIsCompenser(Boolean.TRUE);

                                getMemoryLog().logMessage(
                                        MessageFormat.format(
                                                getSession()
                                                        .getLabel("PROCESS_GENERER_COMPENSATIONS_SECTION_POURSUITE"),
                                                sectionEnCours.getIdExterneRole(), sectionEnCours.getIdExterne()),
                                        FWMessage.AVERTISSEMENT, "");
                            } else {
                                factureACompenser.setIsCompenser(Boolean.FALSE);

                                if (isSectionAVerifier(sectionEnCours)) {
                                    getMemoryLog()
                                            .logMessage(
                                                    MessageFormat.format(
                                                            getSession()
                                                                    .getLabel(
                                                                            "PROCESS_GENERER_COMPENSATIONS_SECTION_CONTENTIEUX_OU_RADIEE"),
                                                            sectionEnCours.getIdExterneRole(), sectionEnCours
                                                                    .getIdExterne()), FWMessage.AVERTISSEMENT, "");
                                }
                            }

                            factureACompenser.add(transaction);
                            getMemoryLog().logMessage("facture ajoutée " + factureACompenser.getIdFactureACompenser(),
                                    "", "");
                        }
                    }

                    // mise à jour des repartitions (pour l'idCompensation)
                    repartitionPaiementsJointEmployeurManager = new APRepartitionPaiementsJointEmployeurManager();
                    repartitionPaiementsJointEmployeurManager.setSession(session);
                    repartitionPaiementsJointEmployeurManager.setForIdLot(forIdLot);
                    repartitionPaiementsJointEmployeurManager.setForIdTiers(key.idTiers);
                    repartitionPaiementsJointEmployeurManager.setForIdAffilie(key.idAffilie);
                    repartitionPaiementsJointEmployeurManager.setForIdParticularite(key.idExtra2);
                    repartitionPaiementsJointEmployeurManager.setForGenrePrestation(key.genrePrestation);

                    statement = repartitionPaiementsJointEmployeurManager.cursorOpen(transaction);
                    repartitionPaiementsJointEmployeur = null;

                    APRepartitionPaiements repartitionPaiements = null;

                    while ((repartitionPaiements = (APRepartitionPaiements) repartitionPaiementsJointEmployeurManager
                            .cursorReadNext(statement)) != null) {
                        repartitionPaiements.setIdCompensation(compensation.getIdCompensation());
                        repartitionPaiements.wantMiseAJourLot(false);
                        repartitionPaiements.update(transaction);
                        getMemoryLog()
                                .logMessage(
                                        "repart updatée " + repartitionPaiements.getIdRepartitionBeneficiairePaiement(),
                                        "", "");
                    }

                    repartitionPaiementsJointEmployeurManager = null;
                }

                // mise à jour du lot
                APLot lot = new APLot();
                lot.setSession(session);
                lot.setIdLot(forIdLot);
                lot.retrieve(transaction);
                lot.setEtat(IAPLot.CS_COMPENSE);
                lot.update(transaction);
            }

            return true;

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            getMemoryLog().logMessage(e.getMessage(), "", "");

            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        APLot lot = new APLot();
        lot.setSession(getSession());
        lot.setIdLot(forIdLot);
        lot.retrieve();

        if (JadeStringUtil.isIntegerEmpty(lot.getIdLot())) {
            this._addError(getTransaction(), getSession().getLabel("NO_LOT_REQUIS"));
        }

        if (lot.getEtat().equals(IAPLot.CS_VALIDE)) {
            this._addError(getTransaction(), getSession().getLabel("LOT_DEJA_VALIDE"));
        }
    }

    private String getAnneePeriodeFacturation() {
        if (!JadeStringUtil.isIntegerEmpty(moisPeriodeFacturation)
                && JadeDateUtil.isGlobazDateMonthYear(moisPeriodeFacturation)) {
            return moisPeriodeFacturation.substring(3);
        }
        return Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    }

    /**
     * @param idTiers
     *            l'idTiers de la personne susceptible de devoir compenser des trucs
     * @param montant
     *            montant qu'on peut compenser
     * 
     * @return une Collection de CAPropositionCompensation
     */
    private Collection<SectionEtCompteAnnexe> getCollectionSectionsACompenser(String idTiers, FWCurrency montant)
            throws Exception {
        BISession sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");

        Collection<SectionEtCompteAnnexe> compensations = new ArrayList<SectionEtCompteAnnexe>();

        if (montant.isZero()) {
            return compensations;
        } else {

            APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) sessionOsiris
                    .getAPIFor(APIPropositionCompensation.class);
            FWCurrency montantTotalNegate = new FWCurrency(montant.toString());
            montantTotalNegate.negate();

            Collection<APISection> sections = propositionCompensation.propositionCompensation(
                    Integer.parseInt(idTiers), montantTotalNegate, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

            for (APISection uneSection : sections) {
                APICompteAnnexe compteAnnexe = uneSection.getCompteAnnexe();

                SectionEtCompteAnnexe sectionEtCompteAnnexe = new SectionEtCompteAnnexe(uneSection, compteAnnexe);
                compensations.add(sectionEtCompteAnnexe);
            }

            return compensations;
        }

    }

    /**
     * @param idTiers
     *            Le tiers dont on veut avoir les dettes
     * 
     * @return le montant des dettes
     * 
     * @throws Exception
     *             si il y a un pb dans OSIRIS pendant la récupération des dettes
     */
    private String getDettes(String idTiers) throws Exception {
        // TODO voir si y a un autre moyen que d'instancier un manager d'OSIRIS
        CACompteAnnexeManager managerCA = new CACompteAnnexeManager();
        managerCA.setSession(getSession());
        managerCA.setForIdTiers(idTiers);
        managerCA.find(BManager.SIZE_NOLIMIT);

        FWCurrency montant = new FWCurrency(0);

        for (int i = 0; i < managerCA.size(); i++) {
            APICompteAnnexe compteAnnexe = (APICompteAnnexe) managerCA.getEntity(i);
            montant.add(new FWCurrency(compteAnnexe.getSolde()));
        }

        if (montant.isPositive()) {
            return montant.toString();
        } else {
            return "";
        }
    }

    @Override
    protected String getEMailObject() {
        APLot lot = new APLot();
        lot.setSession(getSession());
        lot.setIdLot(forIdLot);
        try {
            lot.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getSession().getLabel("PROCESS_GENERER_COMPENSATIONS") + " du lot: " + lot.getDescription()
                + ", status: " + ((getMemoryLog().hasErrors() == true) ? "ECHEC" : "SUCCES");
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getMoisPeriodeFacturation() {
        return moisPeriodeFacturation;
    }

    /**
     * Retourne la somme des montants ventilés d'une répartition à partir du parent.
     */
    private FWCurrency getMontantVentile(BSession session, String idParent, String idLot) throws Exception {

        FWCurrency result = new FWCurrency();

        // Récupération des montants ventilés
        APRepartitionPaiementsJointEmployeurManager mgr = new APRepartitionPaiementsJointEmployeurManager();
        mgr.setForIdLot(forIdLot);
        mgr.setForIdParent(idParent);
        mgr.setParentOnly(false);
        mgr.setSession(session);
        mgr.find();
        for (int i = 0; i < mgr.size(); i++) {
            APRepartitionPaiementsJointEmployeur entity = (APRepartitionPaiementsJointEmployeur) mgr.getEntity(i);
            result.add(new FWCurrency(entity.getMontantVentile()));
        }
        return result;
    }

    /**
     * <p>
     * BZ 8605
     * </p>
     * <p>
     * Ajout supplémentaire, par rapport au mandat InfoRom 463. Les sections et comptes annexes dont le blocage comporte
     * un motif de faillite doivent sortir avec un message particulier (prioritaire sur les poursuites) et être
     * compensés automatiquement.
     * </p>
     * 
     * @param sectionEtCompteAnnexe
     *            une section non soldée, ainsi que son compte annexe
     * @param annee
     *            l'année dans laquelle on aimerait savoir si l'affilié est en faillite
     * @return <code>true</code> si la section doit être vérifiée par l'utilisateur car employeur en faillite
     * @throws APTechnicalException
     *             lorsqu'un problème survient dans la lecture des données de la comptabilité auxiliaire
     */
    protected abstract boolean isAffilieEnFaillite(SectionEtCompteAnnexe sectionEtCompteAnnexe, String annee)
            throws APTechnicalException;

    /**
     * <p>
     * Inforom 463
     * </p>
     * <p>
     * Défini si une section est au contentieux et a déjà passé l'étape "Poursuite envoyée" et que la poursuite n'a pas
     * été radiée
     * </p>
     * <p>
     * L'abstraction est là car {@link APGenererCompensationsProcess004} (la nouvelle implementation prenant en compte
     * ce mandat) est une copie de {@link APGenererCompensationsProcess002} à cela près que 002 ne prend pas en compte
     * la compensation automatique de section. Cela évite donc de tout récrire (002 retournant <code>false</code> dans
     * tous les cas).
     * </p>
     * 
     * @param sectionEtCompteAnnexe
     *            une section non soldée, ainsi que son compte annexe
     * @return <code>true</code> si la section doit être compensée automatiquement (voir spéc. Inforom 463)
     */
    protected abstract boolean isSectionACompenserAutomatiquement(SectionEtCompteAnnexe sectionEtCompteAnnexe);

    /**
     * <p>
     * Inforom 463
     * </p>
     * 
     * <p>
     * Défini si une section est au contentieux et n'a pas passé l'étape "Poursuite envoyée" ou que la poursuite est
     * radiée
     * </p>
     * <p>
     * L'abstraction est là car {@link APGenererCompensationsProcess004} (la nouvelle implementation prenant en compte
     * ce mandat) est une copie de {@link APGenererCompensationsProcess002} à cela près que 002 ne prend pas en compte
     * la compensation automatique de section. Cela évite donc de tout récrire (002 retournant <code>false</code> dans
     * tous les cas).
     * </p>
     * 
     * @param sectionEtCompteAnnexe
     *            une section non soldée, ainsi que son compte annexe
     * @return <code>true</code> si la section doit être vérifiée par l'utilisateur pour compensation (voir spéc.
     *         Inforom 463)
     */
    protected abstract boolean isSectionAVerifier(SectionEtCompteAnnexe sectionEtCompteAnnexe);

    protected boolean isTiersRequerantActif(PRTiersWrapper tiers) {
        if ("true".equalsIgnoreCase(tiers.getProperty(PRTiersWrapper.PROPERTY_INACTIF))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    @Override
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    @Override
    public void setMoisPeriodeFacturation(String string) {
        moisPeriodeFacturation = string;
    }
}
