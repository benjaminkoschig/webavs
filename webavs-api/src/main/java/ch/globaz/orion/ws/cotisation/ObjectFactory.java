
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

    private final static QName _ListerMassesActuelles_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "listerMassesActuelles");
    private final static QName _ListerMassesActuellesResponse_QNAME = new QName("http://cotisation.ws.orion.globaz.ch/", "listerMassesActuellesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.globaz.orion.ws.cotisation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Masse }
     * 
     */
    public Masse createMasse() {
        return new Masse();
    }

    /**
     * Create an instance of {@link ListerMassesActuellesResponse }
     * 
     */
    public ListerMassesActuellesResponse createListerMassesActuellesResponse() {
        return new ListerMassesActuellesResponse();
    }

    /**
     * Create an instance of {@link MassesForAffilie }
     * 
     */
    public MassesForAffilie createMassesForAffilie() {
        return new MassesForAffilie();
    }

    /**
     * Create an instance of {@link ListerMassesActuelles }
     * 
     */
    public ListerMassesActuelles createListerMassesActuelles() {
        return new ListerMassesActuelles();
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

}
