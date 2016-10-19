
package ch.globaz.orion.ws.affiliation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.globaz.orion.ws.affiliation package. 
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

    private final static QName _WebavsException_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "WebavsException");
    private final static QName _FindAdresseCourrierAffilie_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "findAdresseCourrierAffilie");
    private final static QName _FindActiveSuiviCaisseResponse_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "findActiveSuiviCaisseResponse");
    private final static QName _FindCategorieAffiliationResponse_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "findCategorieAffiliationResponse");
    private final static QName _FindActiveSuiviCaisse_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "findActiveSuiviCaisse");
    private final static QName _FindCategorieAffiliation_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "findCategorieAffiliation");
    private final static QName _FindAdresseCourrierAffilieResponse_QNAME = new QName("http://affiliation.ws.orion.globaz.ch/", "findAdresseCourrierAffilieResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.globaz.orion.ws.affiliation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindActiveSuiviCaisseResponse }
     * 
     */
    public FindActiveSuiviCaisseResponse createFindActiveSuiviCaisseResponse() {
        return new FindActiveSuiviCaisseResponse();
    }

    /**
     * Create an instance of {@link FindAdresseCourrierAffilieResponse }
     * 
     */
    public FindAdresseCourrierAffilieResponse createFindAdresseCourrierAffilieResponse() {
        return new FindAdresseCourrierAffilieResponse();
    }

    /**
     * Create an instance of {@link FindCategorieAffiliation }
     * 
     */
    public FindCategorieAffiliation createFindCategorieAffiliation() {
        return new FindCategorieAffiliation();
    }

    /**
     * Create an instance of {@link WebAvsException }
     * 
     */
    public WebAvsException createWebAvsException() {
        return new WebAvsException();
    }

    /**
     * Create an instance of {@link FindActiveSuiviCaisse }
     * 
     */
    public FindActiveSuiviCaisse createFindActiveSuiviCaisse() {
        return new FindActiveSuiviCaisse();
    }

    /**
     * Create an instance of {@link FindAdresseCourrierAffilie }
     * 
     */
    public FindAdresseCourrierAffilie createFindAdresseCourrierAffilie() {
        return new FindAdresseCourrierAffilie();
    }

    /**
     * Create an instance of {@link FindCategorieAffiliationResponse }
     * 
     */
    public FindCategorieAffiliationResponse createFindCategorieAffiliationResponse() {
        return new FindCategorieAffiliationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebAvsException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "WebavsException")
    public JAXBElement<WebAvsException> createWebavsException(WebAvsException value) {
        return new JAXBElement<WebAvsException>(_WebavsException_QNAME, WebAvsException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAdresseCourrierAffilie }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "findAdresseCourrierAffilie")
    public JAXBElement<FindAdresseCourrierAffilie> createFindAdresseCourrierAffilie(FindAdresseCourrierAffilie value) {
        return new JAXBElement<FindAdresseCourrierAffilie>(_FindAdresseCourrierAffilie_QNAME, FindAdresseCourrierAffilie.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindActiveSuiviCaisseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "findActiveSuiviCaisseResponse")
    public JAXBElement<FindActiveSuiviCaisseResponse> createFindActiveSuiviCaisseResponse(FindActiveSuiviCaisseResponse value) {
        return new JAXBElement<FindActiveSuiviCaisseResponse>(_FindActiveSuiviCaisseResponse_QNAME, FindActiveSuiviCaisseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCategorieAffiliationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "findCategorieAffiliationResponse")
    public JAXBElement<FindCategorieAffiliationResponse> createFindCategorieAffiliationResponse(FindCategorieAffiliationResponse value) {
        return new JAXBElement<FindCategorieAffiliationResponse>(_FindCategorieAffiliationResponse_QNAME, FindCategorieAffiliationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindActiveSuiviCaisse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "findActiveSuiviCaisse")
    public JAXBElement<FindActiveSuiviCaisse> createFindActiveSuiviCaisse(FindActiveSuiviCaisse value) {
        return new JAXBElement<FindActiveSuiviCaisse>(_FindActiveSuiviCaisse_QNAME, FindActiveSuiviCaisse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindCategorieAffiliation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "findCategorieAffiliation")
    public JAXBElement<FindCategorieAffiliation> createFindCategorieAffiliation(FindCategorieAffiliation value) {
        return new JAXBElement<FindCategorieAffiliation>(_FindCategorieAffiliation_QNAME, FindCategorieAffiliation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAdresseCourrierAffilieResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://affiliation.ws.orion.globaz.ch/", name = "findAdresseCourrierAffilieResponse")
    public JAXBElement<FindAdresseCourrierAffilieResponse> createFindAdresseCourrierAffilieResponse(FindAdresseCourrierAffilieResponse value) {
        return new JAXBElement<FindAdresseCourrierAffilieResponse>(_FindAdresseCourrierAffilieResponse_QNAME, FindAdresseCourrierAffilieResponse.class, null, value);
    }

}
