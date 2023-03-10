package globaz.tucana.vb.journal;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.vb.TUAbstractPersitentObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
 * Viewbean non attach? ? un fichier DB et donc pas rattach? ? un BEntity
 * 
 * @author fgo date de cr?ation : 8 octobre 2007
 * @version : version 1.0
 * 
 */
public class TUJournalRecapF4ViewBean extends TUAbstractPersitentObject {
    private String annee = new String();
    private String csAgence = new String();
    private ArrayList csRubriqueList = new ArrayList();;
    private String eMail = new String();
    private TUJournal journal = null;
    private String mois = new String();

    /**
     * Constructeur
     */
    public TUJournalRecapF4ViewBean() {
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
     * R?cup?re l'ann?e
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * R?cup?re le csAgence
     * 
     * @return
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * R?cup?ration du code syst?me rubrique
     * 
     * @return
     */
    public ArrayList getCsRubriqueList() {
        return csRubriqueList;
    }

    /**
     * R?cup?re l'adresse e-mail
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
     * R?cup?re le journal
     * 
     * @return
     */
    public TUJournal getJournal() {
        return journal;
    }

    /**
     * R?cup?ration de la s?lection sur le mois
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
     * R?cup?re un document xml W3C
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
     * Modifie l'ann?e
     * 
     * @param newAnnee
     */
    public void setAnnee(String newAnnee) {
        annee = newAnnee;
    }

    /**
     * Modifie le csAgence
     * 
     * @param newMois
     */
    public void setCsAgence(String newMois) {
        csAgence = newMois;
    }

    /**
     * Modification du code syst?me rubrique
     * 
     * @param newCsRubrique
     */
    public void setCsRubriqueList(ArrayList newCsRubrique) {
        csRubriqueList = newCsRubrique;
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
     * Modification de la s?lection sur le mois
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
