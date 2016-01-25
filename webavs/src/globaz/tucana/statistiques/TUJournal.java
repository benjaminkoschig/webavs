package globaz.tucana.statistiques;

import globaz.framework.translation.FWTranslation;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersCodeManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersTypeCode;
import globaz.globall.parameters.FWParametersTypeCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.journal.access.TUDetailGroupeCategorieRubrique;
import globaz.tucana.db.journal.access.TUDetailGroupeCategorieRubriqueManager;
import globaz.tucana.db.statistique.access.TUTemporaire;
import globaz.tucana.exception.fw.TUFWAddException;
import globaz.tucana.exception.fw.TUFWException;
import globaz.tucana.exception.process.TUProcessJournalException;
import globaz.tucana.statistiques.config.TUCategorieRubriqueStatistiqueConfig;
import globaz.tucana.statistiques.config.TUStatistiqueConfig;
import globaz.tucana.statistiques.config.TUStatistiquesConfigProvider;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Représentation d'un journal
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 */
public class TUJournal {
    public static final String ABR_LABEL = "ABR_CS_";

    public static final String TYPE_CHARGEMENT_AGENCE = "4";

    public static final String TYPE_CHARGEMENT_AUCUN = "0";

    public static final String TYPE_CHARGEMENT_CANTON = "1";

    public static final String TYPE_CHARGEMENT_DEFAULT = "2";
    public static final String TYPE_CHARGEMENT_MOIS = "3";
    // private static final String UNKNOWN_LABEL = "Unknown label";
    private static final String UNKNOWN_LABEL = "LABEL_";

    /**
     * Insert un journal en base
     * 
     * @param transaction
     * @param journal
     */
    public static void toDB(BTransaction transaction, TUJournal journal) throws TUFWException {
        TUTemporaire tmp = new TUTemporaire();
        tmp.clear(transaction);
        tmp.setSession(transaction.getSession());
        // Ajoute les informations du journal
        tmp.setDateCreation(journal.getDateCreation().toStr("."));
        tmp.setAnnee(journal.getAnnee());
        tmp.setMoisNume(journal.getMoisNume());
        tmp.setMoisAlpha(journal.getMoisNume());
        tmp.setAgence(journal.getCsAgence());
        // Lecture des groupes catégorie
        Iterator iter = journal.getGroupeCategories().keySet().iterator();
        Iterator iter2 = null;
        Iterator iter3 = null;
        while (iter.hasNext()) {
            TUGroupeCategories groupe = (TUGroupeCategories) journal.getGroupeCategories().get(iter.next());
            tmp.setGroupe(groupe.getLibelle());
            // Lecture des catégories rubrique
            iter2 = groupe.getCategories().iterator();
            while (iter2.hasNext()) {
                TUCategorie categorie = (TUCategorie) iter2.next();
                tmp.setCategorie(categorie.getLibelle());
                // lecture des entry
                iter3 = categorie.getEntries().keySet().iterator();
                while (iter3.hasNext()) {
                    TUEntry entry = (TUEntry) categorie.getEntries().get(iter3.next());
                    tmp.setCantonCourt(entry.getColonne().getAbreviation());
                    tmp.setCantonLong(entry.getColonne().getLibelle());
                    tmp.setTotal(JANumberFormatter.format(entry.getTotal()));
                    try {
                        tmp.add(transaction);
                    } catch (Exception e) {
                        throw new TUFWAddException("TUJournal.toDB() : problème lors de l'ajout d'un enregistrement", e);
                    }
                }

            }

        }
    }

    /**
     * Insert un journal en base sans les entrées indépendemment des colonnes
     * 
     * @param transaction
     * @param journal
     */
    public static void toDBWihtoutEntry(BTransaction transaction, TUJournal journal) throws TUFWException {
        TUTemporaire tmp = new TUTemporaire();
        tmp.clear(transaction);
        tmp.setSession(transaction.getSession());
        // Ajoute les informations du journal
        tmp.setDateCreation(journal.getDateCreation().toStr("."));
        tmp.setAnnee(journal.getAnnee());
        tmp.setMoisNume(journal.getMoisNume());
        tmp.setMoisAlpha(journal.getMoisAlpha());
        tmp.setAgence(journal.getCsAgence());
        // Lecture des groupes catégorie
        Iterator iter = journal.getGroupeCategories().keySet().iterator();
        Iterator iter2 = null;
        while (iter.hasNext()) {
            TUGroupeCategories groupe = (TUGroupeCategories) journal.getGroupeCategories().get(iter.next());
            tmp.setGroupe(groupe.getLibelle());
            // Lecture des catégories rubrique
            iter2 = groupe.getCategories().iterator();
            while (iter2.hasNext()) {
                TUCategorie categorie = (TUCategorie) iter2.next();
                tmp.setCategorie(categorie.getLibelle());
                tmp.setTotal(JANumberFormatter.format(categorie.getTotal()));
                try {
                    tmp.add(transaction);
                } catch (Exception e) {
                    throw new TUFWAddException("TUJournal.toDB() : problème lors de l'ajout d'un enregistrement", e);
                }

            }

        }
    }

    private String annee = null;

    private Map colonnes = null;

    private String csAgence = null;

    private JADate dateCreation = null;

    private Map groupeCategories = null;

    private String libelleAgence = null;

    private String moisAlpha = null;

    private String moisNume = null;

    protected TUJournal() {
        csAgence = "";
        libelleAgence = "";
        annee = "";
        moisNume = "";
        moisAlpha = "";
        init();
    }

    /**
     * Constructeur du journal
     * 
     * @param transaction
     * @param _csAgence
     * @param _libelleAgence
     * @param _annee
     * @param _moisNume
     * @param _moisAlpha
     */
    public TUJournal(BTransaction transaction, String _csAgence, String _libelleAgence, String _annee,
            String _moisNume, String _moisAlpha) {
        csAgence = _csAgence;
        libelleAgence = _libelleAgence;
        annee = _annee;
        moisNume = _moisNume;
        moisAlpha = _moisAlpha;
        init();

    }

    /**
     * Ajoute une colonne à la liste des colonnes des journaux
     * 
     * @param canton
     */
    private void addColonne(TUColonne colonne) {
        if (colonnes == null) {
            colonnes = new TreeMap();
        }
        if (!colonneExiste(colonne.getAbreviation().concat("-").concat(colonne.getRang()).toUpperCase())) {
            colonnes.put(colonne.getAbreviation().concat("-").concat(colonne.getRang()).toUpperCase(), colonne);
        }
    }

    /**
     * @param transaction
     * @param maColonne
     * @param detail
     * @param config
     * @param statistiqueConfig
     * @throws TUProcessJournalException
     */
    private void addData(BTransaction transaction, TUColonne maColonne, TUDetailGroupeCategorieRubrique detail,
            TUCategorieRubriqueStatistiqueConfig config, TUStatistiqueConfig statistiqueConfig)
            throws TUProcessJournalException {
        addGroupe(transaction, detail, maColonne, config, statistiqueConfig);
        addColonne(maColonne);
    }

    /**
     * Ajoute un groupe-catégorie si inexistant
     * 
     * @param transaction
     * @param detail
     * @param colonne
     * @param config
     * @param statistiqueConfig
     * @throws TUProcessJournalException
     */
    private void addGroupe(BTransaction transaction, TUDetailGroupeCategorieRubrique detail, TUColonne colonne,
            TUCategorieRubriqueStatistiqueConfig config, TUStatistiqueConfig statistiqueConfig)
            throws TUProcessJournalException {
        TUGroupeCategories groupe = findGroupeCategories(transaction, detail, config, statistiqueConfig);
        TUCategorieRubriqueStatistiqueConfig conf = statistiqueConfig.getCategorieRubriqueConfig(JadeStringUtil
                .fillWithZeroes(detail.getIdGroupeCategorie(), 3));

        String ordre = groupe.getOrdre();
        if ((conf != null) && (conf.getOrder() != null)) {
            ordre = conf.getOrder();
        }

        groupe.addGroupeCategorie(
                transaction,
                colonne,
                ordre,
                detail.getIdGroupeCategorie(),
                recupereAbreviationCS(transaction, detail.getCsCategorie(), statistiqueConfig),
                transaction.getSession().getCodeLibelle(detail.getCsCategorie()),
                detail.getCsOperation().equals(ITUCSConstantes.CS_ADDITION) ? JAUtil.createBigDecimal(
                        detail.getNombreMontant(), 3) : (JAUtil.createBigDecimal(detail.getNombreMontant(), 3))
                        .negate());
        groupeCategories.put(config == null ? detail.getCsGroupeCategorie() : config.getGroup(), groupe);
        // groupeCategories.put(config == null ? detail.getCsGroupeCategorie() :
        // config.getOrder(), groupe);
    }

    /**
     * Chargement des colonnes
     * 
     * @param transaction
     * @param typeChargement
     * @param config
     * @param detail
     * @param listCanton
     * @param listMois
     * @param listTrimestre
     * @param listAgence
     * @param statistiqueConfig
     * @return
     * @throws TUProcessJournalException
     */
    private void charge(BTransaction transaction, String typeChargement, TUCategorieRubriqueStatistiqueConfig config,
            TUDetailGroupeCategorieRubrique detail, TreeMap listCanton, TreeMap listMois, TreeMap listTrimestre,
            TreeMap listAgence, TUStatistiqueConfig statistiqueConfig) throws TUProcessJournalException {
        // chargement de la colonne canton
        if (TUJournal.TYPE_CHARGEMENT_CANTON.equals(typeChargement)) {
            if (listCanton.size() == 0) {
                loadCanton(transaction, listCanton);
            }

            addData(transaction,
                    new TUColonne(detail.getCanton(), detail.getCanton(), (String) listCanton.get(detail.getCanton())),
                    detail, config, statistiqueConfig);
            // chargement de la colonne mois
        } else if (TUJournal.TYPE_CHARGEMENT_MOIS.equals(typeChargement)) {
            if (!JadeStringUtil.isBlankOrZero(detail.getMois())) {
                // chargement de la liste des mois
                if (listMois.size() == 0) {
                    loadMois(transaction, listMois);
                }
                addData(transaction, new TUColonne("", JadeStringUtil.fillWithZeroes(detail.getMois(), 2),
                        (String) listMois.get(JadeStringUtil.fillWithZeroes(detail.getMois().toString(), 2))), detail,
                        config, statistiqueConfig);
                if (listTrimestre.size() == 0) {
                    loadTrimestre(listTrimestre);
                }
                /*
                 * rang = le numéro du trimestre attribut = le dernier mois du trimestre libellé = le libellé du
                 * trimestre
                 */
                addData(transaction,
                        new TUColonne(JadeStringUtil.fillWithZeroes(((String) listTrimestre.get(JadeStringUtil
                                .fillWithZeroes(detail.getMois().toString(), 2))).substring(0, 1), 2), JadeStringUtil
                                .fillWithZeroes(
                                        new BigDecimal(((String) listTrimestre.get(JadeStringUtil.fillWithZeroes(detail
                                                .getMois().toString(), 2))).substring(0, 1)).multiply(
                                                new BigDecimal("3")).toString(), 2), (String) listTrimestre
                                .get(JadeStringUtil.fillWithZeroes(detail.getMois().toString(), 2)), false), detail,
                        config, statistiqueConfig);
                /*
                 * rang = 06s (1er semestre) attribut = 06 = le libellé du trimestre
                 */

                if (Integer.parseInt(detail.getMois()) < 7) {
                    addData(transaction, new TUColonne(TUCategorie.RANG_PREMIER_SEMESTRE,
                            TUCategorie.ABREVIATION_PREMIER_SEMESTRE, "1er Semestre"), detail, config,
                            statistiqueConfig);
                }
                if (Integer.parseInt(detail.getMois()) < 10) {
                    addData(transaction, new TUColonne(TUCategorie.RANG_9_MOIS, TUCategorie.ABREVIATION_9_MOIS,
                            "9 Mois"), detail, config, statistiqueConfig);
                }

            }
        } else if (TUJournal.TYPE_CHARGEMENT_AGENCE.equals(typeChargement)) {
            if (!JadeStringUtil.isBlankOrZero(detail.getCsAgence())) {
                // chargement de la liste des agences
                if (listAgence.size() == 0) {
                    loadAgence(transaction, listAgence);
                }
                addData(transaction,
                        new TUColonne("", detail.getCsAgence(), (String) listAgence.get(detail.getCsAgence())), detail,
                        config, statistiqueConfig);
            }
        } else if (TUJournal.TYPE_CHARGEMENT_DEFAULT.equals(typeChargement)) {

            addData(transaction, new TUColonne("C1", "C1", "Colonne 1"), detail, config, statistiqueConfig);
        } else {
            addData(transaction,
                    new TUColonne(config.getNom(), config.getNom(), config.getId().concat("-").concat(config.getNom())),
                    detail, config, statistiqueConfig);
        }

    }

    /**
     * Lecture du manager, charge la colonne et met à jour le groupe et la colonne
     * 
     * @param transaction
     * @param detailMgr
     * @param typeChargement
     * @param typeConfig
     * @throws Exception
     */
    public void chargeJournal(BTransaction transaction, TUDetailGroupeCategorieRubriqueManager detailMgr,
            String typeChargement, String typeConfig) throws Exception {
        TUDetailGroupeCategorieRubrique detail = null;
        TUStatistiquesConfigProvider instance = TUStatistiquesConfigProvider.getInstance();
        TUCategorieRubriqueStatistiqueConfig config = null;
        TreeMap listCanton = new TreeMap();
        TreeMap listMois = new TreeMap();
        TreeMap listAgence = new TreeMap();
        TreeMap listTrimestre = new TreeMap();
        TUStatistiqueConfig statistiqueConfig = (TUStatistiqueConfig) TUStatistiquesConfigProvider.getInstance()
                .getStatistiquesConfigContainer().get(transaction.getSession().getCode(typeConfig));
        for (int i = 0; i < detailMgr.size(); i++) {
            detail = (TUDetailGroupeCategorieRubrique) detailMgr.getEntity(i);
            // chargement de la configuration si existe
            config = instance.getRubriqueConfig(statistiqueConfig,
                    JadeStringUtil.fillWithZeroes(detail.getIdGroupeCategorie(), 3));
            charge(transaction, typeChargement, config, detail, listCanton, listMois, listTrimestre, listAgence,
                    statistiqueConfig);
        }
    }

    /**
     * Recherche si la colonne est défini dans la liste des colonnes
     * 
     * @param key
     *            l'abréviation de la colonne en UpperCase()
     * @return
     */
    private boolean colonneExiste(String key) {
        return colonnes.containsKey(key.toUpperCase());
    }

    /**
     * recherche la catégorie et la créée le cas échéant
     * 
     * @param transaction
     * @param detail
     * @param config
     * @param statistiqueConfig
     * @return
     */
    private TUGroupeCategories findGroupeCategories(BTransaction transaction, TUDetailGroupeCategorieRubrique detail,
            TUCategorieRubriqueStatistiqueConfig config, TUStatistiqueConfig statistiqueConfig) {
        String key = config == null ? detail.getCsGroupeCategorie() : config.getGroup();
        if (groupeCategories.containsKey(key)) {
            return (TUGroupeCategories) groupeCategories.get(key);
        } else {
            return new TUGroupeCategories(detail.getCsGroupeCategorie(), config == null ? "0" : config.getOrder(),
                    recupereAbreviationCS(transaction, detail.getCsGroupeCategorie(), statistiqueConfig), transaction
                            .getSession().getCodeLibelle(detail.getCsGroupeCategorie()), detail.getIdGroupeCategorie(),
                    config);
        }
    }

    /**
     * Récupère l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupère le Map des colonnes
     * 
     * @return
     */
    public Map getColonnes() {
        return colonnes;
    }

    /**
     * Récuèpère le code sytème de l'agence
     * 
     * @return
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Récupère la date de création de journal
     * 
     * @return
     */
    public JADate getDateCreation() {
        return dateCreation;
    }

    // /**
    // * Permet d'ajouter les groupes catégories à un journal
    // * @deprecated
    // * @param transaction
    // * @param detailMgr
    // * @param type
    // * @throws Exception
    // */
    // public void addGroupeCategories(BTransaction transaction,
    // TUDetailGroupeCategorieRubriqueManager detailMgr, String type) throws
    // Exception {
    // chargeJournal(transaction, detailMgr, TYPE_CHARGEMENT_AUCUN, type);
    // }

    /**
     * Récupère le Map Groupes de catégories
     * 
     * @return
     */
    public Map getGroupeCategories() {
        return groupeCategories;
    }

    /**
     * Récuèpère le libellé de l'agence
     * 
     * @return
     */
    public String getLibelleAgence() {
        return libelleAgence;
    }

    /**
     * Récupère le moisNume du journal (alpha)
     * 
     * @return
     */
    public String getMoisAlpha() {
        return moisAlpha;
    }

    /**
     * Récupère le moisNume du journal
     * 
     * @return
     */
    public String getMoisNume() {
        return moisNume;
    }

    /**
     * Initialisation des propriétés
     */
    private void init() {
        groupeCategories = new TreeMap();
        colonnes = new TreeMap();
        dateCreation = JACalendar.today();
    }

    /**
     * Récupère la liste des agences
     * 
     * @param transaction
     * @param listAgence
     */
    private void loadAgence(BTransaction transaction, TreeMap listAgence) {
        FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
        manager.setSession(transaction.getSession());
        manager.setForIdGroupe(ITUCSConstantes.CS_GROUPE_AGENCE);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            // la liste sera vide et traitée ainsi
            listAgence.clear();
        }
        for (int i = 0; i < manager.size(); i++) {
            String idCode = ((FWParametersSystemCode) manager.getEntity(i)).getIdCode();
            listAgence.put(idCode, transaction.getSession().getCode(idCode));
        }
    }

    /**
     * Chargement des cantons
     * 
     * @param transaction
     * @param listCanton
     * @throws TUProcessJournalException
     */
    private void loadCanton(BTransaction transaction, TreeMap listCanton) throws TUProcessJournalException {
        FWParametersTypeCodeManager familleManager = new FWParametersTypeCodeManager();
        familleManager.setSession(transaction.getSession());
        familleManager.setForGroupLike(ITUCSConstantes.CS_GROUPE_CANTON);
        try {
            familleManager.find(BManager.SIZE_NOLIMIT);
            FWParametersTypeCode famille = null;
            if (familleManager.size() > 0) {
                famille = ((FWParametersTypeCode) familleManager.getFirstEntity());
            }
            if (famille != null) {

                FWParametersCodeManager codeManager;
                codeManager = FWTranslation.getSystemCodeList(famille.getGroupe(), transaction.getSession());
                for (int i = 0; i < codeManager.size(); i++) {
                    FWParametersCode current = (FWParametersCode) codeManager.getEntity(i);
                    listCanton.put(current.getCurrentCodeUtilisateur().getCodeUtilisateur(), current
                            .getCurrentCodeUtilisateur().getLibelle());
                }
            }
        } catch (Exception e) {
            throw new TUProcessJournalException("Problème lors de la recherche des colonnes", e);
        }
    }

    /**
     * Chargement des mois
     * 
     * @param transaction
     * @param listMois
     * @throws TUProcessJournalException
     */
    private void loadMois(BTransaction transaction, TreeMap listMois) throws TUProcessJournalException {
        String[] myList = null;
        // Chargement des mois de l'année
        if (transaction.getSession().getIdLangueISO().equalsIgnoreCase(JACalendar.LANGUAGE_DE)) {
            myList = JACalendar.MONTH_NAMES_DE;
        } else if (transaction.getSession().getIdLangueISO().equalsIgnoreCase(JACalendar.LANGUAGE_IT)) {
            myList = JACalendar.MONTH_NAMES_IT;
        } else {
            myList = JACalendar.MONTH_NAMES_FR;
        }
        // lecture des mois de l'année afin de charger dans le treeMap
        for (int i = 1; i <= myList.length; i++) {
            if (i != myList.length) {
                listMois.put(JadeStringUtil.fillWithZeroes(Integer.toString(listMois.size() + 1), 2), myList[i]);
            }
        }
    }

    /**
     * Chargement des trimestres
     * 
     * @param listTrimestre
     */
    private void loadTrimestre(TreeMap listTrimestre) {
        int cpt = 1;
        for (int i = 1; i < 13; i++) {
            listTrimestre.put(JadeStringUtil.fillWithZeroes(Integer.toString(listTrimestre.size() + 1), 2), cpt
                    + (cpt == 1 ? "er" : "ème") + " trim.");
            if (i % 3 == 0) {
                cpt++;
            }
        }
    }

    /**
     * Création d'un saut de ligne
     * 
     * @param csvString
     */
    private void newLine(StringBuffer csvString) {
        csvString.append("\n");

    }

    /**
     * Récupère l'abréviation du code système ou le label en fonction du paramétrage du fichier statistique.xml
     * 
     * @param transaction
     * @param csCategorie
     * @param statistiqueConfig
     * @return
     */
    private String recupereAbreviationCS(BTransaction transaction, String csCategorie,
            TUStatistiqueConfig statistiqueConfig) {
        if (statistiqueConfig.getcsLabel()) {
            String abreviation = transaction.getSession().getLabel("ABR_CS_".concat(csCategorie));
            if (abreviation.startsWith(TUJournal.UNKNOWN_LABEL)) {
                return transaction.getSession().getCodeLibelle(csCategorie);
            } else {
                return abreviation;
            }
        } else {
            return transaction.getSession().getCodeLibelle(csCategorie);
        }
    }

    /**
     * Permet de générer le journal sous forme csv
     */
    public void toCsv(StringBuffer csvString) {
        csvString.append(libelleAgence).append(";");
        csvString.append(JACalendar.format(dateCreation, JACalendar.FORMAT_DDsMMsYYYY)).append(";");
        csvString.append("moisNume : ").append(moisNume).append(";");
        csvString.append("annee : ").append(annee).append(";");
        // nouvelle ligne
        newLine(csvString);
        // ajoute un espace
        csvString.append(";");
        // gestion des colonnes
        Iterator it = colonnes.keySet().iterator();
        TreeMap tree = new TreeMap();
        while (it.hasNext()) {
            // ((TUColonne) entry = colonnes.get(it.next())).toCsv(csvString);
            TUColonne entry = (TUColonne) colonnes.get(it.next());
            entry.toCsv(csvString);
            csvString.append(";");
            tree.put(entry.getAbreviation(), "0");
        }
        it = groupeCategories.keySet().iterator();
        // nouvelle ligne
        newLine(csvString);
        while (it.hasNext()) {
            ((TUGroupeCategories) groupeCategories.get(it.next())).toCsv(csvString, tree);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n Affichage journal");
        str.append("\n Année : ").append(annee);
        str.append("\n Mois : ").append(moisAlpha);
        str.append("\n - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        Iterator iter = colonnes.keySet().iterator();
        while (iter.hasNext()) {
            TUColonne element = (TUColonne) colonnes.get(iter.next());
            str.append(element.toString());
        }
        str.append("\n - - - - - - - - - - - - - - - - - - - - - - - - -\n");

        Iterator iter2 = groupeCategories.keySet().iterator();
        while (iter2.hasNext()) {
            TUGroupeCategories element = (TUGroupeCategories) groupeCategories.get(iter2.next());
            str.append(element.toString());
        }
        return str.toString();
    }

    /**
     * Permet de générer le journal sous forme xml
     */
    public void toXml(StringBuffer xmlString) {
        xmlString.append("<journal ");
        xmlString.append("annee=\"").append(annee).append("\" ");
        xmlString.append("moisNume=\"").append(moisNume).append("\" ");
        xmlString.append("moisAlpha=\"").append(moisAlpha).append("\" ");
        xmlString.append("agence=\"").append(csAgence).append("\" ");
        xmlString.append("libelleAgence=\"").append(libelleAgence).append("\" ");
        xmlString.append("dateCreation=\"").append(JACalendar.format(dateCreation, JACalendar.FORMAT_DDsMMsYYYY))
                .append("\">");
        // Gestion des colonnes
        Iterator it = colonnes.keySet().iterator();
        while (it.hasNext()) {
            ((TUColonne) colonnes.get(it.next())).toXml(xmlString);
        }

        it = new TreeMap(groupeCategories).keySet().iterator();
        while (it.hasNext()) {
            ((TUGroupeCategories) groupeCategories.get(it.next())).toXml(xmlString);
        }
        xmlString.append("</journal>");
        // System.out.println(xmlString);
    }

}
