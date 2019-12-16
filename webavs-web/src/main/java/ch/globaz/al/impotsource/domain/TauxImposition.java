package ch.globaz.al.impotsource.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.specifications.Specification;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.al.impotsource.utils.TICantonRequis;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class TauxImposition implements DomainEntity, Comparable<TauxImposition> {
    private String id;
    private Periode periode;
    private String canton;
    private TypeImposition typeImposition;
    private Taux taux;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public TypeImposition getTypeImposition() {
        return typeImposition;
    }

    public void setTypeImposition(TypeImposition typeImposition) {
        this.typeImposition = typeImposition;
    }

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public Date getPeriodeDebut() {
        return periode.getDateDebut();
    }

    public Date getPeriodeFin() {
        return periode.getDateFin();
    }

    @Override
    public int compareTo(TauxImposition o) {
        return periode.compareTo(o.getPeriode());
    }

    public static Map<String, Collection<TauxImposition>> groupByCanton(List<TauxImposition> taux) {
        return Multimaps.index(taux, new Function<TauxImposition, String>() {
            @Override
            public String apply(TauxImposition taux) {
                return taux.canton;
            }
        }).asMap();
    }

    public void validate() throws UnsatisfiedSpecificationException {
        Specification<TauxImposition> spec = new TICantonRequis();
        spec.isSatisfiedBy(this);
    }
}
