/**
 *
 */

$(function () {

    $(".sendbtn")
        .click(
            function () {
                //验证文章标题非空
                var title = $("#articleTitle").val();

                if (title == null || title.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写文章标题");
                    return;
                }

                //验证文章内容非空
                var content = $(".CodeMirror-line").text();
                if (content == null || content.trim().length <= 1) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写文章正文");
                    return;
                }
                //验证文章标签非空
                var tag = $("#articleTag").val();

                if (tag == null || tag.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 至少编写一个标签");
                    return;
                }
                //验证文章摘要非空
                var aabstract = $("#articleAbstract").val();

                if (aabstract == null || aabstract.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写文章摘要");
                    return;
                }

                $("#sub").val("1");
                $("#myform")[0].submit();

            })

    $(".tagbtn")
        .click(
            function () {
                //验证标签名称非空
                var tag = $("#tagTitle").val();

                if (tag == null || tag.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写标签名");
                    return;
                }
                $("#myform")[0].submit();

            })

    $(".linkbtn")
        .click(
            function () {
                //验证链接名称非空
                var title = $("#linkTitle").val();

                if (title == null || title.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写链接名");
                    return;
                }
                //验证链接描述非空
                var desc = $("#linkDescription").val();

                if (desc == null || desc.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写链接描述");
                    return;
                }
                //验证链接地址非空
                var addr = $("#linkAddress").val();

                if (addr == null || addr.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写链接路径");
                    return;
                }

                $("#myform")[0].submit();

            })

    $(".optionbtn")
        .click(
            function () {
                //验证博客名称非空
                var bname = $("#blogName").val();

                if (bname == null || bname.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写博客名称");
                    return;
                }
                //验证博客欢迎词非空
                var bcome = $("#blogWelcome").val();

                if (bcome == null || bcome.trim().length == 0) {

                    $("#msgsp")
                        .html(
                            " <span class='fa fa-times-rectangle' style='color:red'></span> 请填写博客欢迎词");
                    return;
                }
                $("#myform")[0].submit();

            })

})