'use strict';

angular.module('carpoolingApp')
    .factory('CarDriverSearch', function ($resource) {
        return $resource('api/_search/carDrivers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
