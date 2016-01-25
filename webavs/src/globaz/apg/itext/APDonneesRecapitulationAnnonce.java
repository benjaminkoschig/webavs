package globaz.apg.itext;

import globaz.apg.utils.APListeRecapitulationAnnonceUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class APDonneesRecapitulationAnnonce {

    private Map<String, APLigneRecapitulationAnnonce> lignes;
    private Double montantTotalFraisGarde;

    public APDonneesRecapitulationAnnonce() {
        super();
        lignes = new HashMap<String, APLigneRecapitulationAnnonce>();
        montantTotalFraisGarde = null;
    }

    public void addFraisGarde(Double fraisGarde) {
        montantTotalFraisGarde = APListeRecapitulationAnnonceUtils.addIfNotNull(montantTotalFraisGarde, fraisGarde);
    }

    public void addLigne(APLigneRecapitulationAnnonce uneLigne) {
        if (lignes.containsKey(uneLigne.getCodeGenreService())) {
            APLigneRecapitulationAnnonce ligneDejaExistante = lignes.get(uneLigne.getCodeGenreService());
            ligneDejaExistante.addLigne(uneLigne);
        } else {
            lignes.put(uneLigne.getCodeGenreService(), uneLigne);
        }
    }

    public APLigneRecapitulationAnnonce getLignePourGenreService(String csGenreService) {
        return lignes.get(csGenreService);
    }

    public Collection<APLigneRecapitulationAnnonce> getLignes() {
        return lignes.values();
    }

    public Double getMontantTotalAC() {
        Double montantTotalAC = null;
        for (APLigneRecapitulationAnnonce uneLigne : lignes.values()) {
            montantTotalAC = APListeRecapitulationAnnonceUtils.addIfNotNull(montantTotalAC, uneLigne.getMontantAC());
        }
        return montantTotalAC;
    }

    public Double getMontantTotalFraisGarde() {
        return montantTotalFraisGarde;
    }

    public Double getMontantTotalRestitutions() {
        Double montantTotalRestitutions = null;
        for (APLigneRecapitulationAnnonce uneLigne : lignes.values()) {
            montantTotalRestitutions = APListeRecapitulationAnnonceUtils.addIfNotNull(montantTotalRestitutions,
                    uneLigne.getMontantRestitutions());
        }
        return montantTotalRestitutions;
    }

    public Integer getNombreCartesDuplicata() {
        Integer nombreCarteDuplicata = null;
        for (APLigneRecapitulationAnnonce uneLigne : lignes.values()) {
            nombreCarteDuplicata = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreCarteDuplicata,
                    uneLigne.getNombreCartesDuplicata());
        }
        return nombreCarteDuplicata;
    }

    public Integer getNombreCartesQuestionnaires() {
        Integer nombreCartesQuestionnaires = null;
        for (APLigneRecapitulationAnnonce uneLigne : lignes.values()) {
            nombreCartesQuestionnaires = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreCartesQuestionnaires,
                    uneLigne.getNombreCartesQuestionnaires());
        }
        return nombreCartesQuestionnaires;
    }

    public Integer getNombreCartesRectificatives() {
        Integer nombreCartesRectificatives = null;
        for (APLigneRecapitulationAnnonce uneLigne : lignes.values()) {
            nombreCartesRectificatives = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreCartesRectificatives,
                    uneLigne.getNombreCartesRestitutions(), uneLigne.getNombreCartesPaiementRetroactifs());
        }
        return nombreCartesRectificatives;
    }

    public Integer getNombreTotalCartes() {
        return APListeRecapitulationAnnonceUtils.addIfNotNull(getNombreCartesDuplicata(),
                getNombreCartesQuestionnaires(), getNombreCartesRectificatives());
    }
}
