<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0031";
	
	globaz.ij.vb.prononces.IJInfoComplViewBean viewBean = (globaz.ij.vb.prononces.IJInfoComplViewBean)(session.getAttribute("viewBean"));
	
	bButtonCancel = true;
	bButtonValidate = objSession.hasRight(globaz.ij.servlet.IIJActions.ACTION_INFO_COMPL+".ajoute","ADD");
	bButtonDelete =  true;
	bButtonUpdate = true;
	bButtonNew = false;		
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javaScript">

function add() {}

function upd() {}

function validate() {
	state = true;
	    
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_INFO_COMPL%>.ajouter";
    }
    else{
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_INFO_COMPL%>.modifier";
    }
    
    return state;
}

function cancel() {
   document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.chercher";
}

function init(){
	<%if (viewBean.isBaseIndAfterEnd()){%>
	
		if (window.confirm("<ct:FWLabel key='JSP_BASES_IND_WARN'/>")){
			document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_INFO_COMPL%>.transfererPrononceFinal";
			document.forms[0].submit();  
		}
	<%}%>
}

function typeInfoComplChange(){
}

function del() {
	if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_INFO_COMPL%>.supprimer";
        document.forms[0].submit();
	}
}



 </SCRIPT>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="CREER_INFO_COMPL_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></TD>
							<TD colspan="3">
								<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_BASE_IND_NO_PRONONCE"/></TD>
							<TD><INPUT type="text" name="idPrononce" class="disabled" readonly value="<%=viewBean.getIdPrononce()%>"></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_TYPE_INFORMATION_COMPLEMENTAIRE"/></TD>
							<TD><ct:select name="typeInfoCompl" defaultValue="<%=viewBean.getTypeInfoCompl()%>" onchange="typeInfoComplChange();">
									<ct:optionsCodesSystems csFamille="IJINFCMP"/>
								</ct:select>
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TBODY id="transfererPrononce">
							<TR>
								<TD><ct:FWLabel key="JSP_DATE_TRANSFER"/></TD>
								<TD><ct:FWCalendarTag name="dateInfoCompl" value="<%=viewBean.getDateInfoCompl()%>"/></TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_CAISSE"/></TD>
								<TD>
									<INPUT type="text" name="nomCaisse" class="disabled" readonly value="<%=viewBean.getNomCaisse()%>">
									<INPUT type="hidden" name="idTiersCaisse" value="<%=viewBean.getIdTiersCaisse()%>">
									<ct:FWSelectorTag
										name="selecteurCaisse"
										
										methods="<%=viewBean.getMethodesSelecteurCaisse()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.tiers.administration.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>
								</TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_MOTIF_TRANSFER"/></TD>
								<TD><ct:select name="motif" defaultValue="<%=viewBean.getMotif()%>">
									<ct:optionsCodesSystems csFamille="IJMOTRDO"/>
								</ct:select></TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
						</TBODY>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>