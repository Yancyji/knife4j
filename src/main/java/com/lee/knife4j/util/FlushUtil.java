package com.lee.knife4j.util;

import com.lee.knife4j.Repository.BaseRepository;
import com.lee.knife4j.model.Model;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlushUtil {


    public static List<Integer> getPages(BaseRepository<? extends Model> repository) {

        int pageCount = RootUtil.getPageCount(repository.count(), 10);
        List<Integer> pages = Stream.iterate(0, i -> ++i).limit(pageCount).collect(Collectors.toList());

        return pages;
    }

    public static <T extends Model> List<T> findAll(BaseRepository<T> repository) {

        List<T> all = new ArrayList<>();
        FlushUtil.getPages(repository).forEach(page -> {
            List<T> content = repository.findAll(PageRequest.of(page, 300)).getContent();
            all.addAll(content);
        });
        return all;
    }
}