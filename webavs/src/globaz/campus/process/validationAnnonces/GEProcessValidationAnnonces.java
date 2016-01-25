package globaz.campus.process.validationAnnonces;

import globaz.campus.application.GEApplication;
import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.campus.db.lots.GELots;
import globaz.campus.util.GEUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.couverture.AFCouverture;
import globaz.naos.db.couverture.AFCouvertureManager;
import globaz.naos.db.planCaisse.AFPlanCaisse;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.alternate.TIPersonneAvsAdresseListViewBean;
import globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GEProcessValidationAnnonces extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatPassage = null;
    private String idLot = null;
    private String idPassageFacturation = null;
    private String libellePassageFacturation = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        String idPlanCaisse = null;
        String idTiersEtudiant = null;
        String idAffiliation = null;
        String idDecision = null;
        String idEtudiant = null;
        GELots lot = null;
        TIPersonneAvsManager tiersEtudiantMng = null;
        TIPersonneAvsAdresseListViewBean tiersEtudiantDetailleMng = null;
        TITiersViewBean tiersViewBean = null;
        AFAffiliation affiliation = null;
        CPDecisionManager decisionMng = null;
        GEAnnoncesManager annoncesMng = null;
        GEAnnonces annonce = null;
        GECreationMiseajourDonnees data = null;
        BStatement statement = null;
        BTransaction transactionCurseur = null;
        boolean result = true;
        int nbAnnonce = 0;
        try {
            // Recherche de l'id plan caisse étudiant
            idPlanCaisse = _getIdPlanCaisseEtudiant();
            if (JadeStringUtil.isBlankOrZero(idPlanCaisse)) {
                throw new Exception("Il n'existe pas de plan caisse pour les étudiants.");
            }
            // Recherche des annonces en fonction de l'idlot si il est renseigné
            annoncesMng = new GEAnnoncesManager();
            annoncesMng.setSession(getSession());
            if (!JadeStringUtil.isBlankOrZero(getIdLot())) {
                annoncesMng.setForIdLot(getIdLot());
            }
            annoncesMng.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_A_TRAITER);
            // D'abord traiter les annonces avant les imputations
            annoncesMng.orderByAnnoncesImputations();
            // Ouverture de la transaction du curseur
            transactionCurseur = new BTransaction(getSession());
            transactionCurseur.getSession().newTransaction();
            transactionCurseur.openTransaction();
            statement = annoncesMng.cursorOpen(transactionCurseur);
            // Recherche mode d'arrondi pour les frais cot. pers.
            int modeArrondiFad = CPToolBox.getModeArrondiFad(getTransaction());
            data = new GECreationMiseajourDonnees(getSession(), getTransaction());
            data.setModeArrondiFadCotPers(modeArrondiFad);
            data.creationSession(getSession());
            setProgressScaleValue(annoncesMng.getCount(getTransaction()));
            while (((annonce = (GEAnnonces) annoncesMng.cursorReadNext(statement)) != null) && (!annonce.isNew())) {
                try {
                    // Recherche des info du lot
                    if (lot == null) {
                        lot = new GELots();
                        lot.setSession(getSession());
                        lot.setIdLot(annonce.getIdLot());
                        lot.retrieve(getTransaction());
                        if ((lot == null) || lot.isNew()) {
                            throw new Exception("Aucun lot ne correspond à cette annonce (id: "
                                    + annonce.getIdAnnonce() + ")");
                        }
                    } else {
                        if (!annonce.getIdLot().equals(lot.getIdLot())) {
                            lot = new GELots();
                            lot.setSession(getSession());
                            lot.setIdLot(annonce.getIdLot());
                            lot.retrieve(getTransaction());
                            if ((lot == null) || lot.isNew()) {
                                throw new Exception("Aucun lot ne correspond à cette annonce (id: "
                                        + annonce.getIdAnnonce() + ")");
                            }
                        }
                    }
                    // Ne pas traiter les cas doctorants et postegrades et les
                    // mettre à l'état exempté
                    if (GEAnnonces.CS_DOCTORANT.equals(annonce.getCsCodeDoctorant())
                            || GEAnnonces.CS_POSTGRADE.equals(annonce.getCsCodeDoctorant())) {
                        annonce.setCsEtatAnnonce(GEAnnonces.CS_ETAT_EXEMPTE);
                    } else {
                        // réinitialisation
                        idTiersEtudiant = "0";
                        idAffiliation = null;
                        idDecision = null;
                        idEtudiant = null;
                        tiersEtudiantMng = null;
                        tiersEtudiantDetailleMng = null;
                        tiersViewBean = null;
                        affiliation = null;
                        decisionMng = null;
                        int sizeTiersAvs = 0;
                        if (!annonce.getIsImputation().booleanValue()) {
                            // On crée directement le tiers si il est forcé
                            if (annonce.getIsTiersForce().booleanValue()) {
                                // Creation du tiers car aucun tiers n'existe
                                // pour cet étudiant
                                tiersViewBean = data.creationMiseAJourTiers(annonce, idTiersEtudiant, lot.getAnnee(),
                                        this);
                                idTiersEtudiant = tiersViewBean.getIdTiers();
                            } else {
                                // Traitement annonce
                                // Recherche de l'étudiant en fonction du numéro
                                // d'immatriculation
                                // transmis et de l'id tiers école
                                if (!JadeStringUtil.isEmpty(annonce.getNumImmatriculationTransmis())) {
                                    GEAnnoncesManager annoncesManager = new GEAnnoncesManager();
                                    annoncesManager.setSession(getSession());
                                    annoncesManager.setForNumImmatriculationTransmis(annonce
                                            .getNumImmatriculationTransmis());
                                    annoncesManager.setForValidationAnnonce(true);
                                    annoncesManager.setForIdTiersEcole(lot.getIdTiersEcole());
                                    annoncesManager.find();
                                    if (annoncesManager.size() > 0) {
                                        GEAnnonces annonceImmatriculation = (GEAnnonces) annoncesManager
                                                .getFirstEntity();
                                        idTiersEtudiant = annonceImmatriculation.getIdTiers(getSession());
                                        if ((idTiersEtudiant != "0") && (idTiersEtudiant != null)) {
                                            tiersViewBean = data.creationMiseAJourTiers(annonce, idTiersEtudiant,
                                                    lot.getAnnee(), this);
                                        } else {
                                            idTiersEtudiant = "0";
                                        }
                                    }
                                }
                                if (idTiersEtudiant == "0") {
                                    // Si l'étudiant n'existe pas
                                    // Recherche du tiers par num AVS
                                    if (!JadeStringUtil.isBlank(annonce.getNumAvs())) {
                                        tiersEtudiantMng = new TIPersonneAvsManager();
                                        tiersEtudiantMng.setSession(getSession());
                                        tiersEtudiantMng.setForNumAvsActuel(annonce.getNumAvs());
                                        tiersEtudiantMng.setForIncludeInactif(new Boolean(false));
                                        tiersEtudiantMng.find(getTransaction());
                                        sizeTiersAvs = tiersEtudiantMng.size();
                                    }
                                    // Si l'étudiant n'existe pas
                                    if (sizeTiersAvs == 0) {
                                        // Recherche par Nom, Prenom, Sexe, Date
                                        // de Naissance
                                        tiersEtudiantDetailleMng = new TIPersonneAvsAdresseListViewBean();
                                        tiersEtudiantDetailleMng.setSession(getSession());
                                        tiersEtudiantDetailleMng.setForDesignationUpper1(annonce.getNom());
                                        tiersEtudiantDetailleMng.setForDesignationUpper2(annonce.getPrenom());
                                        tiersEtudiantDetailleMng.setForSexe(annonce.getCsSexe());
                                        tiersEtudiantDetailleMng.setForDateNaissance(annonce.getDateNaissance());
                                        tiersEtudiantDetailleMng.setForIncludeInactif(new Boolean(false));
                                        tiersEtudiantDetailleMng.setForSingleAdresseMode(new Boolean(true));
                                        tiersEtudiantDetailleMng.find(getTransaction());
                                        Set idTiersSet = new HashSet();
                                        // On recherche par sexe, dateNaissance
                                        // et 2 prèmieres lettres en majuscules
                                        if (tiersEtudiantDetailleMng.size() <= 0) {
                                            // 1ère recherche äbischer on
                                            // recherche ABischer
                                            tiersEtudiantDetailleMng = new TIPersonneAvsAdresseListViewBean();
                                            tiersEtudiantDetailleMng.setSession(getSession());
                                            // On prend les 2 premières lettres
                                            // en majuscules et sans les
                                            // accents.
                                            tiersEtudiantDetailleMng.setForDesignationUpper1Like(JadeStringUtil
                                                    .substring(GEUtil.convertSpecialChars(annonce.getNom())
                                                            .toUpperCase(), 0, 2));
                                            tiersEtudiantDetailleMng.setForSexe(annonce.getCsSexe());
                                            tiersEtudiantDetailleMng.setForDateNaissance(annonce.getDateNaissance());
                                            tiersEtudiantDetailleMng.setForIncludeInactif(new Boolean(false));
                                            tiersEtudiantDetailleMng.setForSingleAdresseMode(new Boolean(true));
                                            tiersEtudiantDetailleMng.find(getTransaction());
                                            if (tiersEtudiantDetailleMng.size() > 0) {
                                                throw new Exception(
                                                        "Il existe plusieurs tiers ayant le même sexe, la même date de naissance et les mêmes 2 premières lettres du nom de famille.");
                                            } else {
                                                // 2ème recherche äbischer on
                                                // recherche AEbischer
                                                tiersEtudiantDetailleMng = new TIPersonneAvsAdresseListViewBean();
                                                tiersEtudiantDetailleMng.setSession(getSession());
                                                // On prend les 2 premières
                                                // lettres en majuscules et sans
                                                // les accents.
                                                tiersEtudiantDetailleMng.setForDesignationUpper1Like(JadeStringUtil
                                                        .substring(GEUtil.convertSpecialCharsWithE(annonce.getNom())
                                                                .toUpperCase(), 0, 2));
                                                tiersEtudiantDetailleMng.setForSexe(annonce.getCsSexe());
                                                tiersEtudiantDetailleMng
                                                        .setForDateNaissance(annonce.getDateNaissance());
                                                tiersEtudiantDetailleMng.setForIncludeInactif(new Boolean(false));
                                                tiersEtudiantDetailleMng.setForSingleAdresseMode(new Boolean(true));
                                                tiersEtudiantDetailleMng.find(getTransaction());
                                                if (tiersEtudiantDetailleMng.size() > 0) {
                                                    throw new Exception(
                                                            "Il existe plusieurs tiers ayant le même sexe, la même date de naissance et les mêmes 2 premières lettres du nom de famille.");
                                                }
                                            }

                                        }
                                        // Pour avoir les idTiers uniques (en
                                        // cas de plusieurs affiliations sur le
                                        // même tiers
                                        for (Iterator it = tiersEtudiantDetailleMng.iterator(); it.hasNext();) {
                                            TIPersonneAvsAdresseViewBean entity = (TIPersonneAvsAdresseViewBean) it
                                                    .next();
                                            idTiersSet.add(entity.getIdTiers());
                                        }
                                        if (idTiersSet.size() == 0) {
                                            // Creation du tiers car aucun tiers
                                            // n'existe pour cet étudiant
                                            tiersViewBean = data.creationMiseAJourTiers(annonce, idTiersEtudiant,
                                                    lot.getAnnee(), this);
                                            idTiersEtudiant = tiersViewBean.getIdTiers();
                                        } else if (idTiersSet.size() == 1) {
                                            idTiersEtudiant = ((TIPersonneAvsAdresseViewBean) tiersEtudiantDetailleMng
                                                    .getFirstEntity()).getIdTiers();
                                            // Mise à jour du tiers
                                            tiersViewBean = data.creationMiseAJourTiers(annonce, idTiersEtudiant,
                                                    lot.getAnnee(), this);
                                        } else {
                                            throw new Exception(
                                                    "Il existe plusieurs tiers ayant le même nom, prénom, sexe et date de naissance.");
                                        }
                                    } else if (tiersEtudiantMng.size() == 1) {
                                        idTiersEtudiant = ((TITiersViewBean) tiersEtudiantMng.getFirstEntity())
                                                .getIdTiers();
                                        tiersViewBean = data.creationMiseAJourTiers(annonce, idTiersEtudiant,
                                                lot.getAnnee(), this);
                                    } else {
                                        throw new Exception("Il existe plusieurs tiers ayant le numéro AVS.");
                                    }
                                }
                            }

                            if (!JadeStringUtil.isBlankOrZero(idTiersEtudiant)) {
                                // creation de l'étudiant
                                idEtudiant = data.creationEtudiant(idTiersEtudiant, lot.getIdTiersEcole(),
                                        annonce.getNumImmatriculationTransmis());
                                // setter l'id de l'étudiant
                                annonce.setIdEtudiant(idEtudiant);
                                if (!JadeStringUtil.isBlankOrZero(idEtudiant)) {
                                    // On test si l'utilisateur possède une
                                    // affiliation
                                    if (!possedeAffiliation(idTiersEtudiant, lot.getAnnee())) {
                                        // Creation de l'affiliation
                                        affiliation = data.creationAffiliation(annonce, tiersViewBean, lot.getAnnee(),
                                                idPlanCaisse);
                                        idAffiliation = affiliation.getAffiliationId();
                                        if (!JadeStringUtil.isBlankOrZero(idAffiliation)) {
                                            // Creation de la décision si elle
                                            // n'existe pas déjà
                                            decisionMng = new CPDecisionManager();
                                            decisionMng.setSession(getSession());
                                            decisionMng.setForIdTiers(idTiersEtudiant);
                                            decisionMng.setForIdAffiliation(idAffiliation);
                                            decisionMng.setForAnneeDecision(lot.getAnnee());
                                            decisionMng.find(getTransaction());
                                            if (decisionMng.size() > 0) {
                                                throw new Exception("Il existe déjà une décision pour l'année "
                                                        + lot.getAnnee());
                                            } else {
                                                idDecision = data.creationDecisionCotisation(affiliation,
                                                        tiersViewBean, idPassageFacturation, annonce);
                                                annonce.setIdDecision(idDecision);

                                            }
                                        }
                                    } else {
                                        throw new Exception("Le tiers possède déjà une affiliation pour l'année.");
                                    }
                                }
                            }
                            if (JadeStringUtil.isBlank(annonce.getMessagesErreurs())) {
                                if (JadeStringUtil.isBlankOrZero(idTiersEtudiant)) {
                                    throw new Exception("Tiers inconnu ou impossible à créer.");
                                }
                                if (JadeStringUtil.isBlankOrZero(idEtudiant)) {
                                    throw new Exception("Impossible de créer l'étudiant.");
                                }
                                if (JadeStringUtil.isBlankOrZero(idAffiliation)) {
                                    throw new Exception("Impossible de créer l'affiliation.");
                                }
                                if (JadeStringUtil.isBlankOrZero(idDecision)) {
                                    throw new Exception("Impossible de créer la décision.");
                                }
                            }
                        } else {
                            // Traitement imputations
                            if (JadeStringUtil.isBlankOrZero(annonce.getIdAnnonceParent())) {
                                throw new Exception("Aucune annonce ne correspond à cette imputation.");
                            }
                            GEAnnonces annonceParent = new GEAnnonces();
                            annonceParent.setSession(getSession());
                            annonceParent.setIdAnnonce(annonce.getIdAnnonceParent());
                            annonceParent.retrieve(getTransaction());
                            if ((annonceParent == null) || annonceParent.isNew()) {
                                throw new Exception("Aucune annonce ne correspond à cette imputation.");
                            }
                            if (GEAnnonces.CS_ETAT_ERREUR.equals(annonceParent.getCsEtatAnnonce())) {
                                throw new Exception(getSession().getLabel("ANNONCE_PARENT_EN_ERREUR"));
                            }
                            // Recherche de la décision
                            if (JadeStringUtil.isBlankOrZero(annonceParent.getIdDecision())) {
                                throw new Exception("Aucune décision de base existante pour cette imputation.");
                            }
                            CPDecision decisionParent = new CPDecision();
                            decisionParent.setSession(getSession());
                            decisionParent.setIdDecision(annonceParent.getIdDecision());
                            decisionParent.retrieve(getTransaction());
                            if ((decisionParent == null) || decisionParent.isNew()) {
                                throw new Exception("Aucune décision de base existante pour cette imputation.");
                            }
                            // Recherche affiliation
                            affiliation = new AFAffiliation();
                            affiliation.setSession(getSession());
                            affiliation.setAffiliationId(decisionParent.getIdAffiliation());
                            affiliation.retrieve(getTransaction());
                            if ((affiliation == null) || affiliation.isNew()) {
                                throw new Exception("Aucune affiliation existante pour cette imputation.");
                            }
                            // Recherche tiers
                            tiersViewBean = new TITiersViewBean();
                            tiersViewBean.setSession(getSession());
                            tiersViewBean.setIdTiers(affiliation.getIdTiers());
                            tiersViewBean.retrieve(getTransaction());
                            if ((tiersViewBean == null) || tiersViewBean.isNew()) {
                                throw new Exception("Aucun tiers existante pour cette imputation.");
                            }
                            // Création de l'imputation
                            idDecision = data.creationDecisionCotisation(affiliation, tiersViewBean,
                                    idPassageFacturation, annonce);
                            annonce.setIdDecision(idDecision);
                        }
                        annonce.setCsEtatAnnonce(GEAnnonces.CS_ETAT_VALIDE);
                    }
                    nbAnnonce++;
                    annonce.wantCallValidate(false);
                    annonce.wantCallMethodBefore(false);
                    annonce.wantCallMethodAfter(false);
                    if (!JadeStringUtil.isBlank(annonce.getMessagesErreurs())) {
                        annonce.creationLogMessages(annonce.getMessagesErreurs(), getTransaction());
                    }
                    annonce.update(getTransaction());
                } catch (Exception e) {
                    getTransaction().clearErrorBuffer();
                    if (annonce != null) {
                        getMemoryLog().logMessage(
                                "(" + annonce.getNumImmatriculationTransmis() + ", " + annonce.getNom() + ", "
                                        + annonce.getPrenom() + ")" + e.getMessage().toString(),
                                FWViewBeanInterface.WARNING, "ProcessValidationAnnonces");
                        annonce.wantCallValidate(false);
                        annonce.wantCallMethodBefore(false);
                        annonce.wantCallMethodAfter(false);
                        annonce.setMessagesErreurs(e.getMessage().toString());
                        annonce.creationLogMessages(annonce.getMessagesErreurs(), getTransaction());
                        annonce.update(getTransaction());
                    } else {
                        getTransaction().rollback();
                    }
                }
                incProgressCounter();
                getTransaction().commit();
            }
            annoncesMng.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_VALIDE);
            if (annoncesMng.getCount(getTransaction()) >= 1) {
                if ((lot != null) && !lot.isNew()) {
                    if (GELots.CS_ETAT_A_TRAITER.equals(lot.getCsEtatLot())) {
                        lot.setCsEtatLot(GELots.CS_ETAT_VALIDE);
                        lot.wantCallMethodBefore(false);
                        lot.update(getTransaction());
                    }
                }
            }
            // Information dans l'email pour le nombre d'annonces / imputations
            // validées
            getMemoryLog().logMessage("Nombre d'annonces / imputations validées: " + nbAnnonce, FWViewBeanInterface.OK,
                    "ProcessValidationAnnonces");
        } catch (Exception e) {
            getMemoryLog()
                    .logMessage(e.getMessage().toString(), FWViewBeanInterface.ERROR, "ProcessValidationAnnonces");
            result = false;
        } finally {
            try {
                if (annoncesMng != null) {
                    annoncesMng.cursorClose(statement);
                }
            } finally {
                if ((transactionCurseur != null) && transactionCurseur.isOpened()) {
                    transactionCurseur.closeTransaction();
                }
            }
        }
        return result;
    }

    private String _getIdPlanCaisseEtudiant() throws Exception {
        // Recherche de l'id plan caisse étudiant
        String idPlanCaisse = ((GEApplication) GlobazSystem.getApplication(GEApplication.DEFAULT_APPLICATION_CAMPUS))
                .getIdPlanCaisseEtudiant();
        if (JadeStringUtil.isBlankOrZero(idPlanCaisse)) {
            throw new Exception("La propriété idPlanCaisseEtudiant n'est pas définie");
        }
        // Recherche du plan caisse étudiant en fonction de la propriété
        AFPlanCaisse planCaisse = new AFPlanCaisse();
        planCaisse.setSession(getSession());
        planCaisse.setPlanCaisseId(idPlanCaisse);
        planCaisse.retrieve(getTransaction());
        if ((planCaisse == null) || planCaisse.isNew()) {
            throw new Exception("Il n'existe pas de plan caisse pour les étudiants avec l'id " + idPlanCaisse);
        }
        // Recherche de la couverture
        AFCouvertureManager couvertureMng = new AFCouvertureManager();
        couvertureMng.setSession(getSession());
        couvertureMng.setForPlanCaisseId(idPlanCaisse);
        couvertureMng.find(getTransaction());
        boolean trouve = false;
        int i = 0;
        // Tant qu'une assurance de type cot pers n'est pas trouvé...
        while ((i < couvertureMng.size()) && !trouve) {
            AFCouverture couverture = (AFCouverture) couvertureMng.getEntity(i);
            AFAssurance assurance = couverture.getAssurance();
            if (CodeSystem.GENRE_ASS_PERSONNEL.equals(assurance.getAssuranceGenre())
                    && CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(assurance.getTypeAssurance())) {
                trouve = true;
            }
            i++;
        }
        if (!trouve) {
            throw new Exception("Il n'existe pas d'assurance cot pers dans le plan caisse " + idPlanCaisse);
        }
        return idPlanCaisse;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getIdPassageFacturation())) {
            this._addError(getSession().getLabel("PASSAGE_FACTURATION_OBLIGATOIRE"));
        }
        if (!FAPassage.CS_ETAT_OUVERT.equals(etatPassage)) {
            this._addError(getSession().getLabel("PASSAGE_FACTURATION_OUVERT"));
        }
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }

    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted() || getMemoryLog().getErrorLevel().equals(FWMessage.ERREUR)) {
            if (JadeStringUtil.isBlankOrZero(getIdLot())) {
                return getSession().getLabel("RESULTAT_PROCESS_VALIDATION_KO");
            } else {
                return getSession().getLabel("RESULTAT_PROCESS_VALIDATION_PAR_LOT_KO") + getIdLot();
            }
        } else {
            if (JadeStringUtil.isBlankOrZero(getIdLot())) {
                return getSession().getLabel("RESULTAT_PROCESS_VALIDATION_OK");
            } else {
                return getSession().getLabel("RESULTAT_PROCESS_VALIDATION_PAR_LOT_OK") + getIdLot();
            }
        }
    }

    public String getEtatPassage() {
        return etatPassage;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getLibellePassageFacturation() {
        return libellePassageFacturation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private boolean possedeAffiliation(String idTiersEtudiant, String annee) {
        try {
            AFAffiliation aff = CPToolBox.returnAffiliation(getSession(), getTransaction(), idTiersEtudiant, annee, "",
                    1);
            if (aff != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    public void setEtatPassage(String etatPassage) {
        this.etatPassage = etatPassage;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setLibellePassageFacturation(String libellePassageFacturation) {
        this.libellePassageFacturation = libellePassageFacturation;
    }
}
