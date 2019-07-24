<g:if test="${flash.error}">
  <p class="alert-danger all-center p m">${flash.error}</p>
</g:if>
<g:elseif test="${flash.success}">
  <p class="alert-success all-center p m">${flash.success}</p>
</g:elseif>