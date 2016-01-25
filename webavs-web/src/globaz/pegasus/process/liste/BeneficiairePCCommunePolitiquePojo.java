package globaz.pegasus.process.liste;

import ch.globaz.simpleoutputlist.annotation.Column;

public class BeneficiairePCCommunePolitiquePojo implements Comparable<BeneficiairePCCommunePolitiquePojo> {

    private String communePolitique = "";
    private String idTiers = "";
    private String nss = "";
    private String nom = "";
    private String prenom = "";
    private String codePrestation = "";
    private String typePrestation = "";
    private String montant = "";

    @Column(name = "commune", order = 1)
    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Column(name = "Nss", order = 2)
    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Column(name = "Nom", order = 3)
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Column(name = "Prénom", order = 4)
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Column(name = "code préstation", order = 5)
    public String getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    @Column(name = "Montant", order = 6)
    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    @Override
    public int compareTo(BeneficiairePCCommunePolitiquePojo o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }

}
