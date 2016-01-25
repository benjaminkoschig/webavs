/**
 * 
 */
package ch.globaz.amal.business.models.parametremodel;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleParametreModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String anneeValiditeDebut = null;

    private String anneeValiditeFin = null;

    private Boolean codeAnnonceCaisse = null;

    private String codeSystemeFormule = null;

    private String codeTraitementDossier = null;

    private Boolean documentAssiste = null;

    private Boolean documentAvecListe = null;

    private Boolean documentContribuable = null;

    private Boolean documentPC = null;

    private Boolean documentSourcier = null;

    private String idFormule = null;
    private String idParametreModel = null;
    private String nombreElementListe = null;
    private String typeDocumentFamille = null;
    private String typeGed = null;
    private Boolean visibleAttribution = null;
    private Boolean visibleCorrespondance = null;

    public String getAnneeValiditeDebut() {
        return anneeValiditeDebut;
    }

    public String getAnneeValiditeFin() {
        return anneeValiditeFin;
    }

    public Boolean getCodeAnnonceCaisse() {
        return codeAnnonceCaisse;
    }

    /**
     * @return the codeSystemeFormule
     */
    public String getCodeSystemeFormule() {
        return codeSystemeFormule;
    }

    public String getCodeTraitementDossier() {
        return codeTraitementDossier;
    }

    /**
     * @return the documentAssiste
     */
    public Boolean getDocumentAssiste() {
        return documentAssiste;
    }

    /**
     * @return the documentAvecListe
     */
    public Boolean getDocumentAvecListe() {
        return documentAvecListe;
    }

    /**
     * @return the documentContribuable
     */
    public Boolean getDocumentContribuable() {
        return documentContribuable;
    }

    /**
     * @return the documentPC
     */
    public Boolean getDocumentPC() {
        return documentPC;
    }

    /**
     * @return the documentSourcier
     */
    public Boolean getDocumentSourcier() {
        return documentSourcier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idParametreModel;
    }

    public String getIdFormule() {
        return idFormule;
    }

    public String getIdParametreModel() {
        return idParametreModel;
    }

    /**
     * @return the nombreElementListe
     */
    public String getNombreElementListe() {
        return nombreElementListe;
    }

    /**
     * @return the typeDocumentFamille
     */
    public String getTypeDocumentFamille() {
        return typeDocumentFamille;
    }

    public String getTypeGed() {
        return typeGed;
    }

    /**
     * @return the visibleAttribution
     */
    public Boolean getVisibleAttribution() {
        return visibleAttribution;
    }

    /**
     * @return the visibleCorrespondance
     */
    public Boolean getVisibleCorrespondance() {
        return visibleCorrespondance;
    }

    public void setAnneeValiditeDebut(String anneeValiditeDebut) {
        this.anneeValiditeDebut = anneeValiditeDebut;
    }

    public void setAnneeValiditeFin(String anneeValiditeFin) {
        this.anneeValiditeFin = anneeValiditeFin;
    }

    public void setCodeAnnonceCaisse(Boolean codeAnnonceCaisse) {
        this.codeAnnonceCaisse = codeAnnonceCaisse;
    }

    /**
     * @param codeSystemeFormule
     *            the codeSystemeFormule to set
     */
    public void setCodeSystemeFormule(String codeSystemeFormule) {
        this.codeSystemeFormule = codeSystemeFormule;
    }

    public void setCodeTraitementDossier(String codeTraitementDossier) {
        this.codeTraitementDossier = codeTraitementDossier;
    }

    /**
     * @param documentAssiste
     *            the documentAssiste to set
     */
    public void setDocumentAssiste(Boolean documentAssiste) {
        this.documentAssiste = documentAssiste;
    }

    /**
     * @param documentAvecListe
     *            the documentAvecListe to set
     */
    public void setDocumentAvecListe(Boolean documentAvecListe) {
        this.documentAvecListe = documentAvecListe;
    }

    /**
     * @param documentContribuable
     *            the documentContribuable to set
     */
    public void setDocumentContribuable(Boolean documentContribuable) {
        this.documentContribuable = documentContribuable;
    }

    /**
     * @param documentPC
     *            the documentPC to set
     */
    public void setDocumentPC(Boolean documentPC) {
        this.documentPC = documentPC;
    }

    /**
     * @param documentSourcier
     *            the documentSourcier to set
     */
    public void setDocumentSourcier(Boolean documentSourcier) {
        this.documentSourcier = documentSourcier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idParametreModel = id;
    }

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    public void setIdParametreModel(String idParametreModel) {
        this.idParametreModel = idParametreModel;
    }

    /**
     * @param nombreElementListe
     *            the nombreElementListe to set
     */
    public void setNombreElementListe(String nombreElementListe) {
        this.nombreElementListe = nombreElementListe;
    }

    /**
     * @param typeDocumentFamille
     *            the typeDocumentFamille to set
     */
    public void setTypeDocumentFamille(String typeDocumentFamille) {
        this.typeDocumentFamille = typeDocumentFamille;
    }

    public void setTypeGed(String typeGed) {
        this.typeGed = typeGed;
    }

    /**
     * @param visibleAttribution
     *            the visibleAttribution to set
     */
    public void setVisibleAttribution(Boolean visibleAttribution) {
        this.visibleAttribution = visibleAttribution;
    }

    /**
     * @param visibleCorrespondance
     *            the visibleCorrespondance to set
     */
    public void setVisibleCorrespondance(Boolean visibleCorrespondance) {
        this.visibleCorrespondance = visibleCorrespondance;
    }

}
