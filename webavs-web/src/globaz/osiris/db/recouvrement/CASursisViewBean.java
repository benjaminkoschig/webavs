package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.access.recouvrement.CACouvertureSection;
import globaz.osiris.db.access.recouvrement.CACouvertureSectionManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CASection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author SEL <br>
 *         Date : 15 mai 08
 */
public class CASursisViewBean extends CAPlanRecouvrement implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List /* String */idSections;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        Iterator it = getIdSections().iterator();
        while (it.hasNext()) {
            String id = (String) it.next();
            CACouvertureSection couverture = new CACouvertureSection();
            couverture.setIdPlanRecouvrement(getIdPlanRecouvrement());
            if (JadeStringUtil.contains(id, "*")) {
                /**
                 * Caractère "*" utilisé comme séparateur. Voir section_rcListe.jsp et CARecouvrementAction
                 */
                couverture.setIdSection(id.substring(0, id.indexOf("*")));
                couverture.setNumeroOrdre(id.substring(id.indexOf("*") + 1, id.length()));
            } else {
                couverture.setIdSection(id);
            }
            couverture.add(getSession().getCurrentThreadTransaction());
        }

        getSession().getCurrentThreadTransaction().commit();

        if (CAPlanRecouvrement.CS_VEN_VANC.equals(getIdModeVentilation())
                || CAPlanRecouvrement.CS_VEN_VREC.equals(getIdModeVentilation())) {
            // on récupère les sections dans l'ordre de ventilation
            CASection[] sections = CAPlanRecouvrement.serviceSectionsCouvrir(getSession(), getIdPlanRecouvrement());
            // on numérote l'ordre
            Map ordre = new HashMap();
            for (int i = 0; i < sections.length; i++) {
                CASection section = sections[i];
                ordre.put(section.getIdSection(), String.valueOf(i + 1));
            }
            // on balaye nos sections couvertes
            CACouvertureSectionManager couvertures = new CACouvertureSectionManager();
            couvertures.setSession(getSession());
            couvertures.setForIdPlanRecouvrement(getIdPlanRecouvrement());
            couvertures.find(transaction);
            for (int i = 0; i < couvertures.size(); i++) {
                CACouvertureSection couverture = (CACouvertureSection) couvertures.getEntity(i);
                couverture.wantCallMethodAfter(false);
                couverture.setNumeroOrdre((String) ordre.get(couverture.getIdSection()));
                couverture.update(transaction);
            }
        }
    }

    /**
	 */
    public void clearIdSections() {
        idSections.clear();
    }

    /**
     * Retourne la valeur formatée. Si cette dernière est déjà formattée aucune opération ne sera effectuée pour la
     * reformater.
     * 
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getAcompteFormate() {
        if (getAcompte().indexOf("'") > -1) {
            return getAcompte();
        } else {
            return JANumberFormatter.formatNoRound(getAcompte(), 2);
        }
    }

    /**
     * getter pour l'attribut id sections
     * 
     * @return la valeur courante de l'attribut id sections
     */
    public List /* String */getIdSections() {
        return idSections;
    }

    /**
     * Retourne la valeur formatée. Si cette dernière est déjà formattée aucune opération ne sera effectuée pour la
     * reformater.
     * 
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getPlafondFormate() {
        if (getPlafond().indexOf("'") > -1) {
            return getPlafond();
        } else {
            return JANumberFormatter.formatNoRound(getPlafond(), 2);
        }
    }

    /**
     * Retourne la valeur formatée. Si cette dernière est déjà formattée aucune opération ne sera effectuée pour la
     * reformater.
     * 
     * @see globaz.osiris.db.access.recouvrement.CAPlanRecouvrement#getPlafondFormate()
     */
    @Override
    public String getPremierAcompteFormate() {
        if (getPremierAcompte().indexOf("'") > -1) {
            return getPremierAcompte();
        } else {
            return JANumberFormatter.formatNoRound(getPremierAcompte(), 2);
        }
    }

    /**
     * getter pour l'attribut selected ids
     * 
     * @return la valeur courante de l'attribut selected ids
     */
    public String getSelectedIds() {
        if (idSections == null) {
            return "";
        } else {
            StringBuffer retValue = new StringBuffer();

            for (int idSection = 0; idSection < idSections.size(); ++idSection) {
                if (retValue.length() > 0) {
                    retValue.append(',');
                }

                retValue.append(idSections.get(idSection));
            }

            return retValue.toString();
        }
    }

    /**
     * setter pour l'attribut selected ids
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectedIds(String string) {
        idSections = Arrays.asList(JadeStringUtil.split(string, ',', Integer.MAX_VALUE));
    }
}
