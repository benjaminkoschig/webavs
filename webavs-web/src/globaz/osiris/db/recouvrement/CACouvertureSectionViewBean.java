package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.access.recouvrement.CACouvertureSection;
import globaz.osiris.db.access.recouvrement.CACouvertureSectionManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Représente le model de la vue "_de".
 * 
 * @author Pascal Lovy, 10-may-2005
 */
public class CACouvertureSectionViewBean extends CACouvertureSection implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String compteAnnexeIdExterneRole = "";
    private String compteAnnexeIdRole = "";
    private String idCompteAnnexe = "";
    private String sectionCategorieSection = "";
    private String sectionIdExterne = "";

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        updatePropertiesFromSection(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        updateSectionFromProperties(transaction);
        super._beforeAdd(transaction);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getCompteAnnexeIdExterneRole() {
        return compteAnnexeIdExterneRole;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getCompteAnnexeIdRole() {
        return compteAnnexeIdRole;
    }

    /**
     * @return Une représentation textuelle du compte annexe
     */
    public String getCompteAnnexeText() {
        try {
            APICompteAnnexe ca = getSection().getCompteAnnexe();
            return ca.getRole().getDescription() + " " + ca.getIdExterneRole() + " " + ca.getTiers().getNom() + ", "
                    + ca.getTiers() == null ? "" : ca.getTiers().getLieu();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getSectionCategorieSection() {
        return sectionCategorieSection;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getSectionIdExterne() {
        return sectionIdExterne;
    }

    public Vector getSectionsPossibles() {
        Vector v = new Vector();
        CASectionManager sections = new CASectionManager();
        sections.setSession(getSession());
        sections.setForIdCompteAnnexe(getPlanRecouvrement().getCompteAnnexe().getIdCompteAnnexe());
        sections.setForSelectionSections("3");

        // toutes les sections couvertes par ce plan
        CACouvertureSectionManager sectionsCouvertes = new CACouvertureSectionManager();
        sectionsCouvertes.setSession(getSession());
        sectionsCouvertes.setForIdPlanRecouvrement(getPlanRecouvrement().getIdPlanRecouvrement());

        try {
            sectionsCouvertes.find();
            sections.find();
        } catch (Exception e) {
            return v;
        }

        ArrayList aSectionsCouvertes = new ArrayList();
        for (int i = 0; i < sectionsCouvertes.size(); i++) {
            CACouvertureSection couverture = (CACouvertureSection) sectionsCouvertes.getEntity(i);
            aSectionsCouvertes.add(new Integer(couverture.getIdSection()));
        }

        String line[];
        for (int i = 0; i < sections.size(); i++) {
            CASection section = (CASection) sections.getEntity(i);
            line = new String[2];
            line[0] = section.getIdSection();
            if (!aSectionsCouvertes.contains(new Integer(section.getIdSection()))) {
                line[1] = section.getFullDescription() + ", " + section.getSoldeFormate() + " CHF";
                v.add(line);
            }
        }
        // On enlève les sections déjà couvertes

        return v;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setCompteAnnexeIdExterneRole(String string) {
        compteAnnexeIdExterneRole = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setCompteAnnexeIdRole(String string) {
        compteAnnexeIdRole = string;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setSectionCategorieSection(String string) {
        sectionCategorieSection = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setSectionIdExterne(String string) {
        sectionIdExterne = string;
    }

    /**
     * Met à jour les propriétés en fonction de la section.
     * 
     * @param transaction
     *            La transaction courante
     */
    private void updatePropertiesFromSection(BTransaction transaction) {
        CASection section = getSection();
        if (section != null) {
            setSectionIdExterne(section.getIdExterne());
            setSectionCategorieSection(section.getCategorieSection());
            APICompteAnnexe compteAnnexe = section.getCompteAnnexe();
            if (compteAnnexe != null) {
                setCompteAnnexeIdExterneRole(compteAnnexe.getIdExterneRole());
                setCompteAnnexeIdRole(compteAnnexe.getIdRole());
            }
        }
    }

    /**
     * Met à jour la section en fonction des propriétés.
     * 
     * @param transaction
     *            La transaction courante
     */
    private void updateSectionFromProperties(BTransaction transaction) {
        // APICompteAnnexe compteAnnexe = null;
        // CASection section = null;
        // // Recherche du compte annexe
        // if (JadeStringUtil.isBlank(getCompteAnnexeIdExterneRole())
        // || JadeStringUtil.isBlank(getCompteAnnexeIdRole())) {
        // _addError(
        // transaction,
        // getSession().getLabel("COMPTEANNEXE_OBLIGATOIRE"));
        // } else {
        // CACompteAnnexeManager manager = new CACompteAnnexeManager();
        // manager.setSession(getSession());
        // manager.setForIdExterneRole(getCompteAnnexeIdExterneRole());
        // manager.setForIdRole(getCompteAnnexeIdRole());
        // try {
        // manager.find(transaction);
        // } catch (Exception e) {
        // _addError(transaction, e.toString());
        // }
        // if (manager.size() != 1) {
        // _addError(
        // transaction,
        // getSession().getLabel("MISSING_COMPTEANNEXE"));
        // } else {
        // compteAnnexe = (CACompteAnnexe) manager.getEntity(0);
        // }
        // }
        // // Recherche de la section
        // if (JadeStringUtil.isBlank(getSectionIdExterne())
        // || JadeStringUtil.isBlank(getSectionCategorieSection())) {
        // _addError(
        // transaction,
        // getSession().getLabel("SECTION_OBLIGATOIRE"));
        // } else {
        // CASectionManager manager = new CASectionManager();
        // manager.setSession(getSession());
        // manager.setForIdExterne(getSectionIdExterne());
        // manager.setForCategorieSection(getSectionCategorieSection());
        // try {
        // manager.find(transaction);
        // } catch (Exception e) {
        // _addError(transaction, e.toString());
        // }
        // if (manager.size() != 1) {
        // _addError(
        // transaction,
        // getSession().getLabel("MISSING_SECTION"));
        // } else {
        // section = (CASection) manager.getEntity(0);
        // }
        // }
        // // Valide que la section appartienne au compte annexe
        // if (compteAnnexe != null && section != null) {
        // if (compteAnnexe
        // .getIdCompteAnnexe()
        // .equals(section.getCompteAnnexe().getIdCompteAnnexe())) {
        // setIdSection(section.getIdSection());
        // } else {
        // _addError(
        // transaction,
        // getSession().getLabel("SECTION_BAD_COMPTEANNEXE"));
        // }
        // }
    }
}
