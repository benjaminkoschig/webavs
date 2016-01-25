<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0030";
	
	globaz.ij.vb.prononces.IJTerminerPrononceViewBean viewBean = (globaz.ij.vb.prononces.IJTerminerPrononceViewBean)(session.getAttribute("viewBean"));

	globaz.framework.controller.FWController controller1 = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession1 = (globaz.globall.db.BSession)controller1.getSession();
	String eMailAddress=objSession1.getUserEMail();
	
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete =  false;
	bButtonUpdate = objSession.hasRight(globaz.ij.servlet.IIJActions.ACTION_TERMINER_PRONONCE +"terminerPrononce","ADD");
	bButtonNew = false;	

		
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javaScript">

function add() {}

function upd() {
  	
}

function validate() {
	document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_TERMINER_PRONONCE%>.terminerPrononce";
	document.forms[0].submit();
}

function cancel() {
   document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.chercher";
}

function init(){

	<%if (viewBean.isBaseIndAfterEnd()){%>
			if (window.confirm("<ct:FWLabel key='JSP_BASES_IND_WARN'/>")){			
					document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_TERMINER_PRONONCE%>.terminerPrononceFinal";
					document.forms[0].submit();  
		    }
	<%}%>

}

 </SCRIPT>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TERMINER_PRONONCE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_PRONONCE"/></TD>
							<TD><INPUT type="text" name="idLPrononce" class="disabled" readonly value="<%=viewBean.getIdPrononce()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></TD>
							<TD><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
						</TR>
						<tr>
							<td>&nbsp;
							</td>
						</tr>			
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_FIN"/></TD>
							<TD><ct:FWCalendarTag name="dateFinPrononce" value="<%=viewBean.getDateFinPrononce()%>"/></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>