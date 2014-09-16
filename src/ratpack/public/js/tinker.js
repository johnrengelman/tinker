var tinkerApp = angular.module('tinkerApp', ['ngRoute'])
  .controller('ContainerListController', function($scope, $http) {
    $scope.containers = [];
    var promise = $http.get('/api/containers');
    promise.success(function(data) {
      $scope.containers = data;
    });
    promise.error(function(data, status) {
      $scope.containers = ['API Error code: ' + status];
    });
  })
  .controller('ContainerShowController', function($scope, $routeParams, $location, $http) {
    $scope.containers = [];
    $scope.container = null;
    $scope.dataBags = [];
    $scope.compareContainer = null;

    var singlePromise = $http.get('/api/containers/' + $routeParams.container);
    singlePromise.success(function(data) {
      $scope.container = data.name;
      $scope.dataBags = data.dataBags;

      var listPromise = $http.get('/api/containers');
      listPromise.success(function(data) {
        $scope.containers = _.transform(_.without(data, $scope.container), function(result, containerName) {
          result.push({
            name: containerName
          });
        });
      });

      listPromise.error(function(data, status) {
        $scope.containers = ['API Error code: ' + status];
      });
    });

    singlePromise.error(function(data, status) {
      $scope.container = 'API Error Code ' + status;
    });

    $scope.compare = function() {
      if ($scope.compareContainer.name != '') {
        $location.path('/containers/' + $scope.container + '/compare/' + $scope.compareContainer.name);
      }
    }
  })
  .controller('CompareController', function($scope, $routeParams, $route, $http) {
    $scope.leftName = $routeParams.container;
    $scope.rightName = $routeParams.compareContainer;
    $scope.dataBag = null;

    var promise = $http.get('/api/containers/' + $scope.leftName + '/compare/' + $scope.rightName);
    promise.success(function(data) {
      $scope.leftData = _.transform(data.diffs, function(rows, diff) {
        switch (diff.type) {
          case 'Extra DataBag':
            rows.push({
              style: 'bg-danger text-danger',
              data: [diff.type],
              colspan: true
            });
            break;

          case 'Missing DataBag':
            rows.push({
              style: 'bg-success text-success',
              data: [diff.name],
              colspan: true
            });
            break;

          case 'Missing ItemKey':
            rows.push({
              style: null,
              data: [diff.dataBagName, diff.itemName, diff.name],
              colspan: false
            });
            break;

          default:
            rows.push({
              style: 'bg-warning text-warning',
              data: ['Unknown diff type' + diff.type],
              colspan: true
            });
        }
      });
      $scope.rightData = _.transform(data.diffs, function(rows, diff) {
        switch (diff.type) {
          case 'Extra DataBag':
            rows.push({
              style: null,
              data: [diff.name],
              colspan: true
            });
            break;

          case 'Missing DataBag':
            rows.push({
              style: 'text-muted',
              data: [diff.type],
              colspan: true
            });
            break;

          case 'Missing ItemKey':
            rows.push({
              style: 'bg-danger text-danger',
              data: [diff.type],
              colspan: true
            });
            break;

          default:
            rows.push({
              style: 'bg-warning text-warning',
              data: ['Unknown diff type:' + diff.type],
              colspan: true
            })
        }
      });
      $scope.matching = data.match;
    });

    $scope.refreshCompare = function() {
      $route.reload();
    }
  })
  .config(function($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'templates/container_list.html',
        controller: 'ContainerListController'
      })
      .when('/containers/:container', {
        templateUrl: 'templates/container_show.html',
        controller: 'ContainerShowController'
      })
      .when('/containers/:container/compare/:compareContainer', {
        templateUrl: 'templates/compare.html',
        controller: 'CompareController'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
