/*
 * Créé le 19 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.acor.adapter.plat;

import globaz.apg.db.droits.APDroitAPG;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Sous-classe spécifique permettant la création des fichiers nécessaires au calcul des prestations apg. Les fichiers
 * nécessaires et les implémentations utilisées sont:
 * </p>
 * 
 * <table>
 * <tr>
 * <td>fichier DEM_GEDO</td>
 * <td>globaz.prestation.acor.PRFichierVidePrinter</td>
 * </tr>
 * <tr>
 * <td>fichier DEMANDE</td>
 * <td>globaz.prestation.acor.PRFichierDemandeDefautPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier EMPLOYEURS</td>
 * <td>APFichierEmployeurPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier ASSURES_APG</td>
 * <td>APFichierAssuresAPGPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier PERIODES_APG</td>
 * <td>APFichierPeriodesAPGPrinter</td>
 * </tr>
 * </table>
 * 
 * @author vre
 */
public class APACORDroitAPGAdapter extends APAbstractACORDroitAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static String NF_ASSURES_APG = "ASSURES_APG";
    private static String NF_PERIODES_APG = "PERIODES_APG";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private LinkedList fichiers = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAcorDroitAPGAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param droit
     */
    APACORDroitAPGAdapter(BSession session, APDroitAPG droit) {
        super(session, droit);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getDateDepot()
     */
    @Override
    public String getDateDepot() {
        return droit.getDateDepot();
    }

    /**
     * getter pour l'attribut date determinante.
     * 
     * @return la valeur courante de l'attribut date determinante
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String getDateDeterminante() throws PRACORException {
        return droit.getDateDebutDroit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getDateTraitement()
     */
    @Override
    public String getDateTraitement() {
        return null;
    }

    /**
     * getter pour l'attribut fichiers ACOR.
     * 
     * @return la valeur courante de l'attribut fichiers ACOR
     */
    @Override
    public List getFichiersACOR() {
        if (fichiers == null) {
            fichiers = new LinkedList();
            fichiers.add(fichierDemGEDOPrinter());
            fichiers.add(fichierDemandeDefautPrinter());
            fichiers.add(fichierEmployeurPrinter());
            fichiers.add(new APFichierAssuresAPGPrinter(this, NF_ASSURES_APG));
            fichiers.add(new APFichierPeriodesAPGPrinter(this, NF_PERIODES_APG));
        }

        return fichiers;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String idTiersAssure() throws PRACORException {
        return tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
    }

}
