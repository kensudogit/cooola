/**
 * 商品管理サービス
 */
angular.module('cooolaApp')
.service('ProductService', ['$http', 'API_BASE_URL', function($http, API_BASE_URL) {
    
    /**
     * 商品一覧を取得
     */
    this.getProducts = function(page, size) {
        return $http.get(API_BASE_URL + '/products', {
            params: {
                page: page,
                size: size,
                sortBy: 'createdAt',
                sortDir: 'desc'
            }
        });
    };
    
    /**
     * 商品をIDで取得
     */
    this.getProductById = function(id) {
        return $http.get(API_BASE_URL + '/products/' + id);
    };
    
    /**
     * 商品をSKUで取得
     */
    this.getProductBySku = function(sku) {
        return $http.get(API_BASE_URL + '/products/sku/' + sku);
    };
    
    /**
     * 商品をバーコードで取得
     */
    this.getProductByBarcode = function(barcode) {
        return $http.get(API_BASE_URL + '/products/barcode/' + barcode);
    };
    
    /**
     * キーワードで商品を検索
     */
    this.searchProducts = function(keyword) {
        return $http.get(API_BASE_URL + '/products/search', {
            params: { keyword: keyword }
        });
    };
    
    /**
     * カテゴリ別商品一覧を取得
     */
    this.getProductsByCategory = function(categoryId) {
        return $http.get(API_BASE_URL + '/products/category/' + categoryId);
    };
    
    /**
     * 商品を新規作成
     */
    this.createProduct = function(product) {
        return $http.post(API_BASE_URL + '/products', product);
    };
    
    /**
     * 商品を更新
     */
    this.updateProduct = function(id, product) {
        return $http.put(API_BASE_URL + '/products/' + id, product);
    };
    
    /**
     * 商品を削除
     */
    this.deleteProduct = function(id) {
        return $http.delete(API_BASE_URL + '/products/' + id);
    };
    
    /**
     * 商品数を取得
     */
    this.getProductCount = function() {
        return $http.get(API_BASE_URL + '/products/count');
    };
    
    /**
     * アクティブな商品数を取得
     */
    this.getActiveProductCount = function() {
        return $http.get(API_BASE_URL + '/products/count/active');
    };
    
    /**
     * カテゴリ別商品数を取得
     */
    this.getProductCountByCategory = function(categoryId) {
        return $http.get(API_BASE_URL + '/products/count/category/' + categoryId);
    };
}]); 