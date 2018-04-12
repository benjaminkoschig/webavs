package globaz.phenix.db.principale;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.interfaces.IDecision;
import globaz.phenix.process.CPProcessMiseAjourComFisc;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;

public class CPDecision extends globaz.globall.db.BEntity implements IDecision, java.io.Serializable {

    private static final long serialVersionUID = 1395512630014474070L;
    public final static java.lang.String CS_ACOMPTE = "605007";
    public final static java.lang.String CS_ACT_LUCRATIVE = "602007";
    public final static java.lang.String CS_ACT_NON_LUCRATIVE = "602008";

    public final static java.lang.String CS_AGRICULTEUR = "602006";
    public final static String CS_ANNULEE = "604008";
    public final static String CS_CALCUL = "604002";
    public final static java.lang.String CS_CORRECTION = "605003";
    public final static String CS_CREATION = "604001";
    public final static java.lang.String CS_DEFINITIVE = "605002";
    public final static String CS_ETUDIANT = "602009";
    public final static String CS_FACTURATION = "604004";
    public final static String CS_FICHIER_CENTRAL = "602010";
    public final static java.lang.String CS_FRANCHISE = "609024";
    public final static java.lang.String CS_IMPUTATION = "605005";
    // Constantes
    public final static java.lang.String CS_INDEPENDANT = "602001";
    public final static java.lang.String CS_LETTRE_COUPLE = "609022";
    public final static java.lang.String CS_NON_ACTIF = "602002";
    public final static java.lang.String CS_NON_SOUMIS = "602005";
    public final static java.lang.String CS_OPPOSITION = "609021";
    public final static String CS_PB_COMPTABILISATION = "604009";
    // TypeDecision
    public final static java.lang.String CS_PROVISOIRE = "605001";
    public final static String CS_RECOURS = "609023";
    public final static java.lang.String CS_RECTIFICATION = "605004";
    public final static String CS_REDUCTION = "605009";
    public final static String CS_REMISE = "605008";
    public final static String CS_REMPLACEE = "604007";
    public final static java.lang.String CS_RENTIER = "602004";
    public final static String CS_REPRISE = "604005";
    // Spécification
    // public final static java.lang.String CS_SALARIE = "609002";
    public final static java.lang.String CS_SALARIE_DISPENSE = "609020";
    public final static String CS_SORTIE = "604006";
    public final static java.lang.String CS_TSE = "602003";
    public final static String CS_VALIDATION = "604003";
    public final static java.lang.String FIELD_EBIPAS = "EBIPAS";
    public final static java.lang.String FIELD_HTICJT = "HTICJT";
    public final static java.lang.String FIELD_HTITIE = "HTITIE";
    public final static java.lang.String FIELD_IAACTI = "IAACTI";
    public final static java.lang.String FIELD_IAANNE = "IAANNE";

    public final static java.lang.String FIELD_IAAPRI = "IAAPRI";
    public final static java.lang.String FIELD_IAASSU = "IAASSU";
    public final static java.lang.String FIELD_IABCOM = "IABCOM";
    public final static java.lang.String FIELD_IABIMP = "IABIMP";
    public final static java.lang.String FIELD_IABPRO = "IABPRO";
    public final static java.lang.String FIELD_IABSIG = "IABSIG";

    public final static java.lang.String FIELD_IACBLO = "IACBLO";
    public final static java.lang.String FIELD_IACINT = "IACINT";
    public final static java.lang.String FIELD_IADDEB = "IADDEB";
    public final static java.lang.String FIELD_IADFIN = "IADFIN";
    public final static java.lang.String FIELD_IADINF = "IADINF";
    public final static java.lang.String FIELD_IAFACT = "IAFACT";
    // Constantes statiques pour les champs de CPDECIP
    public final static java.lang.String FIELD_IAIDEC = "IAIDEC";
    public final static java.lang.String FIELD_IAOPPO = "IAOPPO";
    public final static java.lang.String FIELD_IARESP = "IARESP";
    public final static java.lang.String FIELD_IATAXA = "IATAXA";

    public final static java.lang.String FIELD_IATETA = "IATETA";
    public final static java.lang.String FIELD_IATGAF = "IATGAF";
    public final static java.lang.String FIELD_IATSPE = "IATSPE";
    public final static java.lang.String FIELD_IATTDE = "IATTDE";
    public final static java.lang.String FIELD_IBIDCF = "IBIDCF";
    public final static java.lang.String FIELD_ICIIFD = "ICIIFD";
    public final static java.lang.String FIELD_ICIIFP = "ICIIFP";
    public final static java.lang.String FIELD_MAIAFF = "MAIAFF";
    public final static java.lang.String FIELD_EBIDDP = "EBIDDP";
    // Constante statique pour la table
    public final static java.lang.String TABLE_CPDECIP = "CPDECIP";

    // IAACTI décision active
    private Boolean active = Boolean.FALSE;
    private AFAffiliation affiliation = null;
    private java.lang.String anneeDecision = "";
    private java.lang.String anneePrise = "";
    private Boolean bloque = Boolean.FALSE;
    private CPCommunicationFiscale communication = null;
    private Boolean complementaire = Boolean.FALSE;
    private Boolean cotiMinimumPayeEnSalarie = Boolean.FALSE;
    private java.lang.String dateFacturation = "";
    private java.lang.String dateInformation = "";
    private Boolean debutActivite = Boolean.FALSE;
    private java.lang.String debutDecision = "";
    private java.lang.String dernierEtat = "";
    private Boolean division2 = Boolean.FALSE;
    private Boolean facturation = Boolean.FALSE;
    private java.lang.String finDecision = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String idAffiliation = "";
    private java.lang.String idCommune = "";
    private java.lang.String idCommunication = "";
    private java.lang.String idConjoint = "";
    private java.lang.String idDecision = "";
    private java.lang.String idIfdDefinitif = "";
    private java.lang.String idIfdProvisoire = "";
    private java.lang.String idPassage = "";
    private java.lang.String idServiceSociale = "";
    private java.lang.String idTiers = "";
    private Boolean impression = Boolean.FALSE;
    private java.lang.String interet = "";
    private Boolean lettreSignature = Boolean.FALSE;
    private java.lang.String nombreMoisTotalDecision = "";
    private Boolean opposition = Boolean.FALSE;
    private Boolean premiereAssurance = Boolean.FALSE;
    private java.lang.String prorata = "";
    private Boolean recours = Boolean.FALSE;
    private java.lang.String responsable = "";
    private Boolean miseEnGEDValidationRetour = Boolean.FALSE;
    private java.lang.String idDemandePortail = "";

    // Sauvegarde de l'id sortie d'une ANL pour pouvoir
    // la supprimer si on rencontre plus tard une imputation (uniquement CSC)
    private java.lang.String saveIdSortie = "";

    private java.lang.String specification = "";

    private boolean suppressionExterne = false;

    private java.lang.String taxation = "";

    private TITiersViewBean tiers = null;

    /*
     * A n'utiliser que pour passer la transaction aux external services
     */
    private BTransaction transactionForExternalService = null;

    private java.lang.String typeDecision = "";

    /**
     * contrôle du genre de décision avec l'affiliation
     * 
     * @param gereDecision
     *            String, affiliation AFAffiliation
     */
    static public boolean _checkGenreDecisionAvecAffiliation(String genreDecision, AFAffiliation affiliation)
            throws java.lang.Exception {
        // Indépendant
        if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(genreDecision)
                    || CPDecision.CS_RENTIER.equalsIgnoreCase(genreDecision)
                    || CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(genreDecision)) {
                return true;
            }
        }
        // Non actif
        if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(genreDecision)
                    || CPDecision.CS_ETUDIANT.equalsIgnoreCase(genreDecision)) {
                return true;
            }
        }
        // NA - Activité lucrative et non lucrative (assurance facultative)
        if (CodeSystem.TYPE_AFFILI_SELON_ART_1A.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_PROVIS.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            if (CPDecision.CS_ACT_LUCRATIVE.equalsIgnoreCase(genreDecision)
                    || CPDecision.CS_ACT_NON_LUCRATIVE.equalsIgnoreCase(genreDecision)) {
                return true;
            }
        }
        // TSE
        if (CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            if (CPDecision.CS_TSE.equalsIgnoreCase(genreDecision)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne la décision de base en fonction de l'id tiers, de l'id affiliation, de la date de début, de la date de
     * fin
     * 
     * @param CPDecision
     * @return CPDecision
     */
    static public CPDecision _returnDecisionBase(BSession session, CPDecision decision, boolean wantTestImputation,
            boolean wantOnlyActive) {
        CPDecision dec = null;
        try {
            CPDecisionManager decisionMng = new CPDecisionManager();
            decisionMng.setSession(session);
            decisionMng.setForIdTiers(decision.getIdTiers());
            decisionMng.setForIdAffiliation(decision.getIdAffiliation());
            decisionMng.setForAnneeDecision(decision.getAnneeDecision());
            decisionMng.setForGenreAffilie(decision.getGenreAffilie());
            decisionMng.setFromLtIdDecision(decision.getIdDecision());
            if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision()) && wantTestImputation) {
                decisionMng.setForTypeDecision(CPDecision.CS_IMPUTATION);
            } else {
                decisionMng.setNotInTypeDecision(CPDecision.CS_IMPUTATION);
            }
            if (wantOnlyActive) {
                decisionMng.setForIsActive(Boolean.TRUE);
            }
            decisionMng.orderByIdDecision();
            decisionMng.find();
            for (int i = 0; i < decisionMng.size(); i++) {
                // Il faut que la décision soit inclue dans celle de base
                dec = (CPDecision) decisionMng.getEntity(i);
                if (BSessionUtil.compareDateFirstLowerOrEqual(session, dec.getDebutDecision(),
                        decision.getDebutDecision())
                        && (BSessionUtil.compareDateFirstGreaterOrEqual(session, dec.getFinDecision(),
                                decision.getFinDecision()))) {
                    return dec;
                }
            }
        } catch (Exception e) {
            session.addError(e.getMessage());
        }
        return dec;
    }

    /**
     * Retourne la décision de base en fonction de l'id tiers, de l'id affiliation, de la date de début, de la date de
     * fin
     * 
     * @param CPDecision
     * @return CPDecision
     */
    static public CPDecision _returnDecisionBase(BSession session, String idDecision) throws Exception {
        CPDecision dec = null;
        CPDecision decision = new CPDecision();
        decision.setSession(session);
        decision.setIdDecision(idDecision);
        decision.retrieve();
        if (!decision.isNew()) {
            dec = CPDecision._returnDecisionBase(session, decision, false, true);
        }
        return dec;
    }

    /**
     * Pour certaine caisse, c'est la date de facturation qui fait office de date de décision et pour d'autres c'est la
     * date d'information. Détermine selon le properties affichageDateFuacturation le champ à prendre en considération
     * 
     * @param CPDecision
     * @return string
     */
    public static String getDateDecision(CPDecision decision) throws Exception {
        boolean affichageDateFacturation = ((CPApplication) GlobazSystem.getApplication("PHENIX"))
                .isAffichageDateFacturation();
        String dateDecision = "";
        if (affichageDateFacturation) {
            dateDecision = decision.getDateFacturation();
        } else {
            dateDecision = decision.getDateInformation();
        }
        return dateDecision;
    }

    public static Vector<?> getUserList(javax.servlet.http.HttpSession httpSession) {
        Vector<?> v = CPUtil.getUserList(httpSession);
        return v;
    }

    /**
     * Commentaire relatif au constructeur CPDecision
     */
    public CPDecision() {
        super();
    }

    /*
     * Traitement après suppression
     */
    @Override
    protected void _afterDelete(BTransaction transaction) {
        try {
            // Données du calcul
            CPDonneesCalculManager donCalculManager = new CPDonneesCalculManager();
            donCalculManager.setSession(getSession());
            donCalculManager.setForIdDecision(getIdDecision());
            donCalculManager.find();
            for (int i = 0; i < donCalculManager.size(); i++) {
                CPDonneesCalcul entity = ((CPDonneesCalcul) donCalculManager.getEntity(i));
                entity.delete(transaction);
            }
            // Cotisation
            CPCotisationManager cotiManager = new CPCotisationManager();
            cotiManager.setSession(getSession());
            cotiManager.setForIdDecision(getIdDecision());
            cotiManager.find();
            for (int i = 0; i < cotiManager.size(); i++) {
                CPCotisation entity = ((CPCotisation) cotiManager.getEntity(i));
                entity.delete(transaction);
            }
            // remarque
            CPRemarqueDecisionManager remaManager = new CPRemarqueDecisionManager();
            remaManager.setSession(getSession());
            remaManager.setForIdDecision(getIdDecision());
            remaManager.find();
            for (int i = 0; i < remaManager.size(); i++) {
                CPRemarqueDecision entity = ((CPRemarqueDecision) remaManager.getEntity(i));
                entity.delete(transaction);
            }
            // Données en entrée
            CPDonneesBase donBase = new CPDonneesBase();
            donBase.setSession(getSession());
            donBase.setIdDecision(getIdDecision());
            donBase.retrieve(transaction);
            if (!donBase.isNew()) {
                donBase.delete(transaction);
            }
            // Suppression du fichier de validation
            CPValidationCalculCommunication validation = new CPValidationCalculCommunication();
            validation.setSession(getSession());
            validation.setIdDecision(getIdDecision());
            validation.setAlternateKey(CPValidationCalculCommunication.ALT_KEY_IDDECISION);
            validation.retrieve();
            if (!validation.isNew()) {
                // Remise à jour des périodes d'affiliation si décision générée
                // par communication fiscale
                // et si communication en retour avait changement de genre
                // différent de vide
                if (!suppressionExterne) {
                    CPCommunicationFiscaleRetourViewBean ret = new CPCommunicationFiscaleRetourViewBean();
                    ret.setSession(getSession());
                    ret.setIdRetour(validation.getIdCommunicationRetour());
                    ret.retrieve();
                    if (!ret.isNew() && !JadeStringUtil.isIntegerEmpty(ret.getChangementGenre())) {
                        if (CPCommunicationFiscaleRetourViewBean.CS_IND_AFI.equalsIgnoreCase(ret.getChangementGenre())) {
                            // Création AFI
                            CPToolBox.ajoutRadiationCotisation(getIdAffiliation(), CodeSystem.TYPE_ASS_AFI,
                                    getDebutDecision(), getFinDecision(), 3, getSession());
                        }
                        if (CPCommunicationFiscaleRetourViewBean.CS_AFI_IND.equalsIgnoreCase(ret.getChangementGenre())) {
                            // Suppression AFI
                            CPToolBox.ajoutRadiationCotisation(getIdAffiliation(), CodeSystem.TYPE_ASS_AFI,
                                    getDebutDecision(), getFinDecision(), 4, getSession());
                        }
                    }
                }
                validation.delete(transaction);
            }
            // Remettre l'état de la communication retour à "réceptionnée" si
            // celle-ci n'a plus de décision (ex: conjoint)
            if (!JadeStringUtil.isIntegerEmpty(getIdCommunication()) && !isSuppressionExterne()) {
                // Recherche si il y a d'autres décisions générées
                CPDecisionManager decManager = new CPDecisionManager();
                decManager.setSession(getSession());
                decManager.setForIdCommunication(getIdCommunication());
                decManager.setForExceptIdDecision(getIdDecision());
                decManager.find();
                if (decManager.size() == 0) {
                    CPCommunicationFiscaleRetourViewBean retour = new CPCommunicationFiscaleRetourViewBean();
                    retour.setSession(getSession());
                    retour.setIdRetour(getIdCommunication());
                    retour.retrieve();
                    if (!retour.isNew()) {
                        retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
                        retour.update(transaction);
                    }
                }
            }
            // PO 9213
            supprimerSortie(transaction, this);

            // MAJ etat de la demande du portail à l'état "A traiter"
            if (!JadeStringUtil.isBlankOrZero(getIdDemandePortail())) {
                EBDemandeModifAcompteEntity dem = new EBDemandeModifAcompteEntity();
                dem.setSession(getSession());
                dem.setIdEntity(getIdDemandePortail());
                dem.retrieve();
                dem.setCsStatut(DemandeModifAcompteStatut.A_TRAITER.getValue());
                dem.update();
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0026") + " " + e.getMessage());
        }

    }

    /*
     * Basculer les décisions dans la bonne affiliation (lors d'une nouvelle saisie ou modification d'une affiliation
     * actuelle)
     */
    public void _basculerDansAffiliation(AFAffiliation affiliation, globaz.globall.db.BTransaction transaction)
            throws Exception {

        CPApplication phenixApp = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                CPApplication.DEFAULT_APPLICATION_PHENIX);
        BSession sessionPhenix = (BSession) phenixApp.newSession(transaction.getSession());
        String anneeLimite = CPToolBox.anneeLimite(transaction);
        boolean transfertChangeNumAffilie = ((CPApplication) sessionPhenix.getApplication())
                .isTransfertChangeNumAffilie();
        CPDecisionManager manager = new CPDecisionManager();
        manager.setSession(affiliation.getSession());
        // Retrouver toutes les décisions dont la date de fin est supérieure à
        // la date début de la nouvelle affiliation
        // essentiel pour la reprise annuelle
        manager.setFromDateFinDecision(affiliation.getDateDebut());
        manager.setToDateFinDecision(affiliation.getDateFin());
        if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            manager.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
        } else if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            manager.setInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
        }
        manager.setForIdTiers(affiliation.getIdTiers());
        manager.find(transaction);
        try {
            for (int i = 0; i < manager.size(); i++) {
                CPDecision myDecision = (CPDecision) manager.getEntity(i);
                boolean transfert = true;
                if (transfertChangeNumAffilie) {
                    // Regarder si c'est le même numéro d'affilié car certaine
                    // caisse
                    // ne veulent pas transférer la décision si le n° d'affilié
                    // change
                    AFAffiliation affi = new AFAffiliation();
                    affi.setSession(affiliation.getSession());
                    affi.setAffiliationId(myDecision.getIdAffiliation());
                    affi.retrieve();
                    if (!affiliation.getAffilieNumero().equals(affi.getAffilieNumero())) {
                        transfert = false;
                    }
                }
                // Si l'idAffiliation de la décision est différente de
                // l'affiliation actuelle (update)
                if (!myDecision.getIdAffiliation().equals(affiliation.getAffiliationId()) && transfert) {
                    // Supprimer les éventuelles sorties en attente
                    // car la décision est désormais valable pour cette
                    // affiliation
                    supprimerSortie(transaction, myDecision);
                    if (!transaction.hasErrors()) {
                        // mettre à jour la nouvelle affiliation dans la
                        // décision
                        myDecision.setIdAffiliation(affiliation.getAffiliationId());
                        myDecision.wantCallValidate(false);
                        myDecision.update(transaction);
                        myDecision.wantCallValidate(true);
                        // Mise à jour des com.fis. ret
                        if (!JadeStringUtil.isIntegerEmpty(myDecision.getIdCommunication())) {
                            CPCommunicationFiscaleRetourViewBean retour = new CPCommunicationFiscaleRetourViewBean();
                            retour.setSession(affiliation.getSession());
                            retour.setIdCommunication(myDecision.getIdCommunication());
                            retour.retrieve();
                            if (!retour.isNew()) {
                                retour.setIdAffiliation(affiliation.getAffiliationId());
                                retour.update(transaction);
                            }
                        }
                        CPToolBox.miseAjourDecisionActive(transaction, affiliation, myDecision.getAnneeDecision(),
                                Integer.parseInt(anneeLimite));
                        CPToolBox.miseAjourImputation(transaction, affiliation, myDecision.getAnneeDecision(),
                                Integer.parseInt(anneeLimite));
                        CPToolBox.miseAjourRemise(transaction, affiliation, myDecision.getAnneeDecision(),
                                Integer.parseInt(anneeLimite));
                        // Mise à jour communication fiscale en envoi (PO 9253)
                        CPProcessMiseAjourComFisc process = new CPProcessMiseAjourComFisc();
                        process.setSession(affiliation.getSession());
                        process.setTransaction(transaction);
                        process.setSimulation(new Boolean(false));
                        process.majCommunicationFiscale(myDecision, true, null, null);
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0032") + " " + affiliation.getAffiliationId());
        }

    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        if (AFParticulariteAffiliation.existeParticularite(transaction, getIdAffiliation(),
                CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE, getDebutDecision())) {
            setFacturation(new Boolean(false));
        }

        // incrémente de +1 le numéro
        // setIdDecision(_incCounter(transaction, idDecision));
        setIdDecision(this._incCounter(transaction, idDecision));
        // Mettre le type à "définitif" si il a assez payé comme salarié
        // Evite ainsi de voir le cas sur la liste des décisions non définitive
        // idem pour les étudiants
        if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(getSpecification())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            if (isProvisoireMetier()) {
                setTypeDecision(CPDecision.CS_DEFINITIVE);
            }
        }
        setDernierEtat(CPDecision.CS_CREATION);
    }

    /*
     * Traitement avant suppression
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) {
        try {
            // Suppression des afacts si ils sont générés
            // Nécessaire pour la caisse suisse pour la suppression dans Uekost
            if (!JadeStringUtil.isIntegerEmpty(getIdPassage())) {
                FAPassage passage = new FAPassage();
                passage.setSession(getSession());
                passage.setIdPassage(getIdPassage());
                passage.retrieve(transaction);
                if (!passage.isNew()) {
                    if ((passage.isEstVerrouille().equals(new Boolean(true)))
                            || (passage.getStatus().equalsIgnoreCase(FAPassage.CS_ETAT_COMPTABILISE))) {
                        _addError(transaction, getSession().getLabel("CP_MSG_0025") + " " + passage.getLibelle() + " "
                                + getSession().getLabel("CP_MSG_0025A"));
                    } else {
                        // Suppression entete facture et ligne de facture
                        // (afact)
                        CPToolBox.suppressionFacture(getSession(), transaction, this);
                    }
                }
            }
        } catch (Exception e) {
            _addError(transaction, getSession().getLabel("CP_MSG_0026") + " " + e.getMessage());
        }

    }

    /*
     * Traitement avant modification
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Mettre le type à "définitif" si il a assez payé comme salarié
        // Evite ainsi de voir le cas sur la liste des décisions non définitive
        if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(getSpecification())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            if (isProvisoireMetier()) {
                setTypeDecision(CPDecision.CS_DEFINITIVE);
            }
        }
    }

    /*
     * Extourne des montant en comptabilité et ci pour une décision suite à une radiation de l'affiliation
     */
    public boolean _calculSortieDebutParDecision(BTransaction transaction, BSession sessionPhenix, IDecision decision,
            AFAffiliation affiliation, FAPassage passage, boolean prorata, boolean remboursement, String anneeRef,
            boolean anneeEnCoursTraite, CACompteAnnexe compte) throws Exception {

        int nbMoisExtourne = 0;
        CPSortie sortie = new CPSortie();
        // Détermination des dates à prendre en compte
        // (pas rembourser un cas qui l'aurait déjà été etc...)
        String dateFin = decision.getFinDecision();
        String dateDebut = decision.getDebutDecision();
        if (prorata && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
            // Pour les décisions dont la période comprend la date de radiation,
            // il faut faire 2 écritures:
            if (remboursement) { // On réactive les cas... date de fin > à
                // celle qui existait
                // Si la décision a proratisé comprend l'ancienne date de fin
                // d'affiliation => extourne + facturation
                if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                        decision.getFinDecision(), affiliation.getDateDebut())) {
                    dateFin = affiliation.getDateDebut();
                    // Calcul du nombre de mois à facturer
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, false,
                            nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, true);
                }
                dateFin = decision.getFinDecision();
                // Facturer la décision jusqu'au mois de fin de l'affiliation
                if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                        decision.getFinDecision(), affiliation.getDateFin())) {
                    dateFin = affiliation.getDateFin();
                }
                // Calcul du nombre de mois à rembourser
                nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, true,
                        nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, true);
            } else {
                // Si la décision a proratisé comprend la date de debut
                // d'affiliation
                if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                        decision.getFinDecision(), affiliation.getDateDebut())) {
                    // Annulation de la décision qui comprend la date de fin
                    // d'affiliation
                    if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                            decision.getFinDecision(), affiliation.getDateDebutSave())) {
                        dateFin = affiliation.getDateDebutSave();
                    }
                    // Calcul du nombre de mois à extrouner
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, true,
                            nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, true);
                    // Facturer la décision jusqu'au mois de fin de
                    // l'affiliation
                    dateDebut = affiliation.getDateDebut();
                    // Calcul du nombre de mois à refacturer
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, false,
                            nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, true);
                } else {
                    // Cas où la décision comprend l'ancienne date de radiation
                    // (cad qu'on change la date de fin dans le passé)
                    dateFin = affiliation.getDateDebutSave();
                    // Calcul du nombre de mois à extourner
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata,
                            remboursement, nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, true);
                }
            }
        } else {
            // Ignorer l'année de radiation car la décision doit être reprise
            if (!anneeRef.equalsIgnoreCase(decision.getAnneeDecision())
                    || (affiliation.getDateDebut().equalsIgnoreCase(affiliation.getDateFin())) || remboursement) {
                // Annulation ou re facturation de la décision entière
                sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, false, remboursement,
                        0, dateFin, anneeEnCoursTraite, compte, true);
            }
        }
        return true;
    }

    public boolean _calculSortieParDecision(BTransaction transaction, BSession sessionPhenix, IDecision decision,
            AFAffiliation affiliation, FAPassage passage, boolean prorata, boolean remboursement, String anneeRef,
            boolean anneeEnCoursTraite, CACompteAnnexe compte) throws Exception {

        int nbMoisExtourne = 0;
        CPSortie sortie = new CPSortie();
        // Détermination des dates à prendre en compte
        // (pas rembourser un cas qui l'aurait déjà été etc...)
        String dateFin = decision.getFinDecision();
        String dateDebut = decision.getDebutDecision();
        if (prorata && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
            // Pour les décisions dont la période comprend la date de radiation,
            // il faut faire 2 écritures:
            if (!remboursement) { // On réactive les cas... date de fin > à
                // celle qui existait
                // Si la décision a proratisé comprend l'ancienne date de fin
                // d'affiliation => extourne + facturation
                if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                        decision.getFinDecision(), affiliation.getDateFinSave())) {
                    dateFin = affiliation.getDateFinSave();
                    // Calcul du nombre de mois à extourner
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, true,
                            nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, false);
                }
                dateFin = decision.getFinDecision();
                // Facturer la décision jusqu'au mois de fin de l'affiliation
                if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                        decision.getFinDecision(), affiliation.getDateFin())) {
                    dateFin = affiliation.getDateFin();
                }
                // Calcul du nombre de mois à refacturer
                nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, false,
                        nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, false);
            } else {
                // Si la décision a proratisé comprend la date de fin
                // d'affiliation
                if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                        decision.getFinDecision(), affiliation.getDateFin())) {
                    // Annulation de la décision qui comprend la date de fin
                    // d'affiliation
                    if (BSessionUtil.compareDateBetween(sessionPhenix, decision.getDebutDecision(),
                            decision.getFinDecision(), affiliation.getDateFinSave())) {
                        dateFin = affiliation.getDateFinSave();
                    }
                    // Calcul du nombre de mois à extourner
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, true,
                            nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, false);
                    // Facturer la décision jusqu'au mois de fin de
                    // l'affiliation
                    dateFin = affiliation.getDateFin();
                    // Calcul du nombre de mois à refacturer
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata, false,
                            nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, false);
                } else {
                    // Cas où la décision comprend l'ancienne date de radiation
                    // (cad qu'on change la date de fin dans le passé)
                    dateFin = affiliation.getDateFinSave();
                    // Calcul du nombre de mois à extourner
                    nbMoisExtourne = (JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut)) + 1;
                    sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, prorata,
                            remboursement, nbMoisExtourne, dateFin, anneeEnCoursTraite, compte, false);
                }
            }
        } else {
            // Ignorer l'année de radiation car la décision doit être reprise
            if (!anneeRef.equalsIgnoreCase(decision.getAnneeDecision())
                    || (affiliation.getDateDebut().equalsIgnoreCase(affiliation.getDateFin())) || !remboursement) {
                // Annulation ou re facturation de la décision entière
                sortie._createSortie(transaction, sessionPhenix, decision, affiliation, passage, false, remboursement,
                        0, dateFin, anneeEnCoursTraite, compte, false);
            }
        }
        return true;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPDECIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idIfdDefinitif = statement.dbReadNumeric("ICIIFD");
        idIfdProvisoire = statement.dbReadNumeric("ICIIFP");
        idCommunication = statement.dbReadNumeric("IBIDCF");
        idPassage = statement.dbReadNumeric("EBIPAS");
        typeDecision = statement.dbReadNumeric("IATTDE");
        idConjoint = statement.dbReadNumeric("HTICJT");
        genreAffilie = statement.dbReadNumeric("IATGAF");
        dateInformation = statement.dbReadDateAMJ("IADINF");
        anneeDecision = statement.dbReadNumeric("IAANNE");
        debutDecision = statement.dbReadDateAMJ("IADDEB");
        finDecision = statement.dbReadDateAMJ("IADFIN");
        bloque = statement.dbReadBoolean("IACBLO");
        interet = statement.dbReadNumeric("IACINT");
        impression = statement.dbReadBoolean("IABIMP");
        facturation = statement.dbReadBoolean("IAFACT");
        division2 = statement.dbReadBoolean("IAASSU");
        anneePrise = statement.dbReadString("IAAPRI");
        taxation = statement.dbReadString("IATAXA");
        responsable = statement.dbReadString("IARESP");
        opposition = statement.dbReadBoolean("IAOPPO");
        prorata = statement.dbReadString("IABPRO");
        specification = statement.dbReadNumeric("IATSPE");
        complementaire = statement.dbReadBoolean("IABCOM");
        lettreSignature = statement.dbReadBoolean("IABSIG");
        active = statement.dbReadBoolean("IAACTI");
        debutActivite = statement.dbReadBoolean("IABDAC");
        premiereAssurance = statement.dbReadBoolean("IABPAS");
        dateFacturation = statement.dbReadDateAMJ("IADFAC");
        dernierEtat = statement.dbReadNumeric("IATETA");
        nombreMoisTotalDecision = statement.dbReadNumeric("IANTOT");
        idServiceSociale = statement.dbReadNumeric("IAISES");
        cotiMinimumPayeEnSalarie = statement.dbReadBoolean("IABCMP");
        idCommune = statement.dbReadNumeric("IAICOM");
        recours = statement.dbReadBoolean("IABREC");
        miseEnGEDValidationRetour = statement.dbReadBoolean("IABGED");
        idDemandePortail = statement.dbReadNumeric("EBIDDP");
    }

    public void _sortieDeb(AFAffiliation affiliation, globaz.globall.db.BTransaction transaction) throws Exception {
        // Recherche de l'année limite d'inscription CI - Paramètre
        CPApplication phenixApp = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                CPApplication.DEFAULT_APPLICATION_PHENIX);
        BSession sessionPhenix = (BSession) phenixApp.newSession(transaction.getSession());
        globaz.musca.api.IFAPassage passage = null;
        boolean anneeEnCoursTraite = false;
        boolean remboursement = false;
        // période à prendre en compte (si il y a déjà eu une date de
        // radiation...)
        int anneeDeb = 0;
        int anneeFin = 0;
        if (BSessionUtil.compareDateFirstGreater(sessionPhenix, affiliation.getDateDebut(),
                affiliation.getDateDebutSave())) {
            // Remboursement
            anneeDeb = JACalendar.getYear(affiliation.getDateDebutSave());
            anneeFin = JACalendar.getYear(affiliation.getDateDebut());
            remboursement = true;
        } else {
            anneeDeb = JACalendar.getYear(affiliation.getDateDebut());
            anneeFin = JACalendar.getYear(affiliation.getDateDebutSave());
        }
        // Lecture des décisions
        CPDecisionManager manager = new CPDecisionManager();
        manager.setSession(sessionPhenix);
        manager.setForIdAffiliation(affiliation.getAffiliationId());
        // Si décision supérieur ou quelle comprend la date de fin et qu'elle
        // est en cours (état validé) => erreur
        // Excepté les décisions encodées à l'avance (Ex 2009 en 2008)
        manager.setForEtat(CPDecision.CS_VALIDATION);
        manager.find(transaction);
        if (manager.size() > 0) {
            _addError(transaction, getSession().getLabel("CP_MSG_0027"));
        }
        manager.setFromAnneeDecision(Integer.toString(anneeDeb));
        if (anneeFin != 0) {
            manager.setToAnneeDecision(Integer.toString(anneeFin));
            // TODO -1 si 01.01.2007 ???
        } else {
            manager.setToAnneeDecision("");
        }
        manager.setForEtat("");
        manager.setFromEtat(CPDecision.CS_VALIDATION);

        manager.orderByAnneeDecisionAsc();
        manager.orderByDateDecisionAsc();
        manager.orderByIdDecision();
        miseAjourCodeActif(affiliation, transaction, remboursement, manager);
        manager.setForIsActive(Boolean.TRUE);
        manager.find(transaction);
        // Ne rien faire si il n'y a pas de décision
        if ((manager.size() > 0) && !transaction.hasErrors()) {
            String genre = ((CPDecision) manager.getFirstEntity()).getGenreAffilie();
            passage = findPassage(affiliation, transaction, sessionPhenix, genre);
            // Si le passage est vide et qu'il existe des décisions pour cet
            // affilié, quitter
            if (passage == null) {
                if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(genre)) {
                    _addError(transaction, getSession().getLabel("CP_MSG_0189"));
                } else {
                    _addError(transaction, getSession().getLabel("CP_MSG_0028"));
                }
            } else {
                String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(phenixApp);
                // Recherche du compte annexe
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(role);
                compte.setIdExterneRole(affiliation.getAffilieNumero());
                compte.wantCallMethodBefore(false);
                compte.retrieve(transaction);
                for (int i = 0; i < manager.size(); i++) {
                    CPDecision myDecision = (CPDecision) manager.getEntity(i);
                    boolean aTraite = checkAnnulationSortie(affiliation, myDecision);
                    // Si facturation et période de décision comprise dans ancienne date de debut d'affiliation
                    // => ne rien faire car elle n'a jamais été extournée par une nouvelle décision
                    if (!remboursement
                            && BSessionUtil.compareDateFirstGreater(sessionPhenix, myDecision.getFinDecision(),
                                    affiliation.getDateDebutSave())) {
                        aTraite = false;
                    }
                    if (aTraite) {
                        if (!CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(myDecision.getSpecification())) {
                            if (!remboursement) {
                                if (BSessionUtil.compareDateFirstGreaterOrEqual(sessionPhenix,
                                        myDecision.getDebutDecision(), affiliation.getDateDebut())) {
                                    _calculSortieDebutParDecision(transaction, sessionPhenix, myDecision, affiliation,
                                            (FAPassage) passage, false, remboursement, Integer.toString(anneeDeb),
                                            anneeEnCoursTraite, compte);
                                }
                            } else {
                                if (BSessionUtil.compareDateFirstLower(sessionPhenix, myDecision.getFinDecision(),
                                        affiliation.getDateDebut())) {
                                    if (BSessionUtil.compareDateEqual(sessionPhenix, affiliation.getDateDebutSave(),
                                            myDecision.getDebutDecision())) {
                                        _calculSortieDebutParDecision(transaction, sessionPhenix, myDecision,
                                                affiliation, (FAPassage) passage, true, remboursement,
                                                Integer.toString(anneeDeb), anneeEnCoursTraite, compte);
                                    } else {
                                        _calculSortieDebutParDecision(transaction, sessionPhenix, myDecision,
                                                affiliation, (FAPassage) passage, false, remboursement,
                                                Integer.toString(anneeDeb), anneeEnCoursTraite, compte);
                                    }

                                }
                            }// fin else
                             // Si année en cours => aller prendre le montant qui a
                             // été facturé pour la cotisation
                             // car la décision n'a pas encore été entiérement
                             // facturé. A ne faire qu'une seule fois pour l'année
                             // Si année > année encours => rien a été encore facturé
                            if ((Integer.parseInt(myDecision.getAnneeDecision()) == JACalendar.today().getYear())
                                    && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(myDecision.getTypeDecision())) {
                                anneeEnCoursTraite = true;
                            }
                        }
                    }
                } // Fin For
            }
        }
    }

    public void _sortieFin(AFAffiliation affiliation, globaz.globall.db.BTransaction transaction) throws Exception {
        // Recherche de l'année limite d'inscription CI - Paramètre
        CPApplication phenixApp = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                CPApplication.DEFAULT_APPLICATION_PHENIX);
        BSession sessionPhenix = (BSession) phenixApp.newSession(transaction.getSession());
        globaz.musca.api.IFAPassage passage = null;
        boolean prorata = false;
        boolean anneeEnCoursTraite = false;
        boolean remboursement = true;
        // période à prendre en compte (si il y a déjà eu une date de
        // radiation...)
        int anneeDeb = 0;
        int anneeFin = 0;
        String varDateDeb = "";
        if (BSessionUtil.compareDateFirstGreater(sessionPhenix, affiliation.getDateFin(), affiliation.getDateFinSave())) {
            if (JAUtil.isDateEmpty(affiliation.getDateFinSave())) {
                anneeDeb = JACalendar.getYear(affiliation.getDateFin());
                varDateDeb = affiliation.getDateFin();
            } else {
                // Re facturation
                anneeDeb = JACalendar.getYear(affiliation.getDateFinSave());
                anneeFin = JACalendar.getYear(affiliation.getDateFin());
                varDateDeb = affiliation.getDateFinSave();
                remboursement = false;
            }
        } else {
            if (JAUtil.isDateEmpty(affiliation.getDateFin())) {
                anneeDeb = JACalendar.getYear(affiliation.getDateFinSave());
                varDateDeb = affiliation.getDateFinSave();
                remboursement = false;
            } else {
                anneeDeb = JACalendar.getYear(affiliation.getDateFin());
                anneeFin = JACalendar.getYear(affiliation.getDateFinSave());
                varDateDeb = affiliation.getDateFin();
            }
        }
        // Si l'affilié a encore une demande de modification dans le portail en cours de travail => erreur
        List<String> statutEnCours = new ArrayList<String>();
        statutEnCours.add(DemandeModifAcompteStatut.A_TRAITER.getValue());
        statutEnCours.add(DemandeModifAcompteStatut.VALIDE.getValue());
        if (EBDemandeModifAcompteEntity.returnNbDemandeInstatusForIdAffiliation(getSession(),
                affiliation.getAffiliationId(), statutEnCours) > 0) {
            _addError(transaction, getSession().getLabel("ERROR_DEMANDE_PORTAIL"));
        }

        // Lecture des décisions
        CPDecisionManager manager = new CPDecisionManager();
        manager.setSession(sessionPhenix);
        manager.setForIdAffiliation(affiliation.getAffiliationId());
        manager.setFromDateDebutDecision(varDateDeb);
        // Si décision supérieur ou quelle comprend la date de fin et qu'elle
        // est en cours (état validé) => erreur
        // Excepté les décisions encodées à l'avance (Ex 2009 en 2008)
        manager.setToAnneeDecision(Integer.toString(JACalendar.getYear(JACalendar.today().toString())));
        manager.setForEtat(CPDecision.CS_VALIDATION);
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        if (manager.size() > 0) {
            _addError(transaction, getSession().getLabel("CP_MSG_0027"));
        }
        manager.setFromAnneeDecision(Integer.toString(anneeDeb));
        if (anneeFin != 0) {
            manager.setToAnneeDecision(Integer.toString(anneeFin));
        } else {
            manager.setToAnneeDecision("");
        }
        manager.setForEtat("");
        manager.setFromEtat(CPDecision.CS_VALIDATION);
        manager.orderByAnneeDecisionAsc();
        manager.orderByDateDecisionAsc();
        manager.orderByIdDecision();

        // Nécessaire de remettre à jour le code actif pour que la décision soit prise en compte
        // Ex d'un cas radié que l'on reprend
        miseAjourCodeActif(affiliation, transaction, remboursement, manager);
        manager.setForIsActive(Boolean.TRUE);
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        // NE rien faire si il n'y a pas de décision
        if ((manager.size() > 0) && !transaction.hasErrors()) {
            String genre = ((CPDecision) manager.getFirstEntity()).getGenreAffilie();
            passage = findPassage(affiliation, transaction, sessionPhenix, genre);
            // Si le passage est vide et qu'il existe des décisions pour cet
            // affilié, quitter
            if (passage == null) {
                if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(genre)) {
                    _addError(transaction, getSession().getLabel("CP_MSG_0189"));
                } else {
                    _addError(transaction, getSession().getLabel("CP_MSG_0028"));
                }
            } else {
                String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(phenixApp);
                // Recherche du compte annexe
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setSession(getSession());
                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compte.setIdRole(role);
                compte.setIdExterneRole(affiliation.getAffilieNumero());
                compte.wantCallMethodBefore(false);
                compte.retrieve(transaction);
                for (int i = 0; i < manager.size(); i++) {
                    CPDecision myDecision = (CPDecision) manager.getEntity(i);
                    // Si une sortie a déjà été créé par la décision et qu'elle n'est pas encore comptabilisée
                    // => il faut seulement effacer la sortie et ne rien faire (cela veut dire que l'utilisateur
                    // à fait une erreur et il remodifie la date
                    boolean aTraite = checkAnnulationSortie(affiliation, myDecision);
                    if (aTraite) {
                        if (!CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(myDecision.getSpecification())) {
                            // Si on rembourse (radiation) on se base sur la date de
                            // fin d'affiliation
                            // sinon c'est une prolongation ou une annulation de
                            // radiation (re facturation)
                            if (((anneeDeb < 2004) && myDecision.isNonActif())
                                    || ("A".equalsIgnoreCase(myDecision.getTaxation()) && !myDecision.isNonActif())) {
                                prorata = true;
                            }
                            if (!remboursement) {
                                // CAS 1: Facturation de la décision si la période
                                // de décision > date de radiation précédente
                                // et période de décision < date de radiation si
                                // date de radiation <> 0
                                boolean finDecisionSmallerFinAffiliation = true;
                                boolean debutDecisionGreaterAncienneRadiation = BSessionUtil
                                        .compareDateFirstLowerOrEqual(sessionPhenix, affiliation.getDateFinSave(),
                                                myDecision.getDebutDecision());
                                if (!JAUtil.isDateEmpty(affiliation.getDateFin())) {
                                    finDecisionSmallerFinAffiliation = BSessionUtil.compareDateFirstLowerOrEqual(
                                            sessionPhenix, myDecision.getFinDecision(), affiliation.getDateFin());
                                }
                                if (finDecisionSmallerFinAffiliation && debutDecisionGreaterAncienneRadiation) {
                                    _calculSortieParDecision(transaction, sessionPhenix, myDecision, affiliation,
                                            (FAPassage) passage, false, remboursement, Integer.toString(anneeDeb),
                                            anneeEnCoursTraite, compte);
                                }
                                // CAS 2: Prorata - La période de décision comprend
                                // la date de radiation (si <>0) ou l'ancienne date
                                // de radiation
                                boolean finAffiliationBetweenDecision = BSessionUtil.compareDateBetween(sessionPhenix,
                                        myDecision.getDebutDecision(), myDecision.getFinDecision(),
                                        affiliation.getDateFin());
                                boolean finAffiliationPrecBetweenDecision = BSessionUtil.compareDateBetween(
                                        sessionPhenix, myDecision.getDebutDecision(), myDecision.getFinDecision(),
                                        affiliation.getDateFinSave());
                                if (finAffiliationBetweenDecision || finAffiliationPrecBetweenDecision) {
                                    _calculSortieParDecision(transaction, sessionPhenix, myDecision, affiliation,
                                            (FAPassage) passage, prorata, remboursement, Integer.toString(anneeDeb),
                                            anneeEnCoursTraite, compte);
                                }
                            } else {
                                // CAS 1: Remboursement de la décision si la période
                                // de décision > date de radiation
                                // et période de décision < ancienneDateDeRadiation
                                // si ancienneDateRadiation <> 0
                                boolean finDecisionSmallerAncienneRadiation = true;
                                boolean finAffiliationSmallerDecision = BSessionUtil.compareDateFirstLowerOrEqual(
                                        sessionPhenix, affiliation.getDateFin(), myDecision.getDebutDecision());
                                if (!JAUtil.isDateEmpty(affiliation.getDateFinSave())) {
                                    finDecisionSmallerAncienneRadiation = BSessionUtil.compareDateFirstLowerOrEqual(
                                            sessionPhenix, myDecision.getFinDecision(), affiliation.getDateFinSave());
                                }
                                if (finAffiliationSmallerDecision && finDecisionSmallerAncienneRadiation) {
                                    _calculSortieParDecision(transaction, sessionPhenix, myDecision, affiliation,
                                            (FAPassage) passage, false, remboursement, Integer.toString(anneeDeb),
                                            anneeEnCoursTraite, compte);
                                }
                                // CAS 2: Prorata - La période de décision comprend
                                // la date de radiation ou l'ancienne date de
                                // radiation (si <>0)
                                boolean finAffiliationBetweenDecision = BSessionUtil.compareDateBetween(sessionPhenix,
                                        myDecision.getDebutDecision(), myDecision.getFinDecision(),
                                        affiliation.getDateFin());
                                boolean finAffiliationPrecBetweenDecision = BSessionUtil.compareDateBetween(
                                        sessionPhenix, myDecision.getDebutDecision(), myDecision.getFinDecision(),
                                        affiliation.getDateFinSave());
                                if (finAffiliationBetweenDecision || finAffiliationPrecBetweenDecision) {
                                    _calculSortieParDecision(transaction, sessionPhenix, myDecision, affiliation,
                                            (FAPassage) passage, prorata, remboursement, Integer.toString(anneeDeb),
                                            anneeEnCoursTraite, compte);
                                }
                            }// fin else
                             // Si année en cours => aller prendre le montant qui a
                             // été facturé pour la cotisation
                             // car la décision n'a pas encore été entiérement
                             // facturé. A ne faire qu'une seule fois pour l'année
                             // Si année > année encours => rien a été encore facturé
                            if ((Integer.parseInt(myDecision.getAnneeDecision()) == JACalendar.today().getYear())
                                    && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(myDecision.getTypeDecision())) {
                                anneeEnCoursTraite = true;
                            }
                        }
                    }
                } // Fin For
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        int anneeDecision = Integer.parseInt(getAnneeDecision());
        int anneeDebutDecision = globaz.globall.util.JACalendar.getYear(getDebutDecision());
        if (anneeDebutDecision < anneeDecision) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0029"));
        }
        int anneeFinDecision = globaz.globall.util.JACalendar.getYear(getFinDecision());
        if (anneeFinDecision > anneeDecision) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0030"));
        }
        // PO 8629
        if (!globaz.globall.db.BSessionUtil.compareDateFirstLower(getSession(), getDebutDecision(), getFinDecision())) {
            if (getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0049"));
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0031"));
            }
        }

        // Test validité du complémentaire - Il doit y avoir une autre décision
        // avec le même n° d'affilié
        boolean testComplementaire = false;
        if (getComplementaire().equals(Boolean.TRUE)) {
            if (CPDecision.CS_VALIDATION.equalsIgnoreCase(getDernierEtat()) || Boolean.TRUE.equals(getActive())) {
                CPDecisionAffiliationManager decM = new CPDecisionAffiliationManager();
                decM.setSession(getSession());
                if (getAffiliation() == null) {
                    AFAffiliation aff = new AFAffiliation();
                    aff.setAffiliationId(getIdAffiliation());
                    aff.setSession(getSession());
                    aff.retrieve();
                    setAffiliation(aff);
                }
                decM.setForNoAffilie(getAffiliation().getAffilieNumero());
                decM.setForAnneeDecision(getAnneeDecision());
                decM.setForExceptIdDecision(getIdDecision());
                decM.setForIdTiers(getIdTiers());
                decM.find();
                for (int i = 0; (i < decM.getSize()) && !testComplementaire; i++) {
                    CPDecisionAffiliation dec = (CPDecisionAffiliation) decM.getEntity(i);
                    if (CPDecision.CS_VALIDATION.equalsIgnoreCase(dec.getEtat())
                            || Boolean.TRUE.equals(dec.getActive())) {
                        testComplementaire = true;
                    }
                }
                if (!testComplementaire) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0188"));
                }
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("ICIIFD",
                this._dbWriteNumeric(statement.getTransaction(), getIdIfdDefinitif(), "idIfdDefinitif"));
        statement.writeField("ICIIFP",
                this._dbWriteNumeric(statement.getTransaction(), getIdIfdProvisoire(), "idIfdProvisoire"));
        statement.writeField("IBIDCF",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunication(), "idCommunication"));
        statement.writeField("EBIPAS", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField("IATTDE",
                this._dbWriteNumeric(statement.getTransaction(), getTypeDecision(), "typeDecision"));
        statement.writeField("HTICJT", this._dbWriteNumeric(statement.getTransaction(), getIdConjoint(), "idConjoint"));
        statement.writeField("IATGAF",
                this._dbWriteNumeric(statement.getTransaction(), getGenreAffilie(), "genreAffilie"));
        statement.writeField("IADINF",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateInformation(), "dateInformation"));
        statement.writeField("IAANNE",
                this._dbWriteNumeric(statement.getTransaction(), getAnneeDecision(), "anneeDecision"));
        statement.writeField("IADDEB",
                this._dbWriteDateAMJ(statement.getTransaction(), getDebutDecision(), "debutDecision"));
        statement.writeField("IADFIN",
                this._dbWriteDateAMJ(statement.getTransaction(), getFinDecision(), "finDecision"));
        statement.writeField("IACBLO", this._dbWriteBoolean(statement.getTransaction(), getBloque(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "bloque"));
        statement.writeField("IACINT", this._dbWriteNumeric(statement.getTransaction(), getInteret(), "interet"));
        statement.writeField("IABIMP", this._dbWriteBoolean(statement.getTransaction(), getImpression(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "impression"));
        statement.writeField("IAASSU", this._dbWriteBoolean(statement.getTransaction(), getDivision2(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "devision2"));
        statement.writeField("IAFACT", this._dbWriteBoolean(statement.getTransaction(), getFacturation(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "facturation"));
        statement.writeField("IAAPRI", this._dbWriteString(statement.getTransaction(), getAnneePrise(), "anneePrise"));
        statement.writeField("IATAXA", this._dbWriteString(statement.getTransaction(), getTaxation(), "taxation"));
        statement
                .writeField("IARESP", this._dbWriteString(statement.getTransaction(), getResponsable(), "responsable"));
        statement.writeField("IAOPPO", this._dbWriteBoolean(statement.getTransaction(), getOpposition(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "opposition"));
        statement.writeField("IABREC", this._dbWriteBoolean(statement.getTransaction(), getRecours(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "recours"));
        statement.writeField("IABPRO", this._dbWriteString(statement.getTransaction(), getProrata(), "prorata"));
        statement.writeField("IATSPE",
                this._dbWriteNumeric(statement.getTransaction(), getSpecification(), "specification"));
        statement.writeField("IABCOM", this._dbWriteBoolean(statement.getTransaction(), getComplementaire(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "complementaire"));
        statement.writeField("IABSIG", this._dbWriteBoolean(statement.getTransaction(), getLettreSignature(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "lettreSignature"));
        statement.writeField("IAACTI", this._dbWriteBoolean(statement.getTransaction(), getActive(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "active"));
        statement.writeField("IABDAC", this._dbWriteBoolean(statement.getTransaction(), getDebutActivite(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "debutActivite"));
        statement.writeField("IABPAS", this._dbWriteBoolean(statement.getTransaction(), getPremiereAssurance(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "premiereAssurance"));
        statement.writeField("IADFAC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFacturation(), "dateFacturation"));
        statement.writeField("IANTOT", this._dbWriteNumeric(statement.getTransaction(), getNombreMoisTotalDecision(),
                "nombreMoisTotalDecision"));
        statement.writeField("IATETA",
                this._dbWriteNumeric(statement.getTransaction(), getDernierEtat(), "dernierEtat"));
        statement.writeField("IAISES",
                this._dbWriteNumeric(statement.getTransaction(), getIdServiceSociale(), "idServiceSociale"));
        statement.writeField("IABCMP", this._dbWriteBoolean(statement.getTransaction(), getCotiMinimumPayeEnSalarie(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "cotiMinimumPayeEnSalarie"));
        statement.writeField("IAICOM", this._dbWriteNumeric(statement.getTransaction(), getIdCommune(), "idCommune"));
        statement.writeField("IABGED", this._dbWriteBoolean(statement.getTransaction(), getMiseEnGEDValidationRetour(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "miseEnGEDValidationRetour"));
        statement.writeField("EBIDDP",
                this._dbWriteNumeric(statement.getTransaction(), getIdDemandePortail(), "idDemandePortail"));
    }

    public void attachTransactionForExternalService(BTransaction transactionForExternalService) {
        this.transactionForExternalService = transactionForExternalService;
    }

    private boolean checkAnnulationSortie(AFAffiliation affiliation, CPDecision myDecision) throws Exception {
        CPSortieManager sortieManager = new CPSortieManager();
        sortieManager.setSession(affiliation.getSession());
        sortieManager.setForIdDecision(myDecision.getIdDecision());
        sortieManager.setForIsComptabilise(Boolean.TRUE);
        sortieManager.find(affiliation.getSession().getCurrentThreadTransaction());
        boolean aTraite = true;
        for (int j = 0; j < sortieManager.size(); j++) {
            CPSortie sortie = ((CPSortie) sortieManager.getEntity(j));
            sortie.delete(affiliation.getSession().getCurrentThreadTransaction());
            aTraite = false;
        }
        return aTraite;
    }

    /**
     * Insert the method's description here. Creation date: (18.06.2003 09:01:14)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                The exception description.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    public BManager find(Hashtable<?, ?> params) throws Exception {

        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration<?> methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                Object value = params.get(methodName);

                Method m = manager.getClass().getMethod(methodName, new Class[] { value.getClass() });

                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }

        manager.find();
        return manager;
    }

    private globaz.musca.api.IFAPassage findPassage(AFAffiliation affiliation,
            globaz.globall.db.BTransaction transaction, BSession sessionPhenix, String genre) {
        globaz.musca.api.IFAPassage passage;
        // Si décision liée à une demande EBU
        if (!JadeStringUtil.isBlankOrZero(getIdDemandePortail())) {
            passage = ServicesFacturation.getProchainPassageFacturation(sessionPhenix, transaction,
                    FAModuleFacturation.CS_MODULE_COT_PERS_PORTAIL);
        } else {
            if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(genre)) {
                passage = ServicesFacturation.getProchainPassageFacturation(sessionPhenix, transaction,
                        FAModuleFacturation.CS_MODULE_ETUDIANT);
            } else {
                // Recherche si séparation indépendant et non-actif - Inforom 314s
                Boolean isSeprationIndNac = false;
                try {
                    isSeprationIndNac = new Boolean(GlobazSystem
                            .getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA).getProperty(
                                    FAApplication.SEPARATION_IND_NA));
                } catch (Exception e) {
                    isSeprationIndNac = Boolean.FALSE;
                }
                if (isSeprationIndNac) {
                    if (affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                            || affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
                        passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                                FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
                    } else {
                        passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                                FAModuleFacturation.CS_MODULE_COT_PERS_IND);
                    }
                } else {
                    passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                            FAModuleFacturation.CS_MODULE_COT_PERS);
                }
            }
        }
        return passage;
    }

    /**
     * @return
     */
    public Boolean getActive() {
        return active;
    }

    public AFAffiliation getAffiliation() throws Exception {
        if ((affiliation == null) && !JadeStringUtil.isBlankOrZero(getIdAffiliation())) {
            AFAffiliation aff = new AFAffiliation();
            aff.setAffiliationId(getIdAffiliation());
            aff.setSession(getSession());
            aff.retrieve();
            return aff;
        }
        return affiliation;
    }

    @Override
    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    public java.lang.String getAnneePrise() {
        return anneePrise;
    }

    public Boolean getBloque() {
        return bloque;
    }

    /**
     * Returns the complementaire.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getComplementaire() {
        return complementaire;
    }

    public Boolean getCotiMinimumPayeEnSalarie() {
        return cotiMinimumPayeEnSalarie;
    }

    /**
     * @return
     */
    public java.lang.String getDateFacturation() {
        return dateFacturation;
    }

    @Override
    public java.lang.String getDateInformation() {
        return dateInformation;
    }

    /**
     * @return
     */
    public Boolean getDebutActivite() {
        return debutActivite;
    }

    @Override
    public java.lang.String getDebutDecision() {
        return debutDecision;
    }

    /**
     * Returns the dernierEtat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDernierEtat() {
        return dernierEtat;
    }

    /*
     * Retourne la déscription de la décision sour la forme Année décision - Type de décision - Genre de décision Ex:
     * 200 - Provisoire - Indépendant
     */
    public java.lang.String getDescriptionDecision() {
        String libGenre = "";
        String libDecision = "";
        try {
            if (!JadeStringUtil.isIntegerEmpty(genreAffilie)) {
                libGenre = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), genreAffilie);
            }
            if (!JadeStringUtil.isIntegerEmpty(typeDecision)) {
                libDecision = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), typeDecision);
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return anneeDecision + " - " + libDecision + " - " + libGenre;
    }

    /**
     * @return
     */
    public Boolean getDivision2() {
        return division2;
    }

    public Boolean getFacturation() {
        return facturation;
    }

    @Override
    public java.lang.String getFinDecision() {
        return finDecision;
    }

    @Override
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:17:23)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    public java.lang.String getIdCommune() {
        return idCommune;
    }

    public java.lang.String getIdCommunication() {
        return idCommunication;
    }

    public java.lang.String getIdConjoint() {
        return idConjoint;
    }

    /**
     * Getter
     */

    @Override
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public java.lang.String getIdDemandePortail() {
        return idDemandePortail;
    }

    public java.lang.String getIdIfdDefinitif() {
        return idIfdDefinitif;
    }

    public java.lang.String getIdIfdProvisoire() {
        return idIfdProvisoire;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdServiceSociale() {
        return idServiceSociale;
    }

    @Override
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public Boolean getImpression() {
        return impression;
    }

    public java.lang.String getInteret() {
        return interet;
    }

    /**
     * Returns the lettreSignature.
     * 
     * @return Boolean
     */
    public Boolean getLettreSignature() {
        return lettreSignature;
    }

    /**
     * Returns info si l'utilisateur a mis en Ged les
     * document de validation fiscale associés à la décision
     * 
     * @return Boolean
     */
    public Boolean getMiseEnGEDValidationRetour() {
        return miseEnGEDValidationRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2003 15:48:31)
     * 
     * @return globaz.globall.db.BManager
     */
    protected BManager getManager() {
        return new CPDecisionManager();
    }

    /**
     * @return
     */
    public java.lang.String getNombreMoisTotalDecision() {
        return nombreMoisTotalDecision;
    }

    /*
     * Retourne le N° Ifd de base pour la communication fiscale
     */
    public java.lang.String getNumIfd() {
        String numIfd = "";
        CPPeriodeFiscale periode = new CPPeriodeFiscale();
        periode.setSession(getSession());
        periode.setIdIfd(getIdIfdDefinitif());
        try {
            periode.retrieve();
            if ((periode != null) && !periode.isNew()) {
                numIfd = periode.getNumIfd();
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return numIfd;
    }

    public Boolean getOpposition() {
        return opposition;
    }

    /*
     * Retourne la périodicité (annuelle, trimestrielle...)
     */
    public java.lang.String getPeriodicite() {
        String periodicite = "";
        AFAffiliation affi = new AFAffiliation();
        affi.setSession(getSession());
        affi.setIdTiers(getIdTiers());
        try {
            affi = affi._getDerniereAffiliation();
            if ((affi != null) && !affi.isNew()) {
                periodicite = affi.getPeriodicite();
                if (!JadeStringUtil.isIntegerEmpty(periodicite)) {
                    periodicite = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), periodicite);
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return periodicite;
    }

    /**
     * @return
     */
    public Boolean getPremiereAssurance() {
        return premiereAssurance;
    }

    public String getProrata() {
        return prorata;
    }

    public Boolean getRecours() {
        return recours;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:01:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getResponsable() {
        return responsable;
    }

    /**
     * Returns the saveIdSortie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getSaveIdSortie() {
        return saveIdSortie;
    }

    /**
     * Returns the specification.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getSpecification() {
        return specification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaxation() {
        return taxation;
    }

    @Override
    public java.lang.String getTypeDecision() {
        return typeDecision;
    }

    /**
     * Verifie si 'il y a une décision en cours pour la même année Date de création : (31.03.2004 15:18:53)
     * 
     * @return boolean
     */
    public Boolean isDecisionEnCours(String idAffiliation, String annee) {
        CPDecisionManager decManager = new CPDecisionManager();
        decManager.setSession(getSession());
        /*
         * Remplacer recherche idTiers par idAffiliation à cause des cas qui sont indépendant et non actif en même temps
         */
        decManager.setForIdAffiliation(idAffiliation);
        decManager.setForAnneeDecision(annee);
        CPDecision myDecision = null;
        try {
            decManager.find();
            // Lecture des décisions de la même année
            for (int i = 0; i < decManager.getSize(); i++) {
                myDecision = (CPDecision) decManager.getEntity(i);
                // Aller rechercher le dernier état
                if ((!CPDecision.CS_FACTURATION.equalsIgnoreCase(myDecision.getDernierEtat()))
                        && (!CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(myDecision.getDernierEtat()))
                        && (!CPDecision.CS_SORTIE.equalsIgnoreCase(myDecision.getDernierEtat()))
                        && (!CPDecision.CS_REPRISE.equalsIgnoreCase(myDecision.getDernierEtat()))
                        && (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(myDecision.getTypeDecision()))
                        && (!CPDecision.CS_NON_SOUMIS.equalsIgnoreCase(myDecision.getGenreAffilie()))
                        // et si ce n'est pas la reprise annuelle, il y a une
                        // autre décision en cours
                        && !CPDecision.CS_ACOMPTE.equals(myDecision.getTypeDecision())) {
                    // Test si passage comptabilisé (utile pour décision pré
                    // encodées)
                    if (!JadeStringUtil.isEmpty(myDecision.getIdPassage())) {
                        FAPassage passage = new FAPassage();
                        passage.setSession(getSession());
                        passage.setIdPassage(myDecision.getIdPassage());
                        passage.retrieve();
                        if (!passage.isNew() && (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus()))) {
                            return (new Boolean(true));
                        }
                    }
                }
            }
            return (new Boolean(false));
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return (new Boolean(false));
        }
    }

    /**
     * 07.09.2007: Cette méthode retourne si la décision est de genre non actif comme provisoire au point de vue métier
     * 
     * @param myDecision
     * @return
     */
    public boolean isNonActif() {
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cette méthode retourne si la décision peut être considérée<br>
     * comme provisoire au point de vue métier
     * 
     * @param myDecision
     * @return
     */
    public boolean isProvisoireMetier() {
        if (CPDecision.CS_PROVISOIRE.equalsIgnoreCase(getTypeDecision())
                || CPDecision.CS_ACOMPTE.equalsIgnoreCase(getTypeDecision())
                || CPDecision.CS_CORRECTION.equalsIgnoreCase(getTypeDecision())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSuppressionExterne() {
        return suppressionExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @return AFAffiliation
     */
    public AFAffiliation loadAffiliation() {
        // enregistrement déjà chargé ?
        if (affiliation == null) {
            // liste pas encore chargée, on la charge
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getIdAffiliation())) {
                try {
                    affiliation.setAffiliationId(getIdAffiliation());
                    affiliation.retrieve();
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                }
            }
        }
        return affiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:46)
     * 
     * @return globaz.phenix.db.principale.CPCommunicationFiscale
     */
    public CPCommunicationFiscale loadCommunication() {
        // enregistrement déjà chargé ?
        if (communication == null) {
            // liste pas encore chargée, on la charge
            communication = new CPCommunicationFiscale();
            communication.setSession(getSession());
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdCommunication())) {
            try {
                communication.setIdCommunication(getIdCommunication());
                communication.retrieve();
            } catch (Exception e) {
                getSession().addError(e.getMessage());
            }
        }
        return communication;
    }

    /*
     * Retourne pour une année la dernière décision comptabilié (c'est à dire dont l'état est "Facturation" @parm=String
     * annee @return CPDecision
     */
    public globaz.phenix.db.principale.CPDecision loadDerniereDecision(int casTest) {
        try {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForAnneeDecision(getAnneeDecision());
            decManager.setForIdTiers(getIdTiers());
            decManager.setForIdAffiliation(getIdAffiliation());
            switch (casTest) {
                case 1:
                    decManager.setNotInTypeDecision(CPDecision.CS_IMPUTATION + ", " + CPDecision.CS_ACOMPTE);
                    break;

                case 2:
                    decManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
                    break;

                default:
                    decManager.setNotInTypeDecision(CPDecision.CS_IMPUTATION + ", " + CPDecision.CS_ACOMPTE);
                    break;
            }
            decManager.setForDebutDecision(getDebutDecision());
            decManager.setForFinDecision(getFinDecision());
            decManager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_REPRISE + ", "
                    + CPDecision.CS_PB_COMPTABILISATION);
            decManager.orderByIdDecision();
            decManager.find();
            if (decManager.size() <= 1) {
                // Recherche pour l'année concernée (période différente(
                decManager.setForDebutDecision("");
                decManager.setForFinDecision("");
                decManager.find();
            }
            if (decManager.isEmpty()) {
                return null;
            } else {
                return (CPDecision) decManager.getEntity(0);
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return null;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    public TITiersViewBean loadTiers() {
        // enregistrement déjà chargé ?
        if (tiers == null) {
            // liste pas encore chargée, on la charge
            tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                try {
                    tiers.setIdTiers(getIdTiers());
                    tiers.retrieve();
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                }
            }
        }
        return tiers;
    }

    private void miseAjourCodeActif(AFAffiliation affiliation, globaz.globall.db.BTransaction transaction,
            boolean remboursement, CPDecisionManager manager) throws Exception {
        // Nécessaire de remettre à jour le code actif pour que la décision soit prise en compte
        // Ex d'un cas radié que l'on reprend
        if (!remboursement) {
            manager.find(transaction);
            String anneeLimite = CPToolBox.anneeLimite(transaction);
            String anneeBk = "";
            for (int i = 0; i < manager.size(); i++) {
                CPDecision myDecision = (CPDecision) manager.getEntity(i);
                if (!anneeBk.equalsIgnoreCase(myDecision.getAnneeDecision())) {
                    anneeBk = myDecision.getAnneeDecision();
                    CPToolBox.miseAjourDecisionActive(transaction, affiliation, myDecision.getAnneeDecision(),
                            Integer.parseInt(anneeLimite));
                    CPToolBox.miseAjourImputation(transaction, affiliation, myDecision.getAnneeDecision(),
                            Integer.parseInt(anneeLimite));
                    CPToolBox.miseAjourRemise(transaction, affiliation, myDecision.getAnneeDecision(),
                            Integer.parseInt(anneeLimite));
                }
            }
        }
    }

    /**
     * @param boolean1
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setAnneeDecision(java.lang.String newAnneeDecision) {
        anneeDecision = newAnneeDecision;
    }

    public void setAnneePrise(java.lang.String newAnneePrise) {
        anneePrise = newAnneePrise;
    }

    public void setBloque(Boolean newBloque) {
        bloque = newBloque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:46)
     * 
     * @param newCommunication
     *            globaz.phenix.db.principale.CPCommunicationFiscale
     */
    public void setCommunication(CPCommunicationFiscale newCommunication) {
        communication = newCommunication;
    }

    /**
     * Sets the complementaire.
     * 
     * @param complementaire
     *            The complementaire to set
     */
    public void setComplementaire(Boolean complementaire) {
        this.complementaire = complementaire;
    }

    public void setCotiMinimumPayeEnSalarie(Boolean cotiMinimumPayeEnSalarie) {
        this.cotiMinimumPayeEnSalarie = cotiMinimumPayeEnSalarie;
    }

    /**
     * @param string
     */
    public void setDateFacturation(java.lang.String string) {
        dateFacturation = string;
    }

    public void setDateInformation(java.lang.String newDateInformation) {
        dateInformation = newDateInformation;
    }

    /**
     * @param boolean1
     */
    public void setDebutActivite(Boolean boolean1) {
        debutActivite = boolean1;
    }

    public void setDebutDecision(java.lang.String newDebutDecision) {
        debutDecision = newDebutDecision;
    }

    /**
     * Sets the dernierEtat.
     * 
     * @param dernierEtat
     *            The dernierEtat to set
     */
    public void setDernierEtat(java.lang.String dernierEtat) {
        this.dernierEtat = dernierEtat;
    }

    public void setDivision2(Boolean newDivision2) {
        division2 = newDivision2;
    }

    public void setFacturation(Boolean newFacturation) {
        facturation = newFacturation;
    }

    public void setFinDecision(java.lang.String newFinDecision) {
        finDecision = newFinDecision;
    }

    public void setGenreAffilie(java.lang.String newGenreAffilie) {
        genreAffilie = newGenreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:17:23)
     * 
     * @param newIdAffiliation
     *            java.lang.String
     */
    public void setIdAffiliation(java.lang.String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    public void setIdCommune(java.lang.String idCommune) {
        this.idCommune = idCommune;
    }

    public void setIdCommunication(java.lang.String newIdCommunication) {
        idCommunication = newIdCommunication;
    }

    public void setIdConjoint(java.lang.String newIdConjoint) {
        idConjoint = newIdConjoint;
    }

    /**
     * Setter
     */

    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setIdDemandePortail(java.lang.String idDemandePortail) {
        this.idDemandePortail = idDemandePortail;
    }

    public void setIdIfdDefinitif(java.lang.String newIdIfdDefinitif) {
        idIfdDefinitif = newIdIfdDefinitif;
    }

    public void setIdIfdProvisoire(java.lang.String newIdIfdProvisoire) {
        idIfdProvisoire = newIdIfdProvisoire;
    }

    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    public void setIdServiceSociale(java.lang.String idServiceSociale) {
        this.idServiceSociale = idServiceSociale;
    }

    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setImpression(Boolean newImpression) {
        impression = newImpression;
    }

    public void setInteret(java.lang.String newInteret) {
        interet = newInteret;
    }

    /**
     * Sets the lettreSignature.
     * 
     * @param lettreSignature
     *            The lettreSignature to set
     */
    public void setLettreSignature(Boolean lettreSignature) {
        this.lettreSignature = lettreSignature;
    }

    /**
     * Permet d'indiquer si l'utilisateur veut la mise en Ged des
     * document de validation fiscale associés à la décision
     * 
     * @param miseEnGEDValidationRetour
     */
    public void setMiseEnGEDValidationRetour(Boolean miseEnGEDValidationRetour) {
        this.miseEnGEDValidationRetour = miseEnGEDValidationRetour;
    }

    /**
     * @param string
     */
    public void setNombreMoisTotalDecision(String string) {
        nombreMoisTotalDecision = string;
    }

    public void setOpposition(Boolean newOpposition) {
        opposition = newOpposition;
    }

    /**
     * @param boolean1
     */
    public void setPremiereAssurance(Boolean boolean1) {
        premiereAssurance = boolean1;
    }

    public void setProrata(java.lang.String newProrata) {
        prorata = newProrata;
    }

    public void setRecours(Boolean recours) {
        this.recours = recours;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:01:46)
     * 
     * @param newResponsable
     *            java.lang.String
     */
    public void setResponsable(java.lang.String newResponsable) {
        responsable = newResponsable;
    }

    /**
     * Sets the saveIdSortie.
     * 
     * @param saveIdSortie
     *            The saveIdSortie to set
     */
    public void setSaveIdSortie(java.lang.String saveIdSortie) {
        this.saveIdSortie = saveIdSortie;
    }

    /**
     * Sets the specification.
     * 
     * @param specification
     *            The specification to set
     */
    public void setSpecification(java.lang.String specification) {
        this.specification = specification;
    }

    public void setSuppressionExterne(boolean suppressionExterne) {
        this.suppressionExterne = suppressionExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:47)
     * 
     * @param newTaxation
     *            java.lang.String
     */
    public void setTaxation(java.lang.String newTaxation) {
        taxation = newTaxation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @param newTiers
     *            globaz.pyxis.db.tiers.TITiersViewBean
     */
    public void setTiers(globaz.pyxis.db.tiers.TITiersViewBean newTiers) {
        tiers = newTiers;
    }

    public void setTypeDecision(java.lang.String newTypeDecision) {
        typeDecision = newTypeDecision;
    }

    protected void supprimerSortie(globaz.globall.db.BTransaction transaction, CPDecision myDecision) throws Exception {
        CPSortieManager sortieManager = new CPSortieManager();
        sortieManager.setSession(transaction.getSession());
        sortieManager.setForAnnee(myDecision.getAnneeDecision());
        sortieManager.setForIdAffiliation(myDecision.getIdAffiliation());
        // Effacer les montants cot pers et CI de sortie
        sortieManager.find(transaction);
        for (int k = 0; k < sortieManager.size(); k++) {
            CPSortie mySortie = (CPSortie) sortieManager.getEntity(k);
            if ((mySortie != null) && !mySortie.isNew()) {
                mySortie.delete(transaction);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        return "Décision : " + getIdDecision() + "\nDébut : " + getDebutDecision() + "\nFin : " + getFinDecision()
                + "\nNo. Affilié : " + loadAffiliation().getAffilieNumero();
    }

    public BTransaction withTransactionForExternalService() {
        return transactionForExternalService;
    }
}
