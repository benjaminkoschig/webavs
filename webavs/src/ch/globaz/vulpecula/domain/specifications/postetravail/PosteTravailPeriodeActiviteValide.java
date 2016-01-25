package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * @author Jonas Paratte (JPA) | Créé le 25.04.2014
 * 
 *         La date du poste doit être incluse dans la période d'affiliation de
 *         l'employeur
 */
public class PosteTravailPeriodeActiviteValide extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(final PosteTravail posteTravail) {
        Date dateDebutAffiliationEmployeur = null;
        Date dateFinAffiliationEmployeur = null;

        if (posteTravail.getEmployeur() == null) {
            return false;
        }

        if (posteTravail.getEmployeur().getDateDebut() != null
                && !posteTravail.getEmployeur().getDateDebut().equals("0")
                && !posteTravail.getEmployeur().getDateDebut().isEmpty()) {
            dateDebutAffiliationEmployeur = new Date(posteTravail.getEmployeur().getDateDebut());
        } else {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_AFFILIATION_REQUISE);
            return false;
        }

        if (posteTravail.getEmployeur().getDateFin() != null && !posteTravail.getEmployeur().getDateFin().equals("0")
                && !posteTravail.getEmployeur().getDateFin().isEmpty()) {
            dateFinAffiliationEmployeur = new Date(posteTravail.getEmployeur().getDateFin());
        }

        if (posteTravail.getDebutActivite() != null) {
            if (posteTravail.getDebutActivite().before(dateDebutAffiliationEmployeur)) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_PERIODE_ACTIVITE_APRES_DEBUT_AFFILIATION,
                        posteTravail.getDebutActiviteAsSwissValue(), dateDebutAffiliationEmployeur.getSwissValue());
            }

            if (dateFinAffiliationEmployeur != null) {
                if (posteTravail.getDebutActivite().after(dateFinAffiliationEmployeur)) {
                    addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_PERIODE_ACTIVITE_AVANT_FIN_AFFILIATION,
                            posteTravail.getDebutActiviteAsSwissValue(), dateDebutAffiliationEmployeur.getSwissValue());
                }
            }
        } else {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_DEBUT_REQUISE);
        }

        if (posteTravail.getFinActivite() != null) {
            if (posteTravail.getEmployeur().getDateFin() != null
                    && posteTravail.getEmployeur().getDateFin().length() > 0
                    && !posteTravail.getEmployeur().getDateFin().equals("0")) {
                if (dateFinAffiliationEmployeur != null) {
                    if (posteTravail.getFinActivite().after(dateFinAffiliationEmployeur)) {
                        addMessage(SpecificationMessage.POSTE_TRAVAIL_DATE_FIN_PERIODE_ACTIVITE_AVANT_FIN_AFFILIATION,
                                posteTravail.getFinActiviteAsSwissValue(), dateFinAffiliationEmployeur.getSwissValue());
                    }
                }
            }
        }

        return true;
    }
}
