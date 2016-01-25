package globaz.ij.acor.adapter.plat;

import globaz.commons.nss.NSUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFichierEuroFormPrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private final static int STATE_DEBUT = 10;
    private final static int STATE_FIN = 100;

    private int state = IJFichierEuroFormPrinter.STATE_DEBUT;

    /*
     * }
     * 
     * Crée une nouvelle instance de la classe IJFichierIJPrinter.
     * 
     * @param parent DOCUMENT ME!
     * 
     * @param nomFichier DOCUMENT ME!
     */
    public IJFichierEuroFormPrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;

    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {

        switch (state) {
            case STATE_DEBUT:
                state = IJFichierEuroFormPrinter.STATE_FIN;
                return true;
            case STATE_FIN:
                return false;
        }
        return false;
    }

    private String nssAssure() throws PRACORException {
        // Recherche du tiers requérant...

        PRTiersWrapper tw = null;
        String nssRequerant = "";
        String idTiersRequerant = "";
        try {
            idTiersRequerant = adapter().getPrononce().loadDemande(null).getIdTiers();
            tw = PRTiersHelper.getTiersParId(getSession(), idTiersRequerant);
            nssRequerant = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            nssRequerant = NSUtil.unFormatAVS(nssRequerant);

        } catch (Exception e) {
            throw new PRACORException("NSS Requérant non trouvé pour idTiers : " + idTiersRequerant);
        }
        return nssRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer )
     */
    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {

        // 1. NSS
        this.writeChaine(cmd, nssAssure());
        // 2. Adresse assuré, rue et no
        this.writeChaine(cmd, "ma rue");
        // 3. Adresse assuré, localite
        this.writeChaine(cmd, "Localite");
        // 4. Adresse assuré, code postal
        this.writeChaine(cmd, "1000");
        // 5. Adresse assuré, pays
        this.writeChaine(cmd, "100");
        // 6. banque, nom titulaire
        this.writeChaine(cmd, "Titulaire");
        // 7. banque, nom etablissement
        this.writeChaine(cmd, "Die Schweizerische Post");
        // 8. banque, rue
        this.writeChaine(cmd, "Strasse");
        // 9. banque, ville
        this.writeChaine(cmd, "Bern");
        // 10. banque,code postal
        this.writeChaine(cmd, "3030");
        // 11. banque, pays
        this.writeChaine(cmd, "100");
        // 12. banque,code BIC/SWIFT
        this.writeChaine(cmd, "POFICHBEXXX");
        // 13. banque,compte IBAN
        this.writeChaine(cmd, "PT50003300004521774591405");
    }
}
