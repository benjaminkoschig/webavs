
package ch.globaz.orion.ws.affiliation;

import java.util.List;
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
@WebService(name = "WebAvsAffiliationService", targetNamespace = "http://affiliation.ws.orion.globaz.ch/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WebAvsAffiliationService {


    /**
     * 
     * @param dateFinPeriode
     * @param numeroAffilie
     * @param dateDebutPeriode
     * @return
     *     returns java.lang.Integer
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findCategorieAffiliation", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.FindCategorieAffiliation")
    @ResponseWrapper(localName = "findCategorieAffiliationResponse", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.FindCategorieAffiliationResponse")
    public Integer findCategorieAffiliation(
        @WebParam(name = "NumeroAffilie", targetNamespace = "")
        String numeroAffilie,
        @WebParam(name = "DateDebutPeriode", targetNamespace = "")
        String dateDebutPeriode,
        @WebParam(name = "DateFinPeriode", targetNamespace = "")
        String dateFinPeriode);

    /**
     * 
     * @param numeroAffilie
     * @param annee
     * @return
     *     returns java.util.List<java.lang.Integer>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findActiveSuiviCaisse", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.FindActiveSuiviCaisse")
    @ResponseWrapper(localName = "findActiveSuiviCaisseResponse", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.FindActiveSuiviCaisseResponse")
    public List<Integer> findActiveSuiviCaisse(
        @WebParam(name = "numeroAffilie", targetNamespace = "")
        String numeroAffilie,
        @WebParam(name = "annee", targetNamespace = "")
        String annee);

    /**
     * 
     * @param numeroAffilie
     * @return
     *     returns java.lang.String
     * @throws WebAvsException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findAdresseCourrierAffilie", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.FindAdresseCourrierAffilie")
    @ResponseWrapper(localName = "findAdresseCourrierAffilieResponse", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.FindAdresseCourrierAffilieResponse")
    public String findAdresseCourrierAffilie(
        @WebParam(name = "numeroAffilie", targetNamespace = "")
        String numeroAffilie)
        throws WebAvsException_Exception
    ;

    /**
     * 
     * @param modeDeclarationSalaire
     * @param numeroAffilie
     * @return
     *     returns boolean
     * @throws WebAvsException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "checkAffiliationAndUpdateModeDeclaration", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.CheckAffiliationAndUpdateModeDeclaration")
    @ResponseWrapper(localName = "checkAffiliationAndUpdateModeDeclarationResponse", targetNamespace = "http://affiliation.ws.orion.globaz.ch/", className = "ch.globaz.orion.ws.affiliation.CheckAffiliationAndUpdateModeDeclarationResponse")
    public boolean checkAffiliationAndUpdateModeDeclaration(
        @WebParam(name = "numeroAffilie", targetNamespace = "")
        String numeroAffilie,
        @WebParam(name = "modeDeclarationSalaire", targetNamespace = "")
        ModeDeclarationSalaire modeDeclarationSalaire)
        throws WebAvsException_Exception
    ;

}
