package globaz.corvus.annonce.service;

import acor.ch.admin.zas.rc.annonces.rente.rc.ListeFolgerecordNrType;
import acor.ch.admin.zas.rc.annonces.rente.rc.VAIKMeldungKassenWechselType;
import ch.horizon.jaspe.util.JACalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class REAnnonceARC3DXmlService {

    private static REAnnonceARC3DXmlService instance = new REAnnonceARC3DXmlService();

    private static final String REF_CAISSE = "REN";
    private static final short MOTIF_ARC = 3;
    private static final List<Integer> LIST_CODE_ENREGISTREMENT = Arrays.asList(1, 6);

    public static REAnnonceARC3DXmlService getInstance() {
        return instance;
    }

    public VAIKMeldungKassenWechselType getAnnonceXml(String newNoCaisseAgence, String nss, String oldNoCaisseAgence,
            Long increment) throws DatatypeConfigurationException, ParseException {

        VAIKMeldungKassenWechselType meldung = new VAIKMeldungKassenWechselType();
        ListeFolgerecordNrType listeFolgerecordNr = new ListeFolgerecordNrType();
        listeFolgerecordNr.getFolgerecordNr().addAll(LIST_CODE_ENREGISTREMENT);
        meldung.setListeFolgerecordNr(listeFolgerecordNr);
        meldung.setKasseZweigstelle(Integer.valueOf(oldNoCaisseAgence));
        meldung.setKasseneigenerHinweis(REF_CAISSE + nss);
        meldung.setMeldungsnummer(increment);
        meldung.setMZRSchluesselzahl(MOTIF_ARC);

        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final String dateStr = JACalendar.todayjjMMMMaaaa();
        final java.util.Date dDate = format.parse(dateStr);
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);
        XMLGregorianCalendar dealCloseDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        dealCloseDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        meldung.setAuftragsdatum(dealCloseDate);

        meldung.setVersichertennummer(nss);
        meldung.setKasseZweigstelleNeu(Integer.valueOf(newNoCaisseAgence));
        return meldung;
    }

}
