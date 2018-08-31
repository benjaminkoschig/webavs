package ch.globaz.vulpecula.domain.models.ebusiness;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

public class Synchronisation implements DomainEntity {
    private String id;
    private String spy;
    private Date dateAjout;
    private Date dateSynchronisation;
    private Decompte decompte;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * @return the dateAjout
     */
    public Date getDateAjout() {
        return dateAjout;
    }

    /**
     * @param dateAjout the dateAjout to set
     */
    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    /**
     * @return the dateSynchronisation
     */
    public Date getDateSynchronisation() {
        return dateSynchronisation;
    }

    /**
     * @param dateSynchronisation the dateSynchronisation to set
     */
    public void setDateSynchronisation(Date dateSynchronisation) {
        this.dateSynchronisation = dateSynchronisation;
    }

    /**
     * @return the decompte
     */
    public Decompte getDecompte() {
        return decompte;
    }

    /**
     * @param decompte the decompte to set
     */
    public void setDecompte(Decompte decompte) {
        this.decompte = decompte;
    }
}
