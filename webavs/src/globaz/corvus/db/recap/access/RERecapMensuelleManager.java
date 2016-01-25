package globaz.corvus.db.recap.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:34:32 CET 2007
 */
public class RERecapMensuelleManager extends BManager {
    /** Table : RERECMEN */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Pour csEtat - cs état récap mensuelle est = à ... (ZRTETA) */
    private String forCsEtat = new String();

    /**
     * Pour dateRapportMensuel - date du rapport mensuel (MMxAAAA) est = à ... (ZRDREC)
     */
    private String forDateRapportMensuel = new String();

    /** Pour idRecapMensuelle - id récap mensuelle est = à ... (ZRIRM) */
    private String forIdRecapMensuelle = new String();

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String "RERECMEN" (Model : RERecapMensuelle)
     * @param statement
     *            de type BStatement
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return new StringBuffer(_getCollection()).append(IRERecapMensuelleDefTable.TABLE_NAME).toString();
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param statement
     *            de type BStatement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param statement
     *            BStatement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        /* composant de la requete initialises avec les options par defaut */
        StringBuffer sqlWhere = new StringBuffer();
        // traitement du positionnement
        if (getForIdRecapMensuelle().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapMensuelleDefTable.ID_RECAP_MENSUELLE).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForIdRecapMensuelle()));
        }
        // traitement du positionnement
        if (getForDateRapportMensuel().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapMensuelleDefTable.DATE_RAPPORT_MENSUEL).append("=")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateRapportMensuel()));
        }
        // traitement du positionnement
        if (getForCsEtat().length() != 0) {
            if (sqlWhere.toString().length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(IRERecapMensuelleDefTable.CS_ETAT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERecapMensuelle();
    }

    /**
     * Renvoie forCsEtat;
     * 
     * @return String csEtat - cs état récap mensuelle;
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * Renvoie forDateRapportMensuel;
     * 
     * @return String dateRapportMensuel - date rapport mensuel (MMxAAAA);
     */
    public String getForDateRapportMensuel() {
        return forDateRapportMensuel;
    }

    /**
     * Renvoie forIdRecapMensuelle;
     * 
     * @return String idRecapMensuelle - id récap mensuelle;
     */
    public String getForIdRecapMensuelle() {
        return forIdRecapMensuelle;
    }

    /**
     * Sélection par forCsEtat
     * 
     * @param newForCsEtat
     *            String - cs état récap mensuelle
     */
    public void setForCsEtat(String newForCsEtat) {
        forCsEtat = newForCsEtat;
    }

    /**
     * Sélection par forDateRapportMensuel
     * 
     * @param newForDateRapportMensuel
     *            String - date rapport mensuel (MMxAAAA)
     */
    public void setForDateRapportMensuel(String newForDateRapportMensuel) {
        forDateRapportMensuel = newForDateRapportMensuel;
    }

    /**
     * Sélection par forIdRecapMensuelle
     * 
     * @param newForIdRecapMensuelle
     *            String - id récap mensuelle
     */
    public void setForIdRecapMensuelle(String newForIdRecapMensuelle) {
        forIdRecapMensuelle = newForIdRecapMensuelle;
    }

}
