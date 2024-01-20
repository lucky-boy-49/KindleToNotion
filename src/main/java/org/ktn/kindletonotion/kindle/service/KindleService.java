package org.ktn.kindletonotion.kindle.service;

import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.kindle.model.Mark;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author 贺佳
 * Kindle服务
 */
@Slf4j
@Service
public class KindleService {

    public Map<String, Book> parseNotes(String filePath) {

        Map<String, Book> books = new HashMap<>(16);

        // 获取全部笔记比进行遍历
        for (String note : getAllNotes(filePath)) {
            // 对笔记进行分割，四个部分：
            String[] mark = note.split("\n");
            if (mark.length == 3) {
                // 去除掉书名中一些特殊的字符，用来拆分出简短的书名
                String[] bookInfo = Pattern.compile("[()<>|\\[\\]（）《》【】｜]\\s*").split(mark[0]);
                // 获取书名
                String name = !bookInfo[0].isEmpty() ? bookInfo[0] : mark[0];
                // 获取该书的作者
                String author;
                if (bookInfo.length > 1) {
                    String a = bookInfo[bookInfo.length - 1];
                    author = "Unknown".equals(a) ? "" : a;
                } else {
                    author = "";
                }

                // 获取笔记信息
                String[] markInfo = mark[1].split("\\|");

                // 获取笔记位置
                String markAddress = markInfo[0].substring(2).trim();

                // 获取笔记时间
                String dateString = markInfo[1].trim().substring(4);
                // 时间模式
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年MM月dd日E ahh:mm:ss");
                LocalDateTime markTime = null;
                try {
                    Date parse = inputFormat.parse(dateString);
                    markTime = parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                } catch (ParseException e) {
                    log.error("字符串转化日期错误", e);
                }

                // 获取笔记内容
                String markContent = mark[2];

                Mark markNode = new Mark(markTime, dateString, markAddress, markContent);
                if (books.containsKey(name)) {
                    // 如果map中存在则直接加入一条笔记
                    Book book = books.get(name);
                    // 添加一条笔记
                    book.getMarks().add(markNode);
                    // 笔记数自增
                    book.markNumsSelfIncreasing();
                } else {
                    // 如果map中不存在这本书则添加书再添加笔记；
                    List<Mark> markList = new LinkedList<>();
                    markList.add(markNode);
                    Book book = new Book(name, author, 1, markList);
                    books.put(name, book);
                }


            } else {
                log.info("笔记格式不正确。");
            }
        }


        return books;

    }

    /**
     * 读取文件的全部内容，替换掉所有的空行然后使用分隔符进行分割返回笔记数组
     * @param filePath 文件路径
     * @return 笔记数组
     */
    private String[] getAllNotes(String filePath) {
        File notes = new File(filePath);

        // 以UTF-8编码读取文件
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(notes), StandardCharsets.UTF_8))) {

            // 分隔符，kindle中每个笔记以==========进行分割
            String delimiter = "==========\n";

            // 内容
            StringBuilder content = new StringBuilder();
            String line;
            // 读取全部笔记
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                content.append(line).append("\n");
            }

            // 对读入的内容去除空行，即将 '\n\n' 替换为 '\n',分隔符进行分隔
            return content.toString()
                    .split(delimiter);

        } catch (IOException e) {
            log.error("读取文件失败：", e);
            throw new RuntimeException(e);
        }

    }

}
