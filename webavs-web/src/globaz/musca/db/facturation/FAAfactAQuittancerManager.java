/*
 * Créé le 17 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class FAAfactAQuittancerManager extends FAAfactManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAQuittancerString;

    @Override
    protected String _getFields(BStatement statement) {
        return "FAAFACP.IDAFACT, FAAFACP.IDENTETEFACTURE, FAAFACP.IDPASSAGE, "
                + "FAAFACP.IDREMARQUE, FAAFACP.IDRUBRIQUE, FAAFACP.AQUITTANCER, FAAFACP.IDEXTFACCOM, "
                + "FAAFACP.MONTANTFACTURE, FAAFACP.MASSEFACTURE, FAAFACP.PSPY, "
                + "TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP.IDEXTERNEROLE, FAENTFP.IDEXTERNEFACTURE, PMTRADP.LIBELLE AS LIBELLERUB, CARUBRP.IDEXTERNE, FAREMAP.TEXTE";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy().length() != 0) {
            if (getOrderBy().equals(FAEnteteFacture.CS_TRI_DEBITEUR)) {
                return "IDEXTERNEROLE";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_NUMERO_DECOMTPE)) {
                return "IDEXTERNEFACTURE";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_NOM)) {
                return "HTLDE1, HTLDE2";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_MONTANT)) {
                return "MONTANTFACTURE";
            } else {
                return getOrderBy();
            }
        }
        // Tri par défaut
        return "";
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        if (getForAQuittancerString().equals("aValider")) {
            super.setForAQuittancer(new Boolean(true));
        } else if (getForAQuittancerString().equals("aRefuser")) {
            super.setForAQuittancer(new Boolean(false));
        } else {
            super.setForAQuittancer(null);
        }

        // composant de la requete initialises avec les options par defaut
        String sqlWhere = super._getWhere(statement);

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "(FAAFACP.IDTYPEAFACT="
                + this._dbWriteNumeric(statement.getTransaction(), FAAfact.CS_AFACT_COMPENSATION);
        sqlWhere += " OR FAAFACP.IDTYPEAFACT="
                + this._dbWriteNumeric(statement.getTransaction(), FAAfact.CS_AFACT_COMPENSATION_INTERNE) + ")";

        return sqlWhere;
    }

    public String getForAQuittancerString() {
        return forAQuittancerString;
    }

    public void setForAQuittancerString(String forAQuittancerString) {
        this.forAQuittancerString = forAQuittancerString;
    }

}
