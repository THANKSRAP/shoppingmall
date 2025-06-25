document.addEventListener("DOMContentLoaded", () => {
    // DOM 요소들
    const majorSelect = document.getElementById("majorSelect")
    const middleSelect = document.getElementById("middleSelect")
    const minorSelect = document.getElementById("minorSelect")
    const categoryData = document.getElementById("categoryData")
    const itemGridContainer = document.getElementById("itemGridContainer")
    const itemListContainer = document.getElementById("itemListContainer")
    const loadingSpinner = document.getElementById("loadingSpinner")
    const emptyState = document.getElementById("emptyState")
    const itemCount = document.getElementById("itemCount")
    const searchInput = document.getElementById("searchInput")
    const searchBtn = document.getElementById("searchBtn")
    const sortSelect = document.getElementById("sortSelect")
    const gridViewBtn = document.getElementById("gridViewBtn")
    const listViewBtn = document.getElementById("listViewBtn")
    const gridView = document.getElementById("gridView")
    const listView = document.getElementById("listView")

    // 초기 데이터
    const majorId = categoryData.dataset.majorId
    const middleId = categoryData.dataset.middleId
    const minorId = categoryData.dataset.minorId

    let currentItems = []
    let currentView = "grid"
    let currentPage = 1
    const itemsPerPage = 12

    // 로딩 상태 관리
    function showLoading() {
        loadingSpinner.style.display = "block"
        gridView.style.display = "none"
        listView.style.display = "none"
        emptyState.style.display = "none"
    }

    function hideLoading() {
        loadingSpinner.style.display = "none"
        if (currentView === "grid") {
            gridView.style.display = "block"
            listView.style.display = "none"
        } else {
            gridView.style.display = "none"
            listView.style.display = "block"
        }
    }

    // 하위 카테고리 채우기
    function populateChildren(parentId, selectElement, selectedValue) {
        const isMiddle = selectElement === middleSelect
        const placeholder = isMiddle ? "전체 중분류" : "전체 소분류"

        selectElement.innerHTML = `<option value="">로딩 중...</option>`
        selectElement.disabled = true

        fetch(`/api/category/parent/${parentId}/children`)
            .then((res) => {
                if (!res.ok) throw new Error("Network response was not ok")
                return res.json()
            })
            .then((data) => {
                selectElement.innerHTML = `<option value="">${placeholder}</option>`
                data.forEach((cat) => {
                    const opt = document.createElement("option")
                    opt.value = cat.categoryId
                    opt.textContent = cat.name
                    if (String(cat.categoryId) === String(selectedValue)) {
                        opt.selected = true
                    }
                    selectElement.appendChild(opt)
                })
                selectElement.disabled = false
                fetchItems()
            })
            .catch((error) => {
                console.error("Error fetching categories:", error)
                selectElement.innerHTML = `<option value="">오류 발생</option>`
                selectElement.disabled = false
            })
    }

    // 상품 목록 가져오기
    function fetchItems() {
        showLoading()

        const major = majorSelect.value
        const middle = middleSelect.value
        const minor = minorSelect.value
        const search = searchInput.value.trim()

        const queryParams = new URLSearchParams()
        if (major) queryParams.append("majorId", major)
        if (middle) queryParams.append("middleId", middle)
        if (minor) queryParams.append("minorId", minor)
        if (search) queryParams.append("search", search)

        fetch(`/api/item?${queryParams.toString()}`)
            .then((res) => {
                if (!res.ok) throw new Error("Network response was not ok")
                return res.json()
            })
            .then((data) => {
                currentItems = data
                applySort()
                hideLoading()
            })
            .catch((error) => {
                console.error("Error fetching items:", error)
                hideLoading()
                showError("상품을 불러오는 중 오류가 발생했습니다.")
            })
    }

    // 정렬 적용
    function applySort() {
        const sortValue = sortSelect.value
        const [field, direction] = sortValue.split("_")

        currentItems.sort((a, b) => {
            let aVal, bVal

            switch (field) {
                case "name":
                    aVal = a.name.toLowerCase()
                    bVal = b.name.toLowerCase()
                    break
                case "price":
                    aVal = a.price
                    bVal = b.price
                    break
                case "grade":
                    aVal = a.grade
                    bVal = b.grade
                    break
                default:
                    return 0
            }

            if (direction === "asc") {
                return aVal > bVal ? 1 : aVal < bVal ? -1 : 0
            } else {
                return aVal < bVal ? 1 : aVal > bVal ? -1 : 0
            }
        })

        updateDisplay()
    }

    // 화면 업데이트
    function updateDisplay() {
        updateItemCount(currentItems.length)

        if (currentItems.length === 0) {
            emptyState.style.display = "block"
            gridView.style.display = "none"
            listView.style.display = "none"
            return
        }

        emptyState.style.display = "none"

        // 페이지네이션 적용
        const startIndex = (currentPage - 1) * itemsPerPage
        const endIndex = startIndex + itemsPerPage
        const pageItems = currentItems.slice(startIndex, endIndex)

        if (currentView === "grid") {
            updateGridView(pageItems)
        } else {
            updateListView(pageItems)
        }

        updatePagination()
    }

    // 그리드 뷰 업데이트
    function updateGridView(items) {
        const container = itemGridContainer
        container.innerHTML = ""

        if (items.length === 0) return

        const row = document.createElement("div")
        row.className = "row"

        items.forEach((item) => {
            const col = document.createElement("div")
            col.className = "col-xl-3 col-lg-4 col-md-6 col-sm-6 mb-4"

            const statusInfo = getStatusInfo(item.status)
            const formattedPrice = new Intl.NumberFormat("ko-KR").format(item.price)

            col.innerHTML = `
        <div class="card item-card h-100 shadow-hover">
          <div class="position-relative">
            <img src="${item.image || "/placeholder.svg?height=250&width=200"}"
                 class="card-img-top item-image" 
                 alt="${escapeHtml(item.name)}"
                 loading="lazy">
            
            <div class="position-absolute top-0 end-0 m-2">
              <span class="badge ${statusInfo.badgeClass}">
                <i class="${statusInfo.icon} me-1"></i>${statusInfo.text}
              </span>
            </div>

            <div class="position-absolute top-0 start-0 m-2">
              <span class="badge bg-primary">등급 ${item.grade}</span>
            </div>
          </div>
          
          <div class="card-body d-flex flex-column">
            <h6 class="card-title item-title mb-2">${escapeHtml(item.name)}</h6>
            <p class="card-text text-muted small mb-2">${escapeHtml(item.description || "")}</p>
            <p class="text-muted small mb-2">
              <i class="fas fa-globe me-1"></i>
              ${escapeHtml(item.manufactureCountry)}
            </p>
            
            <div class="mt-auto">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="item-price text-primary fw-bold">${formattedPrice}원</span>
              </div>
              
              <div class="d-grid">
                <a href="/item/${item.itemId}" class="btn btn-primary btn-sm">
                  <i class="fas fa-eye me-1"></i>
                  상세보기
                </a>
              </div>
            </div>
          </div>
        </div>
      `

            row.appendChild(col)
        })

        container.appendChild(row)
    }

    // 리스트 뷰 업데이트
    function updateListView(items) {
        const tbody = itemListContainer
        tbody.innerHTML = ""

        items.forEach((item) => {
            const row = document.createElement("tr")
            const statusInfo = getStatusInfo(item.status)
            const formattedPrice = new Intl.NumberFormat("ko-KR").format(item.price)

            row.innerHTML = `
        <td>
          <img src="${item.image || "/placeholder.svg?height=60&width=50"}"
               class="rounded" 
               style="width: 50px; height: 60px; object-fit: cover;"
               alt="${escapeHtml(item.name)}">
        </td>
        <td>
          <div class="fw-semibold">${escapeHtml(item.name)}</div>
          <small class="text-muted">${escapeHtml(item.description || "")}</small>
        </td>
        <td class="fw-bold text-primary">${formattedPrice}원</td>
        <td>
          <span class="badge ${statusInfo.badgeClass}">${statusInfo.text}</span>
        </td>
        <td>
          <span class="badge bg-primary">${item.grade}</span>
        </td>
        <td>${escapeHtml(item.manufactureCountry)}</td>
        <td>
          <a href="/item/${item.itemId}" class="btn btn-sm btn-outline-primary">
            <i class="fas fa-eye"></i>
          </a>
        </td>
      `

            tbody.appendChild(row)
        })
    }

    // 페이지네이션 업데이트
    function updatePagination() {
        const totalPages = Math.ceil(currentItems.length / itemsPerPage)
        const pagination = document.getElementById("pagination")

        if (totalPages <= 1) {
            pagination.innerHTML = ""
            return
        }

        let paginationHTML = ""

        // 이전 버튼
        paginationHTML += `
      <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
        <a class="page-link" href="#" data-page="${currentPage - 1}">
          <i class="fas fa-chevron-left"></i>
        </a>
      </li>
    `

        // 페이지 번호들
        const startPage = Math.max(1, currentPage - 2)
        const endPage = Math.min(totalPages, currentPage + 2)

        for (let i = startPage; i <= endPage; i++) {
            paginationHTML += `
        <li class="page-item ${i === currentPage ? "active" : ""}">
          <a class="page-link" href="#" data-page="${i}">${i}</a>
        </li>
      `
        }

        // 다음 버튼
        paginationHTML += `
      <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
        <a class="page-link" href="#" data-page="${currentPage + 1}">
          <i class="fas fa-chevron-right"></i>
        </a>
      </li>
    `

        pagination.innerHTML = paginationHTML

        // 페이지네이션 이벤트 리스너
        pagination.querySelectorAll(".page-link").forEach((link) => {
            link.addEventListener("click", (e) => {
                e.preventDefault()
                const page = Number.parseInt(e.target.closest(".page-link").dataset.page)
                if (page && page !== currentPage && page >= 1 && page <= totalPages) {
                    currentPage = page
                    updateDisplay()
                    window.scrollTo({ top: 0, behavior: "smooth" })
                }
            })
        })
    }

    // 상태 정보 반환
    function getStatusInfo(status) {
        switch (status) {
            case "ON_SALE":
                return {
                    text: "판매중",
                    badgeClass: "bg-success",
                    icon: "fas fa-check-circle",
                }
            case "OUT_OF_STOCK":
                return {
                    text: "품절",
                    badgeClass: "bg-danger",
                    icon: "fas fa-times-circle",
                }
            default:
                return {
                    text: "판매중지",
                    badgeClass: "bg-warning",
                    icon: "fas fa-pause-circle",
                }
        }
    }

    // 상품 개수 업데이트
    function updateItemCount(count) {
        itemCount.textContent = count.toLocaleString()
    }

    // HTML 이스케이프
    function escapeHtml(text) {
        if (!text) return ""
        const div = document.createElement("div")
        div.textContent = text
        return div.innerHTML
    }

    // 오류 표시
    function showError(message) {
        emptyState.innerHTML = `
      <div class="mb-4">
        <i class="fas fa-exclamation-triangle fa-4x text-danger opacity-50"></i>
      </div>
      <h4 class="text-danger">오류 발생</h4>
      <p class="text-muted">${message}</p>
      <button class="btn btn-primary" onclick="location.reload()">
        <i class="fas fa-refresh me-1"></i>
        새로고침
      </button>
    `
        emptyState.style.display = "block"
    }

    // 필터 초기화
    window.resetFilters = () => {
        majorSelect.value = ""
        middleSelect.innerHTML = '<option value="">전체 중분류</option>'
        minorSelect.innerHTML = '<option value="">전체 소분류</option>'
        middleSelect.disabled = true
        minorSelect.disabled = true
        searchInput.value = ""
        sortSelect.value = "name_asc"
        currentPage = 1
        fetchItems()
    }

    // 이벤트 리스너들
    majorSelect.addEventListener("change", () => {
        const majorId = majorSelect.value
        middleSelect.innerHTML = '<option value="">전체 중분류</option>'
        minorSelect.innerHTML = '<option value="">전체 소분류</option>'
        middleSelect.disabled = true
        minorSelect.disabled = true
        currentPage = 1

        if (majorId) {
            populateChildren(majorId, middleSelect, null)
        } else {
            fetchItems()
        }
    })

    middleSelect.addEventListener("change", () => {
        const middleId = middleSelect.value
        minorSelect.innerHTML = '<option value="">전체 소분류</option>'
        minorSelect.disabled = true
        currentPage = 1

        if (middleId) {
            populateChildren(middleId, minorSelect, null)
        } else {
            fetchItems()
        }
    })

    minorSelect.addEventListener("change", () => {
        currentPage = 1
        fetchItems()
    })

    // 검색 기능
    searchBtn.addEventListener("click", () => {
        currentPage = 1
        fetchItems()
    })

    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            currentPage = 1
            fetchItems()
        }
    })

    // 정렬 기능
    sortSelect.addEventListener("change", () => {
        currentPage = 1
        applySort()
    })

    // 뷰 타입 토글
    gridViewBtn.addEventListener("click", () => {
        currentView = "grid"
        gridViewBtn.classList.add("active")
        listViewBtn.classList.remove("active")
        updateDisplay()
    })

    listViewBtn.addEventListener("click", () => {
        currentView = "list"
        listViewBtn.classList.add("active")
        gridViewBtn.classList.remove("active")
        updateDisplay()
    })

    // 초기화
    if (majorId) {
        majorSelect.value = majorId
        populateChildren(majorId, middleSelect, middleId)

        if (middleId) {
            setTimeout(() => {
                if (minorId) {
                    populateChildren(middleId, minorSelect, minorId)
                }
            }, 100)
        }
    } else {
        // 초기 상품 개수 설정
        const initialItems = document.querySelectorAll("#itemGridContainer .col-xl-3")
        updateItemCount(initialItems.length)
    }
})
