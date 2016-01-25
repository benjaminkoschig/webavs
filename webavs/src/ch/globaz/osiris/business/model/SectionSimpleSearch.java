package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class SectionSimpleSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum EtatSolde {
        NEGATIF("4"),
        POSITIF("3");
        private String etat;

        EtatSolde(String etat) {
            this.etat = etat;
        }

        public String getEtat() {
            return etat;
        }
    }

    private EtatSolde forEtatSolde;
    private String forIdCompteAnnexe;
    private String forIdTypeSection;
    private Collection<String> inIdsSection;
    private String likeIdExterne;

    public EtatSolde getForEtatSolde() {
        return forEtatSolde;
    }

    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    public Collection<String> getInIdsSection() {
        return inIdsSection;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public void setForEtatSolde(EtatSolde forEtatSolde) {
        this.forEtatSolde = forEtatSolde;
    }

    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    public void setForIdTypeSection(String forIdTypeSection) {
        this.forIdTypeSection = forIdTypeSection;
    }

    public void setInIdsSection(Collection<String> inIdsSection) {
        this.inIdsSection = inIdsSection;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    @Override
    public Class<SectionSimpleModel> whichModelClass() {
        return SectionSimpleModel.class;
    }
}
