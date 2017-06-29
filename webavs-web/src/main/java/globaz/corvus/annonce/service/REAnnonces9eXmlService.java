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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.admin.zas.rc.AbgangsmeldungType;
import ch.admin.zas.rc.AenderungsmeldungAO9Type;
import ch.admin.zas.rc.AenderungsmeldungHE9Type;
import ch.admin.zas.rc.AenderungsmeldungO9Type;
import ch.admin.zas.rc.DJE9BeschreibungType;
import ch.admin.zas.rc.DJE9BeschreibungWeakType;
import ch.admin.zas.rc.Gutschriften9Type;
import ch.admin.zas.rc.Gutschriften9WeakType;
import ch.admin.zas.rc.IVDaten9Type;
import ch.admin.zas.rc.IVDaten9WeakType;
import ch.admin.zas.rc.IVDatenHE9Type;
import ch.admin.zas.rc.IVDatenHE9WeakType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslWeakType;
import ch.admin.zas.rc.RRMeldung9Type;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.SkalaBerechnungWeakType;
import ch.admin.zas.rc.ZuwachsmeldungAO9Type;
import ch.admin.zas.rc.ZuwachsmeldungHE9Type;
import ch.admin.zas.rc.ZuwachsmeldungO9Type;

/**
 * @author jmc
 * 
 */
public class REAnnonces9eXmlService extends REAbstractAnnonceXmlService implements REAnnonceXmlService {

    private static final Logger LOG = LoggerFactory.getLogger(REAnnonces9eXmlService.class);

    protected static Set<Integer> ORDENTLICHERENTE = new HashSet<Integer>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 33,
            34, 35, 36, 50, 51, 52, 53, 54, 55, 56));
    protected static Set<Integer> AUSSERORDENTLICHERENTE = new HashSet<Integer>(Arrays.asList(20, 21, 22, 23, 24, 25,
            26, 43, 44, 45, 46, 70, 71, 72, 73, 74, 75, 76));
    protected static Set<Integer> HILFLOSENENTSCHAEDIGUNG = new HashSet<Integer>(Arrays.asList(81, 82, 83, 84, 85, 86,
            87, 88, 89, 91, 92, 93, 95, 96, 97));

    private static REAnnonceXmlService instance = new REAnnonces9eXmlService();

    public static REAnnonceXmlService getInstance() {

        return instance;
    }

    @Override
    public Object getAnnonceXml(REAnnoncesAbstractLevel1A annonce, String forMoisAnneeComptable, BSession session,
            BITransaction transaction) throws Exception {
        int genrePrestation = Integer.parseInt(annonce.getGenrePrestation());
        int codeApplication = Integer.parseInt(annonce.getCodeApplication());
        RRMeldung9Type annonceXmlT9 = null;
        switch (codeApplication) {
            case 41:
            case 43:
                REAnnoncesAugmentationModification9Eme augmentation9eme01 = retrieveAnnonceAugModif9(annonce, session);

                REAnnoncesAugmentationModification9Eme augmentation9eme02 = retrieveAnnonceAugModif9eme2(
                        augmentation9eme01, session);

                parseAugmentationAvecAnakin(augmentation9eme01, augmentation9eme02, session, forMoisAnneeComptable);

                if (ORDENTLICHERENTE.contains(genrePrestation)) {
                    if (codeApplication == 41) {
                        annonceXmlT9 = annonceAugmentationOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    } else {
                        annonceXmlT9 = annonceChangementOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    }
                } else if (AUSSERORDENTLICHERENTE.contains(genrePrestation)) {
                    if (codeApplication == 41) {
                        annonceXmlT9 = annonceAugmentationExtraOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    } else {
                        annonceXmlT9 = annonceChangementExtraOrdinaire9e(augmentation9eme01, augmentation9eme02);
                    }
                } else if (HILFLOSENENTSCHAEDIGUNG.contains(genrePrestation)) {
                    if (codeApplication == 41) {
                        annonceXmlT9 = annonceAugmentationAPI9e(augmentation9eme01, augmentation9eme02);
                    } else {
                        annonceXmlT9 = annonceChangementAPI9e(augmentation9eme01, augmentation9eme02);
                    }
                } else {
                    LOG.warn("La valeur du genre de prestation {} ne fait pas partie des valeurs attendues ",
                            genrePrestation);
                    throw new Exception("no match into the expected genre de prestation");
                }
                checkAndUpdate9eme(augmentation9eme02, transaction);
                break;
            case 42:
                REAnnoncesDiminution9Eme diminution9eme01 = retrieveAnnonceDimi9(annonce, session);
                parseDiminutionAvecAnakin(diminution9eme01, session, forMoisAnneeComptable);

                if (ORDENTLICHERENTE.contains(genrePrestation)) {
                    annonceXmlT9 = annonceDiminutionOrdinaire(diminution9eme01);
                }
                if (AUSSERORDENTLICHERENTE.contains(genrePrestation)) {
                    annonceXmlT9 = annonceDiminutionExtraOrdinaire(diminution9eme01);
                }
                if (HILFLOSENENTSCHAEDIGUNG.contains(genrePrestation)) {
                    annonceXmlT9 = annonceDiminutionAPI(diminution9eme01);
                }
                break;
            default:
                LOG.warn("La valeur du code application {} ne fait pas partie des valeurs attendues ", codeApplication);
                throw new Exception("no match into the expected CodeApplication for " + getClass().getSimpleName());
        }

        if (annonceXmlT9 == null) {
            LOG.debug("l'annonce9eme vaut NULL");
            throw new Exception("La génération de l'annonce Xml ne s'est pas effectuiée correctement");
        }
        return annonceXmlT9;

    }

    protected REAnnoncesDiminution9Eme retrieveAnnonceDimi9(REAnnoncesAbstractLevel1A annonce, BSession session)
            throws Exception {
        REAnnoncesDiminution9Eme diminution9eme01 = new REAnnoncesDiminution9Eme();
        diminution9eme01.setSession(session);
        diminution9eme01.setIdAnnonce(annonce.getIdAnnonce());
        diminution9eme01.retrieve();
        return diminution9eme01;
    }

    protected REAnnoncesAugmentationModification9Eme retrieveAnnonceAugModif9(REAnnoncesAbstractLevel1A annonce,
            BSession session) throws Exception {
        REAnnoncesAugmentationModification9Eme augmentation9eme01 = new REAnnoncesAugmentationModification9Eme();
        augmentation9eme01.setSession(session);
        augmentation9eme01.setIdAnnonce(annonce.getIdAnnonce());
        augmentation9eme01.retrieve();
        return augmentation9eme01;
    }

    protected REAnnoncesAugmentationModification9Eme retrieveAnnonceAugModif9eme2(
            REAnnoncesAugmentationModification9Eme augmentation9eme01, BSession session) throws Exception {
        REAnnoncesAugmentationModification9Eme augmentation9eme02 = new REAnnoncesAugmentationModification9Eme();
        augmentation9eme02.setSession(session);
        augmentation9eme02.setIdAnnonce(augmentation9eme01.getIdLienAnnonce());
        augmentation9eme02.retrieve();
        return augmentation9eme02;
    }

    protected void checkAndUpdate9eme(REAnnoncesAugmentationModification9Eme augmentation9eme, BITransaction transaction)
            throws Exception {
        if (!augmentation9eme.isNew()) {
            augmentation9eme.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
            augmentation9eme.update(transaction);
        }
    }

    protected RRMeldung9Type annonceChangementAPI9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.Hilflosenentschaedigung renteAPI = factoryType.createRRMeldung9TypeHilflosenentschaedigung();
        AenderungsmeldungHE9Type modification = factoryType.createAenderungsmeldungHE9Type();
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
        AenderungsmeldungHE9Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungHE9TypeLeistungsbeschreibung();

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
        AenderungsmeldungHE9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungHE9TypeLeistungsbeschreibungBerechnungsgrundlagen();

        // Si pas d'office AI pas de bloc AI

        baseDeCalcul.setIVDaten(rempliIVDatenWeakTypeHE9Assure(enr02));

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteAPI.setAenderungsmeldung(modification);
        annonce9.setHilflosenentschaedigung(renteAPI);
        return annonce9;

    }

    protected RRMeldung9Type annonceAugmentationAPI9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.Hilflosenentschaedigung renteAPI = factoryType.createRRMeldung9TypeHilflosenentschaedigung();
        ZuwachsmeldungHE9Type augmentation = factoryType.createZuwachsmeldungHE9Type();
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
        ZuwachsmeldungHE9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungHE9TypeLeistungsbeschreibung();

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
        ZuwachsmeldungHE9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungHE9TypeLeistungsbeschreibungBerechnungsgrundlagen();

        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenTypeHE9Assure(enr02));
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteAPI.setZuwachsmeldung(augmentation);
        annonce9.setHilflosenentschaedigung(renteAPI);
        return annonce9;

    }

    protected RRMeldung9Type annonceAugmentationOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type augmentation = factoryType.createZuwachsmeldungO9Type();
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
        ZuwachsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }

        description.setMonatsbetrag(new BigDecimal(enr01.getMensualitePrestationsFrancs()));

        // Base de calcul
        ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = rempliSkalaBerechnungTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9Type bte = rempliBonnifications9e(enr02);

        baseDeCalcul.setGutschriften(bte);
        DJE9BeschreibungType ram = rempliDJE9BeschreibungType(enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9Assure(enr02));
        }
        if (!JadeStringUtil.isBlank(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9Conjoint(enr02));
        }

        if (wantGenerateAjournement(enr02)) {
            // Ajournement
            ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubType(enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
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
        AenderungsmeldungO9Type modification = factoryType.createAenderungsmeldungO9Type();
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
        AenderungsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungO9TypeLeistungsbeschreibung();

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
        AenderungsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        // Echelle de la base de calcul
        ch.admin.zas.rc.SkalaBerechnungWeakType echelleCalcul = rempliSkalaBerechnungWeakTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9WeakType bte = rempliBonnificationsWeak9e(enr02);

        baseDeCalcul.setGutschriften(bte);
        DJE9BeschreibungWeakType ram = rempliDJE9BeschreibungWeakType(enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI

        baseDeCalcul.setIVDaten(rempliIVDatenType9AssureWeak(enr02));

        baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9ConjointWeak(enr02));

        if (wantGenerateAjournement(enr02)) {
            // Ajournement
            AenderungsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createAenderungsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubTypeWeak(enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));

        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteOrdinaire.setAenderungsmeldung(modification);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceChangementExtraOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.AusserordentlicheRente renteExtraordinaire = factoryType
                .createRRMeldung9TypeAusserordentlicheRente();
        AenderungsmeldungAO9Type modification = factoryType.createAenderungsmeldungAO9Type();
        modification.setBerichtsmonat(retourneXMLGregorianCalendarFromMonth(enr01.getMoisRapport()));

        modification.setKasseZweigstelle(retourneCaisseAgence());
        modification.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        modification.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslWeakType personne = rempliRRLeistungsberechtigtePersonAuslWeakType(enr01,
                enr01.getNouveauNoAssureAyantDroit());
        modification.setLeistungsberechtigtePerson(personne);
        // Description
        AenderungsmeldungAO9Type.Leistungsbeschreibung description = factoryType
                .createAenderungsmeldungAO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        if (!JadeStringUtil.isBlank(enr01.getDebutDroit())) {
            description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }

        description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        if (!JadeStringUtil.isBlank(enr01.getMensualitePrestationsFrancs())) {
            description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        }

        // Base de calcul
        AenderungsmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createAenderungsmeldungAO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }

        // Echelle de la base de calcul
        SkalaBerechnungWeakType echelleCalcul = rempliSkalaBerechnungWeakTyp(enr02);
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        Gutschriften9WeakType bte = rempliBonnificationsWeak9e(enr02);

        baseDeCalcul.setGutschriften(bte);
        DJE9BeschreibungWeakType ram = rempliDJE9BeschreibungWeakType(enr02);
        baseDeCalcul.setDJEBeschreibung(ram);
        // Si pas d'office AI pas de bloc AI

        baseDeCalcul.setIVDaten(rempliIVDatenType9AssureWeak(enr02));

        baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9ConjointWeak(enr02));

        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        modification.setLeistungsbeschreibung(description);
        renteExtraordinaire.setAenderungsmeldung(modification);
        annonce9.setAusserordentlicheRente(renteExtraordinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceAugmentationExtraOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.AusserordentlicheRente renteExtraordinaire = factoryType
                .createRRMeldung9TypeAusserordentlicheRente();
        ZuwachsmeldungAO9Type augmentation = factoryType.createZuwachsmeldungAO9Type();
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
        ZuwachsmeldungAO9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungAO9TypeLeistungsbeschreibung();

        description.setLeistungsart(enr01.getGenrePrestation());
        description.setAnspruchsbeginn(retourneXMLGregorianCalendarFromMonth(enr01.getDebutDroit()));
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }
        if (JadeStringUtil.isBlank(enr01.getMensualiteRenteOrdRemp())) {
            description.setMonatsbetragErsetzteOrdentlicheRente(new BigDecimal(enr01.getMensualiteRenteOrdRemp()));
        }
        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));

        // Base de calcul
        ZuwachsmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen baseDeCalcul = factoryType
                .createZuwachsmeldungAO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        if (!JadeStringUtil.isBlank(enr02.getAnneeNiveau())) {
            baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromYear(enr02.getAnneeNiveau()));
        }
        baseDeCalcul.setEinkommensgrenzenCode(convertIntToBoolean(testSiNullouZero(enr02.getAnneeNiveau())));
        baseDeCalcul.setMinimalgarantieCode(convertIntToBoolean(testSiNullouZero(enr02.getIsMinimumGaranti())));
        // Echelle de la base de calcul
        if (!JadeStringUtil.isBlank(enr02.getEchelleRente())) {
            SkalaBerechnungType echelleCalcul = rempliSkalaBerechnungTyp(enr02);
            baseDeCalcul.setSkalaBerechnung(echelleCalcul);
        }
        if (!JadeStringUtil.isBlank(enr02.getRevenuAnnuelMoyenSansBTE())) {
            Gutschriften9Type bte = rempliBonnifications9e(enr02);
            baseDeCalcul.setGutschriften(bte);
        }
        if (!JadeStringUtil.isBlank(enr02.getRevenuPrisEnCompte())) {
            DJE9BeschreibungType ram = rempliDJE9BeschreibungType(enr02);
            baseDeCalcul.setDJEBeschreibung(ram);
        }
        // Si pas d'office AI pas de bloc AI
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9Assure(enr02));
        }
        if (!JadeStringUtil.isBlank(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9Conjoint(enr02));
        }
        description.getSonderfallcodeRente().addAll(rempliCasSpecial(enr02));
        if (!JadeStringUtil.isBlank(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(Integer.valueOf(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteExtraordinaire.setZuwachsmeldung(augmentation);
        annonce9.setAusserordentlicheRente(renteExtraordinaire);
        return annonce9;

    }

    protected RRMeldung9Type annonceDiminutionOrdinaire(REAnnoncesDiminution9Eme enr01) throws Exception {

        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        RRMeldung9Type meldung9Type = factoryType.createRRMeldung9Type();
        AbgangsmeldungType diminution = createDiminutionCommune(enr01);

        renteOrdinaire.setAbgangsmeldung(diminution);
        meldung9Type.setOrdentlicheRente(renteOrdinaire);
        return meldung9Type;
    }

    protected RRMeldung9Type annonceDiminutionExtraOrdinaire(REAnnoncesDiminution9Eme enr01) throws Exception {

        RRMeldung9Type.AusserordentlicheRente renteExtraOrdinaire = factoryType
                .createRRMeldung9TypeAusserordentlicheRente();
        RRMeldung9Type meldung9Type = factoryType.createRRMeldung9Type();
        AbgangsmeldungType diminution = createDiminutionCommune(enr01);

        renteExtraOrdinaire.setAbgangsmeldung(diminution);
        meldung9Type.setAusserordentlicheRente(renteExtraOrdinaire);
        return meldung9Type;

    }

    protected RRMeldung9Type annonceDiminutionAPI(REAnnoncesDiminution9Eme enr01) throws Exception {

        RRMeldung9Type.Hilflosenentschaedigung api = factoryType.createRRMeldung9TypeHilflosenentschaedigung();
        RRMeldung9Type meldung9Type = factoryType.createRRMeldung9Type();
        AbgangsmeldungType diminution = createDiminutionCommune(enr01);

        api.setAbgangsmeldung(diminution);
        meldung9Type.setHilflosenentschaedigung(api);
        return meldung9Type;

    }

    private AbgangsmeldungType createDiminutionCommune(REAnnoncesDiminution9Eme enr01) throws Exception {
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
        if (!JadeStringUtil.isBlank(enr01.getFinDroit())) {
            description.setAnspruchsende(retourneXMLGregorianCalendarFromMonth(enr01.getFinDroit()));
        }
        if (!JadeStringUtil.isBlank(enr01.getCodeMutation())) {
            description.setMutationscode(Integer.valueOf(enr01.getCodeMutation()).shortValue());
        }
        description.setMonatsbetrag(new BigDecimal(testSiNullouZero(enr01.getMensualitePrestationsFrancs())));
        diminution.setLeistungsbeschreibung(description);
        return diminution;
    }

    private DJE9BeschreibungType rempliDJE9BeschreibungType(REAnnoncesAugmentationModification9Eme enr02) {
        DJE9BeschreibungType ramDescription = factoryType.createDJE9BeschreibungType();
        if (!JadeStringUtil.isBlank(enr02.getRevenuPrisEnCompte())) {
            ramDescription.setAngerechneteEinkommen(Integer.valueOf(testSiNullouZero(enr02.getRevenuPrisEnCompte()))
                    .shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getRamDeterminant())) {
            ramDescription.setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02
                    .getRamDeterminant())));
        }
        if (!JadeStringUtil.isBlank(enr02.getDureeCotPourDetRAM())) {
            ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal(enr02
                    .getDureeCotPourDetRAM()));
        }

        return ramDescription;
    }

    private DJE9BeschreibungWeakType rempliDJE9BeschreibungWeakType(REAnnoncesAugmentationModification9Eme enr02) {
        DJE9BeschreibungWeakType ramDescription = factoryType.createDJE9BeschreibungWeakType();
        if (!JadeStringUtil.isBlank(enr02.getRevenuPrisEnCompte())) {
            ramDescription.setAngerechneteEinkommen(Integer.valueOf(testSiNullouZero(enr02.getRevenuPrisEnCompte()))
                    .shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getRamDeterminant())) {
            ramDescription.setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02
                    .getRamDeterminant())));
        }
        if (!JadeStringUtil.isBlank(enr02.getDureeCotPourDetRAM())) {
            ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(convertAAMMtoBigDecimal(enr02
                    .getDureeCotPourDetRAM()));
        }

        return ramDescription;
    }

    private Gutschriften9Type rempliBonnifications9e(REAnnoncesAugmentationModification9Eme enr02) {
        Gutschriften9Type bte = factoryType.createGutschriften9Type();
        if (!JadeStringUtil.isBlank(enr02.getRevenuAnnuelMoyenSansBTE())) {
            bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getRevenuAnnuelMoyenSansBTE())));
        }
        if (!JadeStringUtil.isBlank(enr02.getBteMoyennePrisEnCompte())) {
            bte.setAngerechneteErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getBteMoyennePrisEnCompte())));
        }
        if (!JadeStringUtil.isBlank(enr02.getNombreAnneeBTE())) {
            bte.setAnzahlErziehungsgutschrift(convertAAMMtoBigDecimal(enr02.getNombreAnneeBTE()).shortValue());
        }
        return bte;

    }

    private Gutschriften9WeakType rempliBonnificationsWeak9e(REAnnoncesAugmentationModification9Eme enr02) {
        Gutschriften9WeakType bte = factoryType.createGutschriften9WeakType();
        if (!JadeStringUtil.isBlank(enr02.getRevenuAnnuelMoyenSansBTE())) {
            bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getRevenuAnnuelMoyenSansBTE())));
        }
        if (!JadeStringUtil.isBlank(enr02.getBteMoyennePrisEnCompte())) {
            bte.setAngerechneteErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getBteMoyennePrisEnCompte())));
        }
        if (!JadeStringUtil.isBlank(enr02.getNombreAnneeBTE())) {
            bte.setAnzahlErziehungsgutschrift(convertAAMMtoBigDecimal(enr02.getNombreAnneeBTE()).shortValue());
        }
        return bte;
    }

    /**
     * Rempli les données utiles pour les données AI 9e révision
     * 
     * @param enr02
     * 
     * @return
     * @throws Exception
     */
    private IVDaten9Type rempliIVDatenType9Assure(REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDaten9Type donneeAI = factoryType.createIVDaten9Type();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDegreInvalidite())) {
            donneeAI.setInvaliditaetsgrad(Integer.valueOf(enr02.getDegreInvalidite()).shortValue());
        }
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

    private IVDaten9WeakType rempliIVDatenType9AssureWeak(REAnnoncesAugmentationModification9Eme enr02)
            throws Exception {
        IVDaten9WeakType donneeAI = factoryType.createIVDaten9WeakType();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDegreInvalidite())) {
            donneeAI.setInvaliditaetsgrad(Integer.valueOf(enr02.getDegreInvalidite()).shortValue());
        }
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

    private IVDatenHE9Type rempliIVDatenTypeHE9Assure(REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDatenHE9Type donneeAI = factoryType.createIVDatenHE9Type();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        }
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

    private IVDatenHE9WeakType rempliIVDatenWeakTypeHE9Assure(REAnnoncesAugmentationModification9Eme enr02)
            throws Exception {
        IVDatenHE9WeakType donneeAI = factoryType.createIVDatenHE9WeakType();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAICompetent())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAICompetent()));
        }
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

    private IVDaten9Type rempliIVDatenType9Conjoint(REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        IVDaten9Type donneeAI = factoryType.createIVDaten9Type();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAiCompEpouse())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAiCompEpouse()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDegreInvaliditeEpouse())) {
            donneeAI.setInvaliditaetsgrad(Integer.valueOf(enr02.getDegreInvaliditeEpouse()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getCodeInfirmite())) {
            String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
            donneeAI.setGebrechensschluessel(Integer.valueOf(StringUtils.left(codeInfirmite, 3)));
            donneeAI.setFunktionsausfallcode(Integer.valueOf(StringUtils.right(codeInfirmite, 2)).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getSurvenanceEvenAssure())) {
            donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        }
        if (!JadeStringUtil.isBlank(enr02.getAgeDebutInvaliditeEpouse())) {
            donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvaliditeEpouse()));
        }
        return donneeAI;
    }

    private IVDaten9WeakType rempliIVDatenType9ConjointWeak(REAnnoncesAugmentationModification9Eme enr02)
            throws Exception {
        IVDaten9WeakType donneeAI = factoryType.createIVDaten9WeakType();
        if (!JadeStringUtil.isBlank(enr02.getOfficeAiCompEpouse())) {
            donneeAI.setIVStelle(Integer.valueOf(enr02.getOfficeAiCompEpouse()));
        }
        if (!JadeStringUtil.isBlank(enr02.getDegreInvaliditeEpouse())) {
            donneeAI.setInvaliditaetsgrad(Integer.valueOf(enr02.getDegreInvaliditeEpouse()).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getCodeInfirmite())) {
            String codeInfirmite = StringUtils.leftPad(enr02.getCodeInfirmite(), 5);
            donneeAI.setGebrechensschluessel(Integer.valueOf(StringUtils.left(codeInfirmite, 3)));
            donneeAI.setFunktionsausfallcode(Integer.valueOf(StringUtils.right(codeInfirmite, 2)).shortValue());
        }
        if (!JadeStringUtil.isBlank(enr02.getSurvenanceEvenAssure())) {
            donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        }
        if (!JadeStringUtil.isBlank(enr02.getAgeDebutInvaliditeEpouse())) {
            donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvaliditeEpouse()));
        }

        return donneeAI;
    }

    protected boolean wantGenerateAjournement(REAnnoncesAugmentationModification9Eme enr02) {
        return !JadeStringUtil.isBlank(enr02.getDateRevocationAjournement())
                || !JadeStringUtil.isBlank(enr02.getDureeAjournement())
                || !JadeStringUtil.isBlank(enr02.getSupplementAjournement());
    }
}
