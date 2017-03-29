/*
 * Globaz SA.
 */
package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilation;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import ch.globaz.common.domaine.Montant;

public class REDeblocageVersement extends BEntity {

    private static final long serialVersionUID = 1L;

    private String idCompteAnnexe;
    private String idRenteAccordee;
    private String codeRenteAccordee;
    private String idTiersAdressePaiement;
    private String idApplicationAdressePaiement;
    private String idTiersBeneficiaire;

    private Montant montant;
    private String refPaiement;
    private RELigneDeblocage ligneDeblocage = new RELigneDeblocage();
    private RELigneDeblocageVentilation ligneDeblocageVentilation = new RELigneDeblocageVentilation();

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
        idRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        codeRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        idTiersAdressePaiement = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idApplicationAdressePaiement = statement
                .dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        ligneDeblocage.readFromStatement(statement, "ld");
        ligneDeblocageVentilation.readFromStatement(statement, "ldv");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not implemented
    }

    public String formatAdressePaiement() {
        return "";
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public Montant getMontant() {
        return ligneDeblocage.getMontant();
    }

    public RELigneDeblocageType getType() {
        return ligneDeblocage.getType();
    }

    public String getCodeRenteAccordee() {
        return codeRenteAccordee;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public RELigneDeblocage getLigneDeblocage() {
        return ligneDeblocage;
    }

    public RELigneDeblocageVentilation getLigneDeblocageVentilation() {
        return ligneDeblocageVentilation;
    }

}
