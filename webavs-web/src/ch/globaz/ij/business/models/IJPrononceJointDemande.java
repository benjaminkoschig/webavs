package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestation;

public class IJPrononceJointDemande extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDemandePrestation demande;
    private JadeCodeSysteme genreReabilitation;
    private Integer jours;
    private IJSimplePrononce prononce;

    public IJPrononceJointDemande() {
        demande = new SimpleDemandePrestation();
        genreReabilitation = null;
        jours = 0;
        prononce = new IJSimplePrononce();
    }

    public String getCsGenreReadaptation() {
        return prononce.getCsGenreReadaptation();
    }

    public String getDateDebutPrononce() {
        return prononce.getDateDebutPrononce();
    }

    public String getDateFinPrononce() {
        return prononce.getDateFinPrononce();
    }

    public SimpleDemandePrestation getDemande() {
        return demande;
    }

    public JadeCodeSysteme getGenreReabilitation() {
        return genreReabilitation;
    }

    /**
     * Id du prononcé
     * 
     * @return id du prononcé
     */
    @Override
    public String getId() {
        return prononce.getId();
    }

    public String getIdParent() {
        return prononce.getIdParent();
    }

    public String getIdPrononce() {
        return prononce.getIdPrononce();
    }

    public String getIdTiers() {
        return demande.getIdTiers();
    }

    public Integer getJours() {
        return jours;
    }

    public IJSimplePrononce getPrononce() {
        return prononce;
    }

    @Override
    public String getSpy() {
        return demande.getSpy();
    }

    public boolean isPrononceSelectionne() {
        if ("1".equals(prononce.getPrononceSelectionne())) {
            return true;
        } else {
            return false;
        }
    }

    public void setCsGenreReadaptation(String csGenrePrononce) {
        prononce.setCsGenreReadaptation(csGenrePrononce);
    }

    public void setDateDebutPrononce(String dateDebutPrononce) {
        prononce.setDateDebutPrononce(dateDebutPrononce);
    }

    public void setDateFinPrononce(String dateFinPrononce) {
        prononce.setDateFinPrononce(dateFinPrononce);
    }

    public void setGenreReabilitation(JadeCodeSysteme genreReabilitation) {
        this.genreReabilitation = genreReabilitation;
    }

    /**
     * Id du prononcé
     */
    @Override
    public void setId(String id) {
        prononce.setId(id);

    }

    public void setIdParent(String idParent) {
        prononce.setIdParent(idParent);
    }

    public void setIdPrononce(String idPrononce) {
        prononce.setIdPrononce(idPrononce);
    }

    public void setIdTiers(String idTiers) {
        demande.setIdTiers(idTiers);
    }

    public void setJours(Integer jours) {
        this.jours = jours;
    }

    @Override
    public void setSpy(String spy) {
        demande.setSpy(spy);
    }
}
