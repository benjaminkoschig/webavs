package globaz.aquila.db.rdp.cashin.model;

import java.util.ArrayList;
import java.util.List;

public class RequisitionPoursuite {

    private Personne personne;
    private List<Litige> litiges = new ArrayList<Litige>();

    public RequisitionPoursuite(Personne personne, List<Litige> litiges) {
        this.personne = personne;
        this.litiges = litiges;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public List<Litige> getLitiges() {
        return litiges;
    }

    public void setLitiges(List<Litige> litiges) {
        this.litiges = litiges;
    }
}
