<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="GLI0014";

globaz.libra.vb.groupes.LIGroupesViewBean viewBean = (globaz.libra.vb.groupes.LIGroupesViewBean) session.getAttribute("viewBean");

	bButtonCancel = false;	
	selectedIdValue = viewBean.getIdGroupe();

	String domaines[] = viewBean.getDomainesList(false);

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOMAINES_DE%>.ajouter"
  }

  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOMAINES_DE%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOMAINES_DE%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOMAINES_DE%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_DOMAINES_DE%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
    // recharger la page rcListe du parent si une modification a ete effectuee
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
  }
  
  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly && 
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function rechargerPage() {
  	if (document.forms[0].elements("_method").value == "add") {
		document.forms[0].elements("userAction").value = "<%=globaz.libra.servlet.ILIActions.ACTION_DOMAINES_DE%>.afficher";
		document.forms[0].submit();
	}
  }
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_GES_GROUPES_TITRE_DE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">						
			<table width="95%">	
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD><ct:FWLabel key="ECRAN_GES_GROUPES_NO"/></TD>
								<TD><%= viewBean.getIdGroupe() %></TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="ECRAN_GES_LIBELLE"/></TD>
								<TD><input type="text" name="libelleGroupe" value="<%= viewBean.getLibelleGroupe() %>"></TD>
								<TD><ct:FWLabel key="ECRAN_GES_DOMAINE"/></TD>
								<TD>
									<select name="idDomaine" default="">
										<% for (int i=0; i < domaines.length; i=i+2){ %>			
											<OPTION value="<%=domaines[i]%>" <%=domaines[i].equals(viewBean.getIdDomaine())?"selected":""%>><%=domaines[i+1]%></OPTION>					
										<% } %>
									</select>
								</TD>
							</TR>
						</table>
					</td>
				</tr>
			</table>
		</TD>
	</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>