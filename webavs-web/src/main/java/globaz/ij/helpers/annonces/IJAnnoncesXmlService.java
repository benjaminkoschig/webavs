package globaz.ij.helpers.annonces;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.admin.zas.rc.IVTaggelderMeldungType;
import ch.admin.zas.rc.IVTaggelderMeldungType.ErsteTaggeldPeriode;
import ch.admin.zas.rc.IVTaggelderMeldungType.NrVerfuegung;
import ch.admin.zas.rc.IVTaggelderMeldungType.TaggeldPeriode;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.annonces.IJPeriodeAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ebko
 * 
 */
public class IJAnnoncesXmlService {

    private static final Logger LOG = LoggerFactory.getLogger(IJAnnoncesXmlService.class);

    private static final String GRANDE_IJ = "1";
    private static final String PETITE_IJ = "2";
    private static final String ALLOC_INI_TRAVAIL = "3";
    private static final String ALLOC_FRAIS_ASSISTANCE = "4";

    private static IJAnnoncesXmlService instance = new IJAnnoncesXmlService();

    public static IJAnnoncesXmlService getInstance() {

        return instance;
    }

    public IVTaggelderMeldungType getAnnonceXml(IJAnnonce annonce) throws Exception {
        IVTaggelderMeldungType annonceXml = null;
        boolean isCasSpecial = "3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte());

        if (annonce.getCodeApplication().equals("85")) {
            annonceXml = new IVTaggelderMeldungType();
            /*
             * 3 me Révision =================================================
             */

            // Cas spécial pour les annonces de type 3 et 4

            annonceXml.setKasseZweigstelle(annonce.getNoCaisse() + annonce.getNoAgence());
            annonceXml.setBuchungsmonatJahr(retourneXMLGregorianCalendarFromMonth(annonce.getMoisAnneeComptable()));
            annonceXml.setMeldungsInhalt(annonce.getCodeGenreCarte());
            if (!JadeStringUtil.isEmpty(annonce.getPetiteIJ()) && !isCasSpecial) {
                annonceXml.setTaggeldart(annonce.getPetiteIJ());
            }
            annonceXml.setVersichertennummer(JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));
            annonceXml.setVNrEhepartner(JadeStringUtil.removeChar(annonce.getNoAssureConjoint(), '.'));
            if (!JadeStringUtil.isEmpty(annonce.getCodeEtatCivil()) && !isCasSpecial) {
                annonceXml.setZivilstand(Integer.valueOf(annonce.getCodeEtatCivil()).shortValue());
            }
            if (!JadeStringUtil.isEmpty(annonce.getNombreEnfants()) && !isCasSpecial) {
                annonceXml.setAnzahlKinder(Integer.valueOf(annonce.getNombreEnfants()).shortValue());
            } else {
                annonceXml.setAnzahlKinder((short) 0);
            }
            if (!JadeStringUtil.isEmpty(annonce.getRevenuJournalierDeterminant()) && !isCasSpecial) {
                annonceXml.setMassgebendesTageseinkommen(
                        new BigDecimal(testSiNullouZero(annonce.getRevenuJournalierDeterminant())).abs());
            }
            if (!isCasSpecial) {
                annonceXml.setZustaendigeIVStelle(Integer.valueOf(annonce.getOfficeAI()));
            }
            if (!JadeStringUtil.isEmpty(annonce.getCodeGenreReadaptation()) && !isCasSpecial) {
                annonceXml.setEingliederungsart(Integer.valueOf(annonce.getCodeGenreReadaptation()).shortValue());
            }
            if (!JadeStringUtil.isEmpty(annonce.getGarantieAA()) && !isCasSpecial) {
                annonceXml.setUVGarantie(convertIntToBoolean(annonce.getGarantieAA()));
            }
            if (!JadeStringUtil.isEmpty(annonce.getIjReduite()) && !isCasSpecial) {
                annonceXml.setGekuerztesTaggeld(convertIntToBoolean(annonce.getIjReduite()));
            }
            annonceXml.setBesitzstand4IVRevision(convertIntToBoolean(annonce.getDroitAcquis4emeRevision()));
            annonceXml.setNrVerfuegung(resolveNumeroDecision(annonce.getNoDecisionAiCommunication()));

            // Si aucun jour attesté pour 1ère période, on rempli avec les
            // données de la 2ème période
            if (!JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJours())) {
                annonceXml.setErsteTaggeldPeriode(resolvePeriode(annonce.getPeriodeAnnonce1(), annonce));
                if (!annonce.getPeriodeAnnonce2().isNew()
                        || !JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getNombreJours())) {
                    annonceXml.getTaggeldPeriode().add(resolveAutrePeriode(annonce.getPeriodeAnnonce2(), annonce));
                }
            } else {
                annonceXml.setErsteTaggeldPeriode(resolvePeriode(annonce.getPeriodeAnnonce2(), annonce));
            }

        } else {
            /*
             * 4e Révision =================================================
             */
            if (GRANDE_IJ.equals(annonce.getPetiteIJ()) || PETITE_IJ.equals(annonce.getPetiteIJ())) {
                annonceXml = genereAnnoncePetiteGrandeIJ(annonce);
            } else if (ALLOC_INI_TRAVAIL.equals(annonce.getPetiteIJ())) {
                annonceXml = genereAnnonceAIT(annonce);
            } else {
                annonceXml = genereAnnonceAllocAssistance(annonce);
            }
        }

        return annonceXml;
    }

    private IVTaggelderMeldungType genereAnnoncePetiteGrandeIJ(IJAnnonce annonce)
            throws DatatypeConfigurationException, ParseException {
        IVTaggelderMeldungType annonceXml = new IVTaggelderMeldungType();
        annonceXml.setKasseZweigstelle(annonce.getNoCaisse() + annonce.getNoAgence());
        annonceXml.setBuchungsmonatJahr(retourneXMLGregorianCalendarFromMonth(annonce.getMoisAnneeComptable()));
        annonceXml.setMeldungsInhalt(annonce.getCodeGenreCarte());
        annonceXml.setTaggeldart(annonce.getPetiteIJ());
        annonceXml.setVersichertennummer(JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));
        if(!JadeStringUtil.isEmpty(annonce.getNoAssureConjoint())) {
            annonceXml.setVNrEhepartner(JadeStringUtil.removeChar(annonce.getNoAssureConjoint(), '.'));
        }
        annonceXml.setBesitzstand4IVRevision(convertIntToBoolean(annonce.getDroitAcquis4emeRevision()));
        annonceXml.setNrVerfuegung(resolveNumeroDecision(annonce.getNoDecisionAiCommunication()));

        if (!"3".equals(annonce.getCodeGenreCarte()) && !"4".equals(annonce.getCodeGenreCarte())) {
            annonceXml.setZivilstand(Integer.valueOf(annonce.getCodeEtatCivil()).shortValue());
            if (!JadeStringUtil.isEmpty(annonce.getNombreEnfants())){
                annonceXml.setAnzahlKinder(Integer.valueOf(annonce.getNombreEnfants()).shortValue());
            } else {
                annonceXml.setAnzahlKinder((short) 0);
            }
            annonceXml.setZustaendigeIVStelle(Integer.valueOf(annonce.getOfficeAI()));
            annonceXml.setEingliederungsart(Integer.valueOf(annonce.getCodeGenreReadaptation()).shortValue());
            annonceXml.setUVGarantie(convertIntToBoolean(annonce.getGarantieAA()));
            annonceXml.setGekuerztesTaggeld(convertIntToBoolean(annonce.getIjReduite()));

            // Si petite IJ
            if (PETITE_IJ.equals(annonce.getPetiteIJ())) {
                annonceXml.setMassgebendesTageseinkommen(new BigDecimal("0.00"));
            } else {
                annonceXml.setMassgebendesTageseinkommen(
                        new BigDecimal(testSiNullouZero(annonce.getRevenuJournalierDeterminant())).abs());
            }
        }

        // Si aucun jour attesté pour 1ère période, on rempli avec les
        // données de la 2ème période
        if (!JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJours())) {
            annonceXml.setErsteTaggeldPeriode(resolvePeriode(annonce.getPeriodeAnnonce1(), annonce));
            // 2Eme période si elle existe
            if (!annonce.getPeriodeAnnonce2().isNew()
                    || !JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce2().getNombreJours())) {
                annonceXml.getTaggeldPeriode().add(resolveAutrePeriode(annonce.getPeriodeAnnonce2(), annonce));
            }
        } else {
            annonceXml.setErsteTaggeldPeriode(resolvePeriode(annonce.getPeriodeAnnonce2(), annonce));
        }
        return annonceXml;
    }
    
    private IVTaggelderMeldungType genereAnnonceAllocAssistance(IJAnnonce annonce) throws DatatypeConfigurationException, ParseException {
        return genereAnnonceAITouAllocAssitance(annonce, false);
    }
    
    private IVTaggelderMeldungType genereAnnonceAIT(IJAnnonce annonce) throws DatatypeConfigurationException, ParseException {
        return genereAnnonceAITouAllocAssitance(annonce, true);
    }
    

    private IVTaggelderMeldungType genereAnnonceAITouAllocAssitance(IJAnnonce annonce, boolean isAIT)
            throws DatatypeConfigurationException, ParseException {
        IVTaggelderMeldungType annonceXml = new IVTaggelderMeldungType();
        annonceXml.setKasseZweigstelle(annonce.getNoCaisse() + annonce.getNoAgence());
        annonceXml.setBuchungsmonatJahr(retourneXMLGregorianCalendarFromMonth(annonce.getMoisAnneeComptable()));
        annonceXml.setMeldungsInhalt(annonce.getCodeGenreCarte());
        annonceXml.setTaggeldart(annonce.getPetiteIJ());
        annonceXml.setVersichertennummer(JadeStringUtil.removeChar(annonce.getNoAssure(), '.'));
        annonceXml.setZivilstand(Integer.valueOf(annonce.getCodeEtatCivil()).shortValue());
        annonceXml.setZustaendigeIVStelle(Integer.valueOf(annonce.getOfficeAI()));
        annonceXml.setNrVerfuegung(resolveNumeroDecision(annonce.getNoDecisionAiCommunication()));
        
        if(!isAIT) {
            annonceXml.setEingliederungsart(Integer.valueOf(annonce.getCodeGenreReadaptation()).shortValue());
        }

        ErsteTaggeldPeriode periode1Xml = new ErsteTaggeldPeriode();

        // sinon les champs sont à 0
        if (!"3".equals(annonce.getCodeGenreCarte()) && !"4".equals(annonce.getCodeGenreCarte())) {
            annonceXml.setUVGarantie(false);
            annonceXml.setGekuerztesTaggeld(false);
            annonceXml.setAnzahlKinder((short) 0);
            annonceXml.setMassgebendesTageseinkommen(new BigDecimal("0.00"));
            periode1Xml.setAbzugVerpflegungUnterkunft(false);
            periode1Xml.setAnzahlTage(0);
        } else if(isAIT){    // si restitution ou paiement retroactif les champs sont a blanc
            // bz-6043
            if ((annonce.getPeriodeAnnonce1() != null)
                    && !JadeStringUtil.isBlankOrZero((annonce.getPeriodeAnnonce1().getNombreJours()))) {
                periode1Xml.setAnzahlTage(Integer.valueOf(annonce.getPeriodeAnnonce1().getNombreJours()));
            }
        }

        // set a 0
        periode1Xml.setVorzeichenCode(false);
        periode1Xml.setAnspruchKindergeld(false);
        periode1Xml.setBesitzstand5IVRevision(false);
        periode1Xml.setTagesansatz(new BigDecimal("0.00"));
        periode1Xml.setIVTaggeldTotal(new BigDecimal("0.00"));

        // 2Eme période si elle existe, création dans 8G02
        TaggeldPeriode periode2Xml = new TaggeldPeriode();
        periode2Xml.setAnzahlTage(Integer.valueOf(annonce.getPeriodeAnnonce1().getNombreJours()));
        if(isAIT) {
            periode2Xml.setTagesansatz(new BigDecimal(testSiNullouZero(annonce.getPeriodeAnnonce1().getTauxJournalier())));
        } else {
            periode2Xml.setTagesansatz(new BigDecimal("0.00"));
            periode2Xml.setIVTaggeldTotal(new BigDecimal("0.00"));
        }
        
        if (!JadeStringUtil.isIntegerEmpty(annonce.getPeriodeAnnonce1().getNombreJoursInterruption())) {
            periode2Xml.setAnzahlTageEingliederungsunterbruchMitTaggeld(
                    Integer.valueOf(annonce.getPeriodeAnnonce1().getNombreJoursInterruption()));
            periode2Xml.setGrundDerUnterbrechung(
                    Integer.valueOf(annonce.getPeriodeAnnonce1().getCodeMotifInterruption()).shortValue());
        }
        periode2Xml.setAuszahlungTaggeld(Integer.valueOf(annonce.getPeriodeAnnonce1().getVersementIJ()).shortValue());
        periode2Xml.setVorzeichenCode("1".equals(annonce.getPeriodeAnnonce1().getCodeValeurTotalIJ()));
        periode2Xml.setPeriodeBeginn(retourneXMLGregorianCalendar(annonce.getPeriodeAnnonce1().getPeriodeDe()));
        periode2Xml.setPeriodeEnde(retourneXMLGregorianCalendar(annonce.getPeriodeAnnonce1().getPeriodeA()));
        
        if(isAIT) {
            periode2Xml.setBetragEinarbeitungszuschuss(new BigDecimal(annonce.getPeriodeAnnonce1().getMontantAit()));
        } else {
            periode2Xml.setBetragEntschaedigungBetreuungskosten(new BigDecimal(annonce.getPeriodeAnnonce1().getMontantAllocAssistance()));
        }

        // si restitution ou paiement retroactif les champs sont a blanc
        if ("3".equals(annonce.getCodeGenreCarte()) || "4".equals(annonce.getCodeGenreCarte())) {
            periode2Xml.setIVTaggeldTotal(new BigDecimal("0.00"));
            if(isAIT) {
                periode2Xml.setBetragEntschaedigungBetreuungskosten(new BigDecimal("0.00"));
            }
        } else {
            periode2Xml.setAbzugVerpflegungUnterkunft(false);
        }

        // set a 0
        periode2Xml.setAnspruchKindergeld(false);
        periode2Xml.setBesitzstand5IVRevision(false);
        
        annonceXml.setErsteTaggeldPeriode(periode1Xml);
        annonceXml.getTaggeldPeriode().add(periode2Xml);

        return annonceXml;
    }
    
    private NrVerfuegung resolveNumeroDecision(String numero) {
        NrVerfuegung num = new NrVerfuegung();
        String intNum = numero.replaceFirst("^0*", "");
        num.setZustaendigeIVStelle(Integer.valueOf(intNum.substring(0, 3)));
        num.setElfstelligeNrVerfuegung(Long.valueOf(intNum.substring(3, 14)));
        num.setVersichertennummer(intNum.substring(14));
        return num;
    }

    private ErsteTaggeldPeriode resolvePeriode(IJPeriodeAnnonce periode, IJAnnonce annonce)
            throws ParseException, DatatypeConfigurationException {
        ErsteTaggeldPeriode periodeXml = new ErsteTaggeldPeriode();
        if (!"3".equals(annonce.getCodeGenreCarte()) && !"4".equals(annonce.getCodeGenreCarte())) {
            periodeXml.setAbzugVerpflegungUnterkunft(convertIntToBoolean(periode.getDeductionNourritureLogement()));
            if (!JadeStringUtil.isIntegerEmpty(periode.getNombreJoursInterruption())) {
                periodeXml.setAnzahlTageEingliederungsunterbruchMitTaggeld(
                        Integer.valueOf(periode.getNombreJoursInterruption()));
                if (!JadeStringUtil.isIntegerEmpty(periode.getCodeMotifInterruption())) {
                    periodeXml.setGrundDerUnterbrechung(Integer.valueOf(periode.getCodeMotifInterruption()).shortValue());
                }
            }
            if (!JadeStringUtil.isIntegerEmpty(periode.getVersementIJ())) {
                periodeXml.setAuszahlungTaggeld(Integer.valueOf(periode.getVersementIJ()).shortValue());
            }
        }

        periodeXml.setAnzahlTage(Integer.valueOf(periode.getNombreJours()));
        periodeXml.setTagesansatz(new BigDecimal(testSiNullouZero(periode.getTauxJournalier())).abs());
        periodeXml.setIVTaggeldTotal(new BigDecimal(testSiNullouZero(periode.getTotalIJ())).abs());
        periodeXml.setVorzeichenCode("1".equals(periode.getCodeValeurTotalIJ()));
        periodeXml.setPeriodeBeginn(retourneXMLGregorianCalendar(periode.getPeriodeDe()));
        periodeXml.setPeriodeEnde(retourneXMLGregorianCalendar(periode.getPeriodeA()));
        periodeXml.setAnspruchKindergeld(convertIntToBoolean(periode.getDroitPrestationPourEnfant()));
        periodeXml.setBesitzstand5IVRevision(convertIntToBoolean(periode.getGarantieDroitAcquis5emeRevision()));
        return periodeXml;
    }

    private TaggeldPeriode resolveAutrePeriode(IJPeriodeAnnonce periode, IJAnnonce annonce)
            throws ParseException, DatatypeConfigurationException {
        TaggeldPeriode periodeXml = new TaggeldPeriode();
        if (!"3".equals(annonce.getCodeGenreCarte()) && !"4".equals(annonce.getCodeGenreCarte())) {
            periodeXml.setAbzugVerpflegungUnterkunft(convertIntToBoolean(periode.getDeductionNourritureLogement()));
            if (!JadeStringUtil.isIntegerEmpty(periode.getNombreJoursInterruption())) {
                periodeXml
                        .setAnzahlTageEingliederungsunterbruchMitTaggeld(Integer.valueOf(periode.getNombreJoursInterruption()));
                if (!JadeStringUtil.isIntegerEmpty(periode.getCodeMotifInterruption())) {
                    periodeXml.setGrundDerUnterbrechung(Integer.valueOf(periode.getCodeMotifInterruption()).shortValue());
                }
            }
            if (!JadeStringUtil.isIntegerEmpty(periode.getVersementIJ())) {
                periodeXml.setAuszahlungTaggeld(Integer.valueOf(periode.getVersementIJ()).shortValue());
            }
        }
        periodeXml.setAnzahlTage(Integer.valueOf(periode.getNombreJours()));
        periodeXml.setTagesansatz(new BigDecimal(testSiNullouZero(periode.getTauxJournalier())).abs());
        periodeXml.setIVTaggeldTotal(new BigDecimal(testSiNullouZero(periode.getTotalIJ())).abs());
        periodeXml.setVorzeichenCode("1".equals(periode.getCodeValeurTotalIJ()));
        periodeXml.setPeriodeBeginn(retourneXMLGregorianCalendar(periode.getPeriodeDe()));
        periodeXml.setPeriodeEnde(retourneXMLGregorianCalendar(periode.getPeriodeA()));
        periodeXml.setAnspruchKindergeld(convertIntToBoolean(periode.getDroitPrestationPourEnfant()));
        periodeXml.setBesitzstand5IVRevision(convertIntToBoolean(periode.getGarantieDroitAcquis5emeRevision()));

        return periodeXml;
    }

    protected BigDecimal betragEntschaedigungBetreuungskosten;
    protected BigDecimal betragEinarbeitungszuschuss;

    protected String testSiNullouZero(String valeur) {
        if (JadeStringUtil.isBlank(valeur)) {
            return "0.00";
        } else {
            return valeur;
        }
    }

    protected Boolean convertIntToBoolean(String valeurString) {
        if ("1".equals(valeurString)) {
            return true;
        } else {
            return false;
        }
    }

    public XMLGregorianCalendar retourneXMLGregorianCalendarFromMonth(String jaDate)
            throws DatatypeConfigurationException, ParseException {
        final DateFormat format = new SimpleDateFormat("MM.yyyy");
        final java.util.Date dDate = format.parse(jaDate);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);
        XMLGregorianCalendar xmlGrogerianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        xmlGrogerianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED );

        return xmlGrogerianCalendar;
    }

    /**
     * 
     * @param jaDate dd.MM.yyyy
     * @return
     * @throws ParseException
     * @throws DatatypeConfigurationException
     * @throws Exception
     */
    public XMLGregorianCalendar retourneXMLGregorianCalendar(String jaDate)
            throws ParseException, DatatypeConfigurationException {

        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final java.util.Date dDate = format.parse(jaDate);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);
        XMLGregorianCalendar xmlGrogerianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        xmlGrogerianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED );

        return xmlGrogerianCalendar;

    }
    
}
