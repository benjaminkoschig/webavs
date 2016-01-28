package globaz.osiris.db.comptes;

import globaz.aquila.api.ICOContentieux;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationContentieuxAquila;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author sch
 */
public class CAOperationContentieuxAquila extends CAOperation implements APIOperationContentieuxAquila {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean annulerEtapesContentieux = true;

    /**
     * Constructeur par d�faut de CAOperationContentieuxAquila
     */
    public CAOperationContentieuxAquila() {
        super();
        setIdTypeOperation(APIOperation.CAOPERATIONCONTENTIEUXAQUILA);
    }

    /**
     * Constructeur de CAOperationContentieuxAquila
     * 
     * @param parent
     *            CAOperation
     */
    public CAOperationContentieuxAquila(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAOPERATIONCONTENTIEUXAQUILA);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        // Forcer le type d'op�ration
        setIdTypeOperation(APIOperation.CAOPERATIONCONTENTIEUXAQUILA);

        // Avoid null into CAOPERP
        setMasse("0");
        setTaux("0");
        setMontant("0");

        // l'op�ration est directement comptabilis�e de mani�re � �tre
        // d�sactiv�e
        // m�me si le journal n'a pas �t� comptabilis�.
        setEtat(APIOperation.ETAT_COMPTABILISE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws Exception {

        // Laisser la superclasse effectuer son traitement
        super._beforeDelete(transaction);

        // le traitement d'annulation d'une operation contentieux Aquila est
        // effectu� dans _beforeDesactiver()
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperation#_beforeDesactiver(globaz.globall .db.BTransaction)
     */
    @Override
    protected void _beforeDesactiver(BTransaction transaction) {

        // Laisser la superclasse effectuer son traitement
        super._beforeDesactiver(transaction);

        if (annulerEtapesContentieux) {
            try {
                // pr�parer le chargement
                ICOContentieux helper = (ICOContentieux) getSession().getAPIFor(ICOContentieux.class);
                BIApplication remoteApplication = GlobazSystem.getApplication("AQUILA");
                BISession remoteSession = remoteApplication.newSession();

                getSession().connectSession(remoteSession);
                helper.setISession(remoteSession);

                // criteres de chargement
                HashMap criteres = new HashMap();

                criteres.put(ICOContentieux.FOR_ID_SECTION, getIdSection());

                // charger le contentieux
                Collection col = helper.load(criteres);

                if ((col != null) && (col.size() == 1)) {
                    ICOContentieux contentieux = (ICOContentieux) col.iterator().next();

                    // comparer l'etape courante du contentieux avec celle qu'il
                    // devrait avoir pour pouvoir l'annuler
                    if (getIdContrepartie().equals(contentieux.getIdDerniereEtapeContentieux())) {
                        helper.annulerDerniereEtape(criteres, transaction);
                    } else {
                        transaction.addErrors("Impossible d'annuler la derni�re �tape pour le contentieux N�"
                                + contentieux.getIdContentieux() + " (section N�" + contentieux.getIdSection()
                                + ") car une �tape �t� ex�cut�e ult�rieurement � l'ex�cution de ce journal.");
                    }
                } else {
                    transaction
                            .addErrors("Impossible de trouver le contentieux dont il faut annuler la derni�re �tape");
                }
            } catch (Exception e) {
                transaction.addErrors("Impossible d'annuler l'�tape contentieux: " + e.getMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // V�rifier le type d'op�ration
        if (!isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUXAQUILA)) {
            _addError(statement.getTransaction(), getSession().getLabel("TYPE_OPERATION_NOT_CONTENTIEUX"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperation#_valider(globaz.globall.db.BTransaction )
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les donn�es de la superclasse
        super._valider(transaction);

        // TODO sch Ev�ntuellement contr�ler que la mise � jour de l'�tape soit
        // correcte (evenement contentieux)
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        super._writeProperties(statement);
        statement.writeField(CAOperation.FIELD_IDCONTREPARTIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdContrepartie(), "idContrepartie"));
    }

    /**
     * Cette m�thode surcharche dupliquer de CAOperation pour interdire la duplication sur une op�ration contentieux
     * Aquila
     * 
     * @param CAOperationContentieuxAquila
     *            operation
     */
    public void dupliquer(CAOperationContentieuxAquila oper) {

        // Refuser la duplication d'une op�ration de contentieux Aquila
        _addError(null, getSession().getLabel("5186"));
    }

    public boolean isAnnulerEtapesContentieux() {
        return annulerEtapesContentieux;
    }

    public void wantAnnulerEtapesContentieux(boolean b) {
        annulerEtapesContentieux = b;
    }

}
