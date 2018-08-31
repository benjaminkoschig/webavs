package ch.globaz.vulpecula.documents.listesinternes;

import globaz.naos.api.IAFAssurance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

public class RecapitulatifParGenreCaisse {
    private static final String SECTEUR_AVS = "2000";
    private static final String KEY_AVS = "AVS";

    private final Annee annee;
    private final Map<String, List<RecapitulatifParGenreCaisseDTO>> data;
    private final List<RecapitulatifParGenreCaisseDTO> dataAVS;
    private final TaxationOfficeContainer toContainer;

    static class Entry {
        private final String libelle;
        private final Collection<String> comptes;
        private final Montant cotisations;
        private final Montant masseSalariale;

        public Entry(String libelle, Collection<String> comptes, Montant cotisations, Montant masseSalariale) {
            this.libelle = libelle;
            this.comptes = comptes;
            this.cotisations = cotisations;
            this.masseSalariale = masseSalariale;
        }

        public String getLibelle() {
            return libelle;
        }

        public Collection<String> getComptes() {
            return comptes;
        }

        public Montant getCotisations() {
            return cotisations;
        }

        public Montant getMasseSalariale() {
            return masseSalariale;
        }
    }

    public RecapitulatifParGenreCaisse(Annee annee, List<RecapitulatifParGenreCaisseDTO> recaps,
            List<RecapitulatifParGenreCaisseDTO> recapsAVS, TaxationOfficeContainer toContainer) {
        this.annee = annee;
        this.toContainer = toContainer;
        data = groupByLibelle(recaps);
        dataAVS = recapsAVS;

    }

    private Map<String, List<RecapitulatifParGenreCaisseDTO>> groupByLibelle(List<RecapitulatifParGenreCaisseDTO> recaps) {
        Map<String, List<RecapitulatifParGenreCaisseDTO>> data = new LinkedHashMap<String, List<RecapitulatifParGenreCaisseDTO>>();
        for (RecapitulatifParGenreCaisseDTO recap : recaps) {
            if (TypeAssurance.CPR_EMPLOYEUR.getValue().equals(recap.getType())) {
                continue;
            }
            String libelle = getLibelle(recap);
            if (!data.containsKey(libelle)) {
                data.put(libelle, new ArrayList<RecapitulatifParGenreCaisseDTO>());
            }
            data.get(libelle).add(recap);
        }
        return data;
    }

    private String getLibelle(RecapitulatifParGenreCaisseDTO recap) {
        if (SECTEUR_AVS.equals(recap.getIdSecteur())) {
            return KEY_AVS;
        } else if (recap.getType() == null || TypeAssurance.AUTRES.getValue().equals(recap.getType())) {
            return recap.getCotisation();
        } else {
            if (!IAFAssurance.PARITAIRE.equals(recap.getGenre())) {
                return recap.getTypeLibelle() + " IND";
            } else {
                return recap.getTypeLibelle();
            }
        }
    }

    public Annee getAnnee() {
        return annee;
    }

    public Collection<Entry> entries() {
        Collection<Entry> col = new ArrayList<Entry>();
        for (Map.Entry<String, List<RecapitulatifParGenreCaisseDTO>> row : data.entrySet()) {
            List<String> idsExternes = new ArrayList<String>();
            Montant masse = Montant.ZERO;
            Montant montant = Montant.ZERO;

            // Dans le cas de l'AVS, on soustrait les montants des données AVS car il s'agit de comptes de produit
            if (KEY_AVS.equals(row.getKey())) {
                for (RecapitulatifParGenreCaisseDTO recap : dataAVS) {
                    idsExternes.add(recap.getIdExterne());
                    montant = montant.substract(new Montant(recap.getMontant()));
                }
            }
            for (RecapitulatifParGenreCaisseDTO recap : row.getValue()) {
                idsExternes.add(recap.getIdExterne());
                masse = masse.add(new Montant(recap.getMasse()));
                montant = montant.add(new Montant(recap.getMontant()));
            }
            Collections.sort(idsExternes);
            col.add(new Entry(row.getKey(), //
                    idsExternes, //
                    montant.substract(toContainer.getMontant(idsExternes)),  //
                    masse.substract(toContainer.getMasse(idsExternes))));
        }
        return col;
    }
}
