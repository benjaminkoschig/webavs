/*
 * Créé le 2 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.affiliation;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe permettant d'imprimer une lettre de bienvenue à la création d'une nouvelle affiliation
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFLettreDeBienvenue extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Numéro du document
    private static final String DOC_NO = "0001CAF";
    // Nom du modèle de la lettre
    public final static String MODEL_NAME = "NAOS_LETTRE_BIENVENUE";
    public final static String NOM_DOC_EMPLOYEUR = "ACRE50";
    public final static String NOM_DOC_EMPLOYEUR_POTENTIEL = "ACRE70";
    public final static String NOM_DOC_EMPLOYEUR_UNIQUEMENT_AF = "ACRECCAF";
    public final static String NOM_DOC_ETUDIANT = "ACRE89";
    // Constantes pour les noms des documents à aller chercher
    public final static String NOM_DOC_INDEPENDANT = "ACRE03";
    public final static String NOM_DOC_INDEPENDANT_EMPLOYEUR_POTENTIEL = "ACRE73";
    public final static String NOM_DOC_INDEPENDANT_ET_EMPLOYEUR = "ACRE53";
    public final static String NOM_DOC_NON_ACTIF = "ACRE04";
    public final static String NOM_DOC_TSE = "ACRETSE";
    AFAffiliation aff = null;
    ICTDocument[] document = null;
    String idAffiliation = null;
    boolean isFirst = true;
    String langueDestinataireISO = null;

    public AFLettreDeBienvenue() throws Exception {
        new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);

    }

    /**
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public AFLettreDeBienvenue(BProcess parent) throws FWIException {
        super(parent, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "Lettre de bienvenue");
        super.setFileTitle("Lettre de bienvenue");
    }

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public AFLettreDeBienvenue(BSession session) throws FWIException {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, "Lettre de bienvenue");
        super.setFileTitle("Lettre de bienvenue");
    }

    private String _getLangueDestinataireISO() {
        if (langueDestinataireISO == null) {
            langueDestinataireISO = aff.getTiers().getLangueIso();
        }
        return langueDestinataireISO;
    }

    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        bean.setAdresse(aff.getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(),
                aff != null ? aff.getAffilieNumero() : null));

        bean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), _getLangueDestinataireISO()));
        bean.setNoAffilie(aff.getAffilieNumero());

        // Renseignement du numéro ide
        AFIDEUtil.addNumeroIDEInDoc(bean, aff.getNumeroIDE(), aff.getIdeStatut());

        bean.setNoAvs(aff.getTiers().getNumAvsActuel());
        bean.setEmailCollaborateur(" ");
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
            setSendMailOnError(true);
        }
    }

    /**
     * Effectue les opération avant la construction du document
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        document = getICTDocument();
        try {
            // Texte pour le concerne
            this.setParametres(AFLettreDeBienvenue_Param.P_CONCERNE, getTexte(1));
            // Texte pour le corps de texte
            this.setParametres(AFLettreDeBienvenue_Param.P_CORPS, getTexte(2));
            // Texte pour la signature
            this.setParametres(AFLettreDeBienvenue_Param.P_SIGNATURE, getTexte(3));
            // Texte pour les annexes
            this.setParametres(AFLettreDeBienvenue_Param.P_ANNEXE, getTexte(4));
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
        }
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_LETTRE_BIENVENUE"));
        super.setTemplateFile(AFLettreDeBienvenue.MODEL_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport ()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {

        aff = getAff();

        try {
            AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);

            if (app.getLettreBienvenueWaitInteractiveValidation()) {

                int sleepDuring = 1000;
                int numberMaxOfSleep = 5;

                while (numberMaxOfSleep > 0 && JadeStringUtil.isBlankOrZero(aff.getIdTiers())) {
                    Thread.sleep(sleepDuring);
                    numberMaxOfSleep = numberMaxOfSleep - 1;
                    aff = getAff();
                }
            }

        } catch (Exception e) {
            // Nothing to do, C'est le traitement normal qui est exécuté
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), _getLangueDestinataireISO().toUpperCase());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        setTemplateFile(AFLettreDeBienvenue.MODEL_NAME);
        _setHeader(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        ArrayList documents = new ArrayList();
        Map nomDoc = new HashMap();
        nomDoc.put(
                AFLettreDeBienvenue.MODEL_NAME,
                getSession().getApplication().getLabel("NAOS_LETTRE_BIENVENUE",
                        _getLangueDestinataireISO().toUpperCase()));
        documents.add(nomDoc);
        // On sette le nom dans le dataSource
        this.setDataSource(documents);
    }

    private void fillDocInfo() {
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(aff.getAffilieNumero()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), aff.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    aff.getAffilieNumero(), affilieFormater.unformat(aff.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", aff.getAffilieNumero());
        }

        getDocumentInfo().setDocumentTypeNumber(AFLettreDeBienvenue.DOC_NO);
    }

    /**
     * Cette méthode permet de formater le texte {0} est remplacé par le titre du tiers {1} est remplacé par la date de
     * la demande d'affiliation {2} est remplacé par la date de début de l'affiliation {3} est remplacé par le numéro
     * d'affilié
     * 
     * @param paragraphe
     * @return
     * @throws Exception
     */
    private String format(String paragraphe) throws Exception {
        String res = "";
        for (int i = 0; i < paragraphe.length(); i++) {
            if (paragraphe.charAt(i) != '{') {
                res += paragraphe.charAt(i);
            } else if (paragraphe.charAt(i + 1) == '0') {
                /*
                 * String titre = TIAdresseDataSource.getTitre( getSession(), aff.getTiers().getTitreTiers(),
                 * aff.getTiers().getLangue()); if (JadeStringUtil.isEmpty(titre)) { titre =
                 * getSession().getLabel("MADAME_MONSIEUR"); } else { titre = titre.substring(0, 3); if
                 * (titre.startsWith("Mon") || titre.startsWith("Mad") || titre.startsWith("Mes") ||
                 * titre.startsWith("Maî") || titre.startsWith("Doc")) { titre = TIAdresseDataSource.getTitre(
                 * getSession(), aff.getTiers().getTitreTiers(), aff.getTiers().getLangue()); } else { titre =
                 * getSession().getLabel("MADAME_MONSIEUR"); } }
                 */

                // res += titre;
                res += aff.getTiers().getFormulePolitesse(null);
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '1') {
                res += JACalendar.format(aff.getDateDemandeAffiliation(), _getLangueDestinataireISO());
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '2') {
                res += aff.getDateDebut();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '3') {
                res += aff.getAffilieNumero();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '4') {
                if (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                    res += getSession().getApplication().getLabel("ANNEE", _getLangueDestinataireISO());
                    i = i + 2;
                } else if (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                    res += getSession().getApplication().getLabel("MOIS", _getLangueDestinataireISO());
                    i = i + 2;
                } else if (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                    res += getSession().getApplication().getLabel("TRIMESTRE", _getLangueDestinataireISO());
                    i = i + 2;
                } else {
                    i = i + 2;
                }
            } else if (paragraphe.charAt(i + 1) == '5') {
                if (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                    res += getSession().getApplication().getLabel("DE_ANNEE", _getLangueDestinataireISO());
                    i = i + 2;

                } else if (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                    res += getSession().getApplication().getLabel("DE_MOIS", _getLangueDestinataireISO());
                    i = i + 2;
                } else if (aff.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                    res += getSession().getApplication().getLabel("DE_TRIMESTRE", _getLangueDestinataireISO());
                    i = i + 2;
                } else {
                    i = i + 2;
                }
            } else if (paragraphe.charAt(i + 1) == '6') {

                res += aff.getAgenceCom(aff.getAffiliationId(), JACalendar.todayJJsMMsAAAA());
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '7') {
                if (JadeStringUtil.isBlankOrZero(aff.getDateFin())) {
                    res += getSession().getApplication().getLabel("NAOS_COLONNE_DESLE", _getLangueDestinataireISO());
                    res += aff.getDateDebut();
                } else {
                    res += getSession().getApplication().getLabel("NAOS_COLONNE_DU", _getLangueDestinataireISO());
                    res += aff.getDateDebut();
                    res += " " + getSession().getApplication().getLabel("NAOS_COLONNE_AU", _getLangueDestinataireISO());
                    res += aff.getDateFin();
                }
                i = i + 2;
            } else {
                i = i + 2;
            }
        }
        return res;
    }

    /**
     * @return
     */
    public AFAffiliation getAff() {

        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(idAffiliation);
        try {
            affiliation.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERREUR_RECUPERATION_AFFILIATION"));
        }
        return affiliation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer buffer = new StringBuffer();
        if (isOnError()) {
            buffer.append(getSession().getLabel("LETTRE_ERREUR"));

        } else {
            buffer.append(getSession().getLabel("LETTRE_SUCCES"));
        }
        return buffer.toString();
    }

    /**
     * Cette méthode va retourner le catalogue de texte à utiliser
     * 
     * @return ICTDocument[]
     */
    private ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            document.setISession(getSession());
            // On sette le domaine
            document.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
            document.setCodeIsoLangue(_getLangueDestinataireISO());
            // On sette le nom suivant le type d'affilié qu'on vient de créer
            // Création d'un indépendant
            if (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_INDEPENDANT);
            } else if (
            // Création d'un employeur uniquement AF
            (aff._cotisation(getTransaction(), aff.getAffiliationId(), CodeSystem.GENRE_ASS_PARITAIRE,
                    CodeSystem.TYPE_ASS_COTISATION_AVS_AI, "", JACalendar.today().toString(), 2) == null)
                    && (aff._cotisation(getTransaction(), aff.getAffiliationId(), CodeSystem.GENRE_ASS_PARITAIRE,
                            CodeSystem.TYPE_ASS_COTISATION_AF, "", JACalendar.today().toString(), 2) != null)
                    && aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY)) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_EMPLOYEUR_UNIQUEMENT_AF);
            } else if (
            // Création d'un non-actif
            aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
                // si branche économique 89 (étudiant) -> lettre spécifique
                if (aff.getBrancheEconomique().equals(CodeSystem.BRANCHE_ECO_ETUDIANTS)) {
                    document.setNom(AFLettreDeBienvenue.NOM_DOC_ETUDIANT);
                } else {
                    document.setNom(AFLettreDeBienvenue.NOM_DOC_NON_ACTIF);
                }
            } else if (
            // Création d'un employeur
            (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY) || aff.getTypeAffiliation().equals(
                    CodeSystem.TYPE_AFFILI_EMPLOY_D_F))
                    && !aff.isOccasionnel().booleanValue()) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_EMPLOYEUR);
            } else if (
            // Création d'un indépendant et employeur
            aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY) && !aff.isOccasionnel().booleanValue()) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_INDEPENDANT_ET_EMPLOYEUR);
            } else if ((
            // Création d'un indépendant et employeur
                    aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_TSE))
                    || aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_TSE);
            } else if (
            // Création d'un employeur potentiel (SA sans personnel)
            (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_EMPLOY) || aff.getTypeAffiliation().equals(
                    CodeSystem.TYPE_AFFILI_EMPLOY_D_F))
                    && aff.isOccasionnel().booleanValue()) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_EMPLOYEUR_POTENTIEL);
            } else if (
            // Création d'un indépendant qui pourrait être un employeur
            // potentiel
            aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY) && aff.isOccasionnel().booleanValue()) {
                document.setNom(AFLettreDeBienvenue.NOM_DOC_INDEPENDANT_EMPLOYEUR_POTENTIEL);
            }
            // On sette le type de document
            document.setCsTypeDocument(CodeSystem.LETTRE_BIENVENUE);
            // On dit que le document doit être actif pour être sélectionné
            document.setActif(new Boolean(true));
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }
        return res;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Retourne le texte à un niveau donné
     * 
     * @param niveau
     * @return
     * @throws Exception
     */
    private String getTexte(int niveau) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
            if (listeTextes == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "");
            } else {
                for (int i = 0; i < listeTextes.size(); i++) {
                    texte = listeTextes.getTexte(i + 1);
                    if ((i + 1) < listeTextes.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }
        return format(resString);
    }

    public TITiersViewBean getTiers() {
        if (aff == null) {
            return getAff().getTiers();
        } else {
            return aff.getTiers();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param string
     */
    public void setId(String string) {
        idAffiliation = string;
    }

    public void setIdAffiliation(String string) {
        idAffiliation = string;
    }

}
