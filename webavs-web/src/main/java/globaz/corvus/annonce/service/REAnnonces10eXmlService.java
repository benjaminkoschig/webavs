package globaz.corvus.annonce.service;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.admin.zas.rc.AbgangsmeldungType;
import ch.admin.zas.rc.AenderungsmeldungAO10Type;
import ch.admin.zas.rc.AenderungsmeldungHE10Type;
import ch.admin.zas.rc.AenderungsmeldungO10Type;
import ch.admin.zas.rc.DJE10BeschreibungType;
import ch.admin.zas.rc.DJE10BeschreibungWeakType;
import ch.admin.zas.rc.Gutschriften10Type;
import ch.admin.zas.rc.IVDaten10Type;
import ch.admin.zas.rc.IVDaten10WeakType;
import ch.admin.zas.rc.IVDatenHEType;
import ch.admin.zas.rc.IVDatenHEWeakType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslWeakType;
import ch.admin.zas.rc.RRMeldung10Type;
import ch.admin.zas.rc.RentenvorbezugType;
import ch.admin.zas.rc.RentenvorbezugWeakType;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.SkalaBerechnungWeakType;
import ch.admin.zas.rc.ZuwachsmeldungAO10Type;
import ch.admin.zas.rc.ZuwachsmeldungHE10Type;
import ch.admin.zas.rc.ZuwachsmeldungO10Type;

public class REAnnonces10eXmlService extends REAbstractAnnonceXmlService implements REAnnonceXmlService {

    public static final List<Integer> GENRE_PRESTATION_API = Arrays.asList(81, 82, 83, 84, 85, 86, 87, 88, 91, 92, 93,
            95, 96, 97);

    public static final List<Integer> GENRE_PRESTATION_ORDINAIRE = Arrays.asList(10, 13, 14, 15, 16, 33, 34, 35, 50,
            53, 54, 55);
    public static final List<Integer> GENRE_PRESTATION_EXTRAORDINAIRE = Arrays.asList(20, 23, 24, 25, 26, 45, 70, 73,
            74, 75);
    private static REAnnonceXmlService instance = new REAnnonces10eXmlService();

    private ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();

    public static REAnnonceXmlService getInstance() {
        return instance;
    }

    @Override
    public Object getAnnonceXml(REAnnoncesAbstractLevel1A annonce, String forMoisAnneeComptable, BSession session,
            BITransaction transaction) throws Exception {
        int genrePrestation = Integer.parseInt(annonce.getGenrePrestation());
        int codeApplication = Integer.parseInt(annonce.getCodeApplication());
        RRMeldung10Type annonceXmlT10;
        switch (codeApplication) {
            case 44:
            case 46:
                REAnnoncesAugmentationModification10Eme augmentation10eme01 = retrieveAnnonceAugModif10(annonce,
                        session);

                REAnnoncesAugmentationModification10Eme augmentation10eme02 = retrieveAnnonceAugModif10_2(
                        augmentation10eme01, session);

                parseAugmentationAvecAnakin(augmentation10eme01, augmentation10eme02, session, forMoisAnneeComptable);

                if (GENRE_PRESTATION_ORDINAIRE.contains(genrePrestation)) {
                    if (codeApplication == 44) {
                        annonceXmlT10 = genererZuwachmeldungOrdentliche(augmentation10eme01, augmentation10eme02);
                    } else {
                        annonceXmlT10 = genererAenderungsmeldungOrdentliche(augmentation10eme01, augmentation10eme02);
                    }
                } else if (GENRE_PRESTATION_EXTRAORDINAIRE.contains(genrePrestation)) {
                    if (codeApplication == 44) {
                        annonceXmlT10 = genererZuwachmeldungAusserordentliche(augmentation10eme01, augmentation10eme02);
                    } else {
                        annonceXmlT10 = genererAenderungsmeldungAusserordentliche(augmentation10eme01,
                                augmentation10eme02);
                    }
                } else if (GENRE_PRESTATION_API.contains(genrePrestation)) {
                    if (codeApplication == 44) {
                        annonceXmlT10 = genererZuwachmeldungHilflosenentschaedigung(augmentation10eme01,
                                augmentation10eme02);
                    } else {
                        annonceXmlT10 = genererAenderungsmeldungHilflosenentschaedigung(augmentation10eme01,
                                augmentation10eme02);
                    }
                } else {
                    throw new Exception("Genre Prestation non identifi� " + genrePrestation);
                }

                checkAndUpdate10eme2(augmentation10eme02, transaction);
                break;

            case 45:
                REAnnoncesDiminution10Eme diminution10eme01 = retrieveAnnonceDimi10(annonce, session);

                parseDiminutionAvecAnakin(diminution10eme01, session, forMoisAnneeComptable);
                if (GENRE_PRESTATION_ORDINAIRE.contains(genrePrestation)) {
                    annonceXmlT10 = genererAbgangsOrdentliche(diminution10eme01);
                } else if (GENRE_PRESTATION_EXTRAORDINAIRE.contains(genrePrestation)) {
                    annonceXmlT10 = genererAbgangsAusserordentliche(diminution10eme01);
                } else if (GENRE_PRESTATION_API.contains(genrePrestation)) {
                    annonceXmlT10 = genererAbgangsHilflosenentschaedigung(diminution10eme01);
                } else {
                    throw new Exception("Genre Prestation non identifi� " + genrePrestation);
                }
                break;
            default:
                throw new Exception("no match into the expected CodeApplication for " + getClass().getSimpleName());
        }
        if (annonceXmlT10 == null) {
            throw new Exception("La g�m�ation de l'annonce Xml ne s'est pas effectui�e correctement");
        }
        return annonceXmlT10;

    }

    protected void checkAndUpdate10eme2(REAnnoncesAugmentationModification10Eme augmentation10eme02,
            BITransaction transaction) throws Exception {
        if (!augmentation10eme02.isNew()) {
            augmentation10eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
            augmentation10eme02.update(transaction);
        }
    }

    protected REAnnoncesDiminution10Eme retrieveAnnonceDimi10(REAnnoncesAbstractLevel1A annonce, BSession session)
            throws Exception {
        REAnnoncesDiminution10Eme diminution10eme01 = new REAnnoncesDiminution10Eme();
        diminution10eme01.setSession(session);
        diminution10eme01.setIdAnnonce(annonce.getIdAnnonce());
        diminution10eme01.retrieve();
        return diminution10eme01;
    }

    /**
     * DAO
     * 
     * @param session
     * @param augmentation10eme01
     * @return
     * @throws Exception
     */
    protected REAnnoncesAugmentationModification10Eme retrieveAnnonceAugModif10_2(
            REAnnoncesAugmentationModification10Eme augmentation10eme01, BSession session) throws Exception {
        REAnnoncesAugmentationModification10Eme augmentation10eme02 = new REAnnoncesAugmentationModification10Eme();
        augmentation10eme02.setSession(session);
        augmentation10eme02.setIdAnnonce(augmentation10eme01.getIdLienAnnonce());
        augmentation10eme02.retrieve();
        return augmentation10eme02;
    }

    /**
     * DAO
     * 
     * @param annonce
     * @param session
     * @return
     * @throws Exception
     */
    protected REAnnoncesAugmentationModification10Eme retrieveAnnonceAugModif10(REAnnoncesAbstractLevel1A annonce,
            BSession session) throws Exception {
        REAnnoncesAugmentationModification10Eme augmentation10eme01 = new REAnnoncesAugmentationModification10Eme();
        augmentation10eme01.setSession(session);
        augmentation10eme01.setIdAnnonce(annonce.getIdAnnonce());
        augmentation10eme01.retrieve();
        return augmentation10eme01;
    }

    protected RRMeldung10Type genererAbgangsHilflosenentschaedigung(REAnnoncesDiminution10Eme enr01) throws Exception {
        RRMeldung10Type.Hilflosenentschaedigung renteaAPI = factoryType.createRRMeldung10TypeHilflosenentschaedigung();
        AbgangsmeldungType diminution = createDiminutionCommune(enr01);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        renteaAPI.setAbgangsmeldung(diminution);
        meldung10Type.setHilflosenentschaedigung(renteaAPI);
        return meldung10Type;
    }

    protected RRMeldung10Type genererAbgangsAusserordentliche(REAnnoncesDiminution10Eme enr01) throws Exception {
        RRMeldung10Type.AusserordentlicheRente renteExtraordinaire = factoryType
                .createRRMeldung10TypeAusserordentlicheRente();
        AbgangsmeldungType diminution = createDiminutionCommune(enr01);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        renteExtraordinaire.setAbgangsmeldung(diminution);
        meldung10Type.setAusserordentlicheRente(renteExtraordinaire);
        return meldung10Type;
    }

    protected RRMeldung10Type genererAbgangsOrdentliche(REAnnoncesDiminution10Eme enr01) throws Exception {
        RRMeldung10Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung10TypeOrdentlicheRente();
        AbgangsmeldungType diminution = createDiminutionCommune(enr01);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        renteOrdinaire.setAbgangsmeldung(diminution);
        meldung10Type.setOrdentlicheRente(renteOrdinaire);
        return meldung10Type;
    }

    protected AbgangsmeldungType createDiminutionCommune(REAnnoncesDiminution10Eme enr01) throws Exception {
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

    protected RRMeldung10Type genererAenderungsmeldungHilflosenentschaedigung(
            REAnnoncesAugmentationModification10Eme enr01, REAnnoncesAugmentationModification10Eme enr02)
            throws Exception {
        RRMeldung10Type.Hilflosenentschaedigung renteAPI = factoryType.createRRMeldung10TypeHilflosenentschaedigung();
        AenderungsmeldungHE10Type modification = factoryType.createAenderungsmeldungHE10Type();
        modification.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        modification.setKasseZweigstelle(retourneCaisseAgence());
        modification.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01, enr02);
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungHE10Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungHE10TypeLeistungsbeschreibung();

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
        AenderungsmeldungHE10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungHE10TypeLeistungsbeschreibungBerechnungsgrundlagen();

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenHEWeakTypeAssure(enr01, enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteAPI.setAenderungsmeldung(modification);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setHilflosenentschaedigung(renteAPI);
        return meldung10Type;
    }

    protected RRMeldung10Type genererZuwachmeldungHilflosenentschaedigung(
            REAnnoncesAugmentationModification10Eme enr01, REAnnoncesAugmentationModification10Eme enr02)
            throws Exception {
        RRMeldung10Type.Hilflosenentschaedigung renteAPI = factoryType.createRRMeldung10TypeHilflosenentschaedigung();
        ZuwachsmeldungHE10Type augmentation = factoryType.createZuwachsmeldungHE10Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungHE10Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungHE10TypeLeistungsbeschreibung();

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
        ZuwachsmeldungHE10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungHE10TypeLeistungsbeschreibungBerechnungsgrundlagen();

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenHETypeAssure(enr01, enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteAPI.setZuwachsmeldung(augmentation);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setHilflosenentschaedigung(renteAPI);
        return meldung10Type;
    }

    /**
     * Modification ExtraOrdinaire
     * 
     * @param enr01
     * @param enr02
     * @return RRMeldung10Type
     * @throws Exception
     */
    protected RRMeldung10Type genererAenderungsmeldungAusserordentliche(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RRMeldung10Type.AusserordentlicheRente renteExtraordinaire = factoryType
                .createRRMeldung10TypeAusserordentlicheRente();
        AenderungsmeldungAO10Type modification = factoryType.createAenderungsmeldungAO10Type();
        modification.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        modification.setKasseZweigstelle(retourneCaisseAgence());
        modification.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01, enr02);
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungAO10Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungAO10TypeLeistungsbeschreibung();

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
        AenderungsmeldungAO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungAO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenWeakType10Assure(enr01, enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteExtraordinaire.setAenderungsmeldung(modification);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setAusserordentlicheRente(renteExtraordinaire);
        return meldung10Type;
    }

    /**
     * Augmentation ExtraOrdinaire
     * 
     * @param enr01
     * @param enr02
     * @return RRMeldung10Type
     * @throws Exception
     */
    protected RRMeldung10Type genererZuwachmeldungAusserordentliche(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RRMeldung10Type.AusserordentlicheRente renteExtraordinaire = factoryType
                .createRRMeldung10TypeAusserordentlicheRente();
        ZuwachsmeldungAO10Type augmentation = factoryType.createZuwachsmeldungAO10Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungAO10Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungAO10TypeLeistungsbeschreibung();

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
        ZuwachsmeldungAO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungAO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType10Assure(enr01, enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteExtraordinaire.setZuwachsmeldung(augmentation);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setAusserordentlicheRente(renteExtraordinaire);
        return meldung10Type;
    }

    /**
     * Rente ordinaire Modification
     * 
     * utilise les WeakType
     * */
    protected RRMeldung10Type genererAenderungsmeldungOrdentliche(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RRMeldung10Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung10TypeOrdentlicheRente();
        AenderungsmeldungO10Type modification = factoryType.createAenderungsmeldungO10Type();
        modification.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        modification.setKasseZweigstelle(retourneCaisseAgence());
        modification.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01, enr02);
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungO10Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungO10TypeLeistungsbeschreibung();

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
        AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungWeakType echelleCalcul = rempliScalaBerechnungWeakTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        DJE10BeschreibungWeakType ram = rempliDJE10BeschreibungWeakType(enr01, enr02);
        baseDeCalcul.setDJEBeschreibung(ram);

        Gutschriften10Type bte = rempliBonnifications10e(enr01, enr02);
        baseDeCalcul.setGutschriften(bte);

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenWeakType10Assure(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createAenderungsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubTypeWeak(enr01, enr02));
            ajournement.setRentenvorbezug(rempliRentenvorbezugWeakType(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteOrdinaire.setAenderungsmeldung(modification);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setOrdentlicheRente(renteOrdinaire);
        return meldung10Type;
    }

    /**
     * Rente ordinaire Augmentation
     * 
     * @param augmentation10eme01
     * @param augmentation10eme02
     * @return
     * @throws Exception
     */
    protected RRMeldung10Type genererZuwachmeldungOrdentliche(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RRMeldung10Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung10TypeOrdentlicheRente();
        ZuwachsmeldungO10Type augmentation = factoryType.createZuwachsmeldungO10Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01, enr02);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungO10Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO10TypeLeistungsbeschreibung();

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
        ZuwachsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = rempliScalaBerechnungTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        DJE10BeschreibungType ram = rempliDJE10BeschreibungType(enr01, enr02);
        baseDeCalcul.setDJEBeschreibung(ram);

        Gutschriften10Type bte = rempliBonnifications10e(enr01, enr02);
        baseDeCalcul.setGutschriften(bte);

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType10Assure(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            ZuwachsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubType(enr01, enr02));
            ajournement.setRentenvorbezug(rempliRentenvorbezugType(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr01, enr02));
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setOrdentlicheRente(renteOrdinaire);
        return meldung10Type;
    }

    /**
     * M�thode qui retourne un objet qui rempli les ajournements WEAK
     * 
     * @param enr01
     * @param enr02
     * @return un ajournement
     * @throws Exception
     */
    private RentenvorbezugWeakType rempliRentenvorbezugWeakType(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RentenvorbezugWeakType anticipation = factoryType.createRentenvorbezugWeakType();
        anticipation.setAnzahlVorbezugsjahre(Integer.valueOf(enr02.getNbreAnneeAnticipation()));
        anticipation.setVorbezugsdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateDebutAnticipation()));
        anticipation.setVorbezugsreduktion(new BigDecimal(testSiNullouZero(enr02.getReductionAnticipation())));
        return anticipation;
    }

    /**
     * M�thode qui retourne un objet qui rempli les ajournements
     * 
     * @param enr01
     * @param enr02
     * @return un ajournement
     * @throws Exception
     */
    private RentenvorbezugType rempliRentenvorbezugType(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RentenvorbezugType anticipation = factoryType.createRentenvorbezugType();
        anticipation.setAnzahlVorbezugsjahre(Integer.valueOf(enr02.getNbreAnneeAnticipation()));
        anticipation.setVorbezugsdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateDebutAnticipation()));
        anticipation.setVorbezugsreduktion(new BigDecimal(testSiNullouZero(enr02.getReductionAnticipation())));
        return anticipation;
    }

    private Gutschriften10Type rempliBonnifications10e(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) {
        Gutschriften10Type bte = factoryType.createGutschriften10Type();
        bte.setAnzahlErziehungsgutschrift(convertAAMMtoBigDecimal(enr02.getNombreAnneeBTE()));
        bte.setAnzahlBetreuungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getNbreAnneeBTA())));
        bte.setAnzahlUebergangsgutschrift(new BigDecimal(testSiNullouZero(enr02.getNbreAnneeBonifTrans())));
        return bte;
    }

    /**
     * <b> WeakType </b>
     * 
     * @param enr01
     * @param enr02
     * @return
     */
    private DJE10BeschreibungWeakType rempliDJE10BeschreibungWeakType(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) {
        DJE10BeschreibungWeakType ramDescription = factoryType.createDJE10BeschreibungWeakType();
        // duree de coti ram
        ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal(enr02
                .getDureeCotPourDetRAM()));
        ramDescription
                .setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02.getRamDeterminant())));
        ramDescription.setGesplitteteEinkommen(convertIntToBoolean(enr02.getCodeRevenuSplitte()));
        return ramDescription;
    }

    private DJE10BeschreibungType rempliDJE10BeschreibungType(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) {
        DJE10BeschreibungType ramDescription = factoryType.createDJE10BeschreibungType();
        // duree de coti ram
        ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal(enr02
                .getDureeCotPourDetRAM()));
        ramDescription
                .setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02.getRamDeterminant())));
        ramDescription.setGesplitteteEinkommen(convertIntToBoolean(enr02.getCodeRevenuSplitte()));
        return ramDescription;
    }

    /**
     * Rempli les donn�es utiles pour les donn�es AI 9e r�vision
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    private IVDaten10WeakType rempliIVDatenWeakType10Assure(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDaten10WeakType donneeAI = factoryType.createIVDaten10WeakType();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAICompetent()));
        donneeAI.setInvaliditaetsgrad(new Integer(enr02.getDegreInvalidite()).shortValue());
        // s'assurer des 5 positions avant le split
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));

        return donneeAI;
    }

    /**
     * Rempli les donn�es utiles pour les donn�es AI 9e r�vision
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    private IVDaten10Type rempliIVDatenType10Assure(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDaten10Type donneeAI = factoryType.createIVDaten10Type();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAICompetent()));
        donneeAI.setInvaliditaetsgrad(new Integer(enr02.getDegreInvalidite()).shortValue());
        // s'assurer des 5 positions avant le split
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));

        return donneeAI;
    }

    /**
     * Rempli les donn�es utiles pour les donn�es AI 9e r�vision
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    private IVDatenHEType rempliIVDatenHETypeAssure(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDatenHEType donneeAI = factoryType.createIVDatenHEType();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAICompetent()));
        // s'assurer des 5 positions avant le split
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setArtHEAnspruch(Integer.valueOf(enr02.getGenreDroitAPI()).shortValue());
        return donneeAI;
    }

    /**
     * Rempli les donn�es utiles pour les donn�es AI 9e r�vision
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    private IVDatenHEWeakType rempliIVDatenHEWeakTypeAssure(REAnnoncesAugmentationModification10Eme enr01,
            REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDatenHEWeakType donneeAI = factoryType.createIVDatenHEWeakType();
        donneeAI.setIVStelle(new Integer(enr02.getOfficeAICompetent()));
        // s'assurer des 5 positions avant le split
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(new Integer(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(new Integer(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setArtHEAnspruch(Integer.valueOf(enr02.getGenreDroitAPI()).shortValue());
        return donneeAI;
    }
}
