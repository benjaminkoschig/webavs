/*
 * Créé le 15 juin 05
 */
package globaz.apg.process;

import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.excel.APListePrestationVerseeExcel;
import globaz.apg.itext.APListePrestationVersee_Doc;
import globaz.apg.pojo.PrestationVerseeLignePrestationPojo;
import globaz.apg.pojo.PrestationVerseeLigneRecapitulationPojo;
import globaz.apg.pojo.PrestationVerseePojo;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.naos.process.statOfas.AFStatistiquesOfasProcess;
import globaz.pyxis.util.TIToolBox;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mmo
 * 
 *         Ce processus fournit au format PDF et Excel une liste de toutes les prestations payées par employeur pour une
 *         période donnée
 * 
 *         L'utilisateur peut choisir d'envoyer le fichier PDF généré par ce processus en GED
 * 
 * 
 * 
 */
public class APListePrestationVerseeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String LABEL_CAS = "DOC_LISTE_PRESTATION_VERSEE_CAS";
    public final static String LABEL_DATE_PAIEMENT = "DOC_LISTE_PRESTATION_VERSEE_DATE_PAIEMENT";
    public final static String LABEL_DATE_TRAITEMENT = "DOC_LISTE_PRESTATION_VERSEE_DATE_TRAITEMENT";
    public final static String LABEL_GENRE_SERVICE = "DOC_LISTE_PRESTATION_VERSEE_GENRE_SERVICE";
    public final static String LABEL_MONTANT_BRUT = "DOC_LISTE_PRESTATION_VERSEE_MONTANT_BRUT";
    public final static String LABEL_NOM_PRENOM = "DOC_LISTE_PRESTATION_VERSEE_NOM_PRENOM";

    public final static String LABEL_NOMBRE_CAS = "DOC_LISTE_PRESTATION_VERSEE_NOMBRE_CAS";
    public final static String LABEL_NSS = "DOC_LISTE_PRESTATION_VERSEE_NSS";
    public final static String LABEL_NUMERO_AFFILIE = "DOC_LISTE_PRESTATION_VERSEE_NUMERO_AFFILIE";

    public final static String LABEL_PERIODE = "DOC_LISTE_PRESTATION_VERSEE_PERIODE";
    public final static String LABEL_PERIODE_DETAIL = "DOC_LISTE_PRESTATION_VERSEE_PERIODE_DETAIL";
    public final static String LABEL_RECAPITULATION = "DOC_LISTE_PRESTATION_VERSEE_RECAPITULATION";
    public final static String LABEL_SELECTEUR_PRESTATION = "DOC_LISTE_PRESTATION_VERSEE_SELECTEUR_PRESTATION";
    public final static String LABEL_TITRE = "DOC_LISTE_PRESTATION_VERSEE_TITRE";
    public final static String LABEL_TOTAUX = "DOC_LISTE_PRESTATION_VERSEE_TOTAUX";

    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_DEBUT = "DATE_DEBUT";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_FIN = "DATE_FIN";

    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_PAIEMENT = "DATE_PAIEMENT";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION = "GENRE_PRESTATION";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION_LIBELLE = "GENRE_PRESTATION_LIBELLE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE = "GENRE_SERVICE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE_LIBELLE = "GENRE_SERVICE_LIBELLE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_ID_TIERS = "ID_TIERS";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_MONTANT_BRUT = "MONTANT_BRUT";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM = "NOM";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NSS = "NSS";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NUMERO_AFFILIE = "NUMERO_AFFILIE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM_AFFILIE = "NOM_AFFILIE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_PRENOM = "PRENOM";
    public static final String SELECTEUR_PRESTATION_PAR_PAIEMENT = "52025001";
    public static final String SELECTEUR_PRESTATION_PAR_PERIODE = "52025002";

    private String dateDebut;
    private String dateFin;
    private Boolean envoyerGed;

    private List<PrestationVerseePojo> listePrestationVerseePojo;
    private String numeroAffilie;
    private String schemaDBWithTablePrefix;
    private String selecteurPrestation;

    public APListePrestationVerseeProcess() {
        super();
        schemaDBWithTablePrefix = TIToolBox.getCollection();
        dateDebut = "";
        dateFin = "";
        selecteurPrestation = "";
        numeroAffilie = "";
        listePrestationVerseePojo = new ArrayList<PrestationVerseePojo>();
        envoyerGed = new Boolean(false);

    }

    public APListePrestationVerseeProcess(BSession session) {
        super(session);
        schemaDBWithTablePrefix = TIToolBox.getCollection();
        dateDebut = "";
        dateFin = "";
        selecteurPrestation = "";
        numeroAffilie = "";
        listePrestationVerseePojo = new ArrayList<PrestationVerseePojo>();
        envoyerGed = new Boolean(false);

    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean success = true;
        String errorMessage = "";

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // voir javadoc de la méthode
            // Création de la ListePrestationVerseePojo
            creerListePrestationVerseePojo();

            if (!listePrestationVerseePojo.isEmpty()) {
                // création des documents
                List<JadePublishDocument> listeDocumentAPublierPDF = creerListePrestationVerseeDocumentsPDF();

                JadePublishDocument documentAPublierExcel = creerListePrestationVerseeDocumentsExcel();

                // publication des documents
                publierListePrestationVerseeDocumentsPDF(listeDocumentAPublierPDF);
                publierListePrestationVerseeDocumentsExcel(documentAPublierExcel);
            } else {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_MAIL_BODY_AUCUNE_DONNEE_A_IMPRIMER"),
                        FWMessage.INFORMATION, this.getClass().getName());
            }

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (!success || isAborted() || isOnError() || getSession().hasErrors()) {

            success = false;

            getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), errorMessage);
        }

        return success;
    }

    @Override
    protected void _validate() throws Exception {

        boolean isDateDebutValide = true;
        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            isDateDebutValide = false;
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_DEBUT_MANDATORY"));
        }

        boolean isDateFinValide = true;
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            isDateFinValide = false;
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_FIN_MANDATORY"));
        }

        if (isDateDebutValide && isDateFinValide && JadeDateUtil.isDateAfter(dateDebut, dateFin)) {
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_DEBUT_APRES_DATE_FIN"));
        }

        if (isDateDebutValide && JadeDateUtil.isDateBefore(dateDebut, "01.01.1948")) {
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_DEBUT_AVANT_01_01_1948"));
        }

        if (isDateFinValide && JadeDateUtil.isDateAfter(dateFin, JACalendar.todayJJsMMsAAAA())) {
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_FIN_APRES_DATE_DU_JOUR"));
        }

    }

    private JadePublishDocument creerListePrestationVerseeDocumentsExcel() throws Exception {

        APListePrestationVerseeExcel listePrestationVerseeExcel = new APListePrestationVerseeExcel(getSession());
        listePrestationVerseeExcel.setListePrestationVerseePojo(listePrestationVerseePojo);
        listePrestationVerseeExcel.creerDocument();

        JadePublishDocumentInfo docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
        docInfoExcel.setDocumentTypeNumber(APListePrestationVersee_Doc.NUM_INFOROM);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);

        return new JadePublishDocument(listePrestationVerseeExcel.getOutputFile(), docInfoExcel);

    }

    private List<JadePublishDocument> creerListePrestationVerseeDocumentsPDF() throws Exception {

        List<JadePublishDocument> listeDocumentAPublier = new ArrayList<JadePublishDocument>();

        for (PrestationVerseePojo aPrestationVerseePojo : listePrestationVerseePojo) {

            APListePrestationVersee_Doc listePrestationVerseeDoc = new APListePrestationVersee_Doc();
            listePrestationVerseeDoc.setaPrestationVerseePojo(aPrestationVerseePojo);
            listePrestationVerseeDoc.setEnvoyerGed(getEnvoyerGed());
            listePrestationVerseeDoc.executeProcess();
            listeDocumentAPublier.add((JadePublishDocument) listePrestationVerseeDoc.getAttachedDocuments().get(0));

        }

        return listeDocumentAPublier;

    }

    /**
     * 
     * Cette méthode a pour but de créer une liste de PrestationVerseePojo
     * 
     * Un PrestationVerseePojo se rapporte à un affilié et contient pour ce dernier une liste des prestations versées De
     * plus, il contient une map récapitulant le nombre de prestations versées et le montant de ces dernières par genre
     * de service
     * 
     * La liste de PrestationVerseePojo est donc un container de données qui contient toutes les informations
     * nécessaires pour créer la liste des prestations versées demandée dans le mandat InfoRom457 (peu importe le format
     * de sortie souhaité PDF, Excel...)
     */
    private void creerListePrestationVerseePojo() throws Exception {

        String selecteurPrestationLibelle = getSession().getCodeLibelle(selecteurPrestation);

        String sqlQueryListePrestationVersee = getSqlListePrestationVersee();
        List<Map<String, String>> listMapResultQueryListePrestationVersee = executeQuery(sqlQueryListePrestationVersee);

        PrestationVerseePojo aPrestationVerseePojo = null;
        String NumeroAffiliePrecedent = "";

        // Parcours de chaque ligne retournée par la requête sql
        for (Map<String, String> aMapRowResultQueryListePrestationVersee : listMapResultQueryListePrestationVersee) {

            String NumeroAffilie = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NUMERO_AFFILIE);
            String nomAffilie = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM_AFFILIE);
            String IdTiers = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_ID_TIERS);

            // A chaque changement de numéro d'affilié un nouveau PrestationVerseePojo est créé et ajouté à la liste
            // listePrestationVerseePojo
            if (!NumeroAffiliePrecedent.equalsIgnoreCase(NumeroAffilie)) {
                aPrestationVerseePojo = new PrestationVerseePojo();
                aPrestationVerseePojo.setIdTiers(IdTiers);
                aPrestationVerseePojo.setNumeroAffilie(NumeroAffilie);
                aPrestationVerseePojo.setNomAffilie(nomAffilie);
                aPrestationVerseePojo.setSelecteurPrestationLibelle(selecteurPrestationLibelle);
                aPrestationVerseePojo.setDateDebut(getDateDebut());
                aPrestationVerseePojo.setDateFin(getDateFin());
                listePrestationVerseePojo.add(aPrestationVerseePojo);
                NumeroAffiliePrecedent = NumeroAffilie;
            }

            // Récupération des informations de la ligne courante
            String GenreService = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE);
            String GenreServiceLibelle = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE_LIBELLE);
            String GenrePrestation = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION);
            String GenrePrestationLibelle = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION_LIBELLE);
            String NSS = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NSS);
            String Nom = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM);
            String Prenom = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_PRENOM);
            String DateDebut = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_DEBUT);
            String DateFin = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_FIN);
            String MontantBrut = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_MONTANT_BRUT);
            String DatePaiement = aMapRowResultQueryListePrestationVersee
                    .get(APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_PAIEMENT);

            DateDebut = formatDate(DateDebut);
            DateFin = formatDate(DateFin);
            DatePaiement = formatDate(DatePaiement);

            Nom = Nom.trim();
            Prenom = Prenom.trim();

            GenreService = GenreService.trim();
            GenrePrestationLibelle = GenrePrestationLibelle.trim();
            GenrePrestationLibelle = "(" + GenrePrestationLibelle + ")";
            if (APTypeDePrestation.STANDARD.getCodesystemString().equalsIgnoreCase(GenrePrestation)) {
                GenrePrestationLibelle = "";
            }

            double MontantBrutDouble = Double.valueOf(MontantBrut).doubleValue();

            // met à jour les infos NombreCasTotal et MontantBrutTotal du pojo
            // aPrestationVerseePojo
            int nbCasParAffilie = Integer.valueOf(aPrestationVerseePojo.getNombreCasTotal()).intValue();
            nbCasParAffilie += 1;
            double montantBrutParAffilie = Double.valueOf(aPrestationVerseePojo.getMontantBrutTotal()).doubleValue();
            montantBrutParAffilie += MontantBrutDouble;
            aPrestationVerseePojo.setNombreCasTotal(String.valueOf(nbCasParAffilie));
            aPrestationVerseePojo.setMontantBrutTotal(String.valueOf(montantBrutParAffilie));

            // Ajoute la ligne courante à la liste ListeLignePrestationVersee du pojo aPrestationVerseePojo
            PrestationVerseeLignePrestationPojo aPrestationVerseeLignePrestationPojo = new PrestationVerseeLignePrestationPojo();
            aPrestationVerseeLignePrestationPojo.setGenreService(GenreService);
            aPrestationVerseeLignePrestationPojo.setGenrePrestationLibelle(GenrePrestationLibelle);
            aPrestationVerseeLignePrestationPojo.setNss(NSS);
            aPrestationVerseeLignePrestationPojo.setNom(Nom);
            aPrestationVerseeLignePrestationPojo.setPrenom(Prenom);
            aPrestationVerseeLignePrestationPojo.setDateDebut(DateDebut);
            aPrestationVerseeLignePrestationPojo.setDateFin(DateFin);
            aPrestationVerseeLignePrestationPojo.setMontantBrut(MontantBrut);
            aPrestationVerseeLignePrestationPojo.setDatePaiement(DatePaiement);

            aPrestationVerseePojo.getListeLignePrestationVersee().add(aPrestationVerseeLignePrestationPojo);

            // Met à jour la partie récapitulative du pojo aPrestationVerseePojo
            String keyMapLigneRecapitulationPrestationVersee = GenreService + "_" + GenrePrestation;
            PrestationVerseeLigneRecapitulationPojo aPrestationVerseeLigneRecapitulationPojo = null;
            Map<String, PrestationVerseeLigneRecapitulationPojo> mapLigneRecapitulationPrestationVersee = aPrestationVerseePojo
                    .getMapLigneRecapitulationPrestationVersee();
            if (mapLigneRecapitulationPrestationVersee.containsKey(keyMapLigneRecapitulationPrestationVersee)) {
                aPrestationVerseeLigneRecapitulationPojo = mapLigneRecapitulationPrestationVersee
                        .get(keyMapLigneRecapitulationPrestationVersee);
            } else {
                aPrestationVerseeLigneRecapitulationPojo = new PrestationVerseeLigneRecapitulationPojo();
                aPrestationVerseeLigneRecapitulationPojo.setGenreService(GenreService);
                aPrestationVerseeLigneRecapitulationPojo.setLibelleGenreService(GenreServiceLibelle);
                aPrestationVerseeLigneRecapitulationPojo.setGenrePrestationLibelle(GenrePrestationLibelle);

                mapLigneRecapitulationPrestationVersee.put(keyMapLigneRecapitulationPrestationVersee,
                        aPrestationVerseeLigneRecapitulationPojo);
            }

            int nbCasGenreService = Integer.valueOf(aPrestationVerseeLigneRecapitulationPojo.getNombreCas()).intValue();
            nbCasGenreService += 1;
            double montantBrutGenreService = Double.valueOf(aPrestationVerseeLigneRecapitulationPojo.getMontantBrut())
                    .doubleValue();
            montantBrutGenreService += MontantBrutDouble;
            aPrestationVerseeLigneRecapitulationPojo.setNombreCas(String.valueOf(nbCasGenreService));
            aPrestationVerseeLigneRecapitulationPojo.setMontantBrut(String.valueOf(montantBrutGenreService));

        }
    }

    private List<Map<String, String>> executeQuery(String sql) throws JadePersistenceException {

        Statement stmt = null;
        ResultSet resultSet = null;
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<String, String>();

                // Attention ! La première colonne du Resultset est 1 et non 0
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i), resultSet.getString(i));
                }

                results.add(row);
            }

        } catch (SQLException e) {
            throw new JadePersistenceException(getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(AFStatistiquesOfasProcess.class,
                            "Problem to close statement in AFStatistiquesOfasProcess, reason : " + e.toString());
                }
            }

        }

        return results;
    }

    /**
     * 
     * Transforme une date au format aaaammjj en une date au format jj.mm.yyyy
     */
    private String formatDate(String dateAMJ) {

        String dateJMA = dateAMJ.substring(6, 8) + "." + dateAMJ.substring(4, 6) + "." + dateAMJ.substring(0, 4);

        return dateJMA;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    protected String getEMailObject() {

        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_MAIL_OBJECT_ERROR");
        } else {
            return getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_MAIL_OBJECT_SUCCESS");
        }

    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    public List<PrestationVerseePojo> getListePrestationVerseePojo() {
        return listePrestationVerseePojo;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getSelecteurPrestation() {
        return selecteurPrestation;
    }

    private String getSqlListePrestationVersee() throws Exception {

        String dateDebutAMJ = (new JADate(dateDebut)).toAMJ().toString();
        String dateFinAMJ = (new JADate(dateFin)).toAMJ().toString();
        String langueUser = getSession().getIdLangue();

        String sqlWhere = " where pres.VHTETA = 52006005 ";

        if (APListePrestationVerseeProcess.SELECTEUR_PRESTATION_PAR_PAIEMENT.equalsIgnoreCase(selecteurPrestation)) {
            sqlWhere += " AND pres.VHDPMT >= " + dateDebutAMJ + " AND  pres.VHDPMT <= " + dateFinAMJ + " ";
        } else if (APListePrestationVerseeProcess.SELECTEUR_PRESTATION_PAR_PERIODE
                .equalsIgnoreCase(selecteurPrestation)) {
            sqlWhere += " AND pres.VHDDEB <= " + dateFinAMJ + " AND  pres.VHDFIN >= " + dateDebutAMJ + " ";
        } else {
            throw new Exception("Not implemented");
        }

        if (!JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            sqlWhere += "  AND affi.MALNAF = '" + numeroAffilie + "' ";
        }

        String sql = " select affi.MALNAF as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NUMERO_AFFILIE
                + " , "
                + " affi.MADESL as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM_AFFILIE
                + " , "
                + " affi.HTITIE as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_ID_TIERS
                + " , "
                + " lib_genre_service.PCOUID as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE
                + " , "
                + " lib_genre_service.PCOLUT as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE_LIBELLE
                + " , "
                + " pres.VHTGEN as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION
                + " , "
                + " lib_genre_prestation.PCOLUT as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION_LIBELLE
                + " , "
                + " pavs.HXNAVS as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NSS
                + " , "
                + " tier.HTLDE1 as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM
                + " , "
                + " tier.HTLDE2 as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_PRENOM
                + " , "
                + " pres.VHDDEB as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_DEBUT
                + " , "
                + " pres.VHDFIN as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_FIN
                + " , "
                + " pres.VHDMOB as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_MONTANT_BRUT
                + " , "
                + " pres.VHDPMT as "
                + APListePrestationVerseeProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_PAIEMENT
                + " "
                + " from schema.APPRESP pres inner join schema.FWCOUP lib_genre_prestation on(lib_genre_prestation.PCOSID = pres.VHTGEN and lib_genre_prestation.PLAIDE='"
                + langueUser
                + "') "
                + " inner join schema.APREPAP repa on (repa.VIIPRA = pres.VHIPRS and repa.VIIAFF <> 0) "
                + " inner join schema.AFAFFIP affi on (affi.MAIAFF = repa.VIIAFF) "
                + " inner join schema.APDROIP droi on (droi.VAIDRO = pres.VHIDRO) "
                + " inner join schema.FWCOUP lib_genre_service on(lib_genre_service.PCOSID = droi.VATGSE and lib_genre_service.PLAIDE='"
                + langueUser + "') " + " inner join schema.PRDEMAP dema on (dema.WAIDEM = droi.VAIDEM) "
                + " inner join schema.TITIERP tier on (tier.HTITIE = dema.WAITIE) "
                + " inner join schema.TIPAVSP pavs on (pavs.HTITIE = tier.HTITIE) " + sqlWhere
                + " order by repa.VIIAFF, tier.HTLDE1, tier.HTLDE2, pavs.HXNAVS, pres.VHDDEB, pres.VHIPRS ";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {

        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void publierListePrestationVerseeDocumentsExcel(JadePublishDocument documentAPublier) throws Exception {

        this.registerAttachedDocument(documentAPublier);

    }

    private void publierListePrestationVerseeDocumentsPDF(List<JadePublishDocument> listeDocumentAPublier)
            throws Exception {

        registerDocuments(listeDocumentAPublier);

        JadePublishDocumentInfo docInfoMergedDocument = createDocumentInfo();
        docInfoMergedDocument.setDocumentTypeNumber(APListePrestationVersee_Doc.NUM_INFOROM);

        docInfoMergedDocument.setArchiveDocument(false);
        docInfoMergedDocument.setPublishDocument(true);

        boolean replaceDocUnitaire = !getEnvoyerGed();
        this.mergePDF(docInfoMergedDocument, replaceDocUnitaire, 500, false, "numero.affilie.formatte");
    }

    private String replaceSchemaInSqlQuery(String sqlQuery) {

        sqlQuery = sqlQuery.replaceAll("(?i)schema\\.", schemaDBWithTablePrefix);

        return sqlQuery;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

    public void setListePrestationVerseePojo(List<PrestationVerseePojo> listePrestationVerseePojo) {
        this.listePrestationVerseePojo = listePrestationVerseePojo;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setSelecteurPrestation(String selecteurPrestation) {
        this.selecteurPrestation = selecteurPrestation;
    }

}
