<sec:ifAnyGranted roles="ROLE_ADMIN">
  <nav class="nav-rel bg-dark navbar" role="navigation">
    <ul class="">
      <li>
        <g:link controller="admin" action="index">${message(code: 'msg.adminhome', default: 'Admin Home')}</g:link>
      </li>
      <li>
        <g:link controller="admin" action="users">${message(code: 'msg.manageusers', default: 'Manage Users')}</g:link>
      </li>
      <li>
        <g:link controller="admin" action="searchlogs">${message(code: 'msg.searchlogs', default: 'Search Logs')}</g:link>
      </li>
      <li>
        <g:link controller="admin" action="emaillogs">${message(code: 'msg.emaillogs', default: 'Email Logs')}</g:link>
      </li>
      <li>
        <g:link controller="admin"
                action="locations">${message(code: 'msg.managelocationsuggestions', default: 'Manage Location Suggestions')}</g:link>
      </li>
    </ul>
  </nav>
</sec:ifAnyGranted>