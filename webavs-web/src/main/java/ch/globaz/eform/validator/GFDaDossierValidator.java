package ch.globaz.eform.validator;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.common.validation.ValidationError;
import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import ch.globaz.eform.utils.GFUtils;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.jade.exception.JadeApplicationException;
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
public class GFDaDossierValidator {
    private GFDaDossierValidator() {
    }

    public static boolean isExists(String messageId) {
        if (messageId == null) {
            return false;
        }
        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setByMessageId(messageId);
        search.setWhereKey("messageId");
        try {
            search = GFEFormServiceLocator.getGFDaDossierDBService().search(search);
            return search.getSearchResults().length > 1;
        } catch (JadePersistenceException | JadeApplicationServiceNotAvailableException e) {
            LOG.error("Une erreur c'est produite pour la recherche du message id :" + messageId, e);
        }

        return false;
    }

    public static ValidationResult sedexMessage101(SimpleSedexMessage messageSedex, ValidationResult result) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xmlDocument;
        try {
            builder = builderFactory.newDocumentBuilder();
            xmlDocument = builder.parse(messageSedex.getFileLocation());

            XPath xPath = XPathFactory.newInstance().newXPath();

            //Validation du doublon du message ID (message déjà traité)
            Node nodeMessageId = (Node) xPath.compile("/message/header/messageId").evaluate(xmlDocument, XPathConstants.NODE);
            String messageId = nodeMessageId.getFirstChild().getNodeValue();
            if (StringUtils.isEmpty(messageId)) {
                result.addError("messageId", ValidationError.MANDATORY);
            }

            //Validation de la connaissance de la caisse
            Node nodeSenderId = (Node) xPath.compile("/message/header/senderId").evaluate(xmlDocument, XPathConstants.NODE);
            String senderId = nodeSenderId.getFirstChild().getNodeValue();

            if (StringUtils.isEmpty(senderId)) {
                result.addError("senderId", ValidationError.MANDATORY);
            } else {
                AdministrationComplexModel caisse = GFUtils.getCaisseBySedexId(senderId);

                if (caisse == null) {
                    result.addError("senderId", ValidationError.UNKNOWN);
                }
            }

            //Validation de la présence du ourBusinessReferenceId
            Node nodeOurBusinessReferenceId = (Node) xPath.compile("/message/header/ourBusinessReferenceId").evaluate(xmlDocument, XPathConstants.NODE);
            String ourBusinessReferenceId = nodeOurBusinessReferenceId.getFirstChild().getNodeValue();
            if (StringUtils.isEmpty(ourBusinessReferenceId)) {
                result.addError("ourBusinessReferenceId", ValidationError.MANDATORY);
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
            } else {
                PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
                ts.setForNumeroAvsActuel(NSSUtils.formatNss(nss));
                ts = TIBusinessServiceLocator.getPersonneEtendueService().find(ts);
                if (0 == ts.getSize()) {
                    result.addError("nss", ValidationError.UNKNOWN);
                }
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
        } catch (Exception e) {
            LOG.error("Erreur dans le la validation des information du XML", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        }

        return result;
    }

    public static ValidationResult sedexMessage102(SimpleSedexMessage messageSedex, ValidationResult result) {
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
                String attachmentName = attachmentNode.getChildNodes().item(13).getChildNodes().item(1).getFirstChild().getNodeValue().split("/")[1];
                if (messageSedex.attachments.entrySet().stream()
                        .noneMatch(entry -> (entry.getValue()).endsWith(attachmentName))) {
                    result.addError("attachment", ValidationError.MISSING);
                }
                i++;
            }

            //Validation de la connaissance de la caisse
            Node nodeSenderId = (Node) xPath.compile("/message/header/senderId").evaluate(xmlDocument, XPathConstants.NODE);
            String senderId = nodeSenderId.getFirstChild().getNodeValue();

            if (StringUtils.isEmpty(senderId)) {
                result.addError("senderId", ValidationError.MANDATORY);
            } else {
                AdministrationComplexModel caisse = GFUtils.getCaisseBySedexId(senderId);

                if (caisse == null) {
                    result.addError("senderId", ValidationError.UNKNOWN);
                }
            }

            //Validation du doublon du message ID
            Node nodeMessageId = (Node) xPath.compile("/message/header/messageId").evaluate(xmlDocument, XPathConstants.NODE);
            String messageId = nodeMessageId.getFirstChild().getNodeValue();
            if (!StringUtils.isEmpty(messageId)) {
                GFFormulaireSearch search = new GFFormulaireSearch();
                search.setByMessageId(messageId);
                search.setWhereKey("messageId");
                search = GFEFormServiceLocator.getGFEFormDBService().search(search);
                if (search.getSearchResults().length != 0) {
                    result.addError("messageId", ValidationError.ALREADY_EXIST);
                }
            } else {
                result.addError("messageId", ValidationError.MANDATORY);
            }

            //Validation de la présence du ourBusinessReferenceId
            Node nodeOurBusinessReferenceId = (Node) xPath.compile("/message/header/ourBusinessReferenceId").evaluate(xmlDocument, XPathConstants.NODE);
            String ourBusinessReferenceId = nodeOurBusinessReferenceId == null ? null : nodeOurBusinessReferenceId.getFirstChild().getNodeValue();
            if (StringUtils.isEmpty(ourBusinessReferenceId)) {
                result.addError("ourBusinessReferenceId", ValidationError.MANDATORY);
            }

            //Recherche de la value du yourBusinessReferenceId
            Node nodeYourBusinessReferenceId = (Node) xPath.compile("/message/header/yourBusinessReferenceId").evaluate(xmlDocument, XPathConstants.NODE);
            String yourBusinessReferenceId = nodeYourBusinessReferenceId == null ? null : nodeYourBusinessReferenceId.getFirstChild().getNodeValue();
            if (!StringUtils.isEmpty(yourBusinessReferenceId)) {
                result.addInfo("yourBusinessReferenceId", yourBusinessReferenceId);
            }

            //Validation de la présence du subject
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
            PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
            ts.setForNumeroAvsActuel(NSSUtils.formatNss(nss));
            ts = TIBusinessServiceLocator.getPersonneEtendueService().find(ts);
            if (0 == ts.getSize()) {
                result.addError("nss", ValidationError.UNKNOWN);
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
        } catch (JadeApplicationException e) {
            LOG.error("Erreur dans la recherche du tier cible", e);
            result.addError("INTERNAL" , ValidationError.INTERNAL_ERROR);
        }

        return result;
    }
}
