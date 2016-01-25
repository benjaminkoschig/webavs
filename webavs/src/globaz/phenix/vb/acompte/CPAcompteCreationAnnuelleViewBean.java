package globaz.phenix.vb.acompte;

import globaz.phenix.process.acompte.CPProcessAcompteCreationAnnuelle;

public class CPAcompteCreationAnnuelleViewBean extends CPProcessAcompteCreationAnnuelle {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeReprise = "";
    private String forGenreAffilie = "";
    private String fromAffilieDebut = "";
    private String fromAffilieFin = "";
    private String idPassage = "";
    private String libellePassage = "";

    public void add() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public void delete() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    @Override
    public String getForAnneeReprise() {
        return forAnneeReprise;
    }

    @Override
    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    @Override
    public String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    @Override
    public String getFromAffilieFin() {
        return fromAffilieFin;
    }

    @Override
    public String getIdPassage() {
        return idPassage;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    public void retrieve() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    @Override
    public void setForAnneeReprise(String forAnneeReprise) {
        this.forAnneeReprise = forAnneeReprise;
    }

    @Override
    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    @Override
    public void setFromAffilieDebut(String fromAffilieDebut) {
        this.fromAffilieDebut = fromAffilieDebut;
    }

    @Override
    public void setFromAffilieFin(String fromAffilieFin) {
        this.fromAffilieFin = fromAffilieFin;
    }

    @Override
    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    public void update() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

}
