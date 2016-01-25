/*
 * Cr�� le 15 mars 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author jmc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CIEcritureSommeTemp extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatEcritures = new String();
    private String forAnnee = new String();
    private String forCompteIndividuelId = new String();
    private String forEmployeur = new String();
    private String forGenre = new String();
    private String forIdJournal = new String();
    private String forMasseSalariale = new String();
    private String forNotGenre = new String();
    private String forNotId = new String();

    /**
	 * 
	 */
    public CIEcritureSommeTemp() {
        super();
        // TODO Raccord de constructeur auto-g�n�r�
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = new String();
        String firstUnion = " (select KBIECR,KBMMON from " + _getCollection() + _getTableName();
        String secondUnion = " AND KBTEXT IN (0,311006,311002,311008) union select KBIECR,-KBMMON AS KBMMON from "
                + _getCollection() + _getTableName();
        String endUnion = " AND KBTEXT IN (311001,311003,311004,311005,311007,311009)) as RESULT";
        String whereUnion = "";
        // R�cup�ration du CI, du tiers/partenaire
        // Obligation de renomm�e la table CIINDIP pour requ�te sur le
        // partenaire
        if (forIdJournal != null && forIdJournal.length() != 0) {
            whereUnion = " where KCID=" + forIdJournal;
            whereUnion += " AND KBTCPT <> " + CIEcriture.CS_CORRECTION;
        }
        if (forCompteIndividuelId != null && forCompteIndividuelId.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KAIIND=" + forCompteIndividuelId;
            // filter des �criture au CI
            whereUnion += " AND KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + ","
                    + CIEcriture.CS_GENRE_7 + "," + CIEcriture.CS_TEMPORAIRE + "," + CIEcriture.CS_TEMPORAIRE_SUSPENS
                    + "," + CIEcriture.CS_CI_SUSPENS + ")";
        }

        if (etatEcritures != null && etatEcritures.length() != 0) {

            // Si etat == toutesEcritures --> toutes les ecritures, actives et
            // non actives -> bypass
            if (!"toutesEcritures".equals(etatEcritures)) {
                // clause pour les cl�tures
                if (whereUnion.length() != 0) {
                    whereUnion += " AND ";
                } else {
                    whereUnion = " where ";
                }

                // Etats tous || seulementClotures -> toutes les �critures de
                // clotures
                if ("tous".equals(etatEcritures) || "seulementClotures".equals(etatEcritures)) {
                    // toutes �critures cl�tur�es
                    whereUnion += "KKIRAO<>0";
                } else if ("active".equals(etatEcritures)) {
                    // toutes les �criture actives
                    whereUnion += "KKIRAO=0";
                } else {
                    // �critures d'une cl�ture
                    whereUnion += "KKIRAO=" + etatEcritures;
                }
            }
        }
        if (forAnnee != null && forAnnee.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KBNANN=" + forAnnee;
        }
        if (forEmployeur != null && forEmployeur.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += "KBITIE=" + forEmployeur;
        }
        if (forMasseSalariale != null && forMasseSalariale.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            // filter des �criture au CI
            whereUnion += " KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + "," + CIEcriture.CS_GENRE_7
                    + "," + CIEcriture.CS_CI_SUSPENS + ")";

        }
        if (forGenre != null && forGenre.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBTGEN = " + forGenre + " ";

        }
        if (forNotGenre != null && forNotGenre.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBTGEN <> " + forNotGenre + " ";
        }
        if (forNotId != null && forNotId.length() != 0) {
            if (whereUnion.length() != 0) {
                whereUnion += " AND ";
            } else {
                whereUnion = " where ";
            }
            whereUnion += " KBIECR <> " + forNotId + " ";

        }

        return firstUnion + whereUnion + secondUnion + whereUnion + endUnion;
    }

    /**
     * Renvoie le nom de la table
     */
    protected String _getTableName() {
        return "CIECRIP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.02.2003 08:35:54)
     * 
     * @return java.lang.String
     */
    public java.lang.String getEtatEcritures() {
        return etatEcritures;
    }

    /**
     * Returns the forAnnee.
     * 
     * @return String
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.02.2003 13:38:30)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.01.2003 10:31:16)
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

    /**
     * @return
     */
    public String getForNotGenre() {
        return forNotGenre;
    }

    /**
     * @return
     */
    public String getForNotId() {
        return forNotId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.02.2003 08:35:54)
     * 
     * @param newEtatEcritures
     *            java.lang.String
     */
    public void setEtatEcritures(java.lang.String newEtatEcritures) {
        etatEcritures = newEtatEcritures;
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

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.02.2003 13:38:30)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.01.2003 10:31:16)
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

    /**
     * @param string
     */
    public void setForNotGenre(String string) {
        forNotGenre = string;
    }

    /**
     * @param string
     */
    public void setForNotId(String string) {
        forNotId = string;
    }

}
