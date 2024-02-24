package org.ktn.kindletonotion.kindle.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.ktn.kindletonotion.kindle.model.Book;
import org.ktn.kindletonotion.kindle.model.Mark;
import org.ktn.kindletonotion.kindle.model.NoMarkNotes;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
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

        // 记录笔记位置Map，用于保存每一个位置的笔记所在的books中的位置，当读取到一条笔记时，把他保存在到这本书的这条标注中。
        Map<String, Mark> noteToMark = new HashMap<>(16);

        // 未进行笔记登记的笔记
        List<NoMarkNotes> noMarkNotesList = new LinkedList<>();

        // 获取全部笔记比进行遍历
        for (String note : getAllNotes(filePath)) {
            // 对笔记进行分割，四个部分：
            String[] mark = note.split("\n");
            if (mark.length == 3) {
                // 获取笔记信息
                String[] markInfo = mark[1].split("\\|");

                // 获取笔记位置
                String markAddress = markInfo[0].substring(2).trim();

                // 去除掉书名中一些特殊的字符，用来拆分出简短的书名
                String[] bookInfo = Pattern.compile("[()<>|\\[\\]（）《》【】｜]\\s*").split(mark[0]);
                // 获取书名
                String name = (!bookInfo[0].isEmpty() ? bookInfo[0] : mark[0]).trim();

                // 获取笔记内容
                String markContent = mark[2];

                // 截取出具体位置拼接书名：使用正则表达式匹配#到“的”之前的文字；
                Matcher matcher = Pattern.compile("#(.*?)(?=的)").matcher(markAddress);
                // 具体位置
                String position = null;
                if (matcher.find()) {
                    position = name +  matcher.group().split("-")[0].replaceAll("\\D", "");
                }

                if (markAddress.endsWith("标注")) {
                    // 处理标注
                    ProcessMarkup(bookInfo, markInfo, markAddress, markContent, books, name, position, noteToMark);
                } else if (position != null && !position.isEmpty()) {
                    // 处理笔记
                    ProcessingNotes(noteToMark, position, markContent, noMarkNotesList);
                }


            } else {
                log.info("笔记格式不正确。");
            }
        }

        // 处理未找到标注的笔记
        ProcessingNotes(noMarkNotesList, noteToMark);

        return books;

    }

    /**
     * 处理笔记
     * @param noteToMark 标注与位置关系Map
     * @param position 笔记位置
     * @param markContent 笔记内容
     * @param noMarkNotesList 未进行笔记登记的笔记
     */
    private static void ProcessingNotes(Map<String, Mark> noteToMark, String position, String markContent, List<NoMarkNotes> noMarkNotesList) {
        if (noteToMark.containsKey(position)) {
            // 把笔记保存到对应的标注
            savingNotesToMarkup(noteToMark, position, markContent);
        } else {
            // 未找到标注的笔记
            noMarkNotesList.add(new NoMarkNotes(position, markContent));
        }
    }

    /**
     * 处理标注
     * @param bookInfo 书信息
     * @param markInfo 标注信息
     * @param markAddress 标注位置
     * @param markContent 标注内容
     * @param books 书集合
     * @param name 书名
     * @param position 笔记位置
     * @param noteToMark 标注与位置关系Map
     */
    private static void ProcessMarkup(String[] bookInfo, String[] markInfo, String markAddress, String markContent, Map<String, Book> books, String name, String position, Map<String, Mark> noteToMark) {
        // 获取该书的作者
        String author;
        if (bookInfo.length > 1) {
            String a = bookInfo[bookInfo.length - 1];
            author = "Unknown".equals(a) ? "" : a;
        } else {
            author = "";
        }

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

        Mark markNode = new Mark(markTime, dateString, markAddress, markContent, null, false);
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
            books.put(name + "_" + author, book);
        }
        if (position != null && !position.isEmpty() && !noteToMark.containsKey(position)) {
            noteToMark.put(position, markNode);
        }
    }

    /**
     * 把笔记保存到对应的标注
     * @param noteToMark 标注与位置关系Map
     * @param position 笔记位置
     * @param markContent 笔记内容
     */
    private static void savingNotesToMarkup(Map<String, Mark> noteToMark, String position, String markContent) {
        Mark noteMark = noteToMark.get(position);
        noteMark.setNote(markContent);
        noteMark.setHaveNote(true);
    }

    /**
     * 处理未找到标注的笔记
     * @param noMarkNotesList 未找到标注的笔记
     * @param noteToMark 标注与位置关系Map
     */
    private static void ProcessingNotes(List<NoMarkNotes> noMarkNotesList, Map<String, Mark> noteToMark) {
        noMarkNotesList.forEach(note -> {
            String position = note.getPosition();
            if (noteToMark.containsKey(position)) {
                // 把笔记保存到对应的标注
                savingNotesToMarkup(noteToMark, position, note.getContent());
            }
        });
    }

    /**
     * 读取文件的全部内容，替换掉所有的空行然后使用分隔符进行分割返回笔记数组
     * @param filePath 文件路径
     * @return 笔记数组
     */
    private String[] getAllNotes(String filePath) {

        // 以UTF-8编码读取文件并删除bom
        try (FileInputStream fis = new FileInputStream(filePath);
             BOMInputStream bomInputStream = BOMInputStream.builder().setInputStream(fis).get();
             InputStreamReader isr = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

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
