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
 * Processus de facturation des d�clarations de salaires
 * 
 * @author S�bastien Chappatte
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
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Cette m�thode ex�cute le processus de facturation des d�clarations de salaires. Date de cr�ation : (14.02.2002
     * 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        try {
            // Cr�ation d'une session Draco
            dracoSession = GlobazSystem.getApplication("DRACO").newSession(getSession());
            // Cr�ation d'une session Pyxis
            pyxisSession = GlobazSystem.getApplication("PYXIS").newSession(getSession());
            // Cr�ation d'une session Osiris
            osirisSession = GlobazSystem.getApplication("OSIRIS").newSession(getSession());
            // initialiseDecision lit les d�clarations qui ont l'idPassgeFac
            // identique � l'idPassage
            // donn� par le processus et remet idPassageFac � z�ro
            if (!initialiseDeclaration()) {
                return false;
            }
            // Instancier le manager des d�clarations de salaires
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
            // Entrer les informations pour l' �tat du process
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
                    // incr�menter no decompte de la derni�re en-t�te trouv�e
                    _noDecompte = incrementeNoDecompte(decomptePassageMax);
                }

                try {
                    // Mise � jour des tables
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
     * R�cup�rer le compte annexe
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
        // Recherche les decomptes du passage pour un affili�
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
     *            num�ro de d�compte courant
     * @return le dernier no de d�compte du passage ou null si rien n'est trouv�
     * @throws Exception
     */
    private String findLastNoDecomptePassage(String noDecompte) throws Exception {
        FAEnteteFactureManager enteteManager = findEnteteFacture(passage.getIdPassage(), declaration.getAffiliation()
                .getAffilieNumero(), declaration.getAffiliation().getIdTiers());

        int noDecomptePassageMax = 0;
        Boolean decompteExistant = false;
        // recherche sur les en-t�te d�j� existantes
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
     * V�rifie si une section existe en comptabilit� auxiliaire pour ce num�ro de d�compte
     * 
     * @return
     * @throws Exception
     */
    private String findLastSection(String noDecompte) throws Exception {
        // R�cup�rer le compte annexe
        CACompteAnnexe compteAnnexe = findCompteAnnexe();

        // Contr�ler si le num�ro de section(d�compte) existe d�j� dans
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
            // Modif JMC => ne pas tenir compte des d�cisions d'int�ret type 2010189...
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
     * Cette m�thode d�termine l'objet du message en fonction du code d'erreur
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // D�terminer l'objet du message en fonction du code erreur
        String obj;
        obj = "";
        return obj;
    }

    public int getModeFacturation() {
        return modeFacturation;
    }

    /**
     * Incremente de 10 le num�ro de d�compte
     * 
     * @param noDecompte
     *            num�ro � incr�menter
     * @return le num�ro de d�compte incr�ment�
     */
    private String incrementeNoDecompte(String noDecompte) {
        // On incr�mente le no de d�compte et on reteste
        Integer iNoDecompte = Integer.valueOf(noDecompte);
        iNoDecompte += 1;
        return iNoDecompte.toString();
    }

    /**
     * Cr�er un num�ro de d�compte si inexistant
     * 
     * @return le num�ro de d�compte par d�faut
     */
    private String initDefaultNumDecompte() {
        String noDecompte = "";
        // Cr�er un num�ro de d�compte si inexistant
        if (JadeStringUtil.isIntegerEmpty(declaration.getNoDecompte())) {
            // G�n�re le No en fonction du d�compte
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
     * Cette m�thode effectue l'initialisation de la d�claration Elle remet idPassageFac pour les d�cisions qui un
     * idpassage identique � celui donn� par le processus
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
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Cette m�thode permet d'effectuer un rollback de la transaction
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
     * Method setPassage. Utilise le passage pass� en param�tre depuis la facturation
     * 
     * @param passage
     *            passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    /**
     * Cette m�thode effectue la mise � jour des tables suivantes : DSDECLP Ent�te d�claration de salaires DSDELIP Ligne
     * de d�claration de salaires FAAFACP Ligne de facture FAENTFP Ent�te facture
     */
    private boolean updateTables() throws Exception {
        // La d�claration et en cours de facturation
        try {
            declaration.setEnFacturation(true);
            // Recherche de l'ent�te de facture
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
            // Si aucun en-t�te trouv�, cr�ation d'un nouvel en-t�te de facture
            if (entFactureManager.size() == 0) {
                nouveauEntFacture = new FAEnteteFacture();
                nouveauEntFacture.setISession(getSession());
                nouveauEntFacture.setIdPassage(passage.getIdPassage());
                nouveauEntFacture.setIdTiers(declaration.getAffiliation().getIdTiers());
                nouveauEntFacture.setDateReceptionDS(declaration.getDateRetourEff());
                nouveauEntFacture.setIdSoumisInteretsMoratoires(declaration.getSoumisInteret());
                nouveauEntFacture.setReferenceFacture(declaration.getReferenceFacture());
                // Contr�ler que le tiers existe
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
                } // Ajout du nouvel en-t�te de facture
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
                // V�rification de la transaction
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
                // Si l'en-t�te existe on r�cup�re l'idEntete
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
            // Cr�ation des AFACT pour les lignes de la d�claration de salaires
            DSLigneDeclarationListViewBean ligneManager = new DSLigneDeclarationListViewBean();
            ligneManager.setForIdDeclaration(declaration.getIdDeclaration());
            ligneManager.setISession(dracoSession);
            ligneManager.find(getTransaction());
            for (int j = 0; j < ligneManager.size(); j++) {

                DSLigneDeclarationViewBean ligne = (DSLigneDeclarationViewBean) ligneManager.getEntity(j);

                // R�cup�ration du taux et de la fraction
                String tauxFac = new String();
                String fraction = new String();
                BigDecimal bFraction = new BigDecimal("100");
                // R�cup�rer depuis Draco
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
                    // R�cup�rer depuis Naos
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
                // Cr�ation de la ligne de facture
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
                // Masse et montant d�j� factur�
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
                    // Mettre la masse � 0 pour les imp�ts � la source.
                    if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
                        afact.setMasseFacture("0");
                    } else {
                        afact.setMasseFacture(ligne.getMontantDeclaration());
                    }
                }
                afact.setMontantFacture(JANumberFormatter.deQuote(ligne.getSoldeCotisation()));
                // d�sactiver le contr�le des 2 francs lors de la validation de
                // l'afact
                afact.setControleDeuxFrancs(false);
                afact.setNumCaisse(AFAffiliationUtil.getIdCaissePrincipale(declaration.getAffiliation(), true,
                        afact.getAnneeCotisation()));
                // DGI ajout du libell�
                // ACR pour l'imp�t � la source, setter � blanc le libelle pour
                // que ce soit le libell� de la rubrique sur la facture
                if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(ligne.getAssurance().getTypeAssurance())) {
                    afact.setLibelle("");
                } else {
                    afact.setLibelle(ligne.getAssurance().getAssuranceLibelle(
                            declaration.getAffiliation().getTiers().getLangueIso()));
                }
                // L'afact n'est pas cr�� uniquement quand les deux montants
                // ci-dessous sont � z�ro.
                // Permet d'avoir des factures pour les DS � z�ro.
                // if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantInitial()))
                // || !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantDejaFacture()))) {
                // afact.add(getTransaction());
                // } else {
                // afact = new FAAfact();
                // }
                // Si une caisse d�sire que les lignes de d�clarations de salaires qui sont � z�ro
                // ne cr�e pas de lignes de facture PO 5240
                if ("true".equalsIgnoreCase((((BSession) dracoSession).getApplication())
                        .getProperty("nePasFacturerDSZero"))) {
                    if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact.getMontantFacture()))) {
                        afact.add(getTransaction());
                    } else {
                        afact = new FAAfact();
                    }
                } else {
                    // L'afact n'est pas cr�� uniquement si la masse est � z�ro
                    // ou si le montant initial et le montant d�j� factur� sont � z�ro.
                    // Permet d'avoir des factures pour les DS � z�ro. PO 5236
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
                // Mise � jour de la ligne de la d�claration
                ligne.setEnFacturation(true);
                // Masse cumul�e
                if (ligne.getCompteur() != null) {
                    ligne.setMontantFactureACEJour(ligne.getCompteur().getCumulMasse());
                } else {
                    ligne.setMontantFactureACEJour("0");
                }
                // Cotisation cumul�e
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
            // Contr�ler si la section existe d�j� pour le num�ro de d�compte
            // Mise � jour de la d�claration de salaires
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
