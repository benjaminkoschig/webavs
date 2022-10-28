package globaz.corvus.utils.adaptation.rentes;

import acor.ch.admin.zas.rc.annonces.rente.rc.*;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.globall.db.BTransaction;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class REAnnonces53Mapper {

    private final BTransaction transaction;

    public REAnnonce53 createAnnonce53Ordinaire(RRBestandesmeldung10Type bestandesmeldung10Type) throws Exception {
        RRBestandesmeldungO10Type ordentlicheRente = bestandesmeldung10Type.getOrdentlicheRente();

        REAnnonce53 ann53 = new REAnnonce53();

        // 1 | Code application: 53
        ann53.setCodeApplication("53");

        // 2 | Code enregistrement: 01
        ann53.setCodeEnregistrement01("01");

        // 3 | Num�ro de la Caisse
        ann53.setNumeroCaisse(PRConverterUtils.formatIntegerToString(ordentlicheRente.getKasseZweigstelle()).substring(0, 3));

        // 4 | Num�ro de l'agence
        ann53.setNumeroAgence(PRConverterUtils.formatIntegerToString(ordentlicheRente.getKasseZweigstelle()).substring(3, 6));

        // 5 | Num�ro de l'annonce
        ann53.setNumeroAnnonce(PRConverterUtils.formatLongToString(ordentlicheRente.getMeldungsnummer()));

        // 6 | R�f�rence interne de la Caisse
        ann53.setReferenceCaisseInterne(ordentlicheRente.getKasseneigenerHinweis());

        // 7 | Num�ro d'assur� de l'ayant droit �
        ann53.setNoAssAyantDroit(ordentlicheRente.getLeistungsberechtigtePerson().getVersichertennummer());

        // 8 | Premier num�ro d'assur�
        if (!ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().isEmpty()) {
            ann53.setPremierNoAssComplementaire(ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().get(0));
        }

        // 9 | Second num�ro de l'assur�
        if (!ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().isEmpty()) {
            ann53.setSecondNoAssComplementaire(ordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().get(0));
        }

        // 10 | R�serve: � blanc

        // 11 | Etat civi
        ann53.setEtatCivil(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsberechtigtePerson().getZivilstand()));

        // 12 | R�fugi�
        ann53.setIsRefugie(PRConverterUtils.formatBooleanToString(ordentlicheRente.getLeistungsberechtigtePerson().isIstFluechtling()));

        // 13 | Canton/Etat de domicile
        ann53.setCantonEtatDomicile(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsberechtigtePerson().getWohnkantonStaat()));

        // 14 | Genre de prestations
        ann53.setGenrePrestation(ordentlicheRente.getLeistungsbeschreibung().getLeistungsart());

        // 15 | D�but du droit: MMAA
        ann53.setDebutDroit(PRConverterUtils.formatDateToMMAA(ordentlicheRente.getLeistungsbeschreibung().getAnspruchsbeginn()));

        // 16 | Mensualit� de la prestation en francs
        ann53.setMensualitePrestationsFrancs(PRConverterUtils.formatBigDecimalToString(ordentlicheRente.getLeistungsbeschreibung().getMonatsbetrag()));

        // 17 | Fin du droit: MMAA
        ann53.setFinDroit(PRConverterUtils.formatDateToMMAA(ordentlicheRente.getLeistungsbeschreibung().getAnspruchsende()));

        // 18 | Mois de rapport: MMAA
        ann53.setMoisRapport(PRConverterUtils.formatDateToMMAA(ordentlicheRente.getBerichtsmonat()));

        // 19 | Code de mutation
        ann53.setCodeMutation(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getMutationscode()));

        // 20 | R�serve: � blanc

        ann53.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53.add(transaction);

        REAnnonce53 ann53_02 = new REAnnonce53();
        // 1 | Code application: 53
        ann53_02.setCodeApplication("53");

        // 2 | Code enregistrement: 02
        ann53_02.setCodeEnregistrement01("02");

        // 3 | Echelle de rente
        ann53_02.setEchelleRente(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getSkala()));

        // 4 | Dur�e de cotisations prise en compte pour le choix de l'�chelle
        // de rentes avant 1973 : AAMM
        ann53_02.setDureeCoEchelleRenteAv73(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getBeitragsdauerVor1973()));

        // 5 | Dur�e de cotisations prise en compte pour le choix de l'�chelle
        // de rentes apr�s 1973 : AAMM
        ann53_02.setDureeCoEchelleRenteDes73(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getBeitragsdauerAb1973()));

        // 6 | Prise en compte des dur�es de cotisations manquantes en mois pour
        // les ann�es 1948-72
        ann53_02.setDureeCotManquante48_72(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getAnrechnungVor1973FehlenderBeitragsmonate()));

        // 7 | Prise en compte des dur�es de cotisations manquantes en mois pour
        // les ann�es 73-78
        ann53_02.setDureeCotManquante73_78(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getAnrechnungAb1973Bis1978FehlenderBeitragsmonate()));

        // 8 | Ann�es de cotisations de la classe d'�ge
        ann53_02.setAnneeCotClasseAge(PRConverterUtils.formatIntegerToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getSkalaBerechnung().getBeitragsjahreJahrgang()));

        // 9 | Revenu annuel moyen en francs
        ann53_02.setRamDeterminant(PRConverterUtils.formatBigDecimalToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung().getDurchschnittlichesJahreseinkommen()));

        // 10 | Code de revenus splitt�s
        ann53_02.setCodeRevenuSplitte(PRConverterUtils.formatBooleanToString(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung().isGesplitteteEinkommen()));

        // 11 | Dur�e de cotisations pour d�terminer le RAM
        ann53_02.setDureeCotPourDetRAM(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getDJEBeschreibung().getBeitragsdauerDurchschnittlichesJahreseinkommen()));

        // 12 | Ann�e de niveau
        ann53_02.setAnneeNiveau(PRConverterUtils.formatDateToAA(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getNiveaujahr()));

        // 13 | Nombre d'ann�es BTE
        ann53_02.setNombreAnneeBTE(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften().getAnzahlErziehungsgutschrift()));

        // 14 | Nombre d'ann�es BTA
        ann53_02.setNbreAnneeBTA(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften().getAnzahlBetreuungsgutschrift()));

        // 15 | Nombre d'ann�es avec des bonifications transitoires
        ann53_02.setNbreAnneeBonifTrans(PRConverterUtils.formatBigDecimal(ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getGutschriften().getAnzahlUebergangsgutschrift()));

        IVDaten10Type ivDaten = ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDaten();
        if (Objects.nonNull(ivDaten)) {
            // 16 | Office AI comp�tent
            ann53_02.setOfficeAICompetent(PRConverterUtils.formatIntegerToString(ivDaten.getIVStelle()));

            // 17 | Degr� invalidit�
            ann53_02.setDegreInvalidite(PRConverterUtils.formatShortToString(ivDaten.getInvaliditaetsgrad()));

            // 18 | Code l'infirmit�
            StringBuilder codeInfirmite = new StringBuilder(ivDaten.getGebrechensschluessel()).append(ivDaten.getFunktionsausfallcode());
            ann53_02.setCodeInfirmite(codeInfirmite.toString());

            // 19 | Survenance de l'�v�nement assur�
            ann53_02.setSurvenanceEvenAssure(PRConverterUtils.formatDateToMMAA(ivDaten.getDatumVersicherungsfall()));

            // 20 | Age au d�but de l'invalidit�
            ann53_02.setAgeDebutInvalidite(PRConverterUtils.formatBooleanToString(ivDaten.isIstFruehInvalid()));
        }

        // 22 | R�duction
        ann53_02.setReduction(PRConverterUtils.formatShortToString(ordentlicheRente.getLeistungsbeschreibung().getKuerzungSelbstverschulden()));

        List<Short> codesCasSpeciaux = ordentlicheRente.getLeistungsbeschreibung().getSonderfallcodeRente();
        if (Objects.nonNull(codesCasSpeciaux)) {
            if (codesCasSpeciaux.size() > 0) {
                // 23 | Cas sp�cial code 1
                ann53_02.setCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
            }
            if (codesCasSpeciaux.size() > 1) {
                // 24 | Cas sp�cial code 2
                ann53_02.setCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
            }
            if (codesCasSpeciaux.size() > 2) {
                // 25 | Cas sp�cial code 3
                ann53_02.setCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
            }
            if (codesCasSpeciaux.size() > 3) {
                // 26 | Cas sp�cial code 4
                ann53_02.setCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
            }
            if (codesCasSpeciaux.size() > 4) {
                // 27 | Cas sp�cial code 5
                ann53_02.setCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
            }
        }

        RRBestandesmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter flexiblesRentenAlter = ordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getFlexiblesRentenAlter();
        if (Objects.nonNull(flexiblesRentenAlter)) {
            RentenvorbezugType rentenvorbezug = flexiblesRentenAlter.getRentenvorbezug();
            RentenaufschubType rentenaufschub = flexiblesRentenAlter.getRentenaufschub();
            if (Objects.nonNull(rentenvorbezug)) {
                // 28 | Nombre d'ann�es d'anticipation
                ann53_02.setNbreAnneeAnticipation(PRConverterUtils.formatIntegerToString(rentenvorbezug.getAnzahlVorbezugsjahre()));

                // 29 | r�duction anticipation
                ann53_02.setReductionAnticipation(PRConverterUtils.formatBigDecimalToString(rentenvorbezug.getVorbezugsreduktion()));

                // 30 | Date d�but anticipation MMAA
                ann53_02.setDateDebutAnticipation(PRConverterUtils.formatDateToMMAA(rentenvorbezug.getVorbezugsdatum()));
            } else if (Objects.nonNull(rentenaufschub)) {
                // 31 | Dur�e ajournement
                ann53_02.setDureeAjournement(PRConverterUtils.formatBigDecimal(rentenaufschub.getAufschubsdauer()));

                // 32 | Suppl�ment d'ajournement en francs
                ann53_02.setSupplementAjournement(PRConverterUtils.formatBigDecimalToString(rentenaufschub.getAufschubszuschlag()));

                // 33 | Date r�vocation ajournement MMAA
                ann53_02.setDateRevocationAjournement(PRConverterUtils.formatDateToMMAA(rentenaufschub.getAbrufdatum()));
            }
        }

        // 34 | code survivant invalide
        ann53_02.setIsSurvivant(PRConverterUtils.formatBooleanToString(ordentlicheRente.getLeistungsbeschreibung().isIstInvaliderHinterlassener()));

        // 35 | R�serve: � blanc

        ann53_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53_02.add(transaction);

        // mise � jour de l'idLien de l'annonce 01
        ann53.retrieve();
        ann53.setIdLienAnnonce(ann53_02.getIdAnnonce());
        ann53.update(transaction);


        REAnnonce53 ann53_03 = createAnnonces53InfosComplementaires(bestandesmeldung10Type.getZusaetzlicheAngabenZAS());

        // mise � jour de l'idLien de l'annonce 01
        ann53_02.retrieve();
        ann53_02.setIdLienAnnonce(ann53_03.getIdAnnonce());
        ann53_02.update(transaction);

        return ann53;
    }

    private REAnnonce53 createAnnonces53InfosComplementaires(RRBestandesmeldung10Type.ZusaetzlicheAngabenZAS zusaetzlicheAngabenZAS) throws Exception {
        REAnnonce53 ann53_03 = new REAnnonce53();

        // 1 | Code application
        ann53_03.setCodeApplication("53");

        // 2 | Code enregistrement
        ann53_03.setCodeEnregistrement01("03");

        // 3 | Nom de famille - nom de jeune fille, pr�nom
        ann53_03.setEtatNominatif(zusaetzlicheAngabenZAS.getNamensangaben());

        // 4 | Nationalit�
        ann53_03.setEtatOrigine(PRConverterUtils.formatIntegerToString(zusaetzlicheAngabenZAS.getHeimatstaat()));

        // 5 | Fraction de la rente --> quotit� � pr�sent
        ann53_03.setQuotite(PRConverterUtils.formatBigDecimalToString(zusaetzlicheAngabenZAS.getProzentualAnteil()));

        RRBestandesmeldung10Type.ZusaetzlicheAngabenZAS.BisherigeWerte bisherigeWerte = zusaetzlicheAngabenZAS.getBisherigeWerte();
        if (Objects.nonNull(bisherigeWerte)) {
            // 6 | Ancien revenu annuel d�terminant moyen en francs
            ann53_03.setAncienRAM(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getDurchschnittlichesJahreseinkommen()));

            // 7 | Ancien suppl�ment d'ajournement
            ann53_03.setAncienSupplementAjourn(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getAufschubszuschlag()));

            // 8 | Ancienne r�duction
            ann53_03.setAncienRedAnticipation(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getVorbezugsreduktion()));

            // 9 | Ancien montant mensuel
            ann53_03.setAncienMontantMensuel(PRConverterUtils.formatBigDecimalToString(bisherigeWerte.getMonatsbetrag()));

            List<Short> codesCasSpeciaux = bisherigeWerte.getSonderfallcodeRente();
            if (Objects.nonNull(codesCasSpeciaux)) {
                if (codesCasSpeciaux.size() > 0) {
                    // 10 | Ancien code cas sp�cial 1
                    ann53_03.setAncienCodeCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
                }
                if (codesCasSpeciaux.size() > 1) {
                    // 11 | Ancien code cas sp�cial 2
                    ann53_03.setAncienCodeCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
                }
                if (codesCasSpeciaux.size() > 2) {
                    // 12 | Ancien code cas sp�cial 3
                    ann53_03.setAncienCodeCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
                }
                if (codesCasSpeciaux.size() > 3) {
                    // 13 | Ancien code cas sp�cial 4
                    ann53_03.setAncienCodeCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
                }
                if (codesCasSpeciaux.size() > 4) {
                    // 14 | Ancien code cas sp�cial 5
                    ann53_03.setAncienCodeCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
                }
            }

        }

        // 15 | Observations de la centrale
        ann53_03.setObservationCentrale(zusaetzlicheAngabenZAS.getBemerkungenZAS());

        // 16 | R�serve: � blanc

        ann53_03.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53_03.add(transaction);

        return ann53_03;
    }

    public REAnnonce53 createAnnonce53Extraordinaire(RRBestandesmeldung10Type bestandesmeldung10Type) throws Exception {
        RRBestandesmeldungAO10Type ausserordentlicheRente = bestandesmeldung10Type.getAusserordentlicheRente();

        REAnnonce53 ann53 = new REAnnonce53();

        // 1 | Code application: 53
        ann53.setCodeApplication("53");

        // 2 | Code enregistrement: 01
        ann53.setCodeEnregistrement01("01");

        // 3 | Num�ro de la Caisse
        ann53.setNumeroCaisse(PRConverterUtils.formatIntegerToString(ausserordentlicheRente.getKasseZweigstelle()).substring(0, 3));

        // 4 | Num�ro de l'agence
        ann53.setNumeroAgence(PRConverterUtils.formatIntegerToString(ausserordentlicheRente.getKasseZweigstelle()).substring(3, 6));

        // 5 | Num�ro de l'annonce
        ann53.setNumeroAnnonce(PRConverterUtils.formatLongToString(ausserordentlicheRente.getMeldungsnummer()));

        // 6 | R�f�rence interne de la Caisse
        ann53.setReferenceCaisseInterne(ausserordentlicheRente.getKasseneigenerHinweis());

        // 7 | Num�ro d'assur� de l'ayant droit �
        ann53.setNoAssAyantDroit(ausserordentlicheRente.getLeistungsberechtigtePerson().getVersichertennummer());

        // 8 | Premier num�ro d'assur�
        if (!ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().isEmpty()) {
            ann53.setPremierNoAssComplementaire(ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().get(0));
        }

        // 9 | Second num�ro de l'assur�
        if (!ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().isEmpty()) {
            ann53.setSecondNoAssComplementaire(ausserordentlicheRente.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().get(0));
        }

        // 10 | R�serve: � blanc

        // 11 | Etat civi
        ann53.setEtatCivil(PRConverterUtils.formatShortToString(ausserordentlicheRente.getLeistungsberechtigtePerson().getZivilstand()));

        // 12 | R�fugi�
        ann53.setIsRefugie(PRConverterUtils.formatBooleanToString(ausserordentlicheRente.getLeistungsberechtigtePerson().isIstFluechtling()));

        // 13 | Canton/Etat de domicile
        ann53.setCantonEtatDomicile(PRConverterUtils.formatIntegerToString(ausserordentlicheRente.getLeistungsberechtigtePerson().getWohnkantonStaat()));

        // 14 | Genre de prestations
        ann53.setGenrePrestation(ausserordentlicheRente.getLeistungsbeschreibung().getLeistungsart());

        // 15 | D�but du droit: MMAA
        ann53.setDebutDroit(PRConverterUtils.formatDateToMMAA(ausserordentlicheRente.getLeistungsbeschreibung().getAnspruchsbeginn()));

        // 16 | Mensualit� de la prestation en francs
        ann53.setMensualitePrestationsFrancs(PRConverterUtils.formatBigDecimalToString(ausserordentlicheRente.getLeistungsbeschreibung().getMonatsbetrag()));

        // 17 | Fin du droit: MMAA
        ann53.setFinDroit(PRConverterUtils.formatDateToMMAA(ausserordentlicheRente.getLeistungsbeschreibung().getAnspruchsende()));

        // 18 | Mois de rapport: MMAA
        ann53.setMoisRapport(PRConverterUtils.formatDateToMMAA(ausserordentlicheRente.getBerichtsmonat()));

        // 19 | Code de mutation
        ann53.setCodeMutation(PRConverterUtils.formatShortToString(ausserordentlicheRente.getLeistungsbeschreibung().getMutationscode()));

        // 20 | R�serve: � blanc

        ann53.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53.add(transaction);

        REAnnonce53 ann53_02 = new REAnnonce53();
        // 1 | Code application: 53
        ann53_02.setCodeApplication("53");

        // 2 | Code enregistrement: 02
        ann53_02.setCodeEnregistrement01("02");

        // 12 | Ann�e de niveau
        ann53_02.setAnneeNiveau(PRConverterUtils.formatDateToAA(ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getNiveaujahr()));

        IVDaten10Type ivDaten = ausserordentlicheRente.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDaten();
        if (Objects.nonNull(ivDaten)) {
            // 16 | Office AI comp�tent
            ann53_02.setOfficeAICompetent(PRConverterUtils.formatIntegerToString(ivDaten.getIVStelle()));

            // 17 | Degr� invalidit�
            ann53_02.setDegreInvalidite(PRConverterUtils.formatShortToString(ivDaten.getInvaliditaetsgrad()));

            // 18 | Code l'infirmit�
            StringBuilder codeInfirmite = new StringBuilder(ivDaten.getGebrechensschluessel()).append(ivDaten.getFunktionsausfallcode());
            ann53_02.setCodeInfirmite(codeInfirmite.toString());

            // 19 | Survenance de l'�v�nement assur�
            ann53_02.setSurvenanceEvenAssure(PRConverterUtils.formatDateToMMAA(ivDaten.getDatumVersicherungsfall()));

            // 20 | Age au d�but de l'invalidit�
            ann53_02.setAgeDebutInvalidite(PRConverterUtils.formatBooleanToString(ivDaten.isIstFruehInvalid()));
        }

        // 22 | R�duction
        ann53_02.setReduction(PRConverterUtils.formatShortToString(ausserordentlicheRente.getLeistungsbeschreibung().getKuerzungSelbstverschulden()));

        List<Short> codesCasSpeciaux = ausserordentlicheRente.getLeistungsbeschreibung().getSonderfallcodeRente();
        if (Objects.nonNull(codesCasSpeciaux)) {
            if (codesCasSpeciaux.size() > 0) {
                // 23 | Cas sp�cial code 1
                ann53_02.setCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
            }
            if (codesCasSpeciaux.size() > 1) {
                // 24 | Cas sp�cial code 2
                ann53_02.setCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
            }
            if (codesCasSpeciaux.size() > 2) {
                // 25 | Cas sp�cial code 3
                ann53_02.setCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
            }
            if (codesCasSpeciaux.size() > 3) {
                // 26 | Cas sp�cial code 4
                ann53_02.setCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
            }
            if (codesCasSpeciaux.size() > 4) {
                // 27 | Cas sp�cial code 5
                ann53_02.setCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
            }
        }

        // 34 | code survivant invalide
        ann53_02.setIsSurvivant(PRConverterUtils.formatBooleanToString(ausserordentlicheRente.getLeistungsbeschreibung().isIstInvaliderHinterlassener()));

        // 35 | R�serve: � blanc

        ann53_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53_02.add(transaction);

        // mise � jour de l'idLien de l'annonce 01
        ann53.retrieve();
        ann53.setIdLienAnnonce(ann53_02.getIdAnnonce());
        ann53.update(transaction);


        REAnnonce53 ann53_03 = createAnnonces53InfosComplementaires(bestandesmeldung10Type.getZusaetzlicheAngabenZAS());

        // mise � jour de l'idLien de l'annonce 01
        ann53_02.retrieve();
        ann53_02.setIdLienAnnonce(ann53_03.getIdAnnonce());
        ann53_02.update(transaction);

        return ann53;
    }

    public REAnnonce53 createAnnonce53Indemnisation(RRBestandesmeldung10Type bestandesmeldung10Type) throws Exception {
        RRBestandesmeldungHE10Type hilflosenentschaedigung = bestandesmeldung10Type.getHilflosenentschaedigung();

        REAnnonce53 ann53 = new REAnnonce53();

        // 1 | Code application: 53
        ann53.setCodeApplication("53");

        // 2 | Code enregistrement: 01
        ann53.setCodeEnregistrement01("01");

        // 3 | Num�ro de la Caisse
        ann53.setNumeroCaisse(PRConverterUtils.formatIntegerToString(hilflosenentschaedigung.getKasseZweigstelle()).substring(0, 3));

        // 4 | Num�ro de l'agence
        ann53.setNumeroAgence(PRConverterUtils.formatIntegerToString(hilflosenentschaedigung.getKasseZweigstelle()).substring(3, 6));

        // 5 | Num�ro de l'annonce
        ann53.setNumeroAnnonce(PRConverterUtils.formatLongToString(hilflosenentschaedigung.getMeldungsnummer()));

        // 6 | R�f�rence interne de la Caisse
        ann53.setReferenceCaisseInterne(hilflosenentschaedigung.getKasseneigenerHinweis());

        // 7 | Num�ro d'assur� de l'ayant droit �
        ann53.setNoAssAyantDroit(hilflosenentschaedigung.getLeistungsberechtigtePerson().getVersichertennummer());

        // 8 | Premier num�ro d'assur�
        if (!hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().isEmpty()) {
            ann53.setPremierNoAssComplementaire(hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr1Ergaenzend().get(0));
        }

        // 9 | Second num�ro de l'assur�
        if (!hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().isEmpty()) {
            ann53.setSecondNoAssComplementaire(hilflosenentschaedigung.getLeistungsberechtigtePerson().getFamilienAngehoerige().getVNr2Ergaenzend().get(0));
        }

        // 10 | R�serve: � blanc

        // 11 | Etat civi
        ann53.setEtatCivil(PRConverterUtils.formatShortToString(hilflosenentschaedigung.getLeistungsberechtigtePerson().getZivilstand()));

        // 12 | R�fugi�
        ann53.setIsRefugie(PRConverterUtils.formatBooleanToString(hilflosenentschaedigung.getLeistungsberechtigtePerson().isIstFluechtling()));

        // 13 | Canton/Etat de domicile
        ann53.setCantonEtatDomicile(PRConverterUtils.formatIntegerToString(hilflosenentschaedigung.getLeistungsberechtigtePerson().getWohnkantonStaat()));

        // 14 | Genre de prestations
        ann53.setGenrePrestation(hilflosenentschaedigung.getLeistungsbeschreibung().getLeistungsart());

        // 15 | D�but du droit: MMAA
        ann53.setDebutDroit(PRConverterUtils.formatDateToMMAA(hilflosenentschaedigung.getLeistungsbeschreibung().getAnspruchsbeginn()));

        // 16 | Mensualit� de la prestation en francs
        ann53.setMensualitePrestationsFrancs(PRConverterUtils.formatBigDecimalToString(hilflosenentschaedigung.getLeistungsbeschreibung().getMonatsbetrag()));

        // 17 | Fin du droit: MMAA
        ann53.setFinDroit(PRConverterUtils.formatDateToMMAA(hilflosenentschaedigung.getLeistungsbeschreibung().getAnspruchsende()));

        // 18 | Mois de rapport: MMAA
        ann53.setMoisRapport(PRConverterUtils.formatDateToMMAA(hilflosenentschaedigung.getBerichtsmonat()));

        // 19 | Code de mutation
        ann53.setCodeMutation(PRConverterUtils.formatShortToString(hilflosenentschaedigung.getLeistungsbeschreibung().getMutationscode()));

        // 20 | R�serve: � blanc

        ann53.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53.add(transaction);

        REAnnonce53 ann53_02 = new REAnnonce53();
        // 1 | Code application: 53
        ann53_02.setCodeApplication("53");

        // 2 | Code enregistrement: 02
        ann53_02.setCodeEnregistrement01("02");

        IVDatenHEType ivDaten = hilflosenentschaedigung.getLeistungsbeschreibung().getBerechnungsgrundlagen().getIVDaten();
        if (Objects.nonNull(ivDaten)) {
            // 16 | Office AI comp�tent
            ann53_02.setOfficeAICompetent(PRConverterUtils.formatIntegerToString(ivDaten.getIVStelle()));

            // 18 | Code l'infirmit�
            StringBuilder codeInfirmite = new StringBuilder(ivDaten.getGebrechensschluessel()).append(ivDaten.getFunktionsausfallcode());
            ann53_02.setCodeInfirmite(codeInfirmite.toString());

            // 19 | Survenance de l'�v�nement assur�
            ann53_02.setSurvenanceEvenAssure(PRConverterUtils.formatDateToMMAA(ivDaten.getDatumVersicherungsfall()));

            // 21 | Genre du droit � l'API --> n'existe pas dans une rente ordinaire
            ann53_02.setGenreDroitAPI(PRConverterUtils.formatShortToString(ivDaten.getArtHEAnspruch()));
        }


        List<Short> codesCasSpeciaux = hilflosenentschaedigung.getLeistungsbeschreibung().getSonderfallcodeRente();
        if (Objects.nonNull(codesCasSpeciaux)) {
            if (codesCasSpeciaux.size() > 0) {
                // 23 | Cas sp�cial code 1
                ann53_02.setCasSpecial1(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(0)));
            }
            if (codesCasSpeciaux.size() > 1) {
                // 24 | Cas sp�cial code 2
                ann53_02.setCasSpecial2(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(1)));
            }
            if (codesCasSpeciaux.size() > 2) {
                // 25 | Cas sp�cial code 3
                ann53_02.setCasSpecial3(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(2)));
            }
            if (codesCasSpeciaux.size() > 3) {
                // 26 | Cas sp�cial code 4
                ann53_02.setCasSpecial4(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(3)));
            }
            if (codesCasSpeciaux.size() > 4) {
                // 27 | Cas sp�cial code 5
                ann53_02.setCasSpecial5(PRConverterUtils.formatCodeCasSpecial(codesCasSpeciaux.get(4)));
            }
        }

        // 35 | R�serve: � blanc

        ann53_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        ann53_02.add(transaction);

        // mise � jour de l'idLien de l'annonce 01
        ann53.retrieve();
        ann53.setIdLienAnnonce(ann53_02.getIdAnnonce());
        ann53.update(transaction);


        REAnnonce53 ann53_03 = createAnnonces53InfosComplementaires(bestandesmeldung10Type.getZusaetzlicheAngabenZAS());

        // mise � jour de l'idLien de l'annonce 01
        ann53_02.retrieve();
        ann53_02.setIdLienAnnonce(ann53_03.getIdAnnonce());
        ann53_02.update(transaction);

        return ann53;
    }


}
