package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author: mmo
 */
public class CINSSRACompteIndividuel extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String COLUMN_NAME_CI_INACTIF = "CIINA";
    public static final String COLUMN_NAME_CI_INVALIDE = "CIINVA";
    public static final String COLUMN_NAME_CI_OUVERT = "CIOUV";
    public static final String COLUMN_NAME_CODE_MUTATION = "CODMUT";
    public static final String COLUMN_NAME_ID_CI = "IDCI";
    public static final String COLUMN_NAME_NOM_PRENOM = "NOMPRE";
    public static final String COLUMN_NAME_NUMERO_AVS = "NUMAVS";

    private Boolean ciInactif;
    private Boolean ciInvalide;
    private Boolean ciOuvert;
    private String codeMutation;
    private String idCI;
    private String nomPrenom;
    private String numeroAVS;

    @Override
    protected String _getTableName() {
        // non-persistent entity utilisée par CINSSRACompteIndividuelManager
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCI = statement.dbReadNumeric(CINSSRACompteIndividuel.COLUMN_NAME_ID_CI);
        numeroAVS = statement.dbReadString(CINSSRACompteIndividuel.COLUMN_NAME_NUMERO_AVS);
        nomPrenom = statement.dbReadString(CINSSRACompteIndividuel.COLUMN_NAME_NOM_PRENOM);
        codeMutation = statement.dbReadNumeric(CINSSRACompteIndividuel.COLUMN_NAME_CODE_MUTATION);
        ciInactif = statement.dbReadBoolean(CINSSRACompteIndividuel.COLUMN_NAME_CI_INACTIF);
        ciInvalide = statement.dbReadBoolean(CINSSRACompteIndividuel.COLUMN_NAME_CI_INVALIDE);
        ciOuvert = statement.dbReadBoolean(CINSSRACompteIndividuel.COLUMN_NAME_CI_OUVERT);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nothing todo

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nothing todo

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nothing todo

    }

    public Boolean getCiInactif() {
        return ciInactif;
    }

    public Boolean getCiInvalide() {
        return ciInvalide;
    }

    public Boolean getCiOuvert() {
        return ciOuvert;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public String getIdCI() {
        return idCI;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumeroAVS() {
        return numeroAVS;
    }

    public void setCiInactif(Boolean ciInactif) {
        this.ciInactif = ciInactif;
    }

    public void setCiInvalide(Boolean ciInvalide) {
        this.ciInvalide = ciInvalide;
    }

    public void setCiOuvert(Boolean ciOuvert) {
        this.ciOuvert = ciOuvert;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public void setIdCI(String idCI) {
        this.idCI = idCI;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNumeroAVS(String numeroAVS) {
        this.numeroAVS = numeroAVS;
    }

}
