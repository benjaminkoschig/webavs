<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	
	idEcran="PAP0020";

	globaz.apg.vb.annonces.APAnnonceAPGViewBean viewBean = (globaz.apg.vb.annonces.APAnnonceAPGViewBean)session.getAttribute("viewBean");
	bButtonUpdate=false;
	bButtonDelete=false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, paternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)== IPRDemande.CS_TYPE_PATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<script language="JavaScript">

  function add() {}
  
  function upd() {}

  function validate() {
    state = true;
    document.forms[0].elements('userAction').value="apg.annonces.annonceAPG.afficher";
    document.forms[0].elements('_method').value == "add";
    document.forms[0].elements('_valid').value == "fail";
    return state;
  }

  function cancel() {
      document.forms[0].elements('userAction').value="apg.annonces.annonceAPG.chercher";
  }

  function del() {}
  
  function init(){}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCES"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_NOUVELLE_ANNONCE"/></TD>
							<TD>
								<SELECT name="typeAnnonce">
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_MATERNITE%>"><ct:FWLabel key="JSP_ANNONCE_MATERNITE"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_PATERNITE%>"><ct:FWLabel key="JSP_ANNONCE_PATERNITE"/></OPTION>
                                    <OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_PROCHE_AIDANT%>"><ct:FWLabel key="ANNONCE_PROCHE_AIDANT"/></OPTION>
                                    <OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_APGREVISION1999%>"><ct:FWLabel key="JSP_ANNONCE_APG_REVISION_1999"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_APGREVISION2005%>"><ct:FWLabel key="JSP_ANNONCE_APG_REVISION_2005"/></OPTION>
									<OPTION value="<%=globaz.apg.api.annonces.IAPAnnonce.CS_APGSEDEX%>"><ct:FWLabel key="JSP_ANNONCE_APG_SEDEX"/></OPTION>
								</SELECT>
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