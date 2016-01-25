package globaz.lynx.service.tiers;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.util.TIAdressePmtResolver;
import globaz.pyxis.util.TIAdresseResolver;

public class LXTiersService {

    public static final String DOMAINE_FOURNISSEUR = "519015";
    public static final String ROLE_FOURNISSEUR = "517043";

    public static final String ROLE_SOCIETE_DEBITRICE = "517044";

    /**
     * Return l'adressee courrier heritée d'un tier pour le domaine spécifié.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @param domaine
     * @return
     * @throws Exception
     */
    public static String getAdresseAsString(BSession session, BTransaction transaction, String idTiers, String domaine)
            throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getAdresseAsString(
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaine, JACalendar.today().toString());
    }

    /**
     * Retrouve l'adresse du fournisseur.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getAdresseFournisseurAsString(BSession session, BTransaction transaction, String idTiers)
            throws Exception {
        return LXTiersService.getAdresseAsString(session, transaction, idTiers, LXTiersService.DOMAINE_FOURNISSEUR);
    }

    /**
     * Retrouve l'adresse de courrier actuel du fournisseur et la retourne sous forme de datasource afin d'être
     * exploitée. <br/>
     * Nécéssaire pour les écrans de saisies de factures.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static TIAdresseDataSource getAdresseFournisseurCourrierAsDataSource(BSession session,
            BTransaction transaction, String idTiers) throws Exception {
        return TIAdresseResolver.dataSourceAdr(session, idTiers, LXTiersService.DOMAINE_FOURNISSEUR, null, null,
                JACalendar.today().toString(), true, session.getIdLangueISO());
    }

    /**
     * Return les informations de paiements pour un tiers à une date donnée.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @param forDate
     * @return
     * @throws Exception
     */
    public static TIAdressePaiementData getAdresseFournisseurPaiementAsData(BSession session, BTransaction transaction,
            String idTiers, String forDate) throws Exception {
        return TIAdressePmtResolver.dataAdrPmt(session, idTiers, LXTiersService.DOMAINE_FOURNISSEUR, null, forDate,
                true, session.getIdLangueISO());
    }

    /**
     * Retrouve l'adresse de paiement actuel du fournisseur et la retourne sous forme de datasource afin d'être
     * exploitée. <br/>
     * Nécéssaire pour les écrans de saisies de factures.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static TIAdressePaiementDataSource getAdresseFournisseurPaiementAsDataSource(BSession session,
            BTransaction transaction, String idTiers) throws Exception {
        return TIAdressePmtResolver.dataSourceAdrPmt(session, idTiers, LXTiersService.DOMAINE_FOURNISSEUR, null,
                JACalendar.today().toString(), true, session.getIdLangueISO());
    }

    /**
     * @param session
     * @param idAdressePmt
     *            : ID_ADRESSE_PAIEMENT_UNIQUE = "HIIADU"
     * @return null si pas d'adresse trouvée
     * @throws Exception
     */
    public static TIAdressePaiementDataSource getAdresseFournisseurPaiementAsDataSource(BSession session,
            String idAdressePmt) throws Exception {

        LXAdressePaiementDataManager adrPmtMgr = new LXAdressePaiementDataManager();
        adrPmtMgr.setSession(session);
        adrPmtMgr.setForIdAdressePaiement(idAdressePmt);
        adrPmtMgr.find();

        TIAdressePaiementData adrPmt = (TIAdressePaiementData) adrPmtMgr.getFirstEntity();

        if ((adrPmt != null) && !adrPmt.isNew()) {
            TIAdressePaiementDataSource dataSource = new TIAdressePaiementDataSource();
            dataSource.load(adrPmt, "");
            return dataSource;
        }

        return null;
    }

    /**
     * Retrouve l'adresse de paiement actuel du fournisseur.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getAdresseFournisseurPaiementAsString(BSession session, BTransaction transaction,
            String idTiers) throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getAdressePaiementAsString(
                LXTiersService.DOMAINE_FOURNISSEUR, JACalendar.today().toString());
    }

    /**
     * Retrouve l'adresse de paiement actuel d'un organe d'exécution.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getAdresseOrganeExecutionPaiementAsString(BSession session, BTransaction transaction,
            String domaine, String idTiers) throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getAdressePaiementAsString(domaine,
                JACalendar.today().toString(), Boolean.FALSE);
    }

    /**
     * Return l'adresse d'une société.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getAdresseSocieteAsString(BSession session, BTransaction transaction, String idTiers)
            throws Exception {
        return LXTiersService.getAdresseAsString(session, transaction, idTiers, IConstantes.CS_APPLICATION_DEFAUT);
    }

    /**
     * Retrouve le nom/prénom du fournisseur.
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getFournisseurNomPrenom(BSession session, BTransaction transaction, String idTiers)
            throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getNomPrenom();
    }

    /**
     * Retrouve l'id adresse de paiement actuel d'un organe d'exécution. <br/>
     * Utilse pour retrouver l'id de l'adresse lors de l'exécution
     * 
     * @param session
     * @param transaction
     * @param domaine
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getIdAdressePaiementOrganeExecution(BSession session, BTransaction transaction,
            String domaine, String idTiers) throws Exception {
        return TITiers.getAdressePaiementData(domaine, JACalendar.today().toString(), idTiers, null, session)
                .getIdAvoirPaiementUnique();
    }

    /**
     * Retrouve le nom d'un tiers
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getNom(BSession session, BTransaction transaction, String idTiers) throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getDesignation1();
    }

    /**
     * Retrouve le nom complet (Nom et prenom) d'un tiers
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getNomComplet(BSession session, BTransaction transaction, String idTiers) throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getNom();
    }

    /**
     * Retrouve le prenom d'un tiers
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static String getPrenom(BSession session, BTransaction transaction, String idTiers) throws Exception {
        return LXTiersService.getTiers(session, transaction, idTiers).getDesignation2();
    }

    /**
     * Permet la récupération d'un objet TITiers suivant un id tiers
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @return
     * @throws Exception
     */
    private static TITiers getTiers(BSession session, BTransaction transaction, String idTiers) throws Exception {
        TITiers tiers = new TITiers();
        tiers.setSession(session);

        tiers.setIdTiers(idTiers);

        tiers.retrieve(transaction);

        if ((transaction != null) && transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }

        if (tiers.isNew()) {
            throw new Exception(session.getLabel("FOURNISSEUR_TIERS_NOT_FOUND"));
        }

        return tiers;
    }
}
