package ch.globaz.eform.validator;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.common.validation.ValidationError;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

@Slf4j
public class GFEFormValidator {
    public static boolean isExists(String id) {
        GFFormulaireSearch gfeFormSearch = new GFFormulaireSearch();
        gfeFormSearch.setByMessageId(id);
        if (id == null) {
            return false;
        }
        try {
            gfeFormSearch = GFEFormServiceLocator.getGFEFormService().search(gfeFormSearch);
            return gfeFormSearch.getSearchResults().length > 0;
        } catch (JadePersistenceException | JadeApplicationServiceNotAvailableException e) {
            LOG.error("Une erreur c'est produite pour la recherche du gfeFormModel id :" + id, e);
        }

        return false;
    }

    public static ValidationResult sedexMessage(SimpleSedexMessage messageSedex) {
        ValidationResult result = new ValidationResult();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xmlDocument;
        try {
            builder = builderFactory.newDocumentBuilder();
            xmlDocument = builder.parse(messageSedex.getFileLocation());

            XPath xPath = XPathFactory.newInstance().newXPath();

            //Validation des attachements
            NodeList nodeList = (NodeList) xPath.compile("/message/header/attachment").evaluate(xmlDocument, XPathConstants.NODESET);
            int i = 0;
            while (!result.hasError() && i < nodeList.getLength()) {
                Node attachmentNode = nodeList.item(i);
                String attachmentPath = attachmentNode.getChildNodes().item(13).getChildNodes().item(1).getFirstChild().getNodeValue();
                if (messageSedex.attachments.entrySet().stream()
                        .noneMatch(entry -> ("attachments_" + entry.getValue()).equals(attachmentPath))) {
                    result.addError("attachment", ValidationError.EMPTY);
                }
                i++;
            }

            //Validation du doublon du message ID
            Node nodeMessageId = (Node) xPath.compile("/message/header/messageId").evaluate(xmlDocument, XPathConstants.NODE);
            String messageId = nodeMessageId.getFirstChild().getNodeValue();
            if (!StringUtils.isEmpty(messageId)) {
                GFFormulaireSearch search = new GFFormulaireSearch();
                search.setByMessageId(messageId);
                search.setWhereKey("messageId");
                search = GFEFormServiceLocator.getGFEFormService().search(search);
                if (search.getSearchResults().length != 0) {
                    result.addError("messageId", ValidationError.ALREADY_EXIST);
                }
            } else {
                result.addError("messageId", ValidationError.MANDATORY);
            }

            //Validation de la présence de message ID
            Node nodeSubject = (Node) xPath.compile("/message/header/subject").evaluate(xmlDocument, XPathConstants.NODE);
            String subject = nodeSubject.getFirstChild().getNodeValue();
            if (StringUtils.isEmpty(subject)) {
                result.addError("subject", ValidationError.MANDATORY);
            }

            //Validation de la présence de message Date
            Node nodeMessageDate = (Node) xPath.compile("/message/header/messageDate").evaluate(xmlDocument, XPathConstants.NODE);
            String messageDate = nodeMessageDate.getFirstChild().getNodeValue();
            if (StringUtils.isEmpty(messageDate)) {
                result.addError("messageDate", ValidationError.MANDATORY);
            }

            //Validation de la présence du MSS
            Node nodeVn = (Node) xPath.compile("/message/content/insuredPerson/vn").evaluate(xmlDocument, XPathConstants.NODE);
            String nss = nodeVn.getFirstChild().getNodeValue();
            if (StringUtils.isEmpty(nss)) {
                result.addError("nss", ValidationError.MANDATORY);
            } else if (!NSSUtils.checkNSS(nss)) {
                result.addError("nss", ValidationError.MALFORMED);
            }
        } catch (ParserConfigurationException e) {
            LOG.error("Erreur dans la configuration du parceur XML", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        } catch (IOException | SAXException e) {
            LOG.error("Erreur dans le parcing du fichier XML", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        } catch (XPathExpressionException e) {
            LOG.error("Erreur dans le parcing de l'expression xpath", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        } catch (JadePersistenceException e) {
            LOG.error("Erreur dans le parcour du dom xml", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOG.error("Erreur dans le chargement du service eform", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        }

        return result;
    }
}
