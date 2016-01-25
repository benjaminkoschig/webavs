package ch.globaz.orion.ws.service;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class AFNombreCotisationForTypeManager extends BManager {

    private static final long serialVersionUID = 1L;
    private String forNumAffilie = null;
    private String forTypeCotisation = null;
    private String forPeriodeDebut = null;
    private String forPeriodeFin = null;

    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder str = new StringBuilder(
                " (SELECT cot.meddeb, CASE cot.medfin WHEN 0 THEN 99999999 ELSE cot.medfin end as MEDFIN FROM ");
        str.append(_getCollection()).append("AFAFFIP aff ");
        str.append("inner join ").append(_getCollection()).append("AFPLAFP pla on pla.MAIAFF = aff.maiaff ");
        str.append("inner join ").append(_getCollection()).append("AFCOTIP cot on cot.MUIPLA = pla.MUIPLA ");
        str.append("inner join ").append(_getCollection()).append("AFASSUP ass on ass.MBIASS = cot.MBIASS ");

        return str.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder str = new StringBuilder();

        str.append(" aff.malnaf = '" + forNumAffilie + "'");
        str.append(" and ass.mbttyp = " + forTypeCotisation + ") AS TEMP ");

        if (!JadeStringUtil.isEmpty(forPeriodeDebut) && !JadeStringUtil.isEmpty(forPeriodeFin)) {
            str.append(" where MEDDEB < ");
            str.append(_dbWriteDateAMJ(statement.getTransaction(), forPeriodeFin));
            str.append(" and MEDFIN >  ");
            str.append(_dbWriteDateAMJ(statement.getTransaction(), forPeriodeDebut));
        }
        return str.toString();
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setForTypeCotisation(String forTypeCotisation) {
        this.forTypeCotisation = forTypeCotisation;
    }

}
