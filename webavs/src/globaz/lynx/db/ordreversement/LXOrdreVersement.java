package globaz.lynx.db.ordreversement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.service.tiers.LXAdressePaiement;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

public class LXOrdreVersement extends LXOperation implements APICommonOdreVersement {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LXFournisseur fournisseur;

    /**
     * Indique si l'implémentation des héritages est gérée automatiquement ( <code>true</code> par défaut)
     * <p>
     * Cette méthode doit être surchargée pour renvoyer <code>false</code> si les entités ont comme cible la même table
     * (dans ce cas, l'entité parente est certainement une classe abstraite...)
     * 
     * @return <code>true</code> si l'implémentation des héritages est gérée automatiquement, <code>false</code> sinon
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    @Override
    public IntAdressePaiement getAdressePaiement() throws Exception {
        IntAdressePaiement adressePaiement = new LXAdressePaiement();
        adressePaiement.setISession(getSession());

        adressePaiement.retrieve(getIdAdressePaiement());

        return adressePaiement;
    }

    @Override
    public String getCodeISOMonnaieBonification() {
        // TODO : Gestion des monaies étrangères ?

        return getSession().getCode(getCsCodeIsoMonnaie());
    }

    @Override
    public String getCodeISOMonnaieDepot() {
        // TODO : Gestion des monaies étrangères ?

        return getSession().getCode(getCsCodeIsoMonnaie());
    }

    @Override
    public String getCoursConversion() {
        return getCoursMonnaie();
    }

    @Override
    public TIAdresseDataSource getDataSourceAdresseCourrier() throws Exception {
        return LXTiersService.getAdresseFournisseurCourrierAsDataSource(getSession(), null, getFournisseur()
                .getIdTiers());
    }

    /**
     * Charge le fournisseur.
     * 
     * @return
     * @throws Exception
     */
    public LXFournisseur getFournisseur() throws Exception {
        if (((fournisseur == null) || fournisseur.isNew())) {
            LXSection section = LXHelperUtils.getSection(getSession(), null, getIdSection());
            fournisseur = LXHelperUtils.getFournisseur(getSession(), null, section.getIdFournisseur());
        }

        return fournisseur;
    }

    @Override
    public String getMotifFormatOPAE() {
        if (JadeStringUtil.isBlank(getMotif())) {
            return getMotif();
        } else {
            return "";
        }
    }

    @Override
    public String getNomPrenom() throws Exception {
        return LXTiersService.getFournisseurNomPrenom(getSession(), null, getFournisseur().getIdTiers());
    }

    @Override
    public String getNumTransaction() {
        return getNumeroTransaction();
    }

}
