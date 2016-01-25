package globaz.osiris.db.comptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.ordres.CAOrdreVersement;

public class CAOperationOrdreRecouvrementManager extends CAOperationOrdreManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String forCodeISOPays = "";

    // TODO A supprimer si pas de problème lors du traitement des ordres de
    // recouvrement
    /*
     * protected String _getOrder(BStatement statement) { return CAOrdreGroupe.FIELD_IDORDREGROUPE + ", " +
     * CAOperation.FIELD_DATE + ", " + CAOrdreRecouvrement.FIELD_NOMCACHE; }
     */

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        if (JadeStringUtil.isIntegerEmpty(getForIdTypeOperation())
                && JadeStringUtil.isIntegerEmpty(getLikeIdTypeOperation())) {
            setForIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);
        }

        String sqlWhere = super._getWhere(statement);

        if (getForCodeISOPays().length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += CAOrdreVersement.FIELD_CODEISOPAYS + "="
                    + this._dbWriteString(statement.getTransaction(), getForCodeISOPays());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAOperationOrdreRecouvrement();
    }

    /**
     * @return
     */
    public String getForCodeISOPays() {
        return forCodeISOPays;
    }

    /**
     * @param string
     */
    public void setForCodeISOPays(String string) {
        forCodeISOPays = string;
    }

}
