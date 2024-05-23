package com.example.ypcloundmusic.component.lyric.parser;

import com.example.ypcloundmusic.component.lyric.model.Lyric;
import com.example.ypcloundmusic.util.Constant;

/**
 * 歌词解析器
 */
public class LyricParser {
    /**
     * 解析歌词
     *
     * @param type    歌词类型
     * @param content 歌词内容
     * @return 解析后的歌词对象
     */
    public static Lyric parse(int type, String content) {
        switch (type){
            case Constant.KSC:
                return KSCLyricParser.parse(content);
            default:
                //默认为LRC歌词
                return LRCLyricParser.parse(content);
        }

    }
}