<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--

INFO !!!!
Les labels de cette page sont prefixes avec 'LABEL_JSP_COT_D'

--%>
<%
idEcran="PIJ0008";
globaz.ij.vb.prestations.IJCotisationViewBean viewBean = (globaz.ij.vb.prestations.IJCotisationViewBean) session.getAttribute("viewBean");

bButtonCancel = false;
bButtonUpdate = viewBean.getIsEditable();
bButtonDelete = viewBean.getIsEditable();

bButtonValidate = objSession.hasRight(globaz.ij.servlet.IIJActions.ACTION_COTISATIONS+".ajouter","ADD");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<script language="JavaScript">


  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.afficher";
  }

  function del() {
	  if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_COTISATIONS%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
  }
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_COT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>
							<%if (viewBean.getIsImpotSource()!=null) {%>
								<INPUT type="hidden" name="isImpotSource" value="<%=viewBean.getIsImpotSource().booleanValue()%>">
							<%} %>
							<TD><ct:FWLabel key="JSP_COT_D_COTISATION"/></TD>
							<TD><INPUT type="text" name="nomExterne" value="<%=viewBean.getNomExterne()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_COT_D_PERIODE"/></TD>
							<TD>
								<ct:FWLabel key="JSP_DU"/>&nbsp;
								<INPUT type="text" name="dateDebut" value="<%=viewBean.getDateDebut()%>" class="dateDisabled" readonly>&nbsp;
								<ct:FWLabel key="JSP_AU"/>&nbsp;
								<INPUT type="text" name="dateFin" value="<%=viewBean.getDateFin()%>" class="dateDisabled" readonly>&nbsp;
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_COT_D_TAUX_PC"/></TD>
							<TD colspan="3"><INPUT type="text" name="taux" value="<%=viewBean.getTaux()%>" class="montantDisabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_COT_D_MONTANT_BRUT"/></TD>
							<TD><INPUT type="text" name="montantBrut" value="<%=viewBean.getMontantBrut()%>" class="montantDisabled"readonly></TD>
							<TD></TD>
							<TD></TD>
						</TR>
						<TR>
							<TD><LABEL for="montant"><ct:FWLabel key="JSP_COT_D_COTISATION"/></LABEL></TD>
							<TD colspan="3"><INPUT type="text" name="montant" value="<%=viewBean.getMontant()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>