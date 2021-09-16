/*
 * Créé le 22 sept. 05
 */
package globaz.ij.vb.prestations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeJointGrandePetite;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJIJCalculeeJointGrandePetiteViewBean extends IJIJCalculeeJointGrandePetite implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List idsIJCalculees = new ArrayList();
    private IJIndemniteJournaliere indemniteJournaliereAit = null;
    private IJIndemniteJournaliere indemniteJournaliereExterne = null;

    private IJIndemniteJournaliere indemniteJournaliereInterne = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        // chatgement des IJ
        indemniteJournaliereExterne = loadIndemnitesJournalieresExternes();
        if (indemniteJournaliereExterne == null) {
            indemniteJournaliereExterne = new IJIndemniteJournaliere();
        }

        indemniteJournaliereInterne = loadIndemnitesJournalieresInternes();
        if (indemniteJournaliereInterne == null) {
            indemniteJournaliereInterne = new IJIndemniteJournaliere();
        }

        if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(getCsTypeIJ())) {
            // les AIT n'ont pas de valeur pour csTypeIndemnite
            if (!JadeStringUtil.isIntegerEmpty(getIdIJCalculee())) {

                IJIndemniteJournaliereManager indemnites = new IJIndemniteJournaliereManager();
                indemnites.setSession(getSession());
                indemnites.setForIdIJCalculee(getIdIJCalculee());
                indemnites.find();

                if (!indemnites.isEmpty()) {
                    indemniteJournaliereAit = (IJIndemniteJournaliere) indemnites.get(0);
                }
            }
        }

        if (indemniteJournaliereAit == null) {
            indemniteJournaliereAit = new IJIndemniteJournaliere();
        }

        // initialisation de la liste des ids
        idsIJCalculees = new ArrayList();

        IJIJCalculeeManager IJCalculeeManager = new IJIJCalculeeManager();
        IJCalculeeManager.setSession(getSession());
        IJCalculeeManager.setForIdPrononce(getIdPrononce());
        IJCalculeeManager.setOrderBy(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        IJCalculeeManager.find(transaction, BManager.SIZE_NOLIMIT);

        IJIJCalculee ijCalculee = null;

        for (int i = 0; i < IJCalculeeManager.size(); i++) {
            ijCalculee = (IJIJCalculee) IJCalculeeManager.getEntity(i);
            idsIJCalculees.add(ijCalculee.getIdIJCalculee());
        }
    }

    public String getCsTypeIJLibelle() {
        return getSession().getCodeLibelle(getCsTypeIJ());
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVSRequerant());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(
                    getNoAVSRequerant(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut id IJCalculee precedente
     * 
     * @return la valeur courante de l'attribut id IJCalculee precedente
     */
    public String getIdIJCalculeePrecedente() {
        int index = idsIJCalculees.lastIndexOf(getIdIJCalculee());

        try {
            return (String) idsIJCalculees.get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            // on renvoie le premier index
            return (String) idsIJCalculees.get(index);
        }
    }

    /**
     * getter pour l'attribut id IJCalculee suivante
     * 
     * @return la valeur courante de l'attribut id IJCalculee suivante
     */
    public String getIdIJCalculeeSuivante() {
        int index = idsIJCalculees.lastIndexOf(getIdIJCalculee());

        try {
            return (String) idsIJCalculees.get(index + 1);
        } catch (IndexOutOfBoundsException e) {
            return (String) idsIJCalculees.get(index);
        }
    }

    /**
     * getter pour l'attribut indemnite journaliere Ait
     * 
     * @return la valeur courante de l'attribut indemnite journaliere Ait. Jamais null après un retrieve.
     */
    public IJIndemniteJournaliere getIndemniteJournaliereAit() {
        return indemniteJournaliereAit;
    }

    /**
     * getter pour l'attribut indemnite journaliere externe
     * 
     * @return la valeur courante de l'attribut indemnite journaliere externe. Jamais null après un retrieve.
     */
    public IJIndemniteJournaliere getIndemniteJournaliereExterne() {
        return indemniteJournaliereExterne;
    }

    /**
     * getter pour l'attribut indemnite journaliere interne
     * 
     * @return la valeur courante de l'attribut indemnite journaliere interne. Jamais null après un retrieve.
     */
    public IJIndemniteJournaliere getIndemniteJournaliereInterne() {
        return indemniteJournaliereInterne;
    }

    /**
     * getter pour l'attribut montant supplementaire readaptation interne
     * 
     * @return la valeur courante de l'attribut montant supplementaire readaptation interne
     */
    public String getMontantSupplementaireReadaptationInterne() {
        try {
            if (loadIndemnitesJournalieresInternes() != null) {
                return loadIndemnitesJournalieresInternes().getMontantSupplementaireReadaptation();
            } else {
                return "0";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVSRequerant() {
        try {
            return loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } catch (Exception e) {
            return getSession().getLabel("NO_AVS_INTROUVABLE");
        }
    }

    /**
     * getter pour l'attribut prenom nom
     * 
     * @return la valeur courante de l'attribut prenom nom
     */
    public String getPrenomNom() {
        try {
            return loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_PRENOM) + ", "
                    + loadPrononce(null).loadDemande(null).loadTiers().getProperty(PRTiersWrapper.PROPERTY_NOM);
        } catch (Exception e) {
            return getSession().getLabel("NOM_INTROUVABLE");
        }
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return super.getSpy();
    }

    /**
     * getter pour l'attribut date prononce
     * 
     * @return la valeur courante de l'attribut date prononce
     */
    public String getVraieDatePrononce() {
        try {
            String d = loadPrononce(null).getDatePrononce();

            // pour les prononce AIT
            if (JadeStringUtil.isEmpty(d)) {
                d = loadPrononce(null).getDateDebutPrononce();
            }

            return d;
        } catch (Exception e) {
            return getSession().getLabel("DATE_PRONONCE_INTROUVABLE");
        }
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean hasIJExterne() throws Exception {
        return loadIndemnitesJournalieresExternes() != null;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean hasIJInterne() throws Exception {
        return loadIndemnitesJournalieresInternes() != null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasSpy()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return super.hasSpy();
    }

    /**
     * getter pour l'attribut dernier element
     * 
     * @return la valeur courante de l'attribut dernier element
     */
    public boolean isDernierElement() {
        return idsIJCalculees.lastIndexOf(getIdIJCalculee()) == (idsIJCalculees.size() - 1);
    }

    /**
     * Vrais si l'on peut modifier les IJ calculees
     * 
     * @return
     */
    public boolean isModifierPermis() {
        return IIJPrononce.CS_PETITE_IJ.equals(getCsTypeIJ()) || IIJPrononce.CS_GRANDE_IJ.equals(getCsTypeIJ()) || IIJPrononce.CS_FPI.equals(getCsTypeIJ());
    }

    /**
     * getter pour l'attribut premier element
     * 
     * @return la valeur courante de l'attribut premier element
     */
    public boolean isPremierElement() {
        return idsIJCalculees.lastIndexOf(getIdIJCalculee()) == 0;
    }

}
