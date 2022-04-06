$(function(){
    //主题详情多文件导入测试
    function CreateFileName(upLoadFile,obj){
        var L=obj.files.length;
        for(let i=0;i<L;i++){
            upLoadFile.append("files",obj.files[i]);   // 这里的 "File" 需要和服务端的接收参数一致。不然会报错。
        }
        return upLoadFile;
    }
    $("#uploadGdThemeDetail").on("click",function () {
        var FileLIst= CreateFileName(new FormData(),$("#uploadFiles")[0]);
        //url参数处理
        var url ="/api/gdplan/uploadGdThemeDetail";
        console.log($("#id:first").val()+":"+($("#id:first").val()!=null))
        if($("#id:first").val()!=null){
            url=url+"?id="+$("#id:first").val();
        };
        console.log("url:"+url);
        // 发送ajax请求。
        $.ajax({
            url:url,
            type:"POST",
            data: FileLIst,
            headers: {
                "token": $("#token:first").val()
            },
            contentType:false,
            processData:false,
            success: function (res) {
                console.log(res);
            },
            error:function (xhr) {
                console.log(xhr);
            }
        });
    });

    //主题详情下载
    $("#downloadGdThemeDetail").on("click",function() {
        $.ajax({
            url : "/api/gdplan/downloadGdThemeDetail",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: {
                "id": $("#id2:first").val()
            },
            headers: {
                "token": $("#token2:first").val()
            },
            success: function (res) {
                if(response.zip) {
                    location.href = response.zip;
                }
                console.log(res);
            },
            error:function (xhr) {
                console.log(xhr);
            }
        })
    });


})
