package globaz.corvus.utils.adaptation.rentes;

import acor.ch.admin.zas.rc.annonces.rente.rc.*;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.globall.db.BTransaction;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class REAnnonces51Mapper {

    private final BTransaction transaction;

    public REAnnonce51 createAnnonce51Ordinaire(RRBestandesmeldung9Type rrBestandesmeldung9Type) throws Exception {
        RRBestandesmeldungO9Type ordentlicheRente = rrBestandesmeldung9Type.getOrdentlicheRente();
        REAnnonce51 ann51 = new REAnnonce51();

        // 1 | Code application: 51
        ann51.setCodeApplication("51");

        // 2 | Code enregistrement: 01
        ann51.setCodeEnregistrement01("01");

        String numeroCaisseAgence = PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ordentlicheRente.getKasseZweigstelle()),6);
        // 3 | Numéro de la Caisse
        ann51.setNumeroCaisse(numeroCaisseAgence.substring(0, 3));

        // 4 | Numéro de l'agence
        ann51.setNumeroAgence(numeroCaisseAgence.substring(3, 6));

        // 5 | Numéro de l'annonce
        ann51.setNumeroAnnonce(PRConverterUtils.formatLongToString(ordentlicheRente.getMeldungsnummer()));

        // 6 | Référence interne de la Caisse
        ann51.setReferenceCaisseInterne(ordentlicheRente.getKasseneigenerHinweis());

        // 7 | Numéro d'assuré de l'ayant droit à
        // | la prestation (11 chiffres)
        ann51.setNoAssAyantDroit(ordentlicheRente.getLeistungsberechtigtePerson().getVersichertennummer());

        // 8 | Premier numéro d'assuré
        // | complémentaire
        if (!ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().isEmpty()) {
            ann51.setPremierNoAssComplementaire(ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().get(0));
        }

        // 9 | Second numéro de l'assuré
        // | comlpémentaire
        if (!ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().isEmpty()) {
            ann51.setSecondNoAssComplementaire(ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().get(0));
        }

        // 10 | Réserve: à blanc

        // 11 | Etat civi
        ann51.setEtatCivil(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsberechtigtePerson().getZivilstand()));

        // 12 | Réfugié
        ann51.setIsRefugie(PRConverterUtils.formatBooleanToString(ordentlicheRente.getLeistungsberechtigtePerson().isIstFluechtling()));

        // 13 | Canton/Etat de domicile --> sur 3 caractères.
        ann51.setCantonEtatDomicile(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsberechtigtePerson().getWohnkantonStaat()), 3));

        // 14 | Genre de prestations
        ann51.setGenrePrestation(ordentlicheRente.getLeistungsbeschreibung().getLeistungsart());

        // 15 | Début du droit: MMAA
        ann51.setDebutDroit(PRConverterUtils.formatDateToMMAA(ordentlicheRente.getLeistungsbeschreibung().getAnspruchsbeginn()));

        // 16 | Mensualité de la prestation en francs --> sur 5 caractères.
        ann51.setMensualitePrestationsFrancs(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(ordentlicheRente.getLeistungsbeschreibung().getMonatsbetrag()), 5));

        // 18 | Fin du droit: MMAA
        ann51.setFinDroit(PRConverterUtils.formatDateToMMAA(ordentlicheRente.getLeistungsbeschreibung().getAnspruchsende()));

        // 19 | Mois de rapport: MMAA
        ann51.setMoisRapport(PRConverterUtils.formatDateToMMAA(ordentlicheRente.getBerichtsmonat()));

        // 20 | Code de mutation
        ann51.setCodeMutation(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getMutationscode()));

        // 21 | Réserve: à blanc

        ann51.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        REAnnonce51 ann51_02 = new REAnnonce51();
        // 1 | Code application: 51
        ann51_02.setCodeApplication("51");

        // 2 | Code enregistrement: 02
        ann51_02.setCodeEnregistrement01("02");

        // 3 | Revenu annuel moyen déterminant en francs --> sur 8 caractères
        ann51_02.setRamDeterminant(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung().getDurchschnittlichesJahreseinkommen()), 8));

        // 4 | Durée de cotisation pour déterminer le revenu annuel moyen : AAMM
        ann51_02.setDureeCotPourDetRAM(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung().getBeitragsdauerDurchschnittlichesJahreseinkommen()));

        // 5 | Année de niveau
        ann51_02.setAnneeNiveau(PRConverterUtils.formatDateToAA(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getNiveaujahr()));

        // 6 | Revenus pris en compte
        ann51_02.setRevenuPrisEnCompte(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung().getAngerechneteEinkommen()));

        // 7 | Echelle de rente
        ann51_02.setEchelleRente(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getSkala()));

        // 8 | Durée de cotisations prise en compte pour le choix de l'échelle
        // de rentes avant 1973 : AAMM
        ann51_02.setDureeCoEchelleRenteAv73(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getBeitragsdauerVor1973()));

        // 9 | Durée de cotisations prise en compte pour le choix de l'échelle
        // de rentes après 1973 : AAMM
        ann51_02.setDureeCoEchelleRenteDes73(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getBeitragsdauerAb1973()));

        // 10 | Prise en compte des durées de cotisations manquantes en mois pour les années 1948-72 --> sur 2 caractères
        ann51_02.setDureeCotManquante48_72(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getAnrechnungVor1973FehlenderBeitragsmonate()), 2));

        // 11 | Années de cotisations de la classe d'âge --> sur 2 caractères.
        ann51_02.setAnneeCotClasseAge(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getBeitragsjahreJahrgang()), 2));

        RRBestandesmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter flexiblesRentenAlter = ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getFlexiblesRentenAlter();
        if (Objects.nonNull(flexiblesRentenAlter)) {
            // 12 | Durée de l'ajournement : AMM
            ann51_02.setDureeAjournement(flexiblesRentenAlter.getRentenaufschub().getAufschubsdauer().setScale(2).toString().replace(".",""));

            // 13 | Supplément d'ajournement en francs --> sur 5 caractères
            String supplementAjournement = String.valueOf(flexiblesRentenAlter.getRentenaufschub().getAufschubszuschlag().intValue());
            ann51_02.setSupplementAjournement(PRConverterUtils.indentLeftWithZero(supplementAjournement,5));

            // 14 | Date de révocation de l'ajournement : MMAA
            ann51_02.setDateRevocationAjournement(PRConverterUtils.formatDateToMMAA(flexiblesRentenAlter.getRentenaufschub().getAbrufdatum()));
        }

        IVDaten9Type ivDaten = ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDaten();
        if (Objects.nonNull(ivDaten)) {
            // 17 | Office AI compétent - ayant droit
            ann51_02.setOfficeAICompetent(PRConverterUtils.formatIntegerToString(ivDaten.getIVStelle()));

            // 19 | Degré invalidité ayant droit --> sur 3 caractères
            ann51_02.setDegreInvalidite(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDaten.getInvaliditaetsgrad()),3));

            // 21 | Code l'infirmité - ayant droit --> clé d'affliction sur 3 caractères + Code de défaillance fonctionnelle sur 2 caractères
            StringBuilder codeInfirmite = new StringBuilder(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ivDaten.getGebrechensschluessel()),3)).append(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDaten.getFunktionsausfallcode()),2));
            ann51_02.setCodeInfirmite(codeInfirmite.toString());

            // 23 | Survenance de l'événement assuré - ayant droit
            ann51_02.setSurvenanceEvenAssure(PRConverterUtils.formatDateToMMAA(ivDaten.getDatumVersicherungsfall()));

            // 25 | Age au début de l'invalidité - ayant droit
            ann51_02.setAgeDebutInvalidite(PRConverterUtils.formatBooleanToString(ivDaten.isIstFruehInvalid()));
        }

        IVDaten9Type ivDatenFrau = ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDatenEhefrau();
        if (Objects.nonNull(ivDatenFrau)) {
            // 18 | Office AI compétent - épouse
            ann51_02.setOfficeAiCompEpouse(PRConverterUtils.formatIntegerToString(ivDatenFrau.getIVStelle()));

            // 20 | Degré invalidité épouse
            ann51_02.setDegreInvaliditeEpouse(PRConverterUtils.formatShortToString(ivDatenFrau.getInvaliditaetsgrad()));

            // 22 | Code de l'informité - épouse --> clé d'affliction sur 3 caractères + Code de défaillance fonctionnelle sur 2 caractères
            StringBuilder codeInfirmiteEpouse = new StringBuilder(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ivDatenFrau.getGebrechensschluessel()),3)).append(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDatenFrau.getFunktionsausfallcode()),2));
            ann51_02.setCodeInfirmiteEpouse(codeInfirmiteEpouse.toString());

            // 24 | Survenance de l'événement assuré - épouse
            ann51_02.setSurvenanceEvtAssureEpouse(PRConverterUtils.formatDateToMMAA(ivDatenFrau.getDatumVersicherungsfall()));

            // 26 | Age au début de l'invalidité - épouse
            ann51_02.setAgeDebutInvaliditeEpouse(PRConverterUtils.formatBooleanToString(ivDatenFrau.isIstFruehInvalid()));
        }

        // 28 | Réduction --> sur 2 caractères.
        ann51_02.setReduction(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getKuerzungSelbstverschulden()), 2));

        List<Short> codesCasSpeciaux = ordentlicheRente.getLeistungsbeschreibung().getSonderfallcodeRente();
        if (Objects.nonNull(codesCasSpeciaux)) {
            if (codesCasSpeciaux.size() > 0) {
                // 29 | Cas spécial code 1
                ann51_02.setCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
            }
            if (codesCasSpeciaux.size() > 1) {
                // 30 | Cas spécial code 2
                ann51_02.setCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
            }
            if (codesCasSpeciaux.size() > 2) {
                // 31 | Cas spécial code 3
                ann51_02.setCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
            }
            if (codesCasSpeciaux.size() > 3) {
                // 32 | Cas spécial code 4
                ann51_02.setCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
            }
            if (codesCasSpeciaux.size() > 4) {
                // 33 | Cas spécial code 5
                ann51_02.setCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
            }
        }

        // 34 | Prise en compte des durées de cotisations manquantes en mois pour les années 73-78 --> sur 2 caractères
        ann51_02.setDureeCotManquante73_78(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getAnrechnungAb1973Bis1978FehlenderBeitragsmonate()),2));

        // 35 | Revenu annuel moyen sans BTE
        ann51_02.setRevenuAnnuelMoyenSansBTE(PRConverterUtils.formatBigDecimalToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften().getDJEohneErziehungsgutschrift()));

        // 36 | Bonifications pour tâches éducatives moyennes prises en compte
        // en frans
        ann51_02.setBteMoyennePrisEnCompte(PRConverterUtils.formatBigDecimalToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften().getAngerechneteErziehungsgutschrift()));

        // 37 | Nombre d'années BTE
        ann51_02.setNombreAnneeBTE(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften().getAnzahlErziehungsgutschrift()));

        // 38 | Réserve: à blanc

        ann51_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        REAnnonce51 ann51_03 = createAnnonces51InfosComplementaires(rrBestandesmeldung9Type.getZusaetzlicheAngabenZAS());
        ann51_02.setIdLienAnnonce(ann51_03.getIdAnnonce());
        ann51_02.add(transaction);
        ann51.setIdLienAnnonce(ann51_02.getIdAnnonce());
        ann51.add(transaction);

        return ann51;

    }

    private REAnnonce51 createAnnonces51InfosComplementaires(RRBestandesmeldung9Type.ZusaetzlicheAngabenZAS zusaetzlicheAngabenZAS) throws Exception {
        REAnnonce51 ann51_03 = new REAnnonce51();

        // 1 | Code application
        ann51_03.setCodeApplication("51");

        // 2 | Code enregistrement
        ann51_03.setCodeEnregistrement01("03");

        // 3 | Nom de famille - nom de jeune fille, prénom
        ann51_03.setEtatNominatif(zusaetzlicheAngabenZAS.getNamensangaben());

        // 4 | Nationalité
        ann51_03.setEtatOrigine(PRConverterUtils.formatIntegerToString(zusaetzlicheAngabenZAS.getHeimatstaat()));

        // 5 | Fraction de la rente --> quotité
        ann51_03.setQuotite(PRConverterUtils.formatBigDecimalToString(zusaetzlicheAngabenZAS.getProzentualAnteil()));
//        ann51_03.setFractionRente(JadeStringUtil.substring(getChampEnregistrement(annonce), 47, 1));

        RRBestandesmeldung9Type.ZusaetzlicheAngabenZAS.BisherigeWerte bisherigeWerte = zusaetzlicheAngabenZAS.getBisherigeWerte();
        if (Objects.nonNull(bisherigeWerte)) {
            // 6 | Ancien revenu annuel déterminant moyen en francs --> sur 8 caractères
            ann51_03.setAncienRAM(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getDurchschnittlichesJahreseinkommen()),8));

            // 7 | Ancienne RO remplacéé --> sur 5 caractères.
            ann51_03.setMontantAncRenteRemplacee(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getMonatsbetragErsetzteOrdentlicheRente()),5));

            // 8 | Ancien montant mensuel --> sur 5 caractères
            ann51_03.setAncienMontantMensuel(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getMonatsbetrag()),5));

            List<Short> codesCasSpeciaux = bisherigeWerte.getSonderfallcodeRente();
            if (Objects.nonNull(codesCasSpeciaux)) {
                if (codesCasSpeciaux.size() > 0) {
                    // 9 | Ancien code cas spécial 1
                    ann51_03.setAncienCodeCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
                }
                if (codesCasSpeciaux.size() > 1) {
                    // 10 | Ancien code cas spécial 2
                    ann51_03.setAncienCodeCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
                }
                if (codesCasSpeciaux.size() > 2) {
                    // 11 | Ancien code cas spécial 3
                    ann51_03.setAncienCodeCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
                }
                if (codesCasSpeciaux.size() > 3) {
                    // 12 | Ancien code cas spécial 4
                    ann51_03.setAncienCodeCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
                }
                if (codesCasSpeciaux.size() > 4) {
                    // 13 | Ancien code cas spécial 5
                    ann51_03.setAncienCodeCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
                }
            }

            // 15 | Ancien revenu annuel moyen sans BTE
            ann51_03.setAncienRAMSansBTE(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getDJEohneErziehungsgutschrift()));

            // 16 | Anciennes BTE prises en comtpe
            ann51_03.setAncienneBTEMoyennePrisCompte(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getAngerechneteErziehungsgutschrift()));

            // 17 | Ancien supplément d'ajournement en francs --> sur 5 caractères.
            String ancienSupplementAjournement = String.valueOf(bisherigeWerte.getAufschubszuschlag().intValue());
            ann51_03.setAncienSupplementAjournement(PRConverterUtils.indentLeftWithZero(ancienSupplementAjournement, 5));
        }

        // 14 | Observations de la centrale
        ann51_03.setObservationCentrale(zusaetzlicheAngabenZAS.getBemerkungenZAS());

        // 18 | Réserve: à blanc

        ann51_03.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann51_03.add(transaction);

        return ann51_03;

    }

    public REAnnonce51 createAnnonce51Extraordinaire(RRBestandesmeldung9Type rrBestandesmeldung9Type) throws Exception {
        RRBestandesmeldungAO9Type ausserordentlicheRente = rrBestandesmeldung9Type.getAusserordentlicheRente();

        REAnnonce51 ann51 = new REAnnonce51();

        // 1 | Code application: 51
        ann51.setCodeApplication("51");

        // 2 | Code enregistrement: 01
        ann51.setCodeEnregistrement01("01");

        String numeroCaisseAgence = PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ausserordentlicheRente.getKasseZweigstelle()),6);
        // 3 | Numéro de la Caisse
        ann51.setNumeroCaisse(numeroCaisseAgence.substring(0,3));

        // 4 | Numéro de l'agence
        ann51.setNumeroAgence(numeroCaisseAgence.substring(3,6));

        // 5 | Numéro de l'annonce
        ann51.setNumeroAnnonce(PRConverterUtils.formatLongToString(ausserordentlicheRente.getMeldungsnummer()));

        // 6 | Référence interne de la Caisse
        ann51.setReferenceCaisseInterne(ausserordentlicheRente.getKasseneigenerHinweis());

        // 7 | Numéro d'assuré de l'ayant droit à
        // | la prestation (11 chiffres)
        ann51.setNoAssAyantDroit(ausserordentlicheRente.getLeistungsberechtigtePerson().getVersichertennummer());

        // 8 | Premier numéro d'assuré
        // | complémentaire
        if (!ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().isEmpty()) {
            ann51.setPremierNoAssComplementaire(ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().get(0));
        }

        // 9 | Second numéro de l'assuré
        // | comlpémentaire
        if (!ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().isEmpty()) {
            ann51.setSecondNoAssComplementaire(ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().get(0));
        }

        // 10 | Réserve: à blanc

        // 11 | Etat civi
        ann51.setEtatCivil(PRConverterUtils.formatShortToString(ausserordentlicheRente.getLeistungsberechtigtePerson().getZivilstand()));

        // 12 | Réfugié
        ann51.setIsRefugie(PRConverterUtils.formatBooleanToString(ausserordentlicheRente.getLeistungsberechtigtePerson().isIstFluechtling()));

        // 13 | Canton/Etat de domicile --> sur 3 caractères
        ann51.setCantonEtatDomicile(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ausserordentlicheRente.getLeistungsberechtigtePerson().getWohnkantonStaat()), 3));

        // 14 | Genre de prestations
        ann51.setGenrePrestation(ausserordentlicheRente.getLeistungsbeschreibung().getLeistungsart());

        // 15 | Début du droit: MMAA
        ann51.setDebutDroit(PRConverterUtils.formatDateToMMAA(ausserordentlicheRente.getLeistungsbeschreibung().getAnspruchsbeginn()));

        // 16 | Mensualité de la prestation en francs --> sur 5 caractères
        ann51.setMensualitePrestationsFrancs(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(ausserordentlicheRente.getLeistungsbeschreibung().getMonatsbetrag()), 5));

        // 17 | Mensualité de la rente ordinaire | remplacée en francs --> sur 5 caractères
        ann51.setMontantAncRenteRemplacee(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(ausserordentlicheRente.getLeistungsbeschreibung().getMonatsbetragErsetzteOrdentlicheRente()),5));

        // 18 | Fin du droit: MMAA
        ann51.setFinDroit(PRConverterUtils.formatDateToMMAA(ausserordentlicheRente.getLeistungsbeschreibung().getAnspruchsende()));

        // 19 | Mois de rapport: MMAA
        ann51.setMoisRapport(PRConverterUtils.formatDateToMMAA(ausserordentlicheRente.getBerichtsmonat()));

        // 20 | Code de mutation
        ann51.setCodeMutation(PRConverterUtils.formatShortToString(ausserordentlicheRente.getLeistungsbeschreibung().getMutationscode()));

        // 21 | Réserve: à blanc

        ann51.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        REAnnonce51 ann51_02 = new REAnnonce51();
        // 1 | Code application: 51
        ann51_02.setCodeApplication("51");

        // 2 | Code enregistrement: 02
        ann51_02.setCodeEnregistrement01("02");

        // 5 | Année de niveau
        ann51_02.setAnneeNiveau(PRConverterUtils.formatDateToAA(ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getNiveaujahr()));

        DJE9BeschreibungType djeBeschreibung = ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung();
        if (Objects.nonNull(djeBeschreibung)) {
            // 3 | Revenu annuel moyen déterminant en francs --> sur 8 caractères
            ann51_02.setRamDeterminant(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(djeBeschreibung.getDurchschnittlichesJahreseinkommen()), 8));

            // 4 | Durée de cotisation pour déterminer le revenu annuel moyen : AAMM
            ann51_02.setDureeCotPourDetRAM(PRConverterUtils.formatBigDecimal(djeBeschreibung.getBeitragsdauerDurchschnittlichesJahreseinkommen()));

            // 6 | Revenus pris en compte
            ann51_02.setRevenuPrisEnCompte(PRConverterUtils.formatShortToString(djeBeschreibung.getAngerechneteEinkommen()));
        }

        SkalaBerechnungType skalaBerechnung = ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung();
        if (Objects.nonNull(skalaBerechnung)) {
            // 7 | Echelle de rente
            ann51_02.setEchelleRente(PRConverterUtils.formatShortToString(skalaBerechnung.getSkala()));

            // 8 | Durée de cotisations prise en compte pour le choix de l'échelle
            // de rentes avant 1973 : AAMM
            ann51_02.setDureeCoEchelleRenteAv73(PRConverterUtils.formatBigDecimal(skalaBerechnung.getBeitragsdauerVor1973()));

            // 9 | Durée de cotisations prise en compte pour le choix de l'échelle
            // de rentes après 1973 : AAMM
            ann51_02.setDureeCoEchelleRenteDes73(PRConverterUtils.formatBigDecimal(skalaBerechnung.getBeitragsdauerAb1973()));

            // 10 | Prise en compte des durées de cotisations manquantes en mois pour les années 1948-72 --> sur 2 caractères.
            ann51_02.setDureeCotManquante48_72(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(skalaBerechnung.getAnrechnungVor1973FehlenderBeitragsmonate()),2));

            // 11 | Années de cotisations de la classe d'âge --> sur 2 caractères
            ann51_02.setAnneeCotClasseAge(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(skalaBerechnung.getBeitragsjahreJahrgang()),2));

            // 34 | Prise en compte des durées de cotisations manquantes en mois pour les années 73-78 --> sur 2 caractères.
            ann51_02.setDureeCotManquante73_78(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(skalaBerechnung.getAnrechnungAb1973Bis1978FehlenderBeitragsmonate()),2));
        }

        // 15 | Limites de revenu
        ann51_02.setIsLimiteRevenu(PRConverterUtils.formatBooleanToString(ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().isEinkommensgrenzenCode()));

        // 16 | Minimum garanti
        ann51_02.setIsMinimumGaranti(PRConverterUtils.formatBooleanToString(rrBestandesmeldung9Type.getAusserordentlicheRente().getLeistungsbeschreibung().getBerechnungsgrundlagen().isMinimalgarantieCode()));

        IVDaten9Type ivDaten = ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDaten();
        if (Objects.nonNull(ivDaten)) {
            // 17 | Office AI compétent - ayant droit
            ann51_02.setOfficeAICompetent(PRConverterUtils.formatIntegerToString(ivDaten.getIVStelle()));

            // 19 | Degré invalidité ayant droit --> sur 3 caractères
            ann51_02.setDegreInvalidite(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDaten.getInvaliditaetsgrad()), 3));

            // 21 | Code l'infirmité - ayant droit --> clé d'affliction sur 3 caractères + Code de défaillance fonctionnelle sur 2 caractères
            StringBuilder codeInfirmite = new StringBuilder(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ivDaten.getGebrechensschluessel()),3)).append(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDaten.getFunktionsausfallcode()),2));
            ann51_02.setCodeInfirmite(codeInfirmite.toString());

            // 23 | Survenance de l'événement assuré - ayant droit
            ann51_02.setSurvenanceEvenAssure(PRConverterUtils.formatDateToMMAA(ivDaten.getDatumVersicherungsfall()));

            // 25 | Age au début de l'invalidité - ayant droit
            ann51_02.setAgeDebutInvalidite(PRConverterUtils.formatBooleanToString(ivDaten.isIstFruehInvalid()));
        }

        IVDaten9Type ivDatenFrau = ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDatenEhefrau();
        if (Objects.nonNull(ivDatenFrau)) {
            // 18 | Office AI compétent - épouse
            ann51_02.setOfficeAiCompEpouse(PRConverterUtils.formatIntegerToString(ivDatenFrau.getIVStelle()));

            // 20 | Degré invalidité épouse
            ann51_02.setDegreInvaliditeEpouse(PRConverterUtils.formatShortToString(ivDatenFrau.getInvaliditaetsgrad()));

            // 22 | Code de l'informité - épouse --> clé d'affliction sur 3 caractères + Code de défaillance fonctionnelle sur 2 caractères
            StringBuilder codeInfirmiteEpouse = new StringBuilder(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ivDatenFrau.getGebrechensschluessel()),3)).append(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDatenFrau.getFunktionsausfallcode()),2));
            ann51_02.setCodeInfirmiteEpouse(codeInfirmiteEpouse.toString());

            // 24 | Survenance de l'événement assuré - épouse
            ann51_02.setSurvenanceEvtAssureEpouse(PRConverterUtils.formatDateToMMAA(ivDatenFrau.getDatumVersicherungsfall()));

            // 26 | Age au début de l'invalidité - épouse
            ann51_02.setAgeDebutInvaliditeEpouse(PRConverterUtils.formatBooleanToString(ivDatenFrau.isIstFruehInvalid()));
        }

        // 28 | Réduction --> sur 2 caractères
        ann51_02.setReduction(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ausserordentlicheRente.getLeistungsbeschreibung().getKuerzungSelbstverschulden()),2));

        List<Short> codesCasSpeciaux = ausserordentlicheRente.getLeistungsbeschreibung().getSonderfallcodeRente();
        if (Objects.nonNull(codesCasSpeciaux)) {
            if (codesCasSpeciaux.size() > 0) {
                // 29 | Cas spécial code 1
                ann51_02.setCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
            }
            if (codesCasSpeciaux.size() > 1) {
                // 30 | Cas spécial code 2
                ann51_02.setCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
            }
            if (codesCasSpeciaux.size() > 2) {
                // 31 | Cas spécial code 3
                ann51_02.setCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
            }
            if (codesCasSpeciaux.size() > 3) {
                // 32 | Cas spécial code 4
                ann51_02.setCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
            }
            if (codesCasSpeciaux.size() > 4) {
                // 33 | Cas spécial code 5
                ann51_02.setCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
            }
        }

        Gutschriften9Type gutschriften = ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften();
        if (Objects.nonNull(gutschriften)) {
            // 35 | Revenu annuel moyen sans BTE
            ann51_02.setRevenuAnnuelMoyenSansBTE(PRConverterUtils.formatBigDecimalToString(gutschriften.getDJEohneErziehungsgutschrift()));

            // 36 | Bonifications pour tâches éducatives moyennes prises en compte
            // en frans
            ann51_02.setBteMoyennePrisEnCompte(PRConverterUtils.formatBigDecimalToString(gutschriften.getAngerechneteErziehungsgutschrift()));

            // 37 | Nombre d'années BTE
            ann51_02.setNombreAnneeBTE(PRConverterUtils.formatShortToString(gutschriften.getAnzahlErziehungsgutschrift()));
        }

        // 38 | Réserve: à blanc

        ann51_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        REAnnonce51 ann51_03 = createAnnonces51InfosComplementaires(rrBestandesmeldung9Type.getZusaetzlicheAngabenZAS());
        ann51_02.setIdLienAnnonce(ann51_03.getIdAnnonce());
        ann51_02.add(transaction);
        ann51.setIdLienAnnonce(ann51_02.getIdAnnonce());
        ann51.add(transaction);

        return ann51;
    }

    public REAnnonce51 createAnnonce51Indemnisation(RRBestandesmeldung9Type rrBestandesmeldung9Type) throws Exception {
        RRBestandesmeldungHE9Type hilflosenentschaedigung = rrBestandesmeldung9Type.getHilflosenentschaedigung();

        REAnnonce51 ann51 = new REAnnonce51();

        // 1 | Code application: 51
        ann51.setCodeApplication("51");

        // 2 | Code enregistrement: 01
        ann51.setCodeEnregistrement01("01");

        String numeroCaisseAgence = PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(hilflosenentschaedigung.getKasseZweigstelle()),6);
        // 3 | Numéro de la Caisse
        ann51.setNumeroCaisse(numeroCaisseAgence.substring(0,3));

        // 4 | Numéro de l'agence
        ann51.setNumeroAgence(numeroCaisseAgence.substring(3,6));

        // 5 | Numéro de l'annonce
        ann51.setNumeroAnnonce(PRConverterUtils.formatLongToString(hilflosenentschaedigung.getMeldungsnummer()));

        // 6 | Référence interne de la Caisse
        ann51.setReferenceCaisseInterne(hilflosenentschaedigung.getKasseneigenerHinweis());

        // 7 | Numéro d'assuré de l'ayant droit à
        // | la prestation (11 chiffres)
        ann51.setNoAssAyantDroit(hilflosenentschaedigung.getLeistungsberechtigtePerson().getVersichertennummer());

        // 8 | Premier numéro d'assuré
        // | complémentaire
        if (!hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().isEmpty()) {
            ann51.setPremierNoAssComplementaire(hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().get(0));
        }

        // 9 | Second numéro de l'assuré
        // | comlpémentaire
        if (!hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().isEmpty()) {
            ann51.setSecondNoAssComplementaire(hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().get(0));
        }

        // 10 | Réserve: à blanc

        // 11 | Etat civi
        ann51.setEtatCivil(PRConverterUtils.formatShortToString(hilflosenentschaedigung.getLeistungsberechtigtePerson().getZivilstand()));

        // 12 | Réfugié
        ann51.setIsRefugie(PRConverterUtils.formatBooleanToString(hilflosenentschaedigung.getLeistungsberechtigtePerson().isIstFluechtling()));

        // 13 | Canton/Etat de domicile --> sur 3 caractères.
        ann51.setCantonEtatDomicile(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(hilflosenentschaedigung.getLeistungsberechtigtePerson().getWohnkantonStaat()), 3));

        // 14 | Genre de prestations
        ann51.setGenrePrestation(hilflosenentschaedigung.getLeistungsbeschreibung().getLeistungsart());

        // 15 | Début du droit: MMAA
        ann51.setDebutDroit(PRConverterUtils.formatDateToMMAA(hilflosenentschaedigung.getLeistungsbeschreibung().getAnspruchsbeginn()));

        // 16 | Mensualité de la prestation en francs --> sur 5 caractères
        ann51.setMensualitePrestationsFrancs(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatBigDecimalToString(hilflosenentschaedigung.getLeistungsbeschreibung().getMonatsbetrag()), 5));

        // 18 | Fin du droit: MMAA
        ann51.setFinDroit(PRConverterUtils.formatDateToMMAA(hilflosenentschaedigung.getLeistungsbeschreibung().getAnspruchsende()));

        // 19 | Mois de rapport: MMAA
        ann51.setMoisRapport(PRConverterUtils.formatDateToMMAA(hilflosenentschaedigung.getBerichtsmonat()));

        // 20 | Code de mutation
        ann51.setCodeMutation(PRConverterUtils.formatShortToString(hilflosenentschaedigung.getLeistungsbeschreibung().getMutationscode()));

        // 21 | Réserve: à blanc

        ann51.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        REAnnonce51 ann51_02 = new REAnnonce51();
        // 1 | Code application: 51
        ann51_02.setCodeApplication("51");

        // 2 | Code enregistrement: 02
        ann51_02.setCodeEnregistrement01("02");

        IVDatenHE9Type ivDaten = hilflosenentschaedigung.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDaten();
        if (Objects.nonNull(ivDaten)) {
            // 17 | Office AI compétent - ayant droit
            ann51_02.setOfficeAICompetent(PRConverterUtils.formatIntegerToString(ivDaten.getIVStelle()));

            // 21 | Code l'infirmité - ayant droit --> clé d'affliction sur 3 caractères + Code de défaillance fonctionnelle sur 2 caractères
            StringBuilder codeInfirmite = new StringBuilder(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatIntegerToString(ivDaten.getGebrechensschluessel()),3)).append(PRConverterUtils.indentLeftWithZero(PRConverterUtils.formatShortToString(ivDaten.getFunktionsausfallcode()),2));
            ann51_02.setCodeInfirmite(codeInfirmite.toString());

            // 23 | Survenance de l'événement assuré - ayant droit
            ann51_02.setSurvenanceEvenAssure(PRConverterUtils.formatDateToMMAA(ivDaten.getDatumVersicherungsfall()));

            // 27 | Genre du droit à l'API
            ann51_02.setGenreDroitAPI(PRConverterUtils.formatShortToString(ivDaten.getArtHEAnspruch()));
        }


        List<Short> codesCasSpeciaux = hilflosenentschaedigung.getLeistungsbeschreibung().getSonderfallcodeRente();
        if (Objects.nonNull(codesCasSpeciaux)) {
            if (codesCasSpeciaux.size() > 0) {
                // 29 | Cas spécial code 1
                ann51_02.setCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
            }
            if (codesCasSpeciaux.size() > 1) {
                // 30 | Cas spécial code 2
                ann51_02.setCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
            }
            if (codesCasSpeciaux.size() > 2) {
                // 31 | Cas spécial code 3
                ann51_02.setCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
            }
            if (codesCasSpeciaux.size() > 3) {
                // 32 | Cas spécial code 4
                ann51_02.setCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
            }
            if (codesCasSpeciaux.size() > 4) {
                // 33 | Cas spécial code 5
                ann51_02.setCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
            }
        }

        // 38 | Réserve: à blanc

        ann51_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        REAnnonce51 ann51_03 = createAnnonces51InfosComplementaires(rrBestandesmeldung9Type.getZusaetzlicheAngabenZAS());
        ann51_02.setIdLienAnnonce(ann51_03.getIdAnnonce());
        ann51_02.add(transaction);
        ann51.setIdLienAnnonce(ann51_02.getIdAnnonce());
        ann51.add(transaction);

        return ann51;
    }

}
