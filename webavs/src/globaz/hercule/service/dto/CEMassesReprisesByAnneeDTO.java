package globaz.hercule.service.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Fichier de transfert du service d'acces au declaration de salaire.
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEMassesReprisesByAnneeDTO {

    private Map<Integer, BigDecimal> donneesCI = new HashMap<Integer, BigDecimal>();
    private Map<Integer, BigDecimal> donneesAVS = new HashMap<Integer, BigDecimal>();

    /**
     * Constructeur de CEMassesReprisesDTO
     * 
     * @param donneesCI
     * @param donneesAVS
     */
    public CEMassesReprisesByAnneeDTO(Map<Integer, BigDecimal> donneesCI, Map<Integer, BigDecimal> donneesAVS) {
        super();
        this.donneesCI = donneesCI;
        this.donneesAVS = donneesAVS;
    }

    /**
     * Constructeur de CEMassesReprisesDTO
     */
    public CEMassesReprisesByAnneeDTO() {
        super();
    }

    /**
     * Ajoute une donnée de CI pour une année
     * 
     * @param annee
     * @param nbCI
     */
    public void addDonneesCI(Integer annee, BigDecimal nbCI) {
        donneesCI.put(annee, nbCI);
    }

    /**
     * Ajoute une donnée de masse pour une année
     * 
     * @param annee
     * @param masse
     */
    public void addDonneesAVS(Integer annee, BigDecimal masse) {
        donneesAVS.put(annee, masse);
    }

    /**
     * Getter de donneesCI
     * 
     * @return the donneesCI
     */
    public Map<Integer, BigDecimal> getDonneesCI() {
        return donneesCI;
    }

    /**
     * Getter de donneesAVS
     * 
     * @return the donneesAVS
     */
    public Map<Integer, BigDecimal> getDonneesAVS() {
        return donneesAVS;
    }

}
