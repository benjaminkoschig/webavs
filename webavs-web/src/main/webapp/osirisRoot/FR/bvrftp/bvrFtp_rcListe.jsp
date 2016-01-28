
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.bvrftp.CABvrFtpListViewBean" %>
<%@ page import="globaz.osiris.db.bvrftp.CABvrFtpViewBean" %>
<%
CABvrFtpListViewBean viewBean = (CABvrFtpListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
detailLink ="osiris?userAction=osiris.bvrftp.bvrFtp.afficher&selectedId=";

size = viewBean.getPvrFiles().size();

out.print("<script>top.fr_main.document.getElementById('state').value = \"" + viewBean.getFtpConnectionState() + "\";</script>");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH align="left" width="16">&nbsp;</TH>
    <TH align="left" width="20%">Date</TH>
    <TH align="left">Fichier</TH>
    <TH align="center" width="170">Fichier déjà téléchargé ?</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    CABvrFtpViewBean file = viewBean.getPvrFile(i);
    actionDetail = "parent.location.href='"+detailLink+file.getFileName()+"'";

    String styleAndTitle = "";
    if (file.isAlreadyDownloaded()) {
    	styleAndTitle = " style=\"color:#2F9F42;\"";
    	styleAndTitle += " title=\"Fichier déjà téléchargé\"";
    }
    String distantDirName = request.getParameter("distantDirectoryName");
    %>

    <TD class="mtd" width="16" <%=styleAndTitle%>>
    <ct:menuPopup menu="CA-BVR-Lecture" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+file.getFileName())%>">
		<ct:menuParam key="selectedId" value="<%=file.getFileName()%>"/>
		<ct:menuParam key="distantDirectoryName" value="<%=distantDirName%>"/>
		<%
			if (request.getParameter("distantDirectoryName").equals(CABvrFtpListViewBean.FTP_READ_OPAE_REMOTE_DIRECTORY)) {
		%>
			<ct:menuParam key="typeBvrFtp" value="bvr"/>
		<%
			} else {
		%>
			<ct:menuParam key="typeBvrFtp" value="lsv"/>
		<%
			}
		%>

    </ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=file.getFormatedFileDate()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>><%=file.getFileName()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%> align="center">&nbsp;
    <%
    	if (file.isAlreadyDownloaded()) {
	%>
		<IMG src="<%=request.getContextPath()%>/images/ok.gif" >
	<%
    	}
    %>
    </TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>