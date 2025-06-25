document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.querySelector('.search-input');
    const clearBtn = document.getElementById('clearBtn');
    const gridViewBtn = document.getElementById('gridViewBtn');
    const listViewBtn = document.getElementById('listViewBtn');
    const gridView = document.getElementById('gridView');
    const listView = document.getElementById('listView');

    // 검색어 입력 시 클리어 버튼 표시
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            if (this.value.length > 0) {
                clearBtn.style.display = 'block';
            } else {
                clearBtn.style.display = 'none';
            }
        });

        // 초기 로드 시 검색어가 있으면 클리어 버튼 표시
        if (searchInput.value.length > 0) {
            clearBtn.style.display = 'block';
        }
    }

    // 클리어 버튼 클릭
    if (clearBtn) {
        clearBtn.addEventListener('click', function() {
            searchInput.value = '';
            clearBtn.style.display = 'none';
            searchInput.focus();
        });
    }

    // 뷰 타입 토글
    if (gridViewBtn && listViewBtn) {
        gridViewBtn.addEventListener('click', function() {
            gridViewBtn.classList.add('active');
            listViewBtn.classList.remove('active');
            if (gridView) gridView.style.display = 'block';
            if (listView) listView.style.display = 'none';
        });

        listViewBtn.addEventListener('click', function() {
            listViewBtn.classList.add('active');
            gridViewBtn.classList.remove('active');
            if (gridView) gridView.style.display = 'none';
            if (listView) listView.style.display = 'block';
        });
    }
});

// 인기 검색어 클릭
function searchKeyword(keyword) {
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.value = keyword;
        document.querySelector('.search-form').submit();
    }
}