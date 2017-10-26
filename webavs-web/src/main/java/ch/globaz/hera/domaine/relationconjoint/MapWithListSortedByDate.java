package ch.globaz.hera.domaine.relationconjoint;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.parametre.Parametre;

public abstract class MapWithListSortedByDate<E, T extends Parametre<E>, L extends MapWithListSortedByDate<E, T, L>> {
    protected TreeMap<E, TreeSet<T>> map = new TreeMap<E, TreeSet<T>>();
    private Class<L> clazz;

    public MapWithListSortedByDate() {
        clazz = this.getTypeClass();
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
        return map.isEmpty();
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
        for (Set<T> vms : map.values()) {
            newMap.addAll(vms);
        }
        return newMap;
    }

    public Set<T> getValues(E parametreType) {
        return map.get(parametreType);
    }

    public int size() {
        int nb = 0;
        for (Set<T> vms : map.values()) {
            nb = nb + vms.size();
        }
        return nb;
    }

    public L getParameters(E parametreType) {
        checkIfExisteInMap(parametreType);
        L l = newInstance();

        l.addAll(map.get(parametreType));
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
        for (Entry<E, TreeSet<T>> entry : map.entrySet()) {
            long debut = dateDebut.getTime();
            TreeSet<T> params = entry.getValue();
            for (T parametre : params) {
                // Si la date de la periode est plus grande ou egale a la varaiable metier
                if (debut >= parametre.getDateDebut().getTime() && !l.map.containsKey(parametre.getType())) {
                    l.add(parametre);
                }
            }
        }
        return l;
    }

    public T resolveCourant(Date dateValidite) {
        if (map.size() > 1) {
            throw new RuntimeException(
                    "Il n'est pas possible de resoudre le param�tre courrant car trop de parametres sont d�finit. Il faut reduire le scope des param�tres ");
        }
        long debut = dateValidite.getTime();
        TreeSet<T> params = map.firstEntry().getValue();
        T param;
        for (T parametre : params) {
            // Si la date de la periode est plus grande ou egale a la varaiable metier
            param = parametre;
            if (debut >= parametre.getDateDebut().getTime()) {
                return param;
            }
        }
        throw new RuntimeException("Aucune param�tre n'est valable pour la date donn�e en param�tre: " + dateValidite
                + ", pour la class suivante : " + clazz.getSimpleName());
    }

    private void checkIfExisteInMap(E parametreType) {
        if (!map.containsKey(parametreType)) {
            throw new RuntimeException("Aucune valeur (" + parametreType + ") existe dans la map (size:" + map.size()
                    + "), pour la class suivante: " + clazz.getSimpleName());
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
