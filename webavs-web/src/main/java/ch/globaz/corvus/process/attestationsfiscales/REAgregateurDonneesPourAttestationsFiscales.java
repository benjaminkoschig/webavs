package ch.globaz.corvus.process.attestationsfiscales;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscales;
import globaz.corvus.db.attestationsFiscales.REDonneesPourAttestationsFiscalesManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * <p>
 * Utilitaire permettant d'agreger les donn�es retourn�es par le manager
 * {@link REDonneesPourAttestationsFiscalesManager}.
 * </p>
 * <p>
 * Regroupe les donn�es brutes par tiers requ�rant (tiers auquel l'attestation fiscale sera envoy�e) et ajoute tous les
 * tiers b�n�ficiaires (et leurs rentes) li�s � ce tiers requ�rant (par ID tiers de la base de calcul)
 * </p>
 * <p>
 * L'id�e de d�part �tait de ne faire qu'une requ�te afin de r�cup�rer toutes les donn�es n�cessaires aux attestations
 * fiscales, mais l'adresse de courrier du tiers doit obligatoirement �tre charg�e par l'int�rm�diaire du module des
 * tiers (PYXIS). C'est d� � une cascade d'appelle sp�cifique (selon le domaine de l'adresse), car dans un futur proche,
 * cette cascade sera configurable dans le module tiers.<br/>
 * Il sera donc impossible d'assurer la validit� d'une adresse si on ne passe pas par le module tiers pour la r�cup�rer.
 * </p>
 * 
 * @author PBA
 * @see REDonneesPourAttestationsFiscalesManager
 * @see REGenererAttestationsFiscalesProcess
 */
public class REAgregateurDonneesPourAttestationsFiscales {

    private BSession session;

    public REAgregateurDonneesPourAttestationsFiscales(BSession session) {
        super();

        this.session = session;
    }

    private void chargerAdresseCourrierEtTitreTiers(RETiersPourAttestationsFiscales tiers) throws Exception {
        ITITiers tiersTitre = (ITITiers) session.getAPIFor(ITITiers.class);

        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getIdTiers());

        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }

        tiers.setTitreTiers(tiersTitre.getFormulePolitesse(tiers.getCsLangue()));
        String adresseCourrier = PRTiersHelper.getAdresseCourrierFormateeRente(session, tiers.getIdTiers(),
                REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

        if (JadeStringUtil.isBlank(adresseCourrier)) {
            adresseCourrier = PRTiersHelper.getAdresseDomicileFormatee(session, tiers.getIdTiers());
        }

        if (JadeStringUtil.isBlank(adresseCourrier) && (tiers.getRentes().size() > 0)) {
            Iterator<RERentePourAttestationsFiscales> iterateurDesRentes = tiers.getRentes().iterator();
            do {
                RERentePourAttestationsFiscales uneRenteDuTiers = iterateurDesRentes.next();

                TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session, null,
                        uneRenteDuTiers.getIdTiersAdressePaiement(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, null,
                        null);
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                source.load(adresse);

                new TIAdressePaiementBeneficiaireFormater().format(source);

            } while (iterateurDesRentes.hasNext() && JadeStringUtil.isBlank(adresseCourrier));
        }

        tiers.setAdresseCourrierFormatee(adresseCourrier);
    }

    private void chargerAdressesCourrierEtTitresFamilles(List<REFamillePourAttestationsFiscales> familles)
            throws Exception {

        for (REFamillePourAttestationsFiscales uneFamille : familles) {
            RETiersPourAttestationsFiscales tiersRequerant = uneFamille.getTiersRequerant();

            chargerAdresseCourrierEtTitreTiers(tiersRequerant);

            // on charge les adresses de courrier de chacun des membres de la famille pour les attestations fiscales
            // de rente de survivant
            if (REAttestationsFiscalesUtils.isAttestationRenteSurvivant(uneFamille)) {
                for (RETiersPourAttestationsFiscales unTiersBeneficiaire : uneFamille.getTiersBeneficiaires()) {
                    chargerAdresseCourrierEtTitreTiers(unTiersBeneficiaire);
                    if (JadeStringUtil.isBlank(unTiersBeneficiaire.getAdresseCourrierFormatee())) {
                        unTiersBeneficiaire.setAdresseCourrierFormatee(getAdressePaiementFormatee(unTiersBeneficiaire));
                    }
                }
            }
        }
    }

    private String getAdressePaiementFormatee(RETiersPourAttestationsFiscales unTiersBeneficiaire) {
        String idTiersAdressePaiement = null;

        for (RERentePourAttestationsFiscales uneRenteDuTiers : unTiersBeneficiaire.getRentes()) {
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRenteDuTiers
                    .getCodePrestation()));
            if (codePrestation.isAPI()) {
                continue;
            }
            idTiersAdressePaiement = uneRenteDuTiers.getIdTiersAdressePaiement();
            if (!JadeStringUtil.isBlank(idTiersAdressePaiement)) {
                break;
            }
        }

        if (JadeStringUtil.isBlank(idTiersAdressePaiement)) {
            return "";
        }

        try {
            TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(session, null,
                    idTiersAdressePaiement, REApplication.CS_DOMAINE_ADRESSE_CORVUS, null, null);
            TIAdressePaiementDataSource dataSource = new TIAdressePaiementDataSource();
            dataSource.load(adressePaiement);
            return new TIAdresseFormater().format(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getCodeIsoFromCsLangue(String csLangueTiersBaseCalcul) {
        if (ITITiers.CS_FRANCAIS.equals(csLangueTiersBaseCalcul)) {
            return "fr";
        } else if (ITITiers.CS_ALLEMAND.equals(csLangueTiersBaseCalcul)) {
            return "de";
        } else if (ITITiers.CS_ITALIEN.equals(csLangueTiersBaseCalcul)) {
            return "it";
        }
        return null;
    }

    private RERentePourAttestationsFiscales getRente(REDonneesPourAttestationsFiscales uneDonnee) {
        RERentePourAttestationsFiscales rente = new RERentePourAttestationsFiscales();

        rente.setIdRenteAccordee(uneDonnee.getIdRenteAccordee());

        rente.setCodePrestation(uneDonnee.getCodePrestation());
        rente.setDateDebutDroit(uneDonnee.getDateDebutDroit());
        rente.setDateDecision(uneDonnee.getDateDecision());
        rente.setDateFinDroit(uneDonnee.getDateFinDroit());
        rente.setFractionRente(uneDonnee.getFractionRente());
        rente.setQuotiteRente(uneDonnee.getQuotiteRente());
        rente.setIdTiersAdressePaiement(uneDonnee.getIdTiersAdressePaiement());
        rente.setIdTiersBeneficiaire(uneDonnee.getIdTiersBeneficiaire());
        rente.setIsRenteBloquee(uneDonnee.getIsPrestationBloquee());
        rente.setMontantPrestation(uneDonnee.getMontantPrestation());

        return rente;
    }

    private RERetenuePourAttestationsFiscales getRetenue(REDonneesPourAttestationsFiscales uneDonnee) {
        RERetenuePourAttestationsFiscales retenue = new RERetenuePourAttestationsFiscales();

        retenue.setIdRetenue(uneDonnee.getIdRetenue());

        retenue.setCsType(uneDonnee.getCsTypeRetenue());
        retenue.setDateDebut(uneDonnee.getDateDebutRetenue());
        retenue.setDateFin(uneDonnee.getDateFinRetenue());

        return retenue;
    }

    private RETiersPourAttestationsFiscales getTiersBaseCalcul(REDonneesPourAttestationsFiscales uneDonnee) {
        RETiersPourAttestationsFiscales tiers = new RETiersPourAttestationsFiscales();

        tiers.setIdTiers(uneDonnee.getIdTiersBaseCalcul());

        tiers.setNom(uneDonnee.getNomTiersBaseCalcul());
        tiers.setPrenom(uneDonnee.getPrenomTiersBaseCalcul());
        tiers.setNumeroAvs(uneDonnee.getNumeroAvsTiersBaseCalcul());
        tiers.setCsSexe(uneDonnee.getCsSexeTiersBaseCalcul());
        tiers.setDateNaissance(uneDonnee.getDateNaissanceTiersBaseCalcul());
        tiers.setDateDeces(uneDonnee.getDateDecesTiersBaseCalcul());

        tiers.setCsLangue(uneDonnee.getCsLangueTiersBaseCalcul());
        tiers.setCodeIsoLangue(getCodeIsoFromCsLangue(uneDonnee.getCsLangueTiersBaseCalcul()));

        return tiers;
    }

    private RETiersPourAttestationsFiscales getTiersBeneficiaire(REDonneesPourAttestationsFiscales uneDonnee) {
        RETiersPourAttestationsFiscales tiers = new RETiersPourAttestationsFiscales();

        tiers.setIdTiers(uneDonnee.getIdTiersBeneficiaire());

        tiers.setNom(uneDonnee.getNomTiersBeneficiaire());
        tiers.setPrenom(uneDonnee.getPrenomTiersBeneficiaire());
        tiers.setNumeroAvs(uneDonnee.getNumeroAvsTiersBeneficiaire());
        tiers.setCsSexe(uneDonnee.getCsSexeTiersBeneficiaire());
        tiers.setDateNaissance(uneDonnee.getDateNaissanceTiersBeneficiaire());
        tiers.setDateDeces(uneDonnee.getDateDecesTiersBeneficiaire());

        tiers.setCsLangue(uneDonnee.getCsLangueTiersBeneficiaire());
        tiers.setCodeIsoLangue(getCodeIsoFromCsLangue(uneDonnee.getCsLangueTiersBaseCalcul()));

        return tiers;
    }

    /**
     * <p>
     * D�coupe l'attestation en plusieurs si des rentes de survivants de cette famille sont pay�es � des endroits
     * diff�rents.
     * </p>
     * <p>
     * Si des rentes autres que survivants sont pay�es � des endroits diff�rents, la propri�t�
     * {@link REFamillePourAttestationsFiscales#hasPlusieursAdressePaiement()} sera � <code>true</code> afin qu'une
     * phrase suppl�mentaire soit imprim�e dans l'attestation fiscale de cette famille.
     * </p>
     * 
     * @param values
     * @return
     */
    private List<REFamillePourAttestationsFiscales> spliterSiNecessaire(
            Collection<REFamillePourAttestationsFiscales> values) {
        List<REFamillePourAttestationsFiscales> familles = new ArrayList<REFamillePourAttestationsFiscales>();

        for (REFamillePourAttestationsFiscales uneFamille : values) {

            boolean isRenteSurvivant = false;
            boolean hasIdAdressePmtAZero = false;
            Map<String, Set<RETiersPourAttestationsFiscales>> tiersParIdAdressePaiement = new HashMap<String, Set<RETiersPourAttestationsFiscales>>();

            for (RETiersPourAttestationsFiscales unTiersBeneficiaire : uneFamille.getTiersBeneficiaires()) {
                for (RERentePourAttestationsFiscales uneRenteDuTiers : unTiersBeneficiaire.getRentes()) {

                    if (JadeStringUtil.isBlankOrZero(uneRenteDuTiers.getIdTiersAdressePaiement())) {
                        hasIdAdressePmtAZero = true;
                    }

                    CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRenteDuTiers
                            .getCodePrestation()));
                    if (codePrestation.isAPI()) {
                        continue;
                    }
                    if (codePrestation.isSurvivant()) {
                        isRenteSurvivant = true;
                    }

                    if (tiersParIdAdressePaiement.containsKey(uneRenteDuTiers.getIdTiersAdressePaiement())) {
                        Set<RETiersPourAttestationsFiscales> tiersAvecCetteAdressePaiement = tiersParIdAdressePaiement
                                .get(uneRenteDuTiers.getIdTiersAdressePaiement());
                        tiersAvecCetteAdressePaiement.add(unTiersBeneficiaire);
                    } else {
                        tiersParIdAdressePaiement.put(uneRenteDuTiers.getIdTiersAdressePaiement(),
                                new HashSet<RETiersPourAttestationsFiscales>(Arrays.asList(unTiersBeneficiaire)));
                    }
                }
            }

            if (tiersParIdAdressePaiement.size() > 1) {
                if (isRenteSurvivant) {
                    for (Set<RETiersPourAttestationsFiscales> tiersPourUneAdresse : tiersParIdAdressePaiement.values()) {
                        REFamillePourAttestationsFiscales nouvelleFamille = new REFamillePourAttestationsFiscales();
                        for (RETiersPourAttestationsFiscales unTiers : tiersPourUneAdresse) {
                            if (nouvelleFamille.getTiersRequerant() == null) {
                                nouvelleFamille.setTiersRequerant(unTiers);
                            }
                            nouvelleFamille.getMapTiersBeneficiaire().put(unTiers.getIdTiers(), unTiers);
                        }
                        familles.add(nouvelleFamille);
                    }
                } else {
                    /*
                     * Si size == 2 et un id adresse pmt � z�ro on ne met pas le flag � true
                     */
                    if (hasIdAdressePmtAZero && tiersParIdAdressePaiement.size() == 2) {
                        uneFamille.setHasPlusieursAdressePaiement(false);
                    } else {
                        uneFamille.setHasPlusieursAdressePaiement(true);
                    }
                    familles.add(uneFamille);
                }
            } else {
                familles.add(uneFamille);
            }
        }

        return familles;
    }

    /**
     * Regroupe les donn�es brutes par tiers requ�rant (tiers auquel l'attestation fiscale sera envoy�e) et ajoute tous
     * les tiers b�n�ficiaires (et leurs rentes) li�s � ce tiers requ�rant (par ID tiers de la base de calcul)
     * 
     * @param donnees
     *            les donn�es brutes charg�es par {@link REDonneesPourAttestationsFiscalesManager}
     * @return les donn�es regroup�es par tiers requ�rant
     * @throws Exception
     *             dans le cas o� un probl�me survient lors de la r�cup�ration des adresses de courrier/paiement et du
     *             titre du tiers requ�rant
     */
    public List<REFamillePourAttestationsFiscales> transformer(List<REDonneesPourAttestationsFiscales> donnees)
            throws Exception {
        Map<String, REFamillePourAttestationsFiscales> famillesParIdTiersBaseCalcul = new HashMap<String, REFamillePourAttestationsFiscales>();

        for (REDonneesPourAttestationsFiscales uneDonnee : donnees) {
            REFamillePourAttestationsFiscales uneFamille;
            if (famillesParIdTiersBaseCalcul.containsKey(uneDonnee.getIdTiersBaseCalcul())) {
                uneFamille = famillesParIdTiersBaseCalcul.get(uneDonnee.getIdTiersBaseCalcul());
            } else {
                uneFamille = new REFamillePourAttestationsFiscales();
                uneFamille.setTiersRequerant(getTiersBaseCalcul(uneDonnee));
                famillesParIdTiersBaseCalcul.put(uneDonnee.getIdTiersBaseCalcul(), uneFamille);
            }

            RETiersPourAttestationsFiscales unTiersBeneficaire;
            if (uneFamille.getMapTiersBeneficiaire().containsKey(uneDonnee.getIdTiersBeneficiaire())) {
                unTiersBeneficaire = uneFamille.getMapTiersBeneficiaire().get(uneDonnee.getIdTiersBeneficiaire());
            } else {
                unTiersBeneficaire = getTiersBeneficiaire(uneDonnee);
                uneFamille.getMapTiersBeneficiaire().put(uneDonnee.getIdTiersBeneficiaire(), unTiersBeneficaire);
            }

            RERentePourAttestationsFiscales uneRenteDuBeneficiaire;
            if (unTiersBeneficaire.getMapRentes().containsKey(uneDonnee.getIdRenteAccordee())) {
                uneRenteDuBeneficiaire = unTiersBeneficaire.getMapRentes().get(uneDonnee.getIdRenteAccordee());
            } else {
                uneRenteDuBeneficiaire = getRente(uneDonnee);
                unTiersBeneficaire.getMapRentes().put(uneDonnee.getIdRenteAccordee(), uneRenteDuBeneficiaire);
            }

            if (!uneRenteDuBeneficiaire.getMapRetenues().containsKey(uneDonnee.getIdRetenue())) {
                uneRenteDuBeneficiaire.getMapRetenues().put(uneDonnee.getIdRetenue(), getRetenue(uneDonnee));
            }
        }

        List<REFamillePourAttestationsFiscales> familles = spliterSiNecessaire(famillesParIdTiersBaseCalcul.values());

        chargerAdressesCourrierEtTitresFamilles(familles);

        // tri par ordre alphab�tique (nom, pr�nom) des tiers requ�rant
        Collections.sort(familles);
        return familles;
    }
}
