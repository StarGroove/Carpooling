 'use strict';

angular.module('carpoolingApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-carpoolingApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-carpoolingApp-params')});
                }
                return response;
            }
        };
    });
