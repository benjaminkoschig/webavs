package ch.globaz.vulpecula.domain.models.association;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class CotisationFacture {
    private boolean montantMax = false;
    private boolean montantMin = false;
    private CotisationAssociationProfessionnelle cotisation;
    private List<LigneFactureAssociation> lignes = new ArrayList<LigneFactureAssociation>();

    public CotisationAssociationProfessionnelle getCotisation() {
        return cotisation;
    }

    public void setCotisation(CotisationAssociationProfessionnelle cotisation) {
        this.cotisation = cotisation;
    }

    public List<LigneFactureAssociation> getLignes() {
        Collections.sort(lignes);
        return lignes;
    }

    public void setLignes(List<LigneFactureAssociation> lignes) {
        this.lignes = lignes;
    }

    public String getLibelle() {
        return cotisation.getLibelle();
    }

    public String getLibelle(CodeLangue langue) {
        return cotisation.getLibelle(langue);
    }

    public void addLigne(LigneFactureAssociation ligne) {
        lignes.add(ligne);
    }

    public Montant getMontantTotal() {
        Montant montant = Montant.ZERO;
        Montant masse = Montant.ZERO;
        for (LigneFactureAssociation ligne : lignes) {
            montant = montant.add(ligne.getMontantCotisation());
            if (ligne.getMassePourCotisation() != null) {
                masse = masse.add(ligne.getMassePourCotisation());
            }
        }

        List<ParametreCotisationAssociation> parametres = null;
        parametres = VulpeculaRepositoryLocator.getParametreCotisationAssociationRepository()
                .findForFourchetteAndIdCotisation(cotisation.getId(), masse);

        Montant montantMinimum = Montant.ZERO;
        Montant montantMaximum = Montant.ZERO;
        for (ParametreCotisationAssociation parametreCotisationAssociation : parametres) {

            switch (parametreCotisationAssociation.getTypeParam()) {
                case MONTANT_MIN:
                    montantMinimum = parametreCotisationAssociation.getMontant();
                    break;
                case MONTANT_MAX:
                    montantMaximum = parametreCotisationAssociation.getMontant();
                    break;
                default:
                    break;
            }
        }

        if (!montantMinimum.isZero() && montant.less(montantMinimum)) {
            montantMin = true;
            return montantMinimum;
        }
        if (!montantMaximum.isZero() && montant.greater(montantMaximum)) {
            montantMax = true;
            return montantMaximum;
        }

        return montant;
    }

    public boolean isUneLigne() {
        return lignes.size() == 1;
    }

    /**
     * @return the montantMax
     */
    public boolean isMontantMax() {
        return montantMax;
    }

    /**
     * @return the montantMin
     */
    public boolean isMontantMin() {
        return montantMin;
    }

    public String getPrintOrderCotisation() {
        return cotisation.getPrintOrder();
    }

    public String getIdCotisation() {
        return cotisation.getId();
    }
}
