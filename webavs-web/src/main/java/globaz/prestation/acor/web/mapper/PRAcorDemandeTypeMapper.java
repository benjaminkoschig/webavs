package globaz.prestation.acor.web.mapper;

import acor.xsd.common.OrganisationAdresseType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BSession;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
@AllArgsConstructor
public class PRAcorDemandeTypeMapper {

    private final BSession session;
    private final PRTiersWrapper tiersRequerant;

    public DemandeType map() {
        DemandeType demandeType = new DemandeType();

        // NOUVELLES DONNES XSD OBLIGATOIRES
        demandeType.setCaisseAgence(getCaisseAgence(session));
        //TODO adresse de la caisse par des propriétés applications à créer
        demandeType.setAdresseCaisse(createAdresseCaisse());
        demandeType.setMoisRapport(0);
        String langueISO = PRUtil.getISOLangueTiers(tiersRequerant.getLangue());
        demandeType.setLangue(langueISO.toLowerCase(Locale.ROOT).substring(0,1));
        return demandeType;
    }

    private OrganisationAdresseType createAdresseCaisse() {
        OrganisationAdresseType adresseCaisse = new OrganisationAdresseType();

        try {
            ITIAdministration cre = CaisseHelperFactory.getInstance().getAdministrationCaisse(session);
            TITiers tiers = getTiers(cre.getIdTiers());

            TIAdresseDataSource adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                                                                       null, null, true, cre.getLangue());

            if(adresse!=null) {
                adresseCaisse.setNom(cre.getNom());
                adresseCaisse.setAdresse(adresse.casePostale);
                adresseCaisse.setLocalite(adresse.localiteNom);
                adresseCaisse.setCodePostal(adresse.localiteNpa);
                adresseCaisse.setPays(PRConverterUtils.formatRequiredInteger(cre.getIdPays()));
            }else{
                adresseCaisse.setNom("Nom caisse");
                adresseCaisse.setAdresse("Rue de la caisse ou case postale");
                adresseCaisse.setLocalite("Localité de la caisse");
                adresseCaisse.setCodePostal("1111");
                adresseCaisse.setPays(100);//Suisse
            }

        } catch (Exception e) {
            LOG.error("Impossible de récupérer l'adresse de la caisse.", e);
        }
        return adresseCaisse;
    }

    private TITiers getTiers(String idTiers) {
        try {
            TITiers tiers = new TITiers();
            tiers.setSession(session);
            tiers.setIdTiers(idTiers);
            tiers.retrieve();
            return tiers;
        } catch (Exception exception) {
            throw new CommonTechnicalException("Impossible de récupérer le tiers avec l'idTiers: " + idTiers, exception);
        }
    }

    private Integer getCaisseAgence(BSession session) {
        int caisseAgence = 0;
        try {
            String noCaisse = session.getApplication().getProperty(CommonProperties.KEY_NO_CAISSE);
            String noAgence = session.getApplication().getProperty(CommonProperties.KEY_NO_AGENCE);
            caisseAgence = Integer.parseInt(noCaisse + noAgence);
        } catch (Exception e) {
            throw new CommonTechnicalException("Erreur lors de la récupération du numéro de la caisse: ", e);
        }
        return caisseAgence;
    }
}
