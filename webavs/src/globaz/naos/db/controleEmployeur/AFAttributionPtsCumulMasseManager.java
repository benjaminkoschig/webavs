package globaz.naos.db.controleEmployeur;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class AFAttributionPtsCumulMasseManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forNumAffilie = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        try {
            return _getCollection()
                    + "AFAFFIP AF "
                    + "INNER JOIN "
                    + _getCollection()
                    + "AFPLAFP PL ON(AF.MAIAFF=PL.MAIAFF AND PL.MUBINA="
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR) + ")  " + "INNER JOIN " + _getCollection()
                    + "AFCOTIP CO ON(PL.MUIPLA=CO.MUIPLA)  " + "INNER JOIN " + _getCollection()
                    + "AFASSUP ASS ON (CO.MBIASS=ASS.MBIASS AND CO.MEBMER IN(2,0) AND MBTGEN = "
                    + CodeSystem.GENRE_ASS_PARITAIRE + ")  " + "INNER JOIN " + _getCollection()
                    + "CACPTAP CA ON (AF.HTITIE=CA.IDTIERS AND AF.MALNAF=CA.IDEXTERNEROLE AND CA.IDROLE = "
                    + CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(getSession().getApplication()) + ")"
                    + "INNER JOIN " + _getCollection()
                    + "CACPTRP CR ON (CA.IDCOMPTEANNEXE=CR.IDCOMPTEANNEXE AND CR.IDRUBRIQUE=ASS.MBIRUB)";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return " MBTTYP ASC ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        return "malnaf = '" + getForNumAffilie() + "' and annee=" + getForAnnee() + " and (medfin = 0 or medfin >"
                + getForAnnee() + "0101) and meddeb <= " + getForAnnee() + "1231";
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAttributionPtsCumulMasse();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }
}
