<#include "/WEB-INF/pages/inc/header.ftl">
	<title><@s.text name="title"/></title>
<#include "/WEB-INF/pages/inc/menu.ftl">

<h1><@s.text name="portal.home.title"/></h1>

<ul>
<#list resources as r>
  <li>${r.shortname}</li>
</#list>
</ul>
<#include "/WEB-INF/pages/inc/footer.ftl">
