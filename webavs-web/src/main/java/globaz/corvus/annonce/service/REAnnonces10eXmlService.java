package globaz.corvus.annonce.service;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
