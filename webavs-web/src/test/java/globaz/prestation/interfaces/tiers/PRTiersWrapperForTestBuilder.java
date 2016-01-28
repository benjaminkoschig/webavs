package globaz.prestation.interfaces.tiers;

import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Permet de construire des {@link PRTiersWrapper} pour des cas de testes.<br/>
 * Dans le cas d'une construction avec valeurs par d�faut, les valeurs utilis�es sont d�finis dans des champs public
 * static de cette classe.
 * 
 * @author PBA
 */
public class PRTiersWrapperForTestBuilder {

    public static final String CANTON_PAR_DEFAUT = "505026";
    public static final String DATE_DECES_PAR_DEFAUT = "01.01.1970";
    public static final String DATE_NAISSANCE_PAR_DEFAUT = "0";
    private static TITiersViewBean defaultData = null;
    public static final String ETAT_CIVIL_PAR_DEFAUT = "515002";
    public static final String ID_PAYS_PAR_DEFAUT = "100";
    public static final String ID_TIERS_PAR_DEFAUT = "0";
    public static final Boolean IS_INACTIF_PAR_DEFAULT = Boolean.FALSE;
    public static final String LANGUE_PAR_DEFAULT = "503001";
    public static final String NOM_PAR_DEFAUT = "NomDeTest";
    public static final String NUMERO_AVS_PAR_DEFAUT = "756.0000.0000.00";
    public static final String PRENOM_PAR_DEFAUT = "PrenomDeTest";
    public static final String SEXE_PAR_DEFAUT = "516001";
    public static final String TITRE_TIERS_PAR_DEFAUT = "TitredeTest";

    private static TITiersViewBean buildDefaultData() {
        TITiersViewBean defaultData = new TITiersViewBean();

        defaultData.setDateNaissance(PRTiersWrapperForTestBuilder.DATE_NAISSANCE_PAR_DEFAUT);
        defaultData.setDateDeces(PRTiersWrapperForTestBuilder.DATE_DECES_PAR_DEFAUT);
        defaultData.setCanton(PRTiersWrapperForTestBuilder.CANTON_PAR_DEFAUT);
        defaultData.setIdPays(PRTiersWrapperForTestBuilder.ID_PAYS_PAR_DEFAUT);
        defaultData.setIdTiers(PRTiersWrapperForTestBuilder.ID_TIERS_PAR_DEFAUT);
        defaultData.setDesignation1(PRTiersWrapperForTestBuilder.NOM_PAR_DEFAUT);
        defaultData.setNumAvsActuel(PRTiersWrapperForTestBuilder.NUMERO_AVS_PAR_DEFAUT);
        defaultData.setEtatCivil(PRTiersWrapperForTestBuilder.ETAT_CIVIL_PAR_DEFAUT);
        defaultData.setDesignation2(PRTiersWrapperForTestBuilder.PRENOM_PAR_DEFAUT);
        defaultData.setSexe(PRTiersWrapperForTestBuilder.SEXE_PAR_DEFAUT);
        defaultData.setTitreTiers(PRTiersWrapperForTestBuilder.TITRE_TIERS_PAR_DEFAUT);
        defaultData.setLangue(PRTiersWrapperForTestBuilder.LANGUE_PAR_DEFAULT);
        defaultData.setInactif(PRTiersWrapperForTestBuilder.IS_INACTIF_PAR_DEFAULT);

        return defaultData;
    }

    /**
     * Retourne une copie des donn�es par d�faut pour une {@link PRTiersWrapper} de teste
     * 
     * @return une copie des donn�es par d�faut
     */
    public static TITiersViewBean getDefaultData() {
        return PRTiersWrapperForTestBuilder.buildDefaultData();
    }

    /**
     * Construit et retourne un {@link PRTiersWrapper} avec les valeurs par d�faut pour les testes.
     * 
     * @return un {@link PRTiersWrapper} avec les valeurs par d�faut.
     */
    public static PRTiersWrapper getDefaultWrapper() {
        if (PRTiersWrapperForTestBuilder.defaultData == null) {
            PRTiersWrapperForTestBuilder.defaultData = PRTiersWrapperForTestBuilder.buildDefaultData();
        }
        return new PRTiersWrapper(PRTiersWrapperForTestBuilder.defaultData);
    }

    /**
     * Construit et retourne un {@link PRTiersWrapper} dont les donn�es sont vide (cha�nes vides).
     * 
     * @return un {@link PRTiersWrapper} dont les donn�es sont vide (cha�nes vides)
     */
    public static PRTiersWrapper getEmptyWrapper() {
        return new PRTiersWrapper(new TITiersViewBean());
    }

    /**
     * Construit et retourne un {@link PRTiersWrapper} depuis les donn�es du viewBean.<br/>
     * Les donn�es utilis�es sont :
     * <ul>
     * <li>DateNaissance</li>
     * <li>DateDeces</li>
     * <li>Canton</li>
     * <li>IdPays</li>
     * <li>IdTiers</li>
     * <li>Designation1</li>
     * <li>NumAvsActuel</li>
     * <li>EtatCivil</li>
     * <li>Designation2</li>
     * <li>Sexe</li>
     * <li>TitreTiers</li>
     * <li>Langue</li>
     * <li>Inactif</li>
     * </ul>
     * 
     * @param data
     *            un {@link TITiersViewBean} contenant les donn�es qui seront utilis�es pour construire un
     *            {@link PRTiersWrapper}
     * @return un {@link PRTiersWrapper} construit avec les donn�es du viewBean pass� en param�tre
     */
    public static PRTiersWrapper getWrapper(TITiersViewBean data) {
        return new PRTiersWrapper(data);
    }
}
