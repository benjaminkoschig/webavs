package ch.globaz.vulpecula.domain.models.association;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.specifications.cotisationAssociationProf.CotisationsAssociationProfessionnelleLibelleValideSpecification;
import ch.globaz.vulpecula.domain.specifications.cotisationAssociationProf.CotisationsAssociationProfessionnelleMandatoriesSpecification;
import ch.globaz.vulpecula.domain.specifications.cotisationAssociationProf.CotisationsAssociationProfessionnelleMontantMinMaxValideSpecification;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.osiris.db.comptes.CARubrique;

public class CotisationAssociationProfessionnelle implements DomainEntity {
    private String id;
    private Administration associationProfessionnelle;
    private String libelle;
    private String libelleFR;
    private String libelleDE;
    private String libelleIT;
    private Taux masseSalarialeDefaut;
    private CategorieFactureAssociationProfessionnelle facturerDefaut;
    private String spy;
    private GenreCotisationAssociationProfessionnelle genre;
    private String idRubrique;
    private CARubrique rubrique;
    private String printOrder;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelleFR() {
        return libelleFR;
    }

    public void setLibelleFR(String libelleFR) {
        this.libelleFR = libelleFR;
    }

    public String getLibelleDE() {
        return libelleDE;
    }

    public void setLibelleDE(String libelleDE) {
        this.libelleDE = libelleDE;
    }

    public String getLibelleIT() {
        return libelleIT;
    }

    public void setLibelleIT(String libelleIT) {
        this.libelleIT = libelleIT;
    }

    public String getLibelle(CodeLangue langue) {
        String libelle = "";
        if (CodeLangue.FR.equals(langue)) {
            libelle = getLibelleFR();
        } else if (CodeLangue.DE.equals(langue)) {
            libelle = getLibelleDE();
        } else if (CodeLangue.IT.equals(langue)) {
            libelle = getLibelleIT();
        } else {
            libelle = getLibelle();
        }

        if (!((libelle != null) && (libelle.trim().length() > 0))) {
            libelle = getLibelle();
        }

        return libelle;
    }

    public Taux getMasseSalarialeDefaut() {
        return masseSalarialeDefaut;
    }

    public void setMasseSalarialeDefaut(Taux masseSalarialeDefaut) {
        this.masseSalarialeDefaut = masseSalarialeDefaut;
    }

    public CategorieFactureAssociationProfessionnelle getFacturerDefaut() {
        return facturerDefaut;
    }

    public void setFacturerDefaut(final CategorieFactureAssociationProfessionnelle facturerDefaut) {
        this.facturerDefaut = facturerDefaut;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    public String getIdAssociationProfessionnelle() {
        return associationProfessionnelle.getId();
    }

    public Administration getAssociationProfessionnelle() {
        return associationProfessionnelle;
    }

    public void setAssociationProfessionnelle(Administration associationProfessionnelle) {
        this.associationProfessionnelle = associationProfessionnelle;
    }

    public String getCodeAssociationProfessionnelle() {
        return associationProfessionnelle.getCodeAdministration();
    }

    public GenreCotisationAssociationProfessionnelle getGenre() {
        return genre;
    }

    public void setGenre(GenreCotisationAssociationProfessionnelle genre) {
        this.genre = genre;
    }

    /**
     * @return the idRubrique
     */
    public CARubrique getRubrique() {
        return rubrique;
    }

    /**
     * @param idRubrique the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setRubrique(CARubrique rub) {
        rubrique = rub;

        if (rub != null && rub.getId() != null && rub.getId().length() != 0 && !rub.getId().equals("0")) {
            setIdRubrique(rub.getIdRubrique());
        }
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return the printOrder
     */
    public String getPrintOrder() {
        if (printOrder != null && printOrder.length() < 2) {
            return "0" + printOrder;
        }
        return printOrder;
    }

    /**
     * @param printOrder the printOrder to set
     */
    public void setPrintOrder(String printOrder) {
        this.printOrder = printOrder;
    }

    public static Map<AssociationGenre, Collection<CotisationAssociationProfessionnelle>> groupByAssociationGenre(
            List<CotisationAssociationProfessionnelle> cotisations) {
        return Multimaps.index(cotisations, new Function<CotisationAssociationProfessionnelle, AssociationGenre>() {
            @Override
            public AssociationGenre apply(CotisationAssociationProfessionnelle associationCotisation) {
                return new AssociationGenre(associationCotisation.getAssociationProfessionnelle(),
                        associationCotisation.getGenre());
            }
        }).asMap();
    }

    public boolean mustBeFetched() {
        return id != null && spy == null;
    }

    public void validate(List<CotisationAssociationProfessionnelle> associationsList)
            throws UnsatisfiedSpecificationException {

        final CotisationsAssociationProfessionnelleLibelleValideSpecification libelleValide = new CotisationsAssociationProfessionnelleLibelleValideSpecification(
                associationsList);
        libelleValide.isSatisfiedBy(this);

        final CotisationsAssociationProfessionnelleMandatoriesSpecification mandatories = new CotisationsAssociationProfessionnelleMandatoriesSpecification();
        mandatories.isSatisfiedBy(this);

        final CotisationsAssociationProfessionnelleMontantMinMaxValideSpecification montantMinMaxValide = new CotisationsAssociationProfessionnelleMontantMinMaxValideSpecification();
        montantMinMaxValide.isSatisfiedBy(this);

    }
}
