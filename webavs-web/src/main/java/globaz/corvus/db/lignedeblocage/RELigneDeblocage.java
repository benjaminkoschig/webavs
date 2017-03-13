/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.REDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.REDeblocageType;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class RELigneDeblocage extends JadeEntity {
    private static final long serialVersionUID = 1L;

    private String idDeblocage;
    private Integer idTiersCreancier;
    private Integer idRoleDetteEnCompta;
    private Integer idTiersAdressePaiement;
    private Integer idApplicationAdressePaiement;
    private Integer idSectionDetteEnCompta;
    private Integer idRentePrestation;
    private REDeblocageType typeDeblocage;
    private REDeblocageEtat etatDeblocage;
    private Montant montant;
    private String refPaiement;

    @Override
    protected void writeProperties() {
        this.write(RELigneDeblocageTableDef.ID_DEBLOCAGE, idDeblocage);
        this.write(RELigneDeblocageTableDef.ID_TIERS_CREANCIER, idTiersCreancier);
        this.write(RELigneDeblocageTableDef.ID_ROLE_DETTE_EN_COMPTA, idRoleDetteEnCompta);
        this.write(RELigneDeblocageTableDef.ID_TIERS_ADRESSE_PAIEMENT, idTiersAdressePaiement);
        this.write(RELigneDeblocageTableDef.ID_APPLICATION_ADRESSE_PAIEMENT, idApplicationAdressePaiement);
        this.write(RELigneDeblocageTableDef.ID_SECTION_DETTE_EN_COMPTA, idSectionDetteEnCompta);
        this.write(RELigneDeblocageTableDef.ID_RENTE_PRESTATION, idRentePrestation);
        write(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE, typeDeblocage);
        write(RELigneDeblocageTableDef.CS_ETAT, etatDeblocage);
        write(RELigneDeblocageTableDef.MONTANT, montant, CONVERTER_MONTANT);
        this.write(RELigneDeblocageTableDef.REFERENCE_PAIEMENT, refPaiement);
    }

    @Override
    protected void readProperties() {
        idDeblocage = this.read(RELigneDeblocageTableDef.ID_DEBLOCAGE);
        idTiersCreancier = this.read(RELigneDeblocageTableDef.ID_TIERS_CREANCIER);
        idRoleDetteEnCompta = this.read(RELigneDeblocageTableDef.ID_ROLE_DETTE_EN_COMPTA);
        idTiersAdressePaiement = this.read(RELigneDeblocageTableDef.ID_TIERS_ADRESSE_PAIEMENT);
        idApplicationAdressePaiement = this.read(RELigneDeblocageTableDef.ID_APPLICATION_ADRESSE_PAIEMENT);
        idSectionDetteEnCompta = this.read(RELigneDeblocageTableDef.ID_SECTION_DETTE_EN_COMPTA);
        idRentePrestation = this.read(RELigneDeblocageTableDef.ID_RENTE_PRESTATION);
        typeDeblocage = this.read(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE);
        etatDeblocage = this.read(RELigneDeblocageTableDef.CS_ETAT);
        montant = this.read(RELigneDeblocageTableDef.MONTANT, CONVERTER_MONTANT);
        refPaiement = this.read(RELigneDeblocageTableDef.REFERENCE_PAIEMENT);
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return RELigneDeblocageTableDef.class;
    }

    @Override
    public String getIdEntity() {
        return idDeblocage;
    }

    @Override
    public void setIdEntity(String id) {
        idDeblocage = id;
    }

    public String getIdDeblocage() {
        return idDeblocage;
    }

    public void setIdDeblocage(String idDeblocage) {
        this.idDeblocage = idDeblocage;
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

    public REDeblocageType getTypeDeblocage() {
        return typeDeblocage;
    }

    public void setTypeDeblocage(REDeblocageType typeDeblocage) {
        this.typeDeblocage = typeDeblocage;
    }

    public REDeblocageEtat getEtatDeblocage() {
        return etatDeblocage;
    }

    public void setEtatDeblocage(REDeblocageEtat etatDeblocage) {
        this.etatDeblocage = etatDeblocage;
    }

    public Integer getIdRentePrestation() {
        return idRentePrestation;
    }

    public void setIdRentePrestation(Integer idRentePrestation) {
        this.idRentePrestation = idRentePrestation;
    }

}
