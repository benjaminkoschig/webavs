/*
 * Globaz SA.
 */
package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import ch.globaz.common.domaine.Montant;

public class REDeblocageVersement extends BEntity {

    private static final long serialVersionUID = 1L;

    private String idCompteAnnexe;
    private String idRenteAccordee;
    private Long idTiersAdressePaiement;
    private Long idTiersBeneficiaire;
    private Long idSectionSource;
    private Long idSectionCompense;
    private Long idRoleSection;
    private Montant montant;
    private RELigneDeblocageType type;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

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

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public Long getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(Long idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public Long getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setIdTiersBeneficiaire(Long idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public Long getIdSectionSource() {
        return idSectionSource;
    }

    public void setIdSectionSource(Long idSectionSource) {
        this.idSectionSource = idSectionSource;
    }

    public Long getIdSectionCompense() {
        return idSectionCompense;
    }

    public void setIdSectionCompense(Long idSectionCompense) {
        this.idSectionCompense = idSectionCompense;
    }

    public Long getIdRoleSection() {
        return idRoleSection;
    }

    public void setIdRoleSection(Long idRoleSection) {
        this.idRoleSection = idRoleSection;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public RELigneDeblocageType getType() {
        return type;
    }

    public void setType(RELigneDeblocageType type) {
        this.type = type;
    }

}
