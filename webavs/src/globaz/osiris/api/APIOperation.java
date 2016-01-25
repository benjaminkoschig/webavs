package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;

/**
 * Prototype d'une opération de base <br>
 * Date de création : (17.01.2002 17:09:48)
 */
public interface APIOperation extends BIEntity {
    public static final String CAAUXILIAIRE = "A";
    public static final String CAAUXILIAIRE_PAIEMENT = "AP";
    public static final String CAECRITURE = "E";
    public static final String CAECRITURECOMPENSATION = "EC";
    public static final String CAECRITURELISSAGE = "EL";
    public static final String CAOPERATIONBULLETINNEUTRE = "B";
    public static final String CAOPERATIONCONTENTIEUX = "C";
    public static final String CAOPERATIONCONTENTIEUXAQUILA = "D";

    public static final String CAOPERATIONORDRERECOUVREMENT = "R";
    public static final String CAOPERATIONORDREVERSEMENT = "V";
    public static final String CAOPERATIONORDREVERSEMENTAVANCE = "VA";

    public static final String CAPAIEMENT = "EP";
    public static final String CAPAIEMENTBVR = "EPB";
    public static final String CAPAIEMENTETRANGER = "P";
    public static final String CARECOUVREMENT = "EPR";
    public static final String CAVERSEMENT = "EPV";
    public static final String CAVERSEMENTAVANCE = "EPVA";
    public static final String ETAT_COMPTABILISE = "205002";
    public static final String ETAT_ERREUR = "205003";
    public static final String ETAT_ERREUR_VERSEMENT = "205007";
    public static final String ETAT_INACTIF = "205005";
    public static final String ETAT_OUVERT = "205001";
    public static final String ETAT_PROVISOIRE = "205004";
    public static final String ETAT_TRAITE = "205008";
    public static final String ETAT_VERSE = "205006";
    public static final String MASTER = "2";

    public static final String PROVPMT_ACOMPTEOF = "249003";
    public static final String PROVPMT_ACOMPTEOP = "249001";
    public static final String PROVPMT_SOLDEOF = "249004";
    public static final String PROVPMT_SOLDEOP = "249002";

    public static final String SECTION_COMPENSATION_DE = "D";
    public static final String SECTION_COMPENSATION_SUR = "S";

    public static final String SINGLE = "1";
    public static final String SLAVE = "3";

    /**
     * Code système du canton de l'assurance.
     * 
     * @return the assuranceCanton
     */
    public String getAssuranceCanton();

    String getCodeMaster();

    /**
     * Date de création : (17.01.2002 17:27:05)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCode
     */
    FWParametersSystemCode getCsEtat();

    /**
     * Date de création : (17.01.2002 17:27:23)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    FWParametersSystemCodeManager getCsEtats();

    String getDate();

    String getEtat();

    /**
     * L'identifiant de l'assurance définit dans AFASUPP
     * 
     * @return the idAssurance
     */
    public String getIdAssurance();

    String getIdCompteAnnexe();

    String getIdJournal();

    /**
     * Date de création : (17.01.2002 17:13:01)
     * 
     * @return String
     */
    String getIdOperation();

    String getIdSection();

    String getIdSectionCompensation();

    public String getIdTypeOperation();

    String getSectionCompensationDeSur();

    /**
     * Date de création : (17.01.2002 17:27:42)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    FWParametersUserCode getUcEtat();

    /**
     * Modifie le code système du canton de l'assurance stoqué dans l'opération (CAOPERP).
     * 
     * @param assuranceCanton
     *            the assuranceCanton to set
     */
    public void setAssuranceCanton(String assuranceCanton);

    void setDate(String newDate);

    /**
     * @param idAssurance
     *            the idAssurance to set
     */
    public void setIdAssurance(String idAssurance);

    void setIdCompteAnnexe(String newCompteAnnexe);

    void setIdSection(String newSection);

    void setIdSectionCompensation(String idSectionCompensation);

    /**
     * Date de création : (24.01.2002 13:12:55)
     * 
     * @param newIdTypeOperation
     *            String
     */
    void setIdTypeOperation(String newIdTypeOperation);

    void setSectionCompensationDeSur(String deSur);
}
