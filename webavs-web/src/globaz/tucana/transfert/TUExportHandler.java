package globaz.tucana.transfert;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.jade.common.Jade;
import globaz.tucana.application.TUApplication;
import globaz.tucana.db.bouclement.access.TUBouclement;
import globaz.tucana.db.bouclement.access.TUDetail;
import globaz.tucana.db.bouclement.access.TUDetailManager;
import globaz.tucana.db.bouclement.access.TUNoPassage;
import globaz.tucana.db.bouclement.access.TUNoPassageManager;
import globaz.tucana.exception.TUException;
import globaz.tucana.exception.fw.TUFWException;
import globaz.tucana.exception.fw.TUFWFindException;
import globaz.tucana.exception.fw.TUFWRetrieveException;
import globaz.tucana.exception.process.TUInitExportException;
import globaz.tucana.exception.transform.TUXmlEncoderException;
import globaz.tucana.util.DomEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe permettant un exprot de données
 * 
 * @author fgo
 * 
 */
public class TUExportHandler {

    /**
     * Appel principal pour la génération du document d'exportation d'un bouclement
     * 
     * @param transaction
     * @param idBouclement
     * @return
     * @throws TUException
     */
    public static String generate(BTransaction transaction, String idBouclement) throws TUException {
        String destination = Jade.getInstance().getHomeDir() + TUApplication.DEFAULT_APPLICATION_ROOT
                + "/work/TransfertOut"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".xml";
        try {
            Source source = new DOMSource(lectureBouclement(transaction, idBouclement, destination));

            // Création du fichier de sortie
            Result resultat = new StreamResult(destination);

            // Configuration du transformer
            TransformerFactory fabrique = TransformerFactory.newInstance();
            Transformer transformer = fabrique.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            // Transformation
            transformer.transform(source, resultat);

            return destination;
        } catch (TUFWRetrieveException e) {
            throw new TUFWException("Erreur lors du transfer du bouclement", e);
        } catch (TransformerConfigurationException e) {
            throw new TUFWException("Erreur de configuration pour la transformation", e);
        } catch (TransformerException e) {
            throw new TUFWException("Erreur de transformation", e);
        }
    }

    /**
     * Lecture du bouclement de de ses enfants afin de le générer en document xml
     * 
     * @param transaction
     * @param idBouclement
     * @param destination
     * @return
     * @throws TUFWRetrieveException
     */
    private static Document lectureBouclement(BTransaction transaction, String idBouclement, String destination)
            throws TUFWRetrieveException {
        TUBouclement bouclement = new TUBouclement();
        bouclement.setSession(transaction.getSession());
        bouclement.setIdBouclement(idBouclement);
        try {
            // lecture du bouclement
            bouclement.retrieve(transaction);
            if (bouclement.isNew()) {
                throw new TUFWException("TUExportHandler.lectureBouclement() : le bouclement " + idBouclement
                        + " n'existe pas");
            }
            // récupération d'une instance permettant de créer le bouclement
            DomEncoder encoder = DomEncoder.newInstance();
            Element elementBouclement = encoder.makeElement(bouclement, encoder.getDocument());

            // lecture du numéro de passage
            lectureNoPassage(transaction, idBouclement, destination, encoder, elementBouclement);
            // lecture du détail du bouclement
            lectureDetailBouclement(transaction, idBouclement, destination, encoder, elementBouclement);
            // association de l'élément bouclement à l'élément root
            encoder.getRoot().appendChild(elementBouclement);
            // termine le document en liant l'élément root au document
            DomEncoder.endEncoder(encoder);
            return encoder.getDocument();
        } catch (Exception e) {
            throw new TUFWRetrieveException("TUExportHandler.lectureBouclement()", e);
        }
    }

    /**
     * Lecture des détails pour un bouclement
     * 
     * @param transaction
     * @param idBouclement
     * @param destination
     * @param encoder
     * @param elementPere
     * @throws TUFWFindException
     * @throws TUXmlEncoderException
     * @throws TUInitExportException
     */
    private static void lectureDetailBouclement(BTransaction transaction, String idBouclement, String destination,
            DomEncoder encoder, Element elementPere) throws TUFWFindException, TUXmlEncoderException,
            TUInitExportException {
        TUDetailManager detailManager = new TUDetailManager();
        detailManager.setSession(transaction.getSession());
        detailManager.setForIdBouclement(idBouclement);
        try {
            detailManager.find(transaction, BManager.SIZE_NOLIMIT);
            if (detailManager.size() < 1) {
                throw new TUFWFindException(
                        "TUExportHandler.lectureDetailBouclement() : Aucun détail trouvé pour le bouclement "
                                + idBouclement);
            }
        } catch (Exception e) {
            throw new TUFWFindException("TUExportHandler.lectureDetailBouclement()",
                    detailManager.getCurrentSqlQuery(), e);
        }
        TUDetail detail = null;
        for (int i = 0; i < detailManager.size(); i++) {
            detail = (TUDetail) detailManager.getEntity(i);
            // pour chaque détail, création de l'élément et le rattache à
            // l'élément prère (ici le bouclement)
            Element elementClass = encoder.makeElement(detail, encoder.getDocument());
            elementPere.appendChild(elementClass);
        }
    }

    /**
     * Lectue de tous les passages pour un bouclement
     * 
     * @param transaction
     * @param idBouclement
     * @param destination
     * @param encoder
     * @param elementPere
     * @throws TUFWFindException
     * @throws TUXmlEncoderException
     * @throws TUInitExportException
     */
    private static void lectureNoPassage(BTransaction transaction, String idBouclement, String destination,
            DomEncoder encoder, Element elementPere) throws TUFWFindException, TUXmlEncoderException,
            TUInitExportException {
        TUNoPassageManager noPassageManager = new TUNoPassageManager();
        noPassageManager.setSession(transaction.getSession());
        noPassageManager.setForIdBouclement(idBouclement);
        try {
            noPassageManager.find(transaction);
            if (noPassageManager.size() < 1) {
                throw new TUFWFindException("TUExportHandler.lectureNoPassage() : Aucun enregistrement trouvé");
            }
        } catch (Exception e) {
            throw new TUFWFindException("TUExportHandler.lectureNoPassage()", noPassageManager.getCurrentSqlQuery(), e);
        }
        TUNoPassage passage = null;
        for (int i = 0; i < noPassageManager.size(); i++) {
            passage = (TUNoPassage) noPassageManager.getEntity(i);
            // pour chaque détail, création de l'élément et le rattache à
            // l'élément prère (ici le bouclement)
            Element elementClass = encoder.makeElement(passage, encoder.getDocument());
            elementPere.appendChild(elementClass);
        }

    }
}