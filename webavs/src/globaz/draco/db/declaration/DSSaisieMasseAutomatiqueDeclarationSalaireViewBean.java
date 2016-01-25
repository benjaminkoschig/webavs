/*
 * Créé le 27 juin 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.db.declaration;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;

/**
 * @author sda
 */
public class DSSaisieMasseAutomatiqueDeclarationSalaireViewBean extends DSDeclarationViewBean implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String barcode = "";

    /**
	 *
	 */
    public DSSaisieMasseAutomatiqueDeclarationSalaireViewBean() {
        super();
    }

    /**
     * Permet d'effectuer des opérations avant de lancer l'ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {

        // Parse le codebar
        if (getBarcode().substring(0, 3).equals("088") || "201".equals(getBarcode().substring(0, 3))) {
            setAnnee(getBarcode().substring(3, 7));
            setAffilieNumero(getBarcode().substring(7));
            String numeroAffilie = getBarcode().substring(7);

            IFormatData affilieFormater;
            try {
                // On formate les numéros d'affiliés
                CIApplication ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                affilieFormater = ciApp.getAffileFormater();
                if (affilieFormater != null) {
                    numeroAffilie = affilieFormater.format(numeroAffilie);
                }
            } catch (Exception e1) {
                _addError(transaction, e1.getMessage());
            }

            // setAffiliationId(getIdAffiliation(numeroAffilie.substring(0, 3) +
            // "." + numeroAffilie.substring(3, 7))); // TODO utiliser une
            // méthode qui fait ca
            setAffiliationId(getIdAffiliation(numeroAffilie));
            if (getBarcode().substring(0, 3).equals("088")) {
                setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
            }
            if (getBarcode().substring(0, 3).equals("201")) {
                setTypeDeclaration(DSDeclarationViewBean.CS_LTN);
            }
        } else {
            _addError(transaction, getSession().getLabel("BAD_DOCUMENT"));
        }

        // lance le beforeAdd()
        super._beforeAdd(transaction);
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Permet de récupérer un affilié
     * 
     * @return Returns a AFAffiliation
     */
    public String getIdAffiliation(String affiliationNumero) {
        /*
         * AFAffiliation affiliation; // Chargement du manager AFAffiliationManager manager = new
         * AFAffiliationManager(); manager.setSession(getSession()); manager.setForAffilieNumero(affiliationNumero); try
         * { manager.find(); if (!manager.isEmpty()) { affiliation = (AFAffiliation) manager.getEntity(0); } else {
         * affiliation = null; } } catch (Exception e) { _addError(null, e.getMessage()); affiliation = null; } return
         * affiliation.getId();
         */
        // Modif jmc pour ne plus prendre par défaut le premie affilie
        String idAffiliation = "";
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);

            AFAffiliation affilie = application.getAffilieByNo(getSession(), affiliationNumero, true, false, "", "",
                    getAnnee(), "", "");
            idAffiliation = affilie.getAffiliationId();
        } catch (Exception e) {

        }
        return idAffiliation;
    }

    /**
     * @param barcode
     *            the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
