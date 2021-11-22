# uniqueMuseum

Ajax로 이미지 업로드시 403 에러 해결 방안
<meta name="_csrf" th:content="${_csrf.token}">
<meta name="_csrf_header" th:content="${_csrf.headerName}">

beforeSend: function (jqXHR, settings) {
var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");
jqXHR.setRequestHeader(header, token);
}
http.csrf().disable(); // CSRF 토큰을 발행하지 않도록 설정하면 주석 or 삭제 처리 해야 
