<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<c:url var="inquiryUrl" value="/inquiry" /> 
<c:url var="historyUrl" value="/inquiry?action=history" /> 
<!DOCTYPE html> 
<html lang="ja"> 
<head> 
<meta charset="UTF-8"> 
<title>お問い合わせフォーム</title> 
<link rel="stylesheet" href="style.css"> 
</head> 
<body> 
<div class="container"> 
    <h1>お問い合わせフォーム</h1> 
    <c:if test="${sessionScope.pal == null}">
    <a href="/inquiry-system/touroku">登録</a>
    <a href="/inquiry-system/nyusitu">入室</a>
    </c:if>
    <c:if test="${sessionScope.pal == 'join'}">
    <a href="/inquiry-system/settei">設定</a>
    <a href="/inquiry-system/taisitu">退室</a>
    </c:if>
    <c:if test="${sessionScope.op != 'ope0' && sessionScope.op != null}">
    <a href="/inquiry-system/kanri">管理</a>
    </c:if>
    <form action="${inquiryUrl}" method="post" onsubmit="return validateForm()" enctype="multipart/form-data"> 
        <p> 
            <label for="name">名前:</label> 
            <input type="text" id="name" name="name" value="<c:out value='${inquiry.name}'/>">  
            <span class="error-message"><c:out value='${errors.name}' /></span> 
        </p> 
        <p> 
            <label for="email">メールアドレス:</label> 
            <input type="email" id="email" name="email" value="<c:out value='${inquiry.email}'/>"> 
            <span class="error-message"><c:out value='${errors.email}' /></span> 
        </p> 
        <p> 
            <label for="content">内容:</label> 
            <textarea id="content" name="content" rows="5" cols="40"><c:out value='${inquiry.content}' 
/></textarea> 
            <span class="error-message"><c:out value='${errors.content}' /></span> 
        </p> 
        <p> 
            <label for="attachment">添付ファイル:</label> 
            <input type="file" id="attachment" name="attachment"> 
            <span class="error-message"><c:out value='${errorMessage}' /></span> 
        </p> 
        <label for="status-${status.count}">宛先:</label> 
        <select id="status-${status.count}" name="newaaa"> 
                                <option value="ヘルプデスク" selected>ヘルプデスク</option> 
                                <option value="先生へ" >先生へ</option> 
                                <option value="店舗へ" >店舗へ</option> 
                                <option value="自治体へ" >自治体へ</option> 
                                <option value="宿題" >宿題</option>
                                <option value="バグ" >バグ</option>
                                <option value="資料請求" >資料請求</option>
                            </select> 
        <p> 
            <label for="captcha">スパム対策: <c:out value='${captchaQuestion}' /></label> 
            <input type="text" id="captcha" name="captcha" required> 
            <span class="error-message"><c:out value='${errors.captcha}' /></span> 
        </p> 
        <div class="button-group"> 
            <input type="submit" value="確認"> 
        </div> 
    </form> 
<!-- ↓ 404 対策：相対パスではなく絶対パスを明示 --> 
    <div class="button-group"> 
        <a href="${historyUrl}" class="button secondary">お問い合わせ履歴を見る</a> 
    </div> 
</div> 
 
    <script> 
function validateForm() { 
    let isValid = true; 
    const name = document.getElementById('name'); 
    const email = document.getElementById('email'); 
    const content = document.getElementById('content'); 
    const captcha = document.getElementById('captcha'); 
 
    document.querySelectorAll('.error-message').forEach(el => el.textContent = ''); 
 
    if (name.value.trim() === '') { 
        name.nextElementSibling.textContent = '名前は必須です。'; 
        isValid = false; 
    } 
 
    if (email.value.trim() === '') { 
        email.nextElementSibling.textContent = 'メールアドレスは必須です。'; 
        isValid = false; 
    } else if (!/^[\w.%+-]+@[\w.-]+\.[A-Za-z]{2,}$/.test(email.value)) { 
        email.nextElementSibling.textContent = '有効なメールアドレスを入力してください。'; 
        isValid = false; 
    } 
 
    if (content.value.trim() === '') { 
        content.nextElementSibling.textContent = '内容は必須です。'; 
        isValid = false; 
    } 
 
    if (captcha.value.trim() === '') { 
        captcha.nextElementSibling.textContent = 'スパム対策の質問に答えてください。'; 
        isValid = false; 
    } 
 
    return isValid; 
} 
</script> 
 
</body> 
</html> 
    