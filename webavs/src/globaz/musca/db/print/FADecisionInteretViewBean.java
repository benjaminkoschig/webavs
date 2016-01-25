package globaz.musca.db.print;

import globaz.babel.api.ICTDocument;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.itext.FADetailInteretMoratoire_Doc;
import globaz.musca.itext.FAImpressionFacturation;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.pyxis.db.tiers.TITiers;
import java.util.Iterator;

/**
 * Class surchargeant FADetailInteretMoratoire_Doc dans le but d'imprimer 1 d�cision pour 1 int�r�t.<br/>
 * FADetailInteretMoratoire_Doc imprime tout les d�cisions de tout les int�r�ts d'un passage.
 * 
 * @author DDA
 */
public class FADecisionInteretViewBean extends FADetailInteretMoratoire_Doc implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDocument;
    private Iterator<?> myEntityList;

    public FADecisionInteretViewBean() throws Exception {
        super();
    }

    /**
     * Return la liste des entit�s qui ne contient que l'int�r�t moratoire en cours (permet d'imprimer qu'une d�cision
     * et pas toutes les d�cisions d'int�r�ts moratoires).
     */
    @Override
    protected Iterator<?> getEntityList() {
        try {
            if (myEntityList == null) {
                CAInteretMoratoireManager manager = getInteretMoratoireManager();

                setPassage(((CAInteretMoratoire) manager.getFirstEntity()).getPassage());
                this.setEntity(((CAInteretMoratoire) manager.getFirstEntity()).getEnteteFacture());

                myEntityList = manager.getContainer().iterator();

                return myEntityList;
            } else {
                return myEntityList;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getForIdDocument() {
        return forIdDocument;
    }

    /**
     * Retourne le document de babel � utiliser. <br/>
     * Par d�faut la liste compl�te, puis l'utilisateur choisit lequel il veut imprimer.
     */
    @Override
    public ICTDocument[] getICTDocument(String genreInteret) {
        ICTDocument res[] = null;
        ICTDocument document = null;

        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "Error while api for document");
        }
        // On charge le document
        document.setISession(getSession());

        document.setCsDomaine(FAImpressionFacturation.DOMAINE_FACTURATION);
        document.setCsTypeDocument(FAImpressionFacturation.DECOMPTE_COTISATION);
        // document.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);

        if (JadeStringUtil.isIntegerEmpty(getForIdDocument())) {
            try {
                CAInteretMoratoire interet = (CAInteretMoratoire) getInteretMoratoireManager().getFirstEntity();
                if (interet.getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_REMUNERATOIRES)) {
                    document.setNom(FADetailInteretMoratoire_Doc.INT_REMUN_NOMDOC);
                } else if (interet.getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_TARDIF)) {
                    document.setNomLike(FADetailInteretMoratoire_Doc.INT_TARDIF_NOMDOC);
                } else if (interet.getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_DECOMPTE_FINAL)) {
                    document.setNom(FADetailInteretMoratoire_Doc.DEC_FINAL_NOMDOC);
                } else if (interet.getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_COTISATIONS_ARRIEREES)) {
                    document.setNom(FADetailInteretMoratoire_Doc.COT_ARRIERES_NOMDOC);
                } else if (interet.getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES)) {
                    document.setNom(FADetailInteretMoratoire_Doc.COT_PERS_NOMDOC);
                }
            } catch (Exception e) {
                document.setNomLike(FADetailInteretMoratoire_Doc.INT_TARDIF_NOMDOC);
            }
        } else {
            document.setIdDocument(getForIdDocument());
            try {
                document.setCodeIsoLangue(((CAInteretMoratoire) getInteretMoratoireManager().getFirstEntity())
                        .getEnteteFacture().getISOLangueTiers());
            } catch (Exception e) {
                document.setCodeIsoLangue(getSession().getIdLangueISO());
            }
        }

        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWViewBeanInterface.ERROR, "Error while getting document");
        }
        return res;
    }

    /**
     * Return l'id entete de facture en cours.
     * 
     * @return
     */
    public String getIdEnteteFacture() {
        try {
            CAInteretMoratoire interet = new CAInteretMoratoire();
            interet.setSession(getSession());

            interet.setIdInteretMoratoire(getIdInteretMoratoire());

            interet.retrieve();
            if (interet.hasErrors() || interet.isNew()) {
                return "";
            }

            return interet.getIdSectionFacture();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return l'id passage en cours.
     * 
     * @return
     */
    public String getIdPassage() {
        try {
            CAInteretMoratoire interet = new CAInteretMoratoire();
            interet.setSession(getSession());

            interet.setIdInteretMoratoire(getIdInteretMoratoire());

            interet.retrieve();
            if (interet.hasErrors() || interet.isNew()) {
                return "";
            }

            return interet.getIdJournalFacturation();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Return le manager des int�r�ts moratoires setter pour l'int�r�t moratoire en cours.
     * 
     * @return
     * @throws Exception
     */
    private CAInteretMoratoireManager getInteretMoratoireManager() throws Exception {
        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
        manager.setSession(getSession());
        manager.setForIdInteretMoratoire(getIdInteretMoratoire());

        manager.find(getTransaction());

        return manager;

    }

    /**
     * Return le tiers. Si la liste d'entit� n'est pas vide utilis� la class parent.
     */
    @Override
    public TITiers getTiers() {
        if (myEntityList != null) {
            return super.getTiers();
        } else {
            try {
                tiers = new TITiers();
                tiers.setSession(getSession());

                tiers.setIdTiers(((CAInteretMoratoire) getInteretMoratoireManager().getFirstEntity())
                        .getEnteteFacture().getIdTiers());

                tiers.retrieve();

                return tiers;
            } catch (Exception e) {
                this._addError("Erreur lors du retrieve du tiers pour la d�cisions d'int�r�t: " + e.getMessage());
                return null;
            }
        }
    }

    public void setForIdDocument(String forIdDocument) {
        this.forIdDocument = forIdDocument;
    }

}
