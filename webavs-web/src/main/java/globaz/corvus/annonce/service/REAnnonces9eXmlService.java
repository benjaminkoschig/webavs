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
import ch.admin.zas.rc.DJE9BeschreibungType;
import ch.admin.zas.rc.Gutschriften9Type;
import ch.admin.zas.rc.IVDaten9Type;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
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

                parseAugmentationAvecAnakin(augmentation9eme01, augmentation9eme02, session, forMoisAnneeComptable);

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

                } else {
                    throw new Exception("no match into the expected genre de prestation");
                }

                if (!augmentation9eme02.isNew()) {
                    augmentation9eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation9eme02.update(transaction);
                }
                break;
            case 42:
                REAnnoncesDiminution9Eme diminution9eme01 = new REAnnoncesDiminution9Eme();
                diminution9eme01.setSession(session);
                diminution9eme01.setIdAnnonce(annonce.getIdAnnonce());
                diminution9eme01.retrieve();

                parseDiminutionAvecAnakin(diminution9eme01, session, forMoisAnneeComptable);

                break;
        }
        throw new Exception("no match into the expected variable paussibilities");
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
        rempliCasSpecial(enr01, enr02, description);
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
        rempliCasSpecial(enr01, enr02, description);
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
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
        rempliCasSpecial(enr01, enr02, description);
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
        rempliCasSpecial(enr01, enr02, description);
        if (!JadeStringUtil.isBlankOrZero(enr02.getReduction())) {
            description.setKuerzungSelbstverschulden(new Integer(enr02.getReduction()).shortValue());
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return annonce9;

    }

    private DJE9BeschreibungType rempliDJE9BeschreibungType(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) {
        DJE9BeschreibungType ramDescription = factoryType.createDJE9BeschreibungType();
        ramDescription.setAngerechneteEinkommen(new Integer(testSiNullouZero(enr02.getRevenuPrisEnCompte()))
                .shortValue());
        ramDescription
                .setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02.getRamDeterminant())));
        ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02
                .getDureeCotPourDetRAM())));

        return ramDescription;
    }

    private Gutschriften9Type rempliBonnifications9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) {
        Gutschriften9Type bte = factoryType.createGutschriften9Type();
        bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getRevenuAnnuelMoyenSansBTE())));
        bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getBteMoyennePrisEnCompte())));
        bte.setAnzahlErziehungsgutschrift(new Integer(testSiNullouZero(enr02.getNombreAnneeBTE())).shortValue());
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
        donneeAI.setGebrechensschluessel(new Integer(enr02.getCodeInfirmite().substring(0, 2)));
        donneeAI.setFunktionsausfallcode(new Integer(enr02.getCodeInfirmite().substring(2)).shortValue());
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
        donneeAI.setGebrechensschluessel(new Integer(enr02.getCodeInfirmite().substring(0, 2)));
        donneeAI.setFunktionsausfallcode(new Integer(enr02.getCodeInfirmite().substring(2)).shortValue());
        donneeAI.setDatumVersicherungsfall(retourneXMLGregorianCalendarFromMonth(enr02.getSurvenanceEvenAssure()));
        donneeAI.setIstFruehInvalid(convertIntToBoolean(enr02.getAgeDebutInvalidite()));

        return donneeAI;
    }

}
