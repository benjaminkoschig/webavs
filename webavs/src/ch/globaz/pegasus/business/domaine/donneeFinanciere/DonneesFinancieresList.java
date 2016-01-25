package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class DonneesFinancieresList<T extends DonneeFinanciere, L extends DonneesFinancieresList<T, L>> {
    private final List<T> list = new ArrayList<T>();
    private final Class<? super L> clazz;

    public DonneesFinancieresList(Class<? super L> clazz) {
        this.clazz = clazz;
    }

    public List<T> getList() {
        return list;
    }

    public boolean add(T donneeFinanciere) {
        return this.list.add(donneeFinanciere);
    }

    public boolean addAll(DonneesFinancieresList<T, L> list) {
        return this.list.addAll(list.getList());
    }

    public boolean addAll(List<T> list) {
        return this.list.addAll(list);
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public T get(int index) {
        return list.get(index);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public L getDonneesForRequerant() {
        return getDonneeForMembreFamille(RoleMembreFamille.REQUERANT);
    }

    public L getDonneesForConjoint() {
        return getDonneeForMembreFamille(RoleMembreFamille.CONJOINT);
    }

    public L getDonneesForEnfant() {
        return getDonneeForMembreFamille(RoleMembreFamille.ENFANT);
    }

    public L getDonneesForConjointEnfant() {

        L listConjoinEnfant = newInstance();
        for (T df : this.list) {
            if (df.isConjoint() ^ df.isEnfant()) {
                listConjoinEnfant.add(df);
            }
        }

        return listConjoinEnfant;
    }

    private L getDonneeForMembreFamille(RoleMembreFamille membreFamille) {
        // if (this.mapByRoleMembreFamille == null) {
        // this.mapByRoleMembreFamille = this.groupByRoleMembreFamill(list);
        // }
        Map<RoleMembreFamille, List<T>> mapByRoleMembreFamille = groupByRoleMembreFamille(list);
        List<T> list = mapByRoleMembreFamille.get(membreFamille);
        if (list == null) {
            list = new ArrayList<T>();
        }
        L donneesFinancieresList;
        donneesFinancieresList = newInstance();
        donneesFinancieresList.addAll(list);
        return donneesFinancieresList;
    }

    protected L newInstance() {
        try {
            return (L) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<RoleMembreFamille, List<T>> groupByRoleMembreFamille(List<T> list) {

        Map<RoleMembreFamille, List<T>> res = new LinkedHashMap<RoleMembreFamille, List<T>>();
        for (T item : list) {
            RoleMembreFamille key = item.getRoleMembreFamille();
            if (res.get(key) == null) {
                res.put(key, new ArrayList<T>());
            }
            res.get(key).add(item);
        }

        return res;
    }

    public int size() {
        return this.list.size();
    }

    public Montant sum(Each<T> each) {
        Montant sum = Montant.ZERO;
        for (T donnneeFianciere : this.list) {
            sum = sum.add(each.getMontant(donnneeFianciere));
        }
        return sum;
    }

    public Montant sum(Each<T> each, RoleMembreFamille... rolesMembresFamilles) {
        Montant sum = Montant.ZERO;
        for (T donnneeFianciere : this.list) {
            if (donnneeFianciere.getRoleMembreFamille().isIn(rolesMembresFamilles)) {
                sum = sum.add(each.getMontant(donnneeFianciere));
            }
        }
        return sum;
    }

    public Montant sumDette() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Dette) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Dette) donnneeFianciere).computeDette());
                }
            }
        }
        return montant;
    }

    public Montant sumDetteBrut() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Dette) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Dette) donnneeFianciere).computeDetteBrut());
                }
            }
        }
        return montant;
    }

    public Montant sumRevenuAnnuel() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Revenu) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Revenu) donnneeFianciere).computeRevenuAnnuel());
                }
            }
        }
        return montant;
    }

    public Montant sumRevenuAnnuelBrut() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Revenu) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Revenu) donnneeFianciere).computeRevenuAnnuelBrut());
                }
            }
        }
        return montant;
    }

    public Montant sumFortune() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Fortune) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Fortune) donnneeFianciere).computeFortune());
                }
            }
        }
        return montant;
    }

    public Montant sumFortunePartPropriete() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof FortunePartPropriete) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((FortunePartPropriete) donnneeFianciere).computeFortunePartPropriete());
                }
            }
        }
        return montant;
    }

    public Montant sumFortuneBrut() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Fortune) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Fortune) donnneeFianciere).computeFortuneBrut());
                }
            }
        }
        return montant;
    }

    public Montant sumDepense() {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Depense) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Depense) donnneeFianciere).computeDepense());
                }
            }
        }
        return montant;
    }

    public Montant sumInteret(Taux taux) {
        Montant montant = Montant.ZERO_ANNUEL;
        if (!this.list.isEmpty()) {
            T t = this.list.get(0);
            if (t instanceof Interet) {
                for (T donnneeFianciere : this.list) {
                    montant = montant.add(((Interet) donnneeFianciere).computeInteret(taux));
                }
            }
        }
        return montant;
    }

    @Override
    public String toString() {
        return "DonneesFinancieresList [list=" + list.size() + "]";
    }

}
