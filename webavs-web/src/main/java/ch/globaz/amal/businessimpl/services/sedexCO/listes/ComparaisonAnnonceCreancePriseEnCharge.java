package ch.globaz.amal.businessimpl.services.sedexCO.listes;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

@ColumnValueConverter({ MontantConverterToDouble.class })
public class ComparaisonAnnonceCreancePriseEnCharge {
    private String noContribuable = null;
    private String nss = null;
    private String nomPrenom = null;
    private String localite;
    private String annee;
    private String assureur;
    private Montant montantCreance;
    private String message = null;

    @Column(name = "N° de contribuable", order = 0)
    public String getNoContribuable() {
        return noContribuable;
    }

    public void setNoContribuable(String noContribuable) {
        this.noContribuable = noContribuable;
    }

    @Column(name = "NSS", order = 1)
    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Column(name = "Nom prénom", order = 2)
    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    @Column(name = "Localité", order = 3)
    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    @Column(name = "Année", order = 4)
    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Column(name = "Assureur", order = 5)
    public String getAssureur() {
        return assureur;
    }

    public void setAssureur(String assureur) {
        this.assureur = assureur;
    }

    @Column(name = "Montant créance", order = 6)
    public Montant getMontantCreance() {
        return montantCreance;
    }

    public void setMontantCreance(Montant montantCreance) {
        this.montantCreance = montantCreance;
    }

    @Column(name = "Message", order = 7)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
