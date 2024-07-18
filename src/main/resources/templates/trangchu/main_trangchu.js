


document.addEventListener('DOMContentLoaded', function () {
    var pageIntroLink = document.getElementById('pageIntroLink');

    pageIntroLink.addEventListener('click', function (event) {
        // Ngăn chặn hành vi mặc định của button là submit form hoặc chuyển hướng
        event.preventDefault();

        // Thêm hiệu ứng load trang, ví dụ như opacity animation
        document.body.style.opacity = 0;

        // Chuyển hướng sang trang page_intro.php sau 1 giây (1000 milliseconds)
        setTimeout(function () {
            window.location.href = 'page_intro.html';
        }, 1000); // Thời gian chờ trước khi chuyển hướng (1 giây)
    });
});

function goToIndex() {
    window.location.href = 'trangchu.html';
    return false;
}

function goToAbout(event) {
    const footer = document.getElementById('footer');
    const footerPosition = footer.getBoundingClientRect().top + window.pageYOffset;
 
    window.scrollTo({
        top: footerPosition,
        behavior: 'smooth'
    });
}

