
package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "WebAvsCotisationsService", targetNamespace = "http://cotisation.ws.orion.globaz.ch/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WebAvsCotisationsService {


    /**
     * 
     * @param arg0
     * @return
     *     returns ch.globaz.orion.ws.cotisation.MassesForAffilie
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listerMassesActuelles", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.ListerMassesActuelles")
    @ResponseWrapper(localName = "listerMassesActuellesResponse", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.ListerMassesActuellesResponse")
    public MassesForAffilie listerMassesActuelles(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "executerPreRemplissageDan", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.ExecuterPreRemplissageDan")
    @ResponseWrapper(localName = "executerPreRemplissageDanResponse", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.ExecuterPreRemplissageDanResponse")
    public boolean executerPreRemplissageDan(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        Integer arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3);

    /**
     * 
     * @param arg4
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "genererDocumentPucsLisible", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.GenererDocumentPucsLisible")
    @ResponseWrapper(localName = "genererDocumentPucsLisibleResponse", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.GenererDocumentPucsLisibleResponse")
    public String genererDocumentPucsLisible(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4);

    /**
     * 
     * @param montant
     * @param idCotisation
     * @param date
     * @return
     *     returns java.lang.Double
     * @throws WebAvsException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findTauxAssuranceForCotisation", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.FindTauxAssuranceForCotisation")
    @ResponseWrapper(localName = "findTauxAssuranceForCotisationResponse", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.FindTauxAssuranceForCotisationResponse")
    public Double findTauxAssuranceForCotisation(
        @WebParam(name = "idCotisation", targetNamespace = "")
        Integer idCotisation,
        @WebParam(name = "montant", targetNamespace = "")
        BigDecimal montant,
        @WebParam(name = "date", targetNamespace = "")
        String date)
        throws WebAvsException_Exception
    ;

    /**
     * 
     * @param numeroAffilie
     * @param mois
     * @param annee
     * @return
     *     returns ch.globaz.orion.ws.cotisation.DecompteMensuel
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findDecompteMois", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.FindDecompteMois")
    @ResponseWrapper(localName = "findDecompteMoisResponse", targetNamespace = "http://cotisation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.cotisation.FindDecompteMoisResponse")
    public DecompteMensuel findDecompteMois(
        @WebParam(name = "numeroAffilie", targetNamespace = "")
        String numeroAffilie,
        @WebParam(name = "mois", targetNamespace = "")
        String mois,
        @WebParam(name = "annee", targetNamespace = "")
        String annee);

}
