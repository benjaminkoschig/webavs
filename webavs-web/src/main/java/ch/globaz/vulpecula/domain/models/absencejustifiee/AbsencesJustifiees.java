package ch.globaz.vulpecula.domain.models.absencejustifiee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;
import ch.globaz.vulpecula.domain.models.TypeAbsenceJustifieeComparator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.Convention;

public class AbsencesJustifiees implements Iterable<AbsenceJustifiee> {
    private final Collection<AbsenceJustifiee> absenceJustifiees;

    public AbsencesJustifiees(Collection<AbsenceJustifiee> absencesJustifiees) {
        absenceJustifiees = absencesJustifiees;
    }

    @Override
    public Iterator<AbsenceJustifiee> iterator() {
        return absenceJustifiees.iterator();
    }

    public Montant sommeMontantsVerses() {
        Montant montant = new Montant(0);
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            montant = montant.add(absenceJustifiee.getMontantVerse());
        }
        return montant;
    }

    public Montant sommePartsPatronales() {
        Montant montant = new Montant(0);
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            montant = montant.add(absenceJustifiee.getPartPatronale());
        }
        return montant;
    }

    public Montant sommeMontantsBrutsOuvrier() {
        Montant montant = new Montant(0);
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            montant = montant.add(absenceJustifiee.getMontantBrutOuvrier());
        }
        return montant;
    }

    public Montant sommeMontantsBrutsEmploye() {
        Montant montant = new Montant(0);
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            montant = montant.add(absenceJustifiee.getMontantBrutEmploye());
        }
        return montant;
    }

    public Montant sommeSalairesHoraires() {
        Montant montant = new Montant(0);
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            montant = montant.add(absenceJustifiee.getSalaireHoraire());
        }
        return montant;
    }

    public int sommeNombreJours() {
        int nbJours = 0;
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            nbJours += absenceJustifiee.getNombreDeJours();
        }
        return nbJours;
    }

    public static Map<Convention, AJsParType> groupByConventionAndByType(
            Collection<AbsenceJustifiee> absencesJustifiees) {
        Map<Convention, AJsParType> absencesParConventionParType = new TreeMap<Convention, AJsParType>();

        Map<Convention, Collection<AbsenceJustifiee>> absencesParConvention = groupByConvention(absencesJustifiees);
        for (Map.Entry<Convention, Collection<AbsenceJustifiee>> entry : absencesParConvention.entrySet()) {
            AJsParType ajParType = new AJsParType(groupByTypeAbsence(entry.getValue()));
            absencesParConventionParType.put(entry.getKey(), ajParType);
        }
        return absencesParConventionParType;
    }

    public static Map<TypeAbsenceJustifiee, AbsencesJustifiees> groupByTypeAbsence(
            Collection<AbsenceJustifiee> absencesJustifiees) {
        TreeMultimap<TypeAbsenceJustifiee, AbsenceJustifiee> abs = TreeMultimap.create();
        for (AbsenceJustifiee absenceJustifiee : absencesJustifiees) {
            abs.put(absenceJustifiee.getType(), absenceJustifiee);
        }

        Map<TypeAbsenceJustifiee, AbsencesJustifiees> absencesParType = new TreeMap<TypeAbsenceJustifiee, AbsencesJustifiees>(
                new TypeAbsenceJustifieeComparator());
        for (Map.Entry<TypeAbsenceJustifiee, Collection<AbsenceJustifiee>> typeAbsenceEntry : abs.asMap().entrySet()) {
            absencesParType.put(typeAbsenceEntry.getKey(), new AbsencesJustifiees(typeAbsenceEntry.getValue()));
        }
        return absencesParType;
    }

    public static List<AJParEmployeur> groupByEmployeur(Collection<AbsenceJustifiee> absences) {
        List<AJParEmployeur> list = new ArrayList<AJParEmployeur>();
        Map<Employeur, Collection<AbsenceJustifiee>> map = Multimaps
                .index(absences, new Function<AbsenceJustifiee, Employeur>() {
                    @Override
                    public Employeur apply(AbsenceJustifiee absenceJustifiee) {
                        return absenceJustifiee.getEmployeur();
                    }
                }).asMap();
        for (Map.Entry<Employeur, Collection<AbsenceJustifiee>> entry : map.entrySet()) {
            AbsencesJustifiees ajs = new AbsencesJustifiees(entry.getValue());
            list.add(new AJParEmployeur(entry.getKey(), ajs));
        }
        return list;
    }

    private static Map<Convention, Collection<AbsenceJustifiee>> groupByConvention(
            Collection<AbsenceJustifiee> absencesJustifiees) {
        TreeMultimap<Convention, AbsenceJustifiee> abs = TreeMultimap.create();
        for (AbsenceJustifiee absenceJustifiee : absencesJustifiees) {
            abs.put(absenceJustifiee.getConventionEmployeur(), absenceJustifiee);
        }
        return abs.asMap();
    }

    public int size() {
        return absenceJustifiees.size();
    }

    public boolean add(AbsenceJustifiee aj) {
        return absenceJustifiees.add(aj);
    }
}
