package globaz.apg.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Entité base de donnée regroupant les données communes à la génération des annonces APG et RAPG (RAPG : après
 * septembre 2012)
 * 
 * @author PBA
 * @see APRecapitulationAnnonce
 * @see APRecapitulationAnnonceHermes
 */
public abstract class APAbstractRecapitulationAnnonce extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_COMPTECARTES = "VKNCOA";
    public static final String FIELDNAME_SOMMEFRAISGARDE = "VKMSFG";
    public static final String FIELDNAME_SOMMENBJOURS = "VKNSNJ";
    public static final String FIELDNAME_SOMMETOTALAPG = "VKMSTA";

    private String compteCartes;
    private String genreService;
    private String moisAnnee;
    private String sommeFraisDeGarde;
    private String sommeNombreJoursService;
    private String sommeTotalAPG;

    public APAbstractRecapitulationAnnonce() {
        super();

        compteCartes = "";
        genreService = "";
        moisAnnee = "";
        sommeFraisDeGarde = "";
        sommeNombreJoursService = "";
        sommeTotalAPG = "";
    }

    @Override
    protected final boolean _allowAdd() {
        return false;
    }

    @Override
    protected final boolean _allowDelete() {
        return false;
    }

    @Override
    protected final boolean _allowUpdate() {
        return false;
    }

    @Override
    protected final String _getFields(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        sql.append("COUNT(").append(APAnnonceAPG.FIELDNAME_CONTENUANNONCE).append(") AS ")
                .append(APAbstractRecapitulationAnnonce.FIELDNAME_COMPTECARTES).append(",");

        sql.append("SUM(").append(APAnnonceAPG.FIELDNAME_MONTANTALLOCATIONFRAISGARDE).append(") AS ")
                .append(APAbstractRecapitulationAnnonce.FIELDNAME_SOMMEFRAISGARDE).append(",");
        sql.append("SUM(").append(APAnnonceAPG.FIELDNAME_NOMBREJOURSSERVICE).append(") AS ")
                .append(APAbstractRecapitulationAnnonce.FIELDNAME_SOMMENBJOURS).append(",");
        sql.append("SUM(").append(APAnnonceAPG.FIELDNAME_TOTALAPG).append(") AS ")
                .append(APAbstractRecapitulationAnnonce.FIELDNAME_SOMMETOTALAPG).append(",");

        sql.append(APAnnonceAPG.FIELDNAME_GENRE).append(",");
        sql.append(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE);

        String specificFields = getSpecificFields();
        if (!JadeStringUtil.isBlank(specificFields)) {
            sql.append(",").append(specificFields);
        }
        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return APAnnonceAPG.TABLE_NAME;
    }

    @Override
    protected final void _readProperties(BStatement statement) throws Exception {
        compteCartes = statement.dbReadNumeric(APAbstractRecapitulationAnnonce.FIELDNAME_COMPTECARTES);
        genreService = statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_GENRE);
        moisAnnee = formatDateFromDB(statement.dbReadNumeric(APAnnonceAPG.FIELDNAME_MOISANNEECOMPTABLE));
        sommeFraisDeGarde = statement.dbReadNumeric(APAbstractRecapitulationAnnonce.FIELDNAME_SOMMEFRAISGARDE);
        sommeNombreJoursService = statement.dbReadNumeric(APAbstractRecapitulationAnnonce.FIELDNAME_SOMMENBJOURS);
        sommeTotalAPG = statement.dbReadNumeric(APAbstractRecapitulationAnnonce.FIELDNAME_SOMMETOTALAPG);
        readSpecificFields(statement);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected final void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected final void _writeProperties(BStatement statement) throws Exception {
    }

    private String formatDateFromDB(String date) {
        try {
            String ret = JACalendar.format(date.substring(4, 6) + date.substring(0, 4), JACalendar.FORMAT_MMsYYYY);

            return ret;
        } catch (Exception e) {
            return date;
        }
    }

    public abstract String getCodeUtilisateurTypeMontant();

    public String getCompteCartes() {
        return compteCartes;
    }

    public String getGenreService() {
        return genreService;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getSommeFraisDeGarde() {
        return sommeFraisDeGarde;
    }

    public String getSommeNombreJoursService() {
        return sommeNombreJoursService;
    }

    public String getSommeTotalAPG() {
        return sommeTotalAPG;
    }

    protected abstract String getSpecificFields();

    protected abstract void readSpecificFields(BStatement statement) throws Exception;

    public void setCompteCartes(String compteCartes) {
        this.compteCartes = compteCartes;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setSommeFraisDeGarde(String sommeFraisDeGarde) {
        this.sommeFraisDeGarde = sommeFraisDeGarde;
    }

    public void setSommeNombreJoursService(String sommeNombreJoursService) {
        this.sommeNombreJoursService = sommeNombreJoursService;
    }

    public void setSommeTotalAPG(String sommeTotalAPG) {
        this.sommeTotalAPG = sommeTotalAPG;
    }
}
