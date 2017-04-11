package globaz.corvus.annonce.service;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import ch.admin.zas.rc.AbgangsmeldungType;
import ch.admin.zas.rc.AenderungsmeldungO9Type;
import ch.admin.zas.rc.DJE9BeschreibungType;
import ch.admin.zas.rc.DJE9BeschreibungWeakType;
import ch.admin.zas.rc.FamilienAngehoerigeType;
import ch.admin.zas.rc.Gutschriften9Type;
import ch.admin.zas.rc.Gutschriften9WeakType;
import ch.admin.zas.rc.IVDaten9Type;
import ch.admin.zas.rc.IVDaten9WeakType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslWeakType;
import ch.admin.zas.rc.RRMeldung9Type;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.ZuwachsmeldungO9Type;

/**
 * @author jmc
 * 
 */
public class REAnnonces9eXmlService extends REAbstractAnnonceXmlService implements REAnnonceXmlService {

    public static Set<Integer> ordentlicheRente = new HashSet<Integer>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 33,
            34, 35, 36, 50, 51, 52, 53, 54, 55, 56));
    public static Set<Integer> ausserordentlicheRente = new HashSet<Integer>(Arrays.asList(20, 21, 22, 23, 24, 25, 26,
            43, 44, 45, 46, 70, 71, 72, 73, 74, 75, 76));
    public static Set<Integer> hilflosenentschaedigung = new HashSet<Integer>(Arrays.asList(91, 92, 93, 95, 96, 97));

    private static REAnnonceXmlService instance = new REAnnonces9eXmlService();

    public static REAnnonceXmlService getInstance() {

        return instance;
    }

    @Override
    public Object getAnnonceXml(REAnnoncesAbstractLevel1A annonce, String forMoisAnneeComptable, BSession session,
            BITransaction transaction) throws Exception {

        int codeApplication = Integer.parseInt(annonce.getCodeApplication());
        switch (codeApplication) {
            case 41:
            case 43:
                REAnnoncesAugmentationModification9Eme augmentation9eme01 = new REAnnoncesAugmentationModification9Eme();
                augmentation9eme01.setSession(session);
                augmentation9eme01.setIdAnnonce(annonce.getIdAnnonce());
                augmentation9eme01.retrieve();

                REAnnoncesAugmentationModification9Eme augmentation9eme02 = new REAnnoncesAugmentationModification9Eme();
                augmentation9eme02.setSession(session);
                augmentation9eme02.setIdAnnonce(augmentation9eme01.getIdLienAnnonce());
                augmentation9eme02.retrieve();

                if (!augmentation9eme01.isNew()) {
                    augmentation9eme01.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation9eme01.update(transaction);
                }
                if (!augmentation9eme02.isNew()) {
                    augmentation9eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation9eme02.update(transaction);
                }
                // parseAugmentationAvecAnakin(augmentation9eme01, augmentation9eme02, session, forMoisAnneeComptable);

                if (ordentlicheRente.contains(new Integer(augmentation9eme01.getGenrePrestation()))) {
                    if (codeApplication == 41) {
                        return annonceAugmentationOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    } else {
                        return annonceChangementOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    }
                } else if (ausserordentlicheRente.contains(new Integer(augmentation9eme01.getGenrePrestation()))) {
                    if (codeApplication == 41) {
                        return annonceAugmentationExtraOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    } else {
                        return annonceChangementExtraOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    }
                } else if (hilflosenentschaedigung.contains(new Integer(augmentation9eme01.getGenrePrestation()))) {
                    if (codeApplication == 41) {
                        return annonceAugmentationOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    } else {
                        return annonceChangementExtraOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    }

                } else {
                    throw new Exception("no match into the expected genre de prestation");
                }

            case 42:
                REAnnoncesDiminution9Eme diminution9eme01 = new REAnnoncesDiminution9Eme();
                diminution9eme01.setSession(session);
                diminution9eme01.setIdAnnonce(annonce.getIdAnnonce());
                diminution9eme01.retrieve();
                if (!diminution9eme01.isNew()) {
                    diminution9eme01.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    diminution9eme01.update(transaction);
                }

                if (ordentlicheRente.contains(new Integer(diminution9eme01.getGenrePrestation()))) {

                    return annonceDiminutionOrdinaire(diminution9eme01);

                }
                if (ausserordentlicheRente.contains(new Integer(diminution9eme01.getGenrePrestation()))) {

                    return annonceDiminutionExtraOrdinaire(diminution9eme01);

                }
                if (hilflosenentschaedigung.contains(new Integer(diminution9eme01.getGenrePrestation()))) {

                    return annonceDiminutionAPI(diminution9eme01);

                }
                // parseDiminutionAvecAnakin(diminution9eme01, session, forMoisAnneeComptable);

                break;
        }
        throw new Exception("no match into the expected variable paussibilities");
    }

    protected AbgangsmeldungType createDiminutionCommune(REAnnoncesDiminution9Eme enr01) throws Exception {
        AbgangsmeldungType diminution = factoryType.createAbgangsmeldungType();
        diminution.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        diminution.setKasseZweigstelle(retourneCaisseAgence());
        diminution.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        diminution.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        AbgangsmeldungType.LeistungsberechtigtePerson person = factoryType
                .createAbgangsmeldungTypeLeistungsberechtigtePerson();
        person.setVersichertennummer(enr01.getNoAssAyantDroit());
        diminution.setLeistungsberechtigtePerson(person);
        AbgangsmeldungType.Leistungsbeschreibung description = factoryType
                .createAbgangsmeldungTypeLeistungsbeschreibung();
        description.getLeistungsart().add(enr01.getGenrePrestation());
        if (!JadeStringUtil.isBlankOrZero(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getCodeMutation())) {
            description.setMutationscode(new Integer(enr01.getCodeMutation()).shortValue());
        }
        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        diminution.setLeistungsbeschreibung(description);
        return diminution;
    }

    protected RRMeldung9Type annonceAugmentationOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type augmentation = factoryType.createZuwachsmeldungO9Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlankOrZero(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getCodeMutation())) {
            description.setMutationscode(new Integer(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = rempliScalaBerechnungTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9Type bte = rempliBonnifications9e(enr01, enr02);

        baseDeCalcul.setGutschriften(bte);
        DJE9BeschreibungType ram = rempliDJE9BeschreibungType(enr01, enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9Assure(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9Conjoint(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubType(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceChangementOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        AenderungsmeldungO9Type augmentation = factoryType.createAenderungsmeldungO9Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAusWeaklType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        if (!JadeStringUtil.isBlankOrZero(enr01.getDebutDroit())) {
            description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getCodeMutation())) {
            description.setMutationscode(new Integer(enr01.getCodeMutation()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getMensualitePrestationsFrancs())) {
            description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        }
        // Base de calcul
        AenderungsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlankOrZero(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        // Echelle de la base de calcul
        ch.admin.zas.rc.SkalaBerechnungWeakType echelleCalcul = rempliScalaBerechnungWeakTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9WeakType bte = rempliBonnificationsWeak9e(enr01, enr02);

        baseDeCalcul.setGutschriften(bte);
        DJE9BeschreibungWeakType ram = rempliDJE9BeschreibungWeakType(enr01, enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9AssureWeak(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9ConjointWeak(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            AenderungsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createAenderungsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubTypeWeak(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setAenderungsmeldung(augmentation);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceChangementExtraOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type augmentation = factoryType.createZuwachsmeldungO9Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlankOrZero(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getCodeMutation())) {
            description.setMutationscode(new Integer(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = rempliScalaBerechnungTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9Type bte = rempliBonnifications9e(enr01, enr02);

        baseDeCalcul.setGutschriften(bte);
        DJE9BeschreibungType ram = rempliDJE9BeschreibungType(enr01, enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9Assure(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9Conjoint(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubType(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceAugmentationExtraOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type augmentation = factoryType.createZuwachsmeldungO9Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlankOrZero(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getCodeMutation())) {
            description.setMutationscode(new Integer(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = rempliScalaBerechnungTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9Type bte = rempliBonnifications9e(enr01, enr02);

        baseDeCalcul.setGutschriften(bte);

        DJE9BeschreibungType ram = rempliDJE9BeschreibungType(enr01, enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9Assure(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9Conjoint(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubType(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceDiminutionOrdinaire(REAnnoncesDiminution9Eme enr01) throws Exception {

        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        RRMeldung9Type meldung9Type = factoryType.createRRMeldung9Type();
        try {
            AbgangsmeldungType diminution = createDiminutionCommune(enr01);

            renteOrdinaire.setAbgangsmeldung(diminution);
            meldung9Type.setOrdentlicheRente(renteOrdinaire);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meldung9Type;

    }

    protected RRMeldung9Type annonceDiminutionExtraOrdinaire(REAnnoncesDiminution9Eme enr01) throws Exception {

        RRMeldung9Type.AusserordentlicheRente renteExtraOrdinaire = factoryType
                .createRRMeldung9TypeAusserordentlicheRente();
        RRMeldung9Type meldung9Type = factoryType.createRRMeldung9Type();
        try {
            AbgangsmeldungType diminution = createDiminutionCommune(enr01);

            renteExtraOrdinaire.setAbgangsmeldung(diminution);
            meldung9Type.setAusserordentlicheRente(renteExtraOrdinaire);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meldung9Type;

    }

    protected RRMeldung9Type annonceDiminutionAPI(REAnnoncesDiminution9Eme enr01) throws Exception {

        RRMeldung9Type.Hilflosenentschaedigung api = factoryType.createRRMeldung9TypeHilflosenentschaedigung();
        RRMeldung9Type meldung9Type = factoryType.createRRMeldung9Type();
        try {
            AbgangsmeldungType diminution = createDiminutionCommune(enr01);

            api.setAbgangsmeldung(diminution);
            meldung9Type.setHilflosenentschaedigung(api);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meldung9Type;

    }

    protected RRLeistungsberechtigtePersonAuslWeakType rempliRRLeistungsberechtigtePersonAusWeaklType(
            REAnnoncesAugmentationModification9Eme enr01, REAnnoncesAugmentationModification9Eme enr02) {
        RRLeistungsberechtigtePersonAuslWeakType personne = factoryType
                .createRRLeistungsberechtigtePersonAuslWeakType();
        personne.setVersichertennummer(enr01.getNoAssAyantDroit());
        if (!JadeStringUtil.isBlankOrZero(enr01.getNouveauNoAssureAyantDroit())) {
            personne.setGeaenderteVersichertennummer(enr01.getNouveauNoAssureAyantDroit());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getIsRefugie())) {
            personne.setIstFluechtling(convertIntToBoolean(enr01.getIsRefugie()));
        }
        FamilienAngehoerigeType membresDeLaFamille = factoryType.createFamilienAngehoerigeType();
        if (!JadeStringUtil.isBlankOrZero(enr01.getPremierNoAssComplementaire())) {
            membresDeLaFamille.getVNr1Ergaenzend().add(enr01.getPremierNoAssComplementaire());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getSecondNoAssComplementaire())) {
            membresDeLaFamille.getVNr2Ergaenzend().add(enr01.getSecondNoAssComplementaire());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getCantonEtatDomicile())) {
            personne.setWohnkantonStaat(new Integer(enr01.getCantonEtatDomicile()).toString());
        }
        personne.setFamilienAngehoerige(membresDeLaFamille);
        personne.setZivilstand(new Integer(enr01.getEtatCivil()).shortValue());
        return personne;

    }

    private DJE9BeschreibungType rempliDJE9BeschreibungType(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) {
        DJE9BeschreibungType ramDescription = factoryType.createDJE9BeschreibungType();
        ramDescription.setAngerechneteEinkommen(new Integer(testSiNullouZero(enr02.getRevenuPrisEnCompte()))
                .shortValue());
        ramDescription
                .setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02.getRamDeterminant())));
        ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal((enr02
                .getDureeCotPourDetRAM())));

        return ramDescription;
    }

    private DJE9BeschreibungWeakType rempliDJE9BeschreibungWeakType(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) {
        DJE9BeschreibungWeakType ramDescription = factoryType.createDJE9BeschreibungWeakType();
        if (!JadeStringUtil.isBlankOrZero(enr02.getRevenuPrisEnCompte())) {
            ramDescription.setAngerechneteEinkommen(new Integer(testSiNullouZero(enr02.getRevenuPrisEnCompte()))
                    .shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getRamDeterminant())) {
            ramDescription.setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02
                    .getRamDeterminant())));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeCotPourDetRAM())) {
            ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal((enr02
                    .getDureeCotPourDetRAM())));
        }

        return ramDescription;
    }

    private Gutschriften9Type rempliBonnifications9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) {
        Gutschriften9Type bte = factoryType.createGutschriften9Type();
        bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getRevenuAnnuelMoyenSansBTE())));
        bte.setAngerechneteErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getBteMoyennePrisEnCompte())));
        bte.setAnzahlErziehungsgutschrift(new Integer(testSiNullouZero(enr02.getNombreAnneeBTE())).shortValue());
        return bte;
    }

    private Gutschriften9WeakType rempliBonnificationsWeak9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) {
        Gutschriften9WeakType bte = factoryType.createGutschriften9WeakType();
        if (!JadeStringUtil.isBlankOrZero(enr02.getRevenuAnnuelMoyenSansBTE())) {
            bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getRevenuAnnuelMoyenSansBTE())));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getBteMoyennePrisEnCompte())) {
            bte.setAngerechneteErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getBteMoyennePrisEnCompte())));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getNombreAnneeBTE())) {
            bte.setAnzahlErziehungsgutschrift(new Integer(testSiNullouZero(enr02.getNombreAnneeBTE())).shortValue());
        }
        return bte;
    }

    /**
     * Rempli les données utiles pour les données AI 9e révision
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    private IVDaten9Type rempliIVDatenType9Assure(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDaten9Type donneeAI = factoryType.createIVDaten9Type();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAICompetent()));
        donneeAI.setInvaliditaetsgrad(new Integer(enr02.getDegreInvalidite()).shortValue());
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));

        return donneeAI;
    }

    private IVDaten9WeakType rempliIVDatenType9AssureWeak(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDaten9WeakType donneeAI = factoryType.createIVDaten9WeakType();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAICompetent()));
        donneeAI.setInvaliditaetsgrad(new Integer(enr02.getDegreInvalidite()).shortValue());
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));

        return donneeAI;
    }

    /**
     * Méthode qui rempli
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    private IVDaten9Type rempliIVDatenType9Conjoint(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDaten9Type donneeAI = factoryType.createIVDaten9Type();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAiCompEpouse()));
        donneeAI.setInvaliditaetsgrad(new Integer(enr02.getDegreInvaliditeEpouse()).shortValue());
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmiteEpouse(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvtAssureEpouse()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvaliditeEpouse()));

        return donneeAI;
    }

    private IVDaten9WeakType rempliIVDatenType9ConjointWeak(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDaten9WeakType donneeAI = factoryType.createIVDaten9WeakType();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAiCompEpouse()));
        donneeAI.setInvaliditaetsgrad(new Integer(enr02.getDegreInvaliditeEpouse()).shortValue());
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmiteEpouse(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvtAssureEpouse()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvaliditeEpouse()));

        return donneeAI;
    }
}
