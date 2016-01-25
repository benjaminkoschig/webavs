package ch.globaz.corvus.utils.rentesverseesatort;

import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortWrapper;
import globaz.corvus.db.rentesverseesatort.wrapper.REPrestationDuePourCalculRenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.wrapper.RERenteVerseeATortWrapper;
import globaz.corvus.db.rentesverseesatort.wrapper.RERentesPourCalculRenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.wrapper.RETiersPourCalculRenteVerseeATortWrapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;

/**
 * Utilitaire permettant de calculer les rentes vers�e � tort sur une demande de rente.
 */
public class RECalculRentesVerseesATort {

    private static Collection<REDetailCalculRenteVerseeATort> calculerRentesVerseesATort(
            RECalculRentesVerseesATortWrapper wrapper, Periode zoneDeCalcul) {

        RECalculRentesVerseesATort.checkNotNull(wrapper, "[wrapper] can't be null");
        RECalculRentesVerseesATort.checkNotNull(wrapper.getIdDemandeRente(), "[wrapper.idDemandeRente] can't be null");
        RECalculRentesVerseesATort.checkNotNull(zoneDeCalcul, "[zoneDeCalcul] can't be null");

        Collection<REDetailCalculRenteVerseeATort> rentesVerseesATort = new ArrayList<REDetailCalculRenteVerseeATort>();

        // cas du premier calcul, on ne conna�t pas encore les bornes des rentes vers�e � tort donc on ne force pas
        // le calcul sur la p�riode zoneDeCalcul pass�e en param�tre
        for (RETiersPourCalculRenteVerseeATortWrapper unTiers : wrapper.getTiers()) {
            rentesVerseesATort.addAll(RECalculRentesVerseesATort.genererDetailPourPremierCalcul(
                    wrapper.getIdDemandeRente(), unTiers, zoneDeCalcul));
        }

        return rentesVerseesATort;
    }

    /**
     * <p>
     * Calcul et charge le d�tail des rentes vers�es � tort contenu dans le wrapper pass� en param�tre
     * </p>
     * <p>
     * M�thode � utiliser lors du 1�re appel, quand il n'y pas encore eu de mise en base du r�sultat du calcul. Si ce
     * n'est pas la 1�re fois que le calcul doit �tre fait, il faut utiliser la m�thode
     * {@link #chargerDetailRenteVerseeATort(RERenteVerseeATortWrapper)} <br/>
     * Ce calcul se base sur le mois du dernier paiement pour faire le calcul, il n'est donc valide que durant ce mois
     * l� (durant la saise de la demande et la pr�pration de la d�cision).
     * </p>
     * 
     * @param wrapper
     * @param dernierMoisPaiement
     * @return
     */
    public static Collection<REDetailCalculRenteVerseeATort> calculerRentesVerseesATort(
            RECalculRentesVerseesATortWrapper wrapper, String dernierMoisPaiement) {

        RECalculRentesVerseesATort.checkNotNull(wrapper, "[wrapper] can't be null");
        RECalculRentesVerseesATort.checkNotNull(dernierMoisPaiement, "[dernierMoisPaiement] can't be null");

        return RECalculRentesVerseesATort.calculerRentesVerseesATort(wrapper, new Periode("", dernierMoisPaiement));
    }

    /**
     * <p>
     * Permet de calculer le d�tail complet d'une rente vers�e � tort. Cette rente vers�e � tort doit d�j� avoir �t�
     * calcul� auparavent par la m�thode {@link #calculerRentesVerseesATort(RECalculRentesVerseesATortWrapper, String)}
     * et persist�)
     * </p>
     * <p>
     * Ce calcul se base sur la p�riode de calcul contenu dans la rente vers�e � tort
     * </p>
     * 
     * @param wrapper
     * @return
     */
    public static REDetailCalculRenteVerseeATort chargerDetailRenteVerseeATort(RERenteVerseeATortWrapper wrapper) {

        RECalculRentesVerseesATort.checkNotNull(wrapper, "[wrapper] can't be null");

        Long idRenteNouveauDroit = null;
        if (wrapper.getRenteNouveauDroit() != null) {
            idRenteNouveauDroit = wrapper.getRenteNouveauDroit().getIdRenteAccordee();
        }

        List<RELigneDetailCalculRenteVerseeATort> lignesDetail = new ArrayList<RELigneDetailCalculRenteVerseeATort>();
        Long idRenteAncienDroit = null;
        if (wrapper.getRenteAncienDroit() != null) {
            idRenteAncienDroit = wrapper.getRenteAncienDroit().getIdRenteAccordee();

            for (REPrestationDuePourCalculRenteVerseeATort prestationDue : wrapper.getRenteAncienDroit()
                    .getPrestationsDues()) {
                Periode periodePrestationDue = new Periode(prestationDue.getDateDebutPaiement(),
                        prestationDue.getDateFinPaiement());
                Periode periodeIntersection = wrapper.getPeriode().intersectionMois(periodePrestationDue);

                if (periodeIntersection != null) {
                    lignesDetail.add(new RELigneDetailCalculRenteVerseeATort(prestationDue.getMontant(),
                            periodeIntersection));
                }
            }
        }

        return new REDetailCalculRenteVerseeATort(wrapper.getIdRenteVerseeATort(), idRenteNouveauDroit,
                idRenteAncienDroit, wrapper.getIdDemandeRente(), lignesDetail, wrapper.getIdTiers(), wrapper.getNss(),
                wrapper.getPrenom(), wrapper.getNom(), wrapper.getDateNaissance(), wrapper.getDateDeces(),
                wrapper.getSexe(), wrapper.getNationalite(), wrapper.getType());
    }

    private static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static REDetailCalculRenteVerseeATort genererDetailAvecNouveauDroit(Long idDemandeRente,
            RETiersPourCalculRenteVerseeATortWrapper tiers, RERentesPourCalculRenteVerseeATort renteNouveauDroit,
            RERentesPourCalculRenteVerseeATort renteAncienDroit, Periode borneMaxZoneDeCalcul) {

        List<RELigneDetailCalculRenteVerseeATort> lignesDeDetail = new ArrayList<RELigneDetailCalculRenteVerseeATort>();

        Periode periodeRenteNouveauDroit = new Periode(renteNouveauDroit.getDateDebutDroit(),
                renteNouveauDroit.getDateFinDroit());

        Periode periodeRenteAncienDroit = new Periode(renteAncienDroit.getDateDebutDroit(),
                renteAncienDroit.getDateFinDroit());
        Periode zoneDeCalcul = borneMaxZoneDeCalcul.intersectionMois(periodeRenteNouveauDroit
                .intersectionMois(periodeRenteAncienDroit));

        if (zoneDeCalcul != null) {
            for (REPrestationDuePourCalculRenteVerseeATort unePrestationDue : renteAncienDroit.getPrestationsDues()) {

                Periode periodePrestationDue = new Periode(unePrestationDue.getDateDebutPaiement(),
                        unePrestationDue.getDateFinPaiement());
                Periode intersectionAvecZoneCalcul = periodePrestationDue.intersectionMois(zoneDeCalcul);

                if (intersectionAvecZoneCalcul != null) {
                    RELigneDetailCalculRenteVerseeATort uneLigneDeDetail = new RELigneDetailCalculRenteVerseeATort(
                            unePrestationDue.getMontant(), new Periode(intersectionAvecZoneCalcul.getDateDebut(),
                                    intersectionAvecZoneCalcul.getDateFin()));
                    lignesDeDetail.add(uneLigneDeDetail);
                }
            }
            if (!lignesDeDetail.isEmpty()) {
                return new REDetailCalculRenteVerseeATort(renteNouveauDroit.getIdRenteVerseeATort(),
                        renteNouveauDroit.getIdRenteAccordee(), renteAncienDroit.getIdRenteAccordee(), idDemandeRente,
                        lignesDeDetail, renteNouveauDroit.getIdTiersBeneficiaire(), tiers.getNss(), tiers.getPrenom(),
                        tiers.getNom(), tiers.getDateNaissance(), tiers.getDateDeces(), tiers.getSexe(),
                        tiers.getNationalite(), TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
            }
        }
        return null;
    }

    private static Collection<REDetailCalculRenteVerseeATort> genererDetailPourPremierCalcul(Long idDemandeRente,
            RETiersPourCalculRenteVerseeATortWrapper unTiers, Periode zoneDeCalcul) {

        Collection<REDetailCalculRenteVerseeATort> details = new ArrayList<REDetailCalculRenteVerseeATort>();
        Collection<Periode> periodesChevauchement = unTiers.getPeriodesChevauchementDesDroits();

        boolean isNouveauDroitAPI = RECalculRentesVerseesATort.isDroitAPI(unTiers.getRentesNouveauDroit());
        boolean isAncienDroitAPI = RECalculRentesVerseesATort.isDroitAPI(unTiers.getRentesAncienDroit());

        if (periodesChevauchement != null) {
            // pour chaque p�riode de chevauchement, parcours des rentes du nouveau droit actives durant cette p�riode
            for (Periode unePeriodeChevauchement : periodesChevauchement) {

                Collection<RERentesPourCalculRenteVerseeATort> rentesNouveauDroitDansLaPeriode = unTiers
                        .getRentesNouveauDroitDansPeriode(unePeriodeChevauchement);
                Collection<RERentesPourCalculRenteVerseeATort> renteAncienDroitDansLaPeriode = unTiers
                        .getRentesAncienDroitDansPeriode(unePeriodeChevauchement);

                if (rentesNouveauDroitDansLaPeriode.isEmpty() && !renteAncienDroitDansLaPeriode.isEmpty()) {
                    Periode zoneDeCalculFinale = unePeriodeChevauchement.intersectionMois(zoneDeCalcul);
                    if ((zoneDeCalcul != null) && (isNouveauDroitAPI == isAncienDroitAPI)) {
                        for (RERentesPourCalculRenteVerseeATort uneRenteAncienDroit : renteAncienDroitDansLaPeriode) {
                            REDetailCalculRenteVerseeATort unDetail = RECalculRentesVerseesATort
                                    .genererDetailSansNouveauDroit(idDemandeRente, unTiers, uneRenteAncienDroit,
                                            zoneDeCalculFinale);
                            if ((unDetail != null)
                                    && (unDetail.getMontantTotalVerseeATort().compareTo(BigDecimal.ZERO) != 0)) {
                                details.add(unDetail);
                            }
                        }
                    }
                } else {
                    for (RERentesPourCalculRenteVerseeATort uneRenteNouveauDroit : rentesNouveauDroitDansLaPeriode) {
                        // pour chaque rente du nouveau droit, on recherche les rentes de l'ancien droit pour la m�me
                        // personne dans la p�riode de la rente du nouveau droit
                        Periode periodeRenteNouveauDroit = new Periode(uneRenteNouveauDroit.getDateDebutDroit(),
                                uneRenteNouveauDroit.getDateFinDroit());

                        for (RERentesPourCalculRenteVerseeATort uneRentesAncienDroitDansPeriode : unTiers
                                .getRentesAncienDroitDansPeriode(periodeRenteNouveauDroit)) {
                            // on ignore les rentes API si la nouvelle rente n'est pas API. Et inversement on ignore
                            // toutes
                            // les rentes non-API si la nouvelle rente est une API
                            if (uneRenteNouveauDroit.isMemeFamilleGenrePrestation(uneRentesAncienDroitDansPeriode)) {
                                REDetailCalculRenteVerseeATort unDetail = RECalculRentesVerseesATort
                                        .genererDetailAvecNouveauDroit(idDemandeRente, unTiers, uneRenteNouveauDroit,
                                                uneRentesAncienDroitDansPeriode, zoneDeCalcul);
                                if ((unDetail != null)
                                        && (unDetail.getMontantTotalVerseeATort().compareTo(BigDecimal.ZERO) != 0)) {
                                    details.add(unDetail);
                                }
                            }
                        }
                    }
                }
            }
        }

        return details;
    }

    private static REDetailCalculRenteVerseeATort genererDetailSansNouveauDroit(Long idDemandeRente,
            RETiersPourCalculRenteVerseeATortWrapper tiers, RERentesPourCalculRenteVerseeATort renteAncienDroit,
            Periode zoneDeCalcul) {
        List<RELigneDetailCalculRenteVerseeATort> lignesDeDetail = new ArrayList<RELigneDetailCalculRenteVerseeATort>();

        for (REPrestationDuePourCalculRenteVerseeATort unePrestationDue : renteAncienDroit.getPrestationsDues()) {

            Periode periodePrestationDue = new Periode(unePrestationDue.getDateDebutPaiement(),
                    unePrestationDue.getDateFinPaiement());
            Periode intersectionAvecZoneCalcul = periodePrestationDue.intersectionMois(zoneDeCalcul);

            if (intersectionAvecZoneCalcul != null) {
                RELigneDetailCalculRenteVerseeATort uneLigneDeDetail = new RELigneDetailCalculRenteVerseeATort(
                        unePrestationDue.getMontant(), new Periode(intersectionAvecZoneCalcul.getDateDebut(),
                                intersectionAvecZoneCalcul.getDateFin()));
                lignesDeDetail.add(uneLigneDeDetail);
            }
        }
        if (!lignesDeDetail.isEmpty()) {
            return new REDetailCalculRenteVerseeATort(renteAncienDroit.getIdRenteVerseeATort(), null,
                    renteAncienDroit.getIdRenteAccordee(), idDemandeRente, lignesDeDetail,
                    renteAncienDroit.getIdTiersBeneficiaire(), tiers.getNss(), tiers.getPrenom(), tiers.getNom(),
                    tiers.getDateNaissance(), tiers.getDateDeces(), tiers.getSexe(), tiers.getNationalite(),
                    TypeRenteVerseeATort.PRESTATION_TOUCHEE_INDUMENT);
        }
        return null;
    }

    private static boolean isDroitAPI(Collection<RERentesPourCalculRenteVerseeATort> rentesAncienDroit) {
        boolean isDroitAPI = false;

        Iterator<RERentesPourCalculRenteVerseeATort> iterator = rentesAncienDroit.iterator();

        while (iterator.hasNext() && !isDroitAPI) {
            RERentesPourCalculRenteVerseeATort uneRente = iterator.next();
            if (uneRente.getCodePrestation().isAPI()) {
                isDroitAPI = true;
            }
        }

        return isDroitAPI;
    }
}
