/**
 * COOOLa - クラウド型倉庫管理システム
 * メインアプリケーションモジュール
 */
angular.module('cooolaApp', [
    'ngRoute',
    'ngSanitize',
    'ngAnimate',
    'ngMessages',
    'ngCookies',
    'toastr',
    'angular-loading-bar',
    'chart.js'
])
.config(['$routeProvider', '$locationProvider', 'toastrConfig', function($routeProvider, $locationProvider, toastrConfig) {
    
    // ルート設定
    $routeProvider
        .when('/', {
            redirectTo: '/dashboard'
        })
        .when('/dashboard', {
            templateUrl: 'views/dashboard.html',
            controller: 'DashboardController'
        })
        .when('/inventory/list', {
            templateUrl: 'views/inventory/list.html',
            controller: 'InventoryController'
        })
        .when('/inventory/movements', {
            templateUrl: 'views/inventory/movements.html',
            controller: 'InventoryController'
        })
        .when('/inventory/counts', {
            templateUrl: 'views/inventory/counts.html',
            controller: 'InventoryController'
        })
        .when('/orders/inbound', {
            templateUrl: 'views/orders/inbound.html',
            controller: 'OrdersController'
        })
        .when('/orders/outbound', {
            templateUrl: 'views/orders/outbound.html',
            controller: 'OrdersController'
        })
        .when('/masters/products', {
            templateUrl: 'views/masters/products.html',
            controller: 'MastersController'
        })
        .when('/masters/warehouses', {
            templateUrl: 'views/masters/warehouses.html',
            controller: 'MastersController'
        })
        .when('/masters/locations', {
            templateUrl: 'views/masters/locations.html',
            controller: 'MastersController'
        })
        .when('/masters/categories', {
            templateUrl: 'views/masters/categories.html',
            controller: 'MastersController'
        })
        .when('/reports', {
            templateUrl: 'views/reports.html',
            controller: 'ReportsController'
        })
        .when('/profile', {
            templateUrl: 'views/profile.html',
            controller: 'MainController'
        })
        .when('/settings', {
            templateUrl: 'views/settings.html',
            controller: 'MainController'
        })
        .otherwise({
            redirectTo: '/dashboard'
        });
    
    // HTML5モードを有効化
    $locationProvider.html5Mode(false);
    
    // Toastr設定
    angular.extend(toastrConfig, {
        autoDismiss: false,
        containerId: 'toast-container',
        maxOpened: 0,
        newestOnTop: true,
        positionClass: 'toast-top-right',
        preventDuplicates: false,
        preventOpenDuplicates: false,
        target: 'body',
        timeOut: 5000,
        extendedTimeOut: 2000,
        allowHtml: true,
        closeButton: true,
        closeHtml: '<button>&times;</button>',
        progressBar: true,
        progressAnimation: 'decreasing',
        tapToDismiss: true,
        onHidden: null,
        onShown: null,
        onClick: null
    });
    
}])
.run(['$rootScope', 'AuthService', '$location', function($rootScope, AuthService, $location) {
    
    // アプリケーション初期化
    $rootScope.appName = 'COOOLa';
    $rootScope.appVersion = '1.0.0';
    
    // 認証チェック
    $rootScope.$on('$routeChangeStart', function(event, next, current) {
        if (!AuthService.isAuthenticated() && next.originalPath !== '/login') {
            $location.path('/login');
        }
    });
    
    // グローバルエラーハンドリング
    $rootScope.$on('$routeChangeError', function(event, current, previous, rejection) {
        console.error('Route change error:', rejection);
    });
    
}])
.constant('API_BASE_URL', 'http://localhost:8080/api')
.constant('APP_CONFIG', {
    itemsPerPage: 20,
    dateFormat: 'yyyy-MM-dd',
    datetimeFormat: 'yyyy-MM-dd HH:mm:ss',
    currency: 'JPY',
    locale: 'ja-JP'
}); 