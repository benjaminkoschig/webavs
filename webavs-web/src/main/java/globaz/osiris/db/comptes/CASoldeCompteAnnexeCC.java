package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 12:56:55)
 * 
 * @author: Administrator
 */

public class CASoldeCompteAnnexeCC extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String CS_CANDIDAT_POUR_EXCLUSION = "204103";
    private CARole caRole;
    private CACompteAnnexe compteAnnexe = null;
    private CACompteCourant compteCourant = null;
    private String description = new String();
    private String idCompteAnnexe = new String();
    private String idCompteCourant = new String();

    private String idExterneRole = new String();
    private String idRole = new String();

    private String solde = "0.00";

    /**
     * Commentaire relatif au constructeur CACompteAnnexe
     */
    public CASoldeCompteAnnexeCC() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CACompteAnnexe.TABLE_CACPTAP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompteAnnexe = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        description = statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION);
        idRole = statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE);
        idExterneRole = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        idCompteCourant = statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTECOURANT);
        solde = statement.dbReadNumeric(CAOperation.FIELD_MONTANT, 2);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 13:29:20)
     * 
     * @return globaz.osiris.db.comptes.CARole
     */
    public CARole getCARole() {
        if (caRole == null) {
            caRole = new CARole();
            caRole.setISession(getSession());
            caRole.setIdRole(getIdRole());
            try {
                caRole.retrieve();
                if (caRole.isNew()) {
                    caRole = null;
                }
            } catch (Exception e) {
                caRole = null;
            }
        }

        return caRole;
    }

    public CACompteAnnexe getCompteAnnexe() {
        // Si compte pas déjà instancié
        if (compteAnnexe == null) {
            // Instancier un nouveau compte
            compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(getSession());

            // Récupérer le compte en question
            compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());
            try {
                compteAnnexe.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }

        // Retourner le compte
        return compteAnnexe;
    }

    public CACompteCourant getCompteCourant() {
        // Si compte pas déjà instancié
        if (compteCourant == null) {
            // Instancier un nouveau compte
            compteCourant = new CACompteCourant();
            compteCourant.setSession(getSession());

            // Récupérer le compte en question
            compteCourant.setIdCompteCourant(getIdCompteCourant());
            try {
                compteCourant.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }

        // Retourner le compte
        return compteCourant;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Getter
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Returns the idCompteCourant.
     * 
     * @return String
     */
    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getSolde() {
        return JANumberFormatter.deQuote(solde);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.08.2002 14:16:25)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getSoldeToCurrency() {
        return new FWCurrency(getSolde());
    }

    public void setDescription(String newDescription) {
        if (newDescription.length() >= 50) {
            description = newDescription.substring(0, 50);
        } else {
            description = newDescription;
        }
    }

    /**
     * Setter
     */
    public void setIdCompteAnnexe(String newIdCompteAnnexe) {
        idCompteAnnexe = newIdCompteAnnexe;
    }

    /**
     * Sets the idCompteCourant.
     * 
     * @param idCompteCourant
     *            The idCompteCourant to set
     */
    public void setIdCompteCourant(String idCompteCourant) {
        this.idCompteCourant = idCompteCourant;
    }

    public void setIdExterneRole(String newIdExterneRole) {
        idExterneRole = newIdExterneRole;
    }

    public void setIdRole(String newIdRole) {
        idRole = newIdRole;
        caRole = null;
    }

    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    /**
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            return "[" + getIdCompteAnnexe() + "] " + getIdExterneRole() + " " + getDescription();
        } catch (Exception e) {
            return super.toString();
        }
    }
}
