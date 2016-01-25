package globaz.ij.pojo;

public class AdressePaiementDecompteIJPojo {

    private String adressePaiement;
    private String libelle;

    public AdressePaiementDecompteIJPojo() {
        super();
        libelle = "";
        adressePaiement = "";
    }

    public String getAdressePaiement() {
        return adressePaiement;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setAdressePaiement(String adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
