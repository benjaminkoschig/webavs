package globaz.apg.module.calcul;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class APRepartitionPaiementData {

    private List<APCotisationData> cotisations = new ArrayList<APCotisationData>();
    private String idAffilie;
    private String idSituationProfessionnelle;
    private String idTiersAdressePaiement;
    private BigDecimal montantBrut = null;
    private BigDecimal montantNet = null;
    private String nom;
    private String typeAssociationAssurance;
    private String typePaiement;
    private String typePrestation;

    public APRepartitionPaiementData(BigDecimal montantBrut, BigDecimal montantNet, String typePrestation,
            String typePaiement, String idTiersAdressePaiement, String typeAssociationAssurance,
            String idSituationProfessionnelle, String nom, String idAffilie) {
        super();
        this.montantBrut = montantBrut;
        this.montantNet = montantNet;
        this.typePrestation = typePrestation;
        this.typePaiement = typePaiement;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.typeAssociationAssurance = typeAssociationAssurance;
        this.idSituationProfessionnelle = idSituationProfessionnelle;
        this.nom = nom;
        this.idAffilie = idAffilie;
    }

    public List<APCotisationData> getCotisations() {
        return cotisations;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdSituationProfessionnelle() {
        return idSituationProfessionnelle;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public BigDecimal getMontantBrut() {
        return montantBrut;
    }

    public BigDecimal getMontantNet() {
        return montantNet;
    }

    public String getNom() {
        return nom;
    }

    public String getTypeAssociationAssurance() {
        return typeAssociationAssurance;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setCotisations(List<APCotisationData> cotisations) {
        this.cotisations = cotisations;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdSituationProfessionnelle(String idSituationProfessionnelle) {
        this.idSituationProfessionnelle = idSituationProfessionnelle;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setMontantBrut(BigDecimal montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setMontantNet(BigDecimal montantNet) {
        this.montantNet = montantNet;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setTypeAssociationAssurance(String typeAssociationAssurance) {
        this.typeAssociationAssurance = typeAssociationAssurance;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

}
