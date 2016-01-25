package ch.globaz.vulpecula.domain.models.decompte;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class HistoriqueDecompte implements DomainEntity {

    private String id;
    private Decompte decompte;
    private Date date;
    private EtatDecompte etat;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public Decompte getDecompte() {
        return decompte;
    }

    public void setDecompte(final Decompte decompte) {
        this.decompte = decompte;
    }

    public String getDateAsSwissValue() {
        if (date != null) {
            return date.getSwissValue();
        } else {
            return null;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public EtatDecompte getEtat() {
        return etat;
    }

    public String getEtatAsValue() {
        if (etat != null) {
            etat.getValue();
        }
        return null;
    }

    public void setEtat(final EtatDecompte etat) {
        this.etat = etat;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(final String spy) {
        this.spy = spy;
    }

    public String getHeureMinuteSpy() {
        return spy.substring(8, 10) + ":" + spy.substring(10, 12) + ":" + spy.substring(12, 14);
    }

}
