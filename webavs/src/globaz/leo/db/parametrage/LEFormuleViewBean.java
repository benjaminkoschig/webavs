package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENFormule;
import globaz.envoi.db.parametreEnvoi.access.IENComplementFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENFormuleDefTable;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;

/**
 * @author ald
 * @since Créé le 21 mars 05
 * @revision SCO 8 juil. 2010
 */
public class LEFormuleViewBean extends ENFormule implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TABLE_COMP_FORMULE = IENComplementFormuleDefTable.TABLE_NAME;
    private static final String TABLE_FORMULE = IENFormuleDefTable.TABLE_NAME;
    private static final String TABLE_FORMULE_PDF = ILEFormuleDefTable.TABLE_NAME;

    private String catJournalisation = new String();
    private String classeName = new String();
    private String debfin = new String();
    private String domaine = new String();
    private String typeDocument = new String();

    /**
     * Constructeur de LEFormuleViewBean
     */
    public LEFormuleViewBean() {
        super();
        // par defaut une formule n'est :
        // pas recto-verso
        setCsRectoVerso(ILEConstantes.CS_NON);
        // pas automatique
        setCsManuAuto(ILEConstantes.CS_NON);
        // pas indexée dans la ged
        setCsIndexationGed(ILEConstantes.CS_NON);
        // pas de sauvegarde
        setCsSauvegarde(ILEConstantes.CS_NON);
        // et sans fenêtre de copies de decriptif
        setCsFenetreCopiesDes(ILEConstantes.CS_NON);
        // de type formule pdf
        setCsType(ILEConstantes.CS_TYPEFORMULEPDF);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {

        // Ajout du complement Début ou fin
        LEComplementFormuleViewBean formuleDebFin = new LEComplementFormuleViewBean();
        formuleDebFin.setSession(getSession());
        formuleDebFin.setCsTypeFormule(ILEConstantes.CS_IS_DEB_OR_FIN);
        formuleDebFin.setIdFormule(getIdFormule());
        formuleDebFin.setCsValeur(getDebfin());
        formuleDebFin.add(transaction);

        // Ajout du complement categorie journalisation
        LEComplementFormuleViewBean formuleCatJour = new LEComplementFormuleViewBean();
        formuleCatJour.setSession(getSession());
        formuleCatJour.setCsTypeFormule(ILEConstantes.CS_CATEGORIE_GROUPE);
        formuleCatJour.setIdFormule(getIdFormule());
        formuleCatJour.setCsValeur(getCatJournalisation());
        formuleCatJour.add(transaction);

        // Gestion du pdf
        if (!JadeStringUtil.isBlank(getClasseName())) {
            LEPdf pdf = new LEPdf();
            pdf.setSession(getSession());
            pdf.setIdFormule(getIdFormule());
            pdf.setClasseName(getClasseName());
            pdf.add(transaction);
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        LEComplementFormuleManager manager = new LEComplementFormuleManager();
        manager.setSession(getSession());
        manager.setForIdFormule(getIdFormule());
        manager.find(transaction);

        for (int i = 0; i < manager.size(); i++) {
            LEComplementFormuleViewBean complement = (LEComplementFormuleViewBean) manager.getEntity(i);
            complement.delete(transaction);
        }

        // Gestion de la classe
        LEPdf pdf = new LEPdf();
        pdf.setSession(getSession());
        pdf.setIdFormule(getIdFormule());
        pdf.retrieve(transaction);

        if (!pdf.isNew()) {
            pdf.delete(transaction);
        }
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        setCsDocument(getDefinitionFormule().getCsDocument());
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        // Gestion des complements
        LEComplementFormuleManager manager = new LEComplementFormuleManager();
        manager.setSession(getSession());
        manager.setForIdFormule(getIdFormule());
        manager.find(transaction);

        for (int i = 0; i < manager.size(); i++) {
            LEComplementFormuleViewBean formule = (LEComplementFormuleViewBean) manager.getEntity(i);

            if (ILEConstantes.CS_IS_DEB_OR_FIN.equals(formule.getCsTypeFormule())) {
                if (!formule.getCsValeur().equals(getDebfin())) {
                    formule.setCsValeur(getDebfin());
                    formule.update(transaction);
                }
            }
            if (ILEConstantes.CS_CATEGORIE_GROUPE.equals(formule.getCsTypeFormule())) {
                if (!formule.getCsValeur().equals(getCatJournalisation())) {
                    formule.setCsValeur(getCatJournalisation());
                    formule.update(transaction);
                }
            }
        }

        // Gestion de la classe
        LEPdf pdf = new LEPdf();
        pdf.setSession(getSession());
        pdf.setIdFormule(getIdFormule());
        pdf.retrieve(transaction);

        if (pdf.isNew()) {
            pdf.setClasseName(getClasseName());
            pdf.setIdFormule(getIdFormule());
            pdf.add(transaction);
        } else {
            if (JadeStringUtil.isBlank(getClasseName())) {
                pdf.delete(transaction);
            } else if (!getClasseName().equals(pdf.getClasseName())) {
                pdf.setClasseName(getClasseName());
                pdf.update(transaction);
            }
        }
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        setIdFormule(_incCounter(transaction, "0", TABLE_FORMULE, "0", "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append(_getCollection() + TABLE_FORMULE + ".*, ");
        sqlFields.append(_getCollection() + TABLE_FORMULE_PDF + ".*, ");
        sqlFields.append(" A." + IENComplementFormuleDefTable.CS_VALEUR + " DEBFIN, ");
        sqlFields.append(" B." + IENComplementFormuleDefTable.CS_VALEUR + " CATJOUR");

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + TABLE_FORMULE);
        sqlFrom.append(" LEFT OUTER JOIN " + _getCollection() + TABLE_FORMULE_PDF + " ON (" + _getCollection()
                + TABLE_FORMULE_PDF + "." + ILEFormuleDefTable.ID_FORMULE + "=" + _getCollection() + TABLE_FORMULE
                + "." + IENFormuleDefTable.ID_FORMULE + ")");
        sqlFrom.append(" LEFT OUTER JOIN " + _getCollection() + TABLE_COMP_FORMULE + " A ON ( A."
                + IENComplementFormuleDefTable.ID_FORMULE + " = " + _getCollection() + TABLE_FORMULE + "."
                + ILEFormuleDefTable.ID_FORMULE + " AND A." + IENComplementFormuleDefTable.CS_TYPE_FORMULE + " = "
                + ILEConstantes.CS_IS_DEB_OR_FIN + ")");
        sqlFrom.append(" LEFT OUTER JOIN " + _getCollection() + TABLE_COMP_FORMULE + " B ON ( B."
                + IENComplementFormuleDefTable.ID_FORMULE + " = " + _getCollection() + TABLE_FORMULE + "."
                + ILEFormuleDefTable.ID_FORMULE + " AND B." + IENComplementFormuleDefTable.CS_TYPE_FORMULE + " = "
                + ILEConstantes.CS_CATEGORIE_GROUPE + ")");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_FORMULE;
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        classeName = statement.dbReadString(ILEFormuleDefTable.NOM_CLASSE);
        domaine = statement.dbReadString(ILEFormuleDefTable.DOMAINE);
        typeDocument = statement.dbReadString(ILEFormuleDefTable.TYPE_DOC);
        catJournalisation = statement.dbReadNumeric("CATJOUR");
        debfin = statement.dbReadNumeric("DEBFIN");
    }

    /**
     * @see globaz.envoi.db.parametreEnvoi.access.ENFormule#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                _getCollection() + TABLE_FORMULE + "." + IENFormuleDefTable.ID_FORMULE,
                _dbWriteNumeric(statement.getTransaction(), getIdFormule(),
                        "idFormule - clé primaire du fichier des formules"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCatJournalisation() {
        return catJournalisation;
    }

    public String getClasseName() {
        return classeName;
    }

    public String getDebfin() {
        return debfin;
    }

    public String getDomaine() {
        return domaine;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCatJournalisation(String catJournalisation) {
        this.catJournalisation = catJournalisation;
    }

    public void setClasseName(String string) {
        classeName = string;
    }

    public void setDebfin(String debfin) {
        this.debfin = debfin;
    }

    public void setDomaine(String string) {
        domaine = string;
    }

    public void setTypeDocument(String string) {
        typeDocument = string;
    }

}
