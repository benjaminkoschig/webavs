/*
 * Créé le 11 juil. 05
 */
package globaz.ij.impl.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.api.process.IIJGenererCompensationProcess;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJCompensation;
import globaz.ij.db.lots.IJCompensationManager;
import globaz.ij.db.lots.IJFactureACompenser;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJRepartitionJointPrestation;
import globaz.ij.db.prestations.IJRepartitionJointPrestationManager;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsJointEmployeur;
import globaz.ij.db.prestations.IJRepartitionPaiementsJointEmployeurManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.process.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <H1>Process de la génération des compensations</H1>
 * 
 * Traitement spécial :
 * 
 * Si pmt à l'affilié et affilié non radié (sans date de fin), compensation sur factures futures Sinon, proposition de
 * compensations sur factures existantes. La case compensé n'est pas cochées.
 * 
 * bsc
 * 
 * 
 * @author dvh
 */
public class IJGenererCompensationsProcess003 extends BProcess implements IIJGenererCompensationProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = "";
    private String moisPeriodeFacturation = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererCompensationsProcess.
     */
    public IJGenererCompensationsProcess003() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererCompensationsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */

    public IJGenererCompensationsProcess003(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererCompensationsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJGenererCompensationsProcess003(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        try {
            // Il faut que toutes les répartitions de paiement aient une adresse
            // de paiement et une adresse de courier.
            // si tout a ete reparti (dans le cas d'un paiement a l'employeur)
            // pas besoin des adresses de paiement et de courier
            // si tout a ete ventille (dans le cas de dettes) pas besoin
            // d'adresse de paiement
            IJRepartitionJointPrestationManager repartitionJointPrestationManager = new IJRepartitionJointPrestationManager();
            repartitionJointPrestationManager.setSession(getSession());
            repartitionJointPrestationManager.setForIdLot(forIdLot);

            IJRepartitionJointPrestation repartitionJointPrestation = null;
            repartitionJointPrestationManager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < repartitionJointPrestationManager.size(); i++) {
                repartitionJointPrestation = (IJRepartitionJointPrestation) repartitionJointPrestationManager
                        .getEntity(i);

                /*
                 * Test pour les adresses de paiement absente
                 * 
                 * On génère une erreur uniquement dans le cas où la personne reçoit quelque chose !
                 * 
                 * 1) Si montant net = 0 2) Si total des ventilations = montant net 3) Si adresse de paiement = null
                 * 
                 * --> > Si 3 && !1 => KO > Si 3 && !2 => KO > Sinon, OK
                 */

                IJBaseIndemnisation baseIndemnisation = new IJBaseIndemnisation();

                baseIndemnisation.setSession(getSession());
                baseIndemnisation.setIdBaseIndemisation(repartitionJointPrestation.getIdBaseIndemnisation());
                baseIndemnisation.retrieve(transaction);

                IJPrononce prononce = new IJPrononce();

                prononce.setSession(getSession());
                prononce.setIdPrononce((baseIndemnisation).getIdPrononce());
                prononce.retrieve(transaction);

                PRTiersWrapper tw = prononce.loadDemande(transaction).loadTiers();

                if (!isTiersRequerantActif(tw)) {

                    getMemoryLog().logMessage(
                            java.text.MessageFormat.format(
                                    getSession().getLabel("TIERS_INACTIF") + " "
                                            + tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                    new Object[] { repartitionJointPrestation.getNom(),
                                            tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                            repartitionJointPrestation.getIdPrestation() }), FWMessage.ERREUR,
                            getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }

                // 1er test
                if (repartitionJointPrestation.loadAdressePaiement(null) == null
                        && !new FWCurrency(repartitionJointPrestation.getMontantNet()).isZero()) {

                    // 2ème test
                    FWCurrency montantTotalVentilations = new FWCurrency();

                    IJRepartitionJointPrestationManager mgr = new IJRepartitionJointPrestationManager();
                    mgr.setSession(getSession());
                    mgr.setForIdParent(repartitionJointPrestation.getIdRepartitionPaiement());
                    mgr.find();

                    for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                        IJRepartitionJointPrestation entity = (IJRepartitionJointPrestation) iterator.next();

                        montantTotalVentilations.add(entity.getMontantVentile());

                    }

                    if (repartitionJointPrestation.loadAdressePaiement(null) == null
                            && !montantTotalVentilations.equals(new FWCurrency(repartitionJointPrestation
                                    .getMontantNet()))) {

                        String noAVS = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                        getMemoryLog().logMessage(
                                MessageFormat.format(getSession().getLabel("ADRESSE_PAIEMENT_IJ_ABSENTE"),
                                        new Object[] { repartitionJointPrestation.getNom(), noAVS,
                                                repartitionJointPrestation.getIdPrestation() }), FWMessage.ERREUR,
                                getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));

                    }
                }

                // adresse de courrier absente
                if (JadeStringUtil.isEmpty(PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                        repartitionJointPrestation.getIdTiers(), repartitionJointPrestation.getIdAffilie(),
                        IJApplication.CS_DOMAINE_ADRESSE_IJAI))

                        &&

                        ((Float.parseFloat(repartitionJointPrestation.getMontantBrut()) != 0) || (Float
                                .parseFloat(repartitionJointPrestation.getMontantBrut()) == 0 && Float
                                .parseFloat(repartitionJointPrestation.getMontantVentile()) != 0))) {

                    String noAVS = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    getMemoryLog().logMessage(
                            MessageFormat.format(getSession().getLabel("ADRESSE_COURRIER_IJ_ABSENTE"),
                                    new Object[] { repartitionJointPrestation.getNom(), noAVS,
                                            repartitionJointPrestation.getIdPrestation() }), FWMessage.ERREUR,
                            getSession().getLabel("PROCESS_GENERER_COMPENSATIONS"));
                }
            }

            if (!getMemoryLog().isOnErrorLevel() && !getMemoryLog().isOnFatalLevel()) {
                // Suppression des compensations ratachées à ce lot
                IJCompensationManager compensationManager = new IJCompensationManager();
                compensationManager.setForIdLot(forIdLot);
                compensationManager.setSession(session);
                compensationManager.find(transaction, BManager.SIZE_NOLIMIT);

                for (int i = 0; i < compensationManager.size(); i++) {
                    IJCompensation compensation = (IJCompensation) compensationManager.getEntity(i);
                    compensation.delete(transaction);
                }

                compensationManager = null;

                // parcours des répartitions parentes de ce lot pour calculer la
                // somme des montants regroupés par tiers affilié
                // adresse de paiement et departement
                // Creation dans le même temps d'une map contenant en clef un
                // idTiers et en valeur un montant, pour
                // avoir la somme pour un idTiers

                IJRepartitionPaiementsJointEmployeurManager repartitionPaiementsJointEmployeurManager = new IJRepartitionPaiementsJointEmployeurManager();
                repartitionPaiementsJointEmployeurManager.setForIdLot(forIdLot);
                repartitionPaiementsJointEmployeurManager.setSession(session);
                repartitionPaiementsJointEmployeurManager.setOrderBy(IJRepartitionPaiements.FIELDNAME_IDTIERS);
                repartitionPaiementsJointEmployeurManager.setForParentOnly("true");

                BStatement statement = repartitionPaiementsJointEmployeurManager.cursorOpen(transaction);
                IJRepartitionPaiementsJointEmployeur repartitionPaiementsJointEmployeur = null;

                SortedMap sommes = new TreeMap();
                Map sommePourUnTiers = new HashMap();

                while ((repartitionPaiementsJointEmployeur = (IJRepartitionPaiementsJointEmployeur) repartitionPaiementsJointEmployeurManager
                        .cursorReadNext(statement)) != null) {

                    // Si des répartitions on un montant égal à zéro, on ne les
                    // prends pas en compte.
                    // Ce cas est possible si on verse la totalité de la
                    // prestations à l'employeur.
                    // L'assuré n'est pas supprimé des répartitions, mais à un
                    // montant égal à zéro
                    if (JadeStringUtil.isIntegerEmpty(repartitionPaiementsJointEmployeur.getMontantBrut())) {
                        continue;
                    }

                    IJPrononce prononce = new IJPrononce();

                    prononce.setSession(getSession());
                    prononce.setIdPrononce(repartitionPaiementsJointEmployeur.getIdPrononce());
                    prononce.retrieve(transaction);

                    String idTiersAssure = prononce.loadDemande(transaction).loadTiers()
                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

                    // Si des répartitions porte sur un employeur non affilié,
                    // on ne le prends pas en compte
                    if (JadeStringUtil.isIntegerEmpty(repartitionPaiementsJointEmployeur.getIdAffilie())
                            && !idTiersAssure.equals(repartitionPaiementsJointEmployeur.getIdTiers())) {

                        continue;
                    }

                    Key key = new Key(repartitionPaiementsJointEmployeur.getIdTiers(),
                            repartitionPaiementsJointEmployeur.getIdAffilie(), "0",
                            repartitionPaiementsJointEmployeur.getIdParticularite());

                    // Key key = new
                    // Key(repartitionPaiementsJointEmployeur.getIdTiers(),
                    // repartitionPaiementsJointEmployeur.getIdAffilie(),
                    // repartitionPaiementsJointEmployeur.loadAdressePaiement(null).getIdAdressePaiement(),
                    // repartitionPaiementsJointEmployeur.getIdParticularite());
                    key.idDomaineAdressePaiement = repartitionPaiementsJointEmployeur.getIdDomaineAdressePaiement();
                    key.idTiersAdressePaiement = repartitionPaiementsJointEmployeur.getIdTiersAdressePaiement();

                    // On ne prend que les répartition parente. Les répartitions
                    // enfants (montant ventilé) correspondent à une adresse de
                    // paiement
                    // au niveau comptable. Le bénéficiaire en compta est celui
                    // de la répartition parent, seul l'adresse de paiement est
                    // celle
                    // de la répartition ventilée.
                    // Il n'est donc pas possible de compensé sur une 'adresse
                    // de paiement'.
                    // Le montant total à répartir est donc égal au montant net
                    // du parent moins la sommes des montants ventilés.

                    FWCurrency montantRepartition = new FWCurrency(repartitionPaiementsJointEmployeur.getMontantNet());
                    montantRepartition.sub(getMontantVentile(session,
                            repartitionPaiementsJointEmployeur.getIdRepartitionPaiement(), forIdLot));

                    // if
                    // (JadeStringUtil.isIntegerEmpty(repartitionPaiementsJointEmployeur.getIdParent())){
                    // montantRepartition = new
                    // FWCurrency(repartitionPaiementsJointEmployeur.getMontantRestant());
                    // } else {
                    // montantRepartition = new
                    // FWCurrency(repartitionPaiementsJointEmployeur.getMontantVentile());
                    // }

                    if (sommes.containsKey(key)) {
                        FWCurrency somme = (FWCurrency) sommes.get(key);
                        somme.add(new FWCurrency(montantRepartition.toString()));
                    } else {
                        sommes.put(key, new FWCurrency(montantRepartition.toString()));
                    }

                    if (sommePourUnTiers.containsKey(repartitionPaiementsJointEmployeur.getIdTiers())) {
                        FWCurrency sommePourCeTiers = (FWCurrency) sommePourUnTiers
                                .get(repartitionPaiementsJointEmployeur.getIdTiers());
                        sommePourCeTiers.add(new FWCurrency(montantRepartition.toString()));
                    } else {
                        sommePourUnTiers.put(repartitionPaiementsJointEmployeur.getIdTiers(), new FWCurrency(
                                montantRepartition.toString()));
                    }
                }

                repartitionPaiementsJointEmployeurManager.cursorClose(statement);
                repartitionPaiementsJointEmployeurManager = null;
                repartitionPaiementsJointEmployeur = null;

                APISection sectionEnCours = null;
                Collection propositionsCompensationsPourTiersEnCours = null;
                String idTiersEnCours = null;
                Iterator propositionsCompensationsIterator = null;
                FWCurrency montantDejaCompenseSurSectionEnCours = new FWCurrency(0);

                Set keys = sommes.keySet();
                Iterator iterator = keys.iterator();

                while (iterator.hasNext()) {
                    Key key = (Key) iterator.next();

                    // creation de la compensation
                    IJCompensation compensation = new IJCompensation();
                    compensation.setSession(session);
                    compensation.setIdLot(forIdLot);
                    compensation.setIdTiers(key.idTiers);
                    compensation.setIdAffilie(key.idAffilie);
                    compensation.setMontantTotal(((FWCurrency) sommes.get(key)).toString());
                    compensation.setDettes(getDettes(key.idTiers));
                    compensation.add(transaction);
                    getMemoryLog().logMessage(
                            getSession().getLabel("COMPENSATION_AJOUT") + " : " + compensation.getIdCompensation(), "",
                            "");

                    IAFAffiliation employeur = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
                    employeur.setAffiliationId(key.idAffilie);
                    employeur.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
                    employeur.retrieve(getTransaction());

                    // On considère l'affilié comme radié, même si la date de
                    // fin est dans le futur.
                    // Si isRadie = true, pas de compensation sur facture
                    // future.
                    boolean isRadie = false;
                    try {
                        if (employeur != null && !employeur.isNew()
                                && !JadeStringUtil.isBlankOrZero(employeur.getDateFin())) {
                            isRadie = true;
                        }
                    } catch (Exception e) {
                        ;
                    }

                    String codePeriodiciteAffilie = employeur.getPeriodicite();

                    // transformer getMoisPeriodeFacturation en chiffre du mois
                    String moisFac = getMoisPeriodeFacturation();

                    // Si le payement n'est pas un payement direct
                    if (!JadeStringUtil.isIntegerEmpty(compensation.getIdAffilie())) {

                        IJFactureACompenser factureACompenser = new IJFactureACompenser();
                        factureACompenser.setSession(session);
                        factureACompenser.setIdTiers(compensation.getIdTiers());
                        factureACompenser.setIdAffilie(compensation.getIdAffilie());
                        factureACompenser.setIdCompensationParente(compensation.getIdCompensation());
                        if (isRadie) {
                            factureACompenser.setIsCompense(Boolean.FALSE);
                        } else {
                            factureACompenser.setIsCompense(Boolean.TRUE);
                        }
                        factureACompenser.setMontant(compensation.getMontantTotal());
                        factureACompenser.add(transaction);

                    } else {
                        // faire les compensations comme ancienne manière

                        // Si on n'a pas de section en cours, ou que on est sur
                        // un autre idTiers que l'itération
                        // précédente, on
                        // va créer tout ça
                        if ((sectionEnCours == null) || !key.idTiers.equals(idTiersEnCours)) {

                            idTiersEnCours = key.idTiers;
                            montantDejaCompenseSurSectionEnCours = new FWCurrency(0);
                            propositionsCompensationsPourTiersEnCours = getCollectionSectionsACompenser(key.idTiers,
                                    (FWCurrency) sommePourUnTiers.get(key.idTiers));
                            propositionsCompensationsIterator = propositionsCompensationsPourTiersEnCours.iterator();

                            if (propositionsCompensationsIterator.hasNext()) {
                                sectionEnCours = (CASection) propositionsCompensationsIterator.next();
                            } else {
                                sectionEnCours = null;
                            }
                        }

                        // compensation de la section en cours
                        FWCurrency montantTotalCompense = new FWCurrency(0);

                        if (sectionEnCours != null) {
                            // si on n'a pas encore tout compensé sur la section
                            // en cours
                            if (Math.abs(montantDejaCompenseSurSectionEnCours.doubleValue()) != Math.abs(Double
                                    .parseDouble(sectionEnCours.getSolde()))) {
                                IJFactureACompenser factureACompenser = new IJFactureACompenser();
                                factureACompenser.setSession(session);

                                // on va compenser ce qui reste a compenser sur
                                // cette section si on peu
                                if (Math.abs(Double.parseDouble(compensation.getMontantTotal())) > ((Math.abs(Double
                                        .parseDouble(sectionEnCours.getSolde()))) - Math
                                        .abs(montantDejaCompenseSurSectionEnCours.doubleValue()))) {
                                    // on compense la totalité de ce qui reste
                                    // de la section
                                    FWCurrency montantCompense = new FWCurrency(sectionEnCours.getSolde());
                                    montantCompense.sub(montantDejaCompenseSurSectionEnCours.toString());
                                    factureACompenser.setMontant(montantCompense.toString());
                                    montantTotalCompense.add(new FWCurrency(montantCompense.toString()));
                                    montantDejaCompenseSurSectionEnCours
                                            .add(new FWCurrency(montantCompense.toString()));
                                } else {
                                    // on compense ce que l'on peut
                                    factureACompenser.setMontant(compensation.getMontantTotal());
                                    montantTotalCompense.add(new FWCurrency(compensation.getMontantTotal().toString()));
                                    montantDejaCompenseSurSectionEnCours.add(new FWCurrency(compensation
                                            .getMontantTotal().toString()));
                                }

                                factureACompenser.setIdCompensationParente(compensation.getIdCompensation());
                                factureACompenser.setIdTiers(sectionEnCours.getCompteAnnexe().getIdTiers());
                                factureACompenser.setIdFactureCompta(sectionEnCours.getIdSection());
                                factureACompenser.setNoFacture(sectionEnCours.getIdExterne());
                                factureACompenser.setIsCompense(Boolean.FALSE);

                                factureACompenser.add(transaction);
                            }
                        }

                        // compensation des suivantes, tant qu'on en a a
                        // compenser et qu'on peut encore compenser
                        while (propositionsCompensationsIterator.hasNext()
                                && (Math.abs(montantTotalCompense.doubleValue()) < Math.abs(Double
                                        .parseDouble(compensation.getMontantTotal())))) {
                            sectionEnCours = (CASection) propositionsCompensationsIterator.next();
                            montantDejaCompenseSurSectionEnCours = new FWCurrency(0);

                            IJFactureACompenser factureACompenser = new IJFactureACompenser();
                            factureACompenser.setSession(session);

                            // On regarde si le solde de la section est
                            // supérieur au montant total moins ce qu'on a déjà
                            // compensé. Si oui, on compense ce qu'on peut, si
                            // non, on compense tout.
                            if (Math.abs(Double.parseDouble(compensation.getMontantTotal())) > (Math
                                    .abs(montantTotalCompense.doubleValue()) + Math.abs(Double
                                    .parseDouble(sectionEnCours.getSolde())))) {
                                factureACompenser.setMontant(sectionEnCours.getSolde());
                                montantTotalCompense.add(new FWCurrency(sectionEnCours.getSolde().toString()));
                                montantDejaCompenseSurSectionEnCours.add(new FWCurrency(sectionEnCours.getSolde()
                                        .toString()));
                            } else {
                                FWCurrency montantQuonPeutCompenser = new FWCurrency(compensation.getMontantTotal());
                                montantQuonPeutCompenser.sub(montantTotalCompense.toString());
                                factureACompenser.setMontant(montantQuonPeutCompenser.toString());
                                montantTotalCompense.add(new FWCurrency(montantQuonPeutCompenser.toString()));
                                montantDejaCompenseSurSectionEnCours.add(new FWCurrency(montantQuonPeutCompenser
                                        .toString()));
                            }

                            factureACompenser.setIdCompensationParente(compensation.getIdCompensation());

                            factureACompenser.setIdTiers(sectionEnCours.getCompteAnnexe().getIdTiers());
                            factureACompenser.setIdFactureCompta(sectionEnCours.getIdSection());
                            factureACompenser.setNoFacture(sectionEnCours.getIdExterne());
                            factureACompenser.setIsCompense(Boolean.FALSE);

                            factureACompenser.add(transaction);
                            getMemoryLog().logMessage(
                                    getSession().getLabel("FACT_AJOUT") + " : "
                                            + factureACompenser.getIdFactureACompenser(), "", "");

                        }
                    }

                    // mise à jour des repartitions (pour l'idCompensation)
                    repartitionPaiementsJointEmployeurManager = new IJRepartitionPaiementsJointEmployeurManager();
                    repartitionPaiementsJointEmployeurManager.setSession(session);
                    repartitionPaiementsJointEmployeurManager.setForIdLot(forIdLot);
                    repartitionPaiementsJointEmployeurManager.setForIdTiers(key.idTiers);
                    repartitionPaiementsJointEmployeurManager.setForIdAffilie(key.idAffilie);
                    // repartitionPaiementsJointEmployeurManager.setForIdTiersAdressePaiement(key.idTiersAdressePaiement);
                    // repartitionPaiementsJointEmployeurManager.setForIdDomaineAdressePaiement(key.idDomaineAdressePaiement);
                    repartitionPaiementsJointEmployeurManager.setForIdParticularite(key.idExtra2);

                    statement = repartitionPaiementsJointEmployeurManager.cursorOpen(transaction);
                    repartitionPaiementsJointEmployeur = null;

                    IJRepartitionPaiements repartitionPaiements = null;

                    while ((repartitionPaiements = (IJRepartitionPaiements) repartitionPaiementsJointEmployeurManager
                            .cursorReadNext(statement)) != null) {
                        repartitionPaiements.setIdCompensation(compensation.getIdCompensation());
                        repartitionPaiements.wantMiseAJourLot(false);
                        repartitionPaiements.update(transaction);
                        getMemoryLog().logMessage(
                                getSession().getLabel("REPART_MAJ") + " : "
                                        + repartitionPaiements.getIdRepartitionPaiement(), "", "");
                    }

                    repartitionPaiementsJointEmployeurManager = null;
                }

                // mise à jour du lot
                IJLot lot = new IJLot();
                lot.setSession(session);
                lot.setIdLot(forIdLot);
                lot.retrieve(transaction);
                lot.setCsEtat(IIJLot.CS_COMPENSE);
                lot.update(transaction);
            }
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            getMemoryLog().logMessage(e.getMessage(), "", "");
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
        IJLot lot = new IJLot();
        lot.setSession(getSession());
        lot.setIdLot(forIdLot);
        lot.retrieve();

        if (JadeStringUtil.isIntegerEmpty(lot.getIdLot())) {
            _addError(getTransaction(), getSession().getLabel("NO_LOT_REQUIS"));
        }

        if (lot.getCsEtat().equals(IIJLot.CS_VALIDE)) {
            _addError(getTransaction(), getSession().getLabel("LOT_DEJA_VALIDE"));
        }
    }

    /**
     * @param idTiers
     *            l'idTiers de la personne susceptible de devoir compenser des trucs
     * @param montant
     *            montant qu'on peut compenser
     * 
     * @return une Collection de CAPropositionCompensation
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private Collection getCollectionSectionsACompenser(String idTiers, FWCurrency montant) throws Exception {
        BISession sessionOsiris = PRSession.connectSession(getSession(), "OSIRIS");

        Collection compensations;

        if (montant.isZero()) {

            compensations = new LinkedList();
            return compensations;

        } else {

            APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) sessionOsiris
                    .getAPIFor(APIPropositionCompensation.class);
            FWCurrency montantTotalNegate = new FWCurrency(montant.toString());
            montantTotalNegate.negate();

            compensations = propositionCompensation.propositionCompensation(Integer.parseInt(idTiers),
                    montantTotalNegate, APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

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
            montant.add(new FWCurrency(compteAnnexe.getSolde().toString()));
        }

        if (montant.isPositive()) {
            return montant.toString();
        } else {
            return "";
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @return la valeur courante de l'attribut EMail object
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        IJLot lot = new IJLot();
        lot.setSession(getSession());
        lot.setIdLot(forIdLot);
        try {
            lot.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getSession().getLabel("PROCESS_GENERER_COMPENSATIONS")
                + " - "
                + getSession().getLabel("LOT")
                + " : "
                + lot.getDescription()
                + ", "
                + getSession().getLabel("STATUS")
                + " : "
                + ((getMemoryLog().hasErrors() == true) ? getSession().getLabel("ECHEC") : getSession().getLabel(
                        "SUCCES"));
    }

    /**
     * getter pour l'attribut for id lot
     * 
     * @return la valeur courante de l'attribut for id lot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * @return
     */
    public String getMoisPeriodeFacturation() {
        return moisPeriodeFacturation;
    }

    /**
     * 
     * Retourne la somme des montants ventilés d'une répartition à partir du parent.
     * 
     * @param session
     * @param idParent
     * @param idLot
     * @return
     * @throws Exception
     */
    private FWCurrency getMontantVentile(BSession session, String idParent, String idLot) throws Exception {

        FWCurrency result = new FWCurrency();

        // Récupération des montants ventilés
        IJRepartitionPaiementsJointEmployeurManager mgr = new IJRepartitionPaiementsJointEmployeurManager();
        mgr.setForIdLot(forIdLot);
        mgr.setForIdParent(idParent);
        mgr.setForParentOnly("false");
        mgr.setSession(session);
        mgr.find();
        for (int i = 0; i < mgr.size(); i++) {
            IJRepartitionPaiementsJointEmployeur entity = (IJRepartitionPaiementsJointEmployeur) mgr.getEntity(i);
            result.add(new FWCurrency(entity.getMontantVentile()));
        }
        return result;
    }

    protected boolean isTiersRequerantActif(PRTiersWrapper tiers) {

        if ("true".equalsIgnoreCase(tiers.getProperty(PRTiersWrapper.PROPERTY_INACTIF))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ij.external.IIJGenererCompensationProcess#setForIdLot()
     */
    public String setForIdLot() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * setter pour l'attribut for id lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setForIdLot(String string) {
        forIdLot = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ij.external.IIJGenererCompensationProcess#setMoisPeriodeFacturation ()
     */
    public String setMoisPeriodeFacturation() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * @param string
     */
    @Override
    public void setMoisPeriodeFacturation(String string) {
        moisPeriodeFacturation = string;
    }

}
