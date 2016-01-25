/*
 * Créé le 5 juin 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author SPA
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CASoldesMinimesParSecteur extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe = new String();
    private String idCompteCourant = new String();
    private String idExterneRole = new String();
    private String idRole = new String();
    private String idSection = new String();
    private String montant = new String();

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompteAnnexe(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTEANNEXE));
        setIdCompteCourant(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTECOURANT));
        setIdRole(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE));
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
        setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT, 2));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
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
    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    /**
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    public String getIdRole() {
        return idRole;
    }

    /**
     * @return
     */
    public String getIdSection() {
        return idSection;
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
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdCompteCourant(String string) {
        idCompteCourant = string;
    }

    /**
     * @param string
     */
    public void setIdExterneRole(String string) {
        idExterneRole = string;
    }

    /**
     * @param string
     */
    public void setIdRole(String string) {
        idRole = string;
    }

    /**
     * @param string
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

}
