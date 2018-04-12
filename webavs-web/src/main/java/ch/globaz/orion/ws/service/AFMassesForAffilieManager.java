package ch.globaz.orion.ws.service;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Manager permettant de récupérer la liste des cotisations paritaires d'un affilié
 * qui n'ont pas de date de fin ou la date de fin est supérieur a la date
 * passée en paramètre
 * 
 * @author sco
 */
public class AFMassesForAffilieManager extends BManager {

    private static final long serialVersionUID = -2278911471263722259L;

    private String numAffilie = "";
    private String dateFin = "";
    private String forMois = "";
    private String forAnnee = "";
    private boolean cotParitaire = true;
    private boolean cotPers = true;

    @Override
    protected String _getSql(BStatement statement) {

        StringBuilder sqlBuffer = new StringBuilder();

        sqlBuffer
                .append(" SELECT aff.MAIAFF, aff.MADESL, aff.malnaf, aff.MATPER, cot.MEICOT, cot.memmap, cot.meddeb, cot.medfin, ass.MBLLIF, ass.MBLLID, ass.MBLLII, ass.MBTTYP, ass.mbtcan, ass.mbirub, ass.MBTGEN");
        sqlBuffer.append(" FROM ").append(_getCollection()).append("AFAFFIP aff");
        sqlBuffer.append(" inner join ").append(_getCollection()).append("AFADHEP adh on adh.MAIAFF = aff.MAIAFF");
        sqlBuffer.append(" inner join ").append(_getCollection()).append("afcotip cot on cot.MRIADH = adh.MRIADH");
        sqlBuffer.append(" inner join ").append(_getCollection()).append("AFASSUP ass on ass.MBIASS = cot.MBIASS");
        sqlBuffer.append(" where aff.malnaf like '" + getNumAffilie() + "%'");

        if (JadeStringUtil.isEmpty(getDateFin())) {
            sqlBuffer.append(" and cot.MEDFIN = 0");
        } else {

            if (!JadeStringUtil.isEmpty(forMois) && !JadeStringUtil.isEmpty(forAnnee)) {

                sqlBuffer.append(" and ((cot.medfin = 0 and cot.MEDDEB <= " + forAnnee + forMois
                        + "01) or (cot.medfin between " + forAnnee + forMois + "01 and " + forAnnee + forMois
                        + "99 and cot.MEDDEB <= " + forAnnee + forMois + "01)) "
                        + this._dbWriteNumeric(statement.getTransaction(), getDateFin()) + ")");

            } else {

                sqlBuffer.append(" and (cot.MEDFIN = 0 OR cot.MEDFIN >= "
                        + this._dbWriteNumeric(statement.getTransaction(), getDateFin()) + ")");
            }
        }

        // uniquement cotisations paritaires
        if (cotParitaire && !cotPers) {
            sqlBuffer.append(" and ass.MBTGEN = 801001");
        }

        // uniquement cotisations personnelles
        if (cotPers && !cotParitaire) {
            sqlBuffer.append(" and ass.MBTGEN = 801002");
        }

        return sqlBuffer.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFMassesForAffilie();
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getForMois() {
        return forMois;
    }

    public void setForMois(String forMois) {
        this.forMois = StringUtils.leftPad(forMois, 2, "0");
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public boolean isCotParitaire() {
        return cotParitaire;
    }

    public void setCotParitaire(boolean cotParitaire) {
        this.cotParitaire = cotParitaire;
    }

    public boolean isCotPers() {
        return cotPers;
    }

    public void setCotPers(boolean cotPers) {
        this.cotPers = cotPers;
    }

}
