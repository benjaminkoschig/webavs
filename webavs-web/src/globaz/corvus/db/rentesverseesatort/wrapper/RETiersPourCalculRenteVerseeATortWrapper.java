package globaz.corvus.db.rentesverseesatort.wrapper;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

public class RETiersPourCalculRenteVerseeATortWrapper {

    static Collection<Periode> ajouterPeriodeAuDroit(Collection<Periode> droit, Periode periode) {
        if (!RETiersPourCalculRenteVerseeATortWrapper.ajouterPeriodeAuDroitRecursive(droit, periode, false)) {
            droit.add(periode);
        }
        return droit;
    }

    /**
     * @param droit
     * @param periode
     * @param hasBeenAddedOnce
     *            si la période a été ajoutée au moins une fois dans la liste (en la fusionnant à une autre période)
     * @return vrai si la période a été ajoutée au moins une fois, sinon faux
     */
    private static boolean ajouterPeriodeAuDroitRecursive(Collection<Periode> droit, Periode periode,
            boolean hasBeenAddedOnce) {
        Iterator<Periode> iterateurPeriodesDuDroit = droit.iterator();
        boolean hasBeenAdded = false;
        while (iterateurPeriodesDuDroit.hasNext()) {
            Periode unePeriodeDuDroit = iterateurPeriodesDuDroit.next();
            if (hasBeenAddedOnce
                    && RETiersPourCalculRenteVerseeATortWrapper.unePeriodeEnglobeUneAutre(unePeriodeDuDroit, periode)) {
                // si il y a eu un ajout de période et qu'une des deux englobe complètement l'autre, on ne fait rien
            } else {
                Periode periodeUnion = unePeriodeDuDroit.unionMois(periode);
                if (periodeUnion != null) {
                    droit.remove(unePeriodeDuDroit);
                    droit.add(periodeUnion);
                    hasBeenAdded = true;
                }
            }
        }
        if (hasBeenAdded) {
            return RETiersPourCalculRenteVerseeATortWrapper.ajouterPeriodeAuDroitRecursive(droit, periode, true);
        } else {
            return hasBeenAddedOnce;
        }
    }

    static boolean unePeriodeEnglobeUneAutre(Periode periode1, Periode periode2) {

        if ((periode1 == null) || (periode2 == null)) {
            return false;
        }

        if (periode1.equals(periode2)) {
            return true;
        }

        if (periode1.comparerChevauchement(periode2) == ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES) {
            return false;
        }

        String dateDebutPeriode1 = periode1.getDateDebut();
        String dateFinPeriode1 = periode1.getDateFin();

        if (JadeDateUtil.isGlobazDate(dateDebutPeriode1)) {
            dateDebutPeriode1 = JadeDateUtil.convertDateMonthYear(dateDebutPeriode1);
        }
        if (JadeDateUtil.isGlobazDate(dateFinPeriode1)) {
            dateFinPeriode1 = JadeDateUtil.convertDateMonthYear(dateFinPeriode1);
        } else if (JadeStringUtil.isBlank(dateFinPeriode1)) {
            dateFinPeriode1 = "12.2999";
        }

        // Récupération et conversion des date de début / date de fin des deux période au format MM.AAAA
        String dateDebutPeriode2 = periode2.getDateDebut();
        String dateFinPeriode2 = periode2.getDateFin();

        if (JadeDateUtil.isGlobazDate(dateDebutPeriode2)) {
            dateDebutPeriode2 = JadeDateUtil.convertDateMonthYear(dateDebutPeriode2);
        }
        if (JadeDateUtil.isGlobazDate(dateFinPeriode2)) {
            dateFinPeriode2 = JadeDateUtil.convertDateMonthYear(dateFinPeriode2);
        } else if (JadeStringUtil.isBlank(dateFinPeriode2)) {
            dateFinPeriode2 = "12.2999";
        }

        if (((JadeDateUtil.isDateMonthYearAfter(dateDebutPeriode1, dateDebutPeriode2) || dateDebutPeriode1
                .equals(dateDebutPeriode2)) && (JadeDateUtil.isDateMonthYearBefore(dateFinPeriode1, dateFinPeriode2) || dateFinPeriode1
                .equals(dateFinPeriode2)))) {
            return true;
        }

        if (((JadeDateUtil.isDateMonthYearBefore(dateDebutPeriode1, dateDebutPeriode2) || dateDebutPeriode1
                .equals(dateDebutPeriode2)) && (JadeDateUtil.isDateMonthYearAfter(dateFinPeriode1, dateFinPeriode2) || dateFinPeriode1
                .equals(dateFinPeriode2)))) {
            return true;
        }

        return false;
    }

    private String dateDeces;
    private String dateNaissance;
    private Long idTiers;
    private String nationalite;
    private String nom;
    private NumeroSecuriteSociale nss;
    private String prenom;
    private Collection<RERentesPourCalculRenteVerseeATort> rentesAncienDroit;
    private Collection<RERentesPourCalculRenteVerseeATort> rentesNouveauDroit;
    private String sexe;

    public RETiersPourCalculRenteVerseeATortWrapper() {
        super();

        dateDeces = null;
        dateNaissance = null;
        idTiers = null;
        nationalite = null;
        nom = null;
        nss = null;
        prenom = null;
        rentesAncienDroit = null;
        rentesNouveauDroit = null;
        sexe = null;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public Long getIdTiers() {
        return idTiers;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getNom() {
        return nom;
    }

    public NumeroSecuriteSociale getNss() {
        return nss;
    }

    public Collection<Periode> getPeriodeAncienDroit() {
        return getPeriodeDuDroit(rentesAncienDroit);
    }

    Collection<Periode> getPeriodeDuDroit(Collection<RERentesPourCalculRenteVerseeATort> droit) {
        if (droit == null) {
            return null;
        }

        Collection<Periode> periodesDuDroit = new CopyOnWriteArrayList<Periode>();

        for (RERentesPourCalculRenteVerseeATort uneRenteDuDroit : droit) {
            Periode periodeDeLaRente = new Periode(uneRenteDuDroit.getDateDebutDroit(),
                    uneRenteDuDroit.getDateFinDroit());

            if (periodesDuDroit.isEmpty()) {
                periodesDuDroit.add(periodeDeLaRente);
            } else {
                periodesDuDroit = RETiersPourCalculRenteVerseeATortWrapper.ajouterPeriodeAuDroit(periodesDuDroit,
                        periodeDeLaRente);
            }
        }

        return new ArrayList<Periode>(periodesDuDroit);
    }

    /**
     * <p>
     * Recherche et retourne les périodes se chevauchant entre le nouveau et l'ancien droit.<br/>
     * Si il y a un trou dans le nouveau droit, mais que l'ancien droit couvre ce trou, cela retournera aussi une
     * période pour ce laps de temps.
     * </p>
     * <p>
     * Différents cas d'exemples : <br/>
     * 1) Périodes se chevauchant partiellement <br/>
     * 
     * <pre>
     * 1.1)
     * Nouveau droit                       [------------------]
     * Ancien droit       [-----------------------]
     * Résultat                            [xxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 1.2)
     * Nouveau droit      [-----------------------]
     * Ancien droit                        [---------------------]
     * Résultat                            [xxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * 2) Période englobée dans une autre
     * 
     * <pre>
     * 2.1)
     * Nouveau droit      [--------------------------]
     * Ancien droit            [---------]
     * Résultat                [xxxxxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 2.2)
     * Nouveau droit           [---------]
     * Ancien droit       [----------------------]
     * Résultat                [xxxxxxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * 3) Trou dans les périodes des droits
     * 
     * <pre>
     * 3.1)
     * Nouveau droit      [---------]         [-----]
     * Ancien droit           [-----------------------]
     * Résultat               [xxxxx][xxxxxxx][xxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 3.2) Spécialité ici : on ne veut pas de période de chevauchement
     *      lorsque l'ancien droit n'est pas présent
     * Nouveau droit          [-----------------------]
     * Ancien droit       [---------]         [-----]
     * Résultat               [xxxxx]         [xxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * <pre>
     * 3.3) ne pas fusionner les périodes si un trou
     * Nouveau droit      [---------]         [-----]
     * Ancien droit           [---------][-------------]
     * Résultat               [xxxxx][xx][xxx][xxxxx]
     *                    -----------------------------------------> temps
     * </pre>
     * 
     * </p>
     * 
     * @return
     */
    public Collection<Periode> getPeriodesChevauchementDesDroits() {

        Collection<Periode> periodeNouveauDroit = getPeriodesNouveauDroit();
        Collection<Periode> periodeAncienDroit = getPeriodeAncienDroit();

        Collection<Periode> chevauchementDesDroits = new CopyOnWriteArrayList<Periode>();

        if (periodeNouveauDroit == null) {
            return null;
        }

        for (Periode unePeriodeDuNouveauDroit : periodeNouveauDroit) {
            for (Periode unePeriodeAncienDroit : periodeAncienDroit) {
                Periode intersectionDesPeriodes = unePeriodeDuNouveauDroit.intersectionMois(unePeriodeAncienDroit);
                if (intersectionDesPeriodes != null) {
                    chevauchementDesDroits = RETiersPourCalculRenteVerseeATortWrapper.ajouterPeriodeAuDroit(
                            chevauchementDesDroits, intersectionDesPeriodes);
                }
            }
        }
        // cas particulier des périodes de l'ancien droit présente où le nouveau droit ne l'est pas
        if (hasTrouDansLesPeriodesDuNouveauDroit()) {
            Collection<Periode> trousNouveauDroit = getPeriodeTrousNouveauDroit();

            for (Periode unTrouDuNouveauDroit : trousNouveauDroit) {
                // on parcours les rentes de l'ancien droit pour voir si le trou correspond
                for (RERentesPourCalculRenteVerseeATort uneRenteAncienDroit : getRentesAncienDroit()) {
                    Periode periodeRenteAncienDroit = new Periode(uneRenteAncienDroit.getDateDebutDroit(),
                            uneRenteAncienDroit.getDateFinDroit());
                    Periode intersection = unTrouDuNouveauDroit.intersectionMois(periodeRenteAncienDroit);
                    if (intersection != null) {
                        chevauchementDesDroits.add(intersection);
                    }
                }
            }
        }

        if (chevauchementDesDroits.isEmpty()) {
            return null;
        } else {
            return new ArrayList<Periode>(chevauchementDesDroits);
        }
    }

    public Collection<Periode> getPeriodesNouveauDroit() {
        return getPeriodeDuDroit(rentesNouveauDroit);
    }

    Collection<Periode> getPeriodeTrous(Collection<Periode> droit) {
        if (hasTrouDansLesPeriodes(droit)) {

            Collection<Periode> periodesFusionneesDuDroit = new CopyOnWriteArrayList<Periode>();

            // on met bout à bout et on fusionne toutes les périodes qui peuvent l'être
            for (Periode unePeriodeDuDroit : droit) {
                if (periodesFusionneesDuDroit.isEmpty()) {
                    periodesFusionneesDuDroit.add(unePeriodeDuDroit);
                } else {
                    periodesFusionneesDuDroit = RETiersPourCalculRenteVerseeATortWrapper.ajouterPeriodeAuDroit(
                            periodesFusionneesDuDroit, unePeriodeDuDroit);
                }
            }

            // si la période globale du droit comporte des trous (vu que les périodes ont été mises bout à bout et
            // fusionnées, la présence de deux période dans la liste veut dire que le droit comporte deux périodes
            // disjointes : donc un trou dans le droit)
            if (periodesFusionneesDuDroit.size() > 1) {

                Collection<Periode> trousDansLesPeriodes = new ArrayList<Periode>();
                List<Periode> listeTrieePeriodesDroit = new ArrayList<Periode>(periodesFusionneesDuDroit);
                // tri des périodes, voir la doc de PRPeriodWrapper pour la description du tri
                Collections.sort(listeTrieePeriodesDroit);

                // Parcours des périodes afin de définir précisément les trous
                Iterator<Periode> iterateurPeriodes = listeTrieePeriodesDroit.iterator();
                Periode periodePrecedente = null;
                do {
                    Periode periodeCourante = iterateurPeriodes.next();
                    if (periodePrecedente != null) {

                        String dateDebutTrou = periodePrecedente.getDateFin();
                        if (JadeDateUtil.isGlobazDateMonthYear(dateDebutTrou)) {
                            dateDebutTrou = "01." + dateDebutTrou;
                        }
                        dateDebutTrou = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(dateDebutTrou, 1));

                        String dateFinTrou = periodeCourante.getDateDebut();
                        if (JadeDateUtil.isGlobazDateMonthYear(dateFinTrou)) {
                            dateFinTrou = "01." + dateFinTrou;
                        }
                        dateFinTrou = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(dateFinTrou, -1));

                        trousDansLesPeriodes.add(new Periode(dateDebutTrou, dateFinTrou));
                    }

                    periodePrecedente = periodeCourante;
                } while (iterateurPeriodes.hasNext());

                return new ArrayList<Periode>(trousDansLesPeriodes);
            }
        }
        return null;
    }

    public Collection<Periode> getPeriodeTrousNouveauDroit() {
        return getPeriodeTrous(getPeriodesNouveauDroit());
    }

    public String getPrenom() {
        return prenom;
    }

    public RERentesPourCalculRenteVerseeATort getRenteAncienDroitSelonId(Long idRente) {
        if (rentesAncienDroit == null) {
            return null;
        }

        for (RERentesPourCalculRenteVerseeATort uneRente : rentesAncienDroit) {
            if ((uneRente.getIdRenteAccordee() != null) && uneRente.getIdRenteAccordee().equals(idRente)) {
                return uneRente;
            }
        }
        return null;
    }

    private Collection<RERentesPourCalculRenteVerseeATort> getRenteDuDroitDansLaPeriode(
            Collection<RERentesPourCalculRenteVerseeATort> droit, Periode periode) {
        Collection<RERentesPourCalculRenteVerseeATort> rentesDansLaPeriode = new ArrayList<RERentesPourCalculRenteVerseeATort>();

        if (droit != null) {
            for (RERentesPourCalculRenteVerseeATort uneRenteNouveauDroit : droit) {
                Periode periodeDeLaRente = new Periode(uneRenteNouveauDroit.getDateDebutDroit(),
                        uneRenteNouveauDroit.getDateFinDroit());
                Periode intersection = periodeDeLaRente.intersectionMois(periode);

                if ((intersection != null)
                        && ((idTiers == null) || idTiers.equals(uneRenteNouveauDroit.getIdTiersBeneficiaire()))) {
                    rentesDansLaPeriode.add(uneRenteNouveauDroit);
                }
            }
        }

        return rentesDansLaPeriode;
    }

    public Collection<RERentesPourCalculRenteVerseeATort> getRentesAncienDroit() {
        return rentesAncienDroit;
    }

    public Collection<RERentesPourCalculRenteVerseeATort> getRentesAncienDroitDansPeriode(Periode periode) {
        return getRenteDuDroitDansLaPeriode(rentesAncienDroit, periode);
    }

    public Collection<RERentesPourCalculRenteVerseeATort> getRentesNouveauDroit() {
        return rentesNouveauDroit;
    }

    public Collection<RERentesPourCalculRenteVerseeATort> getRentesNouveauDroitDansPeriode(Periode periode) {
        return getRenteDuDroitDansLaPeriode(rentesNouveauDroit, periode);
    }

    public String getSexe() {
        return sexe;
    }

    private boolean hasTrouDansLesPeriodes(Collection<Periode> droit) {
        Periode periodeGlobaleNouveauDroit = null;

        if (droit == null) {
            return false;
        }

        for (Periode periodePourUneRente : droit) {

            if (periodeGlobaleNouveauDroit == null) {
                periodeGlobaleNouveauDroit = periodePourUneRente;
            } else {
                switch (periodeGlobaleNouveauDroit.comparerChevauchementMois(periodePourUneRente)) {
                    case LES_PERIODES_SONT_INDEPENDANTES:
                        return true;
                    default:
                        periodeGlobaleNouveauDroit = periodeGlobaleNouveauDroit.unionMois(periodePourUneRente);
                        break;
                }
            }
        }
        return false;
    }

    public boolean hasTrouDansLesPeriodesDeAncienDroit() {
        return hasTrouDansLesPeriodes(getPeriodeDuDroit(rentesAncienDroit));
    }

    public boolean hasTrouDansLesPeriodesDuNouveauDroit() {
        return hasTrouDansLesPeriodes(getPeriodeDuDroit(rentesNouveauDroit));
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTiers(Long idTiers) {
        this.idTiers = idTiers;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(NumeroSecuriteSociale nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRentesAncienDroit(Collection<RERentesPourCalculRenteVerseeATort> rentesAncienDroit) {
        this.rentesAncienDroit = rentesAncienDroit;
    }

    public void setRentesNouveauDroit(Collection<RERentesPourCalculRenteVerseeATort> rentesNouveauDroit) {
        this.rentesNouveauDroit = rentesNouveauDroit;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}
