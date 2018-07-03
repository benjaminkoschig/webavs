package globaz.pavo.db.comparaison;

import globaz.jade.client.util.JadeStringUtil;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import ch.admin.zas.pool.PoolAntwortVonZAS;
import ch.admin.zas.rc.IKBestandesmeldungType;

public class CIComparaisonIteratorInputXML implements ICIComparaisonIteratorInput {
    private String fileName = "";
    private boolean isReady = false;
    private static final String XSD_FOLDER = "/xsd/P2020/annoncesRC/";
    private static final String XSD_NAME = "AntwortVonZas.xsd";
    private JAXBContext jc;
    private Unmarshaller unmarshaller;
    private int nbCITraite = 0;
    List<Object> listCI = null;

    private int size = -1;

    public CIComparaisonIteratorInputXML() {
        super();
    }

    private CIEnteteRecord _fillBean() throws Exception {
        IKBestandesmeldungType donneeXML = (IKBestandesmeldungType) listCI.get(nbCITraite);
        CIEnteteRecord record = new CIEnteteRecord();
        int tailleNum = 0;
        String tmp = "";
        /*
         * Numéro de la caisse et Agence
         * CAS : Caisse suisse qui ont que 2 chiffres au lieu de 3
         */

        tailleNum = donneeXML.getIKKopf().getKasseZweigstelleIKFuehrend().length();
        record.setCaisse(JadeStringUtil.fillWithZeroes(
                donneeXML.getIKKopf().getKasseZweigstelleIKFuehrend().substring(0, tailleNum - 3), 3));
        record.setAgence(donneeXML.getIKKopf().getKasseZweigstelleIKFuehrend().substring(tailleNum - 3, tailleNum));

        /*
         * Numéro de l'AVS de l'assuré
         */
        record.setNumeroAssure(donneeXML.getIKKopf().getVersichertennummer());
        if (record.getNumeroAssure().startsWith("-") && record.getNumeroAssure().length() > 1) {
            record.setNumeroAssure("756" + record.getNumeroAssure().substring(1));
        }
        /*
         * Numéro de l'AVS antérieur de l'assuré
         */
        record.setNumeroAssureAnterieur(JadeStringUtil.isNull(donneeXML.getIKKopf().getVersichertenNrFruehere()) ? ""
                : donneeXML.getIKKopf().getVersichertenNrFruehere());
        /*
         * Nom et prénom concatané avec un séparateur ","
         */
        record.setNomPrenom(donneeXML.getIKKopf().getVersichertenAngaben().getNamen() + ","
                + donneeXML.getIKKopf().getVersichertenAngaben().getVornamen());
        /*
         * Code système du domicile d'origine
         */

        record.setPays(Integer.toString(donneeXML.getIKKopf().getVersichertenAngaben().getHeimatstaat()));
        /*
         * Genre du motif d'ouverture
         */
        if (donneeXML.getIKKopf().getMZRSchluesselzahl() != null) {
            record.setMotifOuverture(Short.toString(donneeXML.getIKKopf().getMZRSchluesselzahl()));
        }
        /*
         * Année d'ouverture
         */
        if (donneeXML.getIKKopf().getIKEroeffnungsdatum() != null) {
            record.setAnneeOuverture(Integer.toString(donneeXML.getIKKopf().getIKEroeffnungsdatum().getYear())
                    .substring(2, 4));
        }

        /*
         * Motif RCI
         * Date de cloture : Mois+Annee (Ex : 0198)
         * Numéro de la caisse/agence commettante
         * CAS : Caisse suisse qui ont que 2 chiffres au lieu de 3
         */
        if (donneeXML.getFruehererAuftrag() != null) {

            record.setMotifRci(Short.toString(donneeXML.getFruehererAuftrag().getMZRSchluesselzahl()));

            if (donneeXML.getFruehererAuftrag().getZIK().getAbschlussdatum().getMonth() < 10) {
                record.setDateCloture("0"
                        + Integer.toString(donneeXML.getFruehererAuftrag().getZIK().getAbschlussdatum().getMonth())
                        + Integer.toString(donneeXML.getFruehererAuftrag().getZIK().getAbschlussdatum().getYear())
                                .substring(2, 4));
            } else {
                record.setDateCloture(Integer.toString(donneeXML.getFruehererAuftrag().getZIK().getAbschlussdatum()
                        .getMonth())
                        + Integer.toString(donneeXML.getFruehererAuftrag().getZIK().getAbschlussdatum().getYear())
                                .substring(2, 4));
            }
            if (donneeXML.getFruehererAuftrag().getKasseZweigstelleAuftraggebend() != null) {
                tailleNum = donneeXML.getFruehererAuftrag().getKasseZweigstelleAuftraggebend().length();
                record.setCaisse(JadeStringUtil.fillWithZeroes(donneeXML.getFruehererAuftrag()
                        .getKasseZweigstelleAuftraggebend().substring(0, tailleNum - 3), 3));
                record.setAgence(donneeXML.getFruehererAuftrag().getKasseZweigstelleAuftraggebend()
                        .substring(tailleNum - 3, tailleNum));
            }

        }

        nbCITraite++;
        return record;
    }

    private void _init() throws Exception {
        try {
            if (!isReady) {
                SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                URL url = getClass().getResource(XSD_FOLDER + XSD_NAME);
                Schema schema = sf.newSchema(url);
                ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
                PoolAntwortVonZAS pool = factoryPool.createPoolAntwortVonZAS();
                jc = JAXBContext.newInstance(pool.getClass());
                unmarshaller = jc.createUnmarshaller();
                unmarshaller.setSchema(schema);
                pool = (PoolAntwortVonZAS) unmarshaller.unmarshal(new File(getFileName()));
                listCI = pool.getLot().get(0)
                        .getVAIKEmpfangsbestaetigungOrIKEroeffnungsermaechtigungOrIKUebermittlungsauftrag();
                nbCITraite = 0;
                isReady = true;
            }
        } catch (Exception e) {

            throw e;
        }
    }

    @Override
    public void close() {
        isReady = false;
    }

    /**
     * @return
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean hasNext() throws Exception {
        if (!isReady) {
            _init();
        }
        if (listCI != null) {
            if (nbCITraite < listCI.size()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public CIEnteteRecord next() throws Exception {
        if (!isReady) {
            _init();
        }
        if (listCI != null) {
            CIEnteteRecord declaration = _fillBean();
            return declaration;
        } else {
            throw new Exception();
        }
        // CIEnteteRecord declaration = new CIEnteteRecord ();
        // vide le cache pour lire de toute facon la ligne suivante
        // System.out.println("["+line+"]");

    }

    /**
     * @param string
     */
    @Override
    public void setFileName(String string) {
        fileName = string;
    }

    @Override
    public int size() throws Exception {
        if (!isReady) {
            _init();
        }
        if (listCI != null) {
            return listCI.size();
        } else {
            throw new Exception();
        }

    }

}
