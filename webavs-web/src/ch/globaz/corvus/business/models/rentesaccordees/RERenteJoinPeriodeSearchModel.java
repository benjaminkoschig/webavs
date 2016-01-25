package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.HashSet;
import java.util.Set;

public class RERenteJoinPeriodeSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Set<String> forCodePrestationIn;
    private Set<String> forCsEtatRenteAccordeeIn;
    private Set<String> forCsTypePeriodeIn;
    private String forDateNaissanceAnterieureOuEgaleA;
    private String forDateNaissanceUlterieureOuEgaleA;
    private String forPrestationEnCoursDansMois;

    public RERenteJoinPeriodeSearchModel() {
        super();

        forCsEtatRenteAccordeeIn = new HashSet<String>();
        forCsTypePeriodeIn = new HashSet<String>();
        forCodePrestationIn = new HashSet<String>();
        forDateNaissanceAnterieureOuEgaleA = "";
        forDateNaissanceUlterieureOuEgaleA = "";
        forPrestationEnCoursDansMois = "";
    }

    public Set<String> getForCodePrestationIn() {
        return forCodePrestationIn;
    }

    public Set<String> getForCsEtatRenteAccordeeIn() {
        return forCsEtatRenteAccordeeIn;
    }

    public Set<String> getForCsTypePeriodeIn() {
        return forCsTypePeriodeIn;
    }

    public String getForDateNaissanceAnterieureOuEgaleA() {
        return forDateNaissanceAnterieureOuEgaleA;
    }

    public String getForDateNaissanceUlterieureOuEgaleA() {
        return forDateNaissanceUlterieureOuEgaleA;
    }

    public String getForPrestationEnCoursDansMois() {
        return forPrestationEnCoursDansMois;
    }

    public void setForCodePrestationIn(Set<String> forCodePrestationIn) {
        this.forCodePrestationIn = forCodePrestationIn;
    }

    public void setForCsEtatRenteAccordeeIn(Set<String> csEtatRenteAccordee) {
        forCsEtatRenteAccordeeIn = csEtatRenteAccordee;
    }

    public void setForCsTypePeriodeIn(Set<String> forCsTypePeriodeIn) {
        this.forCsTypePeriodeIn = forCsTypePeriodeIn;
    }

    public void setForDateNaissanceAnterieureOuEgaleA(String forDateNaissanceAnterieureOuEgaleA) {
        this.forDateNaissanceAnterieureOuEgaleA = forDateNaissanceAnterieureOuEgaleA;
    }

    public void setForDateNaissanceUlterieureOuEgaleA(String forDateNaissanceUlterieureOuEgaleA) {
        this.forDateNaissanceUlterieureOuEgaleA = forDateNaissanceUlterieureOuEgaleA;
    }

    public void setForPrestationEnCoursDansMois(String forPrestationEnCoursDansMois) {
        this.forPrestationEnCoursDansMois = forPrestationEnCoursDansMois;
    }

    @Override
    public Class<? extends RERenteJoinPeriode> whichModelClass() {
        return RERenteJoinPeriode.class;
    }
}
