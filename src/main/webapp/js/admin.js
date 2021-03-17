'use strict';

/**
 * Déclaration de l'application
 */
var adminApp = angular.module('adminApp', [ "ngRoute" ]);

/**
 * Routes
 */

adminApp.config(function($routeProvider) {
   $routeProvider
   .when("/", {
      templateUrl: "angularpartials/noauth.htm",
      controller: "loginCtrl"
   })
   .when("/admin", {
      templateUrl: "angularpartials/adminpanel.htm",
      controller: "adminCtrl"
   })
   .otherwise({
      redirectTo: '/'
   });
});

/**
 * Service d'authentification
 */
adminApp.service('auth', [ '$http', '$window', function($http, $window) {
   this.isAuth = false;
   this.urlAuth = '/admin/auth';
   
   this.login = function(username, password, callback) {
      var params = 'login=' + encodeURIComponent(username) +
                   '&pass=' + encodeURIComponent(password);
      var service = this;

      $http.post(this.urlAuth, params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .then(function onSuccess(response) {
         let data = response.data;

         if (data.success) {
            $window.localStorage['currentUser'] = username;

            // add jwt token to auth header for all requests made by the $http service
            $http.defaults.headers.common.Authorization = 'Bearer ' + data.token;
            service.isAuth = true;
            callback(true);
         }
         else {
            service.logout();
            callback(false);
         }
      });
   }

   this.logout = function() {
      delete $window.localStorage['currentUser'];
      $http.defaults.headers.common.Authorization = '';
      this.isAuth = false;
   }
}]);


/**
 * Controleur de l'authentification
 */
adminApp.controller('loginCtrl', [ '$scope', '$http', '$location', 'auth',
   function($scope, $http, $location, auth) {
      $scope.msg = false;

      $scope.sendAuth = function() {
         auth.login($scope.login, $scope.pass, function(res) {
            if (res) {
               $scope.msg = false;
               $location.path('/admin');
            }
            else {
               $scope.msg = "Bad login or password";
            }
         });
      };
   }
]);

/**
 * Contrôleur de l'application
 */
adminApp.controller('adminCtrl', [ '$scope', '$http', '$location', '$window', 'auth',
   function($scope, $http, $location, $window, auth) {
      $scope.login = $window.localStorage['currentUser'];
      $scope.supplier = false;
      $scope.format = "png";

      var urlBase = '/admin/';

      $scope.sendImg = function() {
         var form = document.getElementById("img-form");
         var params = new FormData(form);

         $http.post(urlBase + "img", params, {headers: {'Content-Type': undefined}}).
         then(function onSuccess(response) {
            let data = response.data;

            if (data.err) {
               $scope.msg = 'Error: ' + data.err;
            } else {
               $scope.msg = data.msg;
            }
         });
      };

      $scope.doBackup = function() {
         var params = 'backupname=' + encodeURIComponent($scope.backupname);

         $http.post(urlBase + "backup", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            if (data.err) {
               $scope.msg = 'Error: ' + data.err;
            } else {
               $scope.msg = data.msg;
            }
         });
      };

      $scope.doSearch = function() {
         var xml = '<?xml version="1.0"?><searchForm><val>' + $scope.suppliername + '</val></searchForm>';
         var params = 'param=' + encodeURIComponent(xml);

         $http.post(urlBase + "suppliers", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            $scope.search = $scope.suppliername;
            $scope.supplier = true;
            $scope.searchres = data.msg;
            $scope.msg = "";
         });
      };

      $scope.doCheckEmail = function() {
         var params = 'email=' + encodeURIComponent($scope.email);
         $scope.emailres = "Analyse en cours...";

         $http.post(urlBase + "email", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            if (data.err) {
               $scope.msg = 'Error: ' + data.err;
            } else {
               $scope.msg = "";
               $scope.emailres = data.msg;
            }
         });
      };

      $scope.logout = function() {
         auth.logout();
         $location.path('/');
      }
   }
]);
