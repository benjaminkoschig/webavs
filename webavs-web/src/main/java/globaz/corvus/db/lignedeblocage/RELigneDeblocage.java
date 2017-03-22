/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.deblocage.REDeblocageException;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class RELigneDeblocage extends JadeEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private Long idTiersCreancier;
    private Long idRoleSection;
    private Long idTiersAdressePaiement;
    private Long idApplicationAdressePaiement;
    private Long idSectionCompensee;
    private Long idRenteAccordee;
    private Long idLot;
    private RELigneDeblocageType type;
    private RELigneDeblocageEtat etat;
    private Montant montant;
    private String refPaiement;

    @Override
    protected void writeProperties() {
        this.write(RELigneDeblocageTableDef.ID_LOT, idLot);
        this.write(RELigneDeblocageTableDef.ID_TIERS_CREANCIER, idTiersCreancier);
        this.write(RELigneDeblocageTableDef.ID_ROLE_SECTION, idRoleSection);
        this.write(RELigneDeblocageTableDef.ID_TIERS_ADRESSE_PAIEMENT, idTiersAdressePaiement);
        this.write(RELigneDeblocageTableDef.ID_APPLICATION_ADRESSE_PAIEMENT, idApplicationAdressePaiement);
        this.write(RELigneDeblocageTableDef.ID_SECTION_COMPENSEE, idSectionCompensee);
        this.write(RELigneDeblocageTableDef.ID_RENTE_ACCORDEE, idRenteAccordee);
        this.write(RELigneDeblocageTableDef.CS_ETAT, etat);
        this.write(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE, type);
        this.write(RELigneDeblocageTableDef.MONTANT, montant, CONVERTER_MONTANT);
        this.write(RELigneDeblocageTableDef.REFERENCE_PAIEMENT, refPaiement);
    }

    @Override
    protected void readProperties() {
        id = this.read(RELigneDeblocageTableDef.ID);
        idTiersCreancier = this.read(RELigneDeblocageTableDef.ID_TIERS_CREANCIER);
        idRoleSection = this.read(RELigneDeblocageTableDef.ID_ROLE_SECTION);
        idTiersAdressePaiement = this.read(RELigneDeblocageTableDef.ID_TIERS_ADRESSE_PAIEMENT);
        idApplicationAdressePaiement = this.read(RELigneDeblocageTableDef.ID_APPLICATION_ADRESSE_PAIEMENT);
        idSectionCompensee = this.read(RELigneDeblocageTableDef.ID_SECTION_COMPENSEE);
        idRenteAccordee = this.read(RELigneDeblocageTableDef.ID_RENTE_ACCORDEE);
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

    public Long getIdTiersCreancier() {
        return idTiersCreancier;
    }

    public void setIdTiersCreancier(Long idTiersCreancier) {
        this.idTiersCreancier = idTiersCreancier;
    }

    public Long getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(Long idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public Long getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public void setIdApplicationAdressePaiement(Long idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
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
        verifChangeEtat(etat);
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

    public boolean isVersementBeneficiaire() {
        return type.isVersementBeneficiaire();
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public Long getIdRoleSection() {
        return idRoleSection;
    }

    public void setIdRoleSection(Long idRoleSection) {
        this.idRoleSection = idRoleSection;
    }

    public Long getIdSectionCompensee() {
        return idSectionCompensee;
    }

    public void setIdSectionCompensee(Long idSectionCompensee) {
        this.idSectionCompensee = idSectionCompensee;
    }

    public Long getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public void setIdRenteAccordee(Long idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public Long getIdLot() {
        return idLot;
    }

    public void setIdLot(Long idLot) {
        this.idLot = idLot;
    }

    private void verifChangeEtat(RELigneDeblocageEtat etat) {
        if (this.etat != null) {
            if (this.etat.isComptabilise() && !etat.isComptabilise()) {
                throw new REDeblocageException(
                        "It is not possible to change the stat the object is allready comptabilisé !");
            }

            if (this.etat.isEnregistre() && etat.isComptabilise()) {
                throw new REDeblocageException("It is not possible to change the stat from enregistre to comtpabilisé!");
            }
        }
    }
}
