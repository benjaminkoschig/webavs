package ch.globaz.vulpecula.models.decompte;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.common.ValueObject;
import ch.globaz.vulpecula.domain.models.decompte.CotisationCalculee;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import com.google.common.base.Preconditions;

/**
 * @since WebBMS 1.0
 */
public class TableauContributions {

    private DecompteSalaireService decompteSalaireService = VulpeculaServiceLocator.getDecompteSalaireService();

    public static enum TypeContribution {
        CAISSES_SOCIALES,
        AVS,
        AC,
        AC2,
        AF;

        public boolean isCaissesSociales() {
            return equals(CAISSES_SOCIALES);
        }

        public boolean isAvs() {
            return equals(AVS);
        }

        public boolean isAc() {
            return equals(AC);
        }

        public boolean isAc2() {
            return equals(AC2);
        }

        public boolean isAf() {
            return equals(AF);
        }
    }

    public static class EntreeContribution implements ValueObject, Comparable<EntreeContribution> {
        private static final long serialVersionUID = 6494188283418217195L;
        private final Taux taux;
        private final Montant masse;
        private final Annee annee;

        public EntreeContribution(Taux taux, Montant masse, Annee annee) {
            Preconditions.checkNotNull(taux);
            Preconditions.checkNotNull(masse);

            this.taux = taux;
            this.masse = masse;
            this.annee = annee;
        }

        private EntreeContribution(Taux taux, Montant masse) {
            this(taux, masse, null);
        }

        public static EntreeContribution empty() {
            return new EntreeContribution(Taux.ZERO(), Montant.ZERO);
        }

        public Taux getTaux() {
            return taux;
        }

        public Montant getMasse() {
            return masse;
        }

        public Montant getMontant() {
            return masse.multiply(taux).normalize();
        }

        public Annee getAnnee() {
            return annee;
        }

        @Override
        public int compareTo(EntreeContribution o) {
            return o.taux.compareTo(taux);
        }

    }

    private Map<TypeContribution, Collection<EntreeContribution>> tableauContributions;

    public TableauContributions(Decompte decompte) {
        tableauContributions = new TreeMap<TypeContribution, Collection<EntreeContribution>>();
        computeCaissesSociales(decompte);
        computeAvsAc(decompte);
        computeAf(decompte);
    }

    /**
     * @param decompte
     */
    private void computeAvsAc(Decompte decompte) {
        List<CotisationCalculee> cotisationsCalculees = decompte.getTableCotisationsCalculees();
        Map<Pair<TypeAssurance, Annee>, Taux> tauxParTypeAndAnnee = tauxParTypeAndAnnee(cotisationsCalculees);
        for (CotisationCalculee cotisation : cotisationsCalculees) {
            Taux taux = cotisation.getTaux();
            Montant masse = cotisation.getMontant();
            Annee annee = cotisation.getAnnee();

            if (TypeAssurance.COTISATION_AVS_AI.equals(cotisation.getTypeAssurance())) {
                Taux tauxAVS = getTauxAVS(tauxParTypeAndAnnee, annee);
                add(TypeContribution.AVS, new EntreeContribution(tauxAVS, masse, annee));
            } else if (TypeAssurance.ASSURANCE_CHOMAGE.equals(cotisation.getTypeAssurance())) {
                add(TypeContribution.AC, new EntreeContribution(taux, masse, annee));
            } else if (TypeAssurance.COTISATION_AC2.equals(cotisation.getTypeAssurance())) {
                add(TypeContribution.AC2, new EntreeContribution(taux, masse, annee));
            }
        }
    }

    private Taux getTauxAVS(Map<Pair<TypeAssurance, Annee>, Taux> tauxParTypeAndAnnee, Annee annee) {
        Taux tauxAVS = tauxParTypeAndAnnee.get(new Pair<TypeAssurance, Annee>(TypeAssurance.COTISATION_AVS_AI, annee));
        Taux tauxFA = tauxParTypeAndAnnee
                .get(new Pair<TypeAssurance, Annee>(TypeAssurance.FRAIS_ADMINISTRATION, annee));
        if (tauxAVS == null && tauxFA == null) {
            return Taux.ZERO();
        } else if (tauxFA == null) {
            return tauxAVS;
        } else if (tauxAVS == null) {
            return tauxFA;
        } else {
            return tauxAVS.addTaux(tauxFA);
        }
    }

    private Map<Pair<TypeAssurance, Annee>, Taux> tauxParTypeAndAnnee(List<CotisationCalculee> cotisationsCalculees) {
        Map<Pair<TypeAssurance, Annee>, Taux> map = new HashMap<Pair<TypeAssurance, Annee>, Taux>();
        for (CotisationCalculee cc : cotisationsCalculees) {
            map.put(new Pair<TypeAssurance, Annee>(cc.getTypeAssurance(), cc.getAnnee()), cc.getTaux());
        }
        return map;
    }

    private void computeCaissesSociales(Decompte decompte) {
        tableauContributions.put(TypeContribution.CAISSES_SOCIALES,
                new TreeSet<TableauContributions.EntreeContribution>());
        Map<Taux, Montant> data = getTotalCotisationCaisseSocialeSansAF(decompte);

        for (Taux taux : data.keySet()) {
            Montant masse = data.get(taux);
            add(TypeContribution.CAISSES_SOCIALES, new EntreeContribution(taux, masse, null));
        }
    }

    private void computeAf(Decompte decompte) {
        Map<Taux, Montant> data = getTotalCotisationAF(decompte);

        for (Taux taux : data.keySet()) {
            if (!tableauContributions.containsKey(TypeContribution.AF)) {
                tableauContributions.put(TypeContribution.AF, new TreeSet<TableauContributions.EntreeContribution>());
            }
            if (taux.isZero()) {
                continue;
            }
            Montant masse = data.get(taux);
            add(TypeContribution.AF, new EntreeContribution(taux, masse, null));
        }
    }

    private Map<Taux, Montant> getTotalCotisationAF(Decompte decompte) {
        Map<Taux, Montant> cotisations = new HashMap<Taux, Montant>();
        for (DecompteSalaire decompteSalaire : decompte.getLignes()) {
            Taux keyTaux = decompteSalaire.getTauxContribuableForAF();
            if (!decompteSalaireService.isPosteTravailRentier(decompteSalaire)) {
                continue;
            }
            Montant masse = decompteSalaire.getMasseAF();

            if (cotisations.size() != 0 && cotisations.containsKey(keyTaux)) {
                masse = masse.add(cotisations.get(keyTaux));
            }
            cotisations.put(keyTaux, masse);
        }
        return cotisations;
    }

    private Map<Taux, Montant> getTotalCotisationCaisseSocialeSansAF(Decompte decompte) {
        Map<Taux, Montant> cotisations = new HashMap<Taux, Montant>();
        for (DecompteSalaire decompteSalaire : decompte.getLignes()) {
            Taux keyTaux = getTauxContribuableForCaissesSocialesSansAF(decompteSalaire);
            Montant masse = decompteSalaire.getSalaireTotal();

            if (cotisations.size() != 0 && cotisations.containsKey(keyTaux)) {
                masse = masse.add(cotisations.get(keyTaux));
            }
            cotisations.put(keyTaux, masse);
        }
        return cotisations;
    }

    private Taux getTauxContribuableForCaissesSocialesSansAF(DecompteSalaire decompteSalaire) {
        Taux taux = new Taux(0);
        for (CotisationDecompte cotisationDecompte : decompteSalaire.getCotisationsDecompte()) {
            if (decompteSalaireService.isPosteTravailRentier(decompteSalaire)) {
                if (!Assurance.NOT_CAISSES_SOCIALES_AND_AF.contains(cotisationDecompte.getTypeAssurance())) {
                    taux = taux.addTaux(cotisationDecompte.getTaux());
                }
            } else {
                if (!Assurance.NOT_CAISSES_SOCIALES.contains(cotisationDecompte.getTypeAssurance())) {
                    taux = taux.addTaux(cotisationDecompte.getTaux());
                }
            }
        }
        return taux;
    }

    private void add(TypeContribution type, EntreeContribution entree) {
        if (!tableauContributions.containsKey(type)) {
            tableauContributions.put(type, new TreeSet<TableauContributions.EntreeContribution>());
        }
        Collection<EntreeContribution> entrees = tableauContributions.get(type);
        entrees.add(entree);
    }

    public Iterable<Entry<TypeContribution, Collection<EntreeContribution>>> entrySet() {
        return tableauContributions.entrySet();
    }

    void setDecompteSalaireService(DecompteSalaireService decompteSalaireService) {
        this.decompteSalaireService = decompteSalaireService;
    }

    Collection<EntreeContribution> getEntreesAF() {
        return tableauContributions.get(TypeContribution.AF);
    }

    Collection<EntreeContribution> getEntreesAVS() {
        return tableauContributions.get(TypeContribution.AVS);
    }

    Collection<EntreeContribution> getEntreesAC() {
        return tableauContributions.get(TypeContribution.AC);
    }

    Collection<EntreeContribution> getEntreesAC2() {
        return tableauContributions.get(TypeContribution.AC2);
    }
}
