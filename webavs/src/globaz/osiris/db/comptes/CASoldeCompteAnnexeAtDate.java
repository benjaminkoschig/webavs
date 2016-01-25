package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.application.CAApplication;
import globaz.osiris.external.IntRole;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 12:56:55)
 * 
 * @author: Administrator
 */

public class CASoldeCompteAnnexeAtDate extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CARole caRole;
    private String description = new String();
    private String idExterneRole = new String();

    private String idRole = new String();
    private String solde = "0.00";

    /**
     * Commentaire relatif au constructeur CACompteAnnexe
     */
    public CASoldeCompteAnnexeAtDate() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {

        return _getCollection() + "CACPTAP.description, " + _getCollection() + "CACPTAP.idexternerole, "
                + _getCollection() + "CACPTAP.idrole, " + "sum (" + _getCollection() + "CAOPERP.montant) as montant ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CACPTAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        description = statement.dbReadString("DESCRIPTION");
        idExterneRole = statement.dbReadString("IDEXTERNEROLE");
        solde = statement.dbReadNumeric("MONTANT", 2);
        idRole = statement.dbReadNumeric("IDROLE");
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

    public String getDescription() {
        return description;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * Returns the idRole.
     * 
     * @return String
     */
    public String getIdRole() {
        return idRole;
    }

    public IntRole getRole() {
        try {
            CAApplication currentApplication = CAApplication.getApplicationOsiris();

            IntRole intRole = (IntRole) GlobazServer.getCurrentSystem()
                    .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                    .getImplementationFor(getSession(), IntRole.class);
            intRole.retrieve(idRole, idExterneRole);

            return intRole;
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return null;
        }
    }

    public String getSolde() {
        return JANumberFormatter.deQuote(solde);
    }

    /**
     * Sets the idRole.
     * 
     * @param idRole
     *            The idRole to set
     */
    public void setIdRole(String newIdRole) {
        idRole = newIdRole;
        caRole = null;
    }

}
