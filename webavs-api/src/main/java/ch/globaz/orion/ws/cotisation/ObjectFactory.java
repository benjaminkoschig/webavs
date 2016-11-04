
package ch.globaz.orion.ws.cotisation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.globaz.orion.ws.cotisation package. 
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

    private final static QName _ExecuterPreRemplissageDan_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "executerPreRemplissageDan");
    private final static QName _GenererDocumentPucsLisible_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "genererDocumentPucsLisible");
    private final static QName _ListerMassesActuelles_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "listerMassesActuelles");
    private final static QName _ListerMassesActuellesResponse_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "listerMassesActuellesResponse");
    private final static QName _WebavsException_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "WebavsException");
    private final static QName _FindTauxAssuranceForCotisation_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "findTauxAssuranceForCotisation");
    private final static QName _FindTauxAssuranceForCotisationResponse_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "findTauxAssuranceForCotisationResponse");
    private final static QName _FindDecompteMois_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "findDecompteMois");
    private final static QName _FindDecompteMoisResponse_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "findDecompteMoisResponse");
    private final static QName _GenererDocumentPucsLisibleResponse_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "genererDocumentPucsLisibleResponse");
    private final static QName _ExecuterPreRemplissageDanResponse_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "executerPreRemplissageDanResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.globaz.orion.ws.cotisation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DecompteMensuelLine }
     * 
     */
    public DecompteMensuelLine createDecompteMensuelLine() {
        return new DecompteMensuelLine();
    }

    /**
     * Create an instance of {@link ExecuterPreRemplissageDanResponse }
     * 
     */
    public ExecuterPreRemplissageDanResponse createExecuterPreRemplissageDanResponse() {
        return new ExecuterPreRemplissageDanResponse();
    }

    /**
     * Create an instance of {@link ListerMassesActuellesResponse }
     * 
     */
    public ListerMassesActuellesResponse createListerMassesActuellesResponse() {
        return new ListerMassesActuellesResponse();
    }

    /**
     * Create an instance of {@link FindTauxAssuranceForCotisation }
     * 
     */
    public FindTauxAssuranceForCotisation createFindTauxAssuranceForCotisation() {
        return new FindTauxAssuranceForCotisation();
    }

    /**
     * Create an instance of {@link ExecuterPreRemplissageDan }
     * 
     */
    public ExecuterPreRemplissageDan createExecuterPreRemplissageDan() {
        return new ExecuterPreRemplissageDan();
    }

    /**
     * Create an instance of {@link GenererDocumentPucsLisible }
     * 
     */
    public GenererDocumentPucsLisible createGenererDocumentPucsLisible() {
        return new GenererDocumentPucsLisible();
    }

    /**
     * Create an instance of {@link FindTauxAssuranceForCotisationResponse }
     * 
     */
    public FindTauxAssuranceForCotisationResponse createFindTauxAssuranceForCotisationResponse() {
        return new FindTauxAssuranceForCotisationResponse();
    }

    /**
     * Create an instance of {@link FindDecompteMois }
     * 
     */
    public FindDecompteMois createFindDecompteMois() {
        return new FindDecompteMois();
    }

    /**
     * Create an instance of {@link MassesForAffilie }
     * 
     */
    public MassesForAffilie createMassesForAffilie() {
        return new MassesForAffilie();
    }

    /**
     * Create an instance of {@link FindDecompteMoisResponse }
     * 
     */
    public FindDecompteMoisResponse createFindDecompteMoisResponse() {
        return new FindDecompteMoisResponse();
    }

    /**
     * Create an instance of {@link Masse }
     * 
     */
    public Masse createMasse() {
        return new Masse();
    }

    /**
     * Create an instance of {@link DecompteMensuel }
     * 
     */
    public DecompteMensuel createDecompteMensuel() {
        return new DecompteMensuel();
    }

    /**
     * Create an instance of {@link GenererDocumentPucsLisibleResponse }
     * 
     */
    public GenererDocumentPucsLisibleResponse createGenererDocumentPucsLisibleResponse() {
        return new GenererDocumentPucsLisibleResponse();
    }

    /**
     * Create an instance of {@link WebAvsException }
     * 
     */
    public WebAvsException createWebAvsException() {
        return new WebAvsException();
    }

    /**
     * Create an instance of {@link ListerMassesActuelles }
     * 
     */
    public ListerMassesActuelles createListerMassesActuelles() {
        return new ListerMassesActuelles();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuterPreRemplissageDan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "executerPreRemplissageDan")
    public JAXBElement<ExecuterPreRemplissageDan> createExecuterPreRemplissageDan(ExecuterPreRemplissageDan value) {
        return new JAXBElement<ExecuterPreRemplissageDan>(_ExecuterPreRemplissageDan_QNAME, ExecuterPreRemplissageDan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenererDocumentPucsLisible }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "genererDocumentPucsLisible")
    public JAXBElement<GenererDocumentPucsLisible> createGenererDocumentPucsLisible(GenererDocumentPucsLisible value) {
        return new JAXBElement<GenererDocumentPucsLisible>(_GenererDocumentPucsLisible_QNAME, GenererDocumentPucsLisible.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListerMassesActuelles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "listerMassesActuelles")
    public JAXBElement<ListerMassesActuelles> createListerMassesActuelles(ListerMassesActuelles value) {
        return new JAXBElement<ListerMassesActuelles>(_ListerMassesActuelles_QNAME, ListerMassesActuelles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListerMassesActuellesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "listerMassesActuellesResponse")
    public JAXBElement<ListerMassesActuellesResponse> createListerMassesActuellesResponse(ListerMassesActuellesResponse value) {
        return new JAXBElement<ListerMassesActuellesResponse>(_ListerMassesActuellesResponse_QNAME, ListerMassesActuellesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebAvsException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "WebavsException")
    public JAXBElement<WebAvsException> createWebavsException(WebAvsException value) {
        return new JAXBElement<WebAvsException>(_WebavsException_QNAME, WebAvsException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTauxAssuranceForCotisation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "findTauxAssuranceForCotisation")
    public JAXBElement<FindTauxAssuranceForCotisation> createFindTauxAssuranceForCotisation(FindTauxAssuranceForCotisation value) {
        return new JAXBElement<FindTauxAssuranceForCotisation>(_FindTauxAssuranceForCotisation_QNAME, FindTauxAssuranceForCotisation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindTauxAssuranceForCotisationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "findTauxAssuranceForCotisationResponse")
    public JAXBElement<FindTauxAssuranceForCotisationResponse> createFindTauxAssuranceForCotisationResponse(FindTauxAssuranceForCotisationResponse value) {
        return new JAXBElement<FindTauxAssuranceForCotisationResponse>(_FindTauxAssuranceForCotisationResponse_QNAME, FindTauxAssuranceForCotisationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDecompteMois }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "findDecompteMois")
    public JAXBElement<FindDecompteMois> createFindDecompteMois(FindDecompteMois value) {
        return new JAXBElement<FindDecompteMois>(_FindDecompteMois_QNAME, FindDecompteMois.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindDecompteMoisResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "findDecompteMoisResponse")
    public JAXBElement<FindDecompteMoisResponse> createFindDecompteMoisResponse(FindDecompteMoisResponse value) {
        return new JAXBElement<FindDecompteMoisResponse>(_FindDecompteMoisResponse_QNAME, FindDecompteMoisResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenererDocumentPucsLisibleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "genererDocumentPucsLisibleResponse")
    public JAXBElement<GenererDocumentPucsLisibleResponse> createGenererDocumentPucsLisibleResponse(GenererDocumentPucsLisibleResponse value) {
        return new JAXBElement<GenererDocumentPucsLisibleResponse>(_GenererDocumentPucsLisibleResponse_QNAME, GenererDocumentPucsLisibleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuterPreRemplissageDanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cotisation.ws.orion.globaz.ch/", name = "executerPreRemplissageDanResponse")
    public JAXBElement<ExecuterPreRemplissageDanResponse> createExecuterPreRemplissageDanResponse(ExecuterPreRemplissageDanResponse value) {
        return new JAXBElement<ExecuterPreRemplissageDanResponse>(_ExecuterPreRemplissageDanResponse_QNAME, ExecuterPreRemplissageDanResponse.class, null, value);
    }

}
