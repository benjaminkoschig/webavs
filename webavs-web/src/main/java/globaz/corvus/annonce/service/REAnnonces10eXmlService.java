package globaz.corvus.annonce.service;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.zas.rc.DJE9BeschreibungType;
import ch.admin.zas.rc.FamilienAngehoerigeType;
import ch.admin.zas.rc.Gutschriften9Type;
import ch.admin.zas.rc.IVDaten9Type;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRMeldung9Type;
import ch.admin.zas.rc.RentenaufschubType;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.ZuwachsmeldungO9Type;
import ch.globaz.common.properties.CommonProperties;

public class REAnnonces10eXmlService extends REAbstractAnnonceXmlService implements REAnnonceXmlService {

    private static REAnnonceXmlService instance = new REAnnonces10eXmlService();

    private ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();

    public static REAnnonceXmlService getInstance() {
        return instance;
    }

    @Override
    public Object getAnnonceXml(REAnnoncesAbstractLevel1A annonce, String forMoisAnneeComptable, BSession session,
            BITransaction transaction) throws Exception {

        int codeApplication = Integer.parseInt(annonce.getCodeApplication());
        switch (codeApplication) {
            case 44:
            case 46:
                REAnnoncesAugmentationModification10Eme augmentation10eme01 = new REAnnoncesAugmentationModification10Eme();
                augmentation10eme01.setSession(session);
                augmentation10eme01.setIdAnnonce(annonce.getIdAnnonce());
                augmentation10eme01.retrieve();

                REAnnoncesAugmentationModification10Eme augmentation10eme02 = new REAnnoncesAugmentationModification10Eme();
                augmentation10eme02.setSession(session);
                augmentation10eme02.setIdAnnonce(augmentation10eme01.getIdLienAnnonce());
                augmentation10eme02.retrieve();

                parseAugmentationAvecAnakin(augmentation10eme01, augmentation10eme02, session, forMoisAnneeComptable);

                if (!augmentation10eme02.isNew()) {
                    augmentation10eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation10eme02.update(transaction);
                }
                break;
            case 45:
                REAnnoncesDiminution10Eme diminution10eme01 = new REAnnoncesDiminution10Eme();
                diminution10eme01.setSession(session);
                diminution10eme01.setIdAnnonce(annonce.getIdAnnonce());
                diminution10eme01.retrieve();

                parseDiminutionAvecAnakin(diminution10eme01, session, forMoisAnneeComptable);
        }
        throw new Exception("no match into the expected variable paussibilities");
    }

    private String retourneCaisseAgence() throws Exception {
        return CommonProperties.KEY_NO_CAISSE.getValue() + CommonProperties.NUMERO_AGENCE.getValue();
    }

    private Long retourneNoDAnnonceSur6Position(String noAnnonce) {
        if (noAnnonce.length() > 6) {
            noAnnonce = noAnnonce.substring(noAnnonce.length() - 6);
        }
        return new Long(noAnnonce);
    }

    private Boolean convertIntToBoolean(String valeurString) {
        if ("1".equals(valeurString)) {
            return true;
        } else {
            return false;
        }
    }

    protected String formatXPosAppendWithBlank(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append(" ");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

    public RRMeldung9Type annonceAugmentationOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {

        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type augmentation = factoryType.createZuwachsmeldungO9Type();
        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        augmentation.setKasseneigenerHinweis(enr01.getReferenceCaisseInterne());
        // Remplir la personne
        RRLeistungsberechtigtePersonAuslType personne = factoryType.createRRLeistungsberechtigtePersonAuslType();
        personne.setVersichertennummer(enr01.getNoAssAyantDroit());
        personne.setIstFluechtling(convertIntToBoolean(enr01.getIsRefugie()));
        FamilienAngehoerigeType membresDeLaFamille = factoryType.createFamilienAngehoerigeType();
        if (!JadeStringUtil.isBlankOrZero(enr01.getPremierNoAssComplementaire())) {
            membresDeLaFamille.getVNr1Ergaenzend().add(enr01.getPremierNoAssComplementaire());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getSecondNoAssComplementaire())) {
            membresDeLaFamille.getVNr1Ergaenzend().add(enr01.getSecondNoAssComplementaire());
        }
        personne.setWohnkantonStaat(new Integer(enr01.getCantonEtatDomicile()).toString());
        personne.setFamilienAngehoerige(membresDeLaFamille);
        personne.setZivilstand(new Integer(enr01.getEtatCivil()).shortValue());
        // Description
        ZuwachsmeldungO9Type.Leistungsbeschreibung description = factoryType
                .createZuwachsmeldungO9TypeLeistungsbeschreibung();
        augmentation.setLeistungsberechtigtePerson(personne);

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
        baseDeCalcul.setNiveaujahr(retourneXMLGregorianCalendarFromMonth(enr02.getAnneeNiveau()));

        // Echelle de la base de calcul
        SkalaBerechnungType echelleCalcul = factoryType.createSkalaBerechnungType();
        echelleCalcul.setSkala(new Integer(enr02.getEchelleRente()).shortValue());
        echelleCalcul.setBeitragsdauerVor1973(new BigDecimal(testSiNullouZero(enr02.getDureeCoEchelleRenteAv73())));
        echelleCalcul.setBeitragsdauerAb1973(new BigDecimal(testSiNullouZero(enr02.getDureeCoEchelleRenteDes73())));
        echelleCalcul.setAnrechnungVor1973FehlenderBeitragsmonate(new Integer(testSiNullouZero(enr02
                .getDureeCotManquante48_72())));
        echelleCalcul.setAnrechnungAb1973Bis1978FehlenderBeitragsmonate(new Integer(testSiNullouZero(enr02
                .getDureeCotManquante73_78())));
        echelleCalcul.setBeitragsjahreJahrgang(new Integer(testSiNullouZero(enr02.getAnneeCotClasseAge())));
        baseDeCalcul.setSkalaBerechnung(echelleCalcul);

        DJE9BeschreibungType ramDescription = factoryType.createDJE9BeschreibungType();
        ramDescription.setAngerechneteEinkommen(new Integer(testSiNullouZero(enr02.getRevenuPrisEnCompte()))
                .shortValue());
        ramDescription
                .setDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02.getRamDeterminant())));
        ramDescription.setBeitragsdauerDurchschnittlichesJahreseinkommen(new BigDecimal(testSiNullouZero(enr02
                .getDureeCotPourDetRAM())));
        baseDeCalcul.setDJEBeschreibung(ramDescription);

        Gutschriften9Type bte = factoryType.createGutschriften9Type();
        bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getRevenuAnnuelMoyenSansBTE())));
        bte.setDJEohneErziehungsgutschrift(new BigDecimal(testSiNullouZero(enr02.getBteMoyennePrisEnCompte())));
        bte.setAnzahlErziehungsgutschrift(new Integer(testSiNullouZero(enr02.getNombreAnneeBTE())).shortValue());
        baseDeCalcul.setGutschriften(bte);

        // Si pas d'office AI pas de bloc AI
        if (JadeStringUtil.isBlankOrZero(enr02.getOfficeAICompetent())) {
            baseDeCalcul.setIVDaten(rempliIVDatenType9Assure(enr01, enr02));
        }
        if (JadeStringUtil.isBlankOrZero(enr02.getOfficeAiCompEpouse())) {
            baseDeCalcul.setIVDatenEhefrau(rempliIVDatenType9Conjoint(enr01, enr02));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeAjournement())) {
            // Ajournement
            ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter ajournement = factoryType
                    .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagenFlexiblesRentenAlter();
            ajournement.setRentenaufschub(rempliRentenaufschubType(enr01, enr02));
            baseDeCalcul.setFlexiblesRentenAlter(ajournement);
        }
        description.setBerechnungsgrundlagen(baseDeCalcul);
        augmentation.setLeistungsbeschreibung(description);
        renteOrdinaire.setZuwachsmeldung(augmentation);
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return new RRMeldung9Type();

    }

    private void rempliCasSpecial(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02, ZuwachsmeldungO9Type.Leistungsbeschreibung desc) {
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial1())) {
            desc.getSonderfallcodeRente().add(new Integer(enr02.getCasSpecial1()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial2())) {
            desc.getSonderfallcodeRente().add(new Integer(enr02.getCasSpecial2()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial3())) {
            desc.getSonderfallcodeRente().add(new Integer(enr02.getCasSpecial3()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial4())) {
            desc.getSonderfallcodeRente().add(new Integer(enr02.getCasSpecial4()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial5())) {
            desc.getSonderfallcodeRente().add(new Integer(enr02.getCasSpecial5()).shortValue());
        }
    }

    private RentenaufschubType rempliRentenaufschubType(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        RentenaufschubType ajournement = factoryType.createRentenaufschubType();
        ajournement.setAbrufdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateRevocationAjournement()));
        ajournement.setAufschubsdauer(new BigDecimal(testSiNullouZero(enr02.getDureeAjournement())));
        ajournement.setAufschubszuschlag(new BigDecimal(testSiNullouZero(enr02.getSupplementAjournement())));
        return ajournement;
    }

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

    public XMLGregorianCalendar retourneXMLGregorianCalendar(String jaDate) throws Exception {

        final DateFormat format = new SimpleDateFormat("dd.mm.yyyy");
        final java.util.Date dDate = format.parse(jaDate);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
    }

    public XMLGregorianCalendar retourneXMLGregorianCalendarFromMonth(String dateMmYy) throws Exception {

        GregorianCalendar gregory = null;
        if (new Integer(dateMmYy.substring(2)) > 30) {
            new GregorianCalendar(new Integer(dateMmYy.substring(2)) + 1900, new Integer(dateMmYy.substring(0, 2)), 0);
        } else {
            new GregorianCalendar(new Integer(dateMmYy.substring(2)) + 2000, new Integer(dateMmYy.substring(0, 2)), 0);
        }

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
    }

    public XMLGregorianCalendar retourneXMLGregorianCalendarFromYear(String dateYy) throws Exception {

        GregorianCalendar gregory = null;
        if (new Integer(dateYy) > 30) {
            new GregorianCalendar(new Integer(dateYy.substring(2)) + 1900, 0, 0);
        } else {
            new GregorianCalendar(new Integer(dateYy.substring(2)) + 2000, 0, 0);
        }

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
    }

    private String testSiNullouZero(String valeur) {
        if (JadeStringUtil.isBlankOrZero(valeur)) {
            return "0";
        } else {
            return valeur;
        }
    }

}
