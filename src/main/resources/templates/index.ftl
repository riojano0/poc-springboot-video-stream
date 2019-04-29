<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h2>Videos</h2>
    <ul>
        <#list videos as video>
            <li>
                <a href="/${video}">${video}</a>
            </li>
        </#list>
    </ul>
</body>
</html>