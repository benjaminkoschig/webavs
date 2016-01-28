<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
    globaz.naos.db.plan.AFPlanViewBean viewBean = (globaz.naos.db.plan.AFPlanViewBean)session.getAttribute ("viewBean");
	globaz.naos.db.assurance.AFAssuranceListViewBean list = (globaz.naos.db.assurance.AFAssuranceListViewBean)request.getAttribute("assuranceListViewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function showPartie(nomPartieAVoir, nomPartieACacher) {
	// Partie à montrer
	var element = document.all(nomPartieAVoir);
	if (element != null) {
		element.style.display = 'block';
	}
	// Partie à cacher
	element = document.all(nomPartieACacher);
	if (element != null) {
		element.style.display = 'none';
	}
	/*document.all('tPartie2').style.display='none';
	document.all('tPartie1').style.display='block';*/
}

function add() {
    document.forms[0].elements('userAction').value="naos.plan.plan.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;

	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="naos.plan.plan.ajouter";
		construireAssurance();
	} else {
		document.forms[0].elements('userAction').value="naos.plan.plan.modifier";
		construireAssurance();
    }
	return (exit);
}

function construireAssurance(){
	var Str ="";
	var objsCollection = document.forms[0].elements;
	for (var i = 0; i < objsCollection.length; i++) {
		var tmpObj = objsCollection(i);
		if (tmpObj.type=="checkbox"){
			if (tmpObj.checked ==true) {
				Str=Str + tmpObj.name;
				Str += ".";
				/*if (i+2 < objsCollection.length) {
					Str = Str +".";
				}*/
			}
		}
	}
	document.forms[0].selectionAssurance.value=Str;
	/*document.forms[0].assuranceSelection.value=Str;*/
	if (Str.length > 0) {
		Str = Str.substr(0, Str.length - 1);
	}
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.plan.plan.afficher";
}

function del() {
    if (window.confirm("Sie sind dabei, den ausgewählten Versicherungsplan zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="naos.plan.plan.supprimer";
        document.forms[0].submit();
    }
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Detail des Versicherungsplans
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          				<TR> 
            				<TD width="101">Name des Versicherungsplans 
								<INPUT type="text"   name="planLibelle" size="20" maxlength="20"  value="<%=viewBean.getPlanLibelle()%>">
								<INPUT type="hidden" name="selectionAssurance" value="">
								<INPUT type="hidden" name="selectedId" value="<%=request.getParameter("selectedId")%>">
            				</TD>
          
          				</TR>
		 				<TR>  
		  					<TD>
				                <%
									int maxParPage = 6;
									globaz.naos.html.assurance.AFPageAssurancesDirector director = 
										new globaz.naos.html.assurance.AFPageAssurancesDirector(maxParPage);
									director.setPlanId(viewBean.getPlanId());
									int size = list.getSize();
									for (int i = 0; i < size; i++) {
										director.ajouter((globaz.naos.db.assurance.AFAssurance)list.getEntity(i));
									}
								%>
								<%=director.terminer()%>
		  					</TD>
         				</TR>
          				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>