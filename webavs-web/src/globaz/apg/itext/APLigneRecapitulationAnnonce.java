package globaz.apg.itext;

import globaz.apg.utils.APListeRecapitulationAnnonceUtils;

public class APLigneRecapitulationAnnonce {

    private String codeGenreService;
    private Double montantDuplicata;
    private Double montantPaiementRetroactifs;
    private Double montantQuestionnaires;
    private Double montantRestitutions;
    private Integer nombreCartesDuplicata;
    private Integer nombreCartesPaiementRetroactifs;
    private Integer nombreCartesQuestionnaires;
    private Integer nombreCartesRestitutions;
    private Integer nombreJoursDuplicata;
    private Integer nombreJoursPaiementRetroactifs;
    private Integer nombreJoursQuestionnaires;
    private Integer nombreJoursRestitutions;

    protected APLigneRecapitulationAnnonce() {
        super();

        codeGenreService = null;
        montantDuplicata = null;
        montantQuestionnaires = null;
        montantRestitutions = null;
        montantPaiementRetroactifs = null;
        nombreCartesDuplicata = null;
        nombreCartesQuestionnaires = null;
        nombreCartesRestitutions = null;
        nombreCartesPaiementRetroactifs = null;
        nombreJoursDuplicata = null;
        nombreJoursQuestionnaires = null;
        nombreJoursRestitutions = null;
        nombreJoursPaiementRetroactifs = null;
    }

    public void addLigne(APLigneRecapitulationAnnonce uneLigne) {
        montantDuplicata = APListeRecapitulationAnnonceUtils.addIfNotNull(montantDuplicata,
                uneLigne.getMontantDuplicata());
        montantQuestionnaires = APListeRecapitulationAnnonceUtils.addIfNotNull(montantQuestionnaires,
                uneLigne.getMontantQuestionnaires());
        montantRestitutions = APListeRecapitulationAnnonceUtils.addIfNotNull(montantRestitutions,
                uneLigne.getMontantRestitutions());
        montantPaiementRetroactifs = APListeRecapitulationAnnonceUtils.addIfNotNull(montantPaiementRetroactifs,
                uneLigne.getMontantPaiementRetroactifs());
        nombreCartesDuplicata = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreCartesDuplicata,
                uneLigne.getNombreCartesDuplicata());
        nombreCartesQuestionnaires = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreCartesQuestionnaires,
                uneLigne.getNombreCartesQuestionnaires());
        nombreCartesRestitutions = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreCartesRestitutions,
                uneLigne.getNombreCartesRestitutions());
        nombreCartesPaiementRetroactifs = APListeRecapitulationAnnonceUtils.addIfNotNull(
                nombreCartesPaiementRetroactifs, uneLigne.getNombreCartesPaiementRetroactifs());
        nombreJoursDuplicata = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreJoursDuplicata,
                uneLigne.getNombreJoursDuplicata());
        nombreJoursQuestionnaires = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreJoursQuestionnaires,
                uneLigne.getNombreJoursQuestionnaires());
        nombreJoursRestitutions = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreJoursRestitutions,
                uneLigne.getNombreJoursRestitutions());
        nombreJoursPaiementRetroactifs = APListeRecapitulationAnnonceUtils.addIfNotNull(nombreJoursPaiementRetroactifs,
                uneLigne.getNombreJoursPaiementRetroactifs());
    }

    public String getCodeGenreService() {
        return codeGenreService;
    }

    public Double getMontantAC() {
        return APListeRecapitulationAnnonceUtils.addIfNotNull(montantQuestionnaires, montantDuplicata,
                montantPaiementRetroactifs);
    }

    public Double getMontantDuplicata() {
        return montantDuplicata;
    }

    public Double getMontantPaiementRetroactifs() {
        return montantPaiementRetroactifs;
    }

    public Double getMontantQuestionnaires() {
        return montantQuestionnaires;
    }

    public Double getMontantRestitutions() {
        return montantRestitutions;
    }

    public Integer getNombreCartesDuplicata() {
        return nombreCartesDuplicata;
    }

    public Integer getNombreCartesPaiementRetroactifs() {
        return nombreCartesPaiementRetroactifs;
    }

    public Integer getNombreCartesQuestionnaires() {
        return nombreCartesQuestionnaires;
    }

    public Integer getNombreCartesRestitutions() {
        return nombreCartesRestitutions;
    }

    public Integer getNombreJoursDuplicata() {
        return nombreJoursDuplicata;
    }

    public Integer getNombreJoursPaiementRetroactifs() {
        return nombreJoursPaiementRetroactifs;
    }

    public Integer getNombreJoursQuestionnaires() {
        return nombreJoursQuestionnaires;
    }

    public Integer getNombreJoursRestitutions() {
        return nombreJoursRestitutions;
    }

    public void setCodeGenreService(String codeGenreService) {
        this.codeGenreService = codeGenreService;
    }

    public void setMontantDuplicata(Double montantDuplicata) {
        this.montantDuplicata = montantDuplicata;
    }

    public void setMontantPaiementRetroactifs(Double montantPaiementRetroactifs) {
        this.montantPaiementRetroactifs = montantPaiementRetroactifs;
    }

    public void setMontantQuestionnaires(Double montantQuestionnaires) {
        this.montantQuestionnaires = montantQuestionnaires;
    }

    public void setMontantRestitutions(Double montantRestitutions) {
        this.montantRestitutions = montantRestitutions;
    }

    public void setNombreCartesDuplicata(Integer nombreCartesDuplicata) {
        this.nombreCartesDuplicata = nombreCartesDuplicata;
    }

    public void setNombreCartesPaiementRetroactifs(Integer nombreCartesPaiementRetroactifs) {
        this.nombreCartesPaiementRetroactifs = nombreCartesPaiementRetroactifs;
    }

    public void setNombreCartesQuestionnaires(Integer nombreCartesQuestionnaires) {
        this.nombreCartesQuestionnaires = nombreCartesQuestionnaires;
    }

    public void setNombreCartesRestitutions(Integer nombreCartesRestitutions) {
        this.nombreCartesRestitutions = nombreCartesRestitutions;
    }

    public void setNombreJoursDuplicata(Integer nombreJoursDuplicata) {
        this.nombreJoursDuplicata = nombreJoursDuplicata;
    }

    public void setNombreJoursPaiementRetroactifs(Integer nombreJoursPaiementRetroactifs) {
        this.nombreJoursPaiementRetroactifs = nombreJoursPaiementRetroactifs;
    }

    public void setNombreJoursQuestionnaires(Integer nombreJoursQuestionnaires) {
        this.nombreJoursQuestionnaires = nombreJoursQuestionnaires;
    }

    public void setNombreJoursRestitutions(Integer nombreJoursRestitutions) {
        this.nombreJoursRestitutions = nombreJoursRestitutions;
    }
}