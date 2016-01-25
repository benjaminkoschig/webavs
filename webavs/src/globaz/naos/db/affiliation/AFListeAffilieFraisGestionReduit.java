package globaz.naos.db.affiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.naos.db.cotisation.AFCotisation;
import java.io.Serializable;

public class AFListeAffilieFraisGestionReduit extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFCotisation cotisation = new AFCotisation();
    private String genreAssurance = "";
    private String idCotisation = "";
    private String nomAffilieFirstPart = "";
    private String nomAffilieSecondPart = "";
    private String nombreSommationOrNombrePoursuite = "";
    private String numeroAffilie = "";
    private String typeEtapeContentieux = "";

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        cotisation.setCotisationId(getIdCotisation());
        cotisation.setSession(getSession());
        cotisation.retrieve(transaction);
    }

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getTableName() {
        // Pas de table
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numeroAffilie = statement.dbReadString("MALNAF");
        nomAffilieFirstPart = statement.dbReadString("HTLDE1");
        nomAffilieSecondPart = statement.dbReadString("HTLDE2");
        idCotisation = statement.dbReadNumeric("MEICOT");
        genreAssurance = statement.dbReadNumeric("MBTGEN");
        typeEtapeContentieux = statement.dbReadNumeric("ODTETA");
        nombreSommationOrNombrePoursuite = statement.dbReadNumeric("NOMBRESOMMATIONPOURSUITE");

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // On ne fait rien

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // On ne fait rien

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // On ne fait rien

    }

    public AFCotisation getCotisation() {
        return cotisation;
    }

    public String getGenreAssurance() {
        return genreAssurance;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public String getNomAffilieFirstPart() {
        return nomAffilieFirstPart;
    }

    public String getNomAffilieSecondPart() {
        return nomAffilieSecondPart;
    }

    public String getNombreSommationOrNombrePoursuite() {
        return nombreSommationOrNombrePoursuite;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getTypeEtapeContentieux() {
        return typeEtapeContentieux;
    }

}
