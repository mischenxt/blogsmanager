/**
 *
 */

$(function () {

    //点击标签按钮追加标签
    $(".appendtag").click(function () {
        var vadd = $(this).html();
        var vold = $("#articleTag").val().trim();
        $("#articleTag").val(vold.length == 0 ? vadd : vold + "," + vadd);


    })
})

function searchPage(pn) {

    //分页按钮
    //将name为page的hidden赋值
    $("#page").val(pn);

    document.forms[0].submit();

}

function myDelItem(oid) {


    //删除前确认
    if (confirm("确定要删除么?")) {


        $("#delid").val(oid);
        document.forms[0].submit();


    }


}

//置顶/取消置顶
function myToggleTop(oid) {

    $("#topid").val(oid);
    document.forms[0].submit();

}



