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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final int CODE_APPLICATION_AUGMENTATION_DIXIEME = 44;

    private static final Logger LOG = LoggerFactory.getLogger(REAnnonces10eXmlService.class);

    public static final List<Integer> GENRE_PRESTATION_10_API = Arrays.asList(81, 82, 83, 84, 85, 86, 87, 88, 89, 91,
            92, 93, 94, 95, 96, 97);

    public static final List<Integer> GENRE_PRESTATION_10_ORDINAIRE = Arrays.asList(10, 13, 14, 15, 16, 33, 34, 35, 36,
            50, 53, 54, 55, 56);
    public static final List<Integer> GENRE_PRESTATION_10_EXTRAORDINAIRE = Arrays.asList(20, 23, 24, 25, 26, 45, 70,
            73, 74, 75);
    private static REAnnonceXmlService instance = new REAnnonces10eXmlService();

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
            case CODE_APPLICATION_AUGMENTATION_DIXIEME:
            case 46:

                REAnnoncesAugmentationModification10Eme augmentation10eme01 = retrieveAnnonceAugModif10(annonce,
                        session);

                REAnnoncesAugmentationModification10Eme augmentation10eme02 = retrieveAnnonceAugModif10eme2(
                        augmentation10eme01, session);

                parseAugmentationAvecAnakin(augmentation10eme01, augmentation10eme02, session, forMoisAnneeComptable);

                if (GENRE_PRESTATION_10_ORDINAIRE.contains(genrePrestation)) {
                    if (codeApplication == CODE_APPLICATION_AUGMENTATION_DIXIEME) {
                        annonceXmlT10 = genererZuwachmeldungOrdentliche(augmentation10eme01, augmentation10eme02);
                    } else {
                        annonceXmlT10 = genererAenderungsmeldungOrdentliche(augmentation10eme01, augmentation10eme02);
                    }
                } else if (GENRE_PRESTATION_10_EXTRAORDINAIRE.contains(genrePrestation)) {
                    if (codeApplication == CODE_APPLICATION_AUGMENTATION_DIXIEME) {
                        annonceXmlT10 = genererZuwachmeldungAusserordentliche(augmentation10eme01, augmentation10eme02);
                    } else {
                        annonceXmlT10 = genererAenderungsmeldungAusserordentliche(augmentation10eme01,
                                augmentation10eme02);
                    }
                } else if (GENRE_PRESTATION_10_API.contains(genrePrestation)) {
                    if (codeApplication == CODE_APPLICATION_AUGMENTATION_DIXIEME) {
                        annonceXmlT10 = genererZuwachmeldungHilflosenentschaedigung(augmentation10eme01,
                                augmentation10eme02);
                    } else {
                        annonceXmlT10 = genererAenderungsmeldungHilflosenentschaedigung(augmentation10eme01,
                                augmentation10eme02);
                    }
                } else {
                    LOG.warn("La valeur du genre de prestation {} ne fait pas partie des valeurs attendues ",
                            genrePrestation);
                    throw new Exception("Genre Prestation non identifié " + genrePrestation);
                }

                checkAndUpdate10eme2(augmentation10eme02, transaction);
                break;

            case 45:
                REAnnoncesDiminution10Eme diminution10eme01 = retrieveAnnonceDimi10(annonce, session);

                parseDiminutionAvecAnakin(diminution10eme01, session, forMoisAnneeComptable);
                if (GENRE_PRESTATION_10_ORDINAIRE.contains(genrePrestation)) {
                    annonceXmlT10 = genererAbgangsOrdentliche(diminution10eme01);
                } else if (GENRE_PRESTATION_10_EXTRAORDINAIRE.contains(genrePrestation)) {
                    annonceXmlT10 = genererAbgangsAusserordentliche(diminution10eme01);
                } else if (GENRE_PRESTATION_10_API.contains(genrePrestation)) {
                    annonceXmlT10 = genererAbgangsHilflosenentschaedigung(diminution10eme01);
                } else {
                    LOG.warn("La valeur du genre de prestation {} ne fait pas partie des valeurs attendues ",
                            genrePrestation);
                    throw new Exception("Genre Prestation non identifié " + genrePrestation);
                }

                break;
            default:
                LOG.warn("La valeur du code application {} ne fait pas partie des valeurs attendues ", codeApplication);
                throw new Exception("no match into the expected CodeApplication for " + getClass().getSimpleName());
        }
        if (annonceXmlT10 == null) {
            LOG.debug("l'annonce10eme vaut NULL");
            throw new Exception("La génération de l'annonce Xml ne s'est pas effectuiée correctement");
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
    protected REAnnoncesAugmentationModification10Eme retrieveAnnonceAugModif10eme2(
            REAnnoncesAugmentationModification10Eme augmentation10eme01, BSession session) throws Exception {
        REAnnoncesAugmentationModification10Eme augmentation10eme02 = retrieveSugModif10emeGeneric(
                augmentation10eme01.getIdLienAnnonce(), session);
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
        REAnnoncesAugmentationModification10Eme augmentation10eme01 = retrieveSugModif10emeGeneric(
                annonce.getIdAnnonce(), session);
        return augmentation10eme01;
    }

    private REAnnoncesAugmentationModification10Eme retrieveSugModif10emeGeneric(String idAnnonce, BSession session)
            throws Exception {
        REAnnoncesAugmentationModification10Eme augmentation10eme01 = new REAnnoncesAugmentationModification10Eme();
        augmentation10eme01.setSession(session);
        augmentation10eme01.setIdAnnonce(idAnnonce);
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
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            diminution.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        AbgangsmeldungType.LeistungsberechtigtePerson person = factoryType
                .createAbgangsmeldungTypeLeistungsberechtigtePerson();
        person.setVersichertennummer(enr01.getNoAssAyantDroit());
        diminution.setLeistungsberechtigtePerson(person);
        AbgangsmeldungType.Leistungsbeschreibung description = factoryType
                .createAbgangsmeldungTypeLeistungsbeschreibung();
        description.getLeistungsart().add(enr01.getGenrePrestation());
        description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        diminution.setLeistungsbeschreibung(description);
        return diminution;
    }

    /**
     * Modification API
     * 
     * @param enr01
     * @param enr02
     * @return Annonce de Modification API
     * @throws Exception
     */
    protected RRMeldung10Type genererAenderungsmeldungHilflosenentschaedigung(
            REAnnoncesAugmentationModification10Eme enr01, REAnnoncesAugmentationModification10Eme enr02)
            throws Exception {
        RRMeldung10Type.Hilflosenentschaedigung renteAPI = factoryType.createRRMeldung10TypeHilflosenentschaedigung();
        AenderungsmeldungHE10Type modification = factoryType.createAenderungsmeldungHE10Type();
        modification.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        modification.setKasseZweigstelle(retourneCaisseAgence());
        modification.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01,
                enr01.getNouveauNoAssureAyantDroit());
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungHE10Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungHE10TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        if (!JadeStringUtil.isBlank(enr01.getDebutDroit())) {
            description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr01.getMensualitePrestationsFrancs())) {
            description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        }

        // Base de calcul
        AenderungsmeldungHE10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungHE10TypeLeistungsbeschreibungBerechnungsgrundlagen();

        baseDeCalcul.setIVDaten(rempliIVDatenHEWeakTypeAssure(enr02));
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));

        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteAPI.setAenderungsmeldung(modification);
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        meldung10Type.setHilflosenentschaedigung(renteAPI);
        return meldung10Type;
    }

    /**
     * Augmentation API
     * 
     * @param enr01
     * @param enr02
     * @return
     * @throws Exception
     */
    protected RRMeldung10Type genererZuwachmeldungHilflosenentschaedigung(
            REAnnoncesAugmentationModification10Eme enr01, REAnnoncesAugmentationModification10Eme enr02)
            throws Exception {
        RRMeldung10Type.Hilflosenentschaedigung renteAPI = factoryType.createRRMeldung10TypeHilflosenentschaedigung();
        ZuwachsmeldungHE10Type augmentation = factoryType.createZuwachsmeldungHE10Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungHE10Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungHE10TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungHE10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungHE10TypeLeistungsbeschreibungBerechnungsgrundlagen();

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenHETypeAssure(enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));

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
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01,
                enr01.getNouveauNoAssureAyantDroit());
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungAO10Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungAO10TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        if (!JadeStringUtil.isBlank(enr01.getDebutDroit())) {
            description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr01.getMensualitePrestationsFrancs())) {
            description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        }

        // Base de calcul
        AenderungsmeldungAO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungAO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        baseDeCalcul.setIVDaten(rempliIVDatenWeakType10(enr02));
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getIsSurvivant())) {
            description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));
        }

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
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungAO10Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungAO10TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungAO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungAO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType10(enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getIsSurvivant())) {
            description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));
        }

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
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01,
                enr01.getNouveauNoAssureAyantDroit());
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungO10Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungO10TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        if (!JadeStringUtil.isBlank(enr01.getDebutDroit())) {
            description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr01.getMensualitePrestationsFrancs())) {
            description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        }

        // Base de calcul
        AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        // Echelle de la base de calcul
        SkalaBerechnungWeakType echelleCalcul = rempliSkalaBerechnungWeakTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        DJE10BeschreibungWeakType ram = rempliDJE10BeschreibungWeakType(enr02);
        baseDeCalcul.setDJEBeschreibung(ram);

        Gutschriften10Type bte = rempliBonnifications10e(enr02);
        baseDeCalcul.setGutschriften(bte);

        baseDeCalcul.setIVDaten(rempliIVDatenWeakType10(enr02));

        if (wantGenerateAjournement(enr02)) {
            // Ajournement
            AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createAenderungsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            if (!JadeStringUtil.isBlank(enr02.getDateRevocationAjournement())
                    || !JadeStringUtil.isBlank(enr02.getDureeAjournement())
                    || !JadeStringUtil.isBlank(enr02.getSupplementAjournement())) {
                ajournement.setRentenaufschub(rempliRentenaufschubTypeWeak(enr02));
            } else {
                ajournement.setRentenvorbezug(rempliRentenvorbezugWeakType(enr02));
            }
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getIsSurvivant())) {
            description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));
        }

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
        RRMeldung10Type meldung10Type = factoryType.createRRMeldung10Type();
        RRMeldung10Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung10TypeOrdentlicheRente();

        ZuwachsmeldungO10Type augmentation = factoryType.createZuwachsmeldungO10Type();
        augmentation.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        if (!JadeStringUtil.isBlank(enr01.getReferenceCaisseInterne())) {
            augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        }
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = rempliRRLeistungsberechtigtePersonAuslType(enr01);
        augmentation.setLeistungsberechtigtePerson(personne);
        // Description
        ZuwachsmeldungO10Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO10TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagen();
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = rempliSkalaBerechnungTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        DJE10BeschreibungType ram = rempliDJE10BeschreibungType(enr02);
        baseDeCalcul.setDJEBeschreibung(ram);

        Gutschriften10Type bte = rempliBonnifications10e(enr02);
        baseDeCalcul.setGutschriften(bte);

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType10(enr02));
        }

        if (wantGenerateAjournement(enr02)) {
            ZuwachsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO10TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            if (!JadeStringUtil.isBlank(enr02.getDateRevocationAjournement())
                    || !JadeStringUtil.isBlank(enr02.getDureeAjournement())
                    || !JadeStringUtil.isBlank(enr02.getSupplementAjournement())) {
                ajournement.setRentenaufschub(rempliRentenaufschubType(enr02));
            } else {
                ajournement.setRentenvorbezug(rempliRentenvorbezugType(enr02));
            }
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getIsSurvivant())) {
            description.setIstInvaliderHinterlassener(convertIntToBoolean(enr02.getIsSurvivant()));
        }

        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);

        meldung10Type.setOrdentlicheRente(renteOrdinaire);

        return meldung10Type;
    }

    // METHODE PRIVATE ----------------------------------------------------------------------------------

    /**
     * Méthode qui retourne un objet qui rempli les ajournements WEAK
     * 
     * @param enr02
     * 
     * @return un ajournement
     * @throws Exception
     */
    private RentenvorbezugWeakType rempliRentenvorbezugWeakType(REAnnoncesAugmentationModification10Eme enr02)
            throws Exception {
        RentenvorbezugWeakType anticipation = factoryType.createRentenvorbezugWeakType();
        if (!JadeStringUtil.isBlank(enr02.getNbreAnneeAnticipation())) {
            anticipation.setAnzahlVorbezugsjahre(Integer.valueOf(enr02.getNbreAnneeAnticipation()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDateDebutAnticipation())) {
            anticipation.setVorbezugsdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateDebutAnticipation()));
        }
        if (!JadeStringUtil.isBlank(enr02.getReductionAnticipation())) {
            anticipation.setVorbezugsreduktion(new BigDecimal(testSiNullouZero(enr02.getReductionAnticipation())));
        }
        return anticipation;
    }

    /**
     * Méthode qui retourne un objet qui rempli les ajournements
     * 
     * @param enr02
     * 
     * @return un ajournement
     * @throws Exception
     */
    private RentenvorbezugType rempliRentenvorbezugType(REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        RentenvorbezugType anticipation = factoryType.createRentenvorbezugType();
        if (!JadeStringUtil.isBlank(enr02.getNbreAnneeAnticipation())) {
            anticipation.setAnzahlVorbezugsjahre(Integer.valueOf(enr02.getNbreAnneeAnticipation()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDateDebutAnticipation())) {
            anticipation.setVorbezugsdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateDebutAnticipation()));
        }
        if (!JadeStringUtil.isBlank(enr02.getReductionAnticipation())) {
            anticipation.setVorbezugsreduktion(new BigDecimal(testSiNullouZero(enr02.getReductionAnticipation())));
        }
        return anticipation;
    }

    private Gutschriften10Type rempliBonnifications10e(REAnnoncesAugmentationModification10Eme enr02) {
        Gutschriften10Type bte = factoryType.createGutschriften10Type();
        if (!JadeStringUtil.isBlank(enr02.getNombreAnneeBTE())) {
            bte.setAnzahlErziehungsgutschrift(convertAAMMtoBigDecimal(enr02.getNombreAnneeBTE()));
        }
        if (!JadeStringUtil.isBlank(enr02.getNbreAnneeBTA())) {
            bte.setAnzahlBetreuungsgutschrift(formatuneDecimale(enr02.getNbreAnneeBTA()));
        }
        if (!JadeStringUtil.isBlank(enr02.getNbreAnneeBonifTrans())) {
            if (!JadeStringUtil.isBlankOrZero(enr02.getNbreAnneeBonifTrans())) {
                bte.setAnzahlUebergangsgutschrift(new BigDecimal(testSiNullouZero(enr02.getNbreAnneeBonifTrans()))
                        .divide(new BigDecimal(10)).setScale(1));
            }
        }
        return bte;
    }

    /**
     * <b> WeakType </b>
     * 
     * @param enr02
     * 
     * @return
     */
    private DJE10BeschreibungWeakType rempliDJE10BeschreibungWeakType(REAnnoncesAugmentationModification10Eme enr02) {
        DJE10BeschreibungWeakType ramDescription = factoryType.createDJE10BeschreibungWeakType();
        // duree de coti ram
        if (!JadeStringUtil.isBlank(enr02.getDureeCotPourDetRAM())) {
            ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal(enr02
                    .getDureeCotPourDetRAM()));
        }
        if (!JadeStringUtil.isBlank(enr02.getRamDeterminant())) {
            ramDescription.setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02
                    .getRamDeterminant())));
        }
        if (!JadeStringUtil.isBlank(enr02.getCodeRevenuSplitte())) {
            ramDescription.setGesplitteteEinkommen(convertIntToBoolean(enr02.getCodeRevenuSplitte()));
        }
        return ramDescription;
    }

    private DJE10BeschreibungType rempliDJE10BeschreibungType(REAnnoncesAugmentationModification10Eme enr02) {
        DJE10BeschreibungType ramDescription = factoryType.createDJE10BeschreibungType();
        ramDescription
                .setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02.getRamDeterminant())));
        // duree de coti ram
        ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal(enr02
                .getDureeCotPourDetRAM()));
        ramDescription.setGesplitteteEinkommen(convertIntToBoolean(enr02.getCodeRevenuSplitte()));
        return ramDescription;
    }

    /**
     * Rempli les données utiles pour les données AI 9e révision
     * 
     * @param enr02
     * 
     * @return
     * @throws Exception
     */
    private IVDaten10WeakType rempliIVDatenWeakType10(REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDaten10WeakType donneeAI = factoryType.createIVDaten10WeakType();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDegreInvalidite())) {
            donneeAI.setInvaliditaetsgrad(Integer.valueOf(enr02.getDegreInvalidite()).shortValue());
        }
        // s'assurer des 5 positions avant le split
        if (!JadeStringUtil.isBlank(enr02.getCodeInfirmite())) {
            String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
            donneeAI.setGebrechensschluessel(Integer.valueOf(StringUtils.left(codeInfirmite, 3)));
            donneeAI.setFunktionsausfallcode(Integer.valueOf(StringUtils.right(codeInfirmite, 2)).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getSurvenanceEvenAssure())) {
            donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        }
        if (!JadeStringUtil.isBlank(enr02.getAgeDebutInvalidite())) {
            donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));
        }

        return donneeAI;
    }

    /**
     * Rempli les données utiles pour les données AI 9e révision
     * 
     * @param enr02
     * 
     * @return
     * @throws Exception
     */
    private IVDaten10Type rempliIVDatenType10(REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDaten10Type donneeAI = factoryType.createIVDaten10Type();
        donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        donneeAI.setInvaliditaetsgrad(Integer.valueOf(enr02.getDegreInvalidite()).shortValue());
        // s'assurer des 5 positions avant le split
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(Integer.valueOf(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(Integer.valueOf(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));

        return donneeAI;
    }

    /**
     * Rempli les données utiles pour les données AI 9e révision
     * 
     * @param enr02
     * 
     * @return
     * @throws Exception
     */
    private IVDatenHEType rempliIVDatenHETypeAssure(REAnnoncesAugmentationModification10Eme enr02) throws Exception {
        IVDatenHEType donneeAI = factoryType.createIVDatenHEType();
        donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        // s'assurer des 5 positions avant le split
        String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
        donneeAI.setGebrechensschluessel(Integer.valueOf(StringUtils.left(codeInfirmite, 3)));
        donneeAI.setFunktionsausfallcode(Integer.valueOf(StringUtils.right(codeInfirmite, 2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setArtHEAnspruch(Integer.valueOf(enr02.getGenreDroitAPI()).shortValue());
        return donneeAI;
    }

    /**
     * Rempli les données utiles pour les données AI 9e révision
     * 
     * @param enr02
     * 
     * @return
     * @throws Exception
     */
    private IVDatenHEWeakType rempliIVDatenHEWeakTypeAssure(REAnnoncesAugmentationModification10Eme enr02)
            throws Exception {
        IVDatenHEWeakType donneeAI = factoryType.createIVDatenHEWeakType();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        }
        // s'assurer des 5 positions avant le split
        if (!JadeStringUtil.isBlank(enr02.getCodeInfirmite())) {
            String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
            donneeAI.setGebrechensschluessel(Integer.valueOf(StringUtils.left(codeInfirmite, 3)));
            donneeAI.setFunktionsausfallcode(Integer.valueOf(StringUtils.right(codeInfirmite, 2)).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getSurvenanceEvenAssure())) {
            donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        }
        if (!JadeStringUtil.isBlank(enr02.getGenreDroitAPI())) {
            donneeAI.setArtHEAnspruch(Integer.valueOf(enr02.getGenreDroitAPI()).shortValue());
        }
        return donneeAI;
    }

    protected boolean wantGenerateAjournement(REAnnoncesAugmentationModification10Eme enr02) {
        return !JadeStringUtil.isBlank(enr02.getDateRevocationAjournement())
                || !JadeStringUtil.isBlank(enr02.getDureeAjournement())
                || !JadeStringUtil.isBlank(enr02.getSupplementAjournement())
                || !JadeStringUtil.isBlank(enr02.getNbreAnneeAnticipation())
                || !JadeStringUtil.isBlank(enr02.getDateDebutAnticipation())
                || !JadeStringUtil.isBlank(enr02.getReductionAnticipation());
    }

}
