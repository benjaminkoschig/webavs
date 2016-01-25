package globaz.amal.vb.process;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * This object implements XMLFilter and monitors the incoming SAX
 * events. Once it hits a Contribuable element, it creates a new
 * unmarshaller and unmarshals one purchase order.
 * 
 * <p>
 * Once finished unmarshalling it, we will process it, then move on to the next contribuable.
 */
public class AMFichierUploadSplitter extends AMAbstractProcessUpload {

    // private int cpt = 0;

    // private String namespace = null;

    /**
     * 0 si tout s'est bien passé, sinon ... [ à définir ]
     */
    private int codeValid = 0;
    private String paramDateDecision = null;
    private String paramJobDate = null;
    private String paramJobSeq = null;
    private String paramPeriodeMax = null;
    private String paramPeriodeMin = null;

    public AMFichierUploadSplitter(JAXBContext context) {
        this.context = context;
    }

    public AMFichierUploadSplitter(JAXBContext context, String namespace) {
        this.namespace = namespace;
        this.context = context;
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        // forward this event
        super.endElement(namespaceURI, localName, qName);

        if (depth != 0) {
            depth--;
            if (depth == 0) {
                // just finished sending one chunk.

                // emulate the end of a document.
                Enumeration e = namespaces.getPrefixes();
                while (e.hasMoreElements()) {
                    String prefix = (String) e.nextElement();
                    unmarshallerHandler.endPrefixMapping(prefix);
                }
                String defaultURI = namespaces.getURI("");
                if (defaultURI != null) {
                    unmarshallerHandler.endPrefixMapping("");
                }
                unmarshallerHandler.endDocument();

                // stop forwarding events by setting a dummy handler.
                // XMLFilter doesn't accept null, so we have to give it something,
                // hence a DefaultHandler, which does nothing.
                setContentHandler(new DefaultHandler());

                // then retrieve the fully unmarshalled object
                try {
                    ch.globaz.amal.xmlns.am_0001._1.Contribuable c = (ch.globaz.amal.xmlns.am_0001._1.Contribuable) unmarshallerHandler
                            .getResult();

                    process(c);
                } catch (JAXBException je) {
                    // error was found during the unmarshalling.
                    // you can either abort the processing by throwing a SAXException,
                    // or you can continue processing by returning from this method.
                    System.err.println("unable to process an order at line " + locator.getLineNumber());
                    return;
                } catch (AMUploadFichierRepriseException ufe) {
                    JadeThread.logError(this.getClass().getName(), "Erreur lors de l'ajout en DB");
                } catch (JadeApplicationServiceNotAvailableException jasn) {
                    JadeThread.logError(this.getClass().getName(), "Erreur lors de l'ajout en DB");
                } catch (JadePersistenceException jpe) {
                    JadeThread.logError(this.getClass().getName(), "Erreur lors de l'ajout en DB");
                }
                unmarshallerHandler = null;
            }
        }
    }

    public int getCodeValid() {
        return codeValid;
    }

    @Override
    public void process(ch.globaz.amal.xmlns.am_0001._1.Contribuable contribuable) throws JAXBException,
            AMUploadFichierRepriseException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        boolean entityIsValid = true;
        String messageIfNoValid = "";
        SimpleUploadFichierReprise simpleUploadFichierReprise = new SimpleUploadFichierReprise();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Marshaller m = context.createMarshaller();
            m.marshal(contribuable, out);

            simpleUploadFichierReprise.setNoContribuable(contribuable.getNdc());
            simpleUploadFichierReprise.setXmlLignes(JadeStringUtil.decodeUTF8(out.toString()));
        } catch (JAXBException jbe) {
            JadeThread.logError(this.getClass().getName(), jbe.getMessage());
            throw new JAXBException(jbe.getMessage());
        }

        simpleUploadFichierReprise.setTypeReprise("DECTAX");
        try {
            for (ch.globaz.amal.xmlns.am_0001._1.Personne personne : contribuable.getPersonne()) {
                if ("principal".equals(personne.getType())) {
                    String nomPrenom = contribuable.getPersonne().get(0).getNom() + " "
                            + contribuable.getPersonne().get(0).getPrenom();
                    simpleUploadFichierReprise.setNomPrenom(nomPrenom.toUpperCase());
                }
                if (!JadeStringUtil.isBlank(personne.getDateNaiss())
                        && !JadeDateUtil.isGlobazDate(personne.getDateNaiss())) {
                    entityIsValid = false;
                    setCodeValid(1);
                    messageIfNoValid = "La date de naissance : " + personne.getDateNaiss() + " n'est pas valide !";
                }
                if (!JadeStringUtil.isBlank(personne.getDateDeces())
                        && !JadeDateUtil.isGlobazDate(personne.getDateDeces())) {
                    entityIsValid = false;
                    setCodeValid(1);
                    messageIfNoValid = "La date de décès : " + personne.getDateDeces() + " n'est pas valide !";
                }
            }

            for (ch.globaz.amal.xmlns.am_0001._1.Taxation taxation : contribuable.getTaxations().getTaxation()) {
                if (!JadeStringUtil.isBlank(taxation.getDateDec()) && !JadeDateUtil.isGlobazDate(taxation.getDateDec())) {
                    entityIsValid = false;
                    setCodeValid(1);
                    messageIfNoValid = "La date de taxation : " + taxation.getDateDec() + " n'est pas valide !";
                }
                if (!JadeStringUtil.isBlank(taxation.getDepartCommuneDate())
                        && !JadeDateUtil.isGlobazDate(taxation.getDepartCommuneDate())) {
                    entityIsValid = false;
                    setCodeValid(1);
                    messageIfNoValid = "La date de départ de la commune : " + taxation.getDepartCommuneDate()
                            + " n'est pas valide !";
                }
            }
            simpleUploadFichierReprise.setMsgNoValid(messageIfNoValid);
            simpleUploadFichierReprise.setIsValid(entityIsValid);
            simpleUploadFichierReprise = AmalServiceLocator.getSimpleUploadFichierService().create(
                    simpleUploadFichierReprise);

        } catch (Exception e) {
            setCodeValid(1);
            throw new AMUploadFichierRepriseException(e.getMessage());
        }

        cpt++;
        if (cpt % 500 == 1) {
            System.out.println(cpt);
        }
    }

    public void setCodeValid(int codeValid) {
        this.codeValid = codeValid;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if (depth != 0) {
            // we are in the middle of forwarding events.
            // continue to do so.
            depth++;
            super.startElement(namespaceURI, localName, qName, atts);
            return;
        }

        if (namespaceURI.equals(namespace)) {
            if (localName.equals("Contribuables")) {
                paramJobSeq = atts.getValue(atts.getIndex("paramJobSeq"));
                paramJobDate = atts.getValue(atts.getIndex("paramJobDate"));
                paramDateDecision = atts.getValue(atts.getIndex("paramDateDecision"));
                paramPeriodeMin = atts.getValue(atts.getIndex("paramPeriodeMin"));
                paramPeriodeMax = atts.getValue(atts.getIndex("paramPeriodeMax"));
            } else if (localName.equals("Contribuable")) {
                // start a new unmarshaller
                Unmarshaller unmarshaller;
                try {
                    unmarshaller = context.createUnmarshaller();

                } catch (JAXBException e) {
                    // there's no way to recover from this error.
                    // we will abort the processing.
                    throw new SAXException(e);
                }
                unmarshallerHandler = unmarshaller.getUnmarshallerHandler();

                // set it as the content handler so that it will receive
                // SAX events from now on.
                setContentHandler(unmarshallerHandler);

                // fire SAX events to emulate the start of a new document.
                unmarshallerHandler.startDocument();
                unmarshallerHandler.setDocumentLocator(locator);

                Enumeration e = namespaces.getPrefixes();
                while (e.hasMoreElements()) {
                    String prefix = (String) e.nextElement();
                    String uri = namespaces.getURI(prefix);

                    unmarshallerHandler.startPrefixMapping(prefix, uri);
                }
                String defaultURI = namespaces.getURI("");
                if (defaultURI != null) {
                    unmarshallerHandler.startPrefixMapping("", defaultURI);
                }
                super.startElement(namespaceURI, localName, qName, atts);

                // count the depth of elements and we will know when to stop.
                depth = 1;
            }
        }
    }
}
