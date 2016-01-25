package globaz.osiris.db.ordres.format.opt;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACCP;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.print.itext.list.CAIListOrdreGroupe;
import globaz.osiris.print.itext.list.CAIListOrdreGroupeParam;
import globaz.osiris.print.itext.list.CAIListSoldeSectionParam;
import globaz.osiris.utils.CAOsirisContainer;
import globaz.pyxis.util.TIToolBox;
import globaz.webavs.common.CommonExcelmlUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * Insérez la description du type ici. Date de création : (03.06.2003 15:46:19)
 * 
 * @author: Administrator
 */
public class CAIListOrdreGroupeLite extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String getSQLBody(String idOrdreGroupe, String langue) {
        String col = TIToolBox.getCollection();

        return "from " + col + "CAOPOVP ov " + "inner join " + col + "CAOPERP op on (op.idoperation = ov.idordre)"
                + "inner join " + col + "CASECTP s on (s.idsection= op.idsection)" + "inner join " + col
                + "TIAPAIP ap on (ap.HCIAIU = ov.IDADRESSEPAIEMENT)" + "inner join " + col
                + "CACPTAP ca on (op.idcompteannexe = ca.idcompteannexe )" + "inner join " + col
                + "CARROLP ro on (ca.idrole = ro.idrole )" + "inner join " + col
                + "PMTRADP tr on (ro.idtraduction = tr.idtraduction and CODEISOLANGUE = '" + langue + "' )"
                + "inner join " + col

                + "TIADRPP pm on (pm.HIIAPA = ap.HIIAPA)" + "inner join " + col
                + "TIADREP ab on (ab.HAIADR = pm.HAIADR)" + "inner join " + col
                + "TILOCAP lb on (lb.HJILOC = ab.HJILOC)" + "inner join " + col
                + "TIPAYSP pb on (pb.HNIPAY = lb.HNIPAY)" + "left outer join " + col
                + "TITIERP tb on (tb.htitie = pm.HITIAD)" + "left outer join " + col
                + "TIBANQP bq on (bq.htitie = pm.HTITIE)" + "left outer join " + col
                + "TITIERP tbq on (bq.htitie = tbq.HTITIE)" + "WHERE ov.IDORDREGROUPE= " + idOrdreGroupe
                + " AND ov.ESTRETIRE<>'1' AND ov.ESTBLOQUE<>'1'";
    }

    public static String getSQLFields() {
        return "pm.HINCBA adrPmtCompte,"
                + "pm.HICCP adrPmtCCP,"
                + "bq.HTITIE bqIdTiers,"
                + "bq.HUCLEA bqClearing,"
                + "bq.HUNMJ bqNumMiseAjour,"
                + "tb.HTLDE1 beneDesi1,"
                + "tb.HTLDE2 beneDesi2,"
                + "tb.HTLDE3 beneDesi3,"
                + "tb.HTTLAN beneLangue,"
                + "ab.HAADR1 adrLigne1,"
                + "ab.HAADR2 adrLigne2,"
                + "ab.HARUE adrRue,"
                + "ab.HANRUE adrNumRue,"
                + "ab.HACPOS adrCP,"
                + "lb.HJNPA locHJNPA,"
                + "lb.HJLOCA locHJLOCA,"
                + "pb.HNCISO paysISO,"
                + "op.MONTANT opMONTANT,"
                + "ov.CODEISOMONDEP ovCODEISOMONDEP,"
                + "ov.CODEISOMONBON ovCODEISOMONBON,"
                + "ov.NUMTRANSACTION ovNUMTRANSACTION,"
                + "ov.MOTIF ovMOTIF,"
                + "tr.LIBELLE ROLECA,"
                + "ca.IDEXTERNEROLE IDEXTERNEROLE,"
                + "ca.DESCRIPTION DESCRIPTION,"
                + "bq.HUCSWI SWIFT, tbq.HTLDE1 BQHTLDE1, tbq.HNIPAY BQIDPAYS ,tbq.HTLDE2 BQHTLDE2, s.CATEGORIESECTION CATEGORIESECTION,"
                + "s.IDTYPESECTION IDTYPESECTION, s.DATEDEBUTPERIODE DATEDEBUTPERIODE, s.DATEFINPERIODE DATEFINPERIODE, s.IDEXTERNE IDEXTERNESECTION";
    }

    private boolean hasNext = true;

    private String idOrdreGroupe = new String();

    private String idTypeOperation = new String();

    FWIImporterInterface importDoc = null;
    private List<Map<String, Object>> li = null;

    private CAOrdreGroupe ordreGroupe;

    private Map<Integer, Object[]> recap = new TreeMap<Integer, Object[]>();

    private boolean started = false;

    private String typeImpression = "pdf";

    private CAOsirisContainer xlsContainer = new CAOsirisContainer();

    /**
     * @param parent
     * @throws FWIException
     */
    public CAIListOrdreGroupeLite(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "ListOrdreGroupe");
        super.setDocumentTitle(getSession().getLabel("TITLE_LIST_ORDRE_GROUPE"));
    }

    /**
     * Constructor for CAIListOrdreGroupe.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListOrdreGroupeLite(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    private String _formatDate(String date) throws Exception {
        BigDecimal decimal = new BigDecimal(Long.parseLong(date));
        return JACalendar.format(new JADate(decimal), JACalendar.FORMAT_DDsMMsYYYY);
    }

    private String _getAdresseVersement(CAOperationAdrPmt data) {
        String compte = "";
        String ccp = data.getCcp();
        String idTiersBanque = data.getIdTiersBanque();
        String clearing = data.getClearing();
        String codeSwift = data.getCodeSwift();
        String nomBanque = data.getNomBanque1() + " " + data.getNomBanque2();
        String adresseBenef = data.getRue() + " " + data.getNumero() + " " + data.getNpa() + " " + data.getLocalite();

        if (!JadeStringUtil.isEmpty(ccp)) {
            // Compte CCP
            try {
                compte = JACCP.formatWithDash(ccp);
            } catch (Exception e) {
                compte = ccp;
            }
        } else if (!JadeStringUtil.isIntegerEmpty(idTiersBanque)) {
            // Compte bancaire
            if (compte.length() > 0) {
                compte += ", ";
            }
            compte += data.getCompte() + ", ";
            // Clearing ou code swift
            if (!JadeStringUtil.isBlank(clearing)) {
                compte += clearing + " ";
            } else {
                compte += codeSwift + " ";
            }

            // Nom de la banque
            compte += nomBanque;
        }
        // soit on a un compte, soit c'est un mandat
        return (!JadeStringUtil.isEmpty(compte)) ? compte : adresseBenef;
    }

    private String _getBeneficiaire(CAOperationAdrPmt data) {
        String desi = "";
        if (!JadeStringUtil.isEmpty(data.getDesignation1_adr())) {
            // beneficiaire présent dans l'adresse
            desi = data.getDesignation1_adr();
        } else {
            // sinon , on prend le nom du tiers
            desi = data.getDesignation1_tiers() + " " + data.getDesignation2_tiers();
        }
        return desi + " " + data.getNpa() + " " + data.getLocalite();
    }

    private String _getDescriptionCompteAnnexe(CAOperationAdrPmt data) {
        String role = data.getRoleCompteAnnexe();
        return role + " " + data.getIdExterneRole() + " " + data.getDescriptionCompteAnnexe();

    }

    private String _getNature(CAOperationAdrPmt data) throws Exception {

        return getDescriptionSection(getSession(), data.getCategorieSection(), data.getIdExterneSection(),
                _formatDate(data.getDateDebutSection()), _formatDate(data.getDateFinSection()), data.getIdTypeSection());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2002 11:49:57)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    public CAOrdreGroupe _getOrdreGroupe() {
        // Si pas déjà chargé
        if (ordreGroupe == null) {
            try {
                ordreGroupe = new CAOrdreGroupe();
                ordreGroupe.setSession(getSession());
                ordreGroupe.setIdOrdreGroupe(getIdOrdreGroupe());
                ordreGroupe.retrieve(getTransaction());
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage("5147", getIdOrdreGroupe(), FWMessage.FATAL, this.getClass().getName());
                    ordreGroupe = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                ordreGroupe = null;
            }
        }

        return ordreGroupe;

    }

    // Création du paramètre de référence pour les documents de type liste
    private String _getRefParam() {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
            StringBuffer refBuffer = new StringBuffer(getSession().getLabel("REFERENCE") + " ");
            refBuffer.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1));
            refBuffer.append(" (");
            refBuffer.append(formater.format(new Date()));
            refBuffer.append(")");
            refBuffer.append(" - ");
            refBuffer.append(getSession().getUserId());
            return refBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _header() {
        try {
            this.setParametres(
                    FWIImportParametre.PARAM_COMPANYNAME,
                    FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                            ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            // Exercice (date)
            super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
            // Titre du document
            if (_getOrdreGroupe().getTypeOrdreGroupe().equals(CAOrdreGroupe.VERSEMENT)) {
                super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6000"));
            } else {
                super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6001"));
            }
            // Numéro de l'ordre groupé
            super.setParametres(CAIListOrdreGroupeParam.LABEL_NUMERO_ORDRE, getSession().getLabel("NUMERO_ORDRE"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_NUMERO_ORDRE, _getOrdreGroupe().getIdOrdreGroupe());

            // Motif
            super.setParametres(CAIListOrdreGroupeParam.LABEL_MOTIF, getSession().getLabel("MOTIF"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_MOTIF, _getOrdreGroupe().getMotif());
            // Date d'échéance
            super.setParametres(CAIListOrdreGroupeParam.LABEL_DATE_ECHEANCE, getSession().getLabel("DATEECHEANCE"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_DATE_ECHEANCE, _getOrdreGroupe().getDateEcheance());
            // Organe d'exécution
            super.setParametres(CAIListOrdreGroupeParam.LABEL_ORGANE_EXECUTION,
                    getSession().getLabel("ORGANE_EXECUTION"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_ORGANE_EXECUTION, _getOrdreGroupe().getOrganeExecution()
                    .getNom());
            // Numéro d'ordre groupé
            super.setParametres(CAIListOrdreGroupeParam.LABEL_NUMERO_OG, getSession().getLabel("NUMERO_OG"));
            super.setParametres(CAIListOrdreGroupeParam.PARAM_NUMERO_OG, _getOrdreGroupe().getNumeroOG());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }

        return;
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summary() {
        // Texte total en bas de liste
        super.setParametres(CAIListSoldeSectionParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        // Entêtes de colonnes
        this.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("BENEFICIAIRE"));
        this.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("COMPTEANNEXE"));
        this.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("ADRESSE_VERSEMENT"));
        this.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("NATURE_VERSEMENT"));
        this.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("MONTANT"));
        this.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("NUMERO_TRANSACTION"));
        this.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("RECAP_PAR_GENRE_VIREMENT"));
        this.setParametres(FWIImportParametre.getCol(11), getSession().getLabel("NOMBRE"));
        this.setParametres(FWIImportParametre.getCol(12), getSession().getLabel("MONTANT"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport ()
     */
    @Override
    public void afterBuildReport() {

        super.afterBuildReport();

        if ("xls".equals(getTypeImpression())) {
            try {
                printXlsDocument();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }

            return;
        }

        Map globalMap = importDoc.getParametre();
        String currTemplate = importDoc.getDocumentTemplate();
        importDoc.setDocumentTemplate("CAIListOrdreGroupe_sub");
        int numeroPage = importDoc.getDocument().getPages().size();

        try {

            List<Map<String, Object>> recapModel = new ArrayList<Map<String, Object>>();

            for (int kGenre : recap.keySet()) {

                String libelle = "";
                switch (kGenre) {
                    case 22:
                        libelle = getSession().getCodeLibelle("214002"); // CCP service Interieur
                        break;
                    case 27:
                        libelle = getSession().getCodeLibelle("214003"); // Banque service Interieur
                        break;
                    case 24:
                        libelle = getSession().getCodeLibelle("214001"); // Mandat service Interieur
                        break;
                    default:
                        libelle = "genre " + kGenre; // should never append
                }

                int nombre = (Integer) recap.get(kGenre)[0];
                BigDecimal montant = (BigDecimal) recap.get(kGenre)[1];

                Map<String, Object> m = new HashMap<String, Object>();
                m.put("COL_10", libelle);
                m.put("COL_11", nombre);
                m.put("COL_12", montant);

                recapModel.add(m);

            }

            // Tri ascendant par montant.
            Collections.sort(recapModel, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    BigDecimal m1 = (BigDecimal) o1.get("COL_12");
                    BigDecimal m2 = (BigDecimal) o2.get("COL_12");
                    return m1.compareTo(m2);
                }
            });

            importDoc.setParametre(CAIListOrdreGroupeParam.PARAM_PAGENUMERO, new Integer(numeroPage));
            importDoc.setDataSource(new JRMapCollectionDataSource(recapModel));
            importDoc.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
            e.printStackTrace();
        }

        // On redonne les infos d'origine
        importDoc.setParametre(globalMap);
        importDoc.setDocumentTemplate(currTemplate);

    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        importDoc = super.getImporter();
        getDocumentInfo().setTemplateName(importDoc.getDocumentTemplate());
        // Ajout des références en bas de liste
        super.setParametres(FWIImportParametre.PARAM_REFERENCE, _getRefParam());
        getDocumentInfo().setDocumentTypeNumber(CAIListOrdreGroupe.NUMERO_REFERENCE_INFOROM);
        _header();
        _tableHeader();
        _summary();
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.setTemplateFile("CAIListOrdreGroupe");
    }

    @Override
    public boolean beforePrintDocument() {
        if ("xls".equals(getTypeImpression())) {
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * Commentaire relatif à la méthode bindData.
     */
    public void bindData(net.sf.jasperreports.engine.JRDataSource dataSource) throws java.lang.Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 13:59:41)
     * 
     * @param id
     *            java.lang.String
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void bindData(String id) throws Exception {
        setIdOrdreGroupe(id);
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws FWIException {

        li = new ArrayList<Map<String, Object>>();
        CAOrdreGroupe og = _getOrdreGroupe();
        final String langue = getSession().getIdLangueISO().toUpperCase();

        try {

            long count = CASQL.count(og.getSession(), CAIListOrdreGroupeLite.getSQLBody(og.getIdOrdreGroupe(), langue));
            if (getParent() != null) {
                getParent().setProgressScaleValue(count);
                getParent().setProgressDescription("PDF");
            } else {
                setProgressScaleValue(count);
            }
            CASQL.cursor(og.getSession(), CAIListOrdreGroupeLite.getSQLFields(),
                    CAIListOrdreGroupeLite.getSQLBody(og.getIdOrdreGroupe(), langue) + " ORDER BY ov.NOMCACHE",
                    new CASQL.EachRow() {

                        @Override
                        public void eachRow(Map<String, String> row, long rowNumber) throws Exception {
                            CAOperationAdrPmt data = CAOperationAdrPmt.adapt(row);

                            // Vérifier le contexte d'exécution
                            if ((CAIListOrdreGroupeLite.this.getParent() != null)
                                    && CAIListOrdreGroupeLite.this.getParent().isAborted()) {
                                throw new CAAbortException();
                            }

                            /*
                             * Info pour la liste
                             */
                            Map<String, Object> m = new HashMap<String, Object>();
                            BigDecimal montant = new BigDecimal(data.getMontant());

                            m.put("COL_1", CAIListOrdreGroupeLite.this._getBeneficiaire(data));
                            m.put("COL_2", CAIListOrdreGroupeLite.this._getDescriptionCompteAnnexe(data));
                            m.put("COL_3", CAIListOrdreGroupeLite.this._getAdresseVersement(data));
                            m.put("COL_4", CAIListOrdreGroupeLite.this._getNature(data));
                            m.put("COL_5", montant);
                            m.put("COL_6", data.getNumeroTransaction());
                            li.add(m);

                            /*
                             * Info pour la recap par genre de transaction
                             */
                            int genre = CAProcessFormatOrdreOPAELite.findGenre(data);

                            Object[] nbAndMontant = recap.get(genre);
                            if (nbAndMontant == null) {
                                nbAndMontant = new Object[] { 1, montant };
                            } else {
                                int nb = (Integer) nbAndMontant[0];
                                BigDecimal somme = (BigDecimal) nbAndMontant[1];
                                somme = somme.add(montant);
                                nbAndMontant = new Object[] { nb + 1, somme };
                            }
                            recap.put(genre, nbAndMontant);

                            if (CAIListOrdreGroupeLite.this.getParent() != null) {
                                CAIListOrdreGroupeLite.this.getParent().incProgressCounter();
                            } else {
                                CAIListOrdreGroupeLite.this.incProgressCounter();
                            }

                        }
                    });

            super.setDataSource(li);
        } catch (CAAbortException e) {
            // l'utilisateur a aborté le traitement
        } catch (Exception e) {
            throw new FWIException(e);
        }

    }

    public String getDescriptionSection(BSession session, String idCategorie, String idExterne,
            String dateDebutPeriode, String dateFinPeriode, String idTypeSection) {
        // Initialiser
        String s = "";
        String codeISOLangue = session.getIdLangueISO();
        try {
            // Si le sous-type est fourni, récupérer le code système
            if (!JadeStringUtil.isIntegerEmpty(idCategorie)) {
                globaz.globall.parameters.FWParametersSystemCode code = new FWParametersSystemCode();
                code.setISession(session);
                // Déterminer la langue
                code.setIdLangue("F");
                if (codeISOLangue.equalsIgnoreCase("de")) {
                    code.setIdLangue("D");
                } else if (codeISOLangue.equalsIgnoreCase("it")) {
                    code.setIdLangue("I");
                }

                // Si l'idExterne contient un 9 dans la position 7 (AAAAMM9XXX)
                if (!JadeStringUtil.isIntegerEmpty(idExterne) && (idExterne.length() > 7)
                        && (Integer.parseInt(idExterne.substring(6, 7)) == 9)) {

                    code.setIdCode("2560" + idCategorie.substring(4, 6));
                    code.retrieve();

                    // Le code système n'existe pas, on prend donc le libelle
                    // suivant :
                    if (code.isNew()) {
                        code.getCode(idCategorie);
                    }
                } else {
                    code.getCode(idCategorie);
                }

                // Le code système existe
                if (!code.isNew()) {
                    s = code.getCodeUtilisateur(code.getIdLangue()).getLibelle();
                    // Ajouter l'année si période fournie
                    JADate dDebut = new JADate(dateDebutPeriode);
                    JADate dFin = new JADate(dateFinPeriode);
                    // Si aucune date, ne rien faire
                    if ((dDebut.getYear() == 0) && (dFin.getYear() == 0)) {
                        // Ajouter l'année sauf pour cotisations personnelles
                    } else {
                        if (!idCategorie.equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS)) {
                            if (dDebut.getYear() == dFin.getYear()) {
                                s = s + " " + String.valueOf(dFin.getYear());
                            } else {
                                s = s + " " + String.valueOf(dDebut.getYear()) + "-" + String.valueOf(dFin.getYear());
                            }
                        }
                    }
                }

            }
            // Si aucune description, utiliser celle du type de section
            if (JadeStringUtil.isBlank(s)) {
                CATypeSection type = new CATypeSection();
                type.setISession(session);
                type.setIdTypeSection(idTypeSection);
                try {
                    type.retrieve();
                } catch (Exception e) {
                    JadeLogger.warn(this, e);
                    type = null;
                }
                // Description
                if (type != null) {
                    s = s + type.getDescription(codeISOLangue);
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(this, e);
        }
        // Retourne la valeur
        return s;
    }

    /**
     * Returns the idOrdreGroupe.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    /**
     * @return
     */
    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * Returns the hasNext.
     * 
     * @return boolean
     */
    public boolean isHasNext() {
        return hasNext;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void labelsXls() {
        // Entetes du fichier excel
        this.remplirColumn("NUMERO_INFOROM", CAIListOrdreGroupe.NUMERO_REFERENCE_INFOROM, "");
        this.remplirColumn("DATE_DOC", JACalendar.todayJJsMMsAAAA(), "");
        this.remplirColumn("COL_1_LABEL", getSession().getLabel("BENEFICIAIRE"), "");
        this.remplirColumn("COL_2_LABEL", getSession().getLabel("COMPTEANNEXE"), "");
        this.remplirColumn("COL_3_LABEL", getSession().getLabel("ADRESSE_VERSEMENT"), "");
        this.remplirColumn("COL_4_LABEL", getSession().getLabel("NATURE_VERSEMENT"), "");
        this.remplirColumn("COL_5_LABEL", getSession().getLabel("MONTANT"), "");
        this.remplirColumn("COL_6_LABEL", getSession().getLabel("NUMERO_TRANSACTION"), "");

        this.remplirColumn(FWIImportParametre.PARAM_COMPANYNAME,
                (String) getImporter().getParametre().get(FWIImportParametre.PARAM_COMPANYNAME), "");
        this.remplirColumn(FWIImportParametre.PARAM_TITLE,
                (String) getImporter().getParametre().get(FWIImportParametre.PARAM_TITLE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_NUMERO_ORDRE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_NUMERO_ORDRE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_NUMERO_ORDRE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_NUMERO_ORDRE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_MOTIF,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_MOTIF), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_MOTIF,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_MOTIF), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_DATE_ECHEANCE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_DATE_ECHEANCE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_DATE_ECHEANCE,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_DATE_ECHEANCE), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_ORGANE_EXECUTION,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_ORGANE_EXECUTION), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_ORGANE_EXECUTION,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_ORGANE_EXECUTION), "");
        this.remplirColumn(CAIListOrdreGroupeParam.LABEL_NUMERO_OG,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.LABEL_NUMERO_OG), "");
        this.remplirColumn(CAIListOrdreGroupeParam.PARAM_NUMERO_OG,
                (String) getImporter().getParametre().get(CAIListOrdreGroupeParam.PARAM_NUMERO_OG), "");
        this.remplirColumn(CAIListSoldeSectionParam.LABEL_TOTAUX, getSession().getLabel("TOTAL"), "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (started) {
            return false;
        } else {
            started = true;
            return true;
        }
    }

    /**
     * Préparation des données pour le document excel
     */
    private void prepareDataForXLSDoc() {

        BigDecimal total = new BigDecimal(0);

        // labels du doc
        labelsXls();

        // liste
        int size = li.size();
        for (int i = 0; i < size; i++) {

            Map<String, Object> bean = li.get(new Integer(i));

            this.remplirColumn("COL_1", (String) bean.get("COL_1"), "");
            this.remplirColumn("COL_2", (String) bean.get("COL_2"), "");
            this.remplirColumn("COL_3", (String) bean.get("COL_3"), "");
            this.remplirColumn("COL_4", (String) bean.get("COL_4"), "");
            this.remplirColumn("COL_5", (BigDecimal) bean.get("COL_5"), "0.00");
            this.remplirColumn("COL_6", (String) bean.get("COL_6"), "");

            if (bean.get("COL_5") != null) {
                total = total.add((BigDecimal) bean.get("COL_5"));
            }
        }

        this.remplirColumn("TOTAL_MONTANT", total, "0.00");
        this.remplirColumn("TOTAL_TRANS", "" + size, "0");
    }

    public void printXlsDocument() throws Exception {

        try {

            String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                    + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                    + CAIListOrdreGroupe.XLS_DOC_NAME + "Modele.xml";

            String xlsDocPath = Jade.getInstance().getPersistenceDir()
                    + JadeFilenameUtil.addOrReplaceFilenameSuffixUID(CAIListOrdreGroupe.XLS_DOC_NAME + ".xml");

            prepareDataForXLSDoc();

            xlsDocPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xlsDocPath, xlsContainer);

            JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
            docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            docInfoExcel.setDocumentTitle(CAIListOrdreGroupe.XLS_DOC_NAME);
            docInfoExcel.setPublishDocument(true);
            docInfoExcel.setArchiveDocument(false);
            docInfoExcel.setDocumentTypeNumber(CAIListOrdreGroupe.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(docInfoExcel, xlsDocPath);

        } catch (Exception e) {
            throw new Exception("Error generating excel file", e);
        }
    }

    public void remplirColumn(String column, BigDecimal value, String defaultValue) {
        if (value != null) {
            xlsContainer.addValue(column, value.toString());
        } else {
            xlsContainer.addValue(column, defaultValue);
        }
    }

    public void remplirColumn(String column, String value, String defaultValue) {
        if (!JadeStringUtil.isEmpty(value)) {
            xlsContainer.addValue(column, value);
        } else {
            xlsContainer.addValue(column, defaultValue);
        }
    }

    /**
     * Sets the idOrdreGroupe.
     * 
     * @param idOrdreGroupe
     *            The idOrdreGroupe to set
     */
    public void setIdOrdreGroupe(java.lang.String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    /**
     * @param string
     */
    public void setIdTypeOperation(String string) {
        idTypeOperation = string;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
