package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;

public class ControlleurVariablesMetier {

    private VariableMetier variable = null;
    private long variableCourante = 0;

    public ControlleurVariablesMetier(VariableMetier variable, long debutPeriode) {
        super();
        this.variable = variable;
        variableCourante = setVariableCourante(debutPeriode);
    }

    /**
     * Retourne le l�gende de la p�ridode courante
     * 
     * @return
     * @throws CalculBusinessException
     */
    public String getLegendeCourante() throws CalculBusinessException {
        // on r�cup�re la date courante au fomat globaz
        String dateCourante = returnGlobazDate(variableCourante);
        // on recup�re la valeur de la variable m�tier pour le p�riode courante
        String legendeCourante = variable.getLegendesVariablesMetiers().get(variableCourante);
        // si pas de valeur, elle n'est pas d�finies
        if (legendeCourante == null) {
            throw new CalculBusinessException("pegasus.calcul.variablemetier.legende.periode.manquante", BSessionUtil
                    .getSessionFromThreadContext().getCodeLibelle(variable.getCsTypeVariableMetier()), dateCourante);
        }

        return legendeCourante;
    }

    /**
     * Retourne la valeur de la p�riode courante
     * 
     * @return
     * @throws CalculBusinessException
     */
    public String getValeurCourante() throws CalculBusinessException {

        // on r�cup�re la date courante au fomat globaz
        String dateCourante = returnGlobazDate(variableCourante);
        // on recup�re la valeur de la variable m�tier pour le p�riode courante
        String valeurCourante = variable.getVariablesMetiers().get(variableCourante);
        // si pas de valeur, elle n'est pas d�finies
        if (valeurCourante == null) {
            throw new CalculBusinessException("pegasus.calcul.variablemetier.periode.manquante", BSessionUtil
                    .getSessionFromThreadContext().getCodeLibelle(variable.getCsTypeVariableMetier()), dateCourante);
        }

        return valeurCourante;
    }

    public VariableMetier getVariable() {
        return variable;
    }

    public long getVariableCourante() {
        return variableCourante;
    }

    private String returnGlobazDate(long timeStamp) {
        Date date = new Date(timeStamp);
        String dateCourante = JadeDateUtil.getFormattedDate(date);
        return dateCourante;
    }

    public void setVariable(VariableMetier variable) {
        this.variable = variable;
    }

    private long setVariableCourante(long debutPeriode) {
        Map<Long, String> datesOrdonnees = new TreeMap<Long, String>(variable.getVariablesMetiers());

        long dateDebutVarMetier = debutPeriode;

        // iteration sur les cl�s
        for (Long varMet : datesOrdonnees.keySet()) {
            // Si la date de la periode est plus grande ou egale a la varaiable metier
            if (debutPeriode >= varMet) {
                dateDebutVarMetier = varMet;
            } else {
                return dateDebutVarMetier;
            }
        }
        return dateDebutVarMetier;
    }

}
