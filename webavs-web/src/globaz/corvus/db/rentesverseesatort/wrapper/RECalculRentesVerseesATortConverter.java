package globaz.corvus.db.rentesverseesatort.wrapper;

import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.RECalculRentesVerseesATortEntity;
import globaz.corvus.db.rentesverseesatort.RECalculRentesVerseesATortManager;
import globaz.corvus.db.rentesverseesatort.RERenteVerseeATortJointRenteAccordee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.jade.client.util.JadeDateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.domaine.Periode;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * Classe utilitaire permettant de transcrire un résultat de recherche de {@link RECalculRentesVerseesATortManager} en
 * un {@link RECalculRentesVerseesATortWrapper} afin de pouvoir utiliser ces données dans le calcul de
 * {@link RERenteVerseeATortUtil}
 * 
 * @author PBA
 */
public class RECalculRentesVerseesATortConverter {

    public static RERenteVerseeATortWrapper convertRenteVerseeATortEntityToWrapper(
            Collection<RERenteVerseeATortJointRenteAccordee> renteVerseeATort) {

        if (renteVerseeATort == null) {
            return null;
        }

        Long idRenteVerseeATort = null;
        Long idDemandeRente = null;
        Long idTiers = null;
        String nom = null;
        String prenom = null;
        NumeroSecuriteSociale nss = null;
        String dateDeces = null;
        String dateNaissance = null;
        String nationalite = null;
        Periode periodeRenteVerseeATort = null;
        TypeRenteVerseeATort typeRenteVerseeATort = null;
        RERentesPourCalculRenteVerseeATort renteNouveauDroit = null;
        RERentesPourCalculRenteVerseeATort renteAncienDroit = null;
        String sexe = null;
        Set<REPrestationDuePourCalculRenteVerseeATort> prestationsDues = new HashSet<REPrestationDuePourCalculRenteVerseeATort>();

        for (Iterator<RERenteVerseeATortJointRenteAccordee> iterator = renteVerseeATort.iterator(); iterator.hasNext();) {
            RERenteVerseeATortJointRenteAccordee uneEntree = iterator.next();

            if ((renteNouveauDroit == null) && (uneEntree.getIdRenteNouveauDroit() != null)) {
                renteNouveauDroit = RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(
                        uneEntree.getIdRenteNouveauDroit(), uneEntree.getDateDebutNouveauDroit(),
                        uneEntree.getDateFinNouveauDroit(),
                        CodePrestation.getCodePrestation(uneEntree.getCodePrestationNouveauDroit()),
                        uneEntree.getIdTiersBeneficiaire());
            }

            prestationsDues.add(REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(
                    uneEntree.getIdPrestationDue(), uneEntree.getIdRenteAncienDroit(),
                    uneEntree.getDateDebutPaiementPrestationDue(), uneEntree.getDateFinPaiementPrestationDue(),
                    uneEntree.getMontantPrestationDue()));

            if (!iterator.hasNext()) {
                idRenteVerseeATort = uneEntree.getIdRenteVerseeATort();
                idDemandeRente = uneEntree.getIdDemandeRente();
                idTiers = uneEntree.getIdTiersBeneficiaire();
                nom = uneEntree.getNom();
                prenom = uneEntree.getPrenom();
                nss = uneEntree.getNss();
                dateNaissance = uneEntree.getDateNaissance();
                dateDeces = uneEntree.getDateDeces();
                nationalite = uneEntree.getNationalite();
                periodeRenteVerseeATort = new Periode(uneEntree.getDateDebutRenteVerseeATort(),
                        uneEntree.getDateFinRenteVerseeATort());
                typeRenteVerseeATort = uneEntree.getTypeRenteVerseeATort();
                sexe = uneEntree.getSexe();

                if (uneEntree.getIdRenteAncienDroit() != null) {
                    renteAncienDroit = RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(
                            uneEntree.getIdRenteAncienDroit(), uneEntree.getDateDebutAncienDroit(),
                            uneEntree.getDateFinAncienDroit(),
                            CodePrestation.getCodePrestation(uneEntree.getCodePrestationAncienDroit()),
                            uneEntree.getIdTiersBeneficiaire(), prestationsDues);
                }
            }
        }

        return new RERenteVerseeATortWrapper(idRenteVerseeATort, idDemandeRente, idTiers, nom, prenom, nss,
                dateNaissance, dateDeces, sexe, nationalite, renteNouveauDroit, renteAncienDroit,
                periodeRenteVerseeATort, typeRenteVerseeATort);
    }

    public static RECalculRentesVerseesATortWrapper convertRenteVerseeATortWrapperToCalculWrapper(
            RERenteVerseeATortWrapper renteVerseeATortWrapper) {
        RECalculRentesVerseesATortWrapper wrapperCalcul = new RECalculRentesVerseesATortWrapper();

        wrapperCalcul.setIdDemandeRente(renteVerseeATortWrapper.getIdDemandeRente());
        RETiersPourCalculRenteVerseeATortWrapper tiers = new RETiersPourCalculRenteVerseeATortWrapper();
        tiers.setIdTiers(renteVerseeATortWrapper.getIdTiers());
        tiers.setNom(renteVerseeATortWrapper.getNom());
        tiers.setPrenom(renteVerseeATortWrapper.getPrenom());
        tiers.setNss(renteVerseeATortWrapper.getNss());
        tiers.setDateDeces(renteVerseeATortWrapper.getDateDeces());
        tiers.setDateNaissance(renteVerseeATortWrapper.getDateNaissance());
        tiers.setSexe(renteVerseeATortWrapper.getSexe());
        tiers.setNationalite(renteVerseeATortWrapper.getNationalite());
        tiers.setRentesAncienDroit(Arrays.asList(renteVerseeATortWrapper.getRenteAncienDroit()));
        tiers.setRentesNouveauDroit(Arrays.asList(renteVerseeATortWrapper.getRenteNouveauDroit()));
        wrapperCalcul.setTiers(Arrays.asList(tiers));

        return wrapperCalcul;
    }

    public static Collection<RERenteVerseeATort> convertToEntity(
            Collection<REDetailCalculRenteVerseeATort> detailsCalcul) {
        Collection<RERenteVerseeATort> rentesVerseesATort = new ArrayList<RERenteVerseeATort>();

        if (detailsCalcul != null) {
            for (REDetailCalculRenteVerseeATort unDetail : detailsCalcul) {
                RERenteVerseeATort nouvelleRenteVerseeATort = new RERenteVerseeATort();
                nouvelleRenteVerseeATort.setIdRenteVerseeATort(unDetail.getIdRenteVerseeATort());
                nouvelleRenteVerseeATort.setIdDemandeRente(unDetail.getIdDemandeRente());
                nouvelleRenteVerseeATort.setIdRenteAccordeeAncienDroit(unDetail.getIdRenteAccordeeAncienDroit());
                nouvelleRenteVerseeATort.setIdRenteAccordeeNouveauDroit(unDetail.getIdRenteAccordeeNouveauDroit());
                nouvelleRenteVerseeATort.setIdTiers(unDetail.getIdTiers());
                nouvelleRenteVerseeATort.setTypeRenteVerseeATort(TypeRenteVerseeATort.PRESTATION_DEJA_VERSEE);
                nouvelleRenteVerseeATort.setMontant(unDetail.getMontantTotalVerseeATort());
                nouvelleRenteVerseeATort.setDateDebut(unDetail.getDateDebutPeriode());
                nouvelleRenteVerseeATort.setDateFin(unDetail.getDateFinPeriode());
                rentesVerseesATort.add(nouvelleRenteVerseeATort);
            }
        }

        return rentesVerseesATort;
    }

    public static RECalculRentesVerseesATortWrapper convertToWrapper(List<RECalculRentesVerseesATortEntity> entities)
            throws RETechnicalException {
        RECalculRentesVerseesATortWrapper wrapper = new RECalculRentesVerseesATortWrapper();

        class InfoTiers {
            public String dateDeces;
            public String dateNaissance;
            public Long idTiers;
            public String nationalite;
            public String nom;
            public NumeroSecuriteSociale nss;
            public String prenom;
            public String sexe;

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof InfoTiers) {
                    return idTiers.equals(((InfoTiers) obj).idTiers);
                }
                return false;
            }

            @Override
            public int hashCode() {
                return idTiers.hashCode();
            }
        }

        Map<InfoTiers, Map<Long, RERentesPourCalculRenteVerseeATort>> rentesNouveauDroitParTiersEtParIdRente = new HashMap<InfoTiers, Map<Long, RERentesPourCalculRenteVerseeATort>>();
        Map<InfoTiers, Map<Long, RERentesPourCalculRenteVerseeATort>> rentesAncienDroitParTiersEtParIdRente = new HashMap<InfoTiers, Map<Long, RERentesPourCalculRenteVerseeATort>>();

        for (RECalculRentesVerseesATortEntity uneEntitee : entities) {
            // id demande rente
            if (wrapper.getIdDemandeRente() == null) {
                try {
                    wrapper.setIdDemandeRente(Long.parseLong(uneEntitee.getIdDemandeRenteNouveauDroit()));
                } catch (NumberFormatException ex) {
                    wrapper.setIdDemandeRente(null);
                }
            }

            InfoTiers tiers = new InfoTiers();
            tiers.idTiers = Long.parseLong(uneEntitee.getIdTiersBeneficiaireRenteAccordeeAncienDroit());
            tiers.nom = uneEntitee.getNomTiersBeneficiaire();
            tiers.prenom = uneEntitee.getPrenomTiersBeneficiaire();
            tiers.nss = new NumeroSecuriteSociale(uneEntitee.getNssTiersBeneficiaire());
            tiers.dateDeces = uneEntitee.getDateDecesTiersBeneficiaire();
            tiers.dateNaissance = uneEntitee.getDateNaissanceTiersBeneficiaire();
            tiers.sexe = uneEntitee.getSexeTiersBeneficiaire();
            tiers.nationalite = uneEntitee.getNationaliteTiersBeneficiaire();

            Long idRenteNouveauDroit = Long.parseLong(uneEntitee.getIdRenteAccordeeNouveauDroit());
            Map<Long, RERentesPourCalculRenteVerseeATort> renteNouveauDroitDuTiersParId = null;

            // rente du nouveau droit
            if (!rentesNouveauDroitParTiersEtParIdRente.containsKey(tiers)) {
                renteNouveauDroitDuTiersParId = new HashMap<Long, RERentesPourCalculRenteVerseeATort>();
                rentesNouveauDroitParTiersEtParIdRente.put(tiers, renteNouveauDroitDuTiersParId);
            } else {
                renteNouveauDroitDuTiersParId = rentesNouveauDroitParTiersEtParIdRente.get(tiers);
            }

            if (!renteNouveauDroitDuTiersParId.containsKey(idRenteNouveauDroit)) {
                RERentesPourCalculRenteVerseeATort nouvelleRente = RECalculRentesVerseesATortConverter
                        .parseRenteNouveauDroit(uneEntitee);
                renteNouveauDroitDuTiersParId.put(idRenteNouveauDroit, nouvelleRente);
            }

            Long idRenteAncienDroit = Long.parseLong(uneEntitee.getIdRenteAccordeeAncienDroit());
            Map<Long, RERentesPourCalculRenteVerseeATort> renteAncienDroitDuTiersParId = null;

            // rente de l'ancien droit
            if (!rentesAncienDroitParTiersEtParIdRente.containsKey(tiers)) {
                renteAncienDroitDuTiersParId = new HashMap<Long, RERentesPourCalculRenteVerseeATort>();
                rentesAncienDroitParTiersEtParIdRente.put(tiers, renteAncienDroitDuTiersParId);
            } else {
                renteAncienDroitDuTiersParId = rentesAncienDroitParTiersEtParIdRente.get(tiers);
            }

            if (!renteAncienDroitDuTiersParId.containsKey(idRenteAncienDroit)) {
                RERentesPourCalculRenteVerseeATort ancienneRente = RECalculRentesVerseesATortConverter
                        .parseRenteAncienDroit(uneEntitee);
                renteAncienDroitDuTiersParId.put(idRenteAncienDroit, ancienneRente);
            } else {
                RERentesPourCalculRenteVerseeATort ancienneRente = renteAncienDroitDuTiersParId.get(idRenteAncienDroit);
                REPrestationDuePourCalculRenteVerseeATort prestationDue = RECalculRentesVerseesATortConverter
                        .parsePrestationDue(uneEntitee);
                ancienneRente.getPrestationsDues().add(prestationDue);
            }
        }

        Comparator<RERentesPourCalculRenteVerseeATort> triParPeriodeDroit = new Comparator<RERentesPourCalculRenteVerseeATort>() {

            @Override
            public int compare(RERentesPourCalculRenteVerseeATort o1, RERentesPourCalculRenteVerseeATort o2) {
                if ((o1 != null) && (o2 != null)) {
                    Periode periodeRente1 = new Periode(o1.getDateDebutDroit(), o1.getDateFinDroit());
                    Periode periodeRente2 = new Periode(o2.getDateDebutDroit(), o2.getDateFinDroit());
                    return periodeRente1.compareTo(periodeRente2);
                }
                return 0;
            }
        };

        Collection<RETiersPourCalculRenteVerseeATortWrapper> tiers = new ArrayList<RETiersPourCalculRenteVerseeATortWrapper>();
        for (Entry<InfoTiers, Map<Long, RERentesPourCalculRenteVerseeATort>> renteAncienDroitEtInfoDuTiers : rentesAncienDroitParTiersEtParIdRente
                .entrySet()) {
            RETiersPourCalculRenteVerseeATortWrapper unTiers = new RETiersPourCalculRenteVerseeATortWrapper();
            unTiers.setIdTiers(renteAncienDroitEtInfoDuTiers.getKey().idTiers);
            unTiers.setNom(renteAncienDroitEtInfoDuTiers.getKey().nom);
            unTiers.setPrenom(renteAncienDroitEtInfoDuTiers.getKey().prenom);
            unTiers.setNss(renteAncienDroitEtInfoDuTiers.getKey().nss);
            unTiers.setDateDeces(renteAncienDroitEtInfoDuTiers.getKey().dateDeces);
            unTiers.setDateNaissance(renteAncienDroitEtInfoDuTiers.getKey().dateNaissance);
            unTiers.setSexe(renteAncienDroitEtInfoDuTiers.getKey().sexe);
            unTiers.setNationalite(renteAncienDroitEtInfoDuTiers.getKey().nationalite);

            List<RERentesPourCalculRenteVerseeATort> rentesAncienDroitTriees = new ArrayList<RERentesPourCalculRenteVerseeATort>(
                    renteAncienDroitEtInfoDuTiers.getValue().values());
            Collections.sort(rentesAncienDroitTriees, triParPeriodeDroit);
            unTiers.setRentesAncienDroit(rentesAncienDroitTriees);

            Map<Long, RERentesPourCalculRenteVerseeATort> rentesNouveauDroitDuTiers = rentesNouveauDroitParTiersEtParIdRente
                    .get(renteAncienDroitEtInfoDuTiers.getKey());
            if (rentesNouveauDroitDuTiers != null) {
                List<RERentesPourCalculRenteVerseeATort> rentesNouveauDroitTriees = new ArrayList<RERentesPourCalculRenteVerseeATort>(
                        rentesNouveauDroitDuTiers.values());
                Collections.sort(rentesNouveauDroitTriees, triParPeriodeDroit);

                unTiers.setRentesNouveauDroit(rentesNouveauDroitTriees);
            }
            tiers.add(unTiers);
        }
        wrapper.setTiers(tiers);

        return wrapper;
    }

    private static CodePrestation parseCodePrestation(String valueToParse, String messageIfError)
            throws RETechnicalException {
        CodePrestation parsedValue = null;
        try {
            parsedValue = CodePrestation.getCodePrestation(Integer.parseInt(valueToParse));
        } catch (NumberFormatException ex) {
            throw new RETechnicalException(messageIfError, ex);
        }

        if (parsedValue == null) {
            throw new RETechnicalException(messageIfError);
        }

        return parsedValue;
    }

    private static Long parseLong(String valueToParse, String messageIfError) throws RETechnicalException {
        Long parsedValue = null;
        try {
            parsedValue = Long.parseLong(valueToParse);
        } catch (NumberFormatException ex) {
            throw new RETechnicalException(messageIfError, ex);
        }
        return parsedValue;
    }

    private static REPrestationDuePourCalculRenteVerseeATort parsePrestationDue(RECalculRentesVerseesATortEntity entity)
            throws RETechnicalException {
        Long idPrestationDue = null;
        Long idRenteAccordee = null;
        BigDecimal montant = null;

        idPrestationDue = RECalculRentesVerseesATortConverter.parseLong(entity.getIdPrestationDueAncienDroit(),
                "a valid [idPrestationDueAncienDroit] is needed");
        idRenteAccordee = RECalculRentesVerseesATortConverter.parseLong(entity.getIdRenteAccordeeAncienDroit(),
                "a valid [idRenteAccordeeAncienDroit] is needed");

        if (idPrestationDue == 0) {
            throw new RETechnicalException("[idPrestationDueAncienDroit] can't be zero");
        }
        if (idRenteAccordee == 0) {
            throw new RETechnicalException("[idRenteAccordeeAncienDroit] can't be zero");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(entity.getDateDebutPaiementPrestationVerseeDueAncienDroit())) {
            throw new RETechnicalException("a valid [dateDebutPaiementPrestationVerseeDueAncienDroit] is needed");
        }

        try {
            montant = new BigDecimal(entity.getMontantPrestationVerseeDueAncienDroit());
        } catch (NumberFormatException ex) {
            throw new RETechnicalException("a valid [montantPrestationVerseeDueAncienDroit] is needed");
        }

        return REPrestationDuePourCalculRenteVerseeATort.creerPrestationDue(idPrestationDue, idRenteAccordee,
                entity.getDateDebutPaiementPrestationVerseeDueAncienDroit(),
                entity.getDateFinPaiementPrestationVerseeDueAncienDroit(), montant);
    }

    private static RERentesPourCalculRenteVerseeATort parseRenteAncienDroit(RECalculRentesVerseesATortEntity entity)
            throws RETechnicalException {
        Long idRenteAccordee = null;
        Long idTiersBeneficiaire = null;
        CodePrestation codePrestation = null;

        idRenteAccordee = RECalculRentesVerseesATortConverter.parseLong(entity.getIdRenteAccordeeAncienDroit(),
                "a valid [idRenteAccordeeAncienDroit] is needed");
        idTiersBeneficiaire = RECalculRentesVerseesATortConverter.parseLong(
                entity.getIdTiersBeneficiaireRenteAccordeeAncienDroit(),
                "a valid [idTiersBeneficaireAncienDroit] is needed");

        if (idRenteAccordee == 0) {
            throw new RETechnicalException("[idRenteAccordeeAncienDroit] can't be zero");
        }
        if (idTiersBeneficiaire == 0) {
            throw new RETechnicalException("[idTiersBeneficaireAncienDroit] can't be zero");
        }

        codePrestation = RECalculRentesVerseesATortConverter.parseCodePrestation(
                entity.getCodePrestationRenteAccordeeAncienDroit(),
                "a valid [codePrestationRenteAccordeeAncienDroit] is needed");

        REPrestationDuePourCalculRenteVerseeATort prestationDue = RECalculRentesVerseesATortConverter
                .parsePrestationDue(entity);
        Set<REPrestationDuePourCalculRenteVerseeATort> prestationsDuesDeLaRente = new HashSet<REPrestationDuePourCalculRenteVerseeATort>();
        prestationsDuesDeLaRente.add(prestationDue);

        return RERentesPourCalculRenteVerseeATort.creerRenteAncienDroit(idRenteAccordee,
                entity.getDateDebutRenteAccordeeAncienDroit(), entity.getDateFinRenteAccordeeAncienDroit(),
                codePrestation, idTiersBeneficiaire, prestationsDuesDeLaRente);
    }

    private static RERentesPourCalculRenteVerseeATort parseRenteNouveauDroit(RECalculRentesVerseesATortEntity entity)
            throws RETechnicalException {
        Long idRenteAccordee = null;
        Long idTiersBeneficiaire = null;
        CodePrestation codePrestation = null;

        idRenteAccordee = RECalculRentesVerseesATortConverter.parseLong(entity.getIdRenteAccordeeNouveauDroit(),
                "a valid [idRenteAccordeeNouveauDroit] is needed");
        idTiersBeneficiaire = RECalculRentesVerseesATortConverter.parseLong(
                entity.getIdTiersBeneficiaireRenteAccordeeNouveauDroit(),
                "a valid [idTiersBeneficaireNouveauDroit] is needed");

        if (idRenteAccordee == 0) {
            throw new RETechnicalException("[idRenteAccordeeNouveauDroit] can't be zero");
        }
        if (idTiersBeneficiaire == 0) {
            throw new RETechnicalException("[idTiersBeneficaireNouveauDroit] can't be zero");
        }

        codePrestation = RECalculRentesVerseesATortConverter.parseCodePrestation(
                entity.getCodePrestationRenteAccordeeNouveauDroit(),
                "a valid [codePrestationRenteAccordeeNouveauDroit] is needed");

        return RERentesPourCalculRenteVerseeATort.creerRenteNouveauDroit(idRenteAccordee,
                entity.getDateDebutRenteAccordeeNouveauDroit(), entity.getDateFinRenteAccordeeNouveauDroit(),
                codePrestation, idTiersBeneficiaire);
    }
}
