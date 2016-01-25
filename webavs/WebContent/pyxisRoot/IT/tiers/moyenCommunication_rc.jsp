<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0030";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.db.tiers.TIContactViewBean viewBean = (globaz.pyxis.db.tiers.TIContactViewBean)session.getAttribute("viewBean");
	actionNew +="&idContact="+viewBean.getIdContact();
	bButtonFind = false;
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	bButtonNew = objSession.hasRight("pyxis.tiers.moyenCommunication", "ADD");

%>
<SCRIPT>
detailLink ="pyxis?userAction=pyxis.tiers.moyenCommunication.afficher&_method=add&idContact="+<%=viewBean.getIdContact()%>;
usrAction = "pyxis.tiers.moyenCommunication.lister";

timeWaiting=1;
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Mezzi di comunicazioni del contatto<%-- /tpl:put  --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
          <TR> 
            <TD nowrap width="128">Nome del contatto :</TD>
            <TD nowrap colspan="2" width="298"> 
               <b><%=viewBean.getNom() + " " + viewBean.getPrenom()%></b>
              <INPUT type="hidden" name="forIdContact" value="<%=viewBean.getIdContact()%>">
            </TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>

<ct:menuChange displayId="options" menuId="moyenCommunication-detail" showTab="options">
	<ct:menuSetAllParams key="idContact" value="<%=viewBean.getIdContact()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>