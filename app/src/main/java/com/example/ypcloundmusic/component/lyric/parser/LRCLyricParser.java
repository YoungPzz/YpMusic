package com.example.ypcloundmusic.component.lyric.parser;

import com.example.ypcloundmusic.component.lyric.model.Line;
import com.example.ypcloundmusic.component.lyric.model.Lyric;
import com.example.ypcloundmusic.util.SuperDateUtil;

import java.util.ArrayList;

/**
 * LRC歌词解析器
 */
public class LRCLyricParser {

    /**
     * 解析歌词
     * @param data
     * @return
     */
    public static Lyric parse(String data) {
        //创建歌词解析后的对象
        Lyric result = new Lyric();

        //初始化一个列表
        ArrayList<Line> datum = new ArrayList<>();

        //使用\n拆分字符串
        String[] strings = data.split("\n");

        //循环每一行歌词
        for(String lineString :strings){
            Line line = parseLine(lineString);
            if(line != null) {
                datum.add(line);
            }
        }

        result.setDatum(datum);

        //返回解析后的歌词
        return result;
    }


    /**
     * 解析一行歌词
     * 例如：[00:00.300]爱的代价 - 李宗盛
     *
     * @param data
     * @return
     */
    private static Line parseLine(String data) {
        //过滤元数据
        if (data.startsWith("[0")) {
            //创建歌词行
            Line line = new Line();

            //去除前面的[
            data = data.substring(1); //00:00.300]爱的代价 - 李宗盛

            //使用]拆分
            String[] commands = data.split("]", -1); //00:00.300,爱的代价 - 李宗盛

            //开始时间
            line.setStartTime(SuperDateUtil.parseToInt(commands[0]));

            //歌词
            line.setData(commands[1]);

            //返回解析后的歌词行
            return line;
        }

        return null;
    }
}
