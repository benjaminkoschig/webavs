/*
 * Créé le 28 avr. 05
 */
package globaz.prestation.clone.factory;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.xml.data.IPRXmlElement;
import globaz.prestation.clone.xml.data.PRXmlCloneElement;
import globaz.prestation.clone.xml.data.PRXmlLinkedClassElement;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Description DOCUMENT ME!
 * 
 * @author scr
 * 
 *         <p>
 *         Description
 *         </p>
 */
public class PRCloneFactory {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static PRCloneFactory instance = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut instance.
     * 
     * @return la valeur courante de l'attribut instance
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static synchronized PRCloneFactory getInstance() throws Exception {
        if (instance == null) {
            instance = new PRCloneFactory();
        }

        return instance;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe PRCloneFactory.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private PRCloneFactory() throws Exception {
    }

    /**
     * idem que l'autre mais avec une transaction deja creee en externe.
     * 
     * @param fileName
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param cloneable
     *            DOCUMENT ME!
     * @param id
     *            DOCUMENT ME!
     * @param actionType
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IPRCloneable clone(String fileName, BSession session, BITransaction transaction, IPRCloneable cloneable,
            String id, int actionType) throws Exception {
        IPRCloneable parentCloned = null;

        try {
            PRCloneFileParser fileParser = new PRCloneFileParser();
            PRXmlCloneElement xmlElement = fileParser.loadFile(fileName, id);

            if (xmlElement == null) {
                throw new Exception("Référence " + id + " non référence comme object cloneable dans le fichier : "
                        + fileName);
            }

            if (!Class.forName(xmlElement.getClassName()).isInstance(cloneable)) {
                throw new Exception("Object " + cloneable.getClass().getName() + "ne concorde pas avec la référence : "
                        + id + "dans le fichier : " + fileName);
            }

            // Copy of the main element
            parentCloned = cloneable.duplicate(actionType);
            parentCloned.setSession(session);
            ((BEntity) parentCloned).wantCallValidate(false);

            parentCloned = doTreatment(session, transaction, parentCloned, xmlElement, cloneable, actionType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parentCloned;
    }

    /**
     * Duplifie l'object cloneable passé en paramètre. La structure à duplifier est dénifie dans le fichier passé en
     * paramètre
     * 
     * @param fileName
     *            Fichier contenant la structure de clonage.
     * @param session
     * @param cloneable
     *            L'object à cloner
     * @param id
     *            Référence récupérer dans le fichier 'fileName' identifiant la structure à duplifier.
     * @param actionType
     *            Indique s'il le clone doit être créer en tant que nouvel entité indépendante, ou en tant que entité
     *            filles.
     * 
     * @return
     * 
     * @throws Exception
     */
    public IPRCloneable clone(String fileName, BSession session, IPRCloneable cloneable, String id, int actionType)
            throws Exception {
        BITransaction transaction = null;

        boolean isError = false;
        IPRCloneable parentCloned = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();
            parentCloned = clone(fileName, session, transaction, cloneable, id, actionType);
        } catch (Exception e) {
            isError = true;
            e.printStackTrace();
        } finally {
            if (transaction != null) {
                try {
                    if (isError || transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return parentCloned;
    }

    /**
     * Clone la classe passée en paramètre, récupère les feuilles si existante, et appel récursif sur cette méthode pour
     * cloner toutes les sous-feuilles de chaque éléments. Cette méthode insère dans la DB les objects cloné, et génère
     * les id des elements de la nouvelle structure en prenant en compte le type de relation entre elements : relation 1
     * to 1 ou relation 1 to n.
     * 
     * @param session
     * @param transaction
     * @param clonedParent
     * @param xmlParentElement
     * @param cloneableParent
     * @param actionType
     *            DOCUMENT ME!
     * 
     * @return
     * 
     * @throws Exception
     */
    private IPRCloneable doTreatment(BSession session, BITransaction transaction, IPRCloneable clonedParent,
            IPRXmlElement xmlParentElement, IPRCloneable cloneableParent, int actionType) throws Exception {
        if (xmlParentElement.getXmlElements() != null) {
            for (Iterator iter = xmlParentElement.getXmlElements().iterator(); iter.hasNext();) {
                PRXmlLinkedClassElement xmlChildElement = (PRXmlLinkedClassElement) iter.next();

                // Relation 1 - N entre la class parent & enfant
                if (PRXmlLinkedClassElement.RELATION_TYPE_1_TO_N.equals(xmlChildElement.getRelationType())) {
                    // Instanciation du manager pour récupérer les enfants
                    Class c = Class.forName(xmlChildElement.getManagerClassName());
                    BManager mgr = (BManager) c.newInstance();

                    mgr.setSession(session);

                    String setForIdParentMethodName = xmlChildElement.getForIdParent();

                    setForIdParentMethodName = "set" + setForIdParentMethodName.substring(0, 1).toUpperCase()
                            + setForIdParentMethodName.substring(1, setForIdParentMethodName.length());

                    String setIdParentMethodName = xmlChildElement.getIdParent();

                    setIdParentMethodName = "set" + setIdParentMethodName.substring(0, 1).toUpperCase()
                            + setIdParentMethodName.substring(1, setIdParentMethodName.length());

                    Class[] stringType = new Class[1];

                    stringType[0] = String.class;

                    String idParentCloneable = cloneableParent.getUniquePrimaryKey();

                    if (JadeStringUtil.isIntegerEmpty(idParentCloneable)) {
                        continue;
                    }

                    // Appel de setForIdParent en passant l'id du parent
                    // récupéré précédemment.
                    Object[] args = new Object[1];

                    args[0] = idParentCloneable;

                    Method m = mgr.getClass().getMethod(setForIdParentMethodName, stringType);

                    m.invoke(mgr, args);
                    mgr.find(transaction);

                    if (((BEntity) clonedParent).isNew()) {
                        ((BEntity) clonedParent).add(transaction);
                    }

                    // Récupération de l'id du parent cloné
                    String idParentCloned = clonedParent.getUniquePrimaryKey();

                    for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
                        IPRCloneable child = (IPRCloneable) iterator.next();
                        IPRCloneable childCloned = child.duplicate(actionType);

                        childCloned.setSession(session);

                        // Appel de la méthode setIdParent de l'enfant, pour y
                        // setter l'id du parent précédemment
                        // récupéré.
                        Method setIdParentMethod = childCloned.getClass().getMethod(setIdParentMethodName, stringType);

                        args[0] = idParentCloned;
                        setIdParentMethod.invoke(childCloned, args);
                        ((BEntity) childCloned).save(transaction);

                        childCloned = doTreatment(session, transaction, childCloned, xmlChildElement, child, actionType);
                    }
                }
                // else Relation 1 to 1
                else if (PRXmlLinkedClassElement.RELATION_TYPE_1_TO_1.equals(xmlChildElement.getRelationType())) {
                    // retrieve de l'entity
                    Class c = Class.forName(xmlChildElement.getClassName());
                    BEntity entity = (BEntity) c.newInstance();

                    entity.setSession(session);

                    Class[] stringType = new Class[1];

                    stringType[0] = String.class;

                    if (((BEntity) clonedParent).isNew()) {
                        ((BEntity) clonedParent).add(transaction);
                    }

                    // Récupération de l'id du fils de la classe parent à cloner
                    String getIdParentMethodName = xmlChildElement.getIdParent();

                    getIdParentMethodName = "get" + getIdParentMethodName.substring(0, 1).toUpperCase()
                            + getIdParentMethodName.substring(1, getIdParentMethodName.length());

                    Method getIdParentMethod = cloneableParent.getClass().getMethod(getIdParentMethodName, null);
                    // Appel de getIdParent le parent et récupération de la
                    // valeur du fils

                    String idFilsACloner = (String) getIdParentMethod.invoke(cloneableParent, null);

                    ((IPRCloneable) entity).setUniquePrimaryKey(idFilsACloner);
                    entity.retrieve();

                    if (entity.isNew()) {
                        continue;
                    }

                    IPRCloneable childCloned = ((IPRCloneable) entity).duplicate(actionType);

                    childCloned = doTreatment(session, transaction, childCloned, xmlChildElement,
                            (IPRCloneable) entity, actionType);
                    childCloned.setSession(session);

                    // On sauve le child.
                    ((BEntity) childCloned).save(transaction);

                    // On récupère la primary key du child
                    String pkChild = childCloned.getUniquePrimaryKey();

                    // Set id du fils sur le parent
                    String setIdFilsSurParentMethodName = xmlChildElement.getIdParent();

                    setIdFilsSurParentMethodName = "set" + setIdFilsSurParentMethodName.substring(0, 1).toUpperCase()
                            + setIdFilsSurParentMethodName.substring(1, setIdFilsSurParentMethodName.length());

                    Method setIdFilsSurParentMethod = cloneableParent.getClass().getMethod(
                            setIdFilsSurParentMethodName, stringType);

                    // Appel de setPrimaryKey en passant l'id du parent récupéré
                    // précédemment.
                    Object[] args = new Object[1];

                    args[0] = pkChild;
                    setIdFilsSurParentMethod.invoke(clonedParent, args);
                    ((BEntity) clonedParent).save(transaction);
                }
            }
        }

        return clonedParent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param linkedClassElm
     *            DOCUMENT ME!
     */
    public void parseLinkedClass(PRXmlLinkedClassElement linkedClassElm) {
        System.out.println("");
        System.out.println("\t\tclass liée = " + linkedClassElm.getClassName());
        System.out.println("\t\tidParent = " + linkedClassElm.getIdParent() + " relation type = "
                + linkedClassElm.getRelationType() + " mgr = " + linkedClassElm.getManagerClassName());

        if (linkedClassElm.getXmlElements() != null) {
            for (Iterator iter2 = linkedClassElm.getXmlElements().iterator(); iter2.hasNext();) {
                PRXmlLinkedClassElement linkedClassElm2 = (PRXmlLinkedClassElement) iter2.next();

                parseLinkedClass(linkedClassElm2);
            }
        }
    }
}
