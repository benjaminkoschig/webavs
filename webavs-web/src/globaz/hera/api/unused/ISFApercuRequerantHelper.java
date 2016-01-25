package globaz.hera.api.unused;

import globaz.globall.api.BITransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;

/**
 * 
 * Permet d'obtenir les infos pour un requerant On passe l'idRequerant
 * 
 * <code>
 * 
 * 		System.out.println("Test Retrieve");
 * 		ISFApercuRequerant requerant = (ISFApercuRequerant) session.getAPIFor(ISFApercuRequerant.class);
 * 		requerant.setIdRequerant("108");
 * 		requerant.retrieve(transaction);
 * 		System.out.println("Found ? :" + !requerant.isNew());
 * 		System.out.println(requerant.getNom() + "," + requerant.getPrenom());
 * 
 * </code>
 * 
 * @author ado
 * 
 *         28 sept. 05
 */
public class ISFApercuRequerantHelper extends GlobazHelper implements ISFApercuRequerant {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //
    // No AVS
    /**  **/
    public static final String ENTITY_CLASS_NAME = "globaz.hera.db.famille.SFApercuRequerant";
    public static final String PROP_DATE_DECES = "dateDeces";
    public static final String PROP_DATE_NAISSANCE = "dateNaissance";
    /**  **/
    public static final String PROP_ID_REQUERANT = "idRequerant";

    public static final String PROP_NATIONALITE = "csNationalite";
    /**  **/
    public static final String PROP_NO_AVS = "nSS";
    public static final String PROP_NOM = "nom";
    public static final String PROP_PRENOM = "prenom";
    public static final String PROP_SEXE = "csSexe";

    /**
     * @param implementationClassName
     */
    public ISFApercuRequerantHelper() {
        super(ISFApercuRequerantHelper.ENTITY_CLASS_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getDateDeces()
     */
    @Override
    public String getDateDeces() {
        return this.getProp(ISFApercuRequerantHelper.PROP_DATE_DECES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getDateNaissance()
     */
    @Override
    public String getDateNaissance() {
        return this.getProp(ISFApercuRequerantHelper.PROP_DATE_DECES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getIdRequerant()
     */
    @Override
    public String getIdRequerant() {
        return this.getProp(ISFApercuRequerantHelper.PROP_ID_REQUERANT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getNationalite()
     */
    @Override
    public String getNationalite() {
        return this.getProp(ISFApercuRequerantHelper.PROP_NATIONALITE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getNom()
     */
    @Override
    public String getNom() {
        return this.getProp(ISFApercuRequerantHelper.PROP_NOM);
    }

    @Override
    public String getNumeroAvs() {
        return this.getProp(ISFApercuRequerantHelper.PROP_NO_AVS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getPrenom()
     */
    @Override
    public String getPrenom() {
        return this.getProp(ISFApercuRequerantHelper.PROP_PRENOM);
    }

    /**
     * @param vo
     * @param name
     * @return
     */
    private String getProp(GlobazValueObject vo, String name) {
        Object retValue = vo.getProperty(name);
        if (retValue == null) {
            return "";
        } else {
            return retValue.toString();
        }
    }

    /**
     * @param name
     * @return
     */
    private String getProp(String name) {
        return this.getProp(_getValueObject(), name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#getSexe()
     */
    @Override
    public String getSexe() {
        return this.getProp(ISFApercuRequerantHelper.PROP_SEXE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.shared.GlobazHelper#retrieve(globaz.globall.api.BITransaction )
     */
    @Override
    public void retrieve(BITransaction transaction) throws Exception {
        super.retrieve(transaction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFApercuRequerant#setIdRequerant(java.lang.String)
     */
    @Override
    public void setIdRequerant(String idRequerant) {
        setProp(ISFApercuRequerantHelper.PROP_ID_REQUERANT, idRequerant);
    }

    /**
     * @param name
     * @param value
     */
    private void setProp(String name, Object value) {
        _getValueObject().setProperty(name, value);
    }
}
