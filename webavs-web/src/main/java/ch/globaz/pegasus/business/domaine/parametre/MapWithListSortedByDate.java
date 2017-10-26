package ch.globaz.pegasus.business.domaine.parametre;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import ch.globaz.common.domaine.Date;

public abstract class MapWithListSortedByDate<E, T extends Parametre<E>, L extends MapWithListSortedByDate<E, T, L>> {
    protected TreeMap<E, TreeSet<T>> map = new TreeMap<E, TreeSet<T>>();
    private Class<L> clazz;

    public MapWithListSortedByDate() {
        this.clazz = this.getTypeClass();
    }

    public MapWithListSortedByDate(Collection<T> list) {
        this.addAll(list);
    }

    public abstract Class<L> getTypeClass();

    public T add(T parametre) {
        if (!map.containsKey(parametre.getType())) {
            map.put(parametre.getType(), newTreeSet());
        }
        map.get(parametre.getType()).add(parametre);
        return parametre;
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public L addAll(Collection<T> map) {
        for (T parametre : map) {
            add(parametre);
        }
        return (L) this;
    }

    public T resolveMostRecent() {
        checkIfExisteInMap(map.firstKey());
        return map.get(map.firstKey()).first();
    }

    public Set<T> getValues() {
        Set<T> newMap = newTreeSet();
        for (Set<T> vms : this.map.values()) {
            newMap.addAll(vms);
        }
        return newMap;
    }

    public Set<T> getValues(E parametreType) {
        return map.get(parametreType);
    }

    public int size() {
        int nb = 0;
        for (Set<T> vms : this.map.values()) {
            nb = nb + vms.size();
        }
        return nb;
    }

    public boolean hasParameter(E parametreType) {
        return map.containsKey(parametreType);
    }

    public L getParameters(E parametreType) {
        checkIfExisteInMap(parametreType);
        L l = newInstance();

        l.addAll(this.map.get(parametreType));
        return l;
    }

    private L newInstance() {
        L l = null;
        try {
            l = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
        return l;
    }

    public L filtreByPeriode(Date dateDebut) {
        L l = newInstance();
        for (Entry<E, TreeSet<T>> entry : this.map.entrySet()) {
            long debut = dateDebut.getTime();
            TreeSet<T> params = entry.getValue();
            for (T parametre : params) {
                // Si la date de la periode est plus grande ou egale a la varaiable metier
                if (debut >= parametre.getDateDebut().getTime()
                        && (parametre.getDateFin() == null || parametre.getDateFin().getTime() >= dateDebut.getTime())
                        && !l.map.containsKey(parametre.getType())) {
                    l.add(parametre);
                }
            }
        }
        return l;
    }

    public T resolveCourant(Date dateValidite) {
        if (this.map.size() > 1) {
            throw new RuntimeException(
                    "Il n'est pas possible de resoudre le paramétre courrant car trop de parametres sont définit. Il faut reduire le scope des paramétres ");
        }
        long debut = dateValidite.getTime();
        TreeSet<T> params = map.firstEntry().getValue();
        T param;
        for (T parametre : params) {
            // Si la date de la periode est plus grande ou egale a la varaiable metier
            param = parametre;
            if (debut >= parametre.getDateDebut().getTime()
                    && (parametre.getDateFin() == null || parametre.getDateFin().getTime() >= debut)) {
                return param;
            }
        }
        throw new RuntimeException("Aucune paramétre n'est valable pour la date donnée en paramétre: " + dateValidite
                + ", pour la class suivante : " + this.clazz.getSimpleName());
    }

    private void checkIfExisteInMap(E parametreType) {
        if (!map.containsKey(parametreType)) {
            throw new RuntimeException("Aucune valeur (" + parametreType + ") existe dans la map (size:"
                    + this.map.size() + "), pour la class suivante: " + this.clazz.getSimpleName());
        }
    }

    private TreeSet<T> newTreeSet() {
        return new TreeSet<T>(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1.getDateDebut().getTime() > o2.getDateDebut().getTime()) {
                    return -1;
                } else if (o1.getDateDebut().getTime() < o2.getDateDebut().getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
}
