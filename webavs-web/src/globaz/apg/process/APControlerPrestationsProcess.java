/*
 * Créé le 2 juin 05
 */
package globaz.apg.process;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APDroitAPGJointLAPGJointPrestationManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationsControlees;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.itext.APListePrestationsAPGControlees;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APControlerPrestationsProcess extends BProcess {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Gestion des tiers en erreur
     * 
     * @author bsc
     * 
     *         Pour changer le modèle de ce commentaire de type généré, allez à :
     *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
     */
    private class TiersPrestationOnError {
        public boolean hasAdresseCourierError = false;
        public boolean hasAdressePaiementError = false;
        public boolean hasEtatCivilError = false;
        public boolean hasTiersInconnu = false;
        public String idTiers = "";
        private List prestations = new ArrayList();

        public TiersPrestationOnError(String idTiers) {
            this.idTiers = idTiers;
        }

        public void addPrestation(String idPrestation) {
            prestations.add(idPrestation);
        }

        public String getKey() {
            return idTiers;
        }

        public boolean hasPrestation(String idPrestation) {
            return prestations.contains(idPrestation);
        }
    }

    private Boolean controlerPrestations = Boolean.TRUE;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private Boolean imprimerListeDroits = Boolean.TRUE;

    /**
     * Crée une nouvelle instance de la classe APControlerPrestationsProcess.
     */
    public APControlerPrestationsProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APControlerPrestationsProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APControlerPrestationsProcess(BProcess parent) {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APControlerPrestationsProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APControlerPrestationsProcess(BSession session) {
        super(session);
    }

    /**
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        BSession session = getSession();
        BTransaction transaction = getTransaction();

        if (controlerPrestations.booleanValue()) {
            // manager chargé de trouver les prestations validées qui sont
            // issues d'un droit APG en état attente ou
            // partiel
            APDroitAPGJointLAPGJointPrestationManager mgr = new APDroitAPGJointLAPGJointPrestationManager();
            mgr.setSession(session);
            mgr.setForEtatPrestation(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            mgr.setForEtatDroitDifferentDe(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);

            // pour memoriser les tiers en erreur
            Map tiersWithError = new HashMap();

            try {
                APPrestation entity;
                BStatement statement = mgr.cursorOpen(transaction);

                while ((entity = (APPrestation) (mgr.cursorReadNext(statement))) != null) {
                    // mise en etat controlé de la prestation et mise a jour de
                    // la date de contrôle.
                    boolean isErrorControle = false;

                    // contrôle si adresse de pmt et adresse de courrier
                    APRepartitionJointPrestationManager repartitionJointPrestationManager = new APRepartitionJointPrestationManager();
                    repartitionJointPrestationManager.setSession(getSession());
                    repartitionJointPrestationManager.setForIdPrestation(entity.getIdPrestationApg());

                    APRepartitionJointPrestation repartitionJointPrestation = null;
                    repartitionJointPrestationManager.find(BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < repartitionJointPrestationManager.size(); i++) {
                        repartitionJointPrestation = (APRepartitionJointPrestation) repartitionJointPrestationManager
                                .getEntity(i);

                        isErrorControle = false;

                        // le droit correspondant
                        APDroitLAPG droitLAPG = new APDroitLAPG();
                        droitLAPG.setSession(getSession());
                        droitLAPG.setIdDroit(repartitionJointPrestation.getIdDroit());
                        droitLAPG.retrieve();

                        int nbTiersNonTrouve = 0;

                        // le tiers correspondant
                        PRTiersWrapper tiersWrapper = null;

                        /*
                         * Test pour les adresses de paiement absente
                         * 
                         * On génère une erreur uniquement dans le cas où la personne reçoit quelque chose !
                         * 
                         * 2) Si total des ventilations = montant net 3) Si adresse de paiement = null
                         * 
                         * --> > Si 3 && !2 => KO > Sinon, OK
                         */

                        // unique test
                        FWCurrency montantTotalVentilations = new FWCurrency();

                        APRepartitionJointPrestationManager mgr2 = new APRepartitionJointPrestationManager();
                        mgr2.setSession(getSession());
                        mgr2.setForIdParent(repartitionJointPrestation.getIdRepartitionBeneficiairePaiement());
                        mgr2.find();

                        for (Iterator iterator = mgr2.iterator(); iterator.hasNext();) {
                            APRepartitionJointPrestation entity1 = (APRepartitionJointPrestation) iterator.next();

                            montantTotalVentilations.add(entity1.getMontantVentile());

                        }

                        if (repartitionJointPrestation.loadAdressePaiement(null) == null
                                && !montantTotalVentilations.equals(new FWCurrency(repartitionJointPrestation
                                        .getMontantNet()))) {

                            isErrorControle = true;

                            if (tiersWrapper == null) {
                                tiersWrapper = droitLAPG.loadDemande().loadTiers();
                            }

                            if (tiersWrapper == null) {
                                nbTiersNonTrouve++;
                                String idTiers = "";
                                PRDemande demande = droitLAPG.loadDemande();
                                idTiers = demande.getIdTiers();

                                if (tiersWithError.containsKey(idTiers)
                                        && ((TiersPrestationOnError) tiersWithError.get(idTiers)).hasTiersInconnu) {
                                    TiersPrestationOnError tError = (TiersPrestationOnError) tiersWithError
                                            .get(idTiers);
                                    tError.hasTiersInconnu = true;
                                } else {
                                    TiersPrestationOnError tError = new TiersPrestationOnError(idTiers);
                                    tError.hasTiersInconnu = true;
                                    tiersWithError.put(tError.getKey(), tError);
                                    getMemoryLog().logMessage("Tiers non trouvé pour idTiers n° " + idTiers,
                                            FWMessage.AVERTISSEMENT, getSession().getLabel("CONTROLE"));
                                }

                            } else {

                                String noAVS = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                                // on ajoute l'erreur seulement une fois pour
                                // une relation tiers/prestation donne
                                if (!tiersWithError.containsKey(tiersWrapper
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))
                                        || !((TiersPrestationOnError) tiersWithError.get(tiersWrapper
                                                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))).hasAdressePaiementError
                                        || (((TiersPrestationOnError) tiersWithError.get(tiersWrapper
                                                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))).hasAdressePaiementError && !((TiersPrestationOnError) tiersWithError
                                                .get(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS)))
                                                .hasPrestation(repartitionJointPrestation.getIdPrestationApg()))) {

                                    getMemoryLog().logMessage(
                                            java.text.MessageFormat.format(
                                                    getSession().getLabel("ADRESSE_PAIEMENT_ABSENTE"), new Object[] {
                                                            repartitionJointPrestation.getNom(), noAVS,
                                                            repartitionJointPrestation.getIdPrestationApg() }),
                                            FWViewBeanInterface.WARNING, getSession().getLabel("CONTROLE_ERREUR"));

                                    if (!tiersWithError.containsKey(tiersWrapper
                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                                        TiersPrestationOnError tError = new TiersPrestationOnError(
                                                tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                        tError.hasAdressePaiementError = true;
                                        tError.addPrestation(repartitionJointPrestation.getIdPrestationApg());

                                        tiersWithError.put(tError.getKey(), tError);
                                    } else {
                                        TiersPrestationOnError tError = (TiersPrestationOnError) tiersWithError
                                                .get(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                        tError.hasAdressePaiementError = true;
                                        if (!tError.hasPrestation(repartitionJointPrestation.getIdPrestationApg())) {
                                            tError.addPrestation(repartitionJointPrestation.getIdPrestationApg());
                                        }
                                    }
                                }
                            }
                        }

                        // adresse de courrier absente
                        if (JadeStringUtil.isEmpty(PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                                repartitionJointPrestation.getIdTiers(), repartitionJointPrestation.getIdAffilie(),
                                APApplication.CS_DOMAINE_ADRESSE_APG))) {
                            isErrorControle = true;

                            if (tiersWrapper == null) {
                                tiersWrapper = droitLAPG.loadDemande().loadTiers();
                            }

                            if (tiersWrapper == null) {
                                nbTiersNonTrouve++;
                                String idTiers = "";
                                PRDemande demande = droitLAPG.loadDemande();
                                idTiers = demande.getIdTiers();

                                if (tiersWithError.containsKey(idTiers)
                                        && ((TiersPrestationOnError) tiersWithError.get(idTiers)).hasTiersInconnu) {
                                    TiersPrestationOnError tError = (TiersPrestationOnError) tiersWithError
                                            .get(idTiers);
                                    tError.hasTiersInconnu = true;
                                } else {
                                    TiersPrestationOnError tError = new TiersPrestationOnError(idTiers);
                                    tError.hasTiersInconnu = true;
                                    tiersWithError.put(tError.getKey(), tError);
                                    getMemoryLog().logMessage("Tiers non trouvé pour idTiers n° " + idTiers,
                                            FWMessage.AVERTISSEMENT, getSession().getLabel("CONTROLE"));
                                }

                            } else {
                                String noAVS = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                                // on ajoute l'erreur seulement une fois pour un
                                // tiers donne
                                if (!tiersWithError.containsKey(tiersWrapper
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))
                                        || !((TiersPrestationOnError) tiersWithError.get(tiersWrapper
                                                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))).hasAdresseCourierError) {
                                    getMemoryLog().logMessage(
                                            java.text.MessageFormat.format(
                                                    getSession().getLabel("ADRESSE_COURRIER_ABSENTE"), new Object[] {
                                                            repartitionJointPrestation.getNom(), noAVS,
                                                            repartitionJointPrestation.getIdPrestationApg() }),
                                            FWViewBeanInterface.WARNING, getSession().getLabel("CONTROLE_ERREUR"));

                                    if (!tiersWithError.containsKey(tiersWrapper
                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                                        TiersPrestationOnError tError = new TiersPrestationOnError(
                                                tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                        tError.hasAdresseCourierError = true;

                                        tiersWithError.put(tError.getKey(), tError);
                                    } else {
                                        TiersPrestationOnError tError = (TiersPrestationOnError) tiersWithError
                                                .get(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                        tError.hasAdresseCourierError = true;
                                    }
                                }
                            }

                        }

                        // Etat civil absent
                        if (PRTiersHelper.getPersonneAVS(getSession(), droitLAPG.loadDemande().getIdTiers()) == null
                                || JadeStringUtil.isIntegerEmpty(PRTiersHelper.getPersonneAVS(getSession(),
                                        droitLAPG.loadDemande().getIdTiers()).getProperty(
                                        PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL))) {

                            isErrorControle = true;

                            if (tiersWrapper == null) {
                                tiersWrapper = droitLAPG.loadDemande().loadTiers();
                            }

                            if (tiersWrapper == null) {
                                nbTiersNonTrouve++;
                                String idTiers = "";
                                PRDemande demande = droitLAPG.loadDemande();
                                idTiers = demande.getIdTiers();

                                if (tiersWithError.containsKey(idTiers)
                                        && ((TiersPrestationOnError) tiersWithError.get(idTiers)).hasTiersInconnu) {
                                    TiersPrestationOnError tError = (TiersPrestationOnError) tiersWithError
                                            .get(idTiers);
                                    tError.hasTiersInconnu = true;
                                } else {
                                    TiersPrestationOnError tError = new TiersPrestationOnError(idTiers);
                                    tError.hasTiersInconnu = true;
                                    tiersWithError.put(tError.getKey(), tError);
                                    getMemoryLog().logMessage("Tiers non trouvé pour idTiers n° " + idTiers,
                                            FWMessage.AVERTISSEMENT, getSession().getLabel("CONTROLE"));
                                }

                            } else {
                                String noAVS = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                                // on ajoute l'erreur seulement une fois pour un
                                // tiers donne
                                if (!tiersWithError.containsKey(tiersWrapper
                                        .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))
                                        || !((TiersPrestationOnError) tiersWithError.get(tiersWrapper
                                                .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))).hasEtatCivilError) {
                                    getMemoryLog().logMessage(
                                            java.text.MessageFormat.format(getSession().getLabel("ETAT_CIVIL_ABSENT"),
                                                    new Object[] { repartitionJointPrestation.getNom(), noAVS,
                                                            repartitionJointPrestation.getIdPrestationApg() }),
                                            FWViewBeanInterface.WARNING, getSession().getLabel("CONTROLE_ERREUR"));

                                    if (!tiersWithError.containsKey(tiersWrapper
                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                                        TiersPrestationOnError tError = new TiersPrestationOnError(
                                                tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                        tError.hasEtatCivilError = true;

                                        tiersWithError.put(tError.getKey(), tError);
                                    } else {
                                        TiersPrestationOnError tError = (TiersPrestationOnError) tiersWithError
                                                .get(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                        tError.hasEtatCivilError = true;
                                    }
                                }
                            }
                        }
                    }

                    if (!isErrorControle) {
                        entity.setEtat(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
                        entity.setDateControle(JACalendar.todayJJsMMsAAAA());
                        entity.update(transaction);
                        getMemoryLog().logMessage(
                                getSession().getLabel("PRESTATION") + " " + entity.getIdPrestationApg() + " "
                                        + getSession().getLabel("CONTROLE"), FWMessage.INFORMATION,
                                getSession().getLabel("CONTROLE_PRESTATION"));
                    }
                }

                mgr.cursorClose(statement);

                getMemoryLog().logMessage("mise en controle terminée...", "", "controle");

            } catch (Exception e) {
                _addError(transaction, e.toString());
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("CONTROLE_PRESTATION"));

                return false;
            }

            try {
                if (transaction.hasErrors()) {
                    transaction.setRollbackOnly();
                }
                transaction.commit();
            } catch (Exception e2) {
                getMemoryLog().logMessage(e2.getMessage(), "", "");
            }
        } // fin if(controlerPrestation.booleanValue())

        // si la case impression est cochée, création d'un PDF et impression
        if (getImprimerListeDroits().booleanValue()) {

            try {
                APListePrestationsAPGControlees liste = new APListePrestationsAPGControlees(session);
                liste.setEMailAddress(getEMailAddress());
                liste.setSession(session);
                liste.setOrderBy(APPrestationsControlees.FIELDNAME_NOM + ", "
                        + APPrestationsControlees.FIELDNAME_PRENOM);
                liste.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);

                liste.setParent(this);
                liste.executeProcess();

            } catch (Exception e) {
                _addError(transaction, e.getMessage());
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("CONTROLE_PRESTATION"));
                return false;
            }
        }
        return true;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * getter pour l'attribut controler prestations
     * 
     * @return la valeur courante de l'attribut controler prestations
     */
    public Boolean getControlerPrestations() {
        return controlerPrestations;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("CONTROLE_PRESTATION");
    }

    /**
     * getter pour l'attribut imprimer liste droits
     * 
     * @return la valeur courante de l'attribut imprimer liste droits
     */
    public Boolean getImprimerListeDroits() {
        return imprimerListeDroits;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * setter pour l'attribut controler prestations
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setControlerPrestations(Boolean boolean1) {
        controlerPrestations = boolean1;
    }

    /**
     * setter pour l'attribut imprimer liste droits
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setImprimerListeDroits(Boolean boolean1) {
        imprimerListeDroits = boolean1;
    }
}
