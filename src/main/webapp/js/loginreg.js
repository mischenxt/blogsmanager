/**
 *
 */


$(function () {


    $(".sublogin").click(function () {


        //登录用户名非空，长度验证
        var name = $("#username").val();

        if (name == null || name.trim().length < 4) {

            $("#msgsp").html(" <span class='fa fa-times-rectangle' style='color:red'></span> 用户名不能为空，并且长度需4位以上");
            return;
        }
        //密码用户名非空，长度验证
        var pwd = $("#pwd").val();

        if (pwd == null || pwd.trim().length < 3) {

            $("#msgsp").html(" <span class='fa fa-times-rectangle' style='color:red'></span> 密码不能为空，并且长度需3位以上");
            return;
        }


        $("#myform")[0].submit();


    })

    $(".subreg").click(function () {


        //注册用户名非空，长度验证
        var name = $("#username").val();


        if (name == null || name.trim().length < 4) {

            $("#msgsp").html(" <span class='fa fa-times-rectangle' style='color:red'></span> 用户名不能为空，并且长度需4位以上");
            return;
        }
        //注册电子邮箱非空，格式验证
        var email = $("#useremail").val();

        var zheng = /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;

        if (email == null || !zheng.test(email)) {

            $("#msgsp").html(" <span class='fa fa-times-rectangle' style='color:red'></span> 电子邮箱不能为空，并且必须可用");
            return;
        }


        //注册密码非空，长度验证
        var pwd = $("#pwd").val();

        if (pwd == null || pwd.trim().length < 3) {

            $("#msgsp").html(" <span class='fa fa-times-rectangle' style='color:red'></span> 密码不能为空，并且长度需3位以上");
            return;
        }
        //注册密码一致验证
        var repwd = $("#repwd").val();

        if (repwd != pwd) {

            $("#msgsp").html(" <span class='fa fa-times-rectangle' style='color:red'></span> 两次密码输入不一致");
            return;
        }


        $("#myform")[0].submit();


    })

})