/*
 * Créé le 8 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.vb.process;

import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessCreerARDViewBean extends COAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List /* CACompteAnnexe */comptes = new LinkedList();
    private String eMailAddress = "";
    private String idSectionPrincipale;
    private String libelleJournal = "";
    private Map /* String, String */montantsParAdmin = new HashMap();
    private CASection section;

    private List /* COSequence */sequences;
    private Map /* String, String */sequencesParAdmin = new HashMap();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COProcessCreerARDViewBean.
     */
    public COProcessCreerARDViewBean() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut comptes
     * 
     * @param ca
     *            une nouvelle valeur pour cet attribut
     */
    public void addCompte(CACompteAnnexe ca) {
        comptes.add(ca);
    }

    /**
     * setter pour l'attribut montants resps
     * 
     * @param idCAAdmin
     *            une nouvelle valeur pour cet attribut
     * @param montant
     *            DOCUMENT ME!
     */
    public void addMontantParAdmin(String idCAAdmin, String montant) {
        montantsParAdmin.put(idCAAdmin, JANumberFormatter.deQuote(montant));
    }

    /**
     * setter pour l'attribut montants resps
     * 
     * @param idCAAdmin
     *            une nouvelle valeur pour cet attribut
     * @param idSequence
     *            DOCUMENT ME!
     */
    public void addSequenceParAdmin(String idCAAdmin, String idSequence) {
        sequencesParAdmin.put(idCAAdmin, idSequence);
    }

    /**
     * getter pour l'attribut comptes
     * 
     * @return la valeur courante de l'attribut comptes
     */
    public List getComptes() {
        return comptes;
    }

    /**
     * getter pour l'attribut description CA
     * 
     * @return la valeur courante de l'attribut description CA
     */
    public String getDescriptionCA() {
        return section.getCompteAnnexe().getIdExterneRole() + " " + section.getCompteAnnexe().getDescription();
    }

    /**
     * getter pour l'attribut description section
     * 
     * @return la valeur courante de l'attribut description section
     */
    public String getDescriptionSection() {
        return section.getIdExterne() + " " + section.getDescription();
    }

    /**
     * getter pour l'attribut EMail adress
     * 
     * @return la valeur courante de l'attribut EMail adress
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return idSectionPrincipale;
    }

    /**
     * getter pour l'attribut id section
     * 
     * @return la valeur courante de l'attribut id section
     */
    public String getIdSectionPrincipale() {
        return idSectionPrincipale;
    }

    /**
     * getter pour l'attribut libelle journal
     * 
     * @return la valeur courante de l'attribut libelle journal
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * getter pour l'attribut montants resps
     * 
     * @return la valeur courante de l'attribut montants resps
     */
    public Map /* String, String */getMontantsParAdmin() {
        return montantsParAdmin;
    }

    /**
     * getter pour l'attribut nom administrateur
     * 
     * @param ca
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut nom administrateur
     */
    public String getNomAdministrateur(CACompteAnnexe ca) {
        return ca.getTiers().getPrenomNom();
    }

    public String getRemarque() {
        return section.getRemarque().getTexte();
    }

    /**
     * getter pour l'attribut section
     * 
     * @return la valeur courante de l'attribut section
     */
    public CASection getSection() {
        return section;
    }

    /**
     * getter pour l'attribut sequences
     * 
     * @return la valeur courante de l'attribut sequences
     */
    public List /* COSequence */getSequences() {
        return sequences;
    }

    /**
     * getter pour l'attribut sequences par admin
     * 
     * @return la valeur courante de l'attribut sequences par admin
     */
    public Map /* String, String */getSequencesParAdmin() {
        return sequencesParAdmin;
    }

    /**
	 */
    public void reset() {
        comptes.clear();
        montantsParAdmin.clear();
        sequencesParAdmin.clear();
    }

    /**
     * setter pour l'attribut EMail adress
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idSectionPrincipale = newId;
    }

    /**
     * setter pour l'attribut id section
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSectionPrincipale(String string) {
        idSectionPrincipale = string;
    }

    /**
     * setter pour l'attribut libelle journal
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    public void setRemarque(String remarque) {
        section.setTexteRemarque(remarque);
        try {
            section.save();
        } catch (Exception e) {
            _addError(((BSession) getISession()).getLabel("AQUILA_ARD_REMARQUE_ERREUR"));
        }
    }

    /**
     * setter pour l'attribut section
     * 
     * @param section
     *            une nouvelle valeur pour cet attribut
     */
    public void setSection(CASection section) {
        this.section = section;
    }

    /**
     * setter pour l'attribut sequences
     * 
     * @param list
     *            une nouvelle valeur pour cet attribut
     */
    public void setSequences(List list) {
        sequences = list;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean validate() {
        if (JadeStringUtil.isEmpty(eMailAddress)) {
            _addError("L'adresse e-mail n'est pas renseignée");
        }

        if (JadeStringUtil.isEmpty(libelleJournal)) {
            _addError("Le libellé du journal n'est pas renseigné");
        }

        return FWViewBeanInterface.OK.equals(getMsgType());
    }
}
