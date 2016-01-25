/*
 * Créé le 27 juin 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.db.declaration;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.GlobazServer;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.hercule.db.declarationStructuree.CESaisieMasseReceptionViewBean;
import globaz.hercule.utils.CECodeBarreUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;

/**
 * @author sda
 */
public class DSSaisieMasseAutomatiqueViewBean extends BJadePersistentObjectViewBean {

    public static final String TYPE_DOCUMENT_DECLARATION_LTN = "201";
    // Constantes
    public static final String TYPE_DOCUMENT_DECLARATION_SALAIRE = "088";
    public static final String TYPE_DOCUMENT_DECLARATION_STRUCTUREE_D002 = "238";
    public static final String TYPE_DOCUMENT_DECLARATION_STRUCTUREE_D005 = "239";

    // Attributs
    private String barcode = "";
    private String dateRetourEff = "";
    private String saisieEcran = "";
    private String typeDeclarationEcran = "";

    // Constructeurs
    public DSSaisieMasseAutomatiqueViewBean() {
        super();
    }

    // Méthodes

    @Override
    public void add() throws Exception {

        try {
            if (JadeStringUtil.isEmpty(getBarcode()) || JadeStringUtil.isEmpty(getDateRetourEff())
                    || JadeStringUtil.isEmpty(getTypeDeclarationEcran())) {
                throw new Exception(DSSaisieMasseAutomatiqueViewBean.class.getName() + "\n"
                        + ((BSession) getISession()).getLabel("CHAMPS_SAISIE_VIDE_OU_NON_VALIDE"));
            }

            String idDocument = CECodeBarreUtils.giveIdDocument(getBarcode());
            String annee = CECodeBarreUtils.giveAnnee(getBarcode());
            String numeroAffilie = CECodeBarreUtils.giveNumeroAffilieFormate(getBarcode());

            if (DSSaisieMasseAutomatiqueViewBean.TYPE_DOCUMENT_DECLARATION_SALAIRE.equalsIgnoreCase(idDocument)
                    || DSSaisieMasseAutomatiqueViewBean.TYPE_DOCUMENT_DECLARATION_LTN.equalsIgnoreCase(idDocument)) {
                DSSaisieMasseAutomatiqueDeclarationSalaireViewBean saisieAutomatiqueDeclarationSalaireVB = new DSSaisieMasseAutomatiqueDeclarationSalaireViewBean();

                saisieAutomatiqueDeclarationSalaireVB.setISession(getISession());
                saisieAutomatiqueDeclarationSalaireVB.setBarcode(getBarcode());
                saisieAutomatiqueDeclarationSalaireVB.setDateRetourEff(getDateRetourEff());
                if (DSSaisieMasseAutomatiqueViewBean.TYPE_DOCUMENT_DECLARATION_LTN.equalsIgnoreCase(idDocument)) {
                    saisieAutomatiqueDeclarationSalaireVB.setTypeDeclaration(DSDeclarationViewBean.CS_LTN);
                } else {
                    saisieAutomatiqueDeclarationSalaireVB.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                }
                saisieAutomatiqueDeclarationSalaireVB.setSaisieEcran(getSaisieEcran());
                saisieAutomatiqueDeclarationSalaireVB.add();
            } else if (DSSaisieMasseAutomatiqueViewBean.TYPE_DOCUMENT_DECLARATION_STRUCTUREE_D002
                    .equalsIgnoreCase(idDocument)
                    || DSSaisieMasseAutomatiqueViewBean.TYPE_DOCUMENT_DECLARATION_STRUCTUREE_D005
                            .equalsIgnoreCase(idDocument)) {
                String idAffiliation = giveIdAffiliation(numeroAffilie, annee);

                CESaisieMasseReceptionViewBean saisieReceptionVB = new CESaisieMasseReceptionViewBean();
                saisieReceptionVB.setISession(getISession());
                saisieReceptionVB.setForIdAffiliation(idAffiliation);
                saisieReceptionVB.setForNumeroAffilie(numeroAffilie);
                saisieReceptionVB.setForAnnee(annee);
                saisieReceptionVB.setForDateReception(getDateRetourEff());
                saisieReceptionVB.add();
            }
        } catch (Exception e) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(e.getMessage());
            return;
        }

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Getter
     */
    public String getBarcode() {
        return barcode;
    }

    public String getDateRetourEff() {
        return dateRetourEff;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSaisieEcran() {
        return saisieEcran;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTypeDeclarationEcran() {
        return typeDeclarationEcran;
    }

    private String giveIdAffiliation(String numeroAffilie, String annee) throws Exception {
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        AFAffiliation affiliation = application.getAffilieByNo((BSession) getISession(), numeroAffilie, true, false,
                "", "", annee, "", "");
        if (affiliation == null) {
            throw new Exception(DSSaisieMasseAutomatiqueViewBean.class.getName() + "\n"
                    + ((BSession) getISession()).getLabel("AUCUNE_AFFILIATION_EXISTE"));
        }

        return affiliation.getAffiliationId();
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * Setter
     */
    public void setBarcode(String newBarcode) {
        barcode = newBarcode;
    }

    public void setDateRetourEff(String newDateRetourEff) {
        dateRetourEff = newDateRetourEff;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setSaisieEcran(String newSaisieEcran) {
        saisieEcran = newSaisieEcran;
    }

    public void setTypeDeclarationEcran(String newTypeDeclarationEcran) {
        typeDeclarationEcran = newTypeDeclarationEcran;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
