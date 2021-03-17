'use strict';

/**
 * Déclaration de l'application
 */
var dataApp = angular.module('dataApp', [ ]);


/**
 * Contrôleur de l'application
 */
dataApp.controller('dataCtrl', [ '$scope', '$http',
   function($scope, $http) {
      $scope.viewhashres = false;
      var urlBase = '/data';

      $scope.sendPassword = function() {
         var params = 'pass=' + encodeURIComponent($scope.password);
         if ($scope.salt)
            params += '&salt=' + encodeURIComponent($scope.salt);

         $http.post(urlBase + "/gethashes", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            $scope.pass = $scope.password;
            $scope.md5res = data.md5;
            $scope.sha1res = data.sha1;
            $scope.sha256res = data.sha256;
            $scope.sha512res = data.sha512;
            $scope.realsalt = data.salt;
            $scope.freebsdmd5res = data.freebsdmd5;
            $scope.saltedsha256res = data.saltedsha256;
            $scope.saltedsha512res = data.saltedsha512;
            $scope.viewhashres = true;
         });
      };

      $scope.base64decode = function() {
         var params = 'src=' + encodeURIComponent($scope.base64encoded);

         $http.post(urlBase + "/base64decode", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            $scope.base64decodedhexres = data.base64decodedhex;
            $scope.base64decodedasciires = data.base64decodedascii;
            $scope.viewbase64decoded = true;
         });
      };

      $scope.base64encodeAscii = function() {
         var params = 'src=' + encodeURIComponent($scope.asciitoencode);

         $http.post(urlBase + "/base64encodeascii", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            $scope.base64encodedres = data.base64encoded;
            $scope.viewbase64encoded = true;
         });
      };

      $scope.base64encodeHex = function() {
         var params = 'src=' + encodeURIComponent($scope.hextoencode);

         $http.post(urlBase + "/base64encodehex", params, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).
         then(function onSuccess(response) {
            let data = response.data;

            $scope.base64encodedres = data.base64encoded;
            $scope.viewbase64encoded = true;
         });
      }
   }
]);
