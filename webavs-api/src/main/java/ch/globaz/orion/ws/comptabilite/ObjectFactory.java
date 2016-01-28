
package ch.globaz.orion.ws.comptabilite;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.globaz.orion.ws.comptabilite package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DownloadFileResponse_QNAME = new QName("http://comptabilite.ws.orion.globaz.ch/", "downloadFileResponse");
    private final static QName _GenererExtraitCompteAnnexe_QNAME = new QName("http://comptabilite.ws.orion.globaz.ch/", "genererExtraitCompteAnnexe");
    private final static QName _GenererExtraitCompteAnnexeResponse_QNAME = new QName("http://comptabilite.ws.orion.globaz.ch/", "genererExtraitCompteAnnexeResponse");
    private final static QName _ListerApercuCompteAnnexe_QNAME = new QName("http://comptabilite.ws.orion.globaz.ch/", "listerApercuCompteAnnexe");
    private final static QName _DownloadFile_QNAME = new QName("http://comptabilite.ws.orion.globaz.ch/", "downloadFile");
    private final static QName _ListerApercuCompteAnnexeResponse_QNAME = new QName("http://comptabilite.ws.orion.globaz.ch/", "listerApercuCompteAnnexeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.globaz.orion.ws.comptabilite
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenererExtraitCompteAnnexeResponse }
     * 
     */
    public GenererExtraitCompteAnnexeResponse createGenererExtraitCompteAnnexeResponse() {
        return new GenererExtraitCompteAnnexeResponse();
    }

    /**
     * Create an instance of {@link ListerApercuCompteAnnexeResponse }
     * 
     */
    public ListerApercuCompteAnnexeResponse createListerApercuCompteAnnexeResponse() {
        return new ListerApercuCompteAnnexeResponse();
    }

    /**
     * Create an instance of {@link ListerApercuCompteAnnexe }
     * 
     */
    public ListerApercuCompteAnnexe createListerApercuCompteAnnexe() {
        return new ListerApercuCompteAnnexe();
    }

    /**
     * Create an instance of {@link DownloadFileResponse }
     * 
     */
    public DownloadFileResponse createDownloadFileResponse() {
        return new DownloadFileResponse();
    }

    /**
     * Create an instance of {@link GenererExtraitCompteAnnexe }
     * 
     */
    public GenererExtraitCompteAnnexe createGenererExtraitCompteAnnexe() {
        return new GenererExtraitCompteAnnexe();
    }

    /**
     * Create an instance of {@link ApercuCompteAnnexe }
     * 
     */
    public ApercuCompteAnnexe createApercuCompteAnnexe() {
        return new ApercuCompteAnnexe();
    }

    /**
     * Create an instance of {@link DownloadFile }
     * 
     */
    public DownloadFile createDownloadFile() {
        return new DownloadFile();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://comptabilite.ws.orion.globaz.ch/", name = "downloadFileResponse")
    public JAXBElement<DownloadFileResponse> createDownloadFileResponse(DownloadFileResponse value) {
        return new JAXBElement<DownloadFileResponse>(_DownloadFileResponse_QNAME, DownloadFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenererExtraitCompteAnnexe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://comptabilite.ws.orion.globaz.ch/", name = "genererExtraitCompteAnnexe")
    public JAXBElement<GenererExtraitCompteAnnexe> createGenererExtraitCompteAnnexe(GenererExtraitCompteAnnexe value) {
        return new JAXBElement<GenererExtraitCompteAnnexe>(_GenererExtraitCompteAnnexe_QNAME, GenererExtraitCompteAnnexe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenererExtraitCompteAnnexeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://comptabilite.ws.orion.globaz.ch/", name = "genererExtraitCompteAnnexeResponse")
    public JAXBElement<GenererExtraitCompteAnnexeResponse> createGenererExtraitCompteAnnexeResponse(GenererExtraitCompteAnnexeResponse value) {
        return new JAXBElement<GenererExtraitCompteAnnexeResponse>(_GenererExtraitCompteAnnexeResponse_QNAME, GenererExtraitCompteAnnexeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListerApercuCompteAnnexe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://comptabilite.ws.orion.globaz.ch/", name = "listerApercuCompteAnnexe")
    public JAXBElement<ListerApercuCompteAnnexe> createListerApercuCompteAnnexe(ListerApercuCompteAnnexe value) {
        return new JAXBElement<ListerApercuCompteAnnexe>(_ListerApercuCompteAnnexe_QNAME, ListerApercuCompteAnnexe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://comptabilite.ws.orion.globaz.ch/", name = "downloadFile")
    public JAXBElement<DownloadFile> createDownloadFile(DownloadFile value) {
        return new JAXBElement<DownloadFile>(_DownloadFile_QNAME, DownloadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListerApercuCompteAnnexeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://comptabilite.ws.orion.globaz.ch/", name = "listerApercuCompteAnnexeResponse")
    public JAXBElement<ListerApercuCompteAnnexeResponse> createListerApercuCompteAnnexeResponse(ListerApercuCompteAnnexeResponse value) {
        return new JAXBElement<ListerApercuCompteAnnexeResponse>(_ListerApercuCompteAnnexeResponse_QNAME, ListerApercuCompteAnnexeResponse.class, null, value);
    }

}
