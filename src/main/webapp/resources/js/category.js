document.addEventListener('DOMContentLoaded', () => {
    const majorSelect = document.getElementById('majorSelect');
    const middleSelect = document.getElementById('middleSelect');
    const minorSelect = document.getElementById('minorSelect');
    const categoryData = document.getElementById('categoryData');
    const majorId = categoryData.dataset.majorId || '';
    const middleId = categoryData.dataset.middleId || '';
    const minorId = categoryData.dataset.minorId || '';

    // 대분류 선택 시 중분류 로딩
    majorSelect.addEventListener('change', () => {
        const majorId = majorSelect.value;
        middleSelect.innerHTML = '<option value="">중분류 선택</option>';
        minorSelect.innerHTML = '<option value="">소분류 선택</option>';
        minorSelect.disabled = true;
        if (majorId) {
            fetch(`/api/category/parent/${majorId}/children`)
                .then(res => res.json())
                .then(data => {
                    data.forEach(cat => {
                        const opt = document.createElement('option');
                        opt.value = cat.categoryId;
                        opt.textContent = cat.name;
                        middleSelect.appendChild(opt);
                    });
                    middleSelect.disabled = false;
                });
        } else {
            middleSelect.disabled = true;
        }
    });

    // 중분류 선택 시 소분류 로딩
    middleSelect.addEventListener('change', () => {
        const middleId = middleSelect.value;
        minorSelect.innerHTML = '<option value="">소분류 선택</option>';
        if (middleId) {
            fetch(`/api/category/parent/${middleId}/children`)
                .then(res => res.json())
                .then(data => {
                    data.forEach(cat => {
                        const opt = document.createElement('option');
                        opt.value = cat.categoryId;
                        opt.textContent = cat.name;
                        minorSelect.appendChild(opt);
                    });
                    minorSelect.disabled = false;
                });
        } else {
            minorSelect.disabled = true;
        }
    });

    // [추가] 페이지 로딩 시, 이전 선택값 유지
    if (majorId) {
        fetch(`/api/category/parent/${majorId}/children`)
            .then(res => res.json())
            .then(data => {
                data.forEach(cat => {
                    const opt = document.createElement('option');
                    opt.value = cat.categoryId;
                    opt.textContent = cat.name;
                    if (String(cat.categoryId) === middleId) {
                        opt.selected = true;
                    }
                    middleSelect.appendChild(opt);
                });
                middleSelect.disabled = false;

                // 중분류가 선택되어 있으면 소분류도 채움
                if (middleId) {
                    fetch(`/api/category/parent/${middleId}/children`)
                        .then(res => res.json())
                        .then(data => {
                            data.forEach(cat => {
                                const opt = document.createElement('option');
                                opt.value = cat.categoryId;
                                opt.textContent = cat.name;
                                if (String(cat.categoryId) === minorId) {
                                    opt.selected = true;
                                }
                                minorSelect.appendChild(opt);
                            });
                            minorSelect.disabled = false;
                        });
                }
            });
    }
});