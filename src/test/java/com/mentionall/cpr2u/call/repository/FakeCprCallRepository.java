package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.CPRCall;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FakeCprCallRepository implements CprCallRepository {
    Map<Long, Object> map = new HashMap();

    @Override
    public List<CPRCall> findAll() {
        return null;
    }

    @Override
    public List<CPRCall> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CPRCall> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<CPRCall> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(CPRCall entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends CPRCall> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CPRCall> S save(S entity) {
        map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends CPRCall> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CPRCall> findById(Long aLong) {
        return Optional.of((CPRCall) map.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends CPRCall> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends CPRCall> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<CPRCall> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CPRCall getOne(Long aLong) {
        return null;
    }

    @Override
    public CPRCall getById(Long aLong) {
        return null;
    }

    @Override
    public CPRCall getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends CPRCall> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CPRCall> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CPRCall> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CPRCall> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CPRCall> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CPRCall> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CPRCall, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
