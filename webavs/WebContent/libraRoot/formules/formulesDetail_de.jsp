	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%

	idEcran="GLI0019";

	globaz.libra.vb.formules.LIFormulesDetailViewBean viewBean = (globaz.libra.vb.formules.LIFormulesDetailViewBean) session.getAttribute("viewBean");

	bButtonDelete = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="libra-optionsformules">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getFormulePDF().getIdFormule()%>"/>
</ct:menuChange>

<script language="JavaScript">

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

  function add() {
  }

  function upd() {
  }

  function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_FORMULES_RC%>.chercher"; 	  	
  }
	   
  function validate() { 
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_FORMULES_DE%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_FORMULES_DE%>.modifier";
	    }
	  	return state;	    
  }
  
  function init(){
  }

  function postInit(){
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_DE_FORM_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<tr>
								<td width="20%"><b><ct:FWLabel key="ECRAN_DE_FORM_LIBELLE"/></b></td>
								<td width="80%">
									<b><%= viewBean.getISession().getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument()) %></b>

									<input type="hidden" name="idFormule" value="<%= viewBean.getFormulePDF().getIdFormule() %>">
									<input type="hidden" name="definitionFormule" value="<%= viewBean.getFormulePDF().getIdDefinitionFormule() %>">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<tr>
								<td width="20%"><ct:FWLabel key="ECRAN_DE_FORM_NO_FORMULE"/></td>
								<td width="80%">
									<%= viewBean.getFormulePDF().getIdFormule() %>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DE_FORM_FORMULE"/></TD>
								<TD>
									<input type="text" name="csDocument" value="<%=viewBean.getDefinitionFormule().getCsDocument() %>">
										<ct:FWSelectorTag
											name="selecteurFormule"
											methods="<%=viewBean.getMethodesSelectionFormule()%>"
											providerApplication="libra"
											providerPrefix="LI"
											providerAction="libra.formules.formuleCS.chercher"
											target="fr_main"
											redirectUrl="<%=mainServletPath%>"/>
								</TD>
							</TR>
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DE_FORM_DOMAINE"/></TD>
								<TD width="40%">
									<% String[] domaines = viewBean.getDomaines(); %>
									<% String defaultDomain = viewBean.getFormulePDF().getDomaine(); %>
									<select name="csDomaine">
										<%
											for (int i=0; i < domaines.length; i = i+2){
										
												globaz.libra.db.domaines.LIDomaines dom = new globaz.libra.db.domaines.LIDomaines();
												dom.setSession((globaz.globall.db.BSession)viewBean.getISession());
												dom.setIdDomaine(domaines[i]);
												dom.retrieve(); 									
										
										%>
											<OPTION value="<%=dom.getCsDomaine()%>" <%= dom.getCsDomaine().equals(defaultDomain)?"selected":"" %>> <%=domaines[i+1]%></OPTION>
										<% } %>
									</select>
								</TD>
							</TR>	
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DE_FORM_PROCESS"/></TD>
								<TD><input type="text" name="className" value="<%=viewBean.getFormulePDF().getClasseName() %>" size="70"></TD>
							</TR>
						</table>
					</td>
				</tr>	
				<tr><td colspan="6"><br/></td></tr>			
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<TR>
								<TD width="10%"><ct:FWLabel key="ECRAN_DE_FORM_ACTION_FIN"/></TD>
								<TD width="40%">
									<ct:select name="csValeur" defaultValue="<%=viewBean.getComplementFormuleAction().getCsValeur()%>" wantBlank="false" disabled="disabled" >
										<ct:optionsCodesSystems csFamille="LIACTION">
										</ct:optionsCodesSystems>
									</ct:select>
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