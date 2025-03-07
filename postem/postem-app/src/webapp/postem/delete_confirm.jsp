<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf2/sakai" prefix="sakai" %>

<%
    response.setContentType("text/html; charset=UTF-8");
    response.addDateHeader("Expires", System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 365L));
    response.addDateHeader("Last-Modified", System.currentTimeMillis());
    response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
    response.addHeader("Pragma", "no-cache");
%>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
<jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.postem.bundle.Messages"/>
</jsp:useBean>
<f:view>
	<sakai:view title="#{msgs.title_list}">
		<script src="/library/js/spinner.js"></script>
		<script>includeLatestJQuery("postemDeleteConfirm");</script>
		<script>
			$(document).ready(function() {
				var menuLink = $('#postemMainMenuLink');
				var menuLinkSpan = menuLink.closest('span');
				menuLinkSpan.addClass('current');
				menuLinkSpan.html(menuLink.text());
			});
		</script>
		<h:form>
			<%@ include file="/postem/postemMenu.jsp" %>
			<h:outputText styleClass="sak-banner-warn" value="#{msgs.delete_confirm}" />
			<br />
			<table styleClass="itemSummary">
				<tr>
					<th scope="row"><h:outputText value="#{msgs.title_label}" /></th>
					<td><h:outputText value="#{PostemTool.currentGradebook.title}"/></td>
				</tr>
			</table>

			<sakai:button_bar>
				<h:commandButton action="#{PostemTool.processDelete}"
									   onclick="SPNR.disableControlsAndSpin(this, null);"
									   value="#{msgs.bar_delete}"
									   styleClass="active" />
				<h:commandButton action="#{PostemTool.processCancelView}"
									   onclick="SPNR.disableControlsAndSpin(this, null);"
									   value="#{msgs.cancel}" />
			</sakai:button_bar>
		</h:form>
	</sakai:view>
</f:view>
