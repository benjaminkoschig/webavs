package ch.globaz.naos.ree.tools;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Définit les modes d'éxecution du processus.
 * Le mode d'éxecution est un argument du processus fournis lors du lancement du process.</br>
 * <strong>Les arguments possible sont définis par les valeurs <code>userArg</code></strong>
 * 
 * @author lga
 * 
 */
public enum ExecutionMode {

    _5053("53"),
    _5053_AND_5054("53-54");

    private String userArg;

    private ExecutionMode(String userArg) {
        this.userArg = userArg;
    }

    /**
     * Retourne le nom de l'argument de processus pour ce mode d'éxecution
     * 
     * @return le nom de l'argument de processus pour ce mode d'éxecution
     */
    public String getUserArg() {
        return userArg;
    }

    /**
     * Parse la valeur String fournie par l'utilisateur afin de déterminer le mode d'exécution du processus.</br>
     * 
     * 
     * @param userArg La valeur de type String entrée par l'utilisateur
     * @return Le mode d'exécution du processus
     * @throws IllegalArgumentException Si la valeur <code>userArg</code> est vide ou si le mode d'exécution n'a pas pu
     *             être déterminé
     */
    public static ExecutionMode parseUserArg(String userArg) throws IllegalArgumentException {
        if (JadeStringUtil.isEmpty(userArg)) {
            StringBuilder sb = buildValuesList();
            throw new IllegalArgumentException("Invalid empty user argument. [" + userArg
                    + "] for ExecutionMode. Possible values are [" + sb.toString() + "] ");
        }

        if (_5053.getUserArg().equals(userArg)) {
            return ExecutionMode._5053;
        } else if (_5053_AND_5054.getUserArg().equals(userArg)) {
            return ExecutionMode._5053_AND_5054;
        } else {
            StringBuilder sb = buildValuesList();
            throw new IllegalArgumentException("Invalid user argument. [" + userArg
                    + "] is not a valid value for ExecutionMode. Possible values are [" + sb.toString() + "] ");
        }
    }

    private static StringBuilder buildValuesList() {
        StringBuilder sb = new StringBuilder();
        for (ExecutionMode mode : values()) {
            sb.append(mode.getUserArg());
            sb.append(" ");
        }
        return sb;
    }

}
