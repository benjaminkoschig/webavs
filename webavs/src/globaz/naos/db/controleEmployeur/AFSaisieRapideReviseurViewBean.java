package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;

public class AFSaisieRapideReviseurViewBean extends AFControleEmployeur implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public AFControleEmployeur controle = new AFControleEmployeur();

    /**
     * Constructeur d'AFControleEmployeurViewBean
     */
    public AFSaisieRapideReviseurViewBean() {
        super();
    }

    public void miseAJourControle(String numAffilie, String annee, String controleurVisa) {
        AFControleEmployeurManager controleMana = new AFControleEmployeurManager();
        AFControleEmployeur controle = new AFControleEmployeur();
        controleMana.setSession(getSession());
        controleMana.setForNumAffilie(numAffilie);
        controleMana.setForAnnee(annee);
        try {
            controleMana.find();
            if (controleMana.size() > 0) {
                controle = (AFControleEmployeur) controleMana.getFirstEntity();
                controle.setControleurVisa(controleurVisa);
                controle.setControleurNom(controle.getControleurNom());
                controle.update();
            } else {
                setMessage("Il n'existe pas de contrôle pour l'affilié " + numAffilie + " en " + annee);
                setMsgType(FWViewBeanInterface.ERROR);
                // _addError(transaction, getSession().getLabel("550"));
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
    }
}
