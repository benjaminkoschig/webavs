package globaz.pavo.db.compte;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (28.01.2003 10:27:17)
 * 
 * @author: David Girardin
 */
public class CIEcrituresSomme extends globaz.globall.db.BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatEcritures = new String();
    private String forAllEcritures = new String();
    private String forAnnee = new String();
    private String forCodeAmortissement = new String();
    private String forCodeSpecial = new String();
    private String forCompteIndividuelId = new String();
    private String forEmployeur = new String();
    private String forGenre = new String();
    private String forGenreCotPers = "false";
    private String forIdAffilie = "";
    private String forIdJournal = new String();
    private String forMasseSalariale = new String();
    private String forNotCodeAmortissement = "";
    private String forNotEcriture = "";
    private Boolean forNotType = new Boolean(false);
    private String forNumeroAffilie = "";
    private String forNumeroAffilieAllEcritures = "";
    private Boolean nonActif = null;

    /**
     * Commentaire relatif au constructeur CIEcrituresSommes.
     */
    public CIEcrituresSomme() {
        super();
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();
        String firstUnion = " (select KBIECR,KBMMON from " + _getCollection() + _getTableName();
        String joinUnion = " ec join " + _getCollection() + "AFAFFIP af on ec.kbitie = af.maiaff";
        // Ajouter la joint uniquement si on fait une recherche sur malnaf
        if (!JadeStringUtil.isBlank(getForNumeroAffilie())) {
            firstUnion = firstUnion + joinUnion;
        }
        String secondUnion = " AND KBTEXT IN (0,311006,311002,311008) union select KBIECR,-KBMMON AS KBMMON from "
                + _getCollection() + _getTableName();
        String endUnion = " AND KBTEXT IN (311001,311003,311004,311005,311007,311009)) as RESULT";
        if (!JadeStringUtil.isBlank(getForNumeroAffilie())) {
            secondUnion = secondUnion + joinUnion;
        }
        String whereUnion = "";
        // Récupération du CI, du tiers/partenaire
        // Obligation de renommée la table CIINDIP pour requête sur le
        // partenaire
        if ((forIdJournal != null) && (forIdJournal.length() != 0)) {
            whereUnion = " where KCID=" + forIdJournal;
            whereUnion += " AND KBTCPT <> " + CIEcriture.CS_CORRECTION;
        }
        if ((forCompteIndividuelId != null) && (forCompteIndividuelId.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KAIIND=" + forCompteIndividuelId;
            // filter des écriture au CI
            if ((forAllEcritures != null) && (forAllEcritures.length() != 0)) {
                // toutes les écritures sauf correction et suspens supprimés
                whereUnion += " AND KBTCPT NOT IN (" + CIEcriture.CS_CI_SUSPENS_SUPPRIMES + ","
                        + CIEcriture.CS_CORRECTION + ")";
            } else {
                // écritures au CI
                whereUnion += " AND KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + ","
                        + CIEcriture.CS_GENRE_7 + ")";
            }
        }

        if (forNotType.booleanValue()) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            // filter des écriture au CI
            if ((forAllEcritures != null) && (forAllEcritures.length() != 0)) {
                // toutes les écritures sauf correction et suspens supprimés
                whereUnion += "KBTCPT NOT IN (" + CIEcriture.CS_CI_SUSPENS_SUPPRIMES + "," + CIEcriture.CS_CORRECTION
                        + ")";
            } else {
                // écritures au CI
                whereUnion += "KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + ","
                        + CIEcriture.CS_GENRE_7 + ")";
            }
        }

        if ((etatEcritures != null) && (etatEcritures.length() != 0)) {

            // Si etat == toutesEcritures --> toutes les ecritures, actives et
            // non actives -> bypass
            if (!"toutesEcritures".equals(etatEcritures)) {
                // clause pour les clôtures
                if (whereUnion.length() != 0) {
                    whereUnion += " AND ";
                } else {
                    whereUnion = " where ";
                }

                // Etats tous || seulementClotures -> toutes les écritures de
                // clotures
                if ("tous".equals(etatEcritures) || "seulementClotures".equals(etatEcritures)) {
                    // toutes écritures clôturées
                    whereUnion += "KKIRAO<>0";
                } else if ("active".equals(etatEcritures)) {
                    // toutes les écriture actives
                    whereUnion += "KKIRAO=0";
                } else {
                    // écritures d'une clôture
                    whereUnion += "KKIRAO=" + etatEcritures;
                }
            }
        }
        if ((forAnnee != null) && (forAnnee.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KBNANN=" + forAnnee;
        }
        if ((forEmployeur != null) && (forEmployeur.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KBITIE=" + forEmployeur;
        }
        if ((forMasseSalariale != null) && (forMasseSalariale.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            // filter des écriture au CI
            whereUnion += " KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + "," + CIEcriture.CS_GENRE_7
                    + "," + CIEcriture.CS_CI_SUSPENS + ")";
            whereUnion += " AND (KBTGEN =" + CIEcriture.CS_CIGENRE_1 + " OR KBTGEN =" + CIEcriture.CS_CIGENRE_6
                    + " OR(KBTGEN=" + CIEcriture.CS_CIGENRE_7 + " AND KBTSPE=" + CIEcriture.CS_NONFORMATTEUR_SALARIE
                    + ")) ";

        }
        if ((forGenre != null) && (forGenre.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBTGEN = " + forGenre + " ";

        }
        if ((forCodeSpecial != null) && (forCodeSpecial.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBTSPE = " + forCodeSpecial + " ";

        }
        if ((forCodeAmortissement != null) && (forCodeAmortissement.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBTCOD = " + forCodeAmortissement + " ";

        }
        if ((forIdAffilie != null) && (forIdAffilie.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " kbitie = " + forIdAffilie + " ";

        }
        if ((forNotCodeAmortissement != null) && (forNotCodeAmortissement.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBTCOD not in (" + forNotCodeAmortissement + ") ";

        }
        if ((forGenreCotPers != null) && forGenreCotPers.equals("true")) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " (KBTGEN IN (" + CIEcriture.CS_CIGENRE_3 + ", " + CIEcriture.CS_CIGENRE_4 + ", "
                    + CIEcriture.CS_CIGENRE_9 + ", " + CIEcriture.CS_CIGENRE_2 + ")";
            whereUnion += " OR KBTGEN =  " + CIEcriture.CS_CIGENRE_7 + " and KBTSPE IN (312002,312001,0,312004))";

        }
        if ((forNumeroAffilie != null) && (forNumeroAffilie.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumeroAffilie());

        }
        if ((forNumeroAffilieAllEcritures != null) && (forNumeroAffilieAllEcritures.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumeroAffilie());

            if ((forAllEcritures != null) && (forAllEcritures.length() != 0)) {
                // toutes les écritures sauf correction et suspens supprimés
                whereUnion += " AND KBTCPT NOT IN (" + CIEcriture.CS_CI_SUSPENS_SUPPRIMES + ","
                        + CIEcriture.CS_CORRECTION + ")";
            } else {
                // écritures au CI
                whereUnion += " AND KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + ","
                        + CIEcriture.CS_GENRE_7 + ")";
            }

        }
        if (nonActif != null) {
            if (!nonActif.booleanValue()) {
                if (whereUnion.length() != 0) {
                    whereUnion += " AND ";
                } else {
                    whereUnion = " where ";
                }
                whereUnion += " (KBTGEN IN (" + CIEcriture.CS_CIGENRE_3 + ", " + CIEcriture.CS_CIGENRE_9 + ", "
                        + CIEcriture.CS_CIGENRE_2 + ")";
                whereUnion += " OR (KBTGEN =  " + CIEcriture.CS_CIGENRE_7 + "  and KBTSPE ="
                        + CIEcriture.CS_NONFORMATTEUR_INDEPENDANT + "))";
            } else {
                if (whereUnion.length() != 0) {
                    whereUnion += " AND ";
                } else {
                    whereUnion = " where ";
                }
                whereUnion += " (KBTGEN =" + CIEcriture.CS_CIGENRE_4 + " OR (KBTGEN =" + CIEcriture.CS_CIGENRE_7
                        + " and KBTSPE in (312004,0," + CIEcriture.CS_COTISATION_MINIMALE + ")))";
            }
        }

        if ((forNotEcriture != null) && (forNotEcriture.length() != 0)) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBIECR not in (" + forNotEcriture + ") ";

        }

        return firstUnion + whereUnion + secondUnion + whereUnion + endUnion;
    }

    /**
     * Renvoie le nom de la table
     */
    protected String _getTableName() {
        return "CIECRIP";
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2003 08:35:54)
     * 
     * @return java.lang.String
     */
    public java.lang.String getEtatEcritures() {
        return etatEcritures;
    }

    /**
     * @return
     */
    public String getForAllEcritures() {
        return forAllEcritures;
    }

    /**
     * Returns the forAnnee.
     * 
     * @return String
     */
    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCodeAmortissement() {
        return forCodeAmortissement;
    }

    /**
     * @return
     */
    public String getForCodeSpecial() {
        return forCodeSpecial;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.02.2003 13:38:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * Returns the forEmployeur.
     * 
     * @return String
     */
    public String getForEmployeur() {
        return forEmployeur;
    }

    /**
     * Returns the forGenre.
     * 
     * @return String
     */
    public String getForGenre() {
        return forGenre;
    }

    /**
     * @return
     */
    public String getForGenreCotPers() {
        return forGenreCotPers;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2003 10:31:16)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Returns the forMasseSalariale.
     * 
     * @return String
     */
    public String getForMasseSalariale() {
        return forMasseSalariale;
    }

    public String getForNotCodeAmortissement() {
        return forNotCodeAmortissement;
    }

    public String getForNotEcriture() {
        return forNotEcriture;
    }

    public Boolean getForNotType() {
        return forNotType;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public String getForNumeroAffilieAllEcritures() {
        return forNumeroAffilieAllEcritures;
    }

    public Boolean getNonActif() {
        return nonActif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2003 08:35:54)
     * 
     * @param newEtatEcritures
     *            java.lang.String
     */
    public void setEtatEcritures(java.lang.String newEtatEcritures) {
        etatEcritures = newEtatEcritures;
    }

    /**
     * @param string
     */
    public void setForAllEcritures(String string) {
        forAllEcritures = string;
    }

    /**
     * Sets the forAnnee.
     * 
     * @param forAnnee
     *            The forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCodeAmortissement(String forCodeAmortissement) {
        this.forCodeAmortissement = forCodeAmortissement;
    }

    /**
     * @param string
     */
    public void setForCodeSpecial(String string) {
        forCodeSpecial = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.02.2003 13:38:30)
     * 
     * @param newForCompteIndividuelId
     *            java.lang.String
     */
    public void setForCompteIndividuelId(java.lang.String newForCompteIndividuelId) {
        forCompteIndividuelId = newForCompteIndividuelId;
    }

    /**
     * Sets the forEmployeur.
     * 
     * @param forEmployeur
     *            The forEmployeur to set
     */
    public void setForEmployeur(String forEmployeur) {
        this.forEmployeur = forEmployeur;
    }

    /**
     * Sets the forGenre.
     * 
     * @param forGenre
     *            The forGenre to set
     */
    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    /**
     * @param string
     */
    public void setForGenreCotPers(String string) {
        forGenreCotPers = string;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2003 10:31:16)
     * 
     * @param newForIdJournal
     *            java.lang.String
     */
    public void setForIdJournal(java.lang.String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    /**
     * Sets the forMasseSalariale.
     * 
     * @param forMasseSalariale
     *            The forMasseSalariale to set
     */
    public void setForMasseSalariale(String forMasseSalariale) {
        this.forMasseSalariale = forMasseSalariale;
    }

    public void setForNotCodeAmortissement(String forNotCodeAmortissement) {
        this.forNotCodeAmortissement = forNotCodeAmortissement;
    }

    public void setForNotEcriture(String forNotEcriture) {
        this.forNotEcriture = forNotEcriture;
    }

    public void setForNotType(Boolean forNotType) {
        this.forNotType = forNotType;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setForNumeroAffilieAllEcritures(String forNumeroAffilieAllEcritures) {
        this.forNumeroAffilieAllEcritures = forNumeroAffilieAllEcritures;
    }

    public void setNonActif(Boolean nonActif) {
        this.nonActif = nonActif;
    }

}
