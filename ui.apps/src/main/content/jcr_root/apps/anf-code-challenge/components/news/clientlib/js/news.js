$(document).ready(function () {
    if ($(".title .boxed-layout h2").length > 0) {
        $('.js-top-position').insertBefore('.title .boxed-layout h2');
    }

    if ($(".title .boxed-layout h2").length > 0) {
        $('.js-published-date').insertAfter('.title .boxed-layout h2');
    }
});