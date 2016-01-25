package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.helpers.batch.COEtapeHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h1>Description</h1>
 * <p>
 * Représente le model de la vue "_de"
 * </p>
 * 
 * @author Arnaud Dostes, 12-oct-2004
 */
public class COEtapeViewBean extends COEtape implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -6092959882664089849L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private COSequenceViewBean sequence;
    private List sequences;
    private List transitionsDepuis;
    private List transitionsVers;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        // charger la séquence
        sequence = new COSequenceViewBean();
        sequence.setIdSequence(getIdSequence());
        sequence.setSession(getSession());
        sequence.retrieve();

        if (!isLoadedFromManager()) {
            // charger les transitions depuis cette étape
            COTransitionManager listViewBean = new COTransitionManager();

            listViewBean.setSession(getSession());
            listViewBean.setOrderByLibEtapeCSOrder("true");
            listViewBean.setForIdEtape(getIdEtape());
            listViewBean.find();

            transitionsDepuis = Collections.unmodifiableList(new ArrayList(listViewBean.getContainer()));

            // charger les transitions vers cette étape
            listViewBean.setForIdEtape("");
            listViewBean.setForIdEtapeSuivante(getIdEtape());
            listViewBean.find();

            transitionsVers = Collections.unmodifiableList(listViewBean.getContainer());
        }
    }

    /**
     * Retourne le libellé de la séquence pour cette étape.
     * 
     * @return un libellé ou une chaîne vide
     */
    public String getLibSequenceLibelle() {
        return (sequence != null) ? sequence.getLibSequenceLibelle() : "";
    }

    /**
     * Retourne le montant minimal formatte standard (xxx.xx).
     * 
     * @return DOCUMENT ME!
     */
    public String getMontantMinimalFormatte() {
        return JANumberFormatter.format(super.getMontantMinimal());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public COSequenceViewBean getSequence() {
        return sequence;
    }

    /**
     * Retourne la liste des séquences si celle-ci a été {@link #setSequences(List) renseignée}.
     * <p>
     * Dans les actions par défaut, cette liste est renseignée dans {@link COEtapeHelper}.
     * </p>
     * 
     * @return Un liste d'instance de {@link COSequenceViewBean} ou null
     */
    public List getSequences() {
        return sequences;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public List getTransitionsDepuis() {
        return transitionsDepuis;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public List getTransitionsVers() {
        return transitionsVers;
    }

    /**
     * Retourne vrai si le menu déroulant de choix de la séquence dans l'écran doit être actif.
     * 
     * @return vrai si l'utilisateur a le droit de modifier la séquence pour cette étape.
     */
    public boolean isSequenceModifiable() {
        return isNew();
    }

    /**
     * Redéfinie pour éviter d'avoir des guillemets dans la base.
     * 
     * @see globaz.aquila.db.access.batch.COEtape#setMontantMinimal(java.lang.String)
     */
    @Override
    public void setMontantMinimal(String montantMinimal) {
        super.setMontantMinimal(JANumberFormatter.deQuote(montantMinimal));
    }

    /**
     * @see #getSequences()
     */
    public void setSequences(List sequences) {
        this.sequences = sequences;
    }
}
