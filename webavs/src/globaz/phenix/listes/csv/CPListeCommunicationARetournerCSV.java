/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
package globaz.phenix.listes.csv;

import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPCommunicationPlausibilite;
import globaz.phenix.db.communications.CPCommunicationPlausibiliteManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;

/**
 * class CPImpressionCotisationDifferente écrit le 19/01/05 par JPA
 * 
 * class créant un document .xls des décisions avec mise en compte
 * 
 * @author JPA
 */
public class CPListeCommunicationARetournerCSV extends BProcess {
    private static final long serialVersionUID = -5425789957882220691L;
    public static final int CS_ALLEMAND = 503002;
    public static final int CS_FRANCAIS = 503001;
    public static final int CS_ITALIEN = 503004;
    private String canton = "";
    private String dateFichier = "";
    private String emailObjet = "";
    private String idPlausibilite = "";
    private ICommunicationrRetourManager manager = null;
    private BProcess processAppelant = null;
    private Boolean simulation = Boolean.TRUE;
    private ArrayList<String> tabRetour = new ArrayList<String>();

    public CPListeCommunicationARetournerCSV() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        CPRetourIFDCSVFile doc = new CPRetourIFDCSVFile();
        doc = populateSheet(doc);
        doc.setFilename(getNomFichier());
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setDocumentType("");
        docInfo.setDocumentTypeNumber("");
        this.registerAttachedDocument(docInfo, doc.getOutputFile());
        return false;

    }

    @Override
    protected void _validate() throws Exception {
    }

    public String getCanton() {
        return canton;
    }

    public String getDateFichier() {
        return dateFichier;
    }

    @Override
    protected String getEMailObject() {
        return emailObjet;
    }

    /**
     * Réalise le mapping des langue du canton en CODE SYSTEM
     * 
     * @author:BTC
     * @return int CS_FRANCAIS = 503001; CS_ALLEMAND = 503002; CS_ITALIEN = 503004;
     */
    private int getIdLangue() throws Exception {
        int idLangue = CPListeCommunicationARetournerCSV.CS_FRANCAIS; // defaut
        idLangue = Integer.parseInt(((CPApplication) getSession().getApplication())
                .getLangueCantonISO(globaz.phenix.translation.CodeSystem.getCode(getSession(), getCanton())));
        return idLangue;
    }

    public String getIdPlausibilite() {
        return idPlausibilite;
    }

    protected String getLangue() throws Exception {
        switch (getIdLangue()) {
            case CS_FRANCAIS:
                return "FR";
            case CS_ITALIEN:
                return "IT";
            default:
                return "DE";
        }
    }

    /**
     * @param retour
     * @throws Exception
     */
    private String getLibelleErreur(ICommunicationRetour retour) throws Exception {
        String msgString = "";
        CPCommunicationPlausibiliteManager managerPlausi = new CPCommunicationPlausibiliteManager();
        managerPlausi.setSession(getSession());
        managerPlausi.setForIdCommunication(retour.getIdRetour());
        managerPlausi.find();
        for (int i = 0; i < managerPlausi.size(); i++) {
            CPCommunicationPlausibilite lien = (CPCommunicationPlausibilite) managerPlausi.get(i);
            if (JadeStringUtil.isEmpty(msgString) == false) {
                msgString += "\n";
            }
            msgString += lien.getLibellePlausibilite(getSession(), retour);
        }
        return msgString;
    }

    public ICommunicationrRetourManager getManager() {
        return manager;
    }

    /**
     * @throws Exception
     */
    private String getNomFichier() throws Exception {
        // Recherche du nom de fichier (par canton)
        String nom = "";
        CPReceptionReaderManager manager = new CPReceptionReaderManager();
        manager.setSession(getSession());
        manager.setForIdCanton(getCanton());
        manager.changeManagerSize(1);
        manager.find();
        if (manager.size() > 0) {
            CPReceptionReader reader = (CPReceptionReader) manager.getFirstEntity();
            if (reader.getNomFichier().length() > 0) {
                nom = reader.getNomFichier() + "_";
            }
        }
        String nomSuite = "";
        try {
            nomSuite = (getSession().getApplication()).getProperty("nomPourRetourFisc");
            if (JadeStringUtil.isNull(nomSuite)) {
                nomSuite = "";
            } else {
                nomSuite += "_";
            }
        } catch (Exception e) {
            nomSuite = "";
        }
        nom += nomSuite;
        if (!JadeStringUtil.isEmpty(getIdPlausibilite())) {
            nom += getIdPlausibilite() + "_";
        }
        nom += JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD) + "_";
        return nom;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public Boolean getSimulation() {
        return simulation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public CPRetourIFDCSVFile populateSheet(CPRetourIFDCSVFile doc) throws Exception {
        // On retourne la liste spécifique au canton de Zurich
        if (getCanton().equals(IConstantes.CS_LOCALITE_CANTON_VAUD)) {
            return populateSheetVaud(doc);
            // On retourne la liste standard
        } else {
            return populateSheetStandard(doc);
        }
    }

    /*
     * initialisation de la feuille xls Standard
     */
    // création de la liste décisions avec mise en compte
    public CPRetourIFDCSVFile populateSheetStandard(CPRetourIFDCSVFile doc) throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleRetourViewBean retour = null;
        String line = null;
        String sep = ";";
        // Création entête colonne
        tabRetour = setTitleRowStandart();
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(getManager().getCount(getTransaction()));
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        statement = getManager().cursorOpen(getTransaction());
        // parcours du manager et remplissage des cell
        while (((retour = (CPCommunicationFiscaleRetourViewBean) getManager().cursorReadNext(statement)) != null)
                && (!retour.isNew()) && (retour != null)) {
            // 0 - n° affilié
            line = retour.getNumAffilie() + sep;
            // 1 - n° AVS
            line += retour.getNumAvs(0) + sep;
            // 2 - n° de contribuable
            line += retour.getNumContribuableRecu() + sep;
            // 3 - nom et prénom
            line += retour.getPrenom() + " " + retour.getNom() + sep;
            // On va rechercher l'adresse et la localité
            String ville = "";
            String rue = "";
            String numRue = "";
            String npa = "";
            TITiers t = new TITiers();
            t.setSession(getSession());
            t.setIdTiers(retour.getIdTiers());
            t.retrieve();
            TIAdresseDataSource d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005", "31.12."
                    + retour.getAnnee1(), true);
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "01.01." + retour.getAnnee1(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "31.12." + retour.getAnnee1(), true);
            }
            if (d == null) {
                d = t.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        "01.01." + retour.getAnnee1(), true);
            }
            if (d != null) {
                ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
            }
            // 4 - Rue
            line += rue + " " + numRue + sep;
            // 5 - npa
            line += npa + sep;
            // 6 - localité
            line += ville + sep;
            // 7 - Genre
            line += CodeSystem.getLibelle(getSession(), retour.getGenreAffilie()) + sep;
            // 8 - Année
            line += retour.getAnnee1() + sep;
            // 9 - Erreur
            line += getLibelleErreur(retour) + sep;
            // Ajout au tableau
            tabRetour.add(line);
            // Mise à jour
            if (getSimulation().equals(Boolean.FALSE)) {
                CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                comFis.setSession(getSession());
                comFis.setIdCommunication(retour.getIdCommunication());
                comFis.retrieve(getTransaction());
                if (!comFis.isNew()) {
                    comFis.setDateRetour("");
                    comFis.update(getTransaction());
                }
            }

            processAppelant.incProgressCounter();
        }
        doc.setData(tabRetour);
        return doc;
    }

    public CPRetourIFDCSVFile populateSheetVaud(CPRetourIFDCSVFile doc) throws Exception {
        BStatement statement = null;
        CPCommunicationFiscaleRetourVDViewBean retour = null;
        String line = null;
        String sep = ";";
        // Création entête colonne
        tabRetour = setTitleRowVaud();
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(getManager().getCount(getTransaction()));
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        statement = getManager().cursorOpen(getTransaction());
        // parcours du manager et remplissage des cell
        while (((retour = (CPCommunicationFiscaleRetourVDViewBean) getManager().cursorReadNext(statement)) != null)
                && (!retour.isNew()) && (retour != null)) {
            // 1 date retour
            line = retour.getDateRetour() + sep;
            // 2 année
            line += retour.getAnnee1() + sep;
            // 3 numéro de contribuable
            line += retour.getVdNumContribuable() + sep;
            // 4 nom et prénom
            line += retour.getVdNomPrenom() + sep;
            // 5 n° AVS
            line += retour.getVdNumAvs() + sep;
            // 6 Genre
            line += retour.getVdGenreAffilie() + sep;
            // 7 n° affilié
            line += retour.getVdNumAffilie() + sep;
            // 8 adresse
            // line += retour.getVdAddRue()+ sep;
            // 9 npa- Localité
            // line += retour.getVdNumLocalite()+ sep;
            // 10 Erreur
            line += getLibelleErreur(retour) + sep;
            // Ajout au tableau
            tabRetour.add(line);
            // Mise à jour
            if (Boolean.FALSE.equals(getSimulation())) {
                CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                comFis.setSession(getSession());
                comFis.setIdCommunication(retour.getIdCommunication());
                comFis.retrieve(getTransaction());
                if (!comFis.isNew()) {
                    comFis.setDateRetour("");
                    comFis.update(getTransaction());
                }
            }
            processAppelant.incProgressCounter();
        }
        doc.setData(tabRetour);
        return doc;
    }

    public void setCanton(String forCanton) {
        canton = forCanton;
    }

    public void setDateFichier(String string) {
        dateFichier = string;
    }

    public void setEmailObjet(String emailObjet) {
        this.emailObjet = emailObjet;
    }

    public void setIdPlausibilite(String idPlausibilite) {
        this.idPlausibilite = idPlausibilite;
    }

    public void setManager(ICommunicationrRetourManager manager) {
        this.manager = manager;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... standard
     */
    private ArrayList<String> setTitleRowStandart() throws Exception {
        final String naff = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String numContribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String prenomNom = getSession().getApplication().getLabel("PRENOM_NOM", getLangue());
        final String adresse = getSession().getApplication().getLabel("ADRESSE", getLangue());
        final String npa = getSession().getApplication().getLabel("NPA", getLangue());
        final String localite = getSession().getApplication().getLabel("LOCALITE", getLangue());
        final String genre = getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue());
        final String annee = getSession().getApplication().getLabel("ANNEE", getLangue());
        final String erreur = getSession().getApplication().getLabel("ERREUR", getLangue());
        final String[] COL_TITLES = { naff, numAvs, numContribuable, prenomNom, adresse, npa, localite, genre, annee,
                erreur };
        // create Title Row
        String line = "";
        for (int i = 0; i < COL_TITLES.length; i++) {
            line += (COL_TITLES[i]) + ";";
        }
        // Ajout au tableau
        tabRetour.add(line);
        return tabRetour;
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures... spécifique à vaud
     */
    private ArrayList<String> setTitleRowVaud() throws Exception {
        final String dateRetour = getSession().getApplication().getLabel("DETAIL_FISC_VD_DATE_RECEPTION", getLangue());
        final String annee = getSession().getApplication().getLabel("ANNEE", getLangue());
        final String numContribuable = getSession().getApplication().getLabel("NUM_CONTRIBUABLE", getLangue());
        final String nomPrenom = getSession().getApplication().getLabel("NOM_PRENOM", getLangue());
        final String numAvs = getSession().getApplication().getLabel("NUM_AVS", getLangue());
        final String genreAffilie = getSession().getApplication().getLabel("GENRE_AFFILIE", getLangue());
        final String numAffilie = getSession().getApplication().getLabel("NUM_AFFILIE", getLangue());
        final String erreur = getSession().getApplication().getLabel("ERREUR", getLangue());
        final String[] COL_TITLES = { dateRetour, annee, numContribuable, nomPrenom, numAvs, genreAffilie, numAffilie,
                erreur };
        // create Title Row
        String line = "";
        for (int i = 0; i < COL_TITLES.length; i++) {
            line += (COL_TITLES[i]) + ";";
        }
        // Ajout au tableau
        tabRetour.add(line);
        return tabRetour;
    }

}