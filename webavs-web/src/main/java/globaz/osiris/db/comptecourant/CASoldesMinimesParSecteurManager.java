/*
 * Créé le 5 juin 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.io.Serializable;

/**
 * @author SPA
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CASoldesMinimesParSecteurManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteAnnexe = new String();
    private String forIdTypeSection = new String();
    private String forMontantMinime = "1";

    /**
     * retourne les champs de la requête SQL
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + "CACPTAP.idrole," + _getCollection() + "CACPTAP.idexternerole," + _getCollection()
                + "CACPTAP.idcompteannexe," + _getCollection() + "CAOPERP.idsection," + _getCollection()
                + "CAOPERP.idcomptecourant,sum(montant) AS MONTANT";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CAOPERP INNER JOIN " + _getCollection() + "CACPTAP ON " + _getCollection()
                + "CAOPERP.IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE INNER JOIN "
                + _getCollection() + "CASECTP ON " + _getCollection() + "CAOPERP.IDSECTION=" + _getCollection()
                + "CASECTP.IDSECTION";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CACPTAP.IDROLE, " + _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection()
                + "CAOPERP.IDSECTION";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // Condition de base
        String sqlWhere = "IDTYPEOPERATION LIKE 'E%' AND ETAT="
                + this._dbWriteNumeric(statement.getTransaction(), APIOperation.ETAT_COMPTABILISE);
        // Id compte annexe renseigné
        if (!JadeStringUtil.isIntegerEmpty(getForIdCompteAnnexe())) {
            sqlWhere = sqlWhere + " AND " + _getCollection() + "CAOPERP." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }
        // Type de section renseigné
        if (!JadeStringUtil.isIntegerEmpty(getForIdTypeSection())) {
            sqlWhere = sqlWhere + " AND " + _getCollection() + "CASECTP." + CASection.FIELD_IDTYPESECTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection());
        }
        sqlWhere = sqlWhere + " GROUP BY " + _myGetGroupBy(statement);
        return sqlWhere;
    }

    /**
     * retourne la clause GROUP BY de la requete SQL
     */
    protected String _myGetGroupBy(BStatement statement) {
        String groupBy = _getCollection() + "CACPTAP.idrole," + _getCollection() + "CACPTAP.idexternerole,"
                + _getCollection() + "CACPTAP.idcompteannexe," + _getCollection() + "CAOPERP.idsection,"
                + _getCollection() + "CAOPERP.idcomptecourant";
        String having = "HAVING SUM(" + _getCollection() + "CAOPERP.MONTANT) <> 0 AND SUM(" + _getCollection()
                + "CAOPERP.MONTANT) BETWEEN -" + getForMontantMinime() + " AND " + getForMontantMinime();
        return groupBy + " " + having;
    }

    /**
     * Retourne l'entity qui charge les informations
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASoldesMinimesParSecteur();
    }

    /**
     * @return
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return
     */
    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * @return
     */
    public String getForMontantMinime() {
        return forMontantMinime;
    }

    /**
     * @param string
     */
    public void setForIdCompteAnnexe(String string) {
        forIdCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setForIdTypeSection(String string) {
        forIdTypeSection = string;
    }

    /**
     * @param string
     */
    public void setForMontantMinime(String string) {
        forMontantMinime = string;
    }

}
