/*
 * Créé le 12 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.vb.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFApercuRelationConjoint;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.nss.SFUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFApercuRelationConjointViewBean extends SFApercuRelationConjoint implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] { new String[] { "idTiersDepuisPyxis",
            "idTiers" }, };

    private String motif = "";
    private boolean retourDepuisPyxis;
    private String pays;

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * Retourne la liste de tous les pays sans la Suisse et avec un élément vide au départ
     * 
     * @return la liste de tous les pays SANS la Suisse et avec un élément vide au départ
     * @throws Exception
     */
    public Vector getPaysDomicile() throws Exception {
        Vector<String[]> allPays = SFTiersHelper.getPays(getSession());
        Vector<String[]> pays = new Vector<String[]>();
        pays.add(new String[] { "", "" });
        for (String[] strings : allPays) {
            if (strings[0] != null && !strings[0].equals("100")) {
                pays.add(strings);
            }
        }
        return pays;
    }

    public String displayEnfants(String contextPath, String detail) {
        StringBuffer listeEnfants = new StringBuffer();

        listeEnfants.append("<table width=\"100%\" height=\"100%\" style=\"borderStyle=solid;border=0\">");

        Iterator it = getEnfants(null);
        while (it.hasNext()) {
            SFApercuEnfant enfant = (SFApercuEnfant) it.next();
            listeEnfants.append("<TR>");
            if (JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                listeEnfants.append("<TD colspan=\"2\"  style=\"borderStyle=solid;border=0\" onclick=\"" + detail
                        + "\">");
            } else {
                listeEnfants.append("<TD  style=\"borderStyle=solid;border=0\" onclick=\"" + detail + "\">");
            }

            listeEnfants.append(enfant.getPrenom());
            listeEnfants.append(" ");
            listeEnfants.append(enfant.getNom());
            listeEnfants.append(" / ");
            listeEnfants.append(enfant.getDateNaissance());

            if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                listeEnfants.append("</TD>");
                listeEnfants.append("<TD  style=\"borderStyle=solid;border=0\" onclick=\"\">");

                listeEnfants.append("<A href=\"" + contextPath
                        + "\\pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=" + enfant.getIdTiers()
                        + "\" class=\"external_link\" target=\"_parent\">Tiers</A>");
            }

            listeEnfants.append("</TD>");
            listeEnfants.append("</TR>");
        }

        listeEnfants.append("</table>");

        return listeEnfants.toString();
    }

    public String getDetailMembreFamilleVG(String idConjoint) {

        return SFUtil.formatDetailMembreFamilleVueGlobale(getNoAvsConjoint(idConjoint), getNomConjoint(idConjoint)
                + " " + getPrenomConjoint(idConjoint), getDateNaissanceConjoint(idConjoint),
                getDateDecesConjoint(idConjoint), getLibelleSexeConjoint(idConjoint),
                getLibellePaysConjoint(idConjoint));

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantNormal(String idConjoint) {

        return SFUtil.formatDetailRequerantListe(getNoAvsConjoint(idConjoint), getNomConjoint(idConjoint) + " "
                + getPrenomConjoint(idConjoint), getDateNaissanceConjoint(idConjoint),
                getLibelleSexeConjoint(idConjoint), getLibellePaysConjoint(idConjoint));

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantSpecial(String idConjoint) {

        return SFUtil.formatDetailRequerantListeSpecial(getNoAvsConjoint(idConjoint), getNomConjoint(idConjoint) + " "
                + getPrenomConjoint(idConjoint), getDateNaissanceConjoint(idConjoint),
                getLibelleSexeConjoint(idConjoint), getLibellePaysConjoint(idConjoint));

    }

    @Override
    public String getLibellePays() {

        String pays = "";

        if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(getPays1())) {
            pays = getPays1();
        } else {
            pays = getNationalite1();
        }

        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", pays));
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'un beneficiaire de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return METHODES_SEL_BENEFICIAIRE;
    }

    public String getMotif() {
        return motif;
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    /**
     * setter pour l'attribut id tiers depuis pyxis
     * 
     * <p>
     * Cette methode initialise un flag interne qui indique que cette methode a ete appellee lors d'un retour depuis
     * pyxis.
     * </p>
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersDepuisPyxis(String idTiers) throws Exception {
        super.setIdTiers1(idTiers);

        SFTiersWrapper tierWrapper = SFTiersHelper.getTiersParId(getSession(), idTiers);

        super.setNoAvs1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        super.setNom1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_NOM));
        super.setPrenom1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_PRENOM));
        super.setSexe1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_SEXE));
        super.setDateNaissance1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE));
        super.setDateDeces1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_DATE_DECES));
        super.setCanton1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON));
        super.setNationalite1(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
        super.setIdConjoint1(idTiers);
        // super.set(PRUtil.PROVENANCE_TIERS);

        retourDepuisPyxis = true;

    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * @param b
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

}
