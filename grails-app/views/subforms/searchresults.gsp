<g:if test="searchResult">
  <div id="searchresults">
    <h2>
      ${searchResult.location.city}
      <g:if test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>
      , ${searchResult.location.country}
    </h2>
    <div id="currentWeather">
      <h3>Current Weather</h3>

    </div>
    <div id="forecast">
      <h3>5 Day Forecast</h3>
    </div>
  </div>
</g:if>
<g:else>
  Invalid Search
</g:else>