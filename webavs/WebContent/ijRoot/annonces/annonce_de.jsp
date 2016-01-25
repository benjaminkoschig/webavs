<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0027";

	globaz.ij.vb.annonces.IJAnnonceViewBean viewBean = (globaz.ij.vb.annonces.IJAnnonceViewBean)session.getAttribute("viewBean");
	bButtonUpdate=false;
	bButtonDelete=false;
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

  }
  function upd() {}

  function validate() {
    state = true;
    document.forms[0].elements('userAction').value="ij.annonces."+document.forms[0].elements('typeAnnonce').value+".afficher";
    document.forms[0].elements('_method').value == "add";
    return state;
  }

  function cancel() {
      document.forms[0].elements('userAction').value="ij.annonces.annonce.chercher";
  }

  function del() {

  }
  

  	
  	function init(){

  	}

  	
	
	

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCES_NOUVELLE_ANNONCE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_NOUVELLE_ANNONCE"/></TD>
							<TD>
								<SELECT name="typeAnnonce">
									<OPTION value="annonce3EmeRevision"><ct:FWLabel key="JSP_ANNONCES_TROISIEME_REVISION"/></OPTION>
									<OPTION value="annonce4EmeRevision"><ct:FWLabel key="JSP_ANNONCES_QUATRIEME_REVISION"/></OPTION>
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