/**
 * 商品管理コントローラー
 */
angular.module('cooolaApp')
.controller('ProductController', ['$scope', '$http', 'toastr', 'ProductService', function($scope, $http, toastr, ProductService) {
    
    $scope.products = [];
    $scope.categories = [];
    $scope.currentPage = 0;
    $scope.pageSize = 20;
    $scope.totalItems = 0;
    $scope.loading = false;
    $scope.searchKeyword = '';
    
    // 新規商品フォーム
    $scope.newProduct = {
        sku: '',
        name: '',
        description: '',
        categoryId: null,
        unitOfMeasure: '',
        weight: null,
        dimensions: '',
        barcode: ''
    };
    
    // 編集用商品
    $scope.editingProduct = null;
    
    /**
     * 商品一覧を取得
     */
    $scope.loadProducts = function(page = 0) {
        $scope.loading = true;
        
        ProductService.getProducts(page, $scope.pageSize)
            .then(function(response) {
                $scope.products = response.data.content;
                $scope.totalItems = response.data.totalElements;
                $scope.currentPage = response.data.number;
                $scope.loading = false;
            })
            .catch(function(error) {
                console.error('Error loading products:', error);
                toastr.error('商品一覧の取得に失敗しました');
                $scope.loading = false;
            });
    };
    
    /**
     * カテゴリ一覧を取得
     */
    $scope.loadCategories = function() {
        $http.get('/api/categories')
            .then(function(response) {
                $scope.categories = response.data;
            })
            .catch(function(error) {
                console.error('Error loading categories:', error);
                toastr.error('カテゴリ一覧の取得に失敗しました');
            });
    };
    
    /**
     * 商品を検索
     */
    $scope.searchProducts = function() {
        if ($scope.searchKeyword.trim()) {
            ProductService.searchProducts($scope.searchKeyword)
                .then(function(response) {
                    $scope.products = response.data;
                    $scope.totalItems = response.data.length;
                })
                .catch(function(error) {
                    console.error('Error searching products:', error);
                    toastr.error('商品検索に失敗しました');
                });
        } else {
            $scope.loadProducts();
        }
    };
    
    /**
     * 新規商品を作成
     */
    $scope.createProduct = function() {
        if (!$scope.validateProduct($scope.newProduct)) {
            return;
        }
        
        ProductService.createProduct($scope.newProduct)
            .then(function(response) {
                toastr.success('商品を作成しました');
                $scope.resetNewProductForm();
                $scope.loadProducts();
            })
            .catch(function(error) {
                console.error('Error creating product:', error);
                toastr.error('商品の作成に失敗しました');
            });
    };
    
    /**
     * 商品を編集開始
     */
    $scope.startEdit = function(product) {
        $scope.editingProduct = angular.copy(product);
    };
    
    /**
     * 商品を更新
     */
    $scope.updateProduct = function() {
        if (!$scope.validateProduct($scope.editingProduct)) {
            return;
        }
        
        ProductService.updateProduct($scope.editingProduct.id, $scope.editingProduct)
            .then(function(response) {
                toastr.success('商品を更新しました');
                $scope.cancelEdit();
                $scope.loadProducts();
            })
            .catch(function(error) {
                console.error('Error updating product:', error);
                toastr.error('商品の更新に失敗しました');
            });
    };
    
    /**
     * 編集をキャンセル
     */
    $scope.cancelEdit = function() {
        $scope.editingProduct = null;
    };
    
    /**
     * 商品を削除
     */
    $scope.deleteProduct = function(product) {
        if (confirm('この商品を削除しますか？')) {
            ProductService.deleteProduct(product.id)
                .then(function() {
                    toastr.success('商品を削除しました');
                    $scope.loadProducts();
                })
                .catch(function(error) {
                    console.error('Error deleting product:', error);
                    toastr.error('商品の削除に失敗しました');
                });
        }
    };
    
    /**
     * 商品フォームのバリデーション
     */
    $scope.validateProduct = function(product) {
        if (!product.sku || product.sku.trim() === '') {
            toastr.error('SKUを入力してください');
            return false;
        }
        
        if (!product.name || product.name.trim() === '') {
            toastr.error('商品名を入力してください');
            return false;
        }
        
        if (!product.unitOfMeasure || product.unitOfMeasure.trim() === '') {
            toastr.error('単位を入力してください');
            return false;
        }
        
        return true;
    };
    
    /**
     * 新規商品フォームをリセット
     */
    $scope.resetNewProductForm = function() {
        $scope.newProduct = {
            sku: '',
            name: '',
            description: '',
            categoryId: null,
            unitOfMeasure: '',
            weight: null,
            dimensions: '',
            barcode: ''
        };
    };
    
    /**
     * ページ変更
     */
    $scope.changePage = function(page) {
        $scope.loadProducts(page);
    };
    
    /**
     * 初期化
     */
    $scope.init = function() {
        $scope.loadProducts();
        $scope.loadCategories();
    };
    
    // 初期化実行
    $scope.init();
}]); 