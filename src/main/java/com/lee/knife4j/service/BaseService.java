package com.lee.knife4j.service;


import com.lee.knife4j.Repository.BaseRepository;
import com.lee.knife4j.model.Model;
import com.lee.knife4j.util.FlushUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public abstract class BaseService<T extends Model> {

    private volatile boolean scheduled = false;

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    protected abstract BaseRepository<T> getRepository();

    protected String getModelName() {
        return this.getClass().getSimpleName().replace("Service", "").replace("My", "").toLowerCase();
    }

    public T findById(Long id) {
        if (id == null) {
            return null;
        }
        T t = getRepository().findById(id).orElse(null);
        if (t == null || t.getDel()) {
            return null;
        }
        init(t);
        return t;
    }

    @Transactional
    public T save(T t) throws InterruptedException {
        getRepository().save(t);
        if (t.getDel()) {
            return null;
        }
        init(t);
        return t;
    }

    public Page<Long> find(Integer page, Integer pageSize, String search) {
        return getRepository().find(search, PageRequest.of(page, pageSize));
    }

    public T init(T t) {
        return t;
    }

    public List<T> findAll() {
        List<T> ts = FlushUtil.findAll(getRepository());
        ts.forEach(this::init);
        return ts;
    }

    @Transactional
    public void deleteById(Long id) throws InterruptedException {
        T t = this.findById(id);
        if (t != null) {
            getRepository().deleteById(t.getId());
        }

    }
    @Transactional
    public void delById(Long id) throws InterruptedException {
        T t = this.findById(id);
        if (t != null) {
            t.setDel(true);
            save(t);
        }
    }

    public  <T> T updateEntity(T entity,T oldEntity){
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){  // 遍历属性
            try {
                PropertyDescriptor pd = new PropertyDescriptor((String)field.getName(), clazz);
                Method readMethod = pd.getReadMethod(); //获取属性的get方法
                Method writeMethod= pd.getWriteMethod(); //获取属性的set方法
                if (readMethod.invoke(entity)!=null){   // 如果这个属性不为null
                    // old对象set(entity.get())  old对象将属性值设置为entity的属性值
                    writeMethod.invoke(oldEntity,readMethod.invoke(entity));
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        return oldEntity;
    }

    public List<T> saveAll(Iterable<T> entities){
        return  getRepository().saveAll(entities);
    }

}