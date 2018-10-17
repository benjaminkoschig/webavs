package globaz.draco.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.print.itext.DSAttestationFiscaleLtn_Doc;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;

/**
 * @author BJO
 */
public class DSAttestationFiscaleLtnGenProcess extends BProcess {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean affilieTous = false;
    // imprimer à cette date
    private ArrayList<BIEntity> affilieValable = new ArrayList<BIEntity>();
    private String annee;// annee pour l'attestation fiscale
    // document
    private String dateImpression;// Pour ré-imprimer des attestations déjà
    private String dateValeur = JACalendar.todayJJsMMsAAAA();// date sur
    private String fromAffilies;
    private boolean simulation = false;
    private String untilAffilies;

    // private String eMailAddress;

    public DSAttestationFiscaleLtnGenProcess() {
        super();
    }

    public DSAttestationFiscaleLtnGenProcess(BProcess parent) {
        super(parent);
    }

    public DSAttestationFiscaleLtnGenProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean success = true;
        try {
            // Vérification des paramètres
            if (getAffilieTous() == false) {
                if (JadeStringUtil.isBlank(getFromAffilies())) {
                    getMemoryLog().logMessage(getSession().getLabel("CHAMPS_AFFILIE_VIDE"), FWMessage.FATAL,
                            this.getClass().getName());
                }
                if (JadeStringUtil.isBlank(getUntilAffilies())) {
                    getMemoryLog().logMessage(getSession().getLabel("CHAMPS_AFFILIE_VIDE"), FWMessage.FATAL,
                            this.getClass().getName());
                }
            }
            if (JadeStringUtil.isBlank(getAnnee())) {
                getMemoryLog().logMessage(getSession().getLabel("CHAMPS_ANNEE_VIDE"), FWMessage.FATAL,
                        this.getClass().getName());
            }
            if (JAUtil.isDateEmpty(getDateValeur())) {
                getMemoryLog().logMessage(getSession().getLabel("CHAMPS_DATE_VALEUR_VIDE"), FWMessage.FATAL,
                        this.getClass().getName());
            }
            if (JadeStringUtil.isBlank(getEMailAddress())) {
                getMemoryLog().logMessage(getSession().getLabel("MSG_EMAILADDRESS_VIDE"), FWMessage.FATAL,
                        this.getClass().getName());
            }

            // Sortir en cas d'erreurs
            if (getMemoryLog().isOnFatalLevel()) {
                return false;
            }

            // DEBUT DU PROCESS
            // récupération des affiliés
            setProgressDescription("Recherche des affiliés LTN");
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setSession(getSession());
            if (!affilieTous) {
                affiliationManager.setFromAffilieNumero(getFromAffilies());
                affiliationManager.setToAffilieNumero(getUntilAffilies());
            }
            affiliationManager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_LTN });
            affiliationManager.find(BManager.SIZE_NOLIMIT);
            // parcourir le manager et insérer les affiliés valables dans un
            // arrayList
            for (int i = 0; i < affiliationManager.size(); i++) {
                boolean valable = true;
                // récupération du compte annexe et vérification du solde des
                // cotisations
                try {
                    AFAffiliation affEnCours = (AFAffiliation) affiliationManager.getEntity(i);
                    BigDecimal sommeSolde = null;
                    int nbSection = 0;
                    BISession osirisSession = GlobazSystem.getApplication("OSIRIS").newSession(getSession());
                    // Récupérer le compte annexe
                    CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                    compteAnnexe.setISession(osirisSession);
                    compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compteAnnexe.setIdRole(CaisseHelperFactory.getInstance()
                            .getRoleForAffilieParitaire(getSession().getApplication()));
                    compteAnnexe.setIdExterneRole(affEnCours.getAffilieNumero());
                    compteAnnexe.retrieve(getTransaction());
                    if (JadeStringUtil.isBlank(compteAnnexe.getIdCompteAnnexe())) {
                        // System.out.println("Compte annexe inexistant");
                        valable = false;
                    }
                    CASectionManager sectionManager = new CASectionManager();
                    sectionManager.setISession(osirisSession);
                    sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
                    // on sélectionne les DS LTN et les DS LTN COMPLEMENTAIRE (les 2 doivent être soldées)
                    ArrayList<String> categorieList = new ArrayList<String>();
                    categorieList.add(APISection.ID_CATEGORIE_SECTION_LTN);
                    categorieList.add(APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE);
                    sectionManager.setForCategorieSectionIn(categorieList);

                    sectionManager.setForIdTypeSection(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
                    sectionManager.setLikeIdExterne(annee);
                    sectionManager.find(BManager.SIZE_NOLIMIT);
                    // BZ xxx - I140409_003 0> ignorer les sections issues des intérêts
                    for (int j = 0; j < sectionManager.getSize(); j++) {
                        CASection section = (CASection) sectionManager.getEntity(j);
                        if (!"9".equals(section.getIdExterne().substring(6, 7))) {
                            if (sommeSolde == null) {
                                sommeSolde = new BigDecimal(section.getSolde());
                            } else {
                                sommeSolde = sommeSolde.add(new BigDecimal(section.getSolde()));
                            }
                            nbSection++;
                        }
                    }
                    if (sectionManager.size() > 1) {
                        // regarde si il existe un 38 pour cet affilié et cette année
                        DSDeclarationListViewBean declarationCompManager = new DSDeclarationListViewBean();
                        declarationCompManager.setSession(getSession());
                        declarationCompManager.setForAffiliationId(affEnCours.getAffiliationId());
                        declarationCompManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE);
                        declarationCompManager.setForAnnee(annee);
                        declarationCompManager.find();
                        // si il n'existe pas le même nombre de section (osiris) que le nombre de DS (draco) c'est qu'il
                        // y a une erreur => l'affilié n'est pas valable
                        if (nbSection != (declarationCompManager.size() + 1)) {
                            valable = false;
                        }
                    }
                    // if (sommeSolde != null) {
                    // // Si le solde est positif on imprime pas les attestations (l'employeur n'a pas payé)
                    // if (sommeSolde.doubleValue() > 0) {
                    // valable = false;
                    // }
                    // }
                    // si la somme est null on imprime pas les attestations car la déclaration n'est pas en comptabilité
                    // (OSIRIS)
                    if (sommeSolde == null) {
                        valable = false;
                    }

                    if (valable) {
                        affilieValable.add(affiliationManager.getEntity(i));
                    }
                } catch (Exception e) {
                    throw new Exception("Erreur lors du traitement LTN : " + e.getMessage());
                }
            }

            setProgressScaleValue(affilieValable.size());
            // Parcourir les affiliés valables et lancer la génération des
            // attestations pour chacun d'eux
            int cpt = 0;
            for (int i = 0; i < affilieValable.size(); i++) {
                AFAffiliation affilie = (AFAffiliation) affilieValable.get(i);
                cpt++;
                setProgressDescription(
                        affilie.getAffilieNumero() + "<br>" + cpt + "/" + affilieValable.size() + "<br>");
                if (isAborted()) {
                    // fusionner tous les documents en 1 seul fichier
                    JadePublishDocumentInfo docInfo = createDocumentInfo();
                    this.mergePDF(docInfo, true, 500, false, null);
                    setProgressDescription(getSession().getLabel("TRAITEMENT_INTERROMPU") + " "
                            + affilie.getAffilieNumero() + "<br>" + cpt + "/" + affilieValable.size() + "<br>");
                    return false;
                }

                // récupération des déclarations de salaires
                DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
                declarationManager.setSession(getSession());
                declarationManager.setForAffiliationId(affilie.getAffiliationId());
                declarationManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN);
                declarationManager.setForAnnee(getAnnee());

                // si le champs date d'impression est renseigné ca veut dire que
                // l'on veut ré-imprimer des attestations
                if (!JadeStringUtil.isBlank(getDateImpression())) {
                    declarationManager.setForDateFlag(getDateImpression());
                    // sinon on imprime que celles qui n'ont jamais été
                    // imprimées
                } else {
                    declarationManager.setForDateFlagIsNullOrZero(new Boolean(true));
                }
                declarationManager.find(BManager.SIZE_NOLIMIT);

                // parcourir les déclarations, en général 1 seule déclaration
                // par année
                for (int j = 0; j < declarationManager.size(); j++) {
                    DSDeclarationViewBean declaration = (DSDeclarationViewBean) declarationManager.getEntity(j);
                    // execution du process de création du document
                    try {
                        DSAttestationFiscaleLtn_Doc document = new DSAttestationFiscaleLtn_Doc(getSession());
                        document.setIdDeclaration(declaration.getIdDeclaration());
                        document.setSimulation(getSimulation());
                        document.setEMailAddress(getEMailAddress());
                        document.setDateValeur(getDateValeur());
                        document.setProcessMenu(true);
                        document.setParent(this);
                        document.executeProcess();
                    } catch (Exception e) {
                        throw new Exception("Erreur lors de la création du document LTN : " + e.getMessage());
                    }
                }
                incProgressCounter();
            }
            // fusionner tous les documents en 1 seul fichier
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTypeNumber(DSAttestationFiscaleLtn_Doc.numeroInforom);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            if (simulation) {
                docInfo.setDocumentProperty("simulation", "true");
            }
            this.mergePDF(docInfo, false, 500, false, null);

        } catch (Exception e) {
            // On traite l'exception en loggant un erreur fatale et en mettant success à false
            success = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
        return success;
    }

    @Override
    protected void _validate() throws Exception {
        if (getAffilieTous() == false) {
            if (JadeStringUtil.isBlank(getFromAffilies())) {
                this._addError(getTransaction(), getSession().getLabel("CHAMPS_AFFILIE_VIDE"));
            }
            if (JadeStringUtil.isBlank(getUntilAffilies())) {
                this._addError(getTransaction(), getSession().getLabel("CHAMPS_AFFILIE_VIDE"));
            }
        }
        if (JadeStringUtil.isBlank(getAnnee())) {
            this._addError(getTransaction(), getSession().getLabel("CHAMPS_ANNEE_VIDE"));
        }
        if (JAUtil.isDateEmpty(getDateValeur())) {
            this._addError(getTransaction(), getSession().getLabel("CHAMPS_DATE_VALEUR_VIDE"));
        }
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("MSG_EMAILADDRESS_VIDE"));
        }
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
            setSendMailOnError(true);
        }

    }

    public boolean getAffilieTous() {
        return affilieTous;
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("ATTESTATION_FISCALE_LTN_GEN_ECHOUE");
        } else {
            return getSession().getLabel("ATTESTATION_FISCALE_LTN_GEN_REUSSI");
        }
    }

    public String getFromAffilies() {
        return fromAffilies;
    }

    public boolean getSimulation() {
        return simulation;
    }

    public String getUntilAffilies() {
        return untilAffilies;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAffilieTous(boolean affilieTous) {
        this.affilieTous = affilieTous;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setFromAffilies(String fromAffilies) {
        this.fromAffilies = fromAffilies;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public void setUntilAffilies(String untilAffilies) {
        this.untilAffilies = untilAffilies;
    }
}
