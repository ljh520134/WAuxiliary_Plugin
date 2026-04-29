
void onHandleMsg(Object msg) {
    if (!msg.isText()) return;

    String content = msg.getContent();
    String chatID = msg.getTalker();

    if (content != null && content.startsWith("/绘图 ")) {
        String promptText = content.substring(4).trim();
        if (promptText.length() > 0) {
            toast("正在绘制并发送，请稍候...");
            callImageApi(chatID, promptText);
        }
    }
}

void callImageApi(target, prompt) {
    String apiUrl = "https://www.zeoapi.com/v1/chat/completions";
    String authKey = "Bearer 你的API_KEY"; // ⚠️ 请在此处填入你的 Key不要删除Bearer和后面的空格

    Map headers = new HashMap();
    headers.put("Authorization", authKey);
    headers.put("Content-Type", "application/json");

    Map body = new HashMap();
    body.put("model", "gpt-image-2");
    List msgList = new ArrayList();
    Map msgItem = new HashMap();
    msgItem.put("role", "user");
    msgItem.put("content", prompt);
    msgList.add(msgItem);
    body.put("messages", msgList);

    post(apiUrl, body, headers, 60000, new Consumer() {
        void accept(Object response) {
            if (response == null) return;

            try {
                String respStr = response.toString();
                // 1. 提取 Markdown 中的 URL (即 https://... 到 .png 之间的内容)
                int start = respStr.indexOf("https://");
                int end = respStr.indexOf(".png");

                if (start != -1 && end != -1) {
                    String imgUrl = respStr.substring(start, end + 4);
                    log("提取到图片地址: " + imgUrl);

                    // 2. 准备本地保存路径
                    String localPath = cacheDir + "/" + System.currentTimeMillis() + ".png";

                    // 3. 下载并发送
                    download(imgUrl, localPath, new HashMap(), new Consumer() {
                        void accept(Object file) {
                            if (file != null) {
                                sendImage(target, file.getAbsolutePath());
                                toast("图片发送成功");
                            }
                        }
                    });
                } else {
                    log("未能从返回内容中提取到 URL");
                }
            } catch (Exception e) {
                log("解析发送失败: " + e.getMessage());
            }
        }
    });
}
