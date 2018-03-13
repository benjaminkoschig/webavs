package globaz.naos.db.wizard;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.couverture.AFCouverture;
import globaz.naos.db.couverture.AFCouvertureListViewBean;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * La classe définissant les entités nécessaires a la création acccélérée d'une Affiliation(s).
 * 
 * Ne correspond pas à une entité de la DB.
 * 
 * En résumé: - Création de l'entité Affiliation. - Création de l'entité Adhésion - Création de(s) entité(s)
 * PlanAffiliation - Création de(s) entité(s) Cotisation
 * 
 * @author sau
 */
public class AFWizard extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static class AFAdhesionCotisation {
        public AFAdhesion adhesion = new AFAdhesion();
        public List<AFCotisation> cotisationList = new ArrayList<AFCotisation>();
        public List<Boolean> cotisationToAddList = new ArrayList<Boolean>();
        public String planCaisseLabel = new String();
    }

    // Plan Caisse, Adhesion(s) & Cotisations
    private List<?> adhesionCotisationList = new ArrayList<Object>();

    // Affiliation
    private AFAffiliation affiliation = new AFAffiliation();

    // tri sur plan caisse
    private String fromNoCaisse = "";

    // pour pouvoir detecter un changement de tiers via le bouton [...] dans le wizard,
    // et pouvoir mettre à jour les champs de raison social long et court
    private String nomTiersSelection = "";

    // Plan Affiliation
    private List<AFPlanAffiliation> planAffiliationList = new ArrayList<AFPlanAffiliation>();

    /**
     * flag passé a false dans l'action actionAfficherSaisieAffiliation le temps du <i>JSPUtils.setBeanProperties()</i> <br/>
     * permet de s'affranchir de la perte des valeur à la navigation du wizard
     */
    private boolean saveAffiliationProperties = true;

    private boolean isIdeReadOnly = false;
    private boolean isIDEPartage = false;

    private boolean isMessageAnnonceIdeCreationAjouteeToDisplay = false;

    public boolean isMessageAnnonceIdeCreationAjouteeToDisplay() {
        return isMessageAnnonceIdeCreationAjouteeToDisplay;
    }

    // commence par '_' afin de ne pas être prise par jspSetBeanProperties
    public void _setMessageAnnonceIdeCreationAjouteeToDisplay(boolean isMessageAnnonceIdeCreationAjouteeToDisplay) {
        this.isMessageAnnonceIdeCreationAjouteeToDisplay = isMessageAnnonceIdeCreationAjouteeToDisplay;
    }

    public boolean isIdeReadOnly() {
        return isIdeReadOnly;
    }

    public String getNumeroIDESansCHE() {
        return affiliation.getNumeroIDESansCHE();
    }

    // commence par souligné afin de ne pas être prise par jspSetBeanProperties
    public void _setIdeReadOnly(boolean isIdeReadOnly) {
        this.isIdeReadOnly = isIdeReadOnly;
    }

    public boolean isIDEPartage() {
        return isIDEPartage;
    }

    // commence par souligné afin de ne pas être prise par jspSetBeanProperties
    public void _setIDEPartage(boolean isIDEPartage) {
        this.isIDEPartage = isIDEPartage;
    }

    public String getWarningMessageAnnonceIdeCreationNotAdded() {
        return affiliation.getWarningMessageAnnonceIdeCreationNotAdded();
    }

    /**
     * Constructeur de AFAffiliation
     */
    public AFWizard() {
        super();
    }

    /**
     * Retour le nom de la Table.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * Pas utilisé dans ce BEntity.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public void addPlanAffiliationLibelle(java.lang.String newLibelle) {
        AFPlanAffiliation planAffiliation = new AFPlanAffiliation();
        planAffiliation.setLibelle(newLibelle);
        // planAffiliation.setLibelleFacture(newLibelle);
        planAffiliationList.add(planAffiliation);
    }

    /**
     * Ajoute les entités suivantes dans la DB: - Affiliation - Adhésion - PlanAffiliation(s) - Cotisation(s)
     * 
     * @throws Exception
     */
    public void ajouterAffiliation() throws Exception {

        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            // Ajouter l'affiliation
            affiliation.setSession(getSession());
            // retenir les suivi liés
            affiliation.suspendreSuivis();
            affiliation.add(transaction);

            if (!transaction.hasErrors() && !transaction.hasWarnings()) {
                // Ajouter le(s) plan(s) d'affiliation
                for (int i = 0; i < planAffiliationList.size(); i++) {
                    AFPlanAffiliation planAffiliation = planAffiliationList.get(i);
                    planAffiliation.setSession(getSession());
                    planAffiliation.setAffiliationId(affiliation.getAffiliationId());
                    planAffiliation.add(transaction);
                    planAffiliation.getAffiliationId();
                }
            }

            if (!transaction.hasErrors() && !transaction.hasWarnings()) {
                // Ajouter l'adhésion
                for (int i = 0; i < adhesionCotisationList.size(); i++) {
                    AFAdhesionCotisation adhesionCotisation = (AFAdhesionCotisation) adhesionCotisationList.get(i);

                    adhesionCotisation.adhesion.setSession(getSession());
                    adhesionCotisation.adhesion.setAffiliationId(affiliation.getAffiliationId());
                    adhesionCotisation.adhesion.setAddOnlyAdhesion(true);
                    adhesionCotisation.adhesion.add(transaction);

                    if (!transaction.hasErrors() && !transaction.hasWarnings()) {
                        for (int j = 0; j < adhesionCotisation.cotisationToAddList.size(); j++) {

                            if (adhesionCotisation.cotisationToAddList.get(j).booleanValue()) {
                                AFCotisation cotisation = adhesionCotisation.cotisationList.get(j);

                                cotisation.setAdhesionId(adhesionCotisation.adhesion.getAdhesionId());

                                int planAffiliationIndex = JadeStringUtil.parseInt(cotisation.getPlanAffiliationId(),
                                        -1);
                                if (planAffiliationIndex == -1) {
                                    transaction.addErrors("Invalid Index of PlanAffiliation");
                                } else {
                                    String planAffiliationId = planAffiliationList.get(planAffiliationIndex)
                                            .getPlanAffiliationId();

                                    cotisation.setPlanAffiliationId(planAffiliationId);
                                    cotisation.resetLiaisons();
                                    cotisation.add(transaction);
                                }
                            }
                        }
                    }
                }
            }

            if (transaction.hasErrors() || transaction.hasErrors()) {
                // effacer les suivi qui ont été créé avec une autre transaction

                transaction.rollback();
            } else {
                transaction.commit();
                // exécuter les suivi
                affiliation.libererSuivis();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw (e);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
                transaction = null;
            }
        }
    }

    // ***********************************************
    // Getter
    // ***********************************************

    /**
     * Création des cotisation en fonction des paramètres de l'Affiliation et du Plan de Caisse choisi.
     * 
     * @throws Exception
     */
    public void creationCotisation() throws Exception {

        for (int i = 0; i < adhesionCotisationList.size(); i++) {
            AFAdhesionCotisation adhesionCotisations = (AFAdhesionCotisation) adhesionCotisationList.get(i);
            AFAdhesion adhesion = adhesionCotisations.adhesion;

            if ((adhesionCotisations.cotisationList.size() == 0)
                    || !adhesion.getDateDebut().equals(affiliation.getDateDebut())
                    || !adhesion.getDateFin().equals(affiliation.getDateFin())) {

                adhesion.setSession(getSession());
                adhesion.setDateDebut(affiliation.getDateDebut());
                adhesion.setDateFin(affiliation.getDateFin());
                // adhesion.setTypeAdhesion(CodeSystem.TYPE_ADHESION_CAISSE);
                // TODO ajouter notion de caisse principale ou non
                String planCaisseId = adhesion.getPlanCaisseId();

                // Pour chaque Couverture, lister les Assurances
                AFCouvertureListViewBean couvertureList = new AFCouvertureListViewBean();
                couvertureList.setForPlanCaisseId(planCaisseId);
                couvertureList.setSession(getSession());
                couvertureList.find();

                adhesionCotisations.cotisationList.clear();
                adhesionCotisations.cotisationToAddList.clear();
                for (int j = 0; j < couvertureList.size(); j++) {
                    AFCouverture couverture = (AFCouverture) couvertureList.getEntity(j);

                    // Création de la nouvelle cotisation
                    AFCotisation cotisation = new AFCotisation();
                    cotisation.setSession(getSession());
                    cotisation.setPlanCaisseId(planCaisseId);
                    cotisation.setAssuranceId(couverture.getAssuranceId());
                    cotisation.setPlanAffiliationId("0");

                    if (JadeStringUtil.isIntegerEmpty(adhesion.getDateFin())
                            && JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {

                        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), adhesion.getDateDebut(),
                                couverture.getDateDebut())) {
                            cotisation.setDateDebut(adhesion.getDateDebut());
                        } else {
                            cotisation.setDateDebut(couverture.getDateDebut());
                        }
                        cotisation.setDateFin("");
                        cotisation.setMotifFin(CodeSystem.MOTIF_FIN_AUCUN);
                        adhesionCotisations.cotisationList.add(cotisation);

                    } else if (!JadeStringUtil.isIntegerEmpty(adhesion.getDateFin())
                            && !JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {
                        if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(),
                                adhesion.getDateDebut())
                                && BSessionUtil.compareDateBetweenOrEqual(getSession(), adhesion.getDateDebut(),
                                        adhesion.getDateFin(), couverture.getDateFin())) {

                            cotisation.setDateDebut(adhesion.getDateDebut());
                            cotisation.setDateFin(couverture.getDateFin());
                            cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                            adhesionCotisations.cotisationList.add(cotisation);

                        } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), adhesion.getDateDebut(),
                                adhesion.getDateFin(), couverture.getDateDebut())
                                && BSessionUtil.compareDateBetweenOrEqual(getSession(), adhesion.getDateDebut(),
                                        adhesion.getDateFin(), couverture.getDateFin())) {

                            cotisation.setDateDebut(couverture.getDateDebut());
                            cotisation.setDateFin(couverture.getDateFin());
                            cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                            adhesionCotisations.cotisationList.add(cotisation);

                        } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), adhesion.getDateDebut(),
                                adhesion.getDateFin(), couverture.getDateDebut())
                                && BSessionUtil.compareDateFirstLower(getSession(), adhesion.getDateFin(),
                                        couverture.getDateFin())) {

                            cotisation.setDateDebut(couverture.getDateDebut());
                            cotisation.setDateFin(adhesion.getDateFin());
                            cotisation.setMotifFin(affiliation.getMotifFin());
                            adhesionCotisations.cotisationList.add(cotisation);

                        } else if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(),
                                adhesion.getDateDebut())
                                && BSessionUtil.compareDateFirstGreater(getSession(), couverture.getDateFin(),
                                        adhesion.getDateFin())) {

                            cotisation.setDateDebut(adhesion.getDateDebut());
                            cotisation.setDateFin(adhesion.getDateFin());
                            cotisation.setMotifFin(affiliation.getMotifFin());
                            adhesionCotisations.cotisationList.add(cotisation);
                        }

                    } else if (JadeStringUtil.isIntegerEmpty(adhesion.getDateFin())
                            && !JadeStringUtil.isIntegerEmpty(couverture.getDateFin())) {

                        if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(),
                                adhesion.getDateDebut())
                                && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), adhesion.getDateDebut(),
                                        couverture.getDateFin())) {

                            cotisation.setDateDebut(adhesion.getDateDebut());
                            cotisation.setDateFin(couverture.getDateFin());
                            cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                            adhesionCotisations.cotisationList.add(cotisation);

                        } else if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), adhesion.getDateDebut(),
                                couverture.getDateDebut())
                                && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), adhesion.getDateDebut(),
                                        couverture.getDateFin())) {

                            cotisation.setDateDebut(couverture.getDateDebut());
                            cotisation.setDateFin(couverture.getDateFin());
                            cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
                            adhesionCotisations.cotisationList.add(cotisation);
                        }
                    } else {
                        if (BSessionUtil.compareDateFirstLower(getSession(), couverture.getDateDebut(),
                                adhesion.getDateDebut())) {

                            cotisation.setDateDebut(adhesion.getDateDebut());
                            cotisation.setDateFin(adhesion.getDateFin());

                            if (!JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                                    && BSessionUtil.compareDateEqual(getSession(), affiliation.getDateFin(),
                                            getDateFin())) {
                                cotisation.setMotifFin(affiliation.getMotifFin());
                            } else {
                                cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_ADHESION);
                            }
                            adhesionCotisations.cotisationList.add(cotisation);

                        } else if (BSessionUtil.compareDateBetweenOrEqual(getSession(), adhesion.getDateDebut(),
                                adhesion.getDateFin(), couverture.getDateDebut())) {

                            cotisation.setDateDebut(couverture.getDateDebut());
                            cotisation.setDateFin(adhesion.getDateFin());

                            if (!JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                                    && BSessionUtil.compareDateEqual(getSession(), affiliation.getDateFin(),
                                            getDateFin())) {
                                cotisation.setMotifFin(affiliation.getMotifFin());
                            } else {
                                cotisation.setMotifFin(CodeSystem.MOTIF_FIN_FIN_ADHESION);
                            }
                            adhesionCotisations.cotisationList.add(cotisation);
                        }
                    }
                } // end for

                for (int j = 0; j < adhesionCotisations.cotisationList.size(); j++) {
                    adhesionCotisations.cotisationToAddList.add(new Boolean(true));
                }
            } else {
                if (!JadeStringUtil.isIntegerEmpty(affiliation.getMotifFin())) {
                    for (int j = 0; j < adhesionCotisations.cotisationList.size(); j++) {
                        AFCotisation cotisation = adhesionCotisations.cotisationList.get(j);

                        if (!(cotisation.getMotifFin().equals(CodeSystem.MOTIF_FIN_FIN_ADHESION) || cotisation
                                .getMotifFin().equals(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE))) {
                            cotisation.setMotifFin(affiliation.getMotifFin());
                        }
                    }
                }
            }
        }

        for (int i = 0; i < adhesionCotisationList.size(); i++) {
            AFAdhesionCotisation adhesionCotisations = (AFAdhesionCotisation) adhesionCotisationList.get(i);

            for (int j = 0; j < adhesionCotisations.cotisationList.size(); j++) {
                AFCotisation cotisation = adhesionCotisations.cotisationList.get(j);

                if (JadeStringUtil.isEmpty(cotisation.getPeriodicite())
                        && !cotisation.getPeriodicite().equals(affiliation.getPeriodicite())) {
                    cotisation.setPeriodicite(affiliation.getPeriodicite());
                }
            }
        }
    }

    public List<?> getAdhesionCotisationList() {
        return adhesionCotisationList;
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public java.lang.String getAffiliationId() {
        return affiliation.getAffiliationId();
    }

    public java.lang.String getAffilieNumero() {
        return affiliation.getAffilieNumero();
    }

    public java.lang.String getAncienAffilieNumero() {
        return affiliation.getAncienAffilieNumero();
    }

    public java.lang.Boolean getBonusMalus() {
        return affiliation.getBonusMalus();
    }

    public java.lang.String getBrancheEconomique() {
        return affiliation.getBrancheEconomique();
    }

    public java.lang.String getCaissePartance() {
        return affiliation.getCaissePartance();
    }

    public java.lang.String getCaisseProvenance() {
        return affiliation.getCaisseProvenance();
    }

    // public String getIdeDebutAnnonceActive() {
    // return affiliation.getIdeDebutAnnonceActive();
    // }
    //
    // public String getIdeFinAnnonceActive() {
    // return affiliation.getIdeFinAnnonceActive();
    // }
    //
    // public String getIdeDebutAnnoncePassive() {
    // return affiliation.getIdeDebutAnnoncePassive();
    // }
    //
    // public String getIdeFinAnnoncePassive() {
    // return affiliation.getIdeFinAnnoncePassive();
    // }

    public String getIdeStatut() {
        return affiliation.getIdeStatut();
    }

    public String getIdeRaisonSociale() {
        return affiliation.getIdeRaisonSociale();
    }

    public java.lang.String getCategorieNoga() throws Exception {
        if (!JadeStringUtil.isBlank(affiliation.getCodeNoga())) {
            // code, recherche de la catégorie carrespondante
            FWParametersSystemCode child = new FWParametersSystemCode();
            child.setSession(getSession());
            child.setIdCode(affiliation.getCodeNoga());
            child.retrieve();
            FWParametersSystemCode parent = child.getSelection();
            if (parent != null) {
                return parent.getIdCode();
            }
        }
        return "";
    }

    public java.lang.String getCodeFacturation() {
        return affiliation.getCodeFacturation();
    }

    public java.lang.String getCodeNoga() {
        return affiliation.getCodeNoga();
    }

    public FWParametersSystemCodeManager getCSNoga() {
        FWParametersSystemCodeManager mgr = new FWParametersSystemCodeManager();
        mgr.setSession(getSession());
        mgr.getListeCodes("VENOGAVAL", getSession().getIdLangue());

        return mgr;
    }

    public java.lang.String getDateCreation() {
        return affiliation.getDateCreation();
    }

    public java.lang.String getDateDebut() {
        return affiliation.getDateDebut();
    }

    public java.lang.String getDateDemandeAffiliation() {
        return affiliation.getDateDemandeAffiliation();
    }

    public java.lang.String getDateEditionFiche() {
        return affiliation.getDateEditionFiche();
    }

    public java.lang.String getDateEditionFicheM1() {
        return affiliation.getDateEditionFicheM1();
    }

    public java.lang.String getDateEditionFicheM2() {
        return affiliation.getDateEditionFicheM2();
    }

    public java.lang.String getDateFin() {
        return affiliation.getDateFin();
    }

    public java.lang.String getDatePrecDebut() {
        return affiliation.getDatePrecDebut();
    }

    public java.lang.String getDatePrecFin() {
        return affiliation.getDatePrecFin();
    }

    public java.lang.String getDateTent() {
        return affiliation.getDateTent();
    }

    public java.lang.String getDeclarationSalaire() {
        return affiliation.getDeclarationSalaire();
    }

    public Boolean getEnvoiAutomatiqueAnnonceSalaires() {
        return affiliation.getEnvoiAutomatiqueAnnonceSalaires();
    }

    public java.lang.Boolean getEnvoiAutomatiqueLAA() {
        return affiliation.getEnvoiAutomatiqueLAA();
    }

    public java.lang.Boolean getEnvoiAutomatiqueLPP() {
        return affiliation.getEnvoiAutomatiqueLPP();
    }

    /**
     * Retour les "Code System" a exclure pour la sélection du "Motif de fin" d'affiliation.
     * 
     * @return
     */
    public HashSet<?> getExceptMotifFin() {
        return affiliation.getExceptMotifFin();
    }

    public String getFromNoCaisse() {
        return fromNoCaisse;
    }

    public java.lang.String getIdTiers() {
        return affiliation.getIdTiers();
    }

    public java.lang.String getMasseAnnuelle() {
        return affiliation.getMasseAnnuelle();
    }

    public java.lang.String getMassePeriodicite() {
        return affiliation.getMassePeriodicite();
    }

    public java.lang.String getMembreComite() {
        return affiliation.getMembreComite();
    }

    public java.lang.String getMotifCreation() {
        return affiliation.getMotifCreation();
    }

    public java.lang.String getMotifFin() {
        return affiliation.getMotifFin();
    }

    public String getNomTiersSelection() {
        return nomTiersSelection;
    }

    public java.lang.String getNouveauAffilieNumero() {
        return affiliation.getNouveauAffilieNumero();
    }

    public java.lang.String getNumeroIDE() {
        return affiliation.getNumeroIDE();
    }

    public java.lang.String getPeriodicite() {
        return affiliation.getPeriodicite();
    }

    public java.lang.String getPersonnaliteJuridique() {
        return affiliation.getPersonnaliteJuridique();
    }

    public List<AFPlanAffiliation> getPlanAffiliation() {
        return planAffiliationList;
    }

    public List<String[]> getPlanCaisseIdSelected() {
        List<?> adhCotiList = getAdhesionCotisationList();
        List<String[]> result = new ArrayList<String[]>();
        for (int i = 0; i < adhCotiList.size(); i++) {
            AFAdhesionCotisation adhCoti = (AFAdhesionCotisation) adhCotiList.get(i);
            result.add(new String[] { adhCoti.adhesion.getPlanCaisseId(), adhCoti.adhesion.getTypeAdhesion() });
        }
        return result;
    }

    public String getRaisonSociale() {
        return affiliation.getRaisonSociale();
    }

    public String getRaisonSocialeb64() {
        return affiliation.getRaisonSocialeb64();
    }

    public String getActivite() {
        return affiliation.getActivite();
    }

    public String getRaisonSocialeCourt() {
        return affiliation.getRaisonSocialeCourt();
    }

    public List<?> getSuiviCaisseList() {
        return affiliation.getSuiviCaisseList();
    }

    public java.lang.String getTaxeCo2Fraction() {
        return affiliation.getTaxeCo2Fraction();
    }

    public java.lang.String getTaxeCo2Taux() {
        return affiliation.getTaxeCo2Taux();
    }

    /**
     * Rechercher le tiers de l'affiliastion en fonction de son ID.
     * 
     * @return le tiers
     */
    public TITiersViewBean getTiers() {
        return affiliation.getTiers();
    }

    public java.lang.String getTypeAffiliation() {
        return affiliation.getTypeAffiliation();
    }

    public java.lang.String getTypeAssocie() {
        return affiliation.getTypeAssocie();
    }

    public java.lang.Boolean isExonerationGenerale() {
        return affiliation.isExonerationGenerale();
    }

    public java.lang.Boolean isIrrecouvrable() {
        return affiliation.isIrrecouvrable();
    }

    public java.lang.Boolean isLiquidation() {
        return affiliation.isLiquidation();
    }

    public java.lang.Boolean isOccasionnel() {
        return affiliation.isOccasionnel();
    }

    public java.lang.Boolean isPersonnelMaison() {
        return affiliation.isPersonnelMaison();
    }

    public java.lang.Boolean isIdeAnnoncePassive() {
        return affiliation.isIdeAnnoncePassive();
    }

    public java.lang.Boolean isIdeNonAnnoncante() {
        return affiliation.isIdeNonAnnoncante();
    }

    // ***********************************************
    // Setter
    // ***********************************************
    public java.lang.Boolean isReleveParitaire() {
        return affiliation.isReleveParitaire();
    }

    public java.lang.Boolean isRelevePersonnel() {
        return affiliation.isRelevePersonnel();
    }

    public boolean isSaveAffiliationProperties() {
        return saveAffiliationProperties;
    }

    public java.lang.Boolean isTraitement() {
        return affiliation.isTraitement();
    }

    public void saveAffiliationProperties(boolean b) {
        saveAffiliationProperties = b;
    }

    public void setAccesSecurite(String accesSecurite) {
        affiliation.setAccesSecurite(accesSecurite);
    }

    public void setAdhesionCotisationList(List<?> list) {
        adhesionCotisationList = list;
    }

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliation.setAffiliationId(newAffiliationId);
    }

    public void setAffilieNumero(java.lang.String newAffilieNumero) {
        affiliation.setAffilieNumero(newAffilieNumero);
    }

    public void setAncienAffilieNumero(java.lang.String newAncienAffilieNumero) {
        affiliation.setAncienAffilieNumero(newAncienAffilieNumero);
    }

    public void setBonusMalus(java.lang.Boolean newBonusMalus) {
        if (saveAffiliationProperties) {
            affiliation.setBonusMalus(newBonusMalus);
        }
    }

    public void setBrancheEconomique(java.lang.String newBrancheEconomique) {
        affiliation.setBrancheEconomique(newBrancheEconomique);
    }

    public void setCaissePartance(java.lang.String newCaissePartance) {
        affiliation.setCaissePartance(newCaissePartance);
    }

    public void setCaisseProvenance(java.lang.String newCaisseProvenance) {
        affiliation.setCaisseProvenance(newCaisseProvenance);
    }

    public void setCodeFacturation(java.lang.String newTraitement) {
        if (saveAffiliationProperties) {
            affiliation.setCodeFacturation(newTraitement);
        }
    }

    public void setCodeNoga(java.lang.String newCodeNoga) {
        if (saveAffiliationProperties) {
            affiliation.setCodeNoga(newCodeNoga);
        }
    }

    public void setDateCreation(java.lang.String newDateCreation) {
        affiliation.setDateCreation(newDateCreation);
    }

    public void setIdeStatut(java.lang.String newIdeStatut) {
        affiliation.setIdeStatut(newIdeStatut);
    }

    public void setIdeRaisonSociale(java.lang.String newIdeRaisonSociale) {
        affiliation.setIdeRaisonSociale(newIdeRaisonSociale);
    }

    /**
     * conversion Base64 : Une raison sociale peut contenir des caractères non autorisé en get
     * 
     * @param iDE_raisonSociale
     */
    public void setIdeRaisonSocialeb64(String iDE_raisonSocialeb64) {
        try {
            isIDEPartage = AFIDEUtil.hasIDEAllreadyAff(getSession(), getNumeroIDE());
        } catch (Exception e) {
            isIDEPartage = false;
        } finally {
            affiliation.setIdeRaisonSocialeb64(iDE_raisonSocialeb64);
        }
    }

    public void setActivite(String activite) {
        affiliation.setActivite(activite);
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        affiliation.setDateDebut(newDateDebut);
    }

    public void setDateDemandeAffiliation(String string) {
        affiliation.setDateDemandeAffiliation(string);
    }

    public void setDateEditionFiche(java.lang.String newDateEditionFiche) {
        affiliation.setDateEditionFiche(newDateEditionFiche);
    }

    public void setDateEditionFicheM1(java.lang.String newDateEditionFicheM1) {
        affiliation.setDateEditionFicheM1(newDateEditionFicheM1);
    }

    public void setDateEditionFicheM2(java.lang.String newDateEditionFicheM2) {
        affiliation.setDateEditionFicheM2(newDateEditionFicheM2);
    }

    public void setDateFin(java.lang.String newDateFin) {
        affiliation.setDateFin(newDateFin);
    }

    public void setDatePrecDebut(java.lang.String newDatePrecDebut) {
        affiliation.setDatePrecDebut(newDatePrecDebut);
    }

    public void setDatePrecFin(java.lang.String newDatePrecFin) {
        affiliation.setDatePrecFin(newDatePrecFin);
    }

    public void setDateTent(java.lang.String newDateTent) {
        affiliation.setDateTent(newDateTent);
    }

    public void setDeclarationSalaire(java.lang.String newDeclarationSalaire) {
        affiliation.setDeclarationSalaire(newDeclarationSalaire);
    }

    public void setEnvoiAutomatiqueAnnonceSalaires(Boolean envoi) {
        affiliation.setEnvoiAutomatiqueAnnonceSalaires(envoi);
    }

    public void setEnvoiAutomatiqueLAA(java.lang.Boolean boolean1) {
        if (saveAffiliationProperties) {
            affiliation.setEnvoiAutomatiqueLAA(boolean1);
        }
    }

    public void setEnvoiAutomatiqueLPP(java.lang.Boolean boolean1) {
        if (saveAffiliationProperties) {
            affiliation.setEnvoiAutomatiqueLPP(boolean1);
        }
    }

    public void setExonerationGenerale(java.lang.Boolean newExonerationGenerale) {
        if (saveAffiliationProperties) {
            affiliation.setExonerationGenerale(newExonerationGenerale);
        }
    }

    public void setFromNoCaisse(String fromNoCaisse) {
        this.fromNoCaisse = fromNoCaisse;
    }

    public void setIdTiers(java.lang.String newIdTiers) {
        affiliation.setIdTiers(newIdTiers);
    }

    public void setIrrecouvrable(java.lang.Boolean newIrrecouvrable) {
        if (saveAffiliationProperties) {
            affiliation.setIrrecouvrable(newIrrecouvrable);
        }
    }

    public void setLiquidation(java.lang.Boolean newLiquidation) {
        if (saveAffiliationProperties) {
            affiliation.setLiquidation(newLiquidation);
        }
    }

    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle) {
        affiliation.setMasseAnnuelle(newMasseAnnuelle);
    }

    public void setMassePeriodicite(java.lang.String newMassePeriodicite) {
        affiliation.setMassePeriodicite(newMassePeriodicite);
    }

    public void setMembreComite(java.lang.String newMembreComite) {
        affiliation.setMembreComite(newMembreComite);
    }

    public void setMotifCreation(java.lang.String newMotifCreation) {
        affiliation.setMotifCreation(newMotifCreation);
    }

    public void setMotifFin(java.lang.String newMotifFin) {
        affiliation.setMotifFin(newMotifFin);
    }

    public void setNomTiersSelection(String nomTiersSelection) {
        this.nomTiersSelection = nomTiersSelection;
    }

    public void setNumeroIDE(java.lang.String newNumeroIDE) {
        affiliation.setNumeroIDE(newNumeroIDE);
    }

    public void setOccasionnel(java.lang.Boolean newOccasionnel) {
        if (saveAffiliationProperties) {
            affiliation.setOccasionnel(newOccasionnel);
        }
    }

    public void setPeriodicite(java.lang.String newPeriodicite) {
        affiliation.setPeriodicite(newPeriodicite);
    }

    public void setPersonnaliteJuridique(java.lang.String newPersonnaliteJuridique) {
        affiliation.setPersonnaliteJuridique(newPersonnaliteJuridique);
    }

    public void setPersonnelMaison(java.lang.Boolean newPersonnelMaison) {
        if (saveAffiliationProperties) {
            affiliation.setPersonnelMaison(newPersonnelMaison);
        }
    }

    public void setRaisonSociale(String string) {
        affiliation.setRaisonSociale(string);
    }

    public String getIdAnnonceIdeCreationLiee() {
        return affiliation.getIdAnnonceIdeCreationLiee();
    }

    public void setIdAnnonceIdeCreationLiee(String idAnnonceIdeCreationLiee) {
        affiliation.setIdAnnonceIdeCreationLiee(idAnnonceIdeCreationLiee);
    }

    public void setRaisonSocialeCourt(String string) {
        affiliation.setRaisonSocialeCourt(string);
    }

    public void setReleveParitaire(java.lang.Boolean newReleveParitaire) {
        if (saveAffiliationProperties) {
            affiliation.setReleveParitaire(newReleveParitaire);
        }
    }

    public void setRelevePersonnel(java.lang.Boolean newRelevePersonnel) {
        if (saveAffiliationProperties) {
            affiliation.setRelevePersonnel(newRelevePersonnel);
        }
    }

    /**
     * Modifie la session en cours.
     * 
     * @see globaz.globall.db.BAccessBean#setSession(globaz.globall.db.BSession)
     */
    @Override
    public void setSession(BSession newSession) {
        affiliation.setSession(newSession);
        super.setSession(newSession);
    }

    public void setTaxeCo2Fraction(java.lang.String newTaxeCo2Fraction) {
        affiliation.setTaxeCo2Fraction(newTaxeCo2Fraction);
    }

    public void setTaxeCo2Taux(java.lang.String newTaxeCo2Taux) {
        affiliation.setTaxeCo2Taux(newTaxeCo2Taux);
    }

    public void setTraitement(java.lang.Boolean newTraitement) {
        if (saveAffiliationProperties) {
            affiliation.setTraitement(newTraitement);
        }
    }

    public void setTypeAffiliation(java.lang.String newTypeAffiliation) {
        affiliation.setTypeAffiliation(newTypeAffiliation);
    }

    public void setTypeAssocie(java.lang.String newTypeAssocie) {
        if (saveAffiliationProperties) {
            affiliation.setTypeAssocie(newTypeAssocie);
        }
    }

    public void setIdeAnnoncePassive(java.lang.Boolean ideAnnoncePassive) {
        if (saveAffiliationProperties) {
            affiliation.setIdeAnnoncePassive(ideAnnoncePassive);
        }
    }

    public void setIdeNonAnnoncante(java.lang.Boolean ideNonAnnoncante) {
        if (saveAffiliationProperties) {
            affiliation.setIdeNonAnnoncante(ideNonAnnoncante);
        }
    }

    public void setConvention(String convention) {
        affiliation.setConvention(convention);
    }

    public String getConvention() {
        return affiliation.getConvention();
    }

    /**
     * Contrôle si les champs obligatoires de l'affiliation sont renseignés et valides.
     * 
     * @return Une String vide si les champs obligatoires sont renseignés et valides ou Une String contenant les erreurs
     * @throws Exception
     */
    public String validateAffiliation() throws Exception {
        return affiliation.validationMandatory();
    }
}
