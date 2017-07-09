(function () {
'use strict';

angular.module('PalindromeGameApp', [])
.controller('PalindromeGameController', PalindromeGameController)
.service('SearchService', SearchService);

PalindromeGameController.$inject = ['SearchService'];
function PalindromeGameController(SearchService) {
    var ctrl = this;
    ctrl.userName = '';
    ctrl.phrase = '';
    ctrl.score = '';
    ctrl.error = "";
    ctrl.top = [];

    ctrl.checkPhrase = function (userName, phrase) {
        var promise = SearchService.checkPhrase(userName, phrase);
        promise.then(function (response) {
            if(response.score !== undefined)
                ctrl.score = response.score;
            ctrl.error = response.error;
        });

    };
    
    ctrl.getTop = function () {
        var promise = SearchService.getTop();
        promise.then(function (response) {
            if(response.top !== undefined)
                ctrl.top = response.top;
            else
                ctrl.top = [];
        });

    };
}

SearchService.$inject = ['$http'];
function SearchService($http) {
  var service = this;
  
  service.getTop = function() {
    var result = $http({
      method: "GET",
      url: "servlet"
    })
    .then( function(response) {
      return response.data;
    });
    return result;
  };
  service.checkPhrase = function(userName, phrase) {
    var result = $http({
      method: "POST",
      url: "servlet",
      headers: { 'Content-Type': 'application/json' },
      data: {userName: userName, phrase: phrase}
    })
    .then( function(response) {
      return response.data;
    });
    return result;
  };
}

})();
