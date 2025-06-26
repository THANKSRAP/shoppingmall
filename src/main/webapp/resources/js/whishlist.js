// 관심목록 선택 삭제
function deleteSelected() {
    const checkedItems = document.querySelectorAll('.select-item:checked');
    if (checkedItems.length === 0) {
        alert('삭제할 상품을 선택하세요.');
        return;
    }

    const itemIds = [];
    const itemOptionIds = [];

    checkedItems.forEach(checkbox => {
        const wishlistItem = checkbox.closest('.wishlist-item');
        itemIds.push(wishlistItem.dataset.itemId);
        itemOptionIds.push(wishlistItem.dataset.itemOptionId);
    });

    if (!confirm('선택한 상품을 관심목록에서 삭제하시겠습니까?')) return;

    fetch('/wishlist/delete', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `itemIds=${itemIds.join(',')}&itemOptionIds=${itemOptionIds.join(',')}`
    })
        .then(res => res.text())
        .then(result => {
            if (result === 'success') {
                alert('선택한 상품이 삭제되었습니다.');
                location.reload();
            } else if (result === 'login_required') {
                alert('로그인이 필요합니다.');
                location.href = '/user/loginForm';
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}

// 전체 관심목록 삭제
function deleteAll() {
    if (!confirm('정말 전체 관심목록을 삭제하시겠습니까?')) return;

    fetch('/wishlist/deleteAll', {
        method: 'POST'
    })
        .then(res => res.text())
        .then(result => {
            if (result === 'success') {
                alert('전체 관심목록이 삭제되었습니다.');
                location.reload();
            } else if (result === 'login_required') {
                alert('로그인이 필요합니다.');
                location.href = '/user/loginForm';
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}

// 전체 선택/해제
function selectAll(checked) {
    document.querySelectorAll('.select-item').forEach(chk => {
        chk.checked = checked;
    });
}

// 개별 상품 삭제
function deleteWishlistItem(itemId, itemOptionId) {
    if (!confirm('이 상품을 관심목록에서 삭제하시겠습니까?')) return;

    fetch('/wishlist/delete', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `itemIds=${itemId}&itemOptionIds=${itemOptionId}`
    })
        .then(res => res.text())
        .then(result => {
            if (result === 'success') {
                alert('상품이 삭제되었습니다.');
                location.reload();
            } else if (result === 'login_required') {
                alert('로그인이 필요합니다.');
                location.href = '/user/loginForm';
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류'));
}