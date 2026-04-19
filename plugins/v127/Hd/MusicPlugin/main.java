
import java.nio.file.Files;

import me.hd.wauxv.plugin.api.callback.PluginCallBack;

void handleSendMusic(String talker, String title) {
    var apiUrl = "https://api.vkeys.cn/v2/music/netease?word=" + title + "&choose=1";
    get(apiUrl, null, (respCode, respContent) -> {
        var jsonObject = new JSONObject(respContent);
        var data = jsonObject.optJSONObject("data");

        var id = data.optLong("id");
        var songName = data.optString("song");
        var songDesc = data.optString("singer");
        var thumbUrl = data.optString("cover");
        var musicLink = data.optString("link");
        var musicUrl = data.optString("url");

        var cachePath = cacheDir + "/thumbImg" + id + ".png";
        download(thumbUrl, cachePath, null, cacheFile -> {
            var thumbData = Files.readAllBytes(cacheFile.toPath());
            shareMusic(talker, songName, songDesc, musicLink, musicUrl, thumbData, "wx8dd6ecd81906fd84");
            cacheFile.delete();
        });
    });
}

void onHandleMsg(Object msgInfoBean) {
    if (msgInfoBean.isText()) {
        var content = msgInfoBean.getContent();
        var talker = msgInfoBean.getTalker();
        if (content.startsWith("/点歌 ")) {
            var title = content.substring(4);
            handleSendMusic(talker, title);
        }
    }
}
