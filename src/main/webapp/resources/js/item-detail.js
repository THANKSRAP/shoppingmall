// 상품 상세 페이지 JavaScript
document.addEventListener('DOMContentLoaded', () => {
    const colorButtons = document.querySelectorAll('.color-options .option-button');
    const sizeButtons = document.querySelectorAll('.size-options .option-button');
    const colorSelect = document.getElementById('colorSelect');
    const sizeSelect = document.getElementById('sizeSelect');
    const itemOptionIdInput = document.getElementById('itemOptionId');
    const inventoryInfo = document.getElementById('inventoryInfo');
    const quantityInput = document.getElementById('quantity');
    const decreaseBtn = document.getElementById('decrease-btn');
    const increaseBtn = document.getElementById('increase-btn');
    const cartBtn = document.getElementById('cart-btn');
    const orderBtn = document.getElementById('order-btn');
    const wishlistBtn = document.getElementById('wishlist-btn');
    const additionalPriceSpan = document.getElementById('additional-price');
    const totalPriceDiv = document.getElementById('total-price');
    const itemId = document.querySelector('input[name="itemId"]').value;

    let options = [];
    let basePrice = 0;
    let isWishlist = false;

    // 기본 가격 추출
    const basePriceText = document.querySelector('.base-price').textContent;
    basePrice = parseInt(basePriceText.replace(/[^0-9]/g, ''));

    // 옵션 데이터 로드
    fetch(`/api/item-option/${itemId}`)
        .then(res => res.json())
        .then(data => {
            options = data;

            // 단일 옵션인 경우 자동 선택
            if (colorButtons.length === 1) {
                colorButtons[0].classList.add('selected');
                colorSelect.value = colorButtons[0].dataset.value;
                colorSelect.dispatchEvent(new Event('change'));
            }

            if (sizeButtons.length === 1) {
                sizeButtons[0].classList.add('selected');
                sizeSelect.value = sizeButtons[0].dataset.value;
                sizeSelect.dispatchEvent(new Event('change'));
            }
        })
        .catch(error => {
            console.error('옵션 데이터 로드 실패:', error);
        });

    // 색상 버튼 이벤트
    colorButtons.forEach(button => {
        button.addEventListener('click', () => {
            colorButtons.forEach(btn => btn.classList.remove('selected'));
            button.classList.add('selected');
            colorSelect.value = button.dataset.value;
            colorSelect.dispatchEvent(new Event('change'));
        });
    });

    // 사이즈 버튼 이벤트
    sizeButtons.forEach(button => {
        button.addEventListener('click', () => {
            sizeButtons.forEach(btn => btn.classList.remove('selected'));
            button.classList.add('selected');
            sizeSelect.value = button.dataset.value;
            sizeSelect.dispatchEvent(new Event('change'));
        });
    });

    // 선택 업데이트 함수
    function updateSelection() {
        const colorId = Number(colorSelect.value);
        const sizeId = Number(sizeSelect.value);

        if (!colorId || !sizeId) {
            resetSelection();
            return;
        }

        const match = options.find(o =>
            o.colorOptionId === colorId && o.sizeOptionId === sizeId
        );

        if (match) {
            itemOptionIdInput.value = match.itemOptionId;

            // 재고 정보 표시
            inventoryInfo.style.display = 'block';
            inventoryInfo.className = 'inventory-info';

            if (match.quantity > 0) {
                inventoryInfo.textContent = `재고: ${match.quantity}개 남음`;
                if (match.additionalPrice > 0) {
                    inventoryInfo.textContent += ` | 추가금액: +${formatPrice(match.additionalPrice)}원`;
                }
            } else {
                inventoryInfo.textContent = '품절된 상품입니다';
                inventoryInfo.classList.add('out-of-stock');
            }

            // 추가 가격 표시
            if (match.additionalPrice > 0) {
                additionalPriceSpan.textContent = `(+${formatPrice(match.additionalPrice)}원)`;
                additionalPriceSpan.style.display = 'inline';
            } else {
                additionalPriceSpan.style.display = 'none';
            }

            // 수량 제한 설정
            quantityInput.max = match.quantity;
            if (Number(quantityInput.value) > match.quantity) {
                quantityInput.value = Math.max(1, match.quantity);
            }

            // 버튼 상태 업데이트
            const isSoldOut = match.quantity === 0;
            cartBtn.disabled = isSoldOut;
            orderBtn.disabled = isSoldOut;

            updateQuantityButtons();
            updateTotalPrice(match.additionalPrice);

        } else {
            resetSelection();
        }
    }

    // 선택 초기화
    function resetSelection() {
        itemOptionIdInput.value = '';
        inventoryInfo.style.display = 'none';
        additionalPriceSpan.style.display = 'none';
        totalPriceDiv.style.display = 'none';
        quantityInput.removeAttribute('max');
        cartBtn.disabled = true;
        orderBtn.disabled = true;
        updateQuantityButtons();
    }

    // 총 가격 업데이트
    function updateTotalPrice(additionalPrice = 0) {
        const quantity = Number(quantityInput.value);
        if (quantity > 1) {
            const total = (basePrice + additionalPrice) * quantity;
            totalPriceDiv.textContent = `총 ${formatPrice(total)}원`;
            totalPriceDiv.style.display = 'block';
        } else {
            totalPriceDiv.style.display = 'none';
        }
    }

    // 수량 버튼 상태 업데이트
    function updateQuantityButtons() {
        const quantity = Number(quantityInput.value);
        const maxQuantity = Number(quantityInput.max) || 999;

        decreaseBtn.disabled = quantity <= 1;
        increaseBtn.disabled = quantity >= maxQuantity;
    }

    // 가격 포맷팅
    function formatPrice(price) {
        return new Intl.NumberFormat('ko-KR').format(price);
    }

    // 이벤트 리스너
    colorSelect.addEventListener('change', updateSelection);
    sizeSelect.addEventListener('change', updateSelection);

    // 수량 조절 버튼
    decreaseBtn.addEventListener('click', () => {
        const currentValue = Number(quantityInput.value);
        if (currentValue > 1) {
            quantityInput.value = currentValue - 1;
            quantityInput.dispatchEvent(new Event('input'));
        }
    });

    increaseBtn.addEventListener('click', () => {
        const currentValue = Number(quantityInput.value);
        const maxValue = Number(quantityInput.max) || 999;
        if (currentValue < maxValue) {
            quantityInput.value = currentValue + 1;
            quantityInput.dispatchEvent(new Event('input'));
        }
    });

    // 수량 입력 이벤트
    quantityInput.addEventListener('input', () => {
        const colorId = Number(colorSelect.value);
        const sizeId = Number(sizeSelect.value);

        if (colorId && sizeId) {
            const match = options.find(o =>
                o.colorOptionId === colorId && o.sizeOptionId === sizeId
            );
            if (match) {
                updateTotalPrice(match.additionalPrice);
            }
        }
        updateQuantityButtons();
    });

    // 위시리스트 버튼
    wishlistBtn.addEventListener('click', () => {
        isWishlist = !isWishlist;
        wishlistBtn.classList.toggle('active', isWishlist);

        // 여기에 위시리스트 API 호출 로직 추가
        console.log('위시리스트 상태:', isWishlist);
    });

    // 폼 제출 전 검증
    document.querySelector('.options-form').addEventListener('submit', (e) => {
        if (!itemOptionIdInput.value) {
            e.preventDefault();
            alert('색상과 사이즈를 선택해주세요.');
            return;
        }

        const quantity = Number(quantityInput.value);
        if (quantity < 1) {
            e.preventDefault();
            alert('수량을 확인해주세요.');
            return;
        }
    });

});


