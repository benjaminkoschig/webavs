package globaz.aquila.util;

import globaz.jade.log.JadeLogger;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitaires pour les JavaBeans
 * 
 * @author Pascal Lovy, 11-nov-2004
 */
public final class COBeanUtils {

    /**
     * Retourne une repr�sentation textuelle du contenu d'un JavaBean. L'introspection des propri�t�s ne remonte pas
     * plus haut que la classe de base. Les propri�t�s de type enfant de la classe de base sont aussi repr�sent�es
     * textuellement.
     * 
     * @param bean
     *            L'objet � repr�senter
     * @param baseClass
     *            La classe de base
     * @param multiLine
     *            <code>true</code> pour une description sur plusieurs lignes
     * @param displayPackage
     *            <code>true</code> pour afficher le nom du package devant les noms de classe
     * @param indentLevel
     *            Le niveau d'indentation (uniquement pour <code>multiLine=true</code>)
     * @return La repr�sentation textuelle de l'objet
     */
    public static String dumpBean(Object bean, Class baseClass, boolean multiLine, boolean displayPackage,
            int indentLevel) {
        StringBuffer result = new StringBuffer();
        try {
            // Si le bean n'est pas null
            if (bean != null) {
                // Si rien n'a �t� sp�cifi�, la classe de base est celle du bean
                // lui-m�me
                if (baseClass == null) {
                    baseClass = bean.getClass();
                }
                // Si le bean est bien est de type enfant de la classe de base
                if (baseClass.isInstance(bean)) {
                    // Pr�paration des d�limiteurs
                    String prefix = "{";
                    String suffix = "} ";
                    String separator = ",";
                    if (multiLine) {
                        prefix = "\n";
                        suffix = "";
                        separator = "\n";
                    }

                    // Pr�paration de l'indentation
                    String indent = "";
                    for (int i = 0; (i < indentLevel) && multiLine; i++) {
                        indent = indent + "	";
                    }

                    // Ajout du nom de la classe (� la mani�re
                    // Object.toString())
                    String beanClassName = bean.getClass().getName();
                    if (multiLine) {
                        result.append("\n");
                    }
                    result.append(indent);
                    if (displayPackage) {
                        // Avec le nom du package
                        result.append(beanClassName);
                    } else {
                        // Sans le nom du package
                        result.append(beanClassName.substring(beanClassName.lastIndexOf(".") + 1));
                    }
                    result.append("@");
                    result.append(Integer.toHexString(bean.hashCode()));

                    // Traitement des propri�t�s
                    BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), baseClass.getSuperclass());
                    PropertyDescriptor[] descriptorList = beanInfo.getPropertyDescriptors();
                    result.append(prefix);
                    for (int i = 0; i < descriptorList.length; i++) {
                        Method readMethod = descriptorList[i].getReadMethod();
                        // Si la propri�t� peut �tre lue
                        if (readMethod != null) {
                            result.append(indent);
                            // On ajoute son nom
                            result.append(descriptorList[i].getName());
                            result.append("=");
                            Object value = readMethod.invoke(bean, new Object[0]);
                            // Si la propri�t�s est de type enfant de la classe
                            // de base
                            if (baseClass.isInstance(value)) {
                                // On ajoute sa description textuelle
                                result.append(COBeanUtils.dumpBean(value, baseClass, multiLine, displayPackage,
                                        indentLevel + 1));
                            } else {
                                // On ajoute sa valeur convertie en texte
                                result.append(String.valueOf(value));
                            }
                            // Ajoute un s�parateur si il reste des propri�t�s
                            if (i < descriptorList.length - 1) {
                                result.append(separator);
                            }
                        }
                    }
                    result.append(suffix);
                } else {
                    // Le bean n'est pas de type enfant de la classe de base
                    result.append(bean);
                }
            } else {
                // Le bean est null
                result.append("null");
            }
        } catch (Exception e) {
            JadeLogger.error(bean, e);
        }
        return result.toString();
    }

    /**
     * Retourne toutes les propri�t�s d'un bean dans un Map
     * 
     * @param bean
     *            Le bean
     * @param baseClass
     *            La classe de base
     * @return Le Map
     */
    public static Map getAllProperties(Object bean, Class baseClass) {
        HashMap result = new HashMap();
        try {
            // Si le bean n'est pas null
            if (bean != null) {
                // Si rien n'a �t� sp�cifi� ou que le bean n'pas de type enfant
                // de la classe de base,
                // la classe de base est celle du bean lui-m�me
                if ((baseClass == null) || !baseClass.isInstance(bean)) {
                    baseClass = bean.getClass();
                }
                // Traitement des propri�t�s
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), baseClass.getSuperclass());
                PropertyDescriptor[] descriptorList = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < descriptorList.length; i++) {
                    Method readMethod = descriptorList[i].getReadMethod();
                    // Si la propri�t� peut �tre lue
                    if (readMethod != null) {
                        // On l'ajoute au Map
                        Object value = readMethod.invoke(bean, new Object[0]);
                        result.put(descriptorList[i].getName(), value);
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(bean, e);
        }
        return result;
    }

    /**
     * Tente d'affecter aux propri�t�s d'un bean les param�tres contenus dans un Map
     * 
     * @param bean
     *            Le bean
     * @param parameters
     *            Map de param�tres
     */
    public static void setAllProperties(Object bean, Map parameters) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] descriptorList = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < descriptorList.length; i++) {
                Method writeMethod = descriptorList[i].getWriteMethod();
                Object value = parameters.get(descriptorList[i].getName());
                if ((writeMethod != null) && (value != null)) {
                    // Conversion en tableau si objet simple
                    if (writeMethod.getParameterTypes()[0].isArray() && !value.getClass().isArray()) {
                        value = new Object[] { value };
                    }
                    // Conversion en objet simple si tableau
                    if (!writeMethod.getParameterTypes()[0].isArray() && value.getClass().isArray()) {
                        value = ((Object[]) value)[0];
                    }
                    try {
                        writeMethod.invoke(bean, new Object[] { value });
                    } catch (Exception e) {
                        JadeLogger.error(bean, e);
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(bean, e);
        }
    }

    /**
     * Constructeur priv�
     */
    private COBeanUtils() {
    }

}
