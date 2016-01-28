package globaz.helios.db.classifications;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGCompte;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Creation date: (30.06.2003 17:41:29)
 */
public class CGExtendedCompteManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String REPORT_BILAN = "REPORT_BILAN";
    public static final String REPORT_GENERAL = "REPORT_GENERAL";
    public static final String REPORT_PERTES_PROFITS = "REPORT_PERTES_PROFITS";
    protected String forNumeroCompteMax = new String();
    protected String forNumeroCompteMin = new String();
    protected String idDomaine;

    protected String idExerciceComptable;
    protected String idMandat;

    protected String idNature;

    protected String listeIdClasseCompte;
    protected String listeIdGenreCompte;
    protected String typeReport = CGExtendedCompteManager.REPORT_GENERAL;

    /**
     * CGExtendedCompteManager constructor comment.
     */
    public CGExtendedCompteManager() {
        super();
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection()
                + "CGCOMTP.idCompte, "
                + _getCollection()
                + "CGCOMTP.idDomaine, "
                + _getCollection()
                + "CGCOMTP.idNature, "
                + _getCollection()
                + "CGCOMTP.idGenre, "
                + _getCollection()
                + "CGCOMTP.idSecteurAvs, "
                + _getCollection()
                + "CGCOMTP.codeIsoMonnaie, "
                + _getCollection()
                + "CGCLCOP.idClasseCompte, CLSNIV2.noClasse AS noClasseNiv2, CLSNIV1.noClasse AS noClasseNiv1, "
                + "CLSNIV2.libelleFr AS LIBFRCLASSENIV2, CLSNIV2.libelleIt AS LIBITCLASSENIV2, CLSNIV2.libelleDe AS LIBDECLASSENIV2, "
                + "CLSNIV1.libelleFr AS LIBFRCLASSENIV1, CLSNIV1.libelleIt AS LIBITCLASSENIV1, CLSNIV1.libelleDe AS LIBDECLASSENIV1, "
                + _getCollection() + "CGPLANP.libelleFr AS LIBFRPLANCOMPTABLE, " + _getCollection()
                + "CGPLANP.libelleIt AS LIBITPLANCOMPTABLE, " + _getCollection()
                + "CGPLANP.libelleDe AS LIBDEPLANCOMPTABLE, " + _getCollection() + "CGPLANP.idExterne";
    }

    /**
     * Renvoie la liste des tables
     * 
     * @return la liste des tables
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = "";

        sqlFrom += _getCollection() + "CGCLCOP ";

        // jointure interne avec les classes de compte
        sqlFrom += " INNER JOIN " + _getCollection() + "CGCPGRP on " + _getCollection() + "CGCPGRP.idClasseCompte="
                + _getCollection() + "CGCLCOP.idClasseCompte";

        sqlFrom += " INNER JOIN " + _getCollection() + "CGCOMTP on " + _getCollection() + "CGCOMTP.idCompte="
                + _getCollection() + "CGCPGRP.idCompte";

        sqlFrom += " INNER JOIN " + _getCollection() + "CGPLANP on " + _getCollection() + "CGCOMTP.idCompte="
                + _getCollection() + "CGPLANP.idCompte";

        // jointure interne avec les exercices comptables
        sqlFrom += " INNER JOIN " + _getCollection() + "CGEXERP on " + _getCollection() + "CGEXERP.idExerComptable="
                + _getCollection() + "CGPLANP.idExerComptable";

        // jointure externe sur la classe de niveau 2 attachée
        sqlFrom += " LEFT OUTER JOIN " + _getCollection() + "CGCLCOP AS CLSNIV2 on " + _getCollection()
                + "CGCLCOP.idSuperClasse=CLSNIV2.idClasseCompte ";

        // jointure externe sur la classe de niveau 1 attachée
        sqlFrom += " LEFT OUTER JOIN " + _getCollection()
                + "CGCLCOP AS CLSNIV1 on CLSNIV2.idSuperClasse=CLSNIV1.idClasseCompte ";

        return sqlFrom;
    }

    /**
     * Renvoie la composante de tri de la requête SQL (sans le mot-clé ORDER BY)
     * 
     * @return la composante ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        /**
         * On trie sur la classe de compte principale puis sur la secondaire. On se base sur le libellé (qui devrait
         * toujours être présent) pour le niveau primaire
         */
        return "NOCLASSENIV1, NOCLASSENIV2, " + _getCollection() + "CGPLANP.idExterne";
    }

    /**
     * retourne la clause WHERE de la requete SQL + jointure
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Seuls les comptes du domaine spécifié nous intéressent
        if (!JadeStringUtil.isIntegerEmpty(idDomaine)) {
            sqlWhere += _getCollection() + "CGCOMTP.idDomaine=" + idDomaine;
        }

        // Seuls les comptes de la nature spécifiés nous intéressent
        if (!JadeStringUtil.isIntegerEmpty(idNature)) {
            sqlWhere += _getCollection() + "CGCOMTP.idNature=" + idNature;
        }

        if (!JadeStringUtil.isBlank(getTypeReport())) {
            if (getTypeReport().equals(CGExtendedCompteManager.REPORT_BILAN)) {
                if (!sqlWhere.equals("")) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CGCOMTP.idDomaine=" + globaz.helios.db.comptes.CGCompte.CS_COMPTE_BILAN;
            } else if (getTypeReport().equals(CGExtendedCompteManager.REPORT_PERTES_PROFITS)) {
                if (!sqlWhere.equals("")) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CGCOMTP.idDomaine<>" + CGCompte.CS_COMPTE_BILAN;
            }
        }

        // Si l'identifiant d'exercice est nul, vide ou égal à zero on ne filtre
        // pas
        if (!JadeStringUtil.isIntegerEmpty(idExerciceComptable)) {
            if (!sqlWhere.equals("")) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGPLANP.idExerComptable=" + idExerciceComptable;
        }

        // Si l'identifiant de mandat est nul, vide ou égal à zero on ne filtre
        // pas
        if (!JadeStringUtil.isIntegerEmpty(idMandat)) {
            if (!sqlWhere.equals("")) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGCOMTP.idMandat=" + idMandat;
        }

        // Si la chaine d'identifiants de classe de compte est nulle ou vide on
        // ne filtre pas
        if (!JadeStringUtil.isBlank(listeIdClasseCompte)) {
            if (!sqlWhere.equals("")) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (";
            java.util.StringTokenizer parser = new java.util.StringTokenizer(listeIdClasseCompte);
            boolean first = true;
            while (parser.hasMoreTokens()) {
                if (!first) {
                    sqlWhere += " OR ";
                } else {
                    first = false;
                }
                sqlWhere += _getCollection() + "CGCLCOP.idClasseCompte=" + parser.nextToken();

            }
            sqlWhere += ")";
        }

        // Si la chaine d'identifiants de classe de compte est nulle ou vide on
        // ne filtre pas
        if ((getListeIdGenreCompte() != null) && !JadeStringUtil.isBlank(getListeIdGenreCompte())) {
            if (!sqlWhere.equals("")) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (";
            java.util.StringTokenizer parser = new java.util.StringTokenizer(getListeIdGenreCompte());
            boolean first = true;
            while (parser.hasMoreTokens()) {
                if (!first) {
                    sqlWhere += " OR ";
                } else {
                    first = false;
                }
                sqlWhere += _getCollection() + "CGCOMTP.idGenre=" + parser.nextToken();

            }
            sqlWhere += ")";
        }

        if ((getForNumeroCompteMin() != null) && (getForNumeroCompteMin().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGPLANP.idExterne >= '" + getForNumeroCompteMin() + "' ";
            }
        }
        if ((getForNumeroCompteMax() != null) && (getForNumeroCompteMax().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND " + _getCollection() + "CGPLANP.idExterne <= '" + getForNumeroCompteMax() + "' ";
            }
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception Exception
     *                si la création a échouée
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExtendedCompte();
    }

    /**
     * Returns the forNumeroCompteMax.
     * 
     * @return String
     */
    public String getForNumeroCompteMax() {
        return forNumeroCompteMax;
    }

    /**
     * Returns the forNumeroCompteMin.
     * 
     * @return String
     */
    public String getForNumeroCompteMin() {
        return forNumeroCompteMin;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:50:26)
     * 
     * @return String
     */
    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 09:26:27)
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 09:25:59)
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idNature.
     * 
     * @return String
     */
    public String getIdNature() {
        return idNature;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 09:42:55)
     * 
     * @return String
     */
    public String getListeIdClasseCompte() {
        return listeIdClasseCompte;
    }

    /**
     * Returns the listeIdGenreCompte.
     * 
     * @return String
     */
    public String getListeIdGenreCompte() {
        return listeIdGenreCompte;
    }

    /**
     * Returns the typeReport.
     * 
     * @return String
     */
    public String getTypeReport() {
        return typeReport;
    }

    /**
     * Sets the forNumeroCompteMax.
     * 
     * @param forNumeroCompteMax
     *            The forNumeroCompteMax to set
     */
    public void setForNumeroCompteMax(String forNumeroCompteMax) {
        this.forNumeroCompteMax = forNumeroCompteMax;
    }

    /**
     * Sets the forNumeroCompteMin.
     * 
     * @param forNumeroCompteMin
     *            The forNumeroCompteMin to set
     */
    public void setForNumeroCompteMin(String forNumeroCompteMin) {
        this.forNumeroCompteMin = forNumeroCompteMin;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:50:26)
     * 
     * @param newIdDomaine
     *            String
     */
    public void setIdDomaine(String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 09:26:27)
     * 
     * @param newIdExerciceComptable
     *            String
     */
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 09:25:59)
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Sets the idNature.
     * 
     * @param idNature
     *            The idNature to set
     */
    public void setIdNature(String idNature) {
        this.idNature = idNature;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 09:42:55)
     * 
     * @param newListeIdClasseCompte
     *            String
     */
    public void setListeIdClasseCompte(String newListeIdClasseCompte) {
        listeIdClasseCompte = newListeIdClasseCompte;
    }

    /**
     * Sets the listeIdGenreCompte. liste des id de genre de compte, séparé par des espaces.
     * 
     * @param listeIdGenreCompte
     *            The listeIdGenreCompte to set
     */
    public void setListeIdGenreCompte(String listIdGenreCompte) {
        listeIdGenreCompte = listIdGenreCompte;
    }

    /**
     * Sets the typeReport.
     * 
     * @param typeReport
     *            The typeReport to set
     */
    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

}
