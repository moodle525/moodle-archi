$(function () {
    var st = 180;
    $('ul>li').mouseenter(function () {
        $(this).find('div').stop(false, true).slideDown(st);
    }).mouseleave(function () {
        $(this).find('div').stop().slideUp(st);
    });
});