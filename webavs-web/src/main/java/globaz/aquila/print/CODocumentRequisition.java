/**
 *
 */
package globaz.aquila.print;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.external.IntRole;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * Classe pour les documents de réquisitions (RP, RCP et RV)
 * 
 * @author SEL
 * 
 */
public abstract class CODocumentRequisition extends CODocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map idEtapeInfoConfigToValeur = null;
    private FWCurrency montantRestant = null;

    /**
	 *
	 */
    public CODocumentRequisition() {
        super();
    }

    /**
     * @param session
     * @throws FWIException
     */
    public CODocumentRequisition(BSession session) throws FWIException {
        super(session);
    }

    /**
     * @param session
     * @param fileName
     *            : nom du document PDF
     * @throws FWIException
     */
    public CODocumentRequisition(BSession session, String fileName) throws FWIException {
        super(session, fileName);
    }

    /**
     * @return
     */
    public Map getIdEtapeInfoConfigToValeur() {
        return idEtapeInfoConfigToValeur;
    }

    /**
     * @return the montantRestant
     */
    public FWCurrency getMontantRestant() {
        return montantRestant;
    }

    /**
     * Retourne les frais de mainlevée. <br />
     * Parcours les opérations de la section pour les rubriques dont la référence est :
     * <tt>APIReferenceRubrique.FRAIS_MAINLEVEE</tt>
     * 
     * @return le montant total des opérations de la section pour les rubriques dont la référence est :
     *         <tt>APIReferenceRubrique.FRAIS_MAINLEVEE</tt>
     * @throws Exception
     */
    protected String getTaxesMainlevee() throws Exception {
        BigDecimal montant = new BigDecimal("0");
        CAReferenceRubrique ref = new CAReferenceRubrique();
        ref.setSession(getSession());
        CARubrique rubrique;
        if (IntRole.ROLE_AFFILIE_PARITAIRE.equals(curContentieux.getCompteAnnexe().getIdRole())) {
            rubrique = (CARubrique) ref.getRubriqueByCodeReference(APIReferenceRubrique.FRAIS_MAINLEVEE_PARITAIRE);
        } else {
            rubrique = (CARubrique) ref.getRubriqueByCodeReference(APIReferenceRubrique.FRAIS_MAINLEVEE);
        }

        if (rubrique == null) {
            return "";
        }
        CAOperationManager manager = new CAOperationManager();
        manager.setSession(getSession());
        manager.setForIdSection(curContentieux.getSection().getIdSection());
        manager.setForIdCompte(rubrique.getIdRubrique());
        manager.setForEtat(APIOperation.ETAT_COMPTABILISE);
        manager.find(getTransaction());

        if (manager.size() > 0) {
            for (Iterator it = manager.iterator(); it.hasNext();) {
                CAOperation op = (CAOperation) it.next();
                if (!JadeStringUtil.isDecimalEmpty(op.getMontant())) {
                    montant = montant.add(new BigDecimal(op.getMontant()));
                }
            }
        }

        return formatMontant(montant.toString());
    }

    /**
     * Nom de l'office et du canton <br>
     * Renseigne les champs de l'OP pour les documents de Requisition.
     * 
     * @param textForOPName
     *            correspond au CT pour le nom de l'OP
     * @param textForOPCanton
     *            correspond au CT pour le canton de l'OP
     */
    public void initOfficePoursuite(Object key, int ctNiveauTextForOPName, int ctPositionTextForOPName,
            int ctNiveauTextForOPCanton, int ctPositionTextForOPCanton) {
        try {
            // destinataire est l'OP
            destinataireDocument = getTiersService().getOfficePoursuite(getSession(),
                    curContentieux.getCompteAnnexe().getTiers(), curContentieux.getCompteAnnexe().getIdExterneRole());

            if (destinataireDocument == null) {
                this.log(getSession().getLabel("AQUILA_ERR_OP_INTROUVABLE"), FWMessage.AVERTISSEMENT);
                this.setParametres(COParameter.T4, " ");
                this.setParametres(COParameter.T4b, " ");
            } else {
                _setLangueFromTiers(destinataireDocument);

                // TODO Sel : Rechercher l'adresse de l'office de pouruiste domaine standard type courrier herite true
                TIAdresseDataSource adresseOP = this.getAdresseDataSourcePrincipal(destinataireDocument, getLangue());
                cantonOfficePoursuite = adresseOP.canton_court;
                this.setParametres(
                        COParameter.T4,
                        formatMessage(
                                new StringBuilder(getCatalogueTextesUtil().texte(key, ctNiveauTextForOPName,
                                        ctPositionTextForOPName)), new Object[] { destinataireDocument.getNom() }));
                if (adresseOP != null) {
                    this.setParametres(
                            COParameter.T4b,
                            formatMessage(
                                    new StringBuilder(getCatalogueTextesUtil().texte(key, ctNiveauTextForOPCanton,
                                            ctPositionTextForOPCanton)),
                                    new Object[] { getTiersService().getCanton(getSession(), adresseOP.canton_id,
                                            getLangue()) }));
                } else {
                    this.setParametres(COParameter.T4b, " ");
                }
            }
        } catch (Exception e) {
            this.log(getSession().getLabel("AQUILA_ERR_OP_INTROUVABLE"), FWMessage.AVERTISSEMENT);
        }
    }

    // TODO Pour modif de la RP
    /**
     * @param fromDate
     *            la date à partir de laquelle la situation de compte doit débuter
     * @param fDesc
     *            le nom du champ du document dans lequel afficher la description de l'opération
     * @param fMontant
     *            le nom du champ du document dans lequel afficher le montant de l'opération
     * @param fDevise
     *            le nom du champ du document dans lequel afficher le libellé de la devise
     * @param devise
     *            le libellé de la devise
     * @param etape
     *            concernée
     * @param id
     *            du premier journal de la section
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    // protected List createSituationCompteDS(String fromDate, String fDesc, String fMontant, String fDevise, String
    // devise, String csEtape, String idJournal) throws Exception {
    // LinkedList lignes = new LinkedList();
    //
    // montantRestant = new FWCurrency();
    // for (Iterator operIter = situationCompte(fromDate, idJournal, csEtape).iterator(); operIter.hasNext();) {
    // COSituationCompteItem situationCompteItem = (COSituationCompteItem) operIter.next();
    //
    // if (situationCompteItem.isPaiement() || situationCompteItem.isCompensation()) {
    // if (!situationCompteItem.isLineBlocked(csEtape) && situationCompteItem.isMontantDifferentZero()) {
    // HashMap fields = new HashMap();
    //
    // fields.put(fDesc, situationCompteItem.getDescription());
    // fields.put(fMontant, formatMontant(situationCompteItem.getMontant().toString()));
    // fields.put(fDevise, devise);
    //
    // lignes.add(fields);
    //
    // getMontantTotalDetail().add(situationCompteItem.getMontant());
    // }
    //
    // } else {
    // montantRestant.add(situationCompteItem.getMontant());
    // }
    // }
    //
    // return lignes;
    // }

    /**
     * @param map
     */
    public void setIdEtapeInfoConfigToValeur(Map map) {
        idEtapeInfoConfigToValeur = map;
    }

    /**
     * @param montantRestant
     *            the montantRestant to set
     */
    public void setMontantRestant(FWCurrency montantRestant) {
        this.montantRestant = montantRestant;
    }

}
