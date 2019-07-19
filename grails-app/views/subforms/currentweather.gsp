<div id="currentWeather" class="card p-1 m-1">
  <div class="grid-2">
    <div class="all-center">
      <img src="${searchResult.currentWeather.icon}" style="width: 100px"/>

      <p>${searchResult.currentWeather.condition}</p>

      <div class="grid-2">
        <div class="m-1">
          <p>${searchResult.currentWeather.temperature.round()}&deg;</p>
        </div>

        <div class="m-1">
          <p>${searchResult.currentWeather.humidity}% H</p>
        </div>
      </div>

      <p>${searchResult.currentWeather.windSpeed.round()} mph ${searchResult.currentWeather.windDirection} wind</p>

    </div>

    <div class="all-center">
      <h1 class="text-center"><g:message code="msg.currentweather" default="Current Weather"/></h1>

      <h2>
        ${searchResult.location.city}
        <g:if test="${searchResult.location.stateProvince}">, ${searchResult.location.stateProvince}</g:if>
        <br/>${searchResult.location.country}
      </h2>
    </div>
  </div>
</div>