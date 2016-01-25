package globaz.osiris.print.doc;

/**
 * Création du document DTA. Date de création : (22.01.2002 11:34:36)
 * 
 * @author: S.Brand
 */
public class Doc_DTA extends globaz.framework.printing.FWDocument {

    /**
     * Commentaire relatif au constructeur Doc_DTA.
     */
    public Doc_DTA() throws Exception {
        super(globaz.osiris.api.OsirisDef.DEFAULT_APPLICATION_OSIRIS);
        loadWrapper("globaz.framework.printing.FWXmlReport");
    }

    /**
     * Création du document qui va créer un ordre de paiement DTA. Date de création : (22.01.2002 13:13:09)
     * 
     * @param id
     *            java.lang.String
     */
    @Override
    public void _bindData(String id) throws Exception {

        globaz.osiris.db.ordres.CAOrdreGroupe ordre = new globaz.osiris.db.ordres.CAOrdreGroupe();
        ordre.setIdOrdreGroupe(id);
        ordre.setSession(getSession());
        ordre.retrieve();

        String str = globaz.globall.util.JACalendar.todayJJsMMsAAAA();

        globaz.osiris.db.utils.CAAdressePaiementFormatter adr = new globaz.osiris.db.utils.CAAdressePaiementFormatter();
        adr.setAdressePaiement(ordre.getOrganeExecution().getAdressePaiement());

        globaz.osiris.formatter.CAAdresseCourrierFormatter donneurOrdre = new globaz.osiris.formatter.CAAdresseCourrierFormatter();
        donneurOrdre.setAdresseCourrier(adr.getTiersBeneficiaire(), adr.getAdresseCourrierBeneficiaire());
        donneurOrdre.setUseTitle(false);
        donneurOrdre.setUseCountry(false);

        globaz.osiris.formatter.CAAdresseCourrierFormatter donneurBanque = new globaz.osiris.formatter.CAAdresseCourrierFormatter();
        donneurBanque.setAdresseCourrier(adr.getTiersBanque(), adr.getAdresseCourrierBanque());
        donneurBanque.setUseTitle(false);
        donneurBanque.setUseCountry(false);

        String[] adr3 = donneurBanque.getAdresseLines(6);
        String[] adr2 = donneurOrdre.getAdresseLines(6);

        getWrapper().setElement("ligne7", adr2[0]);
        getWrapper().setElement("ligne8", adr2[1]);
        getWrapper().setElement("ligne9", adr2[2]);
        getWrapper().setElement("ligne10", adr2[3]);
        getWrapper().setElement("ligne11", adr2[4]);
        getWrapper().setElement("ligne12", adr2[5]);

        getWrapper().setElement("ligne1", adr3[0]);
        getWrapper().setElement("ligne2", adr3[1]);
        getWrapper().setElement("ligne3", adr3[2]);
        getWrapper().setElement("ligne4", adr3[3]);
        getWrapper().setElement("ligne5", adr3[4]);
        getWrapper().setElement("ligne6", adr3[5]);

        getWrapper().setElement("dateJour", str);
        getWrapper().setElement("lieu", ordre.getOrganeExecution().getAdressePaiement().getTiers().getLieu());
        getWrapper().setElement("numCompte", ordre.getOrganeExecution().getAdressePaiement().getNumCompte());
        getWrapper().setElement("montant", globaz.globall.util.JANumberFormatter.format(ordre.getTotal()));
        getWrapper().setElement("total", globaz.globall.util.JANumberFormatter.format(ordre.getTotal()));
        getWrapper().setElement("idDonneur", ordre.getOrganeExecution().getIdentifiantDTA());
        getWrapper().setElement("idExp", ordre.getOrganeExecution().getIdentifiantDTA());
        getWrapper().setElement("dateSup", ordre.getDateTransmission());
        getWrapper().setElement("dateExe", ordre.getDateEcheance());
        getWrapper().setElement("signature", ordre.getOrganeExecution().getAdressePaiement().getTiers().getNom());

    }
}
