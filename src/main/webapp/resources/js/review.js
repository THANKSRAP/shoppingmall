// 상세 리뷰 모달 관련 변수들
const reviewDetailModal = document.getElementById("reviewDetailModal")
const reviewCloseBtn = document.querySelector(".review-close-btn")
const mainReviewImage = document.getElementById("mainReviewImage")
const imageCounter = document.getElementById("imageCounter")
const indicatorProgress = document.querySelector(".indicator-progress")

function openReviewDetail(reviewId) {
    fetch(`/review/detail?reviewId=${reviewId}`)
        .then(res => {
            if (!res.ok) {
                throw new Error(`HTTP 상태 코드 ${res.status}`);
            }
            return res.json();
        })
        .then(review => {
            console.log("📦 불러온 리뷰 데이터:", review);
            console.log("상품 아이디 출력", review.itemId);
            console.log("🖼️ 상품 이미지 URL:", review.productImage);

            // 이미지 관련 변수
            currentReviewImages = review.image ? [review.image] : [];
            currentImageIndex = 0;

            // 모달 내용 채우기
            document.getElementById("detailReviewTitle").textContent = review.title;
            document.getElementById("detailReviewText").innerHTML = review.content
                .split("\n")
                .map(line => `<p>${line}</p>`)
                .join("");

            document.getElementById("detailRating").innerHTML = renderStars(review.rating);
            document.getElementById("detailProductName").textContent = review.productName;
            const formattedPrice = Number(review.productPrice).toLocaleString("ko-KR");
            document.getElementById("detailProductPrice").textContent = `${formattedPrice}원`;
            document.getElementById("detailViewCount").textContent = `조회 ${review.view}`;

            // 이미지가 있다면 설정
            document.getElementById("mainReviewImage").src =
                review.image ? review.image : "/placeholder.svg?height=400&width=400";


            const productImageUrl = review.productImage?.startsWith("http")
                ? review.productImage
                : "/placeholder.svg?height=80&width=80";

            //
            document.getElementById("detailProductImage").src = productImageUrl;
            // // ✅ 버튼이 이제 DOM에 생겼으므로, 이벤트 연결 가능
            // document.querySelector(".product-detail-btn").addEventListener("click", () => {
            //     if (review.itemId) {
            //         window.location.href = `/shoppingmall/item/${review.itemId}`;
            //     } else {
            //         alert("상품 정보가 없습니다.");
            //     }
            // });

            // 모달 열기
            document.getElementById("reviewDetailModal").style.display = "block";
            document.body.style.overflow = "hidden";
        })
        .catch(err => {
            console.error("❌ 리뷰 상세 불러오기 실패:", err);
            alert("리뷰 정보를 불러오는 데 실패했습니다.");
        });
}

function renderStars(rating) {
    const fullStar = '<i class="fas fa-star"></i>'; // 채워진 별
    const emptyStar = '<i class="far fa-star"></i>'; // 빈 별
    return fullStar.repeat(rating) + emptyStar.repeat(5 - rating);
}


// 상세 리뷰 모달 닫기
reviewCloseBtn.addEventListener("click", () => {
    reviewDetailModal.style.display = "none"
    document.body.style.overflow = "auto"
})


// 모달 외부 클릭 시 닫기
reviewDetailModal.addEventListener("click", (e) => {
    if (e.target === reviewDetailModal) {
        reviewDetailModal.style.display = "none"
        document.body.style.overflow = "auto"
    }
})


document.addEventListener("DOMContentLoaded", () => {
    console.log("✅✅✅✅✅ DOMContentLoaded 시작"); // DOM 로드 확인

    const reviewGrid = document.getElementById("reviewGrid");
    const itemIdInput = document.getElementById("reviewPageItemId"); // <-- 여기 주석을 풀어줘!
    const itemId = itemIdInput ? parseInt(itemIdInput.value) : null; // <-- 여기도 주석을 풀어줘!

    console.log("itemIditemIditemId: " + itemId); // 이제 itemId 값이 잘 찍힐 거야!

    const url = `/review/api/list${itemId ? `?itemId=${itemId}` : ''}`;
    console.log("📡 리뷰 fetch URL:", url);

    fetch(url)
        .then(res => {
            if (!res.ok) {
                throw new Error(`서버 응답 오류: ${res.status}`);
            }
            return res.json();
        })
        .then(reviews => {
            console.log("📊 리뷰 데이터 파싱 완료:", reviews);


            document.getElementById("reviewCount").textContent = reviews.length;


            let totalRating = 0;


            reviews.forEach((review, index) => {
                console.log(`🔍 리뷰 ${index + 1}:`, review);


                totalRating += review.rating;


                const obfuscatedUsername = review.username
                    ? review.username[0] + "*".repeat(review.username.length - 1)
                    : "익명";


                const card = document.createElement("div");
                card.className = "review-card";
                card.dataset.reviewId = review.reviewId;


                card.innerHTML = `
                   <div class="review-image">
                     <img src="${review.image || '/placeholder.svg?height=200&width=200'}" alt="리뷰 사진">
                     <div class="image-overlay">
                       <button class="expand-btn">🔍</button>
                     </div>
                   </div>
                   <div class="review-content">
                     <h3 class="review-title">${review.title}</h3>
                     <p class="review-text">${review.content.length > 80 ? review.content.substring(0, 80) + "..." : review.content}</p>
                     <div class="review-footer">
                       <div class="user-info">
                         <span class="username">${obfuscatedUsername}</span>
                         <div class="rating">${renderStars(review.rating)}</div>
                       </div>
                       <div class="product-info">
                         <img src="${review.productImage || '/placeholder.svg?height=40&width=40'}" alt="상품 이미지" class="product-thumb">
                         <div class="product-details">
                           <span class="product-name">${review.productName || '상품명 없음'}</span>
                           <span class="product-size">${review.productSize ? '사이즈: ' + review.productSize : ''}</span>
                         </div>
                       </div>
                     </div>
                   </div>
               `;

                // const navigatableElems = card.querySelectorAll('.product-navigate');
                // navigatableElems.forEach(elem => {
                //     elem.style.cursor = 'pointer'; // 클릭 가능한 UI처럼
                //     elem.addEventListener('click', function () {
                //         const itemId = this.dataset.itemId;
                //         if (itemId) {
                //             // contextPath 자동 판별
                //             const contextPath = window.location.pathname.includes('/shoppingmall') ? '/shoppingmall' : '';
                //             window.location.href = `${contextPath}/item/${itemId}`;
                //         }
                //     });
                // });

                document.addEventListener("click", (e) => {
                    const productElem = e.target.closest(".product-navigate");
                    if (productElem) {
                        const itemId = document.getElementById("reviewPageItemId").value;
                        console.log("🧭 이동할 itemId:", itemId);
                        if (itemId) {
                            const contextPath = window.location.pathname.includes('/shoppingmall') ? '/shoppingmall' : '';
                            window.location.href = `${contextPath}/item/${itemId}`;
                        }
                    }
                });



                reviewGrid.appendChild(card);
            });


            const avg = reviews.length ? (totalRating / reviews.length).toFixed(1) : "0.0";
            document.getElementById("averageRating").textContent = avg;


            console.log("✅ 모든 리뷰 렌더링 완료, 평균 평점:", avg);
        })
        .catch(err => {
            console.error("❌ 리뷰 데이터를 불러오는 중 에러:", err);
        });




    document.getElementById("reviewGrid").addEventListener("click", (e) => {
        const card = e.target.closest(".review-card");
        if (!card) return;


        // if (e.target.classList.contains("expand-btn")) {
        //     console.log("🔍 확대 버튼 클릭 - 모달 열기 안 함");
        //     return;
        // }


        const reviewId = Number.parseInt(card.dataset.reviewId);
        console.log("📌 (위임) reviewId 추출:", reviewId);


        openReviewDetail(reviewId);
    });


    document.querySelector(".review-close-btn").addEventListener("click", () => {
        document.getElementById("reviewDetailModal").style.display = "none";
        document.body.style.overflow = "auto";
    });


// ESC 키로 모달 닫기
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape" && reviewDetailModal.style.display === "block") {
            reviewDetailModal.style.display = "none"
            document.body.style.overflow = "auto"
        }
    })


// 키보드 네비게이션 (화살표 키로 이미지 이동)
    document.addEventListener("keydown", (e) => {
        if (reviewDetailModal.style.display === "block") {
            if (e.key === "ArrowLeft" && currentImageIndex > 0) {
                currentImageIndex--
                updateGalleryImage()
            } else if (e.key === "ArrowRight" && currentImageIndex < currentReviewImages.length - 1) {
                currentImageIndex++
                updateGalleryImage()
            }
        }
    })


// 액션 버튼 기능
    document.querySelector(".like-btn").addEventListener("click", () => {
        alert("도움이 되었다고 표시했습니다!")
    })


    document.querySelector(".share-btn").addEventListener("click", () => {
        alert("리뷰를 공유합니다!")
    })


// 페이지네이션 기능
    document.querySelectorAll(".page-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
            if (this.classList.contains("prev") || this.classList.contains("next")) {
                console.log("Navigate to", this.textContent)
                return
            }


            // 활성 페이지 변경
            document.querySelectorAll(".page-btn").forEach((b) => b.classList.remove("active"))
            if (!this.classList.contains("prev") && !this.classList.contains("next")) {
                this.classList.add("active")
            }


            console.log("Load page", this.textContent)
        })
    })


    document.querySelectorAll(".review-card").forEach((card, index) => {
        console.log(`🧱 바인딩 시점 - 카드 ${index}:`, card); // 바인딩 시점 카드 정보 확인


        card.addEventListener("click", (e) => {
            console.log("🖱 카드 클릭됨");


            // 확대 버튼 클릭 시에는 상세 모달을 열지 않음
            // if (e.target.classList.contains("expand-btn")) {
            //     console.log("🔍 확대 버튼 클릭 - 모달 열기 건너뜀");
            //     return;
            // }


            const reviewId = Number.parseInt(card.dataset.reviewId);
            console.log("📌 reviewId 추출:", reviewId);


            openReviewDetail(reviewId);
        });


        // 클릭 가능함을 시각적으로 표시
        card.style.cursor = "pointer";
    });


// 리뷰 카드 호버 효과
    document.querySelectorAll(".review-card").forEach((card) => {
        card.addEventListener("mouseenter", function () {
            this.style.transform = "translateY(-2px)"
        })


        card.addEventListener("mouseleave", function () {
            this.style.transform = "translateY(0)"
        })
    })


// 갤러리 네비게이션
    document.querySelector(".prev-btn").addEventListener("click", () => {
        if (currentImageIndex > 0) {
            currentImageIndex--
            updateGalleryImage()
        }
    })


    document.querySelector(".next-btn").addEventListener("click", () => {
        if (currentImageIndex < currentReviewImages.length - 1) {
            currentImageIndex++
            updateGalleryImage()
        }
    })




});






console.log("리뷰 게시판 시스템 초기화 완료")
console.log("Version 2 (리뷰 게시판) → Version 3 (상세 모달) 연결 완료")