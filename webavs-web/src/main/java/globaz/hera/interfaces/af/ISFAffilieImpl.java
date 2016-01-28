/*
 * Créé le 8 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.interfaces.af;

import globaz.globall.shared.GlobazValueObject;
import globaz.hera.interfaces.tiers.ISFTiersImpl;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ISFAffilieImpl extends ISFTiersImpl implements ISFAffilie {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    static final String PROPERTY_AFFILIE_NUMERO = "affilieNumero";
    static final String PROPERTY_BRANCHE_ECONOMIQUE = "brancheEconomique";
    static final String PROPERTY_DATE_DEBUT = "dateDebut";
    static final String PROPERTY_DATE_FIN = "dateFin";
    static final String PROPERTY_ID_AFFILIE = "affiliationId";
    static final String PROPERTY_TIERS_AF = "idTiers";
    static final String PROPERTY_TYPE_AFFILIATION = "typeAffiliation";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    ISFAffilieImpl(GlobazValueObject vo, String idAffilie, String numAffilie, String brancheEconomique,
            String typeAffiliation) {
        super(vo);
        vo.setProperty(PROPERTY_ID_AFFILIE, idAffilie);
        vo.setProperty(PROPERTY_NUM_AFFILIE, numAffilie);
        vo.setProperty(PROPERTY_BRANCHE_ECONOMIQUE, brancheEconomique);
        vo.setProperty(PROPERTY_TYPE_AFFILIATION, typeAffiliation);
        vo.setProperty(PROPERTY_DATE_DEBUT, "");
        vo.setProperty(PROPERTY_DATE_FIN, "");
    }

    ISFAffilieImpl(GlobazValueObject vo, String idAffilie, String numAffilie, String brancheEconomique,
            String typeAffiliation, String dateDebut, String dateFin) {
        super(vo);
        vo.setProperty(PROPERTY_ID_AFFILIE, idAffilie);
        vo.setProperty(PROPERTY_NUM_AFFILIE, numAffilie);
        vo.setProperty(PROPERTY_BRANCHE_ECONOMIQUE, brancheEconomique);
        vo.setProperty(PROPERTY_TYPE_AFFILIATION, typeAffiliation);
        vo.setProperty(PROPERTY_DATE_DEBUT, dateDebut);
        vo.setProperty(PROPERTY_DATE_FIN, dateFin);
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

}
