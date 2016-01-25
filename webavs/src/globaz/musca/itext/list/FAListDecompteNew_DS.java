package globaz.musca.itext.list;

// ITEXT
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.translation.CodeSystem;
import globaz.musca.util.FAUtil;
import globaz.pyxis.db.tiers.TITiers;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Insérez la description du type ici. Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
class FAListDecompteNew_DS extends FAEnteteFactureManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adressePaiement = "";
    private Iterator<?> container = null;
    private String debiteur = "";
    private String description = "";
    private globaz.musca.db.facturation.FAEnteteFacture entity;
    private String imprimable = "";
    private Double montant = new Double(0.0);
    private String remarque = "";

    public FAListDecompteNew_DS() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 16:03:05)
     * 
     * @return java.lang.String
     */
    private String _getDescriptionTiers(FAEnteteFacture entity) {

        StringBuffer description = new StringBuffer("");

        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(entity.getIdTiers());
        try {
            tiers.retrieve();
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
        if (!entity.isNonImprimable().booleanValue()
                && !FAEnteteFacture.CS_MODE_IMP_PASIMPZERO.equalsIgnoreCase(entity.getIdCSModeImpression())) {
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
                rem = globaz.musca.translation.CodeSystem.getLibelle(getSession(), entity.getIdModeRecouvrement());
            }
            return rem + entity.getRemarque();
        } catch (Exception ex) {
            return "Remarque could not be found: " + ex.getMessage();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:33:47)
     */
    @Override
    protected void _init() {
        container = null;
    }

    public globaz.musca.db.facturation.FAEnteteFacture getEntity() {
        return entity;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    Map<String, Object> getFieldValues() throws net.sf.jasperreports.engine.JRException {
        prepareValues();
        // Verify si le passage change -> nouveau document pour l'impression de
        // liste de document
        if (!super.getForIdPassage().equalsIgnoreCase(entity.getIdPassage())) {
            super.setForIdPassage(entity.getIdPassage());
            _init();
        }
        Map<String, Object> row = new HashMap<String, Object>();
        row.put(FWIImportParametre.getCol(1), debiteur);
        row.put(FWIImportParametre.getCol(2), description);
        // Description du décompte
        row.put(FWIImportParametre.getCol(3), montant);
        row.put(FWIImportParametre.getCol(4), adressePaiement);
        // Montant
        row.put(FWIImportParametre.getCol(5), remarque);
        // Débiteur compensé
        row.put(FWIImportParametre.getCol(6), imprimable);
        // propriété à quittancer
        return row;
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            // Charge le container si pas encore chargé
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            // lit le nouveau entity
            if (container.hasNext()) {
                setEntity((FAEnteteFacture) container.next());
                entity = this.getEntity();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }

    private void prepareValues() {
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

    public void setEntity(globaz.musca.db.facturation.FAEnteteFacture entity) {
        this.entity = entity;
    }

}
