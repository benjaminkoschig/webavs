package ch.globaz.hera.businessimpl.services.models.famille;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.exceptions.models.RelationConjointException;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.hera.business.models.famille.RelationConjoint;
import ch.globaz.hera.business.models.famille.SimpleConjoint;
import ch.globaz.hera.business.services.models.famille.MembreFamilleService;
import ch.globaz.hera.business.services.models.famille.RelationConjointService;

/**
 * Handler permettant de rechercher la date de naissance du conjoint
 * via le NNS et une date
 * 
 * @author sce
 * 
 */
public class DateNaissanceConjointHandler {

    private String nSSRequerant = null;
    private String dateDebut = null;
    // private String dateFin = null;
    private final MembreFamilleService membreFamilleService;
    private final RelationConjointService relationConjointService;

    public DateNaissanceConjointHandler(String NSSRequerant, String dadeDebut,
            MembreFamilleService membreFamilleService, RelationConjointService relationConjointService) {
        nSSRequerant = NSSRequerant;
        dateDebut = dadeDebut;
        // this.dateFin = dateFin;
        this.membreFamilleService = membreFamilleService;
        this.relationConjointService = relationConjointService;
        validateParameters();
    }

    protected String handle() throws RelationConjointException, JadePersistenceException, MembreFamilleException {

        DateNaissanceConjoint membreFamilleRequerant = findMembreSFByNSS();

        String idMembreFamilleConjoint = membreFamilleRequerant.getSimpleMembreFamille().getIdMembreFamille();
        RelationConjoint relationRequerant = findRelationForRequerant(idMembreFamilleConjoint);

        if (relationRequerant == null) {
            return "0";
        }

        DateNaissanceConjoint mbrFamille = null;

        // definition de l'id membre famille du conjoint
        String idbrFamConjoint = getIdMembreFamilleConjoint(relationRequerant.getSimpleConjoint(),
                membreFamilleRequerant.getSimpleMembreFamille().getIdMembreFamille());

        mbrFamille = membreFamilleService.readDateNaissanceConjoint(idbrFamConjoint);

        return mbrFamille.getPersonneEtendue().getPersonne().getDateNaissance();
    }

    /**
     * Recherche de l'idMembreFamille qui nous interrese. Etant donne que la relation contient deux idMembreFamille (1
     * et 2!!),
     * on passe l'idMembreFamille du requerant et on recherche celui qui est different dans la relation.
     * 
     * @param conjoint
     * @param idMbrRequerant
     * @return
     * @throws RelationConjointException
     */
    private String getIdMembreFamilleConjoint(SimpleConjoint conjoint, String idMbrRequerant)
            throws RelationConjointException {

        if (conjoint == null) {
            throw new IllegalArgumentException("The conjoint model cannot be null for finding the id conjoint!");
        }
        if (JadeStringUtil.isBlank(idMbrRequerant)) {
            throw new IllegalArgumentException(
                    "The id membreFamilleRequerant cannot be null or empty for finding the id conjoint!");
        }

        if (conjoint.getIdConjoint1().equals(idMbrRequerant)) {
            return conjoint.getIdConjoint2();
        } else if (conjoint.getIdConjoint2().equals(idMbrRequerant)) {
            return conjoint.getIdConjoint1();
        } else {
            // aucune correspondacew trouvé, problème
            throw new RelationConjointException("The idMembreFamilleRequerant[" + idMbrRequerant
                    + " is not present in the id's of the relation, check the data's!");
        }
    }

    protected RelationConjoint findRelationForRequerant(String idConjoint) throws JadePersistenceException,
            RelationConjointException {

        RelationConjoint relation;

        relation = relationConjointService.readRelationForIdMembreFamilleByDate(idConjoint, dateDebut);

        return relation;
    }

    /**
     * Recherche si un membre de famille existe via son nss
     * 
     * @return
     * @throws JadePersistenceException
     * @throws RelationConjointException
     */
    protected DateNaissanceConjoint findMembreSFByNSS() throws JadePersistenceException, RelationConjointException {

        DateNaissanceConjoint conjoint = relationConjointService.readByNss(nSSRequerant);
        return conjoint;
    }

    /**
     * Validation des paramètres d'entrée
     */
    private void validateParameters() {
        if (JadeStringUtil.isBlank(nSSRequerant)) {
            throw new IllegalArgumentException(
                    "Unable to instantiate the date naissance for conjoint the requerant's NSS passed is null or emty");
        }

        if (JadeStringUtil.isBlank(dateDebut)) {
            throw new IllegalArgumentException(
                    "Unable to instantiate the date naissance for conjoint the dateDebut passed is null or emty");
        }

        // if(JadeStringUtil.isBlank(this.dateFin)){
        // throw new
        // IllegalArgumentException("Unable to instantiate the date naissance for conjoint the dateFin passed is null or emty");
        // }
        //
        // if(!JadeDateUtil.isGlobazDate(this.dateFin)){
        // throw new
        // IllegalArgumentException("Unable to instantiate the date naissance for conjoint the dateFin passed is not properly formatted ["
        // + dateFin +"]");
        // }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new IllegalArgumentException(
                    "Unable to instantiate the date naissance for conjoint the dateDebut passed is not properly formatted ["
                            + dateDebut + "]");
        }

    }

    public String getNSSRequerant() {
        return nSSRequerant;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    // public String getDateFin() {
    // return dateFin;
    // }
}
