package globaz.naos.db.statOfas;

import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFInfoRom060StatistiquesOfasCotisationMinimumManager extends AFInfoRom060StatistiquesOfasCotisationManager
        implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Attributs
     */
    private String forMontantMinime = "";

    /**
     * Méthodes
     */
    @Override
    protected String _getHaving() {

        StringBuffer sqlHaving = new StringBuffer();

        sqlHaving.append("HAVING SUM(MONTANT)>0");

        if (!JadeStringUtil.isBlankOrZero(getForMontantMinime())) {
            sqlHaving.append(" AND SUM(MONTANT) <=" + getForMontantMinime());
        }

        return sqlHaving.toString();

    }

    /**
     * Getters
     */
    public String getForMontantMinime() {
        return forMontantMinime;
    }

    /**
     * Setters
     */
    public void setForMontantMinime(String newForMontantMinime) {
        forMontantMinime = newForMontantMinime;
    }

}
