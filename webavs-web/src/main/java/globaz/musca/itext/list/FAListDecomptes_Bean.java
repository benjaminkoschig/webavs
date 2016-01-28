package globaz.musca.itext.list;

import globaz.framework.printing.itext.api.FWIBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.util.FWITextAttribute;
import globaz.framework.printing.itext.util.FWITextElement;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.translation.CodeSystem;
import globaz.musca.util.FAUtil;
import globaz.pyxis.db.tiers.TITiers;
import java.util.HashMap;
import java.util.Map;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class FAListDecomptes_Bean implements FWIBeanInterface {
    private String adressePaiement = "";
    private FAListDecomptes_Doc context = null;
    // fields
    private String debiteur = "";
    private String description = "";
    private String imprimable = "";
    private Double montant = new Double(0.0);
    private String remarque = "";
    private BTransaction transaction = null;

    /**
     * Constructor for FAListDecompte_Bean.
     */
    protected FAListDecomptes_Bean() {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 16:03:05)
     * 
     * @return java.lang.String
     */
    private String _getDescriptionTiers(FAEnteteFacture entity) {

        StringBuffer description = new StringBuffer("");

        TITiers tiers = new TITiers();
        tiers.setIdTiers(entity.getIdTiers());
        try {
            tiers.retrieve(transaction);
            StringBuffer descriptionTiers = new StringBuffer(entity.getDescriptionRole() + " "
                    + entity.getIdExterneRole() + ", " + tiers.getDesignation1() + " " + tiers.getDesignation2());
            String localiteTiers = tiers.getLocalite();

            if (!JadeStringUtil.isBlank(descriptionTiers.toString())) {
                description.append(descriptionTiers);
            }
            if (!JadeStringUtil.isBlank(localiteTiers)) {
                description.append(", " + localiteTiers);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return description.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 16:16:34)
     * 
     * @return java.lang.String
     */
    private String _getImprimable(FAEnteteFacture entity) {
        // marquer d'un X si la facture est non imprimable
        if (!entity.isNonImprimable().booleanValue()) {
            return "X";
        } else {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 16:12:23)
     * 
     * @return java.lang.String
     */
    private String _getRemarque(FAEnteteFacture entity) {
        String rem = "";
        try {
            // Rec/Remarque
            if (!entity.getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_AUTOMATIQUE)) {
                rem = globaz.musca.translation.CodeSystem.getLibelle(transaction.getSession(),
                        entity.getIdModeRecouvrement());
            }
            return rem + entity.getRemarque(transaction);
        } catch (Exception ex) {
            return "Remarque could not be found: " + ex.getMessage();
        }
    }

    /**
     * Constructor for FAListDecompte_Bean.
     */
    private void add(BEntity newEntity) {
        FAEnteteFacture entity = (FAEnteteFacture) newEntity;

        boolean isRetenu = FAEnteteFacture.CS_MODE_RETENU.equalsIgnoreCase(CodeSystem.getCode(entity
                .getIdModeRecouvrement()));

        String montantMinimeNeg = FAApplication.MONTANT_MINIMENEG_DEFVAL;
        String montantMinimePos = FAApplication.MONTANT_MINIMEPOS_DEFVAL;
        String montantMinimeMax = FAApplication.MONTANT_MINIMEMAX_DEFVAL;
        adressePaiement = "";

        try {
            montantMinimeNeg = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
            montantMinimePos = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);
            montantMinimeMax = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEMAX);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        FWCurrency totalFacture = new FWCurrency(entity.getTotalFacture());

        debiteur = _getDescriptionTiers(entity); // Debiteur (1)
        description = entity.getDescriptionDecompte(); // Description du
        // décompte(2)
        montant = new Double(totalFacture.doubleValue()); // Montant(3)
        // {
        // Adresse de paiement
        adressePaiement = "";
        // if (totalFacture.isNegative()) {
        if ((totalFacture.compareTo(new FWCurrency(montantMinimeNeg)) > 0)
                && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) < 0)) {
            adressePaiement = "";
        } else {
            try {
                // adressePaiement = entity.getAdressePaiement(transaction,
                // JACalendar.today().toStr(".")).trim();
                adressePaiement = FAUtil.getAdressePrincipalePaiementFromLinkPmt(getSession(),
                        entity.getIdAdressePaiement());
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        if (JadeStringUtil.isBlankOrZero(adressePaiement)) {
            adressePaiement = "";
        } else {

        }
        // }

        // } // Remarque

        // {
        StringBuffer remBuffer = new StringBuffer();
        if (isRetenu) {
            remBuffer.append(CodeSystem.getCodeLibelle(FAEnteteFacture.CS_MODE_RETENU));

        } // remarque pour montant minime
        if (!totalFacture.isZero() && (totalFacture.compareTo(new FWCurrency(montantMinimeNeg)) > 0)
                && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) < 0)) {
            try {
                remBuffer.append(getSession().getApplication().getLabel("LISDEC_REMMINIME",
                        getSession().getIdLangueISO()));
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        try {
            if (((FAApplication) getSession().getApplication()).isModeReporterMontantMinimal()) {
                if (!totalFacture.isZero() && (totalFacture.compareTo(new FWCurrency(montantMinimeMax)) < 0)
                        && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) > 0)) {
                    remBuffer
                            .append(getSession().getApplication().getLabel("FACREPORT", getSession().getIdLangueISO()));
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if ((new FWCurrency(entity.getTotalFacture()).isNegative()) && JadeStringUtil.isBlankOrZero(adressePaiement)) {
            try {
                // retour à la ligne
                if ((remBuffer.length() != 0)) {
                    remBuffer.append("\n");
                }
                remBuffer.append(getSession().getApplication().getLabel("LISDEC_REMNOADRPAIMENT",
                        getSession().getIdLangueISO()));
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }

        }
        // rajouter la remarque de l'entity
        remBuffer.append(_getRemarque(entity));
        remarque = remBuffer.toString(); // remarque
        // }
        imprimable = _getImprimable(entity); // est imprimable
    }

    public String getCOL_1() {
        return debiteur;
    }

    public String getCOL_2() {
        return description;
    }

    public Double getCOL_3() {
        return montant;
    }

    public String getCOL_4() {
        return adressePaiement;
    }

    public String getCOL_5() {
        return remarque;
    }

    public String getCOL_6() {
        return imprimable;
    }

    /**
     * Returns the context.
     * 
     * @return FAListDecomptes_Doc
     */
    protected FAListDecomptes_Doc getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIBeanInterface#getMontant()
     */
    @Override
    public double getMontant() {
        return getCOL_3().doubleValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIBeanInterface#getNewInstance(BEntity )
     */
    @Override
    public FWIBeanInterface getNewInstance(BEntity entity) {
        FWIBeanInterface bean = new FAListDecomptes_Bean();
        ((FAListDecomptes_Bean) bean).setTransaction(transaction);
        ((FAListDecomptes_Bean) bean).setContext(context);
        ((FAListDecomptes_Bean) bean).add(entity);
        return bean;
    }

    @Override
    public Map getRowOfData() {
        Map row = new HashMap();
        row.put("COL_1", getCOL_1());
        row.put("COL_2", getCOL_2());
        row.put("COL_3", getCOL_3());
        row.put("COL_4", getCOL_4());
        row.put("COL_5", getCOL_5());
        row.put("COL_6", getCOL_6());
        return row;
    }

    protected BSession getSession() {
        return transaction.getSession();
    }

    /**
     * Returns the transaction.
     * 
     * @return BTransaction
     */
    protected BTransaction getTransaction() {
        return transaction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIBeanInterface#rowNbLine()
     */
    @Override
    public float rowNbLine() {
        float adresseHeight = 17f;
        float descriptionHeight = 17f;
        try {
            FWITextElement textEllement = new FWITextElement();
            if (adressePaiement.length() == 0) {
                adresseHeight = textEllement.getTextHeight(new FWITextAttribute(248, 17, "SansSerif", 7), debiteur);
            } else {
                adresseHeight = textEllement.getTextHeight(new FWITextAttribute(129, 17, "SansSerif", 7),
                        adressePaiement);
            }
            if (description.length() == 0) {
                descriptionHeight = textEllement.getTextHeight(new FWITextAttribute(75, 17, "SansSerif", 7),
                        description);
            }
        } catch (FWIException e) {
            JadeLogger.error(this, e);
        }
        return Math.max(adresseHeight, descriptionHeight);
    }

    /**
     * Sets the context.
     * 
     * @param context
     *            The context to set
     */
    protected void setContext(FAListDecomptes_Doc context) {
        this.context = context;
    }

    /**
     * Sets the transaction.
     * 
     * @param transaction
     *            The transaction to set
     */
    protected void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

}
