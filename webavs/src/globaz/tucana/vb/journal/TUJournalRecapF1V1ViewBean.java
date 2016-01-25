package globaz.tucana.vb.journal;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.vb.TUAbstractPersitentObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

/**
 * Viewbean non attaché à un fichier DB et donc pas rattaché à un BEntity
 * 
 * @author fgo date de création : 5 juil. 06
 * @version : version 1.0
 * 
 */
public class TUJournalRecapF1V1ViewBean extends TUAbstractPersitentObject {
    private String annee = new String();
    private String csAgence = new String();
    private String eMail = new String();
    private TUJournal journal = null;
    private String mois = new String();

    /**
     * Constructeur
     */
    public TUJournalRecapF1V1ViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#add()
     */
    @Override
    public void add() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#delete()
     */
    @Override
    public void delete() throws Exception {

    }

    /**
     * Récupère l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupération de l'id code système agence
     * 
     * @return
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Récupère l'adresse e-mail
     * 
     * @return
     */
    public String getEMail() {
        return eMail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#getId()
     */
    @Override
    public String getId() {
        return "";
    }

    /**
     * Récupère le journal
     * 
     * @return
     */
    public TUJournal getJournal() {
        return journal;
    }

    /**
     * Récupère le mois
     * 
     * @return
     */
    public String getMois() {
        return mois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy((BSession) getISession());
    }

    /**
     * Récupère un document xml W3C
     * 
     * @return Document un document du w3c.dom.Document
     */
    public Document getXmlDocument() {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            if (journal != null) {
                StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                journal.toXml(buffer);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toString().getBytes());
                try {
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
                    transformer.setOutputProperty(OutputKeys.INDENT, "true");
                    // transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                    transformer.transform(new StreamSource(bais), new DOMResult(document));
                } catch (TransformerConfigurationException e) {
                    JadeCodingUtil.catchException(this, "getXmlDocument", e);
                } catch (TransformerException e) {
                    JadeCodingUtil.catchException(this, "getXmlDocument", e);
                } catch (TransformerFactoryConfigurationError e) {
                    JadeCodingUtil.catchException(this, "getXmlDocument", e);
                }
                try {
                    bais.close();
                } catch (IOException e) {
                    JadeCodingUtil.catchException(this, "getXmlDocument", e);
                }
            }
        } catch (ParserConfigurationException pce) {
            JadeCodingUtil.catchException(this, "getXmlDocument", pce);
        } catch (FactoryConfigurationError fce) {
            JadeCodingUtil.catchException(this, "getXmlDocument", fce);
        }
        return document;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

    }

    /**
     * Modifie l'année
     * 
     * @param newAnnee
     */
    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    /**
     * Modification de l'id code système agence
     * 
     * @param newCsAgence
     */
    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    /**
     * Modifie l'adresse e-mail
     * 
     * @param newEMail
     */
    public void setEMail(String newEMail) {
        eMail = newEMail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {

    }

    /**
     * Modifie le journal
     * 
     * @param newJournal
     */
    public void setJournal(TUJournal newJournal) {
        journal = newJournal;
    }

    /**
     * Modifie le mois
     * 
     * @param newMois
     */
    public void setMois(String newMois) {
        mois = newMois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.vb.TUAbstractPersitentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
