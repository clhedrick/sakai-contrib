<#if (post??)>
<#if (post.subject??)>
$("#previewSubject").html("${post.subject?html}");
</#if>
<#if (post.text??)>
$("#previewMessage").html("${post.text}");
</#if>
</#if>
$("#previewTable").show();

var s = document.location.toString();
var index = s.indexOf("#preview");

if (index > -1) {
	s = s.substring(0, index);
}

document.location = s + "#preview";

