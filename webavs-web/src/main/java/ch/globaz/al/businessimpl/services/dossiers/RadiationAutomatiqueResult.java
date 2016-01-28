package ch.globaz.al.businessimpl.services.dossiers;

import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * Représente le résultat d'une radiation automatique de dossier
 * 
 * @see ch.globaz.al.businessimpl.services.dossiers.RadiationAutomatiqueService
 * @author jts
 * 
 */
public class RadiationAutomatiqueResult implements Comparable<RadiationAutomatiqueResult> {

    private DossierComplexModel dossier = null;
    private String motifRadiation = null;

    @Override
    public int compareTo(RadiationAutomatiqueResult o) {

        // si affiliés identique
        if (dossier.getDossierModel().getNumeroAffilie().equals(o.dossier.getDossierModel().getNumeroAffilie())) {

            String nomPrenomThis = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1()
                    + " "
                    + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation2();

            String nomPrenomOther = o.dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1()
                    + " "
                    + o.dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation2();

            // nom prénom identiques
            if (nomPrenomThis.equals(nomPrenomOther)) {
                return dossier.getDossierModel().getId().compareTo(o.dossier.getDossierModel().getId());
            } else {
                // comparaison id dossier
                return nomPrenomThis.compareTo(nomPrenomOther);
            }
        } else {
            return dossier.getDossierModel().getNumeroAffilie()
                    .compareTo(o.dossier.getDossierModel().getNumeroAffilie());
        }
    }

    public DossierComplexModel getDossier() {
        return dossier;
    }

    public String getMotifRadiation() {
        return motifRadiation;
    }

    public void setDossier(DossierComplexModel dossier) {
        this.dossier = dossier;
    }

    public void setMotifRadiation(String motifRadiation) {
        this.motifRadiation = motifRadiation;
    }
}