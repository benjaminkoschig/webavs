package ch.globaz.perseus.business.models.donneesfinancieres;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

public class FortuneSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String betweenDateCessionDebut = null;
    private String betweenDateCessionFin = null;
    private String betweenDateFinDebut = null;
    private String betweenDateFinFin = null;
    private String forCsCaisse = null;
    private String forIdDemande = null;
    private String forIdMembreFamille = null;
    private List<String> inType = null;

    public FortuneSearchModel() {
        super();
        inType = new ArrayList<String>();

        FortuneType[] rt = FortuneType.values();
        for (int i = 0; i < rt.length; i++) {
            inType.add(rt[i].getId().toString());
        }
    }

    public String getBetweenDateCessionDebut() {
        return betweenDateCessionDebut;
    }

    public String getBetweenDateCessionFin() {
        return betweenDateCessionFin;
    }

    public String getBetweenDateFinDebut() {
        return betweenDateFinDebut;
    }

    public String getBetweenDateFinFin() {
        return betweenDateFinFin;
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdMembreFamille
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return the inType
     */
    public List<String> getInType() {
        return inType;
    }

    public void setBetweenDateCessionDebut(String betweenDateCessionDebut) {
        this.betweenDateCessionDebut = betweenDateCessionDebut;
    }

    public void setBetweenDateCessionFin(String betweenDateCessionFin) {
        this.betweenDateCessionFin = betweenDateCessionFin;
    }

    public void setBetweenDateFinDebut(String betweenDateFinDebut) {
        this.betweenDateFinDebut = betweenDateFinDebut;
    }

    public void setBetweenDateFinFin(String betweenDateFinFin) {
        this.betweenDateFinFin = betweenDateFinFin;
    }

    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdMembreFamille
     *            the forIdMembreFamille to set
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    /**
     * Attention, par défaut le constructeur remplit ce critère sur tous les types de revenus. Il peut toutefois être
     * modifié pour rechercher une donnée finncière spécifique.
     * 
     * @param inType
     *            liste des types
     */
    public void setInType(List<String> inType) {
        this.inType = inType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Fortune.class;
    }

}
