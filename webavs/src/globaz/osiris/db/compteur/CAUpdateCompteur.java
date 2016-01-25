/*
 * Créé le 18 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.compteur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author spa Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAUpdateCompteur extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee;
    // Private members
    private String idCompteAnnexe;
    private String idRubrique;
    private String masse;
    private String montant;

    /**
	 * 
	 */
    public CAUpdateCompteur() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "caoperp";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCompteAnnexe = statement.dbReadNumeric("IDCOMPTEANNEXE");
        idRubrique = statement.dbReadNumeric("IDCOMPTE");
        annee = statement.dbReadNumeric("ANNEECOTISATION");
        montant = statement.dbReadNumeric("MONTANT", 2);
        masse = statement.dbReadNumeric("MASSE", 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // throw new
        // Exception("Cannot use method 'writePrimaryKey' on this Entity");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new Exception("Cannot use method 'writeProperty' on this Entity");
    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return
     */
    public String getMasse() {
        return masse;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    /**
     * @param string
     */
    public void setMasse(String string) {
        masse = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            return getIdCompteAnnexe() + ":" + getAnnee() + " " + getMontant() + "/" + getMasse();
        } catch (Exception e) {
            return super.toString();
        }
    }

}
