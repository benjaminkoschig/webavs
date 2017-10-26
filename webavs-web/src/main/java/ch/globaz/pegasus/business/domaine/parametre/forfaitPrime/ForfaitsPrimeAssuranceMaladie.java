package ch.globaz.pegasus.business.domaine.parametre.forfaitPrime;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.parametre.MapWithListSortedByDate;

public class ForfaitsPrimeAssuranceMaladie extends
        MapWithListSortedByDate<TypePrime, ForfaitPrimeAssuranceMaladie, ForfaitsPrimeAssuranceMaladie> {

    protected Map<String, ForfaitsPrimeAssuranceMaladie> mapByNpa = new HashMap<String, ForfaitsPrimeAssuranceMaladie>();

    public ForfaitsPrimeAssuranceMaladie() {
    }

    public ForfaitsPrimeAssuranceMaladie(Collection<ForfaitPrimeAssuranceMaladie> list) {
        super(list);
    }

    @Override
    public Class<ForfaitsPrimeAssuranceMaladie> getTypeClass() {
        return ForfaitsPrimeAssuranceMaladie.class;
    }

    public ForfaitsPrimeAssuranceMaladie filtreforPeriode(Date dateDebut) {
        ForfaitsPrimeAssuranceMaladie forfaitsPrimeAssuranceMaladie = new ForfaitsPrimeAssuranceMaladie();
        for (Entry<String, ForfaitsPrimeAssuranceMaladie> entry : mapByNpa.entrySet()) {
            ForfaitsPrimeAssuranceMaladie list = entry.getValue().filtreByPeriode(dateDebut);
            for (TreeSet<ForfaitPrimeAssuranceMaladie> tree : list.map.values()) {
                for (ForfaitPrimeAssuranceMaladie forfaitPrimeAssuranceMaladie : tree) {
                    forfaitsPrimeAssuranceMaladie.addPrime(forfaitPrimeAssuranceMaladie);
                }
            }
        }
        return forfaitsPrimeAssuranceMaladie;
    }

    public void addPrime(ForfaitPrimeAssuranceMaladie parametre) {
        if (!mapByNpa.containsKey(parametre.getIdLocalite())) {
            mapByNpa.put(parametre.getIdLocalite(), new ForfaitsPrimeAssuranceMaladie());
        }
        mapByNpa.get(parametre.getIdLocalite()).add(parametre);
    }

    public int nbForfait() {
        int nb = 0;
        for (ForfaitsPrimeAssuranceMaladie vms : mapByNpa.values()) {
            nb = nb + vms.size();
        }
        return nb;
    }

    public ForfaitsPrimeAssuranceMaladie filtreByIdLocalite(String idLocalite) {
        if (mapByNpa.containsKey(idLocalite)) {
            return mapByNpa.get(idLocalite);
        }
        return new ForfaitsPrimeAssuranceMaladie();
    }

    public ForfaitsPrimeAssuranceMaladie filtreByAge(int age) {
        TypePrime typePrime = resolveTypePrime(age);
        return getParameters(typePrime);
    }

    public ForfaitsPrimeAssuranceMaladie filtreByAge(Date dateReferance, Date dateNaissance) {
        int age = computeAge(dateReferance, dateNaissance);
        TypePrime typePrime = resolveTypePrime(age);
        return getParameters(typePrime);
    }

    int computeAge(Date dateReferance, Date dateNaissance) {
        Calendar calPeriode = Calendar.getInstance();
        calPeriode.setTime(dateReferance.getDate());
        Calendar calPersonne = Calendar.getInstance();
        calPersonne.setTime(dateNaissance.getDate());
        // Même calcul fait dans la class PeriodePCAccordee
        return calPeriode.get(Calendar.YEAR) - calPersonne.get(Calendar.YEAR);
    }

    TypePrime resolveTypePrime(int age) {
        if (age < 19) {
            return TypePrime.ENFANT;
        } else if (age < 26) {
            return TypePrime.JEUNE_ADULTE;
        } else {
            return TypePrime.ADULTE;
        }
    }
}
