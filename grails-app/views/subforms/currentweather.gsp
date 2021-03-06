<div id="currentWeather" class="card p-1 m-1 grid-2">
  <div class="all-center">
    <img src="${searchResult.currentWeather.icon}" style="width: 100px"/>

    <p>${searchResult.currentWeather.condition}</p>

    <div class="grid-2">
      <div class="m-1">
        <p>${searchResult.currentWeather.temperature.round()}&deg;<sup>${params.units}</sup></p>
      </div>

      <div class="m-1">
        <p>${searchResult.currentWeather.humidity}% H</p>
      </div>
    </div>

    <p>${searchResult.currentWeather.windSpeed.round()} mph ${searchResult.currentWeather.windDirection} ${message(code: 'msg.winds', default: 'winds')}</p>

  </div>

  <div class="all-center">
    <sec:ifNotLoggedIn>
      <g:form controller="signup" action="index" method="get">
        <input type="hidden" name="favoritelocation" value="${searchResult.location.city}<g:if
            test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>, ${searchResult.location.country}">
        <input type="submit" value="Add as Favorite Location" class="btn-white p">
      </g:form>
    </sec:ifNotLoggedIn>
    <sec:ifLoggedIn>
      <g:if test="${searchResult.location.toString() != user.favoriteLocation}">
        <g:form controller="account" action="updatelocation" method="post">
          <input type="hidden" name="favoritelocation" value="${searchResult.location.city}<g:if
              test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>, ${searchResult.location.country}">
          <input type="submit" value="${message(code: 'msg.setfavoritelocation', default: 'Set as Favorite Location')}" class="btn-white p">
        </g:form>
      </g:if>
    </sec:ifLoggedIn>
    <h1 class="text-center">${message(code: "msg.currentweather", default: "Current Weather")}</h1>

    <h2>
      ${searchResult.location.city}
      <g:if test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>
      <br/>${searchResult.location.country}
    </h2>
  </div>
</div>