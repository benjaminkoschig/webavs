package ch.globaz.corvus.process.dnra;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.converterImplemented.DateSwissFormatConverter;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.PaysConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;

@ColumnValueConverter({ DateSwissFormatConverter.class, PaysConverter.class })
@Translater(value = LabelTranslater.class, identifier = "LIST_CORVUS_DIFFERENCE_TROUVEE")
public class DifferenceTrouvee {
    private String nss;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private TypeDifference difference;
    private String valeurActuelle;
    private String valeurNouvelle;
    private Date dateChangement;

    @Column(name = "Nss", order = 1)
    public String getNss() {
        return nss;
    }

    @Column(name = "Nom", order = 2)
    public String getNom() {
        return nom;
    }

    @Column(name = "Prenom", order = 3)
    public String getPrenom() {
        return prenom;
    }

    @Column(name = "DateDeNaissance", order = 4)
    public Date getDateNaissance() {
        return dateNaissance;
    }

    @Column(name = "TypeDeDifference", order = 5)
    public TypeDifference getDifference() {
        return difference;
    }

    @Column(name = "ValeurActuelle", order = 7)
    public String getValeurActuelle() {
        return valeurActuelle;
    }

    @Column(name = "NouvelleValeur", order = 8)
    public String getValeurNouvelle() {
        return valeurNouvelle;
    }

    @Column(name = "DateChangementEtatCivil", order = 9)
    public Date getDateChangement() {
        return dateChangement;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDifference(TypeDifference difference) {
        this.difference = difference;
    }

    public void setValeurActuelle(String valeurActuelle) {
        this.valeurActuelle = valeurActuelle;
    }

    public void setValeurNouvelle(String valeurNouvelle) {
        this.valeurNouvelle = valeurNouvelle;
    }

    public void setDateChangement(Date dateChangement) {
        this.dateChangement = dateChangement;
    }

    @Override
    public String toString() {
        return "DifferenceTrouvee [nss=" + nss + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance="
                + dateNaissance + ", difference=" + difference + ", valeurActuelle=" + valeurActuelle
                + ", valeurNouvelle=" + valeurNouvelle + ", dateChangement=" + dateChangement + "]";
    }

}
