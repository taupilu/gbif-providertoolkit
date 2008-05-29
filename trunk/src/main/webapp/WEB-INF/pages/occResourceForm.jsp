<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="occResourceDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='occResourceDetail.heading'/>"/>
	<s:head theme="ajax" debug="true"/>
</head>

<s:form id="occResourceForm" action="saveOccResource" method="post" validate="true">
    <li style="display: none">
        <s:hidden key="occResource.id"/>
    </li>

    <!-- The DatasoureMetadata forms -->
    <s:textfield key="occResource.title" required="true" cssClass="text medium"/>
    <s:textarea key="occResource.description" cssClass="text large"/>

    <div class="group">
	    <s:textfield key="occResource.sourceJdbcConnection" required="true" maxlength="120" cssClass="text large"/>
	    <s:textfield key="occResource.serviceName" maxlength="16" cssClass="text medium"/>
    </div>

    <p>
    	Resource last modified by <s:property value="occResource.modifier.getFullName()"/>: <s:property value="occResource.modified"/>
    </p>

    <li class="buttonBar bottom">
        <s:submit cssClass="button" method="save" key="button.save" theme="simple"/>
        <c:if test="${not empty occResource.id}">
            <s:submit cssClass="button" method="delete" key="button.delete"
                onclick="return confirmDelete('OccResource')" theme="simple"/>
        </c:if>
        <s:submit cssClass="button" method="cancel" key="button.cancel" theme="simple"/>
    </li>
</s:form>

<script type="text/javascript">
    Form.focusFirstElement($("occResourceForm"));
</script>
