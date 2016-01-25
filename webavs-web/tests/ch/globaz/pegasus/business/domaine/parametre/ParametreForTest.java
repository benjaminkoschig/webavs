package ch.globaz.pegasus.business.domaine.parametre;

import ch.globaz.common.domaine.Date;

class ParametreForTest implements Parametre<ParametreType> {

    private Date debut;
    private Date fin;
    private ParametreType type;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateDebut() {
        return debut;
    }

    @Override
    public Date getDateFin() {
        return fin;
    }

    @Override
    public ParametreType getType() {
        return type;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public void setType(ParametreType type) {
        this.type = type;
    }

}
