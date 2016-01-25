package globaz.musca.db.interet.cotipercuentrop.montantsoumis;

import globaz.globall.db.BStatement;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.interet.generic.montantsoumis.FASumMontantSoumisParPlan;
import globaz.osiris.api.APISection;

public class FACotiPercuEnTropParRubrique extends FASumMontantSoumisParPlan {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneRole;
    private String idRole;
    private String idRubrique;

    private String idSousType;

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        setIdRubrique(statement.dbReadNumeric(FAAfact.FIELD_IDRUBRIQUE));
        setIdExterneRole(statement.dbReadString(FAEnteteFacture.FIELD_IDEXTERNEROLE));
        setIdRole(statement.dbReadNumeric(FAEnteteFacture.FIELD_IDROLE));

        setIdSousType(statement.dbReadNumeric(FAEnteteFacture.FIELD_IDSOUSTYPE));
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdSousType() {
        return idSousType;
    }

    public boolean isDecompteTypeCotPers() {
        return APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS.equals(getIdSousType());
    }

    public boolean isDecompteTypeCotPersEtudiants() {
        return APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT.equals(getIdSousType());
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdSousType(String idSousType) {
        this.idSousType = idSousType;
    }
}
