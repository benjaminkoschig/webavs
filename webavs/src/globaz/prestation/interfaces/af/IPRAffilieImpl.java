package globaz.prestation.interfaces.af;

import globaz.globall.shared.GlobazValueObject;
import globaz.prestation.interfaces.tiers.IPRTiersImpl;

public class IPRAffilieImpl extends IPRTiersImpl implements IPRAffilie {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    static final String PROPERTY_AFFILIE_NUMERO = "affilieNumero";
    static final String PROPERTY_BRANCHE_ECONOMIQUE = "brancheEconomique";
    static final String PROPERTY_DATE_DEBUT = "dateDebut";
    static final String PROPERTY_DATE_FIN = "dateFin";
    static final String PROPERTY_ID_AFFILIE = "affiliationId";
    static final String PROPERTY_TIERS_AF = "idTiers";
    static final String PROPERTY_TYPE_AFFILIATION = "typeAffiliation";
    static final String PROPERTY_STATUT_IDE = "ideStatut";
    static final String PROPERTY_NUMERO_IDE = "numeroIDE";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    IPRAffilieImpl(GlobazValueObject vo, String idAffilie, String numAffilie, String brancheEconomique,
            String typeAffiliation, String numeroIDE, String ideStatut) {
        super(vo);
        vo.setProperty(PROPERTY_ID_AFFILIE, idAffilie);
        vo.setProperty(PROPERTY_NUM_AFFILIE, numAffilie);
        vo.setProperty(PROPERTY_BRANCHE_ECONOMIQUE, brancheEconomique);
        vo.setProperty(PROPERTY_TYPE_AFFILIATION, typeAffiliation);
        vo.setProperty(PROPERTY_DATE_DEBUT, "");
        vo.setProperty(PROPERTY_DATE_FIN, "");
        vo.setProperty(PROPERTY_NUMERO_IDE, numeroIDE);
        vo.setProperty(PROPERTY_STATUT_IDE, ideStatut);
    }

    IPRAffilieImpl(GlobazValueObject vo, String idAffilie, String numAffilie, String brancheEconomique,
            String typeAffiliation, String dateDebut, String dateFin, String numeroIDE, String ideStatut) {
        super(vo);
        vo.setProperty(PROPERTY_ID_AFFILIE, idAffilie);
        vo.setProperty(PROPERTY_NUM_AFFILIE, numAffilie);
        vo.setProperty(PROPERTY_BRANCHE_ECONOMIQUE, brancheEconomique);
        vo.setProperty(PROPERTY_TYPE_AFFILIATION, typeAffiliation);
        vo.setProperty(PROPERTY_DATE_DEBUT, dateDebut);
        vo.setProperty(PROPERTY_DATE_FIN, dateFin);
        vo.setProperty(PROPERTY_NUMERO_IDE, numeroIDE);
        vo.setProperty(PROPERTY_STATUT_IDE, ideStatut);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.prestation.interfaces.af.IPRAffilie#getBrancheEconomique()
     */
    @Override
    public String getBrancheEconomique() {
        return getProperty(PROPERTY_BRANCHE_ECONOMIQUE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.af.IPRAffilie#getDateDebut()
     */
    @Override
    public String getDateDebut() {
        return getProperty(PROPERTY_DATE_DEBUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.af.IPRAffilie#getDateFin()
     */
    @Override
    public String getDateFin() {
        return getProperty(PROPERTY_DATE_FIN);
    }

    /**
     * cette méthode n'a pas de sens pour un affilie
     * 
     * @return chaine vide.
     * 
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getDateNaissance()
     */
    @Override
    public String getDateNaissance() {
        return EMPTY_STRING;
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRAffilie#getIdAffilie()
     */
    @Override
    public String getIdAffilie() {
        return getProperty(PROPERTY_ID_AFFILIE);
    }

    /**
     * @see globaz.prestation.interfaces.tiers.IPRAffilie#getNumAffilie()
     */
    @Override
    public String getNumAffilie() {
        return getProperty(PROPERTY_NUM_AFFILIE);
    }

    /**
     * cette méthode n'a pas de sens pour un affilié
     * 
     * @return chaine vide.
     * 
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getPrenom()
     */
    @Override
    public String getPrenom() {
        return EMPTY_STRING;
    }

    /**
     * cette méthode n'a pas de sens pour un affilié
     * 
     * @return chaine vide.
     * 
     * @see globaz.prestation.interfaces.tiers.IPRTiers#getSexe()
     */
    @Override
    public String getSexe() {
        return EMPTY_STRING;
    }

    @Override
    public String getTypeAffiliation() {
        return getProperty(PROPERTY_TYPE_AFFILIATION);
    }

    @Override
    public String getNumeroIDE() {
        return getProperty(PROPERTY_NUMERO_IDE);
    }

    @Override
    public String getIdeStatut() {
        return getProperty(PROPERTY_STATUT_IDE);
    }

}
