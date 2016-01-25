package globaz.osiris.api.ordre;

import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

public interface APICommonOdreVersement {

    public IntAdressePaiement getAdressePaiement() throws Exception;

    public String getCodeISOMonnaieBonification();

    public String getCodeISOMonnaieDepot();

    public String getCoursConversion();

    public TIAdresseDataSource getDataSourceAdresseCourrier() throws Exception;

    public String getIdOperation();

    public String getMontant();

    public String getMotif();

    public String getMotifFormatOPAE();

    public String getNomPrenom() throws Exception;

    public String getNumTransaction();

    public String getReferenceBVR();
}
