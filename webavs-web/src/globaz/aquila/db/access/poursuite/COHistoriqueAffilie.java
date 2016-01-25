package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * @author SCO
 * @since SCO 11 juin 2010
 */
public class COHistoriqueAffilie extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEtape;
    private String idRole;
    private String libelleEtape;
    private String numAffilie;

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAffilie = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        idRole = statement.dbReadString(CACompteAnnexe.FIELD_IDROLE);
        idEtape = statement.dbReadString(ICOEtapeConstante.FNAME_ID_ETAPE);
        libelleEtape = statement.dbReadString(ICOEtapeConstante.FNAME_LIBETAPE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdEtape() {
        return idEtape;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getLibelleEtape() {
        return libelleEtape;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdEtape(String idEtape) {
        this.idEtape = idEtape;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setLibelleEtape(String libelleEtape) {
        this.libelleEtape = libelleEtape;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }
}
