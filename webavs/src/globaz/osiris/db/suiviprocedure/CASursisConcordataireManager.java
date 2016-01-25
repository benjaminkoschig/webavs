package globaz.osiris.db.suiviprocedure;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CASursisConcordataireManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateSursisConcordataire;
    private String forIdCompteAnnexe;

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table).
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return CASursisConcordataire.FIELD_DATE_SURSIS_CONCORDATAIRE + " DESC ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "";

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += CAFaillite.FIELD_ID_COMPTEANNEXE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        if (!JadeStringUtil.isBlank(getForDateSursisConcordataire())) {
            if (where.length() != 0) {
                where += " AND ";
            }

            where += CASursisConcordataire.FIELD_DATE_SURSIS_CONCORDATAIRE + " = "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateSursisConcordataire());
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASursisConcordataire();
    }

    public String getForDateSursisConcordataire() {
        return forDateSursisConcordataire;
    }

    /**
     * @return the forIdCompteAnnexe
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public void setForDateSursisConcordataire(String forDateSursisConcordataire) {
        this.forDateSursisConcordataire = forDateSursisConcordataire;
    }

    /**
     * @param forIdCompteAnnexe
     *            the forIdCompteAnnexe to set
     */
    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }
}
