document.addEventListener('DOMContentLoaded', () => {
    const majorSelect = document.getElementById('majorSelect');
    const middleSelect = document.getElementById('middleSelect');
    const minorSelect = document.getElementById('minorSelect');
    const categoryData = document.getElementById('categoryData');

    const majorId = categoryData.dataset.majorId;
    const middleId = categoryData.dataset.middleId;
    const minorId = categoryData.dataset.minorId;

    // 공통: 하위 카테고리 채우기 함수
    function populateChildren(parentId, selectElement, selectedValue) {
        fetch(`/api/category/parent/${parentId}/children`)
            .then(res => res.json())
            .then(data => {
                selectElement.innerHTML = `<option value="">${selectElement === middleSelect ? '중분류' : '소분류'} 선택</option>`;
                data.forEach(cat => {
                    const opt = document.createElement('option');
                    opt.value = cat.categoryId;
                    opt.textContent = cat.name;
                    if (String(cat.categoryId) === String(selectedValue)) {
                        opt.selected = true;
                    }
                    selectElement.appendChild(opt);
                });
                selectElement.disabled = false;
            });
    }

    // 대분류 변경 시 중분류 초기화
    majorSelect.addEventListener('change', () => {
        const majorId = majorSelect.value;
        middleSelect.innerHTML = '<option value="">중분류 선택</option>';
        minorSelect.innerHTML = '<option value="">소분류 선택</option>';
        minorSelect.disabled = true;

        if (majorId) {
            populateChildren(majorId, middleSelect, null);
        } else {
            middleSelect.disabled = true;
        }
    });

    // 중분류 변경 시 소분류 초기화
    middleSelect.addEventListener('change', () => {
        const middleId = middleSelect.value;
        minorSelect.innerHTML = '<option value="">소분류 선택</option>';

        if (middleId) {
            populateChildren(middleId, minorSelect, null);
        } else {
            minorSelect.disabled = true;
        }
    });

    // 페이지 로드 시 이전 선택값 복원
    if (majorId) {
        majorSelect.value = majorId;
        populateChildren(majorId, middleSelect, middleId);

        if (middleId) {
            populateChildren(middleId, minorSelect, minorId);
        }
    }
});
