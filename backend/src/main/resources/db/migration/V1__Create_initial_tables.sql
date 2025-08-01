-- COOOLa 倉庫管理システム 初期テーブル作成

-- ユーザーテーブル
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('ADMIN', 'MANAGER', 'OPERATOR') NOT NULL DEFAULT 'OPERATOR',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- 倉庫テーブル
CREATE TABLE warehouses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    address TEXT,
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    contact_email VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code)
);

-- 商品カテゴリテーブル
CREATE TABLE product_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES product_categories(id),
    INDEX idx_parent_id (parent_id)
);

-- 商品テーブル
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    unit_of_measure VARCHAR(20) NOT NULL,
    weight DECIMAL(10,3),
    dimensions VARCHAR(50),
    barcode VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES product_categories(id),
    INDEX idx_sku (sku),
    INDEX idx_category_id (category_id),
    INDEX idx_barcode (barcode)
);

-- ロケーション（棚）テーブル
CREATE TABLE locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    location_type ENUM('AISLE', 'RACK', 'SHELF', 'BIN') NOT NULL,
    parent_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (parent_id) REFERENCES locations(id),
    UNIQUE KEY uk_warehouse_code (warehouse_id, code),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_parent_id (parent_id)
);

-- 在庫テーブル
CREATE TABLE inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT,
    quantity DECIMAL(10,3) NOT NULL DEFAULT 0,
    reserved_quantity DECIMAL(10,3) NOT NULL DEFAULT 0,
    available_quantity DECIMAL(10,3) NOT NULL DEFAULT 0,
    unit_cost DECIMAL(10,2),
    lot_number VARCHAR(50),
    expiry_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (location_id) REFERENCES locations(id),
    UNIQUE KEY uk_product_warehouse_location (product_id, warehouse_id, location_id),
    INDEX idx_product_id (product_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_location_id (location_id),
    INDEX idx_lot_number (lot_number),
    INDEX idx_expiry_date (expiry_date)
);

-- 入庫テーブル
CREATE TABLE inbound_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    warehouse_id BIGINT NOT NULL,
    supplier_name VARCHAR(100),
    supplier_order_number VARCHAR(50),
    expected_arrival_date DATE,
    status ENUM('PENDING', 'IN_TRANSIT', 'ARRIVED', 'RECEIVED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_order_number (order_number),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_expected_arrival_date (expected_arrival_date)
);

-- 入庫明細テーブル
CREATE TABLE inbound_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inbound_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    expected_quantity DECIMAL(10,3) NOT NULL,
    received_quantity DECIMAL(10,3) DEFAULT 0,
    unit_cost DECIMAL(10,2),
    lot_number VARCHAR(50),
    expiry_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (inbound_order_id) REFERENCES inbound_orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_inbound_order_id (inbound_order_id),
    INDEX idx_product_id (product_id)
);

-- 出庫テーブル
CREATE TABLE outbound_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    warehouse_id BIGINT NOT NULL,
    customer_name VARCHAR(100),
    customer_order_number VARCHAR(50),
    required_date DATE,
    status ENUM('PENDING', 'PICKING', 'PICKED', 'SHIPPED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    priority ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT') NOT NULL DEFAULT 'NORMAL',
    notes TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_order_number (order_number),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_required_date (required_date),
    INDEX idx_priority (priority)
);

-- 出庫明細テーブル
CREATE TABLE outbound_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    outbound_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    requested_quantity DECIMAL(10,3) NOT NULL,
    picked_quantity DECIMAL(10,3) DEFAULT 0,
    shipped_quantity DECIMAL(10,3) DEFAULT 0,
    unit_price DECIMAL(10,2),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (outbound_order_id) REFERENCES outbound_orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_outbound_order_id (outbound_order_id),
    INDEX idx_product_id (product_id)
);

-- 棚卸しテーブル
CREATE TABLE inventory_counts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    count_number VARCHAR(50) NOT NULL UNIQUE,
    count_date DATE NOT NULL,
    status ENUM('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PLANNED',
    notes TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_count_number (count_number),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_count_date (count_date)
);

-- 棚卸し明細テーブル
CREATE TABLE inventory_count_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inventory_count_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    location_id BIGINT,
    expected_quantity DECIMAL(10,3) NOT NULL DEFAULT 0,
    counted_quantity DECIMAL(10,3),
    variance DECIMAL(10,3),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (inventory_count_id) REFERENCES inventory_counts(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (location_id) REFERENCES locations(id),
    INDEX idx_inventory_count_id (inventory_count_id),
    INDEX idx_product_id (product_id),
    INDEX idx_location_id (location_id)
);

-- 在庫移動テーブル
CREATE TABLE inventory_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movement_number VARCHAR(50) NOT NULL UNIQUE,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    from_location_id BIGINT,
    to_location_id BIGINT,
    quantity DECIMAL(10,3) NOT NULL,
    movement_type ENUM('TRANSFER', 'ADJUSTMENT', 'DAMAGE', 'LOSS') NOT NULL,
    reason TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (from_location_id) REFERENCES locations(id),
    FOREIGN KEY (to_location_id) REFERENCES locations(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_movement_number (movement_number),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_product_id (product_id),
    INDEX idx_movement_type (movement_type),
    INDEX idx_created_at (created_at)
);

-- システム設定テーブル
CREATE TABLE system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_setting_key (setting_key)
);

-- 初期データ挿入
INSERT INTO users (username, email, password, first_name, last_name, role) VALUES
('admin', 'admin@cooola.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Admin', 'User', 'ADMIN');

INSERT INTO warehouses (name, code, address, contact_person, contact_phone, contact_email) VALUES
('メイン倉庫', 'WH001', '東京都渋谷区1-1-1', '田中太郎', '03-1234-5678', 'warehouse@cooola.com'),
('サブ倉庫', 'WH002', '大阪府大阪市2-2-2', '佐藤花子', '06-8765-4321', 'warehouse2@cooola.com');

INSERT INTO product_categories (name, description) VALUES
('電子機器', 'スマートフォン、タブレット、PC等'),
('衣類', 'Tシャツ、ジーンズ、靴等'),
('食品', '生鮮食品、加工食品等'),
('書籍', '小説、ビジネス書、雑誌等');

INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('company_name', 'COOOLa株式会社', '会社名'),
('warehouse_auto_numbering', 'true', '倉庫番号の自動採番'),
('inventory_low_stock_threshold', '10', '在庫不足警告の閾値'),
('barcode_prefix', 'COOOLA', 'バーコードのプレフィックス'); 