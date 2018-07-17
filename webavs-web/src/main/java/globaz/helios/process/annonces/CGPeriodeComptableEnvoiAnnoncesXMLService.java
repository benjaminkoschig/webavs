package globaz.helios.process.annonces;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.zas.rc.BetriebsRechnungType;
import ch.admin.zas.rc.BilanzVerwaltungsRechnungType;
import ch.admin.zas.rc.JahresUmsatzBilanzType;
import ch.admin.zas.rc.MonatsRekapitulationBeitragsBuchhaltungType;
import ch.admin.zas.rc.ObjectFactory;
import ch.globaz.common.exceptions.ValidationException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.application.CGApplication;
import globaz.helios.db.avs.CGExtendedCompteOfas;
import globaz.helios.db.avs.CGExtendedCompteOfasManager;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAff;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAffManager;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.process.helper.CGHelperReleveAVS;
import globaz.helios.process.helper.CGMontantHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeException;

/**
 * @author ebko
 * 
 */

public class CGPeriodeComptableEnvoiAnnoncesXMLService {

    private BSession session = null;
    private BTransaction transaction = null;
    private CGPeriodeComptable periode = null;
    private CGExerciceComptable exercice = null;
    private CGApplication application = null;

    private List<Object> listXml;
    private ObjectFactory factoryType = new ObjectFactory();

    public CGPeriodeComptableEnvoiAnnoncesXMLService(BSession session, BTransaction transaction,
            CGPeriodeComptable periode, CGExerciceComptable exercice, List<Object> listXml, CGApplication application) {
        this.periode = periode;
        this.exercice = exercice;
        this.session = session;
        this.transaction = transaction;
        this.listXml = listXml;
        this.application = application;
    }

    /**
     * @param count
     * @param nbrComptes
     * @param listResultAnnonces8B
     * @throws Exception
     */
    public void construireAnnonces8B(List<CGMontantHelper> listResultAnnonces8B) throws Exception {
        BilanzVerwaltungsRechnungType type = new BilanzVerwaltungsRechnungType();
        type.setKasseZweigstelle(getNumCaisseAgence());
        type.setRechnungsperiode(Integer.parseInt(periode.getCode()));
        type.setRechnungsjahr(retourneXMLGregorianCalendar(exercice.getDateDebut()));

        for (CGMontantHelper result : listResultAnnonces8B) {

            BigInteger numCompte = getNumeroCompte(result.idExterne);
            BigInteger codeValeur;
            BigDecimal montant;

            if (result.actif != null) {
                codeValeur = getCodeValeur(result.actif);
                montant = getMontant(result.actif);
            } else if (result.passif != null) {
                codeValeur = getCodeValeur(result.passif);
                montant = getMontant(result.passif);
            } else {
                codeValeur = new BigInteger("0");
                montant = null;
            }

            JAXBElement<BigInteger> compteXml = factoryType.createBilanzVerwaltungsRechnungTypeKontonummer(numCompte);
            JAXBElement<BigInteger> codeValeurXml = factoryType
                    .createBilanzVerwaltungsRechnungTypeVorzeichenCode(codeValeur);
            JAXBElement<BigDecimal> montantXml = factoryType.createBilanzVerwaltungsRechnungTypeBetrag(montant);

            type.getKontonummerAndVorzeichenCodeAndBetrag().add(compteXml);
            type.getKontonummerAndVorzeichenCodeAndBetrag().add(codeValeurXml);
            type.getKontonummerAndVorzeichenCodeAndBetrag().add(montantXml);

        }

        if (!listResultAnnonces8B.isEmpty()) {
            try {
                CGPeriodeComptableXMLService.getInstance().validateUnitMessage(type);
            } catch (ValidationException e) {
                throw new JadeException(e.getMessageErreurDeValidation().toString());
            }
            listXml.add(type);
        }
    }

    public void preparerAnnoncesBalanceAnnuelleMvt(CGExtendedCompteOfasManager ds,
            CGHelperReleveAVS traitementReleveAvsHelper, boolean isComptaDefinitive) throws Exception {
        // 8E
        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;
        CGExtendedCompteOfas extendedCompteOfas = null;
        int nbrComptes = 0;

        JahresUmsatzBilanzType type = new JahresUmsatzBilanzType();
        type.setKasseZweigstelle(getNumCaisseAgence());
        type.setRechnungsjahr(retourneXMLGregorianCalendar(exercice.getDateDebut()));

        statement = ds.cursorOpen(transaction);
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantMvtAnnuel(transaction, session,
                    extendedCompteOfas, exercice.getIdMandat(), exercice.getIdExerciceComptable(),
                    periode.getIdPeriodeComptable(), !isComptaDefinitive);

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if (((result.passif != null) && !result.passif.isNegative())
                    || ((result.actif != null) && !result.actif.isNegative())) {
                nbrComptes++;

                BigInteger numCompte = getNumeroCompte(extendedCompteOfas.getIdExterne());
                BigDecimal debiteur = getMontant(result.actif);
                BigDecimal creancier = getMontant(result.passif);

                JAXBElement<BigInteger> compteXml = factoryType.createJahresUmsatzBilanzTypeKontonummer(numCompte);
                JAXBElement<BigDecimal> debiteurXml = factoryType.createJahresUmsatzBilanzTypeBetragSoll(debiteur);
                JAXBElement<BigDecimal> creancierXml = factoryType.createJahresUmsatzBilanzTypeBetragHaben(creancier);

                type.getKontonummerAndBetragSollAndBetragHaben().add(compteXml);
                type.getKontonummerAndBetragSollAndBetragHaben().add(debiteurXml);
                type.getKontonummerAndBetragSollAndBetragHaben().add(creancierXml);

            }
        }

        ds.cursorClose(statement);

        // Annonce avec compte 1 minimun doit être envoyée
        if (nbrComptes > 0) {
            try {
                CGPeriodeComptableXMLService.getInstance().validateUnitMessage(type);
            } catch (ValidationException e) {
                throw new JadeException(e.getMessageErreurDeValidation().toString());
            }
            listXml.add(type);
        }
    }

    public void preparerAnnoncesComptesAffilie() throws Exception {
        // 8C
        MonatsRekapitulationBeitragsBuchhaltungType type = new MonatsRekapitulationBeitragsBuchhaltungType();
        type.setKasseZweigstelle(getNumCaisseAgence());
        type.setRechnungsperiode(Integer.parseInt(periode.getCode()));
        type.setRechnungsjahr(retourneXMLGregorianCalendar(exercice.getDateDebut()));

        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;

        CGExtendedContrePartieCpteAffManager contrePartieMgr = new CGExtendedContrePartieCpteAffManager();
        contrePartieMgr.setSession(session);
        contrePartieMgr.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        contrePartieMgr.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        contrePartieMgr.setForIdMandat(exercice.getIdMandat());
        contrePartieMgr.setForIsActive(true);
        contrePartieMgr.setForIsProvisoire(false);

        int nbrComptes = 0;

        CGExtendedContrePartieCpteAff extContePart = null;
        statement = contrePartieMgr.cursorOpen(transaction);
        while ((extContePart = (CGExtendedContrePartieCpteAff) contrePartieMgr.cursorReadNext(statement)) != null) {

            String sMontant = extContePart.getMontant();
            FWCurrency montantCurrency = new FWCurrency(sMontant);

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if (!montantCurrency.isZero()) {
                nbrComptes++;

                BigInteger numCompte = getNumeroCompte(extContePart.getNumeroCompteOfas());
                BigInteger codeValeur = getCodeValeur(montantCurrency);
                BigDecimal montant = getMontant(montantCurrency);

                JAXBElement<BigInteger> compteXml = factoryType
                        .createMonatsRekapitulationBeitragsBuchhaltungTypeKontonummer(numCompte);
                JAXBElement<BigInteger> codeValeurXml = factoryType
                        .createMonatsRekapitulationBeitragsBuchhaltungTypeVorzeichenCode(codeValeur);
                JAXBElement<BigDecimal> montantXml = factoryType
                        .createMonatsRekapitulationBeitragsBuchhaltungTypeBetrag(montant);

                type.getKontonummerAndVorzeichenCodeAndBetrag().add(compteXml);
                type.getKontonummerAndVorzeichenCodeAndBetrag().add(codeValeurXml);
                type.getKontonummerAndVorzeichenCodeAndBetrag().add(montantXml);

            }
        }

        contrePartieMgr.cursorClose(statement);

        if (nbrComptes > 0) {

            try {
                CGPeriodeComptableXMLService.getInstance().validateUnitMessage(type);
            } catch (ValidationException e) {
                throw new JadeException(e.getMessageErreurDeValidation().toString());
            }
            listXml.add(type);
        }
    }

    public void preparerAnnoncesComptesExploitation(CGHelperReleveAVS traitementReleveAvsHelper,
            boolean isComptaDefinitive) throws Exception {
        // 8A
        BetriebsRechnungType type = new BetriebsRechnungType();
        type.setKasseZweigstelle(getNumCaisseAgence());
        type.setRechnungsperiode(Integer.parseInt(periode.getCode()));
        type.setRechnungsjahr(retourneXMLGregorianCalendar(exercice.getDateDebut()));

        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;
        CGExtendedCompteOfas extendedCompteOfas = null;
        CGExtendedCompteOfasManager ds = new CGExtendedCompteOfasManager();
        ds.setSession(session);
        ds.setForIdMandat(exercice.getIdMandat());
        ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_EXPLOITATION);

        int nbrComptes = 0;

        statement = ds.cursorOpen(transaction);
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantComptesExploitation(transaction, session,
                    extendedCompteOfas, exercice.getIdMandat(), exercice.getIdExerciceComptable(),
                    periode.getIdPeriodeComptable(), !isComptaDefinitive);

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if (((result.passif != null) && !result.passif.isNegative())
                    || ((result.actif != null) && !result.actif.isNegative())
                    || ((result.soldeCumule != null) && (!result.soldeCumule.isZero()))) {
                nbrComptes++;

                BigInteger numCompte = getNumeroCompte(extendedCompteOfas.getIdExterne());
                BigDecimal passif = getMontant(result.passif);
                BigDecimal actif = getMontant(result.actif);
                BigDecimal soldeCumule = getMontant(result.soldeCumule);

                JAXBElement<BigInteger> compteXml = factoryType.createBetriebsRechnungTypeKontonummer(numCompte);
                JAXBElement<BigDecimal> actifXml = factoryType.createBetriebsRechnungTypeBetragSoll(actif);
                JAXBElement<BigDecimal> passifXml = factoryType.createBetriebsRechnungTypeBetragHaben(passif);
                JAXBElement<BigDecimal> soldeXml = factoryType.createBetriebsRechnungTypeBetragSaldo(soldeCumule);

                type.getKontonummerAndBetragSollAndBetragHaben().add(compteXml);
                type.getKontonummerAndBetragSollAndBetragHaben().add(actifXml);
                type.getKontonummerAndBetragSollAndBetragHaben().add(passifXml);
                type.getKontonummerAndBetragSollAndBetragHaben().add(soldeXml);

            }
        }

        ds.cursorClose(statement);

        // Annonce avec au moins 1 compte doit être envoyée
        if (nbrComptes > 0) {
            try {
                CGPeriodeComptableXMLService.getInstance().validateUnitMessage(type);
            } catch (ValidationException e) {
                throw new JadeException(e.getMessageErreurDeValidation().toString());
            }
            listXml.add(type);
        }
    }

    private BigInteger getNumeroCompte(String compte) {
        return new BigInteger(compte.substring(0, 3) + compte.substring(4, 8));
    }

    private BigDecimal getMontant(FWCurrency number) {
        if (number != null) {
            return new BigDecimal(number.toString()).abs();
        } else {
            return new BigDecimal("0.00");
        }
    }

    private BigInteger getCodeValeur(FWCurrency number) {
        if (number.isPositive() || number.isZero()) {
            return new BigInteger("0");
        } else {
            return new BigInteger("1");
        }
    }

    private String getNumCaisseAgence() throws Exception {
        String numCaisseAgence = "";

        if (!JadeStringUtil.isIntegerEmpty(periode.getExerciceComptable().getMandat().getNoCaisse())) {
            numCaisseAgence = periode.getExerciceComptable().getMandat().getNoCaisse()
                    + periode.getExerciceComptable().getMandat().getNoAgence();
        } else {
            numCaisseAgence = CaisseHelperFactory.getInstance().getNoCaisse(application)
                    + CaisseHelperFactory.getInstance().getNoAgence(application);
        }
        return numCaisseAgence;
    }

    private XMLGregorianCalendar retourneXMLGregorianCalendar(String jaDate)
            throws DatatypeConfigurationException, ParseException {
        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final java.util.Date dDate = format.parse(jaDate);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);
        XMLGregorianCalendar xmlGrogerianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        xmlGrogerianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        return xmlGrogerianCalendar;
    }

}
