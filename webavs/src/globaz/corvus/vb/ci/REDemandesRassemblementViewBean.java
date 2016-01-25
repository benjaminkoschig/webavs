/*
 * Créé le 31 mai 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.vb.ci;

import globaz.corvus.db.ci.RERassemblementCI;
import globaz.globall.db.BSpy;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author HPE
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class REDemandesRassemblementViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] { new String[] {
            "setIdTiersAyantDroitDepuisPyxis", "idTiers" } };
    private String idDemande = "";
    private String idDemandeRente = "";

    private String idRequerant = "";
    private String idTiersAyantDroitDepuisPyxis = "";

    // la liste des rassemblements CI
    private ArrayList rassemblementsCI = new ArrayList();
    private boolean retourDepuisPyxis;
    // mapping des rassemblement ci et des situations fam.
    private HashMap sitFamRassCI = new HashMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajoute un rassemblement ci a la liste
     * 
     * @param rci
     */
    public void addRassemblementCI(RERassemblementCI rci) {
        rassemblementsCI.add(rci);
    }

    /**
     * Ajoute map une sit fam sur un id rassemblement ci
     * 
     * @param rci
     */
    public void addSitFamToRassemblementCI(ISFMembreFamilleRequerant sitFam, String rasCiId) {
        sitFamRassCI.put(rasCiId, sitFam);
    }

    /**
     * @return
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return
     */
    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * @return
     */
    public String getIdTiersAyantDroitDepuisPyxis() {
        return idTiersAyantDroitDepuisPyxis;
    }

    /**
     * getter pour l'attribut methodes selection ayant droit
     * 
     * @return la valeur courante de l'attribut methodes selection ayant droit
     */
    public Object[] getMethodesSelectionAyantDroit() {
        return METHODES_SEL_BENEFICIAIRE;
    }

    public int getNombreRassemblementsCI() {
        return rassemblementsCI.size();
    }

    /**
     * Donne un iterateur sur la liste des rassemblements ci
     * 
     * @return
     */
    public Iterator getRassemblementsCiIterator() {
        return rassemblementsCI.iterator();
    }

    /**
     * Donne la situation fam. correspondant au rassemblement ci
     * 
     * @return
     */
    public ISFMembreFamilleRequerant getSitFamForRassemblementsCi(String rasCiId) {
        return (ISFMembreFamilleRequerant) sitFamRassCI.get(rasCiId);
    }

    /**
     * getter pour l'attribut spy
     * 
     * @return null
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * @param string
     */
    public void setIdDemande(String string) {
        idDemande = string;
    }

    /**
     * @param string
     */
    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    /**
     * @param string
     */
    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    /**
     * @param string
     */
    public void setIdTiersAyantDroitDepuisPyxis(String string) {
        idTiersAyantDroitDepuisPyxis = string;
        setRetourDepuisPyxis(true);
    }

    /**
     * @param b
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return false;
    }

}
