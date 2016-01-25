	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%

	idEcran="GLI0020";

	globaz.libra.vb.formules.LIRappelViewBean viewBean = (globaz.libra.vb.formules.LIRappelViewBean) session.getAttribute("viewBean");
	
	bButtonDelete = false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="libra-optionsformules">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdFormule() %>"/>
	<ct:menuSetAllParams key="idDefinitionFormule" value="<%=viewBean.getIdDefinitionFormule() %>"/>
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

  function upd() {
  }

  function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_FORMULES_RC%>.chercher"; 	  	
  }
	   
  function validate() { 
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_RAPPEL_DE%>.modifier";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=globaz.libra.servlet.ILIActions.ACTION_RAPPEL_DE%>.modifier";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_DE_RAP_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">
							<TR>
								<TD width="20%"><b><ct:FWLabel key="ECRAN_DE_RAP_LIBELLE"/></b></TD>
								<TD colspan="3"><b><%= viewBean.getLibelleDocument() %></b></TD>
							</TR>
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>				
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">								
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_DE_RAP_TEMPS"/></TD>
								<TD colspan="3">							
									<input type="text" name="tempsAttente" value="<%= viewBean.getTempsAttente() %>">	
								</TD>
							</TR>																
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_DE_RAP_UNITE"/></TD>
								<TD colspan="3">							
									<ct:select name="csUnite" defaultValue="<%=viewBean.getCsUnite()%>" wantBlank="false">
										<ct:optionsCodesSystems csFamille="ENUNITE">
										</ct:optionsCodesSystems>
									</ct:select>
								</TD>
							</TR>
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>				
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">								
							<TR>
								<TD width="20%"><ct:FWLabel key="ECRAN_DE_RAP_FORMULE"/></TD>
								<TD colspan="3">
									<input type="text" name="csDocument" value="<%=viewBean.getDefinitionFormule().getCsDocument() %>">
									<input type="hidden" name="idDefinitionFormule" value="<%=viewBean.getIdDefinitionFormule()%>">
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
								<TD width="20%">&nbsp;</TD>
								<TD colspan="3">
									<b><%= viewBean.getSession().getCodeLibelle(viewBean.getDefinitionFormule().getCsDocument()) %></b>
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