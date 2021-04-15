package globaz.caisse.helper;

import globaz.caisse.report.helper.CaisseReportHelper;
import globaz.caisse.report.helper.CaisseReportHelperOO;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.db.tiers.TIRole;
import globaz.webavs.common.CommonProperties;
import java.util.Hashtable;

/**
 * Classe : Factory class
 * 
 * Description : Helper factory class. Fournit des helper class pour des besoins spécifique aux différentes caisses.
 * 
 * Date de création: 10 août 04
 * 
 * @author scr
 * 
 */
public class CaisseHelperFactory {

    private static ITIAdministration admin = null;
    public final static String BRANCHE_ECO_ASSISTE = "19150029";
    /** Rôle d'un tiers */
    public final static String CS_AFFILIE_PARITAIRE = "517039";

    /** Rôle d'un tiers */
    public final static String CS_AFFILIE_PERSONNEL = "517040";
    /** Genre administration caisse compensation */
    public final static String CS_CAISSE_COMPENSATION = "509001";
    /** Rôle d'un tiers */
    public final static String CS_RENTIER = "517038";
    private static CaisseHelperFactory instance = null;

    public static final String PLUSIEURS_TYPE_AFFILIE = "plusieursTypeAffilie";
    private static final String PROP_DEFAULT_CANTON_CAISSE_LOCATION = "default.canton.caisse.location";

    /**
     * Factory pattern. Return l'instance de l'objet CaisseHelperFactory.
     * 
     * @return
     * @throws Exception
     */
    public synchronized static CaisseHelperFactory getInstance() throws Exception {
        if (instance == null) {
            instance = new CaisseHelperFactory();
        }
        return instance;
    }

    /**
     * Constructor for CaisseHelperFactory.
     */
    private CaisseHelperFactory() throws Exception {
    }

    public ITIAdministration getAdministrationCaisse(BSession local) {
        if (admin == null) {
            try {
                // création de l'API
                ITIAdministration adminIfc = (ITIAdministration) local.getAPIFor(ITIAdministration.class);
                Hashtable params = new Hashtable();
                params.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, CS_CAISSE_COMPENSATION);
                String caisse = JadeStringUtil.stripLeading(getNoCaisse(local.getApplication()), '0');
                if (!JadeStringUtil.isIntegerEmpty(getNoAgence(local.getApplication()))) {
                    caisse += ".";
                    caisse += JadeStringUtil.stripLeading(getNoAgence(local.getApplication()), '0');
                }
                params.put(ITIAdministration.FIND_FOR_CODE_ADMINISTRATION, caisse);

                Object[] res = adminIfc.find(params);
                if (!local.hasErrors()) {
                    if (res.length > 0) {
                        // tiers trouvé
                        if (res[0] instanceof GlobazValueObject) {
                            GlobazValueObject globazObj = (GlobazValueObject) res[0];
                            admin = (ITIAdministration) local.getAPIFor(ITIAdministration.class.getName(), globazObj);
                        }
                    }
                }
            } catch (Exception ex) {
                admin = null;
            }
        }
        return admin;
    }

    public ICaisseReportHelper getCaisseReportHelper(JadePublishDocumentInfo docInfo, BApplication application,
            String codeIsoLangue) {
        ICaisseReportHelper implementation = null;
        implementation = new CaisseReportHelper(docInfo);
        implementation.init(application, codeIsoLangue);
        return implementation;
    }

    public ICaisseReportHelperOO getCaisseReportHelperOO(BApplication application, String codeIsoLangue) {
        ICaisseReportHelperOO implementation = null;
        implementation = new CaisseReportHelperOO();
        implementation.init(application, codeIsoLangue);
        return implementation;
    }

    /**
     * Return le code system du canton de la caisse.
     * 
     * @param application
     * @return
     */
    public String getCsDefaultCantonCaisse(BApplication application) {
        return application.getProperty(PROP_DEFAULT_CANTON_CAISSE_LOCATION);
    }

    /**
     * Return le numéro d'agence de l'application.
     * 
     * @param application
     * @return
     */
    public String getNoAgence(BApplication application) {
        return application.getProperty(CommonProperties.KEY_NO_AGENCE);
    }

    /**
     * Return le numéro de caisse de l'application.
     * 
     * @param application
     * @return
     */
    public String getNoCaisse(BApplication application) {
        return application.getProperty(CommonProperties.KEY_NO_CAISSE);
    }

    /**
     * Return le numéro de caisse formaté de l'application.
     * 
     * @param application
     * @return
     */
    public String getNoCaisseFormatee(BApplication application) {
        return application.getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
    }

    public boolean getPlusieursTypeAffilie(BApplication application) {
        return (application.getProperty(PLUSIEURS_TYPE_AFFILIE).toLowerCase().equals("true"));
    }

    public String getRoleForAffilieParitaire(BApplication application) {
        if (getPlusieursTypeAffilie(application)) {
            return CS_AFFILIE_PARITAIRE;
        } else {
            return TIRole.CS_AFFILIE;
        }
    }

    public String getRoleForAffiliePersonnel(BApplication application) {
        if (getPlusieursTypeAffilie(application)) {
            return CS_AFFILIE_PERSONNEL;
        } else {
            return TIRole.CS_AFFILIE;
        }
    }
}
