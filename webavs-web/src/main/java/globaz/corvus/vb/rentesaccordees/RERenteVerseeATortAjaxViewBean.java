package globaz.corvus.vb.rentesaccordees;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.domaine.RenteAccordee;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.REDetailCalculRenteVerseeATort;
import ch.globaz.corvus.utils.rentesverseesatort.RELigneDetailCalculRenteVerseeATort;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * View bean pour la vue du d�tail d'une rente vers�e � tort
 */
public class RERenteVerseeATortAjaxViewBean implements FWViewBeanInterface, FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<JadeCodeSysteme> codesSystemeTypeRenteVerseeATort;
    private BSpy creationSpy;
    private String dateDeces;
    private String dateNaissance;
    private String descriptionSaisieManuelle;
    private REDetailCalculRenteVerseeATort detailCalcul;
    private Long idDemandeRente;
    private Long idOrdreVersement;
    private Long idRenteSelectionnee;
    private Long idRenteVerseeATort;
    private Long idTiers;
    private String message;
    private boolean modificationPossible;
    private BSpy modificationSpy;
    private BigDecimal montant;
    private String msgType;
    private String nationalite;
    private String nom;
    private NumeroSecuriteSociale nss;
    private String prenom;
    private List<RenteAccordee> rentesNouveauDroit;
    private boolean saisieManuelle;
    private BSession session;
    private String sexe;
    private boolean suppressionDesDecisionsFaite;
    private TypeRenteVerseeATort typeRenteVerseeATort;

    /**
     * Construit l'objet avec tous ses attributs � <code>null</code> except� les collections qui sont initialis�es vides
     */
    public RERenteVerseeATortAjaxViewBean() {
        super();

        codesSystemeTypeRenteVerseeATort = null;
        creationSpy = null;
        dateDeces = null;
        detailCalcul = null;
        descriptionSaisieManuelle = null;
        idDemandeRente = null;
        idOrdreVersement = null;
        idRenteSelectionnee = null;
        idRenteVerseeATort = null;
        idTiers = null;
        message = null;
        modificationSpy = null;
        montant = null;
        msgType = null;
        nom = null;
        nss = null;
        prenom = null;
        rentesNouveauDroit = new ArrayList<RenteAccordee>();
        session = null;
        sexe = null;
        suppressionDesDecisionsFaite = false;
        typeRenteVerseeATort = null;
    }

    /**
     * @return les codes syst�mes pour les type de rente vers�e � tort avec leur traduction
     */
    public Collection<JadeCodeSysteme> getCodesSystemeTypeRenteVerseeATort() {
        return codesSystemeTypeRenteVerseeATort;
    }

    public Integer getCodeSystemeSaisieManuelle() {
        return TypeRenteVerseeATort.SAISIE_MANUELLE.getCodeSysteme();
    }

    /**
     * @return le type de rente vers�e � tort de celle affich�e
     */
    public String getCsTypeRenteVerseeATort() {
        if (typeRenteVerseeATort == null) {
            return "";
        }
        return typeRenteVerseeATort.getCodeSysteme().toString();
    }

    /**
     * @return la date de d�but du paiement du montant vers�e � tort
     */
    public String getDateDebutPeriode() {
        return detailCalcul.getDateDebutPeriode();
    }

    /**
     * @return la date du d�c�s du tiers auquel est li�e cette rente vers�e � tort
     */
    public String getDateDeces() {
        // si retour depuis la page (apr�s modification par exemple)
        if (dateDeces != null) {
            return dateDeces;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getDateDeces();
        }
        return null;
    }

    /**
     * @return la date de fin de paiement du montant vers�e � tort
     */
    public String getDateFinPeriode() {
        return detailCalcul.getDateFinPeriode();
    }

    /**
     * @return la date de naissance du tiers auquel est li�e cette rente vers�e � tort
     */
    public String getDateNaissance() {
        // si retour depuis la page (apr�s modification par exemple)
        if (dateNaissance != null) {
            return dateNaissance;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getDateNaissance();
        }
        return null;
    }

    /**
     * @return la description d'une saisie manuelle (obligatoire pour ce type de rente vers�e � tort)
     */
    public String getDescriptionSaisieManuelle() {
        return descriptionSaisieManuelle;
    }

    /**
     * @return le d�tail du calcul de cette rente vers�e � tort
     */
    public REDetailCalculRenteVerseeATort getDetailCalcul() {
        return detailCalcul;
    }

    /**
     * @return le d�tail du tiers auquel est li�e cette rente vers�e � tort. Contient le NSS; le nom et le pr�nom du
     *         tiers
     */
    public String getDetailRequerant() {
        StringBuilder detailRequerant = new StringBuilder();
        detailRequerant.append(detailCalcul.getNss()).append(" / ").append(detailCalcul.getPrenom()).append(" ")
                .append(detailCalcul.getNom());
        return detailRequerant.toString();
    }

    /**
     * @return l'ID (de BDD) de la demande de rente li�e � cette rente vers�e � tort
     */
    public Long getIdDemandeRente() {
        // si retour depuis la page (apr�s modification par exemple)
        if (idDemandeRente != null) {
            return idDemandeRente;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getIdDemandeRente();
        }
        return null;
    }

    /**
     * @return l'ID (de BDD) de l'ordre de versement li� � cette rente vers�e � tort
     */
    public Long getIdOrdreVersement() {
        return idOrdreVersement;
    }

    /**
     * @return l'ID (de BDD) de la rente accord�e de l'ancien droit ayant permis le calcul de la rente vers�e � tort
     */
    public Long getIdRenteAccordeeAncienDroit() {
        return detailCalcul.getIdRenteAccordeeAncienDroit();
    }

    /**
     * @return l'ID (de BDD) de la rente accord�e du nouveau droit ayant permis le calcul de la rente vers�e � tort
     */
    public Long getIdRenteAccordeeNouveauDroit() {
        return detailCalcul.getIdRenteAccordeeNouveauDroit();
    }

    /**
     * @return l'ID (de BDD) de la rente accord�e que le gestionnaire a saisi sur l'�cran dans le cas d'une rente vers�e
     *         � tort de type saisie manuelle
     */
    public Long getIdRenteSelectionnee() {
        return idRenteSelectionnee;
    }

    /**
     * @return l'ID (de BDD) de la rente vers�e � tort
     */
    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    /**
     * @return l'ID tiers � qui le montant a �t� vers� � tort
     */
    public Long getIdTiers() {
        // si retour depuis la page (apr�s modification par exemple)
        if (idTiers != null) {
            return idTiers;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getIdTiers();
        }
        return null;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @return le d�tail du calcul de la rente vers�e � tort, prestation due par prestation due
     */
    public List<RELigneDetailCalculRenteVerseeATort> getLignesDetail() {
        if (detailCalcul == null) {
            return new ArrayList<RELigneDetailCalculRenteVerseeATort>();
        }
        return detailCalcul.getLignesDetail();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        // rien
        return null;
    }

    /**
     * @return une map contenant comme cl� le code syst�me de type de rente vers�e � tort, et comme valeur la traduction
     *         dans la langue du gestionnaire
     */
    public Map<String, String> getMapCodesSystemeTypeRenteVerseeATort() {
        Langues langueUtilisateur = Langues.getLangueDepuisCodeIso(session.getIdLangueISO());
        Map<String, String> codesSystemesAvecTraduction = new HashMap<String, String>();

        for (JadeCodeSysteme unCodeSysteme : codesSystemeTypeRenteVerseeATort) {
            codesSystemesAvecTraduction.put(unCodeSysteme.getIdCodeSysteme(),
                    unCodeSysteme.getTraduction(langueUtilisateur));
        }
        return codesSystemesAvecTraduction;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return le montant que le gestionnaire a saisi sur l'�cran pour cette rente vers�e � tort
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * @return le montant vers� � tort selon les donn�es stock�es en base de donn�es
     */
    public String getMontantTotalVerseeATort() {
        if (detailCalcul != null) {
            return new FWCurrency(detailCalcul.getMontantTotalVerseeATort().doubleValue()).toStringFormat();
        }
        return null;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * @return la nationalit� du tiers � qui la rente a �t� vers�e � tort
     */
    public String getNationalite() {
        // si retour depuis la page (apr�s modification par exemple)
        if (nationalite != null) {
            return nationalite;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getNationalite();
        }
        return null;
    }

    /**
     * @return le nom du tiers � qui la rente a �t� vers�e � tort
     */
    public String getNom() {
        // si retour depuis la page (apr�s modification par exemple)
        if (nom != null) {
            return nom;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getNom();
        }
        return null;
    }

    /**
     * @return le NSS du tiers � qui la rente a �t� vers�e � tort
     */
    public String getNss() {
        // si retour depuis la page (apr�s modification par exemple)
        if (nss != null) {
            return nss.toString();
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getNss().toString();
        }
        return null;
    }

    /**
     * @return le pr�nom du tiers � qui la rente a �t� vers�e � tort
     */
    public String getPrenom() {
        // si retour depuis la page (apr�s modification par exemple)
        if (prenom != null) {
            return prenom;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getPrenom();
        }
        return null;
    }

    /**
     * @return la liste des rentes accord�es du nouveau droit (de la demande dont est issue la rente vers�e � tort).
     *         Utile au gestionnaire en cas de saisie manuelle de rente vers�e � tort
     */
    public Collection<RenteAccordee> getRentesNouveauDroit() {
        return rentesNouveauDroit;
    }

    /**
     * @return le sexe du tiers � qui la rente a �t� vers�e � tort
     */
    public String getSexe() {
        // si retour depuis la page (apr�s modification par exemple)
        if (sexe != null) {
            return sexe;
        }
        // si chargement du d�tails depuis le helper
        if (detailCalcul != null) {
            return detailCalcul.getSexe();
        }
        return null;
    }

    /**
     * @return les "spy" de base de donn�es dans le m�me format que sur un page de d�tail standard
     */
    public String getSpies() {
        StringBuilder spies = new StringBuilder();
        spies.append("Creation: ");
        if (creationSpy != null) {
            spies.append(creationSpy.getDate()).append(", ").append(creationSpy.getTime()).append(" - ")
                    .append(creationSpy.getUser());
        }
        spies.append(" / ").append("Update: ");
        if (modificationSpy != null) {
            spies.append(modificationSpy.getDate()).append(", ").append(modificationSpy.getTime()).append(" - ")
                    .append(modificationSpy.getUser());
        }
        return spies.toString();
    }

    /**
     * @return le type de la rente vers�e � tort
     */
    public TypeRenteVerseeATort getTypeRenteVerseeATort() {
        return typeRenteVerseeATort;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    /**
     * @return <code>true</code> si la/les d�cision/s li�e/s � la demande de cette rente vers�e � tort ne sont pas
     *         encore valid�es. <code>false</code> si toutes les d�cisions de la demande sont valid�es
     */
    public boolean isModificationPossible() {
        return modificationPossible;
    }

    public boolean isSaisieManuelle() {
        return saisieManuelle;
    }

    /**
     * @return true si lors de la modification d'une rente vers�e � tort, la suppression des d�cisions li�es � la
     *         demande a eu lieu
     */
    public boolean isSuppressionDesDecisionsFaite() {
        return suppressionDesDecisionsFaite;
    }

    @Override
    public Iterator<?> iterator() {
        // rien
        return null;
    }

    public void setCodesSystemeTypeRenteVerseeATort(Collection<JadeCodeSysteme> codesSystemeTypeRenteVerseeATort) {
        this.codesSystemeTypeRenteVerseeATort = codesSystemeTypeRenteVerseeATort;
    }

    public void setCreationSpy(BSpy creationSpy) {
        this.creationSpy = creationSpy;
    }

    public void setCsTypeRenteVerseeATort(String csTypeRenteVerseeATort) {
        typeRenteVerseeATort = TypeRenteVerseeATort.parse(csTypeRenteVerseeATort);
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDescriptionSaisieManuelle(String descriptionSaisieManuelle) {
        this.descriptionSaisieManuelle = descriptionSaisieManuelle;
    }

    public void setDetailCalcul(REDetailCalculRenteVerseeATort detailCalcul) {
        this.detailCalcul = detailCalcul;
    }

    @Override
    public void setGetListe(boolean getListe) {
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = Long.parseLong(idDemandeRente);
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = Long.parseLong(idOrdreVersement);
    }

    public void setIdRenteSelectionnee(String idRenteSelectionnee) {
        this.idRenteSelectionnee = Long.parseLong(idRenteSelectionnee);
    }

    public void setIdRenteVerseeATort(String idRenteVerseeATort) {
        this.idRenteVerseeATort = Long.parseLong(idRenteVerseeATort);
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = Long.parseLong(idTiers);
    }

    @Override
    public void setISession(BISession newSession) {
        session = (BSession) newSession;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setModificationPossible(boolean modificationPossible) {
        this.modificationPossible = modificationPossible;
    }

    public void setModificationSpy(BSpy modificationSpy) {
        this.modificationSpy = modificationSpy;
    }

    public void setMontant(String montant) {
        this.montant = new BigDecimal(montant.replace("'", ""));
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(NumeroSecuriteSociale nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRentesNouveauDroit(Collection<RenteAccordee> rentesNouveauDroit) {
        this.rentesNouveauDroit = new ArrayList<RenteAccordee>(rentesNouveauDroit);
        Collections.sort(this.rentesNouveauDroit, new Comparator<RenteAccordee>() {

            @Override
            public int compare(RenteAccordee rente1, RenteAccordee rente2) {
                if (JadeStringUtil.isBlankOrZero(rente1.getMoisFin())) {
                    return -1;
                } else if (JadeStringUtil.isBlankOrZero(rente2.getMoisFin())) {
                    return 1;
                } else {
                    return rente1.getCodePrestation().getCodePrestation()
                            - rente2.getCodePrestation().getCodePrestation();
                }
            }
        });
    }

    public void setSaisieManuelle(boolean saisieManuelle) {
        this.saisieManuelle = saisieManuelle;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * A mettre � "vrai" lorsqu'il y a eu modification d'une rente vers�e � tort alors que des int�r�ts moratoires ou
     * des cr�anciers sont pr�sents dans la demande. Permet de re-diriger l'utilisateur sur l'�cran des demandes afin
     * d'�viter que les menus li�es � la d�cision (qui vient d'�tre supprim�e) soient toujours affich�s apr�s la
     * suppression
     * 
     * @param suppressionDesDecisionsFaites
     */
    public void setSuppressionDesDecisionsFaite(boolean suppressionDesDecisionsFaite) {
        this.suppressionDesDecisionsFaite = suppressionDesDecisionsFaite;
    }

    public void setTypeRenteVerseeATort(TypeRenteVerseeATort typeRenteVerseeATort) {
        this.typeRenteVerseeATort = typeRenteVerseeATort;
    }
}
