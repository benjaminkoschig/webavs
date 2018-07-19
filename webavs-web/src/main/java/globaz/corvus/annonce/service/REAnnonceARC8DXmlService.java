package globaz.corvus.annonce.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.zas.rc.MonatsRekapitulationRentenType;
import ch.admin.zas.rc.ObjectFactory;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.jade.client.util.JadeStringUtil;

public class REAnnonceARC8DXmlService {

    private static REAnnonceARC8DXmlService instance = new REAnnonceARC8DXmlService();

    public static REAnnonceARC8DXmlService getInstance() {
        return instance;
    }

    private ObjectFactory factoryType = new ObjectFactory();

    public MonatsRekapitulationRentenType getAnnonceXml(REDetailRecapMensuelleViewBean recap, String noCaisseAgence)
            throws DatatypeConfigurationException, ParseException {

        MonatsRekapitulationRentenType type = new MonatsRekapitulationRentenType();
        type.setKasseZweigstelle(noCaisseAgence);
        type.setRechnungsperiode(Integer.valueOf(recap.getDateRapport().substring(0, 2)));
        type.setRechnungsjahr(retourneXMLGregorianCalendar(recap.getDateRapport().substring(3, 7)));

        addElement(type, recap.getElem500001());
        addElement(type, recap.getElem500002());
        addElement(type, recap.getElem500003());
        addElement(type, recap.getElem500004());
        addElement(type, recap.getElem500005());
        addElement(type, recap.getElem500006());
        addElement(type, recap.getElem500007());
        addElement(type, recap.getElem500099());

        addElement(type, recap.getElem501001());
        addElement(type, recap.getElem501002());
        addElement(type, recap.getElem501003());
        addElement(type, recap.getElem501004());
        addElement(type, recap.getElem501005());
        addElement(type, recap.getElem501006());
        addElement(type, recap.getElem501007());
        addElement(type, recap.getElem501099());

        addElement(type, recap.getElem503001());
        addElement(type, recap.getElem503002());
        addElement(type, recap.getElem503003());
        addElement(type, recap.getElem503004());
        addElement(type, recap.getElem503005());
        addElement(type, recap.getElem503007());
        addElement(type, recap.getElem503099());

        addElement(type, recap.getElem510001());
        addElement(type, recap.getElem510002());
        addElement(type, recap.getElem510003());
        addElement(type, recap.getElem510004());
        addElement(type, recap.getElem510005());
        addElement(type, recap.getElem510007());
        addElement(type, recap.getElem510099());

        addElement(type, recap.getElem511001());
        addElement(type, recap.getElem511002());
        addElement(type, recap.getElem511003());
        addElement(type, recap.getElem511004());
        addElement(type, recap.getElem511005());
        addElement(type, recap.getElem511007());
        addElement(type, recap.getElem511099());

        addElement(type, recap.getElem513001());
        addElement(type, recap.getElem513002());
        addElement(type, recap.getElem513003());
        addElement(type, recap.getElem513004());
        addElement(type, recap.getElem513005());
        addElement(type, recap.getElem513007());
        addElement(type, recap.getElem513099());

        return type;
    }

    private void addElement(MonatsRekapitulationRentenType type, RERecapElement recap) {
        JAXBElement<BigInteger> rubrique = factoryType.createMonatsRekapitulationRentenTypeRubriknummer(getBigInt(recap.getCodeRecap()));
        JAXBElement<BigInteger> code = factoryType.createMonatsRekapitulationRentenTypeVorzeichenCode(getCodeValeur(recap.getCodeRecap()));
        JAXBElement<BigDecimal> montant = factoryType.createMonatsRekapitulationRentenTypeBetrag(getMontant(recap.getMontant()));
        type.getRubriknummerAndVorzeichenCodeAndBetrag().add(rubrique);
        type.getRubriknummerAndVorzeichenCodeAndBetrag().add(code);
        type.getRubriknummerAndVorzeichenCodeAndBetrag().add(montant);
    }

    private BigInteger getBigInt(String value) {
        return new BigInteger(value);
    }

    private BigInteger getCodeValeur(String codeRecap) {

        if (codeRecap.endsWith("7") || codeRecap.endsWith("4")) {
            return BigInteger.valueOf(1);
        } else {
            return BigInteger.valueOf(0);
        }
    }

    private BigDecimal getMontant(String montant) {
        if (!JadeStringUtil.isEmpty(montant)) {
            String montantDec = JadeStringUtil.removeChar(JadeStringUtil.removeChar(montant, '\''), '-');
            return new BigDecimal(montantDec).abs();
        } else {
            return new BigDecimal("0.00");
        }
    }

    private XMLGregorianCalendar retourneXMLGregorianCalendar(String jaDate)
            throws DatatypeConfigurationException, ParseException {
        final DateFormat format = new SimpleDateFormat("yyyy");
        final java.util.Date dDate = format.parse(jaDate);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);
        XMLGregorianCalendar xmlGrogerianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        xmlGrogerianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        return xmlGrogerianCalendar;
    }

}
