package globaz.corvus.anakin;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import ch.admin.ofit.anakin.donnee.Annonce10eme;
import ch.admin.ofit.anakin.donnee.Annonce9eme;
import ch.admin.ofit.anakin.donnee.AnnonceAbstraite;

public class REArcConverter {

    public final static String formatNSS(String nss) {

        return NSUtil.formatAVSUnknown(nss);

        /*
         * if (JadeStringUtil.isBlank(nss) == true) {
         * 
         * return "000.0000.00000.00"; } StringBuffer buffer = new StringBuffer(); for (int i = 0; i < nss.length();
         * i++) { char c = nss.charAt(i); if (Character.isDigit(c)) { buffer.append(c); } } if (buffer.length() > 13) {
         * buffer.setLength(13); } while (buffer.length() < 13) { buffer.append("0"); } StringBuffer avsBuffer = new
         * StringBuffer(); avsBuffer.append(buffer.substring(0, 3)); avsBuffer.append(".");
         * avsBuffer.append(buffer.substring(3, 7)); avsBuffer.append("."); avsBuffer.append(buffer.substring(7, 11));
         * avsBuffer.append("."); avsBuffer.append(buffer.substring(11)); return avsBuffer.toString();
         */
    }

    /**
     * Convertisseur d'une annonce rente (CORVUS) en une annonce abstraite (ANAKIN) de la centrale.
     * 
     * @param arc01
     *            , arc02 Annonces code enregistrement 01 et 02. arc02 peut être null.
     * @param moisRapport Le mois de rapport au format MMAA
     * @return
     * @throws Exception
     */
    public AnnonceAbstraite convertToAnakinArc(BSession session, REAnnonceHeader arc01, REAnnonceHeader arc02,
            String moisRapport) throws Exception {

        AnnonceAbstraite result = null;
        if (arc01 instanceof REAnnoncesDiminution9Eme) {
            result = getAnnonceDiminution09(session, (REAnnoncesDiminution9Eme) arc01, moisRapport);
        }

        else if (arc01 instanceof REAnnoncesAugmentationModification9Eme) {
            result = getAnnonceAugmentation09(session, (REAnnoncesAugmentationModification9Eme) arc01,
                    (REAnnoncesAugmentationModification9Eme) arc02, moisRapport);
        } else if (arc01 instanceof REAnnoncesAugmentationModification10Eme) {
            result = getAnnonceAugmentation10(session, (REAnnoncesAugmentationModification10Eme) arc01,
                    (REAnnoncesAugmentationModification10Eme) arc02, moisRapport);
        }

        else if (arc01 instanceof REAnnoncesDiminution10Eme) {
            result = getAnnonceDiminution10(session, (REAnnoncesDiminution10Eme) arc01, moisRapport);
        } else {
            throw new Exception("Converter not yet implemented for this type of ARC.");
        }

        return result;

    }

    private Annonce9eme getAnnonceAugmentation09(BSession session, REAnnoncesAugmentationModification9Eme arc01,
            REAnnoncesAugmentationModification9Eme arc02, String moisRapport) {

        Annonce9eme result = new Annonce9eme();

        // Elements en suspensa
        result.setDateMutation("");
        // Enregistrement 01
        result.setNumeroCaisse(arc01.getNumeroCaisse() + arc01.getNumeroAgence());
        result.setNumeroAnnonce(arc01.getNumeroAnnonce());
        result.setReferenceInterne(arc01.getReferenceCaisseInterne());

        // Si la NNSS
        // Remplir alors la date de naissance et le code sexe
        result.setNumeroAVS(arc01.getNoAssAyantDroit());

        // NSS
        if ((arc01.getNoAssAyantDroit() != null) && (arc01.getNoAssAyantDroit().length() == 13)) {
            try {
                String nss = arc01.getNoAssAyantDroit();
                nss = REArcConverter.formatNSS(nss);
                PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                // HOMME = 516001
                // FEMME = 516002
                if ("516002".equals(sexe)) {
                    result.setSexeNumeroAVS("2");
                } else {
                    result.setSexeNumeroAVS("1");
                }

                // jjmmaaaa
                result.setNaissanceNumeroAVS(dateNaissance);
            } catch (Exception e) {
                ;
            }

        }

        if (!JadeStringUtil.isBlankOrZero(arc01.getNouveauNoAssureAyantDroit())) {
            result.setNumeroAVSCorrige(arc01.getNouveauNoAssureAyantDroit());
        }

        if (arc01.getPremierNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire1(arc01.getPremierNoAssComplementaire());
            if (arc01.getPremierNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getPremierNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire1("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire1("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire1(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        if (arc01.getSecondNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire2(arc01.getSecondNoAssComplementaire());
            if (arc01.getSecondNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getSecondNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire2("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire2("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire2(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        result.setEtatCivil(arc01.getEtatCivil());
        result.setCodeRefugie(arc01.getIsRefugie());
        result.setDomicile(arc01.getCantonEtatDomicile());
        result.setGenrePrestation(arc01.getGenrePrestation());
        result.setDebutDroit(arc01.getDebutDroit());
        result.setMontantPrestation(arc01.getMensualitePrestationsFrancs());
        result.setMontantRenteOrdinaireRemplacee(arc01.getMensualiteRenteOrdRemp());
        result.setFinDroit(arc01.getFinDroit());

        if (JadeStringUtil.isBlankOrZero(moisRapport)) {
            result.setDateEtatRegistre(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));
        } else {
            result.setDateEtatRegistre(moisRapport);
        }

        if (!JadeStringUtil.isBlankOrZero(arc01.getCodeMutation())) {
            result.setRaisonDiminution(arc01.getCodeMutation());
            if (!"78".equals(arc01.getCodeMutation())) {
                result.setDateAnnonceDiminution(moisRapport);
            }
        }

        // type de mutation
        // augmentation: 31 (10ème) 30 (9ème)
        // diminution : 21 (10ème) 20 (9ème)
        // correction : 11 (10ème) 10 (9ème)

        if ("41".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("30");
        } else if ("42".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("20");
        } else if ("43".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("10");
        }

        // Enregistrement 02

        if (arc02 != null) {

            result.setRevenuAnnuelMoyen(arc02.getRamDeterminant());

            result.setDureeCotisationRevenuAnnuelMoyen(arc02.getDureeCotPourDetRAM());

            result.setAnneeNiveau(arc02.getAnneeNiveau());

            result.setCodeRevenu(arc02.getRevenuPrisEnCompte());

            result.setEchelleRente(arc02.getEchelleRente());

            result.setPeriodeCotisationAvant1973(arc02.getDureeCoEchelleRenteAv73());

            result.setPeriodeCotisationDes1973(arc02.getDureeCoEchelleRenteDes73());

            if (!JadeStringUtil.isBlankOrZero(arc02.getDureeCotManquante73_78())) {

            }

            result.setAnneesAppointAvant1973(arc02.getDureeCotManquante48_72());
            result.setAnneesAppointDes1973(arc02.getDureeCotManquante73_78());

            result.setClasseAge(arc02.getAnneeCotClasseAge());

            if (!JadeStringUtil.isBlankOrZero(arc02.getDureeAjournement())) {
                result.setDureeAjournement(arc02.getDureeAjournement());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getSupplementAjournement())) {
                result.setSupplementAjournement(arc02.getSupplementAjournement());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getDateRevocationAjournement())) {
                result.setDateRevocation(arc02.getDateRevocationAjournement());
            }

            result.setLimiteRevenu(arc02.getIsLimiteRevenu());

            result.setMinimumGaranti(arc02.getIsMinimumGaranti());

            result.setComissionAI(arc02.getOfficeAICompetent());

            result.setComissionAIEpouse(arc02.getOfficeAiCompEpouse());

            result.setDegreInvalidite(arc02.getDegreInvalidite());

            result.setDegreInvaliditeEpouse(arc02.getDegreInvaliditeEpouse());

            result.setClefInfirmite(arc02.getCodeInfirmite());

            result.setClefInfirmiteEpouse(arc02.getCodeInfirmiteEpouse());

            result.setSurvenanceEvenementAssure(arc02.getSurvenanceEvenAssure());

            if (!JadeStringUtil.isBlankOrZero(arc02.getSurvenanceEvtAssureEpouse())) {
                result.setSurvenanceEvenementEpouse(arc02.getSurvenanceEvtAssureEpouse());
            }

            result.setInvaliditePrecoce(arc02.getAgeDebutInvalidite());
            // result.setInvaliditePrecoceEpouse(arc02.getAgeDebutInvaliditeEpouse());

            if (!JadeStringUtil.isBlankOrZero(arc02.getGenreDroitAPI())) {
                result.setGenreDroitAPI(arc02.getGenreDroitAPI());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getReduction())) {
                result.setReduction(arc02.getReduction());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial1())) {
                result.setCasSpecial(arc02.getCasSpecial1(), 1);
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial2())) {
                result.setCasSpecial(arc02.getCasSpecial2(), 2);
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial3())) {
                result.setCasSpecial(arc02.getCasSpecial3(), 3);
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial4())) {
                result.setCasSpecial(arc02.getCasSpecial4(), 4);
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial5())) {
                result.setCasSpecial(arc02.getCasSpecial5(), 5);
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getRevenuAnnuelMoyenSansBTE())) {
                result.setRevenuSansBonification(arc02.getRevenuAnnuelMoyenSansBTE());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getBteMoyennePrisEnCompte())) {
                result.setMontantBonification(arc02.getBteMoyennePrisEnCompte());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getNombreAnneeBTE())) {
                result.setAnneesEducation(arc02.getNombreAnneeBTE());
            }
        }

        return result;

    }

    private Annonce10eme getAnnonceAugmentation10(BSession session, REAnnoncesAugmentationModification10Eme arc01,
            REAnnoncesAugmentationModification10Eme arc02, String moisRapport) {

        Annonce10eme result = new Annonce10eme();

        // Elements en suspensa
        result.setDateMutation("");

        // Enregistrement 01
        result.setNumeroCaisse(arc01.getNumeroCaisse() + arc01.getNumeroAgence());

        result.setNumeroAnnonce(arc01.getNumeroAnnonce());
        result.setReferenceInterne(arc01.getReferenceCaisseInterne());

        // Si la NNSS
        // Remplir alors la date de naissance et le code sexe
        result.setNumeroAVS(arc01.getNoAssAyantDroit());

        // NSS
        if ((arc01.getNoAssAyantDroit() != null) && (arc01.getNoAssAyantDroit().length() == 13)) {
            try {
                String nss = arc01.getNoAssAyantDroit();
                nss = REArcConverter.formatNSS(nss);
                PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                // HOMME = 516001
                // FEMME = 516002
                if ("516002".equals(sexe)) {
                    result.setSexeNumeroAVS("2");
                } else {
                    result.setSexeNumeroAVS("1");
                }

                // jjmmaaaa
                result.setNaissanceNumeroAVS(dateNaissance);
            } catch (Exception e) {
                ;
            }

        }

        if (!JadeStringUtil.isBlankOrZero(arc01.getNouveauNoAssureAyantDroit())) {
            result.setNumeroAVSCorrige(arc01.getNouveauNoAssureAyantDroit());
        }

        if (arc01.getPremierNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire1(arc01.getPremierNoAssComplementaire());
            if (arc01.getPremierNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getPremierNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire1("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire1("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire1(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        if (arc01.getSecondNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire2(arc01.getSecondNoAssComplementaire());
            if (arc01.getSecondNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getSecondNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire2("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire2("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire2(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }
        result.setEtatCivil(arc01.getEtatCivil());
        result.setCodeRefugie(arc01.getIsRefugie());
        result.setDomicile(arc01.getCantonEtatDomicile());
        result.setGenrePrestation(arc01.getGenrePrestation());
        result.setDebutDroit(arc01.getDebutDroit());
        result.setMontantPrestation(arc01.getMensualitePrestationsFrancs());

        if (!JadeStringUtil.isBlankOrZero(arc01.getFinDroit())) {
            result.setFinDroit(arc01.getFinDroit());
        }

        if (JadeStringUtil.isBlankOrZero(moisRapport)) {
            result.setDateEtatRegistre(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));
        } else {
            result.setDateEtatRegistre(moisRapport);
        }

        if (!JadeStringUtil.isBlankOrZero(arc01.getCodeMutation())) {
            result.setRaisonDiminution(arc01.getCodeMutation());

            if (!"78".equals(arc01.getCodeMutation())) {
                result.setDateAnnonceDiminution(moisRapport);
            }
        }

        // type de mutation
        // augmentation: 31 (10ème) 30 (9ème)
        // diminution : 21 (10ème) 20 (9ème)
        // correction : 11 (10ème) 10 (9ème)
        if ("44".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("31");
        } else if ("45".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("21");
        } else if ("46".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("11");
        }

        // Enregistrement 02

        if (arc02 != null) {

            result.setEchelleRente(arc02.getEchelleRente());

            result.setPeriodeCotisationAvant1973(arc02.getDureeCoEchelleRenteAv73());
            result.setPeriodeCotisationDes1973(arc02.getDureeCoEchelleRenteDes73());
            result.setAnneesAppointAvant1973(arc02.getDureeCotManquante48_72());
            result.setAnneesAppointDes1973(arc02.getDureeCotManquante73_78());

            result.setDureeCotisationRevenuAnnuelMoyen(arc02.getDureeCotPourDetRAM());
            result.setRevenuAnnuelMoyen(arc02.getRamDeterminant());
            result.setClasseAge(arc02.getAnneeCotClasseAge());
            result.setCodeSplitting(arc02.getCodeRevenuSplitte());

            if (!JadeStringUtil.isBlank(arc02.getAnneeNiveau())) {
                result.setAnneeNiveau(arc02.getAnneeNiveau());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getNombreAnneeBTE())) {
                result.setBonificationEducation(arc02.getNombreAnneeBTE());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getNbreAnneeBTA())) {
                result.setBonificationAssistance(arc02.getNbreAnneeBTA());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getNbreAnneeBonifTrans())) {
                result.setBonificationTransitoire(arc02.getNbreAnneeBonifTrans());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getOfficeAICompetent())) {
                result.setOfficeAI(arc02.getOfficeAICompetent());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getDegreInvalidite())) {
                result.setDegreInvalidite(arc02.getDegreInvalidite());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCodeInfirmite())) {
                result.setClefInfirmite(arc02.getCodeInfirmite());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getSurvenanceEvenAssure())) {
                result.setSurvenanceEvenementAssure(arc02.getSurvenanceEvenAssure());
            }
            result.setInvaliditePrecoce(arc02.getAgeDebutInvalidite());

            if (!JadeStringUtil.isBlankOrZero(arc02.getGenreDroitAPI())) {
                result.setGenreDroitAPI(arc02.getGenreDroitAPI());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getReduction())) {
                result.setReduction(arc02.getReduction());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial1())) {
                result.setCasSpecial(arc02.getCasSpecial1(), 1);
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial2())) {
                result.setCasSpecial(arc02.getCasSpecial2(), 2);
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial3())) {
                result.setCasSpecial(arc02.getCasSpecial3(), 3);
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial4())) {
                result.setCasSpecial(arc02.getCasSpecial4(), 4);
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getCasSpecial5())) {
                result.setCasSpecial(arc02.getCasSpecial5(), 5);
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getNbreAnneeAnticipation())) {
                result.setDureeAnticipation(arc02.getNbreAnneeAnticipation());
            }

            if (!JadeStringUtil.isBlankOrZero(arc02.getReductionAnticipation())) {
                result.setReductionAnticipation(arc02.getReductionAnticipation());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getDateDebutAnticipation())) {
                result.setDateAnticipation(arc02.getDateDebutAnticipation());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getDureeAjournement())) {
                result.setDureeAjournement(arc02.getDureeAjournement());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getSupplementAjournement())) {
                result.setSupplementAjournement(arc02.getSupplementAjournement());
            }
            if (!JadeStringUtil.isBlankOrZero(arc02.getDateRevocationAjournement())) {
                result.setDateRevocation(arc02.getDateRevocationAjournement());
            }

            if (!JadeStringUtil.isBlank(arc02.getIsSurvivant())) {
                result.setCodeSurvivantInvalide(arc02.getIsSurvivant());
            }
        }
        return result;
    }

    private Annonce9eme getAnnonceDiminution09(BSession session, REAnnoncesDiminution9Eme arc01, String moisRapport) {

        Annonce9eme result = new Annonce9eme();

        // Elements en suspensa
        result.setDateMutation("");
        // Enregistrement 01
        result.setNumeroCaisse(arc01.getNumeroCaisse() + arc01.getNumeroAgence());
        result.setNumeroAnnonce(arc01.getNumeroAnnonce());
        result.setReferenceInterne(arc01.getReferenceCaisseInterne());

        // Si la NNSS
        // Remplir alors la date de naissance et le code sexe
        result.setNumeroAVS(arc01.getNoAssAyantDroit());

        // NSS
        if ((arc01.getNoAssAyantDroit() != null) && (arc01.getNoAssAyantDroit().length() == 13)) {
            try {
                String nss = arc01.getNoAssAyantDroit();
                nss = REArcConverter.formatNSS(nss);
                PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                // HOMME = 516001
                // FEMME = 516002
                if ("516002".equals(sexe)) {
                    result.setSexeNumeroAVS("2");
                } else {
                    result.setSexeNumeroAVS("1");
                }

                // jjmmaaaa
                result.setNaissanceNumeroAVS(dateNaissance);
            } catch (Exception e) {
                ;
            }

        }

        if (arc01.getPremierNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire1(arc01.getPremierNoAssComplementaire());
            if (arc01.getPremierNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getPremierNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire1("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire1("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire1(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        if (arc01.getSecondNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire2(arc01.getSecondNoAssComplementaire());
            if (arc01.getSecondNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getSecondNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire2("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire2("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire2(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        result.setEtatCivil(arc01.getEtatCivil());
        result.setCodeRefugie(arc01.getIsRefugie());
        result.setDomicile(arc01.getCantonEtatDomicile());
        result.setGenrePrestation(arc01.getGenrePrestation());
        result.setDebutDroit(arc01.getDebutDroit());
        result.setMontantPrestation(arc01.getMensualitePrestationsFrancs());
        result.setFinDroit(arc01.getFinDroit());
        if (JadeStringUtil.isBlankOrZero(moisRapport)) {
            result.setDateEtatRegistre(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));
        } else {
            result.setDateEtatRegistre(moisRapport);
        }

        if (!JadeStringUtil.isBlankOrZero(arc01.getCodeMutation())) {
            result.setRaisonDiminution(arc01.getCodeMutation());
            if (!"78".equals(arc01.getCodeMutation())) {
                result.setDateAnnonceDiminution(moisRapport);
            }
        }

        // type de mutation
        // augmentation: 31 (10ème) 30 (9ème)
        // diminution : 21 (10ème) 20 (9ème)
        // correction : 11 (10ème) 10 (9ème)

        if ("41".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("30");
        } else if ("42".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("20");
        } else if ("43".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("10");
        }

        return result;

    }

    private Annonce10eme getAnnonceDiminution10(BSession session, REAnnoncesDiminution10Eme arc01, String moisRapport) {
        // Elements en suspensa
        Annonce10eme result = new Annonce10eme();

        result.setDateMutation("");

        // Enregistrement 01
        result.setNumeroCaisse(arc01.getNumeroCaisse() + arc01.getNumeroAgence());
        result.setNumeroAnnonce(arc01.getNumeroAnnonce());
        result.setReferenceInterne(arc01.getReferenceCaisseInterne());

        // Si la NNSS
        // Remplir alors la date de naissance et le code sexe
        result.setNumeroAVS(arc01.getNoAssAyantDroit());

        // NSS
        if ((arc01.getNoAssAyantDroit() != null) && (arc01.getNoAssAyantDroit().length() == 13)) {
            try {
                String nss = arc01.getNoAssAyantDroit();
                nss = REArcConverter.formatNSS(nss);
                PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
                dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");

                String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                // HOMME = 516001
                // FEMME = 516002
                if ("516002".equals(sexe)) {
                    result.setSexeNumeroAVS("2");
                } else {
                    result.setSexeNumeroAVS("1");
                }

                // jjmmaaaa
                result.setNaissanceNumeroAVS(dateNaissance);
            } catch (Exception e) {
                ;
            }

        }

        if (arc01.getPremierNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire1(arc01.getPremierNoAssComplementaire());
            if (arc01.getPremierNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getPremierNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire1("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire1("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire1(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        if (arc01.getSecondNoAssComplementaire() != null) {

            // NSS complementaire 1
            result.setNumeroAVSComplementaire2(arc01.getSecondNoAssComplementaire());
            if (arc01.getSecondNoAssComplementaire().length() == 13) {

                try {
                    String nss = arc01.getSecondNoAssComplementaire();
                    nss = REArcConverter.formatNSS(nss);
                    PRTiersWrapper tw = PRTiersHelper.getTiers(session, nss);
                    String dateNaissance = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

                    dateNaissance = JadeStringUtil.change(dateNaissance, ".", "");
                    String sexe = tw.getProperty(PRTiersWrapper.PROPERTY_SEXE);

                    // HOMME = 516001
                    // FEMME = 516002
                    if ("516002".equals(sexe)) {
                        result.setSexeNumeroAVSComplementaire2("2");
                    } else {
                        result.setSexeNumeroAVSComplementaire2("1");
                    }
                    // jjmmaaaa
                    result.setNaissanceNumeroAVSComplementaire2(dateNaissance);
                } catch (Exception e) {
                    ;
                }
            }
        }

        result.setEtatCivil(arc01.getEtatCivil());
        result.setCodeRefugie(arc01.getIsRefugie());
        result.setDomicile(arc01.getCantonEtatDomicile());
        result.setGenrePrestation(arc01.getGenrePrestation());
        result.setDebutDroit(arc01.getDebutDroit());
        result.setMontantPrestation(arc01.getMensualitePrestationsFrancs());

        if (!JadeStringUtil.isBlankOrZero(arc01.getFinDroit())) {
            result.setFinDroit(arc01.getFinDroit());
        }
        if (JadeStringUtil.isBlankOrZero(moisRapport)) {
            result.setDateEtatRegistre(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));
        } else {
            result.setDateEtatRegistre(moisRapport);
        }

        if (!JadeStringUtil.isBlankOrZero(arc01.getCodeMutation())) {
            result.setRaisonDiminution(arc01.getCodeMutation());

            if (!"78".equals(arc01.getCodeMutation())) {
                result.setDateAnnonceDiminution(moisRapport);
            }
        }

        // type de mutation
        // augmentation: 31 (10ème) 30 (9ème)
        // diminution : 21 (10ème) 20 (9ème)
        // correction : 11 (10ème) 10 (9ème)
        if ("44".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("31");
        } else if ("45".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("21");
        } else if ("46".equals(arc01.getCodeApplication())) {
            result.setCodeMutation("11");
        }

        return result;

    }

}
