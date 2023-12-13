package com.app.repository;

import com.app.entity.Blog;
import com.app.modal.BlogModal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {


    Page<Blog> findAll(Specification<Blog> spec, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Blog b WHERE b.createdBy = :createdBy")
    Integer countByCreatedBy(@Param("createdBy") String createdBy);

    @Query("SELECT b FROM Blog b ORDER BY b.createdAt DESC")
    Page<Blog> findLatestBlogs(Specification<Blog> spec, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.id NOT IN (SELECT v.blogId FROM View v)")
    Page<Blog> findAllNotSeen(Specification<Blog> spec, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.id = :id")
    Page<Blog> BlogID(@Param("id") Integer id, Pageable pageable);

    @Query("SELECT NEW com.app.modal.BlogModal(" +
            "b.id, b.createdAt, b.createdBy, b.isActivated, b.modifiedAt, " +
            "b.modifiedBy, b.cloudinaryId, b.description, b.image, " +
            "a.avatar, a.name, a.isVerify, " +
            "COUNT(br.reactionLike), COUNT(br.comment), COUNT(br.share)) " +
            "FROM " +
            "Blog b " +
            "LEFT JOIN BlogReaction br ON b.id = br.blog.id " +
            "JOIN Account a ON b.createdBy = a.email " +
            "GROUP BY " +
            "b.id, b.createdAt, b.createdBy, b.isActivated, b.modifiedAt, " +
            "b.modifiedBy, b.cloudinaryId, b.description, b.image, " +
            "a.avatar, a.name, a.isVerify")
    Page<BlogModal> getAllBlogWithAccount(Specification<BlogModal> spec, Pageable pageable);

}

