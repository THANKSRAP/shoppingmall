-- 1. user 테이블
CREATE TABLE user
(
    user_id                      BIGINT      NOT NULL AUTO_INCREMENT,
    email                        VARCHAR(60) NOT NULL,
    password                     VARCHAR(60) NOT NULL,
    pre_password                 VARCHAR(60),
    name                         VARCHAR(30) NOT NULL,
    resident_registration_number VARCHAR(20),
    phone_number                 VARCHAR(30) NOT NULL,
    gender                       ENUM('M', 'F') NOT NULL DEFAULT 'M',
    registration_at              TIMESTAMP   NOT NULL,
    user_id_creation_at          TIMESTAMP   NOT NULL,
    sms_marketing_status         BOOLEAN     NOT NULL DEFAULT FALSE,
    email_marketing_status       BOOLEAN     NOT NULL DEFAULT FALSE,
    customer_status              ENUM('ACTIVE', 'SUSPENDED', 'LOCKED', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    is_withdrawal                BOOLEAN     NOT NULL DEFAULT FALSE,
    withdrawal_at                TIMESTAMP,
    withdrawal_reason            VARCHAR(300),
    role                         ENUM('CUSTOMER', 'ADMIN', 'EMPLOYEE') NOT NULL DEFAULT 'CUSTOMER',
    created_at                   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);
-- 2. Admin 테이블
CREATE TABLE admin
(
    admin_id   BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role       ENUM('ADMINISTRATOR', 'SHIPMENT_MANAGER', 'CS_MANAGER') NOT NULL,
    email      VARCHAR(30) NOT NULL,
    status     ENUM('ACTIVE', 'INACTIVE', 'ON_LEAVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- 3. Category 테이블
CREATE TABLE category
(
    category_id        BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    parent_category_id BIGINT,
    name               VARCHAR(50) NOT NULL,
    depth              INT         NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_category_id) REFERENCES category (category_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);
-- 4. Item 테이블
CREATE TABLE item
(
    item_id             BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category_id         BIGINT NOT NULL,
    name                VARCHAR(255),
    image               VARCHAR(255),
    description         TEXT,
    price               DECIMAL(10, 2),
    status              ENUM('ON_SALE', 'OUT_OF_STOCK', 'QUALITY_CHECK', 'ADDRESS_ISSUE', 'WAREHOUSE_OVERLOAD', 'ETC') DEFAULT 'ON_SALE',
    grade               ENUM('A', 'B', 'C') DEFAULT 'A',
    manufacture_country VARCHAR(100),
    is_best_seller      BOOLEAN DEFAULT false,
    is_new              BOOLEAN DEFAULT false,
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP
);
-- 5. Option 테이블
CREATE TABLE `option`
(
    option_id  BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type       ENUM('COLOR', 'SIZE') NOT NULL,
    value      VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- 6. Item_Option 테이블
CREATE TABLE item_option
(
    item_option_id   BIGINT NOT NULL AUTO_INCREMENT,
    item_id          BIGINT NOT NULL,
    color_option_id  BIGINT NOT NULL,
    size_option_id   BIGINT NOT NULL,
    additional_price DECIMAL(10, 2) DEFAULT 0.00,
    created_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (item_option_id, item_id),
    CONSTRAINT fk_item_option_to_item FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE,
    CONSTRAINT fk_item_option_to_color_option FOREIGN KEY (color_option_id) REFERENCES `option` (option_id) ON DELETE RESTRICT,
    CONSTRAINT fk_item_option_to_size_option FOREIGN KEY (size_option_id) REFERENCES `option` (option_id) ON DELETE RESTRICT
);
-- 7. Inventory 테이블
CREATE TABLE inventory
(
    item_id        BIGINT NOT NULL,
    item_option_id BIGINT NOT NULL,
    quantity       INT       DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (item_id, item_option_id),
    FOREIGN KEY (item_id) REFERENCES item (item_id),
    FOREIGN KEY (item_option_id) REFERENCES item_option (item_option_id)
);
-- 8. StockTransaction_History 테이블
CREATE TABLE stocktransaction_history
(
    stocktransaction_history_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item_id                     BIGINT NOT NULL,
    item_option_id              BIGINT NOT NULL,
    admin_id                    BIGINT NOT NULL,
    quantity_change             INT    NOT NULL,
    trasaction_type             ENUM('IN', 'OUT') NOT NULL,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES item (item_id) ON DELETE CASCADE,
    FOREIGN KEY (item_option_id) REFERENCES item_option (item_option_id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES admin (admin_id) ON DELETE CASCADE
);
-- 9. Notice 테이블
CREATE TABLE notice
(
    notice_id  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    admin_id   BIGINT       NOT NULL,
    title      VARCHAR(100) NOT NULL,
    content    TEXT         NOT NULL,
    view_count INT          NOT NULL DEFAULT 0,
    status     ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
    is_pinned  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_admin_TO_notice FOREIGN KEY (admin_id) REFERENCES admin (admin_id) ON DELETE CASCADE
);
-- 10. user 관련 하위 테이블들
-- user_grade
CREATE TABLE user_grade
(
    user_grade_id BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    grade         ENUM('BRONZE', 'SILVER', 'GOLD', 'VIP') NOT NULL DEFAULT 'BRONZE',
    grade_benefit TEXT,
    created_at    TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP NOT NULL,
    CONSTRAINT FK_User_TO_user_grade FOREIGN KEY (user_id) REFERENCES user (user_id)
);
-- grade_change_history
CREATE TABLE grade_change_history
(
    grade_history_id BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_grade_id    BIGINT    NOT NULL,
    grade_change_at  TIMESTAMP NOT NULL,
    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP NOT NULL,
    CONSTRAINT FK_user_grade_TO_grade_change_history FOREIGN KEY (user_grade_id) REFERENCES user_grade (user_grade_id)
);
-- delivery_address
CREATE TABLE delivery_address
(
    delivery_address_id    BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT       NOT NULL,
    recipient_name         VARCHAR(30)  NOT NULL,
    recipient_phone_number VARCHAR(30)  NOT NULL,
    recipient_email varchar(255) NOT NULL,
    postal_code            VARCHAR(10)  NOT NULL,
    main_address           VARCHAR(100) NOT NULL,
    detailed_addres        VARCHAR(100),
    is_default             BOOLEAN      NOT NULL DEFAULT false,
    is_active              BOOLEAN      NOT NULL DEFAULT true,
    created_at             TIMESTAMP    NOT NULL,
    updated_at             TIMESTAMP,
    CONSTRAINT FK_User_TO_delivery_address FOREIGN KEY (user_id) REFERENCES user (user_id)
);
-- wish_list
CREATE TABLE wish_list
(
    wish_list_id BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT    NOT NULL,
    item_id      BIGINT    NOT NULL,
    added_at     TIMESTAMP NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (item_id) REFERENCES item (item_id),
);
-- cart
CREATE TABLE cart
(
    cart_id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    item_id        BIGINT NOT NULL,
    item_option_id BIGINT NOT NULL,
    quantity       INT    NOT NULL DEFAULT 1,
    added_at       TIMESTAMP,
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (item_id) REFERENCES item (item_id),
    FOREIGN KEY (item_option_id) REFERENCES item_option (item_option_id)
);
-- review
CREATE TABLE review
(
    review_id  BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item_id    BIGINT      NOT NULL,
    user_id    BIGINT      NOT NULL,
    rating     INT         NOT NULL CHECK (rating BETWEEN 1 AND 5),
    title      VARCHAR(50) NOT NULL,
    content    TEXT        NOT NULL,
    image      VARCHAR(255),
    view       INT         NOT NULL DEFAULT 0,
    is_hidden  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- item_question
CREATE TABLE item_question
(
    item_question_id BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item_id          BIGINT       NOT NULL,
    user_id          BIGINT       NOT NULL,
    image            VARCHAR(255),
    type             ENUM('PRODUCT', 'SIZE', 'SHIPPING', 'DELIVERY', 'OTHER') NOT NULL DEFAULT 'PRODUCT',
    title            VARCHAR(255) NOT NULL,
    content          TEXT         NOT NULL,
    is_secret        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP
);
-- item_anwer
CREATE TABLE item_answer
(
    item_answer_id   BIGINT    NOT NULL AUTO_INCREMENT,
    item_question_id BIGINT    NOT NULL,
    admin_id         BIGINT    NOT NULL,
    content          TEXT      NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP NULL,
    PRIMARY KEY (item_answer_id)
);
-- `order`
CREATE TABLE `order`
(
    order_id     BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    status       ENUM('PENDING','PAID','PROCESSING','SHIPPED','DELIVERED','CANCELLED','RETURNED','COMPLETED','PARTIALLY_CANCELLED','PARTIALLY_RETURNED') NOT NULL DEFAULT 'PENDING',
    items_price  DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    delivery_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- order_item
CREATE TABLE order_item
(
    item_sequence INT            NOT NULL,
    order_id      BIGINT         NOT NULL,
    item_id       BIGINT         NOT NULL,
    status        ENUM('PENDING','PAID','PROCESSING','SHIPPED','DELIVERED','CANCELLED','RETURNED','COMPLETED') NOT NULL DEFAULT 'PENDING',
    delay_reason  ENUM('OUT_OF_STOCK','QUALITY_CHECK','ADDRESS_ISSUE','WAREHOUSE_OVERLOAD','ETC'),
    price         DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (order_id, item_sequence)
);
-- payment_method
CREATE TABLE payment_method
(
    payment_method_id                   BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id                             BIGINT  NOT NULL,
    payment_method                      ENUM('CREDIT_CARD', 'BANK_TRANSFER') NOT NULL DEFAULT 'CREDIT_CARD',
    card_company_code                   ENUM('CC010', 'CC020', 'CC030', 'CC040', 'CC050', 'CC060', 'CC070', 'CC080', 'CC090', 'CC100'),
    card_number                         VARCHAR(255),
    bank_code                           ENUM('BANK001', 'BANK002', 'BANK003', 'BANK004', 'BANK005'),
    account_number                      VARCHAR(60),
    user_cash_receipt_number            VARCHAR(60),
    user_taxpayer_identification_number VARCHAR(60),
    billing_key                         VARCHAR(255),
    is_default                          BOOLEAN NOT NULL DEFAULT false,
    is_active                           BOOLEAN NOT NULL DEFAULT true,
    pay_deny_reason                     ENUM('CARD_EXPIRED', 'INVALID_CARD', 'INSUFFICIENT_FUNDS', 'CARD_LIMIT_EXCEEDED'),
    payment_owner_type                  ENUM('INDIVIDUAL', 'CORPORATE') NOT NULL DEFAULT 'INDIVIDUAL',
    created_at                          TIMESTAMP,
    updated_at                          TIMESTAMP,
    CONSTRAINT FK_User_TO_payment_method FOREIGN KEY (user_id) REFERENCES user (user_id)
);