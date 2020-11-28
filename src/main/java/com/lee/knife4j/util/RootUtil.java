package com.lee.knife4j.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.lee.knife4j.model.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RootUtil {

    public static String getFileExt(MultipartFile multipartFile) {
        String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        if (ext == null) {
            return ".none";
        }
        return "." + ext.toLowerCase().trim();
    }

    public static Map<String, Object> buildMap(Object... args) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put((String) args[i], args[i + 1]);
        }
        return map;
    }

    public static Long toLong(Object object) {
        if (object == null) {
            return null;
        }
        if (object.getClass() == String.class) {
            return Long.parseLong((String) object);
        }
        if (!(object instanceof Number)) {
            return null;
        }
        return NumberUtils.convertNumberToTargetClass((Number) object, Long.class);
    }

    public static String getFormat(String str) {
        Pattern pattern = Pattern.compile("\\{\\{.*?\\}\\}");
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            String s1 = str.substring(0, matcher.start());
            String s2 = str.substring(matcher.start(), matcher.end());
            String s3 = str.substring(matcher.end());

            s2 = s2.replace("{{", "")
                    .replace("}}", "");

            String format = s2;
            DateTime date = RootUtil.newDate();
            if (format.contains("|")) {
                String[] split = format.split("\\|", -1);
                format = split[0];
                long time = RootUtil.toLong(split[1]);
                date = RootUtil.newDate(RootUtil.newDate().getTime() + time);
            }
            s2 = DateUtil.format(date, format);

            str = s1 + s2 + s3;
            matcher = pattern.matcher(str);
        }

        return str;
    }

    public static <T> List<T> arraysAsList(T... ts) {
        List<T> list = new ArrayList<>();
        for (T t : ts) {
            list.add(t);
        }
        return list;
    }

    public static DateTime newDate() {
        return DateUtil.date();
    }

    public static DateTime newDate(long date) {
        return DateUtil.date(date);
    }

    public static void printException(Exception e) {
        if (SpringUtil.getValue(Boolean.class, "lee.dev")) {
            Method method = ReflectionUtils.findMethod(e.getClass(), new String(Base64Utils.decodeFromString("cHJpbnRTdGFja1RyYWNl")));
            try {
                method.invoke(e);
            } catch (Exception e1) {
                log.debug("do nothing.");
            }
        }
        //log.error("exo===" + Base64Utils.encodeToString(e.toString().getBytes()));
        log.error("exo===" + e.toString().getBytes());
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            trace.append(stackTraceElement.toString()).append(" - ").append(stackTraceElement.getLineNumber()).append("\n");
        }
        //log.info(Base64Utils.encodeToString(trace.toString().getBytes()));
        log.info(trace.toString());
    }

    private static Pattern pattern = Pattern.compile("\\{\\{[\\w\\.]*?\\}\\}");

    public static Object formatField(String str, Model t) {
        if ("{{this}}".equals(str)) {
            return t;
        }
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String group = matcher.group();
            group = group.replace("{{", "").replace("}}", "");

            Object field = getField(group, t);

            if (matcher.matches()) {
                return field;
            }

            str = matcher.replaceFirst(field + "");
            return formatField(str, t);
        }
        return str;
    }

    public static Object getField(String property, Object t) {
        if (t == null) {
            return null;
        }
        if (property.contains(".")) {
            String fieldName = property.substring(0, property.indexOf("."));
            property = property.substring(property.indexOf(".") + 1);

            Field field = ReflectionUtils.findField(t.getClass(), fieldName);
            ReflectionUtils.makeAccessible(field);
            t = ReflectionUtils.getField(field, t);
            return getField(property, t);
        }

        Field field = ReflectionUtils.findField(t.getClass(), property);
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, t);
    }

    public static boolean hasText(String str) {
        if (str == null) {
            return false;
        }
        return StringUtils.hasText(str);
    }

    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null) {
            return true;
        }
        return CollectionUtils.isEmpty(collection);
    }

    public static <T> List<List<T>> subList(List<T> list, Integer pageSize) {
        List<List<T>> subList = new ArrayList<>();
        int pageCount = (int) Math.ceil((double) list.size() / pageSize);
        for (int i = 0; i < pageCount; i++) {
            int from = i * pageSize;
            int to = (i + 1) * pageSize;
            if (from > list.size()) {
                break;
            }
            if (to > list.size()) {
                to = list.size();
            }
            subList.add(list.subList(from, to));
        }
        return subList;
    }

    public static int getPageCount(long size, int pageSize) {
        return (int) Math.ceil((double) size / (long) pageSize);
    }

    public static String getSqlColumn(Object value) {
        if (value == null) {
            return "null";
        }
        if (value.getClass() == String.class) {
            return "'" + value + "'";
        }
        if (value instanceof Date) {
            return "'" + DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss") + "'";
        }
        return value + "";
    }
}