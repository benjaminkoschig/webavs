package ch.globaz.orion.ws.service.manager;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import org.apache.commons.lang.StringUtils;

public class AFReleveForSddManager extends BManager {

    private static final long serialVersionUID = 1L;

    private String forIdReleve;

    @Override
    protected String _getSql(BStatement statement) {

        StringBuilder sqlBuffer = new StringBuilder();

        sqlBuffer.append(" SELECT ");
        sqlBuffer.append(" fact.MONTANTFACTURE as MONTANTFACTURE,");
        sqlBuffer.append(" fact.MASSEFACTURE as MASSEFACTURE,");
        sqlBuffer.append(" fact.idRubrique as ID_RUBRIQUE");
        sqlBuffer.append(" FROM ");
        sqlBuffer.append(_getCollection()).append("AFREVEP rel ");
        sqlBuffer.append(" INNER JOIN ").append(_getCollection())
                .append("FAAFACP fact on fact.IDENTETEFACTURE = rel.MMEBID");
        sqlBuffer.append(" WHERE ");

        sqlBuffer.append(" mmirel = ");

        if (StringUtils.isNotBlank(getForIdReleve())) {
            sqlBuffer.append(getForIdReleve());
        }

        // --etat facturé et comptabilisé
        sqlBuffer.append(" and rel.MMETAT in (827002,827003) ");

        return sqlBuffer.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFReleveForSdd();
    }

    public String getForIdReleve() {
        return forIdReleve;
    }

    public void setForIdReleve(String forIdReleve) {
        this.forIdReleve = forIdReleve;
    }

}
