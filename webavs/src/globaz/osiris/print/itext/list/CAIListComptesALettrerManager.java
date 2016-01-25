package globaz.osiris.print.itext.list;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;

/**
 * Cette classe prépare le manager pour l'impression de la liste des soldes par section
 * 
 * @author: Sébastien Chappatte
 */
public class CAIListComptesALettrerManager extends CAIListSoldeSectionManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String aLettrer = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        sqlWhere = super._getWhere(statement);

        // traitement des cas à lettrer
        if (!JadeStringUtil.isBlank(getALettrer())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "((" + _getCollection() + "CASECTP.SOLDE > 0 AND " + _getCollection() + "CACPTAP.SOLDE < "
                    + _getCollection() + "CASECTP.SOLDE AND " + _getCollection() + "CACPTAP.SOLDE < 0) OR " + "("
                    + _getCollection() + "CASECTP.SOLDE < 0 AND " + _getCollection() + "CACPTAP.SOLDE > "
                    + _getCollection() + "CASECTP.SOLDE AND " + _getCollection() + "CACPTAP.SOLDE > 0) OR ("
                    + _getCollection() + "CASECTP.SOLDE <> 0 AND " + _getCollection() + "CACPTAP.SOLDE = 0))";
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASection();
    }

    /**
     * @return
     */
    public String getALettrer() {
        return aLettrer;
    }

    /**
     * @param string
     */
    public void setALettrer(String string) {
        aLettrer = string;
    }

}
