package globaz.ij.acor.adapter.plat;

import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * inscrit le fichier des familles.
 * </p>
 * 
 * <p>
 * Les relations sont regroupees de la maniere suivante:
 * </p>
 * 
 * <dl>
 * <dt>personnes mariees</dt>
 * <dd>inscrit une ligne avec date de mariage et date de fin de lien vide, etat marie</dd>
 * 
 * <dt>personnes mariees puis separees ou divorcees</dt>
 * <dd>inscrit une ligne avec date de mariage et date de separation ou de divorce comme date de fin, etat separe ou
 * divorce.</dd>
 * 
 * <dt>personnes mariees puis separees puis divorcees</dt>
 * <dd>inscrit une ligne avec date de mariage et date de divorce comme date de fin, etat divorce</dd>
 * 
 * <dt>personnes remariees</dt>
 * <dd>pour chaque cycle de mariage-divorce, une nouvelle ligne est inscrite avec les regles precedentes.</dd>
 * </dl>
 * 
 * <p>
 * Exemplde de cas Requérant : SCR -> enfant ZOE Femme 1 : ISA -> enfant ZOE Femme 2 : JOE
 * 
 * 
 * [ (1) Marie avec ISA ][ (2) Divorcé de ISA [ (3) Marié JOE ][ (4) Divorcé de JOE [ (5) re-Marié avec ISA
 * 
 * 
 * Il faut envoyer les relations 2, 4 et 5 à ACOR.
 * 
 * 
 * </p>
 * 
 * @author scr
 * 
 * 
 * 
 */
public class IJFichierFamillePrinter extends PRAbstractFichierPlatPrinter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private class Ligne {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private ISFMembreFamilleRequerant conjoint;
        private boolean conjointHomme;
        private String dateFin;
        private String dateMariage;
        private String typeLien;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        private Ligne(ISFMembreFamilleRequerant conjoint, boolean conjointHomme, String typeLien, String dateMariage) {
            this.conjoint = conjoint;
            this.conjointHomme = conjointHomme;
            this.typeLien = typeLien;
            this.dateMariage = dateMariage;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut conjoint.
         * 
         * @return la valeur courante de l'attribut conjoint
         */
        public ISFMembreFamilleRequerant getConjoint() {
            return conjoint;
        }

        /**
         * getter pour l'attribut date fin.
         * 
         * @return la valeur courante de l'attribut date fin
         */
        public String getDateFin() {
            return dateFin;
        }

        /**
         * getter pour l'attribut date mariage.
         * 
         * @return la valeur courante de l'attribut date mariage
         */
        public String getDateMariage() {
            return dateMariage;
        }

        /**
         * getter pour l'attribut type lien.
         * 
         * @return la valeur courante de l'attribut type lien
         */
        public String getTypeLien() {
            return typeLien;
        }

        /**
         * getter pour l'attribut conjoint homme.
         * 
         * @return la valeur courante de l'attribut conjoint homme
         */
        public boolean isConjointHomme() {
            return conjointHomme;
        }

        /**
         * setter pour l'attribut conjoint.
         * 
         * @param conjoint
         *            une nouvelle valeur pour cet attribut
         */
        public void setConjoint(ISFMembreFamilleRequerant conjoint) {
            this.conjoint = conjoint;
        }

        /**
         * setter pour l'attribut conjoint homme.
         * 
         * @param conjointHomme
         *            une nouvelle valeur pour cet attribut
         */
        public void setConjointHomme(boolean conjointHomme) {
            this.conjointHomme = conjointHomme;
        }

        /**
         * setter pour l'attribut date fin.
         * 
         * @param dateFin
         *            une nouvelle valeur pour cet attribut
         */
        public void setDateFin(String dateFin) {
            this.dateFin = dateFin;
        }

        /**
         * setter pour l'attribut date mariage.
         * 
         * @param dateMariage
         *            une nouvelle valeur pour cet attribut
         */
        public void setDateMariage(String dateMariage) {
            this.dateMariage = dateMariage;
        }

        /**
         * setter pour l'attribut type lien.
         * 
         * @param typeRelation
         *            une nouvelle valeur pour cet attribut
         */
        public void setTypeLien(String typeRelation) {
            typeLien = typeRelation;
        }
    }

    private Iterator conjoints;
    private Iterator lignes;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private List lignesList = new LinkedList();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierFamillePrinter.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierFamillePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;
    }

    /**
     * cree la liste des lignes a inscrire.
     * 
     * 
     * Exemplde de cas Requérant : SCR -> enfant ZOE Femme 1 : ISA -> enfant ZOE Femme 2 : JOE
     * 
     * 
     * [ (1) Marie avec ISA ][ (2) Divorcé de ISA [ (3) Marié JOE ][ (4) Divorcé de JOE [ (5) re-Marié avec ISA
     * 
     * 
     * Il faut envoyer les relations 2, 4 et 5 à ACOR.
     * 
     * 2 appels à cette méthode, une fois pour ISA et une fois pour JOE. Dans l'exemple de ISA, on va recevoir les
     * relactions 1 + 2 + 5.
     * 
     * Algorythme : Parcours des relations de la plus ancienne à la plus ancienne, cad 1, 2, 5
     * 
     * 
     * 
     * Itération sur tous les cas suivants.... Si cas en cours est le dernier ou cas suivant est de type MARIAGE
     * 
     * on stocke le cas en cours }
     * 
     * 
     * @throws PRACORException
     * 
     */
    private void creerLignes() throws PRACORException, Exception {
        // charger l'historique de toutes les relations pour le conjoint
        ISFMembreFamilleRequerant conjoint = (ISFMembreFamilleRequerant) conjoints.next();

        // Les relations sont ordonnées de la plus ancienne à la plus récente.

        ISFRelationFamiliale[] relationsAll = adapter().relations(adapter().idTiersAssure(),
                adapter().idMembreFamilleRequerant(), conjoint.getIdMembreFamille());

        // effacer les anciennes lignes
        lignesList.clear();

        // regroup
        String dateMariage = null;

        for (int idRelation = 0; idRelation < relationsAll.length; ++idRelation) {

            ISFRelationFamiliale relation = relationsAll[idRelation];

            if (globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                    || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation
                            .getTypeLien())
                    || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                    || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())) {

                dateMariage = relation.getDateDebutRelation();
            }

            boolean isLastElement = idRelation == (relationsAll.length - 1) ? true : false;

            String csTypeLienNextElem = null;
            if (!isLastElement) {
                csTypeLienNextElem = relationsAll[idRelation + 1].getTypeLien();
            }

            if (isLastElement || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(csTypeLienNextElem)
                    || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(csTypeLienNextElem)) {

                // BZ-5083
                Ligne l = null;
                // Le type de lien séparé de fait doit être considéré comme marié ou lpart_enregistré
                if (globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relation.getTypeLien())) {
                    if (globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                            .getTypeRelation())) {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_MARIE, dateMariage);
                    } else {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                relation.getTypeLien(), dateMariage);
                    }
                } else if (globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT.equals(relation
                        .getTypeLien())) {
                    if (globaz.hera.api.ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equals(relation
                            .getTypeRelation())) {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE, dateMariage);
                    } else {
                        l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                                relation.getTypeLien(), dateMariage);
                    }
                } else {
                    l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                            relation.getTypeLien(), dateMariage);
                }

                // //On stocke le cas en cours
                // Ligne l = new Ligne(conjoint, conjoint.getIdMembreFamille() == relation.getIdMembreFamilleHomme(),
                // relation.getTypeLien(), dateMariage);

                if (!globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relation.getTypeLien())
                        && !globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relation
                                .getTypeLien())) {

                    l.setDateFin(relation.getDateDebut());
                } else {
                    l.setDateFin(relation.getDateFin());
                }
                lignesList.add(l);
            }
        }

        lignes = lignesList.iterator();
    }

    /**
     * regarde s'il y a encore des lignes a inscrire.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public boolean hasLignes() throws PRACORException {
        // on recherche la prochaine ligne
        if ((lignes == null) || !lignes.hasNext()) {
            try {
                // charger la liste des relations avec les conjoints si necessaire
                if (conjoints == null) {
                    conjoints = adapter().conjoints();

                    if (!conjoints.hasNext()) {
                        return false;
                    }
                }

                // creer les lignes la premiere fois
                if (lignes == null) {
                    creerLignes();
                }

                // creer les lignes pour le conjoint suivant s'il n'y en a plus pour le conjoint courant
                while (!lignes.hasNext() && conjoints.hasNext()) {
                    creerLignes();
                }

                return lignes.hasNext();
            } catch (PRACORException e) {
                throw e;
            } catch (Exception e) {
                throw new PRACORException("impossible de charger les relations familiales", e);
            }
        } else {
            return lignes.hasNext();
        }
    }

    /**
     * inscrit une ligne.
     * 
     * @param writer
     *            DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     * 
     * @deprecated
     */
    @Deprecated
    public void printLigne(PrintWriter writer) throws PRACORException {
        Ligne ligne = (Ligne) lignes.next();

        // 1+2. le no AVS de l'homme suivi de celui de la femme ou celui de l'assure vivant suivi du decede
        if (globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(ligne.typeLien)
                || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(ligne.typeLien)) {
            this.writeAssureVivantDabord(writer, ligne.conjoint);
        } else {
            this.writeAssureHommeDabord(writer, ligne);
        }

        // 3. type de lien
        this.writeEntier(writer, PRACORConst.csTypeLienToACOR(getSession(), ligne.typeLien));

        // 4. date de debut de mariage
        this.writeDate(writer, ligne.dateMariage);

        // 5. date de fin de lien
        this.writeDate(writer, ligne.dateFin);

        // 6. pension alimentaire
        this.writeBoolean(writer, false);

        // 7. demi-rente de couple demandee
        this.writeBoolean(writer, false);

        ligne = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.lang.StringBuffer)
     */
    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        Ligne ligne = (Ligne) lignes.next();

        // 1+2. le no AVS de l'homme suivi de celui de la femme ou celui de l'assure vivant suivi du decede
        if (globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(ligne.typeLien)
                || globaz.hera.api.ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(ligne.typeLien)) {
            this.writeAssureVivantDabord(cmd, ligne.conjoint);
        } else {
            this.writeAssureHommeDabord(cmd, ligne);
        }

        // 3. type de lien
        this.writeEntier(cmd, PRACORConst.csTypeLienToACOR(getSession(), ligne.typeLien));

        // 4. date de debut de mariage
        this.writeDate(cmd, ligne.dateMariage);

        // 5. date de fin de lien
        this.writeDate(cmd, ligne.dateFin);

        // 6. pension alimentaire
        this.writeBoolean(cmd, false);

        // 7. demi-rente de couple demandee
        this.writeBoolean(cmd, false);

        ligne = null;
    }

    /**
     * 
     * @param writer
     * @param ligne
     * @throws PRACORException
     * 
     * @deprecated
     */
    @Deprecated
    private void writeAssureHommeDabord(PrintWriter writer, Ligne ligne) throws PRACORException {
        if (ligne.isConjointHomme()) {
            // Le type de lien est null pour les relations de type enfant commun.
            // Dans ce cas, mettre le conjoint avec un nss vide.
            if (ligne.getTypeLien() == null) {
                this.writeAVS(writer, "0000000000000");
            } else {
                this.writeAVS(writer, ligne.conjoint.getNss());
            }
            this.writeAVS(writer, adapter().numeroAVSAssure());
        } else {
            this.writeAVS(writer, adapter().numeroAVSAssure());

            if (ligne.getTypeLien() == null) {
                this.writeAVS(writer, "0000000000000");
            } else {
                this.writeAVS(writer, ligne.conjoint.getNss());
            }
        }
    }

    private void writeAssureHommeDabord(StringBuffer cmd, Ligne ligne) throws PRACORException {

        if (ligne.isConjointHomme()) {
            // Le type de lien est null pour les relations de type enfant commun.
            // Dans ce cas, mettre le conjoint avec un nss vide.
            if (ligne.getTypeLien() == null) {
                this.writeAVS(cmd, "0000000000000");
            } else {
                this.writeAVS(cmd, ligne.conjoint.getNss());
            }
            this.writeAVS(cmd, adapter().numeroAVSAssure());
        } else {
            this.writeAVS(cmd, adapter().numeroAVSAssure());

            if (ligne.getTypeLien() == null) {
                this.writeAVS(cmd, "0000000000000");
            } else {
                this.writeAVS(cmd, ligne.conjoint.getNss());
            }
        }
    }

    /**
     * 
     * @param writer
     * @param conjoint
     * @throws PRACORException
     * 
     * @deprecated
     */
    @Deprecated
    private void writeAssureVivantDabord(PrintWriter writer, ISFMembreFamilleRequerant conjoint) throws PRACORException {
        // j'imagine mal qu'un requerant mort demande une ij non ? bah ! on est jamais trop sur...
        if (JAUtil.isDateEmpty(adapter().requerant().getDateDeces())) {
            // l'assure vivant
            this.writeAVS(writer, adapter().numeroAVSAssure());

            // l'ex-assure mort
            this.writeAVS(writer, conjoint.getNss());
        } else {
            // l'assure vivant
            this.writeAVS(writer, conjoint.getNss());

            // l'ex assure mort
            this.writeAVS(writer, adapter().numeroAVSAssure());
        }
    }

    private void writeAssureVivantDabord(StringBuffer cmd, ISFMembreFamilleRequerant conjoint) throws PRACORException {
        // j'imagine mal qu'un requerant mort demande une ij non ? bah ! on est jamais trop sur...
        if (JAUtil.isDateEmpty(adapter().requerant().getDateDeces())) {
            // l'assure vivant
            this.writeAVS(cmd, adapter().numeroAVSAssure());

            // l'ex-assure mort
            this.writeAVS(cmd, conjoint.getNss());
        } else {
            // l'assure vivant
            this.writeAVS(cmd, conjoint.getNss());

            // l'ex assure mort
            this.writeAVS(cmd, adapter().numeroAVSAssure());
        }
    }
    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------
}
