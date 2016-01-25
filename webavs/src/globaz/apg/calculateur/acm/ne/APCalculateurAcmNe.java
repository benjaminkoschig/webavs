package globaz.apg.calculateur.acm.ne;

import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.calculateur.IAPPrestationCalculateur;
import globaz.apg.calculateur.acm.alfa.APCalculateurAcmAlphaDonnesPersistence;
import globaz.apg.calculateur.pojo.APPrestationCalculeeAPersister;
import globaz.apg.calculateur.pojo.APRepartitionCalculeeAPersister;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APCotisation;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.enums.APAssuranceTypeAssociation;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.exceptions.APCalculerAcmNeException;
import globaz.apg.module.calcul.APCotisationData;
import globaz.apg.module.calcul.APRepartitionPaiementData;
import globaz.apg.module.calcul.APSituationProfessionnelleData;
import globaz.apg.properties.APProperties;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calcul les prestations ACM NE (avec répartitions et cotisations) en fonction des prestations standard et des
 * situations professionnelles
 * 
 * @author jje
 */
public class APCalculateurAcmNe implements IAPPrestationCalculateur {

    private static final String DATE_FIN_MAX = "31.12.9999";

    @Override
    public List<Object> calculerPrestation(final List<Object> listeObjectDeDomainePourCalcul) throws Exception,
            JAException, APCalculerAcmNeException {

        final List<Object> prestationsCalculees = new ArrayList<Object>();

        for (final Object object : listeObjectDeDomainePourCalcul) {
            final APCalculateurAcmNeDonneeDomaine prestationStandard = (APCalculateurAcmNeDonneeDomaine) object;

            // initialisation des dates et des montants totaux de la nouvelle prestation acm ne
            final JADate prestationStandardDateDebutJd = new JADate(prestationStandard.getDateDebut());
            JADate prestationStandardDateFinJd = null;
            if (JadeStringUtil.isBlankOrZero(prestationStandard.getDateFin())) {
                prestationStandardDateFinJd = new JADate(APCalculateurAcmNe.DATE_FIN_MAX);
            } else {
                prestationStandardDateFinJd = new JADate(prestationStandard.getDateFin());
            }

            BigDecimal montantPrestationAcmNeJournalier = new BigDecimal("0");
            BigDecimal montantPrestationAcmNeBrut = new BigDecimal("0");
            BigDecimal montantPrestationAcmNeNet = new BigDecimal("0");

            // création de la nouvelle prestation acm ne
            final APCalculateurAcmNeDonneesPersistence prestationAcmNe = new APCalculateurAcmNeDonneesPersistence(
                    prestationStandard.getDateDebut(), prestationStandard.getDateFin(), 1,
                    prestationStandard.getIdDroit());

            /*
             * pour chaque situation professionnelle du droit de type acm ne, on crée une répartition de paiement et ses
             * cotisations pour autant que la période de la situation prof. soit comprise dans la période de la
             * prestation standard
             */
            for (final APSituationProfessionnelleData situationProf : prestationStandard.getSituationProfessionnelle()
                    .values()) {

                if (isCalculPrestationAcmNeRequis(situationProf)) {
                    if (isPeriodeSitProCompriseDansPeridodePrestationStandard(prestationStandardDateDebutJd,
                            prestationStandardDateFinJd, situationProf.getDateDebut(), situationProf.getDateFin())) {

                        final BigDecimal montantBrutRepartition = getMontantBrutRepartition(
                                situationProf.getMontantJournalierAcmNe(), prestationAcmNe.getNombreDeJoursSoldes());
                        final BigDecimal[] tauxAvsAcFne = prestationStandard.getTaux().get(situationProf.getId());

                        // Création des cotisations
                        final APCotisationData cotisationAvs = creerCotisation(montantBrutRepartition, tauxAvsAcFne[0],
                                APProperties.ASSURANCE_AVS_PAR_ID.getValue());
                        final APCotisationData cotisationAc = creerCotisation(montantBrutRepartition, tauxAvsAcFne[1],
                                APProperties.ASSURANCE_AC_PAR_ID.getValue());
                        APCotisationData cotisationFne = null;
                        // si FNE, ajout d'une cotisation supplémentaire
                        if (isAssociationFne(situationProf)) {
                            // Pour du FNE, Le montant est le montant brut de la repartition de la prestation
                            // standard
                            // correspondante plus le montant brut de la repartition ACM
                            BigDecimal montantBrutCotisationFne = montantBrutRepartition.add(prestationStandard
                                    .getRepartitions().get(situationProf.getId()));
                            cotisationFne = creerCotisation(montantBrutCotisationFne, tauxAvsAcFne[2],
                                    APProperties.ASSURANCE_FNE_ID.getValue());
                        }

                        // création de la répartition
                        final APRepartitionPaiementData repartitionPaiementData = creerRepartition(
                                montantBrutRepartition, cotisationAvs, cotisationAc, cotisationFne,
                                IAPRepartitionPaiements.CS_NORMAL, IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR,
                                situationProf.getIdTiersEmployeur(), situationProf.getAssociation()
                                        .getCodesystemToString(), situationProf.getId(), situationProf.getNom(),
                                situationProf.getIdAffilie());
                        prestationAcmNe.getRepartitionsPaiementMap().add(repartitionPaiementData);

                        // màj montant prestation
                        montantPrestationAcmNeJournalier = montantPrestationAcmNeJournalier.add(situationProf
                                .getMontantJournalierAcmNe());
                        montantPrestationAcmNeBrut = montantPrestationAcmNeBrut.add(repartitionPaiementData
                                .getMontantBrut());
                        montantPrestationAcmNeNet = montantPrestationAcmNeBrut.add(repartitionPaiementData
                                .getMontantNet());
                    }
                }
            }

            prestationAcmNe.setMontantBrut(montantPrestationAcmNeJournalier);
            prestationAcmNe.setMontantNet(montantPrestationAcmNeJournalier);
            prestationAcmNe.setMontantJournalier(montantPrestationAcmNeJournalier);
            prestationAcmNe.setNombreDeJoursSoldes(1);

            // On ne stocke pas les prestations ACM-NE avec un montant à 0
            if (isMontantJournalierPositif(montantPrestationAcmNeBrut)) {
                prestationsCalculees.add(prestationAcmNe);
            }
        }
        return prestationsCalculees;
    }

    private APCotisationData creerCotisation(final BigDecimal montantBrutRepartition, final BigDecimal tauxAc,
            final String idAssurance) {

        final BigDecimal montantBrutCotisation = getMontantBrutCotisation(montantBrutRepartition, tauxAc);
        final APCotisationData cotisationAc = new APCotisationData(montantBrutCotisation, montantBrutRepartition,
                tauxAc, APCotisation.TYPE_ASSURANCE, idAssurance);

        return cotisationAc;
    }

    private APRepartitionPaiementData creerRepartition(final BigDecimal montantBrutRepartition,
            final APCotisationData cotisationAvs, final APCotisationData cotisationAc,
            final APCotisationData cotisationFne, final String typePrestation, final String typePaiement,
            final String idTiersAdressePaiement, final String typeAssociationAssurance,
            final String idSituationProfessionnelle, final String nom, final String idAffilie) {

        final BigDecimal montantNetRepartition = getMontantNetRepartition(montantBrutRepartition,
                round5cts(cotisationAvs.getMontantCotisation()), round5cts(cotisationAc.getMontantCotisation()),
                cotisationFne == null ? new BigDecimal("0") : round5cts(cotisationFne.getMontantCotisation()));

        // création de la répartition
        final APRepartitionPaiementData repartition = new APRepartitionPaiementData(montantBrutRepartition,
                montantNetRepartition, typePrestation, typePaiement, idTiersAdressePaiement, typeAssociationAssurance,
                idSituationProfessionnelle, nom, idAffilie);
        repartition.getCotisations().add(cotisationAvs);
        repartition.getCotisations().add(cotisationAc);
        if (cotisationFne != null) {
            repartition.getCotisations().add(cotisationFne);
        }

        return repartition;

    }

    /**
     * Convertis une liste d'objets de domaine vers des objects de persistence
     * 
     * @param Une
     *            liste d'object de type @see {APResultatPrestationCalculateur}
     * @return Une liste d'objects de type @see {APResultatCalculPrestationEntite}
     */
    @Override
    public List<APPrestationCalculeeAPersister> domainToPersistence(final List<Object> listePrestationDomainCalculees)
            throws Exception {

        // La liste d'entitées de persistance converties
        final List<APPrestationCalculeeAPersister> entitesConverties = new ArrayList<APPrestationCalculeeAPersister>();

        // Pour chacune de prestation de domain calculées
        for (final Object o : listePrestationDomainCalculees) {

            // Prestation calculée, c'est un objet de domaine
            final APCalculateurAcmNeDonneesPersistence prestationDomainCalculee = (APCalculateurAcmNeDonneesPersistence) o;

            // Prestation calculée convertie vers la persistence
            final APPrestationCalculeeAPersister prestationPersistente = new APPrestationCalculeeAPersister();

            final APPrestation prestation = new APPrestation();
            // Les prestations ACM ne doivent pas être annoncées
            prestation.setContenuAnnonce(null);
            prestation.setDateCalcul(JACalendar.todayJJsMMsAAAA());
            prestation.setDateDebut(prestationDomainCalculee.getDateDebut());
            prestation.setDateFin(prestationDomainCalculee.getDateFin());
            prestation.setIdDroit(prestationDomainCalculee.getIdDroit());
            prestation.setNombreJoursSoldes(String.valueOf(prestationDomainCalculee.getNombreDeJoursSoldes()));
            prestation.setType(IAPPrestation.CS_TYPE_NORMAL);
            prestation.setMontantJournalier(round5cts(prestationDomainCalculee.getMontantJournalier()).toString());
            prestation.setMontantBrut(round5cts(prestationDomainCalculee.getMontantBrut()).toString());
            prestation.setGenre(String.valueOf(APTypeDePrestation.ACM_NE.getCodesystem()));
            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestationPersistente.setPrestation(prestation);

            // Création des répartitions des paiements et cotisations
            for (final APRepartitionPaiementData apRepPaiDat : prestationDomainCalculee.getRepartitionsPaiementMap()) {

                final APRepartitionPaiements apRepartitionPaiementsEntity = new APRepartitionPaiements();
                apRepartitionPaiementsEntity.wantCallValidate(false);
                apRepartitionPaiementsEntity.setDateValeur(JACalendar.todayJJsMMsAAAA());
                apRepartitionPaiementsEntity.setMontantBrut(round5cts(apRepPaiDat.getMontantBrut()).toString());
                apRepartitionPaiementsEntity.setMontantNet(round5cts(apRepPaiDat.getMontantNet()).toString());
                apRepartitionPaiementsEntity.setTypeAssociationAssurance(apRepPaiDat.getTypeAssociationAssurance());
                apRepartitionPaiementsEntity.setTypePaiement(apRepPaiDat.getTypePaiement());
                apRepartitionPaiementsEntity.setTypePrestation(apRepPaiDat.getTypePrestation());
                apRepartitionPaiementsEntity.setIdSituationProfessionnelle(apRepPaiDat.getIdSituationProfessionnelle());
                apRepartitionPaiementsEntity.setNom(apRepPaiDat.getNom());
                apRepartitionPaiementsEntity.setIdTiers(apRepPaiDat.getIdTiersAdressePaiement());
                apRepartitionPaiementsEntity.setIdTiersAdressePaiement(apRepPaiDat.getIdTiersAdressePaiement());
                apRepartitionPaiementsEntity.setIdAffilie(apRepPaiDat.getIdAffilie());

                final APRepartitionCalculeeAPersister apResRepAcmNeEntite = new APRepartitionCalculeeAPersister();
                apResRepAcmNeEntite.setRepartitionPaiements(apRepartitionPaiementsEntity);

                // Création des cotisations
                for (final APCotisationData cotisationData : apRepPaiDat.getCotisations()) {
                    final APCotisation apCot = new APCotisation();
                    apCot.setType(String.valueOf(cotisationData.getType()));
                    apCot.setMontantBrut(round5cts(cotisationData.getMontantBrut()).toString());
                    apCot.setType(APCotisation.TYPE_ASSURANCE);
                    apCot.setDateDebut(prestationDomainCalculee.getDateDebut());
                    apCot.setDateFin(prestationDomainCalculee.getDateFin());
                    apCot.setMontant(round5cts(cotisationData.getMontantCotisation()).toString());
                    apCot.setIdExterne(cotisationData.getIdAssurance());
                    apCot.setTaux(cotisationData.getTaux().multiply(new BigDecimal(100))
                            .setScale(2, RoundingMode.HALF_UP).toString());

                    apResRepAcmNeEntite.getCotisations().add(apCot);
                }
                prestationPersistente.getRepartitions().add(apResRepAcmNeEntite);
            }
            entitesConverties.add(prestationPersistente);
        }
        return entitesConverties;
    }

    private APAssuranceTypeAssociation getAssociationFromSituationProf(final APSitProJointEmployeur apSitProJoiEmpEnt) {
        APAssuranceTypeAssociation association;
        if (APAssuranceTypeAssociation.FNE.isCodeSystemEqual(apSitProJoiEmpEnt.getCsAssuranceAssociation())) {
            association = APAssuranceTypeAssociation.FNE;
        } else if (APAssuranceTypeAssociation.MECP.isCodeSystemEqual(apSitProJoiEmpEnt.getCsAssuranceAssociation())) {
            association = APAssuranceTypeAssociation.MECP;
        } else if (APAssuranceTypeAssociation.PP.isCodeSystemEqual(apSitProJoiEmpEnt.getCsAssuranceAssociation())) {
            association = APAssuranceTypeAssociation.PP;
        } else {
            association = APAssuranceTypeAssociation.NONE;
        }
        return association;
    }

    private BigDecimal getMontantBrutCotisation(final BigDecimal montantBrutRepartition, final BigDecimal tauxAc) {
        final BigDecimal montantBrutCotisation = montantBrutRepartition.multiply(tauxAc);
        return montantBrutCotisation;
    }

    private BigDecimal getMontantBrutRepartition(final BigDecimal montantJournalierAcmNe, final int nombreDeJoursSoldes) {
        return montantJournalierAcmNe.multiply(new BigDecimal(nombreDeJoursSoldes));
    }

    private BigDecimal getMontantNetRepartition(final BigDecimal montantBrutRepartition,
            final BigDecimal montantCotisationAvs, final BigDecimal montantCotisationAc,
            final BigDecimal montantCotisationFne) {

        final BigDecimal montantNetRepartition = montantBrutRepartition.add(montantCotisationAvs)
                .add(montantCotisationAc).add(montantCotisationFne);

        return montantNetRepartition;
    }

    private boolean isAssociationFne(final APSituationProfessionnelleData situationProf) {
        return situationProf.getAssociation() == APAssuranceTypeAssociation.FNE;
    }

    private boolean isMontantJournalierPositif(BigDecimal bd) {
        return (bd.compareTo(BigDecimal.ZERO) > 0);
    }

    private boolean isPeriodeSitProCompriseDansPeridodePrestationStandard(final JADate prestationStandardDateDebutJd,
            final JADate prestationStandardDateFinJd, final String dateDebut, final String dateFin) throws JAException {

        final JACalendar cal = new JACalendarGregorian();

        final JADate situationProfDateDebutJd = new JADate(dateDebut);
        JADate situationProfDateFinJd = null;
        if (JadeStringUtil.isBlank(dateFin)) {
            situationProfDateFinJd = new JADate(APCalculateurAcmNe.DATE_FIN_MAX);
        } else {
            situationProfDateFinJd = new JADate(dateFin);
        }

        return
        // prest [......]
        // sitpr ..[..]..
        (((cal.compare(situationProfDateDebutJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_FIRSTUPPER) && (cal
                .compare(situationProfDateDebutJd, prestationStandardDateFinJd) == JACalendar.COMPARE_FIRSTLOWER)) && ((cal
                .compare(situationProfDateFinJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_FIRSTUPPER) && (cal
                .compare(situationProfDateFinJd, prestationStandardDateFinJd) == JACalendar.COMPARE_FIRSTLOWER)))
                ||
                // prest [......] et [.....]
                // sitpr [...]... et [.....]
                ((cal.compare(situationProfDateDebutJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_EQUALS) && ((cal
                        .compare(situationProfDateFinJd, prestationStandardDateFinJd) == JACalendar.COMPARE_EQUALS) || (cal
                        .compare(situationProfDateFinJd, prestationStandardDateFinJd) == JACalendar.COMPARE_FIRSTLOWER)))
                ||
                // prest [......] et [.....]
                // sitpr [......] et ..[...]
                ((cal.compare(situationProfDateFinJd, prestationStandardDateFinJd) == JACalendar.COMPARE_EQUALS) && ((cal
                        .compare(situationProfDateDebutJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_EQUALS) || (cal
                        .compare(situationProfDateDebutJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_FIRSTLOWER)))

                ||
                // prest ..[..]..
                // sitpr [......]
                (((cal.compare(situationProfDateDebutJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_FIRSTLOWER) && (cal
                        .compare(situationProfDateDebutJd, prestationStandardDateFinJd) == JACalendar.COMPARE_FIRSTLOWER)) &&

                ((cal.compare(situationProfDateFinJd, prestationStandardDateDebutJd) == JACalendar.COMPARE_FIRSTUPPER) && (cal
                        .compare(situationProfDateFinJd, prestationStandardDateFinJd) == JACalendar.COMPARE_FIRSTUPPER)));
    }

    /**
     * Définit si des prestations ACM NE doivent êtres calculées pour cette situation prof Deux conditions doivent êtres
     * remplies : </br> - Une association doit être renseigné (FNE, MECP, PP) - un montant 'total ACM' supérieur à 0
     * doit être renseigné
     * 
     * @param situationProf
     *            La situation prof à tester
     * @return <code>true</code> si les conditions son réunies pour le calcul de prestation ACM NE pour cette sit. prof.
     */
    private boolean isCalculPrestationAcmNeRequis(final APSituationProfessionnelleData situationProf) {
        if (situationProf == null) {
            throw new IllegalArgumentException(
                    "APCalculateurAcmNe.isCalculPrestationAcmNeRequis(APSituationProfessionnelleData situationProf) : situationProf is null");
        }
        boolean doCalcul = false;
        APAssuranceTypeAssociation assurance = situationProf.getAssociation();
        if (assurance != null) {
            switch (assurance) {
                case FNE:
                case MECP:
                case PP:
                    BigDecimal totalAcm = situationProf.getMontantJournalierAcmNe();
                    if (totalAcm != null) {
                        doCalcul = isMontantJournalierPositif(totalAcm);
                    }
                default:
                    break;
            }
        }
        return doCalcul;
    }

    /**
     * Créé l'objet en fonction des entités situation professionnelle et droit
     * 
     * @param idDroit
     * @param montantJournalierAcm
     * @param session
     * @return APPrestationStandardData
     * @throws JadePersistenceException
     * @throws Exception
     */
    @Override
    public List<Object> persistenceToDomain(final Object data) throws JadePersistenceException, Exception {

        final APCalculateurAcmAlphaDonnesPersistence donneesPersistancePourCalcul = (APCalculateurAcmAlphaDonnesPersistence) data;
        final String idDroit = donneesPersistancePourCalcul.getIdDroit();

        // Données en entrée
        final List<APRepartitionJointPrestation> listePrestationsPersistance = donneesPersistancePourCalcul
                .getPrestationJointRepartitions();

        // List temporaire contenant les prestations standard existantes avec ses répartitions
        Map<String, APCalculateurAcmNeDonneeDomaine> listePrestations = new HashMap<String, APCalculateurAcmNeDonneeDomaine>();

        // Pour chacune des prestations standard joint répartition
        for (APRepartitionJointPrestation prestationJoinRepartition : listePrestationsPersistance) {

            // Création-ajout de la prestation standard existante
            if (!listePrestations.containsKey(prestationJoinRepartition.getIdPrestationApg())) {

                APCalculateurAcmNeDonneeDomaine prestationAcmNeDomainCalculee = new APCalculateurAcmNeDonneeDomaine(
                        prestationJoinRepartition.getDateDebut(), prestationJoinRepartition.getDateFin(),
                        Integer.parseInt(prestationJoinRepartition.getNbJoursSoldes()), idDroit,
                        donneesPersistancePourCalcul.getTaux());
                listePrestations.put(prestationJoinRepartition.getIdPrestationApg(), prestationAcmNeDomainCalculee);
            }

            // Ajout des repartitions dont l'id parent est null
            listePrestations
                    .get(prestationJoinRepartition.getIdPrestationApg())
                    .getRepartitions()
                    .put(prestationJoinRepartition.getIdSituationProfessionnelle(),
                            new BigDecimal(prestationJoinRepartition.getMontantBrut()));
        }

        // Données en sortie
        final List<Object> listePrestationsAcmNeDomaineConverties = new ArrayList<Object>();

        // Pour chacune des prestations standard Joint répartition
        for (final APCalculateurAcmNeDonneeDomaine prestationStandard : listePrestations.values()) {

            // Recherche de la situation professionnelle
            final List<APSitProJointEmployeur> apSitProJoiEmpEntityList = donneesPersistancePourCalcul
                    .getSituationProfessionnelleEmployeur();

            String idSituationProfessionnelleCourante = "";

            for (final APSitProJointEmployeur apSitProJoiEmpEnt : apSitProJoiEmpEntityList) {

                if (apSitProJoiEmpEnt != null) {
                    // nouvelle situation professionnelle
                    if (!idSituationProfessionnelleCourante.equals(apSitProJoiEmpEnt.getIdSitPro())) {
                        idSituationProfessionnelleCourante = apSitProJoiEmpEnt.getIdSitPro();
                        // Définition de l'association
                        final APAssuranceTypeAssociation association = getAssociationFromSituationProf(apSitProJoiEmpEnt);
                        // Définition du type de prestation
                        // APTypeDePrestation typeDePrestation = null;
                        //
                        // typeDePrestation = this.getTypeDePrestationDepuisSituationProf(apSitProJoiEmpEnt);

                        prestationStandard.getSituationProfessionnelle().put(
                                apSitProJoiEmpEnt.getIdSitPro(),
                                new APSituationProfessionnelleData(association, apSitProJoiEmpEnt.getDateDebut(),
                                        apSitProJoiEmpEnt.getDateFin(), new BigDecimal(apSitProJoiEmpEnt
                                                .getMontantJournalierAcmNe()), /* typeDePrestation, */
                                        apSitProJoiEmpEnt.getIdSitPro(), apSitProJoiEmpEnt.getIdTiers(),
                                        apSitProJoiEmpEnt.getIdAffilie(), apSitProJoiEmpEnt.getNom()));
                    }
                } else {
                    throw new Exception("APCalculerAcmNeService.convert(): impossible de retrouver la prestation");
                }
            }
            listePrestationsAcmNeDomaineConverties.add(prestationStandard);
        }

        return listePrestationsAcmNeDomaineConverties;
    }

    private BigDecimal round5cts(final BigDecimal valeur) {
        return new BigDecimal(Math.ceil(valeur.doubleValue() * 20) / 20).setScale(2, RoundingMode.HALF_UP);
    }

}
