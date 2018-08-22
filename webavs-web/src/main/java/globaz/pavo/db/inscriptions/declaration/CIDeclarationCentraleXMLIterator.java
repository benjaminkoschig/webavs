package globaz.pavo.db.inscriptions.declaration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import ch.admin.zas.pool.PoolAntwortVonZAS;
import ch.admin.zas.pool.PoolAntwortVonZAS.Lot;
import ch.admin.zas.rc.ALVEntschaedigungenMeldungType;
import ch.admin.zas.rc.ALVEntschaedigungenMeldungType.Aufzeichnungen;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class CIDeclarationCentraleXMLIterator implements ICIDeclarationIterator {
    private String filename = "";
    private boolean isReady = false;
    private String provenance = "";
    private String line = null; // doir être initaialisé a null !
    BSession session = null;
    private int size = 0;
    private JAXBContext jc;
    private Unmarshaller unmarshaller;
    PoolAntwortVonZAS lots;
    List<Aufzeichnungen> listCI;
    Iterator<Aufzeichnungen> itIKStat;
    private BTransaction transaction = null;
    private String agence = "";
    private static final String XSD_FOLDER = "/xsd/P2020/annoncesRC/";
    private static final String XSD_NAME = "AntwortVonZas.xsd";

    private void _init() throws Exception {
        try {
            if (!isReady) {
                SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                URL url = getClass().getResource(XSD_FOLDER + XSD_NAME);
                Schema schema = sf.newSchema(url);
                ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
                lots = factoryPool.createPoolAntwortVonZAS();
                jc = JAXBContext.newInstance(PoolAntwortVonZAS.class);
                unmarshaller = jc.createUnmarshaller();
                unmarshaller.setSchema(schema);
                lots = (PoolAntwortVonZAS) unmarshaller.unmarshal(new File(getFilename()));
                isReady = true;
            }
        } catch (Exception e) {

            throw e;
        }
    }

    private CIDeclarationRecord _fillBean(Aufzeichnungen annonceXML) {
        // champs
        String noAvs = annonceXML.getIKKopf().getVersichertennummer().get(0);
        if (!JadeStringUtil.isBlankOrZero(noAvs)) {
            if (noAvs.startsWith("-")) {
                if (NSUtil.unFormatAVS(noAvs).length() > 1) {
                    noAvs = "756" + noAvs.substring(1);
                }
            }
        } else {
            noAvs = "00000000000";

        }

        String numeroAffilie = JadeStringUtil
                .fillWithSpaces(annonceXML.getEintragungIKMeldung().get(0).getAKAbrechnungsNr(), 11);
        String greEc = "";
        if (annonceXML.getEintragungIKMeldung().get(0).getSchluesselzahlStornoeintrag() != null) {
            greEc = annonceXML.getEintragungIKMeldung().get(0).getSchluesselzahlStornoeintrag() + "";
            if (annonceXML.getEintragungIKMeldung().get(0).getSchluesselzahlBeitragsart() != null) {
                greEc = greEc + annonceXML.getEintragungIKMeldung().get(0).getSchluesselzahlBeitragsart();
            } else {
                greEc = greEc + "0";
            }
        } else {
            greEc = "0";
            if (annonceXML.getEintragungIKMeldung().get(0).getSchluesselzahlBeitragsart() != null) {
                greEc = greEc + annonceXML.getEintragungIKMeldung().get(0).getSchluesselzahlBeitragsart();
            } else {
                greEc = greEc + "0";
            }
        }

        int moisDebut = annonceXML.getEintragungIKMeldung().get(0).getBeitragsdauer().getAnfangsmonat();
        int moisFin = annonceXML.getEintragungIKMeldung().get(0).getBeitragsdauer().getEndmonat();
        String annee = Integer.toString(annonceXML.getEintragungIKMeldung().get(0).getBeitragsjahr().getYear());
        String montantEcr = JadeStringUtil
                .fillWithZeroes(annonceXML.getEintragungIKMeldung().get(0).getEinkommen().toString(), 9);

        CIDeclarationRecord dec = new CIDeclarationRecord();
        dec.setNumeroAvs(noAvs);
        dec.setNumeroAffilie(numeroAffilie);
        dec.setGenreEcriture(greEc);
        dec.setAnnee(annee);
        dec.setMoisDebut(moisDebut);
        dec.setMoisFin(moisFin);
        dec.setMontantEcr(montantEcr);
        dec.setAgence(agence);

        dec.setMontantPositif(true);
        return dec;
    }

    @Override
    public void close() {
        isReady = false;
    }

    @Override
    public String getDateReception() {
        return null;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public TreeMap<String, Object> getNbSalaires() {
        return null;
    }

    @Override
    public TreeMap getNoAffiliePourReception() throws Exception {
        return null;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    @Override
    public TreeMap<String, Object> getTotauxJournaux() {
        return null;
    }

    @Override
    public BTransaction getTransaction() {
        return transaction;
    }

    @Override
    public boolean hasNext() throws Exception {
        if (itIKStat.hasNext()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public CIDeclarationRecord next() throws Exception {
        CIDeclarationRecord record = null;
        record = _fillBean(itIKStat.next());

        return record;
    }

    @Override
    public void setProvenance(String provenance) {
        this.provenance = provenance;

    }

    @Override
    public void setSession(BSession session) {
        this.session = session;

    }

    @Override
    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void setTypeImport(String type) {
    }

    @Override
    public int size() throws Exception {
        if (!isReady) {
            _init();
        }
        if (lots != null) {
            List<ALVEntschaedigungenMeldungType> listIKStat = new ArrayList<ALVEntschaedigungenMeldungType>();
            listCI = new ArrayList<Aufzeichnungen>();
            if (size < 1) {
                /*
                 * Parcourir les lots et prendre que les annonces CI tout en récupérant le numéro de l'agence
                 */
                for (Lot lot : lots.getLot()) {
                    for (Object object : lot
                            .getVAIKEmpfangsbestaetigungOrIKEroeffnungsermaechtigungOrIKUebermittlungsauftrag()) {
                        if (object instanceof ALVEntschaedigungenMeldungType) {
                            listIKStat.add((ALVEntschaedigungenMeldungType) object);
                            agence = listIKStat.get(0).getKasseZweigstelleIKFuehrend().substring(3, 6);

                        }
                    }
                }
                /*
                 * Trier pour avoir les inscriptions AC des annonces CI
                 */
                String noAffilie;
                for (ALVEntschaedigungenMeldungType list : listIKStat) {
                    for (ch.admin.zas.rc.ALVEntschaedigungenMeldungType.Aufzeichnungen annonceXMLRAW : list
                            .getAufzeichnungen()) {
                        noAffilie = annonceXMLRAW.getEintragungIKMeldung().get(0).getAKAbrechnungsNr();
                        if (noAffilie.length() > 5) {
                            if (noAffilie.substring(0, 6).contains("999999")) {
                                listCI.add(annonceXMLRAW);
                                size++;
                            }
                        }
                    }
                }

                itIKStat = listCI.iterator();
                return size;
            } else {
                return size;
            }

        } else {
            throw new Exception();
        }

    }

    @Override
    public void setFilename(String filename) {
        this.filename = filename;

    }

}
