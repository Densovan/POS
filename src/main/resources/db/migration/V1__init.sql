CREATE TABLE tax_classes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL, -- 'Standard', 'Reduced', 'Zero-Rated', 'Exempt'
    rate DECIMAL(5,2) NOT NULL, -- 10.00 for 10%
    description TEXT,
    is_default BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE brands (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE,
    logo_url TEXT,
    website_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE units (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL, -- 'Piece', 'Kg', 'Liter'
    abbreviation VARCHAR(10), -- 'pc', 'kg', 'L'
    is_base_unit BOOLEAN DEFAULT true,
    conversion_factor DECIMAL(10,4) DEFAULT 1, -- For base unit = 1
    base_unit_id UUID REFERENCES units(id), -- For conversion hierarchy
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    parent_id UUID REFERENCES categories(id),
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    image_url TEXT,
    tax_class_id UUID REFERENCES tax_classes(id),
    is_active BOOLEAN DEFAULT true,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) UNIQUE NOT NULL, -- 'admin', 'manager', 'cashier', 'owner'
    description TEXT,
    is_system_role BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    module VARCHAR(50) NOT NULL, -- 'pos', 'products', 'inventory', 'reports', etc.
    action VARCHAR(50) NOT NULL, -- 'create', 'read', 'update', 'delete', 'export'
    description TEXT,
    UNIQUE(module, action)
);

CREATE TABLE suppliers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    tax_id VARCHAR(100),
    
    -- Contact info
    contact_person VARCHAR(255),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    address TEXT,
    
    -- Banking
    bank_name VARCHAR(255),
    bank_account_number VARCHAR(100),
    bank_account_name VARCHAR(255),
    
    -- Payment terms
    payment_terms VARCHAR(50), -- 'net_30', 'net_60', 'cash_on_delivery'
    credit_limit DECIMAL(12,2),
    
    -- Performance
    outstanding_balance DECIMAL(12,2) DEFAULT 0,
    total_purchases DECIMAL(12,2) DEFAULT 0,
    average_delivery_days INTEGER,
    
    notes TEXT,
    is_active BOOLEAN DEFAULT true,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20) UNIQUE NOT NULL,
    
    -- Address
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'Cambodia',
    
    -- Loyalty
    loyalty_points INTEGER DEFAULT 0,
    loyalty_tier VARCHAR(20) DEFAULT 'bronze', -- 'bronze', 'silver', 'gold', 'platinum'
    total_spent DECIMAL(12,2) DEFAULT 0,
    total_orders INTEGER DEFAULT 0,
    last_purchase_date TIMESTAMP,
    
    -- Marketing
    birth_date DATE,
    anniversary_date DATE,
    is_subscribed BOOLEAN DEFAULT true,
    
    -- Tags
    tags TEXT[], -- ['vip', 'wholesale', 'corporate']
    
    -- Notes
    notes TEXT,
    
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expense_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employees (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255),
    position VARCHAR(100),
    hourly_rate DECIMAL(10,2),
    salary DECIMAL(12,2),
    hire_date DATE,
    termination_date DATE,
    emergency_contact_name VARCHAR(255),
    emergency_contact_phone VARCHAR(20),
    profile_image_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID REFERENCES employees(id),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    two_factor_secret VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    last_login_at TIMESTAMP,
    login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP,
    refresh_token VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- deferred: employees <-> users circular reference
ALTER TABLE employees ADD COLUMN user_id UUID REFERENCES users(id);

CREATE TABLE warehouses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(255),
    manager_id UUID REFERENCES employees(id),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shifts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID REFERENCES employees(id),
    shift_date DATE NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    expected_end_time TIMESTAMP,
    cash_start_amount DECIMAL(10,2) DEFAULT 0,
    cash_end_amount DECIMAL(10,2),
    cash_expected_amount DECIMAL(10,2),
    cash_difference DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'active', -- 'active', 'closed', 'cancelled'
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attendance (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID REFERENCES employees(id),
    clock_in TIMESTAMP NOT NULL,
    clock_out TIMESTAMP,
    break_start TIMESTAMP,
    break_end TIMESTAMP,
    total_hours DECIMAL(5,2),
    status VARCHAR(20) DEFAULT 'present', -- 'present', 'absent', 'late', 'half-day'
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE role_permissions (
    role_id UUID REFERENCES roles(id) ON DELETE CASCADE,
    permission_id UUID REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,
    barcode VARCHAR(100),
    category_id UUID REFERENCES categories(id),
    brand_id UUID REFERENCES brands(id),
    unit_id UUID REFERENCES units(id),
    tax_class_id UUID REFERENCES tax_classes(id),
    
    -- Pricing
    cost_price DECIMAL(10,2) NOT NULL,
    selling_price DECIMAL(10,2) NOT NULL,
    wholesale_price DECIMAL(10,2),
    profit_margin DECIMAL(5,2) GENERATED ALWAYS AS 
        ((selling_price - cost_price) / selling_price * 100) STORED,
    
    -- Inventory defaults
    min_stock_level INTEGER DEFAULT 0,
    max_stock_level INTEGER,
    reorder_point INTEGER DEFAULT 0,
    reorder_quantity INTEGER,
    
    -- Product details
    description TEXT,
    short_description VARCHAR(500),
    weight DECIMAL(8,2),
    weight_unit VARCHAR(10), -- 'kg', 'g', 'lb'
    dimensions VARCHAR(100), -- 'LxWxH'
    
    -- Flags
    is_variant_parent BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    is_track_stock BOOLEAN DEFAULT true,
    is_serialized BOOLEAN DEFAULT false, -- For electronics
    has_batches BOOLEAN DEFAULT false, -- For expiry tracking
    
    -- SEO & Media
    main_image_url TEXT,
    meta_title VARCHAR(255),
    meta_description TEXT,
    meta_keywords TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_prices CHECK (cost_price >= 0 AND selling_price >= 0)
);

CREATE TABLE product_variants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    sku VARCHAR(100) UNIQUE NOT NULL,
    barcode VARCHAR(100),
    
    -- Variant attributes (JSON for flexibility)
    attributes JSONB NOT NULL, -- {"color": "Black", "size": "M"}
    
    -- Pricing (override parent if different)
    cost_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    
    -- Inventory (override parent)
    min_stock_level INTEGER,
    reorder_point INTEGER,
    
    -- Variant-specific
    image_url TEXT,
    is_active BOOLEAN DEFAULT true,
    is_track_stock BOOLEAN DEFAULT true,
    is_serialized BOOLEAN DEFAULT false,
    has_batches BOOLEAN DEFAULT false,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT variant_prices_check CHECK (
        (selling_price IS NULL OR selling_price >= 0) AND 
        (cost_price IS NULL OR cost_price >= 0)
    )
);

CREATE TABLE product_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    variant_id UUID REFERENCES product_variants(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT false,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shop_info (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(255),
    website_url TEXT,
    logo_url TEXT,
    tax_id VARCHAR(100),
    
    -- Default settings
    currency_symbol VARCHAR(10) DEFAULT '$',
    currency_code VARCHAR(3) DEFAULT 'USD',
    timezone VARCHAR(50) DEFAULT 'Asia/Phnom_Penh',
    date_format VARCHAR(20) DEFAULT 'MM/DD/YYYY',
    
    -- POS defaults
    default_payment_method VARCHAR(30) DEFAULT 'cash',
    default_tax_class_id UUID REFERENCES tax_classes(id),
    
    -- Receipt
    receipt_header TEXT,
    receipt_footer TEXT,
    receipt_thank_you TEXT,
    
    -- Hardware
    receipt_printer_type VARCHAR(30), -- 'thermal', 'standard'
    receipt_printer_ip VARCHAR(50),
    receipt_printer_port INTEGER,
    kitchen_printer_ip VARCHAR(50),
    kitchen_printer_port INTEGER,
    cash_drawer_port VARCHAR(50),
    barcode_scanner_type VARCHAR(30), -- 'usb', 'bluetooth'
    
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) UNIQUE,
    
    -- Email notifications
    email_low_stock BOOLEAN DEFAULT true,
    email_out_of_stock BOOLEAN DEFAULT true,
    email_expiring_products BOOLEAN DEFAULT true,
    email_daily_summary BOOLEAN DEFAULT true,
    email_new_purchase BOOLEAN DEFAULT false,
    email_new_sale BOOLEAN DEFAULT false,
    
    -- In-app notifications
    in_app_low_stock BOOLEAN DEFAULT true,
    in_app_out_of_stock BOOLEAN DEFAULT true,
    in_app_expiring_products BOOLEAN DEFAULT true,
    in_app_daily_summary BOOLEAN DEFAULT false,
    
    -- SMS notifications
    sms_low_stock BOOLEAN DEFAULT false,
    sms_out_of_stock BOOLEAN DEFAULT false,
    
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    action VARCHAR(50) NOT NULL, -- 'login', 'logout', 'create', 'update', 'delete', 'export'
    module VARCHAR(50) NOT NULL,
    record_id UUID,
    old_data JSONB,
    new_data JSONB,
    ip_address INET,
    user_agent TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE system_backups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    backup_type VARCHAR(20) NOT NULL, -- 'full', 'database', 'files'
    file_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_size INTEGER,
    backup_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),
    restored_at TIMESTAMP,
    restored_by UUID REFERENCES users(id),
    notes TEXT
);

CREATE TABLE customer_addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
    
    address_type VARCHAR(20) DEFAULT 'home', -- 'home', 'work', 'other'
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'Cambodia',
    
    is_default BOOLEAN DEFAULT false,
    delivery_instructions TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    po_number VARCHAR(50) UNIQUE NOT NULL,
    supplier_id UUID REFERENCES suppliers(id),
    
    order_date DATE NOT NULL,
    expected_delivery_date DATE,
    actual_delivery_date DATE,
    
    -- Status
    status VARCHAR(20) DEFAULT 'draft', -- 'draft', 'sent', 'confirmed', 'received', 'cancelled'
    
    -- Financials
    subtotal DECIMAL(12,2) NOT NULL,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    shipping_cost DECIMAL(12,2) DEFAULT 0,
    total DECIMAL(12,2) NOT NULL,
    
    -- Notes
    supplier_notes TEXT,
    internal_notes TEXT,
    
    created_by UUID REFERENCES users(id),
    approved_by UUID REFERENCES users(id),
    approved_at TIMESTAMP,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_order_id UUID REFERENCES purchase_orders(id) ON DELETE CASCADE,
    
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    
    ordered_quantity INTEGER NOT NULL,
    received_quantity INTEGER DEFAULT 0,
    unit_cost DECIMAL(12,2) NOT NULL,
    total_cost DECIMAL(12,2) GENERATED ALWAYS AS (ordered_quantity * unit_cost) STORED,
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_receipts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_order_id UUID REFERENCES purchase_orders(id),
    receipt_number VARCHAR(50) UNIQUE NOT NULL,
    
    receipt_date DATE NOT NULL,
    supplier_id UUID REFERENCES suppliers(id),
    supplier_invoice_number VARCHAR(100),
    
    -- Received items
    received_by UUID REFERENCES users(id),
    inspected_by UUID REFERENCES users(id),
    
    -- Status
    status VARCHAR(20) DEFAULT 'draft', -- 'draft', 'confirmed', 'cancelled'
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_receipt_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_receipt_id UUID REFERENCES purchase_receipts(id) ON DELETE CASCADE,
    purchase_order_item_id UUID REFERENCES purchase_order_items(id),
    
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    
    received_quantity INTEGER NOT NULL,
    accepted_quantity INTEGER NOT NULL,
    rejected_quantity INTEGER DEFAULT 0,
    rejection_reason VARCHAR(255),
    
    unit_cost DECIMAL(12,2) NOT NULL,
    total_cost DECIMAL(12,2) GENERATED ALWAYS AS (accepted_quantity * unit_cost) STORED,
    
    batch_number VARCHAR(100),
    expiry_date DATE,
    serial_numbers TEXT[], -- Array of serial numbers
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE supplier_payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_id UUID REFERENCES suppliers(id),
    purchase_order_id UUID REFERENCES purchase_orders(id),
    
    payment_date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL, -- 'cash', 'bank_transfer', 'check'
    amount DECIMAL(12,2) NOT NULL,
    
    reference_number VARCHAR(100),
    bank_name VARCHAR(255),
    check_number VARCHAR(100),
    
    notes TEXT,
    created_by UUID REFERENCES users(id),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expenses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id UUID REFERENCES expense_categories(id),
    
    title VARCHAR(255) NOT NULL,
    description TEXT,
    amount DECIMAL(12,2) NOT NULL,
    
    expense_date DATE NOT NULL,
    due_date DATE,
    payment_date DATE,
    
    payment_method VARCHAR(30), -- 'cash', 'bank_transfer', 'credit_card'
    reference_number VARCHAR(100),
    
    -- Recurring
    is_recurring BOOLEAN DEFAULT false,
    recurrence_pattern VARCHAR(20), -- 'daily', 'weekly', 'monthly', 'yearly'
    recurrence_end_date DATE,
    
    -- Approval
    status VARCHAR(20) DEFAULT 'pending', -- 'pending', 'approved', 'rejected', 'paid'
    approved_by UUID REFERENCES users(id),
    approved_at TIMESTAMP,
    
    notes TEXT,
    created_by UUID REFERENCES users(id),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expense_attachments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    expense_id UUID REFERENCES expenses(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_size INTEGER, -- bytes
    mime_type VARCHAR(100),
    
    uploaded_by UUID REFERENCES users(id),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    
    -- Stock counts
    current_stock INTEGER DEFAULT 0,
    available_stock INTEGER DEFAULT 0, -- Current - Reserved
    reserved_stock INTEGER DEFAULT 0, -- For held orders
    minimum_stock INTEGER DEFAULT 0,
    maximum_stock INTEGER,
    
    -- Location tracking (for multi-store)
    warehouse_id UUID REFERENCES warehouses(id),
    shelf_location VARCHAR(50),
    
    -- Last updated
    last_count_date DATE,
    last_counted_by UUID REFERENCES users(id),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT valid_stock CHECK (current_stock >= 0 AND available_stock >= 0),
    UNIQUE(product_id, variant_id, warehouse_id)
);

CREATE TABLE batches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    inventory_id UUID REFERENCES inventory(id),
    
    batch_number VARCHAR(100) NOT NULL,
    manufacturing_date DATE,
    expiry_date DATE NOT NULL,
    received_quantity INTEGER NOT NULL,
    current_quantity INTEGER NOT NULL,
    reserved_quantity INTEGER DEFAULT 0,
    
    cost_price DECIMAL(10,2) NOT NULL, -- Cost for this specific batch
    
    supplier_id UUID REFERENCES suppliers(id),
    purchase_receipt_id UUID REFERENCES purchase_receipts(id),
    
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT valid_batch_qty CHECK (current_quantity >= 0 AND reserved_quantity >= 0)
);

CREATE TABLE stock_movements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    inventory_id UUID REFERENCES inventory(id),
    batch_id UUID REFERENCES batches(id),
    
    movement_type VARCHAR(20) NOT NULL, -- 'stock_in', 'stock_out', 'adjustment', 'count'
    quantity INTEGER NOT NULL,
    previous_quantity INTEGER,
    new_quantity INTEGER,
    
    reference_type VARCHAR(50), -- 'purchase_receipt', 'sale', 'stock_adjustment', 'stock_count'
    reference_id UUID, -- Foreign key to the source document
    
    notes TEXT,
    performed_by UUID REFERENCES users(id),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_adjustments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    inventory_id UUID REFERENCES inventory(id),
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    
    adjustment_type VARCHAR(20) NOT NULL, -- 'increase', 'decrease'
    quantity INTEGER NOT NULL,
    reason VARCHAR(50) NOT NULL, -- 'damage', 'theft', 'expired', 'counting_error'
    notes TEXT,
    
    approved_by UUID REFERENCES users(id),
    approved_at TIMESTAMP,
    
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_counts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    count_date DATE NOT NULL,
    warehouse_id UUID REFERENCES warehouses(id),
    expected_by UUID REFERENCES users(id),
    counted_by UUID REFERENCES users(id),
    counted_at TIMESTAMP,
    
    status VARCHAR(20) DEFAULT 'draft', -- 'draft', 'in_progress', 'completed', 'cancelled'
    notes TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_count_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    stock_count_id UUID REFERENCES stock_counts(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    inventory_id UUID REFERENCES inventory(id),
    
    expected_quantity INTEGER NOT NULL,
    counted_quantity INTEGER,
    variance INTEGER GENERATED ALWAYS AS (counted_quantity - expected_quantity) STORED,
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sales (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    
    -- Customer info
    customer_id UUID REFERENCES customers(id),
    customer_name VARCHAR(255), -- Denormalized for quick view
    customer_phone VARCHAR(20),
    customer_email VARCHAR(255),
    
    -- Employee info
    cashier_id UUID REFERENCES users(id),
    
    -- Sale details
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sale_type VARCHAR(20) DEFAULT 'retail', -- 'retail', 'wholesale', 'online'
    order_type VARCHAR(20) DEFAULT 'takeaway', -- 'dine-in', 'takeaway', 'delivery'
    
    -- Table/Service info (for restaurants)
    table_number VARCHAR(20),
    service_type VARCHAR(20),
    guest_count INTEGER,
    
    -- Financials
    subtotal DECIMAL(12,2) NOT NULL,
    discount_type VARCHAR(20), -- 'percentage', 'fixed'
    discount_amount DECIMAL(12,2) DEFAULT 0,
    discount_reason TEXT,
    coupon_code VARCHAR(50),
    tax_amount DECIMAL(12,2) DEFAULT 0,
    total DECIMAL(12,2) NOT NULL,
    
    -- Payment status
    payment_status VARCHAR(20) DEFAULT 'pending', -- 'pending', 'paid', 'partially_paid', 'refunded'
    order_status VARCHAR(20) DEFAULT 'completed', -- 'pending', 'processing', 'completed', 'cancelled'
    
    -- Notes
    customer_notes TEXT,
    internal_notes TEXT,
    
    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Soft delete
    is_deleted BOOLEAN DEFAULT false,
    deleted_at TIMESTAMP,
    deleted_by UUID REFERENCES users(id)
);

CREATE TABLE loyalty_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
    sale_id UUID REFERENCES sales(id),
    
    transaction_type VARCHAR(20) NOT NULL, -- 'earned', 'redeemed', 'expired', 'adjusted'
    points INTEGER NOT NULL,
    description TEXT,
    
    balance_before INTEGER NOT NULL,
    balance_after INTEGER NOT NULL,
    
    expiry_date DATE, -- Points may expire after X months
    
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sale_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sale_id UUID REFERENCES sales(id) ON DELETE CASCADE,
    
    -- Product info (denormalized for historical accuracy)
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    product_name VARCHAR(255) NOT NULL,
    variant_attributes JSONB, -- {"color": "Black", "size": "M"}
    sku VARCHAR(100),
    barcode VARCHAR(100),
    
    -- Quantity & Pricing
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    cost_price DECIMAL(12,2) NOT NULL, -- Cost at time of sale
    discount_amount DECIMAL(12,2) DEFAULT 0,
    tax_rate DECIMAL(5,2) DEFAULT 0,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    total DECIMAL(12,2) GENERATED ALWAYS AS 
        ((quantity * unit_price) - discount_amount + tax_amount) STORED,
    
    -- Tracking
    batch_id UUID REFERENCES batches(id),
    
    -- Return/Refund tracking
    is_returned BOOLEAN DEFAULT false,
    returned_quantity INTEGER DEFAULT 0,
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE serial_numbers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    batch_id UUID REFERENCES batches(id),
    
    serial_number VARCHAR(100) UNIQUE NOT NULL,
    imei VARCHAR(100), -- For phones
    
    status VARCHAR(20) DEFAULT 'in_stock', -- 'in_stock', 'sold', 'returned', 'damaged'
    
    sale_id UUID REFERENCES sales(id),
    sale_item_id UUID REFERENCES sale_items(id),
    customer_id UUID REFERENCES customers(id),
    
    warranty_start_date DATE,
    warranty_end_date DATE,
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- deferred: sale_items <-> serial_numbers circular reference
ALTER TABLE sale_items ADD COLUMN serial_number_id UUID REFERENCES serial_numbers(id);

CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sale_id UUID REFERENCES sales(id) ON DELETE CASCADE,
    
    payment_method VARCHAR(30) NOT NULL, -- 'cash', 'card', 'qr', 'bank_transfer', 'store_credit', 'gift_card'
    payment_type VARCHAR(20) DEFAULT 'standard', -- 'standard', 'split', 'partial'
    
    -- Payment details
    amount DECIMAL(12,2) NOT NULL,
    reference_number VARCHAR(100), -- Transaction ID, Check No, etc.
    
    -- Card specific
    card_last_four VARCHAR(4),
    card_brand VARCHAR(20), -- 'visa', 'mastercard', 'amex'
    bank_name VARCHAR(100),
    
    -- QR specific
    qr_code_data TEXT,
    payment_provider VARCHAR(50), -- 'gcash', 'paymaya', 'wechat'
    
    -- Status
    payment_status VARCHAR(20) DEFAULT 'completed', -- 'pending', 'completed', 'failed', 'refunded'
    
    -- Refund tracking
    refunded_amount DECIMAL(12,2) DEFAULT 0,
    refund_reference VARCHAR(100),
    refunded_at TIMESTAMP,
    refunded_by UUID REFERENCES users(id),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE held_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sale_id UUID REFERENCES sales(id),
    
    customer_id UUID REFERENCES customers(id),
    cashier_id UUID REFERENCES users(id),
    
    cart_data JSONB NOT NULL, -- Full cart state
    expiry_time TIMESTAMP NOT NULL, -- Auto-clear after X hours
    
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resumed_at TIMESTAMP,
    is_resumed BOOLEAN DEFAULT false
);

CREATE TABLE refunds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    original_sale_id UUID REFERENCES sales(id),
    refund_invoice_number VARCHAR(50) UNIQUE NOT NULL,
    
    customer_id UUID REFERENCES customers(id),
    processed_by UUID REFERENCES users(id),
    
    refund_type VARCHAR(20) DEFAULT 'partial', -- 'full', 'partial'
    refund_method VARCHAR(30) NOT NULL, -- 'cash', 'original_payment', 'store_credit'
    
    total_refund_amount DECIMAL(12,2) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    notes TEXT,
    
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE refund_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    refund_id UUID REFERENCES refunds(id) ON DELETE CASCADE,
    original_sale_item_id UUID REFERENCES sale_items(id),
    
    product_id UUID REFERENCES products(id),
    variant_id UUID REFERENCES product_variants(id),
    
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    refund_amount DECIMAL(12,2) NOT NULL,
    
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exchanges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    original_sale_id UUID REFERENCES sales(id),
    new_sale_id UUID REFERENCES sales(id),
    
    customer_id UUID REFERENCES customers(id),
    processed_by UUID REFERENCES users(id),
    
    exchange_type VARCHAR(20) DEFAULT 'swap', -- 'swap', 'upgrade', 'downgrade'
    price_difference DECIMAL(12,2) DEFAULT 0,
    reason VARCHAR(255),
    notes TEXT,
    
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exchange_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    exchange_id UUID REFERENCES exchanges(id) ON DELETE CASCADE,
    
    -- Returned item
    returned_sale_item_id UUID REFERENCES sale_items(id),
    returned_quantity INTEGER NOT NULL,
    
    -- New item
    new_product_id UUID REFERENCES products(id),
    new_variant_id UUID REFERENCES product_variants(id),
    new_quantity INTEGER NOT NULL,
    new_unit_price DECIMAL(12,2) NOT NULL,
    
    price_adjustment DECIMAL(12,2) DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
