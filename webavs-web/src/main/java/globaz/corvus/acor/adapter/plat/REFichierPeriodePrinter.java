package globaz.corvus.acor.adapter.plat;

import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.enums.TypeDeDetenteur;
import globaz.hera.wrapper.SFPeriodeWrapper;
import globaz.ij.application.IJApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REFichierPeriodePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Iterator assures;
    private int idPeriode;

    private Object membre;
    private ISFPeriode[] periodes;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REFichierPeriodePrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public REFichierPeriodePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private REACORDemandeAdapter adapter() {
        return (REACORDemandeAdapter) parent;
    }

    private String getTypePeriode(ISFPeriode periode) {

        if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE)) {
            return "do";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_TRAVAILLE)) {
            return "tr";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE)) {
            return "na";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_AFFILIATION)) {
            return "af";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_COTISATION)) {
            return "ex";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE)) {
            return "ae";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT)) {
            return "rc";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE)) {
            return "et";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_IJ)) {
            return "ij";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE)) {
            return "be";
        } else if (periode.getType().equals(ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION)) {
            return "in";
        } else {
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {

        if (assures == null) {
            assures = adapter().membres().iterator();
        }

        if ((periodes == null) || (idPeriode >= periodes.length)) {
            // s'il n'y a plus de membre, on arrete le processus
            if (!assures.hasNext()) {
                return false;
            }

            // sinon on passe aux periodes du membre suivant.
            idPeriode = 0;
            periodes = null;

            while (assures.hasNext() && (periodes == null)) {
                membre = assures.next();

                try {
                    ISFPeriode[] periodesToFilre = adapter().situationFamiliale().getPeriodes(
                            ((ISFMembreFamilleRequerant) membre).getIdMembreFamille());

                    // On filtre les période qui ne sont pas connues d'ACOR
                    List<ISFPeriode> listPeriode = new ArrayList<ISFPeriode>();
                    for (int i = 0; i < periodesToFilre.length; i++) {
                        if (!getTypePeriode(periodesToFilre[i]).isEmpty()) {
                            listPeriode.add(periodesToFilre[i]);
                        }
                    }
                    periodes = listPeriode.toArray(new ISFPeriode[listPeriode.size()]);

                    // si demande survivant
                    if (adapter().getTypeDemande().equals(PRACORConst.CA_TYPE_DEMANDE_SURVIVANT)) {
                        // si membre = requérant
                        PRDemande demandePrest = new PRDemande();
                        demandePrest.setSession(getSession());
                        demandePrest.setIdDemande(adapter().getDemande().getIdDemandePrestation());
                        demandePrest.retrieve();

                        if (demandePrest.getIdTiers().equals(((ISFMembreFamilleRequerant) membre).getIdTiers())) {

                            // Workaround ACOR
                            // Si personne décédée sans période de domicile en
                            // suisse,
                            // Il faut la crééer pour l'envoyer à ACOR autrement
                            // ACOR ne calcul pas.
                            // si requérant avec date décès
                            if (!JadeStringUtil.isBlankOrZero(((ISFMembreFamilleRequerant) membre).getDateDeces())) {
                                // si suisse

                                if (((ISFMembreFamilleRequerant) membre).getCsNationalite().equals("100")) {
                                    // voir si une période de domicile en suisse
                                    // dans la liste
                                    boolean isPeriodeDomicileSuisse = false;
                                    for (int i = 0; i < periodes.length; i++) {
                                        ISFPeriode periode = periodes[i];
                                        if (periode.getNoAvs().equals(((ISFMembreFamilleRequerant) membre).getNss())
                                                && ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE.equals(periode
                                                        .getType())) {
                                            isPeriodeDomicileSuisse = true;
                                        }
                                    }

                                    if (!isPeriodeDomicileSuisse) {
                                        // Créer une période de domicile en
                                        // suisse pour cet assuré
                                        SFPeriodeWrapper periode = new SFPeriodeWrapper();
                                        periode.setDateFin(((ISFMembreFamilleRequerant) membre).getDateDeces());
                                        periode.setDateDebut(((ISFMembreFamilleRequerant) membre).getDateNaissance());
                                        periode.setNoAvs(((ISFMembreFamilleRequerant) membre).getNss());
                                        periode.setPays(((ISFMembreFamilleRequerant) membre).getCsNationalite());
                                        periode.setType(ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE);

                                        List lperiodes = new ArrayList();
                                        for (int i = 0; i < periodes.length; i++) {
                                            lperiodes.add(periodes[i]);
                                        }
                                        lperiodes.add(periode);
                                        periodes = (ISFPeriode[]) lperiodes.toArray(new ISFPeriode[lperiodes.size()]);
                                    }

                                }

                            }

                        }

                    }

                } catch (Exception e) {
                    throw new PRACORException(getSession().getLabel("ERREUR_PERIODES_ENFANT"), e);
                }

                if ((periodes != null) && (periodes.length == 0)) {
                    periodes = null;
                }

            }
        }
        return (periodes != null) && (idPeriode < periodes.length);
    }

    /**
     * Retourne la valeur de la propriétés ij.format.periode.aaaamm
     * 
     * @return la valeur de la propriétés ij.format.periode.aaaamm
     * @throws PRACORException
     */
    private boolean isFormatPeriodeAAAAMM() throws PRACORException {
        String propFormatPeriodeIJ = null;
        try {
            BIApplication ijApplication = GlobazSystem.getApplication(IJApplication.DEFAULT_APPLICATION_IJ);
            propFormatPeriodeIJ = ijApplication.getProperty(IJApplication.PROPERTY_PERIODE_IJ_FORMAT_AAAMM);
        } catch (Exception e) {
            throw new PRACORException(e.toString(), e);
        }

        boolean propValue = false;
        if (!JadeStringUtil.isEmpty(propFormatPeriodeIJ)) {
            propValue = Boolean.valueOf(propFormatPeriodeIJ);
        }
        return propValue;
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * @throws Exception
     */
    public void printLigne(PrintWriter writer) throws Exception {

        ISFPeriode periode = periodes[idPeriode++];

        // 1. le no AVS de l'assure
        this.writeAVS(writer, ((ISFMembreFamilleRequerant) membre).getNss());

        // 2. type de periode
        this.writeAVS(writer, getTypePeriode(periode));

        if (ISFSituationFamiliale.CS_TYPE_PERIODE_IJ.equals(periode.getType())) {
            // si version AAAAMM
            if (isFormatPeriodeAAAAMM()) {

                // 3. date début
                this.writeDateAAAAMM(writer, periode.getDateDebut());

                // 4. date fin
                if (JadeStringUtil.isIntegerEmpty(periode.getDateFin())) {
                    this.writeDateAAAAMM(writer, "999999");
                } else {
                    this.writeDateAAAAMM(writer, periode.getDateFin());
                }
            }
            // else version AAAAMMJJ
            else {

                // 3. date début
                this.writeDate(writer, periode.getDateDebut());

                // 4. date fin
                if (JadeStringUtil.isIntegerEmpty(periode.getDateFin())) {
                    this.writeDate(writer, "99999999");
                } else {
                    this.writeDate(writer, periode.getDateFin());
                }
            }
        } else {
            // 3. date début
            this.writeDateAAAAMM(writer, periode.getDateDebut());

            // 4. date fin
            if (JadeStringUtil.isIntegerEmpty(periode.getDateFin())) {
                this.writeDateAAAAMM(writer, "999999");
            } else {
                this.writeDateAAAAMM(writer, periode.getDateFin());
            }
        }

        // 5. NSS complémentaire
        // En 1er il faut aller lire le type de détenteur (s'il est renseigné
        String csTypeDeDetenteur = periode.getCsTypeDeDetenteur();
        String toWrite = "0";
        boolean isNSS = false;
        if (JadeStringUtil.isBlankOrZero(csTypeDeDetenteur)) {
            TypeDeDetenteur typeDeDetenteur = TypeDeDetenteur.fromCodeSytem(csTypeDeDetenteur);

            if (typeDeDetenteur != null) {
                switch (typeDeDetenteur) {
                    case TIERS:
                    case TUTEUR_LEGAL:
                        toWrite = typeDeDetenteur.getAcorKey();
                        break;
                    // Si le type de détenteur est famille, il faut inscrire le NSS du tiers enregistré dans
                    // SFPERIOD.WHIDBT
                    case FAMILLE:
                        isNSS = true;
                        String idTiersDetenteur = periode.getIdDetenteurBTE();
                        if (JadeStringUtil.isBlankOrZero(idTiersDetenteur)) {
                            PRTiersWrapper wrapper = PRTiersHelper.getTiersParId(getSession(), idTiersDetenteur);
                            if (wrapper != null) {
                                toWrite = wrapper.getNSS();
                            }
                        }
                        break;

                    default:
                        break;
                }
            }

        }
        if (isNSS) {
            this.writeAVS(writer, toWrite);
        } else {
            this.writeChaine(writer, toWrite);
        }

        this.writeChaineSansFinDeChamp(writer, getSession().getCode(periode.getPays()));
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {

        ISFPeriode periode = periodes[idPeriode++];

        // 1. le no AVS de l'assure
        this.writeAVS(writer, ((ISFMembreFamilleRequerant) membre).getNss());

        // 2. type de periode
        this.writeAVS(writer, getTypePeriode(periode));

        if (ISFSituationFamiliale.CS_TYPE_PERIODE_IJ.equals(periode.getType())) {
            // si version AAAAMM
            if (isFormatPeriodeAAAAMM()) {

                // 3. date début
                this.writeDateAAAAMM(writer, periode.getDateDebut());

                // 4. date fin
                if (JadeStringUtil.isIntegerEmpty(periode.getDateFin())) {
                    this.writeDateAAAAMM(writer, "999999");
                } else {
                    this.writeDateAAAAMM(writer, periode.getDateFin());
                }
            }
            // else version AAAAMMJJ
            else {

                // 3. date début
                this.writeDate(writer, periode.getDateDebut());

                // 4. date fin
                if (JadeStringUtil.isIntegerEmpty(periode.getDateFin())) {
                    this.writeDate(writer, "99999999");
                } else {
                    this.writeDate(writer, periode.getDateFin());
                }
            }
        } else {
            // 3. date début
            this.writeDateAAAAMM(writer, periode.getDateDebut());

            // 4. date fin
            if (JadeStringUtil.isIntegerEmpty(periode.getDateFin())) {
                this.writeDateAAAAMM(writer, "999999");
            } else {
                this.writeDateAAAAMM(writer, periode.getDateFin());
            }
        }

        // 5. NSS complémentaire
        // En 1er il faut aller lire le type de détenteur (s'il est renseigné
        String csTypeDeDetenteur = periode.getCsTypeDeDetenteur();
        String toWrite = "0";
        boolean isNSS = false;
        if (!JadeStringUtil.isBlankOrZero(csTypeDeDetenteur)) {
            TypeDeDetenteur typeDeDetenteur = TypeDeDetenteur.fromCodeSytem(csTypeDeDetenteur);

            if (typeDeDetenteur != null) {
                switch (typeDeDetenteur) {
                    case TIERS:
                    case TUTEUR_LEGAL:
                        toWrite = typeDeDetenteur.getAcorKey();
                        break;
                    // Si le type de détenteur est famille, il faut inscrire le NSS du tiers enregistré dans
                    // SFPERIOD.WHIDBT
                    case FAMILLE:
                        isNSS = true;
                        // idTiersDetenteur n'est pas un idTiers mais un id membre famille
                        String idMembreFamille = periode.getIdDetenteurBTE();
                        if (!JadeStringUtil.isBlankOrZero(idMembreFamille)) {
                            try {
                                SFMembreFamille membre = new SFMembreFamille();
                                membre.setSession(getSession());
                                membre.setIdMembreFamille(idMembreFamille);
                                membre.retrieve();
                                if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) {
                                    PRTiersWrapper wrapper = PRTiersHelper.getTiersParId(getSession(),
                                            membre.getIdTiers());
                                    if (wrapper != null) {
                                        toWrite = wrapper.getNSS();
                                    }
                                }
                            } catch (Exception exception) {
                                throw new PRACORException(exception.toString(), exception);
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
            // String p = periode.getNoAvsDetenteurBTE();

        }
        if (isNSS) {
            this.writeAVS(writer, toWrite);
        } else {
            this.writeChaine(writer, toWrite);
        }

        this.writeChaineSansFinDeChamp(writer, getSession().getCode(periode.getPays()));
    }
}
