package ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.parametre.Parametre;

public class MonnaieEtrangere implements Parametre<MonnaieEtrangereType> {
    private MonnaieEtrangereType Type;
    private Date dateDebut;
    private Date dateFin;
    private String id;
    private Taux taux;

    @Override
    public MonnaieEtrangereType getType() {
        return Type;
    }

    public void setType(MonnaieEtrangereType type) {
        Type = type;
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

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    public boolean isFrancSuisse() {
        return getType().isFrancSuisse();
    }

    @Override
    public String toString() {
        return "MonnaieEtrangere [Type=" + Type + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", id=" + id
                + ", taux=" + taux + "]";
    }

}
