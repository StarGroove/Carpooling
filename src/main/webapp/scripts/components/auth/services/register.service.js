'use strict';

angular.module('carpoolingApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


