package globaz.corvus.annonce.service;

import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;
import ch.admin.zas.rc.FamilienAngehoerigeType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslWeakType;
import ch.admin.zas.rc.RentenaufschubType;
import ch.admin.zas.rc.RentenaufschubWeakType;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.SkalaBerechnungWeakType;
import ch.globaz.common.properties.CommonProperties;

public abstract class REAbstractAnnonceXmlService {

    protected ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();

    /**
     * Valide une annonce d'augmentation avec ANAKIN <br/>
     * Lance une exception si une erreur de parsing survient
     * 
     * @param enregistrement01
     * @param enregistrement02
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    void parseAugmentationAvecAnakin(REAnnoncesAbstractLevel1A enregistrement01,
            REAnnoncesAbstractLevel1A enregistrement02, BSession session, String forMoisAnneeComptable)
            throws Exception {

        Enumeration erreurs = REAnakinParser.getInstance().parse(session, enregistrement01, enregistrement02,
                forMoisAnneeComptable);

        StringBuilder stringBuilder = new StringBuilder();
        while ((erreurs != null) && erreurs.hasMoreElements()) {
            AnnonceErreur erreur = (AnnonceErreur) erreurs.nextElement();
            stringBuilder.append(erreur.getMessage()).append("\n");
        }
        if (stringBuilder.length() > 0) {
            throw new Exception(stringBuilder.toString());
        }

    }

    void parseDiminutionAvecAnakin(REAnnoncesAbstractLevel1A enregistrement01, BSession session,
            String forMoisAnneeComptable) throws Exception {
        parseAugmentationAvecAnakin(enregistrement01, null, session, forMoisAnneeComptable);
    }

    /**
     * 
     * @param jaDate dd.mm.yyyy
     * @return
     * @throws Exception
     */
    public XMLGregorianCalendar retourneXMLGregorianCalendar(String jaDate) throws Exception {

        final DateFormat format = new SimpleDateFormat("dd.mm.yyyy");
        final java.util.Date dDate = format.parse(jaDate);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
    }

    protected RRLeistungsberechtigtePersonAuslType rempliRRLeistungsberechtigtePersonAuslType(
            REAnnoncesAbstractLevel2A enr01, REAnnoncesAbstractLevel2A enr02) {
        RRLeistungsberechtigtePersonAuslType personne = factoryType.createRRLeistungsberechtigtePersonAuslType();
        personne.setVersichertennummer(enr01.getNoAssAyantDroit());
        personne.setIstFluechtling(convertIntToBoolean(enr01.getIsRefugie()));
        FamilienAngehoerigeType membresDeLaFamille = factoryType.createFamilienAngehoerigeType();
        if (!JadeStringUtil.isBlankOrZero(enr01.getPremierNoAssComplementaire())) {
            membresDeLaFamille.getVNr1Ergaenzend().add(enr01.getPremierNoAssComplementaire());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getSecondNoAssComplementaire())) {
            membresDeLaFamille.getVNr2Ergaenzend().add(enr01.getSecondNoAssComplementaire());
        }
        personne.setWohnkantonStaat(new Integer(enr01.getCantonEtatDomicile()).toString());
        personne.setFamilienAngehoerige(membresDeLaFamille);
        personne.setZivilstand(new Integer(enr01.getEtatCivil()).shortValue());
        return personne;

    }

    /**
     * RRLeistungsberechtigtePersonAusl<b>Weak</b>Type
     * 
     * @param enr01
     * @param enr02
     * @return
     */
    protected RRLeistungsberechtigtePersonAuslWeakType rempliRRLeistungsberechtigtePersonAuslWeakType(
            REAnnoncesAbstractLevel2A enr01, REAnnoncesAbstractLevel2A enr02) {
        RRLeistungsberechtigtePersonAuslWeakType personne = factoryType
                .createRRLeistungsberechtigtePersonAuslWeakType();
        personne.setVersichertennummer(enr01.getNoAssAyantDroit());
        personne.setIstFluechtling(convertIntToBoolean(enr01.getIsRefugie()));
        FamilienAngehoerigeType membresDeLaFamille = factoryType.createFamilienAngehoerigeType();
        if (!JadeStringUtil.isBlankOrZero(enr01.getPremierNoAssComplementaire())) {
            membresDeLaFamille.getVNr1Ergaenzend().add(enr01.getPremierNoAssComplementaire());
        }
        if (!JadeStringUtil.isBlankOrZero(enr01.getSecondNoAssComplementaire())) {
            membresDeLaFamille.getVNr2Ergaenzend().add(enr01.getSecondNoAssComplementaire());
        }
        personne.setWohnkantonStaat(new Integer(enr01.getCantonEtatDomicile()).toString());
        personne.setFamilienAngehoerige(membresDeLaFamille);
        personne.setZivilstand(new Integer(enr01.getEtatCivil()).shortValue());
        return personne;

    }

    public XMLGregorianCalendar retourneXMLGregorianCalendarFromMonth(String dateMmYy) throws Exception {

        GregorianCalendar gregory = null;
        if (new Integer(dateMmYy.substring(2)) > 48) {
            gregory = new GregorianCalendar(new Integer(dateMmYy.substring(2)) + 1900, new Integer(dateMmYy.substring(
                    0, 2)), 0);
        } else {
            gregory = new GregorianCalendar(new Integer(dateMmYy.substring(2)) + 2000, new Integer(dateMmYy.substring(
                    0, 2)), 0);
        }

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
    }

    public XMLGregorianCalendar retourneXMLGregorianCalendarFromYear(String dateYy) throws Exception {

        GregorianCalendar gregory = null;
        if (new Integer(dateYy) > 48) {
            gregory = new GregorianCalendar(new Integer(dateYy) + 1900, 0, 0);
        } else {
            gregory = new GregorianCalendar(new Integer(dateYy) + 2000, 0, 0);
        }

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
    }

    protected String testSiNullouZero(String valeur) {
        if (JadeStringUtil.isBlankOrZero(valeur)) {
            return "0";
        } else {
            return valeur;
        }
    }

    protected String retourneCaisseAgence() throws Exception {
        return CommonProperties.KEY_NO_CAISSE.getValue() + CommonProperties.NUMERO_AGENCE.getValue();
    }

    protected Long retourneNoDAnnonceSur6Position(String noAnnonce) {
        if (noAnnonce.length() > 6) {
            return new Long(noAnnonce.substring(noAnnonce.length() - 6));
        }
        return new Long(noAnnonce);
    }

    protected Boolean convertIntToBoolean(String valeurString) {
        if ("1".equals(valeurString)) {
            return true;
        } else {
            return false;
        }
    }

    protected SkalaBerechnungType rempliScalaBerechnungTyp(REAnnoncesAbstractLevel2A enr02) {
        SkalaBerechnungType echelleCalcul = factoryType.createSkalaBerechnungType();
        echelleCalcul.setSkala(new Integer(enr02.getEchelleRente()).shortValue());
        echelleCalcul.setBeitragsdauerVor1973(convertAAMMtoBigDecimal(enr02.getDureeCoEchelleRenteAv73()));
        echelleCalcul.setBeitragsdauerAb1973(convertAAMMtoBigDecimal(enr02.getDureeCoEchelleRenteDes73()));
        echelleCalcul.setAnrechnungVor1973FehlenderBeitragsmonate(new Integer(testSiNullouZero(enr02
                .getDureeCotManquante48_72())));
        echelleCalcul.setAnrechnungAb1973Bis1978FehlenderBeitragsmonate(new Integer(testSiNullouZero(enr02
                .getDureeCotManquante73_78())));
        echelleCalcul.setBeitragsjahreJahrgang(new Integer(testSiNullouZero(enr02.getAnneeCotClasseAge())));
        return echelleCalcul;
    }

    protected SkalaBerechnungWeakType rempliScalaBerechnungWeakTyp(REAnnoncesAbstractLevel2A enr02) {
        SkalaBerechnungWeakType echelleCalcul = factoryType.createSkalaBerechnungWeakType();
        if (!JadeStringUtil.isBlankOrZero(enr02.getEchelleRente())) {
            echelleCalcul.setSkala(new Integer(enr02.getEchelleRente()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeCoEchelleRenteAv73())) {
            echelleCalcul.setBeitragsdauerVor1973(convertAAMMtoBigDecimal(enr02.getDureeCoEchelleRenteAv73()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeCoEchelleRenteDes73())) {
            echelleCalcul.setBeitragsdauerAb1973(convertAAMMtoBigDecimal(enr02.getDureeCoEchelleRenteDes73()));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeCotManquante48_72())) {
            echelleCalcul.setAnrechnungVor1973FehlenderBeitragsmonate(new Integer(testSiNullouZero(enr02
                    .getDureeCotManquante48_72())));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getDureeCotManquante73_78())) {
            echelleCalcul.setAnrechnungAb1973Bis1978FehlenderBeitragsmonate(new Integer(testSiNullouZero(enr02
                    .getDureeCotManquante73_78())));
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getAnneeCotClasseAge())) {
            echelleCalcul.setBeitragsjahreJahrgang(new Integer(testSiNullouZero(enr02.getAnneeCotClasseAge())));
        }
        return echelleCalcul;
    }

    protected BigDecimal convertAAMMtoBigDecimal(String strAAMM) {
        return BigDecimal.valueOf(Long.valueOf(testSiNullouZero(strAAMM)), 2).setScale(2);
    }

    /**
     * Méthode qui remplit les cas spéciaux
     * 
     * @param enr01
     * @param enr02
     * @param desc
     */
    protected List<Short> rempliCasSpecial(REAnnoncesAbstractLevel2A enr01, REAnnoncesAbstractLevel2A enr02) {
        List<Short> listCas = new ArrayList<Short>();
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial1())) {
            listCas.add(new Integer(enr02.getCasSpecial1()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial2())) {
            listCas.add(new Integer(enr02.getCasSpecial2()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial3())) {
            listCas.add(new Integer(enr02.getCasSpecial3()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial4())) {
            listCas.add(new Integer(enr02.getCasSpecial4()).shortValue());
        }
        if (!JadeStringUtil.isBlankOrZero(enr02.getCasSpecial5())) {
            listCas.add(new Integer(enr02.getCasSpecial5()).shortValue());
        }
        return listCas;
    }

    /**
     * Méthode qui retourne un objet qui rempli les ajournements
     * 
     * @param enr01
     * @param enr02
     * @return un ajournement
     * @throws Exception
     */
    protected RentenaufschubType rempliRentenaufschubType(REAnnoncesAbstractLevel2A enr01,
            REAnnoncesAbstractLevel2A enr02) throws Exception {
        RentenaufschubType ajournement = factoryType.createRentenaufschubType();
        ajournement.setAbrufdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateRevocationAjournement()));
        ajournement.setAufschubsdauer(new BigDecimal(testSiNullouZero(enr02.getDureeAjournement())));
        ajournement.setAufschubszuschlag(new BigDecimal(testSiNullouZero(enr02.getSupplementAjournement())));
        return ajournement;
    }

    protected RentenaufschubWeakType rempliRentenaufschubTypeWeak(REAnnoncesAbstractLevel2A enr01,
            REAnnoncesAbstractLevel2A enr02) throws Exception {
        RentenaufschubWeakType ajournement = factoryType.createRentenaufschubWeakType();
        ajournement.setAbrufdatum(retourneXMLGregorianCalendarFromMonth(enr02.getDateRevocationAjournement()));
        ajournement.setAufschubsdauer(new BigDecimal(testSiNullouZero(enr02.getDureeAjournement())));
        ajournement.setAufschubszuschlag(new BigDecimal(testSiNullouZero(enr02.getSupplementAjournement())));
        return ajournement;
    }
}
