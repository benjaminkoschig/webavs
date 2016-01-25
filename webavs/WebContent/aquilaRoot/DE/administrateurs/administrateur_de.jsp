<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%

    idEcran="GCO0014";

	bButtonUpdate = false;
	globaz.aquila.db.administrateurs.COAdministrateurViewBean viewBean = (globaz.aquila.db.administrateurs.COAdministrateurViewBean)session.getAttribute("viewBean");

	Object[] tiersMethods = new Object[]{new String[]{"setIdTiers","getIdTiers"}};
	
	String forIdExterneLike=request.getParameter("forIdExterneLike");
	String nomAffilie = request.getParameter("nomAffilie");
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsAdministrateurs" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdCompteAnnexe()%>"/>
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdCompteAnnexe()%>"/>
	<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
	<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=viewBean.getIdCompteAnnexe()%>"/>
</ct:menuChange>

<script language="JavaScript">
	function add() {
	    document.forms[0].elements('userAction').value="aquila.administrateurs.administrateur.ajouter";
	}
	
	function upd() {
	}
	
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="aquila.administrateurs.administrateur.ajouter";
	    else
	        document.forms[0].elements('userAction').value="aquila.administrateurs.administrateur.modifier";
	    
	    return state;
	
	}
	
	function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="aquila.administrateurs.administrateur.chercher";
	 else
	  document.forms[0].elements('userAction').value="aquila.administrateurs.administrateur.chercher";
	  
	}
	
	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="aquila.administrateurs.administrateur.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	
	}

  	
	
	

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Verwalters<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Partner</TD>
							<TD><INPUT type="text" name="nomTiers" value="<%=viewBean.getNomTiers()%>" class="libelleLong disabled" readonly> <ct:FWSelectorTag name="tiersSelector"  providerApplication="pyxis" providerPrefix="TI" providerAction="pyxis.tiers.tiers.chercher" methods="<%=tiersMethods%>"/></TD>
						</TR>
						<TR>
							<TD>Adresse</TD>
							<TD><TEXTAREA cols="24" rows="6" class="disabled" readonly><%=viewBean.getAdresse()%></TEXTAREA></TD>
						</TR>
						<TR>
							<TD>Saldo</TD>
							<TD><INPUT type="text" value="<%=viewBean.getSoldeFormate()%>" class="disabled" readonly onchange="validateFloatNumber(this);"></TD>
						</TR>
						<TR>
							<TD><INPUT type="hidden" name="likeIdExterneRole" value=<%=forIdExterneLike%>></TD>
							<TD><INPUT type="hidden" name="nomAffilie" value="<%=nomAffilie%>"></TD>
							<TD><INPUT type="hidden" name="idExterneRoleChoisi" value="<%=forIdExterneLike%>"></TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>