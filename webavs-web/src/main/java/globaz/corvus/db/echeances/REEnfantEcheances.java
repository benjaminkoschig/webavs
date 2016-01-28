package globaz.corvus.db.echeances;

import globaz.jade.client.util.JadeDateUtil;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.corvus.business.models.echeances.IREEnfantEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;

public class REEnfantEcheances implements IREEnfantEcheances {

    private String dateDeces;
    private String dateNaissance;
    private String idTiers;
    private String nom;
    private String prenom;
    private Set<IRERenteEcheances> rentes;

    public REEnfantEcheances() {
        dateDeces = "";
        dateNaissance = "";
        nom = "";
        prenom = "";
        rentes = new HashSet<IRERenteEcheances>();
    }

    @Override
    public int compareTo(IREEnfantEcheances o) {
        if (JadeDateUtil.isDateAfter(o.getDateNaissance(), getDateNaissance())) {
            return -1;
        } else if (JadeDateUtil.isDateBefore(o.getDateNaissance(), getDateNaissance())) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof REEnfantEcheances) {
            return ((IREEnfantEcheances) obj).getIdTiers().equals(getIdTiers());
        }
        return false;
    }

    @Override
    public String getDateDeces() {
        return dateDeces;
    }

    @Override
    public String getDateNaissance() {
        return dateNaissance;
    }

    @Override
    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public Set<IRERenteEcheances> getRentes() {
        return rentes;
    }

    @Override
    public int hashCode() {
        return (this.getClass().getName() + " - " + getIdTiers()).hashCode();
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRentes(Set<IRERenteEcheances> rentes) {
        this.rentes = rentes;
    }
}
