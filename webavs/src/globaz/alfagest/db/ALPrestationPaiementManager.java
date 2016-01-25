package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Créé le 20 janv. 06
 * 
 * @author dch
 * 
 *         Manager pour les prestations de paiement (ALPrestationPaiement)
 */
public class ALPrestationPaiementManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // numéro de facture
    private String numeroFacture = null;
    // type de cotisation => 0 = paritaire et personnelle
    private int typeCotisation = 0;

    /**
     * Crée une nouvelle entité.
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception si la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALPrestationPaiement();
    }

    /**
     * Renvoie la liste des champs.
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "LID, LNOFA, LPERD, LPERA, MALNAF, Q02.HTITIE, HTLDE1, HTLDE2, ETYPAL, A7CAT, A7CHST, LNOAF, SUM(NMONT+NMSUP) as TOTMONT";
    }

    /**
     * Renvoie la clause FROM (par défaut, la clause FROM de l'entité).
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "JAFPRCP INNER JOIN " + _getCollection() + "AFAFFIP AS Q01 ON (LNOAF = MAIAFF)"
                + " INNER JOIN " + _getCollection() + "TITIERP AS Q02 ON (Q01.HTITIE = Q02.HTITIE)" + " INNER JOIN "
                + _getCollection() + "JAFPEPR ON (LID = MIDREC)" + " INNER JOIN " + _getCollection()
                + "JAFPHPR ON (MID = NIDE)" + " INNER JOIN " + _getCollection() + "JAFPDOS ON (MIDD= EID)"
                + " INNER JOIN " + _getCollection() + "JAFPRUBR ON (NID = A7IDDT)";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL (sans le mot-clé WHERE).
     * 
     * @return la composante WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        return where(statement) + groupBy() + orderBy();
    }

    /**
     * @return
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * @param string
     */
    public void setNumeroFacture(String string) {
        numeroFacture = string;
    }

    /**
     * Définit le type de cotisation
     * 
     * @param newType
     */
    public void setTypeCotisation(int newType) {
        typeCotisation = newType;
    }

    /**
     * Renvoye la partie GROUP BY de la requête
     */
    private String groupBy() {
        return " GROUP BY LID, LNOFA, LPERD, LPERA, MALNAF, Q02.HTITIE, HTLDE1, HTLDE2, ETYPAL, A7CAT, A7CHST, LNOAF";
    }

    /**
     * Renvoye la partie ORDER BY de la requête
     */
    private String orderBy() {
        return " ORDER BY MALNAF, LID, A7CHST";
    }

    /**
     * Renvoye la partie WHERE de la requête
     */
    private String where(BStatement statement) {
        String where = "LETAT = 'TR' AND LID<>99999999 AND METAT = 'TR' AND A7CAT = 'COMPTA' AND LBONI = 'I' AND MBONI='I'";

        String annee = numeroFacture.substring(0, 4);
        String mois = numeroFacture.substring(4, 6);

        /*
         * if(mois.equals("03") || mois.equals("04") || mois.equals("05"))
         * {
         * where += " AND (LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), numeroFacture) +
         * " OR (LNOFA >= " + _dbWriteNumeric(statement.getTransaction(), annee + "41000") +
         * " AND LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), annee + "41999") + "))";
         * }
         * else if(mois.equals("06") || mois.equals("07") || mois.equals("08"))
         * {
         * where += " AND (LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), numeroFacture) +
         * " OR (LNOFA >= " + _dbWriteNumeric(statement.getTransaction(), annee + "41000") +
         * " AND LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), annee + "42999") + "))";
         * }
         * else if(mois.equals("09") || mois.equals("10") || mois.equals("11"))
         * {
         * where += " AND (LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), numeroFacture) +
         * " OR (LNOFA >= " + _dbWriteNumeric(statement.getTransaction(), annee + "41000") +
         * " AND LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), annee + "43999") + "))";
         * }
         * else if(mois.equals("12"))
         * {
         * where += " AND (LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), numeroFacture) +
         * " OR (LNOFA >= " + _dbWriteNumeric(statement.getTransaction(), annee + "40000") +
         * " AND LNOFA <= " + _dbWriteNumeric(statement.getTransaction(), annee + "44999") + "))";
         * }
         * else
         */
        where += " AND LPERA <= " + numeroFacture;
        // selon le type de cotisation choisi, on ajoute la clause
        if (typeCotisation == 1) {
            where += " AND (ETYPAL ='I' OR ETYPAL = 'N' OR ETYPAL = 'E')";
        } else if (typeCotisation == 2) {
            where += " AND (ETYPAL!='I' AND ETYPAL!='N' AND ETYPAL!='E')";
        }
        return where;
    }
}