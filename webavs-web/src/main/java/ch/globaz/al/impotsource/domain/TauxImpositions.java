package ch.globaz.al.impotsource.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.globaz.al.exception.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class TauxImpositions {
    private Map<String, Collection<TauxImposition>> tauxImpositions;

    public TauxImpositions(List<TauxImposition> tauxImpositions) {
        Collections.sort(tauxImpositions, new Comparator<TauxImposition>() {
            @Override
            public int compare(TauxImposition ti1, TauxImposition ti2) {
                return ti1.getPeriode().compareTo(ti2.getPeriode());
            }
        });
        this.tauxImpositions = Multimaps.index(tauxImpositions, new Function<TauxImposition, String>() {
            @Override
            public String apply(TauxImposition tauxImposition) {
                return tauxImposition.getCanton();
            }
        }).asMap();
    }

    public boolean chevauche(TauxImposition tauxImposition) {
        Collection<TauxImposition> taux = tauxImpositions.get(tauxImposition.getCanton());
        if (taux == null) {
            return false;
        }
        for (TauxImposition ti : taux) {
            if (ti.getId().equals(tauxImposition.getId())) {
                continue;
            }
            if (ti.getPeriode().chevauche(tauxImposition.getPeriode())) {
                return true;
            }
        }
        return false;
    }

    public Taux getTauxImpotSource(String canton, Date date) throws TauxImpositionNotFoundException {
        return getTaux(canton, date, TypeImposition.IMPOT_SOURCE);
    }

    public Taux getTauxComissionPerception(String canton, Date date) throws TauxImpositionNotFoundException {
        return getTaux(canton, date, TypeImposition.COMMISSION_PERCEPTION);
    }

    private Taux getTaux(String canton, Date date, TypeImposition typeImposition)
            throws TauxImpositionNotFoundException {
        Collection<TauxImposition> taux = tauxImpositions.get(canton);
        if (taux == null) {
            throw new TauxImpositionNotFoundException(date, canton, typeImposition.getValue());
        }

        for (TauxImposition tauxImposition : tauxImpositions.get(canton)) {
            if (typeImposition.equals(tauxImposition.getTypeImposition())) {
                if (tauxImposition.getPeriode().contains(date)) {
                    return tauxImposition.getTaux();
                }
            }
        }
        throw new TauxImpositionNotFoundException(date, canton, typeImposition.getValue());
    }
}
