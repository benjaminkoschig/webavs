package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * Vérifie que toutes les adhésions aux cotisations du poste de travail dispose
 * bien d'une periode
 * 
 * @author Arnaud Geiser (AGE) | Créé le 19 févr. 2014
 * 
 */
public class PosteTravailAdhesionCotisationDateValideSpecification extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(final PosteTravail poste) {
        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : poste.getAdhesionsCotisations()) {

            Date dateDebutPoste = poste.getDebutActivite();
            Date dateFinPoste = poste.getFinActivite();

            if (adhesionCotisationPosteTravail.getPeriode() == null) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_ADHESION_PERIODE_VALIDE_REQUISE);
            }
            // Il faut également que la période soit comprise dans celle de la période d'activité du poste de travail

            Date dateDebutCotisation = adhesionCotisationPosteTravail.getPeriode().getDateDebut();

            Date dateFinCotisation = null;
            if (adhesionCotisationPosteTravail.getPeriode().getDateFin() != null) {
                dateFinCotisation = adhesionCotisationPosteTravail.getPeriode().getDateFin();
            }

            if (dateDebutPoste == null) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_COTISATION_OBLIGATOIRE);
            }

            if (dateDebutCotisation != null) {
                if (dateDebutCotisation.before(dateDebutPoste)) {
                    addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_COTISATION_APRES_DATE_DEBUT_POSTE,
                            dateDebutCotisation.getSwissValue(), dateDebutPoste.getSwissValue());
                }

                if (dateFinPoste != null) {
                    if (dateDebutCotisation.after(dateFinPoste)) {
                        addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_COTISATION_AVANT_DATE_FIN_POSTE,
                                dateDebutCotisation.getSwissValue(), dateFinPoste.getSwissValue());
                    }
                }
            } else {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_REQUISE);
            }

            if (dateFinPoste != null) {
                if (dateFinCotisation != null) {
                    if (dateFinCotisation.after(dateFinPoste)) {
                        addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_FIN_COTISATION_AVANT_DATE_FIN_POSTE,
                                dateFinCotisation.getSwissValue(), dateFinPoste.getSwissValue());
                    }
                } else {
                    addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_FIN_COTISATION_REQUISE);
                }
            }

        }

        return true;
    }
}
