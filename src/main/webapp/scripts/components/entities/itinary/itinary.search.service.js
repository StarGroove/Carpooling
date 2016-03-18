'use strict';

angular.module('carpoolingApp')
    .factory('ItinarySearch', function ($resource) {
        return $resource('api/_search/itinarys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
