<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%	globaz.pavo.db.process.CIAnnonceCentraleProcessViewBean viewBean = (globaz.pavo.db.process.CIAnnonceCentraleProcessViewBean)session.getAttribute("viewBean");
	subTableWidth = "60%";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%idEcran = "CCI3003";%>
<SCRIPT language="javascript">
function add() {}
function upd() {}
function validate() {
	document.forms[0].elements('userAction').value="pavo.process.annonceCentraleProcess.executer";
	return validateFields();
}
function cancel() {}
function del() {}
function init(){}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Anzahl Meldungen pro Tag : </TD>
							<TD><INPUT type="text" name="nbreMax"></INPUT></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>Anzahl Meldungen zu senden : </TD>
							<%	 java.util.Vector vec = new java.util.Vector();
						        String[] newLine = new String[2];
						        newLine[0] = "0";
						        newLine[1] = "Toutes";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "50000";
						        newLine[1] = "50'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "100000";
						        newLine[1] = "100'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "150000";
						        newLine[1] = "150'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "200000";
						        newLine[1] = "200'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "250000";
						        newLine[1] = "250'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "300000";
						        newLine[1] = "300'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "350000";
						        newLine[1] = "350'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "400000";
						        newLine[1] = "400'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "450000";
						        newLine[1] = "450'000";
						        vec.addElement(newLine);
						        newLine = new String[2];
						        newLine[0] = "500000";
						        newLine[1] = "500'000";
						        vec.addElement(newLine);
							%>
							<TD><ct:FWListSelectTag data="<%=vec%>" defaut="" name="nbreTotal"/></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>Sind es die letzten IK-Buchungen dieses Jahr zu melden ?</TD>
							<% java.util.Vector ouiNon = new java.util.Vector();
								String[] line = new String[2];
						        line[0] = "False";
						        line[1] = "Nein";
						        ouiNon.addElement(line);
						        line = new String[2];
						        line[0] = "True";
						        line[1] = "Ja";
						        ouiNon.addElement(line);
							%>
							<TD><ct:FWListSelectTag data="<%=ouiNon%>" defaut="" name="isDernier"/></TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD>Verarbeitungsergebnis per E-Mail senden an </TD>
							<TD><INPUT type="text" name="email" size="40" value="<%=viewBean.getSession().getUserEMail()%>"></INPUT></TD>
							<TD>&nbsp;</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>