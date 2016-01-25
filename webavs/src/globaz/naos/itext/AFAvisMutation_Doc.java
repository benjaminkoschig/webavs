package globaz.naos.itext;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.avisMutation.AFProcessAvisSedexWriterNew;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.process.fichierCentral.AFExportFichierCentralXmlParser;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.TIAdresseListFormater;
import globaz.pyxis.api.ITIHistoriqueTiers;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIHistoriqueTiers;
import globaz.pyxis.db.tiers.TIHistoriqueTiersManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonProperties;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import org.w3c.dom.Element;

/**
 * Insérez la description du type ici. Date de création : (02.05.2003 10:26:03)
 * 
 * @author: Administrator, vre
 */
public class AFAvisMutation_Doc extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Une classe interne qui interprete l'historique d'un tiers et determine quels étaient ses noms et no AVS avant la
     * derniere modification en date et les valeurs des booleens nomChange et noAVSChange.
     * 
     * @author vre
     */
    private class DonneesTiers {

        // type
        public static final int TIERS_ACTUEL = 0;
        public static final int TIERS_ANCIEN_AVS = 3;
        public static final int TIERS_ANCIEN_NOM = 1;
        public static final int TIERS_ANCIEN_NOMAVS = 2;

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private JADate date;
        private JADate dateDerniereModif;
        private String designation3;
        private String designation4;
        private String noAVS;
        private String nom;
        private String prenom;
        private String titre;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private DonneesTiers(AFAffiliation affToUse, int type) throws Exception {
            if ((DonneesTiers.TIERS_ANCIEN_NOM == type) || (DonneesTiers.TIERS_ANCIEN_NOMAVS == type)) {
                /*
                 * Une modification a semble-t-il ete apportee au nom du tiers, on va determiner le nom du tiers avant
                 * la derniere modification en date, pour cela on va:
                 * 
                 * 1. charger l'historique du tiers 2. classer les modifications par date de modif croissante dans une
                 * map. 3. enlever de la map la derniere modif en date. 4. fusionner les historiques restants pour
                 * obtenir le nom le plus recent
                 */

                // 1. charger l'historique du tiers
                TIHistoriqueTiersManager historiques = new TIHistoriqueTiersManager();

                historiques.setForIdTiers(affToUse.getIdTiers());
                historiques.setSession(getSession());
                historiques.find();

                /*
                 * 2. classer les historiques par date de modif pour trier les modifs par date on utilise une TreeMap
                 */
                TreeMap /* {dateModif -> MapChamps} */<JADate, HashMap<String, TIHistoriqueTiers>> dateModToChamps = new TreeMap<JADate, HashMap<String, TIHistoriqueTiers>>(
                        new Comparator<Object>() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                long date1 = ((JADate) o1).toLong();
                                long date2 = ((JADate) o2).toLong();

                                if (date1 > date2) {
                                    return 1;
                                } else if (date1 < date2) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });

                for (int idHist = 0; idHist < historiques.size(); ++idHist) {
                    TIHistoriqueTiers hist = (TIHistoriqueTiers) historiques.get(idHist);
                    JADate dateMod = new JADate(hist.getDateDebut());

                    HashMap /* {noChamp -> TIHistoriqueTiers} */<String, TIHistoriqueTiers> champs = dateModToChamps
                            .get(dateMod);

                    if (champs == null) {
                        champs = new HashMap<String, TIHistoriqueTiers>();
                        dateModToChamps.put(dateMod, champs);
                    }

                    TIHistoriqueTiers val = champs.get(dateMod);

                    /*
                     * on insere dans la map des champs uniquement si le champ ne s'y trouve pas encore ou que le champ
                     * qui s'y trouve deja est issu d'une modification plus ancienne. Ce dernier cas peut survenir dans
                     * les cas ou plusieurs modifs ont ete effectuees le meme jour.
                     */
                    if ((val == null) || (hist.getSpy().getTimeStamp() > val.getSpy().getTimeStamp())) {
                        champs.put(hist.getChamp(), hist);
                    }
                }

                // il y a forcement au moins une entree dans la map, celle qui
                // correspond a la creation du tiers
                if (dateModToChamps.size() > 1) {
                    // s'il y a plus d'une entree dans la map c'est que des
                    // modifications ont effectivement ete faites
                    // 3. enlever la derniere modif
                    dateDerniereModif = dateModToChamps.lastKey();
                    dateModToChamps.remove(dateModToChamps.lastKey());

                    // 4. fusionner les valeurs restantes pour obtenir les
                    // donnees exhaustives
                    // seules les valeurs modifiees sont historisees, si on veut
                    // obtenir toutes les données il faut
                    // donc forcement fusionner tout l'historique
                    Iterator<JADate> clesIter = dateModToChamps.keySet().iterator(); // dates
                    // croissantes

                    date = clesIter.next();

                    /*
                     * on commence avec les valeurs a la creation du tiers, c'est la seule map qui contiendra toutes les
                     * valeurs (nom, prenom, ...)
                     */
                    HashMap<String, TIHistoriqueTiers> champs = dateModToChamps.get(date);

                    // fusionner les modifs plus recentes
                    while (clesIter.hasNext()) {
                        date = clesIter.next();

                        for (Iterator<?> valeursIter = ((HashMap<?, ?>) dateModToChamps.get(date)).values().iterator(); valeursIter
                                .hasNext();) {
                            TIHistoriqueTiers recent = (TIHistoriqueTiers) valeursIter.next();

                            champs.put(recent.getChamp(), recent); // remplacer
                        }
                    }

                    nom = (champs.get(ITIHistoriqueTiers.FIELD_DESIGNATION1)).getValeur();
                    prenom = (champs.get(ITIHistoriqueTiers.FIELD_DESIGNATION2)).getValeur();
                    designation3 = (champs.get(ITIHistoriqueTiers.FIELD_DESIGNATION3)).getValeur();
                    designation4 = (champs.get(ITIHistoriqueTiers.FIELD_DESIGNATION4)).getValeur();
                    titre = (champs.get(ITIHistoriqueTiers.FIELD_TITRE)).getValeur();
                } else {
                    // nomChange est a vrai par erreur car il n'y a pas eu de
                    // changements depuis la creation du tiers
                    nom = affToUse.getTiers().getDesignation1();
                    prenom = affToUse.getTiers().getDesignation2();
                    designation3 = affToUse.getTiers().getDesignation3();
                    designation4 = affToUse.getTiers().getDesignation4();
                    titre = affToUse.getTiers().getTitreTiers();
                }
            } else {
                // le nom n'a pas ete change ou l'utilisteur ne veut pas en
                // tenir compte
                nom = affToUse.getTiers().getDesignation1();
                prenom = affToUse.getTiers().getDesignation2();
                designation3 = affToUse.getTiers().getDesignation3();
                designation4 = affToUse.getTiers().getDesignation4();
                titre = affToUse.getTiers().getTitreTiers();
            }

            if ((DonneesTiers.TIERS_ANCIEN_NOMAVS == type) || (DonneesTiers.TIERS_ANCIEN_AVS == type)) {
                // l'utilisateur veut annoncer un changement de no AVS
                TIHistoriqueAvsManager histAVS = new TIHistoriqueAvsManager();

                histAVS.setForIdTiers(affToUse.getIdTiers());
                histAVS.setSession(getSession());
                histAVS.find();

                // il y a au moins une valeur dans l'historique, celle de la
                // creation du tiers
                if (histAVS.size() > 1) {
                    JADate ddm = new JADate(((TIHistoriqueAvs) histAVS.get(histAVS.size() - 1)).getEntreeVigueur());
                    JADate d = new JADate(((TIHistoriqueAvs) histAVS.get(histAVS.size() - 2)).getEntreeVigueur());

                    if ((dateDerniereModif == null) || (ddm.toLong() < dateDerniereModif.toLong())) {
                        dateDerniereModif = ddm;
                    }

                    if ((date == null) || (d.toLong() < date.toLong())) {
                        date = d;
                    }

                    noAVS = ((TIHistoriqueAvs) histAVS.get(histAVS.size() - 2)).getNumAvs();
                }
            }

            if (JadeStringUtil.isEmpty(noAVS)) {
                // gere en meme temps la noAVSChange a true par erreur et le
                // noAVSChange a false
                noAVS = affToUse.getTiers().getNumAvsActuel();
            }
        }
    }

    // liste des caisse par canton
    private static Hashtable<String, String> caisseCanton = new Hashtable<String, String>();
    private static final String DONNEE_INCONNUE = "inconnu";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String FILE_NAME = "AvisMutation";
    protected static final String FILE_TITLE = "Avis Mutation";

    public static final String IMPR_CHANGEMENT = "1";

    public static final String IMPR_NOUVELLE_AFFILIATION = "2";
    public static final String IMPR_SORTIE = "4";
    // private static final String IMPR_CHANGEMENT_NOM_OU_NOAVS = "5";
    // private static final String IMPR_CHANGEMENT_ADRESSE = "6";
    private static final String MOTIF_CREATE_ASSOCIE = "12";
    private static final String MOTIF_CREATE_DOMICILE = "14";

    // motifs
    private static final String MOTIF_CREATE_RI_SOC = "11";
    private static final String MOTIF_CREATE_SUCCURSALE = "13";
    private static final String MOTIF_DELETE_ASSOCIE = "32";
    private static final String MOTIF_DELETE_DOMICILE = "34";
    private static final String MOTIF_DELETE_RI_SOC = "31";

    private static final String MOTIF_DELETE_SUCCURSALE = "33";
    private static final String MOTIF_UPDATE_ASSOCIE = "22";
    private static final String MOTIF_UPDATE_NEW = "20";

    private static final String MOTIF_UPDATE_RI_SOC = "21";

    private static final String MOTIF_UPDATE_SUCCURSALE = "23";
    protected static final String TEMPLATE_FILE = "NAOS_MUTATION_AVIS";
    private static final String TYPE_EXPLOITATION = "508021";

    static {
        AFAvisMutation_Doc.caisseCanton.put("505001", "001"); // CS_ZURICH
        AFAvisMutation_Doc.caisseCanton.put("505002", "002"); // CS_BERNE
        AFAvisMutation_Doc.caisseCanton.put("505003", "003"); // CS_LUCERNE
        AFAvisMutation_Doc.caisseCanton.put("505004", "004"); // CS_URI
        AFAvisMutation_Doc.caisseCanton.put("505005", "005"); // CS_SCHWYZ
        AFAvisMutation_Doc.caisseCanton.put("505006", "006"); // CS_OBWALD
        AFAvisMutation_Doc.caisseCanton.put("505007", "007"); // CS_NIDWALD
        AFAvisMutation_Doc.caisseCanton.put("505008", "008"); // CS_GLARIS
        AFAvisMutation_Doc.caisseCanton.put("505009", "009"); // CS_ZOUG
        AFAvisMutation_Doc.caisseCanton.put("505010", "010"); // CS_FRIBOURG
        AFAvisMutation_Doc.caisseCanton.put("505011", "011"); // CS_SOLEURE
        AFAvisMutation_Doc.caisseCanton.put("505012", "012"); // CS_BALE_VILLE
        AFAvisMutation_Doc.caisseCanton.put("505013", "013"); // CS_BALE_CAMPAGNE
        AFAvisMutation_Doc.caisseCanton.put("505014", "014"); // CS_SCHAFFOUSE
        AFAvisMutation_Doc.caisseCanton.put("505015", "015"); // CS_APPENZELL_AR
        AFAvisMutation_Doc.caisseCanton.put("505016", "016"); // CS_APPENZELL_AI
        AFAvisMutation_Doc.caisseCanton.put("505017", "017"); // CS_SAINT_GALL
        AFAvisMutation_Doc.caisseCanton.put("505018", "018"); // CS_GRISONS
        AFAvisMutation_Doc.caisseCanton.put("505019", "019"); // CS_ARGOVIE
        AFAvisMutation_Doc.caisseCanton.put("505020", "020"); // CS_THURGOVIE
        AFAvisMutation_Doc.caisseCanton.put("505021", "021"); // CS_TESSIN
        AFAvisMutation_Doc.caisseCanton.put("505022", "022"); // CS_VAUD
        AFAvisMutation_Doc.caisseCanton.put("505023", "023"); // CS_VALAIS
        AFAvisMutation_Doc.caisseCanton.put("505024", "024"); // CS_NEUCHATEL
        AFAvisMutation_Doc.caisseCanton.put("505025", "025"); // CS_GENEVE
        AFAvisMutation_Doc.caisseCanton.put("505026", "150"); // CS_JURA
    }
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private boolean adresseChange = false;
    private TIAvoirAdresse adresseDomicile;
    private AFAffiliation affiliation = null;

    private boolean avsChange = false;
    private boolean cantonChange = false;
    private boolean cantonDomDiff = false;
    protected AFAffiliation currentAffiliation;
    private String dateAvis = JACalendar.todayJJsMMsAAAA();
    private String dateImpression = "";
    // exporter XML pour annonces électroniques
    private AFExportFichierCentralXmlParser exporterXML = null;
    private java.lang.String idAffiliation = "";
    protected ArrayList<AFAffiliation> itAvis = new ArrayList<AFAffiliation>();
    private AFAffiliation maisonMere = null;
    private boolean morePageNeeded = false;
    private boolean nomChange = false;
    private String observation = "";

    private int pageNo = 1;
    private boolean pmChange = false;
    private String selectionImpression = null;
    private boolean siegeChange = false;

    boolean singleXML = false;

    private AFAffiliation snc = null;
    // private AFAvisMutation avisMutation;
    private TITiersViewBean tiers;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    // private AFAnnonceAffilie annonceAffiliation = null;
    // private Vector fileNames = new Vector();
    private String tiersId = "";

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 09:38:26)
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public AFAvisMutation_Doc() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Insérez la description de la méthode ici. Date de création : (26.03.2003 09:38:26)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @param newSelectionImpression
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public AFAvisMutation_Doc(BSession session, String newSelectionImpression) throws Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, AFAvisMutation_Doc.FILE_NAME);
        setFileTitle(AFAvisMutation_Doc.FILE_TITLE);
        selectionImpression = newSelectionImpression;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        try {
            if (isMorePageNeeded()) {
                AFAvisMutation_Doc process2 = getInstance(getSession(), selectionImpression);

                // process2.setObservation("");
                process2.setTiersId(getTiersId());
                process2.setIdAffiliation(idAffiliation);
                process2.setObservation(observation);
                process2.setPmChange(pmChange);
                process2.setSiegeChange(siegeChange);
                process2.setCantonChange(cantonChange);
                process2.setPageNo(2);
                process2.setCantonDomDiff(cantonDomDiff);
                process2.setDateAvis(dateAvis);
                // ArrayList tmp = new ArrayList();
                // tmp.add(currentAffiliation);
                // process2.setIteratorAvis(tmp);

                // process2.setOut(out);
                process2.executeProcess();

                List<?> l = process2.getDocumentList();

                for (int i = 0; i < l.size(); i++) {
                    addDocument((JasperPrint) l.get(i));
                }
            }
            // si impression isolé, génération du fichier XML correspondant
            // a activer avec mandat 5.1
            /*
             * String destination = Jade.getInstance().getHomeDir() + "/" + AFApplication.DEFAULT_APPLICATION_NAOS_REP +
             * "/work/fichierCentral" + System.currentTimeMillis() + ".xml"; Writer out = new OutputStreamWriter(new
             * FileOutputStream(destination),"ISO-8859-1");
             * out.write(JadeXmlWriter.asXML(getExporterXML().getDocument())); out.close();
             * registerAttachedDocument(destination);
             */

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber("0007CAF");
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Zones communes à toutes les différents type de décisions
        setSendCompletionMail(false);
        setFileTitle(AFAvisMutation_Doc.FILE_TITLE);
        setTemplateFile(AFAvisMutation_Doc.TEMPLATE_FILE);

        try {
            loadTiers();
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setSession(getSession());
            affiliationManager.setForIdTiers(getTiersId());
            affiliationManager.setForAffiliationId(getIdAffiliation());
            affiliationManager.find();

            for (int i = 0; i < affiliationManager.getSize(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);
                itAvis.add(affiliation);
            }
            if (itAvis.size() > 0) {
                currentAffiliation = itAvis.get(0);
            }
        } catch (Exception e) {
            throw new FWIException(e);
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean beforePrintDocument() {
        if (getImporter().size() == 0) {
            getImporter().setDocumentTemplate("VIDE");

            try {
                super.addDocument(getImporter().importReport());
            } catch (FWIException e) {
                // CAST OMMIT
            }
        }

        if (super.size() == 1) {
            try {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            } catch (FWIException e) {
                // CAST OMMIT
            }
        }

        return super.beforePrintDocument();
    }

    /**
     * Définition des types de modification, ici cela sert a rien, mais pour une classe fils oui.
     */
    protected void beforeSetMotif() throws Exception {
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void createDataSource() throws FWIException {
        fillDocInfo();
        try {
            super.setDocumentTitle(getSession().getLabel("TITRE_DOC_AVIS_MUTATION") + " "
                    + affiliation.getAffilieNumero());

            // recherche de l'adresse de l'affilié
            // actuelle sauf pour les changement de canton ou l'ancienne adresse
            // est affichée
            TIAvoirAdresse adresse = this.findAdresse(affiliation, !isMorePageNeeded(), true);

            // rechercher la localite pour l'adresse
            TILocalite localite = getLocalite(adresse);
            // Recherche du tiers
            DonneesTiers tiersToUse = new DonneesTiers(affiliation, DonneesTiers.TIERS_ACTUEL);

            if (adresseDomicile != null) {
                // tester si l'adresse privée est renseignée et si le canton est
                // différent de celle du siège pour la création et la radiation
                TILocalite locDom = getLocalite(adresseDomicile);
                if (locDom.getIdPays().equals(IConstantes.ID_PAYS_SUISSE)
                        && !localite.getIdCanton().equals(locDom.getIdCanton())
                        && !selectionImpression.equals(AFAvisMutation_Doc.IMPR_CHANGEMENT)) {
                    // canton différent
                    cantonDomDiff = true;
                    if (pageNo == 1) {
                        setMorePageNeeded(true);
                    }
                }
            }

            // recherche du/des motifs correspondant à la sélection effectuée
            findAndSetMotif();

            // adresses
            TIAvoirAdresse adresseGauche = adresse;
            TILocalite localiteGauche = localite;
            DonneesTiers tiersGauche = tiersToUse;
            if (AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && adresseChange && !cantonChange) {
                // utiliser l'ancienne adresse de l'affilie
                if (adresse != null) {
                    adresseGauche = adresse.findPreviousRelation();
                    localiteGauche = getLocalite(adresseGauche);
                }
            } else if (cantonDomDiff) {
                // affichage adresse privée
                if (pageNo == 1) {
                    // privée dans partie droite
                    adresse = adresseDomicile;
                    localite = getLocalite(adresseDomicile);
                } else {
                    // inverser les adresses
                    adresseGauche = adresseDomicile;
                    localiteGauche = getLocalite(adresseDomicile);
                }

            }

            if (AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && (nomChange || avsChange)) {
                // rechercher l'ancien no AVS ou nom
                if (nomChange && avsChange) {
                    tiersGauche = new DonneesTiers(affiliation, DonneesTiers.TIERS_ANCIEN_NOMAVS);
                } else if (nomChange) {
                    tiersGauche = new DonneesTiers(affiliation, DonneesTiers.TIERS_ANCIEN_NOM);
                } else if (avsChange) {
                    tiersGauche = new DonneesTiers(affiliation, DonneesTiers.TIERS_ANCIEN_AVS);
                }
            }

            // caisse dans en-tête
            super.setParametres("P_NOMCAISSE", AFAvisMutation_Doc.caisseCanton.get(localiteGauche.getIdCanton()) + " "
                    + getSession().getCodeLibelle(localiteGauche.getIdCanton()));
            Element avis = getExporterXML().addAvis(AFAvisMutation_Doc.caisseCanton.get(localiteGauche.getIdCanton()),
                    JACalendar.todayJJsMMsAAAA(), getSession().getUserFullName());

            // générer la partie gauche du document
            generePartieGauche(adresseGauche, localiteGauche, tiersGauche);

            // remplir la deuxieme moitie de la page s'il s'agit d'un changement
            // d'adresse ou de nom
            if (needPartieDroite()) {
                generePartieDroite(adresse, localite, tiersToUse);
            }
            // observations
            // si associé, ajouter la rs courte de la SNC
            if ((snc = AFAffiliationUtil.isAssocie(affiliation, dateAvis)) != null) {
                observation = getSession().getLabel("AVIS_OFAS_SNC") + ":\n" + snc.getRaisonSocialeCourt() + "\n"
                        + observation;
                getExporterXML().addSNC(avis,
                        getSession().getLabel("AVIS_OFAS_SNC") + ":\n" + snc.getRaisonSocialeCourt());
            }
            if (adresseDomicile != null) {
                // tester si l'adresse privée est renseignée et si le canton est
                // différent de celle du siège
                if (!cantonDomDiff) {
                    // même canton, afficher l'adresse dans les observations
                    TIAdresseDataSource ds = new TIAdresseDataSource();
                    ds.setSession(getSession());
                    ds.load(adresseDomicile);
                    TIAdresseListFormater f = new TIAdresseListFormater();
                    super.setParametres("P_OBSERVATION",
                            getSession().getLabel("AVIS_OFAS_PRIVE") + ":\n" + f.format(ds));
                    getExporterXML().addAdresseDomicile(avis,
                            getSession().getLabel("AVIS_OFAS_PRIVE") + ":\n" + f.format(ds));
                    super.setParametres("P_OBSERVATIONC2", observation);
                } else {
                    super.setParametres("P_OBSERVATION", observation);
                }
            } else {
                super.setParametres("P_OBSERVATION", observation);
            }
            getExporterXML().addObservations(avis, observation);
            /*
             * if ((affiliation.getDateFin().equalsIgnoreCase("")) || (affiliation.getDateFin().equalsIgnoreCase("0")))
             * { if (selectionImpression.equals("3")) { // n'est plus utilisé super.setParametres("P_OBSERVATION", "");
             * } else { super.setParametres("P_OBSERVATION", !JadeStringUtil.isBlank(observation)?observation:""); } }
             * if ((!affiliation.getDateFin().equals("")) && (!selectionImpression.equals("3"))) { // radiation
             * super.setParametres("P_OBSERVATION", !JadeStringUtil.isBlank(observation
             * )?observation:getSession().getCodeLibelle (affiliation.getMotifFin())); } if
             * (selectionImpression.equals("1")) { // changement de canton if (isDoAfterBuildReport()) { // ancienne
             * caisse super.setParametres("P_OBSERVATION", !JadeStringUtil.isBlank(observation
             * )?observation:getSession().getLabel("OBSERVATION_CHCANT")); } else { // nouvelle caisse
             * super.setParametres("P_OBSERVATION", !JadeStringUtil
             * .isBlank(observation)?observation:getSession().getCodeLibelle (affiliation.getMotifCreation())); } }
             */

            // pied de page
            super.setParametres("P_DATEAVIS", JACalendar.todayJJsMMsAAAA());
            super.setParametres("P_SACHBEARBEITER", getSession().getUserFullName());
            // BApplication applic = getSession().getApplication();
            String noCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
            super.setParametres("P_CAISSE_FOOTER", noCaisse.trim());
            // System.out.println("timbre.caisse."+tiers.getLangueIso());
            String timbre = getTemplateProperty(getDocumentInfo(), "timbre.caisse."
                    + tiers.getLangueIso().toUpperCase());
            if (JadeStringUtil.isEmpty(timbre)) {
                timbre = getTemplateProperty(getDocumentInfo(), "nom.caisse." + tiers.getLangueIso().toUpperCase())
                        + "\n"
                        + getTemplateProperty(getDocumentInfo(), "header.adresse.caisse."
                                + tiers.getLangueIso().toUpperCase());
            }
            super.setParametres("P_ADRESSE", timbre);

            super.getImporter().getParametre();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FWIException(e);
        }
    }

    private void fillDocInfo() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affiliation.getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliation.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affiliation.getAffilieNumero());
        }
        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getDocumentInfo().getDocumentProperty("numero.affilie.formatte"), getDocumentInfo()
                            .getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "afterPrintDocument()", e);
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    private TIAvoirAdresse findAdresse(AFAffiliation affToUse, boolean current, boolean saveDomicile) {
        // rechercher l'adresse de l'affilie
        TIAvoirAdresse adresseCriteres = new TIAvoirAdresse();
        adresseCriteres.setIdTiers(affToUse.getIdTiers());
        adresseCriteres.setSession(affToUse.getSession());
        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        // recherche adresse d'exploitation
        adresseCriteres.setTypeAdresse(AFAvisMutation_Doc.TYPE_EXPLOITATION);
        TIAvoirAdresse domicile;
        TIAvoirAdresse principale = this.findAdresse(adresseCriteres, current);
        if (principale == null) {
            // recherche sans no d'affilié
            adresseCriteres.setIdExterne("");
            principale = this.findAdresse(adresseCriteres, current);
            if (principale == null) {
                // recherche adresse courrier COTISATIONS
                adresseCriteres.setIdApplication(ICommonConstantes.CS_APPLICATION_COTISATION);
                adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
                principale = this.findAdresse(adresseCriteres, current);
                if (principale == null) {
                    // recherche adresse courrier sans no aff.
                    adresseCriteres.setIdExterne("");
                    principale = this.findAdresse(adresseCriteres, current);
                }
            }
            if (principale == null) {
                // recherche adresse courrier DEFAUT
                adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
                principale = this.findAdresse(adresseCriteres, current);
                if (principale == null) {
                    // recherche adresse courrier sans no aff.
                    adresseCriteres.setIdExterne("");
                    principale = this.findAdresse(adresseCriteres, current);
                }
            }
        }
        // recherche domicile
        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
        domicile = this.findAdresse(adresseCriteres, current);
        if (domicile == null) {
            // recherche dom sans no d'affilié
            adresseCriteres.setIdExterne("");
            domicile = this.findAdresse(adresseCriteres, current);
        }

        if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(affToUse.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equals(affToUse.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_LTN.equals(affToUse.getTypeAffiliation())) {
            // si employeur, utiliser domicile si existante, principale sinon
            if (domicile != null) {
                return domicile;
            } else {
                return principale;
            }
        } else {
            // pour IND, NA, etc, utiliser principale, domicile sinon
            if (principale == null) {
                return domicile;
            } else {
                if (saveDomicile) {
                    // sauver adresse domicile (privée)
                    adresseDomicile = domicile;
                }
                return principale;
            }
        }
    }

    private TIAvoirAdresse findAdresse(TIAvoirAdresse adresse, boolean current) {
        if (current) {
            return adresse.findCurrentRelation();
        } else {
            adresse = adresse.findCurrentRelation();
            if (adresse != null) {
                return adresse.findPreviousRelation();
            } else {
                return null;
            }
        }
    }

    /**
     * Recherche du motif correspondant à la sélection et attribut du code dans le document
     * 
     * @exception si
     *                une erreur survient
     */
    private void findAndSetMotif() throws Exception {
        String motifGauche = "P_MOTIF";
        String motifDroite = "P_MOTIFA2";
        if (selectionImpression != null) {

            if (selectionImpression.equals(AFAvisMutation_Doc.IMPR_CHANGEMENT) && cantonChange) {
                // dans le cas d'un changement de canton, on fait un document
                // pour chaque canton:
                if (pageNo == 1) {
                    // d'abord un 31, 32 ou 33 pour l'ancienne relation
                    if (AFAffiliationUtil.isAssocieCommanditaire(affiliation)) {
                        super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_DELETE_ASSOCIE);
                    } else if ((maisonMere = AFAffiliationUtil.isSuccursale(affiliation, dateAvis)) != null) {
                        super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_DELETE_SUCCURSALE);
                    } else {
                        super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_DELETE_RI_SOC);
                    }
                } else {
                    // ensuite un 11, 12 ou 13 pour la nouvelle relation
                    if (AFAffiliationUtil.isAssocieCommanditaire(affiliation)) {
                        super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_CREATE_ASSOCIE);
                    } else if ((maisonMere = AFAffiliationUtil.isSuccursale(affiliation, dateAvis)) != null) {
                        super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_CREATE_SUCCURSALE);
                    } else {
                        super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_CREATE_RI_SOC);
                    }
                }
            }
            if (selectionImpression.equals(AFAvisMutation_Doc.IMPR_NOUVELLE_AFFILIATION)) {
                // nouvelle affiliation
                // code 11 -> RI/Société, 12 -> associé, commanditaire, 13 ->
                // succursale
                if (pageNo == 2) {
                    // inverser 11 et 14
                    String temp = motifGauche;
                    motifGauche = motifDroite;
                    motifDroite = temp;
                }
                if (AFAffiliationUtil.isAssocieCommanditaire(affiliation)) {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_CREATE_ASSOCIE);
                } else if ((maisonMere = AFAffiliationUtil.isSuccursale(affiliation, dateAvis)) != null) {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_CREATE_SUCCURSALE);
                } else {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_CREATE_RI_SOC);
                }
                if (cantonDomDiff) {
                    // ajouter un 14 dans la partie de droite
                    super.setParametres(motifDroite, AFAvisMutation_Doc.MOTIF_CREATE_DOMICILE);
                }
            }

            if (selectionImpression.equals(AFAvisMutation_Doc.IMPR_SORTIE)) {
                // radiation 31, 32 ou 33
                if (pageNo == 2) {
                    // inverser 11 et 14
                    String temp = motifGauche;
                    motifGauche = motifDroite;
                    motifDroite = temp;
                }
                if (AFAffiliationUtil.isAssocieCommanditaire(affiliation)) {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_DELETE_ASSOCIE);
                } else if ((maisonMere = AFAffiliationUtil.isSuccursale(affiliation, dateAvis)) != null) {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_DELETE_SUCCURSALE);
                } else {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_DELETE_RI_SOC);
                }
                if (cantonDomDiff) {
                    // ajouter un 14 dans la partie de droite
                    super.setParametres(motifDroite, AFAvisMutation_Doc.MOTIF_DELETE_DOMICILE);
                }

            }

            if (selectionImpression.equals(AFAvisMutation_Doc.IMPR_CHANGEMENT) && !cantonChange) {
                // dans le cas d'un changement d'adresse ou de nom ou de no avs
                // on met les infos nouvelles a droite dans
                // un 20 et les infos anciennes a gauche dans un 21, 22 ou 23
                if (AFAffiliationUtil.isAssocieCommanditaire(affiliation)) {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_UPDATE_ASSOCIE);
                } else if ((maisonMere = AFAffiliationUtil.isSuccursale(affiliation, dateAvis)) != null) {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_UPDATE_SUCCURSALE);
                } else {
                    super.setParametres(motifGauche, AFAvisMutation_Doc.MOTIF_UPDATE_RI_SOC);
                }
                // motif partie droite
                super.setParametres(motifDroite, AFAvisMutation_Doc.MOTIF_UPDATE_NEW);
            }
        }
    }

    /**
     * Génère la partie droite du document
     * 
     * @param adresse
     *            l'adresse à afficher
     * @throws Exception
     *             si une erreur survient
     */
    private void generePartieDroite(TIAvoirAdresse adresse, TILocalite localite, DonneesTiers tiersToUse)
            throws Exception {
        // no affilié et IDE
        String numeroAffilie = AFIDEUtil.createNumAffAndNumeroIdeForPrint(affiliation.getAffilieNumero(),
                affiliation.getNumeroIDE(), affiliation.getIdeStatut());
        super.setParametres("P_NOAFFILIEA2", numeroAffilie);
        // personnalité juridique
        super.setParametres("P_JURIDIQUEA2", getSession().getCode(affiliation.getPersonnaliteJuridique()));

        // titre
        if (JadeStringUtil.isIntegerEmpty(tiersToUse.titre)) {
            super.setParametres("P_TITREA2", "0");
        } else if (ITITiers.CS_MADAME.equals(tiersToUse.titre)) {
            super.setParametres("P_TITREA2", "2");
        } else {
            super.setParametres("P_TITREA2", "1");
        }

        // no AVS
        if ((AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && avsChange) || cantonDomDiff) {
            super.setParametres("P_AVSNOA2", tiersToUse.noAVS);
        }

        // tiers
        if ((AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && (nomChange || avsChange))
                || cantonDomDiff) {
            super.setParametres("P_NOM6A2", tiersToUse.nom + " " + tiersToUse.prenom);
            super.setParametres("P_NOM7A2", tiersToUse.designation3);
            super.setParametres("P_NOM8A2", tiersToUse.designation4);
        }

        // adresse
        if ((adresse != null)
                && ((AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && adresseChange) || cantonDomDiff)) {
            if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse1())) {
                super.setParametres("P_NOM6A2", adresse.getLigneAdresse1());
            }
            if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse2())) {
                super.setParametres("P_NOM7A2", adresse.getLigneAdresse2());
            }
            if (!JadeStringUtil.isEmpty(adresse.getLigneAdresse3())) {
                super.setParametres("P_NOM8A2", adresse.getLigneAdresse3());
            }
            if (!JadeStringUtil.isEmpty(adresse.getRue())) {
                super.setParametres("P_RUENOA2", adresse.getRue() + " " + adresse.getNumeroRue());
            } else {
                // afficher la case postale
                super.setParametres(
                        "P_RUENOA2",
                        TIAdresseDataSource.getFullCasePostale(affiliation.getTiers().getLangue(),
                                adresse.getCasePostale()));
            }
            super.setParametres("P_PLZA2", localite.getLocaliteCourt());
            super.setParametres("P_LIEUA2", localite.getNumPostal() + " " + localite.getLocalite());
        } else {
            // si changement de nom et/ou no AVS, ne pas imprimer l'adresse
            super.setParametres("P_RUENOA2", "");
            super.setParametres("P_PLZA2", "");
            super.setParametres("P_LIEUA2", "");
        }

        super.setParametres("P_AVSDATE1A2", "");
        super.setParametres("P_AVSDATE2A2", "");
        super.setParametres("P_AVSDATE3A2", "");
        super.setParametres("P_AVSDATE4A2", "");
        super.setParametres("P_PERSOMX1A2", "");
        super.setParametres("P_PERSOMX2A2", "");
        super.setParametres("P_PERSOMDATE1A2", "");
        if (pmChange) {
            AFAffiliation pm = AFAffiliationUtil.hasPersonnelMaison(affiliation, dateAvis);
            if (pm != null) {
                super.setParametres("P_PERSOMX1A2", "");
                super.setParametres("P_PERSOMX2A2", " X");
                if (JadeStringUtil.isEmpty(pm.getDateFin())) {
                    super.setParametres("P_PERSOMDATE1A2", pm.getDateDebut());
                } else {
                    super.setParametres("P_PERSOMDATE1A2", pm.getDateDebut() + "-" + pm.getDateFin());
                }
            }
        }
        super.setParametres("P_PERSOMDATE2A2", "");
        super.setParametres("P_CAF1A2", "");
        super.setParametres("P_CAF2A2", "");
        super.setParametres("P_CAF3A2", "");
        super.setParametres("P_CAF4A2", "");
        super.setParametres("P_CAFDATE1A2", "");
        super.setParametres("P_CAFDATE2A2", "");
        super.setParametres("P_CAFDATE3A2", "");
        super.setParametres("P_CAFDATE4A2", "");

        // partie B2
        if ((maisonMere != null) && siegeChange) {
            // succursale -> affichage des données de la maison mère
            // recherche de l'adresse de l'affilié
            TIAvoirAdresse adresseMaisonMere = this.findAdresse(maisonMere, true, false);
            // rechercher la localite pour l'adresse
            TILocalite localiteMaisonMere = getLocalite(adresseMaisonMere);
            // Recherche du tiers
            DonneesTiers tiersMaisonMere = new DonneesTiers(maisonMere, DonneesTiers.TIERS_ACTUEL);
            super.setParametres("P_NOM15B2", tiersMaisonMere.nom + " " + tiersMaisonMere.prenom);
            super.setParametres("P_NOM16B2", tiersMaisonMere.designation3);
            if (!JadeStringUtil.isEmpty(adresseMaisonMere.getRue())) {
                super.setParametres("P_NOM17B2", adresseMaisonMere.getRue() + " " + adresseMaisonMere.getNumeroRue());
            } else {
                // afficher la case postale
                super.setParametres(
                        "P_NOM17B2",
                        TIAdresseDataSource.getFullCasePostale(maisonMere.getTiers().getLangue(),
                                adresseMaisonMere.getCasePostale()));
            }
            super.setParametres("P_PLZB2", localiteMaisonMere.getNumPostal());
            super.setParametres("P_LIEUB2", localiteMaisonMere.getLocalite());
        } else {
            super.setParametres("P_NOM15B2", "");
            super.setParametres("P_NOM16B2", "");
            super.setParametres("P_NOM17B2", "");
            super.setParametres("P_PLZB2", "");
            super.setParametres("P_LIEUB2", "");
        }

        super.setParametres("P_DATE19C2_1", "");
        super.setParametres("P_DATE19C2_2", "");
        super.setParametres("P_DATE19C2_3", "");
        super.setParametres("P_COTPERS19C2_1", "");
        super.setParametres("P_COTPERS19C2_2", "");
        super.setParametres("P_COTPERS19C2_3", "");
        super.setParametres("P_X19C2_1", "");
        super.setParametres("P_X19C2_2", "");
        super.setParametres("P_X19C2_3", "");
        super.setParametres("P_Y19C2_1", "");
        super.setParametres("P_Y19C2_2", "");
        super.setParametres("P_Y19C2_3", "");
    }

    /**
     * Génère la partie gauche du document
     * 
     * @param adresse
     *            l'adresse à afficher
     * @throws Exception
     *             si une erreur survient
     */
    private void generePartieGauche(TIAvoirAdresse adresse, TILocalite localite, DonneesTiers tiersToUse)
            throws Exception {

        // no affilié et IDE
        String numeroAffilie = AFIDEUtil.createNumAffAndNumeroIdeForPrint(affiliation.getAffilieNumero(),
                affiliation.getNumeroIDE(), affiliation.getIdeStatut());
        super.setParametres("P_NOAFFILIE", numeroAffilie);
        // personnalité juridique
        super.setParametres("P_JURIDIQUE", getSession().getCode(affiliation.getPersonnaliteJuridique()));

        // titre
        if (JadeStringUtil.isIntegerEmpty(tiersToUse.titre)) {
            super.setParametres("P_TITRE", "0");
        } else if (ITITiers.CS_MADAME.equals(tiersToUse.titre)) {
            super.setParametres("P_TITRE", "2");
        } else if ("19190005".equals(tiersToUse.titre)) {
            super.setParametres("P_TITRE", "4");
        } else {
            super.setParametres("P_TITRE", "1");
        }

        // tiers
        super.setParametres("P_AVSNO", tiersToUse.noAVS);
        if ((adresse == null) || JadeStringUtil.isEmpty(adresse.getLigneAdresse1())
                || (AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && !adresseChange)) {
            super.setParametres("P_NOM6A1", tiersToUse.nom + " " + tiersToUse.prenom);
        } else {
            super.setParametres("P_NOM6A1", adresse.getLigneAdresse1());
        }
        if ((adresse == null) || JadeStringUtil.isEmpty(adresse.getLigneAdresse2())) {
            super.setParametres("P_NOM7A1", tiersToUse.designation3);
        } else {
            super.setParametres("P_NOM7A1", adresse.getLigneAdresse2());
        }
        if ((adresse == null) || JadeStringUtil.isEmpty(adresse.getLigneAdresse3())) {
            super.setParametres("P_NOM8A1", tiersToUse.designation4);
        } else {
            super.setParametres("P_NOM8A1", adresse.getLigneAdresse3());
        }

        // adresse
        if (adresse != null) {
            if (!JadeStringUtil.isEmpty(adresse.getRue())) {
                super.setParametres("P_RUENO", adresse.getRue() + " " + adresse.getNumeroRue());
            } else {
                // afficher la case postale
                super.setParametres(
                        "P_RUENO",
                        TIAdresseDataSource.getFullCasePostale(affiliation.getTiers().getLangue(),
                                adresse.getCasePostale()));
            }
            super.setParametres("P_PLZ", localite.getLocaliteCourt());
            super.setParametres("P_LIEU", localite.getNumPostal() + " " + localite.getLocalite());
        } else {
            super.setParametres("P_RUENO", "");
            super.setParametres("P_PLZ", "");
            super.setParametres("P_LIEU", "");
        }
        // date AVS, recherche de la coti AVS paritaire (employeur, RI)
        String dateAVS = AFAffiliationUtil.getDateDebutCotisationsAVS(affiliation, dateAvis,
                CodeSystem.GENRE_ASS_PARITAIRE);
        String dateAVSpers = AFAffiliationUtil.getDateDebutCotisationsAVS(affiliation, dateAvis,
                CodeSystem.GENRE_ASS_PERSONNEL);
        if ((dateAVS == null)
                || ((dateAVSpers != null) && BSessionUtil.compareDateFirstLower(getSession(), dateAVSpers, dateAVS))) {
            // si pas d'AVS par, utiliser AVS pers ou si AVS pers est plus
            // ancienne, utilisée cette dernière
            dateAVS = dateAVSpers;
        }
        String caisseAVS = "";
        if (dateAVS == null) {
            dateAVS = AFAvisMutation_Doc.DONNEE_INCONNUE;
            // si taxé sous, prendre la date de début et de fin de l'affiliation
            if (AFAffiliationUtil.isTaxeSous(affiliation, dateAvis)) {
                dateAVS = affiliation.getDateDebut();
            } else {
                caisseAVS = AFAvisMutation_Doc.DONNEE_INCONNUE;
                // pas d'AVS, rechercher caisse externe
                AFSuiviCaisseAffiliation caisse = AFAffiliationUtil.getCaissseAVS(affiliation, dateAvis);
                if (caisse != null) {
                    // la caisse existe
                    if (!JadeStringUtil.isIntegerEmpty(caisse.getDateDebut())) {
                        dateAVS = caisse.getDateDebut();
                    }
                    if (caisse.getAdministration() != null) {
                        caisseAVS = caisse.getAdministration().getCodeInstitution();
                    }
                }
            }
        }

        if (AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && cantonChange && (adresse != null)) {
            if (pageNo == 1) {
                // setParametres("P_AVSDATE1", affiliation.getDateFin());
                this.setParametres("P_AVSDATE1", dateAVS + "-" + adresse.getDateFinRelation());
            } else {
                // setParametres("P_AVSDATE1", dateAVS);
                this.setParametres("P_AVSDATE1", adresse.getDateDebutRelation());
            }
        } else if (AFAvisMutation_Doc.IMPR_SORTIE.equals(selectionImpression) && (adresse != null)) {
            this.setParametres("P_AVSDATE1", dateAVS + "-" + affiliation.getDateFin());
        } else {
            this.setParametres("P_AVSDATE1", dateAVS);
        }

        super.setParametres("P_AVSDATE2", caisseAVS);
        super.setParametres("P_AVSDATE3", "");
        super.setParametres("P_AVSDATE4", "");
        AFAffiliation pm = AFAffiliationUtil.hasPersonnelMaison(affiliation, dateAvis);
        if ((pm == null) || pmChange) {
            // pas de personnel de maison ou nouveau -> indiquer dans partie
            // droite
            super.setParametres("P_PERSOMX1", " X");
            super.setParametres("P_PERSOMX2", "");
            super.setParametres("P_PERSOMDATE1", "");
        } else {
            super.setParametres("P_PERSOMX1", "");
            super.setParametres("P_PERSOMX2", " X");
            if (JadeStringUtil.isEmpty(pm.getDateFin())) {
                super.setParametres("P_PERSOMDATE1", pm.getDateDebut());
            } else {
                super.setParametres("P_PERSOMDATE1", pm.getDateDebut() + "-" + pm.getDateFin());
            }
        }
        super.setParametres("P_PERSOMDATE2", "");

        String caisseAFFromAdhesion = "";

        try {
            AFAdhesionManager mgrAdhesion = new AFAdhesionManager();
            mgrAdhesion.setSession(getSession());
            mgrAdhesion.setForAffiliationId(affiliation.getAffiliationId());
            mgrAdhesion.find(BManager.SIZE_NOLIMIT);
            AFAdhesion adhesion = null;
            TIAdministrationViewBean vbAdministrationTier = null;

            for (int i = 1; i <= mgrAdhesion.size(); i++) {
                adhesion = (AFAdhesion) mgrAdhesion.getEntity(i - 1);
                if (adhesion != null) {
                    vbAdministrationTier = adhesion.getAdministrationCaisse();
                    if (vbAdministrationTier != null) {
                        if (AFProcessAvisSedexWriterNew.GENRE_CAISSE_AF.equalsIgnoreCase(vbAdministrationTier
                                .getGenreAdministration())) {
                            caisseAFFromAdhesion = vbAdministrationTier.getCodeInstitution();
                            break;
                        } else if (AFProcessAvisSedexWriterNew.GENRE_CAISSE_PROF.equalsIgnoreCase(vbAdministrationTier
                                .getGenreAdministration()) && hasPlanCaisseCotiAF(adhesion.getPlanCaisseId())) {
                            caisseAFFromAdhesion = vbAdministrationTier.getCodeInstitution();
                        }

                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        // date AF
        String dateAF = AFAffiliationUtil.getDateDebutCotisationsAF(affiliation, dateAvis,
                CodeSystem.GENRE_ASS_PARITAIRE);
        String dateAFpers = AFAffiliationUtil.getDateDebutCotisationsAF(affiliation, dateAvis,
                CodeSystem.GENRE_ASS_PERSONNEL);
        if ((dateAF == null)
                || ((dateAFpers != null) && BSessionUtil.compareDateFirstLower(getSession(), dateAFpers, dateAF))) {
            // si pas d'AF par, utiliser AF pers ou si AF pers est plus
            // ancienne, utilisée cette dernière
            dateAF = dateAFpers;
        }
        String caisseAF = "";
        if (dateAF == null) {
            // pas d'AF, rechercher caisse externe
            AFSuiviCaisseAffiliation caisse = AFAffiliationUtil.getCaissseAF(affiliation, dateAvis);
            if (caisse != null) {
                // la caisse existe
                dateAF = AFAvisMutation_Doc.DONNEE_INCONNUE;
                caisseAF = AFAvisMutation_Doc.DONNEE_INCONNUE;
                if (!JadeStringUtil.isIntegerEmpty(caisse.getDateDebut())) {
                    dateAF = caisse.getDateDebut();
                }
                if (caisse.getAdministration() != null) {
                    caisseAF = caisse.getAdministration().getCodeInstitution();
                }
            } else {
                dateAF = "";
            }
        }

        if (JadeStringUtil.isBlankOrZero(caisseAF) || AFAvisMutation_Doc.DONNEE_INCONNUE.equalsIgnoreCase(caisseAF)) {
            if (!JadeStringUtil.isBlankOrZero(dateAF) && !AFAvisMutation_Doc.DONNEE_INCONNUE.equalsIgnoreCase(dateAF)) {
                caisseAF = caisseAFFromAdhesion;
            }
        }

        if (JadeStringUtil.isEmpty(dateAF)) {
            super.setParametres("P_CAF1", " X");
            super.setParametres("P_CAF2", "");
        } else {
            super.setParametres("P_CAF1", "");
            super.setParametres("P_CAF2", " X");
        }
        super.setParametres("P_CAF3", "");
        super.setParametres("P_CAF4", "");

        if (AFAvisMutation_Doc.IMPR_CHANGEMENT.equals(selectionImpression) && cantonChange && (adresse != null)
                && !JadeStringUtil.isEmpty(dateAF)) {
            if (pageNo == 1) {
                this.setParametres("P_CAFDATE1", dateAF + "-" + adresse.getDateFinRelation());
            } else {
                this.setParametres("P_CAFDATE1", adresse.getDateDebutRelation());
            }
        } else if (AFAvisMutation_Doc.IMPR_SORTIE.equals(selectionImpression) && !JadeStringUtil.isEmpty(dateAF)) {
            this.setParametres("P_CAFDATE1", dateAF + "-" + affiliation.getDateFin());
        } else {
            this.setParametres("P_CAFDATE1", dateAF);
        }
        super.setParametres("P_CAFDATE2", caisseAF);
        super.setParametres("P_CAFDATE3", "");
        super.setParametres("P_CAFDATE4", "");
        // partie B1
        if ((maisonMere != null) && !siegeChange) {
            // succursale -> affichage des données de la maison mère
            // recherche de l'adresse de l'affilié
            TIAvoirAdresse adresseMaisonMere = this.findAdresse(maisonMere, true, false);
            // rechercher la localite pour l'adresse
            TILocalite localiteMaisonMere = getLocalite(adresseMaisonMere);
            // Recherche du tiers
            DonneesTiers tiersMaisonMere = new DonneesTiers(maisonMere, DonneesTiers.TIERS_ACTUEL);
            super.setParametres("P_NOM15B1", tiersMaisonMere.nom + " " + tiersMaisonMere.prenom);
            super.setParametres("P_NOM16B1", tiersMaisonMere.designation3);
            if (adresseMaisonMere != null) {
                if (!JadeStringUtil.isEmpty(adresseMaisonMere.getRue())) {
                    super.setParametres("P_NOM17B1",
                            adresseMaisonMere.getRue() + " " + adresseMaisonMere.getNumeroRue());
                } else {
                    // afficher la case postale
                    super.setParametres(
                            "P_NOM17B1",
                            TIAdresseDataSource.getFullCasePostale(maisonMere.getTiers().getLangue(),
                                    adresseMaisonMere.getCasePostale()));
                }
                super.setParametres("P_PLZB1", localiteMaisonMere.getNumPostal());
                super.setParametres("P_LIEUB1", localiteMaisonMere.getLocalite());
            }
        } else {
            super.setParametres("P_NOM15B1", "");
            super.setParametres("P_NOM16B1", "");
            super.setParametres("P_NOM17B1", "");
            super.setParametres("P_PLZB1", "");
            super.setParametres("P_LIEUB1", "");
        }
        super.setParametres("P_DATE19C1_1", "");
        super.setParametres("P_DATE19C1_2", "");
        super.setParametres("P_DATE19C1_3", "");
        super.setParametres("P_COTPERS19C1_1", "");
        super.setParametres("P_COTPERS19C1_2", "");
        super.setParametres("P_COTPERS19C1_3", "");
        super.setParametres("P_X19C1_1", "");
        super.setParametres("P_X19C1_2", "");
        super.setParametres("P_X19C1_3", "");
        super.setParametres("P_Y19C1_1", "");
        super.setParametres("P_Y19C1_2", "");
        super.setParametres("P_Y19C1_3", "");
    }

    public boolean getAdresseChange() {
        return adresseChange;
    }

    public boolean getAvsChange() {
        return avsChange;
    }

    public boolean getCantonChange() {
        return cantonChange;
    }

    /**
     * @return
     */
    public String getDateAvis() {
        return dateAvis;
    }

    /**
     * Retourn la date d'impression Date de création : (03.05.2003 10:40:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateImpression() {
        return dateImpression;
    }

    /**
     * Retourne la sélection pour l'impression
     * 
     * @return java.lang.String
     */
    public boolean getDoAfterBuildReport() {
        return morePageNeeded;
    }

    public AFExportFichierCentralXmlParser getExporterXML() throws Exception {
        if (exporterXML == null) {
            String noCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
            exporterXML = new AFExportFichierCentralXmlParser(noCaisse.trim());
            singleXML = true;
        }
        return exporterXML;
    }

    /**
     * @return
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    protected AFAvisMutation_Doc getInstance(BSession session, String selectionImpression) throws Exception {
        return new AFAvisMutation_Doc(session, selectionImpression);
    }

    /**
     * @return
     */
    public ArrayList<AFAffiliation> getIteratorAvis() {
        return itAvis;
    }

    private TILocalite getLocalite(TIAvoirAdresse adresse) throws Exception {
        TILocalite localite = new TILocalite();

        localite.setSession(getSession());

        if (adresse != null) {
            localite.setIdLocalite(adresse.getIdLocalite());
            localite.retrieve();
        }

        return localite;
    }

    public boolean getMorePageNeeded() {
        return morePageNeeded;
    }

    /**
     * Retourne la sélection pour l'impression
     * 
     * @return boolean
     */
    public boolean getNomChange() {
        return nomChange;
    }

    /**
     * Retourne la sélection pour l'impression
     * 
     * @return java.lang.String
     */
    public java.lang.String getObservation() {
        return observation;
    }

    /**
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */

    public boolean getPmChange() {
        return pmChange;
    }

    /**
     * Retourne la sélection pour l'impression
     * 
     * @return java.lang.String
     */
    public java.lang.String getSelectionImpression() {
        return selectionImpression;
    }

    public boolean getSiegeChange() {
        return siegeChange;
    }

    /**
     * Retourne la sélection pour l'impression
     * 
     * @return java.lang.String
     */
    protected TITiersViewBean getTiers() {
        return tiers;
    }

    /**
     * Retourne la sélection pour l'impression
     * 
     * @return java.lang.String
     */
    public String getTiersId() {
        return tiersId;
    }

    private boolean hasPlanCaisseCotiAF(String idPlanCaisse) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idPlanCaisse)) {
            throw new Exception("can't check if plan caisse has a coti AF because idPlanCaisse is blank or zero ");
        }

        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setSession(getSession());
        cotisMgr.setForPlanCaisseId(idPlanCaisse);
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);

        return cotisMgr.getCount() >= 1;
    }

    /**
     * @return
     */
    public boolean isAdresseChange() {
        return adresseChange;
    }

    /**
     * @return
     */
    public boolean isAvsChange() {
        return avsChange;
    }

    /**
     * @return
     */
    public boolean isCantonChange() {
        return cantonChange;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public boolean isCantonDomDiff() {
        return cantonDomDiff;
    }

    /**
     * @return
     */
    public boolean isMorePageNeeded() {
        return morePageNeeded;
    }

    /**
     * @return
     */
    public boolean isNomChange() {
        return nomChange;
    }

    /**
     * @return
     */
    public boolean isPmChange() {
        return pmChange;
    }

    /**
     * @return
     */
    public boolean isSiegeChange() {
        return siegeChange;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub -> A seter
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param string
     */
    protected void loadTiers() {
        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getTiersId())) {
            return;
        }

        // Si log pas déjà chargé
        if (tiers == null) {
            // Instancier un nouveau LOG
            tiers = new TITiersViewBean();
            tiers.setSession(getSession());

            // Récupérer le log en question
            tiers.setIdTiers(getTiersId());

            try {
                tiers.retrieve();

                if (tiers.getSession().hasErrors()) {
                    tiers = null;
                }
            } catch (Exception e) {
                this._addError(null, e.getMessage());
                tiers = null;
            }
        }
    }

    /**
     * Retourne si la partie droite du document (A2,B2, etc) doit être remplie
     * 
     * @return true si la partie droite doit être remplie
     */
    private boolean needPartieDroite() {
        return (selectionImpression.equals(AFAvisMutation_Doc.IMPR_CHANGEMENT) && !cantonChange) || cantonDomDiff;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public boolean next() throws FWIException {
        if (itAvis.size() > 0) {
            try {
                AFAffiliation affiliation = itAvis.remove(0);
                setAffiliation(affiliation);

                // String observation = "";
                // setObservation(observation);
            } catch (Exception e) {
                throw new FWIException(e);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * @param b
     */
    public void setAdresseChange(boolean b) {
        adresseChange = b;
    }

    /**
     * Sets the affiliation.
     * 
     * @param affiliation
     *            The affiliation to set
     */
    protected void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * @param b
     */
    public void setAvsChange(boolean b) {
        avsChange = b;
    }

    /**
     * @param b
     */
    public void setCantonChange(boolean b) {
        cantonChange = b;
    }

    /**
     * @param b
     */
    public void setCantonDomDiff(boolean b) {
        cantonDomDiff = b;
    }

    /**
     * @param string
     */
    public void setDateAvis(String string) {
        dateAvis = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2003 10:40:25)
     * 
     * @param newDateImpression
     *            java.lang.String
     */
    public void setDateImpression(java.lang.String newDateImpression) {
        dateImpression = newDateImpression;
    }

    public void setExporterXML(AFExportFichierCentralXmlParser exporter) {
        exporterXML = exporter;
    }

    /**
     * @param string
     */
    public void setIdAffiliation(java.lang.String string) {
        idAffiliation = string;
    }

    /**
     * @param iterator
     */
    public void setIteratorAvis(ArrayList<AFAffiliation> iterator) {
        itAvis = iterator;
    }

    /**
     * @param b
     */
    public void setMorePageNeeded(boolean b) {
        morePageNeeded = b;
    }

    /**
     * @param b
     */
    public void setNomChange(boolean b) {
        nomChange = b;
    }

    /**
     * Sets the observation.
     * 
     * @param observation
     *            The observation to set
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     * @param i
     */
    public void setPageNo(int i) {
        pageNo = i;
    }

    /**
     * @param b
     */
    public void setPmChange(boolean b) {
        pmChange = b;
    }

    /**
     * @param newSelectionimpression
     */
    public void setSelectionImpression(String newSelectionimpression) {
        selectionImpression = newSelectionimpression;
    }

    /**
     * @param b
     */
    public void setSiegeChange(boolean b) {
        siegeChange = b;
    }

    /**
     * @param string
     */
    public void setTiersId(java.lang.String string) {
        tiersId = string;
    }
}
