// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-3.3.1.min
//= require bootstrap
//= require popper.min
//= require typeahead
//= require_self

var cities = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    prefetch: '/location/search/?q=lin&max=20',
    remote: {
        url: '/location/search/?q=%QUERY',
        wildcard: '%QUERY'
    }
});

$('#remote .typeahead').typeahead(
    {
        minLength: 3,
        hint: true
    }, {
        name: 'location',
        display: Handlebars.compile('{{city}}, {{stateProvince}}, {{country}}'),
        source: cities,
        limit: 50,
        templates: {
            empty: ['no cities found'],
            suggestion: Handlebars.compile('<div><strong>{{city}}</strong>, {{stateProvince}}, {{country}}</div>')
        }
    });