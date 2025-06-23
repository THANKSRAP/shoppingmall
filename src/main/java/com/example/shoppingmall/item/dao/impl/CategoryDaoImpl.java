package com.example.shoppingmall.item.dao.impl;

import com.example.shoppingmall.item.dao.CategoryDao;
import com.example.shoppingmall.item.domain.Category;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private final SqlSession sqlSession;
    private static final String NAMESPACE = "CategoryMapper.";

    private CategoryDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<Category> findAll() {
        return sqlSession.selectList(NAMESPACE + "findAll");
    }

    @Override
    public Category findById(Long id) {
        return sqlSession.selectOne(NAMESPACE + "findById", id);
    }

    @Override
    public List<Category> findByDepth(int depth) {
        return sqlSession.selectList(NAMESPACE + "findByDepth", depth);
    }

    @Override
    public List<Category> findByParentCategoryId(Long parentCategoryId) {
        return sqlSession.selectList(NAMESPACE + "findByParentCategoryId", parentCategoryId);
    }

    @Override
    public Category findByItemId(Long itemId) {
        return sqlSession.selectOne(NAMESPACE + "findByItemId", itemId);
    }

    @Override
    public Category findParentCategoryById(Long categoryId) {
        return sqlSession.selectOne(NAMESPACE + "findParentCategoryById", categoryId);
    }

    @Override
    public List<Category> findByDepthAndParentCategoryId(Integer depth, Long parentCategoryId) {
        var params = new java.util.HashMap<String, Object>();
        params.put("depth", depth);
        params.put("parentCategoryId", parentCategoryId);
        return sqlSession.selectList(NAMESPACE + "findByDepthAndParentCategoryId", params);
    }
}
