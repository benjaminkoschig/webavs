package globaz.draco.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationListViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationViewBean;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

/**
 * Processus de facturation des déclarations de salaires
 * 
 * @author Sébastien Chappatte
 */
public final class DSProcessFacturationDeclarationSalaire extends BProcess {

    private static final long serialVersionUID = -3655002736380549669L;
    public static int MODE_SANS_CRTL_EMPL = 1;
    public static int MODE_STANDARD = 0;
    public static int MODE_UNIQUE_CTRL_EMPL = 2;

    private String _noDecompte = null;
    private FAAfact afact = null;
    private boolean aMettreEnErreur = false;
    private boolean avantRegeneration = false;
    private DSDeclarationViewBean declaration = null;
    private BISession dracoSession = null;
    private DSDeclarationListViewBean manager = null;
    private int modeFacturation = DSProcessFacturationDeclarationSalaire.MODE_STANDARD;
    private FAEnteteFacture nouveauEntFacture = null;
    private BISession osirisSession = null;
    private globaz.musca.db.facturation.FAPassage passage = null;
    private BISession pyxisSession = null;
    private int shouldNbCas = 0;

    /**
     * Commentaire relatif au constructeur DSProcessFacturationDeclarationSalaire.
     */
    public DSProcessFacturationDeclarationSalaire() {
        super();
    }

    /**
     * Commentaire relatif au constructeur DSProcessFacturationDeclarationSalaire.
     * 
     * @param parent
     *            globaz.framework.process.FWProcess
     */
    public DSProcessFacturationDeclarationSalaire(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Cette méthode exécute le processus de facturation des déclarations de salaires. Date de création : (14.02.2002
     * 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // Création d'une session Draco
            dracoSession = GlobazSystem.getApplication("DRACO").newSession(getSession());
            // Création d'une session Pyxis
            pyxisSession = GlobazSystem.getApplication("PYXIS").newSession(getSession());
            // Création d'une session Osiris
            osirisSession = GlobazSystem.getApplication("OSIRIS").newSession(getSession());
            // initialiseDecision lit les déclarations qui ont l'idPassgeFac
            // identique à l'idPassage
            // donné par le processus et remet idPassageFac à zéro
            if (!initialiseDeclaration()) {
                return false;
            }
            // Instancier le manager des déclarations de salaires
            manager = new DSDeclarationListViewBean();
            manager.setISession(dracoSession);
            manager.setForIdPassageFac("0");
            manager.setForEtat(DSDeclarationViewBean.CS_AFACTURER);
            manager.setForSelectionTri("3");
            manager.changeManagerSize(BManager.SIZE_NOLIMIT);
            if (modeFacturation == DSProcessFacturationDeclarationSalaire.MODE_UNIQUE_CTRL_EMPL) {
                manager.setForTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
            }
            if (modeFacturation == DSProcessFacturationDeclarationSalaire.MODE_SANS_CRTL_EMPL) {
                manager.setForNotTypeDeclaration(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR);
            }
            manager.find(getTransaction());
            afact = new FAAfact();
            /*--------------------------------------------------------------------
             Initiliser la progression du process
             ---------------------------------------------------------------------
             */
            shouldNbCas = manager.getCount(getTransaction()); // provisoire
            // Entrer les informations pour l' état du process
            getParent().setState("(" + passage.getIdPassage() + ") Passage");
            if (shouldNbCas > 0) {
                getParent().setProgressScaleValue(shouldNbCas);
            } else {
                getParent().setProgressScaleValue(1);
            }
            // ---------------------------------------------------------------------
            // ---------------------------------------------------------------------
            long progressCounter = -1; // compteur du progression
            // ---------------------------------------------------------------------

            Vector messages = new Vector();
            for (int i = 0; i < manager.size(); i++) {
                if (isAborted()) {
                    break;
                }
                declaration = (DSDeclarationViewBean) manager.get(i);

                // Progression
                getParent().setProgressCounter(progressCounter++);

                _noDecompte = initDefaultNumDecompte();

                String lastSection = findLastSection(_noDecompte);
                if (lastSection != null) {
                    _noDecompte = incrementeNoDecompte(lastSection);
                    if ((_noDecompte.length() == 9) && _noDecompte.substring(6, 7).equals("9")) {
                        getTransaction().addErrors(
                                "Maximum section number reached, over xxxxx890. Section can not be created "
                                        + declaration.getAffilieNumero() + " - " + _noDecompte);
                    }
                }

                String decomptePassageMax = findLastNoDecomptePassage(_noDecompte);
                if (decomptePassageMax != null) {
                    // incrémenter no decompte de la dernière en-tête trouvée
                    _noDecompte = incrementeNoDecompte(decomptePassageMax);
                }

                try {
                    // Mise à jour des tables
                    if (!updateTables()) {
                        // continue;
                    } // Validation finale

                    if (getTransaction().hasErrors()) {
                        rollbackTransaction(((BSession) dracoSession).getLabel("FACTURATION_ERREUR")
                                + declaration.getIdDeclaration());
                        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                        aMettreEnErreur = true;
                    } else {
                        getTransaction().commit();
                    } // Sortie si erreur fatale
                } catch (Exception e) {
                    getMemoryLog().logMessage(
                            ((BSession) dracoSession).getLabel("FACTURATION_DECL_NON_TRAITEE_1")
                                    + declaration.getAffilieNumero()
                                    + ((BSession) dracoSession).getLabel("FACTURATION_DECL_NON_TRAITEE_2"),
                            FWMessage.ERREUR, this.getClass().getName());
                } finally {
                    for (Iterator iterMessages = getMemoryLog().getMessagesToVector().iterator(); iterMessages
                            .hasNext();) {
                        messages.add(iterMessages.next());
                    }
                    getMemoryLog().clear();
                }

            }
            for (Iterator iter = messages.iterator(); iter.hasNext();) {
                getMemoryLog().getMessagesToVector().add(iter.next());
            }
            if (aMettreEnErreur) {
                abort();
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            abort();
            return false;
        }
        return true;
    }

    /**
     * @param nonImprimable
     * @return
     */
    private String enteteFactureIdCSModeImpressionSetter(Boolean nonImprimable) {

        if (nonImprimable) {
            return FAEnteteFacture.CS_MODE_IMP_SEPAREE;
        } else if (declaration.getNotImpressionDecFinalAZero()) {
            return FAEnteteFacture.CS_MODE_IMP_PASIMPZERO;
        } else {
            return FAEnteteFacture.CS_MODE_IMP_STANDARD;
        }
    }

    /**
     * Récupérer le compte annexe
     * 
     * @return
     * @throws Exception
     */
    private CACompteAnnexe findCompteAnnexe() throws Exception {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setISession(osirisSession);
        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compteAnnexe.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                getSession().getApplication()));
        compteAnnexe.setIdExterneRole(declaration.getAffiliation().getAffilieNumero());
        compteAnnexe.retrieve(getTransaction());
        return compteAnnexe;
    }

    /**
     * @return
     * @throws Exception
     */
    private FAEnteteFactureManager findEnteteFacture(String idPassage, String numAffilie, String idTiers)
            throws Exception {
        // Recherche les decomptes du passage pour un affilié
        FAEnteteFactureManager entete = new FAEnteteFactureManager();
        entete.setSession(getSession());
        entete.setForIdPassage(idPassage);
        entete.setForIdExterneRole(numAffilie);
        entete.setForIdTiers(idTiers);
        entete.find(getTransaction());
        return entete;
    }

    /**
     * @param noDecompte
     *            numéro de décompte courant
     * @return le dernier no de décompte du passage ou null si rien n'est trouvé
     * @throws Exception
     */
    private String findLastNoDecomptePassage(String noDecompte) throws Exception {
        FAEnteteFactureManager enteteManager = findEnteteFacture(passage.getIdPassage(), declaration.getAffiliation()
                .getAffilieNumero(), declaration.getAffiliation().getIdTiers());

        int noDecomptePassageMax = 0;
        Boolean decompteExistant = false;
        // recherche sur les en-tête déjà existantes
        for (int j = 0; j < enteteManager.size(); j++) {
            FAEnteteFacture entete = (FAEnteteFacture) enteteManager.getEntity(j);

            if (noDecompte.equals(entete.getIdExterneFacture())) {
                decompteExistant = true;
            }

            // recherche et sauvegarde du dernier id externe facturation
            String idFactStart = entete.getIdExterneFacture().substring(0, 6);
            int noDecompteEntete = Integer.parseInt(entete.getIdExterneFacture());

            if (noDecompte.substring(0, 6).equals(idFactStart) && (noDecomptePassageMax < noDecompteEntete)) {
                noDecomptePassageMax = noDecompteEntete;
            }
        }

        if ((noDecomptePassageMax == 0) || !decompteExistant) {
            return null;
        } else {
            return String.valueOf(noDecomptePassageMax);
        }
    }

    /**
     * Vérifie si une section existe en comptabilité auxiliaire pour ce numéro de décompte
     * 
     * @return
     * @throws Exception
     */
    private String findLastSection(String noDecompte) throws Exception {
        // Récupérer le compte annexe
        CACompteAnnexe compteAnnexe = findCompteAnnexe();

        // Contrôler si le numéro de section(décompte) existe déjà dans
        // la compta auxiliaire
        int lastSection = 0;

        if (compteAnnexe.isNew()) {
            return null;
        }

        Boolean decompteExistant = false;

        CASectionManager sectionManager = findSectionsLikeIdExterne(noDecompte, compteAnnexe.getIdCompteAnnexe());
        for (int i = 0; i < sectionManager.size(); i++) {
            CASection section = (CASection) sectionManager.getEntity(i);

            if (noDecompte.equals(section.getIdExterne())) {
                decompteExistant = true;
            }

            String idFactStart = section.getIdExterne().substring(0, 6);
            int noSection = Integer.parseInt(section.getIdExterne());
            // Modif JMC => ne pas tenir compte des décisions d'intêret type 2010189...
            if (noDecompte.substring(0, 6).equals(idFactStart) && (lastSection < noSection)
                    && !"9".equals(section.getIdExterne().substring(6, 7))) {
                lastSection = noSection;
            }
        }

        if ((lastSection == 0) || !decompteExistant) {
            return null;
        } else {
            return String.valueOf(lastSection);
        }
    }

    /**
     * @param noDecompte
     * @param compteAnnexe
     * @return
     * @throws Exception
     */
    private CASectionManager findSectionsLikeIdExterne(String noDecompte, String idCompteAnnexe) throws Exception {
        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setISession(osirisSession);
        sectionManager.setForIdCompteAnnexe(idCompteAnnexe);
        sectionManager.setForIdTypeSection(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
        sectionManager.setLikeIdExterne(noDecompte.substring(0, 6));
        sectionManager.find(getTransaction());
        return sectionManager;
    }

    /**
     * Cette méthode détermine l'objet du message en fonction du code d'erreur
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj;
        obj = "";
        return obj;
    }

    public int getModeFacturation() {
        return modeFacturation;
    }

    /**
     * Incremente de 10 le numéro de décompte
     * 
     * @param noDecompte
     *            numéro à incrémenter
     * @return le numéro de décompte incrémenté
     */
    private String incrementeNoDecompte(String noDecompte) {
        // On incrémente le no de décompte et on reteste
        Integer iNoDecompte = Integer.valueOf(noDecompte);
        iNoDecompte += 1;
        return iNoDecompte.toString();
    }

    /**
     * Créer un numéro de décompte si inexistant
     * 
     * @return le numéro de décompte par défaut
     */
    private String initDefaultNumDecompte() {
        String noDecompte = "";
        // Créer un numéro de décompte si inexistant
        if (JadeStringUtil.isIntegerEmpty(declaration.getNoDecompte())) {
            // Génére le No en fonction du décompte
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "13000";
            } else if (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "18000";
            } else if (DSDeclarationViewBean.CS_ICI.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "34000";
            } else if (DSDeclarationViewBean.CS_DIVIDENDE.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "35000";
            } else if (DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "14000";
            } else if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "17000";
            } else if (DSDeclarationViewBean.CS_LTN.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "33000";
            } else if (DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "38000";
            } else if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                noDecompte = declaration.getAnnee() + "36000";
            } else {
                // Ne devrait pas arriver...
                noDecompte = declaration.getAnnee() + "13000";
            }

        } else {
            noDecompte = declaration.getNoDecompte();
        }

        return noDecompte;
    }

    /**
     * Cette méthode effectue l'initialisation de la déclaration Elle remet idPassageFac pour les décisions qui un
     * idpassage identique à celui donné par le processus
     */
    private boolean initialiseDeclaration() throws Exception {
        return true;
    }

    /**
     * Returns the avantRegeneration.
     * 
     * @return boolean
     */
    public boolean isAvantRegeneration() {
        return avantRegeneration;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Cette méthode permet d'effectuer un rollback de la transaction
     * 
     * @param message
     *            java.lang.String
     */
    private void rollbackTransaction(String message) {
        // Logger l'erreur
        getMemoryLog().logMessage(message, FWMessage.ERREUR, this.getClass().getName());
        // Logger les messages d'erreur de la transaction
        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        try {
            getTransaction().rollback();
            getTransaction().clearErrorBuffer();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.error(this, e);
        }
    }

    /**
     * Sets the avantRegeneration.
     * 
     * @param avantRegeneration
     *            The avantRegeneration to set
     */
    public void setAvantRegeneration(boolean avantRegeneration) {
        this.avantRegeneration = avantRegeneration;
    }

    public void setModeFacturation(int modeFacturation) {
        this.modeFacturation = modeFacturation;
    }

    /**
     * Method setPassage. Utilise le passage passé en paramètre depuis la facturation
     * 
     * @param passage
     *            passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Cette méthode effectue la mise à jour des tables suivantes : DSDECLP Entête déclaration de salaires DSDELIP Ligne
     * de déclaration de salaires FAAFACP Ligne de facture FAENTFP Entête facture
     */
    private boolean updateTables() throws Exception {
        // La déclaration et en cours de facturation
        try {
            declaration.setEnFacturation(true);
            // Recherche de l'entête de facture
            FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
            entFactureManager.setISession(getSession());
            entFactureManager.setForIdTiers(declaration.getAffiliation().getIdTiers());
            entFactureManager.setForIdPassage(passage.getIdPassage());
            entFactureManager.setForIdTypeFacture("1");
            entFactureManager.setForIdExterneRole(declaration.getAffiliation().getAffilieNumero());
            entFactureManager.setForIdExterneFacture(_noDecompte);
            entFactureManager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                    getSession().getApplication()));
            entFactureManager.find(getTransaction());
            String idEnteteFacture = "";
            // Si aucun en-tête trouvé, création d'un nouvel en-tête de facture
            if (entFactureManager.size() == 0) {
                nouveauEntFacture = new FAEnteteFacture();
                nouveauEntFacture.setISession(getSession());
                nouveauEntFacture.setIdPassage(passage.getIdPassage());
                nouveauEntFacture.setIdTiers(declaration.getAffiliation().getIdTiers());
                nouveauEntFacture.setDateReceptionDS(declaration.getDateRetourEff());
                nouveauEntFacture.setIdSoumisInteretsMoratoires(declaration.getSoumisInteret());
                nouveauEntFacture.setReferenceFacture(declaration.getReferenceFacture());
                // Contrôler que le tiers existe
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setISession(pyxisSession);
                tiers.setIdTiers(declaration.getAffiliation().getIdTiers());
                tiers.retrieve(getTransaction());
                if (!tiers.isNew()) {
                    nouveauEntFacture.setIdExterneRole(declaration.getAffiliation().getAffilieNumero());
                } else {
                    getMemoryLog().logMessage(
                            ((BSession) dracoSession).getLabel("FACTURATION_TIERS_NON_TROUVE") + " "
                                    + declaration.getAffiliation().getAffilieNumero(), FWMessage.ERREUR,
                            this.getClass().getName());
                    return false;
                } // Ajout du nouvel en-tête de facture
                nouveauEntFacture.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                        getSession().getApplication()));
                nouveauEntFacture.setIdTypeFacture("1");
                if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())
                        && !JadeStringUtil.isBlankOrZero(declaration.getForControleEmployeurId())) {
                    nouveauEntFacture.setIdControle(declaration.getForControleEmployeurId());
                }
                // constante
                nouveauEntFacture.setIdExterneFacture(_noDecompte);
                // DGI init plan
                nouveauEntFacture.initDefaultPlanValue(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                        getSession().getApplication()));

                nouveauEntFacture.setIdCSModeImpression(enteteFactureIdCSModeImpressionSetter(nouveauEntFacture
                        .isNonImprimable()));

                nouveauEntFacture.add(getTransaction());
                //
                // Vérification de la transaction
                if (getTransaction().hasErrors()) {

                    getMemoryLog().logMessage(
                            ((BSession) dracoSession).getLabel("FACTURATION_DECL_NON_TRAITEE_1")
                                    + declaration.getAffiliation().getAffilieNumero()
                                    + ((BSession) dracoSession).getLabel("FACTURATION_DECL_NON_TRAITEE_2"),
                            FWMessage.ERREUR, this.getClass().getName());
                    return false;
                }
                afact.setIdEnteteFacture(nouveauEntFacture.getIdEntete());
                idEnteteFacture = nouveauEntFacture.getIdEntete();
            } else {
                // Si l'en-tête existe on récupère l'idEntete
                FAEnteteFacture entity2 = (FAEnteteFacture) entFactureManager.getEntity(0);
                idEnteteFacture = entity2.getIdEntete();
                entity2.setDateReceptionDS(declaration.getDateRetourEff());
                if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())
                        && !JadeStringUtil.isBlankOrZero(declaration.getForControleEmployeurId())) {
                    entity2.setIdControle(declaration.getForControleEmployeurId());
                }
                entity2.setIdCSModeImpression(enteteFactureIdCSModeImpressionSetter(entity2.isNonImprimable()));
                entity2.setReferenceFacture(declaration.getReferenceFacture());
                entity2.update(getTransaction());
                afact.setIdEnteteFacture(entity2.getIdEntete());
            }
            // Création des AFACT pour les lignes de la déclaration de salaires
            DSLigneDeclarationListViewBean ligneManager = new DSLigneDeclarationListViewBean();
            ligneManager.setForIdDeclaration(declaration.getIdDeclaration());
            ligneManager.setISession(dracoSession);
            ligneManager.find(getTransaction());
            for (int j = 0; j < ligneManager.size(); j++) {

                DSLigneDeclarationViewBean ligne = (DSLigneDeclarationViewBean) ligneManager.getEntity(j);

                // Récupération du taux et de la fraction
                String tauxFac = new String();
                String fraction = new String();
                BigDecimal bFraction = new BigDecimal("100");
                // Récupérer depuis Draco
                if (!JadeStringUtil.isBlank(ligne.getTauxAssuranceDeclaration())) {
                    fraction = JANumberFormatter.deQuote(ligne.getFractionAssuranceDeclaration());
                    if (!fraction.equals("100")) {
                        if (JadeStringUtil.isBlank(fraction)) {
                            tauxFac = "0";
                        } else {
                            bFraction = bFraction.divide(new BigDecimal(fraction), 10, BigDecimal.ROUND_HALF_DOWN);
                            BigDecimal bTaux = new BigDecimal(ligne.getTauxAssuranceDeclaration());
                            bTaux = bTaux.multiply(bFraction);
                            tauxFac = bTaux.toString();
                        }
                    } else {
                        tauxFac = JANumberFormatter.deQuote(ligne.getTauxAssuranceDeclaration());
                    }
                } else {
                    // Récupérer depuis Naos
                    fraction = JANumberFormatter.deQuote(ligne.getFractionAssurance());
                    if (!fraction.equals("100")) {
                        if (JadeStringUtil.isBlank(fraction)) {
                            tauxFac = "0";
                        } else {
                            bFraction = bFraction.divide(new BigDecimal(fraction), 10, BigDecimal.ROUND_HALF_DOWN);
                            BigDecimal bTaux = new BigDecimal(ligne.getTauxAssurance());
                            bTaux = bTaux.multiply(bFraction);
                            tauxFac = bTaux.toString();
                        }
                    } else {
                        tauxFac = JANumberFormatter.deQuote(ligne.getTauxAssurance());
                    }
                }
                // Création de la ligne de facture
                afact.setISession(getSession());
                afact.setIdEnteteFacture(idEnteteFacture);
                afact.setIdPassage(passage.getIdPassage());
                // Choix du module
                afact.setIdModuleFacturation(globaz.musca.external.ServicesFacturation.getIdModFacturationByType(
                        getSession(), getTransaction(), FAModuleFacturation.CS_MODULE_DECLARATION_SALAIRE));
                afact.setTypeModule(FAModuleFacturation.CS_MODULE_DECLARATION_SALAIRE);

                AFAssurance assurance = new AFAssurance();
                assurance.setSession(getSession());
                assurance.setAssuranceId(ligne.getAssuranceId());
                assurance.retrieve();
                if (DSDeclarationViewBean.CS_PRINCIPALE.equals(declaration.getTypeDeclaration())
                        || DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(declaration.getTypeDeclaration())
                        || DSDeclarationViewBean.CS_LTN.equals(declaration.getTypeDeclaration())) {
                    if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
                        afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                    } else {
                        afact.setIdTypeAfact(FAAfact.CS_AFACT_TABLEAU);
                    }
                } else {
                    afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                }
                if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_IMPOT_SOURCE))) {
                    afact.setIdRubrique(assurance.getRubriqueId());
                } else {
                    afact.setIdRubrique(ligne.getCotisation().getAssurance().getRubriqueId());
                }

                if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                    afact.setDebutPeriode("01.01." + ligne.getAnneCotisation());
                    afact.setFinPeriode("31.12." + ligne.getAnneCotisation());
                } else {
                    afact.setDebutPeriode("01.01." + declaration.getAnnee());
                    afact.setFinPeriode("31.12." + declaration.getAnnee());
                }
                if (JadeStringUtil.isBlank(ligne.getMontantDeclaration())) {
                    afact.setMasseInitiale("0");
                    afact.setMontantInitial("0");
                } else {
                    // Masse et montant initial
                    if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
                        afact.setMasseInitiale("0");
                    } else {
                        afact.setMasseInitiale(JANumberFormatter.deQuote(ligne.getMontantDeclaration()));
                    }
                    afact.setMontantInitial(JANumberFormatter.deQuote(ligne.getCotisationDue()));
                }
                // Masse et montant déjà facturé
                if ((ligne.getCompteur() != null)
                        && !DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(declaration.getTypeDeclaration())
                        && !DSDeclarationViewBean.CS_ICI.equals(declaration.getTypeDeclaration())
                        && !DSDeclarationViewBean.CS_DIVIDENDE.equals(declaration.getTypeDeclaration())
                        && !DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())
                        && !DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                    afact.setMasseDejaFacturee(JANumberFormatter.deQuote(ligne.getCompteur().getCumulMasse()));
                    afact.setMontantDejaFacture(JANumberFormatter.deQuote(ligne.getCompteur().getCumulCotisation()));
                } else {
                    afact.setMontantDejaFacture("0");
                    afact.setMasseDejaFacturee("0");
                }
                afact.setAffichtaux(new Boolean(!ligne.isTauxCache()));
                afact.setTauxInitial(tauxFac);
                afact.setTauxDejaFacture(tauxFac);
                afact.setTauxFacture(tauxFac);

                if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                    afact.setAnneeCotisation(ligne.getAnneCotisation());
                } else {
                    afact.setAnneeCotisation(declaration.getAnnee());
                }

                if (DSDeclarationViewBean.CS_PRINCIPALE.equals(declaration.getTypeDeclaration())
                        || DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equals(declaration.getTypeDeclaration())) {
                    afact.setMasseFacture(JANumberFormatter.deQuote(ligne.getDecompte()));
                } else {
                    // Mettre la masse à 0 pour les impôts à la source.
                    if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
                        afact.setMasseFacture("0");
                    } else {
                        afact.setMasseFacture(ligne.getMontantDeclaration());
                    }
                }
                afact.setMontantFacture(JANumberFormatter.deQuote(ligne.getSoldeCotisation()));
                // désactiver le contrôle des 2 francs lors de la validation de
                // l'afact
                afact.setControleDeuxFrancs(false);
                afact.setNumCaisse(AFAffiliationUtil.getIdCaissePrincipale(declaration.getAffiliation(), true,
                        afact.getAnneeCotisation()));
                // DGI ajout du libellé
                // ACR pour l'impôt à la source, setter à blanc le libelle pour
                // que ce soit le libellé de la rubrique sur la facture
                if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(ligne.getAssurance().getTypeAssurance())) {
                    afact.setLibelle("");
                } else {
                    afact.setLibelle(ligne.getAssurance().getAssuranceLibelle(
                            declaration.getAffiliation().getTiers().getLangueIso()));
                }
                // L'afact n'est pas créé uniquement quand les deux montants
                // ci-dessous sont à zéro.
                // Permet d'avoir des factures pour les DS à zéro.
                // if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantInitial()))
                // || !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantDejaFacture()))) {
                // afact.add(getTransaction());
                // } else {
                // afact = new FAAfact();
                // }
                // Si une caisse désire que les lignes de déclarations de salaires qui sont à zéro
                // ne crée pas de lignes de facture PO 5240
                if ("true".equalsIgnoreCase((((BSession) dracoSession).getApplication())
                        .getProperty("nePasFacturerDSZero"))) {
                    if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantFacture()))) {
                        afact.add(getTransaction());
                    } else {
                        afact = new FAAfact();
                    }
                } else {
                    // L'afact n'est pas créé uniquement si la masse est à zéro
                    // ou si le montant initial et le montant déjà facturé sont à zéro.
                    // Permet d'avoir des factures pour les DS à zéro. PO 5236
                    if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMasseFacture()))
                            || (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantInitial())) || !JadeStringUtil
                                    .isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantDejaFacture())))) {
                        afact.add(getTransaction());
                    } else {
                        afact = new FAAfact();
                    }
                }
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(
                            (((BSession) dracoSession).getLabel("FACTURATION_AFACT_ERREUR_AJOUT")
                                    + declaration.getAffiliation().getAffilieNumero() + " " + declaration.getAnnee()),
                            FWMessage.ERREUR, this.getClass().getName());
                    return false;
                }
                // Mise à jour de la ligne de la déclaration
                ligne.setEnFacturation(true);
                // Masse cumulée
                if (ligne.getCompteur() != null) {
                    ligne.setMontantFactureACEJour(ligne.getCompteur().getCumulMasse());
                } else {
                    ligne.setMontantFactureACEJour("0");
                }
                // Cotisation cumulée
                if (ligne.getCompteur() != null) {
                    ligne.setCumulCotisationDeclaration(ligne.getCompteur().getCumulCotisation());
                } else {
                    ligne.setCumulCotisationDeclaration("0");
                }
                if (JadeStringUtil.isBlank(ligne.getTauxAssuranceDeclaration())) {
                    ligne.setTauxAssuranceDeclaration(ligne.getTauxAssurance());
                    ligne.setFractionAssuranceDeclaration(ligne.getFractionAssurance());
                }
                ligne.update(getTransaction());
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(
                            (((BSession) dracoSession).getLabel("FACTURATION_AFACT_ERREUR_AJOUT")
                                    + declaration.getAffiliation().getAffilieNumero() + " " + declaration.getAnnee()),
                            FWMessage.ERREUR, this.getClass().getName());
                    return false;
                }
            }
            // Contrôler si la section existe déjà pour le numéro de décompte
            // Mise à jour de la déclaration de salaires
            declaration.setEnFacturation(true);
            declaration.setIdPassageFac(passage.getIdPassage());
            declaration.setNoDecompte(_noDecompte);
            declaration.setEtat(DSDeclarationViewBean.CS_COMPTABILISE);
            declaration.update(getTransaction());
            if (getTransaction().hasErrors()) {
                getMemoryLog().logMessage(
                        (((BSession) dracoSession).getLabel("FACTURATION_AFACT_ERREUR_AJOUT")
                                + declaration.getAffiliation().getAffilieNumero() + " " + declaration.getAnnee()),
                        FWMessage.ERREUR, this.getClass().getName());
                return false;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    (((BSession) dracoSession).getLabel("FACTURATION_AFACT_ERREUR_AJOUT")
                            + declaration.getAffiliation().getAffilieNumero() + " " + declaration.getAnnee()),
                    FWMessage.ERREUR, this.getClass().getName());
            getTransaction().addErrors(((BSession) dracoSession).getLabel("FACTURATION_AFACT_ERREUR_AJOUT"));
            return false;
        }
        return true;
    }
}
