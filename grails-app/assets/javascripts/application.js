// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require popper.min
//= require typeahead
//= require_self

//city, stateprovince, country
let locations = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/location/search/?q=%QUERY',
        wildcard: '%QUERY'
    }
});

$('.typeahead').typeahead(
    {
        minLength: 2,
        hint: false,
        highlight: true
    }, {
        name: 'location',
        display: Handlebars.compile('{{city}}, {{stateProvince}}, {{country}}'),
        source: locations,
        limit: 30,
        templates: {
            empty: ['no cities found'],
            suggestion: Handlebars.compile('<div><strong>{{city}}</strong>, {{stateProvince}}, {{country}}</div>')
        }
    });

//cities only
let cities = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/location/searchcity/?q=%QUERY',
        wildcard: '%QUERY'
    }
});


$('.typeaheadCity').typeahead(
    {
        minLength: 4,
        hint: false
    }, {
        name: 'city',
        source: cities,
        limit: 30,
        templates: {
            empty: ['No record of city']
        }
    });

// states / provinces only
let stateProvinces = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/location/searchstateprovince/?q=%QUERY',
        wildcard: '%QUERY'
    }
});


$('.typeaheadStateProvince').typeahead(
    {
        minLength: 3,
        hint: false
    }, {
        name: 'stateProvince',
        source: stateProvinces,
        limit: 30,
        templates: {
            empty: ['no record of state or province']
        }
    });

//countries only
let countries = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/location/searchcountry/?q=%QUERY',
        wildcard: '%QUERY'
    }
});


$('.typeaheadCountry').typeahead(
    {
        minLength: 2,
        hint: false
    }, {
        name: 'country',
        source: countries,
        limit: 30,
        templates: {
            empty: ['no record of country']
        }
    });

