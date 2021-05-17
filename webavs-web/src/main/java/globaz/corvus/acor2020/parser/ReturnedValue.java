package globaz.corvus.acor2020.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReturnedValue {

    private List<Long> idRenteAccordees = new LinkedList<>();
    private List<String> remarquesParticulieres = new ArrayList<>();
    private Long idCopieDemande;

    /**
     * @return the idRenteAccordees
     */
    public final List<Long> getIdRenteAccordees() {
        return idRenteAccordees;
    }

    /**
     * @param idRenteAccordees the idRenteAccordees to set
     */
    public final void setIdRenteAccordees(List<Long> idRenteAccordees) {
        this.idRenteAccordees = idRenteAccordees;
    }

    /**
     * @return the idCopieDemande
     */
    public final Long getIdCopieDemande() {
        return idCopieDemande;
    }

    /**
     * @param idCopieDemande the idCopieDemande to set
     */
    public final void setIdCopieDemande(Long idCopieDemande) {
        if (this.idCopieDemande == null) {
            this.idCopieDemande = idCopieDemande;
        }
    }

    public List<String> getRemarquesParticulieres() {
        return remarquesParticulieres;
    }
}
