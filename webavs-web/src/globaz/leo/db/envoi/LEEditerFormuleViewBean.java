/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.envoi;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.parametrage.LEChampListViewBean;
import globaz.leo.db.parametrage.LEDestinataireListViewBean;
import globaz.leo.db.parametrage.LEDestinataireViewBean;
import globaz.leo.db.parametrage.LEFormulePDFViewBean;
import globaz.leo.db.parametrage.LEGroupesListViewBean;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.leo.process.handler.LEParamEnvoiHandler;
import globaz.lupus.db.journalisation.LUComplementJournalListViewBean;
import globaz.lupus.db.journalisation.LUComplementJournalViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersManager;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEditerFormuleViewBean extends LUJournalViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categorie;
    private String domaine;
    private String etapeCourante;
    private String etapeSuivante;
    private String idChoixDestinataire;

    /**
     * @return
     */
    public String getCategorie() throws Exception {
        if (JadeStringUtil.isEmpty(categorie)) {
            loadFormule();
        }
        return categorie;
    }

    public LEChampListViewBean getChamps(String csGroupe) {
        LEChampListViewBean cMng = new LEChampListViewBean();
        cMng.setSession(getSession());
        cMng.setForCsGroupe(csGroupe);
        try {
            cMng.find();
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
        }
        return cMng;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.lupus.db.journalisation.LUJournalViewBean#getDestinataire()
     */
    public String getDestinataire(String numero) {
        String destinataire = "";
        try {
            if (!JadeStringUtil.isEmpty(getIdChoixDestinataire())) {
                TITiersManager tiersManager = new TITiersManager();
                tiersManager.setSession(getSession());
                tiersManager.setForIdTiers(getIdChoixDestinataire());
                tiersManager.find();
                if (tiersManager.size() > 0) {
                    destinataire = ((TITiers) tiersManager.getEntity(0)).getAdresseAsString(
                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, IConstantes.CS_APPLICATION_DEFAUT,
                            JACalendar.todayJJsMMsAAAA(), numero);
                }
            }
        } catch (Exception e) {
            destinataire = e.getMessage();
        }
        return destinataire;
    }

    /**
     * @return
     */
    public String getDomaine() throws Exception {
        if (JadeStringUtil.isEmpty(domaine)) {
            loadFormule();
        }
        return domaine;
    }

    /**
     * @return
     */
    public String getEtapeCourante() {
        if (JadeStringUtil.isEmpty(etapeCourante)) {
            loadEtapes();
        }
        return etapeCourante;
    }

    public String getEtapeSuivante() {
        if (JadeStringUtil.isEmpty(etapeSuivante)) {
            loadEtapes();
        }
        return etapeSuivante;
    }

    protected ICTDocument[] getICTDocument(String isoLangue) throws Exception {
        ICTDocument res[] = null;
        ICTDocument document = null;
        document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        document.setISession(getSession());
        document.setCsDomaine(getDomaine());
        document.setCsTypeDocument(getCategorie());
        document.setDefault(new Boolean(true));
        document.setCodeIsoLangue(isoLangue);
        document.setActif(new Boolean(true));
        res = document.load();
        return res;
    }

    /**
     * @return
     */
    public String getIdChoixDestinataire() {
        try {
            if (JadeStringUtil.isEmpty(idChoixDestinataire)) {
                LEEnvoiHandler handler = new LEEnvoiHandler();
                LEDestinataireListViewBean dest = handler.getDestinataires(getEtapeCourante(), getSession(), null);
                if (dest.size() > 0) {
                    idChoixDestinataire = ((LEDestinataireViewBean) dest.getFirstEntity()).getIdTiers();
                }
            }
        } catch (Exception e) {
            idChoixDestinataire = "";
        }
        return idChoixDestinataire;
    }

    public LEGroupesListViewBean getListeGroupes() {
        LEGroupesListViewBean l = new LEGroupesListViewBean();
        try {
            l.setSession(getSession());
            l.setForCsFormule(getEtapeSuivante());
            l.find();
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
        }
        return l;
    }

    public ICTListeTextes getListePosition(String level) {
        try {
            ICTDocument listeTxt = getICTDocument(getSession().getIdLangueISO())[0];
            if (listeTxt != null) {
                int intLevel = Integer.parseInt(level);
                return listeTxt.getTextes(intLevel);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    /**
	 * 
	 */
    private void loadEtapes() {
        LUComplementJournalListViewBean l = getCplJourn();
        for (int i = 0; i < l.size(); i++) {
            LUComplementJournalViewBean c = (LUComplementJournalViewBean) l.getEntity(i);
            if (ILEConstantes.CS_ETAPE_SUIVANTE_GROUPE.equals(c.getCsTypeCodeSysteme())) {
                etapeSuivante = c.getValeurCodeSysteme();
            }
            if (ILEConstantes.CS_DEF_FORMULE_GROUPE.equals(c.getCsTypeCodeSysteme())) {
                etapeCourante = c.getValeurCodeSysteme();
            }
        }
    }

    /**
	 * 
	 */
    private void loadFormule() throws Exception {
        LEParamEnvoiHandler handler = new LEParamEnvoiHandler();
        LEFormulePDFViewBean formule = handler.getFormule(getEtapeCourante(), getSession(), null);
        categorie = formule.getTypeDocument();
        domaine = formule.getDomaine();
    }

    /**
     * @param string
     */
    public void setCategorie(String string) {
        categorie = string;
    }

    /**
     * @param string
     */
    public void setDomaine(String string) {
        domaine = string;
    }

    /**
     * @param string
     */
    public void setIdChoixDestinataire(String string) {
        idChoixDestinataire = string;
    }

}
