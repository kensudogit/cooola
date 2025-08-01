/**
 * COOOLa - クラウド型倉庫管理システム
 * メインアプリケーションモジュール
 * AngularJS 1.8.x を使用したSPA（Single Page Application）
 */
angular.module('cooolaApp', [
    'ngRoute',           // ルーティング機能
    'ngSanitize',        // HTMLサニタイゼーション
    'ngAnimate',         // アニメーション機能
    'ngMessages',        // メッセージ表示機能
    'ngCookies',         // Cookie管理機能
    'toastr',            // トースト通知
    'angular-loading-bar', // ローディングバー
    'chart.js'           // チャート描画機能
])
.config(['$routeProvider', '$locationProvider', 'toastrConfig', function($routeProvider, $locationProvider, toastrConfig) {
    
    // ========================================
    // ルート設定
    // ========================================
    // AngularJSのngRouteを使用したSPAルーティング設定
    $routeProvider
        // ルートパスをダッシュボードにリダイレクト
        .when('/', {
            redirectTo: '/dashboard'
        })
        // ダッシュボード画面
        .when('/dashboard', {
            templateUrl: 'views/dashboard.html',
            controller: 'DashboardController'
        })
        // 在庫管理関連ルート
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
        // 入出庫管理関連ルート
        .when('/orders/inbound', {
            templateUrl: 'views/orders/inbound.html',
            controller: 'OrdersController'
        })
        .when('/orders/outbound', {
            templateUrl: 'views/orders/outbound.html',
            controller: 'OrdersController'
        })
        // マスタ管理関連ルート
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
        // レポート画面
        .when('/reports', {
            templateUrl: 'views/reports.html',
            controller: 'ReportsController'
        })
        // ユーザー関連ルート
        .when('/profile', {
            templateUrl: 'views/profile.html',
            controller: 'MainController'
        })
        .when('/settings', {
            templateUrl: 'views/settings.html',
            controller: 'MainController'
        })
        // デフォルトルート（存在しないパスはダッシュボードにリダイレクト）
        .otherwise({
            redirectTo: '/dashboard'
        });
    
    // ========================================
    // ロケーション設定
    // ========================================
    // HTML5モードを無効化（ハッシュベースルーティングを使用）
    $locationProvider.html5Mode(false);
    
    // ========================================
    // Toastr設定（トースト通知）
    // ========================================
    // トースト通知の表示設定をカスタマイズ
    angular.extend(toastrConfig, {
        autoDismiss: false,              // 自動非表示無効
        containerId: 'toast-container',  // コンテナID
        maxOpened: 0,                    // 最大表示数（0=制限なし）
        newestOnTop: true,               // 新しい通知を上に表示
        positionClass: 'toast-top-right', // 表示位置（右上）
        preventDuplicates: false,        // 重複防止無効
        preventOpenDuplicates: false,    // 開いている重複防止無効
        target: 'body',                  // 表示対象要素
        timeOut: 5000,                   // 自動非表示時間（5秒）
        extendedTimeOut: 2000,           // マウスオーバー時の延長時間（2秒）
        allowHtml: true,                 // HTML許可
        closeButton: true,               // 閉じるボタン表示
        closeHtml: '<button>&times;</button>', // 閉じるボタンのHTML
        progressBar: true,               // プログレスバー表示
        progressAnimation: 'decreasing', // プログレスバーアニメーション
        tapToDismiss: true,              // タップで非表示
        onHidden: null,                  // 非表示時のコールバック
        onShown: null,                   // 表示時のコールバック
        onClick: null                    // クリック時のコールバック
    });
    
}])
.run(['$rootScope', 'AuthService', '$location', function($rootScope, AuthService, $location) {
    
    // ========================================
    // アプリケーション初期化
    // ========================================
    // グローバル変数の設定
    $rootScope.appName = 'COOOLa';      // アプリケーション名
    $rootScope.appVersion = '1.0.0';    // アプリケーションバージョン
    
    // ========================================
    // 認証チェック
    // ========================================
    // ルート変更開始時の認証チェック
    $rootScope.$on('$routeChangeStart', function(event, next, current) {
        // 認証されていない場合で、ログインページ以外にアクセスしようとした場合
        if (!AuthService.isAuthenticated() && next.originalPath !== '/login') {
            $location.path('/login');  // ログインページにリダイレクト
        }
    });
    
    // ========================================
    // グローバルエラーハンドリング
    // ========================================
    // ルート変更エラー時の処理
    $rootScope.$on('$routeChangeError', function(event, current, previous, rejection) {
        console.error('Route change error:', rejection);  // エラーをコンソールに出力
    });
    
}])
// ========================================
// 定数定義
// ========================================
// APIベースURL（バックエンドサーバーのエンドポイント）
.constant('API_BASE_URL', 'http://localhost:8080/api')
// アプリケーション設定
.constant('APP_CONFIG', {
    itemsPerPage: 20,                    // 1ページあたりの表示件数
    dateFormat: 'yyyy-MM-dd',            // 日付フォーマット
    datetimeFormat: 'yyyy-MM-dd HH:mm:ss', // 日時フォーマット
    currency: 'JPY',                     // 通貨（日本円）
    locale: 'ja-JP'                      // ロケール（日本語）
}); 