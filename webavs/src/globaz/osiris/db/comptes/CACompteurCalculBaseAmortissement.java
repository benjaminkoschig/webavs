package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CACompteurCalculBaseAmortissement extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeAmortissement = "";
    private String cumulCotisationsForAnnee = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setAnneeAmortissement(statement.dbReadNumeric("anneeAmortissement"));
        setCumulCotisationsForAnnee(statement.dbReadNumeric("cumulCotisationsForAnnee", 2));

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAnneeAmortissement() {
        return anneeAmortissement;
    }

    /**
     * @return the cumulCotisationsForAnnee
     */
    public String getCumulCotisationsForAnnee() {
        return cumulCotisationsForAnnee;
    }

    public void setAnneeAmortissement(String anneeAmortissement) {
        this.anneeAmortissement = anneeAmortissement;
    }

    /**
     * @param cumulCotisationsForAnnee
     *            the cumulCotisationsForAnnee to set
     */
    public void setCumulCotisationsForAnnee(String cumulCotisationsForAnnee) {
        this.cumulCotisationsForAnnee = cumulCotisationsForAnnee;
    }

}
