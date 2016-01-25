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
     * Indique si l'impl�mentation des h�ritages est g�r�e automatiquement ( <code>true</code> par d�faut)
     * <p>
     * Cette m�thode doit �tre surcharg�e pour renvoyer <code>false</code> si les entit�s ont comme cible la m�me table
     * (dans ce cas, l'entit� parente est certainement une classe abstraite...)
     * 
     * @return <code>true</code> si l'impl�mentation des h�ritages est g�r�e automatiquement, <code>false</code> sinon
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
        // TODO : Gestion des monaies �trang�res ?

        return getSession().getCode(getCsCodeIsoMonnaie());
    }

    @Override
    public String getCodeISOMonnaieDepot() {
        // TODO : Gestion des monaies �trang�res ?

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
