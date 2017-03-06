package globaz.musca.itext.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.translation.CodeSystem;
import globaz.musca.util.FAUtil;
import globaz.pyxis.db.tiers.TITiers;
import net.sf.jasperreports.engine.JRException;
import ch.globaz.common.domaine.Montant;

class DecomptesLoader {
    private final BSession session;
    private final String idPassage;
    private final String idTriDecompte;
    private final String idSousType;
    private final String idTri;
    private FWCurrency montantMinimeNeg;
    private FWCurrency montantMinimePos;
    private FWCurrency montantMinimeMax;
    private FAListDecompteNew_DS manager;

    public DecomptesLoader(BSession session, String idPassage, String idTriDecompte, String idSousType, String idTri) {
        this.session = session;
        this.idPassage = idPassage;
        this.idTriDecompte = idTriDecompte;
        this.idSousType = idSousType;
        this.idTri = idTri;
        loadProperties();
    }

    public void load() {
        try {
            manager = new FAListDecompteNew_DS();
            manager.setSession(session);
            // Where clause
            manager.setForIdPassage(idPassage);
            manager.setForTriDecompte(idTriDecompte);
            manager.setForIdSousType(idSousType);
            // Order by
            manager.setOrderBy(idTri);
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new RuntimeException("Unable to find the decompte with this idPassage:" + idPassage, e);
        }
    }

    public int size() {
        return manager.getSize();
    }

    public boolean next() {
        try {
            return manager.next();
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public FaDecompteBeanXls getBean() {
        FAEnteteFacture entity = manager.getEntity();
        FaDecompteBeanXls bean = new FaDecompteBeanXls();
        bean.setDebiteur(retriveDescriptionTiers(entity));
        bean.setAddressPaiement(retriveAdressePaiement(entity));
        bean.setDecompte(entity.getDescriptionDecompte());
        bean.setMontant(new Montant(entity.getTotalFacture()));
        boolean isAdressePaimentEmpty = JadeStringUtil.isBlankOrZero(bean.getAddressPaiement());
        bean.setRecRem(generateRemarque(entity, isAdressePaimentEmpty));
        bean.setImprimable(resolveIsImprimable(entity));
        return bean;
    }

    private void loadProperties() {
        montantMinimeNeg = new FWCurrency(FAApplication.MONTANT_MINIMENEG_DEFVAL);
        montantMinimePos = new FWCurrency(FAApplication.MONTANT_MINIMEPOS_DEFVAL);
        montantMinimeMax = new FWCurrency(FAApplication.MONTANT_MINIMEMAX_DEFVAL);
        try {
            montantMinimeNeg = new FWCurrency(session.getApplication().getProperty(FAApplication.MONTANT_MINIMENEG));
            montantMinimePos = new FWCurrency(session.getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS));
            montantMinimeMax = new FWCurrency(session.getApplication().getProperty(FAApplication.MONTANT_MINIMEMAX));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.04.2003 16:16:34)
     * 
     * @return java.lang.String
     */
    private String resolveIsImprimable(FAEnteteFacture entity) {
        // marquer d'un X si la facture est non imprimable
        if (!entity.isNonImprimable()
                && !FAEnteteFacture.CS_MODE_IMP_PASIMPZERO.equalsIgnoreCase(entity.getIdCSModeImpression())) {
            return "X";
        } else {
            return "";
        }
    }

    private String retriveDescriptionTiers(FAEnteteFacture entity) {
        StringBuilder descriptionTiers = new StringBuilder();

        TITiers tiers = new TITiers();
        tiers.setSession(session);
        tiers.setIdTiers(entity.getIdTiers());
        try {
            tiers.retrieve();
            descriptionTiers.append(entity.getDescriptionRole() + " " + entity.getIdExterneRole() + ", "
                    + tiers.getDesignation1() + " " + tiers.getDesignation2());
            String localiteTiers = tiers.getLocalite();

            if (!JadeStringUtil.isBlank(localiteTiers)) {
                descriptionTiers.append(", " + localiteTiers);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return descriptionTiers.toString();
    }

    private String retriveAdressePaiement(FAEnteteFacture entity) {
        String adressePaiement = "";
        FWCurrency totalFacture = new FWCurrency(entity.getTotalFacture());

        if ((totalFacture.compareTo(montantMinimeNeg) > 0) && (totalFacture.compareTo(montantMinimePos) < 0)) {
            adressePaiement = "";
        } else {
            try {
                adressePaiement = FAUtil
                        .getAdressePrincipalePaiementFromLinkPmt(session, entity.getIdAdressePaiement());
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        if (JadeStringUtil.isBlankOrZero(adressePaiement)) {
            adressePaiement = "";
        }
        return adressePaiement;
    }

    private String generateRemarque(FAEnteteFacture entity, boolean isAdressePaimentEmpty) {

        boolean isRetenu = FAEnteteFacture.CS_MODE_RETENU.equalsIgnoreCase(CodeSystem.getCode(entity
                .getIdModeRecouvrement()));
        StringBuilder remBuffer = new StringBuilder();
        FWCurrency totalFacture = new FWCurrency(entity.getTotalFacture());

        if (isRetenu) {
            remBuffer.append(CodeSystem.getCodeLibelle(FAEnteteFacture.CS_MODE_RETENU));

        } // remarque pour montant minime
        if (!totalFacture.isZero() && (totalFacture.compareTo(montantMinimeNeg) > 0)
                && (totalFacture.compareTo(montantMinimePos) < 0)) {
            try {
                remBuffer.append(session.getApplication().getLabel("LISDEC_REMMINIME", session.getIdLangueISO()));
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        try {
            if (((FAApplication) session.getApplication()).isModeReporterMontantMinimal()) {
                if (!totalFacture.isZero() && (totalFacture.compareTo(montantMinimeMax) < 0)
                        && (totalFacture.compareTo(montantMinimePos) > 0)) {
                    remBuffer.append(session.getApplication().getLabel("FACREPORT", session.getIdLangueISO()));
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if (totalFacture.isNegative() && isAdressePaimentEmpty) {
            try {
                // retour à la ligne
                if (remBuffer.length() != 0) {
                    remBuffer.append("\n");
                }
                remBuffer.append(session.getApplication().getLabel("LISDEC_REMNOADRPAIMENT", session.getIdLangueISO()));
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        // rajouter la remarque de l'entity
        remBuffer.append(getRemarque(entity));
        return remBuffer.toString();
    }

    private String getRemarque(FAEnteteFacture entity) {
        String rem = "";
        try {
            if (!entity.getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_AUTOMATIQUE)) {
                rem = globaz.musca.translation.CodeSystem.getLibelle(session, entity.getIdModeRecouvrement());
            }
            return rem + entity.getRemarque();
        } catch (Exception ex) {
            return "Remarque could not be found: " + ex.getMessage();
        }
    }

}
