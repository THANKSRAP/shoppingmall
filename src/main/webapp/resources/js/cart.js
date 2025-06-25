// 수량 +, - 버튼 조절
function changeQuantity(cartId, delta) {
    const qtyInput = document.getElementById('qty_' + cartId);
    let currentQty = parseInt(qtyInput.value);
    currentQty += delta;
    if (currentQty < 1) currentQty = 1;
    qtyInput.value = currentQty;

    // ✅ 상품 금액 업데이트
    updateItemPrice(cartId, currentQty);

    // ✅ 전체 합계 업데이트
    updateSummary();

    // ✅ 서버에 PATCH 요청 (선택사항)
    updateCartQuantity(cartId, currentQty, null);
}

function updateItemPrice(cartId, quantity) {
    const totalPriceEl = document.querySelector(`#price_${cartId}`);
    if (!totalPriceEl) return;

    const unitPrice = parseInt(totalPriceEl.dataset.price);  // 단가 가져오기
    const totalPrice = unitPrice * quantity;

    totalPriceEl.innerText = totalPrice.toLocaleString() + '원';
}

function updateCartQuantity(cartId, quantity, optionDetail) {
    console.log("PATCH 요청 보냄:", {
        cartId,
        quantity,
        optionDetail
    });

    fetch('api/cart/item/' + cartId, {
        method: 'PATCH',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({quantity: quantity})
    })

        .then(async res => {
            const text = await res.text();
            console.log("서버 응답 상태:", res.status);
            console.log("서버 응답 본문:", text);
            if (res.ok) {
                alert('장바구니가 업데이트 되었습니다.');
                location.reload();
            } else {
                alert('업데이트 실패');
            }
        })
        .catch((err) => {
            console.error("서버 오류:", err);
            alert('서버 오류');
        });
}

function updateSummary() {
    const productTotalEls = document.querySelectorAll('[id^="price_"]');
    const checkboxes = document.querySelectorAll('.select-item');

    let totalPrice = 0;

    // 체크된 상품이 있는지 확인
    const anyChecked = Array.from(checkboxes).some(chk => chk.checked);

    if (anyChecked) {
        // 체크된 상품 가격만 합산
        checkboxes.forEach(chk => {
            if (chk.checked) {
                const cartId = chk.value;
                const priceEl = document.getElementById('price_' + cartId);
                if (priceEl) {
                    const price = parseInt(priceEl.innerText.replace(/[^0-9]/g, '')) || 0;
                    totalPrice += price;
                }
            }
        });
    } else {
        // 체크된게 없으면 모든 상품 가격 합산
        productTotalEls.forEach(el => {
            const price = parseInt(el.innerText.replace(/[^0-9]/g, '')) || 0;
            totalPrice += price;
        });
    }

    const shippingFee = totalPrice >= 100000 ? 0 : 3000;
    const totalAmount = totalPrice + shippingFee;

    document.getElementById('total').innerText = totalPrice.toLocaleString() + '원';
    document.getElementById('shippingFee').innerText = shippingFee.toLocaleString() + '원';
    document.getElementById('totalAmount').innerText = totalAmount.toLocaleString() + '원';
}





// 전체 선택 / 해제
function selectAll(checked) {
    document.querySelectorAll('.select-item').forEach(chk => {
        chk.checked = checked;
    });
    updateSummary();
}


// 선택 삭제
function deleteSelected(e) {
    if (e) e.preventDefault();

    const checkedItems = Array.from(document.querySelectorAll('.select-item:checked'));
    if (checkedItems.length === 0) {
        alert('삭제할 상품을 선택하세요.');
        return;
    }

    const ids = checkedItems.map(chk => chk.closest('.product-item').dataset.cartId);
    if (!confirm('선택한 상품을 삭제하시겠습니까?')) return;

    fetch('/cart/items', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cartItemIds: ids })
    })
        .then(res => {
            if (res.ok) {
                alert('선택한 상품이 삭제되었습니다.');
                location.reload();
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}


// 장바구니에서 상품 제거
function removeFromCart(cartId) {
    if (!confirm('정말 삭제하시겠습니까?')) return;


    fetch('/cart/api/items/' + cartId, {
        method: 'DELETE'
    })
        .then(res => {
            if (res.ok) {
                alert('상품이 삭제되었습니다.');
                location.reload();
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}



// 기존 cart.js 코드들...

// 관심상품 버튼 기능 추가
document.addEventListener('DOMContentLoaded', function() {
    initWishlistButtons();
});

/**
 * 관심상품 버튼 초기화
 */
function initWishlistButtons() {
    // 모든 관심상품 버튼에 이벤트 리스너 추가
    document.querySelectorAll('.wishlist-btn').forEach(button => {
        button.addEventListener('click', function() {
            addToWishlist(this);
        });
    });
}


/**
 * 관심목록에 상품 추가
 */
function addToWishlist(button) {
    // 장바구니 아이템에서 상품 ID 찾기
    const cartItem = button.closest('.cart-item') ||
        button.closest('tr') ||
        button.closest('[data-item-id]');

    let itemId = null;

    // 여러 방법으로 itemId 찾기
    if (cartItem) {
        // data-item-id 속성에서 찾기
        itemId = cartItem.dataset.itemId;

        // input 필드에서 찾기
        if (!itemId) {
            const itemIdInput = cartItem.querySelector('input[name="itemId"]') ||
                cartItem.querySelector('[data-item-id]');
            if (itemIdInput) {
                itemId = itemIdInput.value || itemIdInput.dataset.itemId;
            }
        }
    }

    // 버튼의 data 속성에서 찾기
    if (!itemId) {
        itemId = button.dataset.itemId;
    }

    if (!itemId) {
        alert('상품 정보를 찾을 수 없습니다.');
        console.error('ItemId not found');
        return;
    }

    // 서버에 관심목록 추가 요청
    fetch('/wishlist/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'itemId=' + itemId
    })
        .then(response => response.text())
        .then(result => {
            handleWishlistResponse(result, button);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
}

// 관심상품 버튼 클릭 이벤트 (기존 코드를 이것으로 교체)
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('wishlist-btn')) {
        const itemId = e.target.getAttribute('data-item-id');

        if (!itemId) {
            alert('상품 정보를 찾을 수 없습니다.');
            return;
        }

        fetch('/wishlist/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'itemId=' + itemId
        })
            .then(response => {
                console.log('응답 상태:', response.status);
                return response.text();
            })
            .then(result => {
                console.log('서버 응답:', result);
                handleWishlistResponse(result);
            })
            .catch(error => {
                console.error('관심목록 추가 오류:', error);
                alert('네트워크 오류가 발생했습니다.');
            });
    }
});

// 관심목록 응답 처리 함수
function handleWishlistResponse(result) {
    switch(result) {
        case 'success':
            alert('관심목록에 추가되었습니다.');
            break;
        case 'already_exists':
            alert('이미 관심목록에 있는 상품입니다.');
            break;
        case 'login_required':
            alert('로그인이 필요합니다.');
            window.location.href = '/user/loginForm';
            break;
        case 'error':
        default:
            alert('관심목록 추가에 실패했습니다.');
            break;
    }
}


// 전체 상품 주문
function orderAll() {
    const cartElements = document.querySelectorAll('.product-item');
    const carts = [];
    let itemsPrice = 0;

    cartElements.forEach(el => {
        const cartId = el.dataset.cartId;
        const price = parseFloat(el.dataset.price); // 👉 이게 더 정확
        const quantity = parseInt(document.getElementById('qty_' + cartId).value);

        const totalPrice = price * quantity;
        itemsPrice += totalPrice;

        carts.push({
            cart_id: parseInt(cartId),
            price: totalPrice
        });
    });

    const deliveryFee = itemsPrice >= 100000 ? 0 : 3000;

    const payload = {
        carts: carts,
        items_price: itemsPrice,
        delivery_fee: deliveryFee
    };

    // 주문 데이터 서버에 전송
    fetch('cart/order', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (res.ok) {
                window.location.href = '/order';
            } else {
                alert('주문 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}




// 선택 상품 주문
function orderSelected() {
    const checkedItems = Array.from(document.querySelectorAll('.select-item:checked'));
    if (checkedItems.length === 0) {
        alert('주문할 상품을 선택하세요.');
        return;
    }

    const carts = [];
    let itemsPrice = 0;

    checkedItems.forEach(chk => {
        const itemEl = chk.closest('.product-item');
        const cartId = parseInt(itemEl.dataset.cartId);
        const price = parseFloat(itemEl.dataset.price);
        const quantity = parseInt(document.getElementById('qty_' + cartId).value);

        if (quantity < 1) {
            alert('상품 수량은 1 이상이어야 합니다.');
            return;
        }

        const total = price * quantity;
        itemsPrice += total;

        carts.push({
            cart_id: cartId,
            price: total
        });
    });

    const deliveryFee = itemsPrice >= 100000 ? 0 : 3000;

    const orderData = {
        carts: carts,
        items_price: itemsPrice,
        delivery_fee: deliveryFee
    };

    fetch('cart/order', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(orderData)
    })
        .then(res => {
            if (res.ok) {
                window.location.href = '/order';
            } else {
                alert('주문 요청 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}



// 초기 계산 + 체크박스나 수량 변경 시 자동 계산
window.addEventListener('DOMContentLoaded', updateSummary);
document.addEventListener('change', function (e) {
    if (e.target.classList.contains('select-item') ||
        e.target.classList.contains('quantity-input')) {
        updateSummary();
    }
});
// ✅ 전체선택
document.getElementById('select-all-link').addEventListener('click', function (e) {
    e.preventDefault();
    document.querySelectorAll('.select-item').forEach(chk => chk.checked = true);
    updateSummary();
});

// ✅ 선택삭제
document.getElementById('delete-selected-link').addEventListener('click', deleteSelected);

// ✅ 전체상품삭제
document.getElementById('delete-all-link').addEventListener('click', function (e) {
    e.preventDefault();

    if (!confirm('정말 전체 상품을 삭제하시겠습니까?')) return;

    fetch('/cart/api/all', {
        method: 'DELETE'
    })
        .then(res => {
            if (res.ok) {
                alert('전체 상품이 삭제되었습니다.');
                location.reload();
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류'));
});

document.querySelector('.order-all-btn').addEventListener('click', e => {
    e.preventDefault();
    orderAll();
});

document.querySelector('.order-selected-btn').addEventListener('click', e => {
    e.preventDefault();
    orderSelected();
});