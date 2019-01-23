//自定义日期格式
function format(time, format){
    var t = new Date(time);
    var tf = function(i){return (i < 10 ? '0' : '') + i};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
        switch(a){
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
}
//当登陆过期，返回403时ajax执行
/*$(function() { 已移至jquery.min.js
    $.ajaxSetup({
        error: function(xhr,status,error) {
            if (xhr.status==403) {
                layer.alert('登录已过期', {icon: 0},function(index) {
                    layer.close(index);
                    window.location.href="/manage/login";
                });
            }
        }
    });
});*/
