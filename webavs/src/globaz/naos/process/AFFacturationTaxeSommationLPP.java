/*
 * Cr�� le 28 avr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.db.processFacturation.AFFacturationSommationLeo;
import java.util.ArrayList;

/**
 * @author sda
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AFFacturationTaxeSommationLPP extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    BProcess context;
    /**
	 * 
	 */

    FAPassage passage = new FAPassage();

    public AFFacturationTaxeSommationLPP() {
        super();
    }

    /**
     * @param parent
     */
    public AFFacturationTaxeSommationLPP(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public AFFacturationTaxeSommationLPP(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        // Cr�ation d'un ArrayList qui contient l'�tape suivante
        ArrayList b = new ArrayList();
        b.add(ILEConstantes.CS_DEF_FORMULE_AMENDE_LPP);

        // On sette les param�tres auquels doit correspondre notre envoi
        LEEtapesSuivantesListViewBean listeEtapes = new LEEtapesSuivantesListViewBean();
        // On sette la session
        listeEtapes.setSession(getSession());
        // On sette l'�tape suivante
        listeEtapes.setForCsFormule(b);
        // On lui sette la cat�gorie de l'envoi
        listeEtapes.setForCategories(ILEConstantes.CS_CATEGORIE_SUIVI_LPP);
        listeEtapes.setWantOrderBy(false);
        try {
            // On sette les param�tres n�cessaires � la facturation de l'amende
            AFFacturationSommationLeo sommationLeo = new AFFacturationSommationLeo();
            // On sette la session
            sommationLeo.setSession(getSession());
            // On sette le passage
            sommationLeo.setPassage(getPassage());
            // On lui passe le viewBean contenant tous les param�tres de l'envoi
            sommationLeo.setListeEtapes(listeEtapes);
            // On sette l'idFinFacture
            sommationLeo.setCategorieSection(getCategorieSection());
            // On sette le type de facture
            sommationLeo.setTypeFacture(getTypeFacture());
            // On sette le montant
            sommationLeo.setMontant(getMontant());
            // On sette la rubrique
            sommationLeo.setRubrique(getRubrique());
            // On sette le role de l'affili�
            sommationLeo.setRoleAffilie(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                    getSession().getApplication()));
            sommationLeo.setContext(getContext());
            sommationLeo.executeProcess();
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            return false;
        }
        return true;
    }

    // Retourne la cat�gorie de section (chiffres de fin du num�ro de la
    // facture, stock� dans la table fwparp)
    public String getCategorieSection() {
        try {
            return String.valueOf((int) JadeStringUtil.parseDouble(
                    FWFindParameter.findParameter(getTransaction(), "843003", "CATSECTION", "0", "0", 2), 0.0));
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            return null;
        }
    }

    public BProcess getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    // Retourne le montant de l'amende (stock� dans la table fwparp)
    public String getMontant() {
        try {
            return String.valueOf((int) JadeStringUtil.parseDouble(
                    FWFindParameter.findParameter(getTransaction(), "843002", "MONTANT", "0", "0", 2), 0.0));
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            return null;
        }
    }

    /**
     * @return
     */
    public FAPassage getPassage() {
        return passage;
    }

    // Retourne l'id de la rubrique � utiliser (stock� dans la table fwparp )
    public String getRubrique() {
        try {
            return String.valueOf((int) JadeStringUtil.parseDouble(
                    FWFindParameter.findParameter(getTransaction(), "843001", "RUBRIQUE", "0", "0", 2), 0.0));
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            return null;
        }
    }

    // Retourne le type de facture (stock� dans la table fwparp)
    public String getTypeFacture() {
        try {
            return String.valueOf((int) JadeStringUtil.parseDouble(
                    FWFindParameter.findParameter(getTransaction(), "843004", "TYPFACTURE", "0", "0", 2), 0.0));
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Raccord de m�thode auto-g�n�r�
        return null;
    }

    public void setContext(BProcess context) {
        this.context = context;
    }

    /**
     * @param passage
     */
    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }
}
