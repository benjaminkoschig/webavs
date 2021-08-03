/*
 * Créé le 27 mai 05
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APDroitAPGDTO extends APDroitDTO {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idSituationFam = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitAPGDTO.
     * 
     * @param droitDTO
     *            DOCUMENT ME!
     * @param idSituationFam
     *            DOCUMENT ME!
     */
    public APDroitAPGDTO(APDroitDTO droitDTO, String idSituationFam) {
        setIdDroit(droitDTO.getIdDroit());
        setDateDebutDroit(droitDTO.getDateDebutDroit());
        setDateDebutDroitDelaiCadre(droitDTO.getDateDebutDroitDelaiCadre());
        setModifiable(droitDTO.isModifiable());
        setNoAVS(droitDTO.getNoAVS());
        setNomPrenom(droitDTO.getNomPrenom());
        this.idSituationFam = idSituationFam;
    }

    /**
     * Crée une nouvelle instance de la classe APDroitAPGDTO.
     * 
     * @param droit
     *            DOCUMENT ME!
     */
    public APDroitAPGDTO(APDroitLAPG droit) {
        super(droit);
        if (droit instanceof APDroitAPG) {
            idSituationFam = ((APDroitAPG) droit).getIdSituationFam();
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id situation fam
     * 
     * @return la valeur courante de l'attribut id situation fam
     */
    public String getIdSituationFam() {
        return idSituationFam;
    }

    /**
     * setter pour l'attribut id situation fam
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFam(String string) {
        idSituationFam = string;
    }
}
