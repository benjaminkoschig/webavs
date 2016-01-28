/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.suiviReleves;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.naos.itext.AFAbstractDocument;
import globaz.naos.itext.AFAdresseDestination;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Permet de générer le document des suivi des relevés
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFSuiviReleves_Doc extends AFAbstractDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String getPeriodeAffichage(String periode, String langueIsoTiers) {
        // recherche de l'année
        String year = periode.substring(periode.length() - 4);
        String moisPeriode = periode.substring(0, periode.length() - 5);
        String mois = "";
        if (moisPeriode.length() <= 2) {
            // un seul mois, format mm.aaaa
            mois += JACalendar.getMonthName(Integer.valueOf(moisPeriode).intValue(), langueIsoTiers) + " ";
        } else {
            // deux mois
            String moisPeriodeDebut = moisPeriode.substring(0, periode.indexOf('-'));
            String moisPeriodeFin = moisPeriode.substring(periode.indexOf('-') + 1);
            if (moisPeriodeDebut.length() <= 2) {
                // format mm-mm.aaaa
                mois += JACalendar.getMonthName(Integer.valueOf(moisPeriodeDebut).intValue(), langueIsoTiers) + " ";
                mois += "à ";
                mois += JACalendar.getMonthName(Integer.valueOf(moisPeriodeFin).intValue(), langueIsoTiers) + " ";
            } else {
                // format mm.aaaa-mm.aaaa
                mois += JACalendar.getMonthName(
                        Integer.valueOf(moisPeriodeDebut.substring(0, moisPeriodeDebut.indexOf('.'))).intValue(),
                        langueIsoTiers) + " ";
                mois += "à ";
                mois += JACalendar.getMonthName(Integer.valueOf(moisPeriodeFin).intValue(), langueIsoTiers) + " ";
            }
        }
        return mois + year;
    }

    // Adresse de destination
    AFAdresseDestination adresse;
    // Document pour le catalogue de texte
    ICTDocument[] document = null;

    // Nombre de niveaux pour le catalogue de texte
    int nbNiveaux = 0;

    public AFSuiviReleves_Doc() throws Exception {
        super();
    }

    /**
     * Constructeur AFSuiviReleves_DOC
     * 
     * @param session
     * @throws Exception
     */
    public AFSuiviReleves_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFSuviReleves_Param.L_NOMDOCSUIVIRELEVE));
    }

    /**
     * A executer avant la construction du document
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        ICaisseReportHelper caisseReportHelper = null;
        try {
            caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), getIsoLangueDestinataire());
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        try {
            setHeader(headerBean, getIsoLangueDestinataire());
        } catch (Exception e1) {
            // TODO Bloc catch auto-généré
            e1.printStackTrace();
        }
        try {
            caisseReportHelper.addHeaderParameters(this, headerBean);
        } catch (Exception e2) {
            // TODO Bloc catch auto-généré
            e2.printStackTrace();
        }
        super.beforeBuildReport(caisseReportHelper);

    }

    /**
     * Crée la source de données du document
     */
    @Override
    public void createDataSource() throws Exception {
        super.createDataSource();

        // première chose à faire renseigner le docinfo
        fillDocInfo();

        // Création du header du document
        /*
         * ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
         * getSession().getApplication(), getSession().getIdLangueISO()); CaisseHeaderReportBean headerBean = new
         * CaisseHeaderReportBean();
         * 
         * setHeader(headerBean); caisseReportHelper.addHeaderParameters(this, headerBean);
         */
        // On sette le nom du document attaché
        ArrayList documents = new ArrayList();
        Map nomDoc = new HashMap();
        nomDoc.put(AFSuviReleves_Param.L_NOMDOCSUIVIRELEVE,
                getSession().getApplication().getLabel("NAOS_SUIVI_RELEVE_NOMDOC", getIsoLangueDestinataire()));
        documents.add(nomDoc);
        // On sette le nom dans le dataSource
        this.setDataSource(documents);
    }

    @Override
    protected void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber("0005CAF");
        super.fillDocInfo();
    }

    private String format(String paragraphe) throws Exception {
        String delai = FWFindParameter.findParameter(getTransaction(), "10800040", "DELAIPMT", "0", "0", 2);
        String taxe = FWFindParameter.findParameter(getTransaction(), "10800041", "TAXE", "", "0", 2);
        String res = "";

        for (int i = 0; i < paragraphe.length(); i++) {
            if (paragraphe.charAt(i) != '{') {
                res += paragraphe.charAt(i);
            } else if (paragraphe.charAt(i + 1) == '0') {
                res += AFSuiviReleves_Doc.getPeriodeAffichage(getPeriode(), getIsoLangueDestinataire());
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '1') {
                // Prend la date de rappel et ajoute le nombre de jours définit
                // dans la base de données
                int delaiD = new Double(delai).intValue();
                String dateRappel = documentDataSource.getField(LEEnvoiDataSource.DATE_RAPPEL);
                JACalendar dateEcheanceCal = new JACalendarGregorian();
                String dateEcheance = "";
                dateEcheance = dateEcheanceCal.addDays(dateRappel, delaiD);
                res += JACalendar.format(dateEcheance, getIsoLangueDestinataire());
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '2') {
                res += taxe + " CHF";
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '3') {
                res += getAdresse().getFormulePolitesse(getIdDestinataire());
                i = i + 2;
            }

        }
        return res;
    }

    /**
     * Renvoie l'adresse de destination
     * 
     * @return
     */
    public AFAdresseDestination getAdresse() {
        if (adresse == null) {
            adresse = new AFAdresseDestination(getSession());
        }
        return adresse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getCategorie()
     */
    @Override
    public String getCategorie() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getDomaine()
     */
    @Override
    public String getDomaine() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * Retourne le document contenant les textes à utiliser
     * 
     * @return res
     */
    private ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_API"));
        }

        document.setISession(getSession());
        document.setCsDomaine(CodeSystem.DOMAINE);

        /*
         * if(documentDataSource.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE).equals
         * (ILEConstantes.CS_RELEVES_SOMMATIOM)){ document.setCsTypeDocument(CodeSystem.RAPPEL_SUIVI_RELEVES); }else{
         * //Sinon on lui passe le type sommation document.setCsTypeDocument(CodeSystem.SOMMATION_SUIVI_RELEVES); }
         */

        // pour séléctionner le type de document on se base sur l'étape actuelle
        // et non pas l'étape suivante car
        // la FER par exemple n'utilise pas l'étape sommation
        if (documentDataSource.getField(LEEnvoiDataSource.CS_TYPE_DOCUMENT).equals(ILEConstantes.CS_RELEVES_RAPPEL)) {
            document.setCsTypeDocument(CodeSystem.RAPPEL_SUIVI_RELEVES);
        }
        if (documentDataSource.getField(LEEnvoiDataSource.CS_TYPE_DOCUMENT).equals(ILEConstantes.CS_RELEVES_SOMMATIOM)) {
            document.setCsTypeDocument(CodeSystem.SOMMATION_SUIVI_RELEVES);
        }

        // document.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        document.setDefault(new Boolean(true));
        document.setActif(new Boolean(true));
        try {
            document.setCodeIsoLangue(getIsoLangueDestinataire());
        } catch (Exception e) {
            document.setCodeIsoLangue("fr");
        }

        try {
            // On charge tous les documents correspondants à notre requete
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_DOC"));
        }

        return res;

    }

    @Override
    public String getIsoLangueDestinataire() throws Exception {
        return getAdresse().getLangueDestinataire(getIdDestinataire());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getNbLevel()
     */
    @Override
    public int getNbLevel() {
        // TODO Raccord de méthode auto-généré
        return 0;
    }

    /**
     * Renvoie le nom du destinataire
     */
    @Override
    protected String getNomDestinataire() throws Exception {
        return getAdresse().getNomDestinataire(getIdTiers());
    }

    /**
     * Renvoie le nom du document
     */
    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFSuviReleves_Param.L_NOMDOCSUIVIRELEVE);
    }

    /**
     * Retourne le template à utiliser
     */
    @Override
    protected String getTemplate() {
        return AFSuviReleves_Param.TEMPLATESUIVIRELEVE;
    }

    /**
     * Retourne le texte du niveau sélectionné
     * 
     * @param niveau
     * @return
     */
    private String getTexte(int niveau) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        // Si le document est nul on envoie un message d'info à l'utilisateur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_DOC_DEFAUT"), FWMessage.ERREUR, "ERREUR");
        } else {
            // Sinon on va charger les textes du niveau en paramètres pour le
            // document
            ICTListeTextes listeTextes = null;
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                getMemoryLog().logMessage(getSession().getLabel("ERROR_GETTING_LIST_TEXTES"), FWMessage.ERREUR,
                        "ERREUR");
            }
            // Si il n'y a pas de texte pour le niveau, on envoie un message
            // d'info à l'utilisateur
            if (listeTextes == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE"), FWMessage.ERREUR, "ERREUR");
            } else {
                // Dans le cas contraire on va concaténer tous les textes du
                // niveau dans une chaine de caractères
                for (int i = 0; i < listeTextes.size(); i++) {
                    texte = listeTextes.getTexte(i + 1);
                    if (i + 1 < listeTextes.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }

        return this.format(resString);
    }

    /**
     * Initialise le document
     */
    @Override
    protected void initDocument(String isoLangue) throws Exception {
        // On sette le document à utiliser
        document = this.getICTDocument();
        try {
            nbNiveaux = document[0].size();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("DOC_VIDE"));
        }
        // On lui sette le texte qui sera dans le concerne
        this.setParametres(AFSuviReleves_Param.P_CONCERNE, getTexte(1));
        // On lui sette le texte qui sera dans le corps du document
        this.setParametres(AFSuviReleves_Param.P_CORPS, getTexte(2));
        // On lui sette le texte qui sera la signature du document
        this.setParametres(AFSuviReleves_Param.P_SIGNATURE, getTexte(3));
        // On lui sette le texte qui sera le PS du document
        this.setParametres(AFSuviReleves_Param.P_PS, getTexte(4));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#setFieldToCatTexte(int, java.lang.String)
     */
    @Override
    public void setFieldToCatTexte(int i, String value) {
        // TODO Raccord de méthode auto-généré

    }

    /**
     * Set le header du document
     */
    @Override
    public void setHeader(CaisseHeaderReportBean bean, String isoLangueTiers) throws Exception {
        bean.setAdresse(getAdresse().getAdresseDestinatairePlanFactu(getIdTiers(), getNumAff()));
        bean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), getIsoLangueDestinataire()));
        bean.setNoAffilie(getNumAff());
        bean.setNoAvs(" ");
        bean.setEmailCollaborateur(" ");
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());

    }

}
