/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class RELigneDeblocage extends JadeEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer idTiersCreancier;
    private Integer idRoleDetteEnCompta;
    private Integer idTiersAdressePaiement;
    private Integer idApplicationAdressePaiement;
    private Integer idSectionDetteEnCompta;
    private Integer idRentePrestation;
    private Integer idLot;
    private RELigneDeblocageType type;
    private RELigneDeblocageEtat etat;
    private Montant montant;
    private String refPaiement;

    @Override
    protected void writeProperties() {
        this.write(RELigneDeblocageTableDef.ID, id);
        this.write(RELigneDeblocageTableDef.ID_TIERS_CREANCIER, idTiersCreancier);
        this.write(RELigneDeblocageTableDef.ID_ROLE_DETTE_EN_COMPTA, idRoleDetteEnCompta);
        this.write(RELigneDeblocageTableDef.ID_TIERS_ADRESSE_PAIEMENT, idTiersAdressePaiement);
        this.write(RELigneDeblocageTableDef.ID_APPLICATION_ADRESSE_PAIEMENT, idApplicationAdressePaiement);
        this.write(RELigneDeblocageTableDef.ID_SECTION_DETTE_EN_COMPTA, idSectionDetteEnCompta);
        this.write(RELigneDeblocageTableDef.ID_RENTE_PRESTATION, idRentePrestation);
        this.write(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE, type);
        this.write(RELigneDeblocageTableDef.CS_ETAT, etat);
        this.write(RELigneDeblocageTableDef.MONTANT, montant, CONVERTER_MONTANT);
        this.write(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE, type);
        this.write(RELigneDeblocageTableDef.CS_ETAT, etat);
        this.write(RELigneDeblocageTableDef.REFERENCE_PAIEMENT, refPaiement);
        this.write(RELigneDeblocageTableDef.ID_LOT, idLot);
    }

    @Override
    protected void readProperties() {
        id = this.read(RELigneDeblocageTableDef.ID);
        idTiersCreancier = this.read(RELigneDeblocageTableDef.ID_TIERS_CREANCIER);
        idRoleDetteEnCompta = this.read(RELigneDeblocageTableDef.ID_ROLE_DETTE_EN_COMPTA);
        idTiersAdressePaiement = this.read(RELigneDeblocageTableDef.ID_TIERS_ADRESSE_PAIEMENT);
        idApplicationAdressePaiement = this.read(RELigneDeblocageTableDef.ID_APPLICATION_ADRESSE_PAIEMENT);
        idSectionDetteEnCompta = this.read(RELigneDeblocageTableDef.ID_SECTION_DETTE_EN_COMPTA);
        idRentePrestation = this.read(RELigneDeblocageTableDef.ID_RENTE_PRESTATION);
        idLot = this.read(RELigneDeblocageTableDef.ID_LOT);
        type = this.read(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE, RELigneDeblocageType.class);
        etat = this.read(RELigneDeblocageTableDef.CS_ETAT, RELigneDeblocageEtat.class);
        montant = this.read(RELigneDeblocageTableDef.MONTANT, CONVERTER_MONTANT);
        refPaiement = this.read(RELigneDeblocageTableDef.REFERENCE_PAIEMENT);
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return RELigneDeblocageTableDef.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;
    }

    public Integer getIdTiersCreancier() {
        return idTiersCreancier;
    }

    public void setIdTiersCreancier(Integer idTiersCreancier) {
        this.idTiersCreancier = idTiersCreancier;
    }

    public Integer getIdRoleDetteEnCompta() {
        return idRoleDetteEnCompta;
    }

    public void setIdRoleDetteEnCompta(Integer idRoleDetteEnCompta) {
        this.idRoleDetteEnCompta = idRoleDetteEnCompta;
    }

    public Integer getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(Integer idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public Integer getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public void setIdApplicationAdressePaiement(Integer idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
    }

    public Integer getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public void setIdSectionDetteEnCompta(Integer idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

    public Integer getIdRentePrestation() {
        return idRentePrestation;
    }

    public void setIdRentePrestation(Integer idRentePrestation) {
        this.idRentePrestation = idRentePrestation;
    }

    public RELigneDeblocageType getType() {
        return type;
    }

    public void setType(RELigneDeblocageType type) {
        this.type = type;
    }

    public RELigneDeblocageEtat getEtat() {
        return etat;
    }

    public void setEtat(RELigneDeblocageEtat etat) {
        this.etat = etat;
    }

    public boolean isComptabilise() {
        return etat.isComptabilise();
    }

    public boolean isEnregistre() {
        return etat.isEnregistre();
    }

    public boolean isValide() {
        return etat.isValide();
    }

    public boolean isCreancier() {
        return type.isCreancier();
    }

    public boolean isDetteEnCompta() {
        return type.isDetteEnCompta();
    }

    public boolean isImpotsSource() {
        return type.isImpotsSource();
    }

    public boolean isVersementBeneficaire() {
        return type.isVersementBeneficaire();
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

}
