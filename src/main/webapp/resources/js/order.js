function goBack() {
    window.history.back();
}

function parsePrice(text) {
    return parseInt(text.replace(/[^0-9]/g, ''), 10);
}

function processPayment() {
    const useExisting = document.getElementById('recent-tab').classList.contains('active');
    const deliveryAddressId = useExisting ? parseInt(document.querySelector('.address-list-item.selected')?.getAttribute('data-id')) : null;

    const recipient = document.getElementById('recipient-name').value;
    const postal = document.getElementById('postal-code').value;
    const address = document.getElementById('basic-address').value;
    const detail = document.getElementById('detail-address').value;
    const phone = `${document.getElementById('phone1').value}-${document.getElementById('phone2').value}-${document.getElementById('phone3').value}`;
    const email = `${document.getElementById('email-user').value}@${document.getElementById('email-domain').value}`;
    const isDefault = document.getElementById('save-default').checked;
    console.log(isDefault)

    const products = Array.from(document.querySelectorAll('.product-item')).map(item => {
        return {
            itemId: parseInt(item.getAttribute('data-item-id')), // ← itemId가 어딨는지 확인 필요
            quantity: parseInt(item.querySelector('.product-details').textContent.replace(/\D/g, '')),
            price: parseInt(item.querySelector('.product-price').textContent.replace(/[^0-9]/g, ''))
        };
    });

    const itemsPrice = parsePrice(document.getElementById('itemsPrice').textContent);
    const deliveryFee = parsePrice(document.getElementById('deliveryFee').textContent);

    document.getElementById("checkoutPage").style.display = "none";
    document.getElementById("paymentCompletePage").style.display = "block";

    const checkoutBtn = document.querySelector(".checkout-btn");
    if (checkoutBtn) {
        checkoutBtn.style.display = "none";
    }

    window.scrollTo({ top: 0, behavior: 'smooth' });

    const deliveryAddress = {
        recipientName: recipient,
        postalCode: postal,
        mainAddress: address,
        detailedAddress: detail,
        recipientPhoneNumber: phone,
        recipientEmail: email,
        isDefault: isDefault
    };

    const requestData = {
        useExistingAddress: useExisting,
        deliveryAddressId: useExisting ? deliveryAddressId : null,
        deliveryAddress: useExisting ? null : deliveryAddress,
        items: products,
        itemsPrice: itemsPrice,
        deliveryFee: deliveryFee
    };

    fetch('/order/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
    }).then(res => res.json())
        .then(data => {
            updatePaymentCompletePage(data);

            document.getElementById('checkoutPage').classList.add('hidden');
            document.getElementById('paymentCompletePage').classList.add('active');
        })
        .catch(err => alert(err.message));
}

// Toggle section collapse/expand
function toggleSection(sectionId) {
    const section = document.getElementById(sectionId);
    section.classList.toggle('section-collapsed');
}

// Navigation functions
function goBack() {
    if (document.getElementById('paymentCompletePage').classList.contains('active')) {
        // If on payment complete page, go back to checkout
        document.getElementById('paymentCompletePage').classList.remove('active');
        document.getElementById('checkoutPage').classList.remove('hidden');
    } else {
        // If on checkout page, go back to previous page
        if (confirm('페이지를 나가시겠습니까?')) {
            history.back();
        }
    }
}

function viewOrderHistory() {
    alert('주문내역 페이지로 이동합니다.');
}

function continueShopping() {
    window.location.href = "/";
}

// Tab switching functionality
document.getElementById('recent-tab').addEventListener('click', function() {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    this.classList.add('active');
    document.getElementById('recent-content').style.display = 'block';
    document.getElementById('direct-content').classList.remove('active');
});

document.getElementById('direct-tab').addEventListener('click', function() {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    this.classList.add('active');
    document.getElementById('recent-content').style.display = 'none';
    document.getElementById('direct-content').classList.add('active');
});

// Address list modal functions
function showAddressList() {
    document.getElementById('addressListModal').style.display = 'block';
}

function closeAddressList() {
    document.getElementById('addressListModal').style.display = 'none';
}

// Address selection
document.querySelectorAll('.address-list-item').forEach(item => {
    item.addEventListener('click', function(e) {
        if (e.target.classList.contains('edit-btn') || e.target.classList.contains('delete-btn')) {
            return;
        }

        document.querySelectorAll('.address-list-item').forEach(i => i.classList.remove('selected'));
        this.classList.add('selected');

        // Update main address display
        const addressData = JSON.parse(this.dataset.address);
        updateMainAddress(addressData);

        closeAddressList();
    });
});

function updateMainAddress(data) {
    const addressInfo = document.querySelector('.address-info');
    console.log(data.isDefault);
    addressInfo.innerHTML = `
        <div class="address-name">
            ${data.isDefault ? '[기본] ' + data.name : data.name}
        </div>
        <div class="address-detail">
            [${data.postal}] ${data.address} ${data.detail}
        </div>
        <div class="address-phone">
            ${data.phone1}-${data.phone2}-${data.phone3}
        </div>
    `;
}

// Edit address function
function editAddress(btn) {
    const addressItem = btn.closest('.address-list-item');
    const addressData = JSON.parse(addressItem.dataset.address);

    // Switch to direct input tab
    document.getElementById('direct-tab').click();

    // Fill form with address data
    document.getElementById('recipient-name').value = addressData.name;
    document.getElementById('postal-code').value = addressData.postal;
    document.getElementById('basic-address').value = addressData.address;
    document.getElementById('detail-address').value = addressData.detail;
    document.getElementById('phone1').value = addressData.phone1;
    document.getElementById('phone2').value = addressData.phone2;
    document.getElementById('phone3').value = addressData.phone3;

    const emailParts = addressData.email.split('@');
    document.getElementById('email-user').value = emailParts[0];
    document.getElementById('email-domain').value = emailParts[1];
    document.getElementById('save-default').checked = !!addressData.isDefault;
    // Select "새로운 배송지" radio button
    document.getElementById('new-address').checked = true;

    closeAddressList();
}

// Delete address function
function deleteAddress(btn) {
    if (confirm('이 배송지를 삭제하시겠습니까?')) {
        btn.closest('.address-list-item').remove();
    }
}

// Message dropdown functions
function toggleMessageOptions(event) {
    const options = event.target.nextElementSibling;
    if (options) {
        options.classList.toggle('show');
    }
}

function selectMessage(option) {
    const dropdown = option.closest('.message-dropdown');
    const select = dropdown.querySelector('.message-select');
    select.textContent = option.textContent;
    option.closest('.message-options').classList.remove('show');

    // Add selected class
    dropdown.querySelectorAll('.message-option').forEach(opt => opt.classList.remove('selected'));
    option.classList.add('selected');
}

// Close dropdowns when clicking outside
document.addEventListener('click', function(e) {
    if (!e.target.closest('.message-dropdown')) {
        document.querySelectorAll('.message-options').forEach(options => {
            options.classList.remove('show');
        });
    }
});

// Address search functionality
document.querySelector('.address-search-btn').addEventListener('click', function () {
    new daum.Postcode({
        oncomplete: function (data) {
            const roadAddr = data.roadAddress;
            const extraAddr = [];

            // 참고항목 조합
            if (data.bname && /[동|로|가]$/g.test(data.bname)) {
                extraAddr.push(data.bname);
            }
            if (data.buildingName && data.apartment === 'Y') {
                extraAddr.push(data.buildingName);
            }

            const fullRoadAddr = extraAddr.length > 0 ? roadAddr + ' (' + extraAddr.join(', ') + ')' : roadAddr;

            // 값 채우기
            document.getElementById('postal-code').value = data.zonecode;
            document.getElementById('basic-address').value = fullRoadAddr;
            document.getElementById('detail-address').focus();
        }
    }).open();
});

// Remove product functionality
document.querySelectorAll('.remove-btn').forEach(btn => {
    btn.addEventListener('click', function() {
        const productItems = document.querySelectorAll('.product-item');

        if (productItems.length <= 1) {
            alert('최소 1개의 상품은 주문해야 합니다.');
            return;
        }

        if (confirm('상품을 삭제하시겠습니까?')) {
            this.closest('.product-item').remove();
            updateTotal();
        }
    });
});

// Payment method selection
document.querySelectorAll('input[name="payment"]').forEach(radio => {
    radio.addEventListener('change', function() {
        const cardInput = document.querySelector('.card-input');
        cardInput.style.display = this.id === 'card' ? 'block' : 'none';
    });
});

// Update total calculation
function updateTotal() {
    const productItems = document.querySelectorAll('.product-item');

    const prices = Array.from(productItems).map(el =>
        parseInt(el.querySelector('.product-price').textContent.replace(/[^0-9]/g, ''))
    );
    const total = prices.reduce((sum, price) => sum + price, 0);

    const deliveryFee = total >= 100000 ? 0 : 3000;
    const finalTotal = total + deliveryFee;

    document.querySelectorAll('.payment-row.total .amount').forEach(el => {
        el.textContent = finalTotal.toLocaleString() + '원';
    });

    document.querySelectorAll('.payment-row:nth-child(1) .amount').forEach(el => {
        el.textContent = total.toLocaleString() + '원';
    });

    document.querySelectorAll('.payment-row:nth-child(2) .amount').forEach(el => {
        el.textContent = (deliveryFee > 0 ? '+' : '') + deliveryFee.toLocaleString() + '원';
    });

    const shippingInfo = document.getElementById('shippingInfoText');
    if (shippingInfo) {
        if (deliveryFee === 0) {
            shippingInfo.textContent = '배송비 0 (무료배송)';
        } else {
            shippingInfo.textContent = '배송비 ' + deliveryFee.toLocaleString() + '원';
        }
    }

    const checkoutBtn = document.querySelector('.checkout-btn');
    if (checkoutBtn) {
        const amountSpan = checkoutBtn.querySelector('.amount');
        if (amountSpan) {
            amountSpan.textContent = finalTotal.toLocaleString() + '원';
        }
    }

    const itemCount = productItems.length;
    const itemCountElement = document.querySelector('.item-count');
    if (itemCountElement) {
        itemCountElement.textContent = itemCount + '개';
    }
}

function updatePaymentCompletePage(response) {
    // 주문번호
    const orderNumberEl = document.getElementById('orderNumber');
    if (orderNumberEl) {
        orderNumberEl.textContent = response.orderNumber || '-';
    }

    // 주문 정보
    const orderInfo = document.getElementById('orderInfoSection');
    if (orderInfo) {
        const map = {
            '상품금액': response.itemsPrice,
            '배송비': response.deliveryFee,
            '할인금액': response.discountAmount,
            '결제금액': response.totalAmount
        };
        orderInfo.querySelectorAll('.order-info-row').forEach(row => {
            const label = row.querySelector('.order-info-label')?.textContent.trim();
            const valueSpan = row.querySelector('.order-info-value');
            if (label && valueSpan && map[label] !== undefined) {
                valueSpan.textContent = formatPrice(map[label]);
            }
        });
    }

    // 배송 정보
    const deliveryInfo = document.getElementById('deliveryInfoSection');
    if (deliveryInfo) {
        const map = {
            '받는분': response.recipientName,
            '연락처': response.recipientPhoneNumber,
            '배송지': response.fullAddress
        };
        deliveryInfo.querySelectorAll('.order-info-row').forEach(row => {
            const label = row.querySelector('.order-info-label')?.textContent.trim();
            const valueSpan = row.querySelector('.order-info-value');
            if (label && valueSpan && map[label] !== undefined) {
                valueSpan.textContent = map[label];
            }
        });
    }
}

function formatPrice(num) {
    if (typeof num !== 'number') return '-';
    return num.toLocaleString('ko-KR') + '원';
}

document.addEventListener('DOMContentLoaded', function () {
    updateTotal();
});
