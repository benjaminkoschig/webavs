<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.helios.db.comptes.*, globaz.helios.db.interfaces.*, globaz.globall.db.*, globaz.helios.translation.*" %>
<%
	idEcran="GCF0009";
	CGExerciceComptableViewBean viewBean = (CGExerciceComptableViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdExerciceComptable();	
	userActionValue = "helios.comptes.exerciceComptable.modifier";
	String idMandat = viewBean.getIdMandat();
		
	java.util.Vector vList = new java.util.Vector();
	String[] list = new String[2];

	list[0] = "0";
	list[1] = "Exercice Précédent";
	if (languePage.equalsIgnoreCase("DE"))
		list[1] = "Vorhergehendes Rechnungsjahr";
	vList.add(list);

	try {
		CGExerciceComptableManager manager = new CGExerciceComptableManager();
		manager.setSession((BSession)CodeSystem.getSession(session));
		manager.setForIdMandat(idMandat);
		manager.find();
	
		for (int i = 0;i<manager.size();i++) {
			list = new String[2];
			CGExerciceComptable entity = (CGExerciceComptable)manager.getEntity(i);
			list[0] = entity.getIdExerciceComptable();
			list[1] = entity.getFullDescription();
			vList.add(list);
		}
	} catch(Exception e){
		// si probleme, retourne une liste vide.
	}
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.supprimer";
        document.forms[0].submit();
    }
}

function refreshPage() {
	document.forms[0].elements('_method').value == "add"
	document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.afficher";
	document.forms[0].submit();	
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Rechnungsjahres<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
  <tr>
    <td>Nummer</td>
    <td><input type="text" name="idExerciceComptable" class="libelleDisabled" readonly value="<%=viewBean.getIdExerciceComptable()%>"></td>
  </tr>

  <tr>
    <td>Mandant</td>
    <td> <%    			
    		if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
	      <ct:FWListSelectTag name="idMandat"
	 	  defaut="<%=idMandat%>"
	 	  data="<%=globaz.helios.translation.CGListes.getMandatListe(session)%>"/>
	 	  <script>
		 	  	element = document.getElementById("idMandat");
		 	  	element.onchange=refreshPage;
	 	  </script> 	  
      <%} else {%>
 	  <input  type="text" readonly class="libelleLongDisabled" value="<%=viewBean.getMandat().getLibelle()%>" >
      <%}%>
    </td>
  </tr>
  <tr>
    <td>Beginndatum des Rechnungsjahr</td>
    <td>
    <ct:FWCalendarTag name="dateDebut" 
                value="<%=viewBean.getDateDebut()%>" 

              />*
    </td>
  </tr>
  <tr>
    <td>Enddatum des Rechnungsjahr</td>
    <td>
    <ct:FWCalendarTag name="dateFin" 
                value="<%=viewBean.getDateFin()%>" 
                              />*
    </td>
  </tr>
  <tr>
    <td>Abgeschlossenes Rechnungsjahr </td>
    <td><input type="checkbox" name="estCloture" <%=(viewBean.isEstCloture().booleanValue())?"CHECKED":""%> ></td>
  </tr>  
  <%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
  <tr>
    <td>Erstellung des Kontoplan ab :</td>
    <td><select name="idExerForPlan">
<% for (int i=0 ; i<vList.size() ; i++) {String[] values = (String[])vList.get(i);%><option value="<%=values[0]%>"><%=values[1]%></option><%}%>
	</select>
    </td>
  </tr>
  <%}%>
     
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="CG-exerciceComptable" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdExerciceComptable()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>