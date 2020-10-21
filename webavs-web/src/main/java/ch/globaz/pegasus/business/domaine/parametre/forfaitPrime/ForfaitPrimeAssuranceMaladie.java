package ch.globaz.pegasus.business.domaine.parametre.forfaitPrime;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.parametre.Parametre;

public class ForfaitPrimeAssuranceMaladie implements Parametre<TypePrime> {
    private String id;
    private String idLocalite;
    private TypePrime typePrime;
    private Date dateDebut;
    private Date dateFin;
    private Montant montantPrimeMoy;
    private Montant montantPrimeReductionMaxCanton;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    public void setType(TypePrime typePrime) {
        this.typePrime = typePrime;
    }

    @Override
    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    public Date getDateFin() {
        return dateFin;
    }

    @Override
    public TypePrime getType() {
        return typePrime;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Montant getMontantPrimeMoy() {
        return montantPrimeMoy;
    }

    public void setMontantPrimeMoy(Montant montantPrimeMoy) {
        this.montantPrimeMoy = montantPrimeMoy;
    }

    public Montant getMontantPrimeReductionMaxCanton() {
        return montantPrimeReductionMaxCanton;
    }

    public void setMontantPrimeReductionMaxCanton(Montant montantPrimeReductionMaxCanton) {
        this.montantPrimeReductionMaxCanton = montantPrimeReductionMaxCanton;
    }

    @Override
    public String toString() {
        return "ForfaitPrimeAssuranceMaladie [id=" + id + ", idLocalite=" + idLocalite + ", typePrime=" + typePrime
                + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", montantPrimeMoy=" + montantPrimeMoy + ", montantPrimeReductionMaxCanton=" + montantPrimeReductionMaxCanton +"]";
    }

}
