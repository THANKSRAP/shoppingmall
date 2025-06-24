document.addEventListener('DOMContentLoaded', function() {
    // 현재 페이지에 날짜 입력 필드가 있는지 먼저 확인
    const startDateInput = document.querySelector('input[name="startDate"]');
    const endDateInput = document.querySelector('input[name="endDate"]');

    // 둘 다 없으면 이 스크립트를 실행하지 않음
    if (!startDateInput && !endDateInput) {
        return; // 조기 종료
    }

    // 날짜 설정 로직
    const today = new Date();
    const lastMonth = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());

    if (startDateInput && !startDateInput.value) {
        startDateInput.value = lastMonth.toISOString().split('T')[0];
    }

    if (endDateInput && !endDateInput.value) {
        endDateInput.value = today.toISOString().split('T')[0];
    }


    // // null 체크 추가
    // if (startDateInput && !startDateInput.value) {
    //     startDateInput.value = lastMonth.toISOString().split('T')[0];
    // }
    // if (endDateInput && !endDateInput.value) {
    //     endDateInput.value = today.toISOString().split('T')[0];
    // }

});