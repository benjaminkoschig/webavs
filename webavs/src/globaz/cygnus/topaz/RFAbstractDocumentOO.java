/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.cygnus.process.RFDocumentsProcess;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BSession;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSAllocataire;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 */
public abstract class RFAbstractDocumentOO {

    public enum Caisse {
        AGENCE_LAUSANNE,
        CCJU,
        CCVD
    }

    protected static final String CDT_ADRESSECAISSE = "{adresseCaisse}";

    protected static final String CDT_TITRE = "{titre}";
    public final static String HEADER_CAISSES_CONCERNES = "header.caissesConcernes.";
    public final static String HEADER_COMMENTAIRE = "header.commentaire.";
    // en tête
    public final static String HEADER_NUMERO_LETTRE = "header.numeroLettre.";
    public final static String HEADER_TITRE_LETTRE = "header.titreLettre.";
    protected String adresse = "";
    // constantes utilisés pour remplir le document
    protected final String ADRESSE_DESTINATAIRE = "{adresseDestinataire}";
    protected final String AGENCE = "agence";
    protected Iterator annexes = null;
    protected final String BUREAU = "Bureau";
    protected Caisse caisse;
    protected final String CAISSE = "caisse";
    protected final String CAISSE_OU_AGENCE = "{caisseOuAgence}";
    protected ICaisseReportHelperOO caisseHelper;;
    protected String codeIsoLangue = "";
    protected Iterator copies = null;
    protected DocumentData data;
    protected final String DATE_DEMANDE = "{dateDemande}";
    protected final String DATE_PREMIER_VERSEMENT = "{datePremierVersement}";
    protected String dateSurDocument = "";
    protected JadePrintDocumentContainer documentContainer;
    protected DocumentData documentData;
    protected ICTDocument documentEnTete;
    protected ICTDocument documentHelper;
    protected ICTDocument documentPrincipale;
    protected String domaineLettreEnTete = "";
    protected String eMail = "";
    protected String idDroit = "";
    protected String idTiers = "";
    protected boolean isCopie = false;
    protected final String MONTANT_OCTROI = "{montantOctroi}";
    protected String nom = "";
    protected final String NOM_ASSURE = "{nomAssure}";
    protected final String NOM_DOCUMENT = "{nomDocument}";
    // constante utilisé pour remplir l'entete
    protected final String NUMERO_LETTRE = "{numeroLettre}";
    protected String prenom = "";
    protected final String PRENOM_ASSURE = "{prenomAssure}";
    protected final String REGIME = "{regime}";
    protected final String SERVICE = "Service";
    protected final String SERVICE_OU_BUREAU = "{serviceOuBureau}";
    protected BSession session;
    protected String sexe = "";
    protected final String SEXE = "{sexe}";
    protected PRTiersWrapper tiersWrapper;
    protected String titre = "";
    protected final String TITRE = "{titreAssure}";
    protected String titreComplet = "";
    protected final String TYPE_DOCUMENT = "{typeDocument}";

    abstract public void chargerCatalogueTexte() throws Exception;

    abstract public void chargerDonneesEnTete() throws Exception;

    public void generationLettre(FWMemoryLog memoryLog, RFCopieDecisionsValidationData... copie) throws Exception {

    }

    public void generationLettre(RFCopieDecisionsValidationData... copie) throws Exception {

    }

    public String getAdresse() {
        return adresse;
    }

    public Iterator getAnnexes() {
        return annexes;
    }

    /**
     * @return the codeIsoLangue
     */
    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public Iterator getCopies() {
        return copies;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public ICTDocument getDocumentHelper() {
        return documentHelper;
    }

    public ICTDocument getDocumentPrincipale() {
        return documentPrincipale;
    }

    public String getDomaineLettreEnTete() {
        return domaineLettreEnTete;
    }

    /**
     * Methode pour obtenir des détails sur la caisse d'assurance sociale
     * 
     * @param nom
     * @param rue
     * @param localite
     * @return Selon boolean passé, peut retourner le nom, la rue ou le npa-localite
     * @throws Exception
     */
    // public String getDonneesCaisse(boolean nom, boolean rue, boolean localite) throws Exception {
    // String donneesCaisse = null;
    //
    // // Recherche de l'administration concernée
    // TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
    // // Adaptation du code d'administration pour la CCVD
    // String codeAdministration = CaisseHelperFactory.getInstance()
    // .getNoCaisseFormatee(this.session.getApplication());
    // if (codeAdministration.equals("022")) {
    // codeAdministration = "22";
    // }
    // // Set les parametres necessaires
    // tiAdministrationMgr.setSession(this.getSession());
    // tiAdministrationMgr.setForCodeAdministration(codeAdministration);
    // tiAdministrationMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
    // tiAdministrationMgr.changeManagerSize(0);
    // tiAdministrationMgr.find();
    // // Charge les donnees souhaitees
    // TIAdministrationViewBean tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr.getFirstEntity();
    // String nomCaisse = tiAdministration.getNom();
    // String rueCaisse = tiAdministration.getRue();
    // String localiteCaisse = tiAdministration.getLocaliteLong();
    //
    // if (nom) {
    // if (!JadeStringUtil.isBlankOrZero(nomCaisse)) {
    // donneesCaisse = nomCaisse;
    // }
    // }
    // if (rue) {
    // if (!JadeStringUtil.isBlankOrZero(rueCaisse)) {
    // if (nom != false) {
    // donneesCaisse = donneesCaisse + ", " + rueCaisse;
    // } else {
    // donneesCaisse = rueCaisse;
    // }
    // }
    // }
    // if (localite) {
    // if (!JadeStringUtil.isBlankOrZero(localiteCaisse)) {
    // if ((nom != false) || (rue != false)) {
    // donneesCaisse = donneesCaisse + ", " + localiteCaisse;
    // } else {
    // donneesCaisse = localiteCaisse;
    // }
    // }
    // }
    // return donneesCaisse;
    // }

    public String geteMail() {
        return eMail;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public BSession getSession() {
        return session;
    }

    public PRTiersWrapper getTiersWrapper() {
        return tiersWrapper;
    }

    public String getTitre() {
        return titre;
    }

    public String getTitreComplet() {
        return titreComplet;
    }

    public boolean isCopie() {
        return isCopie;
    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            String idTiers, String numeroDocument, boolean miseEnGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
        pubInfos.setPublishDocument(false);
        pubInfos.setDocumentType(numeroDocument);
        pubInfos.setDocumentTypeNumber(numeroDocument);
        pubInfos.setArchiveDocument(miseEnGed);

        TIDocumentInfoHelper.fill(pubInfos, idTiers, getSession(), null, null, null);

        this.generationLettre();

        documentContainer.addDocument(getDocumentData(), pubInfos);
        return documentContainer;
    }

    abstract public void remplirDocument() throws Exception;

    public void run() {
        // TODO Auto-generated method stub

    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAnnexes(Iterator annexes) {
        this.annexes = annexes;
    }

    /**
     * @param codeIsoLangue
     *            the codeIsoLangue to set
     */
    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setCopies(Iterator copies) {
        this.copies = copies;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setDocumentHelper(ICTDocument documentHelper) {
        this.documentHelper = documentHelper;
    }

    public void setDocumentPrincipale(ICTDocument documentPrincipale) {
        this.documentPrincipale = documentPrincipale;
    }

    public void setDomaineLettreEnTete(String domaineLettreEnTete) {
        this.domaineLettreEnTete = domaineLettreEnTete;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsCopie(boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTiersWrapper(PRTiersWrapper tiersWrapper) {
        this.tiersWrapper = tiersWrapper;
    }

    public void setTitre(String titre) {
        if (ALCSAllocataire.TITRE_MONSIEUR.equals(titre)) {
            this.titre = session.getLabel("JSP_RF_DOCUMENT_MONSIEUR");
        } else if (ALCSAllocataire.TITRE_MADAME.equals(titre)) {
            this.titre = session.getLabel("JSP_RF_DOCUMENT_MADAME");
        } else if (ALCSAllocataire.TITRE_MADEMOISELLE.equals(titre)) {
            this.titre = session.getLabel("JSP_RF_DOCUMENT_MADEMOISELLE");
        }
    }

    public void setTitreComplet(String titre) {
        if (ALCSAllocataire.TITRE_MONSIEUR.equals(titre)) {
            titreComplet = session.getLabel("JSP_RF_DOCUMENT_MONSIEUR_COMPLET");
        } else if (ALCSAllocataire.TITRE_MADAME.equals(titre)) {
            titreComplet = session.getLabel("JSP_RF_DOCUMENT_MADAME_COMPLET");
        } else if (ALCSAllocataire.TITRE_MADEMOISELLE.equals(titre)) {
            titreComplet = session.getLabel("JSP_RF_DOCUMENT_MADEMOISELLE_COMPLET");
        }
    }

}
